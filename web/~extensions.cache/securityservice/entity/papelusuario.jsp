
<%@include file="../include/beans.jsp"%>

<%@page import="java.sql.*"%>
<%@page import="securityservice.entity.*"%>
<%@page import="securityservice.ui.entity.*"%>

<%
  try {
    // nossa instância de Papel Usuário
    PapelUsuario papelUsuario = (PapelUsuario)facade.getEntity(PapelUsuario.CLASS_NAME);

    // se estamos excluindo...
    if (Controller.isDeleting(request)) {
      // registros selecionados
      PapelUsuarioInfo[] selectedInfoList = (PapelUsuarioInfo[])EntityGrid.selectedInfoList(papelUsuario, request);
      // compõe a resposta
      Form.composeAjaxResponse(response, Form.COMPOSE_TYPE_JAVA_SCRIPT);
      // nosso Grid
      EntityGrid entityGrid = EntityGrid.getInstance(facade, papelUsuario);
      // exclui e apaga do Grid
      for (int i=0; i<selectedInfoList.length; i++) {
        papelUsuario.delete(selectedInfoList[i]);%>
        <%=entityGrid.deleteRow(selectedInfoList[i])%>
      <%} // for
      // dispara
      return;
    } // if

    // lista que iremos exibir
    PapelUsuarioInfo[] papelUsuarioInfoList = new PapelUsuarioInfo[0];

    // define valores dos parâmetros de usuário
    papelUsuario.userParamList().setParamsValues(request);
    // verifica tipo de objeto mestre na relação
    boolean masterIsPapel = papelUsuario.USER_PARAM_MASTER.getValue().equals(PapelUsuario.MASTER_IS_PAPEL);
    boolean masterIsUsuario = papelUsuario.USER_PARAM_MASTER.getValue().equals(PapelUsuario.MASTER_IS_USUARIO);
    // se não sabemos o mestre da relação...exceção
    if (!masterIsPapel && !masterIsUsuario)
      throw new Exception("Mestre da relação indefinido.");

    // mensagem de erro na pesquisa
    String searchErrorMessage = "";
    try {
      // pesquisa os registros para exibir
      if (
          masterIsPapel && !papelUsuario.USER_PARAM_PAPEL_ID.getValue().equals("")
         )
        papelUsuarioInfoList = papelUsuario.selectByPapelId(
              NumberTools.parseInt(papelUsuario.USER_PARAM_PAPEL_ID.getValue())
            );
      else if (
          masterIsUsuario && !papelUsuario.USER_PARAM_USUARIO_ID.getValue().equals("")
         )
        papelUsuarioInfoList = papelUsuario.selectByUsuarioId(
              NumberTools.parseInt(papelUsuario.USER_PARAM_USUARIO_ID.getValue())
            );
    }
    catch (Exception e) {
      searchErrorMessage = ExtendedException.extractMessage(e);
    } // try-catch

    // nosso Form de exclusão
    Form formDelete = new Form(request, "formDeletePapelUsuario", PapelUsuario.ACTION, PapelUsuario.COMMAND_DELETE, "", false, true);
    // nosso Grid
    EntityGrid entityGrid = EntityGrid.getInstance(facade, papelUsuario, 0, 0);
    entityGrid.addColumn(PapelUsuario.LOOKUP_USUARIO, 250);
    entityGrid.addColumn(PapelUsuario.LOOKUP_PAPEL, 250);
    entityGrid.setColumnImage(0, papelUsuario.extension().imageList().getImageUrl("usuario16x16.png"));
    entityGrid.setColumnImage(1, papelUsuario.extension().imageList().getImageUrl("papel16x16.png"));
    // oculta uma das colunas de acordo com o uso
    entityGrid.setColumnWidth((masterIsPapel ? 1 : 0), 0);
    entityGrid.setColumnWidth((masterIsPapel ? 0 : 1), 250);
%>

<html>
  <head>
    <title><%=PapelUsuario.ACTION.getCaption()%></title>
    <link href="<%=facade.getStyle().userInterface()%>" rel="stylesheet" type="text/css">
    <script src="include/scripts.js" type="text/javascript"></script>
  </head>
  <body style="margin:0px;" onselectstart="return true;" oncontextmenu="return false;">

    <!-- Form de exclusão -->
    <%=formDelete.begin()%>
      <table style="width:100%; height:100%;">
        <tr style="height:auto;">
          <td>
            <!-- Grid -->
            <%=entityGrid.script(papelUsuarioInfoList, "", searchErrorMessage)%>
          </td>
        </tr>
        <tr style="height:20px;">
          <td>
            <%=CommandControl.entityFormButton(facade, papelUsuario, PapelUsuario.ACTION_CADASTRO, PapelUsuario.COMMAND_INSERT, ImageList.COMMAND_INSERT, false)%>&nbsp;
            <%=CommandControl.formButton(facade, formDelete, ImageList.COMMAND_DELETE, "Deseja mesmo excluir o(s) registro(s) selecionado(s)?", true, false)%>
          </td>
        </tr>
      </table>
    <%=formDelete.end()%>

  </body>
</html>

<%}
  catch (Exception e) {
    Controller.forwardException(e, pageContext);
  } // try-catch
%>
