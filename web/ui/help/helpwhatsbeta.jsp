
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

<%@page import="iobjects.ui.report.*"%>
<%@page import="iobjects.xml.ui.*"%>
<%@page import="iobjects.util.*"%>

<%
  try {
%>

<html>
  <head>
    <title><%=facade.applicationInformation().getName() + " - Ajuda"%></title>
    <link href="<%=facade.getStyle().reportInterface()%>" rel="stylesheet" type="text/css">
    <script src="include/scripts.js" type="text/javascript"></script>
  </head>

  <body style="margin:5px;" onselectstart="return true;" oncontextmenu="return false;">

    <!-- Título -->
    <table style="width:100%;">
      <tr>
        <td class="Title" valign="top">Fase beta</td>
      </tr>
    </table>

    <p>
      Um objeto em fase <b>beta</b> indica que seu desenvolvimento e/ou seus
      testes ainda não foram completamente finalizados, contudo já se encontra
      em um nível aceitável de estabilidade e confiabilidade.
    </p>
    <p>
      A liberação de funcionalidades nesta fase possibilita que os usuários tenham
      acesso às novidades com maior antecedência e auxiliem a equipe de desenvolvimento
      a localizar possíveis defeitos e inconsistências.
    </p>
    <p>
      Tais funcionalidades devem ser utilizadas com atenção. Os usuários
      devem estar cientes de que, embora seu uso esteja sujeito aos mesmos termos
      gerais de garantia da aplicação, podem ocorrer erros inesperados e imprecisão
      nas informações apresentadas.
    </p>

    <!-- Título -->
    <p class="Title1">Objetos em fase beta nesta versão</p>

    <ul>
    <%// mudanças recentes
      ReleaseNotesFile.ChangeList changeList = facade.releaseNotes().mostRecentRelease().changeList();
      // loop nas mudanças
      for (int i=0; i<changeList.size(); i++) {
        // mudança da vez
        ReleaseNotesFile.Change change = changeList.get(i);
        // se não é beta...continua
        if (!change.isBeta())
          continue;
        // Action relacionado
        Action action = facade.actionList().get(change.getActionName(), false);
        // se não encontramos...continua
        if (action == null)
          continue;%>
      <li><%=action.getCaption()%></li>
      <%} // for w%>
    </ul>

  </body>
</html>

<%}
  catch (Exception e) {
    Controller.forwardException(e, pageContext);
  } // try-catch
%>
