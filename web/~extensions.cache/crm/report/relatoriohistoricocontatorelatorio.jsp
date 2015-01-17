
<%@include file="../include/beans.jsp"%>

<%@page import="imanager.entity.*"%>
<%@page import="imanager.misc.*"%>
<%@page import="imanager.report.*"%>

<%!
  public String getLast(String value) {
    int index = value.lastIndexOf("/");
    return index >= 0 ? value.substring(index+1) : value;
  }
%>

<%
  // início do bloco protegido
  try {
    // nossa instância de Histórico Contato
    RelatorioHistoricoContato historicoContato = (RelatorioHistoricoContato)facade.getReport(RelatorioHistoricoContato.CLASS_NAME);
    // nossa instância de Contato
    Contato contato = (Contato)facade.getEntity(Contato.CLASS_NAME);
    // nossa instância de Atendimento
    Atendimento atendimento = (Atendimento)facade.getEntity(Atendimento.CLASS_NAME);

    // define os valores dos parâmetros de usuário
    historicoContato.userParamList().clearValues();
    historicoContato.userParamList().setParamsValues(request);
    // se não temos o Contato...exceção
    if (historicoContato.USER_PARAM_CONTATO_ID.getIntValue() == 0)
        throw new Exception("Este processo deve ser chamado através do comando '" + RelatorioHistoricoContato.COMMAND_MAKE_REPORT.getCaption() + "' existente em cadastros e relatórios do sistema.");

    // contato
    int contatoId = historicoContato.USER_PARAM_CONTATO_ID.getIntValue();
    ContatoInfo contatoInfo = contato.selectByPrimaryKey(contatoId);

    // hoje
    java.util.Date today = DateTools.getActualDate();

    // nosso Grid
    ReportGrid reportGridRelatorioHistoricoContato = new ReportGrid("gridRelatorioHistoricoContato", true);
    reportGridRelatorioHistoricoContato.addColumn("Parâmetro", "parametro", 50, ReportGrid.ALIGN_LEFT, ReportGrid.FORMAT_NONE, ReportGrid.TOTAL_TYPE_COUNT);
    reportGridRelatorioHistoricoContato.addColumn("Código Hash", "hashcode", 50, ReportGrid.ALIGN_RIGHT, ReportGrid.FORMAT_INTEGER, ReportGrid.TOTAL_TYPE_SUM);
%>

<html>
  <head>
    <title><%=RelatorioHistoricoContato.ACTION.getCaption()%></title>
    <link href="<%=facade.getStyle().reportInterface()%>" rel="stylesheet" type="text/css">
    <script src="include/scripts.js" type="text/javascript"></script>
  </head>
  <body style="margin:0px;" oncontextmenu="return false;">

    <!-- Cabeçalho -->
    <%=ReportHeader.script(facade, RelatorioHistoricoContato.ACTION, true, true, true, true, true)%>

    <%=GroupBox.begin("Identificação")%>
      <table style="width:100%;">
        <tr>
          <td style="width:auto;" class="FieldLabel"><%=Contato.FIELD_NOME.getCaption()%></td>
          <td style="width:70px;" class="FieldLabel"><%=Contato.FIELD_TIPO_PESSOA.getCaption()%></td>
          <td style="width:70px;" class="FieldLabel"><%=Contato.FIELD_CLIENTE.getCaption()%></td>
          <td style="width:70px;" class="FieldLabel"><%=Contato.FIELD_FORNECEDOR.getCaption()%></td>
          <td style="width:70px;" class="FieldLabel"><%=Contato.FIELD_FUNCIONARIO.getCaption()%></td>
          <td style="width:70px;" class="FieldLabel"><%=Contato.FIELD_VENDEDOR.getCaption()%></td>
          <td style="width:70px;" class="FieldLabel"><%=Contato.FIELD_TRANSPORTADOR.getCaption()%></td>
        </tr>
        <tr>
          <td><a title="Ir para Contato" href="<%=Contato.ACTION_CADASTRO.url(Contato.COMMAND_EDIT, Contato.FIELD_CONTATO_ID.getFieldAlias() + "=" + contatoInfo.getContatoId())%>" target="frameContent"><%=contatoInfo.getNome()%></a></td>
          <td><%=TipoPessoa.LOOKUP_LIST_FOR_FIELD[contatoInfo.getTipoPessoa()]%></td>
          <td><%=NaoSim.LOOKUP_LIST_FOR_FIELD[contatoInfo.getCliente()]%></td>
          <td><%=NaoSim.LOOKUP_LIST_FOR_FIELD[contatoInfo.getFornecedor()]%></td>
          <td><%=NaoSim.LOOKUP_LIST_FOR_FIELD[contatoInfo.getFuncionario()]%></td>
          <td><%=NaoSim.LOOKUP_LIST_FOR_FIELD[contatoInfo.getVendedor()]%></td>
          <td><%=NaoSim.LOOKUP_LIST_FOR_FIELD[contatoInfo.getTransportador()]%></td>
        </tr>
      </table>
    <%=GroupBox.end()%>

    <%=GroupBox.begin("Dados Cadastrais")%>
      <table cellpadding="0" cellspacing="0" style="width:100%;">
        <tr>
          <td style="width:35%;">

            <!-- campos Grupo Contato e Percentual Comissão -->
            <table style="width:100%;">
              <tr>
                <td style="width:auto;" class="FieldLabel"><%=Contato.LOOKUP_GRUPO_CONTATO.getCaption()%></td>
                <td style="width:85px;" class="FieldLabel"><%=Contato.FIELD_PERCENTUAL_COMISSAO.getCaption()%></td>
                <td style="width:85px;" class="FieldLabel"><%=Contato.FIELD_PERCENTUAL_INDICACAO.getCaption()%></td>
              </tr>
              <tr>
                <td><%=contatoInfo.lookupValueList().get(Contato.LOOKUP_GRUPO_CONTATO).getDisplayFieldValuesToString()%></td>
                <td><%=contatoInfo.getPercentualComissao()%></td>
                <td><%=contatoInfo.getPercentualIndicacao()%></td>
              </tr>
            </table>

          </td>
          <td style="width:65%;">

            <%if (contatoInfo.getTipoPessoa() == TipoPessoa.FISICA) {%>
              <!-- campos CPF, RG, Data Nascimento e Sexo -->
              <table id="tablePessoaFisica" style="width:100%;">
                <tr>
                  <td style="width:auto;" class="FieldLabel"><%=Contato.FIELD_CPF.getCaption()%></td>
                  <td style="width:130px;" class="FieldLabel"><%=Contato.FIELD_RG.getCaption()%></td>
                  <td style="width:100px;" class="FieldLabel"><%=Contato.FIELD_DATA_NASCIMENTO.getCaption()%></td>
                  <td style="width:80px;" class="FieldLabel"><%=Contato.FIELD_SEXO.getCaption()%></td>
                </tr>
                <tr>
                  <td><%=StringTools.formatCustomMask(contatoInfo.getCpf(), Contato.FIELD_CPF.getMask())%></td>
                  <td><%=contatoInfo.getRg()%></td>
                  <td><%=DateTools.formatDate(contatoInfo.getDataNascimento())%></td>
                  <td><%=Sexo.LOOKUP_LIST_FOR_FIELD[contatoInfo.getSexo()]%></td>
                </tr>
              </table>
            <%}
              else {%>
              <!-- campos Razão Social, CNPJ, Inscrições -->
              <table id="tablePessoaJuridica" style="width:100%;">
                <tr>
                  <td style="width:auto;" class="FieldLabel"><%=Contato.FIELD_RAZAO_SOCIAL.getCaption()%></td>
                  <td style="width:110px;" class="FieldLabel"><%=Contato.FIELD_CNPJ.getCaption()%></td>
                  <td style="width:110px;" class="FieldLabel"><%=Contato.FIELD_INSCRICAO_ESTADUAL.getCaption()%></td>
                  <td style="width:110px;" class="FieldLabel"><%=Contato.FIELD_INSCRICAO_MUNICIPAL.getCaption()%></td>
                </tr>
                <tr>
                  <td><%=contatoInfo.getRazaoSocial()%></td>
                  <td><%=StringTools.formatCustomMask(contatoInfo.getCnpj(), Contato.FIELD_CNPJ.getMask())%></td>
                  <td><%=contatoInfo.getInscricaoEstadual()%></td>
                  <td><%=contatoInfo.getInscricaoMunicipal()%></td>
                </tr>
              </table>
            <%}%>

          </td>
        </tr>
      </table>
    <%=GroupBox.end()%>

    <!-- campos CEP, Endereço, Número, Complemento, Bairro, Cidade, CEP, Telefones, Emails -->
    <%=GroupBox.begin("Endereço")%>
      <table style="width:100%;">
        <tr>
          <td style="width:60px;" class="FieldLabel"><%=Contato.FIELD_CEP.getCaption()%></td>
          <td style="width:auto;" class="FieldLabel"><%=Contato.FIELD_ENDERECO.getCaption()%></td>
          <td style="width:60px;" class="FieldLabel"><%=Contato.FIELD_ENDERECO_NUMERO.getCaption()%></td>
          <td style="width:100px;" class="FieldLabel"><%=Contato.FIELD_ENDERECO_COMPLEMENTO.getCaption()%></td>
          <td style="width:150px;" class="FieldLabel"><%=Contato.FIELD_ENDERECO_BAIRRO.getCaption()%></td>
          <td style="width:auto;" class="FieldLabel"><%=Contato.FIELD_CIDADE.getCaption()%></td>
          <td style="width:25px;" class="FieldLabel"><%=Contato.FIELD_UF.getCaption()%></td>
        </tr>
        <tr>
          <td><%=contatoInfo.getCep()%></td>
          <td><%=contatoInfo.getEndereco()%></td>
          <td><%=contatoInfo.getEnderecoNumero()%></td>
          <td><%=contatoInfo.getEnderecoComplemento()%></td>
          <td><%=contatoInfo.getEnderecoBairro()%></td>
          <td><%=contatoInfo.getCidade()%></td>
          <td><%=contatoInfo.getUf()%></td>
        </tr>
      </table>
      <table style="width:100%;">
        <tr>
          <td style="width:90px;" class="FieldLabel"><%=Contato.FIELD_TELEFONE_RESIDENCIAL.getCaption()%></td>
          <td style="width:90px;" class="FieldLabel"><%=Contato.FIELD_TELEFONE_CELULAR.getCaption()%></td>
          <td style="width:90px;" class="FieldLabel"><%=Contato.FIELD_TELEFONE_TRABALHO.getCaption()%></td>
          <td style="width:auto;" class="FieldLabel"><%=Contato.FIELD_EMAIL.getCaption()%></td>
        </tr>
        <tr>
          <td><%=contatoInfo.getTelefoneResidencial()%></td>
          <td><%=contatoInfo.getTelefoneCelular()%></td>
          <td><%=contatoInfo.getTelefoneTrabalho()%></td>
          <td><%=contatoInfo.getEmail()%></td>
        </tr>
      </table>
    <%=GroupBox.end()%>

    <%=GroupBox.begin("Anotações")%>
      <%=contatoInfo.getAnotacoes()%>
    <%=GroupBox.end()%>

    <!-- Agendamentos -->
      <%if (facade.getLoggedUser().hasAccessRight(RelatorioHistoricoContato.ACTION, RelatorioHistoricoContato.COMMAND_AGENDAMENTOS)) {
        // nossos agendamentos
        AgendaHorarioInfo[] agendaHorarioInfoList = historicoContato.getAgendamentos(contatoId);
        // se temos algo...
        if (agendaHorarioInfoList.length > 0) {
          // nosso grid
          ReportGridEx reportGrid = new ReportGridEx("reportGrid", true);
          reportGrid.addColumn("Agenda", 30, ReportGrid.ALIGN_LEFT);
          reportGrid.addColumn("Data", 10, ReportGrid.ALIGN_CENTER);
          reportGrid.addColumn("Hora", 7, ReportGrid.ALIGN_CENTER);
          reportGrid.addColumn("Status", 15, ReportGrid.ALIGN_LEFT);
          reportGrid.addColumn("", 38, ReportGrid.ALIGN_LEFT);
        %>

        <%=ReportTitle.script("<img src='images/commands/down.png' border='0' style='cursor:hand;' onclick=\"if (divAgendamento.style.display=='none') { divAgendamento.style.display='block'; this.src = 'images/commands/up.png'; } else { divAgendamento.style.display='none'; this.src = 'images/commands/down.png'; }\" />&nbsp;Agendamentos", ReportTitle.LEVEL_1)%>
        <div id="divAgendamento" style="display:none;">
          <%String empresa = "";
            // loop nos registros
            for (int i=0; i<agendaHorarioInfoList.length; i++) {
              AgendaHorarioInfo agendaHorarioInfo = agendaHorarioInfoList[i];
              String agendamentoEmpresa           = agendaHorarioInfo.lookupValueList().get(AgendaHorario.LOOKUP_EMPRESA).getDisplayFieldValuesToString();%>
            <%if (!empresa.equals(agendamentoEmpresa)) {
                empresa = agendamentoEmpresa;%>
              <%=(i > 0) ? reportGrid.end() : ""%>
              <p class="FieldLabel"><b><%=empresa%></b></p>
              <%=reportGrid.begin()%>
            <%} // if%>
            <%=reportGrid.beginRow()%>
              <%=reportGrid.beginCell()%> <%=agendaHorarioInfo.lookupValueList().get(AgendaHorario.LOOKUP_AGENDA).getDisplayFieldValuesToString()%> <%=reportGrid.endCell()%>
              <%=reportGrid.beginCell()%> <%=DateTools.formatDate(agendaHorarioInfo.getDataAgendamento())%> <%=reportGrid.endCell()%>
              <%=reportGrid.beginCell()%> <a title="Ir para Agendamento" href="<%=AgendaHorario.ACTION_CADASTRO.url(AgendaHorario.COMMAND_EDIT, AgendaHorario.FIELD_EMPRESA_ID.getFieldAlias() + "=" + agendaHorarioInfo.getEmpresaId() + "&" + AgendaHorario.FIELD_AGENDA_ID.getFieldAlias() + "=" + agendaHorarioInfo.getAgendaId()  + "&" + AgendaHorario.FIELD_AGENDA_HORARIO_ID.getFieldAlias() + "=" + agendaHorarioInfo.getAgendaHorarioId()  + "&" + AgendaHorario.FIELD_DATA_AGENDAMENTO.getFieldAlias() + "=" + DateTools.formatDate(agendaHorarioInfo.getDataAgendamento())  + "&" + AgendaHorario.FIELD_HORA_AGENDAMENTO.getFieldAlias() + "=" + agendaHorarioInfo.getHoraAgendamento())%>" target="frameContent"><%=AgendaHorario.FIELD_HORA_AGENDAMENTO.getFormatedFieldValue(agendaHorarioInfo)%></a> <%=reportGrid.endCell()%>
              <%=reportGrid.beginCell()%> <%=StatusAgendaHorario.LOOKUP_LIST_FOR_FIELD[agendaHorarioInfo.getStatus()]%> <%=reportGrid.endCell()%>
              <%=reportGrid.beginCell()%> <%=reportGrid.endCell()%>
            <%=reportGrid.endRow()%>
          <%} // for%>
          <%=reportGrid.end()%>
        </div>
      <%}%>
    <%} //if%>

    <!-- Atendimentos -->
    <%if (facade.getLoggedUser().hasAccessRight(RelatorioHistoricoContato.ACTION, RelatorioHistoricoContato.COMMAND_ATENDIMENTOS)) {
        // nossos atendimentos
        AtendimentoInfo[] atendimentoInfoList = historicoContato.getAtendimentos(contatoId);
        // se temos algo...
        if (atendimentoInfoList.length > 0) {
          // nosso grid
          ReportGridEx reportGrid = new ReportGridEx("reportGrid", true);
          reportGrid.addColumn("ID", 5, ReportGrid.ALIGN_LEFT);
          reportGrid.addColumn("Início", 15, ReportGrid.ALIGN_LEFT);
          reportGrid.addColumn("Departamento", 30, ReportGrid.ALIGN_LEFT);
          reportGrid.addColumn("Assunto", 30, ReportGrid.ALIGN_LEFT);
          reportGrid.addColumn("Meio", 20, ReportGrid.ALIGN_LEFT);
        %>

        <%=ReportTitle.script("<img src='images/commands/down.png' border='0' style='cursor:hand;' onclick=\"if (divAtendimento.style.display=='none') { divAtendimento.style.display='block'; this.src = 'images/commands/up.png'; } else { divAtendimento.style.display='none'; this.src = 'images/commands/down.png'; }\" />&nbsp;Atendimentos", ReportTitle.LEVEL_1)%>
        <div id="divAtendimento" style="display:none;">
          <%String empresa = "";
            // loop nos registros
            for (int i=0; i<atendimentoInfoList.length; i++) {
              AtendimentoInfo atendimentoInfo = atendimentoInfoList[i];
              String atendimentoEmpresa       = atendimentoInfo.lookupValueList().get(Atendimento.LOOKUP_EMPRESA).getDisplayFieldValuesToString();%>
            <%if (!empresa.equals(atendimentoEmpresa)) {
                empresa = atendimentoEmpresa;%>
              <%=(i > 0) ? reportGrid.end() : ""%>
              <p class="FieldLabel"><b><%=empresa%></b></p>
              <%=reportGrid.begin()%>
            <%} // if%>
            <%=reportGrid.beginRow()%>
              <%=reportGrid.beginCell()%> <a title="Ir para Atendimento" href="<%=Atendimento.ACTION_CADASTRO.url(Atendimento.COMMAND_EDIT, Atendimento.FIELD_EMPRESA_ID.getFieldAlias() + "=" + atendimentoInfo.getEmpresaId() + "&" + Atendimento.FIELD_ATENDIMENTO_ID.getFieldAlias() + "=" + atendimentoInfo.getAtendimentoId())%>" target="frameContent"><%=atendimentoInfo.getAtendimentoId()%></a> <%=reportGrid.endCell()%>
              <%=reportGrid.beginCell()%> <%=DateTools.formatDateTime(atendimentoInfo.getDataHoraInicio())%> <%=reportGrid.endCell()%>
              <%=reportGrid.beginCell()%> <%=atendimentoInfo.lookupValueList().get(Atendimento.LOOKUP_DEPARTAMENTO).getDisplayFieldValuesToString()%> <%=reportGrid.endCell()%>
              <%=reportGrid.beginCell()%> <%=getLast(atendimentoInfo.lookupValueList().get(Atendimento.LOOKUP_ASSUNTO).getDisplayFieldValuesToString())%> <%=reportGrid.endCell()%>
              <%=reportGrid.beginCell()%> <%=atendimentoInfo.lookupValueList().get(Atendimento.LOOKUP_MEIO).getDisplayFieldValuesToString()%> <%=reportGrid.endCell()%>
            <%=reportGrid.endRow()%>
          <%} // for%>
          <%=reportGrid.end()%>
        </div>
      <%}%>
    <%} //if%>

    <!-- Tarefas -->
    <%if (facade.getLoggedUser().hasAccessRight(RelatorioHistoricoContato.ACTION, RelatorioHistoricoContato.COMMAND_TAREFAS)) {
        // nossas tarefas
        TarefaInfo[] tarefaInfoList = historicoContato.getTarefas(contatoId);
        // se temos algo...
        if (tarefaInfoList.length > 0) {
          // nosso grid
          ReportGridEx reportGrid = new ReportGridEx("reportGrid", true);
          reportGrid.addColumn("ID", 5, ReportGrid.ALIGN_LEFT);
          reportGrid.addColumn("Prazo", 10, ReportGrid.ALIGN_LEFT);
          reportGrid.addColumn("Departamento", 25, ReportGrid.ALIGN_LEFT);
          reportGrid.addColumn("Assunto", 25, ReportGrid.ALIGN_LEFT);
          reportGrid.addColumn("Usuário", 15, ReportGrid.ALIGN_LEFT);
          reportGrid.addColumn("Status", 20, ReportGrid.ALIGN_LEFT);
        %>

        <%=ReportTitle.script("<img src='images/commands/down.png' border='0' style='cursor:hand;' onclick=\"if (divTarefa.style.display=='none') { divTarefa.style.display='block'; this.src = 'images/commands/up.png'; } else { divTarefa.style.display='none'; this.src = 'images/commands/down.png'; }\" />&nbsp;Tarefas", ReportTitle.LEVEL_1)%>
        <div id="divTarefa" style="display:none;">
          <%String empresa = "";
            // loop nos registros
            for (int i=0; i<tarefaInfoList.length; i++) {
              TarefaInfo tarefaInfo = tarefaInfoList[i];
              String tarefaEmpresa  = tarefaInfo.lookupValueList().get(Tarefa.LOOKUP_EMPRESA).getDisplayFieldValuesToString();%>
            <%if (!empresa.equals(tarefaEmpresa)) {
                empresa = tarefaEmpresa;%>
              <%=(i > 0) ? reportGrid.end() : ""%>
              <p class="FieldLabel"><b><%=empresa%></b></p>
              <%=reportGrid.begin()%>
            <%} // if%>
            <%=reportGrid.beginRow()%>
              <%=reportGrid.beginCell()%> <a title="Ir para Tarefa" href="<%=Tarefa.ACTION_CADASTRO.url(Tarefa.COMMAND_EDIT, Tarefa.FIELD_EMPRESA_ID.getFieldAlias() + "=" + tarefaInfo.getEmpresaId() + "&" + Tarefa.FIELD_TAREFA_ID.getFieldAlias() + "=" + tarefaInfo.getTarefaId())%>" target="frameContent"><%=tarefaInfo.getTarefaId()%></a> <%=reportGrid.endCell()%>
              <%=reportGrid.beginCell()%> <%=(tarefaInfo.getPrazo().before(today) ? "<font color=red>" : "") + DateTools.formatDate(tarefaInfo.getPrazo()) + (tarefaInfo.getPrazo().before(today) ? "</font>" : "")%> <%=reportGrid.endCell()%>
              <%=reportGrid.beginCell()%> <%=tarefaInfo.lookupValueList().get(Tarefa.LOOKUP_DEPARTAMENTO).getDisplayFieldValuesToString()%> <%=reportGrid.endCell()%>
              <%=reportGrid.beginCell()%> <%=getLast(tarefaInfo.lookupValueList().get(Tarefa.LOOKUP_ASSUNTO).getDisplayFieldValuesToString())%> <%=reportGrid.endCell()%>
              <%=reportGrid.beginCell()%> <%=tarefaInfo.lookupValueList().get(Tarefa.LOOKUP_USUARIO).getDisplayFieldValuesToString()%> <%=reportGrid.endCell()%>
              <%=reportGrid.beginCell()%> <%=StatusTarefa.LOOKUP_LIST_FOR_FIELD[tarefaInfo.getStatus()]%> <%=reportGrid.endCell()%>
            <%=reportGrid.endRow()%>
          <%} // for%>
          <%=reportGrid.end()%>
        </div>

      <%}%>
    <%} //if%>

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
