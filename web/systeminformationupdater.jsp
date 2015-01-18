
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
<%@include file="include/beans.jsp"%>

<%@page import="java.io.*"%>
<%@page import="java.sql.*"%>
<%@page import="java.util.*"%>

<%@page import="iobjects.xml.sql.*"%>
<%@page import="iobjects.xml.*"%>

<%
  try {
    // arquivo de configuração do atualizador
    UpdaterFile updaterFile = new UpdaterFile(facade.configurationLocalPath());

%>

<html>
  <head>
    <title><%=facade.applicationInformation().getName()%> - Informações do Sistema</title>
    <link href="<%=facade.getStyle().userInterface()%>" rel="stylesheet" type="text/css">
    <link href="<%=facade.iconURL()%>" rel="shortcut icon" />
    <script src="include/scripts.js" type="text/javascript"></script>
  </head>

  <body style="margin:0px;" onselectstart="return false;" oncontextmenu="return false;">

    <%
      Grid gridVariables = new Grid(facade, "gridVariables", 0, 0);
      gridVariables.columns().add(new Grid.Column("Propriedade", 250));
      gridVariables.columns().add(new Grid.Column("Valor", 500));
    %>
    <%=gridVariables.begin()%>
      <!-- Updater -->
      <%=gridVariables.addRow(new String[]{"<b>Updater</b>", ""})%>
            <!-- Repository -->
            <%=gridVariables.addRow(new String[]{"&nbsp;&nbsp;&nbsp;<b>Repository</b>", ""})%>
                  <!-- URL -->
                  <%=gridVariables.addRow(new String[]{"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;URL", updaterFile.repositoryURL()})%>
            <!-- Installed-->
            <%=gridVariables.addRow(new String[]{"&nbsp;&nbsp;&nbsp;<b>Installed</b>", ""})%>
                  <%RepositoryFile.Update[] updateList = updaterFile.installedUpdateList();
                    for (int i=updateList.length-1; i>=0; i--) {
                      RepositoryFile.Update update = updateList[i];%>
                    <!-- Update -->
                    <%=gridVariables.addRow(new String[]{"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>" + DateTools.formatDate(update.getDate()) + "</b>", "<a href=javascript:alert('Ainda não implementado.')>Reinstalar...</a>"})%>
                    <%=gridVariables.addRow(new String[]{"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Type", RepositoryFile.TYPES[update.getType()]})%>
                    <%=gridVariables.addRow(new String[]{"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Packages", update.getApplicationPackage() + " - " + update.getWorkPackage()})%>
                  <%} // for%>
                  <%if (updateList.length == 0) {%>
                    <%=gridVariables.addRow(new String[]{"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Nenhuma atualização realizada.", ""})%>
                  <%} // if%>
    <%=gridVariables.end()%>

  </body>
</html>

<%}
  catch (Exception e) {
    Controller.forwardException(e, pageContext);
  } // try-catch
%>
