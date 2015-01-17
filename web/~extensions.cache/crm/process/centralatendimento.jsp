
<%@include file="../include/beans.jsp"%>

<%@page import="imanager.entity.*"%>
<%@page import="imanager.process.*"%>
<%@page import="imanager.ui.entity.*"%>
<%@page import="imanager.misc.*"%>
<%@page import="imanager.report.*"%>

<%
  // início do bloco protegido
  try {
    // nossa instância de CentralAtendimento
    CentralAtendimento centralAtendimento = (CentralAtendimento)facade.getProcess(CentralAtendimento.CLASS_NAME);
    // nossa instância de AtendimentoFrequente
    AtendimentoFrequente atendimentoFrequente = (AtendimentoFrequente)facade.getEntity(AtendimentoFrequente.CLASS_NAME);
    // nossa instância de Atendimento
    Atendimento atendimento = (Atendimento)facade.getEntity(Atendimento.CLASS_NAME);
    // nossa instância de Oportunidade
    Oportunidade oportunidade = (Oportunidade)facade.getEntity(Oportunidade.CLASS_NAME);
    // nossa instância de Tarefa
    Tarefa tarefa = (Tarefa)facade.getEntity(Tarefa.CLASS_NAME);
    // nossa instância de Contato
    RelatorioHistoricoContato relatorioHistoricoContato = (RelatorioHistoricoContato)facade.getReport(RelatorioHistoricoContato.CLASS_NAME);
    // oculta o frame bar de pesquisa
    atendimento.CONTROL_PARAM_CENTRAL_ATENDIMENTO.setValue("true");
    oportunidade.CONTROL_PARAM_CENTRAL_ATENDIMENTO.setValue("true");
    tarefa.CONTROL_PARAM_CENTRAL_ATENDIMENTO.setValue("true");

    // lista que iremos exibir
    AtendimentoFrequenteInfo[] atendimentoFrequenteInfoList = new AtendimentoFrequenteInfo[0];
    atendimentoFrequenteInfoList = atendimentoFrequente.selectByFilter(selectedEmpresaId, "", 0, 0, null);
    // mensagem de erro na pesquisa
    StringBuffer searchErrorMessage = new StringBuffer();

    // info para editar
    AtendimentoInfo atendimentoInfo = null;
    AtendimentoFrequenteInfo atendimentoFrequenteInfo = null;

    // info em branco
    atendimentoInfo = new AtendimentoInfo();
    // valor padrão
    atendimentoInfo.setDataHoraInicio(new Timestamp(DateTools.getActualDateTime().getTime()));


    // se estamos salvando...salva
    if (Controller.isSaving(request)) {
      // info preenchido na página
      atendimentoInfo = (AtendimentoInfo)atendimento.entityInfoFromRequest(request);
      // valores padrão
      atendimentoInfo.setEmpresaId(selectedEmpresaId);
      atendimentoInfo.setLinkExterno("http://");
      // guarda valores para o próximo atendimento
      int departamentoId = (int)atendimentoInfo.getDepartamentoId();
      int meioId         = (int)atendimentoInfo.getMeioId();
      // insere no atendimento
      atendimento.insert(atendimentoInfo);
      // compõe a resposta
      Form.composeAjaxResponse(response, Form.COMPOSE_TYPE_JAVA_SCRIPT);
      // limpa os dados na tela
      %>
      clearFields();
      <%
      // valores para o próximo atendimento
      atendimentoInfo.setDepartamentoId(departamentoId);
      atendimentoInfo.setMeioId(meioId);
      // dispara
      return;
    }

    // nosso Form de pesquisa
    
    // nosso Form de exclusão
    Form formData = new Form(request, "formDataCentralAtendimento", CentralAtendimento.ACTION, CentralAtendimento.COMMAND_SAVE, "", true, true);
    // nosso FrameBar
    FrameBar frameBar = new FrameBar(facade, "frameBarCentralAtendimento");
    // nosso  PageControl
    PageControl pageControl = new PageControl(facade, "pageControl", true);

%>

<html>
  <head>
    <title><%=CentralAtendimento.ACTION.getCaption()%></title>
    <link href="<%=facade.getStyle().userInterface()%>" rel="stylesheet" type="text/css">
    <script src="include/scripts.js" type="text/javascript"></script>
  </head>
  <body style="margin:0px;" onselectstart="return true;" oncontextmenu="return false;">

    <%// guarda as descrições dos atendimentos frequentes
      for (int i=0; i<atendimentoFrequenteInfoList.length; i++) {%>
        <div id="atendimentoFrequanteDescricaoList" style="display:none;"><%=atendimentoFrequenteInfoList[i].getDescricao()%></div>
    <%} //for %>

    <script type="text/javascript">

      var atendimentoFrequanteDepartamentoIdList = [];
      var atendimentoFrequanteAssuntoIdList      = [];

      <%// guarda os dados dos atendimentos frequentes
        for (int i=0; i<atendimentoFrequenteInfoList.length; i++) {%>
          atendimentoFrequanteDepartamentoIdList[<%=i%>] = <%=atendimentoFrequenteInfoList[i].getDepartamentoId()%>;
          atendimentoFrequanteAssuntoIdList[<%=i%>]      = <%=atendimentoFrequenteInfoList[i].getAssuntoId()%>;
      <%} //for %>

      function atendimentoFrequente(index) {
        // nossos objetos
        departamentoId = document.getElementById("<%=Atendimento.FIELD_DEPARTAMENTO_ID.getFieldAlias()%>");
        // seleciona o departamento
        departamentoId.value = atendimentoFrequanteDepartamentoIdList[index];
        // seleciona o assunto
        assuntoId = <%=EntityFormLookupSelectEx.changeValue(facade, Atendimento.LOOKUP_ASSUNTO, "atendimentoFrequanteAssuntoIdList[index]") %>;
        // cola a descrição
        {
          // põe o foco
          wysiwyg.contentWindow.focus();
          // insere o html
          insertHTML("<p>", wysiwygId);
          insertHTML(document.getElementsByName("atendimentoFrequanteDescricaoList")[index].innerHTML, wysiwygId);
        }
      }

      function clearFields() {
        // limpa todos os campos
        // atendimento
        var atendimentoId = document.getElementById("<%=Atendimento.FIELD_ATENDIMENTO_ID.getFieldAlias()%>").value = 0;
        // cliente
        var clienteId = <%=LookupSearch.clear(facade, Atendimento.FIELD_CLIENTE_ID.getFieldAlias())%>;
        // data hora início
        var dataHoraTermino = document.getElementById("<%=Atendimento.FIELD_DATA_HORA_INICIO.getFieldAlias()%>").value = '<%=DateTools.formatDateTime(new Timestamp(DateTools.getActualDateTime().getTime()))%>';
        // data hora termino
        var dataHoraTermino = document.getElementById("<%=Atendimento.FIELD_DATA_HORA_TERMINO.getFieldAlias()%>").value = "";
        //  assunto
        var assuntoId = <%=EntityFormLookupSelectEx.clear(facade, Atendimento.LOOKUP_ASSUNTO)%>;
        // campanha
        var campanhaId = document.getElementById("<%=Atendimento.FIELD_CAMPANHA_ID.getFieldAlias()%>").value = 0;
        // descrição
        {
          // põe o foco
          wysiwyg.contentWindow.focus();
          // seleciona tudo
          wysiwyg.contentWindow.document.execCommand("SelectAll");
          // deleta
          wysiwyg.contentWindow.document.execCommand("Delete");
        }
        // usuário inclusão
        var usuarioInclusaoId = document.getElementById("<%=Atendimento.FIELD_USUARIO_INCLUSAO_ID.getFieldAlias()%>").value = 0;
        // usuário alteração
        var usuarioAlteracaoId = document.getElementById("<%=Atendimento.FIELD_USUARIO_ALTERACAO_ID.getFieldAlias()%>").value = 0;
        // data inclusão
        var dataInclusao = document.getElementById("<%=Atendimento.FIELD_DATA_INCLUSAO.getFieldAlias()%>").value = "";
        // data hora alteração
        var dataHoraAlteracao = document.getElementById("<%=Atendimento.FIELD_DATA_HORA_ALTERACAO.getFieldAlias()%>").value = "";

        // limpa o parâmetro cliente
        var frameAtendimento = document.getElementById("pageControliframe2");
        var userParamAtendimentoClienteId = frameAtendimento.contentWindow.document.getElementById("<%=atendimento.USER_PARAM_CLIENTE_ID.getName()%>");
        var buttonAtendimentoPesquisar = frameAtendimento.contentWindow.document.getElementById("actionButtonatendimento");
        userParamAtendimentoClienteId.value = -1;
        buttonAtendimentoPesquisar.click();
        // limpa o parâmetro cliente
        var frameTarefa = document.getElementById("pageControliframe3");
        var userParamTarefaClienteId = frameTarefa.contentWindow.document.getElementById("<%=tarefa.USER_PARAM_CLIENTE_ID.getName()%>");
        var buttonTarefaPesquisar = frameTarefa.contentWindow.document.getElementById("actionButtontarefa");
        userParamTarefaClienteId.value = -1;
        buttonTarefaPesquisar.click();
        // limpa o parâmetro cliente
        var frameOportunidade = document.getElementById("pageControliframe4");
        var userParamOportunidadeClienteId = frameOportunidade.contentWindow.document.getElementById("<%=oportunidade.USER_PARAM_CLIENTE_ID.getName()%>");
        var buttonOportunidadePesquisar = frameOportunidade.contentWindow.document.getElementById("actionButtonoportunidade");
        userParamOportunidadeClienteId.value = -1;
        buttonOportunidadePesquisar.click();
        // histórico contato
        var frameHistorico = document.getElementById("pageControliframe5");
        frameHistorico.contentWindow.location.href = "about:blank";
      }

      function showClienteInfo() {
        // nossos parâmetros
        var clienteId = document.getElementById("<%=Atendimento.FIELD_CLIENTE_ID.getFieldAlias()%>").value;
        // se não temos cliente...dispara
        if (clienteId <= 0)
          return;
        // mostra os atendimentos do cliente
        var frameAtendimento = document.getElementById("pageControliframe2");
        var userParamAtendimentoClienteId = frameAtendimento.contentWindow.document.getElementById("<%=atendimento.USER_PARAM_CLIENTE_ID.getName()%>");
        var buttonAtendimentoPesquisar = frameAtendimento.contentWindow.document.getElementById("actionButtonatendimento");
        userParamAtendimentoClienteId.value = clienteId;
        buttonAtendimentoPesquisar.click();
        // mostra as tarefas do cliente
        var frameTarefa = document.getElementById("pageControliframe3");
        var userParamTarefaClienteId = frameTarefa.contentWindow.document.getElementById("<%=tarefa.USER_PARAM_CLIENTE_ID.getName()%>");
        var buttonTarefaPesquisar = frameTarefa.contentWindow.document.getElementById("actionButtontarefa");
        userParamTarefaClienteId.value = clienteId;
        buttonTarefaPesquisar.click();
        // mostra as oportunidades do cliente
        var frameOportunidade = document.getElementById("pageControliframe4");
        var userParamOportunidadeClienteId = frameOportunidade.contentWindow.document.getElementById("<%=oportunidade.USER_PARAM_CLIENTE_ID.getName()%>");
        var buttonOportunidadePesquisar = frameOportunidade.contentWindow.document.getElementById("actionButtonoportunidade");
        userParamOportunidadeClienteId.value = clienteId;
        buttonOportunidadePesquisar.click();
        // mostra o histórico do cliente
        var frameHistorico = document.getElementById("pageControliframe5");
        frameHistorico.contentWindow.location.href = "<%=RelatorioHistoricoContato.ACTION_CENTRAL_ATENDIMENTO.url(RelatorioHistoricoContato.COMMAND_MAKE_REPORT, relatorioHistoricoContato.USER_PARAM_CONTATO_ID.getName() + "=")%>" + clienteId;
      }

      function cadastroClienteChangeValue(contatoId, nome) {
        <%=LookupSearch.changeValue(facade, atendimento.FIELD_CLIENTE_ID.getFieldAlias(), "contatoId", "nome")%>;
      }
      
    </script>

    <!-- inicia o FrameBar -->
    <%=frameBar.begin()%>

      <!-- área de frames -->
      <%=frameBar.beginFrameArea()%>

        <!-- Frame de identificação do objeto -->
        <%=frameBar.actionFrame(CentralAtendimento.ACTION)%>

        <!-- Frame de comandos -->
        <%=frameBar.beginFrame("Comandos", false, false)%>
          <table style="width:100%;">
            <tr>
              <td><%=CommandControl.actionCustomScriptHyperlink(facade, centralAtendimento.ACTION, centralAtendimento.COMMAND_INSERT, ImageList.COMMAND_INSERT, "#", "", "clearFields()", false)%></td>
            </tr>
            <tr>
              <td><%=CommandControl.formHyperlink(facade, formData, ImageList.COMMAND_SAVE, "Deseja mesmo salvar o registro?", true, false)%></td>
            </tr>
            <tr>
              <td><%=CommandControl.actionHyperlink(facade, CadastroCliente.ACTION, CadastroCliente.COMMAND_CADASTRAR, ImageList.IMAGE_ENTITY, false)%></td>
            </tr>
          </table>
        <%=frameBar.endFrame()%>

        <!-- Frame de pesquisa -->
        <%=frameBar.beginFrame("Atendimentos Frequentes", false, true)%>
          <!-- Form de pesquisa -->
            <table>
              <%// se temos algum atendimento frequênte...
                if (atendimentoFrequenteInfoList.length > 0) {
                  // loop nos registros
                  for (int i=0; i<atendimentoFrequenteInfoList.length; i++) {%>
                  <tr>
                    <td><a href="javascript:void(0);" onclick="atendimentoFrequente(<%=i%>)"><%=atendimentoFrequenteInfoList[i].getNome()%></a></td>
                  </tr>
                <%} //for %>
              <%} 
                else {%>
                <tr>
                  <td>(nenhum)</td>
                </tr>
              <%}%>
            </table>
        <%=frameBar.endFrame()%>

      <%=frameBar.endFrameArea()%>

      <!-- Form de dados-->
      <%=formData.begin()%>

        <!-- área de dados -->
        <%=frameBar.beginClientArea()%>

          <!-- dados ocultos atendimento -->
          <%=EntityFormEdit.script(facade, Atendimento.FIELD_EMPRESA_ID, atendimentoInfo, request, -1, "", "")%>
          <%=EntityFormEdit.script(facade, Atendimento.FIELD_DATA_INCLUSAO, atendimentoInfo, request, -1, "", "")%>
          <%=EntityFormEdit.script(facade, Atendimento.FIELD_USUARIO_INCLUSAO_ID, atendimentoInfo, request, -1, "", "")%>
          <%=EntityFormEdit.script(facade, Atendimento.FIELD_DATA_HORA_ALTERACAO, atendimentoInfo, request, -1, "", "")%>
          <%=EntityFormEdit.script(facade, Atendimento.FIELD_USUARIO_ALTERACAO_ID, atendimentoInfo, request, -1, "", "")%>
          <%=EntityFormEdit.script(facade, Atendimento.FIELD_LINK_EXTERNO, atendimentoInfo, request, -1, "", "")%>
          <!-- área do cadastro -->
            <table style="width:100%;height:100%">
              <tr>
                <td style="width:100%;">
                  <%=pageControl.begin()%>
                    <%=pageControl.beginTabSheet("Atendimento")%>
                    <table style="width:100%;height:100%">
                      <tr style="height:30px">
                        <td style="width:100%">
                          <%=GroupBox.begin("Identificação")%>
                            <table style="width:100%">
                              <tr>
                                <td style="width:35px;"><%=EntityFieldLabel.script(facade, Atendimento.FIELD_ATENDIMENTO_ID, request, "" , "", true)%></td>
                                <td style="width:auto;"><%=EntityLookupLabel.script(facade, Atendimento.LOOKUP_CLIENTE, request, "linkCliente", "", Contato.ACTION, false)%></td>
                                <td style="width:100px;"><%=EntityFieldLabel.script(facade, Atendimento.FIELD_DATA_HORA_INICIO, request)%></td>
                                <td style="width:100px;"><%=EntityFieldLabel.script(facade, Atendimento.FIELD_DATA_HORA_TERMINO, request)%></td>
                                <td style="width:auto;"><%=EntityLookupLabel.script(facade, Atendimento.LOOKUP_MEIO, request, "linkMeio", "", Meio.ACTION, false)%></td>
                              </tr>
                              <tr>
                                <td style="width:35px;"><%=EntityFormEdit.script(facade, Atendimento.FIELD_ATENDIMENTO_ID, atendimentoInfo, request, 0, "", "", true, false)%></td>
                                <td style="width:auto;"><%=ContatoUI.lookupSearchForLookup(facade, Atendimento.LOOKUP_CLIENTE, atendimentoInfo, NaoSim.SIM, NaoSim.TODOS, NaoSim.TODOS, NaoSim.TODOS, NaoSim.TODOS, 0, "", "showClienteInfo();", false)%></td>
                                <td style="width:100px;"><%=EntityFormEdit.script(facade, Atendimento.FIELD_DATA_HORA_INICIO, atendimentoInfo, request, 0, "", "")%></td>
                                <td style="width:100px;"><%=EntityFormEdit.script(facade, Atendimento.FIELD_DATA_HORA_TERMINO, atendimentoInfo, request, 0, "", "")%></td>
                                <td style="width:auto;"><%=EntityFormLookupSelect.script(facade, Atendimento.CLASS_NAME, Atendimento.LOOKUP_MEIO, atendimentoInfo, request, 0, "", "", true)%></td>
                              </tr>
                            </table>
                          <%=GroupBox.end()%>
                        </td>
                      </tr>
                      <tr style="height:30px">
                        <td style="width:100%">
                          <%=GroupBox.begin("Atendimento")%>
                            <table style="width:100%;">
                              <tr>
                                <td style="width:25%;"><%=EntityLookupLabel.script(facade, Atendimento.LOOKUP_DEPARTAMENTO, request, "linkDepartamento", "", Departamento.ACTION, false)%></td>
                                <td style="width:50%;"><%=EntityLookupLabel.script(facade, Atendimento.LOOKUP_ASSUNTO, request, "linkAssunto", "", Assunto.ACTION, false)%></td>
                                <td style="width:25%;"><%=EntityLookupLabel.script(facade, Atendimento.LOOKUP_CAMPANHA, request, "linkCampanha", "", Campanha.ACTION, false)%></td>
                              </tr>
                              <tr>
                                <td><%=DepartamentoUI.lookupListForLookup(facade, Atendimento.LOOKUP_DEPARTAMENTO, atendimentoInfo, selectedEmpresaId, 0, "", "")%></td>
                                <td><%=EntityFormLookupSelectEx.script(facade, Atendimento.CLASS_NAME, Atendimento.LOOKUP_ASSUNTO, Assunto.LEVEL_DELIMITER, atendimentoInfo, request, 0, "", "", true)%></td>
                                <td><%=CampanhaUI.lookupListForLookup(facade, Atendimento.LOOKUP_CAMPANHA, atendimentoInfo, NaoSim.NAO, 0, "", "")%></td>
                              </tr>
                            </table>
                          <%=GroupBox.end()%>
                        </td>
                      </tr>
                      <tr style="height:auto">
                        <td>
                          <%=EntityFormMemo.script(facade, Atendimento.FIELD_DESCRICAO, atendimentoInfo, request, 0, 0, "", "", false, false)%>
                        </td>
                      </tr>
                    </table>
                    <%=pageControl.endTabSheet()%>
                    <%=pageControl.iFrameTabSheet("Anteriores", Atendimento.ACTION.url(atendimento.CONTROL_PARAM_CENTRAL_ATENDIMENTO.getName() + "=true&"
                                                                                      + atendimento.USER_PARAM_ATENDIMENTO_ID.getName() + "=0&"
                                                                                      + atendimento.USER_PARAM_DEPARTAMENTO_ID.getName() + "=0&"
                                                                                      + atendimento.USER_PARAM_ASSUNTO_ID.getName() + "=0&"
                                                                                      + atendimento.USER_PARAM_MEIO_ID.getName() + "=0&"
                                                                                      + atendimento.USER_PARAM_USUARIO_ID.getName() + "=0&"
                                                                                      + atendimento.USER_PARAM_CAMPANHA_ID.getName() + "=0"))%>
                    <%=pageControl.iFrameTabSheet("Tarefas", Tarefa.ACTION.url(tarefa.CONTROL_PARAM_CENTRAL_ATENDIMENTO.getName() + "=true&"
                                                                                      + tarefa.USER_PARAM_TAREFA_ID.getName() + "=0&"
                                                                                      + tarefa.USER_PARAM_DEPARTAMENTO_ID.getName() + "=0&"
                                                                                      + tarefa.USER_PARAM_ASSUNTO_ID.getName() + "=0&"
                                                                                      + tarefa.USER_PARAM_STATUS.getName() + "=" + StatusTarefa.TODOS + "&"
                                                                                      + tarefa.USER_PARAM_USUARIO_ID.getName() + "=0"))%>
                    <%=pageControl.iFrameTabSheet("Opotunidades", Oportunidade.ACTION.url(oportunidade.CONTROL_PARAM_CENTRAL_ATENDIMENTO.getName() + "=true&"
                                                                                      + oportunidade.USER_PARAM_OPORTUNIDADE_ID.getName() + "=0&"
                                                                                      + oportunidade.USER_PARAM_DEPARTAMENTO_ID.getName() + "=0&"
                                                                                      + oportunidade.USER_PARAM_USUARIO_ID.getName() + "=0&"
                                                                                      + oportunidade.USER_PARAM_CAMPANHA_ID.getName() + "=0&"
                                                                                      + oportunidade.USER_PARAM_FASE_ID.getName() + "=0&"
                                                                                      + oportunidade.USER_PARAM_STATUS.getName() + "=" + StatusOportunidade.TODOS))%>
                    <%=pageControl.iFrameTabSheet("Histórico", "about:blank")%>
                  <%=pageControl.end()%>
                </td>
              </tr>
            </table>
        <%=frameBar.endClientArea()%>
      <%=formData.end()%>

    <%=frameBar.end()%>

    <script type="text/javascript">
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
