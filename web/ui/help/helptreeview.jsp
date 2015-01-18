
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
<%@include file="../../include/beans.jsp"%>

<jsp:useBean id="helpTreeview" class="iobjects.ui.treeview.BusinessObjectTreeView" scope="session" />

<%
  // nossos parâmetros
  String actionName = request.getParameter(HelpManager.ACTION_NAME);
  if (actionName == null)
    actionName = "";

  // configura a árvore de objetos
  helpTreeview.setFacade(facade);
  helpTreeview.setAvoidWaitCursor(true);
  helpTreeview.setAutoAlign(true);
  helpTreeview.setActionList(facade.actionList());
  helpTreeview.setId("helpTreeView");
  helpTreeview.setLinkToHelp(true);
  helpTreeview.setShowCommands(false);
  helpTreeview.setShowNestedActions(false);
  helpTreeview.setShowMobileActions(false);
  helpTreeview.setTarget("frameHelpContent");
%>

<html>
  <head>
    <title><%=facade.applicationInformation().getName() + " - Ajuda"%></title>
    <link href="<%=facade.getStyle().userInterface()%>" rel="stylesheet" type="text/css">
    <script src="include/scripts.js" type="text/javascript"></script>
  </head>

  <body class="ApplicationTreeView" style="margin:0px;" onselectstart="return false;" oncontextmenu="return false;">

    <table style="width:100%; height:100%;" cellpadding="0" cellspacing="0">
      <tr style="height:21px;">
        <td class="ApplicationTreeViewHeader">Árvore de objetos</td>
      </tr>
      <tr style="height:auto;">
        <td>
          <%=helpTreeview.script()%>
        </td>
      </tr>
    </table>

  </body>
</html>
