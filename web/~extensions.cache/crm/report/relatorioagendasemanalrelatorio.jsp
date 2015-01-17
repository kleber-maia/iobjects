       
<%@include file="../include/beans.jsp"%>

<%@page import="imanager.report.*"%>
<%@page import="imanager.entity.*"%>
<%@page import="imanager.misc.*"%>

<%!
class RelatorioAgendaSemanalInfo {
  String agenda  = "";
  String nome    = "";
  public RelatorioAgendaSemanalInfo(String agenda, String nome) {
    this.agenda    = agenda;
    this.nome      = nome;
  }
}

  public String verificaFeriado(AgendaFeriadoInfo[] agendaFeriadoInfoList, int empresaId, int agendaId, java.util.Date data, int consolidado) {
    // resultado
    StringBuffer result = new StringBuffer();
    // converte a data para Timestamp
    Timestamp dataVez    = new Timestamp(data.getTime());
    // formata o dia e data da vez
    String diaAtual      = DateTools.formatDay(dataVez, false, false);
    String diaMesAtual[] = DateTools.splitDate(dataVez);
    String diaFormatado  = diaAtual + ", " + diaMesAtual[0] + "/" + diaMesAtual[1] + ": ";
    // loop nos feriados
    for (int i=0; i<agendaFeriadoInfoList.length; i++) {
      // feriado da vez
      AgendaFeriadoInfo agendaFeriadoInfo = agendaFeriadoInfoList[i];
      // se a data do feriado é a mesma do registro...
      if (agendaFeriadoInfo.getFeriado().equals(dataVez)) {
        // se o bloqueio é total... retorna o nome do feriado
        if (agendaFeriadoInfo.getBloqueio() == TipoBloqueio.TOTAL) 
          return diaFormatado + agendaFeriadoInfo.getNome();
        // se o bloqueio é por empresa e a empresa do feriado é a mesma do registro... retorna o nome do feriado
        else if ((agendaFeriadoInfo.getBloqueio() == TipoBloqueio.EMPRESA && agendaFeriadoInfo.getEmpresaId() == empresaId) || (agendaFeriadoInfo.getBloqueio() == TipoBloqueio.EMPRESA && consolidado == NaoSim.SIM))
          result.append(result.length() > 0 ? ", " + agendaFeriadoInfo.getNome() : agendaFeriadoInfo.getNome());
        // se o bloqueio é por agenda, a empresa do feriado é a mesma do registro e a agenda do feriado é a mesma do registro... retorna o nome do feriado
        else if (agendaFeriadoInfo.getBloqueio() == TipoBloqueio.AGENDA && agendaFeriadoInfo.getEmpresaId() == empresaId && agendaFeriadoInfo.getAgendaId() == agendaId && consolidado == NaoSim.NAO)
          return diaFormatado + agendaFeriadoInfo.getNome();
      }
    }
    return result.length() > 0 ? diaFormatado + result.toString() : "";
   }
%>

