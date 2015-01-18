
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
        <td class="Title" valign="top">Novidades</td>
      </tr>
    </table>

    <%// loop nas versões...
       for (int i=0; i<facade.releaseNotes().releaseList().length; i++) {
         // versão da vez
         ReleaseNotesFile.Release release = facade.releaseNotes().releaseList()[i];%>
           <p class="Title1"><%=release.getLabel()%> em <%=DateTools.formatDate(release.getDate())%></p>
           <%// se não temos mudanças...
           if (release.changeList().size() == 0) {%>
             <p>Nenhuma mudança encontrada.</p>
           <%}
             else {%>
           <table>
           <%// loop nas mudanças
             for (int w=0; w<release.changeList().size(); w++) {
               // mudança da vez
               ReleaseNotesFile.Change change = release.changeList().get(w);
               // Action relacionado
               Action action = facade.actionList().get(change.getActionName(), false);
               // se não encontramos...continua
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
