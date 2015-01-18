/*
The MIT License (MIT)

Copyright (c) 2008 Kleber Maia de Andrade

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/   
package iobjects.schedule;

import java.util.*;

import iobjects.*;
import iobjects.util.*;
import iobjects.xml.schedule.*;

/**
 * Representa a agenda da aplica��o, capaz de iniciar se��es de usu�rio e
 * executar tarefas agendadas.
 * <p>
 * Tarefas com agendamento di�rio ser�o executadas na hora definida, iniciando
 * no dia em que a aplica��o foi executada e se repetindo a cada 24 horas.
 * Se a aplica��o for iniciada ap�s a hora agendada para a execu��o da tarefa
 * ela somente ocorrer� no dia seguinte.
 * </p>
 * <p>
 * Tarefas com agendamento semanal ser�o executadas na hora e dias definidos,
 * iniciando no dia em que a aplica��o foi executada e se repetindo a cada
 * 7 dias. Se a aplica��o for iniciada ap�s a hora e dia agendados a execu��o
 * da tarefa somente ocorrer� no pr�ximo dia e hora do agendamento ou na pr�xima
 * semana, o que acontecer primeiro.
 * </p>
 * <p>
 * Tarefas com agendamento mensal ser�o executadas na hora e dias definidos,
 * iniciando no dia em que a aplica��o foi executada. Se a aplica��o foi iniciada
 * ap�s a hora e dia agendados a execu��o da tarefa somente ocorrer� no pr�ximo
 * dia e hora do agendamento ou no pr�ximo m�s, o que acontecer primeiro. A
 * tarefa n�o se repetir� a cada m�s, a menos que a aplica��o seja reiniciada
 * durante esse per�odo.
 * </p>
 * @since 2006
 */
public class Schedule {

  /**
   * Define a extens�o dos arquivos de configura��o de tarefas agendadas.
   */
  static public final String TASK_FILE_EXTENSION = ".task";

  static public final long ONE_HOUR = 1000 * 60 * 60;
  static public final long ONE_DAY  = ONE_HOUR * 24;

  static private Schedule  instance    = null;
  static private boolean   tasksLoaded = false;
  static private TaskList  taskList    = new TaskList();

  private TaskFiles taskFiles     = new TaskFiles();
  private Timer     timer         = new Timer();
  private String    tasksFilePath = "";

  private Schedule(String tasksFilePath) {
    try {
      // guarda o caminho dos arquivos de tarefas
      this.tasksFilePath = tasksFilePath;
      // carrega as tarefas agendadas
      loadTaskFiles();
    }
    catch (Exception e) {
      throw new RuntimeException(e);
    } // try-catch
  }

  /**
   * Libera recursos.
   * @throws Throwable
   */
  public void finalize() throws Throwable {
    taskFiles = null;
    timer     = null;
  }

  /**
   * Retorna a inst�ncia de Scheduller. Caso ainda n�o tenha sido instanciada,
   * chamar o m�todo getInstace(String tasksFilePath).
   * @return Schedule Retorna a inst�ncia de Scheduller.
   */
  static public Schedule getInstace() {
    return instance;
  }

  /**
   * Retorna a inst�ncia de Scheduller para ser usada pela aplica��o.
   * @param tasksFilePath String Caminho onde se encontram os arquivos
   *        de tarefas agendadas.
   * @return Scheduller Retorna a inst�ncia de Scheduller para ser usada pela
   *         aplica��o.
   */
  static public Schedule getInstace(String tasksFilePath) {
    if (instance == null)
      instance = new Schedule(tasksFilePath);
    return instance;
  }

