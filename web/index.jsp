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
