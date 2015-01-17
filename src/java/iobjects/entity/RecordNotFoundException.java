package iobjects.entity;

/**
 * Representa a exce��o lan�ada quando um registro esperado n�o � encontrado.
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