  /**
   * Retorna a pr�xima data que ocorrer� o dia da semana indicado por 'day' e
   * define a hora como 'hour'.
   * @param day int Dia da semana que se deseja saber a pr�xima ocorr�ncia.
   * @param hour String Hora para ser definida na data.
   * @return Date Retorna a pr�xima data que ocorrer� o dia da semana indicado
   *              por 'day' e define a hora como 'hour'.
   */
  static private Date getNextDayOfWeek(int    day,
                                       String hour) {
    // nosso resultado
    Date result = new Date();
    // nosso calend�rio
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(result);
    // procura pelo dia desejado
    while (calendar.get(Calendar.DAY_OF_WEEK) != day)
      calendar.add(Calendar.DATE, 1);
    // constr�i a data com a hora informada
    result = DateTools.parseDateTime(DateTools.formatDate(calendar.getTime()) + " " + hour);
    // se j� passou da hora agendada...s� na pr�xima semana
    Date now = new Date();
    if (now.after(result))
      result = DateTools.getCalculatedDays(result, 7);
    // retorna
    return result;
  }

  /**
   * Carrega as tarefas agendadas. Caso as tarefas j� tenho sido carregadas,
   * cancela todos os agendamentos existentes.
   * @throws Exception Em caso de exce��o na tentativa de acesso aos arquivos
   *                   de tarefas agendadas.
   */
  public void loadTaskFiles() throws Exception {
    // se j� carregamos as tarefas...descarta os agendamentos atuais
    if (tasksLoaded) {
      // desfaz os agendamentos
      timer.cancel();
      timer = new Timer();
      // limpa a lista de tarefas agendadas atrav�s de arquivo
      // nesse caso as tarefas agendadas pelas classes ser�o mantidas
      // e adicionadas ao Timer mais a frente
      for (int i=taskList.size()-1; i>=0; i--)
        if (taskList.get(i).getClassName().equals(""))
          taskList.remove(i);
    } // if

    // pega a lista de arquivos de configura��o de conex�es
    String[] fileExtensions = {TASK_FILE_EXTENSION};
    String[] fileNames = FileTools.getFileNames(tasksFilePath, fileExtensions, false);
    // limpa as tarefas atuais
    taskFiles.clear();
    // lista de tarefas tempor�ria
    TaskList tempTaskList = new TaskList();
    // loop nos arquivos para carreg�-los
    for (int i=0; i<fileNames.length; i++) {
      // arquivo da vez
      String fileName = fileNames[i];
      // nome da tarefa
      String taskName = fileName.substring(0, fileName.indexOf('.'));
      // carrega o arquivo
      TaskFile taskFile = new TaskFile(tasksFilePath + fileName);

      // agenda sua execu��o hor�ria
      if (taskFile.schedule().getHourly().getActive()) {
        // data atual
        String today = DateTools.formatDate(DateTools.getActualDate());
        // loop nas horas
        for (int w=0; w<taskFile.schedule().getHourly().size(); w++) {
          // hor�rio agendado hoje
          String[] dateParts = DateTools.splitDate(new Date());
          Date firstTime = DateTools.parseDateTime(today + " " + NumberTools.completeZero(taskFile.schedule().getHourly().getHour(w), 2) + ":00");
          // cria agendamento
          tempTaskList.add(new Task(taskName, firstTime, ONE_DAY));
        } // for
      } // if
      
      // agenda sua execu��o di�ria
      if (taskFile.schedule().getDaily().getActive()) {
        // data atual
        Date now = new Date();
        // data agendada no dia de hoje
        Date firstTime = DateTools.parseDateTime(DateTools.formatDate(DateTools.getActualDate()) + " " + taskFile.schedule().getDaily().getHour());
        // se j� passou da hora agendada...s� amanh�
        if (now.after(firstTime))
          firstTime = DateTools.getCalculatedDays(firstTime, 1);
        // cria agendamento
        tempTaskList.add(new Task(taskName, firstTime, ONE_DAY));
      } // if

      // agenda sua execu��o semanal
      if (taskFile.schedule().getWeekly().getActive()) {
        // cria agendamento para o pr�ximo domingo
        if (taskFile.schedule().getWeekly().getSunday())
          tempTaskList.add(new Task(taskName, getNextDayOfWeek(Calendar.SUNDAY, taskFile.schedule().getWeekly().getHour()), ONE_DAY * 7));
        // cria agendamento para a pr�xima segunda
        if (taskFile.schedule().getWeekly().getMonday())
          tempTaskList.add(new Task(taskName, getNextDayOfWeek(Calendar.MONDAY, taskFile.schedule().getWeekly().getHour()), ONE_DAY * 7));
        // cria agendamento para a pr�xima ter�a
        if (taskFile.schedule().getWeekly().getTuesday())
          tempTaskList.add(new Task(taskName, getNextDayOfWeek(Calendar.TUESDAY, taskFile.schedule().getWeekly().getHour()), ONE_DAY * 7));
        // cria agendamento para a pr�xima quarta
        if (taskFile.schedule().getWeekly().getWednesday())
          tempTaskList.add(new Task(taskName, getNextDayOfWeek(Calendar.WEDNESDAY, taskFile.schedule().getWeekly().getHour()), ONE_DAY * 7));
        // cria agendamento para a pr�xima quinta
        if (taskFile.schedule().getWeekly().getThursday())
          tempTaskList.add(new Task(taskName, getNextDayOfWeek(Calendar.THURSDAY, taskFile.schedule().getWeekly().getHour()), ONE_DAY * 7));
        // cria agendamento para a pr�xima sexta
        if (taskFile.schedule().getWeekly().getFriday())
          tempTaskList.add(new Task(taskName, getNextDayOfWeek(Calendar.FRIDAY, taskFile.schedule().getWeekly().getHour()), ONE_DAY * 7));
        // cria agendamento para o pr�ximo s�bado
        if (taskFile.schedule().getWeekly().getSaturday())
          tempTaskList.add(new Task(taskName, getNextDayOfWeek(Calendar.SATURDAY, taskFile.schedule().getWeekly().getHour()), ONE_DAY * 7));
      } // if

      // agenda sua execu��o mensal
      if (taskFile.schedule().getMonthly().getActive()) {
        // data atual
        Date now = new Date();
        // loop nos dias
        for (int w=0; w<taskFile.schedule().getMonthly().size(); w++) {
          // dia agendado no m�s atual
          String[] dateParts = DateTools.splitDate(new Date());
          Date firstTime = DateTools.parseDateTime(taskFile.schedule().getMonthly().getDay(w) + "/" + dateParts[1] + "/" + dateParts[2] + " " + taskFile.schedule().getDaily().getHour());
          // se j� passou da hora agendada...s� m�s que vem
          if (now.after(firstTime))
            firstTime = DateTools.getCalculatedMonths(firstTime, 1);
          // cria agendamento sem recorr�ncia
          tempTaskList.add(new Task(taskName, firstTime));
        } // for
      } // if

      // adiciona na lista
      taskFiles.add(taskFile, taskName);
    } // for

    // p�e as tarefas para serem executadas
    for (int i=0; i<tempTaskList.size(); i++) {
      // tarefa da vez
      Task task = tempTaskList.get(i);
      // p�e na fila de execu��o
      scheduleTask(task);
    } // for

    // marca as tarefas como carregadas
    tasksLoaded = true;
  }

