package iobjects.security;

import iobjects.*;

/**
 * Representa o serviço de segurança da aplicação.
 * <b>Esta interface deve ser implementada por apenas uma classe descendente
 * de BusinessObject na aplicação.</b>
 */
public interface SecurityService {

  /**
   * Altera a senha do usuário identificado por 'userId'.
   * @param userId int Identifacação do usuário.
   * @param oldPassword String Senha antiga.
   * @param newPassword String Nova senha.
   * @throws SecurityException Em caso de a senha anterior não conferir, a nova
   *                           senha não combinar com a política de definições
   *                           de senhas ou na tentativa de acesso ao banco de
   *                           dados.
   */
  public void changePassword(int    userId,
                             String oldPassword,
                             String newPassword) throws SecurityException;

  /**
   * Retorna o Action utilizado para acesso à interface do serviço de segurança.
   * @return Action Retorna o Action utilizado para acesso à interface do serviço
   *         de segurança.
   */
  public Action getSecurityServiceAction();

  /**
   * Retorna o usuário identificado por 'userId'. As informações sobre as ações
   * e relações mestre as quais o usuário têm direito de acesso não são
   * retornadas a partir deste método por motivo de segurança e desempenho.
   * @param userId int Identificação do usuário que se deseja retornar.
   * @return User Retorna o usuário referenciado por 'userId'.
   * @throws SecurityException Em caso de exceção na tentativa de localizar o usuário.
   */
  public User getUser(int userId) throws SecurityException;

  /**
   * Efetua o logon do usuário identificado por 'name' e 'password' e retorna
   * o User referente. Retorna null caso o 'nome' e 'password' não confiram.
   * @param name String Nome do usuário.
   * @param password String Senha do usuário.
   * @return User Retorna o User referente ao usuário que efetuou logon.
   * @throws SecurityException Em caso de exceção na tentativa de localizar o
   *                           usuário ou efetuar logon.
   */
  public User logon(String name,
                    String password) throws SecurityException;

}
