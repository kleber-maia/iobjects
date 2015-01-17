package iobjects.security;

/**
 * Representa uma exceção lançada durante uma operação de segurança.
 */

public class SecurityException extends Exception {

  public SecurityException(String message) {
    super(message);
  }

}
