package Exceptions;

public class NoPaletesOnWaitingListException extends Exception {

    /**
     * Implementação do método responsável pela interpretação do erro obtido
     */
    public NoPaletesOnWaitingListException() {
        super();
    }

    /**
     * Implementação do método responsável pela interpretação do erro obtido apresentando uma mensagem
     */
    public NoPaletesOnWaitingListException(String m) {
        super(m);
    }
}
