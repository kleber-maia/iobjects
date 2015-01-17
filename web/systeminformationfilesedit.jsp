<%@include file="include/beans.jsp"%>

<%@page import="java.io.*"%>
<%@page import="java.sql.*"%>
<%@page import="java.util.*"%>

<%!
  String CONTENT   = "content";
  String FILE_NAME = "fileName";
  String SAVE      = "save";

  public String loadFile(String fileName) throws IOException {
    // abre o arquivo para leitura
    FileInputStream fileStream = new FileInputStream(fileName);
    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileStream, "UTF-8"));
    // nosso resultado
    StringBuffer result = new StringBuffer();
    while (true) {
      // linha atual
      String line = bufferedReader.readLine();
      // se acabamos a leitura...dispara
      if (line == null)
        break;
      // adiciona no nosso resultado
      else
        result.append(line + "\r\n");
    } // while
    // retorna
    return result.toString();
  }

  public void saveFile(String fileName, String content) throws IOException {
    // abre o arquivo para escrita
    FileOutputStream fileStream = new FileOutputStream(fileName, false);
    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(fileStream, "UTF-8"));
    bufferedWriter.write(content);
    // salva
    StringBuffer result = new StringBuffer();
    // fecha
    bufferedWriter.close();
    fileStream.close();
  }

%>

<%
  try {
    // nome do arquivo que iremos editar
    String fileName = request.getParameter(FILE_NAME);
    // arquivo associado
    File file = new File(fileName);

    // se devemos salvar o arquivo...salva
    if (request.getParameter(SAVE) != null)
      saveFile(fileName, request.getParameter(CONTENT));

    // carrega o arquivo
    String fileContent = loadFile(fileName);
%>

<html>
  <head>
    <title><%=facade.applicationInformation().getName()%> - <%=file.getParentFile().getName() + "/" + file.getName()%></title>
    <link href="<%=facade.getStyle().userInterface()%>" rel="stylesheet" type="text/css">
    <link href="<%=facade.iconURL()%>" rel="shortcut icon" />
    <script src="include/scripts.js" type="text/javascript"></script>
  </head>

  <body style="margin:0px 0px 0px 0px;" onselectstart="return true;" oncontextmenu="return false;" onload="formEditor.<%=CONTENT%>.focus();">

    <script type="text/javascript">
      var modified = false;

      function cancel() {
        // se o conteúdo foi modificado
        if (modified) {
          // se não quer perder as alterações...dispara
          if (!confirm('Deseja mesmo descartar as alterações?'))
            return;
        } // if
        // fecha a janela
        window.close();
      }

      function saves() {
        // se o conteúdo não foi modificado...dispara
        if (!modified)
          return;
        // salva as alterações
        formEditor.submit();
      }

    </script>

    <form name="formEditor" action="controller" method="POST">
      <input type="hidden" name="<%=Controller.ACTION%>" value="<%=Controller.ACTION_SYSTEM_INFORMATION_FILES_EDIT.getName()%>" />
      <input type="hidden" name="<%=FILE_NAME%>" value="<%=fileName%>" />
      <input type="hidden" name="<%=SAVE%>" value="true" />
      <table cellpadding="4" style="width:100%; height:100%;">
        <!-- editor -->
        <tr style="height:auto;">
          <td>
            <textarea name="<%=CONTENT%>" onchange="modified=true;" style="font-family:courier new; font-size:10pt; width:100%; height:100%;" wrap="off"><%=fileContent%></textarea>
          </td>
        </tr>
        <!-- barra de ferramentas -->
        <tr style="height:20px;">
          <td align="right">
            <%=Button.script(facade, "buttonSalvar", "<u>S</u>alvar", "Salva as alterações.", ImageList.COMMAND_SAVE, "s", Button.KIND_DEFAULT, "width:90px;", "saves();", false)%>&nbsp;&nbsp;&nbsp;
            <%=Button.script(facade, "buttonCancelar", "Cancela<u>r</u>", "Cancela as alterações.", ImageList.COMMAND_DELETE, "r", Button.KIND_DEFAULT, "width:90px;", "cancel();", false)%>
          </td>
        </tr>
      </table>
    </form>

  </body>
</html>

<%
  }
  catch (Exception e) {
    Controller.forwardException(e, pageContext);
  } // try-catch
%>
