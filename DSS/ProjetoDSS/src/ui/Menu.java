package ui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class Menu {
    private static Scanner is = new Scanner(System.in);
    private List<String> opcoes;
    private int op;

    /**
     * Construtor do Menu
     * @param opcoes Opções possíveis
     */
    public Menu(String[] opcoes) {
        this.opcoes = Arrays.asList(opcoes);
        this.op = 0;
    }

    /**
     * Construtor do Menu
     * @param opcoes Opções possíveis
     */
    public Menu(List<String> opcoes) {
        this.opcoes = new ArrayList<>();
        this.opcoes.addAll(opcoes);
    }

    /**
     * Implementação do método responsável por interpretar a escolha do utilizador
     */
    public void executa() {
        do {
            showMenu();
            this.op = lerOpcao();
        } while (this.op == -1);
    }

    /**
     * Implementação do método que apresenta o menu com todas as opções disponíveis
     */
    private void showMenu() {
        System.out.println("\n <<< Menu >>>");
        for (int i = 0; i < this.opcoes.size(); i++) {
            System.out.println(i + 1 + " - " + this.opcoes.get(i));
        }
        System.out.println("0 - Sair");
    }

    /**
     * Implementação do método responsável por interpretar a escolha feita pelo utilizador
     * @return Escolha feita pelo utilizador
     */
    private int lerOpcao() {
        int op;

        System.out.print("Opção: ");
        try {
            op = Integer.parseInt(is.nextLine());
            if (op < 0 || op > this.opcoes.size()) {
                System.out.println("Opção Inválida!");
                op = -1;
            }
        } catch (NumberFormatException e) { // Não foi inscrito um int
            op = -1;
            System.out.println("Opção Inválida!");
        }
        return op;
    }

    /**
     * Implementação do método que devolve a escolha feita pelo utilizador.
     * @return Opção escolhida
     */
    public int getOpcao() {
        return this.op;
    }
}
