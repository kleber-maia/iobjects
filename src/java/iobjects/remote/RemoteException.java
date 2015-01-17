package iobjects.remote;

import java.io.*;

/**
 * Representa uma exce��o remota.
 */
public class RemoteException extends Exception implements Serializable {

  String causeClassName = "";
  String causeMessage   = "";

  /**
   * Construtor estendido: n�o deve ser chamado pela aplica��o.
   */
  public RemoteException() {
  }

  /**
   * Construtor padr�o.
   * @param cause Throwable Causa da exce��o.
   */
  public RemoteException(Throwable cause) {
    super(cause);
    // *
    this.causeClassName = cause.getClass().getName();
    this.causeMessage = cause.getMessage();
  }

  public void deserialize(SerializeReader reader) throws IOException {
    // l� o nome da classe
    causeClassName = reader.readString();
    // l� a mensagem
    causeMessage = reader.readString();
  }

  /**
   * Retorna o nome da classe da causa da exce��o.
   * @return String Retorna o nome da classe da causa da exce��o.
   */
  public String getCauseClassName() {
    return causeClassName;
  }

  /**
   * Retorna a mensagem da causa da exce��o.
   * @return String Retorna a mensagem da causa da exce��o.
   */
  public String getCauseMessage() {
    return causeMessage;
  }

  public void serialize(SerializeWriter writer) throws IOException {
    // escreve o nome da classe
    writer.writeString(causeClassName);
    // escreve a mensagem
    writer.writeString(causeMessage);
  }

}
