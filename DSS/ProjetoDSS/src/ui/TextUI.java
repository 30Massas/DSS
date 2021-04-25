package ui;

import Exceptions.*;
import Modelo.Sistema;
import Modelo.SistemaFacade;

import java.util.Scanner;

public class TextUI {

    private SistemaFacade modelo;

    private Menu menu;

    private Scanner sc;

    /**
     * Implementação do método que apresenta o menu inicial do programa
     */
    public TextUI() {
        String[] opcoes = {
                "Comunicar código QR", // 1
                "Sistema: Comunicar Ordem de Transporte", // 2
                "Notificar Recolha de Paletes", // 3
                "Notificar Entrega de Paletes", // 4
                "Gestor: Consultar listagem de localizações" //5
        };
        this.menu = new Menu(opcoes);
        this.modelo = new Sistema();
        this.sc = new Scanner(System.in);
    }

    /**
     * Implementação do método que corre o programa
     */
    public void run() {
        do {
            menu.executa();
            switch (menu.getOpcao()) {
                case 1:
                    comunicarCodigoQR();
                    break;
                case 2:
                    comunicarOrdemDeTransporte();
                    break;
                case 3:
                    notificarRecolhaDePaletes();
                    break;
                case 4:
                    notificarEntregaDePaletes();
                    break;
                case 5:
                    consultarListagem();
                    break;
            }
        } while (menu.getOpcao() != 0); // A opção 0 é usada para sair do menu.
        System.out.println("Saindo ...");
    }

    /**
     * Implementação do método responsável por comunicar com o modelo para comunicarCodigoQR
     */
    public void comunicarCodigoQR() {
        System.out.print("Insira o nome do Produto: ");
        String prod = sc.nextLine();
        System.out.println(modelo.comunicaCodigoQR(prod));
    }

    /**
     * Implementação do método responsável por comunicar com o modelo para comunicarOrdemDeTransporte
     */
    public void comunicarOrdemDeTransporte() {
        try {
            System.out.println(modelo.comunicaOrdemDeTransporte());

        } catch (NoPaletesOnWaitingListException | NoPrateleirasAvailableException | NoRobotAvailableException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Implementação do método responsável por comunicar com o modelo para notificarRecolhaDePaletes
     */
    public void notificarRecolhaDePaletes() {
        try {
            System.out.println(modelo.notificaRecolhaDePaletes());
        } catch (NoPaletesToCollectException | NoRobotAvailableException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Implementação do método responsável por comunicar com o modelo para notificarEntregarDePaletes
     */
    public void notificarEntregaDePaletes() {
        try {
            System.out.println(modelo.notificaEntregaDePaletes());
        } catch (NoPaletesToDeliverException | NoPrateleirasAvailableException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Implementação do método responsável por comunicar com o modelo para consultarListagem
     */
    public void consultarListagem() {
        try {
            System.out.println(modelo.consultaListagem());
        } catch (NoExistingPaletesException e) {
            System.out.println(e.getMessage());
        }
    }

}
