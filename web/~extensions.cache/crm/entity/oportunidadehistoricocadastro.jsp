       
<%@include file="../include/beans.jsp"%>

<%@page import="imanager.entity.*"%>

<%
  // início do bloco protegido
  try {
    // nossa instância de OportunidadeHistorico
    OportunidadeHistorico oportunidadeHistorico = (OportunidadeHistorico)facade.getEntity(OportunidadeHistorico.CLASS_NAME);
    // nossa instância de Oportunidade
    Oportunidade oportunidade = (Oportunidade)facade.getEntity(Oportunidade.CLASS_NAME);

    // OportunidadeHistoricoInfo para editar
    OportunidadeHistoricoInfo oportunidadeHistoricoInfo = null;

    // se estamos inserindo...
    if (Controller.isInserting(request)) {
      // info em branco
      oportunidadeHistoricoInfo = new OportunidadeHistoricoInfo();
    }
    // se estamos salvando...
    else if (Controller.isSaving(request)) {
      // info preenchido na página
      oportunidadeHistoricoInfo = (OportunidadeHistoricoInfo)oportunidadeHistorico.entityInfoFromRequest(request);
      // se estamos salvando e é um novo...inclui
      if (Controller.isSavingNew(request)) {
        oportunidadeHistorico.insert(oportunidadeHistoricoInfo);
      }
      // se estamos salvando e é um existente...atualiza
      else {
        oportunidadeHistorico.update(oportunidadeHistoricoInfo);
      } // if
      // compõe a resposta
      Form.composeAjaxResponse(response, Form.COMPOSE_TYPE_JAVA_SCRIPT);
      // atualiza a consulta
      %><%=EntityGrid.forwardBrowse(facade, oportunidadeHistorico, OportunidadeHistorico.ACTION, Controller.isSavingNew(request) ? EntityGrid.OPERATION_INSERT : EntityGrid.OPERATION_UPDATE, oportunidadeHistoricoInfo)%><%
      // dispara
      return;
    }
    // se estamos editando...
    else if (Controller.isEditing(request)) {
      // info do registro para editar
      oportunidadeHistoricoInfo =
        oportunidadeHistorico.selectByPrimaryKey(
          NumberTools.parseInt(request.getParameter(OportunidadeHistorico.FIELD_EMPRESA_ID.getFieldAlias())),
          NumberTools.parseInt(request.getParameter(OportunidadeHistorico.FIELD_OPORTUNIDADE_ID.getFieldAlias())),
          NumberTools.parseInt(request.getParameter(OportunidadeHistorico.FIELD_OPORTUNIDADE_HISTORICO_ID.getFieldAlias()))
        );
    }
    // se o comando é desconhecido...
    else {
      throw new UnknownCommandException();
    } // if

    // nosso FrameBar
    FrameBar frameBar = new FrameBar(facade, "frameBarOportunidadeHistorico");
    // nosso Form de dados
    Form formData = new Form(request, "formDataOportunidadeHistorico", OportunidadeHistorico.ACTION_CADASTRO, OportunidadeHistorico.COMMAND_SAVE, "", true, true);
%>

<html>
  <head>
    <title><%=OportunidadeHistorico.ACTION.getCaption()%></title>
    <link href="<%=facade.getStyle().userInterface()%>" rel="stylesheet" type="text/css">
    <script src="include/scripts.js" type="text/javascript"></script>
  </head>
  <body style="margin:0px;" onselectstart="return true;" oncontextmenu="return false;">

    <!-- inicia o FrameBar -->
    <%=frameBar.begin()%>

      <!-- área de frames -->
      <%=frameBar.beginFrameArea()%>

        <!-- Frame de identificação do objeto -->
        <%=frameBar.actionFrame(OportunidadeHistorico.ACTION)%>

        <!-- Frame de comandos -->
        <%=frameBar.beginFrame("Comandos", false, false)%>
          <table style="width:100%;">
            <tr>
              <td><%=CommandControl.formHyperlink(facade, formData, ImageList.COMMAND_SAVE, "Deseja mesmo salvar o registro?", true, false)%></td>
            </tr>
            <tr>
              <td><%=CommandControl.entityFormHyperlink(facade, oportunidadeHistorico, OportunidadeHistorico.ACTION_CADASTRO, OportunidadeHistorico.COMMAND_INSERT, ImageList.COMMAND_INSERT, false)%></td>
            </tr>
            <tr>
              <td><%=CommandControl.entityBrowseHyperlink(facade, oportunidadeHistorico, OportunidadeHistorico.ACTION, ImageList.COMMAND_BACK, "Voltar", "Volta para a consulta.", false)%></td>
            </tr>
          </table>
        <%=frameBar.endFrame()%>
      <%=frameBar.endFrameArea()%>

      <!-- Form de dados -->
      <%=formData.begin()%>

        <!-- área de dados -->
        <%=frameBar.beginClientArea()%>
          <!-- dados de OportunidadeHistorico -->
          <table>
            <!-- campo Empresa -->
            <tr>
              <td><%=EntityFieldLabel.script(facade, OportunidadeHistorico.FIELD_EMPRESA_ID, request)%></td>
              <td><%=EntityFormEdit.script(facade, OportunidadeHistorico.FIELD_EMPRESA_ID, oportunidadeHistoricoInfo, request, 70, "", "")%></td>
            </tr>
            <!-- campo Oportunidade  -->
            <tr>
              <td><%=EntityFieldLabel.script(facade, OportunidadeHistorico.FIELD_OPORTUNIDADE_ID, request)%></td>
              <td><%=EntityFormEdit.script(facade, OportunidadeHistorico.FIELD_OPORTUNIDADE_ID, oportunidadeHistoricoInfo, request, 70, "", "")%></td>
            </tr>
            <!-- campo Oportunidade Histórico -->
            <tr>
              <td><%=EntityFieldLabel.script(facade, OportunidadeHistorico.FIELD_OPORTUNIDADE_HISTORICO_ID, request)%></td>
              <td><%=EntityFormEdit.script(facade, OportunidadeHistorico.FIELD_OPORTUNIDADE_HISTORICO_ID, oportunidadeHistoricoInfo, request, 70, "", "")%></td>
            </tr>
            <!-- campo Status Anterior -->
            <tr>
              <td><%=EntityFieldLabel.script(facade, OportunidadeHistorico.FIELD_STATUS_ANTERIOR, request)%></td>
              <td><%=EntityFormEdit.script(facade, OportunidadeHistorico.FIELD_STATUS_ANTERIOR, oportunidadeHistoricoInfo, request, 70, "", "")%></td>
            </tr>
            <!-- campo Status -->
            <tr>
              <td><%=EntityFieldLabel.script(facade, OportunidadeHistorico.FIELD_STATUS, request)%></td>
              <td><%=EntityFormEdit.script(facade, OportunidadeHistorico.FIELD_STATUS, oportunidadeHistoricoInfo, request, 70, "", "")%></td>
            </tr>
            <!-- campo Usuário Inclusão -->
            <tr>
              <td><%=EntityFieldLabel.script(facade, OportunidadeHistorico.FIELD_USUARIO_INCLUSAO_ID, request)%></td>
              <td><%=EntityFormEdit.script(facade, OportunidadeHistorico.FIELD_USUARIO_INCLUSAO_ID, oportunidadeHistoricoInfo, request, 70, "", "")%></td>
            </tr>
            <!-- campo Data Hora Inclusão -->
            <tr>
              <td><%=EntityFieldLabel.script(facade, OportunidadeHistorico.FIELD_DATA_HORA_INCLUSAO, request)%></td>
              <td><%=EntityFormEdit.script(facade, OportunidadeHistorico.FIELD_DATA_HORA_INCLUSAO, oportunidadeHistoricoInfo, request, 70, "", "")%></td>
            </tr>
            <tr>
              <td><%=EntityFieldLabel.script(facade, OportunidadeHistorico.FIELD_INFORMACAO, request)%></td>
              <td><%=EntityFormEdit.script(facade, OportunidadeHistorico.FIELD_INFORMACAO, oportunidadeHistoricoInfo, request, 70, "", "")%></td>
            </tr>
          </table>
        <%=frameBar.endClientArea()%>
        
      <%=formData.end()%>
    <%=frameBar.end()%>

  </body>
</html>

<%}
  // término do bloco protegido
  catch (Exception e) {
    Controller.forwardException(e, pageContext);
  } // try-catch
%>
