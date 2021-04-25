package Modelo;

import java.util.ArrayList;
import java.util.List;

public class Percurso {
    private List<GPS> recolha;
    private int distancia;


    /**
     * Construtor do Percurso a ser efetuado pelo Robot
     * @param recolha Lista de localizações de recolha
     * @param distancia Distância a percorrer
     */
    public Percurso(List<GPS> recolha, int distancia) {
        this.recolha = recolha;
        this.distancia = distancia;
    }

    /**
     * Construtor por cópia do Percurso
     * @param p Percurso a copiar
     */
    public Percurso(Percurso p) {
        this.recolha = new ArrayList<>();
        for (GPS g : p.getRecolha())
            this.recolha.add(g.clone());
        this.distancia = p.getDistancia();
    }

    /**
     * Implementação do método que devolve a lista de localizações necessárias para proceder à recolha
     * @return Lista de localizações
     */
    public List<GPS> getRecolha() {
        List<GPS> novo = new ArrayList<>();
        for (GPS gps : recolha) {
            novo.add(gps.clone());
        }
        return recolha;
    }

    /**
     * Implementação do método que devolve a distância necessária a percorrer
     * @return Distância a percorrer
     */
    public int getDistancia() {
        return this.distancia;
    }

    /**
     * Implementação do método que procede à copia do Percurso
     * @return Cópia do Percurso
     */
    public Percurso clone() {
        return new Percurso(this);
    }

    /**
     * Implementação do método que converte numa string o Percurso a percorrer
     * @return Percurso a percorrer como mensagem
     */
    public String toString() {
        return this.recolha.toString() + " com distancia: " + this.distancia;
    }
}
