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
package iobjects.servlet;

import java.io.*;
import java.lang.reflect.*;
import java.util.*;

import javax.servlet.*;
import javax.servlet.http.*;

import iobjects.*;
import iobjects.ui.ajax.*;
import iobjects.util.*;

/**
 * Representa o Servlet AjaxServer da aplicação responável pela execução
 * de métodos remotos e retorno dos dados em formato XML.
 */
public class AjaxServer extends HttpServlet {

  static public final String REMOTE_CLASS_NAME               = "className";
  static public final String REMOTE_METHOD_NAME              = "methodName";
  static public final String REMOTE_PARAMETER_VALUES         = "parameterValues";
  static public final String REMOTE_PARAMETER_TYPES          = "parameterTypes";
  static public final String REMOTE_CLASS_IS_BUSINESS_OBJECT = "isBusinessObject";

  static public final int PARAMETER_TYPE_BOOLEAN       = 0;
  static public final int PARAMETER_TYPE_DATE          = 1;
  static public final int PARAMETER_TYPE_DATE_TIME     = 2;
  static public final int PARAMETER_TYPE_DOUBLE        = 3;
  static public final int PARAMETER_TYPE_INTEGER       = 4;
  static public final int PARAMETER_TYPE_STRING        = 5;
  static public final int PARAMETER_TYPE_TIMESTAMP     = 6;
  static public final int PARAMETER_TYPE_STRING_ARRAY  = 7;
  static public final int PARAMETER_TYPE_INTEGER_ARRAY = 8;
  static public final int PARAMETER_TYPE_DOUBLE_ARRAY  = 9;

  static public final Class[] PARAMETER_TYPES = {Boolean.class,
                                                 Date.class,
                                                 Date.class,
                                                 Double.class,
                                                 Integer.class,
                                                 String.class,
                                                 java.sql.Timestamp.class,
                                                 String[].class,
                                                 int[].class,
                                                 double[].class};

  static public final String CLASS_NAME     = "iobjects.servlet.AjaxServer";
  static public final String LAST_EXCEPTION = "lastException";

  static public final String METHOD_LOGGEDIN  = "loggedin";
  static public final String METHOD_LOGIN     = "login";
  static public final String METHOD_LOGOFF    = "logoff";
  static public final String METHOD_PING      = "ping";

  static public final String PARAM_DEFAULT_CONNECTION_NAME = "defaultConnectionName";
  static public final String PARAM_USER_NAME               = "userName";
  static public final String PARAM_PASSWORD                = "password";
  static public final String PARAM_PING_KEY                = "pingKey";

  static public final String HTTP_HEADER_CONTENT_TYPE_TEXT = "text/plain;charset=ISO-8859-1";
  static public final String HTTP_HEADER_CONTENT_TYPE_XML  = "text/xml;charset=ISO-8859-1";

  static public final String XML_TAG = "<?xml version='1.0' encoding='ISO-8859-1'?>";

