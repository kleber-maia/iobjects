package iobjects.remote;

import java.io.*;

/**
 * Representa uma exceção remota.
 */
public class RemoteException extends Exception implements Serializable {

  String causeClassName = "";
  String causeMessage   = "";

  /**
   * Construtor estendido: não deve ser chamado pela aplicação.
   */
  public RemoteException() {
  }

  /**
   * Construtor padrão.
   * @param cause Throwable Causa da exceção.
   */
  public RemoteException(Throwable cause) {
    super(cause);
    // *
    this.causeClassName = cause.getClass().getName();
    this.causeMessage = cause.getMessage();
  }

  public void deserialize(SerializeReader reader) throws IOException {
    // lê o nome da classe
    causeClassName = reader.readString();
    // lê a mensagem
    causeMessage = reader.readString();
  }

  /**
   * Retorna o nome da classe da causa da exceção.
   * @return String Retorna o nome da classe da causa da exceção.
   */
  public String getCauseClassName() {
    return causeClassName;
  }

  /**
   * Retorna a mensagem da causa da exceção.
   * @return String Retorna a mensagem da causa da exceção.
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
