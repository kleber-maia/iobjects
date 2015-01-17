
<%@include file="../include/beans.jsp"%>

<%@page import="imanager.card.*"%>
<%@page import="imanager.entity.*"%>
<%@page import="imanager.misc.*"%>

<%
  class UsuarioAtendimento{
    String nome = "";
    double quantidade = 0.0D;
    public UsuarioAtendimento(String nome, double quantidade) {
      this.nome = nome;
      this.quantidade = quantidade;
    }
  }
  // início do bloco protegido
  try {
    // nossa instância de Atendimento UsuarioAtendimento
    CartaoAtendimentoUsuario cartaoAtendimentoUsuario = (CartaoAtendimentoUsuario)facade.getCard(CartaoAtendimentoUsuario.CLASS_NAME);
    // nosso gráfico
    Chart chart = new Chart(facade, "chart");
%>

<html>
  <head>
    <title><%=CartaoAtendimentoUsuario.ACTION.getCaption()%></title>
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
      ResultSet resultSetCartaoAtendimentoUsuario = cartaoAtendimentoUsuario.getResultSetAtendimentoUsuario(selectedEmpresaId);
      ParamList usuarioAtendimentoList = new ParamList();
      int usuarioAtendimentoId = 0;
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
          while (resultSetCartaoAtendimentoUsuario.next()) {
            if (usuarioAtendimentoId != resultSetCartaoAtendimentoUsuario.getInt(1)) {
              %>
              <tr>
                <td colspan="4"><b><%=resultSetCartaoAtendimentoUsuario.getString(2)%></b></td> 
              </tr>
              <%
              usuarioAtendimentoId = resultSetCartaoAtendimentoUsuario.getInt(1);
            }
            // nosso index do usuarioAtendimento
            int index = usuarioAtendimentoList.indexOf(usuarioAtendimentoId + ""); 
            // se já possuimos o usuarioAtendimento na lista... incrementa quantidade.
            if (index < 0) {
              UsuarioAtendimento usuarioAtendimento = new UsuarioAtendimento(resultSetCartaoAtendimentoUsuario.getString(2).toUpperCase(), 1);
              usuarioAtendimentoList.add(new Param(usuarioAtendimentoId + "", usuarioAtendimento));
            }
            // se não...adiciona o usuarioAtendimento
            else {
              UsuarioAtendimento usuarioAtendimento = (UsuarioAtendimento)usuarioAtendimentoList.get(index).getObject();
              usuarioAtendimento.quantidade++;
            } // if
            String telefone = "";
            if (!resultSetCartaoAtendimentoUsuario.getString(5).trim().equals(""))
              telefone = resultSetCartaoAtendimentoUsuario.getString(5);
            else if (!resultSetCartaoAtendimentoUsuario.getString(6).trim().equals(""))
              telefone = resultSetCartaoAtendimentoUsuario.getString(6);
            else if (!resultSetCartaoAtendimentoUsuario.getString(7).trim().equals(""))
              telefone = resultSetCartaoAtendimentoUsuario.getString(7);%>
          <tr>
            <td>
              <%// se não é um dispositivo móvel...
                if (!facade.getBrowserMobile()) {%>
                  <a href="<%=Atendimento.ACTION_CADASTRO.url(Atendimento.COMMAND_EDIT, Atendimento.FIELD_EMPRESA_ID.getFieldAlias() + "=" + selectedEmpresaId + "&" + Atendimento.FIELD_ATENDIMENTO_ID.getFieldAlias() + "=" + resultSetCartaoAtendimentoUsuario.getInt(3))%>" title="Ir para Atendimento..." target="frameContent"><%=resultSetCartaoAtendimentoUsuario.getString(3)%></a>
              <%}
                else {%>
                  <%=resultSetCartaoAtendimentoUsuario.getString(3)%>
              <%} // if%>
            </td>
            <td  align="center"><%=DateTools.formatDate(resultSetCartaoAtendimentoUsuario.getTimestamp(8))%></td>
            <td>
              <%// se não é um dispositivo móvel...
                if (facade.getBrowserMobile()) {%>              
                  <a href="javascript:void(0);" onclick="anotacoes('<%="anotacoesAtendimentoId" + resultSetCartaoAtendimentoUsuario.getString(3)%>')"><%=resultSetCartaoAtendimentoUsuario.getString(4)%></a>
              <%}
                else {%>                  
                  <%=resultSetCartaoAtendimentoUsuario.getString(4)%>
              <%} // if%>
            </td>
            <td  align="center"><%=StringTools.formatCustomMask(telefone, "(00) 0000-0000")%></td>
            <%if (facade.getBrowserMobile()) {%>
                <tr>
                  <td></td>
                  <td colspan="3"><div  id="<%="anotacoesAtendimentoId" + resultSetCartaoAtendimentoUsuario.getString(3)%>" style="display:none"><%=resultSetCartaoAtendimentoUsuario.getString(9)%></div></td>
                </tr>
            <%} // if%>            
          </tr>
        <%} // while%>
        </table>

    <%}
      finally {
        // libera recursos
        resultSetCartaoAtendimentoUsuario.getStatement().close();
        resultSetCartaoAtendimentoUsuario.close();
      } // try-finally

      // loop nos usuarioAtendimentos
      for(int i = 0; i < usuarioAtendimentoList.size(); i++) {
        Param param = usuarioAtendimentoList.get(i); 
        UsuarioAtendimento usuarioAtendimento = (UsuarioAtendimento)param.getObject(); 
        chart.addValue(usuarioAtendimento.nome, "", (double)usuarioAtendimento.quantidade);
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
