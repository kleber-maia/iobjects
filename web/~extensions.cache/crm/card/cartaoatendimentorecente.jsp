
<%@include file="../include/beans.jsp"%>

<%@page import="imanager.card.*"%>
<%@page import="imanager.entity.*"%>
<%@page import="imanager.misc.*"%>

<%
  // início do bloco protegido
  try {
    // nossa instância de Atendimento Recente
    CartaoAtendimentoRecente cartaoAtendimentoRecente = (CartaoAtendimentoRecente)facade.getCard(CartaoAtendimentoRecente.CLASS_NAME);
%>

<html>
  <head>
    <title><%=CartaoAtendimentoRecente.ACTION.getCaption()%></title>
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

    <table style="width:100%;">
      <tr>
        <td style="width:35px;"><b>ID</b></td>
        <td style="width:105px;"><b>Início</b></td>
        <td style="width:auto;"><b>Cliente</b></td>
      </tr>
    <%// atendimentos recentes
      AtendimentoInfo[] atendimentoInfoList = cartaoAtendimentoRecente.getAtendimentoRecente(selectedEmpresaId, facade.getLoggedUser().getId());
      // loop nos registros
      for (int i=0; i<atendimentoInfoList.length; i++) {
        AtendimentoInfo atendimentoInfo = atendimentoInfoList[i];%>
      <tr>
        <td>
          <%// se não é um dispositivo móvel...
            if (!facade.getBrowserMobile()) {%>
              <a href="<%=Atendimento.ACTION_CADASTRO.url(Atendimento.COMMAND_EDIT, Atendimento.FIELD_EMPRESA_ID.getFieldAlias() + "=" + selectedEmpresaId + "&" + Atendimento.FIELD_ATENDIMENTO_ID.getFieldAlias() + "=" + atendimentoInfo.getAtendimentoId())%>" title="Ir para Atendimento..." target="frameContent"><%=atendimentoInfo.getAtendimentoId()%></a>
          <%}
            else {%>
              <%=atendimentoInfo.getAtendimentoId()%>
          <%}%>
        </td>
        <td><%=DateTools.formatDateTime(atendimentoInfo.getDataHoraInicio())%></td>
        <td>
          <%// se não é um dispositivo móvel...
            if (facade.getBrowserMobile()) {%>              
              <a href="javascript:void(0);" onclick="anotacoes('<%="anotacoesAtendimentoId" + atendimentoInfo.getAtendimentoId()%>')"><%=atendimentoInfo.lookupValueList().get(Atendimento.LOOKUP_CLIENTE).getDisplayFieldValuesToString()%></a>
          <%}
            else {%>                  
              <%=atendimentoInfo.lookupValueList().get(Atendimento.LOOKUP_CLIENTE).getDisplayFieldValuesToString()%>
          <%} // if%>
        </td>
        <%if (facade.getBrowserMobile()) {%>
            <tr>
              <td></td>
              <td colspan="3"><div  id="<%="anotacoesAtendimentoId" + atendimentoInfo.getAtendimentoId()%>" style="display:none"><%=atendimentoInfo.getDescricao()%></div></td>
            </tr> 
        <%} // if%>              
      </tr>
    <%} // while%>
    </table>

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
