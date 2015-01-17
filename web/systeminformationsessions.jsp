<%@include file="include/beans.jsp"%>

<%@page import="java.io.*"%>
<%@page import="java.sql.*"%>
<%@page import="java.util.*"%>

<%@page import="iobjects.graph.*"%>
<%@page import="iobjects.xml.sql.*"%>

<%!
  public String  LOG_ENTRY_LIST      = "logEntryList";
  public Command COMMAND_CHANGE_DATE = new Command("changeDate", "", "Atualizar");
  
  public void fillChart(Chart chart, LogEntryList logEntryList) {
    // obt�m as entradas de log ordenadas por data e hora
    LogEntry[] logEntryArray = logEntryList.getLogEntryList(LogEntryList.ORDER_BY_DATE_TIME);

    // vamos usar bastante o calend�rio
    Calendar calendar = Calendar.getInstance();

       // totalizadores hor�rios de exce��es e usu�rios conectados
    int[] exceptionSum = new int[24];
    int[] userSum      = new int[24];
    /*
    // totaliza as exce��es hora a hora
    for (int i=0; i<logEntryArray.length; i++) {
      // entrada da vez
      LogEntry logEntry = logEntryArray[i];
      // se n�o � uma exce��o...continua
      if (logEntry.getEvent() != LogEntry.EVENT_EXCEPTION)
        continue;
      // obt�m a hora da exce��o
      calendar.setTime(logEntry.getDateTime());
      int hour = calendar.get(Calendar.HOUR);
      // soma no totalizar
      exceptionSum[hour]++;
    } // for
    // adiciona a s�rie de valores de exce��es ao gr�fico
    chart.addSerie("Exce��es");
    for (int i=0; i<exceptionSum.length; i++) {
      chart.addValue(i + "h", exceptionSum[i]);
    } // for
    */
    
    // totaliza as conex�es
    int logonSum = 0;
    for (int i=0; i<logEntryArray.length; i++) {
      // entrada da vez
      LogEntry logEntry = logEntryArray[i];
      // se � uma exce��o...continua
      if (logEntry.getEvent() == LogEntry.EVENT_EXCEPTION)
        continue;
      // obt�m a hora da exce��o
      calendar.setTime(logEntry.getDateTime());
      int hour = calendar.get(Calendar.HOUR_OF_DAY);
      // soma ou subtrai no totalizador
      if (logEntry.getEvent() == LogEntry.EVENT_LOGON)
        logonSum++;
      else
        logonSum--;
      // p�e no array
      userSum[hour] = logonSum;
    } // for
    // gera a lista de valores de usu�rios conectados
    chart.addSerie("Acessos");
    for (int i=1; i<userSum.length; i++) {
      chart.addValue(i + "h", "", userSum[i]);
    } // for
  }
%>

