       
<%@include file="../include/beans.jsp"%>

<%@page import="imanager.entity.*"%>
<%@page import="imanager.report.*"%>
<%@page import="imanager.misc.*"%>

<%@page import="securityservice.entity.*"%>

<%
  // início do bloco protegido
  try {
    // nossa instância de Tarefa Departamento
    RelatorioTarefaDepartamento relatorioTarefaDepartamento = (RelatorioTarefaDepartamento)facade.getReport(RelatorioTarefaDepartamento.CLASS_NAME);
    // nossa instância de Assunto
    Assunto assunto = (Assunto)facade.getEntity(Assunto.CLASS_NAME);
    // nossa instância de Usuário
    Usuario usuario = (Usuario)facade.getEntity(Usuario.CLASS_NAME);
    // nossa instância de Tarefa
    Tarefa tarefa = (Tarefa)facade.getEntity(Tarefa.CLASS_NAME);

    // define os valores dos parâmetros de usuário
    relatorioTarefaDepartamento.userParamList().setParamsValues(request);

    // nosso gráfico
    Chart chartRelatorioTarefaDepartamento = new Chart(facade, "chartRelatorioTarefaDepartamento");

    // procura pelo Assunto
    AssuntoInfo assuntoInfo = null;
    if (relatorioTarefaDepartamento.USER_PARAM_ASSUNTO.getIntValue() > 0)
      assuntoInfo = assunto.selectByPrimaryKey(relatorioTarefaDepartamento.USER_PARAM_ASSUNTO.getIntValue());
    // procura pelo Usuário
    UsuarioInfo usuarioInfo = null;
    if (relatorioTarefaDepartamento.USER_PARAM_USUARIO.getIntValue() > 0)
      usuarioInfo = usuario.selectByUsuarioId(relatorioTarefaDepartamento.USER_PARAM_USUARIO.getIntValue());

    // estamos na central de atendimento?
    boolean isCentralAtendimento = request.getParameter(tarefa.CONTROL_PARAM_CENTRAL_ATENDIMENTO.getName()) != null;
    // grava na sessão
    session.setAttribute(tarefa.CONTROL_PARAM_CENTRAL_ATENDIMENTO.getName(), isCentralAtendimento);

    // nosso Grid
    ReportGridEx reportGridRelatorioTarefaDepartamento = new ReportGridEx("gridRelatorioTarefaDepartamento", true);
    reportGridRelatorioTarefaDepartamento.addColumn("ID", 5, ReportGrid.ALIGN_LEFT);
    reportGridRelatorioTarefaDepartamento.addColumn("Usuário", 15, ReportGrid.ALIGN_LEFT);
    reportGridRelatorioTarefaDepartamento.addColumn("Assunto", 20, ReportGrid.ALIGN_LEFT);
    reportGridRelatorioTarefaDepartamento.addColumn("Cliente", 20, ReportGrid.ALIGN_LEFT);
    reportGridRelatorioTarefaDepartamento.addColumn("Status", 15, ReportGrid.ALIGN_LEFT);
    reportGridRelatorioTarefaDepartamento.addColumn("Prazo", 10, ReportGrid.ALIGN_LEFT);
    reportGridRelatorioTarefaDepartamento.addColumn("Prorrogado", 5, ReportGrid.ALIGN_RIGHT);
    reportGridRelatorioTarefaDepartamento.addColumn("Dias em Atraso", 10, ReportGrid.ALIGN_RIGHT);
%>

<html>
  <head>
    <title><%=RelatorioTarefaDepartamento.ACTION.getCaption()%></title>
    <link href="<%=facade.getStyle().reportInterface()%>" rel="stylesheet" type="text/css">
    <script src="include/scripts.js" type="text/javascript"></script>
  </head>
  <body style="margin:5px;" oncontextmenu="return false;">

    <!-- Cabeçalho -->
    <%=ReportHeader.script(facade, RelatorioTarefaDepartamento.ACTION, true, true, true, true, true)%>

    <!-- Parâmetros -->
    <p>
      <table>
        <tr>
          <td><%=ReportParamLabel.script(facade, relatorioTarefaDepartamento.USER_PARAM_DATA_INICIAL)%></td>
          <td><%=ReportParamText.script(facade, relatorioTarefaDepartamento.USER_PARAM_DATA_INICIAL)%></td>
        </tr>
        <tr>
          <td><%=ReportParamLabel.script(facade, relatorioTarefaDepartamento.USER_PARAM_DATA_FINAL)%></td>
          <td><%=ReportParamText.script(facade, relatorioTarefaDepartamento.USER_PARAM_DATA_FINAL)%></td>
        </tr>
        <tr>
          <td><%=ReportParamLabel.script(facade, relatorioTarefaDepartamento.USER_PARAM_STATUS)%></td>
          <td><%=ReportParamText.script(facade, relatorioTarefaDepartamento.USER_PARAM_STATUS)%></td>
        </tr>
        <tr>
          <td><%=ReportParamLabel.script(facade, relatorioTarefaDepartamento.USER_PARAM_ASSUNTO)%></td>
          <td><%=assuntoInfo != null ? assuntoInfo.getNome() : ""%></td>
        </tr>
        <tr>
          <td><%=ReportParamLabel.script(facade, relatorioTarefaDepartamento.USER_PARAM_USUARIO)%></td>
          <td><%=usuarioInfo != null ? usuarioInfo.getNome() : ""%></td>
        </tr>
        <tr>
          <td><%=ReportParamLabel.script(facade, relatorioTarefaDepartamento.USER_PARAM_PRORROGADO)%></td>
          <td><%=ReportParamText.script(facade, relatorioTarefaDepartamento.USER_PARAM_PRORROGADO)%></td>
        </tr>
      </table>
    </p>

    <!-- Gráfico -->
    <p align="center">
      <div id="divChart" align="center" style="width:100%; height:300px;">Carregando dados...</div>
    </p>

    <!-- Grid de dados -->
    <%ResultSet resultSetRelatorioTarefaDepartamento = relatorioTarefaDepartamento.getResultSetTarefaDepartamento(selectedEmpresaId, relatorioTarefaDepartamento.USER_PARAM_DATA_INICIAL.getDateValue(), relatorioTarefaDepartamento.USER_PARAM_DATA_FINAL.getDateValue(), relatorioTarefaDepartamento.USER_PARAM_STATUS.getIntValue(), relatorioTarefaDepartamento.USER_PARAM_ASSUNTO.getIntValue(), relatorioTarefaDepartamento.USER_PARAM_USUARIO.getIntValue(), relatorioTarefaDepartamento.USER_PARAM_PRORROGADO.getIntValue(), relatorioTarefaDepartamento.USER_PARAM_CONSOLIDADO.getIntValue());%>
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
        // armazena o nome do departamento da vez
        String proximoDepartamento = "";
        // loop nos registros
        while(resultSetRelatorioTarefaDepartamento.next()) {
          // se o nome do assunto é diferente do anterior...
          if(!resultSetRelatorioTarefaDepartamento.getString("nome_departamento").equals(proximoDepartamento)) {
            // se não é o primeiro registro... fecha o grid
            if(!primeiro) {
              // adiciona ao gráfico
              chartRelatorioTarefaDepartamento.addValue(proximoDepartamento, "", totalRegistros);%>
              <%=reportGridRelatorioTarefaDepartamento.beginRow(true)%>
                <%=reportGridRelatorioTarefaDepartamento.beginCell(8)%> <%=totalRegistros%> registro(s) <%=reportGridRelatorioTarefaDepartamento.endCell()%>
              <%=reportGridRelatorioTarefaDepartamento.endRow()%>
            <%=reportGridRelatorioTarefaDepartamento.end()%>
          <%} // if
            // imprime novo cabeçalho na tela %>
          <%=ReportTitle.script(resultSetRelatorioTarefaDepartamento.getString("nome_departamento"), ReportTitle.LEVEL_1)%>
          <%// proximoNome recebe o nome do próximo assunto
            proximoDepartamento = resultSetRelatorioTarefaDepartamento.getString("nome_departamento");%>
          <%// zera totalizador
            totalRegistros = 0;%>
          <%=reportGridRelatorioTarefaDepartamento.begin()%>
          <%// se é o primeiro registro... primeiro recebe false
            if(primeiro)
             primeiro = false;%>
        <%} // if
          // seta dataPrazo como a data do registro atual
          dataPrazo.setTime(resultSetRelatorioTarefaDepartamento.getTimestamp("prazo"));
          // calcula a quantidade de dias em atraso
          diasAtraso = ((dataAtual.getTimeInMillis() - dataPrazo.getTimeInMillis())/(1000 * 60 * 60 * 24)) > 0 ? (dataAtual.getTimeInMillis() - dataPrazo.getTimeInMillis())/(1000 * 60 * 60 * 24) : 0;%>
        <%=reportGridRelatorioTarefaDepartamento.beginRow()%>
          <%=reportGridRelatorioTarefaDepartamento.beginCell()%> <a href="<%=Tarefa.ACTION_CADASTRO.url(Tarefa.COMMAND_EDIT, Tarefa.FIELD_EMPRESA_ID.getFieldAlias() + "=" + resultSetRelatorioTarefaDepartamento.getInt("empresa_id") + "&" + Tarefa.FIELD_TAREFA_ID.getFieldAlias() + "=" + resultSetRelatorioTarefaDepartamento.getInt("tarefa_id"))%>" title="Ir para Tarefa..." target="frameContent"> <%=resultSetRelatorioTarefaDepartamento.getInt("tarefa_id")%> </a> <%=reportGridRelatorioTarefaDepartamento.endCell()%>
          <%=reportGridRelatorioTarefaDepartamento.beginCell()%> <%=resultSetRelatorioTarefaDepartamento.getString("nome_usuario")%> <%=reportGridRelatorioTarefaDepartamento.endCell()%>
          <%=reportGridRelatorioTarefaDepartamento.beginCell()%> <%=resultSetRelatorioTarefaDepartamento.getString("nome_assunto")%> <%=reportGridRelatorioTarefaDepartamento.endCell()%>
          <%=reportGridRelatorioTarefaDepartamento.beginCell()%> <%=resultSetRelatorioTarefaDepartamento.getString("nome_cliente")%> <%=reportGridRelatorioTarefaDepartamento.endCell()%>
          <%=reportGridRelatorioTarefaDepartamento.beginCell()%> <%=StatusTarefa.LOOKUP_LIST_FOR_FIELD[resultSetRelatorioTarefaDepartamento.getInt("status")]%> <%=reportGridRelatorioTarefaDepartamento.endCell()%>
          <%=reportGridRelatorioTarefaDepartamento.beginCell()%> <%=diasAtraso != 0 ? "<font style=color:red>" + DateTools.formatDate(resultSetRelatorioTarefaDepartamento.getTimestamp("prazo")) + "</font>" : DateTools.formatDate(resultSetRelatorioTarefaDepartamento.getTimestamp("prazo"))%> <%=reportGridRelatorioTarefaDepartamento.endCell()%>
          <%=reportGridRelatorioTarefaDepartamento.beginCell()%> <%=resultSetRelatorioTarefaDepartamento.getString("prorrogado")%> <%=reportGridRelatorioTarefaDepartamento.endCell()%>
          <%=reportGridRelatorioTarefaDepartamento.beginCell()%> <%=(int)diasAtraso%> <%=reportGridRelatorioTarefaDepartamento.endCell()%>
        <%=reportGridRelatorioTarefaDepartamento.endRow()%>
        <%// adiciona no totalizador
          totalRegistros++;
          // se é o ultimo registro...%>
        <%if(resultSetRelatorioTarefaDepartamento.isLast()) {
            // adiciona ao gráfico
            chartRelatorioTarefaDepartamento.addValue(resultSetRelatorioTarefaDepartamento.getString("nome_departamento"), "", totalRegistros);%>
            <%=reportGridRelatorioTarefaDepartamento.beginRow(true)%>
              <%=reportGridRelatorioTarefaDepartamento.beginCell(8)%> <%=totalRegistros%> registro(s) <%=reportGridRelatorioTarefaDepartamento.endCell()%>
            <%=reportGridRelatorioTarefaDepartamento.endRow()%>
          <%=reportGridRelatorioTarefaDepartamento.end()%>
        <%} // if%>
      <%} // while%>
    <%}
      finally {
        // libera recursos
        resultSetRelatorioTarefaDepartamento.getStatement().close();
        resultSetRelatorioTarefaDepartamento.close();
      } // try-finally
    %>

    <!-- Rodapé -->
    <%=ReportFooter.script(facade, true, true)%>

    <!-- gera o gráfico -->
    <%=chartRelatorioTarefaDepartamento.script(Chart.TYPE_COLUMN, "divChart", "Gráfico Tarefa por Usuário", "", "", Chart.INTERFACE_STYLE_REPORT_INTERFACE)%>

  </body>
</html>

<%}
  // término do bloco protegido
  catch (Exception e) {
    Controller.forwardException(e, pageContext);
  } // try-catch
%>
