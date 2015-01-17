package iobjects.util.mail;

/**
 * Interface que deve ser implementada pelas classes que terão a capacidade
 * de receber mensagens através do serviço de e-mail da aplicação.
 */
public interface Pop3Callback {

  /**
   * Evento chamado sempre que uma mensagem for recebida.
   * @param message Message recebida.
   * @return boolean <b>True para que a mensagem seja apagada definitivamente do
   *                 servidor. Essa é a ação normal a ser tomada após cada
   *                 mensagem ter sido processada com sucesso.</b>
   * @throws Exception Em caso de exceção na tentativa de processamento da mensagem.
   */
  public boolean onReceiveMessage(Message message) throws Exception;

}
