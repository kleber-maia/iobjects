
<%@include file="../include/beans.jsp"%>

<%@page import="imanager.entity.*"%>
<%@page import="imanager.ui.entity.*"%>

<%@page import="securityservice.ui.entity.*"%>

<%
  // início do bloco protegido
  try {
    // nossa instância de Tarefa Encaminhamento
    TarefaEncaminhamento tarefaEncaminhamento = (TarefaEncaminhamento)facade.getEntity(TarefaEncaminhamento.CLASS_NAME);
    // nossa instância de Tarefa
    Tarefa tarefa = (Tarefa)facade.getEntity(Tarefa.CLASS_NAME);

    // TarefaEncaminhamentoInfo para editar
    TarefaEncaminhamentoInfo tarefaEncaminhamentoInfo = null;

    // se estamos inserindo...
    if (Controller.isInserting(request)) {
      // info em branco
      tarefaEncaminhamentoInfo = new TarefaEncaminhamentoInfo();
      // valores padrão
      tarefaEncaminhamentoInfo.setEmpresaId(tarefaEncaminhamento.USER_PARAM_EMPRESA_ID.getIntValue());
      tarefaEncaminhamentoInfo.setTarefaId(tarefaEncaminhamento.USER_PARAM_TAREFA_ID.getIntValue());
    }
    // se estamos salvando...
    else if (Controller.isSaving(request)) {
      // info preenchido na página
      tarefaEncaminhamentoInfo = (TarefaEncaminhamentoInfo)tarefaEncaminhamento.entityInfoFromRequest(request);
      // se estamos salvando e é um novo...
      if (Controller.isSavingNew(request)) {
        // inclui
        tarefaEncaminhamento.insert(tarefaEncaminhamentoInfo);
        // atualiza
        tarefaEncaminhamentoInfo = (TarefaEncaminhamentoInfo)tarefaEncaminhamento.refresh(tarefaEncaminhamentoInfo);
      } // if
      // localiza a Tarefa
      TarefaInfo tarefaInfo = tarefa.selectByPrimaryKey(tarefaEncaminhamentoInfo.getEmpresaId(), tarefaEncaminhamentoInfo.getTarefaId());
      // compõe a resposta
      Form.composeAjaxResponse(response, Form.COMPOSE_TYPE_JAVA_SCRIPT);
      // atualiza os valores no form de Tarefa
      %>parent.parent.updateFields(<%=tarefaEncaminhamentoInfo.getDepartamentoId()%>, "<%=tarefaEncaminhamentoInfo.lookupValueList().get(TarefaEncaminhamento.LOOKUP_DEPARTAMENTO).getDisplayFieldValuesToString()%>", <%=tarefaEncaminhamentoInfo.getUsuarioId()%>, "<%=tarefaEncaminhamentoInfo.lookupValueList().get(TarefaEncaminhamento.LOOKUP_USUARIO).getDisplayFieldValuesToString()%>");<%
      // atualiza a consulta de Tarefa
      %><%=EntityGrid.updateBrowse(facade, tarefa, EntityGrid.OPERATION_UPDATE, tarefaInfo, "")%><%
      // atualiza a consulta
      %><%=EntityGrid.forwardBrowse(facade, tarefaEncaminhamento, TarefaEncaminhamento.ACTION, Controller.isSavingNew(request) ? EntityGrid.OPERATION_INSERT : EntityGrid.OPERATION_UPDATE, tarefaEncaminhamentoInfo)%><%
      // dispara
      return;
    }
    // se estamos editando...
    else if (Controller.isEditing(request)) {
      // info do registro para editar
      tarefaEncaminhamentoInfo =
        tarefaEncaminhamento.selectByPrimaryKey(
          NumberTools.parseInt(request.getParameter(TarefaEncaminhamento.FIELD_EMPRESA_ID.getFieldAlias())),
          NumberTools.parseInt(request.getParameter(TarefaEncaminhamento.FIELD_TAREFA_ID.getFieldAlias())),
          NumberTools.parseInt(request.getParameter(TarefaEncaminhamento.FIELD_TAREFA_ENCAMINHAMENTO_ID.getFieldAlias()))
        );
    }
    // se o comando é desconhecido...
    else {
      throw new UnknownCommandException();
    } // if

    // nosso Form de dados
    Form formData = new Form(request, "formDataTarefaEncaminhamento", TarefaEncaminhamento.ACTION_CADASTRO, TarefaEncaminhamento.COMMAND_SAVE, "", true, true);
%>

<html>
  <head>
    <title><%=TarefaEncaminhamento.ACTION.getCaption()%></title>
    <link href="<%=facade.getStyle().userInterface()%>" rel="stylesheet" type="text/css">
    <script src="include/scripts.js" type="text/javascript"></script>
  </head>
  <body style="margin:0px;" onselectstart="return true;" oncontextmenu="return false;">

    <!-- Form de dados -->
    <%=formData.begin()%>

        <!-- dados de Contato Meta -->
        <%=GroupBox.begin("Identificação")%>
          <!-- dados ocultos -->
          <%=EntityFormEdit.script(facade, TarefaEncaminhamento.FIELD_EMPRESA_ID, tarefaEncaminhamentoInfo, request, -1, "", "")%>
          <%=EntityFormEdit.script(facade, TarefaEncaminhamento.FIELD_TAREFA_ID, tarefaEncaminhamentoInfo, request, -1, "", "")%>
          <%=EntityFormEdit.script(facade, TarefaEncaminhamento.FIELD_TAREFA_ENCAMINHAMENTO_ID, tarefaEncaminhamentoInfo, request, -1, "", "")%>
          <%=EntityFormEdit.script(facade, TarefaEncaminhamento.FIELD_USUARIO_INCLUSAO_ID, tarefaEncaminhamentoInfo, request, -1, "", "")%>
          <%=EntityFormEdit.script(facade, TarefaEncaminhamento.FIELD_DATA_HORA_INCLUSAO, tarefaEncaminhamentoInfo, request, -1, "", "")%>
          <!-- dados de Tarefa Encaminhamento -->
          <table style="width:100%;">
            <!-- campo Departamento ID -->
            <tr>
              <td style="width:250px;"><%=EntityLookupLabel.script(facade, TarefaEncaminhamento.LOOKUP_DEPARTAMENTO, request)%></td>
              <td style="width:150px;"><%=EntityLookupLabel.script(facade, TarefaEncaminhamento.LOOKUP_USUARIO, request)%></td>
              <td style="width:auto;"><%=EntityFieldLabel.script(facade, TarefaEncaminhamento.FIELD_DESCRICAO, request)%></td>
            </tr>
            <tr>
              <td><%=DepartamentoUI.lookupListForLookup(facade, TarefaEncaminhamento.LOOKUP_DEPARTAMENTO, tarefaEncaminhamentoInfo, selectedEmpresaId, 0, "", "")%></td>
              <td><%=UsuarioUI.lookupSearchForLookup(facade, TarefaEncaminhamento.LOOKUP_USUARIO, tarefaEncaminhamentoInfo, 0, "", "", false)%></td>
              <td><%=EntityFormEdit.script(facade, TarefaEncaminhamento.FIELD_DESCRICAO, tarefaEncaminhamentoInfo, request, 0, "", "")%></td>
            </tr>
          </table>
      <%=GroupBox.end()%>
    <%=formData.end()%>

    <%=CommandControl.formButton(facade, formData, ImageList.COMMAND_SAVE, "Deseja mesmo salvar o registro?", true, false)%>&nbsp;
    <%=CommandControl.entityBrowseButton(facade, tarefaEncaminhamento, TarefaEncaminhamento.ACTION, ImageList.COMMAND_BACK, "Voltar", "Volta para a consulta.", false)%>

  </body>
</html>

<%}
  // término do bloco protegido
  catch (Exception e) {
    Controller.forwardException(e, pageContext);
  } // try-catch
%>
