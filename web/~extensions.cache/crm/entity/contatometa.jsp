     
<%@include file="../include/beans.jsp"%>

<%@page import="imanager.entity.*"%>

<%!
  public ContatoMetaInfo[] search(ContatoMeta contatoMeta, int empresaId, StringBuffer searchErrorMessage) {
    try {
      // resultado da pesquisa...
      return contatoMeta.selectByFilter(
              empresaId,
              contatoMeta.USER_PARAM_CONTATO_ID.getIntValue()
            );
    }
    catch (Exception e) {
      searchErrorMessage.append(ExtendedException.extractMessage(e));
      return new ContatoMetaInfo[0];
    } // try-catch
  }
%>

<%
  // início do bloco protegido
  try {
    // nossa instância de Contato Meta
    ContatoMeta contatoMeta = (ContatoMeta)facade.getEntity(ContatoMeta.CLASS_NAME);

    // lista que iremos exibir
    ContatoMetaInfo[] contatoMetaInfoList = new ContatoMetaInfo[0];
    // mensagem de erro na pesquisa
    StringBuffer searchErrorMessage = new StringBuffer();

    // se estamos excluindo...
    if (Controller.isDeleting(request)) {
      // registros selecionados
      ContatoMetaInfo[] selectedInfoList = (ContatoMetaInfo[])EntityGrid.selectedInfoList(contatoMeta, request);
      // compõe a resposta
      Form.composeAjaxResponse(response, Form.COMPOSE_TYPE_JAVA_SCRIPT);
      // nosso Grid
      EntityGrid entityGrid = EntityGrid.getInstance(facade, contatoMeta);
      // exclui e apaga do Grid
      for (int i=0; i<selectedInfoList.length; i++) {
        contatoMeta.delete(selectedInfoList[i]);%>
        <%=entityGrid.deleteRow(selectedInfoList[i])%>
      <%} // for
      // dispara
      return;
    }
    // se estamos pesquisando...
    else if (Controller.isSearching(request)) {
      // define os valores dos parâmetros de usuário
      contatoMeta.userParamList().setParamsValues(request);
      // compõe a resposta
      Form.composeAjaxResponse(response, Form.COMPOSE_TYPE_JAVA_SCRIPT);
      // atualiza tudo
      contatoMetaInfoList = search(contatoMeta, selectedEmpresaId, searchErrorMessage);
      %><%=EntityGrid.getInstance(facade, contatoMeta).update(contatoMetaInfoList, "", searchErrorMessage.toString())%><%
      // dispara
      return;
    } // if

    // define os valores dos parâmetros de usuário
    contatoMeta.userParamList().setParamsValues(request);
    // realiza a pesquisa
    contatoMetaInfoList = search(contatoMeta, selectedEmpresaId, searchErrorMessage);

    // nosso Form de exclusão
    Form formDelete = new Form(request, "formDeleteContatoMeta", ContatoMeta.ACTION, ContatoMeta.COMMAND_DELETE, "", false, true, true);
    // nosso Grid
    EntityGrid entityGrid = EntityGrid.getInstance(facade, contatoMeta, 0, 0);
    entityGrid.addColumn(ContatoMeta.FIELD_MES, 30, ContatoMeta.ACTION_CADASTRO, ContatoMeta.COMMAND_EDIT, "", true);
    entityGrid.addColumn(ContatoMeta.FIELD_ANO, 50);
    entityGrid.addColumn(ContatoMeta.FIELD_META_VENDA, 100);
    entityGrid.addColumn(ContatoMeta.FIELD_META_SERVICO, 100);
  
%>

<html>
  <head>
    <title><%=ContatoMeta.ACTION.getCaption()%></title>
    <link href="<%=facade.getStyle().userInterface()%>" rel="stylesheet" type="text/css">
    <script src="include/scripts.js" type="text/javascript"></script>
  </head>
  <body style="margin:0px;" onselectstart="return true;" oncontextmenu="return false;">

    <table style="width:100%; height:100%;">
      <tr>
        <td style="height:auto;">

          <!-- Form de exclusão -->
          <%=formDelete.begin()%>
              <!-- Grid -->
              <%=entityGrid.script(contatoMetaInfoList, "", searchErrorMessage.toString())%>
          <%=formDelete.end()%>

        </td>
      </tr>
      <tr>
        <td style="height:20px;">

          <%=CommandControl.entityFormButton(facade, contatoMeta, ContatoMeta.ACTION_CADASTRO, ContatoMeta.COMMAND_INSERT, ImageList.COMMAND_INSERT, false)%>&nbsp;
          <%=CommandControl.formButton(facade, formDelete, ImageList.COMMAND_DELETE, "Deseja mesmo excluir o(s) registro(s) selecionado(s)?", true, false)%>

        </td>
      </tr>
    </table>

  </body>
</html>

<%}
  // término do bloco protegido
  catch (Exception e) {
    Controller.forwardException(e, pageContext);
  } // try-catch
%>
