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
        <td class="Title" valign="top">Novidades</td>
      </tr>
    </table>

    <%// loop nas vers�es...
       for (int i=0; i<facade.releaseNotes().releaseList().length; i++) {
         // vers�o da vez
         ReleaseNotesFile.Release release = facade.releaseNotes().releaseList()[i];%>
           <p class="Title1"><%=release.getLabel()%> em <%=DateTools.formatDate(release.getDate())%></p>
           <%// se n�o temos mudan�as...
           if (release.changeList().size() == 0) {%>
             <p>Nenhuma mudan�a encontrada.</p>
           <%}
             else {%>
           <table>
           <%// loop nas mudan�as
             for (int w=0; w<release.changeList().size(); w++) {
               // mudan�a da vez
               ReleaseNotesFile.Change change = release.changeList().get(w);
               // Action relacionado
               Action action = facade.actionList().get(change.getActionName(), false);
               // se n�o encontramos...continua
               if (action == null)
                 continue;%>
             <tr>
               <td nowrap="nowrap" colspan="2">
                 <%=action.getCaption()%>
                 <%if (action.getBeta()) {%>(<b>Beta</b>)<%} // if%>
               </td>
             </tr>
             <tr>
               <td style="width:16px;"></td>
               <td><%=change.getDescription()%></td>
             </tr>
           <%} // for w%>
           </table>
         <%} // if
       } // for i
    %>

  </body>
</html>

<%}
  catch (Exception e) {
    Controller.forwardException(e, pageContext);
  } // try-catch
%>
