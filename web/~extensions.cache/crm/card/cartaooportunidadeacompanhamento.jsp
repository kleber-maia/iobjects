
<%@include file="../include/beans.jsp"%>

<%@page import="imanager.card.*"%>
<%@page import="imanager.entity.*"%>
<%@page import="imanager.misc.*"%>

<%!
  public String formatDataAcompanhamento(Timestamp dataAcompanhamento, java.util.Date today) throws Exception {
    if (dataAcompanhamento.before(today))
      return "<font style=color:red>" + DateTools.formatDateRelative(dataAcompanhamento).toUpperCase() + "</font>";
    else
      return DateTools.formatDateRelative(dataAcompanhamento).toUpperCase();
  }
%>

<%
  // início do bloco protegido
  try {
    // nossa instância de Oportunidade Acompanhamento
    CartaoOportunidadeAcompanhamento cartaoOportunidadeAcompanhamento = (CartaoOportunidadeAcompanhamento)facade.getCard(CartaoOportunidadeAcompanhamento.CLASS_NAME);

    // data de hoje
    java.util.Date today = DateTools.getActualDate();
%>

<html>
  <head>
    <title><%=CartaoOportunidadeAcompanhamento.ACTION.getCaption()%></title>
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
      ResultSet resultSetOportunidadeAcompanhamento = cartaoOportunidadeAcompanhamento.getResultSetOportunidadeAcompanhamento(selectedEmpresaId);
      try {%>

        <table style="width:100%;">
          <tr>
            <td style="width:35px;"><b>Oportunidade</b></td>
            <td align="center" style="width:140px;"><b>Acompanhamento</b></td>
            <td style="width:auto;"><b>Cliente</b></td>
            <td align="center" style="width:90px;"><b>Telefone</b></td>
          </tr>
        <%String usuario = "";
          // loop nos registros
          while (resultSetOportunidadeAcompanhamento.next()) {
            // se mudou de usuário...
            if (!usuario.equals(resultSetOportunidadeAcompanhamento.getString(3))) {
              usuario = resultSetOportunidadeAcompanhamento.getString(3);%>
              <tr>
                <td colspan="4"><b><%=usuario%></b></td>
              </tr>
          <%} // if
            String telefone = "";
            if (!resultSetOportunidadeAcompanhamento.getString(5).trim().equals(""))
              telefone = resultSetOportunidadeAcompanhamento.getString(5);
            else if (!resultSetOportunidadeAcompanhamento.getString(6).trim().equals(""))
              telefone = resultSetOportunidadeAcompanhamento.getString(6);
            else if (!resultSetOportunidadeAcompanhamento.getString(7).trim().equals(""))
              telefone = resultSetOportunidadeAcompanhamento.getString(7);%>
          <tr>
            <td>
              <%// se não é um dispositivo móvel...
                if (!facade.getBrowserMobile()) {%>
                  <a href="<%=Oportunidade.ACTION_CADASTRO.url(Oportunidade.COMMAND_EDIT, Oportunidade.FIELD_EMPRESA_ID.getFieldAlias() + "=" + selectedEmpresaId + "&" + Oportunidade.FIELD_OPORTUNIDADE_ID.getFieldAlias() + "=" + resultSetOportunidadeAcompanhamento.getInt(1))%>" title="Ir para Oportunidade..." target="frameContent"><%=resultSetOportunidadeAcompanhamento.getInt(1)%></a>
              <%}
                else {%>
                  <%=resultSetOportunidadeAcompanhamento.getInt(1)%>
            <%} // if%>
            </td>
            <td align="center"><%=formatDataAcompanhamento(resultSetOportunidadeAcompanhamento.getTimestamp(2), today)%></td>
            <td>
              <%// se não é um dispositivo móvel...
                if (facade.getBrowserMobile()) {%>              
                  <a href="javascript:void(0);" onclick="anotacoes('<%="anotacoesOportunidadeId" + resultSetOportunidadeAcompanhamento.getString(1)%>')"><%=resultSetOportunidadeAcompanhamento.getString(4)%></a>
              <%}
                else {%>                  
                  <%=resultSetOportunidadeAcompanhamento.getString(4)%>
              <%} // if%>              
            </td>
            <td align="center"><%=StringTools.formatCustomMask(telefone, "(00) 0000-0000")%></td>
            <%if (facade.getBrowserMobile()) {%>
                <tr>
                  <td></td>
                  <td colspan="3"><div  id="<%="anotacoesOportunidadeId" + resultSetOportunidadeAcompanhamento.getString(1)%>" style="display:none"><%=resultSetOportunidadeAcompanhamento.getString(8)%></div></td>
                </tr>
            <%} // if%>            
          </tr>
        <%} // while%>
        </table>
    <%}
      finally {
        // libera recursos
        resultSetOportunidadeAcompanhamento.getStatement().close();
        resultSetOportunidadeAcompanhamento.close();
      } // try-finally
    %>

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
