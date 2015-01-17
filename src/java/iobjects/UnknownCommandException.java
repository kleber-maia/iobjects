package iobjects;

/**
 * Representa uma exceção de comando desconhecido.
 */
public class UnknownCommandException extends Exception {

  static public final String MESSAGE = "Comando desconhecido.";

  /**
   * Construtor padrão.
   */
  public UnknownCommandException() {
    super(MESSAGE);
  }

}
