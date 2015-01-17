
<%@include file="../include/beans.jsp"%>

<%@page import="imanager.entity.*"%>
<%@page import="imanager.misc.*"%>
<%@page import="imanager.report.*"%>
<%@page import="imanager.ui.entity.*"%>

<%
  // início do bloco protegido
  try {
    // nossa instância de Atendimento
    Atendimento atendimento = (Atendimento)facade.getEntity(Atendimento.CLASS_NAME);
    // nossa instância de Historico Contato
    RelatorioHistoricoContato relatorioHistoricoContato = (RelatorioHistoricoContato)facade.getReport(RelatorioHistoricoContato.CLASS_NAME);

    // AtendimentoInfo para editar
    AtendimentoInfo atendimentoInfo = null;

    // se estamos inserindo...
    if (Controller.isInserting(request)) {
      // info em branco
      atendimentoInfo = new AtendimentoInfo();
      // valores padrão
      atendimentoInfo.setEmpresaId(selectedEmpresaId);
      atendimentoInfo.setDataHoraInicio(new Timestamp(System.currentTimeMillis()));
      atendimentoInfo.setLinkExterno("http://");
    }
    // se estamos salvando...
    else if (Controller.isSaving(request)) {
      // info preenchido na página
      atendimentoInfo = (AtendimentoInfo)atendimento.entityInfoFromRequest(request);
      // valores padrão
      if (atendimentoInfo.getDataHoraTermino().equals(DateTools.ZERO_DATE))
        atendimentoInfo.setDataHoraTermino(new Timestamp(System.currentTimeMillis()));
      // se estamos salvando e é um novo...inclui
      if (Controller.isSavingNew(request)) {
        atendimento.insert(atendimentoInfo);
      }
      // se estamos salvando e é um existente...atualiza
      else {
        atendimento.update(atendimentoInfo);
      } // if
      // compõe a resposta
      Form.composeAjaxResponse(response, Form.COMPOSE_TYPE_JAVA_SCRIPT);
      // atualiza a consulta
      %><%=EntityGrid.forwardBrowse(facade, atendimento, Atendimento.ACTION, Controller.isSavingNew(request) ? EntityGrid.OPERATION_INSERT : EntityGrid.OPERATION_UPDATE, atendimentoInfo)%><%
      // dispara
      return;
    }
    // se estamos editando...
    else if (Controller.isEditing(request)) {
      // info do registro para editar
      atendimentoInfo =
        atendimento.selectByPrimaryKey(
          NumberTools.parseInt(request.getParameter(Atendimento.FIELD_EMPRESA_ID.getFieldAlias())),
          NumberTools.parseInt(request.getParameter(Atendimento.FIELD_ATENDIMENTO_ID.getFieldAlias()))
        );
    }
    // se o comando é desconhecido...
    else {
      throw new UnknownCommandException();
    } // if

    // estamos na central de atendimento?
    Object obj = session.getAttribute(atendimento.CONTROL_PARAM_CENTRAL_ATENDIMENTO.getName());
    boolean isCentralAtendimento = (obj != null) && ((Boolean)obj).booleanValue();

    // nosso FrameBar
    FrameBar frameBar = new FrameBar(facade, "frameBarAtendimento");
    // nosso Form de dados
    Form formData = new Form(request, "formDataAtendimento", Atendimento.ACTION_CADASTRO, Atendimento.COMMAND_SAVE, "", true, true);
    // nosso PageControl
    PageControl pageControl = new PageControl(facade, "pageControl", true);
%>

<html>
  <head>
    <title><%=Atendimento.ACTION.getCaption()%></title>
    <link href="<%=facade.getStyle().userInterface()%>" rel="stylesheet" type="text/css">
    <script src="include/scripts.js" type="text/javascript"></script>
  </head>
  <body style="margin:0px;" onselectstart="return true;" oncontextmenu="return false;">

    <script type="text/javascript">

      function relatorioHistoricoContato() {
        // contatoId
        var contatoId = document.getElementById("<%=Atendimento.FIELD_CLIENTE_ID.getFieldAlias()%>").value;
        // chama o relatório
        <%=RelatorioHistoricoContato.ACTION.url(RelatorioHistoricoContato.COMMAND_MAKE_REPORT, relatorioHistoricoContato.USER_PARAM_CONTATO_ID.getName() + "=' + contatoId + '")%>
      }

      function abrirLink() {
        var url = document.getElementById('<%=Atendimento.FIELD_LINK_EXTERNO.getFieldAlias()%>').value;
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
          <%=frameBar.actionFrame(Atendimento.ACTION)%>

          <!-- Frame de comandos -->
          <%=frameBar.beginFrame("Comandos", false, false)%>
            <table style="width:100%;">
              <tr>
                <td><%=CommandControl.formHyperlink(facade, formData, ImageList.COMMAND_SAVE, "Deseja mesmo salvar o registro?", true, false)%></td>
              </tr>
              <tr>
                <td><%=CommandControl.entityFormHyperlink(facade, atendimento, Atendimento.ACTION_CADASTRO, Atendimento.COMMAND_INSERT, ImageList.COMMAND_INSERT, false)%></td>
              </tr>
              <tr>
                <td><%=CommandControl.actionCustomScriptHyperlink(facade, RelatorioHistoricoContato.ACTION, RelatorioHistoricoContato.COMMAND_MAKE_REPORT, ImageList.IMAGE_REPORT, "javascript:void(0);", "", "relatorioHistoricoContato()", false)%></td>
              </tr>
              <tr>
                <td><%=CommandControl.entityBrowseHyperlink(facade, atendimento, Atendimento.ACTION, ImageList.COMMAND_BACK, "Voltar", "Volta para a consulta.", false)%></td>
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
          <%=EntityFormEdit.script(facade, Atendimento.FIELD_EMPRESA_ID, atendimentoInfo, request, -1, "", "")%>
          <%=EntityFormEdit.script(facade, Atendimento.FIELD_DATA_INCLUSAO, atendimentoInfo, request, -1, "", "")%>
          <%=EntityFormEdit.script(facade, Atendimento.FIELD_USUARIO_INCLUSAO_ID, atendimentoInfo, request, -1, "", "")%>
          <%=EntityFormEdit.script(facade, Atendimento.FIELD_DATA_HORA_ALTERACAO, atendimentoInfo, request, -1, "", "")%>
          <%=EntityFormEdit.script(facade, Atendimento.FIELD_USUARIO_ALTERACAO_ID, atendimentoInfo, request, -1, "", "")%>
          <!-- dados de Atendimento -->
          <table style="width:100%; height:100%" cellpadding="0" cellspacing="0">
            <tr style="height:50px;">
              <td>

                <%=GroupBox.begin("Identificação")%>
                  <table style="width:100%; table-layout: fixed;">
                    <tr>
                      <td style="width:35px;"><%=EntityFieldLabel.script(facade, Atendimento.FIELD_ATENDIMENTO_ID, request, "", "", true)%></td>
                      <td style="width:auto;"><%=EntityLookupLabel.script(facade, Atendimento.LOOKUP_CLIENTE, request, "linkCliente", "", Contato.ACTION, false)%></td>
                      <td style="width:200px;"><%=EntityLookupLabel.script(facade, Atendimento.LOOKUP_DEPARTAMENTO, request, "linkDepartamento", "", Departamento.ACTION, false)%></td>
                      <td style="width:200px;"><%=EntityLookupLabel.script(facade, Atendimento.LOOKUP_ASSUNTO, request, "linkAssunto", "", Assunto.ACTION, false)%></td>
                      <td style="width:200px;"><%=EntityLookupLabel.script(facade, Atendimento.LOOKUP_CAMPANHA, request, "linkCampanha", "", Campanha.ACTION, false)%></td>
                    </tr>
                    <tr>
                      <td><%=EntityFormEdit.script(facade, Atendimento.FIELD_ATENDIMENTO_ID, atendimentoInfo, request, 0, "", "", true, false)%></td>
                      <td><%=ContatoUI.lookupSearchForLookup(facade, Atendimento.LOOKUP_CLIENTE, atendimentoInfo, NaoSim.SIM, NaoSim.TODOS, NaoSim.TODOS, NaoSim.TODOS, NaoSim.TODOS, 0, "", "", false)%></td>
                      <td><%=DepartamentoUI.lookupListForLookup(facade, Atendimento.LOOKUP_DEPARTAMENTO, atendimentoInfo, selectedEmpresaId, 0, "", "")%></td>
                      <td><%=EntityFormLookupSelectEx.script(facade, Atendimento.CLASS_NAME, Atendimento.LOOKUP_ASSUNTO, Assunto.LEVEL_DELIMITER, atendimentoInfo, request, 0, "", "", true)%></td>
                      <td><%=CampanhaUI.lookupListForLookup(facade, Atendimento.LOOKUP_CAMPANHA, atendimentoInfo, NaoSim.NAO, 0, "", "")%></td>
                    </tr>
                  </table>
                <%=GroupBox.end()%>

              </td>
            </tr>
            <tr style="height:50px;">
              <td>

                <table style="width:100%; table-layout: fixed;" cellpadding="0" cellspacing="0">
                  <tr>
                    <td style="width:50%;">

                      <%=GroupBox.begin("Atendimento")%>
                        <table style="width:100%; table-layout: fixed;">
                          <tr>
                            <td style="width:100px;"><%=EntityFieldLabel.script(facade, Atendimento.FIELD_DATA_HORA_INICIO, request)%></td>
                            <td style="width:100px;"><%=EntityFieldLabel.script(facade, Atendimento.FIELD_DATA_HORA_TERMINO, request)%></td>
                            <td style="width:auto;"><%=EntityLookupLabel.script(facade, Atendimento.LOOKUP_MEIO, request, "linkMeio", "", Meio.ACTION, false)%></td>
                          </tr>
                          <tr>
                            <td><%=EntityFormEdit.script(facade, Atendimento.FIELD_DATA_HORA_INICIO, atendimentoInfo, request, 0, "", "")%></td>
                            <td><%=EntityFormEdit.script(facade, Atendimento.FIELD_DATA_HORA_TERMINO, atendimentoInfo, request, 0, "", "")%></td>
                            <td><%=EntityFormLookupSelect.script(facade, Atendimento.CLASS_NAME, Atendimento.LOOKUP_MEIO, atendimentoInfo, request, 0, "", "", true)%></td>
                          </tr>
                        </table>
                      <%=GroupBox.end()%>

                    </td>
                    <td style="width:50%;">

                      <%// se não estamos na central de atendimento...
                      if (!isCentralAtendimento) {%>
                        <!-- campos Usuários e Datas -->
                        <%=GroupBox.begin("Segurança")%>
                          <table style="width:100%; table-layout: fixed;">
                            <tr>
                              <td style="width:25%;"><%=EntityFieldLabel.script(facade, Atendimento.FIELD_USUARIO_INCLUSAO_ID, request)%></td>
                              <td style="width:25%;"><%=EntityFieldLabel.script(facade, Atendimento.FIELD_USUARIO_ALTERACAO_ID, request)%></td>
                            </tr>
                            <tr>
                              <td><%=FormEdit.script(facade, "inclusao", (atendimentoInfo.getUsuarioInclusaoId() > 0 ? facade.securityService().getUser(atendimentoInfo.getUsuarioInclusaoId()).getName() : "") + " - " + DateTools.formatDate(atendimentoInfo.getDataInclusao()), 0, 0, FormEdit.ALIGN_LEFT, "", "", "", "", "", true)%></td>
                              <td><%=FormEdit.script(facade, "alteracao", (atendimentoInfo.getUsuarioAlteracaoId() > 0 ? facade.securityService().getUser(atendimentoInfo.getUsuarioAlteracaoId()).getName() : "") + " - " + DateTools.formatDateTime(atendimentoInfo.getDataHoraAlteracao()), 0, 0, FormEdit.ALIGN_LEFT, "", "", "", "", "", true)%></td>
                            </tr>
                          </table>
                        <%=GroupBox.end()%>
                    <%} // if
                      else {%>
                        <%=GroupBox.begin("Link Externo")%>
                        <table style="width:100%; table-layout: fixed;">
                          <tr>
                            <td style="width: auto;"><%=EntityFieldLabel.script(facade, Atendimento.FIELD_LINK_EXTERNO, request)%></td>
                          </tr>
                          <tr>
                            <td><%=EntityFormEdit.script(facade, Atendimento.FIELD_LINK_EXTERNO, atendimentoInfo, request, 0, "", "")%></td><td style="width:4%;"><%=Button.script(facade, "botaoAbrir", "Abrir", "Abrir", "", "", Button.KIND_DEFAULT, "", "abrirLink()", false)%></td>
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
                      <table style="width:100%">
                        <tr>
                          <td style="width: auto;"><%=EntityFieldLabel.script(facade, Atendimento.FIELD_LINK_EXTERNO, request)%></td>
                        </tr>
                        <tr>
                          <td><%=EntityFormEdit.script(facade, Atendimento.FIELD_LINK_EXTERNO, atendimentoInfo, request, 0, "", "")%></td><td style="width: 4%;"><%=Button.script(facade, "botaoAbrir", "Abrir", "Abrir", "", "", Button.KIND_DEFAULT, "", "abrirLink()", false)%></td>
                        </tr>
                      </table>
                    <%=GroupBox.end()%>
                  </td>
                </tr>
            <%} // if%>

            <tr style="height:auto;">
              <td style="width:100%;">

                <%=pageControl.begin()%>
                  <%=pageControl.beginTabSheet("Descrição")%>
                    <%=EntityFormMemo.script(facade, Atendimento.FIELD_DESCRICAO, atendimentoInfo, request, 0, 0, "", "", false, true)%>
                  <%=pageControl.endTabSheet()%>
                <%=pageControl.end()%>

              </td>
            </tr>

                <%// se estamos na central de atendimento...
                  if (isCentralAtendimento) {%>
                    <tr style="height:15px">
                      <td style="width:100%">
                        <table cellpadding="0" cellspacing="0">
                          <tr>
                            <td><%=CommandControl.entityBrowseButton(facade, atendimento, Atendimento.ACTION, ImageList.COMMAND_BACK, "Voltar", "Volta para a consulta.", false)%></td>
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

    <script type="text/javascript">
/*      
      // nosso Id
      var wysiwygId = "<%=Atendimento.FIELD_DESCRICAO.getFieldAlias()%>";
      // toolbar do Wysiwyg
      var toolbar = document.getElementById("toolbar" + wysiwygId);
      // wysiwyg
      var wysiwyg = document.getElementById("wysiwyg" + wysiwygId);
      // evento keyDown
      if (wysiwyg.addEventListener)
        wysiwyg.addEventListener("keydown", wysiwygKeyDown, false);
      else
        wysiwyg.contentWindow.document.attachEvent("onkeydown", wysiwygKeyDown);
      // adiciona os novos botões
      toolbar.innerHTML = "<button style='width:125px; height:24px; text-align:center;' title='Inicia um diálogo do cliente. (Alt+C)' class='ToolButton' onClick='clienteClick();' onmouseover='className=\"ToolButtonOver\";' onmouseout='className=\"ToolButton\";' unselectable='on'><img src=\"images/balloon16x16.png\" align=\"absmiddle\" border=0>&nbsp;Diálogo do Cliente</button>"
                        + "<button style='width:125px; height:24px; text-align:center;' title='Inicia um diálogo do usuário. (Alt+U)' class='ToolButton' onClick='usuarioClick();' onmouseover='className=\"ToolButtonOver\";' onmouseout='className=\"ToolButton\";' unselectable='on'><img src=\"images/user16x16.png\" align=\"absmiddle\" border=0>&nbsp;Diálogo do Usuário</button>";
*/
      function clienteClick() {
        format("Cliente: ", "black");
      }

      function usuarioClick() {
        format("Usuário: ", "red");
      }

      function format(text, color) {
        // põe o foco
        wysiwyg.contentWindow.focus();
        // insere o html
        insertHTML("<p>", wysiwygId);
        wysiwyg.contentWindow.document.execCommand("ForeColor", false, color);
        insertHTML("<span style='color:" + color + ";'>" + text + "</span>", wysiwygId);
      }

      function wysiwygKeyDown(event) {
        // tecla pressionada
        var key = (event.keyCode ? event.keyCode : event.which ? event.which : event.charCode);
        // se teclou Alt
        var isAlt = event.altKey;
        // se teclou Alt+C
     	  if (isAlt && (key == 67))
    	  	  clienteClick();
     	  else if (isAlt && (key == 85))
    	  	  usuarioClick();
      }
    </script>

  </body>
</html>

<%}
  // término do bloco protegido
  catch (Exception e) {
    Controller.forwardException(e, pageContext);
  } // try-catch
%>
