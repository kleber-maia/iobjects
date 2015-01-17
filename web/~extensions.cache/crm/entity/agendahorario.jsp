
<%@include file="../include/beans.jsp"%>

<%@page import="imanager.entity.*"%>
<%@page import="imanager.misc.*"%>
<%@page import="imanager.ui.entity.*"%>

<%!
  public String buildGrid(Agenda agenda, AgendaInfo agendaInfo, AgendaHorarioInfo[] agendaHorarioInfoList, Timestamp dataAgendamento, Grid gridHorario, String feriado, String searchErrorMessage, boolean beginEndGrid) {
    // nosso resultado
    StringBuffer result = new StringBuffer();
    // intervalo entre horários
    double intervalo = agendaInfo.getIntervalo();
    // quantidade de horários no dia
    double quantidadeHorario = 24 * (60 / intervalo);
    // configura nosso calendário
    Calendar calendar = Calendar.getInstance();
    // configura nosso calendário Info
    Calendar calendarInfo = Calendar.getInstance();
    // seta a data do calendário para a data que o usuário inseriu
    calendar.setTime(dataAgendamento);
    // dia da semana
    int diaSemana = calendar.get(Calendar.DAY_OF_WEEK) - 1;
    // tabela de horários indisponíveis
    boolean[][] tabelaHorario = agenda.decodeTabelaHorario(agendaInfo.getTabelaHorario());
    // inicia o grid
    if (beginEndGrid)
      result.append(gridHorario.begin());
    // limpa as linhas existentes
    else
      result.append(gridHorario.clearRows());
    // adiciona tantas linhas quantos forem os horários disponíveis
    for (int i=0; i<quantidadeHorario; i++) {
      // horário
      String horario = DateTools.formatTime(calendar.getTime());
      // se é um horário indisponível...continua
      boolean indisponivel = !tabelaHorario[diaSemana][calendar.get(Calendar.HOUR_OF_DAY)];
      // próximo horário
      calendar.add(calendar.MINUTE, agendaInfo.getIntervalo());
      if (indisponivel)
        continue;
      // procura o agendamento nesse horário
      AgendaHorarioInfo agendaHorarioInfo = null;
      // loop nos registros
      for (int w=0; w<agendaHorarioInfoList.length; w++) {
        // se o horário é o mesmo do registro...
        if (StringTools.formatCustomMask(agendaHorarioInfoList[w].getHoraAgendamento(), "00:00").equals(horario)) {
          // seta agendaHorarioInfo para o registro da vez
          agendaHorarioInfo = agendaHorarioInfoList[w];
          break;
        } // if
      } // for w
        // adiciona ao grid
        if (agendaHorarioInfo != null && feriado.equals("")) {
          calendarInfo.setTime(agendaHorarioInfo.getDataAgendamento());
          String status = "";
          // se o status é pendente... sem cor
          status = (agendaHorarioInfo.getStatus() == StatusAgendaHorario.PENDENTE ? StatusAgendaHorario.LOOKUP_LIST_FOR_FIELD[agendaHorarioInfo.getStatus()] : status);
          // se o status é confirmado... cor azul
          status = (agendaHorarioInfo.getStatus() == StatusAgendaHorario.CONFIRMADO ? "<div style=color:#ffffff;background-color:#0000FF;height:18px;padding-top:2px;>" + StatusAgendaHorario.LOOKUP_LIST_FOR_FIELD[agendaHorarioInfo.getStatus()] + "</div>" : status);
          // se o status é concluido... cor verde
          status = (agendaHorarioInfo.getStatus() == StatusAgendaHorario.CONCLUIDO ? "<div style=color:#ffffff;background-color:#00CC00;height:18px;padding-top:2px;>" + StatusAgendaHorario.LOOKUP_LIST_FOR_FIELD[agendaHorarioInfo.getStatus()] + "</div>" : status);
          // se o status é cancelado... cor vermelho
          status = (agendaHorarioInfo.getStatus() == StatusAgendaHorario.CANCELADO ? "<div style=color:#ffffff;background-color:#FF0000;height:18px;padding-top:2px;>" + StatusAgendaHorario.LOOKUP_LIST_FOR_FIELD[agendaHorarioInfo.getStatus()] + "</div>" : status);
          result.append(
                  gridHorario.addRow(
                    new String[]{horario, // horário
                                 status, // status
                                 "<a href=" + AgendaHorario.ACTION_CADASTRO.url(AgendaHorario.COMMAND_EDIT, AgendaHorario.FIELD_EMPRESA_ID.getFieldAlias() + "=" + agendaHorarioInfo.getEmpresaId() + "&" + AgendaHorario.FIELD_AGENDA_ID.getFieldAlias() + "=" + agendaHorarioInfo.getAgendaId() + "&" + AgendaHorario.FIELD_AGENDA_HORARIO_ID.getFieldAlias() + "=" + agendaHorarioInfo.getAgendaHorarioId() + "&" + AgendaHorario.FIELD_DATA_AGENDAMENTO.getFieldAlias() + "=" + DateTools.formatDate(calendarInfo.getTime()) + "&" + AgendaHorario.FIELD_HORA_AGENDAMENTO.getFieldAlias() + "=" + agendaHorarioInfo.getHoraAgendamento()) + ">" + agendaHorarioInfo.lookupValueList().get(AgendaHorario.LOOKUP_CONTATO).getDisplayFieldValuesToString() + "</a>"}, // nome do agendamento
                    new String[]{agendaHorarioInfo.getEmpresaId() + "", agendaHorarioInfo.getAgendaId() + "", agendaHorarioInfo.getAgendaHorarioId() + "", DateTools.formatDate(agendaHorarioInfo.getDataAgendamento()), agendaHorarioInfo.getHoraAgendamento()}
          ));}
        else
          result.append(
                  gridHorario.addRow(
                    new String[]{horario, // horário
                                 "", // status
                                 (!feriado.equals("") ? feriado : "<a href=" + AgendaHorario.ACTION_CADASTRO.url(AgendaHorario.COMMAND_INSERT, AgendaHorario.FIELD_AGENDA_ID.getFieldAlias() + "=" + agendaInfo.getAgendaId() + "&" + AgendaHorario.FIELD_DATA_AGENDAMENTO.getFieldAlias() + "=" + DateTools.formatDate(dataAgendamento) + "&" + AgendaHorario.FIELD_HORA_AGENDAMENTO.getFieldAlias() + "=" + horario.replace(":","")) + ">Inserir</a>")}, // nome do agendamento
                    new String[]{"0", "0", "0"}
          ));
    } // for i
    // termina o grid
    if (beginEndGrid)
      result.append(gridHorario.end(searchErrorMessage));
    // muda o status
    else
      result.append(gridHorario.changeStatus(searchErrorMessage));
    // retorna
    return result.toString();
  }

  public Grid getGridInstance(Facade facade, HttpServletRequest request) {
    Object grid = request.getSession().getAttribute("gridAgendaHorario");
    if (grid != null)
      return (Grid)grid;
    else {
      grid = new Grid(facade, "gridHorario", new String[]{AgendaHorario.FIELD_EMPRESA_ID.getFieldAlias(), AgendaHorario.FIELD_AGENDA_ID.getFieldAlias(), AgendaHorario.FIELD_AGENDA_HORARIO_ID.getFieldAlias()}, Grid.SELECT_MULTIPLE, 0, 0);
      request.getSession().setAttribute("gridAgendaHorario", grid);
      return (Grid)grid;
    } // if
  }

  public AgendaHorarioInfo[] search(AgendaHorario agendaHorario, int selectedEmpresaId, StringBuffer searchErrorMessage) {
    try {
      // resultado da pesquisa...
      return agendaHorario.selectByFilter(
              selectedEmpresaId,
              NumberTools.parseInt(agendaHorario.USER_PARAM_AGENDA_ID.getValue()),
              0,
              DateTools.parseDate(agendaHorario.USER_PARAM_DATA_AGENDAMENTO.getValue()),
              null);
    }
    catch (Exception e) {
      searchErrorMessage.append(ExtendedException.extractMessage(e));
      return new AgendaHorarioInfo[0];
    } // try-catch
  }
