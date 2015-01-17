<%@include file="../../include/beans.jsp"%>

<%@page import="java.io.*"%>

<%!
  String COOKIE_DIRECTORY = "wysiwygLinkDirectory";

   public String[] getDirectoryList(String path) {
    File[] directoryList = FileTools.getDirectories(path, false);
    String[] result = new String[directoryList.length];
    for (int i=0; i<directoryList.length; i++)
      result[i] = directoryList[i].getName();
    return StringTools.arraySort(result);
  }

  public String[] getFileList(String path, String directory) {
    return FileTools.getFileNames(path + File.separator + directory, new String[]{"*"}, false, FileTools.ORDER_BY_NAME);
  }
%>

<%
  // caminho dos documentos no servidor
  String documentsPath = request.getParameter("documentsPath");
  if (documentsPath == null)
    documentsPath = "";
  // url dos documentos no servidor
  String documentsUrl = request.getParameter("documentsUrl");
  if (documentsUrl == null)
    documentsUrl = "";
  // diretório selecionado
  String directory = request.getParameter("directory");
  if (directory == null)
    directory = "";
  // nosso Id
  String wysiwyg = request.getParameter("wysiwyg");

  // podemos localizar documentos?
  boolean searchDocuments = !documentsPath.equals("") && !documentsUrl.equals("");

  // lista de diretórios e de arquivos
  String[] directoryList = new String[0];
  String[] fileList = new String[0];

  // se podemos localizar documentos...
  if (searchDocuments) {
    // lista de diretórios
    directoryList = getDirectoryList(documentsPath);
    // se não temos um diretório selecionado...pega do cookie
    if (directory.equals(""))
      directory = HttpTools.getCookieValue(request, COOKIE_DIRECTORY, (directoryList.length > 0  ? directoryList[0] : ""));
    // salva o diretório
    HttpTools.setCookieValue(response, COOKIE_DIRECTORY, directory, false);
    // lista de documentos
    fileList = getFileList(documentsPath, directory);
  } // if
%>

<html>
  <head>
    <title>Inserir hyperlink</title>
    <link href="../../<%=facade.getStyle().userInterface()%>" rel="stylesheet" type="text/css">
    <script src="../../include/scripts.js" type="text/javascript"></script>
  </head>

  <script language="JavaScript" type="text/javascript">

    var qsParm = new Array();


    /* ---------------------------------------------------------------------- *\
      Function    : retrieveWYSIWYG()
      Description : Retrieves the textarea ID for which the link will be inserted into.
    \* ---------------------------------------------------------------------- */
    function retrieveWYSIWYG() {
      var query = window.location.search.substring(1);
      var parms = query.split('&');
      for (var i=0; i<parms.length; i++) {
        var pos = parms[i].indexOf('=');
        if (pos > 0) {
           var key = parms[i].substring(0,pos);
           var val = parms[i].substring(pos+1);
           qsParm[key] = val;
        }
      }
    }


    function insertHyperLink() {
      var hyperLink = document.getElementById('url').value;
      window.opener.document.getElementById('wysiwyg' + qsParm['wysiwyg']).contentWindow.document.execCommand("CreateLink", false, hyperLink);
      window.close();
    }

  </script>

  <body style="margin: 0px 0px 0px 0px;" onselectstart="return false;" oncontextmenu="return true;" onload="retrieveWYSIWYG();">

    <table border="0" cellpadding="0" cellspacing="0" style="padding: 10px;">

    <%// se podemos localizar documents
      if (searchDocuments) {%>
    <tr><td>

    <span style="font-weight: bold;">Localizar arquivo:</span>
    <br />
    <br />
    <table width="380" border="0" cellpadding="0" cellspacing="0" style="border: 1px solid; padding: 5px;">
      <tr>
        <td style="padding-bottom: 2px; padding-top: 0px;" width="80">Pasta:</td>
        <td style="padding-bottom: 2px; padding-top: 0px;" width="300">
          <%=FormSelect.script(facade,
                               "directory",
                               directoryList,
                               directoryList,
                               directory,
                               0,
                               0,
                               FormSelect.SELECT_TYPE_SINGLE,
                               "",
                               "",
                               "",
                               "window.location.href = 'insert_hyperlink.jsp?wysiwyg=" + wysiwyg + "&documentsPath=" + documentsPath + "&documentsUrl=" + documentsUrl + "&directory=' + this.options[this.selectedIndex].value;",
                               false)%>
        </td>
      </tr>
      <tr>
        <td style="padding-bottom: 2px; padding-top: 0px; " valign="top">Arquivo:</td>
        <td style="padding-bottom: 2px; padding-top: 0px;">
          <%=FormSelect.script(facade,
                               "file",
                               fileList,
                               fileList,
                               "",
                               0,
                               120,
                               FormSelect.SELECT_TYPE_MULTIPLE,
                               "",
                               "",
                               "",
                               "document.getElementById('url').value = '" + documentsUrl + "' + '/' + '" + directory + "' + '/' + this.options[this.selectedIndex].value;",
                               false)%>
        </td>
      </tr>
    </table>

    </td></tr>
    <%} // if%>

    <tr><td>
    <span style="font-weight: bold;">Inserir hyperlink:</span>
    <br />
    <br />
    <table border="0" cellpadding="0" cellspacing="0" style="width:380px; border: 1px solid; padding: 5px;">
     <tr>
      <td style="padding-bottom: 2px; padding-top: 0px; width:75px; font-family: arial, verdana, helvetica; font-size: 11px;">Caminho (URL):</td>
      <td style="padding-bottom: 2px; padding-top: 0px;"><input type="text" name="url" id="url" value=""  style="font-size: 10px; width: 100%;"></td>
     </tr>
    </table>

    <div align="right" style="padding-top: 10px;">
      <%=Button.script(facade, "buttonOK", "OK", "", "", "", Button.KIND_DEFAULT, "", "insertHyperLink();", false)%>&nbsp;&nbsp;&nbsp;
      <%=Button.script(facade, "buttonCancelar", "Cancelar", "", "", "", Button.KIND_DEFAULT, "", "window.close();", false)%>
    </div>

    </tr></td</table>

  </body>
</html>
