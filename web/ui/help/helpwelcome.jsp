<%@include file="../../include/beans.jsp"%>

<%@page import="iobjects.ui.report.*"%>

<%
  HelpManager helpManager = facade.helpManager();
%>

<html>
  <head>
    <title><%=facade.applicationInformation().getName() + " - Ajuda"%></title>
    <link href="<%=facade.getStyle().reportInterface()%>" rel="stylesheet" type="text/css">
    <script src="include/scripts.js" type="text/javascript"></script>
  </head>

  <body style="margin:5px;" onselectstart="return true;" oncontextmenu="return false;">

    <!-- t�tulo -->
    <table>
      <tr>
        <td class="Title"><%=facade.applicationInformation().getName()%></td>
      </tr>
    </table>

    <!-- Bem vindo -->
    <p>
      Bem vindo a central de ajuda e suporte da aplica��o. Selecione na �rvore
      ao lado o objeto para o qual deseja obter
      ajuda<%=helpManager.faqList().size() == 0 ? "." : " ou verifique sua d�vida na lista de perguntas freq�entes abaixo."%>
    </p>

    <%// se temos FAQ's...
      if (helpManager.faqList().size() > 0) {
        // m�dulos dos FAQ's
        String[] modules = helpManager.faqList().getModuleNames();
        // loop nos m�dulos
        for (int i=0; i<modules.length; i++) {
          // m�dulo da vez
          String module = modules[i];%>

          <p class="Title1"><%=module%></p>

          <ol>
          <%// loop nos FAQ's do m�dulo
            FAQList moduleFAQList = helpManager.faqList().getModuleFAQList(module);
            for (int w=0; w<moduleFAQList.size(); w++) {
              // FAQ da vez
              FAQ faq = moduleFAQList.get(w);%>
              <li><a href="<%=Controller.ACTION_HELP_FAQ.url(HelpManager.FAQ_NAME + "=" + faq.getName())%>"><%=faq.getQuestion()%></a></li>
              <%if (!faq.getPresentationFileName().equals("")) {%>
              <img src="images/presentation16x16.png" align="absmiddle" title="Possui apresenta��o." />
              <%} // if%>
          <%} // for w%>
          </ol>

    <%  } // for i
      } // if%>

  </body>
</html>
