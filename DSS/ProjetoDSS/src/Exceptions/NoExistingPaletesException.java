package Exceptions;

public class NoExistingPaletesException extends Exception {

    /**
     * Implementação do método responsável pela interpretação do erro obtido
     */
    public NoExistingPaletesException() {
        super();
    }

    /**
     * Implementação do método responsável pela interpretação do erro obtido apresentando uma mensagem
     */
    public NoExistingPaletesException(String m) {
        super(m);
    }
}
