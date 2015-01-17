<%@include file="include/beans.jsp"%>

<%@page import="java.io.*"%>
<%@page import="java.sql.*"%>
<%@page import="java.util.*"%>

<%!
  String DELETE        = "delete";
  String DIRECTORY     = "directory";
  String DOWNLOAD      = "download";
  String FILE_NAME     = "fileName";
  String FILE_UPLOAD   = "fileUpload";
  String NEW           = "new";
  String NEW_FILE_NAME = "newFileName";
  String RENAME        = "rename";
  String UPLOAD        = "upload";
%>

<%
  try {
    // diretório atual
    String directory = request.getParameter(DIRECTORY);
    if (directory == null)
      directory = "";
    // arquivo atual
    String fileName = request.getParameter(FILE_NAME);
    if (fileName == null)
      fileName = "";
    // novo nome do arquivo atual
    String newFileName = request.getParameter(NEW_FILE_NAME);
    if (newFileName == null)
      newFileName = "";

    // comando para realizar
    String command = request.getParameter(Controller.COMMAND);
    if (command == null)
      command = "";
    // se quer fazer download
    else if (command.equals(DOWNLOAD) && !fileName.equals("")) {
      // comprime o arquivo de origem na pasta de download
      File[] downloadFiles = new File[1];
      downloadFiles[0] = new File(facade.applicationLocalWorkPath() + directory + File.separatorChar + fileName);
      Zip zipFile = new Zip(facade.downloadLocalPath() + directory + File.separator + fileName + ".zip",
                            downloadFiles);
      // no final do arquivo existe um iframe responsável pelo início do
      // download do arquivo selecionado a partir da pasta de download da aplicação
    }
    // se quer excluir
    else if (command.equals(DELETE) && !fileName.equals("")) {
      // arquivo que será excluído
      File deleteFile = new File(facade.applicationLocalWorkPath() + directory + File.separatorChar + fileName);
      // exclui
      deleteFile.delete();
    }
    // se quer criar um novo
    else if (command.equals(NEW) && !newFileName.equals("")) {
      // novo arquivo
      File newFile = new File(facade.applicationLocalWorkPath() + directory + File.separatorChar + newFileName);
      // cria
      newFile.createNewFile();
    }
    // se quer renomear
    else if (command.equals(RENAME) && !fileName.equals("") && !newFileName.equals("")) {
      // arquivo que será renomeado
      File renameFile = new File(facade.applicationLocalWorkPath() + directory + File.separatorChar + fileName);
      // novo arquivo
      File newFile = new File(facade.applicationLocalWorkPath() + directory + File.separatorChar + newFileName);
      // renomeia
      renameFile.renameTo(newFile);
    } // if

    // lista de sub-diretórios no diretorio de trabalho
    File[] workFileDirs = FileTools.getDirectories(facade.applicationLocalWorkPath(), false);
    // lista de arquivos para exibir
    File[] files = new File[0];
    // se temos um diretório para listar...pega a lista de arquivos
    if (!directory.equals("")) {
      String[] extensions = {"*"};
      files = FileTools.getFiles(facade.applicationLocalWorkPath() + directory,
                                 extensions,
                                 false,
                                 FileTools.ORDER_BY_NAME);
    } // if

    // nosso grid
    Grid grid = new Grid(facade, "gridFiles", new String[]{FILE_NAME}, Grid.SELECT_SINGLE, 0, 0);
    grid.columns().add(new Grid.Column("Arquivo", 270));
    grid.columns().add(new Grid.Column("Tamanho", 70, Grid.ALIGN_RIGHT));
    grid.columns().add(new Grid.Column("Data", 120));
%>

<html>
  <head>
    <title><%=facade.applicationInformation().getName()%> - Arquivos do Sistema</title>
    <link href="<%=facade.getStyle().userInterface()%>" rel="stylesheet" type="text/css">
    <link href="<%=facade.iconURL()%>" rel="shortcut icon" />
    <script src="include/scripts.js" type="text/javascript"></script>
  </head>

    <script type="text/javascript">

      // evita múltiplas submissões do formulário
      formSubmited = false;

      /**
       * Posiciona no diretório informado por 'dir'.
       */
      function chdir(dir) {
        // se o form já foi submetido...dispara
        if (formSubmited) {
          alert("Aguarde o término da operação.");
          return;
        } // if
        // altera o diretório
        formFiles.<%=DIRECTORY%>.value = dir;
        // submete o form
        formSubmited = true;
        formFiles.submit();
      }

      /**
       * Realiza exclusão do arquivo selecionado.
       */
      function deleteFile() {
        // se o form já foi submetido...dispara
        if (formSubmited) {
          alert("Aguarde o término da operação.");
          return;
        } // if
        // se não temos arquivo selecionado...dispara
        var fileName = <%=grid.selectedKeyValues()%>;
        if (fileName == "") {
          alert("Nenhum arquivo selecionado.");
          return;
        } // if
        // se não confirma a exclusão...dispara
        if (!confirm("Essa operação é irreversível e irá excluir o arquivo selecionado no servidor.\r\rDeseja mesmo excluir o arquivo selecionado."))
          return;
        // altera o comando
        formFiles.<%=Controller.COMMAND%>.value = "<%=DELETE%>";
        // submete o form
        formSubmited = true;
        formFiles.submit();
      }

      /**
       * Realiza o download do arquivo selecionado.
       */
      function download() {
        // se o form já foi submetido...dispara
        if (formSubmited) {
          alert("Aguarde o término da operação.");
          return;
        } // if
        // se não tem arquivo selecionado...dispara
        var fileName = <%=grid.selectedKeyValues()%>;
        if (fileName == "") {
          alert("Nenhum arquivo selecionado.");
          return;
        } // if
        // altera o comando
        formFiles.<%=Controller.COMMAND%>.value = "<%=DOWNLOAD%>";
        // submete o form
        formSubmited = true;
        formFiles.submit();
      }

      /**
       * Realiza a edição de 'fileName'.
       */
      function edit(fileName) {
        var windowTop=(screen.availHeight-600)/2;
        var windowLeft=(screen.availWidth-800)/2;
        window.open("<%=Controller.ACTION_SYSTEM_INFORMATION_FILES_EDIT.url()%>&<%=FILE_NAME%>=" + fileName,
                    "systemFileEdit",
                    "top=" + windowTop + ",left=" + windowLeft + ",width=800,height=600,location=no,menubar=no,toolbar=no,status=no,scrollbars=no,resizable=yes");
      }

      /**
       * Cria um novo arquivo.
       */
      function newFile() {
        // se o form já foi submetido...dispara
        if (formSubmited) {
          alert("Aguarde o término da operação.");
          return;
        } // if
        // pede o nome do novo arquivo
        var newFileName = prompt("Informe o nome do novo arquivo.", "");
        if ((newFileName == null) || (newFileName == ""))
          return;
        // informa o novo nome do arquivo
        formFiles.<%=NEW_FILE_NAME%>.value = newFileName;
        // altera o comando
        formFiles.<%=Controller.COMMAND%>.value = "<%=NEW%>";
        // submete o form
        formSubmited = true;
        formFiles.submit();
      }

      /**
       * Renomea o arquivo selecionado.
       */
      function rename() {
        // se o form já foi submetido...dispara
        if (formSubmited) {
          alert("Aguarde o término da operação.");
          return;
        } // if
        // se não tem arquivo selecionado...dispara
        var fileName = <%=grid.selectedKeyValues()%>;
        if (fileName == "") {
          alert("Nenhum arquivo selecionado.");
          return;
        } // if
        // pede o novo nome do arquivo
        var newFileName = prompt("Informe o novo nome do arquivo.", fileName);
        if ((newFileName == null) || (newFileName == "") || (newFileName == fileName))
          return;
        // informa o novo nome do arquivo
        formFiles.<%=NEW_FILE_NAME%>.value = newFileName;
        // altera o comando
        formFiles.<%=Controller.COMMAND%>.value = "<%=RENAME%>";
        // submete o form
        formSubmited = true;
        formFiles.submit();
      }

      /**
       * Mostra a janela de upload.
       */
      function upload() {
        <%=Popup.script(Controller.ACTION_SYSTEM_INFORMATION_FILES_UPLOAD.url(DIRECTORY + "=" + directory),
                        Controller.ACTION_SYSTEM_INFORMATION_FILES_UPLOAD.getName(),
                        125,
                        345,
                        Popup.POSITION_CENTER)%>
      }

    </script>

    <body style="margin:0px 0px 0px 0px;" onselectstart="return false;" oncontextmenu="return false;">

    <!-- form -->
    <form id="formFiles" action="controller" method="POST">
      <input type="hidden" name="<%=Controller.ACTION%>" value="<%=Controller.ACTION_SYSTEM_INFORMATION_FILES.getName()%>" />
      <input type="hidden" name="<%=Controller.COMMAND%>" />
      <input type="hidden" name="<%=DIRECTORY%>" value="<%=directory%>" />
      <input type="hidden" name="<%=NEW_FILE_NAME%>" />

      <!-- árvore e lista de arquivos -->
      <table style="width:100%; height:100%;">
        <tr>
          <!-- árvore de diretórios -->
          <td style="width:250px;">

            <table cellpadding="0" cellspacing="0" style="width:100%; height:100%;">
              <tr style="height:auto;">
                <td>
                  <table style="border:1px solid; width:100%; height:100%;">
                    <tr>
                      <td class="GridHeader" style="height:21px;">Diretórios de trabalho</td>
                    </tr>
                    <tr>
                      <td style="height:auto;">
                        <div id="treeview" class="Window" style="float:client; width:100%; height:100%; overflow:scroll;" onscroll="saveScrollPosition(treeview);">
                          <table class="Window">
                            <tr>
                              <td valign="top" nowrap>
                                <span style="height:18px;"><img src="images/treeview/module.gif" align="absmiddle" /> Diretório de trabalho</span><br />
                                  <%// loop nos diretórios de trabalho
                                    for (int i=0; i<workFileDirs.length; i++) {
                                      File workFileDir = workFileDirs[i];%>
                                    <span style="height:18px;"><img src="images/treeview/<%=directory.equals(workFileDir.getName()) ? "folderopened.gif" : "folderclosed.gif"%>" align="absmiddle" /><a href="javascript:chdir('<%=workFileDir.getName()%>')"><%=workFileDir.getName()%></a></span><br />
                                  <%} // for%>
                              </td>
                            </tr>
                          </table>
                        </div>
                      </td>
                    </tr>
                    <tr>
                      <td class="GridStatus" style="height:21px;"><%=workFileDirs.length%> diretório(s)</td>
                    </tr>
                  </table>
                </td>
              </tr>
              <tr style="height:28px;">
                <td valign="bottom">
                </td>
              </tr>
            </table>

          </td>

          <!-- lista de arquivos -->
          <td style="width:auto;" valign="top">

            <table cellpadding="0" cellspacing="0" style="width:100%; height:100%;">
              <tr style="height:auto;">
                <td>
                  <%=grid.begin()%>
                    <%// loop nos arquivos
                      for (int i=0; i<files.length; i++) {
                        // arquivo da vez
                        File file = files[i];
                        // coluna arquivo
                        String colFile = "<img src='images/treeview/unknown.gif' align='absmiddle' />&nbsp;"
                                       + (file.getName().indexOf(ExtensionManager.EXTENSION_FILE_EXTENSION) < 0 
                                          ? "<a href=javascript:edit('" + file.getAbsolutePath().replace('\\', '/') + "') title='Editar...'>" + file.getName() + "</a>"
                                          : file.getName());
                        String colSize = (file.length() < 1024 ? "1" : NumberTools.format(file.length() / 1024, 1, 0)) + "KB";
                        String colDate = DateTools.formatDate(new Timestamp(file.lastModified()));
                        %>
                      <%=grid.addRow(new String[]{colFile, colSize, colDate}, new String[]{file.getName()})%>
                    <%} // for%>
                  <%=grid.end(files.length + " arquivos")%>
                </td>
              </tr>
              <tr style="height:28px;">
                <td valign="bottom">
                  <%=Button.script(facade, "buttonNovo", "Novo", "Novo arquivo...", "", "", Button.KIND_DEFAULT, "", "newFile();", directory.equals(""))%>&nbsp;
                  <%=Button.script(facade, "buttonDownload", "Download", "Compactar e fazer download.", "", "", Button.KIND_DEFAULT, "", "download();", directory.equals(""))%>&nbsp;
                  <%=Button.script(facade, "buttonRenomear", "Renomear", "Renomear o arquivo...", "", "", Button.KIND_DEFAULT, "", "rename();", directory.equals(""))%>&nbsp;
                  <%=Button.script(facade, "buttonApagar", "Apagar", "Apagar o arquivo...", "", "", Button.KIND_DEFAULT, "", "deleteFile();", directory.equals(""))%>&nbsp;
                  <%--<button onclick="deleteFile();" <%=directory.equals("") ? "disabled" : ""%>>Excluir...</button>&nbsp;--%>
                  <%--<button onclick="upload();" <%=directory.equals("") ? "disabled" : ""%>>Upload...</button>--%>
                </td>
              </tr>
            </table>

        </tr>
      </table>

      <%// se quer fazer o download de um arquivo
        if (command.equals(DOWNLOAD)) {%>
        <div style="position:absolute; left:0px; top:0px; width:0px; height:0px; visibility:hidden;">
          <iframe src="<%=facade.downloadUrl()%>/<%=directory%>/<%=fileName%>.zip"></iframe>
        </div>
      <%} // if%>

    </form>

  </body>
</html>

<%}
  catch (Exception e) {
    Controller.forwardException(e, pageContext);
  } // try-catch
%>
