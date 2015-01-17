<%@include file="include/beans.jsp"%>

<%@page import="java.io.*"%>
<%@page import="java.sql.*"%>
<%@page import="java.util.*"%>

<%@page import="iobjects.xml.sql.*"%>

<%
  // nosso FrameBar
  FrameBar frameBar = new FrameBar(facade, "frameBarSystemInformation");
%>

<html>
  <head>
    <title>Informações do Sistema</title>
    <link href="<%=facade.getStyle().userInterface()%>" rel="stylesheet" type="text/css">
    <link href="<%=facade.iconURL()%>" rel="shortcut icon" />
    <script src="include/scripts.js" type="text/javascript"></script>
  </head>

  <body style="margin: 0px;" onselectstart="return false;" oncontextmenu="return false;">

  <!-- nossoo FrameBar -->
  <%=frameBar.begin()%>
    <%=frameBar.beginFrameArea()%>
      <!-- frame de identificação -->
      <%=frameBar.beginFrame("Informações do Sistema", false, false)%>
        As informações do sistema são ferramentas para sua administração.
      <%=frameBar.endFrame()%>
      <!-- frame de informações -->
      <%=frameBar.beginFrame("Informações", false, false)%>
        <table>
          <tr>
            <td><a href="<%=Controller.ACTION_SYSTEM_INFORMATION_SESSIONS.url()%>" target="frameInformation" title="Informações de sessões e usuários.">Sessões</a></td>
          </tr>
          <tr>
            <td><a href="<%=Controller.ACTION_SYSTEM_INFORMATION_LOG.url()%>" target="frameInformation" title="Visualiza o log da aplicação.">Logs</a></td>
          </tr>
          <tr>
            <td><a href="<%=Controller.ACTION_SYSTEM_INFORMATION_EXTENSIONS.url()%>" target="frameInformation" title="Informações de extensões de objetos.">Extensões</a></td>
          </tr>
        </table>
      <%=frameBar.endFrame()%>
      <!-- frame de ferramentas -->
      <%=frameBar.beginFrame("Ferramentas", false, false)%>
        <table>
          <tr>
            <td><a href="<%=Controller.ACTION_SYSTEM_INFORMATION_SCHEDULE.url()%>" target="frameInformation" title="Informações e ferramentas do agendador de tarefas da aplicação.">Agendador</a></td>
          </tr>
          <tr>
            <td><a href="<%=Controller.ACTION_SYSTEM_INFORMATION_UPDATER.url()%>" target="frameInformation" title="Informações e ferramentas do atualizador da aplicação.">Atualizador</a></td>
          </tr>
          <tr>
            <td><a href="<%=Controller.ACTION_SYSTEM_INFORMATION_FILES.url()%>" target="frameInformation" title="Gerenciador de arquivos de sistema">Arquivos de Sistema</a></td>
          </tr>
          <tr>
            <td><a href="<%=Controller.ACTION_SYSTEM_INFORMATION_DATABASES.url()%>" target="frameInformation" title="Gerenciador de bases de dados do sistema">Bancos de dados</a></td>
          </tr>
        </table>
      <%=frameBar.endFrame()%>
    <%=frameBar.endFrameArea()%>

    <%=frameBar.beginClientArea()%>
      <iframe src="<%=Controller.ACTION_SYSTEM_INFORMATION_SESSIONS.url()%>" frameborder="0" name="frameInformation" scrolling="no" style="width:100%; height:100%"></iframe>
    <%=frameBar.endClientArea()%>
  <%=frameBar.end()%>

  </body>
</html>
