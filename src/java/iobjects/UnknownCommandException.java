package iobjects;

/**
 * Representa uma exce��o de comando desconhecido.
 */
public class UnknownCommandException extends Exception {

  static public final String MESSAGE = "Comando desconhecido.";

  /**
   * Construtor padr�o.
   */
  public UnknownCommandException() {
    super(MESSAGE);
  }

}
