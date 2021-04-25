package Exceptions;

public class NoPaletesToCollectException extends Exception {

    /**
     * Implementação do método responsável pela interpretação do erro obtido
     */
    public NoPaletesToCollectException() {
        super();
    }

    /**
     * Implementação do método responsável pela interpretação do erro obtido apresentando uma mensagem
     */
    public NoPaletesToCollectException(String m) {
        super(m);
    }
}
