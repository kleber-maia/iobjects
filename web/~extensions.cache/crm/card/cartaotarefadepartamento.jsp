
<%@include file="../include/beans.jsp"%>

<%@page import="imanager.card.*"%>
<%@page import="imanager.entity.*"%>
<%@page import="imanager.misc.*"%>

<%
  class Departamento{
    String nome = "";
    double quantidade = 0.0D;
    public Departamento(String nome, double quantidade) {
      this.nome = nome;
      this.quantidade = quantidade;
    }
  }
  // início do bloco protegido
  try {
    // nossa instância de Atendimento Departamento
    CartaoTarefaDepartamento cartaoTarefaDepartamento = (CartaoTarefaDepartamento)facade.getCard(CartaoTarefaDepartamento.CLASS_NAME);
    // nosso gráfico
    Chart chart = new Chart(facade, "chart");
%>

<html>
  <head>
    <title><%=CartaoTarefaDepartamento.ACTION.getCaption()%></title>
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
      ResultSet resultSetCartaoTarefaDepartamento = cartaoTarefaDepartamento.getResultSetTarefaDepartamento(selectedEmpresaId);
      ParamList departamentoList = new ParamList();
      int departamentoId = 0;
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
          while (resultSetCartaoTarefaDepartamento.next()) {
            if (departamentoId != resultSetCartaoTarefaDepartamento.getInt(1)) {
              %>
              <tr>
                <td colspan="5"><b><%=resultSetCartaoTarefaDepartamento.getString(2)%></b></td> 
              </tr>
              <%
              departamentoId = resultSetCartaoTarefaDepartamento.getInt(1);
            }
            // nosso index do departamento
            int index = departamentoList.indexOf(departamentoId + ""); 
            // se já possuimos o departamento na lista... incrementa quantidade.
            if (index < 0) {
              Departamento departamento = new Departamento(resultSetCartaoTarefaDepartamento.getString(2).toUpperCase(), 1);
              departamentoList.add(new Param(departamentoId + "", departamento));
            }
            // se não...adiciona o departamento
            else {
              Departamento departamento = (Departamento)departamentoList.get(index).getObject();
              departamento.quantidade++;
            } // if
            String telefone = "";
            if (!resultSetCartaoTarefaDepartamento.getString(5).trim().equals(""))
              telefone = resultSetCartaoTarefaDepartamento.getString(5);
            else if (!resultSetCartaoTarefaDepartamento.getString(6).trim().equals(""))
              telefone = resultSetCartaoTarefaDepartamento.getString(6);
            else if (!resultSetCartaoTarefaDepartamento.getString(7).trim().equals(""))
              telefone = resultSetCartaoTarefaDepartamento.getString(7);%>
          <tr>
            <td>
              <%// se não é um dispositivo móvel...
                if (!facade.getBrowserMobile()) {%>
                  <a href="<%=Tarefa.ACTION_CADASTRO.url(Tarefa.COMMAND_EDIT, Tarefa.FIELD_EMPRESA_ID.getFieldAlias() + "=" + selectedEmpresaId + "&" + Tarefa.FIELD_TAREFA_ID.getFieldAlias() + "=" + resultSetCartaoTarefaDepartamento.getInt(3))%>" title="Ir para Tarefa..." target="frameContent"><%=resultSetCartaoTarefaDepartamento.getString(3)%></a>
              <%}
                else {%>
                  <%=resultSetCartaoTarefaDepartamento.getString(3)%>
              <%} // if%>
            </td>
            <td  align="center"><%=DateTools.formatDate(resultSetCartaoTarefaDepartamento.getTimestamp(8))%></td>
            <td>
              <%// se não é um dispositivo móvel...
                if (facade.getBrowserMobile()) {%>              
                  <a href="javascript:void(0);" onclick="anotacoes('<%="anotacoesTarefaId" + resultSetCartaoTarefaDepartamento.getString(3)%>')"><%=resultSetCartaoTarefaDepartamento.getString(4)%></a>
              <%}
                else {%>                  
                  <%=resultSetCartaoTarefaDepartamento.getString(4)%> 
              <%} // if%>
            </td>
            <td><%=resultSetCartaoTarefaDepartamento.getString(10)%></td>
            <td  align="center"><%=StringTools.formatCustomMask(telefone, "(00) 0000-0000")%></td>
          <%if (facade.getBrowserMobile()) {%>
              <tr>
                <td></td>
                <td colspan="4"><div  id="<%="anotacoesTarefaId" + resultSetCartaoTarefaDepartamento.getString(3)%>" style="display:none"><%=resultSetCartaoTarefaDepartamento.getString(11)%></div></td>
              </tr>
          <%} // if%>               
          </tr>
        <%} // while%>
        </table>

    <%}
      finally {
        // libera recursos
        resultSetCartaoTarefaDepartamento.getStatement().close();
        resultSetCartaoTarefaDepartamento.close();
      } // try-finally

      // loop nos departamentos
      for(int i = 0; i < departamentoList.size(); i++) {
        Param param = departamentoList.get(i); 
        Departamento departamento = (Departamento)param.getObject(); 
        chart.addValue(departamento.nome, "", (double)departamento.quantidade);
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
