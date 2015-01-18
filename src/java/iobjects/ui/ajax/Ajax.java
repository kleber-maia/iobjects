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
package iobjects.ui.ajax;

import javax.servlet.http.*;

import iobjects.*;
import iobjects.servlet.*;
import iobjects.util.*;

/**
 * Utilitário para utilização das rotinas de comunicação e manipulação de XML
 * através do modelo Ajax.
 */
public class Ajax {

  static public final int PARAMETER_TYPE_BOOLEAN   = AjaxServer.PARAMETER_TYPE_BOOLEAN;
  static public final int PARAMETER_TYPE_DATE      = AjaxServer.PARAMETER_TYPE_DATE;
  static public final int PARAMETER_TYPE_DATE_TIME = AjaxServer.PARAMETER_TYPE_DATE_TIME;
  static public final int PARAMETER_TYPE_DOUBLE    = AjaxServer.PARAMETER_TYPE_DOUBLE;
  static public final int PARAMETER_TYPE_INTEGER   = AjaxServer.PARAMETER_TYPE_INTEGER;
  static public final int PARAMETER_TYPE_STRING    = AjaxServer.PARAMETER_TYPE_STRING;
  static public final int PARAMETER_TYPE_TIMESTAMP = AjaxServer.PARAMETER_TYPE_TIMESTAMP;

  static public final String HTTP_HEADER_CONTENT_TYPE_TEXT = "text/plain;charset=ISO-8859-1";
  static public final String HTTP_HEADER_CONTENT_TYPE_XML  = "text/xml;charset=ISO-8859-1";
  
  private Facade  facade           = null;
  private String  id               = "";
  private String  action           = "";
  private String  command          = "";
  private String  className        = "";
  private boolean isBusinessObject = false;
  private String  methodName       = "";
  private int[]   parameterTypes   = {};

  /**
   * Cosntrutor padrão. <b>Utilize este construtor para chamar métodos diretamente
   * de objetos de negócios da aplicação.</b>
   * @param facade Facade Fachada.
   * @param id String Identificação do objeto na página.
   * @param className String Nome da classe no servidor que será instanciada
   *                         para execução da requisição Ajax. Deve estar
   *                         no formato "package.ClassName". <b>A classe indicada
   *                         deve ser descendente de BusinessObject ou qualquer
   *                         outra classe, mas desde que possua um contrutor
   *                         default sem parâmetros.</b>
   * @param isBusinessObject boolean True informa que a classe identificada por
   *                         'className' é descendente de BusinessObject.
   * @param methodName String Nome do método (sem ()) na classe indicada que
   *                          será executado pela requisição Ajax.
   * @param parameterTypes int[] Tipos dos parâmetros do método informado, indicando
   *                             exatamente sua assinatura. <b>Os tipos dos
   *                             parâmetros devem ser objetos e não tipos nativos.
   *                             Ex.: Integer, Boolean, Double, String, etc...</b>
   */
  public Ajax(Facade   facade,
              String   id,
              String   className,
              boolean  isBusinessObject,
              String   methodName,
              int[]    parameterTypes) {
    // nossos valores
    this.facade = facade;
    this.id = id;
    this.className = className;
    this.isBusinessObject = isBusinessObject;
    this.methodName = methodName;
    this.parameterTypes = parameterTypes;
  }

  /**
   * Cosntrutor estendido. <b>Utilize este construtor para executar comandos
   * em páginas da aplicação.</b>
   * @param facade Facade Fachada.
   * @param id String Identificação do objeto na página.
   * @param action Action Ação de destino que da requisição para ser executada pelo Controller.
   * @param command Command Comando para ser executado na ação de destino.
   */
  public Ajax(Facade facade,
              String id,
              String action,
              String command) {
    // nossos valores
    this.facade = facade;
    this.id = id;
    this.action = action;
    this.command = command;
  }

  /**
   * Redireciona para a página de tratamento de exceção.
   * <p>Exemplo</p>
   * <pre>
   *   &lt;script type="text/javascript"&gt;
   *     if (&lt;%=myAjax.isResponseStatusError()%&gt;)
   *       &lt;%=myAjax.forwardException()%&gt;;
   *   &lt;/script&gt;
   * </pre>
   */
  static public String forwardException() {
    return "Ajax_forwardException();";
  }

