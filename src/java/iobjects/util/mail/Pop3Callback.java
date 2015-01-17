package iobjects.util.mail;

/**
 * Interface que deve ser implementada pelas classes que ter�o a capacidade
 * de receber mensagens atrav�s do servi�o de e-mail da aplica��o.
 */
public interface Pop3Callback {

  /**
   * Evento chamado sempre que uma mensagem for recebida.
   * @param message Message recebida.
   * @return boolean <b>True para que a mensagem seja apagada definitivamente do
   *                 servidor. Essa � a a��o normal a ser tomada ap�s cada
   *                 mensagem ter sido processada com sucesso.</b>
   * @throws Exception Em caso de exce��o na tentativa de processamento da mensagem.
   */
  public boolean onReceiveMessage(Message message) throws Exception;

}
