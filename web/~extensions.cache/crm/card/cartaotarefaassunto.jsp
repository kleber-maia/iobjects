
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
  // início do bloco protegido
  try {
    // nossa instância de Atendimento Assunto
    CartaoTarefaAssunto cartaoTarefaAssunto = (CartaoTarefaAssunto)facade.getCard(CartaoTarefaAssunto.CLASS_NAME);
    // nosso gráfico
    Chart chart = new Chart(facade, "chart");
%>

<html>
  <head>
    <title><%=CartaoTarefaAssunto.ACTION.getCaption()%></title>
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
      ResultSet resultSetCartaoTarefaAssunto = cartaoTarefaAssunto.getResultSetTarefaAssunto(selectedEmpresaId);
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
            <td style="width:40px;"><b>Tarefa</b></td>
            <td align="center" style="width:70px;"><b>Prazo</b></td>
            <td style="width:auto;"><b>Cliente</b></td>
            <td style="width:100px;"><b>Usuário</b></td>
            <td align="center" style="width:90px;"><b>Telefone</b></td>
          </tr>
        <%// loop nos registros
          while (resultSetCartaoTarefaAssunto.next()) {
            if (assuntoId != resultSetCartaoTarefaAssunto.getInt(1)) {
              %>
              <tr>
                <td colspan="5"><b><%=resultSetCartaoTarefaAssunto.getString(2)%></b></td> 
              </tr>
              <%
              assuntoId = resultSetCartaoTarefaAssunto.getInt(1);
            }
            // nosso index do assunto
            int index = assuntoList.indexOf(assuntoId + ""); 
            // se já possuimos o assunto na lista... incrementa quantidade.
            if (index < 0) {
              Assunto assunto = new Assunto(resultSetCartaoTarefaAssunto.getString(2).toUpperCase(), 1);
              assuntoList.add(new Param(assuntoId + "", assunto));
            }
            // se não...adiciona o assunto
            else {
              Assunto assunto = (Assunto)assuntoList.get(index).getObject();
              assunto.quantidade++;
            } // if
            String telefone = "";
            if (!resultSetCartaoTarefaAssunto.getString(5).trim().equals(""))
              telefone = resultSetCartaoTarefaAssunto.getString(5);
            else if (!resultSetCartaoTarefaAssunto.getString(6).trim().equals(""))
              telefone = resultSetCartaoTarefaAssunto.getString(6);
            else if (!resultSetCartaoTarefaAssunto.getString(7).trim().equals(""))
              telefone = resultSetCartaoTarefaAssunto.getString(7);%>
          <tr>
            <td>
              <%// se não é um dispositivo móvel...
                if (!facade.getBrowserMobile()) {%>
                  <a href="<%=Tarefa.ACTION_CADASTRO.url(Tarefa.COMMAND_EDIT, Tarefa.FIELD_EMPRESA_ID.getFieldAlias() + "=" + selectedEmpresaId + "&" + Tarefa.FIELD_TAREFA_ID.getFieldAlias() + "=" + resultSetCartaoTarefaAssunto.getInt(3))%>" title="Ir para Tarefa..." target="frameContent"><%=resultSetCartaoTarefaAssunto.getString(3)%></a>
              <%}
                else {%>
                  <%=resultSetCartaoTarefaAssunto.getString(3)%>
              <%} // if%>
            </td>
            <td  align="center"><%=DateTools.formatDate(resultSetCartaoTarefaAssunto.getTimestamp(8))%></td>
            <td>
              <%// se não é um dispositivo móvel...
                if (facade.getBrowserMobile()) {%>              
                  <a href="javascript:void(0);" onclick="anotacoes('<%="anotacoesTarefaId" + resultSetCartaoTarefaAssunto.getString(3)%>')"><%=resultSetCartaoTarefaAssunto.getString(4)%></a>
              <%}
                else {%>                  
                  <%=resultSetCartaoTarefaAssunto.getString(4)%>
              <%} // if%>
            </td>
            <td><%=resultSetCartaoTarefaAssunto.getString(10)%></td>
            <td align="center"><%=StringTools.formatCustomMask(telefone, "(00) 0000-0000")%></td>
            <%if (facade.getBrowserMobile()) {%>
                <tr>
                  <td></td>
                  <td colspan="4"><div  id="<%="anotacoesTarefaId" + resultSetCartaoTarefaAssunto.getString(3)%>" style="display:none"><%=resultSetCartaoTarefaAssunto.getString(11)%></div></td>
                </tr>
            <%} // if%>
          </tr>
        <%} // while%>
        </table>

    <%}
      finally {
        // libera recursos
        resultSetCartaoTarefaAssunto.getStatement().close();
        resultSetCartaoTarefaAssunto.close();
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
  // término do bloco protegido
  catch (Exception e) {
    // encaminha exceção
    Controller.forwardException(e, pageContext);
  } // try-catch
%>
