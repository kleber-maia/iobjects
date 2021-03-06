
<%--
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
--%>
<%@page import="iobjects.servlet.*"%>
<%@page import="iobjects.util.*"%>

<%--
  Esta p�gina � o ponto de entrada padr�o da aplica��o de acordo com a
  configura��o realizada no deployment descriptor. Sua responsabilidade �
  verificar o tipo de cliente que realizou a requisi��o, se ele � um cliente
  compat�vel e redirecionar a requisi��o para o recurso default ideal.
--%>

<%
  try {
      // redireciona diretamente para o Controller
      HttpTools.forward("controller", pageContext);
      // dispara
      return;
/*    // nome e vers�o do browser
    String userAgentName    = Controller.getUserAgentName(request);
    double userAgentVersion = Controller.getUserAgentVersion(request);
    // se � o Tomcat 5 "bulinando"...dispara
    if (userAgentName.equals("") || (userAgentVersion == 0)) {
      return;
    }
    // se n�o � o Tomcat...redireciona diretamente para o Controller
    else {
      // redireciona diretamente para o Controller
      HttpTools.forward("controller", pageContext);
      // dispara
      return;
    } // if
*/
  }
  catch (Exception e) {
    e.printStackTrace();
    Controller.forwardException(e, pageContext);
  } // try-catch
%>
