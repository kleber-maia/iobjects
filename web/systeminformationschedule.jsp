<%@include file="include/beans.jsp"%>

<%@page import="java.io.*"%>
<%@page import="java.sql.*"%>
<%@page import="java.util.*"%>

<%@page import="iobjects.schedule.*"%>

<%!
  String COMMAND_RELOAD_TASKS = "reloadTasks";
  String TASK_NAME            = "taskName";
%>

<%
  try {
    // agendador da aplicação
    Schedule schedule = facade.schedule();
    // nome da tarefa para executar
    String taskName = request.getParameter(TASK_NAME);
    if (taskName == null)
      taskName = "";
    // comando recarregar tarefas
    boolean reloadTasks = request.getParameter(COMMAND_RELOAD_TASKS) != null;

    // se quer executar uma tarefa...
    if (!taskName.equals("")) {
      // localiza a tarefa
      Task task = schedule.taskList().get(taskName);
      // executa...
      task.run();
    }
    // se quer recarregar as tarefas...
    else if (reloadTasks) {
      // recarrega
      schedule.loadTaskFiles();
    } // if
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
      <%// se executamos uma tarefa...avisa
        if (!taskName.equals("")) {%>
        alert("A tarefa '<%=taskName%>' foi executada em segundo plano.");
      <%} // if%>

      <%// se recarregas as tarefas...avisa
        if (reloadTasks) {%>
        alert("As tarefas e seus agendamentos foram recarregados.");
      <%} // if%>

      function reload() {
        // se não confirmou...dispara
        if (!confirm("Deseja mesmo recarregar as tarefas e seus agendamentos?"))
          return;
        // chama a execução
        window.location.href = "<%=Controller.ACTION_SYSTEM_INFORMATION_SCHEDULE.url(COMMAND_RELOAD_TASKS + "=true")%>";
      }

      function runTask(taskName) {
        // se não confirmou...dispara
        if (!confirm("Deseja mesmo que a tarefa '" + taskName + "' seja executada agora?"))
          return;
        // chama a execução
        window.location.href = "<%=Controller.ACTION_SYSTEM_INFORMATION_SCHEDULE.url(TASK_NAME + "=")%>" + taskName;
      }
    </script>


    <table cellpadding="0" cellspacing="0" style="width:100%; height:100%;">
      <tr style="width:auto;">
        <td>
          <%
            Grid gridVariables = new Grid(facade, "gridVariables", 0, 0);
            gridVariables.columns().add(new Grid.Column("Propriedade", 250));
            gridVariables.columns().add(new Grid.Column("Valor", 500));
          %>
          <%=gridVariables.begin()%>
            <!-- Task list -->
            <%=gridVariables.addRow(new String[]{"<b>Task list</b>", ""})%>
                  <%// se não temos tarefas...
                    if (schedule.taskList().size() == 0) {%>
                    <%=gridVariables.addRow(new String[]{"&nbsp;&nbsp;&nbsp;Nenhuma tarefa agendada.", ""})%>
                  <%} // if%>
                  <%// loop nas tarefas agendadas
                    for (int i=0; i<schedule.taskList().size(); i++) {
                      // tarefa da vez
                      Task task = schedule.taskList().get(i);%>
                      <!-- Task -->
                      <%=gridVariables.addRow(new String[]{"&nbsp;&nbsp;&nbsp;<b>" + task.getName() + "</b>", "<a href=javascript:runTask('" + task.getName() + "')>Executar agora...</a>"})%>
                        <!-- First Time -->
                        <%=gridVariables.addRow(new String[]{"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;First time", DateTools.formatDateTime(task.getFirstTime())})%>
                        <!-- Period -->
                        <%=gridVariables.addRow(new String[]{"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Period", task.getPeriod() >= Schedule.ONE_DAY ? (task.getPeriod() / Schedule.ONE_DAY) + " Day(s)" : (task.getPeriod() / Schedule.ONE_HOUR) + " Hour(s)"})%>
                        <!-- Scheduled -->
                        <%=gridVariables.addRow(new String[]{"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Scheduled", task.getScheduled() + ""})%>
                  <%} // for%>
          <%=gridVariables.end()%>
        </td>
      </tr>
      <tr style="height:28px;">
        <td valign="bottom">
          <%=Button.script(facade, "buttonReload", "Recarregar", "Recarrega as tarefas e seus agendamentos...", "", "", Button.KIND_DEFAULT, "", "reload();", false)%>&nbsp;
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
