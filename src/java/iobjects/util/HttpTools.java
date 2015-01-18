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
package iobjects.util;

import java.io.*;
import java.net.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;

import iobjects.*;

/**
 * Classe utilit�ria para opera��es com elementos HTTP.
 */

public class HttpTools {

  /**
   * Acessa o recurso na internet indicado por 'url' e retorna o conte�do
   * entregue pelo servidor. <b>O caminho deve ser completo, como: 
   * http://www.softgroup.com.br ou http://localhost:8084?param=teste.</b>
   * @param url Caminho na internet para ser navegado.
   * @return
   * @throws Exception Exce��o em caso de erro na tentativa de acesso ao caminho 
   *                   especificado.
   */
  static public String browse(String url) throws Exception {
    // nossa conex�o
    HttpURLConnection connection = null;
    try {
      // gerenciador de cookies
      CookieManager cm = new CookieManager();
      CookieHandler.setDefault(cm);        
      // URL do servidor
      URL url_ = new URL(url);
      // cria a conex�o com o servidor
      connection = (HttpURLConnection)url_.openConnection();
      // vamos apenas receber conte�do
      //connection.setDoInput(true);
      //connection.setDoOutput(false);
      // vamos usar o m�todo GET
      //connection.setRequestMethod("POST");
      // envia a requisi��o e obt�m o c�digo da resposta
      int responseCode = connection.getResponseCode();
      // se n�o recebemos uma resposta OK...avisa
      if (responseCode != HttpURLConnection.HTTP_OK)
        throw new Exception(responseCode + " - " + connection.getResponseMessage());
      // obt�m o conte�do
      InputStream inputStream = connection.getInputStream();
      byte[] buffer = new byte[1024];
      StringBuilder result = new StringBuilder();
      while (true) {
        int count = inputStream.read(buffer);
        if (count > 0)
          result.append(new String(buffer, 0, count));
        else
          break;   
      } // while
      // retorna
      return result.toString();
    }
    finally {
      // libera recursos
      if (connection != null)
        connection.disconnect();
    } // try-finally
  }
    
  /**
   * Retorna o Cookie contido em 'request' com nome indicado em 'name'.
   * @param request HttpServletRequest Requisi��o onde o cookie sera pesquisado.
   * @param name String Nome do cookie que se deseja retornar.
   * @return Cookie Retorna o Cookie contido em 'request' com nome indicado em 'name'.
   */
  static public Cookie getCookie(HttpServletRequest request,
                                 String             name) {
    Cookie[] cookies = null;
    try {
      // pega os Cookies
      cookies = request.getCookies();
    }
    catch (Exception e) {
      // se chegou at� aqui...n�o achamos
      return null;
    } // try-catch
    // se n�o temos cookies...dispara
    if (cookies == null)
      return null;
    // loop a procura do Cookie desejado
    Cookie result;
    for (int i = 0; i<cookies.length; i++) {
      // cookie da vez
      result = cookies[i];
      // se achamos...retorna
      if (result.getName().equalsIgnoreCase(name))
        return result;
    } // for
    // se chegou at� aqui...n�o achamos
    return null;
  }

  /**
   * Retorna o valor do cookie contido em 'request' com nome indicado em 'name'.
   * @param request HttpServletRequest Requisi��o onde o cookie sera pesquisado.
   * @param name String Nome do cookie cujo valor se deseja retornar.
   * @return String Retorna o valor do cookie contido em 'request' com nome
   *                indicado em 'name'.
   */
  static public String getCookieValue(HttpServletRequest request,
                                      String             name) {
    // retorna
    return getCookieValue(request, name, null);
  }

  /**
   * Retorna o valor do cookie contido em 'request' com nome indicado em 'name'.
   * @param request HttpServletRequest Requisi��o onde o cookie sera pesquisado.
   * @param name String Nome do cookie cujo valor se deseja retornar.
   * @param defaultValue String Valor para retornar caso o retorno da leitura
   *                     do cookie retorne 'null'.
   * @return String Retorna o valor do cookie contido em 'request' com nome
   *                indicado em 'name'.
   */
  static public String getCookieValue(HttpServletRequest request,
                                      String             name,
                                      String             defaultValue) {
    // procura o Cookie
    Cookie cookie = getCookie(request, name);
    // se achamos...retorna seu valor
    if (cookie == null)
      return defaultValue;
    else {
      // nosso resultado
      StringBuffer result = new StringBuffer(cookie.getValue());
      // retira as " do in�cio e do final adicionadas pelo Tomcat 5.x
      if ((result.length() > 0) && (result.charAt(0) == '"'))
        result.delete(0, 1);
      if ((result.length() > 0) && (result.charAt(result.length()-1) == '"'))
        result.delete(result.length()-1, result.length());
      // retorna
      return result.toString();
    } // if
  }

  /**
   * Redireciona a requisi��o representada por 'pageContext' para o Action
   * especificado.
   * @param action Action de destino.
   * @param command Command para ser executado em 'action'.
   * @param params String Par�metros do tipo 'param1=value1&param2=value2' para
   *               serem adicionados a URL.
   * @param context PageContext Contexto da p�gina.
   * @throws ServletException Em caso de exce��o na tentativa de localizar o
   *                          caminho especificado.
   * @throws IOException Em caso de exce��o na tentativa de escrever para a sa�da
   *                     padr�o.
   */
  static public void forward(Action      action,
                             Command     command,
                             String      params,
                             PageContext context) throws ServletException, IOException {
    context.getServletContext().getRequestDispatcher("/" + action.url(command, params)).forward(context.getRequest(), context.getResponse());
  }

