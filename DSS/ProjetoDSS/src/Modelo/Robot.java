package Modelo;


import java.util.List;

public class Robot {
    private String codRobot;
    private boolean livre;
    private String codPalete;
    private GPS localizacao;

    /**
     * Construtor do Robot
     * @param codRobot Código do Robot
     * @param localizacao Localização do Robot
     * @param livre Disponibilidade do Robot
     * @param pal Palete pelo qual está encarregue
     */
    public Robot(String codRobot, GPS localizacao, boolean livre, String pal) {
        this.codRobot = codRobot;
        setLocalizacao(localizacao);
        setLivre(livre);
        this.codPalete = pal;
    }

    /**
     * Construtor por cópia do Robot
     * @param r Robot a copiar
     */
    public Robot(Robot r) {
        this.codRobot = r.getCod();
        setLocalizacao(r.getLocalizacao());
        setLivre(r.isAvailable());
        this.codPalete = r.getCodPalete();
    }

    /**
     * Implementação do método que devolve a localização de um Robot
     * @return Localização
     */
    public GPS getLocalizacao() {
        return this.localizacao.clone();
    }

    /**
     * Implementação do método que define a localização de um Robot
     * @param localizacao Localização designada
     */
    public void setLocalizacao(GPS localizacao) {
        this.localizacao = localizacao;
    }

    /**
     * Implementação do método que averigua a disponibilidade de um Robot
     * @return Valor lógico da disponibilidade (true caso esteja disponível)
     */
    public boolean isAvailable() {
        return livre;
    }

    /**
     * Implementação do método que define a disponibilidade de um Robot
     * @param livre Disponibilidade a atribuir
     */
    public void setLivre(boolean livre) {
        this.livre = livre;
    }

    /**
     * Implementação do método que devolve o código da palete pela qual o Robot está disponível
     * @return Código da palete
     */
    public String getCodPalete() {
        return this.codPalete;
    }

    /**
     * Implementação do método que define a palete pelo qual o Robot está encarregue
     * @param p Código da palete a designar ao Robot
     */
    public void setCodPalete(String p) {
        this.codPalete = p;
    }

    /**
     * Implementação do método que devolve o código do Robot
     * @return Código do Robot
     */
    public String getCod() {
        return this.codRobot;
    }

    /**
     * Implementação do método que copia um Robot e os seus atributos
     * @return Robot copiado
     */
    public Robot clone() {
        return new Robot(this);
    }

    /**
     * Implementação do método que simula a entrega de uma palete por um Robot
     * @param percurso Percurso calculado e atribuido ao Robot para efetuar a entrega
     * @return Mensagem de sucesso
     */
    public String doDelivering(Percurso percurso) {
        List<GPS> aux = percurso.getRecolha();
        for (GPS g : aux) {
            this.localizacao = g.clone();
        }
        this.codPalete = null;
        this.livre = true;
        return ("Robot " + this.codRobot + ":> Transporte Feito com Sucesso");
    }

    /**
     * Implementação do método que simula a recolha de uma palete por um Robot
     * @param percurso Percurso calculado e atribuido ao Robot para efetuar a recolha
     * @param palete Palete a recolher
     * @return Mensagem de sucesso
     */
    public String doRecolha(Percurso percurso, Palete palete) {
        List<GPS> aux = percurso.getRecolha();
        for (GPS g : aux) {
            this.localizacao = g.clone();
        }
        this.codPalete = palete.getCodPalete();
        return ("Robot " + this.codRobot + ":> Palete Recolhida com Sucesso!");
    }

}
