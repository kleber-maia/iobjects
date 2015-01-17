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