  /**
   * Processa as requisições Get recebidas.
   * @param request HttpServletRequest referente à requisição.
   * @param response HttpServletResponse referente à resposta.
   * @throws ServletException Em caso de exceção na tentativa de realizar uma
   *                          das operações de redirecionamento e/ou execução
   *                          de comandos.
   * @throws IOException Em caso de exceção na tentativa de realizar uma
   *                     das operações de redirecionamento e/ou execução
   *                     de comandos.
   */
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    doPost(request, response);
  }

  /**
   * Processa as requisições Post recebidas.
   * @param request HttpServletRequest referente à requisição.
   * @param response HttpServletResponse referente à resposta.
   * @throws ServletException Em caso de exceção na tentativa de realizar uma
   *                          das operações de redirecionamento e/ou execução
   *                          de comandos.
   * @throws IOException Em caso de exceção na tentativa de realizar uma
   *                     das operações de redirecionamento e/ou execução
   *                     de comandos.
   */
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    try {
      // nossos parâmetros
      String   className         = request.getParameter(REMOTE_CLASS_NAME);
      boolean  isBusinessObject  = Boolean.parseBoolean(request.getParameter(REMOTE_CLASS_IS_BUSINESS_OBJECT));
      String   methodName        = request.getParameter(REMOTE_METHOD_NAME);
      String[] parameterValues   = request.getParameterValues(REMOTE_PARAMETER_VALUES);
               if (parameterValues == null) 
                 parameterValues = new String[0];
/*
               else
                 for (int i=0; i<parameterValues.length; i++)
                   parameterValues[i] = URLDecoder.decode(parameterValues[i], request.getCharacterEncoding());
*/
      String[] strParameterTypes = request.getParameterValues(REMOTE_PARAMETER_TYPES);
      int[]    parameterTypes    = (strParameterTypes != null ? NumberTools.parseIntArray(strParameterTypes) : new int[0]);
      
      // nosso resultado
      Object result = null;

      // se quer efetuar um ping...
      if (className.equals(CLASS_NAME) && methodName.equals(METHOD_PING)) {
        // retorna a mesma chave recebida
        result = parameterValues[0];
      }
      // se quer saber se existe um usuário que efetuou logon...
      else if (className.equals(CLASS_NAME) && methodName.equals(METHOD_LOGGEDIN)) {
        // verifica o Facade
        Facade facade = Controller.getFacade(request);
        // temos um usuário autenticado?
        result = facade.getLoggedUser() != null;
      }
      // se quer efetuar login...
      else if (className.equals(CLASS_NAME) && methodName.equals(METHOD_LOGIN)) {
        // verifica o Facade
        Facade facade = Controller.getFacade(request);
        // nossos parâmetros
        String defaultConnectionName = parameterValues[0];
        String userName              = parameterValues[1];
        String password              = parameterValues[2];
        // se mudou o nome do usuário...logoff
        if ((facade.getLoggedUser() != null) && (!facade.getLoggedUser().getName().equals(userName)))
          facade.logoff();
        // se não fez logon...faz
        if (facade.getLoggedUser() == null)
          facade.logon(defaultConnectionName, userName, password);
      }
      // se quer efetuar logoff...
      else if (className.equals(CLASS_NAME) && methodName.equals(METHOD_LOGOFF)) {
        // verifica o Facade
        Facade facade = Controller.getFacade(request);
        // efetua logoff
        facade.logoff();
      }
      // se quer outro método...
      else {
        // verifica o Facade
        Facade facade = Controller.getFacade(request);
        // objeto desejado
        Object object = null;
        // se é a Facade...
        if (className.equals(facade.getClass().getName()))
          object = facade;
        // se é um Business Object...
        else if (isBusinessObject)
          object = facade.getBusinessObject(className);
        // se não é um Business Object...
        else {
          // instancia
          object = Class.forName(className).newInstance();
          // se tem um Método setFacade()...chama
          Method setFacadeMethod = object.getClass().getMethod("setFacade", new Class[]{facade.getClass()});
          if (setFacadeMethod != null)
            setFacadeMethod.invoke(object, new Object[]{facade});
        } // if
        // se não encontramos o objeto...exceção
        if (object == null)
          throw new ExtendedException(getClass().getName(), "doPost", "Objeto não encontrado: " + className + ".");

        // obtém o método no objeto de negócio que iremos executar
        Class[] methodTypes = new Class[parameterTypes.length];
        for (int i=0; i<methodTypes.length; i++) {
          methodTypes[i] = PARAMETER_TYPES[parameterTypes[i]];
          if (methodTypes[i].isAssignableFrom(Boolean.class))
            methodTypes[i] = boolean.class;
          else if (methodTypes[i].isAssignableFrom(Integer.class))
            methodTypes[i] = int.class;
          else if (methodTypes[i].isAssignableFrom(Double.class))
            methodTypes[i] = double.class;
        } // for
        Method objectMethod = object.getClass().getMethod(methodName, methodTypes);
        // transforma os argumentos em suas respectivas classes
        Object[] methodArgs = getMethodArgs(request, parameterValues, parameterTypes);

        try {
          // invoca o método e guarda o resultado
          result = objectMethod.invoke(object, methodArgs);
        }
        catch (InvocationTargetException e) {
          // repassa a exceção original da classe RemoteException
          throw (Exception)e.getTargetException();
        } // try-catch
      } // if

      // se não temos o que enviar de volta...
      if (result == null) {
        response.setContentType(HTTP_HEADER_CONTENT_TYPE_TEXT);
        response.setStatus(response.SC_OK);
      }
      // se o resultado é um AjaxBean...envia o XML
      else if (result instanceof AjaxBean) {
        response.setContentType(HTTP_HEADER_CONTENT_TYPE_XML);
        response.setStatus(response.SC_OK);
        response.getOutputStream().print(XML_TAG);
        response.getOutputStream().print(((AjaxBean)result).toXML());
      }
      // em outro caso...envia na forma de texto
      else {
        response.setContentType(HTTP_HEADER_CONTENT_TYPE_TEXT);
        response.setStatus(response.SC_OK);
        if (result instanceof String)
          response.getOutputStream().print((String)result);
        else
          response.getOutputStream().print(result.toString());
      } // if

    }
    catch (Exception e) {
      // envia a mensagem de exceção
      String message = ExtendedException.extractMessage(e);
      String source = ExtendedException.extractClassName(e);
             source += (!source.equals("") ? "." : "") + ExtendedException.extractMethodName(e);
      String exceptionClassName = ExtendedException.extractExceptionClassName(e);
      response.reset();
      response.setStatus(response.SC_INTERNAL_SERVER_ERROR);
      response.addHeader(LAST_EXCEPTION, "Exceção: " + message
                                       + (!source.equals("") ? "\r\nOrigem: " + source : "")
                                       + (!exceptionClassName.equals("") ? "\r\nTipo: " + exceptionClassName : ""));
      response.setContentType(HTTP_HEADER_CONTENT_TYPE_TEXT);
      response.getOutputStream().print("Exceção: " + message
                                     + (!source.equals("") ? "\r\nOrigem: " + source : "")
                                     + (!exceptionClassName.equals("") ? "\r\nTipo: " + exceptionClassName : ""));
    } // try-catch
  }

  private Object[] getMethodArgs(HttpServletRequest request,
                                 String[]           parameterValues,
                                 int[]              parameterTypes) throws Exception {
    // nosso resultado
    Object[] result = new Object[parameterValues.length];
    // loop nos valores
    for (int i=0; i<parameterValues.length; i++) {
      // verifica qual o tipo de dado esperado
      switch (parameterTypes[i]) {
        case PARAMETER_TYPE_BOOLEAN      : result[i] = new Boolean(parameterValues[i]); break;
        case PARAMETER_TYPE_DATE         : result[i] = DateTools.parseDate(parameterValues[i]); break;
        case PARAMETER_TYPE_DATE_TIME    : result[i] = DateTools.parseDateTime(parameterValues[i]); break;
        case PARAMETER_TYPE_DOUBLE       : result[i] = new Double(NumberTools.parseDouble(parameterValues[i])); break;
        case PARAMETER_TYPE_INTEGER      : result[i] = new Integer(NumberTools.parseInt(parameterValues[i])); break;
        case PARAMETER_TYPE_STRING       : result[i] = parameterValues[i]; break;
        case PARAMETER_TYPE_TIMESTAMP    : result[i] = DateTools.parseDateTime(parameterValues[i]); break;
        case PARAMETER_TYPE_STRING_ARRAY : result[i] = request.getParameterValues(parameterValues[i]); break;
        case PARAMETER_TYPE_INTEGER_ARRAY: result[i] = NumberTools.parseIntArray(request.getParameterValues(parameterValues[i])); break;
        case PARAMETER_TYPE_DOUBLE_ARRAY : result[i] = NumberTools.parseDoubleArray(request.getParameterValues(parameterValues[i])); break;
      } // switch
    } // for
    // retorna
    return result;
  }

}