<%
  // início do bloco protegido
  try {
    // nossa instância de RelatorioAgendaSemanal
    RelatorioAgendaSemanal relatorioAgendaSemanal = (RelatorioAgendaSemanal)facade.getReport(RelatorioAgendaSemanal.CLASS_NAME);
    // nossa instância de Agenda
    Agenda agenda = (Agenda)facade.getEntity(Agenda.CLASS_NAME);
    // nossa instância de AgendaFeriado
    AgendaFeriado agendaFeriado = (AgendaFeriado)facade.getEntity(AgendaFeriado.CLASS_NAME);

    // define os valores dos parâmetros de usuário
    relatorioAgendaSemanal.userParamList().setParamsValues(request);
    
    // vetor com o primeiro e último dia da semana
    Vector diasSemana = relatorioAgendaSemanal.calculateWeek(relatorioAgendaSemanal.USER_PARAM_DATA.getDateValue()); 
    // nossa lista com os feriados da semana
    AgendaFeriadoInfo[] agendaFeriadoInfoList = agendaFeriado.selectWeek("", TipoBloqueio.TODOS, (Timestamp)diasSemana.elementAt(0), (Timestamp)diasSemana.elementAt(1), null); 
    // primeira data da semana
    Timestamp dataTimestamp = (Timestamp)diasSemana.elementAt(0);
    java.util.Date dataDomingo = new java.util.Date(dataTimestamp.getTime());
    // nossos vetores
    Vector diaSemana = new Vector();
    // loop nos feriados
    for (int i=0; i<7; i++) {
      // adiciona os feriados no vetor
      diaSemana.add(verificaFeriado(agendaFeriadoInfoList, selectedEmpresaId, relatorioAgendaSemanal.USER_PARAM_CONSOLIDADO.getIntValue() == NaoSim.NAO ? relatorioAgendaSemanal.USER_PARAM_AGENDA.getIntValue() : 0, DateTools.getCalculatedDays(dataDomingo, i), relatorioAgendaSemanal.USER_PARAM_CONSOLIDADO.getIntValue()));
    } // for
    // prepara cada dia da semana que tiver feriado
    String domingo = diaSemana.elementAt(0).toString().length() > 0 ? " <a href='#feriado'><small> Feriado</small></a>" : "";
    String segunda = diaSemana.elementAt(1).toString().length() > 0 ? " <a href='#feriado'><small> Feriado</small></a>" : "";
    String terca   = diaSemana.elementAt(2).toString().length() > 0 ? " <a href='#feriado'><small> Feriado</small></a>" : "";
    String quarta  = diaSemana.elementAt(3).toString().length() > 0 ? " <a href='#feriado'><small> Feriado</small></a>" : "";
    String quinta  = diaSemana.elementAt(4).toString().length() > 0 ? " <a href='#feriado'><small> Feriado</small></a>" : "";
    String sexta   = diaSemana.elementAt(5).toString().length() > 0 ? " <a href='#feriado'><small> Feriado</small></a>" : "";
    String sabado  = diaSemana.elementAt(6).toString().length() > 0 ? " <a href='#feriado'><small> Feriado</small></a>" : "";
    
    // nosso Grid
    ReportGridEx reportGridRelatorioAgendaSemanal = new ReportGridEx("gridRelatorioAgendaSemanal", true);
    reportGridRelatorioAgendaSemanal.addColumn("Horários", 2, ReportGrid.ALIGN_LEFT);
    reportGridRelatorioAgendaSemanal.addColumn("Domingo" + domingo, 14, ReportGrid.ALIGN_LEFT);
    reportGridRelatorioAgendaSemanal.addColumn("Segunda" + segunda, 14, ReportGrid.ALIGN_LEFT);
    reportGridRelatorioAgendaSemanal.addColumn("Terça"   + terca, 14, ReportGrid.ALIGN_LEFT);
    reportGridRelatorioAgendaSemanal.addColumn("Quarta"  + quarta, 14, ReportGrid.ALIGN_LEFT);
    reportGridRelatorioAgendaSemanal.addColumn("Quinta"  + quinta, 14, ReportGrid.ALIGN_LEFT);
    reportGridRelatorioAgendaSemanal.addColumn("Sexta"   + sexta, 14, ReportGrid.ALIGN_LEFT);
    reportGridRelatorioAgendaSemanal.addColumn("Sábado"  + sabado, 14, ReportGrid.ALIGN_LEFT);
    
    // nossas cores de cada agenda
    String[] cores = new String[32];
    cores[0]  = "#ea9999"; 
    cores[1]  = "#f9cb9c"; 
    cores[2]  = "#ffe599"; 
    cores[3]  = "#b6d7a8"; 
    cores[4]  = "#a2c4c9"; 
    cores[5]  = "#9fc5e8"; 
    cores[6]  = "#b4a7d6"; 
    cores[7]  = "#d5a6bd"; 
    cores[8]  = "#e06666"; 
    cores[9]  = "#f6b26b"; 
    cores[10] = "#ffd966"; 
    cores[11] = "#93c47d"; 
    cores[12] = "#76a5af"; 
    cores[13] = "#6fa8dc"; 
    cores[14] = "#8e7cc3"; 
    cores[15] = "#c27ba0"; 
    cores[16] = "#cc0000"; // letra branca
    cores[17] = "#e69138"; 
    cores[18] = "#f1c232"; 
    cores[19] = "#6aa84f"; 
    cores[20] = "#45818e"; 
    cores[21] = "#3d85c6"; 
    cores[22] = "#674ea7"; 
    cores[23] = "#a64d79"; 
    cores[24] = "#990000"; 
    cores[25] = "#b45f06"; 
    cores[26] = "#bf9000"; 
    cores[27] = "#38761d"; 
    cores[28] = "#134f5c"; 
    cores[29] = "#0b5394"; 
    cores[30] = "#351c75"; 
    cores[31] = "#741b47"; 
    
    
%>

<html>
  <head>
    <title><%=RelatorioAgendaSemanal.ACTION.getCaption()%></title>
    <link href="<%=facade.getStyle().reportInterface()%>" rel="stylesheet" type="text/css">
    <script src="include/scripts.js" type="text/javascript"></script>
  </head>
  <body style="margin:5px;" oncontextmenu="return false;" onload="preencheAgenda()">
    
    <%// maior e menor hora da semana 
      String horaMin = "";
      String horaMax = "";
      // nosso resultSet com os horários mínimos e máximos
      ResultSet maxMin = relatorioAgendaSemanal.selectByMinMaxHorario(selectedEmpresaId, relatorioAgendaSemanal.USER_PARAM_CONSOLIDADO.getIntValue() == NaoSim.NAO ? relatorioAgendaSemanal.USER_PARAM_AGENDA.getIntValue() : 0, relatorioAgendaSemanal.USER_PARAM_DATA.getDateValue(), relatorioAgendaSemanal.USER_PARAM_CONSOLIDADO.getIntValue());
      maxMin.next();
      // não temos agendamento... exceção
      if (maxMin.getString("min_hora") == null || maxMin.getString("max_hora") == null) 
        throw new ExtendedException("Agenda sem nenhum Agendamento para esta semana.");
      // zera os minutos da menor e maior hora
      horaMin = maxMin.getString("min_hora").replace(maxMin.getString("min_hora").substring(2), "00");
      horaMax = maxMin.getString("max_hora").replace(maxMin.getString("max_hora").substring(2), "00");
      // intervalo de tempo da semana
      int diferencaHoras = (Integer.parseInt(horaMax) - Integer.parseInt(horaMin))/100;
      // Nosso ResultSet
      ResultSet resultSetRelatorioAgendaSemanal = relatorioAgendaSemanal.getResultSetRelatorioAgendaSemanal(selectedEmpresaId, relatorioAgendaSemanal.USER_PARAM_DATA.getDateValue(), relatorioAgendaSemanal.USER_PARAM_CONSOLIDADO.getIntValue() == NaoSim.NAO ? relatorioAgendaSemanal.USER_PARAM_AGENDA.getIntValue() : 0, horaMin, horaMax, diferencaHoras, relatorioAgendaSemanal.USER_PARAM_CONSOLIDADO.getIntValue());
      // nosso ParamList
      ParamList relatorioAgendaSemanalList = new ParamList();
      // vetor com as empresas e as agendas
      Vector empresaAgenda = new Vector();
      // vetor com as agendas e as cores
      Vector coresAgenda   = new Vector();
      // vetor com as legendas
      Vector legendaAgenda = new Vector();
      // controlador do vetor quem tem as cores
      int controladorCores = 0;
      // loop nos registros
      while (resultSetRelatorioAgendaSemanal.next()) {
        // obtém empresaId, nome da agenda, contato, hora, data, status do agendamento da vez
        int       empresaId        = resultSetRelatorioAgendaSemanal.getInt("empresa_id");
        int       agendaId         = resultSetRelatorioAgendaSemanal.getInt("agenda_id");
        int       agendaHorarioId  = resultSetRelatorioAgendaSemanal.getInt("agenda_horario_id");
        String    agendaNome       = resultSetRelatorioAgendaSemanal.getString("agenda");
        String    nome             = resultSetRelatorioAgendaSemanal.getString("contato");
        String    horaIndex        = resultSetRelatorioAgendaSemanal.getString("hora_agendamento").substring(0, 2);
        int       statusRegistro   = resultSetRelatorioAgendaSemanal.getInt("status");
        Timestamp data             = resultSetRelatorioAgendaSemanal.getTimestamp("data_agendamento");
        String    hora             = resultSetRelatorioAgendaSemanal.getString("hora_agendamento");
        String    horaFormatada    = StringTools.formatCustomMask(hora, "00:00");
        String    horariosMarcados = "";
        String    linkAgendamento  = AgendaHorario.ACTION_CADASTRO.url(AgendaHorario.COMMAND_EDIT, AgendaHorario.FIELD_EMPRESA_ID.getFieldAlias() + "=" + empresaId + "&" + AgendaHorario.FIELD_AGENDA_ID.getFieldAlias() + "=" + agendaId + "&" + AgendaHorario.FIELD_AGENDA_HORARIO_ID.getFieldAlias() + "=" + agendaHorarioId + "&" + AgendaHorario.FIELD_DATA_AGENDAMENTO.getFieldAlias() + "=" + DateTools.formatDate(data) + "&" + AgendaHorario.FIELD_HORA_AGENDAMENTO.getFieldAlias() + "=" + hora);
        // nosso índice
        int       index            = relatorioAgendaSemanalList.indexOf(horaIndex + "-" + data);
        // modifica a cor da hora de acordo com o status
        String status = "";
        // se o status é pendente... cor preta
        status = (statusRegistro == StatusAgendaHorario.PENDENTE ? "#FFFFFF" : status);
        // se o status é confirmado... cor azul
        status = (statusRegistro == StatusAgendaHorario.CONFIRMADO ? "#0000FF" : status);
        // se o status é concluido... cor verde
        status = (statusRegistro == StatusAgendaHorario.CONCLUIDO ? "#00CC00" : status);
        // se o status é cancelado... cor vermelha
        status = (statusRegistro == StatusAgendaHorario.CANCELADO ? "#FF0000" : status);
        // se temos uma agenda com nome...
        if (!agendaNome.equals("")) {
          // se esta agenda não está no vetor... adiciona no vetor e adiciona sua cor
          if (!empresaAgenda.contains(empresaId + agendaNome)) {
            empresaAgenda.addElement(empresaId + agendaNome);
            coresAgenda.addElement(cores[controladorCores++]);
            legendaAgenda.addElement(agendaNome);
          } // if
        } // if
        // modifica a cor do nome de acordo com a agenda
        String corAgenda = "";
        // se temos uma agenda no vetor... modifica sua cor de acordo com a agenda
        if (empresaAgenda.contains(empresaId + agendaNome)) 
          corAgenda = coresAgenda.elementAt(empresaAgenda.indexOf(empresaId + agendaNome)).toString();
        // cor do div em relação a agenda e status do agendamento
        String divCorAgenda = "<div style=" + (empresaAgenda.indexOf(empresaId + agendaNome) >= 16 ? "color:#ffffff;" : "") + "background-color:" + corAgenda + ";height:18px;padding-top:2px;>"
                            + "&nbsp;<span style=background-color:" + status + ";height:18px;width:8px;padding-top:2px;></span>&nbsp;"
                            +   horaFormatada + "<br>"  
                            +   "<a href='"+ linkAgendamento +"' target='frameContent'>" + nome + "</a>"
                            + "</div>";
        // se não temos o agendamento na lista...
        if (index < 0) {
          // se não temos nome... insere em branco
          if (nome.equals(""))
            horariosMarcados = "";
          // senão... insere os dados da vez
          else
            horariosMarcados = divCorAgenda;
          // cria seu agendamento
          RelatorioAgendaSemanalInfo relatorioAgendaSemanalInfo = new RelatorioAgendaSemanalInfo(horariosMarcados, nome);
          // insere na lista
          relatorioAgendaSemanalList.add(new Param(horaIndex + "-" + data, relatorioAgendaSemanalInfo));
        }
        // se já temos o agendamento na lista...
        else {
          // obtém o seu agendamento
          RelatorioAgendaSemanalInfo relatorioAgendaSemanalInfo = (RelatorioAgendaSemanalInfo)relatorioAgendaSemanalList.get(index).getObject();
          // se o agendamento está com contato e se existe contato da vez... concatena o novo agendamento
          if (!relatorioAgendaSemanalInfo.nome.equals("") && !nome.equals(""))
            // concatena o agendamento anterior com o agendamento da vez
            relatorioAgendaSemanalInfo.agenda = relatorioAgendaSemanalInfo.agenda + "<br>" + divCorAgenda;
          // senão se agendamento inserido foi em branco... atualiza com o agendamento da vez
          else if (relatorioAgendaSemanalInfo.nome.equals("")) {
            // guarda o nome do último agendamento com contato
            relatorioAgendaSemanalInfo.nome = nome;
            // atualiza o agendamento
            relatorioAgendaSemanalInfo.agenda = divCorAgenda;
          } // else-if
        } // if
      } // while%>
    
    <!-- Cabeçalho -->
    <%=ReportHeader.script(facade, RelatorioAgendaSemanal.ACTION, true, true, true, true, true)%> 
    
    <!-- Parâmetros -->
    <p>
      <table>
        <tr>
          <td><%=ReportParamLabel.script(facade, relatorioAgendaSemanal.USER_PARAM_DATA)%></td>
          <td><%=ReportParamText.script(facade, relatorioAgendaSemanal.USER_PARAM_DATA)%></td>
        </tr>
        <tr>
          <td valign="top;"><%=ReportParamLabel.script(facade, relatorioAgendaSemanal.USER_PARAM_AGENDA)%></td>
          <td>
            <table>
              <tr>
              <%// loop nas legendas
              for (int i=0; i<legendaAgenda.size(); i++) {%>
                <%=i % 8 == 0 ? "</tr><tr>" : ""%>
                  <td><div style="background-color:<%=coresAgenda.elementAt(i)%>;width:18px;height:18px;padding-top:2px"></td></div><td> <%=legendaAgenda.elementAt(i).toString()%></td>
              <%} // for%>
              </tr>
            </table>
          </td>
        </tr>
        <tr>
          <td><%=ReportParamLabel.script(facade, relatorioAgendaSemanal.USER_PARAM_CONSOLIDADO)%></td>
          <td><%=ReportParamText.script(facade, relatorioAgendaSemanal.USER_PARAM_CONSOLIDADO)%></td>
        </tr>
      </table>
    </p>
     
      <script type="text/javascript">
        
      function preencheAgenda() {
      <%// nossos controladores do grid
        int x = 0; // horários 
        int y = 0; // dias da semana
        // loop nos registros
        for (int i=0; i<relatorioAgendaSemanalList.size(); i++) {
          Param param = relatorioAgendaSemanalList.get(i);
          // registro da vez
          RelatorioAgendaSemanalInfo relatorioAgendaSemanalInfo = (RelatorioAgendaSemanalInfo)param.getObject();
          // se acabamos uma semana passa para o próximo horário
          if (y == 7) {
            y = 0;
            x++;
          } // if%>
          // preenche o div com o agendamento da vez
          document.getElementById('<%=x + "x" + y%>').innerHTML = "<%=relatorioAgendaSemanalInfo.agenda%>";
          <%// adiciona um dia na semana
          y++;
        } // for i%>
      }
      
    </script>
    
    <!-- Título -->
    <%=ReportTitle.script("Dados", ReportTitle.LEVEL_1)%>
    <!-- nosso grid -->
    <%=reportGridRelatorioAgendaSemanal.begin()%>
        <%// loop nas horas
          for (int i=0; i<=diferencaHoras; i++) {%>
        <%=reportGridRelatorioAgendaSemanal.beginRow()%>
        <%=reportGridRelatorioAgendaSemanal.beginCell()%> <%=StringTools.formatCustomMask(horaMin, "00:00")%> <%=reportGridRelatorioAgendaSemanal.endCell()%>
          <%=reportGridRelatorioAgendaSemanal.beginCell()%> <div id="<%=i%>x0"></div> <%=reportGridRelatorioAgendaSemanal.endCell()%>
          <%=reportGridRelatorioAgendaSemanal.beginCell()%> <div id="<%=i%>x1"></div> <%=reportGridRelatorioAgendaSemanal.endCell()%>
          <%=reportGridRelatorioAgendaSemanal.beginCell()%> <div id="<%=i%>x2"></div> <%=reportGridRelatorioAgendaSemanal.endCell()%>
          <%=reportGridRelatorioAgendaSemanal.beginCell()%> <div id="<%=i%>x3"></div> <%=reportGridRelatorioAgendaSemanal.endCell()%>
          <%=reportGridRelatorioAgendaSemanal.beginCell()%> <div id="<%=i%>x4"></div> <%=reportGridRelatorioAgendaSemanal.endCell()%>
          <%=reportGridRelatorioAgendaSemanal.beginCell()%> <div id="<%=i%>x5"></div> <%=reportGridRelatorioAgendaSemanal.endCell()%>
          <%=reportGridRelatorioAgendaSemanal.beginCell()%> <div id="<%=i%>x6"></div> <%=reportGridRelatorioAgendaSemanal.endCell()%>
        <%=reportGridRelatorioAgendaSemanal.endRow()%>
        <%// adiciona 1h
          horaMin = NumberTools.completeZero(Integer.parseInt(horaMin) + 100, 4);
        } // for%>
    <%=reportGridRelatorioAgendaSemanal.end()%>
     
    <%// loop nos feriados
      for (int i=0; i<7; i++) {
        // se temos um feriado... adiciona o título feriado
        if (!diaSemana.elementAt(i).equals("")) {%>
          <%="<a name='feriado'> " + ReportTitle.script("Feriados", ReportTitle.LEVEL_1) + "</a>"%>
          <%break;
        } // if
    } // for%>
    <%// loop nos feriados
      for (int i=0; i<7; i++) {%>
        <%=diaSemana.elementAt(i).toString().length() > 0 ? "<li>" + diaSemana.elementAt(i).toString() + "</li>" : ""%>
    <%} // for%>
    
    <!-- Rodapé -->
    <%=ReportFooter.script(facade, true, true)%>
    
  </body>
</html>

<%}
  // término do bloco protegido
  catch (Exception e) {
    Controller.forwardException(e, pageContext);
  } // try-catch
%>