  /**
   * Retorna o código em JavaScript que retornará o valor do cabeçalho da resposta
   * da requisição Ajax indicado por 'header'.
   * <p>Exemplo</p>
   * <pre>
   *   &lt;script type="text/javascript"&gt;
   *     var headerValue = &lt;%=myAjax.getResponseHeader("paramName")%&gt;;
   *   &lt;/script&gt;
   * </pre>
   * @return String
   */
  public String getResponseHeader(String header) {
    return "Ajax_getResponseHeader(" + id + ", '" + header + "')";
  }
  
  /**
   * Retorna o ID da requisição Ajax. Esse é o valor utilizado no parâmetro
   * 'request' das funções Ajax em JavaScript.
   * @return String
   */
  public String id() {
    return id;
  }

  /**
   * Retorna o código em JavaScript que retornará true ou false indicando se
   * o status da resposta do servidor para a requisição Ajax foi uma exceção.
   * <p>Exemplo 1</p>
   * <pre>
   *   &lt;script type="text/javascript"&gt;
   *     var responseStatusOK = &lt;%=myAjax.isResponseStatusError()%&gt;;
   *   &lt;/script&gt;
   * </pre>
   * <p>Exemplo 2</p>
   * <pre>
   *   &lt;script type="text/javascript"&gt;
   *     if (&lt;%=myAjax.isResponseStatusError()%&gt;)
   *       alert(&lt;%=myAjax.responseErrorMessage()%&gt;);
   *   &lt;/script&gt;
   * </pre>
   * <p>Exemplo 3</p>
   * <pre>
   *   &lt;script type="text/javascript"&gt;
   *     if (&lt;%=myAjax.isResponseStatusError()%&gt;)
   *       &lt;%=myAjax.forwardException()%&gt;;
   *   &lt;/script&gt;
   * </pre>
   * @return String
   */
  public String isResponseStatusError() {
    return "Ajax_isResponseStatusError(" + id + ")";
  }

  /**
   * Retorna o código em JavaScript que retornará true ou false indicando se
   * o status da resposta do servidor para a requisição Ajax foi OK.
   * <p>Exemplo 1</p>
   * <pre>
   *   &lt;script type="text/javascript"&gt;
   *     var responseStatusOK = &lt;%=myAjax.isResponseStatusOK()%&gt;;
   *   &lt;/script&gt;
   * </pre>
   * <p>Exemplo 2</p>
   * <pre>
   *   &lt;script type="text/javascript"&gt;
   *     if (&lt;%=myAjax.isResponseStatusOK()%&gt;)
   *       // use os métodos responseText() ou responseXML() para obter o conteúdo.
   *   &lt;/script&gt;
   * </pre>
   * @return String
   */
  public String isResponseStatusOK() {
    return "Ajax_isResponseStatusOK(" + id + ")";
  }

  /**
   * Retorna o código em JavaScript que retornará true ou false indicando se
   * a resposta do servidor para a requisição Ajax já está pronta para ser manipulada.
   * <p>Exemplo 1</p>
   * <pre>
   *   &lt;script type="text/javascript"&gt;
   *     var responseReady = &lt;%=myAjax.isResponseReady()%&gt;;
   *   &lt;/script&gt;
   * </pre>
   * <p>Exemplo 2</p>
   * <pre>
   *   &lt;script type="text/javascript"&gt;
   *     if (!&lt;%=myAjax.isResponseReady()%&gt;)
   *       return;
   *   &lt;/script&gt;
   * </pre>
   * @return String
   */
  public String isResponseReady() {
    return "Ajax_isResponseReady(" + id + ")";
  }

  /**
   * Retorna o código em JavaScript que retornará true ou false indicando se
   * a resposta do servidor para a requisição Ajax é do tipo Texto.
   * <p>Exemplo 1</p>
   * <pre>
   *   &lt;script type="text/javascript"&gt;
   *     var responseTypeText = &lt;%=myAjax.isResponseTypeText()%&gt;;
   *   &lt;/script&gt;
   * </pre>
   * <p>Exemplo 2</p>
   * <pre>
   *   &lt;script type="text/javascript"&gt;
   *     alert("Reposta é texto: " + &lt;%=myAjax.isResponseTypeText()%&gt;);
   *   &lt;/script&gt;
   * </pre>
   * @return String
   */
  public String isResponseTypeText() {
    return "Ajax_isResponseTypeText(" + id + ")";
  }

