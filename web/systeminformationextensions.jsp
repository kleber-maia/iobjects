<%@include file="include/beans.jsp"%>

<%@page import="java.io.*"%>
<%@page import="java.sql.*"%>
<%@page import="java.util.*"%>

<%@page import="iobjects.xml.sql.*"%>
<%@page import="iobjects.xml.*"%>

<%
  try {
    int card    = 0;
    int entity  = 0;
    int process = 0;
    int report  = 0;
    int other   = 0;
%>

<html>
  <head>
    <title><%=facade.applicationInformation().getName()%> - Informa��es do Sistema</title>
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
      <%for (int i=0; i<facade.extensionManager().extensions().length; i++) {
          // extens�o da vez
          Extension extension = facade.extensionManager().extensions()[i];
          // acumuladores
          card    += extension.getCardCount();
          entity  += extension.getEntityCount();
          process += extension.getProcessCount();
          report  += extension.getReportCount();
          other   += extension.getOtherCount();
          // arquivo
          File file = new File(extension.getFileName());%>
      <!-- nome do arquivo -->
      <%=gridVariables.addRow(new String[]{"<b>" + extension.getName() + "</b>", DateTools.formatDateTime(new java.util.Date(file.lastModified()))})%>
            <!-- business objects -->
            <%=gridVariables.addRow(new String[]{"&nbsp;&nbsp;&nbsp;Business Objects", extension.getEntityCount() + extension.getCardCount() + extension.getProcessCount() + extension.getReportCount() + ""})%>
                  <!-- cart�es -->
                 <%=gridVariables.addRow(new String[]{"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Card", extension.getCardCount() + ""})%>
                  <!-- entidades -->
                 <%=gridVariables.addRow(new String[]{"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Entity", extension.getEntityCount() + ""})%>
                  <!-- processos -->
                 <%=gridVariables.addRow(new String[]{"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Process", extension.getProcessCount() + ""})%>
                  <!-- relat�rios -->
                 <%=gridVariables.addRow(new String[]{"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Report", extension.getReportCount() + ""})%>
            <!-- outros -->
            <%=gridVariables.addRow(new String[]{"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Outros", extension.getOtherCount() + ""})%>
      <%} // for i%>
      <%=gridVariables.end("Business Objects: " + (entity + card + process + report) + "&nbsp;-&nbsp;" + "Card: " + card + "&nbsp;-&nbsp;" + "Entity: " + entity + "&nbsp;-&nbsp;" + "Process: " + process + "&nbsp;-&nbsp;" + "Report: " + report + "&nbsp;-&nbsp;" + "Outros: " + other)%>

  </body>
</html>

<%}
  catch (Exception e) {
    Controller.forwardException(e, pageContext);
  } // try-catch
%>
