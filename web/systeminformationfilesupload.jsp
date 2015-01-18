
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
<%@include file="include/beans.jsp"%>

<%@page import="java.io.*"%>
<%@page import="java.sql.*"%>
<%@page import="java.util.*"%>

<%!
  String DIRECTORY = "directory";
  String FILE_NAME = "fileName";
  String UPLOAD    = "upload";
%>

<%
  // diretório atual
  String directory = request.getParameter(DIRECTORY);
  if (directory == null)
    directory = "";
  // arquivo atual
  String fileName = request.getParameter(FILE_NAME);
  if (fileName == null)
    fileName = "";

  // comando para realizar
  String command = request.getParameter(Controller.COMMAND);
  if (command == null)
    command = "";
  // se quer fazer upload
  else if (command.equals(UPLOAD)) {
    // avisa sobre o não recebimento do arquivo
    %>
    <script language="javascript" type="">
      alert("O arquivo não foi enviado corretamente.");
      window.close();
    </script>
    <%
    // dispara
    return;
  } // if
%>

<html>
  <head>
    <title><%=facade.applicationInformation().getName()%> - Upload</title>
    <link href="<%=facade.getStyle().userInterface()%>" rel="stylesheet" type="text/css">
    <link href="<%=facade.iconURL()%>" rel="shortcut icon" />
    <script src="include/scripts.js" type="text/javascript"></script>
  </head>

  <script type="text/javascript">

    // evita múltiplas submissões do formulário
    formSubmited = false;

    /**
     * Realiza o upload.
     */
    function upload() {
      // se o form já foi submetido...dispara
      if (formSubmited) {
        alert("Aguarde o término da operação.");
        return;
      } // if
      // se não selecionou um arquivo...dispara
      if (formUpload.<%=FILE_NAME%>.value == "") {
        alert("Selecione o arquivo para upload.");
        return;
      } // if
      // avisa que espere
      document.all.divAlerta.style.visibility = "visible";
      document.all.buttonEnviar.disabled = true;
      document.all.buttonFechar.disabled = true;
      formUpload.<%=FILE_NAME%>.disabled = true;
      // submete o form
      formSubmited = true;
      formUpload.submit();
    }

  </script>

  <body style="margin: 5px 5px 5px 5px;" onselectstart="return false;" oncontextmenu="return false;">

  <!-- form -->
  <form id="formUpload" enctype="multipart/form-data" method="POST" action="controller">
    <input type="hidden" name="<%=Controller.ACTION%>" value="<%=Controller.ACTION_SYSTEM_INFORMATION_FILES_UPLOAD.getName()%>" />
    <input type="hidden" name="<%=Controller.COMMAND%>" value="<%=UPLOAD%>" />
    <input type="hidden" name="<%=DIRECTORY%>" value="<%=directory%>" />

    <table>
      <tr>
        <td colspan="2">
          Selecione o arquivo para ser enviado para "<%=directory%>".
        </td>
      </tr>
      <tr>
        <td colspan="2"><input type="file" name="<%=FILE_NAME%>" style="width:330px;" /></td>
      </tr>
      <tr>
        <td colspan="2">
          Obs.: se o arquivo tiver a extensão ".zip" ele será descompactado
          e seu conteúdo copiado neste diretório.
        </td>
      </tr>
      <tr>
        <td>&nbsp;</td>
      </tr>
      <tr>
        <td>
          <div id="divAlerta" style="color:#FF0000; visibility:hidden;">Fazendo upload...</div>
        </td>
        <td align="right">
          <button id="buttonEnviar" onclick="upload();">Enviar</button>&nbsp;
          <button id="buttonFechar" onclick="window.close();">Fechar</button>
        </td>
      </tr>
    </table>

  </form>

  <%// se não informou o diretório...dispara
    if (directory.equals("")) {%>
  <script type="text/javascript">
    alert("Não há um diretório selecionado para upload.");
    window.close();
  </script>
  <%} // if%>

  </body>
</html>
