package iobjects.entity;

/**
 * Representa a exceção lançada quando mais de um registro foi encontrado
 * enquanto apenas um registro era esperado.
 */
public class ManyRecordsFoundException extends EntityException {

  public ManyRecordsFoundException(String message) {
    super(message);
  }

  public ManyRecordsFoundException(String className, String methodName,
                                   String message) {
    super(className, methodName, message);
  }

  public ManyRecordsFoundException(String className, String methodName,
                                   Exception source) {
    super(className, methodName, source);
  }

}
