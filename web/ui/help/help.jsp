<%@include file="../../include/beans.jsp"%>

<%
  // nossos parâmetros
  String actionName = request.getParameter(HelpManager.ACTION_NAME);
  String faqName    = request.getParameter(HelpManager.FAQ_NAME);
%>

<html>
  <head>
    <title><%=facade.applicationInformation().getName() + " - Centro de ajuda e suporte"%></title>
    <link href="<%=facade.getStyle().userInterface()%>" rel="stylesheet" type="text/css">
    <script src="include/scripts.js" type="text/javascript"></script>
  </head>

  <frameset rows="80,*" framespacing="0" frameborder="no" border="0">
    <frame src="<%=Controller.ACTION_HELP_TOP.url()%>" name="frameHelpTop" scrolling="no" noresize="noresize">
    <frameset id="framesetHelp" cols="200,*" framespacing="1" frameborder="0" border="0">
      <frame src="<%=Controller.ACTION_HELP_TREEVIEW.url((actionName != null ? HelpManager.ACTION_NAME + "=" + actionName : ""))%>"  name="frameHelpTreeView" scrolling="no">
      <frame src="<%=(actionName != null ? Controller.ACTION_HELP_BUSINESS_OBJECT.url(HelpManager.ACTION_NAME + "=" + actionName) : faqName != null ? Controller.ACTION_HELP_FAQ.url(HelpManager.FAQ_NAME + "=" + faqName) : Controller.ACTION_HELP_WELCOME.url())%>" name="frameHelpContent" scrolling="auto" >
    </frameset>
  </frameset>

  <noframes>
    <body onselectstart="return false;" oncontextmenu="return false;">
      Este serviço utiliza frames mas seu browser não tem suporte a este recurso.
    </body>
  </noframes>

</html>
