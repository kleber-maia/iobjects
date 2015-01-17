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

    <!-- T�tulo -->
    <table style="width:100%;">
      <tr>
        <td class="Title" valign="top">Fase beta</td>
      </tr>
    </table>

    <p>
      Um objeto em fase <b>beta</b> indica que seu desenvolvimento e/ou seus
      testes ainda n�o foram completamente finalizados, contudo j� se encontra
      em um n�vel aceit�vel de estabilidade e confiabilidade.
    </p>
    <p>
      A libera��o de funcionalidades nesta fase possibilita que os usu�rios tenham
      acesso �s novidades com maior anteced�ncia e auxiliem a equipe de desenvolvimento
      a localizar poss�veis defeitos e inconsist�ncias.
    </p>
    <p>
      Tais funcionalidades devem ser utilizadas com aten��o. Os usu�rios
      devem estar cientes de que, embora seu uso esteja sujeito aos mesmos termos
      gerais de garantia da aplica��o, podem ocorrer erros inesperados e imprecis�o
      nas informa��es apresentadas.
    </p>

    <!-- T�tulo -->
    <p class="Title1">Objetos em fase beta nesta vers�o</p>

    <ul>
    <%// mudan�as recentes
      ReleaseNotesFile.ChangeList changeList = facade.releaseNotes().mostRecentRelease().changeList();
      // loop nas mudan�as
      for (int i=0; i<changeList.size(); i++) {
        // mudan�a da vez
        ReleaseNotesFile.Change change = changeList.get(i);
        // se n�o � beta...continua
        if (!change.isBeta())
          continue;
        // Action relacionado
        Action action = facade.actionList().get(change.getActionName(), false);
        // se n�o encontramos...continua
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
