<%@include file="include/beans.jsp"%>

<%@page import="java.io.*"%>
<%@page import="java.sql.*"%>
<%@page import="java.util.*"%>

<%!
  static public final String LOG_ENTRY_LIST          = "logEntryList";
  static public final String LOG_DATE                = "logDate";
  static public final String DEFAULT_CONNECTION_NAME = "defaultConnectionName";
  static public final String USER_NAME               = "userName";
%>

<%
  try {
    // pega a data que iremos utilizar
    Param paramLogDate = (Param)session.getAttribute(LOG_DATE);
    // pega a lista atual de entrada de logs da sessão
    LogEntryList logEntryList = (LogEntryList)session.getAttribute(LOG_ENTRY_LIST);

    // pega o nome da conexão e do usuário da requisição
    String defaultConnectionName = request.getParameter(DEFAULT_CONNECTION_NAME);
    String userName = request.getParameter(USER_NAME);

    // utilizamos o recurso de Conexão Default?
    boolean useDefaultConnection = facade.applicationDefaultConnection().getOnLogonTime();
%>

<html>
  <head>
    <title><%=facade.applicationInformation().getName()%> - Detalhe de Acessos</title>
    <link href="<%=facade.getStyle().userInterface()%>" rel="stylesheet" type="text/css">
    <link href="<%=facade.iconURL()%>" rel="shortcut icon" />
    <script src="include/scripts.js" type="text/javascript"></script>
  </head>

  <body style="margin:5px;" onselectstart="return false;" oncontextmenu="return false;">

    <table style="width:100%; height:100%; table-layout:fixed;">
      <tr>
        <td style="height:20px;">

          <table style="width:100%; height:100%; border:1px solid;">
            <%if (useDefaultConnection) {%>
            <tr>
              <td nowrap="nowrap"><b><%=facade.applicationDefaultConnection().getCaption()%>:</b></td>
              <td><%=defaultConnectionName%></td>
            </tr>
            <%} // if%>
            <tr>
              <td style="width:100px;" nowrap="nowrap"><b>Usuário:</b></td>
              <td><%=userName%></td>
            </tr>
          </table>

        </td>
      </tr>
      <tr>
        <td style="height:auto;">

          <%
            // nosso Grid
            Grid gridLog = new Grid(facade, "gridLog", 0, 0);
            gridLog.columns().add(new Grid.Column("Data/Hora", 120));
            gridLog.columns().add(new Grid.Column("Evento", 120));
            gridLog.columns().add(new Grid.Column("Mensagem", 500));
            // pega a lista de entradas ordenada por Usuário
            LogEntry[] logEntries = logEntryList.getLogEntryList(LogEntryList.ORDER_BY_ORIGINAL_DATE_TIME);
          %>
          <%=gridLog.begin()%>
            <%// nosso contador
              int exceptionCount = 0;
              int entriesCount   = 0;
              // loop nas entradas
              for (int i=0; i<logEntries.length; i++) {
                // entrada da vez
                LogEntry logEntry = logEntries[i];
                // se não é o usuário desejado...continua
                if (!logEntry.getDefaultConnectionName().equals(defaultConnectionName) ||
                    !logEntry.getUserName().equals(userName))
                  continue;
                // incrementa os contadores
                if (logEntry.getEvent() == LogEntry.EVENT_EXCEPTION)
                  exceptionCount++;
                entriesCount++;
                %>
                <%=gridLog.addRow(new String[]{DateTools.formatDateTime(logEntry.getOriginalDateTime()),
                                               (
                                                 logEntry.getEvent() == LogEntry.EVENT_LOGON ? "<img alt='' src='" + ImageList.COMMAND_NEXT + "' align='absmiddle'/> Logon"
                                                 : logEntry.getEvent() == LogEntry.EVENT_EXCEPTION ? "<img alt='' src='" + ImageList.COMMAND_DELETE + "' align='absmiddle'/> Exceção"
                                                 : "<img alt='' src='" + ImageList.COMMAND_BACK + "' align='absmiddle'/> Logoff"
                                               ),
                                               logEntry.getMessage().replace('"', '\'')})%>
              <%} // for%>
          <%=gridLog.end(exceptionCount + " exceções em " + entriesCount + " entradas")%>

        </td>
      </tr>
      <tr>
        <td align="right" style="height:30px;">
          <%=Button.script(facade, "buttonClose", "Fechar", "", "", "", Button.KIND_DEFAULT, "", "window.close();", false)%>
        </td>
      </tr>
    </table>

  </body>
</html>

<%}
  catch (Exception e) {
    Controller.forwardException(e, pageContext);
  } // try-catch
%>
