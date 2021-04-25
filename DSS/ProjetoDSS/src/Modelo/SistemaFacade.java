package Modelo;

import Exceptions.*;

public interface SistemaFacade {

    /**
     * Implementação do método que comunica ao sistema um código QR de uma palete
     * @param produto Palete à qual vai ser atribuída um código
     * @return Código QR atribuído à palete
     */
    String comunicaCodigoQR(String produto);

    /**
     * Implementação do método que procura um Robot de forma a realizar o transporte de uma palete e ao mesmo tempo uma prateleira onde armazená-la
     * @return Mensagem de sucesso/erro
     * @throws NoPaletesOnWaitingListException caso não haja paletes à espera de transporte
     * @throws NoPrateleirasAvailableException caso não haja prateleiras disponíveis para armazenar
     * @throws NoRobotAvailableException caso não haja robots disponíveis para transportar
     */
    String comunicaOrdemDeTransporte() throws NoPaletesOnWaitingListException, NoPrateleirasAvailableException, NoRobotAvailableException;

    /**
     * Implementação do método designado a notificar o sistema que o robot já procedeu à recolha de uma palete
     * @return Mensagem de sucesso/erro
     * @throws NoPaletesToCollectException caso não haja paletes para recolha
     * @throws NoRobotAvailableException caso não haja robots disponíveis
     */
    String notificaRecolhaDePaletes() throws NoPaletesToCollectException, NoRobotAvailableException;

    /**
     * Implementação do método designado a notificar o sistema de que um robot já procedeu à entrega efetiva de uma palete
     * @return Mensagem de sucesso/erro
     * @throws NoPaletesToDeliverException caso não existam paletes para entregar
     * @throws NoPrateleirasAvailableException caso não existam prateleiras disponíveis
     */
    String notificaEntregaDePaletes() throws NoPaletesToDeliverException, NoPrateleirasAvailableException;

    /**
     * Implementação do método que apresenta a lista com a informação sobre todas as paletes presentes no sistema
     * @return Lista com a informação sobre as paletes
     * @throws NoExistingPaletesException caso não existam paletes no sistema
     */
    String consultaListagem() throws NoExistingPaletesException;
}
