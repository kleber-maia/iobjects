
<%@include file="../include/beans.jsp"%>

<%@page import="imanager.entity.*"%>
<%@page import="imanager.report.*"%>
<%@page import="imanager.ui.entity.*"%>

<%@page import="securityservice.entity.*"%>

<%!
  public String gridFooter(ReportGridEx reportGrid, int totalRegistros) throws Exception {
    return reportGrid.beginRow(true)
         +   reportGrid.beginCell(9) + "<div style=\"text-align:left;\">" + totalRegistros + " registro(s)</div>" + reportGrid.endCell()
         + reportGrid.endRow()
         + reportGrid.end();
  }
%>

<%
  // início do bloco protegido
  try {
    // nossa instância de Ranking Atendimento Usuário
    RelatorioRankingAtendimentoUsuario relatorioRankingAtendimentoUsuario = (RelatorioRankingAtendimentoUsuario)facade.getReport(RelatorioRankingAtendimentoUsuario.CLASS_NAME);
    // nossa instância de Departamento
    Departamento departamento = (Departamento)facade.getEntity(Departamento.CLASS_NAME);
    // nossa instância de Campanha
    Campanha campanha = (Campanha)facade.getEntity(Campanha.CLASS_NAME);
    // nossa instância de Atendimento
    Atendimento atendimento = (Atendimento)facade.getEntity(Atendimento.CLASS_NAME);

    // define os valores dos parâmetros de usuário
    relatorioRankingAtendimentoUsuario.userParamList().setParamsValues(request);

    // nosso gráfico
    Chart chart = new Chart(facade, "chartRelatorioRankingAtendimentoUsuario");

    // procura pelo Departamento
    DepartamentoInfo departamentoInfo = null;
    if(relatorioRankingAtendimentoUsuario.USER_PARAM_DEPARTAMENTO_ID.getIntValue() > 0)
      departamentoInfo = departamento.selectByPrimaryKey(selectedEmpresaId, relatorioRankingAtendimentoUsuario.USER_PARAM_DEPARTAMENTO_ID.getIntValue());
    // procura pela Campanha
    CampanhaInfo campanhaInfo = null;
    if(relatorioRankingAtendimentoUsuario.USER_PARAM_CAMPANHA_ID.getIntValue() > 0)
      campanhaInfo = campanha.selectByPrimaryKey(relatorioRankingAtendimentoUsuario.USER_PARAM_CAMPANHA_ID.getIntValue());

    // estamos na central de atendimento?
    boolean isCentralAtendimento = request.getParameter(atendimento.CONTROL_PARAM_CENTRAL_ATENDIMENTO.getName()) != null;
    // grava na sessão
    session.setAttribute(atendimento.CONTROL_PARAM_CENTRAL_ATENDIMENTO.getName(), isCentralAtendimento);

    // nosso Grid
    ReportGridEx reportGridRelatorioRankingAtendimentoUsuario = new ReportGridEx("gridRelatorioRankingAtendimentoUsuario", true);
    reportGridRelatorioRankingAtendimentoUsuario.addColumn("Usuário", 75, ReportGrid.ALIGN_LEFT);
    reportGridRelatorioRankingAtendimentoUsuario.addColumn("Atendimento", 25, ReportGrid.ALIGN_RIGHT);

    // nosso Grid
    ReportGridEx reportGridUsuario = new ReportGridEx("gridUsuario", true);
    reportGridUsuario.addColumn("!", 3, ReportGrid.ALIGN_LEFT);
    reportGridUsuario.addColumn("ID", 3, ReportGrid.ALIGN_LEFT);
    reportGridUsuario.addColumn("Departamento", 13, ReportGrid.ALIGN_LEFT);
    reportGridUsuario.addColumn("Assunto", 15, ReportGrid.ALIGN_LEFT);
    reportGridUsuario.addColumn("Meio", 13, ReportGrid.ALIGN_LEFT);
    reportGridUsuario.addColumn("Campanha", 15, ReportGrid.ALIGN_LEFT);
    reportGridUsuario.addColumn("Cliente", 18, ReportGrid.ALIGN_LEFT);
    reportGridUsuario.addColumn("Data/Hora Início", 10, ReportGrid.ALIGN_LEFT);
    reportGridUsuario.addColumn("Data/Hora Término", 10, ReportGrid.ALIGN_LEFT);
%>

<html>
  <head>
    <title><%=RelatorioRankingAtendimentoUsuario.ACTION.getCaption()%></title>
    <link href="<%=facade.getStyle().reportInterface()%>" rel="stylesheet" type="text/css">
    <script src="include/scripts.js" type="text/javascript"></script>
  </head>
  <body style="margin:5px;" oncontextmenu="return false;">

    <!-- Cabeçalho -->
    <%=ReportHeader.script(facade, RelatorioRankingAtendimentoUsuario.ACTION, true, true, true, true, true)%>

    <!-- Parâmetros -->
    <p>
      <table>
        <tr>
          <td><%=ReportParamLabel.script(facade, relatorioRankingAtendimentoUsuario.USER_PARAM_DATA_INICIAL)%></td>
          <td><%=ReportParamText.script(facade, relatorioRankingAtendimentoUsuario.USER_PARAM_DATA_INICIAL)%></td>
        </tr>
        <tr>
          <td><%=ReportParamLabel.script(facade, relatorioRankingAtendimentoUsuario.USER_PARAM_DATA_FINAL)%></td>
          <td><%=ReportParamText.script(facade, relatorioRankingAtendimentoUsuario.USER_PARAM_DATA_FINAL)%></td>
        </tr>
        <tr>
          <td><%=ReportParamLabel.script(facade, relatorioRankingAtendimentoUsuario.USER_PARAM_DEPARTAMENTO_ID)%></td>
          <td><%=departamentoInfo != null ? departamentoInfo.getNome() : ""%></td>
        </tr>
        <tr>
          <td><%=ReportParamLabel.script(facade, relatorioRankingAtendimentoUsuario.USER_PARAM_CAMPANHA_ID)%></td>
          <td><%=campanhaInfo != null ? campanhaInfo.getNome() : ""%></td>
        </tr>
        <tr>
          <td><%=ReportParamLabel.script(facade, relatorioRankingAtendimentoUsuario.USER_PARAM_CONSOLIDADO)%></td>
          <td><%=ReportParamText.script(facade, relatorioRankingAtendimentoUsuario.USER_PARAM_CONSOLIDADO)%></td>
        </tr>
      </table>
    </p>

    <!-- Gráfico -->
    <p align="center">
      <div id="divChart" align="center" style="width:100%; height:300px;">Carregando dados...</div>
    </p>
    
    <script type="text/javascript">
      function showHideAnotacao(empresaId, atendimentoId) {
        var rowAnotacao = document.getElementById("rowAnotacao" + empresaId + atendimentoId);
        if (rowAnotacao.style.display == "none")
          rowAnotacao.style.display = "block";
        else
          rowAnotacao.style.display = "none";
      }
    </script>

    <!-- Título de dados -->
    <%=ReportTitle.script("Dados", ReportTitle.LEVEL_1)%>

    <!-- Grid de dados -->
    <%ResultSet resultsetRelatorioRankingAtendimentoUsuario = relatorioRankingAtendimentoUsuario.getResultSetRankingAtendimentoUsuario(selectedEmpresaId, relatorioRankingAtendimentoUsuario.USER_PARAM_DEPARTAMENTO_ID.getIntValue(), relatorioRankingAtendimentoUsuario.USER_PARAM_CAMPANHA_ID.getIntValue(), relatorioRankingAtendimentoUsuario.USER_PARAM_DATA_INICIAL.getDateValue(), relatorioRankingAtendimentoUsuario.USER_PARAM_DATA_FINAL.getDateValue(), relatorioRankingAtendimentoUsuario.USER_PARAM_CONSOLIDADO.getIntValue());%>
    <%ResultSet resultSetUsuarios = relatorioRankingAtendimentoUsuario.getResultSetUsuarios(selectedEmpresaId, relatorioRankingAtendimentoUsuario.USER_PARAM_DEPARTAMENTO_ID.getIntValue(), relatorioRankingAtendimentoUsuario.USER_PARAM_CAMPANHA_ID.getIntValue(), relatorioRankingAtendimentoUsuario.USER_PARAM_DATA_INICIAL.getDateValue(), relatorioRankingAtendimentoUsuario.USER_PARAM_DATA_FINAL.getDateValue(), relatorioRankingAtendimentoUsuario.USER_PARAM_CONSOLIDADO.getIntValue());%>
    <%// totalizadores
      int totalRegistros = 0;
      int totalAtendimentos = 0;
    %>
    <%=reportGridRelatorioRankingAtendimentoUsuario.begin()%>
    <%try {
        // loop nos registros
        while(resultsetRelatorioRankingAtendimentoUsuario.next()) {
          // adiciona nos totalizadores
          totalRegistros++;
          totalAtendimentos += resultsetRelatorioRankingAtendimentoUsuario.getInt("total_atendimento");
          // adiciona ao gráfico
          chart.addValue(resultsetRelatorioRankingAtendimentoUsuario.getString("nome"), "", resultsetRelatorioRankingAtendimentoUsuario.getInt("total_atendimento"));%>
       <%=reportGridRelatorioRankingAtendimentoUsuario.beginRow()%>
         <%=reportGridRelatorioRankingAtendimentoUsuario.beginCell()%> <%=resultsetRelatorioRankingAtendimentoUsuario.getString("nome")%> <%= reportGridRelatorioRankingAtendimentoUsuario.endCell()%>
         <%=reportGridRelatorioRankingAtendimentoUsuario.beginCell()%> <%=resultsetRelatorioRankingAtendimentoUsuario.getInt("total_atendimento")%> <%= reportGridRelatorioRankingAtendimentoUsuario.endCell()%>
       <%=reportGridRelatorioRankingAtendimentoUsuario.endRow()%>
      <%} // while%>
     <%=reportGridRelatorioRankingAtendimentoUsuario.beginRow(true)%>
       <%=reportGridRelatorioRankingAtendimentoUsuario.beginCell()%> <%=NumberTools.format(totalRegistros)%> registros <%=reportGridRelatorioRankingAtendimentoUsuario.endCell()%>
       <%=reportGridRelatorioRankingAtendimentoUsuario.beginCell()%> <%=NumberTools.format(totalAtendimentos)%> <%=reportGridRelatorioRankingAtendimentoUsuario.endCell()%>
     <%=reportGridRelatorioRankingAtendimentoUsuario.endRow()%>
    <%=reportGridRelatorioRankingAtendimentoUsuario.end()%>
      <%// controlador
        boolean primeiro = true;
        // zera totalizador
        totalRegistros = 0;
        // armazena o nome do usuário da vez
        String proximoNome = "";
        // contador
        int count = 0;
        // loop nos registros
        while(resultSetUsuarios.next()) {
          // se o nome do usuário é diferente do anterior...
          if(!resultSetUsuarios.getString("nome_usuario").equals(proximoNome)) {
            // se não é o primeiro registro... fecha o grid
            if(!primeiro) {%>
              <%=gridFooter(reportGridUsuario, totalRegistros)%>
          <%} // if
          // zera nosso contador
          count = 0;
            // imprime novo cabeçalho na tela %>
          <%=ReportTitle.script(resultSetUsuarios.getString("nome_usuario"), ReportTitle.LEVEL_1)%>
          <%// proximoNome recebe o nome do próximo usuário
            proximoNome = resultSetUsuarios.getString("nome_usuario");%>
          <%// zera totalizador
            totalRegistros = 0;%>
          <%=reportGridUsuario.begin()%>
          <%if(primeiro)
             primeiro = false;%>
        <%} // if%>
        <%=reportGridUsuario.beginRow()%>
          <%=reportGridUsuario.beginCell()%> <a href="javascript:showHideAnotacao(<%=resultSetUsuarios.getString("in_empresa_id")%>, <%=resultSetUsuarios.getString("in_atendimento_id")%>)">[+]</a> <%=reportGridUsuario.endCell()%>
          <%=reportGridUsuario.beginCell()%> <a href="<%=Atendimento.ACTION_CADASTRO.url(Atendimento.COMMAND_EDIT, Atendimento.FIELD_EMPRESA_ID.getFieldAlias() + "=" + resultSetUsuarios.getInt("in_empresa_id") + "&" + Atendimento.FIELD_ATENDIMENTO_ID.getFieldAlias() + "=" + resultSetUsuarios.getInt("in_atendimento_id"))%>" title="Ir para Atendimento..." target="frameContent"> <%=resultSetUsuarios.getInt("in_atendimento_id")%> </a> <%=reportGridUsuario.endCell()%>
          <%=reportGridUsuario.beginCell()%> <%=resultSetUsuarios.getString("nome_departamento")%> <%=reportGridUsuario.endCell()%>
          <%=reportGridUsuario.beginCell()%> <%=resultSetUsuarios.getString("nome_assunto")%> <%=reportGridUsuario.endCell()%>
          <%=reportGridUsuario.beginCell()%> <%=resultSetUsuarios.getString("nome_meio")%> <%=reportGridUsuario.endCell()%>
          <%=reportGridUsuario.beginCell()%> <%=resultSetUsuarios.getString("nome_campanha")%> <%=reportGridUsuario.endCell()%>
          <%=reportGridUsuario.beginCell()%> <%=resultSetUsuarios.getString("nome_cliente")%> <%=reportGridUsuario.endCell()%>
          <%=reportGridUsuario.beginCell()%> <%=DateTools.formatDateTime(resultSetUsuarios.getTimestamp("dt_inicio"))%> <%=reportGridUsuario.endCell()%>
          <%=reportGridUsuario.beginCell()%> <%=DateTools.formatDateTime(resultSetUsuarios.getTimestamp("dt_termino"))%> <%=reportGridUsuario.endCell()%>
        <%=reportGridUsuario.endRow()%>
        
        <%// incrementa o contador
          count++;
          // estilo da linha
          String rowClassName = count % 2 == 0 ? "gridRowEven" : "griRowOdd";%>
        <tr id="rowAnotacao<%=resultSetUsuarios.getString("in_empresa_id") + resultSetUsuarios.getString("in_atendimento_id")%>" style="display:none;">
          <td class="<%=rowClassName%>"> </td>
          <td class="<%=rowClassName%>" colspan="8"> <%=resultSetUsuarios.getString("va_descricao")%> </td>
        </tr>
        
        <%// adiciona no totalizador
          totalRegistros++;
          // se é o ultimo registro...%>
        <%if(resultSetUsuarios.isLast()) {%>
          <%=gridFooter(reportGridUsuario, totalRegistros)%>
        <%} // if%>
      <%} // while%>
    <%}
      finally {
        // libera recursos
        resultsetRelatorioRankingAtendimentoUsuario.getStatement().close();
        resultsetRelatorioRankingAtendimentoUsuario.close();
        resultSetUsuarios.getStatement().close();
        resultSetUsuarios.close();
      } // try-finally
    %>

    <!-- Rodapé -->
    <%=ReportFooter.script(facade, true, true)%>

    <!-- gera o gráfico -->
    <%=chart.script(Chart.TYPE_COLUMN, "divChart", "Ranking Atendimento Usuário", "", "", Chart.INTERFACE_STYLE_REPORT_INTERFACE)%>

  </body>
</html>

<%}
  // término do bloco protegido
  catch (Exception e) {
    Controller.forwardException(e, pageContext);
  } // try-catch
%>
