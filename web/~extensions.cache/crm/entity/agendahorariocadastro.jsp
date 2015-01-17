
<%@include file="../include/beans.jsp"%>

<%@page import="imanager.entity.*"%>
<%@page import="imanager.report.*"%>
<%@page import="imanager.ui.entity.*"%>
<%@page import="imanager.misc.*"%>
<%@page import="imanager.process.*"%>

<%
  // início do bloco protegido
  try {
    // nossa instância de AgendaHorario
    AgendaHorario agendaHorario = (AgendaHorario)facade.getEntity(AgendaHorario.CLASS_NAME);
    // nossa instância de Agenda
    Agenda agenda = (Agenda)facade.getEntity(Agenda.CLASS_NAME);
    // nossa instância de Historico Contato
    RelatorioHistoricoContato relatorioHistoricoContato = (RelatorioHistoricoContato)facade.getReport(RelatorioHistoricoContato.CLASS_NAME);

    // AgendaHorarioInfo para editar
    AgendaHorarioInfo agendaHorarioInfo = null;

    // AgendaInfo para editar
    AgendaInfo agendaInfo = null;

    // se estamos inserindo...
    if (Controller.isInserting(request)) {
      // info em branco
      agendaHorarioInfo = new AgendaHorarioInfo();
      // valores padrões
      agendaHorarioInfo.setEmpresaId(selectedEmpresaId);
      agendaHorarioInfo.setAgendaId(Integer.parseInt(request.getParameter(AgendaHorario.FIELD_AGENDA_ID.getFieldAlias())));
      agendaHorarioInfo.setDataAgendamento(DateTools.parseDate(request.getParameter(AgendaHorario.FIELD_DATA_AGENDAMENTO.getFieldAlias())));
      agendaHorarioInfo.setHoraAgendamento(request.getParameter(AgendaHorario.FIELD_HORA_AGENDAMENTO.getFieldAlias()));
    }
    // se estamos salvando...
    else if (Controller.isSaving(request)) {
      // info preenchido na página
      agendaHorarioInfo = (AgendaHorarioInfo)agendaHorario.entityInfoFromRequest(request);
      // se estamos salvando e é um novo...inclui
      if (Controller.isSavingNew(request)) {
        agendaHorario.insert(agendaHorarioInfo);
      }
      // se estamos salvando e é um existente...atualiza
      else {
        agendaHorario.update(agendaHorarioInfo);
      } // if
      // compõe a resposta
      Form.composeAjaxResponse(response, Form.COMPOSE_TYPE_JAVA_SCRIPT);
      // atualiza a consulta
      %><%=EntityGrid.forwardBrowse(facade, agendaHorario, AgendaHorario.ACTION, Controller.isSavingNew(request) ? EntityGrid.OPERATION_INSERT : EntityGrid.OPERATION_UPDATE, agendaHorarioInfo)%><%
      // dispara
      return;
    }
    // se estamos editando...
    else if (Controller.isEditing(request)) {
      // info do registro para editar
      agendaHorarioInfo =
        agendaHorario.selectByPrimaryKey(
          NumberTools.parseInt(request.getParameter(AgendaHorario.FIELD_EMPRESA_ID.getFieldAlias())),
          NumberTools.parseInt(request.getParameter(AgendaHorario.FIELD_AGENDA_ID.getFieldAlias())),
          NumberTools.parseInt(request.getParameter(AgendaHorario.FIELD_AGENDA_HORARIO_ID.getFieldAlias()))
        );
    }
    // se o comando é desconhecido...
    else {
      throw new UnknownCommandException();
    } // if

    // nosso FrameBar
    FrameBar frameBar = new FrameBar(facade, "frameBarAgendaHorario");
    // nosso Form de dados
    Form formData = new Form(request, "formDataAgendaHorario", AgendaHorario.ACTION_CADASTRO, AgendaHorario.COMMAND_SAVE, "", true, true);
    // nosso Page Control
    PageControl pageControl = new PageControl(facade, "pageControl", true);
%>

<html>
  <head>
    <title><%=AgendaHorario.ACTION.getCaption()%></title>
    <link href="<%=facade.getStyle().userInterface()%>" rel="stylesheet" type="text/css">
    <script src="include/scripts.js" type="text/javascript"></script>
  </head>
  <body style="margin:0px;" onselectstart="return true;" oncontextmenu="return false;">

    <script type="text/javascript">
      function relatorioHistoricoContato() {
        // contatoId
        var contatoId = document.getElementById("<%=AgendaHorario.FIELD_CONTATO_ID.getFieldAlias()%>").value;
        // chama o relatório
        <%=RelatorioHistoricoContato.ACTION.url(RelatorioHistoricoContato.COMMAND_MAKE_REPORT, relatorioHistoricoContato.USER_PARAM_CONTATO_ID.getName() + "=' + contatoId + '")%>
      }
      
      function cadastroClienteChangeValue(contatoId, nome) {
        <%=LookupSearch.changeValue(facade, AgendaHorario.FIELD_CONTATO_ID.getFieldAlias(), "contatoId", "nome")%>;
      }
    </script>

    <!-- inicia o FrameBar -->
    <%=frameBar.begin()%>

      <!-- área de frames -->
      <%=frameBar.beginFrameArea()%>

        <!-- Frame de identificação do objeto -->
        <%=frameBar.actionFrame(AgendaHorario.ACTION)%>

        <!-- Frame de comandos -->
        <%=frameBar.beginFrame("Comandos", false, false)%>
          <table style="width:100%;">
            <tr>
              <td><%=CommandControl.formHyperlink(facade, formData, ImageList.COMMAND_SAVE, "Deseja mesmo salvar o registro?", true, false)%></td>
            </tr>
            <tr>
              <td><%=CommandControl.entityFormHyperlink(facade, agendaHorario, AgendaHorario.ACTION_CADASTRO, AgendaHorario.COMMAND_INSERT, ImageList.COMMAND_INSERT, false)%></td>
            </tr>
            <tr>
              <td><%=CommandControl.actionCustomScriptHyperlink(facade, RelatorioHistoricoContato.ACTION, RelatorioHistoricoContato.COMMAND_MAKE_REPORT, ImageList.IMAGE_REPORT, "javascript:void(0);", "", "relatorioHistoricoContato()", false)%></td>
            </tr>
            <tr>
              <td><%=CommandControl.actionHyperlink(facade, CadastroCliente.ACTION, CadastroCliente.COMMAND_CADASTRAR, ImageList.IMAGE_ENTITY, false)%></td>
            </tr>
            <tr>
              <td><%=CommandControl.entityBrowseHyperlink(facade, agendaHorario, AgendaHorario.ACTION, ImageList.COMMAND_BACK, "Voltar", "Volta para a consulta.", false)%></td>
            </tr>
          </table>
        <%=frameBar.endFrame()%>
      <%=frameBar.endFrameArea()%>

      <!-- Form de dados -->
      <%=formData.begin()%>

        <!-- área de dados -->
        <%=frameBar.beginClientArea()%>
          <!-- dados ocultos -->
          <%=EntityFormEdit.script(facade, AgendaHorario.FIELD_EMPRESA_ID, agendaHorarioInfo, request, -1, "", "", true, false)%>
          <%=EntityFormEdit.script(facade, AgendaHorario.FIELD_AGENDA_ID, agendaHorarioInfo, request, -1, "", "", true, false)%>
          <%=EntityFormEdit.script(facade, AgendaHorario.FIELD_AGENDA_HORARIO_ID, agendaHorarioInfo, request, -1, "", "", true, false)%>
          <%=EntityFormEdit.script(facade, AgendaHorario.FIELD_USUARIO_INCLUSAO_ID, agendaHorarioInfo, request, -1, "", "")%>
          <%=EntityFormEdit.script(facade, AgendaHorario.FIELD_DATA_HORA_INCLUSAO, agendaHorarioInfo, request, -1, "", "")%>
          <%=EntityFormEdit.script(facade, AgendaHorario.FIELD_USUARIO_ALTERACAO_ID, agendaHorarioInfo, request, -1, "", "")%>
          <%=EntityFormEdit.script(facade, AgendaHorario.FIELD_DATA_HORA_ALTERACAO, agendaHorarioInfo, request, -1, "", "")%>

        <%// agendaInfo
          agendaInfo = agenda.selectByPrimaryKey(selectedEmpresaId, agendaHorarioInfo.getAgendaId());
          // intervalo entre cada horário
          double intervalo = agendaInfo.getIntervalo();
          // quantidade de horários do dia
          double quantidadeHorario = 24 * (60 / intervalo);
          // vetor com os horários disponíveis
          Vector vetorListaHorarios = new Vector();
          // configura nosso calendário
          Calendar calendar = Calendar.getInstance();
          // data selecionada pelo usuário
          calendar.setTime(agendaHorarioInfo.getDataAgendamento());
          // dia da semana
          int diaSemana = calendar.get(Calendar.DAY_OF_WEEK) - 1;
          // tabela de horários indisponíveis
          boolean[][] tabelaHorario = agenda.decodeTabelaHorario(agendaInfo.getTabelaHorario());
          // loop nos horários
          for (int i=0; i<quantidadeHorario; i++) {
            // horário da vez
            String horario = DateTools.formatTime(calendar.getTime());
            // tabela de horários indisponíveis
            boolean indisponivel = !tabelaHorario[diaSemana][calendar.get(Calendar.HOUR_OF_DAY)];
            // próximo horário
            calendar.add(calendar.MINUTE, agendaInfo.getIntervalo());
            // se é um horário indisponível... continua
            if (indisponivel)
              continue;
            else
              // armazena os horários disponíveis
              vetorListaHorarios.addElement(horario);
          } // for
          // array que armazenará os horários disponíveis a partir do vetorListaHorarios
          String listaHorarios[] = new String[vetorListaHorarios.size()];
          // hora escolhida pelo usuário
          String horaEscolhida = StringTools.formatCustomMask(request.getParameter(AgendaHorario.FIELD_HORA_AGENDAMENTO.getFieldAlias()), "00:00");
          // hora marcada pelo usuário
          String horaMarcada = "";
          // loop nos horários disoníveis
          for(int i=0; i<vetorListaHorarios.size(); i++) {
            // adiciona o horário disponível da vez
            listaHorarios[i] = (String)vetorListaHorarios.elementAt(i);
            // verifica se o horário disponível da vez é o mesmo que o usuário escolheu
            if (listaHorarios[i].equals(horaEscolhida))
              // seta horaMarcada para o horário que o usuário escolheu
              horaMarcada = horaEscolhida;
          } // for %>
          <table cellpadding="0" cellspacing="0" style="width:100%; height:100%;">
            <%// obtém o nome da agenda
              String nomeAgenda = (Controller.isInserting(request) ? agenda.selectByPrimaryKey(agendaHorarioInfo.getEmpresaId(), agendaHorarioInfo.getAgendaId()).getNome() : "");
              if (!nomeAgenda.isEmpty()) {%>
            <!-- dados de AgendaHorario -->
              <tr style="height:10px;">
                <td class="Info"><img alt="" src="<%=ImageList.IMAGE_INFORMATION%>" align="absmiddle" /> Inserindo Agendamento em '<%=nomeAgenda%>'.</td>
              </tr>
            <%}%>
            <tr>
              <td>
                <table cellpadding="0" cellspacing="0" style="width:100%;height:100%">
                  <tr>
                    <td style="width:100%">
                      <table cellpadding="0" cellspacing="0" style="width:100%">
                        <tr>
                          <td style="width:auto;">
                            <!-- Data Agendamento, Contato ID, Status -->
                            <%=GroupBox.begin("Identificação")%>
                              <table style="width:100%">
                                <tr>
                                  <td style="width:90px"><%=EntityFieldLabel.script(facade, AgendaHorario.FIELD_DATA_AGENDAMENTO, request)%></td>
                                  <td style="width:60px"><%=EntityFieldLabel.script(facade, AgendaHorario.FIELD_HORA_AGENDAMENTO, request)%></td>
                                  <td style="width:auto;"><%=EntityFieldLabel.script(facade, AgendaHorario.FIELD_CONTATO_ID, request)%></td>
                                  <td style="width:120px"><%=EntityFieldLabel.script(facade, AgendaHorario.FIELD_STATUS, request)%></td>
                                </tr>
                                <tr>
                                  <td><%=EntityFormEdit.script(facade, AgendaHorario.FIELD_DATA_AGENDAMENTO, agendaHorarioInfo, request, 0, "", "", false, false)%></td>
                                  <td><%=FormSelect.script(facade, AgendaHorario.FIELD_HORA_AGENDAMENTO.getFieldAlias(), listaHorarios, listaHorarios, horaMarcada, FormSelect.SELECT_TYPE_SINGLE, "")%></td>
                                  <td><%=ContatoUI.lookupSearchForLookup(facade, AgendaHorario.LOOKUP_CONTATO, agendaHorarioInfo, NaoSim.SIM, NaoSim.TODOS, NaoSim.TODOS, NaoSim.TODOS, NaoSim.TODOS, 0, "", "", false)%></td>
                                  <td><%=EntityFormSelect.script(facade, AgendaHorario.FIELD_STATUS, agendaHorarioInfo, request, 0, "", "", false)%></td>
                                </tr>
                              </table>
                            <%=GroupBox.end()%>
                          </td>
                          <td style="width:40%">
                            <!-- Segurança -->
                            <%=GroupBox.begin("Segurança")%>
                              <table style="width:100%">
                                <tr>
                                  <td style="width:50%;"><%=EntityFieldLabel.script(facade, AgendaHorario.FIELD_USUARIO_INCLUSAO_ID, request, "", "", true)%></td>
                                  <td style="width:50%;"><%=EntityFieldLabel.script(facade, AgendaHorario.FIELD_USUARIO_ALTERACAO_ID, request, "", "", true)%></td>
                                </tr>
                                <tr>
                                  <td><%=FormEdit.script(facade, "inclusao", (agendaHorarioInfo.getUsuarioInclusaoId() > 0 ? facade.securityService().getUser(agendaHorarioInfo.getUsuarioInclusaoId()).getName() : "") + " - " + DateTools.formatDateTime(agendaHorarioInfo.getDataHoraInclusao()), 0, 0, FormEdit.ALIGN_LEFT, "", "", "", "", "", true)%></td>
                                  <td><%=FormEdit.script(facade, "alteracao", (agendaHorarioInfo.getUsuarioAlteracaoId() > 0 ? facade.securityService().getUser(agendaHorarioInfo.getUsuarioAlteracaoId()).getName() : "") + " - " + DateTools.formatDateTime(agendaHorarioInfo.getDataHoraAlteracao()), 0, 0, FormEdit.ALIGN_LEFT, "", "", "", "", "", true)%></td>
                                </tr>
                              </table>
                            <%=GroupBox.end()%>
                          </td>
                        </tr>
                      </table>
                    </td>
                  </tr>
                  <tr>
                    <td style="height:100%">
                      <!-- Anotações -->
                      <%=pageControl.begin()%>
                        <%=pageControl.beginTabSheet("Anotações")%>
                          <%=EntityFormMemo.script(facade, AgendaHorario.FIELD_ANOTACOES, agendaHorarioInfo, request, 0, 0, "", "", false, false)%>
                        <%=pageControl.endTabSheet()%>
                      <%=pageControl.end()%>
                    </td>
                  </tr>
                </table>
              </td>
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
