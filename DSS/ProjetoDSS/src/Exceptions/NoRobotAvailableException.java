package Exceptions;

public class NoRobotAvailableException extends Exception{

    /**
     * Implementação do método responsável pela interpretação do erro obtido
     */
    public NoRobotAvailableException(){
        super();
    }

    /**
     * Implementação do método responsável pela interpretação do erro obtido apresentando uma mensagem
     */
    public NoRobotAvailableException(String m){
        super(m);
    }
}
