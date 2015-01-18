
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
