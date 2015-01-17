<%@include file="../../include/beans.jsp"%>

<%@page import="java.io.*"%>

<%!
  String COOKIE_DIRECTORY = "wysiwygImageDirectory";

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
  // caminho das imagens no servidor
  String imagesPath = request.getParameter("imagesPath");
  if (imagesPath == null)
    imagesPath = "";
  // url das imagens no servidor
  String imagesUrl = request.getParameter("imagesUrl");
  if (imagesUrl == null)
    imagesUrl = "";
  // diretório selecionado
  String directory = request.getParameter("directory");
  if (directory == null)
    directory = "";
  // nosso Id
  String wysiwyg = request.getParameter("wysiwyg");

  // devemos localizar imagens?
  boolean searchImages = !imagesPath.equals("") && !imagesUrl.equals("");

  // lista de diretórios e de arquivos
  String[] directoryList = new String[0];
  String[] fileList = new String[0];

  // se devemos localizar imagens...
  if (searchImages) {
    // lista de diretórios
    directoryList = getDirectoryList(imagesPath);
    // se não temos um diretório selecionado...pega do cookie
    if (directory.equals(""))
      directory = HttpTools.getCookieValue(request, COOKIE_DIRECTORY, (directoryList.length > 0  ? directoryList[0] : ""));
    // salva o diretório
    HttpTools.setCookieValue(response, COOKIE_DIRECTORY, directory, false);
    // lista de imagens
    fileList = getFileList(imagesPath, directory);
  } // if
%>

<html>
  <head>
    <title>Inserir imagem</title>
    <link href="../../<%=facade.getStyle().userInterface()%>" rel="stylesheet" type="text/css">
    <script src="../../include/scripts.js" type="text/javascript"></script>
  </head>

  <script language="JavaScript" type="text/javascript">

    var qsParm = new Array();


    /* ---------------------------------------------------------------------- *\
      Function    : retrieveWYSIWYG()
      Description : Retrieves the textarea ID for which the image will be inserted into.
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


    /* ---------------------------------------------------------------------- *\
      Function    : insertImage()
      Description : Inserts image into the WYSIWYG.
    \* ---------------------------------------------------------------------- */
    function insertImage() {
      var image = '<img class="album" src="' + document.getElementById('imageurl').value + '" alt="' + document.getElementById('alt').value + '" ' + document.getElementById('alignment').value + ' border="' + document.getElementById('borderThickness').value + '" hspace="' + document.getElementById('horizontal').value + '" vspace="' + document.getElementById('vertical').value + '">';
      window.opener.insertHTML(image, qsParm['wysiwyg']);
      window.close();
    }

  </script>

  <body style="margin: 0px 0px 0px 0px;" onselectstart="return false;" oncontextmenu="return false;" onLoad="retrieveWYSIWYG();">

    <table border="0" cellpadding="0" cellspacing="0" style="padding: 10px;">

    <%// se podemos localizar imagens
      if (searchImages) {%>
    <tr><td>

    <span style="font-weight: bold;">Localizar imagem:</span>
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
                               "window.location.href = 'insert_image.jsp?wysiwyg=" + wysiwyg + "&imagesPath=" + imagesPath + "&imagesUrl=" + imagesUrl + "&directory=' + this.options[this.selectedIndex].value;",
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
                               "document.getElementById('imageurl').value = '" + imagesUrl + "' + '/' + '" + directory + "' + '/' + this.options[this.selectedIndex].value;",
                               false)%>
        </td>
      </tr>
    </table>

    </td></tr>
    <%} // if%>

    <tr><td>

    <span style="font-weight: bold;">Inserir imagem:</span>
    <br />
    <br />
    <table width="380" border="0" cellpadding="0" cellspacing="0" style="border: 1px solid; padding: 5px;">
     <tr>
      <td style="padding-bottom: 2px; padding-top: 0px; " width="80">Caminho (URL):</td>
            <td style="padding-bottom: 2px; padding-top: 0px;" width="300"><input type="text" name="imageurl" id="imageurl" value=""  style="font-size: 10px; width: 100%;"></td>
     </tr>
     <tr>
      <td style="padding-bottom: 2px; padding-top: 0px; ">Texto opcional:</td>
            <td style="padding-bottom: 2px; padding-top: 0px;"><input type="text" name="alt" id="alt" value=""  style="font-size: 10px; width: 100%;"></td>
     </tr>
    </table>



    <table width="380" border="0" cellpadding="0" cellspacing="0" style="margin-top: 10px;"><tr><td>

    <span style=" font-weight: bold;">Aparência:</span>
    <br />
    <br />
    <table width="185" border="0" cellpadding="0" cellspacing="0" style="border: 1px solid; padding: 5px;">
     <tr>
      <td style="padding-bottom: 2px; padding-top: 0px; " width="100">Alinhamento:</td>
            <td style="padding-bottom: 2px; padding-top: 0px;" width="85">
            <select name="alignment" id="alignment" style=" width: 100%;">
             <option value=''>Nenhum</option>
             <option value='style="float:left; margin:4px; margin-left:0px; margin-right:16px;"'>Esquerda</option>
             <option value='style="float:right; margin:4px; margin-right:0px; margin-left:16px;"'>Direita</option>
            </select>
            </td>
     </tr>
     <tr>
      <td style="padding-bottom: 2px; padding-top: 0px; ">Borda:</td>
            <td style="padding-bottom: 2px; padding-top: 0px;"><input type="text" name="borderThickness" id="borderThickness" value=""  style="font-size: 10px; width: 100%;"></td>
     </tr>
    </table>

    </td>
    <td width="10">&nbsp;</td>
    <td>

    <span style=" font-weight: bold;">Espaçamento:</span>
    <br />
    <br />
    <table width="185" border="0" cellpadding="0" cellspacing="0" style="border: 1px solid; padding: 5px;">
     <tr>
      <td style="padding-bottom: 2px; padding-top: 0px; " width="80">Horizontal:</td>
            <td style="padding-bottom: 2px; padding-top: 0px;" width="105"><input type="text" name="horizontal" id="horizontal" value=""  style="font-size: 10px; width: 100%;"></td>
     </tr>
     <tr>
      <td style="padding-bottom: 2px; padding-top: 0px; ">Vertical:</td>
            <td style="padding-bottom: 2px; padding-top: 0px;"><input type="text" name="vertical" id="vertical" value=""  style="font-size: 10px; width: 100%;"></td>
     </tr>
    </table>

    </td></tr></table>

    <div align="right" style="padding-top: 10px;">
      <%=Button.script(facade, "buttonOK", "OK", "", "", "", Button.KIND_DEFAULT, "", "insertImage();", false)%>&nbsp;&nbsp;&nbsp;
      <%=Button.script(facade, "buttonCancelar", "Cancelar", "", "", "", Button.KIND_DEFAULT, "", "window.close();", false)%>
    </div>

  </body>
</html>
