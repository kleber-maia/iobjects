package iobjects.security;

/**
 * Representa o serviço de registro da aplicação.
 * <b>Esta interface deve ser implementada por apenas uma classe descendente
 * de BusinessObject na aplicação.</b>
 */
public interface Registry {

  /**
   * Representa a raiz de informações baseadas no usuário. Utilize esta raiz
   * para armazenar informações referentes aos usuários do sistema.
   */
  static public final int ROOT_USER            = 0;
  /**
   * Representa a raiz de informações para a relação mestre. Utilize esta raiz
   * para armazenar informações referentes as relações mestres do sistema.
   */
  static public final int ROOT_MASTER_RELATION = 1;
  /**
   * Representa a raiz de informações para todo o sistema. Utilize esta raiz
   * para armazenar informações referentes ao sistema.
   */
  static public final int ROOT_SYSTEM          = 2;

  /**
   * Retorna o valor da chave de registro que foi gravado como um double.
   * @param root Raiz de informações.
   * @param key Chave onde o valor foi gravado.
   * @param name Nome do valor gravado.
   * @param defaultValue Valor padrão, caso a chave não seja encontrada.
   * @return Retorna o valor da chave de registro que foi gravado como um double.
   * @throws java.lang.Exception Em caso de exceção na tentativa de acesso ao registro.
   */
  public double readDouble(int root, String key, String name, double defaultValue) throws Exception;

  /**
   * Retorna o valor da chave de registro que foi gravado como um int.
   * @param root Raiz de informações.
   * @param key Chave onde o valor foi gravado.
   * @param name Nome do valor gravado.
   * @param defaultValue Valor padrão, caso a chave não seja encontrada.
   * @return Retorna o valor da chave de registro que foi gravado como um int.
   * @throws java.lang.Exception Em caso de exceção na tentativa de acesso ao registro.
   */
  public int readInteger(int root, String key, String name, int defaultValue) throws Exception;

  /**
   * Retorna o valor da chave de registro que foi gravado como uma String.
   * @param root Raiz de informações.
   * @param key Chave onde o valor foi gravado.
   * @param name Nome do valor gravado.
   * @param defaultValue Valor padrão, caso a chave não seja encontrada.
   * @return Retorna o valor da chave de registro que foi gravado como uma String.
   * @throws java.lang.Exception Em caso de exceção na tentativa de acesso ao registro.
   */
  public String readString(int root, String key, String name, String defaultValue) throws Exception;

  /**
   * Grava o valor da chave de registro como um double.
   * @param root Raiz de informações.
   * @param key Chave onde o valor será gravado.
   * @param name Nome do valor que será gravado.
   * @param value Valor a ser gravado.
   * @throws java.lang.Exception Em caso de exceção na tentativa de acesso ao registro.
   */
  public void writeDouble(int root, String key, String name, double value) throws Exception;

  /**
   * Grava o valor da chave de registro como um int.
   * @param root Raiz de informações.
   * @param key Chave onde o valor será gravado.
   * @param name Nome do valor que será gravado.
   * @param value Valor a ser gravado.
   * @throws java.lang.Exception Em caso de exceção na tentativa de acesso ao registro.
   */
  public void writeInteger(int root, String key, String name, int value) throws Exception;

  /**
   * Grava o valor da chave de registro como uma String.
   * @param root Raiz de informações.
   * @param key Chave onde o valor será gravado.
   * @param name Nome do valor que será gravado.
   * @param value Valor a ser gravado.
   * @throws java.lang.Exception Em caso de exceção na tentativa de acesso ao registro.
   */
  public void writeString(int root, String key, String name, String value) throws Exception;
  
}
