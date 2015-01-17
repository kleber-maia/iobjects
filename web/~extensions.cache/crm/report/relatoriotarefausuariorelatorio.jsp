       
<%@include file="../include/beans.jsp"%>

<%@page import="imanager.entity.*"%>
<%@page import="imanager.report.*"%>
<%@page import="imanager.misc.*"%>

<%
  // início do bloco protegido
  try {
    // nossa instância de Tarefa Usuário
    RelatorioTarefaUsuario relatorioTarefaUsuario = (RelatorioTarefaUsuario)facade.getReport(RelatorioTarefaUsuario.CLASS_NAME);
    // nossa instância de Assunto
    Assunto assunto = (Assunto)facade.getEntity(Assunto.CLASS_NAME);
    // nossa instância de Departamento
    Departamento departamento = (Departamento)facade.getEntity(Departamento.CLASS_NAME);
    // nossa instância de Tarefa
    Tarefa tarefa = (Tarefa)facade.getEntity(Tarefa.CLASS_NAME);

    // define os valores dos parâmetros de usuário
    relatorioTarefaUsuario.userParamList().setParamsValues(request);

    // nosso gráfico
    Chart chartRelatorioTarefaUsuario = new Chart(facade, "chartRelatorioTarefaUsuario");

    // procura pelo Assunto
    AssuntoInfo assuntoInfo = null;
    if (relatorioTarefaUsuario.USER_PARAM_ASSUNTO.getIntValue() > 0)
      assuntoInfo = assunto.selectByPrimaryKey(relatorioTarefaUsuario.USER_PARAM_ASSUNTO.getIntValue());
    // procura pelo Departamento
    DepartamentoInfo departamentoInfo = null;
    if (relatorioTarefaUsuario.USER_PARAM_DEPARTAMENTO.getIntValue() > 0)
      departamentoInfo = departamento.selectByPrimaryKey(selectedEmpresaId, relatorioTarefaUsuario.USER_PARAM_DEPARTAMENTO.getIntValue());

    // estamos na central de atendimento?
    boolean isCentralAtendimento = request.getParameter(tarefa.CONTROL_PARAM_CENTRAL_ATENDIMENTO.getName()) != null;
    // grava na sessão
    session.setAttribute(tarefa.CONTROL_PARAM_CENTRAL_ATENDIMENTO.getName(), isCentralAtendimento);

    // nosso Grid
    ReportGridEx reportGridRelatorioTarefaUsuario = new ReportGridEx("gridRelatorioTarefaUsuario", true);
    reportGridRelatorioTarefaUsuario.addColumn("ID", 5, ReportGrid.ALIGN_LEFT);
    reportGridRelatorioTarefaUsuario.addColumn("Departamento", 15, ReportGrid.ALIGN_LEFT);
    reportGridRelatorioTarefaUsuario.addColumn("Assunto", 20, ReportGrid.ALIGN_LEFT);
    reportGridRelatorioTarefaUsuario.addColumn("Cliente", 20, ReportGrid.ALIGN_LEFT);
    reportGridRelatorioTarefaUsuario.addColumn("Status", 15, ReportGrid.ALIGN_LEFT);
    reportGridRelatorioTarefaUsuario.addColumn("Prazo", 10, ReportGrid.ALIGN_LEFT);
    reportGridRelatorioTarefaUsuario.addColumn("Prorrogado", 5, ReportGrid.ALIGN_RIGHT);
    reportGridRelatorioTarefaUsuario.addColumn("Dias em Atraso", 10, ReportGrid.ALIGN_RIGHT);
%>

<html>
  <head>
    <title><%=RelatorioTarefaUsuario.ACTION.getCaption()%></title>
    <link href="<%=facade.getStyle().reportInterface()%>" rel="stylesheet" type="text/css">
    <script src="include/scripts.js" type="text/javascript"></script>
  </head>
  <body style="margin:5px;" oncontextmenu="return false;">

    <!-- Cabeçalho -->
    <%=ReportHeader.script(facade, RelatorioTarefaUsuario.ACTION, true, true, true, true, true)%>

    <!-- Parâmetros -->
    <p>
      <table>
        <tr>
          <td><%=ReportParamLabel.script(facade, relatorioTarefaUsuario.USER_PARAM_DATA_INICIAL)%></td>
          <td><%=ReportParamText.script(facade, relatorioTarefaUsuario.USER_PARAM_DATA_INICIAL)%></td>
        </tr>
        <tr>
          <td><%=ReportParamLabel.script(facade, relatorioTarefaUsuario.USER_PARAM_DATA_FINAL)%></td>
          <td><%=ReportParamText.script(facade, relatorioTarefaUsuario.USER_PARAM_DATA_FINAL)%></td>
        </tr>
        <tr>
          <td><%=ReportParamLabel.script(facade, relatorioTarefaUsuario.USER_PARAM_STATUS)%></td>
          <td><%=ReportParamText.script(facade, relatorioTarefaUsuario.USER_PARAM_STATUS)%></td>
        </tr>
        <tr>
          <td><%=ReportParamLabel.script(facade, relatorioTarefaUsuario.USER_PARAM_ASSUNTO)%></td>
          <td><%=assuntoInfo != null ? assuntoInfo.getNome() : ""%></td>
        </tr>
        <tr>
          <td><%=ReportParamLabel.script(facade, relatorioTarefaUsuario.USER_PARAM_DEPARTAMENTO)%></td>
          <td><%=departamentoInfo != null ? departamentoInfo.getNome() : ""%></td>
        </tr>
        <tr>
          <td><%=ReportParamLabel.script(facade, relatorioTarefaUsuario.USER_PARAM_PRORROGADO)%></td>
          <td><%=ReportParamText.script(facade, relatorioTarefaUsuario.USER_PARAM_PRORROGADO)%></td>
        </tr>
      </table>
    </p>

    <!-- Gráfico -->
    <p align="center">
      <div id="divChart" align="center" style="width:100%; height:300px;">Carregando dados...</div>
    </p>

    <!-- Grid de dados -->
    <%ResultSet resultSetRelatorioTarefaUsuario = relatorioTarefaUsuario.getResultSetTarefaUsuario(selectedEmpresaId, relatorioTarefaUsuario.USER_PARAM_DATA_INICIAL.getDateValue(), relatorioTarefaUsuario.USER_PARAM_DATA_FINAL.getDateValue(), relatorioTarefaUsuario.USER_PARAM_STATUS.getIntValue(), relatorioTarefaUsuario.USER_PARAM_ASSUNTO.getIntValue(), relatorioTarefaUsuario.USER_PARAM_DEPARTAMENTO.getIntValue(), relatorioTarefaUsuario.USER_PARAM_PRORROGADO.getIntValue(), relatorioTarefaUsuario.USER_PARAM_CONSOLIDADO.getIntValue());%>
    <%try {
        // controlador do primeiro registro
        boolean primeiro = true;
        // dias em atraso
        Calendar dataPrazo = Calendar.getInstance();
        Calendar dataAtual = Calendar.getInstance();
        // armazena os dias em atraso
        double diasAtraso = 0;
        // totalizador
        int totalRegistros = 0;
        // armazena o nome do usuário da vez
        String proximoNome = "";
        // loop nos registros
        while(resultSetRelatorioTarefaUsuario.next()) {
          // se o nome do usuário é diferente do anterior...
          if(!resultSetRelatorioTarefaUsuario.getString("nome_usuario").equals(proximoNome)) {
            // se não é o primeiro registro... fecha o grid
            if(!primeiro) {
              // adiciona ao gráfico
              chartRelatorioTarefaUsuario.addValue(proximoNome, "", totalRegistros);%>
              <%=reportGridRelatorioTarefaUsuario.beginRow(true)%>
                <%=reportGridRelatorioTarefaUsuario.beginCell(8)%> <%=totalRegistros%> registro(s) <%=reportGridRelatorioTarefaUsuario.endCell()%>
              <%=reportGridRelatorioTarefaUsuario.endRow()%>
            <%=reportGridRelatorioTarefaUsuario.end()%>
          <%} // if
            // imprime novo cabeçalho na tela %>
          <%=ReportTitle.script(resultSetRelatorioTarefaUsuario.getString("nome_usuario"), ReportTitle.LEVEL_1)%>
          <%// proximoNome recebe o nome do próximo usuário
            proximoNome = resultSetRelatorioTarefaUsuario.getString("nome_usuario");%>
          <%// zera totalizador
            totalRegistros = 0;%>
          <%=reportGridRelatorioTarefaUsuario.begin()%>
          <%// se é o primeiro registro... primeiro recebe false
            if(primeiro)
             primeiro = false;%>
        <%} // if
          // seta dataPrazo como a data do registro atual
          dataPrazo.setTime(resultSetRelatorioTarefaUsuario.getTimestamp("prazo"));
          // calcula a quantidade de dias em atraso
          diasAtraso = ((dataAtual.getTimeInMillis() - dataPrazo.getTimeInMillis())/(1000 * 60 * 60 * 24)) > 0 ? (dataAtual.getTimeInMillis() - dataPrazo.getTimeInMillis())/(1000 * 60 * 60 * 24) : 0;%>
        <%=reportGridRelatorioTarefaUsuario.beginRow()%>
          <%=reportGridRelatorioTarefaUsuario.beginCell()%> <a href="<%=Tarefa.ACTION_CADASTRO.url(Tarefa.COMMAND_EDIT, Tarefa.FIELD_EMPRESA_ID.getFieldAlias() + "=" + resultSetRelatorioTarefaUsuario.getInt("empresa_id") + "&" + Tarefa.FIELD_TAREFA_ID.getFieldAlias() + "=" + resultSetRelatorioTarefaUsuario.getInt("tarefa_id"))%>" title="Ir para Tarefa..." target="frameContent"> <%=resultSetRelatorioTarefaUsuario.getInt("tarefa_id")%> </a> <%=reportGridRelatorioTarefaUsuario.endCell()%>
          <%=reportGridRelatorioTarefaUsuario.beginCell()%> <%=resultSetRelatorioTarefaUsuario.getString("nome_departamento")%> <%=reportGridRelatorioTarefaUsuario.endCell()%>
          <%=reportGridRelatorioTarefaUsuario.beginCell()%> <%=resultSetRelatorioTarefaUsuario.getString("nome_assunto")%> <%=reportGridRelatorioTarefaUsuario.endCell()%>
          <%=reportGridRelatorioTarefaUsuario.beginCell()%> <%=resultSetRelatorioTarefaUsuario.getString("nome_cliente")%> <%=reportGridRelatorioTarefaUsuario.endCell()%>
          <%=reportGridRelatorioTarefaUsuario.beginCell()%> <%=StatusTarefa.LOOKUP_LIST_FOR_FIELD[resultSetRelatorioTarefaUsuario.getInt("status")]%> <%=reportGridRelatorioTarefaUsuario.endCell()%>
          <%=reportGridRelatorioTarefaUsuario.beginCell()%> <%=diasAtraso != 0 ? "<font style=color:red>" + DateTools.formatDate(resultSetRelatorioTarefaUsuario.getTimestamp("prazo")) + "</font>" : DateTools.formatDate(resultSetRelatorioTarefaUsuario.getTimestamp("prazo"))%> <%=reportGridRelatorioTarefaUsuario.endCell()%>
          <%=reportGridRelatorioTarefaUsuario.beginCell()%> <%=resultSetRelatorioTarefaUsuario.getString("prorrogado")%> <%=reportGridRelatorioTarefaUsuario.endCell()%>
          <%=reportGridRelatorioTarefaUsuario.beginCell()%> <%=(int)diasAtraso%> <%=reportGridRelatorioTarefaUsuario.endCell()%>
        <%=reportGridRelatorioTarefaUsuario.endRow()%>
        <%// adiciona no totalizador
          totalRegistros++;
          // se é o ultimo registro...%>
        <%if(resultSetRelatorioTarefaUsuario.isLast()) {
            // adiciona ao gráfico
            chartRelatorioTarefaUsuario.addValue(resultSetRelatorioTarefaUsuario.getString("nome_usuario"), "", totalRegistros);%>
            <%=reportGridRelatorioTarefaUsuario.beginRow(true)%>
              <%=reportGridRelatorioTarefaUsuario.beginCell(8)%> <%=totalRegistros%> registro(s) <%=reportGridRelatorioTarefaUsuario.endCell()%>
            <%=reportGridRelatorioTarefaUsuario.endRow()%>
          <%=reportGridRelatorioTarefaUsuario.end()%>
        <%} // if%>
      <%} // while%>
    <%}
      finally {
        // libera recursos
        resultSetRelatorioTarefaUsuario.getStatement().close();
        resultSetRelatorioTarefaUsuario.close();
      } // try-finally
    %>

    <!-- Rodapé -->
    <%=ReportFooter.script(facade, true, true)%>

    <!-- gera o gráfico -->
    <%=chartRelatorioTarefaUsuario.script(Chart.TYPE_COLUMN, "divChart", "Gráfico Tarefa por Usuário", "", "", Chart.INTERFACE_STYLE_REPORT_INTERFACE)%>

  </body>
</html>

<%}
  // término do bloco protegido
  catch (Exception e) {
    Controller.forwardException(e, pageContext);
  } // try-catch
%>
