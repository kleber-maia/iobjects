       
<%@include file="../include/beans.jsp"%>

<%@page import="imanager.report.*"%>

<%!
  public class Vendas {
    public double valor = 0;
    public Timestamp data = DateTools.ZERO_DATE;
    public String    campanha = "";
    public int       quantidade = 0;
    public Vendas(double valor, Timestamp data, String campanha, int quantidade) {
      this.valor = valor;
      this.data = data;
      this.campanha = campanha; 
      this.quantidade = quantidade;
    }
  }
%>

<%
  // início do bloco protegido
  try {
    // nossa instância de Campanha Vendas
    RelatorioCampanhaOportunidade campanhaOportunidade = (RelatorioCampanhaOportunidade)facade.getReport(RelatorioCampanhaOportunidade.CLASS_NAME);

    // define os valores dos parâmetros de usuário
    campanhaOportunidade.userParamList().setParamsValues(request);

    // nosso gráfico
    Chart chartRelatorioCampanhaOportunidade = new Chart(facade, "chartRelatorioCampanhaOportunidade");

    // nosso Grid
    ReportGridEx reportGridRelatorioCampanhaOportunidade = new ReportGridEx("gridRelatorioCampanhaOportunidade", true);
    reportGridRelatorioCampanhaOportunidade.addColumn("Data", 20, ReportGridEx.ALIGN_LEFT);
    reportGridRelatorioCampanhaOportunidade.addColumn("Campanha", 40, ReportGridEx.ALIGN_LEFT);
    reportGridRelatorioCampanhaOportunidade.addColumn("Quantidade", 20, ReportGridEx.ALIGN_LEFT);
    reportGridRelatorioCampanhaOportunidade.addColumn("Valor", 20, ReportGridEx.ALIGN_RIGHT);
%>

<html>
  <head>
    <title><%=RelatorioCampanhaOportunidade.ACTION.getCaption()%></title>
    <link href="<%=facade.getStyle().reportInterface()%>" rel="stylesheet" type="text/css">
    <script src="include/scripts.js" type="text/javascript"></script>
  </head>
  <body style="margin:5px;" oncontextmenu="return false;">

    <!-- Cabeçalho -->
    <%=ReportHeader.script(facade, RelatorioCampanhaOportunidade.ACTION, true, true, true, true, true)%>

    <!-- Parâmetros -->
    <p>
      <table>
        <tr>
          <td><%=ReportParamLabel.script(facade, campanhaOportunidade.USER_PARAM_DATA_INICIAL)%></td>
          <td><%=ReportParamText.script(facade, campanhaOportunidade.USER_PARAM_DATA_INICIAL)%></td>
        </tr>
        <tr>
          <td><%=ReportParamLabel.script(facade, campanhaOportunidade.USER_PARAM_DATA_FINAL)%></td>
          <td><%=ReportParamText.script(facade, campanhaOportunidade.USER_PARAM_DATA_FINAL)%></td>
        </tr>
        <tr>
          <td><%=ReportParamLabel.script(facade, campanhaOportunidade.USER_PARAM_CONSOLIDADO)%></td>
          <td><%=ReportParamText.script(facade, campanhaOportunidade.USER_PARAM_CONSOLIDADO)%></td>
        </tr>        
      </table>
    </p>

    <!-- Gráfico -->
    <p align="center">
      <div id="divChart" align="center" style="width:100%; height:300px;">Carregando dados...</div>
    </p>

    <!-- Título de dados -->
    <%=ReportTitle.script("Dados", ReportTitle.LEVEL_1)%>
 
    <%=reportGridRelatorioCampanhaOportunidade.begin()%>
    <%// nosso ResultSet
      ResultSet resultSetCartaoCampanhaVendas = campanhaOportunidade.getResultSetRelatorioCampanhaOportunidade(selectedEmpresaId, campanhaOportunidade.USER_PARAM_DATA_INICIAL.getDateValue(), campanhaOportunidade.USER_PARAM_DATA_FINAL.getDateValue(), campanhaOportunidade.USER_PARAM_CONSOLIDADO.getIntValue());
      ParamList vendasList = new ParamList();
      try {%>
        <%chartRelatorioCampanhaOportunidade.addSerie("Vendas sem Campanha");
          // loop nos registros
          while (resultSetCartaoCampanhaVendas.next()) {
            Param param = vendasList.get(DateTools.formatDate(resultSetCartaoCampanhaVendas.getTimestamp("data")).substring(0, 5));
            if (param == null) {
              vendasList.add(new Param(DateTools.formatDate(resultSetCartaoCampanhaVendas.getTimestamp("data")).substring(0, 5), new Vendas(resultSetCartaoCampanhaVendas.getDouble("valor"), resultSetCartaoCampanhaVendas.getTimestamp("data"), resultSetCartaoCampanhaVendas.getObject("campanha") != null ? resultSetCartaoCampanhaVendas.getString("campanha") : "", resultSetCartaoCampanhaVendas.getInt("quantidade"))));
              chartRelatorioCampanhaOportunidade.addValue(DateTools.formatDate(resultSetCartaoCampanhaVendas.getTimestamp("data")).substring(0, 5), "Vendas sem Campanha", resultSetCartaoCampanhaVendas.getObject("campanha") != null ? 0 : resultSetCartaoCampanhaVendas.getDouble("valor"));
            }
            else {
              Vendas vendas = (Vendas)param.getObject();
              vendas.campanha += "/" + resultSetCartaoCampanhaVendas.getString("campanha"); 
            } // if%>
        <%} // while%>
      <%for (int i=0; i<vendasList.size();i++) {
          Vendas vendas = (Vendas)vendasList.get(i).getObject();%>
          <%=reportGridRelatorioCampanhaOportunidade.beginRow()%>
            <%=reportGridRelatorioCampanhaOportunidade.beginCell()%><%=DateTools.formatDate(vendas.data)%><%=reportGridRelatorioCampanhaOportunidade.endCell()%>
            <%=reportGridRelatorioCampanhaOportunidade.beginCell()%><%=vendas.campanha%><%=reportGridRelatorioCampanhaOportunidade.endCell()%>
            <%=reportGridRelatorioCampanhaOportunidade.beginCell()%><%=vendas.quantidade%><%=reportGridRelatorioCampanhaOportunidade.endCell()%>
            <%=reportGridRelatorioCampanhaOportunidade.beginCell()%><%=NumberTools.format(vendas.valor)%><%=reportGridRelatorioCampanhaOportunidade.endCell()%>
          <%=reportGridRelatorioCampanhaOportunidade.endRow()%>
      <%} // for%>
        <%=reportGridRelatorioCampanhaOportunidade.end()%>

    <%}
      finally {
        // libera recursos
        resultSetCartaoCampanhaVendas.getStatement().close();
        resultSetCartaoCampanhaVendas.close();
      } // try-finally

      chartRelatorioCampanhaOportunidade.addSerie("Vendas com Campanha");
      // loop nos assuntos
      for(int i = 0; i < vendasList.size(); i++) {
        Param param = vendasList.get(i); 
        Vendas vendas = (Vendas)param.getObject(); 
        chartRelatorioCampanhaOportunidade.addValue(DateTools.formatDate(vendas.data).substring(0, 5), "Vendas com Campanha", !vendas.campanha.isEmpty() ? vendas.valor : 0);   
      } // for%>

    <!-- Rodapé -->
    <%=ReportFooter.script(facade, true, true)%>

    
    <%=chartRelatorioCampanhaOportunidade.script(Chart.TYPE_AREA, "divChart", "Gráfico", "", "", Chart.INTERFACE_STYLE_REPORT_INTERFACE)%>

  </body>
</html>

<%}
  // término do bloco protegido
  catch (Exception e) {
    Controller.forwardException(e, pageContext);
  } // try-catch
%>
