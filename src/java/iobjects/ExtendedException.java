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
package iobjects;

/**
 * Representa uma exce��o lan�ada por um Objeto de Neg�cio da aplica��o.
 */
public class ExtendedException extends Exception {

  /**
   * Termina��o do nome da classe da Exce��o nas mensagens de exce��o
   * retornadas pelo m�todo getMessage(). Ex.:
   * java.lang.RuntimeException(): ...
   */
  static private final String EXCEPTION         = "Exception:";
  static public  final String MESSAGE_SEPARATOR = "##";

  /**
   * Construtor padr�o.
   * @param message String Mensagem da exce��o.
   */
  public ExtendedException(String message) {
    super(excludeExtraInfo(message));
  }

  /**
   * Construtor padr�o.
   * @param className String Nome da classe que originou a exce��o.
   * @param methodName String Nome do m�todo que originou a exce��o.
   * @param message String Mensagem da exce��o.
   */
  public ExtendedException(String className,
                           String methodName,
                           String message) {
    super(excludeExtraInfo(message)
          + MESSAGE_SEPARATOR + className
          + MESSAGE_SEPARATOR + methodName);
  }

  /**
   * Construtor padr�o.
   * @param className String Nome da classe que originou a exce��o.
   * @param methodName String Nome do m�todo que originou a exce��o.
   * @param source Exception Exce��o de origem.
   */
  public ExtendedException(String    className,
                           String    methodName,
                           Exception source) {
    super((source != null ? excludeExtraInfo(source.getMessage()) : "")
          + MESSAGE_SEPARATOR + className
          + MESSAGE_SEPARATOR + methodName
          + (source != null ? MESSAGE_SEPARATOR + source.getClass().getName() : ""));
    if (source != null)
      this.setStackTrace(source.getStackTrace());
  }

  /**
   * Retorna 'message' sem as informa��es extras. Isso evitar� o uso indevido
   * de ExtendedException em cascata.
   * @param message String Mensagem cujas informa��es extendidas ser�o retiradas.
   * @return String Retorna 'message' sem as informa��es extras. Isso evitar� o
   *         uso indevido de ExtendedException em cascata.
   */
  static private String excludeExtraInfo(String message) {
    int separatorIndex = message.indexOf(MESSAGE_SEPARATOR);
    if (separatorIndex >= 0)
      return message.substring(0, separatorIndex);
    else
      return message;
  }

  /**
   * Retorna a parte da mensagem de 'e'.
   * @param e Exception que cont�m a mensagem formatada com informa��es extras.
   * @return String Retorna a parte da mensagem de 'e'.
   */
  static public String extractMessage(Exception e) {
    // se n�o recebemos uma exce��o...retorna
    if ((e == null) || (e.getMessage() == null))
      return "";
    // nosso resultado
    String result = "";
    // separa em partes
    String[] parts = e.getMessage().split(MESSAGE_SEPARATOR);
    if (parts.length >= 1)
      result = parts[0];
    else
      result = e.getMessage();
    // se temos o nome da classe da exce��o...retira
    if (result.indexOf(EXCEPTION) >= 0)
      result = result.substring(result.lastIndexOf(EXCEPTION) + EXCEPTION.length()).trim();
    // retorna
    return result;
  }

  /**
   * Retorna a parte do nome da classe de 'e'.
   * @param e Exception que cont�m a mensagem formatada com informa��es extras.
   * @return String Retorna a parte do nome da classe de 'e'.
   */
  static public String extractClassName(Exception e) {
    // se n�o recebemos uma exce��o...retorna
    if ((e == null) || (e.getMessage() == null))
      return "";
    // tenta extrair das partes
    String[] parts = e.getMessage().split(MESSAGE_SEPARATOR);
    if (parts.length >= 2)
      return parts[1];
    else
      return "";
  }

  /**
   * Retorna a parte do nome do m�todo de 'e'.
   * @param e Exception que cont�m a mensagem formatada com informa��es extras.
   * @return String Retorna a parte do nome do m�todo de 'e'.
   */
  static public String extractMethodName(Exception e) {
    // se n�o recebemos uma exce��o...retorna
    if ((e == null) || (e.getMessage() == null))
      return "";
    String[] parts = e.getMessage().split(MESSAGE_SEPARATOR);
    if (parts.length >= 3)
      return parts[2];
    else
      return "";
  }

  /**
   * Retorna a parte do nome da classe da exce��o de 'e'.
   * @param e Exception que cont�m a mensagem formatada com informa��es extras.
   * @return String Retorna a parte do nome da classe da exce��o de 'e'.
   */
  static public String extractExceptionClassName(Exception e) {
    // se n�o recebemos uma exce��o...retorna
    if (e == null)
      return "";
    // separa a mensagem em partes
    String[] parts = (e.getMessage() != null ? e.getMessage().split(MESSAGE_SEPARATOR) : new String[]{""});
    // se temos a identifica��o da classe da exce��o...retorna
    if (parts.length >= 4)
      return parts[3];
    // se n�o temos...
    else
      return e.getClass().getName();
  }

}
