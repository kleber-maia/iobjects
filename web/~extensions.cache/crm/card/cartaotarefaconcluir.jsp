
<%@include file="../include/beans.jsp"%>

<%@page import="imanager.card.*"%>
<%@page import="imanager.entity.*"%>
<%@page import="imanager.misc.*"%>

<%!
  public String formatPrazo(Timestamp prazo, java.util.Date today) throws Exception {
    if (prazo.before(today))
      return "<font style=color:red>" + DateTools.formatDateRelative(prazo).toUpperCase() + "</font>";
    else
      return DateTools.formatDateRelative(prazo).toUpperCase();
  }
%>

<%
  // início do bloco protegido
  try {
    // nossa instância de Tarefa Concluir
    CartaoTarefaConcluir cartaoTarefaConcluir = (CartaoTarefaConcluir)facade.getCard(CartaoTarefaConcluir.CLASS_NAME);
    // data de hoje
    java.util.Date today = DateTools.getActualDate();
%>

<html>
  <head>
    <title><%=CartaoTarefaConcluir.ACTION.getCaption()%></title>
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
        <td style="width:100px;" colspan="5"><b>Minhas tarefas</b></td>
      </tr>
      <tr>
        <td style="width:35px;"><b>ID</b></td>
        <td style="width:90px;"><b>Prazo</b></td>
        <td style="width:auto;"><b>Cliente</b></td>
        <td style="width:140px;"><b>Status</b></td>
        <td></td>
      </tr>
    <%// tarefas recentes
      TarefaInfo[] tarefaInfoList = cartaoTarefaConcluir.getTarefaConcluir(selectedEmpresaId, facade.getLoggedUser().getId());
      // loop nos registros
      for (int i=0; i<tarefaInfoList.length; i++) {
        TarefaInfo tarefaInfo = tarefaInfoList[i];%>
      <tr>
        <td>
          <%// se não é um dispositivo móvel...
            if (!facade.getBrowserMobile()) {%>
              <a href="<%=Tarefa.ACTION_CADASTRO.url(Tarefa.COMMAND_EDIT, Tarefa.FIELD_EMPRESA_ID.getFieldAlias() + "=" + selectedEmpresaId + "&" + Tarefa.FIELD_TAREFA_ID.getFieldAlias() + "=" + tarefaInfo.getTarefaId())%>" title="Ir para Tarefa..." target="frameContent"><%=tarefaInfo.getTarefaId()%></a>
          <%}
            else {%>
              <%=tarefaInfo.getTarefaId()%>
          <%}%>
        </td>
        <td><%=formatPrazo(tarefaInfo.getPrazo(), today)%></td>
        <td>
          <%// se não é um dispositivo móvel...
            if (facade.getBrowserMobile()) {%>              
              <a href="javascript:void(0);" onclick="anotacoes('<%="anotacoesTarefaId" + tarefaInfo.getTarefaId()%>')"><%=tarefaInfo.lookupValueList().get(Tarefa.LOOKUP_CLIENTE).getDisplayFieldValuesToString()%></a>
          <%}
            else {%>                  
              <%=tarefaInfo.lookupValueList().get(Tarefa.LOOKUP_CLIENTE).getDisplayFieldValuesToString()%>
          <%} // if%>
        </td>
        <td><%=StatusTarefa.LOOKUP_LIST_FOR_FIELD[tarefaInfo.getStatus()]%></td>
        <td></td>
        <%if (facade.getBrowserMobile()) {%>
            <tr>
              <td></td>
              <td colspan="4"><div  id="<%="anotacoesTarefaId" + tarefaInfo.getTarefaId()%>" style="display:none"><%=tarefaInfo.getDescricao()%></div></td>
            </tr>
        <%} // if%>        
      </tr>
    <%} // for%>
    <%// nosso ResultSet
    ResultSet resultSetCartaoTarefaConcluir = cartaoTarefaConcluir.getResultSetTarefaConcluir(selectedEmpresaId, facade.getLoggedUser().getId());
    try {%>
      <tr style="height:25px" valign="bottom">
        <td colspan="5"><b>Tarefas delegadas</b></td>
      </tr>
      <tr>
         <td><b>ID</b></td>
         <td><b>Prazo</b></td>
         <td><b>Cliente</b></td>
         <td><b>Usuário Responsável</b></td>
         <td><b>Status</b></td>
      </tr>
    <%while (resultSetCartaoTarefaConcluir.next()) {%>
      <tr>
      <td><a href="<%=Tarefa.ACTION_CADASTRO.url(Tarefa.COMMAND_EDIT, Tarefa.FIELD_EMPRESA_ID.getFieldAlias() + "=" + selectedEmpresaId + "&" + Tarefa.FIELD_TAREFA_ID.getFieldAlias() + "=" + resultSetCartaoTarefaConcluir.getInt(2))%>" title="Ir para Tarefa..." target="frameContent"><%=resultSetCartaoTarefaConcluir.getInt(2)%></a></td>
        <%=(resultSetCartaoTarefaConcluir.getTimestamp(3).before(DateTools.getActualDate()) ? "<td> <span style=\"color:red\">"  + DateTools.formatDate(resultSetCartaoTarefaConcluir.getTimestamp(3)) + "</td> </span>" : "<td>" + DateTools.formatDate(resultSetCartaoTarefaConcluir.getTimestamp(3)) + "</td>")%>
        <td>
          <%// se não é um dispositivo móvel...
            if (facade.getBrowserMobile()) {%>              
              <a href="javascript:void(0);" onclick="anotacoes('<%="anotacoesTarefaId" + resultSetCartaoTarefaConcluir.getString(2)%>')"><%=resultSetCartaoTarefaConcluir.getString(1)%></a>
          <%}
            else {%>                  
              <%=resultSetCartaoTarefaConcluir.getString(1)%>
          <%} // if%>          
        </td>
        <td><%=resultSetCartaoTarefaConcluir.getString(5)%></td>
        <td><%=StatusTarefa.LOOKUP_LIST_FOR_FIELD[resultSetCartaoTarefaConcluir.getInt(4)]%></td>
        <%if (facade.getBrowserMobile()) {%>
            <tr>
              <td></td>
              <td colspan="4"><div  id="<%="anotacoesTarefaId" + resultSetCartaoTarefaConcluir.getString(2)%>" style="display:none"><%=resultSetCartaoTarefaConcluir.getString(6)%></div></td>
            </tr>
        <%} // if%>                
      </tr>
      <%} // while%>
    </table>
    <%}
      finally {
        // libera recursos
        resultSetCartaoTarefaConcluir.getStatement().close();
        resultSetCartaoTarefaConcluir.close();
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