%>

<%
  // início do bloco protegido
  try {
    // nossa instância de AgendaHorario
    AgendaHorario agendaHorario = (AgendaHorario)facade.getEntity(AgendaHorario.CLASS_NAME);
    // nossa instância de Agenda
    Agenda agenda = (Agenda)facade.getEntity(Agenda.CLASS_NAME);
    // nossa instância de Agenda Feriado
    AgendaFeriado agendaFeriado = (AgendaFeriado)facade.getEntity(AgendaFeriado.CLASS_NAME);

    // AgendaInfo para editar
    AgendaInfo agendaInfo = null;
    // se não temos uma agenda selecionada...
    if (agendaHorario.USER_PARAM_AGENDA_ID.getIntValue() == 0) {
      // agendas existentes
      AgendaInfo[] agendaInfoList = agenda.selectByFilter(selectedEmpresaId, "", null);
      // se não temos nenhuma agenda...exceção
      if (agendaInfoList.length == 0)
        throw new Exception("Nenhuma Agenda cadastrada.");
      // se temos...usa a primeira
      else
        agendaInfo = agendaInfoList[0];
    }
    // se temos...seleciona
    else
      agendaInfo = agenda.selectByPrimaryKey(selectedEmpresaId, agendaHorario.USER_PARAM_AGENDA_ID.getIntValue());
    
    // lista que iremos exibir
    AgendaHorarioInfo[] agendaHorarioInfoList = new AgendaHorarioInfo[0];
    // mensagem de erro na pesquisa
    StringBuffer searchErrorMessage = new StringBuffer();

    // se estamos excluindo...
    if (Controller.isDeleting(request)) {
      // registros selecionados
      AgendaHorarioInfo[] selectedInfoList = (AgendaHorarioInfo[])EntityGrid.selectedInfoList(agendaHorario, request);
      // compõe a resposta
      Form.composeAjaxResponse(response, Form.COMPOSE_TYPE_JAVA_SCRIPT);
      // exclui
      for (int i=0; i<selectedInfoList.length; i++)
        agendaHorario.delete(selectedInfoList[i]);
      agendaInfo            = agenda.selectByPrimaryKey(selectedEmpresaId, agendaHorario.USER_PARAM_AGENDA_ID.getIntValue());
      agendaHorarioInfoList = agendaHorario.selectByFilter(selectedEmpresaId, agendaHorario.USER_PARAM_AGENDA_ID.getIntValue(), 0, agendaHorario.USER_PARAM_DATA_AGENDAMENTO.getDateValue(), null);
    %>
    <%=buildGrid(agenda, agendaInfo, agendaHorarioInfoList, agendaHorario.USER_PARAM_DATA_AGENDAMENTO.getDateValue(), getGridInstance(facade, request), agendaHorario.USER_PARAM_FERIADO.getValue(), searchErrorMessage.toString(), false)%>
    <%// dispara
      return;
    }
    // se estamos pesquisando...
    else if (Controller.isSearching(request)) {
      // define os valores dos parâmetros de usuário
      agendaHorario.userParamList().setParamsValues(request);
      // compõe a resposta
      Form.composeAjaxResponse(response, Form.COMPOSE_TYPE_JAVA_SCRIPT);
      // info do registro para pesquisar
      agendaInfo            = agenda.selectByPrimaryKey(selectedEmpresaId, agendaHorario.USER_PARAM_AGENDA_ID.getIntValue());
      agendaHorarioInfoList = search(agendaHorario, selectedEmpresaId, searchErrorMessage);
      // feriado
      AgendaFeriadoInfo[] agendaFeriadoInfo = agendaFeriado.selectFeriado(selectedEmpresaId, agendaInfo.getAgendaId(), agendaHorario.USER_PARAM_DATA_AGENDAMENTO.getDateValue());
      // seta o feriado
      agendaHorario.USER_PARAM_FERIADO.setValue(agendaFeriadoInfo.length != 0 ? agendaFeriadoInfo[0].getNome() : "");
    %>
    <%=buildGrid(agenda, agendaInfo, agendaHorarioInfoList, agendaHorario.USER_PARAM_DATA_AGENDAMENTO.getDateValue(), getGridInstance(facade, request), agendaHorario.USER_PARAM_FERIADO.getValue(), searchErrorMessage.toString(), false)%>
    <%// dispara
      return;
    } // if

    // define os valores dos parâmetros de usuário
    agendaHorario.userParamList().setParamsValues(request);
    // se não temos uma agenda...
    if (agendaHorario.USER_PARAM_AGENDA_ID.getIntValue() == 0) {
      AgendaInfo[] agendaInfoList = agenda.selectByFilter(selectedEmpresaId, "", null);
      if (agendaInfoList.length > 0)
        agendaHorario.USER_PARAM_AGENDA_ID.setValue(agendaInfoList[0].getAgendaId() + "");
    } // if
    // feriado
    AgendaFeriadoInfo[] agendaFeriadoInfo = agendaFeriado.selectFeriado(selectedEmpresaId, agendaInfo.getAgendaId(), agendaHorario.USER_PARAM_DATA_AGENDAMENTO.getDateValue());
    // seta o feriado
    agendaHorario.USER_PARAM_FERIADO.setValue(agendaFeriadoInfo.length != 0 ? agendaFeriadoInfo[0].getNome() : "");
    // realiza a pesquisa
    agendaHorarioInfoList = search(agendaHorario, selectedEmpresaId, searchErrorMessage);

    // nosso Form de pesquisa
    Form formSearch = new Form(request, "formSearchAgendaHorario", AgendaHorario.ACTION, AgendaHorario.COMMAND_SEARCH, "", true, true);
    // nosso Form de exclusão
    Form formDelete = new Form(request, "formDeleteAgendaHorario", AgendaHorario.ACTION, AgendaHorario.COMMAND_DELETE, "", false, true, true);
    // nosso FrameBar
    FrameBar frameBar = new FrameBar(facade, "frameBarAgendaHorario");

    // nosso Grid de horário
    Grid gridHorario = getGridInstance(facade, request);
    if (gridHorario.columns().size() == 0) {
      gridHorario.columns().add(new Grid.Column("Horário", 70, Grid.ALIGN_CENTER));
      gridHorario.columns().add(new Grid.Column("Status", 80, Grid.ALIGN_CENTER));
      gridHorario.columns().add(new Grid.Column("Usuário", 350, Grid.ALIGN_LEFT));
    } // if
%>

<html>
  <head>
    <title><%=AgendaHorario.ACTION.getCaption()%></title>
    <link href="<%=facade.getStyle().userInterface()%>" rel="stylesheet" type="text/css">
    <script src="include/scripts.js" type="text/javascript"></script>
  </head>
  <body style="margin:0px;" onselectstart="return true;" oncontextmenu="return false;">
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
              <td><%=CommandControl.formHyperlink(facade, formDelete, ImageList.COMMAND_DELETE, "Deseja mesmo excluir o(s) registro(s) selecionado(s)?", true, false)%></td>
            </tr>
          </table>
        <%=frameBar.endFrame()%>

        <!-- Frame de pesquisa -->
        <%=frameBar.beginFrame("Pesquisa", false, true)%>
          <!-- Form de pesquisa -->
          <%=formSearch.begin()%>
              <table style="width:100%;">
                <tr>
                  <td><%=ParamLabel.script(facade, agendaHorario.USER_PARAM_AGENDA_ID)%></td>
                </tr>
                <tr>
                  <td><%=AgendaUI.lookupListForParam(facade, selectedEmpresaId, agendaHorario.USER_PARAM_AGENDA_ID, 0, "", "", false)%></td>
                </tr>
                <tr>
                  <td><%=ParamLabel.script(facade, agendaHorario.USER_PARAM_DATA_AGENDAMENTO)%></td>
                </tr>
                <tr>
                  <td><%=ParamFormEdit.script(facade, agendaHorario.USER_PARAM_DATA_AGENDAMENTO, 0, "", "")%></td>
                </tr>
                <tr>
                  <td><%=CommandControl.formButton(facade, formSearch, ImageList.COMMAND_SEARCH, "", true, false)%></td>
                </tr>
              </table>
          <%=formSearch.end()%>
        <%=frameBar.endFrame()%>

      <%=frameBar.endFrameArea()%>

      <!-- área do Grid -->
      <%=frameBar.beginClientArea()%>
        <!-- Form de exclusão -->
        <%=formDelete.begin()%>
            
          <%=buildGrid(agenda, agendaInfo, agendaHorarioInfoList, agendaHorario.USER_PARAM_DATA_AGENDAMENTO.getDateValue(), gridHorario, agendaHorario.USER_PARAM_FERIADO.getValue(), searchErrorMessage.toString(), true)%>

        <%=formDelete.end()%>
      <%=frameBar.endClientArea()%>

    <%=frameBar.end()%>


  </body>
</html>

<%}
  // término do bloco protegido
  catch (Exception e) {
    Controller.forwardException(e, pageContext);
  } // try-catch
%>
