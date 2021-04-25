package Modelo;

import Exceptions.*;
import database.PaleteDAO;
import database.PrateleiraDAO;
import database.RobotDAO;
import ui.Menu;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Sistema implements SistemaFacade {
    private Map<String, Palete> paletes;
    private Map<String, Prateleira> prateleiras;
    private Map<String, Robot> robots;
    private List<String> paletesWaitingForDelivering;
    private List<GPS> mapa;

    /**
     * Construtor do Sistema, responsável por inicializar a base de dados e povoar as prateleiras e os robots
     * e tudo o resto necessário ao bom funcionamento do programa
     */
    public Sistema() {
        this.prateleiras = PrateleiraDAO.getInstance();
        this.paletes = PaleteDAO.getInstance();
        this.robots = RobotDAO.getInstance();
        PrateleiraDAO.povoa();
        RobotDAO.povoa();

        List<String> paletesReservadas = new ArrayList<>();
        for (String s : this.robots.keySet()) {
            if (this.robots.get(s).getCodPalete() != null)
                paletesReservadas.add(this.robots.get(s).getCodPalete());
        }

        this.paletesWaitingForDelivering = new ArrayList<>();
        if (this.paletes.size() > 0)
            for (String s : this.paletes.keySet()) {
                if (this.paletes.get(s).getLoc().equals("0-0") &&
                        !paletesReservadas.contains(s)) { // se estiver na receçao, adiciona 0-0 - receção
                    this.paletesWaitingForDelivering.add(s);
                }
            }
        this.mapa = GPS.criaMapa();
    }

    /**
     * Implementação do método que comunica ao sistema um código QR de uma palete
     * @param produto Palete à qual vai ser atribuída um código
     * @return Código QR atribuído à palete
     */
    public String comunicaCodigoQR(String produto) {
        String cod;
        try {
            do {
                cod = LeitorQrCode.geraCodigoQR();
            } while (this.paletes.get(cod) != null);

            this.paletes.put(cod, new Palete(cod, "0-0", produto));
            this.paletesWaitingForDelivering.add(cod);

            return ("Sistema:> Código Gerado com Sucesso! : " + cod);
        } catch (Exception e) {
            e.printStackTrace();
            return "Não foi possível gerar o código :(";
        }
    }

    /**
     * Implementação do método que procura um Robot de forma a realizar o transporte de uma palete e ao mesmo tempo uma prateleira onde armazená-la
     * @return Mensagem de sucesso/erro
     * @throws NoPaletesOnWaitingListException caso não haja paletes à espera de transporte
     * @throws NoPrateleirasAvailableException caso não haja prateleiras disponíveis para armazenar
     * @throws NoRobotAvailableException caso não haja robots disponíveis para transportar
     */
    public String comunicaOrdemDeTransporte() throws NoPaletesOnWaitingListException, NoPrateleirasAvailableException, NoRobotAvailableException {
        if (this.paletesWaitingForDelivering.size() == 0)
            throw new NoPaletesOnWaitingListException("Sistema:> Não existem Paletes para Transporte!");

        List<String> menuOptions = new ArrayList<>();
        for (String s : this.paletesWaitingForDelivering) {
            menuOptions.add(s + ": " + this.paletes.get(s).getM());
        }

        Menu menu = new Menu(menuOptions);
        int i;
        menu.executa();
        i = menu.getOpcao();

        String produtoATransportar = this.paletesWaitingForDelivering.get(i - 1);
        String prateleira = getPrateleiraLivre();
        if (prateleira.isEmpty()) {
            throw new NoPrateleirasAvailableException("Sistema:> Não existem Prateleiras Disponíveis!");
        }

        Prateleira p = this.prateleiras.get(prateleira);
        p.setDisponibilidade(false);
        this.prateleiras.put(p.getCodPrateleira(), p); // atualizar prateleira (disponibilidade - false)
        return comunicaRobotMaisProximo(produtoATransportar, prateleira);

    }


    // ver qual o robot mais proximo
    // enviar-lhe o percurso de ir buscar a palete + o de entregar
    // robot comunicar que iniciou a entrega

    /**
     * Implementação do método que comunica ao sistema qual o robot mais próximo para efetuar determinada tarefa
     * @param codigoPalete palete a averiguar a distância
     * @param prateleiraDestino prateleira onde será armazenada a palete
     * @return Mensagem com o código do Robot escolhido
     * @throws NoRobotAvailableException caso não haja robots disponíveis
     */
    public String comunicaRobotMaisProximo(String codigoPalete, String prateleiraDestino) throws NoRobotAvailableException { // robot -> localizacao da palete -> destino
        int minimo = 99;
        int atual;
        String aux = this.paletes.get(codigoPalete).getLoc(); // localização da palete
        GPS gpsPalete;
        gpsPalete = this.prateleiras.get(aux).getLocalizacao().clone();
        String robotEscolhido = "";
        // Escolher o Robot mais próximo
        for (String s : this.robots.keySet()) {
            if (this.robots.get(s).isAvailable()) {
                GPS inicio = this.robots.get(s).getLocalizacao();
                if ((atual = GPS.criaCaminho(this.mapa, inicio, gpsPalete).getDistancia()) < minimo) { // encontrou novo robot mais proximo
                    robotEscolhido = s;
                    minimo = atual;
                }
            }
        }
        if (robotEscolhido.isEmpty()) {
            Prateleira p = this.prateleiras.get(prateleiraDestino);
            p.setDisponibilidade(true);
            this.prateleiras.put(prateleiraDestino, p); // voltar a atualizar prateleira
            throw new NoRobotAvailableException("Sistema:> Não existe nenhum robot disponível!");
        }
        Robot escolhido = this.robots.get(robotEscolhido);
        escolhido.setLivre(false);
        escolhido.setCodPalete(codigoPalete); // robot fica "reservado"
        this.robots.put(robotEscolhido, escolhido); // atualizar robot (disponibilidade - false)
        this.paletesWaitingForDelivering.remove(codigoPalete); // remover palete da lista à espera de ser entregue

        return ("Sistema:> Robot Escolhido: " + robotEscolhido);
    }

    /**
     * Implementação do método que averigua qual a prateleira livre mais próxima
     * @return Código da palete escolhida
     */
    public String getPrateleiraLivre() { // busca uma prateleira livre
        String pEscolhida = "";
        int minimo = 99;
        int valor;
        for (String s : this.prateleiras.keySet()) {
            if (this.prateleiras.get(s).isAvailable() && !s.equals("0-0") && !s.equals("e-e")) {
                if ((valor = GPS.criaCaminho(this.mapa, new GPS(0, 0), this.prateleiras.get(s).getLocalizacao()).getDistancia()) < minimo) {
                    minimo = valor;
                    pEscolhida = s;
                }
            }
        }
        return pEscolhida;
    }

    /**
     * Implementação do método que averigua qual a prateleira onde será armazenada uma palete
     * @return Código da prateleira onde será armazenada a palete
     */
    public String getPrateleiraParaArmazenamento() {
        for (String s : this.prateleiras.keySet()) {
            if (!this.prateleiras.get(s).isAvailable() && this.prateleiras.get(s).getCodPalete() == null && !s.equals("0-0") && !s.equals("e-e"))
                return s;
        }
        return null;
    }

    /**
     * Implementação do método que devolve o código do Robot atribuido ao transporte de uma palete
     * @param codPalete Código da palete a averiguar
     * @return Código do robot previamente designado ao transporte dessa palete
     */
    public String getRobotReservado(String codPalete) {
        String robotEscolhido = "";

        for (String s : this.robots.keySet()) {
            if (this.robots.get(s).getCodPalete() != null && this.robots.get(s).getCodPalete().equals(codPalete))
                return s;
        }
        return robotEscolhido;
    }

    /**
     * Implementação do método designado a notificar o sistema que o robot já procedeu à recolha de uma palete
     * @return Mensagem de sucesso/erro
     * @throws NoPaletesToCollectException caso não haja paletes para recolha
     * @throws NoRobotAvailableException caso não haja robots disponíveis
     */
    public String notificaRecolhaDePaletes() throws NoPaletesToCollectException, NoRobotAvailableException {
        // verificar se existe alguma palete para recolher
        // condição : tem de estar na base de dados, com localização 0 0, e não pode estar na lista
        List<String> paletesARecolher = new ArrayList<>();
        for (String s : this.paletes.keySet()) {
            if (this.paletes.get(s).getLoc().equals("0-0") && !this.paletesWaitingForDelivering.contains(s)) {
                paletesARecolher.add(s + ": " + this.paletes.get(s).getM());
            }
        }
        if (paletesARecolher.isEmpty()) {
            throw new NoPaletesToCollectException("Sistema:> Não há Paletes para Recolher!");
        }
        Menu menuDePaletes = new Menu(paletesARecolher);
        menuDePaletes.executa();
        int escolha = menuDePaletes.getOpcao();

        String aux = paletesARecolher.get(escolha - 1);
        String palete_a_recolher = aux.substring(0, aux.indexOf(":"));

        Robot r = this.robots.get(getRobotReservado(palete_a_recolher));
        if (!r.isAvailable() && (r.getCodPalete() != null && r.getCodPalete().equals(palete_a_recolher))) {

            Palete p = this.paletes.get(palete_a_recolher);
            String output = r.doRecolha(GPS.criaCaminho(this.mapa, r.getLocalizacao(), new GPS(0, 0)), p);

            p.setLoc(r.getCod()); // localização da palete passa a ser o robot
            this.paletes.put(palete_a_recolher, p); // atualizar palete (localização)
            this.robots.put(r.getCod(), r); // atualizar robot (localização + palete)

            return output;
        }
        else{
            throw new NoRobotAvailableException("Sistema:> Não há robots disponíveis!");
        }
    }

    /**
     * Implementação do método designado a notificar o sistema de que um robot já procedeu à entrega efetiva de uma palete
     * @return Mensagem de sucesso/erro
     * @throws NoPaletesToDeliverException caso não existam paletes para entregar
     * @throws NoPrateleirasAvailableException caso não existam prateleiras disponíveis
     */
    public String notificaEntregaDePaletes() throws NoPaletesToDeliverException, NoPrateleirasAvailableException {
        List<String> paletesAEntregar = new ArrayList<>();
        for (String s : this.paletes.keySet()) {
            Palete p = this.paletes.get(s);
            if (p.getLoc().startsWith("R")) {
                paletesAEntregar.add(p.getLoc() +
                        ": " + p.getCodPalete() +
                        ", " + p.getM());
            }
        }
        if (paletesAEntregar.isEmpty()) {
            throw new NoPaletesToDeliverException("Sistema:> Não há paletes para entregar");
        }
        Menu menuDePaletes = new Menu(paletesAEntregar);
        menuDePaletes.executa();
        int escolha = menuDePaletes.getOpcao();

        String aux = paletesAEntregar.get(escolha - 1);
        String codPalete = aux.substring(aux.indexOf(" ") + 1, aux.indexOf(","));

        String codprateleira = getPrateleiraParaArmazenamento();
        if(codprateleira == null){
            throw new NoPrateleirasAvailableException("Sistema:> Ocorreu um erro...");
        }
        Prateleira prateleira = this.prateleiras.get(codprateleira);
        Robot robot = this.robots.get(this.paletes.get(codPalete).getLoc());

        String output = robot.doDelivering(GPS.criaCaminho(this.mapa, robot.getLocalizacao().clone(), prateleira.getLocalizacao().clone()));
        Palete palete = this.paletes.get(codPalete);
        palete.setLoc(codprateleira);

        prateleira.setCodPalete(codPalete);
        this.paletes.put(codPalete, palete); // atualizar palete (localização)
        this.robots.put(robot.getCod(), robot); // atualizar robot (palete, localizaçao, disponibilidade)
        this.prateleiras.put(codprateleira, prateleira); // atualizar prateleira (palete)
        return output;
    }

    /**
     * Implementação do método que apresenta a lista com a informação sobre todas as paletes presentes no sistema
     * @return Lista com a informação sobre as paletes
     * @throws NoExistingPaletesException caso não existam paletes no sistema
     */
    public String consultaListagem() throws NoExistingPaletesException {
        if(this.paletes.size()==0) throw new NoExistingPaletesException("Sistema:> Não existem Paletes no Sistema");
        StringBuilder output = new StringBuilder();
        for (String s : this.paletes.keySet()) {
            Palete p = this.paletes.get(s);
            GPS coordenadas;
            if (p.getLoc().startsWith("R"))
                coordenadas = this.robots.get(p.getLoc()).getLocalizacao().clone();
            else
                coordenadas = this.prateleiras.get(p.getLoc()).getLocalizacao().clone();
            output.append("Palete { código: ")
                    .append(s)
                    .append(", material: ")
                    .append(p.getM())
                    .append(", localização: ")
                    .append(p.getLoc()).append(", GPS: ")
                    .append(coordenadas.toString())
                    .append(" }\n");
        }
        return "Sistema:>\n" + output;
    }
}
