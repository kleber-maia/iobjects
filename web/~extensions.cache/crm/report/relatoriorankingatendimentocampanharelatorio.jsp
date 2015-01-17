
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
    // nossa instância de Ranking Atendimento Campanha
    RelatorioRankingAtendimentoCampanha relatorioRankingAtendimentoCampanha = (RelatorioRankingAtendimentoCampanha)facade.getReport(RelatorioRankingAtendimentoCampanha.CLASS_NAME);
    // nossa instância de Usuário
    Usuario usuario = (Usuario)facade.getEntity(Usuario.CLASS_NAME);
    // nossa instância de Assunto
    Assunto assunto = (Assunto)facade.getEntity(Assunto.CLASS_NAME);
    // nossa instância de Atendimento
    Atendimento atendimento = (Atendimento)facade.getEntity(Atendimento.CLASS_NAME);

    // define os valores dos parâmetros de campanha
    relatorioRankingAtendimentoCampanha.userParamList().setParamsValues(request);

    // nosso gráfico
    Chart chart = new Chart(facade, "chartRelatorioRankingAtendimentoCampanha");

    // procura pelo Usuário
    UsuarioInfo usuarioInfo = null;
    if(relatorioRankingAtendimentoCampanha.USER_PARAM_USUARIO_ID.getIntValue() > 0)
      usuarioInfo = usuario.selectByUsuarioId(relatorioRankingAtendimentoCampanha.USER_PARAM_USUARIO_ID.getIntValue());
    // procura pelo Assunto
    AssuntoInfo assuntoInfo = null;
    if(relatorioRankingAtendimentoCampanha.USER_PARAM_ASSUNTO_ID.getIntValue() > 0)
      assuntoInfo = assunto.selectByPrimaryKey(relatorioRankingAtendimentoCampanha.USER_PARAM_ASSUNTO_ID.getIntValue());

    // estamos na central de atendimento?
    boolean isCentralAtendimento = request.getParameter(atendimento.CONTROL_PARAM_CENTRAL_ATENDIMENTO.getName()) != null;
    // grava na sessão
    session.setAttribute(atendimento.CONTROL_PARAM_CENTRAL_ATENDIMENTO.getName(), isCentralAtendimento);


    // nosso Grid
    ReportGridEx reportGridRelatorioRankingAtendimentoCampanha = new ReportGridEx("gridRelatorioRankingAtendimentoCampanha", true);
    reportGridRelatorioRankingAtendimentoCampanha.addColumn("Campanha", 75, ReportGrid.ALIGN_LEFT);
    reportGridRelatorioRankingAtendimentoCampanha.addColumn("Atendimento", 25, ReportGrid.ALIGN_RIGHT);

    // nosso Grid
    ReportGridEx reportGridAssunto = new ReportGridEx("gridAssunto", true);
    reportGridAssunto.addColumn("!", 3, ReportGrid.ALIGN_CENTER);
    reportGridAssunto.addColumn("ID", 3, ReportGrid.ALIGN_LEFT);
    reportGridAssunto.addColumn("Departamento", 13, ReportGrid.ALIGN_LEFT);
    reportGridAssunto.addColumn("Usuário", 10, ReportGrid.ALIGN_LEFT);
    reportGridAssunto.addColumn("Meio", 13, ReportGrid.ALIGN_LEFT);
    reportGridAssunto.addColumn("Assunto", 20, ReportGrid.ALIGN_LEFT);
    reportGridAssunto.addColumn("Cliente", 18, ReportGrid.ALIGN_LEFT);
    reportGridAssunto.addColumn("Data/Hora Início", 10, ReportGrid.ALIGN_LEFT);
    reportGridAssunto.addColumn("Data/Hora Término", 10, ReportGrid.ALIGN_LEFT);
%>

