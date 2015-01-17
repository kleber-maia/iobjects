<%@ page isErrorPage="true" %>

<%@page import="iobjects.*"%>

<%
    // caminho desejado
    ErrorData ed = pageContext.getErrorData();
    try {
        // informações da sessão
        Facade facade = null;
        Object attr = session.getAttribute("facade");
        if (attr != null)
            facade = (Facade)attr;
        // se temos Facade e estamos em um website...
        if (facade != null && !facade.getDefaultSiteName().isEmpty()) {
            // encaminha a requisição para o site
            String path = ed.getRequestURI();
            if (path.startsWith("/"))
                path = path.substring(1);
            pageContext.forward("/?path=" + path);
            return;
        } // if
        /*
        */
    }
    catch (Exception e) {%>
        <html>
          <head>
            <title>Página não encontrada.</title>
          </head>

          <body>
              <%=e%>
          </body>
        </html>
  <%}
%>

<html>
  <head>
    <title>Página não encontrada.</title>
  </head>

  <body>
      <h1 style="display:inline;">Página não encontrada:</h1>
      <h2 style="display:inline;"><%=ed.getRequestURI()%></h2>
  </body>
</html>