<%
  try {
    // lista atual de entrada de logs da sess�o
    LogEntryList logEntryList = new LogEntryList(facade.logLocalPath(), DateTools.getActualDate());

    // lista de sess�es existentes e ordenadas
    HttpSession[] sessionList = SessionManager.getInstance().getSessionList();
    // utilizamos o recurso de Conex�o Default?
    boolean useDefaultConnection = facade.applicationDefaultConnection().getOnLogonTime();

    // nosso gr�fico
    Chart chartSessions = new Chart(facade, "chartSessions");
    // preenche o gr�fico
    fillChart(chartSessions, logEntryList);
%>

<html>
  <head>
    <title><%=facade.applicationInformation().getName()%> - Informa��es do Sistema</title>
    <link href="<%=facade.getStyle().userInterface()%>" rel="stylesheet" type="text/css">
    <link href="<%=facade.iconURL()%>" rel="shortcut icon" />
    <script src="include/scripts.js" type="text/javascript"></script>
  </head>

  <body style="margin:0px;" onselectstart="return false;" oncontextmenu="return false;">

    <!-- grid -->
    <table style="width:100%; height:100%;">
      <!-- Gr�fico -->
      <tr style="height:40%;">
        <td>

          <div id="divChart" style="border:1px solid; width:100%; height:100%;">Carregando dados...</div>

        </td>
      </tr>
      <!-- Grid -->
      <tr style="height:60%;">
        <td>

          <%
            // totalizadores
            int noUserCount = 0;
            // lista de nomes das conex�es default
            Vector defaultConnectionNameList = new Vector();
            // nosso Grid
            Grid gridSessions = new Grid(facade, "gridSessions", 0, 0);
            gridSessions.columns().add(new Grid.Column(facade.applicationDefaultConnection().getCaption(), (useDefaultConnection ? 150 : 0)));
            gridSessions.columns().add(new Grid.Column("Usu�rio", 150));
            gridSessions.columns().add(new Grid.Column("Cria��o", 100));
            gridSessions.columns().add(new Grid.Column("�ltimo acesso", 100));
            gridSessions.columns().add(new Grid.Column("Expira��o", 100));
            gridSessions.columns().add(new Grid.Column("Site", 150));
            gridSessions.columns().add(new Grid.Column("Host", 250));
            gridSessions.columns().add(new Grid.Column("Agente", 750));
          %>
          <%=gridSessions.begin()%>
            <%// loop nas sess�es
              for (int i=0; i<sessionList.length; i++) {
                // sess�o da vez
                HttpSession sessionItem = sessionList[i];
                // seus dados principais
                String defaultConnectionName = "";
                String userName              = "";
                String siteName              = "";
                String remoteAddress         = "";
                String remoteAgent           = "";
                // se j� foi invalidada...
                if (SessionManager.getInstance().getInvalidated(sessionItem))
                  // conex�o default e nome do usu�rio
                  userName = "<span style=\"color:#ff0000;\">INVALIDADA</span>";
                // se ainda � v�lida...
                else {
                  // Facade da sess�o
                  Facade facadeItem = SessionManager.getInstance().getFacade(sessionItem);
                  // se n�o temos Facade
                  if (facadeItem == null) {
                    // conex�o default e nome do usu�rio
                    defaultConnectionName = "";
                    userName = "<span style='color:#ff0000;'>SEM FACADE</span>";
                  }
                  // se temos Facade
                  else {
                    // site, endere�o remoto e agente
                    siteName = facadeItem.getDefaultSiteName();
                    remoteAddress = facadeItem.getRemoteAddress() + " (" + facadeItem.getRemoteHost() + ")";
                    remoteAgent   = facadeItem.getRemoteAgent();
                    // conex�o default e nome do usu�rio
                    defaultConnectionName = facadeItem.getDefaultConnectionName();
                    if (!defaultConnectionNameList.contains(defaultConnectionName))
                     defaultConnectionNameList.add(defaultConnectionName);
                    if (facadeItem.getLoggedUser() == null) {
                      userName = "<span style='color:#ff0000;'>SEM USU�RIO</span>";
                      noUserCount++;
                    }
                    else
                      userName = facadeItem.getLoggedUser().getName();
                    // arquivo de conex�o associado
                    ConnectionFile connectionFile = facadeItem.connectionManager().connectionFiles().get(defaultConnectionName);
                    if ((connectionFile != null) && connectionFile.readOnly())
                      defaultConnectionName = "<img src='images/warning16x16.png' title='Somente leitura.' align=absmiddle /> " + defaultConnectionName;
                  } // if
                } // if%>
              <%=gridSessions.addRow(new String[]{defaultConnectionName,
                                                  userName, 
                                                  DateTools.formatDateTime(new Timestamp(sessionItem.getCreationTime())),
                                                  DateTools.formatDateTime(new Timestamp(sessionItem.getLastAccessedTime())),
                                                  DateTools.formatDateTime(new Timestamp(sessionItem.getLastAccessedTime() + (sessionItem.getMaxInactiveInterval() * 1000))),
                                                  siteName,
                                                  remoteAddress,
                                                  remoteAgent})%>
<%--
                <%// se usamos o recurso Default Connection
                  if (useDefaultConnection) {%>
                  <%=gridSessions.beginCell()%> <%=defaultConnectionName%> <%=gridSessions.endCell()%>
                <%} // if %>
                <%=gridSessions.beginCell()%> <%=userName%> <%=gridSessions.endCell()%>
                <%=gridSessions.beginCell()%> <%=DateTools.formatDateTime(new Timestamp(sessionItem.getCreationTime()))%> <%=gridSessions.endCell()%>
                <%=gridSessions.beginCell()%> <%=DateTools.formatDateTime(new Timestamp(sessionItem.getLastAccessedTime()))%> <%=gridSessions.endCell()%>
                <%=gridSessions.beginCell()%> <%=DateTools.formatDateTime(new Timestamp(sessionItem.getLastAccessedTime() + (sessionItem.getMaxInactiveInterval() * 1000)))%> <%=gridSessions.endCell()%>
                <%=gridSessions.beginCell()%> <%=siteName%> <%=gridSessions.endCell()%>
                <%=gridSessions.beginCell()%> <%=remoteAddress%> <%=gridSessions.endCell()%>
                <%=gridSessions.beginCell()%> <%=remoteAgent%> <%=gridSessions.endCell()%>
--%>
            <%} // for%>
          <%=gridSessions.end(sessionList.length + " sess�es (" + noUserCount + " sem usu�rio)" + (useDefaultConnection ? " em " + defaultConnectionNameList.size() + " itens de " + facade.applicationDefaultConnection().getCaption() : ""))%>

        </td>
      </tr>
    </table>

    <%=chartSessions.script(Chart.TYPE_AREA, "divChart", "", "Hor�rio", "Acessos", Chart.INTERFACE_STYLE_USER_INTERFACE)%>

  </body>
</html>

<%}
  catch (Exception e) {
    Controller.forwardException(e, pageContext);
  } // try-catch
%>
