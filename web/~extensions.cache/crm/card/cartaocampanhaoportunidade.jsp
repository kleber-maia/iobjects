<%@include file="../include/beans.jsp"%>

<%@page import="imanager.card.*"%>
<%@page import="imanager.entity.*"%>
<%@page import="imanager.misc.*"%>

<%!
  public class OportunidadeCard {
    public double valor = 0;
    public Timestamp data = DateTools.ZERO_DATE;
    public String    campanha = "";
    public int       quantidade = 0;
    public OportunidadeCard(double valor, Timestamp data, String campanha, int quantidade) {
      this.valor = valor;
      this.data = data;
      this.campanha = campanha; 
      this.quantidade = quantidade;
    }
  }
%>

<%// início do bloco protegido
  try {
    // nossa instância de Atendimento Assunto
    CartaoCampanhaOportunidade cartaoCampanhaOportunidade = (CartaoCampanhaOportunidade)facade.getCard(CartaoCampanhaOportunidade.CLASS_NAME);
    
    // nosso gráfico
    Chart chart = new Chart(facade, "chart");
%>

<html>
  <head>
    <title><%=CartaoAtendimentoAssunto.ACTION.getCaption()%></title>
    <link href="<%=facade.getStyle().userInterface()%>" rel="stylesheet" type="text/css">
    <script src="include/scripts.js" type="text/javascript"></script>
  </head>
  <body class="frameBody" style="border:0px; margin:0px;" onselectstart="return false;" oncontextmenu="return false;">          

    <%// nosso ResultSet
      ResultSet resultSetCartaoCampanhaOportunidade = cartaoCampanhaOportunidade.getResultSetCampanhaOportunidade(selectedEmpresaId);
      ParamList oportunidadeList = new ParamList();
      try {%>
        <div id="divChart" align="center" style="width:100%;"></div>
        <script type="text/javascript">
          var aHeight = (document.body.offsetHeight != null ? document.body.offsetHeight : 320);
          document.getElementById("divChart").style.height = aHeight;
        </script>
        <table align="center" style="width: 100%;">
          <tr>
            <td style="width: 70px;"><b>Data</b></td>
            <td style="width:auto;"><b>Campanha</b></td>
            <td align="right" style="width:100px;"><b>Quantidade</b></td>
            <td align="right" style="width:100px;"><b>Valor</b></td>
          </tr>
        <%chart.addSerie("Oportunidade sem Campanha");
          // loop nos registros
          while (resultSetCartaoCampanhaOportunidade.next()) {
            Param param = oportunidadeList.get(DateTools.formatDate(resultSetCartaoCampanhaOportunidade.getTimestamp("data")).substring(0, 5));
            if (param == null) {
              oportunidadeList.add(new Param(DateTools.formatDate(resultSetCartaoCampanhaOportunidade.getTimestamp("data")).substring(0, 5), new OportunidadeCard(resultSetCartaoCampanhaOportunidade.getDouble("valor"), resultSetCartaoCampanhaOportunidade.getTimestamp("data"), resultSetCartaoCampanhaOportunidade.getObject("campanha") != null ? resultSetCartaoCampanhaOportunidade.getString("campanha") : "", resultSetCartaoCampanhaOportunidade.getInt("quantidade"))));
              chart.addValue(DateTools.formatDate(resultSetCartaoCampanhaOportunidade.getTimestamp("data")).substring(0, 5), "Oportunidade sem Campanha", resultSetCartaoCampanhaOportunidade.getObject("campanha") != null ? 0 : resultSetCartaoCampanhaOportunidade.getDouble("valor"));
            }
            else {
              OportunidadeCard oportunidade = (OportunidadeCard)param.getObject();
              oportunidade.campanha += "/" + resultSetCartaoCampanhaOportunidade.getString("campanha"); 
            } // if%>
        <%} // while%>
      <%for (int i=0; i<oportunidadeList.size();i++) {
          OportunidadeCard oportunidade = (OportunidadeCard)oportunidadeList.get(i).getObject();%>
          <tr>
            <td><%=DateTools.formatDate(oportunidade.data).substring(0, 5)%></td>
            <td><%=oportunidade.campanha%></td> 
            <td align="right"><%=oportunidade.quantidade%></td>
            <td align="right"><%=NumberTools.format(oportunidade.valor)%></td>
          </tr>
      <%} // for%>
        </table>

    <%}
      finally {
        // libera recursos
        resultSetCartaoCampanhaOportunidade.getStatement().close();
        resultSetCartaoCampanhaOportunidade.close();
      } // try-finally

      chart.addSerie("Oportunidade com Campanha");
      // loop nos assuntos
      for(int i = 0; i < oportunidadeList.size(); i++) {
        Param param = oportunidadeList.get(i); 
        OportunidadeCard oportunidade = (OportunidadeCard)param.getObject(); 
        chart.addValue(DateTools.formatDate(oportunidade.data).substring(0, 5), "Oportunidade com Campanha", !oportunidade.campanha.isEmpty() ? oportunidade.valor : 0);   
      } // for%>
    
    <%=chart.script(Chart.TYPE_AREA, "divChart", "", "", "", Chart.INTERFACE_STYLE_USER_INTERFACE, false, !Controller.isMobileRequest(request))%>    

  </body>
</html>

<%
  }
  // término do bloco protegido
  catch (Exception e) {
    // encaminha exceção
    Controller.forwardException(e, pageContext);
  } // try-catch
%>
