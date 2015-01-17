       
<%@include file="../include/beans.jsp"%>

<%@page import="imanager.entity.*"%>
<%@page import="imanager.misc.*"%>
<%@page import="imanager.report.*"%>

<%
  // início do bloco protegido
  try {
    // nossa instância de RelatorioRankingOportunidadeUsuario
    RelatorioRankingOportunidadeUsuario relatorioRankingOportunidadeUsuario = (RelatorioRankingOportunidadeUsuario)facade.getReport(RelatorioRankingOportunidadeUsuario.CLASS_NAME);
    // nossa instância de Campanha
    Campanha campanha = (Campanha)facade.getEntity(Campanha.CLASS_NAME);
    // nossa instância de Departamento
    Departamento departamento = (Departamento)facade.getEntity(Departamento.CLASS_NAME);
    // nossa instância de Fase
    Fase fase = (Fase)facade.getEntity(Fase.CLASS_NAME);
    // nossa instância de Oportunidade
    Oportunidade oportunidade = (Oportunidade)facade.getEntity(Oportunidade.CLASS_NAME);

    // define os valores dos parâmetros de usuário
    relatorioRankingOportunidadeUsuario.userParamList().setParamsValues(request);

    // nosso gráfico
    Chart chartRelatorioRankingOportunidadeUsuario = new Chart(facade, "chartRelatorioRankingOportunidadeUsuario");

    // procura pela Campanha
    CampanhaInfo campanhaInfo = null;
    if(relatorioRankingOportunidadeUsuario.USER_PARAM_CAMPANHA_ID.getIntValue() > 0)
      campanhaInfo = campanha.selectByPrimaryKey(relatorioRankingOportunidadeUsuario.USER_PARAM_CAMPANHA_ID.getIntValue());
    // procura pelo Departamento
    DepartamentoInfo departamentoInfo = null;
    if(relatorioRankingOportunidadeUsuario.USER_PARAM_DEPARTAMENTO_ID.getIntValue() > 0)
      departamentoInfo = departamento.selectByPrimaryKey(selectedEmpresaId, relatorioRankingOportunidadeUsuario.USER_PARAM_DEPARTAMENTO_ID.getIntValue());
    // procura pelo Fase
    FaseInfo faseInfo = null;
    if(relatorioRankingOportunidadeUsuario.USER_PARAM_FASE_ID.getIntValue() > 0)
      faseInfo = fase.selectByPrimaryKey(relatorioRankingOportunidadeUsuario.USER_PARAM_FASE_ID.getIntValue());

    // estamos na central de atendimento?
    boolean isCentralAtendimento = request.getParameter(oportunidade.CONTROL_PARAM_CENTRAL_ATENDIMENTO.getName()) != null;
    // grava na sessão
    session.setAttribute(oportunidade.CONTROL_PARAM_CENTRAL_ATENDIMENTO.getName(), isCentralAtendimento);

    // nosso Grid
    ReportGridEx reportGridRelatorioRankingOportunidadeUsuario = new ReportGridEx("gridRelatorioRankingOportunidadeUsuario", true);
    reportGridRelatorioRankingOportunidadeUsuario.addColumn("Usuário", 75, ReportGrid.ALIGN_LEFT);
    reportGridRelatorioRankingOportunidadeUsuario.addColumn("Oportunidade", 25, ReportGrid.ALIGN_RIGHT);
    // nosso Grid
    ReportGridEx reportGridUsuario = new ReportGridEx("gridUsuario", true);
    reportGridUsuario.addColumn("ID", 5, ReportGrid.ALIGN_LEFT);
    reportGridUsuario.addColumn("Departamento", 15, ReportGrid.ALIGN_LEFT);
    reportGridUsuario.addColumn("Campanha", 18, ReportGrid.ALIGN_LEFT);
    reportGridUsuario.addColumn("Cliente", 15, ReportGrid.ALIGN_LEFT);
    reportGridUsuario.addColumn("Fase", 15, ReportGrid.ALIGN_LEFT);
    reportGridUsuario.addColumn("Acompanhamento", 10, ReportGrid.ALIGN_LEFT);
    reportGridUsuario.addColumn("Status", 10, ReportGrid.ALIGN_LEFT);
    reportGridUsuario.addColumn("% Sucesso", 7, ReportGrid.ALIGN_RIGHT);
    reportGridUsuario.addColumn("Valor", 5, ReportGrid.ALIGN_RIGHT);
%>

<html>
  <head>
    <title><%=RelatorioRankingOportunidadeUsuario.ACTION.getCaption()%></title>
    <link href="<%=facade.getStyle().reportInterface()%>" rel="stylesheet" type="text/css">
    <script src="include/scripts.js" type="text/javascript"></script>
  </head>
  <body style="margin:5px;" oncontextmenu="return false;">

    <!-- Cabeçalho -->
    <%=ReportHeader.script(facade, RelatorioRankingOportunidadeUsuario.ACTION, true, true, true, true, true)%>

    <!-- Parâmetros -->
    <p>
      <table>
        <tr>
          <td><%=ReportParamLabel.script(facade, relatorioRankingOportunidadeUsuario.USER_PARAM_DATA_INICIAL)%></td>
          <td><%=ReportParamText.script(facade, relatorioRankingOportunidadeUsuario.USER_PARAM_DATA_INICIAL)%></td>
        </tr>
        <tr>
          <td><%=ReportParamLabel.script(facade, relatorioRankingOportunidadeUsuario.USER_PARAM_DATA_FINAL)%></td>
          <td><%=ReportParamText.script(facade, relatorioRankingOportunidadeUsuario.USER_PARAM_DATA_FINAL)%></td>
        </tr>
        <tr>
          <td><%=ReportParamLabel.script(facade, relatorioRankingOportunidadeUsuario.USER_PARAM_CAMPANHA_ID)%></td>
          <td><%=campanhaInfo != null ? campanhaInfo.getNome() : ""%></td>
        </tr>
        <tr>
          <td><%=ReportParamLabel.script(facade, relatorioRankingOportunidadeUsuario.USER_PARAM_DEPARTAMENTO_ID)%></td>
          <td><%=departamentoInfo != null ? departamentoInfo.getNome() : ""%></td>
        </tr>
        <tr>
          <td><%=ReportParamLabel.script(facade, relatorioRankingOportunidadeUsuario.USER_PARAM_FASE_ID)%></td>
          <td><%=faseInfo != null ? faseInfo.getNome() : ""%></td>
        </tr>
        <tr>
          <td><%=ReportParamLabel.script(facade, relatorioRankingOportunidadeUsuario.USER_PARAM_STATUS)%></td>
          <td><%=ReportParamText.script(facade, relatorioRankingOportunidadeUsuario.USER_PARAM_STATUS)%></td>
        </tr>
        <tr>
          <td><%=ReportParamLabel.script(facade, relatorioRankingOportunidadeUsuario.USER_PARAM_CONSOLIDADO)%></td>
          <td><%=ReportParamText.script(facade, relatorioRankingOportunidadeUsuario.USER_PARAM_CONSOLIDADO)%></td>
        </tr>
      </table>
    </p>

    <!-- Gráfico -->
    <p align="center">
      <div id="divChart" align="center" style="width:100%; height:300px;">Carregando dados...</div>
    </p>

    <!-- Título de dados -->
    <%=ReportTitle.script("Dados", ReportTitle.LEVEL_1)%>

    <!-- Grid de dados -->
    <%ResultSet resultsetRankingOportunidadeUsuario = relatorioRankingOportunidadeUsuario.getResultSetRelatorioRankingOportunidadeUsuario(selectedEmpresaId, relatorioRankingOportunidadeUsuario.USER_PARAM_CAMPANHA_ID.getIntValue(), relatorioRankingOportunidadeUsuario.USER_PARAM_DEPARTAMENTO_ID.getIntValue(), relatorioRankingOportunidadeUsuario.USER_PARAM_FASE_ID.getIntValue(), relatorioRankingOportunidadeUsuario.USER_PARAM_STATUS.getIntValue(), relatorioRankingOportunidadeUsuario.USER_PARAM_DATA_INICIAL.getDateValue(), relatorioRankingOportunidadeUsuario.USER_PARAM_DATA_FINAL.getDateValue(), relatorioRankingOportunidadeUsuario.USER_PARAM_CONSOLIDADO.getIntValue());%>
    <%ResultSet resultsetUsuarios = relatorioRankingOportunidadeUsuario.getResultSetUsuarios(selectedEmpresaId, relatorioRankingOportunidadeUsuario.USER_PARAM_CAMPANHA_ID.getIntValue(), relatorioRankingOportunidadeUsuario.USER_PARAM_DEPARTAMENTO_ID.getIntValue(), relatorioRankingOportunidadeUsuario.USER_PARAM_FASE_ID.getIntValue(), relatorioRankingOportunidadeUsuario.USER_PARAM_STATUS.getIntValue(), relatorioRankingOportunidadeUsuario.USER_PARAM_DATA_INICIAL.getDateValue(), relatorioRankingOportunidadeUsuario.USER_PARAM_DATA_FINAL.getDateValue(), relatorioRankingOportunidadeUsuario.USER_PARAM_CONSOLIDADO.getIntValue());%>

    <%// totalizadores
      int totalRegistros = 0;
      int totalAtendimentos = 0;%>
    <%=reportGridRelatorioRankingOportunidadeUsuario.begin()%>
    <%try {
        // loop nos registros
        while(resultsetRankingOportunidadeUsuario.next()) {
          // adiciona nos totalizadores
          totalRegistros++;
          totalAtendimentos += resultsetRankingOportunidadeUsuario.getInt("total_oportunidade");
          // adiciona ao gráfico
          chartRelatorioRankingOportunidadeUsuario.addValue(resultsetRankingOportunidadeUsuario.getString("nome"), "", resultsetRankingOportunidadeUsuario.getInt("total_oportunidade"));%>
       <%=reportGridRelatorioRankingOportunidadeUsuario.beginRow()%>
         <%=reportGridRelatorioRankingOportunidadeUsuario.beginCell()%> <%=resultsetRankingOportunidadeUsuario.getString("nome")%> <%= reportGridRelatorioRankingOportunidadeUsuario.endCell()%>
         <%=reportGridRelatorioRankingOportunidadeUsuario.beginCell()%> <%=resultsetRankingOportunidadeUsuario.getInt("total_oportunidade")%> <%= reportGridRelatorioRankingOportunidadeUsuario.endCell()%>
       <%=reportGridRelatorioRankingOportunidadeUsuario.endRow()%>
      <%} // while%>
     <%=reportGridRelatorioRankingOportunidadeUsuario.beginRow(true)%>
       <%=reportGridRelatorioRankingOportunidadeUsuario.beginCell()%> <%=NumberTools.format(totalRegistros)%> registros <%=reportGridRelatorioRankingOportunidadeUsuario.endCell()%>
       <%=reportGridRelatorioRankingOportunidadeUsuario.beginCell()%> <%=NumberTools.format(totalAtendimentos)%> <%=reportGridRelatorioRankingOportunidadeUsuario.endCell()%>
     <%=reportGridRelatorioRankingOportunidadeUsuario.endRow()%>
    <%=reportGridRelatorioRankingOportunidadeUsuario.end()%>
      <%// controlador
        boolean primeiro = true;
        // zera totalizador
        totalRegistros = 0;
        // armazena o nome do campanha da vez
        String proximoNome = "";
        // loop nos registros
        while(resultsetUsuarios.next()) {
          // se o nome do campanha é diferente do anterior...
          if(!resultsetUsuarios.getString("nome_usuario").equals(proximoNome)) {
            // se não é o primeiro registro... fecha o grid
            if(!primeiro) {%>
              <%=reportGridUsuario.beginRow(true)%>
                <%=reportGridUsuario.beginCell(9)%> <%=totalRegistros%> registro(s) <%=reportGridUsuario.endCell()%>
              <%=reportGridUsuario.endRow()%>
            <%=reportGridUsuario.end()%>
          <%} // if
            // imprime novo cabeçalho na tela %>
          <%=ReportTitle.script(resultsetUsuarios.getString("nome_usuario"), ReportTitle.LEVEL_1)%>
          <%// proximoNome recebe o nome do próximo campanha
            proximoNome = resultsetUsuarios.getString("nome_usuario");%>
          <%// zera totalizador
            totalRegistros = 0;%>
          <%=reportGridUsuario.begin()%>
          <%if(primeiro)
             primeiro = false;%>
        <%} // if%>
        <%=reportGridUsuario.beginRow()%>
          <%=reportGridUsuario.beginCell()%> <a href="<%=Oportunidade.ACTION_CADASTRO.url(Oportunidade.COMMAND_EDIT, Oportunidade.FIELD_EMPRESA_ID.getFieldAlias() + "=" + resultsetUsuarios.getInt("empresa_id") + "&" + Oportunidade.FIELD_OPORTUNIDADE_ID.getFieldAlias() + "=" + resultsetUsuarios.getInt("in_oportunidade_id"))%>" title="Ir para Oportunidade..." target="frameContent"> <%=resultsetUsuarios.getInt("in_oportunidade_id")%> </a> <%=reportGridUsuario.endCell()%>
          <%=reportGridUsuario.beginCell()%> <%=resultsetUsuarios.getString("nome_departamento")%> <%=reportGridUsuario.endCell()%>
          <%=reportGridUsuario.beginCell()%> <%=resultsetUsuarios.getString("nome_campanha")%> <%=reportGridUsuario.endCell()%>
          <%=reportGridUsuario.beginCell()%> <%=resultsetUsuarios.getString("nome_cliente")%> <%=reportGridUsuario.endCell()%>
          <%=reportGridUsuario.beginCell()%> <%=resultsetUsuarios.getString("nome_fase")%> <%=reportGridUsuario.endCell()%>
          <%=reportGridUsuario.beginCell()%> <%=DateTools.formatDate(resultsetUsuarios.getTimestamp("da_acompanhamento"))%> <%=reportGridUsuario.endCell()%>
          <%=reportGridUsuario.beginCell()%> <%=StatusOportunidade.LOOKUP_LIST_FOR_FIELD[resultsetUsuarios.getInt("status")]%> <%=reportGridUsuario.endCell()%>
          <%=reportGridUsuario.beginCell()%> <%=resultsetUsuarios.getInt("sucesso")%> <%=reportGridUsuario.endCell()%>
          <%=reportGridUsuario.beginCell()%> <%=resultsetUsuarios.getInt("valor")%> <%=reportGridUsuario.endCell()%>
        <%=reportGridUsuario.endRow()%>
        <%// adiciona no totalizador
          totalRegistros++;
          // se é o ultimo registro...%>
        <%if(resultsetUsuarios.isLast()) {%>
            <%=reportGridUsuario.beginRow(true)%>
              <%=reportGridUsuario.beginCell(9)%> <%=totalRegistros%> registro(s) <%=reportGridUsuario.endCell()%>
            <%=reportGridUsuario.endRow()%>
          <%=reportGridUsuario.end()%>
        <%} // if%>
      <%} // while%>
    <%}
      finally {
        // libera recursos
        resultsetRankingOportunidadeUsuario.getStatement().close();
        resultsetRankingOportunidadeUsuario.close();
        resultsetUsuarios.getStatement().close();
        resultsetUsuarios.close();
      } // try-finally
    %>

    <!-- Rodapé -->
    <%=ReportFooter.script(facade, true, true)%>
  
    <%=chartRelatorioRankingOportunidadeUsuario.script(Chart.TYPE_COLUMN, "divChart", "Gráfico Ranking Oportunidade Usuário", "", "", Chart.INTERFACE_STYLE_REPORT_INTERFACE)%>

  </body>
</html>

<%}
  // término do bloco protegido
  catch (Exception e) {
    Controller.forwardException(e, pageContext);
  } // try-catch
%>
