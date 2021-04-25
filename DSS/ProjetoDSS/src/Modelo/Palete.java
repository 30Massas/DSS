package Modelo;


public class Palete {
    private String codPalete;
    private String localizacao; //localização da prateleira/robot
    private String material;

    /**
     * Construtor da Palete
     * @param codPalete Código da Palete
     * @param localizacao Localização da Palete
     * @param material Material da Palete
     */
    public Palete(String codPalete, String localizacao, String material) {
        this.codPalete = codPalete;
        this.localizacao = localizacao;
        this.material = material;

    }

    /**
     * Construtor por cópia da Palete
     * @param p Palete a copiar
     */
    public Palete(Palete p) {
        setCodPalete(p.getCodPalete());
        setLoc(p.getLoc());
        setM(p.getM());
    }


    /**
     * Implementação do método que devolve o código da Palete
     * @return Código da Palete
     */
    public String getCodPalete() {
        return codPalete;
    }

    /**
     * Implementação do método que define o código da Palete
     * @param codPalete Código a ser atribuído à Palete
     */
    public void setCodPalete(String codPalete) {
        this.codPalete = codPalete;
    }

    /**
     * Implementação do método que devolve a localização da Palete
     * @return Localização da Palete
     */
    public String getLoc() {
        return localizacao;
    }

    /**
     * Implementação do método que define a localização da Palete
     * @param loc
     */
    public void setLoc(String loc) {
        this.localizacao = loc;
    }

    /**
     * Implementação do método que devolve o material da Palete
     * @return Material da Palete
     */
    public String getM() {
        return material;
    }

    /**
     * Implementação do método que define o material da Palete
     * @param material Material a ser atribuído à Palete
     */
    public void setM(String material) {
        this.material = material;
    }

    /**
     * Implementação do método que copia uma Palete
     * @return Cópia da palete
     */
    public Palete clone() {
        return new Palete(this);
    }
}
