<%@include file="../../include/beans.jsp"%>

<%@page import="iobjects.ui.report.*"%>
<%@page import="iobjects.util.*"%>

<%
  try {
    // nossos parâmetros
    String faqName = request.getParameter(HelpManager.FAQ_NAME);
    // se não recebemos um FAQ...exceção
    if ((faqName == null) || (faqName.equals("")))
      throw new Exception("Nenhum tópico de ajuda para exibir.");

    // nosso Faq
    FAQ faq = facade.helpManager().faqList().get(faqName);
    // se não temos resposta e temos nome do arquivo...
    if (faq.getAnswer().equals("") && !faq.getAnswerFileName().equals(""))
      // carrega a resposta e guarda na propriedade
      faq.setAnswer(StringTools.arrayStringToString(FileTools.loadTextFile(faq.getAnswerFileName()), " "));
%>

<html>
  <head>
    <title><%=facade.applicationInformation().getName() + " - Ajuda"%></title>
    <link href="<%=facade.getStyle().reportInterface()%>" rel="stylesheet" type="text/css">
    <script src="include/scripts.js" type="text/javascript"></script>
  </head>

  <body style="margin:5px;" onselectstart="return true;" oncontextmenu="return false;">

    <!-- pergunta -->
    <table style="width:100%;">
      <tr>
        <td class="Title" valign="top"><%=faq.getQuestion()%></td>
        <!-- apresentação -->
        <%if (!faq.getPresentationFileName().equals("")) {%>
        <td align="right">
          <a href="<%=faq.getPresentationFileName()%>" target="__blank">
            <center>
              <img src="images/presentation32x32.png" border="0" /><br />Apresentação
            </center>
          </a>
        </td>
        <%} // if%>
      </tr>
    </table>


    <!-- resposta -->
    <p>
      <%=faq.getAnswer()%>
    </p>

    <%// se temos FAQ's relacionados...
       if (faq.seeAlsoFAQList().size() > 0) {%>
    <!-- FAQ's -->
    <p class="Title1">Veja também</p>
    <ul>
    <%// loop nos Faqs aninhados
      for (int i=0; i<faq.seeAlsoFAQList().size(); i++) {
        // FAQ da Vez
        FAQ nestedFAQ = faq.seeAlsoFAQList().get(i);%>
        <li><a href="<%=Controller.ACTION_HELP_FAQ.url(HelpManager.FAQ_NAME + "=" + faq.getName())%>"><%=faq.getQuestion()%></a></li>
        <%if (!faq.getPresentationFileName().equals("")) {%>
        <img src="images/presentation16x16.png" align="absmiddle" title="Possui apresentação." />
        <%} // if%>
    <%} // for%>
    </ul>
    <%} // if%>

  </body>
</html>

<%}
  catch (Exception e) {
    Controller.forwardException(e, pageContext);
  } // try-catch
%>
