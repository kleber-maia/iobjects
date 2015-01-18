/*
The MIT License (MIT)

Copyright (c) 2008 Kleber Maia de Andrade

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/   
package iobjects.security;

import iobjects.*;

/**
 * Representa o servi�o de seguran�a da aplica��o.
 * <b>Esta interface deve ser implementada por apenas uma classe descendente
 * de BusinessObject na aplica��o.</b>
 */
public interface SecurityService {

  /**
   * Altera a senha do usu�rio identificado por 'userId'.
   * @param userId int Identifaca��o do usu�rio.
   * @param oldPassword String Senha antiga.
   * @param newPassword String Nova senha.
   * @throws SecurityException Em caso de a senha anterior n�o conferir, a nova
   *                           senha n�o combinar com a pol�tica de defini��es
   *                           de senhas ou na tentativa de acesso ao banco de
   *                           dados.
   */
  public void changePassword(int    userId,
                             String oldPassword,
                             String newPassword) throws SecurityException;

  /**
   * Retorna o Action utilizado para acesso � interface do servi�o de seguran�a.
   * @return Action Retorna o Action utilizado para acesso � interface do servi�o
   *         de seguran�a.
   */
  public Action getSecurityServiceAction();

  /**
   * Retorna o usu�rio identificado por 'userId'. As informa��es sobre as a��es
   * e rela��es mestre as quais o usu�rio t�m direito de acesso n�o s�o
   * retornadas a partir deste m�todo por motivo de seguran�a e desempenho.
   * @param userId int Identifica��o do usu�rio que se deseja retornar.
   * @return User Retorna o usu�rio referenciado por 'userId'.
   * @throws SecurityException Em caso de exce��o na tentativa de localizar o usu�rio.
   */
  public User getUser(int userId) throws SecurityException;

  /**
   * Efetua o logon do usu�rio identificado por 'name' e 'password' e retorna
   * o User referente. Retorna null caso o 'nome' e 'password' n�o confiram.
   * @param name String Nome do usu�rio.
   * @param password String Senha do usu�rio.
   * @return User Retorna o User referente ao usu�rio que efetuou logon.
   * @throws SecurityException Em caso de exce��o na tentativa de localizar o
   *                           usu�rio ou efetuar logon.
   */
  public User logon(String name,
                    String password) throws SecurityException;

}