  /**
   * Executa a tarefa agendada configurada em 'taskName' e utiliza 'facade'
   * para obter as inst�ncias dos BusinessObject's necess�rios.
   * <p>
   * As tarefas ser�o executadas em um bloco protegido contra exce��es. O status
   * de execu��o, ou mensagem de exce��o, de cada tarefa ser� gravado no arquivo
   * de log. Este log ser� enviado por e-mail, caso esteja configurado no trabalho
   * agendado.
   * </p>
   * @param facade Facade Fachada.
   * @param taskName String Nome da tarefa agendada para ser executada.
   */
  public synchronized void runScheduledTask(Facade facade,
                                            String taskName) {
    // se a aplica��o ainda n�o est� pronta...dispara
    if (Facade.getApplicationStatus() != Facade.APPLICATION_STATUS_READY)
      return;
    // mensagens de log
    String message = "";
    // tivemos alguma exce��o?
    boolean hasException = false;
    // log de execu��o
    Vector runLog = new Vector();
    // identifica��o do Log
    String logId = "schedule_" + taskName;
    // tenta executar
    Task     task     = null;
    TaskFile taskFile = null;
    try {
      // pega a tarefa
      task = taskList.get(taskName);
      // se n�o encontramos...exce��o
      if (task == null)
        throw new ExtendedException(getClass().getName(), "runScheduledTask", "Tarefa n�o encontrada: " + taskName + ".");
      // se a tarefa representa uma simples classe...
      if (!task.getClassName().equals("")) {
        // adiciona no log
        message = "Executando: " + task.getClassName() + "...";
        runLog.add(DateTools.formatDateTimePrecision(new Date()) + " -> " + message);
        facade.log().write(logId, message);
        // instancia o BusinessObject
        BusinessObject businessObject = facade.getBusinessObject(task.getClassName());
        // se � realmente um Schedulable...
        if (businessObject instanceof Scheduleable) {
          // executa
          Scheduleable.RunStatus runStatus = ((Scheduleable)businessObject).runScheduledTask();
          // adiciona no log
          message = "Executando: Sucesso=" + (runStatus.getSucess() ? "SIM" : "N�O") + "; Mensagem=" + runStatus.getMessage();
          runLog.add(DateTools.formatDateTimePrecision(new Date()) + " -> " + message);
          facade.log().write(logId, message);
        }
        // se n�o � um Scheduleable...exce��o
        else {
          // exce��o
          throw new ExtendedException(getClass().getName(), "runScheduledTask", businessObject.getClass().getName() + " n�o implementa a interface " + Scheduleable.class.getName() + ".");
        } // if
      }
      // se a tarefa representa um arquivo de configura��es...
      else {
        // pega as configura��es da tarefa agendada
        taskFile = taskFiles.get(taskName);
        // se n�o achamos o arquivo de configura��es...exce��o
        if (taskFile == null)
          throw new ExtendedException(getClass().getName(), "runScheduledTask", "Tarefa n�o encontrada: " + taskName + ".");
        // loop nas op��es de autentica��o
        for (int i=0; i<taskFile.authentication().size(); i++) {
          try {
            // login da vez
            TaskFile.Login login = taskFile.authentication().item(i);
            // adiciona no log
            message = "Efetuando logon: DefaultConnectionName=" + login.getDefaultConnectionName() + "; UserName=" + login.getUserName() + "...";
            runLog.add(DateTools.formatDateTimePrecision(new Date()) + " -> " + message);
            facade.log().write(logId, message);
            // tenta efetuar logon com o usu�rio informado
            facade.logon(login.getDefaultConnectionName(),
                         login.getUserName(),
                         login.getPassword());
            // adiciona no log
            message = "Efetuando logon: OK.";
            runLog.add(DateTools.formatDateTimePrecision(new Date()) + " -> " + message);
            facade.log().write(logId, message);

            // loop nos objetos de neg�cio configurados
            for (int w=0; w<taskFile.businessObjects().size(); w++) {
              try {
                // BusinessObject da vez
                BusinessObject businessObject = facade.getBusinessObject(taskFile.businessObjects().item(w));
                // se � realmente um Schedulable...executa
                if (businessObject instanceof Scheduleable) {
                  // adiciona no log
                  message = "Executando: " + businessObject.getClass().getName() + "...";
                  runLog.add(DateTools.formatDateTimePrecision(new Date()) + " -> " + message);
                  facade.log().write(logId, message);
                  // executa
                  Scheduleable.RunStatus runStatus = ((Scheduleable)businessObject).runScheduledTask();
                  // adiciona no log
                  message = "Executando: Sucesso=" + (runStatus.getSucess() ? "SIM" : "N�O") + "; Mensagem=" + runStatus.getMessage();
                  runLog.add(DateTools.formatDateTimePrecision(new Date()) + " -> " + message);
                  facade.log().write(logId, message);
                }
                // se n�o � um Scheduleable...exce��o
                else {
                  // exce��o
                  throw new ExtendedException(getClass().getName(), "runScheduledTask", businessObject.getClass().getName() + " n�o implementa a interface " + Scheduleable.class.getName() + ".");
                } // if
              }
              catch (Exception e) {
                // adiciona a exce��o no log
                message = "Exce��o: " + e.getMessage();
                runLog.add(DateTools.formatDateTimePrecision(new Date()) + " -> " + message);
                facade.log().write(logId, message);
                hasException = true;
              } // try-catch
            } // for w

            // adiciona no log
            message = "Efetuando logoff...";
            runLog.add(DateTools.formatDateTimePrecision(new Date()) + " -> " + message);
            facade.log().write(logId, message);
            // efetua Logoff
            facade.logoff();
            // adiciona no log
            message = "Efetuando logoff: OK.";
            runLog.add(DateTools.formatDateTimePrecision(new Date()) + " -> " + message);
            facade.log().write(logId, message);
          }
          catch (Exception e) {
            // adiciona a exce��o no log
            message = "Exce��o: " + e.getMessage();
            runLog.add(DateTools.formatDateTimePrecision(new Date()) + " -> " + message);
            facade.log().write(logId, message);
            hasException = true;
          } // try-catch
        } // for i
      } // if
    }
    catch (Exception e) {
      // adiciona a exce��o no log
      message = "Exce��o: " + e.getMessage();
      runLog.add(DateTools.formatDateTimePrecision(new Date()) + " -> " + message);
      facade.log().write(logId, message);
      hasException = true;
    } // try-catch

    // tenta enviar o relat�rio por e-mail
    try {
      // se devemos e podemos enviar o log por e-mail...
      if ((taskFile != null) && taskFile.schedule().getReportStatus() && facade.mailService().getSmtpActive()) {
        // adiciona a exce��o no log
        message = "Enviando relat�rio: " + taskFile.schedule().getReportAddress() + "...";
        runLog.add(DateTools.formatDateTimePrecision(new Date()) + " -> " + message);
        facade.log().write(logId, message);
        // mensagem com o log
        String[] messageLines = new String[runLog.size()];
        runLog.copyInto(messageLines);
        // assunto da mensagem
        String subject = "Schedule " + taskName + (hasException ? " EXCE��O" : "");
        // envia o log por e-mail
        facade.mailService().sendMessage(taskFile.schedule().getReportAddress(),
                                         subject,
                                         StringTools.arrayStringToString(messageLines, "\r\n"));
        // adiciona a exce��o no log
        message = "Enviando relat�rio: OK.";
        runLog.add(DateTools.formatDateTimePrecision(new Date()) + " -> " + message);
        facade.log().write(logId, message);
      } // if
    }
    catch (Exception e) {
      // adiciona a exce��o no log
      message = "Exce��o: " + e.getMessage();
      runLog.add(DateTools.formatDateTimePrecision(new Date()) + " -> " + message);
      facade.log().write(logId, message);
      hasException = true;
    } // try-catch
  }

  /**
   * Agenda 'task' para execu��o de acordo com suas configura��es.
   * @param task Task que se deseja agendar para execu��o.
   */
  public void scheduleTask(Task task) {
    // se a tarefa j� foi agendada...continua
    if (task.getScheduled())
      return;
    // se � uma tarefa recorrente...
    if (task.getPeriod() > 0)
      timer.scheduleAtFixedRate(task, task.getFirstTime(), task.getPeriod());
    // se n�o � recorrente...
    else
      timer.schedule(task, task.getFirstTime());
    // marca a tarefa como agendada
    task.setScheduled(true);
    // adiciona a nossa lista
    taskList.add(task);
  }

  /**
   * Retorna a lita de tarefas agendadas.
   * @return TaskList Retorna a lita de tarefas agendadas.
   */
  public TaskList taskList() {
    return taskList;
  }

}
