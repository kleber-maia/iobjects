package iobjects.entity;

/**
 * Representa a exceção lançada quando um registro esperado não é encontrado.
 */
public class RecordNotFoundException extends EntityException {

  public RecordNotFoundException(String message) {
    super(message);
  }

  public RecordNotFoundException(String className,
                                 String methodName,
                                 String message) {
    super(className, methodName, message);
  }

  public RecordNotFoundException(String    className,
                                 String    methodName,
                                 Exception source) {
    super(className, methodName, source);
  }

}
