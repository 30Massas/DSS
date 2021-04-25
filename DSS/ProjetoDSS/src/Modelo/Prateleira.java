package Modelo;

public class Prateleira {
    private String codPrateleira;
    private boolean disponibilidade;
    private String codPalete;
    private GPS localizacao;

    /**
     * Construtor da Prateleiora
     * @param codPrateleira Código da Prateleira
     * @param disponibilidade Disponibilidade da Prateleira
     * @param codPalete Código da Palete que lá será armazenada
     * @param localizacao Localização da Prateleira
     */
    public Prateleira(String codPrateleira, boolean disponibilidade, String codPalete, GPS localizacao) {
        this.codPrateleira = codPrateleira;
        this.disponibilidade = disponibilidade;
        this.codPalete = codPalete;
        this.localizacao = localizacao;
    }

    /**
     * Implementação do método que devolve o código da Prateleira
     * @return Código da Prateleira
     */
    public String getCodPrateleira() {
        return codPrateleira;
    }

    /**
     * Implementação do método que averigua a disponibilidade da Prateleira
     * @return Valor lógico da disponibilidade (true caso esteja disponível)
     */
    public boolean isAvailable() {
        return disponibilidade;
    }

    /**
     * Implementação do método que define a disponibilidade da Prateleira
     * @param disponibilidade Disponibilidade a atribuir à Prateleira
     */
    public void setDisponibilidade(boolean disponibilidade) {
        this.disponibilidade = disponibilidade;
    }

    /**
     * Implementação do método que devolve o código da Palete lá armazenada
     * @return Código da Palete
     */
    public String getCodPalete() {
        return codPalete;
    }

    /**
     * Implementação do método que define a Palete a ser armazenada nessa Prateleira
     * @param codPalete Código da Palete a armazenar
     */
    public void setCodPalete(String codPalete) {
        this.codPalete = codPalete;
    }

    /**
     * Implementação do método que devolve a localização da Prateleira
     * @return Localização da Prateleira
     */
    public GPS getLocalizacao() {
        return localizacao;
    }

}
