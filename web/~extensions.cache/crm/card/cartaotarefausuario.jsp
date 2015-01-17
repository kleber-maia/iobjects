
<%@include file="../include/beans.jsp"%>

<%@page import="imanager.card.*"%>
<%@page import="imanager.entity.*"%>
<%@page import="imanager.misc.*"%>

<%
  class UsuarioTarefa{
    String nome = "";
    double quantidade = 0.0D;
    public UsuarioTarefa(String nome, double quantidade) {
      this.nome = nome;
      this.quantidade = quantidade;
    }
  }
  // início do bloco protegido
  try {
    // nossa instância de Atendimento UsuarioTarefa
    CartaoTarefaUsuario cartaoTarefaUsuario = (CartaoTarefaUsuario)facade.getCard(CartaoTarefaUsuario.CLASS_NAME);
    // nosso gráfico
    Chart chart = new Chart(facade, "chart");
%>

<html>
  <head>
    <title><%=CartaoTarefaUsuario.ACTION.getCaption()%></title>
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
      ResultSet resultSetCartaoTarefaUsuario = cartaoTarefaUsuario.getResultSetTarefaUsuario(selectedEmpresaId);
      ParamList usuarioTarefaList = new ParamList();
      int usuarioTarefaId = 0;
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
            <td align="center" style="width:90px;"><b>Telefone</b></td>
          </tr>
        <%// loop nos registros
          while (resultSetCartaoTarefaUsuario.next()) {
            if (usuarioTarefaId != resultSetCartaoTarefaUsuario.getInt(1)) {
              %>
              <tr>
                <td colspan="4"><b><%=resultSetCartaoTarefaUsuario.getString(2)%></b></td> 
              </tr>
              <%
              usuarioTarefaId = resultSetCartaoTarefaUsuario.getInt(1);
            }
            // nosso index do usuarioTarefa
            int index = usuarioTarefaList.indexOf(usuarioTarefaId + ""); 
            // se já possuimos o usuarioTarefa na lista... incrementa quantidade.
            if (index < 0) {
              UsuarioTarefa usuarioTarefa = new UsuarioTarefa(resultSetCartaoTarefaUsuario.getString(2).toUpperCase(), 1);
              usuarioTarefaList.add(new Param(usuarioTarefaId + "", usuarioTarefa));
            }
            // se não...adiciona o usuarioTarefa
            else {
              UsuarioTarefa usuarioTarefa = (UsuarioTarefa)usuarioTarefaList.get(index).getObject();
              usuarioTarefa.quantidade++;
            } // if
            String telefone = "";
            if (!resultSetCartaoTarefaUsuario.getString(5).trim().equals(""))
              telefone = resultSetCartaoTarefaUsuario.getString(5);
            else if (!resultSetCartaoTarefaUsuario.getString(6).trim().equals(""))
              telefone = resultSetCartaoTarefaUsuario.getString(6);
            else if (!resultSetCartaoTarefaUsuario.getString(7).trim().equals(""))
              telefone = resultSetCartaoTarefaUsuario.getString(7);%>
          <tr>
            <td>
              <%// se não é um dispositivo móvel...
                if (!facade.getBrowserMobile()) {%>
                  <a href="<%=Tarefa.ACTION_CADASTRO.url(Tarefa.COMMAND_EDIT, Tarefa.FIELD_EMPRESA_ID.getFieldAlias() + "=" + selectedEmpresaId + "&" + Tarefa.FIELD_TAREFA_ID.getFieldAlias() + "=" + resultSetCartaoTarefaUsuario.getInt(3))%>" title="Ir para Tarefa..." target="frameContent"><%=resultSetCartaoTarefaUsuario.getString(3)%></a>
              <%}
                else {%>
                  <%=resultSetCartaoTarefaUsuario.getString(3)%>
              <%} // if%>
            </td>
            <td  align="center"><%=DateTools.formatDate(resultSetCartaoTarefaUsuario.getTimestamp(8))%></td>
            <td>
              <%// se não é um dispositivo móvel...
                if (facade.getBrowserMobile()) {%>              
                  <a href="javascript:void(0);" onclick="anotacoes('<%="anotacoesTarefaId" + resultSetCartaoTarefaUsuario.getString(3)%>')"><%=resultSetCartaoTarefaUsuario.getString(4)%></a>
              <%}
                else {%>                  
                  <%=resultSetCartaoTarefaUsuario.getString(4)%> 
              <%} // if%>
            </td>
            <td  align="center"><%=StringTools.formatCustomMask(telefone, "(00) 0000-0000")%></td>
          <%if (facade.getBrowserMobile()) {%>
              <tr>
                <td></td>
                <td colspan="3"><div id="<%="anotacoesTarefaId" + resultSetCartaoTarefaUsuario.getString(3)%>" style="display:none"><%=resultSetCartaoTarefaUsuario.getString(9)%></div></td>
              </tr>
          <%} // if%>            
          </tr>
        <%} // while%>
        </table>

    <%}
      finally {
        // libera recursos
        resultSetCartaoTarefaUsuario.getStatement().close();
        resultSetCartaoTarefaUsuario.close();
      } // try-finally

      // loop nos usuarioTarefas
      for(int i = 0; i < usuarioTarefaList.size(); i++) {
        Param param = usuarioTarefaList.get(i); 
        UsuarioTarefa usuarioTarefa = (UsuarioTarefa)param.getObject(); 
        chart.addValue(usuarioTarefa.nome, "", (double)usuarioTarefa.quantidade);
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
