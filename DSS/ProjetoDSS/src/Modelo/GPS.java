package Modelo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

public class GPS {
    private int x;
    private int y;

    /**
     * Implementação do método que cria o percurso a percorrer pelo Robot
     * @param mapa Mapa do armazém
     * @param source Localização inicial
     * @param destination Destino final
     * @return Percurso a efetuar pelo Robot
     */
    public static Percurso criaCaminho(List<GPS> mapa, GPS source, GPS destination) {
        double melhor = 99;

        double total = 0;

        List<GPS> tentativa = new ArrayList<>();
        List<GPS> caminho = new ArrayList<>();

        if (source.getY() == destination.getY()) {
            caminho.add(destination.clone());
            melhor = dist2Pontos(source, destination);
        } else {

            boolean valid = false;
            for (GPS temp : mapa) {
                GPS back = source.clone();
                tentativa.add(source.clone());
                if (temp.getY() == back.getY()) {
                    total += dist2Pontos(back, temp);
                    tentativa.add(temp.clone());
                    back = temp.clone();
                    for (GPS temp2 : mapa) {
                        if (temp2.getX() == back.getX() && temp2.getY() == destination.getY()) {
                            total += dist2Pontos(back, temp2);
                            tentativa.add(temp2.clone());
                            back = temp2.clone();
                            total += dist2Pontos(back, destination);
                            tentativa.add(destination.clone());
                            valid = true;
                        }
                    }
                }
                if (!valid) {
                    total = 0;
                    tentativa = new ArrayList<>();
                } else {
                    if (total < melhor) {
                        melhor = total;
                        caminho = new ArrayList<>(tentativa);
                    }
                    tentativa = new ArrayList<>();
                    total = 0;
                    valid = false;
                }
            }
        }
        return new Percurso(caminho, (int) melhor);
    }

    /**
     * Implementação do método que calcula a distância entre dois pontos
     * @param a Ponto inicial
     * @param b Ponto final
     * @return Distância entre os dois pontos
     */
    public static double dist2Pontos(GPS a, GPS b) {
        return sqrt(pow((a.getX() - b.getX()), 2) + pow((a.getY() - b.getY()), 2));
    }

    /**
     * Implementação do método que cria o mapa inicial do armazém
     * @return Lista com as localizações essenciais à interpretação do mapa
     */
    public static List<GPS> criaMapa() {
        List<GPS> mapa = new ArrayList<>();
        mapa.add(new GPS(4, 0));
        mapa.add(new GPS(26, 0));
        mapa.add(new GPS(4, 5));
        mapa.add(new GPS(26, 5));
        mapa.add(new GPS(26, 3));
        return mapa;
    }

    /**
     * Construtor da localização GPS
     * @param x Coordenada X
     * @param y Coordenada Y
     */
    public GPS(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Construtor por cópia da localização GPS
     * @param g Localização GPS a copiar
     */
    public GPS(GPS g) {
        setX(g.getX());
        setY(g.getY());
    }

    /**
     * Implementação do método que devolve a coordenada X de uma localização
     * @return Coordenada X
     */
    public int getX() {
        return x;
    }

    /**
     * Implementação do método que define a coordenada X de uma localização
     * @param x Coordenada X a definir
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * Implementação do método que devolve a coordenada Y de uma localização
     * @return Coordenada Y
     */
    public int getY() {
        return y;
    }

    /**
     * Implementação do método que define a coordenada Y de uma localização
     * @param y Coordenada Y a definir
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * Implementação do método que efetua a cópia da uma localização GPS
     * @return Cópia de uma localização GPS
     */
    public GPS clone() {
        return new GPS(this);
    }

    /**
     * Implementação do método que verifica se duas localizações são iguais
     * @param o Localização a comparar
     * @return Valor lógico da comparação (true se forem iguais)
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GPS gps = (GPS) o;
        return x == gps.x && y == gps.y;
    }

    /**
     * Implementação do método que converte numa string uma dada localização GPS
     * @return Mensagem com a localização
     */
    @Override
    public String toString() {
        return "GPS{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
