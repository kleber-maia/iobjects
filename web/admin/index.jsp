<%@page import="iobjects.servlet.*"%>
<%@page import="iobjects.util.*"%>

<%--
  Esta p�gina � o ponto de entrada alternativo da aplica��o.
  Quando o dom�nio informado no browser leva o usu�rio diretamente a um dos
  sites do portal, esta � a forma de acessar a aplica��o.
--%>

<%
  try {
    // nome e vers�o do browser
    String userAgentName    = Controller.getUserAgentName(request);
    double userAgentVersion = Controller.getUserAgentVersion(request);
    // se � o Tomcat 5 "bulinando"...dispara
    if (userAgentName.equals("") || (userAgentVersion == 0)) {
      return;
    }
    // se n�o � o Tomcat...redireciona diretamente para o Controller
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
