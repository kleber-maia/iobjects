
<%@include file="../include/beans.jsp"%>

<%@page import="imanager.entity.*"%>

<%!
  String HAS_ANOTHER_OWNER = "hasAnotherOwner";

 public TarefaEncaminhamentoInfo[] search(TarefaEncaminhamento tarefaEncaminhamento, StringBuffer searchErrorMessage) {
    try {
      // resultado da pesquisa...
      return tarefaEncaminhamento.selectByFilter(
              NumberTools.parseInt(tarefaEncaminhamento.USER_PARAM_EMPRESA_ID.getValue()),
              NumberTools.parseInt(tarefaEncaminhamento.USER_PARAM_TAREFA_ID.getValue()),
              null);
    }
    catch (Exception e) {
      searchErrorMessage.append(ExtendedException.extractMessage(e));
      return new TarefaEncaminhamentoInfo[0];
    } // try-catch
  }
%>

<%
  // início do bloco protegido
  try {
    // nossa instância de Tarefa Encaminhamento
    TarefaEncaminhamento tarefaEncaminhamento = (TarefaEncaminhamento)facade.getEntity(TarefaEncaminhamento.CLASS_NAME);

    // lista que iremos exibir
    TarefaEncaminhamentoInfo[] tarefaEncaminhamentoInfoList = new TarefaEncaminhamentoInfo[0];
    // mensagem de erro na pesquisa
    StringBuffer searchErrorMessage = new StringBuffer();

    // se estamos excluindo...
    if (Controller.isDeleting(request)) {
      // registros selecionados
      TarefaEncaminhamentoInfo[] selectedInfoList = (TarefaEncaminhamentoInfo[])EntityGrid.selectedInfoList(tarefaEncaminhamento, request);
      // compõe a resposta
      Form.composeAjaxResponse(response, Form.COMPOSE_TYPE_JAVA_SCRIPT);
      // nosso Grid
      EntityGrid entityGrid = EntityGrid.getInstance(facade, tarefaEncaminhamento);
      // exclui e apaga do Grid
      for (int i=0; i<selectedInfoList.length; i++) {
        tarefaEncaminhamento.delete(selectedInfoList[i]);%>
        <%=entityGrid.deleteRow(selectedInfoList[i])%>
      <%} // for
      // dispara
      return;
    }
    // se estamos pesquisando...
    else if (Controller.isSearching(request)) {
      // define os valores dos parâmetros de usuário
      tarefaEncaminhamento.userParamList().setParamsValues(request);
      // compõe a resposta
      Form.composeAjaxResponse(response, Form.COMPOSE_TYPE_JAVA_SCRIPT);
      // atualiza tudo
      tarefaEncaminhamentoInfoList = search(tarefaEncaminhamento, searchErrorMessage);
      %><%=EntityGrid.getInstance(facade, tarefaEncaminhamento).update(tarefaEncaminhamentoInfoList, "", searchErrorMessage.toString())%><%
      // dispara
      return;
    } // if

    // verifica se a Tarefa está associada a outro usuário
    boolean hasAnotherOwner = Controller.getParameter(request, HAS_ANOTHER_OWNER).equals("true");

    // define os valores dos parâmetros de usuário
    tarefaEncaminhamento.userParamList().setParamsValues(request);
    // realiza a pesquisa
    tarefaEncaminhamentoInfoList = search(tarefaEncaminhamento, searchErrorMessage);

    // nosso Grid
    EntityGrid entityGrid = EntityGrid.getInstance(facade, tarefaEncaminhamento, 0, 0);
    entityGrid.setSelectType(EntityGrid.SELECT_TYPE_NONE);
    entityGrid.addColumn(TarefaEncaminhamento.FIELD_TAREFA_ENCAMINHAMENTO_ID, 35);
    entityGrid.addColumn(TarefaEncaminhamento.LOOKUP_DEPARTAMENTO, 200);
    entityGrid.addColumn(TarefaEncaminhamento.LOOKUP_USUARIO, 150);
    entityGrid.addColumn(TarefaEncaminhamento.FIELD_DESCRICAO, 300);
    entityGrid.addColumn(TarefaEncaminhamento.LOOKUP_USUARIO_INCLUSAO, 150);
    entityGrid.addColumn(TarefaEncaminhamento.FIELD_DATA_HORA_INCLUSAO, 120);
%>

<html>
  <head>
    <title><%=TarefaEncaminhamento.ACTION.getCaption()%></title>
    <link href="<%=facade.getStyle().userInterface()%>" rel="stylesheet" type="text/css">
    <script src="include/scripts.js" type="text/javascript"></script>
  </head>
  <body style="margin:0px;" onselectstart="return true;" oncontextmenu="return false;">

    <table style="width:100%; height:100%;">
      <tr>
        <td style="height:auto;">
          <!-- Grid -->
          <%=entityGrid.script(tarefaEncaminhamentoInfoList, "", searchErrorMessage.toString())%>
        </td>
      </tr>
      <tr>
        <td style="height:20px;">
          <%=CommandControl.entityFormButton(facade, tarefaEncaminhamento, TarefaEncaminhamento.ACTION_CADASTRO, TarefaEncaminhamento.COMMAND_INSERT, ImageList.COMMAND_INSERT, hasAnotherOwner && !facade.getLoggedUser().getPrivileged())%>
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