  /**
   * Redireciona a requisi��o representada por 'pageContext' para o caminho
   * especificado.
   * @param path String Caminho de destino.
   * @param context PageContext Contexto da p�gina.
   * @throws ServletException Em caso de exce��o na tentativa de localizar o
   *                          caminho especificado.
   * @throws IOException Em caso de exce��o na tentativa de escrever para a sa�da
   *                     padr�o.
   */
  static public void forward(String      path,
                             PageContext context) throws ServletException, IOException {
    context.getServletContext().getRequestDispatcher("/" + path).forward(context.getRequest(), context.getResponse());
  }

  /**
   * Redireciona a requisi��o representada por 'pageContext' para o caminho
   * especificado.
   * @param action Action de destino.
   * @param command Command para ser executado em 'action'.
   * @param params String Par�metros do tipo 'param1=value1&param2=value2' para
   *               serem adicionados a URL.
   * @param context ServletContext Contexto do Servlet.
   * @param request HttpServletRequest Requisi��o.
   * @param response HttpServletResponse Resposta.
   * @throws ServletException Em caso de exce��o na tentativa de localizar o
   *                          caminho especificado.
   * @throws IOException Em caso de exce��o na tentativa de escrever para a sa�da
   *                     padr�o.
   */
  static public void forward(Action              action,
                             Command             command,
                             String              params,
                             ServletContext      context,
                             HttpServletRequest  request,
                             HttpServletResponse response) throws ServletException, IOException {
    context.getRequestDispatcher("/" + action.url(command, params)).forward(request, response);
  }

  /**
   * Redireciona a requisi��o representada por 'pageContext' para o caminho
   * especificado.
   * @param path String Caminho de destino.
   * @param request HttpServletRequest Requisi��o.
   * @param response HttpServletResponse Resposta.
   * @param context ServletContext Contexto do Servlet.
   * @throws ServletException Em caso de exce��o na tentativa de localizar o
   *                          caminho especificado.
   * @throws IOException Em caso de exce��o na tentativa de escrever para a sa�da
   *                     padr�o.
   */
  static public void forward(String              path,
                             ServletContext      context,
                             HttpServletRequest  request,
                             HttpServletResponse response) throws ServletException, IOException {
    context.getRequestDispatcher("/" + path).forward(request, response);
  }

  /**
   * Retorna a String de query de 'request' reconstru�da de modo a evitar o
   * problema de codifica��o da URL.
   * @param request HttpServletRequest Requisi��o.
   * @return String Retorna a String de query de 'request' reconstru�da de modo
   *                a evitar o problema de codifica��o da URL.
   */
  static public String rebuildQueryString(HttpServletRequest request) {
    // nosso resultado
    StringBuffer result = new StringBuffer(request.getQueryString().length());
    // nomes dos par�metros existentes
    Enumeration paramNames = request.getParameterNames();
    // loop nos par�metros
    while (paramNames.hasMoreElements()) {
      // nome par�metro da vez
      String paramName = (String)paramNames.nextElement();
      // loop nos valores do par�metro atual
      String[] paramValues = request.getParameterValues(paramName);
      for (int i=0; i<paramValues.length; i++) {
        if (result.length() > 0)
          result.append("&");
        result.append(paramName + "=" + paramValues[i]);
      } // for
    } // while
    // retorna
    return result.toString();
  }

  /**
   * Cria um novo cookie com o nome indicado por 'name' e adiciona em 'response'
   * e retorna-o.
   * @param response HttpServletResponse Resopnsta onde o cookie ser� adicionado.
   * @param name String Nome do cookie que sera criado.
   * @param value String Valor do cookie.
   * @param secure boolean True se o cookie for seguro.
   * @return Cookie Cria um novo cookie com o nome indicado por 'name' e adiciona
   * em 'response' e retorna-o.
   */
  static public Cookie setCookieValue(HttpServletResponse response,
                                      String              name,
                                      String              value,
                                      boolean             secure) {
    return setCookieValue(response, name, value, secure, 365);
  }

  /**
   * Cria um novo cookie com o nome indicado por 'name' e adiciona em 'response'
   * e retorna-o.
   * @param response HttpServletResponse Resopnsta onde o cookie ser� adicionado.
   * @param name String Nome do cookie que sera criado.
   * @param value String Valor do cookie.
   * @param secure boolean True se o cookie for seguro.
   * @param daysToExpire int Dias para que o cookie expire.
   * @return Cookie Cria um novo cookie com o nome indicado por 'name' e adiciona
   * em 'response' e retorna-o.
   */
  static public Cookie setCookieValue(HttpServletResponse response,
                                      String              name,
                                      String              value,
                                      boolean             secure,
                                      int                 daysToExpire) {
    // cria o Cookie
    Cookie result = new Cookie(name, value);
    // � um cookie seguro?
    result.setSecure(secure);
    // define sua data de expira��o para daqui a 1 ano
    result.setMaxAge(daysToExpire * 24 * 60 * 60);
    // adiciona na resposta da requisi��o
    response.addCookie(result);
    // retorna
    return result;
  }

}