  /**
   * Retorna o código em JavaScript que retornará true ou false indicando se
   * a resposta do servidor para a requisição Ajax é do tipo XML.
   * <p>Exemplo 1</p>
   * <pre>
   *   &lt;script type="text/javascript"&gt;
   *     var responseTypeXML = &lt;%=myAjax.isResponseTypeXML()%&gt;;
   *   &lt;/script&gt;
   * </pre>
   * <p>Exemplo 2</p>
   * <pre>
   *   &lt;script type="text/javascript"&gt;
   *     alert("Reposta é XML: " + &lt;%=myAjax.isResponseTypeXML()%&gt;);
   *   &lt;/script&gt;
   * </pre>
   * @return String
   */
  public String isResponseTypeXML() {
    return "Ajax_isResponseTypeXML(" + id + ")";
  }

  /**
   * Retorna o código em JavaScript que realiza a requisição Ajax para o servidor.
   * <b>A requisição será síncrona, ou seja, a execução do JavaScript somente
   * continuará após a resposta do servidor.</b>
   * <p>Veja exemplo em request(String[] parameterValues, String callbackFunction)</p>
   * @return String Retorna o código em JavaScript que realiza a requisição Ajax
   *                para o servidor.
   */
  public String request() {
    return request(new String[]{}, "");
  }

  /**
   * Retorna o código em JavaScript que realiza a requisição Ajax para o servidor.
   * <b>A requisição será síncrona, ou seja, a execução do JavaScript somente
   * continuará após a resposta do servidor.</b>
   * <p>Veja exemplo em request(String[] parameterValues, String callbackFunction)</p>
   * @param parameterValues String[] Valores (entre '') ou nomes das variáveis
   *                                 locais em JavaScript que serão passados como
   *                                 argumentos para o método que será executado
   *                                 no objeto indicado.
   *                                 <b>Se estiver realizando uma chamada a um Action
   *                                 da aplicação, os parâmetros devem ser no formato
   *                                 'paramName=paramValue'.</b>
   * @return String Retorna o código em JavaScript que realiza a requisição Ajax
   *                para o servidor.
   */
  public String request(String[] parameterValues) {
    return request(parameterValues, "");
  }

  /**
   * Retorna o código em JavaScript que realiza a requisição Ajax para o servidor.
   * <b>A requisição será assíncrona, ou seja, a execução do JavaScript continuará
   * normalmente independente da resposta do servidor.</b>
   * <p>Exemplo:</p>
   * <pre>
   *     &lt;%
   *       // declara um objeto Ajax
   *       Ajax myAjax = new Ajax(facade, "myAjax", "package.ClassName", "methodName", new int[]{Ajax.PARAMETER_TYPE_STRING, Ajax.PARAMETER_TYPE_INT});
   *     %&gt;
   *     &lt;%=Ajax.link()%&gt;
   *     &lt;script type="text/javascript"&gt;
   *
   *       // variáveis para serem alimentadas pelas rotinas da página
   *       var param1 = "myBalloon";
   *       var param2 = 123456;
   *
   *       function myAjaxTest() {
   *         // faz a requisição
   *         &lt;%=myAjax.request(new String[]{"param1", "param2"}, "myAjaxCallback")%&gt;
   *         // código independente da resposta do servidor
   *         // ...
   *       }
   *
   *       function myAjaxCallback() {
   *         // se ainda não recebemos a resposta...dispara
   *         if (!&lt;%=myBalloon.isResponseReady()%&gt;)
   *           return;
   *         // se ocorreu tudo OK...
   *         if (&lt;%=myAjax.isResponseStatusOK()%&gt;)
   *           alert("OK!");
   *         // se ocorreu um erro...
   *         else
   *           &lt;%=myAjax.forwardException()%&gt;;
   *       }
   * </pre>
   * @param parameterValues String[] Valores (entre '') ou nomes das variáveis
   *                                 locais em JavaScript que serão passados como
   *                                 argumentos para o método que será executado
   *                                 no objeto indicado.
   *                                 <b>Se estiver realizando uma chamada a um Action
   *                                 da aplicação, os parâmetros devem ser no formato
   *                                 'paramName=paramValue'.</b>
   * @param callbackFunction String Nome da função local em JavaScript (sem ())
   *                                que será chamada após a chegada da resposta
   *                                do servidor.
   * @return String Retorna o código em JavaScript que realiza a requisição Ajax
   *                para o servidor.
   */
  public String request(String[] parameterValues,
                        String   callbackFunction) {
    // se devemos chamar um BusinessObject...
    if (!className.equals(""))
      return id + " = null; "
           + id + " = "
           + "Ajax_requestForBusinessObject(\"" + className + "\", "
                                          + isBusinessObject + ", "
                                          + "\"" + methodName + "\", "
                                          + "[" + StringTools.arrayStringToString(parameterValues, ", ") + "], "
                                          + ((parameterTypes != null) && (parameterTypes.length > 0)
                                            ? java.util.Arrays.toString(parameterTypes) + ", "
                                            : "null, ")
                                          + (!callbackFunction.equals("")
                                            ? callbackFunction
                                            : "null")
                                          + ");";
    // se devemos chamar um Action...
    else
      return id + " = null; "
           + id + " = "
           + "Ajax_requestForAction(\"" + action + "\", "
                                  + "\"" + command + "\", "
                                  + "[" + StringTools.arrayStringToString(parameterValues, ", ") + "], "
                                  + (!callbackFunction.equals("")
                                    ? callbackFunction
                                    : "null")
                                  + ");";
  }

