package Exceptions;

public class NoPaletesToDeliverException extends Exception {

    /**
     * Implementação do método responsável pela interpretação do erro obtido
     */
    public NoPaletesToDeliverException() {
        super();
    }

    /**
     * Implementação do método responsável pela interpretação do erro obtido apresentando uma mensagem
     */
    public NoPaletesToDeliverException(String m) {
        super(m);
    }
}
