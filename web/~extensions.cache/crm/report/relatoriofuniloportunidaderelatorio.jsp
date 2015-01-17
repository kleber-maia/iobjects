
<%@include file="../include/beans.jsp"%>

<%@page import="imanager.entity.*"%>
<%@page import="imanager.misc.*"%>
<%@page import="imanager.report.*"%>

<%@page import="securityservice.entity.*"%>

<%
  // início do bloco protegido
  try {
    // nossa instância de RelatorioFunilOportunidade
    RelatorioFunilOportunidade relatorioFunilOportunidade = (RelatorioFunilOportunidade)facade.getReport(RelatorioFunilOportunidade.CLASS_NAME);
    // nossa instância de Departamento
    Departamento departamento = (Departamento)facade.getEntity(Departamento.CLASS_NAME);
    // nossa instância de Campanha
    Campanha campanha = (Campanha)facade.getEntity(Campanha.CLASS_NAME);
    // nossa instância de Usuario
    Usuario usuario = (Usuario)facade.getEntity(Usuario.CLASS_NAME);
    // nossa instância de Oportunidade
    Oportunidade oportunidade = (Oportunidade)facade.getEntity(Oportunidade.CLASS_NAME);

    // define os valores dos parâmetros de usuário
    relatorioFunilOportunidade.userParamList().setParamsValues(request);

    // nosso gráfico
    Chart chartRelatorioFunilOportunidade = new Chart(facade, "chartRelatorioFunilOportunidade");

    // procura pelo Departamento
    DepartamentoInfo departamentoInfo = null;
    if (relatorioFunilOportunidade.USER_PARAM_DEPARTAMENTO_ID.getIntValue() > 0)
      departamentoInfo = departamento.selectByPrimaryKey(selectedEmpresaId, relatorioFunilOportunidade.USER_PARAM_DEPARTAMENTO_ID.getIntValue());
    // procura pela Campanha
    CampanhaInfo campanhaInfo = null;
    if (relatorioFunilOportunidade.USER_PARAM_CAMPANHA_ID.getIntValue() > 0)
      campanhaInfo = campanha.selectByPrimaryKey(relatorioFunilOportunidade.USER_PARAM_CAMPANHA_ID.getIntValue());
    // procura pelo Usuário
    UsuarioInfo usuarioInfo = null;
    if (relatorioFunilOportunidade.USER_PARAM_USUARIO.getIntValue() > 0)
      usuarioInfo = usuario.selectByUsuarioId(relatorioFunilOportunidade.USER_PARAM_USUARIO.getIntValue());

    // estamos na central de atendimento?
    boolean isCentralAtendimento = request.getParameter(oportunidade.CONTROL_PARAM_CENTRAL_ATENDIMENTO.getName()) != null;
    // grava na sessão
    session.setAttribute(oportunidade.CONTROL_PARAM_CENTRAL_ATENDIMENTO.getName(), isCentralAtendimento);

    // nosso Grid
    ReportGridEx reportGridRelatorioFunilOportunidade = new ReportGridEx("gridRelatorioFunilOportunidade", true);
    reportGridRelatorioFunilOportunidade.addColumn("ID", 4, ReportGrid.ALIGN_LEFT);
    reportGridRelatorioFunilOportunidade.addColumn("Departamento", 15, ReportGrid.ALIGN_LEFT);
    reportGridRelatorioFunilOportunidade.addColumn("Campanha", 15, ReportGrid.ALIGN_LEFT);
    reportGridRelatorioFunilOportunidade.addColumn("Usuário", 10, ReportGrid.ALIGN_LEFT);
    reportGridRelatorioFunilOportunidade.addColumn("Cliente", 20, ReportGrid.ALIGN_LEFT);
    reportGridRelatorioFunilOportunidade.addColumn("Acompanhamento", 10, ReportGrid.ALIGN_LEFT);
    reportGridRelatorioFunilOportunidade.addColumn("Status", 15, ReportGrid.ALIGN_LEFT);
    reportGridRelatorioFunilOportunidade.addColumn("% Sucesso", 5, ReportGrid.ALIGN_RIGHT);
    reportGridRelatorioFunilOportunidade.addColumn("Valor", 6, ReportGrid.ALIGN_RIGHT);
%>

<html>
  <head>
    <title><%=RelatorioFunilOportunidade.ACTION.getCaption()%></title>
    <link href="<%=facade.getStyle().reportInterface()%>" rel="stylesheet" type="text/css">
    <script src="include/scripts.js" type="text/javascript"></script>
  </head>
  <body style="margin:5px;" oncontextmenu="return false;">

    <!-- Cabeçalho -->
    <%=ReportHeader.script(facade, RelatorioFunilOportunidade.ACTION, true, true, true, true, true)%>

    <!-- Parâmetros -->
    <p>
      <table>
        <tr>
          <td><%=ReportParamLabel.script(facade, relatorioFunilOportunidade.USER_PARAM_DATA_INICIAL)%></td>
          <td><%=ReportParamText.script(facade, relatorioFunilOportunidade.USER_PARAM_DATA_INICIAL)%></td>
        </tr>
        <tr>
          <td><%=ReportParamLabel.script(facade, relatorioFunilOportunidade.USER_PARAM_DATA_FINAL)%></td>
          <td><%=ReportParamText.script(facade, relatorioFunilOportunidade.USER_PARAM_DATA_FINAL)%></td>
        </tr>
        <tr>
          <td><%=ReportParamLabel.script(facade, relatorioFunilOportunidade.USER_PARAM_STATUS)%></td>
          <td><%=ReportParamText.script(facade, relatorioFunilOportunidade.USER_PARAM_STATUS)%></td>
        </tr>
        <tr>
          <td><%=ReportParamLabel.script(facade, relatorioFunilOportunidade.USER_PARAM_USUARIO)%></td>
          <td><%=usuarioInfo != null ?  usuarioInfo.getNome() : ""%></td>
        </tr>
        <tr>
          <td><%=ReportParamLabel.script(facade, relatorioFunilOportunidade.USER_PARAM_DEPARTAMENTO_ID)%></td>
          <td><%=departamentoInfo != null ?  departamentoInfo.getNome() : ""%></td>
        </tr>
        <tr>
          <td><%=ReportParamLabel.script(facade, relatorioFunilOportunidade.USER_PARAM_CAMPANHA_ID)%></td>
          <td><%=campanhaInfo != null ?  campanhaInfo.getNome() : ""%></td>
        </tr>
        <tr>
          <td><%=ReportParamLabel.script(facade, relatorioFunilOportunidade.USER_PARAM_CONSOLIDADO)%></td>
          <td><%=ReportParamText.script(facade, relatorioFunilOportunidade.USER_PARAM_CONSOLIDADO)%></td>
        </tr>
      </table>
    </p>

    <!-- Gráfico -->
    <p align="center">
      <div id="divChart" align="center" style="width:100%; height:300px;">Carregando dados...</div>
    </p>

    <!-- Grid de dados -->
    <%ResultSet resultSetRelatorioFunilOportunidade = relatorioFunilOportunidade.getResultSetRelatorioFunilOportunidade(selectedEmpresaId, relatorioFunilOportunidade.USER_PARAM_DATA_INICIAL.getDateValue(), relatorioFunilOportunidade.USER_PARAM_DATA_FINAL.getDateValue(), relatorioFunilOportunidade.USER_PARAM_STATUS.getIntValue(), relatorioFunilOportunidade.USER_PARAM_USUARIO.getIntValue(), relatorioFunilOportunidade.USER_PARAM_DEPARTAMENTO_ID.getIntValue(), relatorioFunilOportunidade.USER_PARAM_CAMPANHA_ID.getIntValue(), relatorioFunilOportunidade.USER_PARAM_CONSOLIDADO.getIntValue());%>
    <%try {
        // controlador
        boolean primeiro = true;
        // totalizador
        int totalRegistros = 0;
        // armazena o nome da fase da vez
        String proximaFase = "";
        // loop nos registros
        while(resultSetRelatorioFunilOportunidade.next()) {
          // se o nome da fase é diferente do anterior...
          if(!resultSetRelatorioFunilOportunidade.getString("nome_fase").equals(proximaFase)) {
            // se não é o primeiro registro... fecha o grid
            if(!primeiro) {
              // adiciona ao gráfico
              chartRelatorioFunilOportunidade.addValue(proximaFase, "", totalRegistros);%>
              <%=reportGridRelatorioFunilOportunidade.beginRow(true)%>
                <%=reportGridRelatorioFunilOportunidade.beginCell(9)%> <%=totalRegistros%> registro(s) <%=reportGridRelatorioFunilOportunidade.endCell()%>
              <%=reportGridRelatorioFunilOportunidade.endRow()%>
            <%=reportGridRelatorioFunilOportunidade.end()%>
          <%} // if
            // imprime novo cabeçalho na tela %>
          <%=ReportTitle.script(resultSetRelatorioFunilOportunidade.getString("nome_fase"), ReportTitle.LEVEL_1)%>
          <%// proximaFase recebe o nome da próxima fase
            proximaFase = resultSetRelatorioFunilOportunidade.getString("nome_fase");%>
          <%// zera totalizador
            totalRegistros = 0;%>
          <%=reportGridRelatorioFunilOportunidade.begin()%>
          <%if(primeiro)
             primeiro = false;%>
        <%} // if%>
        <%=reportGridRelatorioFunilOportunidade.beginRow()%>
          <%=reportGridRelatorioFunilOportunidade.beginCell()%> <a href="<%=Oportunidade.ACTION_CADASTRO.url(Oportunidade.COMMAND_EDIT, Oportunidade.FIELD_EMPRESA_ID.getFieldAlias() + "=" + resultSetRelatorioFunilOportunidade.getInt("empresa_id") + "&" + Oportunidade.FIELD_OPORTUNIDADE_ID.getFieldAlias() + "=" + resultSetRelatorioFunilOportunidade.getInt("in_oportunidade_id"))%>" title="Ir para Oportunidade..." target="frameContent"> <%=resultSetRelatorioFunilOportunidade.getInt("in_oportunidade_id")%> </a> <%=reportGridRelatorioFunilOportunidade.endCell()%>
          <%=reportGridRelatorioFunilOportunidade.beginCell()%> <%=resultSetRelatorioFunilOportunidade.getString("nome_departamento")%> <%=reportGridRelatorioFunilOportunidade.endCell()%>
          <%=reportGridRelatorioFunilOportunidade.beginCell()%> <%=resultSetRelatorioFunilOportunidade.getString("nome_campanha")%> <%=reportGridRelatorioFunilOportunidade.endCell()%>
          <%=reportGridRelatorioFunilOportunidade.beginCell()%> <%=resultSetRelatorioFunilOportunidade.getString("nome_usuario")%> <%=reportGridRelatorioFunilOportunidade.endCell()%>
          <%=reportGridRelatorioFunilOportunidade.beginCell()%> <%=resultSetRelatorioFunilOportunidade.getString("nome_cliente")%> <%=reportGridRelatorioFunilOportunidade.endCell()%>
          <%=reportGridRelatorioFunilOportunidade.beginCell()%> <%=DateTools.formatDate(resultSetRelatorioFunilOportunidade.getTimestamp("da_acompanhamento"))%> <%=reportGridRelatorioFunilOportunidade.endCell()%>
          <%=reportGridRelatorioFunilOportunidade.beginCell()%> 
            <%int status = resultSetRelatorioFunilOportunidade.getInt("status");%>
            <%="<font style=color:" + StatusOportunidade.COLOR_LIST_FOR_FIELD[status] + ";>" + StatusOportunidade.LOOKUP_LIST_FOR_FIELD[status] + "</font>"%> 
          <%=reportGridRelatorioFunilOportunidade.endCell()%>
          <%=reportGridRelatorioFunilOportunidade.beginCell()%> <%=resultSetRelatorioFunilOportunidade.getInt("sucesso")%> <%=reportGridRelatorioFunilOportunidade.endCell()%>
          <%=reportGridRelatorioFunilOportunidade.beginCell()%> <%=NumberTools.format(resultSetRelatorioFunilOportunidade.getDouble("valor"))%> <%=reportGridRelatorioFunilOportunidade.endCell()%>
        <%=reportGridRelatorioFunilOportunidade.endRow()%>
        <%// adiciona no totalizador
          totalRegistros++;
          // se é o ultimo registro...%>
        <%if(resultSetRelatorioFunilOportunidade.isLast()) {
            // adiciona ao gráfico
            chartRelatorioFunilOportunidade.addValue(resultSetRelatorioFunilOportunidade.getString("nome_fase"), "", totalRegistros);%>
            <%=reportGridRelatorioFunilOportunidade.beginRow(true)%>
              <%=reportGridRelatorioFunilOportunidade.beginCell(9)%> <%=totalRegistros%> registro(s) <%=reportGridRelatorioFunilOportunidade.endCell()%>
            <%=reportGridRelatorioFunilOportunidade.endRow()%>
          <%=reportGridRelatorioFunilOportunidade.end()%>
        <%} // if%>
      <%} // while%>
    <%}
      finally {
        // libera recursos
        resultSetRelatorioFunilOportunidade.getStatement().close();
        resultSetRelatorioFunilOportunidade.close();
      } // try-finally
    %>

    <!-- Rodapé -->
    <%=ReportFooter.script(facade, true, true)%>


    <%=chartRelatorioFunilOportunidade.script(Chart.TYPE_AREA, "divChart", "Gráfico", "", "", Chart.INTERFACE_STYLE_REPORT_INTERFACE)%>

  </body>
</html>

<%}
  // término do bloco protegido
  catch (Exception e) {
    Controller.forwardException(e, pageContext);
  } // try-catch
%>
