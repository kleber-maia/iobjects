
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
  Esta página é o ponto de entrada alternativo da aplicação.
  Quando o domínio informado no browser leva o usuário diretamente a um dos
  sites do portal, esta é a forma de acessar a aplicação.
--%>

<%
  try {
    // nome e versão do browser
    String userAgentName    = Controller.getUserAgentName(request);
    double userAgentVersion = Controller.getUserAgentVersion(request);
    // se é o Tomcat 5 "bulinando"...dispara
    if (userAgentName.equals("") || (userAgentVersion == 0)) {
      return;
    }
    // se não é o Tomcat...redireciona diretamente para o Controller
    else {%>
      <script type="text/javascript">
        window.location.href = "../controller?<%=Controller.ACTION + "=" + Controller.ACTION_HOME.getName()%>";
      </script>
  <%} // if
  }
  catch (Exception e) {
    Controller.forwardException(e, pageContext);
  } // try-catch
%>
