package Exceptions;

public class NoPrateleirasAvailableException extends Exception{

    /**
     * Implementação do método responsável pela interpretação do erro obtido
     */
    public NoPrateleirasAvailableException(){
        super();
    }

    /**
     * Implementação do método responsável pela interpretação do erro obtido apresentado uma mensagem
     */
    public NoPrateleirasAvailableException(String m){
        super(m);
    }
}
