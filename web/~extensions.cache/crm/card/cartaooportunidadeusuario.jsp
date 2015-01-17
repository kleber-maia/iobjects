
<%@include file="../include/beans.jsp"%>

<%@page import="imanager.card.*"%>
<%@page import="imanager.entity.*"%>
<%@page import="imanager.misc.*"%>

<%
  class UsuarioOportunidade{
    String nome = "";
    double quantidade = 0.0D;
    public UsuarioOportunidade(String nome, double quantidade) {
      this.nome = nome;
      this.quantidade = quantidade;
    }
  }
  // início do bloco protegido
  try {
    // nossa instância de Atendimento UsuarioOportunidade
    CartaoOportunidadeUsuario cartaoOportunidadeUsuario = (CartaoOportunidadeUsuario)facade.getCard(CartaoOportunidadeUsuario.CLASS_NAME);
    // nosso gráfico
    Chart chart = new Chart(facade, "chart");
%>

<html>
  <head>
    <title><%=CartaoOportunidadeUsuario.ACTION.getCaption()%></title>
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
      ResultSet resultSetCartaoOportunidadeUsuario = cartaoOportunidadeUsuario.getResultSetOportunidadeUsuario(selectedEmpresaId);
      ParamList usuarioOportunidadeList = new ParamList();
      int usuarioOportunidadeId = 0;
      try {%>
        <div id="divChart" align="center" style="width:100%;"></div>
        <script type="text/javascript">
          var aHeight = (document.body.offsetHeight != null ? document.body.offsetHeight : 320);
          document.getElementById("divChart").style.height = aHeight;
        </script>
        <table style="width:100%;">
          <tr>
            <td style="width:35px;"><b>Oportunidade</b></td>
            <td align="center" style="width:140px;"><b>Acompanhamento</b></td>
            <td style="width:auto;"><b>Cliente</b></td>
            <td align="center" style="width:90px;"><b>Telefone</b></td>
          </tr>
        <%// loop nos registros
          while (resultSetCartaoOportunidadeUsuario.next()) {
            if (usuarioOportunidadeId != resultSetCartaoOportunidadeUsuario.getInt(1)) {
              %>
              <tr>
                <td colspan="4"><b><%=resultSetCartaoOportunidadeUsuario.getString(2)%></b></td> 
              </tr>
              <%
              usuarioOportunidadeId = resultSetCartaoOportunidadeUsuario.getInt(1);
            }
            // nosso index do usuarioOportunidade
            int index = usuarioOportunidadeList.indexOf(usuarioOportunidadeId + ""); 
            // se já possuimos o usuarioOportunidade na lista... incrementa quantidade.
            if (index < 0) {
              UsuarioOportunidade usuarioOportunidade = new UsuarioOportunidade(resultSetCartaoOportunidadeUsuario.getString(2).toUpperCase(), 1);
              usuarioOportunidadeList.add(new Param(usuarioOportunidadeId + "", usuarioOportunidade));
            }
            // se não...adiciona o usuarioOportunidade
            else {
              UsuarioOportunidade usuarioOportunidade = (UsuarioOportunidade)usuarioOportunidadeList.get(index).getObject();
              usuarioOportunidade.quantidade++;
            } // if
            String telefone = "";
            if (!resultSetCartaoOportunidadeUsuario.getString(5).trim().equals(""))
              telefone = resultSetCartaoOportunidadeUsuario.getString(5);
            else if (!resultSetCartaoOportunidadeUsuario.getString(6).trim().equals(""))
              telefone = resultSetCartaoOportunidadeUsuario.getString(6);
            else if (!resultSetCartaoOportunidadeUsuario.getString(7).trim().equals(""))
              telefone = resultSetCartaoOportunidadeUsuario.getString(7);%>
          <tr>
            <td>
              <%// se não é um dispositivo móvel...
                if (!facade.getBrowserMobile()) {%>
                  <a href="<%=Oportunidade.ACTION_CADASTRO.url(Oportunidade.COMMAND_EDIT, Oportunidade.FIELD_EMPRESA_ID.getFieldAlias() + "=" + selectedEmpresaId + "&" + Oportunidade.FIELD_OPORTUNIDADE_ID.getFieldAlias() + "=" + resultSetCartaoOportunidadeUsuario.getInt(3))%>" title="Ir para Oportunidade..." target="frameContent"><%=resultSetCartaoOportunidadeUsuario.getString(3)%></a>
              <%}
                else {%>
                  <%=resultSetCartaoOportunidadeUsuario.getString(3)%>
              <%} // if%>
            </td>
            <td  align="center"><%=DateTools.formatDate(resultSetCartaoOportunidadeUsuario.getTimestamp(8))%></td>
            <td>
              <%// se não é um dispositivo móvel...
                if (facade.getBrowserMobile()) {%>              
                  <a href="javascript:void(0);" onclick="anotacoes('<%="anotacoesOportunidadeId" + resultSetCartaoOportunidadeUsuario.getString(3)%>')"><%=resultSetCartaoOportunidadeUsuario.getString(4)%></a>
              <%}
                else {%>                  
                  <%=resultSetCartaoOportunidadeUsuario.getString(4)%>
              <%} // if%>              
            </td>
            <td  align="center"><%=StringTools.formatCustomMask(telefone, "(00) 0000-0000")%></td>
            <%if (facade.getBrowserMobile()) {%>
                <tr>
                  <td></td>
                  <td colspan="3"><div  id="<%="anotacoesOportunidadeId" + resultSetCartaoOportunidadeUsuario.getString(3)%>" style="display:none"><%=resultSetCartaoOportunidadeUsuario.getString(9)%></div></td>
                </tr>
            <%} // if%>            
          </tr>
        <%} // while%>
        </table>

    <%}
      finally {
        // libera recursos
        resultSetCartaoOportunidadeUsuario.getStatement().close();
        resultSetCartaoOportunidadeUsuario.close();
      } // try-finally

      // loop nos usuarioOportunidades
      for(int i = 0; i < usuarioOportunidadeList.size(); i++) {
        Param param = usuarioOportunidadeList.get(i); 
        UsuarioOportunidade usuarioOportunidade = (UsuarioOportunidade)param.getObject(); 
        chart.addValue(usuarioOportunidade.nome, "", (double)usuarioOportunidade.quantidade);
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
