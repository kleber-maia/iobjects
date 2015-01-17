<%@page import="iobjects.servlet.*"%>
<%@page import="iobjects.util.*"%>

<%--
  Esta página é o ponto de entrada padrão da aplicação de acordo com a
  configuração realizada no deployment descriptor. Sua responsabilidade é
  verificar o tipo de cliente que realizou a requisição, se ele é um cliente
  compatível e redirecionar a requisição para o recurso default ideal.
--%>

<%
  try {
      // redireciona diretamente para o Controller
      HttpTools.forward("controller", pageContext);
      // dispara
      return;
/*    // nome e versão do browser
    String userAgentName    = Controller.getUserAgentName(request);
    double userAgentVersion = Controller.getUserAgentVersion(request);
    // se é o Tomcat 5 "bulinando"...dispara
    if (userAgentName.equals("") || (userAgentVersion == 0)) {
      return;
    }
    // se não é o Tomcat...redireciona diretamente para o Controller
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
