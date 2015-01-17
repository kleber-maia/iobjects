<%@include file="include/beans.jsp"%>

<%@page import="java.io.*"%>
<%@page import="java.sql.*"%>
<%@page import="java.util.*"%>

<%!
  static public final String LOG_ENTRY_LIST          = "logEntryList";
  static public final String LOG_DATE                = "logDate";
  static public final String DEFAULT_CONNECTION_NAME = "defaultConnectionName";
  static public final String USER_NAME               = "userName";

  static public final Command COMMAND_CHANGE_DATE = new Command("changeDate", "", "Atualizar");
%>

<%
  try {
    // pega a data que iremos utilizar
    Param paramLogDate = (Param)session.getAttribute(LOG_DATE);
    // se ainda não temos uma...
    if (paramLogDate == null) {
      // cria
      paramLogDate = new Param(LOG_DATE, "Data", "", DateTools.formatDate(new java.util.Date()), 10, Param.ALIGN_LEFT, Param.FORMAT_DATE);
      // guarda na sessão
      session.setAttribute(LOG_DATE, paramLogDate);
    }
    // se temos...
    else {
      // pega seu valor
      paramLogDate.setValue(request);
    } // if


    // pega a lista atual de entrada de logs da sessão
    LogEntryList logEntryList = (LogEntryList)session.getAttribute(LOG_ENTRY_LIST);
    // se ainda não temos um ou mudamos a data...
    if ((logEntryList == null) || (Controller.getCommand(request).equals(COMMAND_CHANGE_DATE.getName()))) {
      // cria obtendo o log da data de hoje
      logEntryList = new LogEntryList(facade.logLocalPath(), paramLogDate.getDateValue());
      // guarda na sessão
      session.setAttribute(LOG_ENTRY_LIST, logEntryList);
    } // if

    // utilizamos o recurso de Conexão Default?
    boolean useDefaultConnection = facade.applicationDefaultConnection().getOnLogonTime();

    // nosso Form
    Form form = new Form(request, "form", Controller.ACTION_SYSTEM_INFORMATION_LOG, COMMAND_CHANGE_DATE, "", true);
%>

<html>
  <head>
    <title><%=facade.applicationInformation().getName()%> - Informações do Sistema</title>
    <link href="<%=facade.getStyle().userInterface()%>" rel="stylesheet" type="text/css">
    <link href="<%=facade.iconURL()%>" rel="shortcut icon" />
    <script src="include/scripts.js" type="text/javascript"></script>
  </head>

  <body style="margin:0px;" onselectstart="return false;" oncontextmenu="return false;">

    <script type="text/javascript">

      function showDetail(defaultConnectionName, userName) {
        <%=Popup.script(Controller.ACTION_SYSTEM_INFORMATION_LOG_DETAIL.url(DEFAULT_CONNECTION_NAME + "=' + defaultConnectionName + '&" + USER_NAME + "=' + userName + '"), "logDetail", 600, 800, Popup.POSITION_CENTER)%>
      }

    </script>

    <%=form.begin()%>
      <!-- barra de ferramentas e grid -->
      <table style="width:100%; height:100%;">
        <!-- Barra de Ferramentas -->
        <tr style="height:20px;">
          <td>

            <!-- Barra de Ferramentas -->
            <table style="width:100%; height:100%; border:1px solid;">
              <tr>
                <td style="width:50%;">Resumo de Acessos</td>
                <td style="width:50%;" align="right">
                  <table cellpadding="0" cellspacing="0">
                    <tr>
                      <td>Data:&nbsp;</td>
                      <td><%=ParamFormEdit.script(facade, paramLogDate, 65, "", "", false)%></td>
                      <td><%=Button.script(facade, "buttonChangeDate", "", COMMAND_CHANGE_DATE.getDescription(), ImageList.COMMAND_SEARCH, "", Button.KIND_DEFAULT, "width:22px; height:20px;", form.submitScript(), false)%></td>
                    </tr>
                  </table>
                </td>
              </tr>
            </table>

          </td>
        </tr>
        <!-- Grid -->
        <tr style="height:auto;">
          <td>

            <%
              // nosso totalizador
              int defaultConnectionCount = 0;
              int userCount              = 0;
              // nosso Grid
              Grid gridLog = new Grid(facade, "gridLog", 0, 0);
              gridLog.columns().add(new Grid.Column(facade.applicationDefaultConnection().getCaption(), (useDefaultConnection ? 250 : 0)));
              gridLog.columns().add(new Grid.Column("Usuário", 250));
              // pega a lista de entradas ordenada por Usuário
              LogEntry[] logEntries = logEntryList.getLogEntryList(LogEntryList.ORDER_BY_USER_NAME);
            %>
            <%=gridLog.begin()%>
              <%
                String defaultConnectionName = "?";
                String userName              = "?";
                for (int i=0; i<logEntries.length; i++) {
                  // entrada da vez
                  LogEntry logEntry = logEntries[i];
                  // se não é referente a um usuário...continua
                  if (logEntry.getUserName().startsWith("~loggedoff"))
                    continue;
                  // se é o mesmo usuário...continua
                  if (defaultConnectionName.equals(logEntry.getDefaultConnectionName()) &&
                      userName.equals(logEntry.getUserName()))
                    continue;
                  // se usamos conexão default...
                  if (useDefaultConnection) {
                   // se não é a mesma anterior...
                   if (!defaultConnectionName.equals(logEntry.getDefaultConnectionName()))
                    // incrementa o totalizador
                    defaultConnectionCount++;
                  } // if%>
                  <%=gridLog.addRow(new String[]{(!defaultConnectionName.equals(logEntry.getDefaultConnectionName()) ? (logEntry.getDefaultConnectionName().equals("") ? "[nenhum]" : logEntry.getDefaultConnectionName()) : ""),
                                                 (useDefaultConnection ? "&nbsp;&nbsp;&nbsp;" : "") + "<a href=javascript:showDetail(" + (useDefaultConnection ? "'" + logEntry.getDefaultConnectionName() + "'," : "'',") + "'" + logEntry.getUserName().replaceAll(" ", "%20") + "'" + ")>" + logEntry.getUserName() + "</a>"})%>
                <%// totalizador de usuários
                  userCount++;
                  // guarda o usuário da vez
                  defaultConnectionName = logEntry.getDefaultConnectionName();
                  userName = logEntry.getUserName();
                } // for%>
            <%=gridLog.end(userCount + " Usuários " + (useDefaultConnection ? " em " + defaultConnectionCount + " itens de " + facade.applicationDefaultConnection().getCaption() : ""))%>

          </td>
        </tr>
      </table>
    <%=form.end()%>

  </body>
</html>

<%}
  catch (Exception e) {
    Controller.forwardException(e, pageContext);
  } // try-catch
%>
