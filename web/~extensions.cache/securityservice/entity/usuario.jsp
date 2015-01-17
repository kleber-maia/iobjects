
<%@include file="../include/beans.jsp"%>

<%@page import="java.sql.*"%>
<%@page import="securityservice.entity.*"%>

<%
  try {
    // nossa instância de Usuário
    Usuario usuario = (Usuario)facade.getEntity(Usuario.CLASS_NAME);

    // se estamos excluindo...
    if (Controller.isDeleting(request)) {
      // registros selecionados
      UsuarioInfo[] selectedInfoList = (UsuarioInfo[])EntityGrid.selectedInfoList(usuario, request);
      // compõe a resposta
      Form.composeAjaxResponse(response, Form.COMPOSE_TYPE_JAVA_SCRIPT);
      // nosso Grid
      EntityGrid entityGrid = EntityGrid.getInstance(facade, usuario);
      // exclui e apaga do Grid
      for (int i=0; i<selectedInfoList.length; i++) {
        usuario.delete(selectedInfoList[i]);%>
        <%=entityGrid.deleteRow(selectedInfoList[i])%>
      <%} // for
      // dispara
      return;
    } // if

    // lista que iremos exibir
    UsuarioInfo[] usuarioInfoList = new UsuarioInfo[0];

    // mensagem de erro na pesquisa
    String searchErrorMessage = "";
    try {
      // lista que iremos exibir
      usuarioInfoList = usuario.selectByNome("");
    }
    catch (Exception e) {
      searchErrorMessage = ExtendedException.extractMessage(e);
    } // try-catch

    // nosso Form de exclusão
    Form formDelete = new Form(request, "formDeleteUsuario", Usuario.ACTION, Usuario.COMMAND_DELETE, "", false, true, true);
    // nosso FrameBar
    FrameBar frameBar = new FrameBar(facade, "frameBarUsuario");
    // nosso Grid
    EntityGrid entityGrid = EntityGrid.getInstance(facade, usuario, 0, 0);
    entityGrid.addColumn(Usuario.FIELD_NOME, 250, Usuario.ACTION_CADASTRO, Usuario.COMMAND_EDIT, "", false);
    entityGrid.addColumn(Usuario.FIELD_DESCRICAO, 250);
    entityGrid.addColumn(Usuario.FIELD_NIVEL, 150);
    entityGrid.addColumn(Usuario.FIELD_INATIVO, 70);
    // imagem do Grid
    entityGrid.setColumnImage(0, usuario.extension().imageList().getImageUrl("usuario16x16.gif"));
%>

<html>
  <head>
    <title><%=Usuario.ACTION.getCaption()%></title>
    <link href="<%=facade.getStyle().userInterface()%>" rel="stylesheet" type="text/css">
    <script src="include/scripts.js" type="text/javascript"></script>
  </head>
  <body style="margin:0px;" onselectstart="return true;" oncontextmenu="return false;">

    <!-- inicia o FrameBar -->
    <%=frameBar.begin()%>

      <!-- área de frames -->
      <%=frameBar.beginFrameArea()%>

        <!-- Frame de identificação do objeto -->
        <%=frameBar.actionFrame(Usuario.ACTION)%>

        <!-- Frame de comandos -->
        <%=frameBar.beginFrame("Comandos", false, false)%>
          <table style="width:100%;">
            <tr>
              <td><%=CommandControl.entityFormHyperlink(facade, usuario, Usuario.ACTION_CADASTRO, Usuario.COMMAND_INSERT, ImageList.COMMAND_INSERT, false)%></td>
            </tr>
            <tr>
              <td><%=CommandControl.formHyperlink(facade, formDelete, ImageList.COMMAND_DELETE, "Deseja mesmo excluir o(s) registro(s) selecionado(s)?", true, false)%></td>
            </tr>
          </table>
        <%=frameBar.endFrame()%>

      <%=frameBar.endFrameArea()%>

      <!-- área do Grid -->
      <%=frameBar.beginClientArea()%>
        <!-- Form de exclusão -->
        <%=formDelete.begin()%>

          <!-- Grid -->
          <%=entityGrid.script(usuarioInfoList, "", searchErrorMessage)%>

        <%=formDelete.end()%>
      <%=frameBar.endClientArea()%>

    <%=frameBar.end()%>

  </body>
</html>

<%}
  catch (Exception e) {
    Controller.forwardException(e, pageContext);
  } // try-catch
%>
