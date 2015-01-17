
<%@include file="../include/beans.jsp"%>

<%@page import="imanager.card.*"%>
<%@page import="imanager.entity.*"%>
<%@page import="imanager.misc.*"%>

<%
  class Campanha{
    String nome = "";
    double quantidade = 0.0D;
    public Campanha(String nome, double quantidade) {
      this.nome = nome;
      this.quantidade = quantidade;
    }
  }
  // início do bloco protegido
  try {
    // nossa instância de Atendimento Campanha
    CartaoAtendimentoCampanha cartaoAtendimentoCampanha = (CartaoAtendimentoCampanha)facade.getCard(CartaoAtendimentoCampanha.CLASS_NAME);
    // nossa instância de Atendimento
    Atendimento atendimento = (Atendimento)facade.getEntity(Atendimento.CLASS_NAME);  
    // nosso gráfico
    Chart chart = new Chart(facade, "chart");
%>

<html>
  <head>
    <title><%=CartaoAtendimentoCampanha.ACTION.getCaption()%></title>
    <link href="<%=facade.getStyle().userInterface()%>" rel="stylesheet" type="text/css">
    <script src="include/scripts.js" type="text/javascript"></script>
  </head>
  <body class="frameBody" style="border:0px; margin:0px;" onselectstart="return false;" oncontextmenu="return false;">
    
    <script type="text/javascript">
      function anotacoes(id) {
        var divAnotacoes = document.getElementById(id);
        if (divAnotacoes.style.display == "none")
          divAnotacoes.style.display = "block";
        else
          divAnotacoes.style.display = "none";
      }
    </script>            

    <%// nosso ResultSet
      ResultSet resultSetCartaoAtendimentoCampanha = cartaoAtendimentoCampanha.getResultSetCartaoAtendimentoCampanha(selectedEmpresaId);
      ParamList campanhaList = new ParamList();
      int campanhaId = 0;
      try {%>
        <div id="divChart" align="center" style="width:100%;"></div>
        <script type="text/javascript">
          var aHeight = (document.body.offsetHeight != null ? document.body.offsetHeight : 320);
          document.getElementById("divChart").style.height = aHeight;
        </script>
        <table style="width:100%;">
          <tr>
            <td style="width:40px;"><b>Atendimento</b></td>
            <td align="center" style="width:70px;"><b>Data</b></td>
            <td style="width:auto;"><b>Cliente</b></td>
            <td align="center" style="width:90px;"><b>Telefone</b></td>
          </tr>
        <%// loop nos registros
          while (resultSetCartaoAtendimentoCampanha.next()) {
            if (campanhaId != resultSetCartaoAtendimentoCampanha.getInt(1)) {
              %>
              <tr>
                <td colspan="4"><b><%=resultSetCartaoAtendimentoCampanha.getString(2)%></b></td> 
              </tr>
              <%
              campanhaId = resultSetCartaoAtendimentoCampanha.getInt(1);
            }
            // nosso index do campanha
            int index = campanhaList.indexOf(campanhaId + ""); 
            // se já possuimos o campanha na lista... incrementa quantidade.
            if (index < 0) {
              Campanha campanha = new Campanha(resultSetCartaoAtendimentoCampanha.getString(2).toUpperCase(), 1);
              campanhaList.add(new Param(campanhaId + "", campanha));
            }
            // se não...adiciona o campanha
            else {
              Campanha campanha = (Campanha)campanhaList.get(index).getObject();
              campanha.quantidade++;
            } // if
            String telefone = "";
            if (!resultSetCartaoAtendimentoCampanha.getString(5).trim().equals(""))
              telefone = resultSetCartaoAtendimentoCampanha.getString(5);
            else if (!resultSetCartaoAtendimentoCampanha.getString(6).trim().equals(""))
              telefone = resultSetCartaoAtendimentoCampanha.getString(6);
            else if (!resultSetCartaoAtendimentoCampanha.getString(7).trim().equals(""))
              telefone = resultSetCartaoAtendimentoCampanha.getString(7);%>
          <tr>
            <td>
              <%// se não é um dispositivo móvel...
                if (!facade.getBrowserMobile()) {%>
                  <a href="<%=Atendimento.ACTION_CADASTRO.url(Atendimento.COMMAND_EDIT, Atendimento.FIELD_EMPRESA_ID.getFieldAlias() + "=" + selectedEmpresaId + "&" + Atendimento.FIELD_ATENDIMENTO_ID.getFieldAlias() + "=" + resultSetCartaoAtendimentoCampanha.getInt(3))%>" title="Ir para Atendimento..." target="frameContent"><%=resultSetCartaoAtendimentoCampanha.getString(3)%></a>
              <%}
                else {%>
                  <%=resultSetCartaoAtendimentoCampanha.getString(3)%>
              <%} // if%>
            </td>
            <td  align="center"><%=DateTools.formatDate(resultSetCartaoAtendimentoCampanha.getTimestamp(8))%></td>
            <td>
              <%// se não é um dispositivo móvel...
                if (facade.getBrowserMobile()) {%>              
                  <a href="javascript:void(0);" onclick="anotacoes('<%="anotacoesAtendimentoId" + resultSetCartaoAtendimentoCampanha.getString(3)%>')"><%=resultSetCartaoAtendimentoCampanha.getString(4)%></a>
              <%}
                else {%>                  
                  <%=resultSetCartaoAtendimentoCampanha.getString(4)%>
              <%} // if%>
            </td>
            <td  align="center"><%=StringTools.formatCustomMask(telefone, "(00) 0000-0000")%></td>
            <%if (facade.getBrowserMobile()) {%>
                <tr>
                  <td></td>
                  <td colspan="3"><div  id="<%="anotacoesAtendimentoId" + resultSetCartaoAtendimentoCampanha.getString(3)%>" style="display:none"><%=resultSetCartaoAtendimentoCampanha.getString(9)%></div></td> 
                </tr>
            <%} // if%>                                    
          </tr>
        <%} // while%>
        </table>

    <%}
      finally {
        // libera recursos
        resultSetCartaoAtendimentoCampanha.getStatement().close();
        resultSetCartaoAtendimentoCampanha.close();
      } // try-finally

      // loop nos campanhas
      for(int i = 0; i < campanhaList.size(); i++) {
        Param param = campanhaList.get(i); 
        Campanha campanha = (Campanha)param.getObject(); 
        chart.addValue(campanha.nome, "", (double)campanha.quantidade);
      } // for
    %>
    
    <%=chart.script(Chart.TYPE_COLUMN, "divChart", "", "", "", Chart.INTERFACE_STYLE_USER_INTERFACE, false, !Controller.isMobileRequest(request))%>    

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
