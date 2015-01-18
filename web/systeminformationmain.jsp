
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
      Grid gridVariables = new Grid(facade, "gridVariables", true);
      gridVariables.columns().add(new Grid.Column("Propriedade", 250));
      gridVariables.columns().add(new Grid.Column("Valor", 500));
    %>
    <%=gridVariables.begin()%>
      <!-- Application -->
      <%=gridVariables.beginRow()%>
        <%=gridVariables.beginCell()%><b>Application</b><%=gridVariables.endCell()%>
        <%=gridVariables.beginCell()%><%=gridVariables.endCell()%>
      <%=gridVariables.endRow()%>
            <!-- ID -->
            <%=gridVariables.beginRow()%>
              <%=gridVariables.beginCell()%>&nbsp;&nbsp;&nbsp;ID<%=gridVariables.endCell()%>
              <%=gridVariables.beginCell()%><%=facade.applicationId()%><%=gridVariables.endCell()%>
            <%=gridVariables.endRow()%>
            <!-- Host Name -->
            <%=gridVariables.beginRow()%>
              <%=gridVariables.beginCell()%>&nbsp;&nbsp;&nbsp;Host Name<%=gridVariables.endCell()%>
              <%=gridVariables.beginCell()%><%=NetworkInfo.getHostName()%><%=gridVariables.endCell()%>
            <%=gridVariables.endRow()%>
            <!-- Host Address -->
            <%=gridVariables.beginRow()%>
              <%=gridVariables.beginCell()%>&nbsp;&nbsp;&nbsp;Host Address<%=gridVariables.endCell()%>
              <%=gridVariables.beginCell()%><%=NetworkInfo.getIPAddress()%><%=gridVariables.endCell()%>
            <%=gridVariables.endRow()%>
            <!-- Remote URL -->
            <%=gridVariables.beginRow()%>
              <%=gridVariables.beginCell()%>&nbsp;&nbsp;&nbsp;Remote URL<%=gridVariables.endCell()%>
              <%=gridVariables.beginCell()%><%=Controller.getRemoteURL()%><%=gridVariables.endCell()%>
            <%=gridVariables.endRow()%>
    <%=gridVariables.end()%>

  </body>
</html>

<%}
  catch (Exception e) {
    Controller.forwardException(e, pageContext);
  } // try-catch
%>
