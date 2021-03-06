
<%@include file="../include/beans.jsp"%>

<%@page import="imanager.card.*"%>
<%@page import="imanager.entity.*"%>
<%@page import="imanager.misc.*"%>

<%
  class Assunto{
    String nome = "";
    double quantidade = 0.0D;
    public Assunto(String nome, double quantidade) {
      this.nome = nome;
      this.quantidade = quantidade;
    }
  }
  // in�cio do bloco protegido
  try {
    // nossa inst�ncia de Atendimento Assunto
    CartaoAtendimentoAssunto cartaoAtendimentoAssunto = (CartaoAtendimentoAssunto)facade.getCard(CartaoAtendimentoAssunto.CLASS_NAME);
    // nossa inst�ncia de Atendimento
    Atendimento atendimento = (Atendimento)facade.getEntity(Atendimento.CLASS_NAME);  
    // nosso gr�fico
    Chart chart = new Chart(facade, "chart");
%>

<html>
  <head>
    <title><%=CartaoAtendimentoAssunto.ACTION.getCaption()%></title>
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
      ResultSet resultSetCartaoAtendimentoAssunto = cartaoAtendimentoAssunto.getResultSetAtendimentoAssunto(selectedEmpresaId);
      ParamList assuntoList = new ParamList();
      int assuntoId = 0;
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
          while (resultSetCartaoAtendimentoAssunto.next()) {
            if (assuntoId != resultSetCartaoAtendimentoAssunto.getInt(1)) {
              %>
              <tr>
                <td colspan="4"><b><%=resultSetCartaoAtendimentoAssunto.getString(2)%></b></td> 
              </tr>
              <%
              assuntoId = resultSetCartaoAtendimentoAssunto.getInt(1);
            }
            // nosso index do assunto
            int index = assuntoList.indexOf(assuntoId + ""); 
            // se j� possuimos o assunto na lista... incrementa quantidade.
            if (index < 0) {
              Assunto assunto = new Assunto(resultSetCartaoAtendimentoAssunto.getString(2).toUpperCase(), 1);
              assuntoList.add(new Param(assuntoId + "", assunto));
            }
            // se n�o...adiciona o assunto
            else {
              Assunto assunto = (Assunto)assuntoList.get(index).getObject();
              assunto.quantidade++;
            } // if
            String telefone = "";
            if (!resultSetCartaoAtendimentoAssunto.getString(5).trim().equals(""))
              telefone = resultSetCartaoAtendimentoAssunto.getString(5);
            else if (!resultSetCartaoAtendimentoAssunto.getString(6).trim().equals(""))
              telefone = resultSetCartaoAtendimentoAssunto.getString(6);
            else if (!resultSetCartaoAtendimentoAssunto.getString(7).trim().equals(""))
              telefone = resultSetCartaoAtendimentoAssunto.getString(7);%>
          <tr>
            <td>
              <%// se n�o � um dispositivo m�vel...
                if (!facade.getBrowserMobile()) {%>
                  <a href="<%=Atendimento.ACTION_CADASTRO.url(Atendimento.COMMAND_EDIT, Atendimento.FIELD_EMPRESA_ID.getFieldAlias() + "=" + selectedEmpresaId + "&" + Atendimento.FIELD_ATENDIMENTO_ID.getFieldAlias() + "=" + resultSetCartaoAtendimentoAssunto.getInt(3))%>" title="Ir para Atendimento..." target="frameContent"><%=resultSetCartaoAtendimentoAssunto.getString(3)%></a>
              <%}
                else {%>
                  <%=resultSetCartaoAtendimentoAssunto.getString(3)%>
              <%} // if%>
            </td>
            <td  align="center"><%=DateTools.formatDate(resultSetCartaoAtendimentoAssunto.getTimestamp(8))%></td>
            <td>
              <%// se n�o � um dispositivo m�vel...
                if (facade.getBrowserMobile()) {%>              
                  <a href="javascript:void(0);" onclick="anotacoes('<%="anotacoesAtendimentoId" + resultSetCartaoAtendimentoAssunto.getString(3)%>')"><%=resultSetCartaoAtendimentoAssunto.getString(4)%></a>
              <%}
                else {%>                  
                  <%=resultSetCartaoAtendimentoAssunto.getString(4)%>
              <%} // if%>
            </td>
            <td  align="center"><%=StringTools.formatCustomMask(telefone, "(00) 0000-0000")%></td>
            <%if (facade.getBrowserMobile()) {%>
                <tr>
                  <td></td>
                  <td colspan="3"><div  id="<%="anotacoesAtendimentoId" + resultSetCartaoAtendimentoAssunto.getString(3)%>" style="display:none"><%=resultSetCartaoAtendimentoAssunto.getString(9)%></div></td> 
                </tr>
            <%} // if%>             
          </tr>
        <%} // while%>
        </table>

    <%}
      finally {
        // libera recursos
        resultSetCartaoAtendimentoAssunto.getStatement().close();
        resultSetCartaoAtendimentoAssunto.close();
      } // try-finally

      // loop nos assuntos
      for(int i = 0; i < assuntoList.size(); i++) {
        Param param = assuntoList.get(i); 
        Assunto assunto = (Assunto)param.getObject(); 
        chart.addValue(assunto.nome, "", (double)assunto.quantidade);
      } // for
    %>
    
    <%=chart.script(Chart.TYPE_COLUMN, "divChart", "", "", "", Chart.INTERFACE_STYLE_USER_INTERFACE, false, !Controller.isMobileRequest(request))%>    

  </body>
</html>

<%
  }
  // t�rmino do bloco protegido
  catch (Exception e) {
    // encaminha exce��o
    Controller.forwardException(e, pageContext);
  } // try-catch
%>
