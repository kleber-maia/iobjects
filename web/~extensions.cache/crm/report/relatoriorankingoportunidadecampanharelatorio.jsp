       
<%@include file="../include/beans.jsp"%>

<%@page import="imanager.entity.*"%>
<%@page import="imanager.misc.*"%>
<%@page import="imanager.report.*"%>

<%@page import="securityservice.entity.*"%>

<%
  // início do bloco protegido
  try {
    // nossa instância de RelatorioRankingOportunidadeCampanha
    RelatorioRankingOportunidadeCampanha relatorioRankingOportunidadeCampanha = (RelatorioRankingOportunidadeCampanha)facade.getReport(RelatorioRankingOportunidadeCampanha.CLASS_NAME);
    // nossa instância de Usuário
    Usuario usuario = (Usuario)facade.getEntity(Usuario.CLASS_NAME);
    // nossa instância de Departamento
    Departamento departamento = (Departamento)facade.getEntity(Departamento.CLASS_NAME);
    // nossa instância de Fase
    Fase fase = (Fase)facade.getEntity(Fase.CLASS_NAME);
    // nossa instância de Oportunidade
    Oportunidade oportunidade = (Oportunidade)facade.getEntity(Oportunidade.CLASS_NAME);

    // define os valores dos parâmetros de usuário
    relatorioRankingOportunidadeCampanha.userParamList().setParamsValues(request);

    // nosso gráfico
    Chart chartRelatorioRankingOportunidadeCampanha = new Chart(facade, "chartRelatorioRankingOportunidadeCampanha");

    // procura pelo Usuário
    UsuarioInfo usuarioInfo = null;
    if(relatorioRankingOportunidadeCampanha.USER_PARAM_USUARIO_ID.getIntValue() > 0)
      usuarioInfo = usuario.selectByUsuarioId(relatorioRankingOportunidadeCampanha.USER_PARAM_USUARIO_ID.getIntValue());
    // procura pelo Departamento
    DepartamentoInfo departamentoInfo = null;
    if(relatorioRankingOportunidadeCampanha.USER_PARAM_DEPARTAMENTO_ID.getIntValue() > 0)
      departamentoInfo = departamento.selectByPrimaryKey(selectedEmpresaId, relatorioRankingOportunidadeCampanha.USER_PARAM_DEPARTAMENTO_ID.getIntValue());
    // procura pelo Fase
    FaseInfo faseInfo = null;
    if(relatorioRankingOportunidadeCampanha.USER_PARAM_FASE_ID.getIntValue() > 0)
      faseInfo = fase.selectByPrimaryKey(relatorioRankingOportunidadeCampanha.USER_PARAM_FASE_ID.getIntValue());

    // estamos na central de atendimento?
    boolean isCentralAtendimento = request.getParameter(oportunidade.CONTROL_PARAM_CENTRAL_ATENDIMENTO.getName()) != null;
    // grava na sessão
    session.setAttribute(oportunidade.CONTROL_PARAM_CENTRAL_ATENDIMENTO.getName(), isCentralAtendimento);

    // nosso Grid
    ReportGridEx reportGridRelatorioRankingOportunidadeCampanha = new ReportGridEx("gridRelatorioRankingOportunidadeCampanha", true);
    reportGridRelatorioRankingOportunidadeCampanha.addColumn("Campanha", 75, ReportGrid.ALIGN_LEFT);
    reportGridRelatorioRankingOportunidadeCampanha.addColumn("Oportunidade", 25, ReportGrid.ALIGN_RIGHT);
    // nosso Grid
    ReportGridEx reportGridCampanha = new ReportGridEx("gridCampanha", true);
    reportGridCampanha.addColumn("ID", 5, ReportGrid.ALIGN_LEFT);
    reportGridCampanha.addColumn("Departamento", 20, ReportGrid.ALIGN_LEFT);
    reportGridCampanha.addColumn("Usuário", 13, ReportGrid.ALIGN_LEFT);
    reportGridCampanha.addColumn("Cliente", 15, ReportGrid.ALIGN_LEFT);
    reportGridCampanha.addColumn("Fase", 15, ReportGrid.ALIGN_LEFT);
    reportGridCampanha.addColumn("Acompanhamento", 10, ReportGrid.ALIGN_LEFT);
    reportGridCampanha.addColumn("Status", 10, ReportGrid.ALIGN_LEFT);
    reportGridCampanha.addColumn("% Sucesso", 7, ReportGrid.ALIGN_RIGHT);
    reportGridCampanha.addColumn("Valor", 5, ReportGrid.ALIGN_RIGHT);
%>

<html>
  <head>
    <title><%=RelatorioRankingOportunidadeCampanha.ACTION.getCaption()%></title>
    <link href="<%=facade.getStyle().reportInterface()%>" rel="stylesheet" type="text/css">
    <script src="include/scripts.js" type="text/javascript"></script>
  </head>
  <body style="margin:5px;" oncontextmenu="return false;">

    <!-- Cabeçalho -->
    <%=ReportHeader.script(facade, RelatorioRankingOportunidadeCampanha.ACTION, true, true, true, true, true)%>

    <!-- Parâmetros -->
    <p>
      <table>
        <tr>
          <td><%=ReportParamLabel.script(facade, relatorioRankingOportunidadeCampanha.USER_PARAM_DATA_INICIAL)%></td>
          <td><%=ReportParamText.script(facade, relatorioRankingOportunidadeCampanha.USER_PARAM_DATA_INICIAL)%></td>
        </tr>
        <tr>
          <td><%=ReportParamLabel.script(facade, relatorioRankingOportunidadeCampanha.USER_PARAM_DATA_FINAL)%></td>
          <td><%=ReportParamText.script(facade, relatorioRankingOportunidadeCampanha.USER_PARAM_DATA_FINAL)%></td>
        </tr>
        <tr>
          <td><%=ReportParamLabel.script(facade, relatorioRankingOportunidadeCampanha.USER_PARAM_USUARIO_ID)%></td>
          <td><%=usuarioInfo != null ? usuarioInfo.getNome() : ""%></td>
        </tr>
        <tr>
          <td><%=ReportParamLabel.script(facade, relatorioRankingOportunidadeCampanha.USER_PARAM_DEPARTAMENTO_ID)%></td>
          <td><%=departamentoInfo != null ? departamentoInfo.getNome() : ""%></td>
        </tr>
        <tr>
          <td><%=ReportParamLabel.script(facade, relatorioRankingOportunidadeCampanha.USER_PARAM_FASE_ID)%></td>
          <td><%=faseInfo != null ? faseInfo.getNome() : ""%></td>
        </tr>
        <tr>
          <td><%=ReportParamLabel.script(facade, relatorioRankingOportunidadeCampanha.USER_PARAM_STATUS)%></td>
          <td><%=ReportParamText.script(facade, relatorioRankingOportunidadeCampanha.USER_PARAM_STATUS)%></td>
        </tr>
        <tr>
          <td><%=ReportParamLabel.script(facade, relatorioRankingOportunidadeCampanha.USER_PARAM_CONSOLIDADO)%></td>
          <td><%=ReportParamText.script(facade, relatorioRankingOportunidadeCampanha.USER_PARAM_CONSOLIDADO)%></td>
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
    <%ResultSet resultsetRankingOportunidadeCampanha = relatorioRankingOportunidadeCampanha.getResultSetRelatorioRankingOportunidadeCampanha(selectedEmpresaId, relatorioRankingOportunidadeCampanha.USER_PARAM_USUARIO_ID.getIntValue(), relatorioRankingOportunidadeCampanha.USER_PARAM_DEPARTAMENTO_ID.getIntValue(), relatorioRankingOportunidadeCampanha.USER_PARAM_FASE_ID.getIntValue(), relatorioRankingOportunidadeCampanha.USER_PARAM_STATUS.getIntValue(), relatorioRankingOportunidadeCampanha.USER_PARAM_DATA_INICIAL.getDateValue(), relatorioRankingOportunidadeCampanha.USER_PARAM_DATA_FINAL.getDateValue(), relatorioRankingOportunidadeCampanha.USER_PARAM_CONSOLIDADO.getIntValue());%>
    <%ResultSet resultsetCampanhas = relatorioRankingOportunidadeCampanha.getResultSetCampanhas(selectedEmpresaId, relatorioRankingOportunidadeCampanha.USER_PARAM_USUARIO_ID.getIntValue(), relatorioRankingOportunidadeCampanha.USER_PARAM_DEPARTAMENTO_ID.getIntValue(), relatorioRankingOportunidadeCampanha.USER_PARAM_FASE_ID.getIntValue(), relatorioRankingOportunidadeCampanha.USER_PARAM_STATUS.getIntValue(), relatorioRankingOportunidadeCampanha.USER_PARAM_DATA_INICIAL.getDateValue(), relatorioRankingOportunidadeCampanha.USER_PARAM_DATA_FINAL.getDateValue(), relatorioRankingOportunidadeCampanha.USER_PARAM_CONSOLIDADO.getIntValue());%>

    <%// totalizadores
      int totalRegistros = 0;
      int totalAtendimentos = 0;%>
    <%=reportGridRelatorioRankingOportunidadeCampanha.begin()%>
    <%try {
        // loop nos registros
        while(resultsetRankingOportunidadeCampanha.next()) {
          // adiciona nos totalizadores
          totalRegistros++;
          totalAtendimentos += resultsetRankingOportunidadeCampanha.getInt("total_oportunidade");
          // adiciona ao gráfico
          chartRelatorioRankingOportunidadeCampanha.addValue(resultsetRankingOportunidadeCampanha.getString("nome"), "", resultsetRankingOportunidadeCampanha.getInt("total_oportunidade"));%>
       <%=reportGridRelatorioRankingOportunidadeCampanha.beginRow()%>
         <%=reportGridRelatorioRankingOportunidadeCampanha.beginCell()%> <%=resultsetRankingOportunidadeCampanha.getString("nome")%> <%= reportGridRelatorioRankingOportunidadeCampanha.endCell()%>
         <%=reportGridRelatorioRankingOportunidadeCampanha.beginCell()%> <%=resultsetRankingOportunidadeCampanha.getInt("total_oportunidade")%> <%= reportGridRelatorioRankingOportunidadeCampanha.endCell()%>
       <%=reportGridRelatorioRankingOportunidadeCampanha.endRow()%>
      <%} // while%>
     <%=reportGridRelatorioRankingOportunidadeCampanha.beginRow(true)%>
       <%=reportGridRelatorioRankingOportunidadeCampanha.beginCell()%> <%=NumberTools.format(totalRegistros)%> registros <%=reportGridRelatorioRankingOportunidadeCampanha.endCell()%>
       <%=reportGridRelatorioRankingOportunidadeCampanha.beginCell()%> <%=NumberTools.format(totalAtendimentos)%> <%=reportGridRelatorioRankingOportunidadeCampanha.endCell()%>
     <%=reportGridRelatorioRankingOportunidadeCampanha.endRow()%>
    <%=reportGridRelatorioRankingOportunidadeCampanha.end()%>
      <%// controlador
        boolean primeiro = true;
        // zera totalizador
        totalRegistros = 0;
        // armazena o nome do campanha da vez
        String proximoNome = "";
        // loop nos registros
        while(resultsetCampanhas.next()) {
          // se o nome do campanha é diferente do anterior...
          if(!resultsetCampanhas.getString("nome_campanha").equals(proximoNome)) {
            // se não é o primeiro registro... fecha o grid
            if(!primeiro) {%>
              <%=reportGridCampanha.beginRow(true)%>
                <%=reportGridCampanha.beginCell(9)%> <%=totalRegistros%> registro(s) <%=reportGridCampanha.endCell()%>
              <%=reportGridCampanha.endRow()%>
            <%=reportGridCampanha.end()%>
          <%} // if
            // imprime novo cabeçalho na tela %>
          <%=ReportTitle.script(resultsetCampanhas.getString("nome_campanha"), ReportTitle.LEVEL_1)%>
          <%// proximoNome recebe o nome do próximo campanha
            proximoNome = resultsetCampanhas.getString("nome_campanha");%>
          <%// zera totalizador
            totalRegistros = 0;%>
          <%=reportGridCampanha.begin()%>
          <%if(primeiro)
             primeiro = false;%>
        <%} // if%>
        <%=reportGridCampanha.beginRow()%>
          <%=reportGridCampanha.beginCell()%> <a href="<%=Oportunidade.ACTION_CADASTRO.url(Oportunidade.COMMAND_EDIT, Oportunidade.FIELD_EMPRESA_ID.getFieldAlias() + "=" + resultsetCampanhas.getInt("empresa_id") + "&" + Oportunidade.FIELD_OPORTUNIDADE_ID.getFieldAlias() + "=" + resultsetCampanhas.getInt("in_oportunidade_id"))%>" title="Ir para Oportunidade..." target="frameContent"> <%=resultsetCampanhas.getInt("in_oportunidade_id")%> </a> <%=reportGridCampanha.endCell()%>
          <%=reportGridCampanha.beginCell()%> <%=resultsetCampanhas.getString("nome_departamento")%> <%=reportGridCampanha.endCell()%>
          <%=reportGridCampanha.beginCell()%> <%=resultsetCampanhas.getString("nome_usuario")%> <%=reportGridCampanha.endCell()%>
          <%=reportGridCampanha.beginCell()%> <%=resultsetCampanhas.getString("nome_cliente")%> <%=reportGridCampanha.endCell()%>
          <%=reportGridCampanha.beginCell()%> <%=resultsetCampanhas.getString("nome_fase")%> <%=reportGridCampanha.endCell()%>
          <%=reportGridCampanha.beginCell()%> <%=DateTools.formatDate(resultsetCampanhas.getTimestamp("da_acompanhamento"))%> <%=reportGridCampanha.endCell()%>
          <%=reportGridCampanha.beginCell()%> <%=StatusOportunidade.LOOKUP_LIST_FOR_FIELD[resultsetCampanhas.getInt("status")]%> <%=reportGridCampanha.endCell()%>
          <%=reportGridCampanha.beginCell()%> <%=resultsetCampanhas.getInt("sucesso")%> <%=reportGridCampanha.endCell()%>
          <%=reportGridCampanha.beginCell()%> <%=resultsetCampanhas.getInt("valor")%> <%=reportGridCampanha.endCell()%>
        <%=reportGridCampanha.endRow()%>
        <%// adiciona no totalizador
          totalRegistros++;
          // se é o ultimo registro...%>
        <%if(resultsetCampanhas.isLast()) {%>
            <%=reportGridCampanha.beginRow(true)%>
              <%=reportGridCampanha.beginCell(9)%> <%=totalRegistros%> registro(s) <%=reportGridCampanha.endCell()%>
            <%=reportGridCampanha.endRow()%>
          <%=reportGridCampanha.end()%>
        <%} // if%>
      <%} // while%>
    <%}
      finally {
        // libera recursos
        resultsetRankingOportunidadeCampanha.getStatement().close();
        resultsetRankingOportunidadeCampanha.close();
        resultsetCampanhas.getStatement().close();
        resultsetCampanhas.close();
      } // try-finally
    %>

    <!-- Rodapé -->
    <%=ReportFooter.script(facade, true, true)%>

    
    <%=chartRelatorioRankingOportunidadeCampanha.script(Chart.TYPE_COLUMN, "divChart", "Gráfico Ranking Oportunidade Campanha", "", "", Chart.INTERFACE_STYLE_REPORT_INTERFACE)%>

  </body>
</html>

<%}
  // término do bloco protegido
  catch (Exception e) {
    Controller.forwardException(e, pageContext);
  } // try-catch
%>
