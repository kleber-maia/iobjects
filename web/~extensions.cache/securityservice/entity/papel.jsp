
<%@include file="../include/beans.jsp"%>

<%@page import="java.sql.*"%>
<%@page import="securityservice.entity.*"%>
<%@page import="securityservice.ui.entity.*"%>

<%
  try {
    // nossa instância de Papel
    Papel papel = (Papel)facade.getEntity(Papel.CLASS_NAME);

    // se estamos excluindo...
    if (Controller.isDeleting(request)) {
      // registros selecionados
      PapelInfo[] selectedInfoList = (PapelInfo[])EntityGrid.selectedInfoList(papel, request);
      // compõe a resposta
      Form.composeAjaxResponse(response, Form.COMPOSE_TYPE_JAVA_SCRIPT);
      // nosso Grid
      EntityGrid entityGrid = EntityGrid.getInstance(facade, papel);
      // exclui e apaga do Grid
      for (int i=0; i<selectedInfoList.length; i++) {
        papel.delete(selectedInfoList[i]);%>
        <%=entityGrid.deleteRow(selectedInfoList[i])%>
      <%} // for
      // dispara
      return;
    } // if

    // lista que iremos exibir
    PapelInfo[] papelInfoList = new PapelInfo[0];

    // mensagem de erro na pesquisa
    String searchErrorMessage = "";
    try {
      // lista que iremos exibir
      papelInfoList = papel.selectByDescricao("");
    }
    catch (Exception e) {
      searchErrorMessage = ExtendedException.extractMessage(e);
    } // try-catch

    // nosso Form de exclusão
    Form formDelete = new Form(request, "formDeletePapel", Papel.ACTION, Papel.COMMAND_DELETE, "", false, true, true);
    // nosso FrameBar
    FrameBar frameBar = new FrameBar(facade, "frameBarPapel");
    // nosso Grid
    EntityGrid entityGrid = EntityGrid.getInstance(facade, papel, 0, 0);
    entityGrid.addColumn(Papel.FIELD_NOME, 250, Papel.ACTION_CADASTRO, Papel.COMMAND_EDIT, "", false);
    entityGrid.addColumn(Papel.FIELD_DESCRICAO, 250);
    entityGrid.addColumn(Papel.FIELD_PRIVILEGIADO, 70);
    // imagem do Grid
    entityGrid.setColumnImage(0, papel.extension().imageList().getImageUrl("papel16x16.gif"));
%>

<html>
  <head>
    <title><%=Papel.ACTION.getCaption()%></title>
    <link href="<%=facade.getStyle().userInterface()%>" rel="stylesheet" type="text/css">
    <script src="include/scripts.js" type="text/javascript"></script>
  </head>
  <body style="margin:0px;" onselectstart="return true;" oncontextmenu="return false;">

    <!-- inicia o FrameBar -->
    <%=frameBar.begin()%>

      <!-- área de frames -->
      <%=frameBar.beginFrameArea()%>

        <!-- Frame de identificação do objeto -->
        <%=frameBar.actionFrame(Papel.ACTION)%>

        <!-- Frame de comandos -->
        <%=frameBar.beginFrame("Comandos", false, false)%>
          <table style="width:100%;">
            <tr>
              <td><%=CommandControl.entityFormHyperlink(facade, papel, Papel.ACTION_CADASTRO, Papel.COMMAND_INSERT, ImageList.COMMAND_INSERT, false)%></td>
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
          <%=entityGrid.script(papelInfoList, "", searchErrorMessage)%>

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
