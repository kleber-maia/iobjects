
<%@include file="../include/beans.jsp"%>

<%@page import="imanager.entity.*"%>
<%@page import="imanager.process.*"%>
<%@page import="imanager.misc.*"%>
<%@page import="imanager.report.*"%>
<%@page import="imanager.ui.entity.*"%>

<%@page import="securityservice.ui.entity.*"%>

<%!
  String HAS_ANOTHER_OWNER = "hasAnotherOwner";
%>

<%
  // início do bloco protegido
  try {
    // nossa instância de Tarefa
    Tarefa tarefa = (Tarefa)facade.getEntity(Tarefa.CLASS_NAME);
    // nossa instância de Tarefa Encaminhamento
    TarefaEncaminhamento tarefaEncaminhamento = (TarefaEncaminhamento)facade.getEntity(TarefaEncaminhamento.CLASS_NAME);
    // nossa instância de Historico Contato
    RelatorioHistoricoContato relatorioHistoricoContato = (RelatorioHistoricoContato)facade.getReport(RelatorioHistoricoContato.CLASS_NAME);

    // TarefaInfo para editar
    TarefaInfo tarefaInfo = null;

    // se estamos inserindo...
    if (Controller.isInserting(request)) {
      // info em branco
      tarefaInfo = new TarefaInfo();
      // valores padrão
      tarefaInfo.setEmpresaId(selectedEmpresaId);
      tarefaInfo.setPrazo(new Timestamp(DateTools.getCalculatedDays(1).getTime()));
      tarefaInfo.setLinkExterno("http://");
    }
    // se estamos salvando...
    else if (Controller.isSaving(request)) {
      // info preenchido na página
      tarefaInfo = (TarefaInfo)tarefa.entityInfoFromRequest(request);
      // se estamos salvando e é um novo...inclui
      if (Controller.isSavingNew(request)) {
        tarefa.insert(tarefaInfo);
      }
      // se estamos salvando e é um existente...atualiza
      else {
        tarefaInfo = tarefa.update(tarefaInfo);
      } // if
      // compõe a resposta
      Form.composeAjaxResponse(response, Form.COMPOSE_TYPE_JAVA_SCRIPT);
      // atualiza a consulta
      %><%=EntityGrid.forwardBrowse(facade, tarefa, Tarefa.ACTION, Controller.isSavingNew(request) ? EntityGrid.OPERATION_INSERT : EntityGrid.OPERATION_UPDATE, tarefaInfo)%><%
      %>updateProrrogacao('<%=NumberTools.format(tarefaInfo.getProrrogacao())%>');<%
      // dispara
      return;
    }
    // se estamos editando...
    else if (Controller.isEditing(request)) {
      // info do registro para editar
      tarefaInfo =
        tarefa.selectByPrimaryKey(
          NumberTools.parseInt(request.getParameter(Tarefa.FIELD_EMPRESA_ID.getFieldAlias())),
          NumberTools.parseInt(request.getParameter(Tarefa.FIELD_TAREFA_ID.getFieldAlias()))
        );
    }
    // se o comando é desconhecido...
    else {
      throw new UnknownCommandException();
    } // if

    // verifica se a Tarefa está associada a outro usuário
    boolean hasAnotherOwner = !Controller.isInserting(request) && (tarefaInfo.getUsuarioId() != facade.getLoggedUser().getId()) && !facade.getLoggedUser().getPrivileged();

    // estamos na central de atendimento?
    Object obj = session.getAttribute(tarefa.CONTROL_PARAM_CENTRAL_ATENDIMENTO.getName());
    boolean isCentralAtendimento = (obj != null) && ((Boolean)obj).booleanValue();

    // nosso FrameBar
    FrameBar frameBar = new FrameBar(facade, "frameBarTarefa");
    // nosso Form de dados
    Form formData = new Form(request, "formDataTarefa", Tarefa.ACTION_CADASTRO, Tarefa.COMMAND_SAVE, "", true, true);
    // nosso PageControl
    PageControl pageControl = new PageControl(facade, "pageControl", true);
%>

<html>
  <head>
    <title><%=Tarefa.ACTION.getCaption()%></title>
    <link href="<%=facade.getStyle().userInterface()%>" rel="stylesheet" type="text/css">
    <script src="include/scripts.js" type="text/javascript"></script>
  </head>
  <body style="margin:0px;" onselectstart="return true;" oncontextmenu="return false;">

    <script type="text/javascript">

      function relatorioHistoricoContato() {
        // contatoId
        var contatoId = document.getElementById("<%=Tarefa.FIELD_CLIENTE_ID.getFieldAlias()%>").value;
        // chama o relatório
        <%=RelatorioHistoricoContato.ACTION.url(RelatorioHistoricoContato.COMMAND_MAKE_REPORT, relatorioHistoricoContato.USER_PARAM_CONTATO_ID.getName() + "=' + contatoId + '")%>
      }

      function updateFields(departamentoId, departamento, usuarioId, usuario) {
        document.getElementById("<%=Tarefa.FIELD_DEPARTAMENTO_ID.getFieldAlias()%>").value = departamentoId;
        document.getElementById("<%=Tarefa.FIELD_DEPARTAMENTO_ID.getFieldAlias() + ExternalLookup.USER%>").value = departamento;
        document.getElementById("<%=Tarefa.FIELD_USUARIO_ID.getFieldAlias()%>").value = usuarioId;
        document.getElementById("<%=Tarefa.FIELD_USUARIO_ID.getFieldAlias() + ExternalLookup.USER%>").value = usuario;
      }
      
      function updateProrrogacao(prorrogacao){
        document.getElementById("<%=Tarefa.FIELD_PRORROGACAO.getFieldAlias()%>").value = prorrogacao;
      }

      function abrirLink() {
        var url = document.getElementById('<%=Tarefa.FIELD_LINK_EXTERNO.getFieldAlias()%>').value;
        if (url == "http://" || url == "")
          return;
        window.open(url,'LinkExterno','resizable=yes,scrollbars=yes,status=no');
      }

    </script>

    <%// se não estamos na central de atendimento...
      if (!isCentralAtendimento) {%>
        <!-- inicia o FrameBar -->
        <%=frameBar.begin()%>

          <!-- área de frames -->
          <%=frameBar.beginFrameArea()%>

            <!-- Frame de identificação do objeto -->
            <%=frameBar.actionFrame(Tarefa.ACTION)%>

            <!-- Frame de comandos -->
            <%=frameBar.beginFrame("Comandos", false, false)%>
              <table style="width:100%;">
                <tr>
                  <td><%=CommandControl.formHyperlink(facade, formData, ImageList.COMMAND_SAVE, "Deseja mesmo salvar o registro?", true, hasAnotherOwner)%></td>
                </tr>
                <tr>
                  <td><%=CommandControl.entityFormHyperlink(facade, tarefa, Tarefa.ACTION_CADASTRO, Tarefa.COMMAND_INSERT, ImageList.COMMAND_INSERT, false)%></td>
                </tr>
                <tr>
                  <td><%=CommandControl.actionCustomScriptHyperlink(facade, RelatorioHistoricoContato.ACTION, RelatorioHistoricoContato.COMMAND_MAKE_REPORT, ImageList.IMAGE_REPORT, "javascript:void(0);", "", "relatorioHistoricoContato()", false)%></td>
                </tr>
                <tr>
                  <td><%=CommandControl.entityBrowseHyperlink(facade, tarefa, Tarefa.ACTION, ImageList.COMMAND_BACK, "Voltar", "Volta para a consulta.", false)%></td>
                </tr>
              </table>
            <%=frameBar.endFrame()%>
          <%=frameBar.endFrameArea()%>
      <%} // if%>

      <!-- Form de dados -->
      <%=formData.begin()%>

        <!-- área de dados -->
        <%=frameBar.beginClientArea()%>
          <!-- dados ocultos -->
          <%=EntityFormEdit.script(facade, Tarefa.FIELD_EMPRESA_ID, tarefaInfo, request, -1, "", "")%>
          <%=EntityFormEdit.script(facade, Tarefa.FIELD_DATA_INCLUSAO, tarefaInfo, request, -1, "", "")%>
          <%=EntityFormEdit.script(facade, Tarefa.FIELD_USUARIO_INCLUSAO_ID, tarefaInfo, request, -1, "", "")%>
          <%=EntityFormEdit.script(facade, Tarefa.FIELD_DATA_HORA_ALTERACAO, tarefaInfo, request, -1, "", "")%>
          <%=EntityFormEdit.script(facade, Tarefa.FIELD_USUARIO_ALTERACAO_ID, tarefaInfo, request, -1, "", "")%>
          <!-- dados de Tarefa -->
          <table style="width:100%; height:100%;" cellpadding="0" cellspacing="0" >
          <%// se a Tarefa possui outro dono...
            if (hasAnotherOwner) {%>
            <tr style="height:10px;">
              <td class="Info"><img alt="" src="images/information16x16.png" align="absmiddle" /> Esta Tarefa somente poderá ser editada pelo Usuário ao qual está associada.</td>
            </tr>
          <%} // if%>
            <tr style="height:50px;">
              <td>

                <%=GroupBox.begin("Identificação")%>
                  <table style="width:100%; table-layout: fixed;">
                    <tr>
                      <td style="width:35px;"><%=EntityFieldLabel.script(facade, Tarefa.FIELD_TAREFA_ID, request, "", "", true)%></td>
                      <td style="width:auto;"><%=EntityLookupLabel.script(facade, Tarefa.LOOKUP_CLIENTE, request, "linkCliente", "", Contato.ACTION, false)%></td>
                      <td style="width:300px;"><%=EntityLookupLabel.script(facade, Tarefa.LOOKUP_DEPARTAMENTO, request, "linkDepartamento", "", Departamento.ACTION, false)%></td>
                      <td style="width:300px;"><%=EntityLookupLabel.script(facade, Tarefa.LOOKUP_ASSUNTO, request, "linkAssunto", "", Assunto.ACTION, false)%></td>
                    </tr>
                    <tr>
                      <td><%=EntityFormEdit.script(facade, Tarefa.FIELD_TAREFA_ID, tarefaInfo, request, 0, "", "", true, false)%></td>
                      <td><%=ContatoUI.lookupSearchForLookup(facade, Tarefa.LOOKUP_CLIENTE, tarefaInfo, NaoSim.SIM, NaoSim.TODOS, NaoSim.TODOS, NaoSim.TODOS, NaoSim.TODOS, 0, "", "", false)%></td>
                      <td>
                        <%if (Controller.isInserting(request)) {%>
                          <%=DepartamentoUI.lookupListForLookup(facade, Tarefa.LOOKUP_DEPARTAMENTO, tarefaInfo, selectedEmpresaId, 0, "", "")%>
                        <%}
                          else {%>
                          <%=EntityFormEdit.script(facade, Tarefa.FIELD_DEPARTAMENTO_ID, tarefaInfo, request, -1, "", "")%>
                          <%=FormEdit.script(facade, Tarefa.FIELD_DEPARTAMENTO_ID.getFieldAlias() + ExternalLookup.USER, tarefaInfo.lookupValueList().get(Tarefa.LOOKUP_DEPARTAMENTO).getDisplayFieldValuesToString(), 0, 0, FormEdit.ALIGN_LEFT, "", "", "", "", "", true)%>
                        <%} // if%>
                      </td>
                      <td><%=EntityFormLookupSelectEx.script(facade, Tarefa.CLASS_NAME, Tarefa.LOOKUP_ASSUNTO, Assunto.LEVEL_DELIMITER, tarefaInfo, request, 0, "", "", true)%></td>
                    </tr>
                  </table>
                <%=GroupBox.end()%>

              </td>
            </tr>
            <tr style="height:50px;">
              <td>

                <table style="width:100%; table-layout: fixed;" cellpadding="0" cellspacing="0" >
                  <tr>
                    <td style="width:60%;">

                      <%=GroupBox.begin("Tarefa")%>
                        <table style="width:100%; table-layout: fixed;">
                          <tr>
                            <td style="width:auto;"><%=EntityLookupLabel.script(facade, Tarefa.LOOKUP_USUARIO, request)%></td>
                            <td style="width:100px;"><%=EntityFieldLabel.script(facade, Tarefa.FIELD_PRAZO_ORIGINAL, request, "", "", true)%></td>
                            <td style="width:100px;"><%=EntityFieldLabel.script(facade, Tarefa.FIELD_PRAZO, request)%></td>
                            <td style="width:70px;"><%=EntityFieldLabel.script(facade, Tarefa.FIELD_PRORROGACAO, request)%></td>
                            <td style="width:200px;"><%=EntityFieldLabel.script(facade, Tarefa.FIELD_STATUS, request)%></td>
                          </tr>
                          <tr>
                            <td><%=UsuarioUI.lookupSearchForLookup(facade, Tarefa.LOOKUP_USUARIO, tarefaInfo, 0, "", "", false)%></td>
                            <td><%=EntityFormEdit.script(facade, Tarefa.FIELD_PRAZO_ORIGINAL, tarefaInfo, request, 0, "", "", true, false)%></td>
                            <td><%=EntityFormEdit.script(facade, Tarefa.FIELD_PRAZO, tarefaInfo, request, 0, "", "")%></td>
                            <td><%=EntityFormEdit.script(facade, Tarefa.FIELD_PRORROGACAO, tarefaInfo, request, 0, "", "")%></td>
                            <td><%=EntityFormSelect.script(facade, Tarefa.FIELD_STATUS, tarefaInfo, request, 0, "", "", false)%></td>
                          </tr>
                        </table>
                      <%=GroupBox.end()%>

                    </td>
                    <td style="width:40%;">

                       <%// se não estamos na central de atendimento...
                         if (!isCentralAtendimento) {%>
                          <!-- campos Usuários e Datas -->
                          <%=GroupBox.begin("Segurança")%>
                            <table style="width:100%; table-layout: fixed;">
                              <tr>
                                <td style="width:25%;"><%=EntityFieldLabel.script(facade, Tarefa.FIELD_USUARIO_INCLUSAO_ID, request)%></td>
                                <td style="width:25%;"><%=EntityFieldLabel.script(facade, Tarefa.FIELD_USUARIO_ALTERACAO_ID, request)%></td>
                              </tr>
                              <tr>
                                <td><%=FormEdit.script(facade, "inclusao", (tarefaInfo.getUsuarioInclusaoId() > 0 ? facade.securityService().getUser(tarefaInfo.getUsuarioInclusaoId()).getName() : "") + " - " + DateTools.formatDate(tarefaInfo.getDataInclusao()), 0, 0, FormEdit.ALIGN_LEFT, "", "", "", "", "", true)%></td>
                                <td><%=FormEdit.script(facade, "alteracao", (tarefaInfo.getUsuarioAlteracaoId() > 0 ? facade.securityService().getUser(tarefaInfo.getUsuarioAlteracaoId()).getName() : "") + " - " + DateTools.formatDateTime(tarefaInfo.getDataHoraAlteracao()), 0, 0, FormEdit.ALIGN_LEFT, "", "", "", "", "", true)%></td>
                              </tr>
                            </table>
                          <%=GroupBox.end()%>
                          <%}
                        else {%>
                          <%=GroupBox.begin("Link Externo")%>
                          <table style="width:100%; table-layout: fixed;">
                            <tr>
                              <td style="width: auto;"><%=EntityFieldLabel.script(facade, Tarefa.FIELD_LINK_EXTERNO, request)%></td>
                            </tr>
                            <tr>
                              <td><%=EntityFormEdit.script(facade, Tarefa.FIELD_LINK_EXTERNO, tarefaInfo, request, 0, "", "")%></td><td style="width: 4%;"><%=Button.script(facade, "botaoAbrir", "Abrir", "Abrir", "", "", Button.KIND_DEFAULT, "", "abrirLink()", false)%></td>
                            </tr>
                          </table>
                        <%=GroupBox.end()%>
                      <%} // if%>

                    </td>
                  </tr>
                </table>
              </td>
            </tr>

            <%// se não estamos na central de atendimento...
              if (!isCentralAtendimento) {%>
                <tr style="height:15px">
                  <td style="width:100%">
                    <%=GroupBox.begin("Link Externo")%>
                      <table style="width:100%; table-layout: fixed;">
                        <tr>
                          <td style="width: auto;"><%=EntityFieldLabel.script(facade, Tarefa.FIELD_LINK_EXTERNO, request)%></td>
                        </tr>
                        <tr>
                          <td><%=EntityFormEdit.script(facade, Tarefa.FIELD_LINK_EXTERNO, tarefaInfo, request, 0, "", "")%></td><td style="width: 4%;"><%=Button.script(facade, "botaoAbrir", "Abrir", "Abrir", "", "", Button.KIND_DEFAULT, "", "abrirLink()", false)%></td>
                        </tr>
                      </table>
                    <%=GroupBox.end()%>
                  </td>
                </tr>
            <%} // if%>

            <tr style="height:auto;">
              <td style="width:100%;">

                <%=pageControl.begin()%>
                  <!-- Descrição -->
                  <%=pageControl.beginTabSheet("Descrição")%>
                    <%=EntityFormMemo.script(facade, Tarefa.FIELD_DESCRICAO, tarefaInfo, request, 0, 0, "", "", false, true)%>
                  <%=pageControl.endTabSheet()%>
                  <!-- Encaminhamento -->
                  <%// se estamos inserindo
                    if (Controller.isInserting(request)) {%>
                    <%=pageControl.beginTabSheet("Encaminhamento")%>
                      <p>Não disponível durante a inclusão.</p>
                    <%=pageControl.endTabSheet()%>
                  <%}
                    else {%>
                    <%=pageControl.iFrameTabSheet("Encaminhamento", TarefaEncaminhamento.ACTION.url(tarefaEncaminhamento.USER_PARAM_EMPRESA_ID.getName() + "=" + tarefaInfo.getEmpresaId() + "&" + tarefaEncaminhamento.USER_PARAM_TAREFA_ID.getName() + "=" + tarefaInfo.getTarefaId() + (hasAnotherOwner ? "&" + HAS_ANOTHER_OWNER + "=true" : "")))%>
                  <%} // if%>
                <%=pageControl.end()%>

              </td>
            </tr>

               <%// se estamos na central de atendimento...
                 if (isCentralAtendimento) {%>
                  <tr style="height:15px">
                    <td style="width:100%">
                      <table cellpadding="0" cellspacing="0">
                        <tr>
                          <td><%=CommandControl.formButton(facade, formData, ImageList.COMMAND_SAVE, "Deseja mesmo salvar o registro?", true, hasAnotherOwner)%></td>
                          <td>&nbsp;</td>
                          <td><%=CommandControl.entityBrowseButton(facade, tarefa, Tarefa.ACTION, ImageList.COMMAND_BACK, "Voltar", "Volta para a consulta.", false)%></td>
                        </tr>
                      </table>
                    </td>
                  </tr>
               <%} // if%>

          </table>
        <%=frameBar.endClientArea()%>

      <%=formData.end()%>
    <%if(!isCentralAtendimento) {%>
      <%=frameBar.end()%>
    <%} // if%>

  </body>
</html>

<%}
  // término do bloco protegido
  catch (Exception e) {
    Controller.forwardException(e, pageContext);
  } // try-catch
%>
