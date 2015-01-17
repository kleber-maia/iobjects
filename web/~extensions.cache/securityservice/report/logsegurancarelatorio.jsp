
<%@include file="../include/beans.jsp"%>

<%@page import="securityservice.report.*"%>
<%@page import="securityservice.entity.*"%>
<%@page import="securityservice.misc.*"%>

<%!
  public class EventListenerLogSeguranca implements ReportGrid.EventListener {
    ResultSet resultSet = null;
    public String onAddCell(String columnName,
                            String value) throws Exception {
      return value;
    }
    public void onRecord(ResultSet resultSet) throws Exception {
      // guarda o ResultSet
      this.resultSet = resultSet;
    }
    public double onTotalizeColumn(String columnName,
                                   double total) throws Exception {
      return total;
    }
  }
%>

<%
  // início do bloco protegido
  try {
    // nossa instância de LogSeguranca
    LogSeguranca logseguranca = (LogSeguranca)facade.getReport(LogSeguranca.CLASS_NAME);
    // nossa instância de Usuário
    Usuario usuario = (Usuario)facade.getEntity(Usuario.CLASS_NAME);

    // define os valores dos parâmetros de usuário
    logseguranca.userParamList().setParamsValues(request);

    // proucura pelo usuário
    UsuarioInfo usuarioInfo = null;
    if (logseguranca.USER_PARAM_USUARIO.getIntValue() > 0)
      usuarioInfo = usuario.selectByUsuarioId(logseguranca.USER_PARAM_USUARIO.getIntValue());

    // nosso Grid
    ReportGridEx reportGridLogSeguranca = new ReportGridEx("gridLogSeguranca", true);
    reportGridLogSeguranca.addColumn("Data/Hora", 15, ReportGrid.ALIGN_LEFT);
    reportGridLogSeguranca.addColumn("Usuário", 15, ReportGrid.ALIGN_LEFT);
    reportGridLogSeguranca.addColumn("Operação", 30, ReportGrid.ALIGN_LEFT);
    reportGridLogSeguranca.addColumn("Objeto", 40, ReportGrid.ALIGN_LEFT);
%>

<html>
  <head>
    <title><%=LogSeguranca.ACTION.getCaption()%></title>
    <link href="<%=facade.getStyle().reportInterface()%>" rel="stylesheet" type="text/css">
    <script src="include/scripts.js" type="text/javascript"></script>
  </head>
  <body style="margin:5px;" oncontextmenu="return false;">

    <!-- Cabeçalho -->
    <%=ReportHeader.script(facade, LogSeguranca.ACTION, true, true, true, true, true)%>

    <!-- Parâmetros -->
    <p>
      <table>
        <tr>
          <td><%=ReportParamLabel.script(facade, logseguranca.USER_PARAM_DATA)%></td>
          <td><%=ReportParamText.script(facade, logseguranca.USER_PARAM_DATA)%></td>
        </tr>
        <tr>
          <td><%=ReportParamLabel.script(facade, logseguranca.USER_PARAM_USUARIO)%></td>
          <td><%=usuarioInfo != null ? usuarioInfo.getNome() : ""%></td>
        </tr>
        <tr>
          <td><%=ReportParamLabel.script(facade, logseguranca.USER_PARAM_OPERACAO)%></td>
          <td><%=ReportParamText.script(facade, logseguranca.USER_PARAM_OPERACAO)%></td>
        </tr>
      </table>
    </p>

    <!-- Título de dados -->
    <%=ReportTitle.script("Dados", ReportTitle.LEVEL_1)%>

    <!-- Grid de dados -->
    <%=reportGridLogSeguranca.begin()%>
      <%// totalizador
        int totalRegistros = 0;
        // nosso resultSet
        ResultSet resultSetLogSeguranca = logseguranca.getResultSetLogSeguranca(logseguranca.USER_PARAM_DATA.getDateValue(), logseguranca.USER_PARAM_USUARIO.getIntValue(), logseguranca.USER_PARAM_OPERACAO.getIntValue());
        try {
          // loop nos registros
          while (resultSetLogSeguranca.next()) {
            // acumula os registros
            totalRegistros++;%>
       <%=reportGridLogSeguranca.beginRow()%>
          <%=reportGridLogSeguranca.beginCell()%> <%=DateTools.formatDateTime(resultSetLogSeguranca.getTimestamp("dt_operacao"))%> <%=reportGridLogSeguranca.endCell()%>
          <%=reportGridLogSeguranca.beginCell()%> <%=usuario.selectByUsuarioId(resultSetLogSeguranca.getInt("in_usuario_id")).getNome()%> <%=reportGridLogSeguranca.endCell()%>
          <%=reportGridLogSeguranca.beginCell()%> <%=TipoOperacaoLog.LOOKUP_LIST_FOR_PARAM[resultSetLogSeguranca.getInt("sm_operacao")]%> <%=reportGridLogSeguranca.endCell()%>
          <%=reportGridLogSeguranca.beginCell()%> <%=resultSetLogSeguranca.getString("va_objeto")%> <%=reportGridLogSeguranca.endCell()%>
       <%=reportGridLogSeguranca.endRow()%>
       <%=reportGridLogSeguranca.beginRow()%>
          <%=reportGridLogSeguranca.beginCell(4)%> <%=resultSetLogSeguranca.getString("va_log")%> <%=reportGridLogSeguranca.endCell()%>
      <%=reportGridLogSeguranca.endRow()%>
      <%  } // while %>
      <%} // try
        finally {
          // libera recursos
          resultSetLogSeguranca.getStatement().close();
          resultSetLogSeguranca.close();
        } // try-finally%>
     <%=reportGridLogSeguranca.beginRow(true)%>
       <%=reportGridLogSeguranca.beginCell(4)%> <%=NumberTools.format(totalRegistros)%> registros <%=reportGridLogSeguranca.endCell()%>
     <%=reportGridLogSeguranca.endRow()%>
   <%=reportGridLogSeguranca.end()%>

    <!-- Rodapé -->
    <%=ReportFooter.script(facade, true, true)%>


  </body>
</html>

<%}
  // término do bloco protegido
  catch (Exception e) {
    Controller.forwardException(e, pageContext);
  } // try-catch
%>
