
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
  // temos alguma funcionalidade Beta?
  boolean hasBeta = false;
  // *
  iobjects.xml.ui.ReleaseNotesFile.ChangeList changeList = facade.releaseNotes().mostRecentRelease().changeList();
  for (int i=0; i<changeList.size(); i++) {
    if (changeList.get(i).isBeta()) {
      hasBeta = true;
      break;
    } // if
  } // for
%>

<html>
  <head>
    <title><%=facade.applicationInformation().getName() + " - Ajuda"%></title>
    <link href="<%=facade.getStyle().userInterface()%>" rel="stylesheet" type="text/css">
    <script src="include/scripts.js" type="text/javascript"></script>
  </head>

  <script type="text/javascript">

    function executeCommand(command) {
      parent.frameHelpContent.document.execCommand(command);
    }

  </script>

  <body class="ApplicationTop" style="margin:0px;" onselectstart="return false;" oncontextmenu="return false;">

    <!-- imagem e titulo -->
    <table class="ApplicationTop" style="width:100%; height:55px;">
      <tr>
        <td align="left" style="width:auto;">
          <img src="images/help32x32.png" align="absmiddle" alt="" />
          <span style="font-size:14pt;">Centro de ajuda e suporte</span>
        </td>
      </tr>
    </table>

    <!-- barra de ferramentas -->
    <table cellpadding="0" cellspacing="0" style="width:100%; height:25px; border:0px;">
      <tr>
        <td class="ToolbarLeft"></td>
        <td class="Toolbar">
          <%=Button.script(facade,
                           "toolbuttonVoltar",
                           "Voltar",
                           "Mostra o conteúdo anteriormente exibido.",
                           ImageList.COMMAND_BACK,
                           "",
                           Button.KIND_TOOLBUTTON,
                           "width:100px;",
                           "history.back();",
                           false)%>
          <%=Button.script(facade,
                           "toolbuttonWelcome",
                           "Início",
                           "Mostra o conteúdo inicial de ajuda.",
                           ImageList.COMMAND_HELP,
                           "",
                           Button.KIND_TOOLBUTTON,
                           "width:100px;",
                           "parent.frameHelpContent.document.location.href = '" + Controller.ACTION_HELP_WELCOME.url() + "';",
                           false)%>
          <%=Button.script(facade,
                           "toolbuttonWhatsNew",
                           "Novidades",
                           "Mostra as novidades e atualizações entre versões.",
                           "images/release16x16.png",
                           "",
                           Button.KIND_TOOLBUTTON,
                           "width:100px;",
                           "parent.frameHelpContent.document.location.href = '" + Controller.ACTION_HELP_WHATS_NEW.url() + "';",
                           false)%>
          <%// se temos algo em fase beta...
            if (hasBeta) {%>
            <%=Button.script(facade,
                             "toolbuttonBeta",
                             "Fase beta",
                             "Mostra as informações sobre os objetos em fase beta.",
                             "images/beta16x16.png",
                             "",
                             Button.KIND_TOOLBUTTON,
                             "width:100px;",
                             "parent.frameHelpContent.document.location.href = '" + Controller.ACTION_HELP_WHATS_BETA.url() + "';",
                             false)%>
          <%} // if%>
          <%=Button.script(facade,
                           "toolbuttonImprimir",
                           "Imprimir",
                           "Envia o conteúdo exibido para uma impressora.",
                           ImageList.COMMAND_PRINT,
                           "",
                           Button.KIND_TOOLBUTTON,
                           "width:100px;",
                           "executeCommand('print')",
                           false)%>
          <%=Button.script(facade,
                           "toolbuttonCopiar",
                           "Copiar",
                           "Envia o conteúdo exibido para a área de transferência.",
                           ImageList.COMMAND_COPY,
                           "",
                           Button.KIND_TOOLBUTTON,
                           "width:100px;",
                           "executeCommand('selectAll');executeCommand('copy');",
                           false)%>
        </td>
        <td class="ToolbarRight"></td>
      </tr>
    </table>

  </body>
</html>
