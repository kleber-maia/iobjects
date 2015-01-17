       
<%@include file="../include/beans.jsp"%>

<%@page import="imanager.entity.*"%>

<%
  // início do bloco protegido
  try {
    // nossa instância de CampanhaAutomatizada
    CampanhaAutomatizada campanhaautomatizada = (CampanhaAutomatizada)facade.getEntity(CampanhaAutomatizada.CLASS_NAME);

    // CampanhaAutomatizadaInfo para editar
    CampanhaAutomatizadaInfo campanhaautomatizadaInfo = null;

    // se estamos inserindo...
    if (Controller.isInserting(request)) {
      // info em branco
      campanhaautomatizadaInfo = new CampanhaAutomatizadaInfo();
      // altera valores
      campanhaautomatizadaInfo.setCampanhaId(campanhaautomatizada.USER_PARAM_CAMPANHA_ID.getIntValue());
    }
    // se estamos salvando...
    else if (Controller.isSaving(request)) {
      // info preenchido na página
      campanhaautomatizadaInfo = (CampanhaAutomatizadaInfo)campanhaautomatizada.entityInfoFromRequest(request);
      // se estamos salvando e é um novo...inclui
      if (Controller.isSavingNew(request)) {
        campanhaautomatizada.insert(campanhaautomatizadaInfo);
      }
      // se estamos salvando e é um existente...atualiza
      else {
        campanhaautomatizada.update(campanhaautomatizadaInfo);
      } // if
      // compõe a resposta
      Form.composeAjaxResponse(response, Form.COMPOSE_TYPE_JAVA_SCRIPT);
      // atualiza a consulta
      %><%=EntityGrid.forwardBrowse(facade, campanhaautomatizada, CampanhaAutomatizada.ACTION, Controller.isSavingNew(request) ? EntityGrid.OPERATION_INSERT : EntityGrid.OPERATION_UPDATE, campanhaautomatizadaInfo)%><%
      // dispara
      return;
    }
    // se estamos editando...
    else if (Controller.isEditing(request)) {
      // info do registro para editar
      campanhaautomatizadaInfo =
        campanhaautomatizada.selectByPrimaryKey(
          NumberTools.parseInt(request.getParameter(CampanhaAutomatizada.FIELD_CAMPANHA_ID.getFieldAlias())),
          NumberTools.parseInt(request.getParameter(CampanhaAutomatizada.FIELD_CAMPANHA_AUTOMATIZADA_ID.getFieldAlias()))
        );
    }
    // se o comando é desconhecido...
    else {
      throw new UnknownCommandException();
    } // if

    // nosso FrameBar
    FrameBar frameBar = new FrameBar(facade, "frameBarCampanhaAutomatizada");
    // nosso Form de dados
    Form formData = new Form(request, "formDataCampanhaAutomatizada", CampanhaAutomatizada.ACTION_CADASTRO, CampanhaAutomatizada.COMMAND_SAVE, "", true, true, true);
%>

<html>
  <head>
    <title><%=CampanhaAutomatizada.ACTION.getCaption()%></title>
    <link href="<%=facade.getStyle().userInterface()%>" rel="stylesheet" type="text/css">
    <script src="include/scripts.js" type="text/javascript"></script>
  </head>
  <body style="margin:0px;" onselectstart="return true;" oncontextmenu="return false;">
    <!-- Form de dados -->
    <%=formData.begin()%>
      <%=EntityFormEdit.script(facade, CampanhaAutomatizada.FIELD_CAMPANHA_ID, campanhaautomatizadaInfo, request, -1, "", "")%>
      <%=EntityFormEdit.script(facade, CampanhaAutomatizada.FIELD_CAMPANHA_AUTOMATIZADA_ID, campanhaautomatizadaInfo, request, -1, "", "")%>
      <!-- dados de CampanhaAutomatizada -->
      <table style="width:100%; height:100%">
        <tr>
          <td style="width:auto;height:25px"><%=EntityFieldLabel.script(facade, CampanhaAutomatizada.FIELD_NOME_REMETENTE, request)%></td>
          <td style="width:250px;height:25px"><%=EntityFieldLabel.script(facade, CampanhaAutomatizada.FIELD_EMAIL_REMETENTE, request)%></td>
          <td style="width:100px;height:25px"><%=EntityFieldLabel.script(facade, CampanhaAutomatizada.FIELD_DIAS_ENVIO, request)%></td>
        </tr>
        <tr>
          <td style="width:auto;height:25px"><%=EntityFormEdit.script(facade, CampanhaAutomatizada.FIELD_NOME_REMETENTE, campanhaautomatizadaInfo, request, 0, "", "")%></td>
          <td style="width:250px;height:25px"><%=EntityFormEdit.script(facade, CampanhaAutomatizada.FIELD_EMAIL_REMETENTE, campanhaautomatizadaInfo, request, 0, "", "")%></td>
          <td style="width:100px;height:25px"><%=EntityFormEdit.script(facade, CampanhaAutomatizada.FIELD_DIAS_ENVIO, campanhaautomatizadaInfo, request, 0, "", "")%></td>
        </tr>
        <tr>
          <td style="width:auto;height:25px" colspan="3"><%=EntityFieldLabel.script(facade, CampanhaAutomatizada.FIELD_ASSUNTO, request)%></td>
        </tr>
        <tr>
          <td style="width:auto;height:25px" colspan="3"><%=EntityFormEdit.script(facade, CampanhaAutomatizada.FIELD_ASSUNTO, campanhaautomatizadaInfo, request, 0, "", "")%></td>
        </tr>
        <tr>
          <td colspan="3" style="height:25px;"><%=EntityFieldLabel.script(facade, CampanhaAutomatizada.FIELD_MENSAGEM, request)%></td>
        </tr>
        <tr>
          <td colspan="3" style="height:auto"><%=EntityFormMemo.script(facade, CampanhaAutomatizada.FIELD_MENSAGEM, campanhaautomatizadaInfo, request, 0, 0, "", "", false, true)%></td>
        </tr>
        <tr>
          <td colspan="3" style="height:25px">
            <%=CommandControl.formButton(facade, formData, ImageList.COMMAND_SAVE, "Deseja mesmo salvar o registro?", true, false)%>&nbsp;&nbsp;
            <%=CommandControl.entityFormButton(facade, campanhaautomatizada, CampanhaAutomatizada.ACTION_CADASTRO, CampanhaAutomatizada.COMMAND_INSERT, ImageList.COMMAND_INSERT, false)%>&nbsp;&nbsp;
            <%=CommandControl.entityBrowseButton(facade, campanhaautomatizada, CampanhaAutomatizada.ACTION, ImageList.COMMAND_BACK, "Voltar", "Volta para a consulta.", false)%></td>
        </tr>
      </table>
    <%=formData.end()%>
  </body>
</html>

<%}
  // término do bloco protegido
  catch (Exception e) {
    Controller.forwardException(e, pageContext);
  } // try-catch
%>
