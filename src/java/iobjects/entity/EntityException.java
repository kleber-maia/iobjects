package iobjects.entity;

import iobjects.*;

/**
 * Representa a classe base das exceções lançadas pela classes que representam
 * entidades na aplicação.
 */
public class EntityException extends ExtendedException {

  public EntityException(String message) {
    super(message);
  }

  public EntityException(String className,
                         String methodName,
                         String message) {
    super(className, methodName, message);
  }

  public EntityException(String    className,
                         String    methodName,
                         Exception source) {
    super(className, methodName, source);
  }

}
