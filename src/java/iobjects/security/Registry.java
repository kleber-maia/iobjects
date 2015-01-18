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

/**
 * Representa o servi�o de registro da aplica��o.
 * <b>Esta interface deve ser implementada por apenas uma classe descendente
 * de BusinessObject na aplica��o.</b>
 */
public interface Registry {

  /**
   * Representa a raiz de informa��es baseadas no usu�rio. Utilize esta raiz
   * para armazenar informa��es referentes aos usu�rios do sistema.
   */
  static public final int ROOT_USER            = 0;
  /**
   * Representa a raiz de informa��es para a rela��o mestre. Utilize esta raiz
   * para armazenar informa��es referentes as rela��es mestres do sistema.
   */
  static public final int ROOT_MASTER_RELATION = 1;
  /**
   * Representa a raiz de informa��es para todo o sistema. Utilize esta raiz
   * para armazenar informa��es referentes ao sistema.
   */
  static public final int ROOT_SYSTEM          = 2;

  /**
   * Retorna o valor da chave de registro que foi gravado como um double.
   * @param root Raiz de informa��es.
   * @param key Chave onde o valor foi gravado.
   * @param name Nome do valor gravado.
   * @param defaultValue Valor padr�o, caso a chave n�o seja encontrada.
   * @return Retorna o valor da chave de registro que foi gravado como um double.
   * @throws java.lang.Exception Em caso de exce��o na tentativa de acesso ao registro.
   */
  public double readDouble(int root, String key, String name, double defaultValue) throws Exception;

  /**
   * Retorna o valor da chave de registro que foi gravado como um int.
   * @param root Raiz de informa��es.
   * @param key Chave onde o valor foi gravado.
   * @param name Nome do valor gravado.
   * @param defaultValue Valor padr�o, caso a chave n�o seja encontrada.
   * @return Retorna o valor da chave de registro que foi gravado como um int.
   * @throws java.lang.Exception Em caso de exce��o na tentativa de acesso ao registro.
   */
  public int readInteger(int root, String key, String name, int defaultValue) throws Exception;

  /**
   * Retorna o valor da chave de registro que foi gravado como uma String.
   * @param root Raiz de informa��es.
   * @param key Chave onde o valor foi gravado.
   * @param name Nome do valor gravado.
   * @param defaultValue Valor padr�o, caso a chave n�o seja encontrada.
   * @return Retorna o valor da chave de registro que foi gravado como uma String.
   * @throws java.lang.Exception Em caso de exce��o na tentativa de acesso ao registro.
   */
  public String readString(int root, String key, String name, String defaultValue) throws Exception;

  /**
   * Grava o valor da chave de registro como um double.
   * @param root Raiz de informa��es.
   * @param key Chave onde o valor ser� gravado.
   * @param name Nome do valor que ser� gravado.
   * @param value Valor a ser gravado.
   * @throws java.lang.Exception Em caso de exce��o na tentativa de acesso ao registro.
   */
  public void writeDouble(int root, String key, String name, double value) throws Exception;

  /**
   * Grava o valor da chave de registro como um int.
   * @param root Raiz de informa��es.
   * @param key Chave onde o valor ser� gravado.
   * @param name Nome do valor que ser� gravado.
   * @param value Valor a ser gravado.
   * @throws java.lang.Exception Em caso de exce��o na tentativa de acesso ao registro.
   */
  public void writeInteger(int root, String key, String name, int value) throws Exception;

  /**
   * Grava o valor da chave de registro como uma String.
   * @param root Raiz de informa��es.
   * @param key Chave onde o valor ser� gravado.
   * @param name Nome do valor que ser� gravado.
   * @param value Valor a ser gravado.
   * @throws java.lang.Exception Em caso de exce��o na tentativa de acesso ao registro.
   */
  public void writeString(int root, String key, String name, String value) throws Exception;
  
}
