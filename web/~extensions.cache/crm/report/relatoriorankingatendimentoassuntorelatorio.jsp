
<%@include file="../include/beans.jsp"%>

<%@page import="imanager.entity.*"%>
<%@page import="imanager.report.*"%>
<%@page import="imanager.ui.entity.*"%>

<%@page import="securityservice.entity.*"%>

<%!
  public String gridFooter(ReportGridEx reportGrid, int totalRegistros) throws Exception {
    return reportGrid.beginRow(true)
         + reportGrid.beginCell(9) + "<div style=\"text-align:left;\">" + totalRegistros + " registro(s)</div>" + reportGrid.endCell()
         + reportGrid.endRow()
         + reportGrid.end();
  }
%>

<%
  // início do bloco protegido
  try {
    // nossa instância de Ranking Atendimento Assunto
    RelatorioRankingAtendimentoAssunto relatorioRankingAtendimentoAssunto = (RelatorioRankingAtendimentoAssunto)facade.getReport(RelatorioRankingAtendimentoAssunto.CLASS_NAME);
    // nossa instância de Usuário
    Usuario usuario = (Usuario)facade.getEntity(Usuario.CLASS_NAME);
    // nossa instância de Campanha
    Campanha campanha = (Campanha)facade.getEntity(Campanha.CLASS_NAME);
    // nossa instância de Atendimento
    Atendimento atendimento = (Atendimento)facade.getEntity(Atendimento.CLASS_NAME);

    // define os valores dos parâmetros de assunto
    relatorioRankingAtendimentoAssunto.userParamList().setParamsValues(request);

    // nosso gráfico
    Chart chart = new Chart(facade, "chartRelatorioRankingAtendimentoAssunto");

    // procura pelo Usuário
    UsuarioInfo usuarioInfo = null;
    if(relatorioRankingAtendimentoAssunto.USER_PARAM_USUARIO_ID.getIntValue() > 0)
      usuarioInfo = usuario.selectByUsuarioId(relatorioRankingAtendimentoAssunto.USER_PARAM_USUARIO_ID.getIntValue());
    // procura pela Campanha
    CampanhaInfo campanhaInfo = null;
    if(relatorioRankingAtendimentoAssunto.USER_PARAM_CAMPANHA_ID.getIntValue() > 0)
      campanhaInfo = campanha.selectByPrimaryKey(relatorioRankingAtendimentoAssunto.USER_PARAM_CAMPANHA_ID.getIntValue());

    // estamos na central de atendimento?
    boolean isCentralAtendimento = request.getParameter(atendimento.CONTROL_PARAM_CENTRAL_ATENDIMENTO.getName()) != null;
    // grava na sessão
    session.setAttribute(atendimento.CONTROL_PARAM_CENTRAL_ATENDIMENTO.getName(), isCentralAtendimento);

    // nosso Grid
    ReportGridEx reportGridRelatorioRankingAtendimentoAssunto = new ReportGridEx("gridRelatorioRankingAtendimentoAssunto", true);
    reportGridRelatorioRankingAtendimentoAssunto.addColumn("Assunto", 75, ReportGrid.ALIGN_LEFT);
    reportGridRelatorioRankingAtendimentoAssunto.addColumn("Atendimento", 25, ReportGrid.ALIGN_RIGHT);

    // nosso Grid
    ReportGridEx reportGridAssunto = new ReportGridEx("gridAssunto", true);
    reportGridAssunto.addColumn("!", 3, ReportGrid.ALIGN_CENTER);
    reportGridAssunto.addColumn("ID", 3, ReportGrid.ALIGN_LEFT);
    reportGridAssunto.addColumn("Departamento", 13, ReportGrid.ALIGN_LEFT);
    reportGridAssunto.addColumn("Usuário", 10, ReportGrid.ALIGN_LEFT);
    reportGridAssunto.addColumn("Meio", 13, ReportGrid.ALIGN_LEFT);
    reportGridAssunto.addColumn("Campanha", 20, ReportGrid.ALIGN_LEFT);
    reportGridAssunto.addColumn("Cliente", 18, ReportGrid.ALIGN_LEFT);
    reportGridAssunto.addColumn("Data/Hora Início", 10, ReportGrid.ALIGN_LEFT);
    reportGridAssunto.addColumn("Data/Hora Término", 10, ReportGrid.ALIGN_LEFT);
%>

<html>
  <head>
    <title><%=RelatorioRankingAtendimentoAssunto.ACTION.getCaption()%></title>
    <link href="<%=facade.getStyle().reportInterface()%>" rel="stylesheet" type="text/css">
    <script src="include/scripts.js" type="text/javascript"></script>
  </head>
  <body style="margin:5px;" oncontextmenu="return false;">

    <!-- Cabeçalho -->
    <%=ReportHeader.script(facade, RelatorioRankingAtendimentoAssunto.ACTION, true, true, true, true, true)%>

    <!-- Parâmetros -->
    <p>
      <table>
        <tr>
          <td><%=ReportParamLabel.script(facade, relatorioRankingAtendimentoAssunto.USER_PARAM_DATA_INICIAL)%></td>
          <td><%=ReportParamText.script(facade, relatorioRankingAtendimentoAssunto.USER_PARAM_DATA_INICIAL)%></td>
        </tr>
        <tr>
          <td><%=ReportParamLabel.script(facade, relatorioRankingAtendimentoAssunto.USER_PARAM_DATA_FINAL)%></td>
          <td><%=ReportParamText.script(facade, relatorioRankingAtendimentoAssunto.USER_PARAM_DATA_FINAL)%></td>
        </tr>
        <tr>
          <td><%=ReportParamLabel.script(facade, relatorioRankingAtendimentoAssunto.USER_PARAM_USUARIO_ID)%></td>
          <td><%=usuarioInfo != null ? usuarioInfo.getNome() : ""%></td>
        </tr>
        <tr>
          <td><%=ReportParamLabel.script(facade, relatorioRankingAtendimentoAssunto.USER_PARAM_CAMPANHA_ID)%></td>
          <td><%=campanhaInfo != null ? campanhaInfo.getNome() : ""%></td>
        </tr>
        <tr>
          <td><%=ReportParamLabel.script(facade, relatorioRankingAtendimentoAssunto.USER_PARAM_CONSOLIDADO)%></td>
          <td><%=ReportParamText.script(facade, relatorioRankingAtendimentoAssunto.USER_PARAM_CONSOLIDADO)%></td>
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
    <%ResultSet resultsetRelatorioRankingAtendimentoAssunto = relatorioRankingAtendimentoAssunto.getResultSetRankingAtendimentoAssunto(selectedEmpresaId, relatorioRankingAtendimentoAssunto.USER_PARAM_CAMPANHA_ID.getIntValue(), relatorioRankingAtendimentoAssunto.USER_PARAM_USUARIO_ID.getIntValue(), relatorioRankingAtendimentoAssunto.USER_PARAM_DATA_INICIAL.getDateValue(), relatorioRankingAtendimentoAssunto.USER_PARAM_DATA_FINAL.getDateValue(), relatorioRankingAtendimentoAssunto.USER_PARAM_CONSOLIDADO.getIntValue());%>
    <%ResultSet resultSetAssuntos = relatorioRankingAtendimentoAssunto.getResultSetAssuntos(selectedEmpresaId, relatorioRankingAtendimentoAssunto.USER_PARAM_CAMPANHA_ID.getIntValue(), relatorioRankingAtendimentoAssunto.USER_PARAM_USUARIO_ID.getIntValue(), relatorioRankingAtendimentoAssunto.USER_PARAM_DATA_INICIAL.getDateValue(), relatorioRankingAtendimentoAssunto.USER_PARAM_DATA_FINAL.getDateValue(), relatorioRankingAtendimentoAssunto.USER_PARAM_CONSOLIDADO.getIntValue());%>
    <%// totalizadores
      int totalRegistros = 0;
      int totalAtendimentos = 0;
    %>
    <%=reportGridRelatorioRankingAtendimentoAssunto.begin()%>
    <%try {
        // loop nos registros
        while(resultsetRelatorioRankingAtendimentoAssunto.next()) {
          // adiciona nos totalizadores
          totalRegistros++;
          totalAtendimentos += resultsetRelatorioRankingAtendimentoAssunto.getInt("total_assunto");
          // adiciona ao gráfico
          chart.addValue(resultsetRelatorioRankingAtendimentoAssunto.getString("nome"), "", resultsetRelatorioRankingAtendimentoAssunto.getInt("total_assunto"));%>
       <%=reportGridRelatorioRankingAtendimentoAssunto.beginRow()%>
         <%=reportGridRelatorioRankingAtendimentoAssunto.beginCell()%> <%=resultsetRelatorioRankingAtendimentoAssunto.getString("nome")%> <%= reportGridRelatorioRankingAtendimentoAssunto.endCell()%>
         <%=reportGridRelatorioRankingAtendimentoAssunto.beginCell()%> <%=resultsetRelatorioRankingAtendimentoAssunto.getInt("total_assunto")%> <%= reportGridRelatorioRankingAtendimentoAssunto.endCell()%>
       <%=reportGridRelatorioRankingAtendimentoAssunto.endRow()%>
      <%} // while%>
     <%=reportGridRelatorioRankingAtendimentoAssunto.beginRow(true)%>
       <%=reportGridRelatorioRankingAtendimentoAssunto.beginCell()%> <%=NumberTools.format(totalRegistros)%> registros <%=reportGridRelatorioRankingAtendimentoAssunto.endCell()%>
       <%=reportGridRelatorioRankingAtendimentoAssunto.beginCell()%> <%=NumberTools.format(totalAtendimentos)%> <%=reportGridRelatorioRankingAtendimentoAssunto.endCell()%>
     <%=reportGridRelatorioRankingAtendimentoAssunto.endRow()%>
    <%=reportGridRelatorioRankingAtendimentoAssunto.end()%>
      <%// controlador
        boolean primeiro = true;
        // zera totalizador
        totalRegistros = 0;
        // armazena o nome do assunto da vez
        String proximoNome = "";
        // contador
        int count = 0;
        // loop nos registros
        while(resultSetAssuntos.next()) {
          // se o nome do assunto é diferente do anterior...
          if(!resultSetAssuntos.getString("nome_assunto").equals(proximoNome)) {
            // se não é o primeiro registro... fecha o grid
            if(!primeiro) {%>
              <%=gridFooter(reportGridAssunto, totalRegistros)%>
          <%} // if
          // zera nosso contador
          count = 0;
          // imprime novo cabeçalho na tela%>
          <%=ReportTitle.script(resultSetAssuntos.getString("nome_assunto"), ReportTitle.LEVEL_1)%>
          <%// proximoNome recebe o nome do próximo assunto
            proximoNome = resultSetAssuntos.getString("nome_assunto");%>
          <%// zera totalizador
            totalRegistros = 0;%>
          <%=reportGridAssunto.begin()%>
          <%if(primeiro)
             primeiro = false;%>
        <%} // if%>
        <%=reportGridAssunto.beginRow()%>
          <%=reportGridAssunto.beginCell()%> <a href="javascript:showHideAnotacao(<%=resultSetAssuntos.getString("in_empresa_id")%>, <%=resultSetAssuntos.getString("in_atendimento_id")%>)">[+]</a> <%=reportGridAssunto.endCell()%>
          <%=reportGridAssunto.beginCell()%> <a href="<%=Atendimento.ACTION_CADASTRO.url(Atendimento.COMMAND_EDIT, Atendimento.FIELD_EMPRESA_ID.getFieldAlias() + "=" + resultSetAssuntos.getInt("in_empresa_id") + "&" + Atendimento.FIELD_ATENDIMENTO_ID.getFieldAlias() + "=" + resultSetAssuntos.getInt("in_atendimento_id"))%>" title="Ir para Atendimento..." target="frameContent"> <%=resultSetAssuntos.getInt("in_atendimento_id")%> </a> <%=reportGridAssunto.endCell()%>
          <%=reportGridAssunto.beginCell()%> <%=resultSetAssuntos.getString("nome_departamento")%> <%=reportGridAssunto.endCell()%>
          <%=reportGridAssunto.beginCell()%> <%=resultSetAssuntos.getString("nome_usuario")%> <%=reportGridAssunto.endCell()%>
          <%=reportGridAssunto.beginCell()%> <%=resultSetAssuntos.getString("nome_meio")%> <%=reportGridAssunto.endCell()%>
          <%=reportGridAssunto.beginCell()%> <%=resultSetAssuntos.getString("nome_campanha")%> <%=reportGridAssunto.endCell()%>
          <%=reportGridAssunto.beginCell()%> <%=resultSetAssuntos.getString("nome_cliente")%> <%=reportGridAssunto.endCell()%>
          <%=reportGridAssunto.beginCell()%> <%=DateTools.formatDateTime(resultSetAssuntos.getTimestamp("dt_inicio"))%> <%=reportGridAssunto.endCell()%>
          <%=reportGridAssunto.beginCell()%> <%=DateTools.formatDateTime(resultSetAssuntos.getTimestamp("dt_termino"))%> <%=reportGridAssunto.endCell()%>
        <%=reportGridAssunto.endRow()%>

        <%// incrementa o contador
          count++;
          // estilo da linha
          String rowClassName = count % 2 == 0 ? "gridRowEven" : "griRowOdd";%>
        <tr id="rowAnotacao<%=resultSetAssuntos.getString("in_empresa_id") + resultSetAssuntos.getString("in_atendimento_id")%>" style="display:none;">
          <td class="<%=rowClassName%>"> </td>
          <td class="<%=rowClassName%>" colspan="8"> <%=resultSetAssuntos.getString("va_descricao")%> </td>
        </tr>
        
        <%// adiciona no totalizador
          totalRegistros++;
          // se é o ultimo registro...%>
        <%if(resultSetAssuntos.isLast()) {%>
          <%=gridFooter(reportGridAssunto, totalRegistros)%>
        <%} // if%>
      <%} // while%>
    <%}
      finally {
        // libera recursos
        resultsetRelatorioRankingAtendimentoAssunto.getStatement().close();
        resultsetRelatorioRankingAtendimentoAssunto.close();
        resultSetAssuntos.getStatement().close();
        resultSetAssuntos.close();
      } // try-finally
    %>

    <!-- Rodapé -->
    <%=ReportFooter.script(facade, true, true)%>

    <!-- gera o gráfico -->
    <%=chart.script(Chart.TYPE_COLUMN, "divChart", "Ranking Atendimento Assunto", "", "", Chart.INTERFACE_STYLE_REPORT_INTERFACE)%>

  </body>
</html>

<%}
  // término do bloco protegido
  catch (Exception e) {
    Controller.forwardException(e, pageContext);
  } // try-catch
%>
