
<%@page import="imanager.misc.NaoSim"%>
<%@include file="../include/beans.jsp"%>

<%@page import="imanager.entity.*"%>

<%
  // início do bloco protegido
  try {
    // nossa instância de Campanha
    Campanha campanha = (Campanha)facade.getEntity(Campanha.CLASS_NAME);

    // CampanhaInfo para editar
    CampanhaInfo campanhaInfo = null;

    // se estamos inserindo...
    if (Controller.isInserting(request)) {
      // info em branco
      campanhaInfo = new CampanhaInfo();
    }
    // se estamos salvando...
    else if (Controller.isSaving(request)) {
      // info preenchido na página
      campanhaInfo = (CampanhaInfo)campanha.entityInfoFromRequest(request);
      // se estamos salvando e é um novo...inclui
      if (Controller.isSavingNew(request)) {
        campanha.insert(campanhaInfo);
      }
      // se estamos salvando e é um existente...atualiza
      else {
        campanha.update(campanhaInfo);
      } // if
      // compõe a resposta
      Form.composeAjaxResponse(response, Form.COMPOSE_TYPE_JAVA_SCRIPT);
      // atualiza a consulta
      %><%=EntityGrid.forwardBrowse(facade, campanha, Campanha.ACTION, Controller.isSavingNew(request) ? EntityGrid.OPERATION_INSERT : EntityGrid.OPERATION_UPDATE, campanhaInfo)%><%
      // dispara
      return;
    }
    // se estamos editando...
    else if (Controller.isEditing(request)) {
      // info do registro para editar
      campanhaInfo =
        campanha.selectByPrimaryKey(
          NumberTools.parseInt(request.getParameter(Campanha.FIELD_CAMPANHA_ID.getFieldAlias()))
        );
    }
    // se o comando é desconhecido...
    else {
      throw new UnknownCommandException();
    } // if

    // nosso FrameBar
    FrameBar frameBar = new FrameBar(facade, "frameBarCampanha");
    // nosso Form de dados
    Form formData = new Form(request, "formDataCampanha", Campanha.ACTION_CADASTRO, Campanha.COMMAND_SAVE, "", true, true);
    // nosso PageControl
    PageControl pageControl = new PageControl(facade, "pageControl", true);
    
    CampanhaAutomatizada campanhaAutomatizada = (CampanhaAutomatizada)facade.getEntity(CampanhaAutomatizada.CLASS_NAME);
%>

<html>
  <head>
    <title><%=Campanha.ACTION.getCaption()%></title>
    <link href="<%=facade.getStyle().userInterface()%>" rel="stylesheet" type="text/css">
    <script src="include/scripts.js" type="text/javascript"></script>
  </head>
  <body style="margin:0px;" onselectstart="return true;" oncontextmenu="return true;">

    <!-- inicia o FrameBar -->
    <%=frameBar.begin()%>

      <!-- área de frames -->
      <%=frameBar.beginFrameArea()%>

        <!-- Frame de identificação do objeto -->
        <%=frameBar.actionFrame(Campanha.ACTION)%>

        <!-- Frame de comandos -->
        <%=frameBar.beginFrame("Comandos", false, false)%>
          <table style="width:100%;">
            <tr>
              <td><%=CommandControl.formHyperlink(facade, formData, ImageList.COMMAND_SAVE, "Deseja mesmo salvar o registro?", true, false)%></td>
            </tr>
            <tr>
              <td><%=CommandControl.entityFormHyperlink(facade, campanha, Campanha.ACTION_CADASTRO, Campanha.COMMAND_INSERT, ImageList.COMMAND_INSERT, false)%></td>
            </tr>
            <tr>
              <td><%=CommandControl.entityBrowseHyperlink(facade, campanha, Campanha.ACTION, ImageList.COMMAND_BACK, "Voltar", "Volta para a consulta.", false)%></td>
            </tr>
          </table>
        <%=frameBar.endFrame()%>
      <%=frameBar.endFrameArea()%>

      <!-- Form de dados -->
      <%=formData.begin()%>

        <!-- área de dados -->
        <%=frameBar.beginClientArea()%>
          <!-- dados ocultos -->
          <%=EntityFormEdit.script(facade, Campanha.FIELD_CAMPANHA_ID, campanhaInfo, request, -1, "", "")%>
          <%=EntityFormEdit.script(facade, Campanha.FIELD_USUARIO_INCLUSAO_ID, campanhaInfo, request, -1, "", "")%>
          <%=EntityFormEdit.script(facade, Campanha.FIELD_USUARIO_ALTERACAO_ID, campanhaInfo, request, -1, "", "")%>
          <%=EntityFormEdit.script(facade, Campanha.FIELD_DATA_HORA_ALTERACAO, campanhaInfo, request, -1, "", "")%>
          <%=EntityFormEdit.script(facade, Campanha.FIELD_DATA_INCLUSAO, campanhaInfo, request, -1, "", "")%>
          <!-- dados de Campanha -->
          <table style="width:100%; height:100%;">
            <tr style="height:50px;">
              <td style="width:auto">
                <!-- campos Nome e Arquivo -->
                <%=GroupBox.begin("Identificação")%>
                  <table style="width:100%; table-layout: fixed;">
                    <tr>
                      <td style="width:auto;"><%=EntityFieldLabel.script(facade, Campanha.FIELD_NOME, request)%></td>
                      <td style="width:90px;"><%=EntityFieldLabel.script(facade, Campanha.FIELD_DATA_INICIO, request)%></td>
                      <td style="width:90px;"><%=EntityFieldLabel.script(facade, Campanha.FIELD_DATA_TERMINO, request)%></td>
                      <td style="width:100px;"><%=EntityFieldLabel.script(facade, Campanha.FIELD_VALOR_INVESTIDO, request)%></td>
                    </tr>
                    <tr>
                      <td><%=EntityFormEdit.script(facade, Campanha.FIELD_NOME, campanhaInfo, request, 0, "", "")%></td>
                      <td><%=EntityFormEdit.script(facade, Campanha.FIELD_DATA_INICIO, campanhaInfo, request, 0, "", "")%></td>
                      <td><%=EntityFormEdit.script(facade, Campanha.FIELD_DATA_TERMINO, campanhaInfo, request, 0, "", "")%></td>
                      <td><%=EntityFormEdit.script(facade, Campanha.FIELD_VALOR_INVESTIDO, campanhaInfo, request, 0, "", "")%></td>
                    </tr>
                  </table>
                <%=GroupBox.end()%>
              </td>
              <%if(facade.getLoggedUser().getSuperUser()) {%>
                <td style="width:200px;">
                  <!-- campos Nome e Arquivo -->
                  <%=GroupBox.begin("Follow Up")%>
                    <table style="width:100%; table-layout: fixed;">
                      <tr>
                        <td style="width:50%;"><%=EntityFieldLabel.script(facade, Campanha.FIELD_AUTOMATIZADA, request, "", "", !Controller.isInserting(request))%></td>
                        <td style="width:50%;"><%=EntityFieldLabel.script(facade, Campanha.FIELD_DIAS_PERDIDA, request, "label" + Campanha.FIELD_DIAS_PERDIDA.getFieldAlias() + "", "", Controller.isInserting(request) ? false : campanhaInfo.getAutomatizada() == NaoSim.NAO)%></td>
                      </tr>
                      <tr>
                        <%if (!Controller.isInserting(request)) {%>
                        <td><%=EntityFormEdit.script(facade, Campanha.FIELD_AUTOMATIZADA, campanhaInfo, request, 0, "", "", true, false)%></td>
                        <%}
                             else {%>
                        <td><%=EntityFormSelect.script(facade, Campanha.FIELD_AUTOMATIZADA, campanhaInfo, request, 0, "", "", false)%></td>
                        <%}%>
                        <td><%=EntityFormEdit.script(facade, Campanha.FIELD_DIAS_PERDIDA, campanhaInfo, request, 0, "", "", Controller.isInserting(request) ? false : campanhaInfo.getAutomatizada() == NaoSim.NAO, false)%></td>
                      </tr>
                    </table>
                  <%=GroupBox.end()%>
                </td>
              <%}
                else {%>
                  <%=EntityFormEdit.script(facade, Campanha.FIELD_DIAS_PERDIDA, campanhaInfo, request, -1, "", "", false, false)%>
                  <%=EntityFormEdit.script(facade, Campanha.FIELD_AUTOMATIZADA, campanhaInfo, request, -1, "", "", false, false)%>
              <%}%>
              <td style="width:100px">
                <!-- campos Nome e Arquivo -->
                <%=GroupBox.begin("Arquivo")%>
                  <table style="width:100%; table-layout: fixed;">
                    <tr>
                      <td><%=EntityFieldLabel.script(facade, Campanha.FIELD_ARQUIVO, request)%></td>
                    </tr>
                    <tr>
                      <td><%=EntityFormSelect.script(facade, Campanha.FIELD_ARQUIVO, campanhaInfo, request, 0, "", "", false)%></td>
                    </tr>
                  </table>
                <%=GroupBox.end()%>
              </td>
            </tr>
            <tr style="height:<%=campanhaInfo.getAutomatizada() == NaoSim.SIM && facade.getLoggedUser().getSuperUser() ? "100px" : "auto"%>;">
              <td colspan="3">
                <table style="width:100%;height:100%">
                  <tr>
                    <td style="height:25px"><%=EntityFieldLabel.script(facade, Campanha.FIELD_DESCRICAO, request)%></td>
                  </tr>
                  <tr>
                    <td style="height:auto"><%=EntityFormMemo.script(facade, Campanha.FIELD_DESCRICAO, campanhaInfo, request, 0, 0, "", "", false, false)%></td>
                  </tr>
                </table>
              </td>
            </tr>
            <tr style="height:50px">
              <td colspan="3">
                <%=GroupBox.begin("Segurança")%>
                  <table style="width:100%;">
                    <tr>
                      <td style="width:50%;"><%=EntityFieldLabel.script(facade, Campanha.FIELD_USUARIO_INCLUSAO_ID, request)%></td>
                      <td style="width:50%;"><%=EntityFieldLabel.script(facade, Campanha.FIELD_USUARIO_ALTERACAO_ID, request)%></td>
                    </tr>
                    <tr>
                      <td><%=FormEdit.script(facade, "inclusao", (campanhaInfo.getUsuarioInclusaoId() > 0 ? facade.securityService().getUser(campanhaInfo.getUsuarioInclusaoId()).getName() : "") + " - " + DateTools.formatDate(campanhaInfo.getDataInclusao()), 0, 0, FormEdit.ALIGN_LEFT, "", "", "", "", "", true)%></td>
                      <td><%=FormEdit.script(facade, "alteracao", (campanhaInfo.getUsuarioAlteracaoId() > 0 ? facade.securityService().getUser(campanhaInfo.getUsuarioAlteracaoId()).getName() : "") + " - " + DateTools.formatDateTime(campanhaInfo.getDataHoraAlteracao()), 0, 0, FormEdit.ALIGN_LEFT, "", "", "", "", "", true)%></td>
                    </tr>
                  </table>
                <%=GroupBox.end()%>
              </td>
            </tr>
            <%if(campanhaInfo.getAutomatizada() == NaoSim.SIM && facade.getLoggedUser().getSuperUser()) {%>
              <tr style="height:auto">
                <td colspan="3">
                  <%=pageControl.begin()%>
                    <!-- Automatizada -->
                    <%if (campanhaInfo.getCampanhaId()== 0) {%>
                      <%=pageControl.beginTabSheet("Automatizada")%>
                        Não disponível durante a inclusão.
                      <%=pageControl.endTabSheet()%>
                    <%}
                      else {%>
                        <%=pageControl.iFrameTabSheet("Automatizada", CampanhaAutomatizada.ACTION.url(campanhaAutomatizada.USER_PARAM_CAMPANHA_ID.getName() + "=" + campanhaInfo.getCampanhaId()))%>
                    <%} // if%>                
                  <%=pageControl.end()%>
                </td>
              </tr>
            <%}%>
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