  /**
   * Retorna o código em JavaScript que retornará a mensagem de erro recebida
   * como resposta do servidor para a requisição Ajax.
   * <p>Exemplo 1</p>
   * <pre>
   *   &lt;script type="text/javascript"&gt;
   *     var errorMessage = &lt;%=myAjax.responseErrorMessage()%&gt;;
   *   &lt;/script&gt;
   * </pre>
   * <p>Exemplo 2</p>
   * <pre>
   *   &lt;script type="text/javascript"&gt;
   *     alert(&lt;%=myAjax.responseErrorMessage()%&gt;);
   *   &lt;/script&gt;
   * </pre>
   * @return String
   */
  public String responseErrorMessage() {
    return "Ajax_responseErrorMessage(" + id + ")";
  }

  /**
   * Retorna o código em JavaScript que retornará a resposta do servidor
   * para a requisição Ajax no formato texto.
   * <p>Exemplo 1</p>
   * <pre>
   *   &lt;script type="text/javascript"&gt;
   *     var result = &lt;%=myAjax.responseText()%&gt;;
   *   &lt;/script&gt;
   * </pre>
   * <p>Exemplo 2</p>
   * <pre>
   *   &lt;script type="text/javascript"&gt;
   *     alert(&lt;%=myAjax.responseText()%&gt;);
   *   &lt;/script&gt;
   * </pre>
   * @return String
   */
  public String responseText() {
    return "Ajax_responseText(" + id + ")";
  }

  /**
   * Retorna o código em JavaScript que retornará a resposta do servidor
   * para a requisição Ajax no formato XML.
   * <p>Exemplo 1</p>
   * <pre>
   *   &lt;script type="text/javascript"&gt;
   *     var result = &lt;%=myAjax.responseXML()%&gt;;
   *   &lt;/script&gt;
   * </pre>
   * <p>Exemplo 2</p>
   * <pre>
   *   &lt;script type="text/javascript"&gt;
   *     alert(&lt;%=myAjax.responseXML()%&gt;.xml);
   *   &lt;/script&gt;
   * </pre>
   * @return String
   */
  public String responseXML() {
    return "Ajax_responseXML(" + id + ")";
  }

  /**
   * Define o tipo de conteúdo da resposta do processamento no lado servidor como 
   * sendo texto.
   * <b>Use este método quando o processamento no lado servidor for realizado
   * diretamente em uma página JSP para assegurar que os resultados serão
   * recebidos e processados corretamente no lado cliente pelas rotinas Ajax.</b>
   * @param response HttpServletResponse
   */
  static public void setResponseTypeText(HttpServletResponse response) {
    // define o status OK
    response.setStatus(HttpServletResponse.SC_OK);
    // define o tipo como texto
    response.setContentType(HTTP_HEADER_CONTENT_TYPE_TEXT);
  }

  /**
   * Define um cabeçalho personalizado na resposta do processamento no lado 
   * servidor.
   * <b>Use este método quando o processamento no lado servidor for realizado
   * diretamente em uma página JSP para assegurar que os resultados serão
   * recebidos e processados corretamente no lado cliente pelas rotinas Ajax.</b>
   * @param response HttpServletResponse
   * @param header String Nome do parâmetro.
   * @param value String Valor do parâmetro.
   */
  static public void setResponseHeader(HttpServletResponse response,
                                       String              header,
                                       String              value) {
    // define se é o conteúdo que está sendo retornado
    response.addHeader(header, value);
  }

}