<html>
  <head>
    <title><%=RelatorioRankingAtendimentoCampanha.ACTION.getCaption()%></title>
    <link href="<%=facade.getStyle().reportInterface()%>" rel="stylesheet" type="text/css">
    <script src="include/scripts.js" type="text/javascript"></script>
  </head>
  <body style="margin:5px;" oncontextmenu="return false;">

    <!-- Cabeçalho -->
    <%=ReportHeader.script(facade, RelatorioRankingAtendimentoCampanha.ACTION, true, true, true, true, true)%>

    <!-- Parâmetros -->
    <p>
      <table>
        <tr>
          <td><%=ReportParamLabel.script(facade, relatorioRankingAtendimentoCampanha.USER_PARAM_DATA_INICIAL)%></td>
          <td><%=ReportParamText.script(facade, relatorioRankingAtendimentoCampanha.USER_PARAM_DATA_INICIAL)%></td>
        </tr>
        <tr>
          <td><%=ReportParamLabel.script(facade, relatorioRankingAtendimentoCampanha.USER_PARAM_DATA_FINAL)%></td>
          <td><%=ReportParamText.script(facade, relatorioRankingAtendimentoCampanha.USER_PARAM_DATA_FINAL)%></td>
        </tr>
        <tr>
          <td><%=ReportParamLabel.script(facade, relatorioRankingAtendimentoCampanha.USER_PARAM_USUARIO_ID)%></td>
          <td><%=usuarioInfo != null ? usuarioInfo.getNome() : ""%></td>
        </tr>
        <tr>
          <td><%=ReportParamLabel.script(facade, relatorioRankingAtendimentoCampanha.USER_PARAM_ASSUNTO_ID)%></td>
          <td><%=assuntoInfo != null ? assuntoInfo.getNome() : ""%></td>
        </tr>
        <tr>
          <td><%=ReportParamLabel.script(facade, relatorioRankingAtendimentoCampanha.USER_PARAM_CONSOLIDADO)%></td>
          <td><%=ReportParamText.script(facade, relatorioRankingAtendimentoCampanha.USER_PARAM_CONSOLIDADO)%></td>
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
    <%ResultSet resultsetRelatorioRankingAtendimentoCampanha = relatorioRankingAtendimentoCampanha.getResultSetRankingAtendimentoCampanha(selectedEmpresaId, relatorioRankingAtendimentoCampanha.USER_PARAM_USUARIO_ID.getIntValue(), relatorioRankingAtendimentoCampanha.USER_PARAM_ASSUNTO_ID.getIntValue(), relatorioRankingAtendimentoCampanha.USER_PARAM_DATA_INICIAL.getDateValue(), relatorioRankingAtendimentoCampanha.USER_PARAM_DATA_FINAL.getDateValue(), relatorioRankingAtendimentoCampanha.USER_PARAM_CONSOLIDADO.getIntValue());%>
    <%ResultSet resultSetCampanhas = relatorioRankingAtendimentoCampanha.getResultSetCampanhas(selectedEmpresaId, relatorioRankingAtendimentoCampanha.USER_PARAM_USUARIO_ID.getIntValue(), relatorioRankingAtendimentoCampanha.USER_PARAM_ASSUNTO_ID.getIntValue(), relatorioRankingAtendimentoCampanha.USER_PARAM_DATA_INICIAL.getDateValue(), relatorioRankingAtendimentoCampanha.USER_PARAM_DATA_FINAL.getDateValue(), relatorioRankingAtendimentoCampanha.USER_PARAM_CONSOLIDADO.getIntValue());%>
    <%// totalizadores
      int totalRegistros = 0;
      int totalAtendimentos = 0;
    %>
    <%=reportGridRelatorioRankingAtendimentoCampanha.begin()%>
    <%try {
        // loop nos registros
        while(resultsetRelatorioRankingAtendimentoCampanha.next()) {
          // adiciona nos totalizadores
          totalRegistros++;
          totalAtendimentos += resultsetRelatorioRankingAtendimentoCampanha.getInt("total_campanha");
          // adiciona ao gráfico
          chart.addValue(resultsetRelatorioRankingAtendimentoCampanha.getString("nome"), "", resultsetRelatorioRankingAtendimentoCampanha.getInt("total_campanha"));%>
       <%=reportGridRelatorioRankingAtendimentoCampanha.beginRow()%>
         <%=reportGridRelatorioRankingAtendimentoCampanha.beginCell()%> <%=resultsetRelatorioRankingAtendimentoCampanha.getString("nome")%> <%= reportGridRelatorioRankingAtendimentoCampanha.endCell()%>
         <%=reportGridRelatorioRankingAtendimentoCampanha.beginCell()%> <%=resultsetRelatorioRankingAtendimentoCampanha.getInt("total_campanha")%> <%= reportGridRelatorioRankingAtendimentoCampanha.endCell()%>
       <%=reportGridRelatorioRankingAtendimentoCampanha.endRow()%>
      <%} // while%>
     <%=reportGridRelatorioRankingAtendimentoCampanha.beginRow(true)%>
       <%=reportGridRelatorioRankingAtendimentoCampanha.beginCell()%> <%=NumberTools.format(totalRegistros)%> registros <%=reportGridRelatorioRankingAtendimentoCampanha.endCell()%>
       <%=reportGridRelatorioRankingAtendimentoCampanha.beginCell()%> <%=NumberTools.format(totalAtendimentos)%> <%=reportGridRelatorioRankingAtendimentoCampanha.endCell()%>
     <%=reportGridRelatorioRankingAtendimentoCampanha.endRow()%>
    <%=reportGridRelatorioRankingAtendimentoCampanha.end()%>
      <%// controlador
        boolean primeiro = true;
        // zera totalizador
        totalRegistros = 0;
        // armazena o nome do campanha da vez
        String proximoNome = "";
        // contador
        int count = 0;
        // loop nos registros
        while(resultSetCampanhas.next()) {
          // se o nome do campanha é diferente do anterior...
          if(!resultSetCampanhas.getString("nome_campanha").equals(proximoNome)) {
            // se não é o primeiro registro... fecha o grid
            if(!primeiro) {%>
              <%=gridFooter(reportGridAssunto, totalRegistros)%>
          <%} // if
          // zera nosso contador
          count = 0;
            // imprime novo cabeçalho na tela %>
          <%=ReportTitle.script(resultSetCampanhas.getString("nome_campanha"), ReportTitle.LEVEL_1)%>
          <%// proximoNome recebe o nome do próximo campanha
            proximoNome = resultSetCampanhas.getString("nome_campanha");%>
          <%// zera totalizador
            totalRegistros = 0;%>
          <%=reportGridAssunto.begin()%>
          <%if(primeiro)
             primeiro = false;%>
        <%} // if%>
        <%=reportGridAssunto.beginRow()%>
          <%=reportGridAssunto.beginCell()%> <a href="javascript:showHideAnotacao(<%=resultSetCampanhas.getString("in_empresa_id")%>, <%=resultSetCampanhas.getString("in_atendimento_id")%>)">[+]</a> <%=reportGridAssunto.endCell()%>
          <%=reportGridAssunto.beginCell()%> <a href="<%=Atendimento.ACTION_CADASTRO.url(Atendimento.COMMAND_EDIT, Atendimento.FIELD_EMPRESA_ID.getFieldAlias() + "=" + resultSetCampanhas.getInt("in_empresa_id") + "&" + Atendimento.FIELD_ATENDIMENTO_ID.getFieldAlias() + "=" + resultSetCampanhas.getInt("in_atendimento_id"))%>" title="Ir para Atendimento..." target="frameContent"> <%=resultSetCampanhas.getInt("in_atendimento_id")%> </a> <%=reportGridAssunto.endCell()%>
          <%=reportGridAssunto.beginCell()%> <%=resultSetCampanhas.getString("nome_departamento")%> <%=reportGridAssunto.endCell()%>
          <%=reportGridAssunto.beginCell()%> <%=resultSetCampanhas.getString("nome_usuario")%> <%=reportGridAssunto.endCell()%>
          <%=reportGridAssunto.beginCell()%> <%=resultSetCampanhas.getString("nome_meio")%> <%=reportGridAssunto.endCell()%>
          <%=reportGridAssunto.beginCell()%> <%=resultSetCampanhas.getString("nome_assunto")%> <%=reportGridAssunto.endCell()%>
          <%=reportGridAssunto.beginCell()%> <%=resultSetCampanhas.getString("nome_cliente")%> <%=reportGridAssunto.endCell()%>
          <%=reportGridAssunto.beginCell()%> <%=DateTools.formatDateTime(resultSetCampanhas.getTimestamp("dt_inicio"))%> <%=reportGridAssunto.endCell()%>
          <%=reportGridAssunto.beginCell()%> <%=DateTools.formatDateTime(resultSetCampanhas.getTimestamp("dt_termino"))%> <%=reportGridAssunto.endCell()%>
        <%=reportGridAssunto.endRow()%>
        
        <%// incrementa o contador
          count++;
          // estilo da linha
          String rowClassName = count % 2 == 0 ? "gridRowEven" : "griRowOdd";%>
        <tr id="rowAnotacao<%=resultSetCampanhas.getString("in_empresa_id") + resultSetCampanhas.getString("in_atendimento_id")%>" style="display:none;">
          <td class="<%=rowClassName%>"> </td>
          <td class="<%=rowClassName%>" colspan="8"> <%=resultSetCampanhas.getString("va_descricao")%> </td>
        </tr>
                
        <%// adiciona no totalizador
          totalRegistros++;
          // se é o ultimo registro...%>
        <%if(resultSetCampanhas.isLast()) {%>
          <%=gridFooter(reportGridAssunto, totalRegistros)%>
        <%} // if%>
      <%} // while%>
    <%}
      finally {
        // libera recursos
        resultsetRelatorioRankingAtendimentoCampanha.getStatement().close();
        resultsetRelatorioRankingAtendimentoCampanha.close();
        resultSetCampanhas.getStatement().close();
        resultSetCampanhas.close();
      } // try-finally
    %>

    <!-- Rodapé -->
    <%=ReportFooter.script(facade, true, true)%>

    <!-- gera o gráfico -->
    <%=chart.script(Chart.TYPE_COLUMN, "divChart", "Ranking Atendimento Campanha", "", "", Chart.INTERFACE_STYLE_REPORT_INTERFACE)%>

  </body>
</html>

<%}
  // término do bloco protegido
  catch (Exception e) {
    Controller.forwardException(e, pageContext);
  } // try-catch
%>
