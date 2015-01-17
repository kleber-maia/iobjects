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
