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
 * Representa a agenda da aplicação, capaz de iniciar seções de usuário e
 * executar tarefas agendadas.
 * <p>
 * Tarefas com agendamento diário serão executadas na hora definida, iniciando
 * no dia em que a aplicação foi executada e se repetindo a cada 24 horas.
 * Se a aplicação for iniciada após a hora agendada para a execução da tarefa
 * ela somente ocorrerá no dia seguinte.
 * </p>
 * <p>
 * Tarefas com agendamento semanal serão executadas na hora e dias definidos,
 * iniciando no dia em que a aplicação foi executada e se repetindo a cada
 * 7 dias. Se a aplicação for iniciada após a hora e dia agendados a execução
 * da tarefa somente ocorrerá no próximo dia e hora do agendamento ou na próxima
 * semana, o que acontecer primeiro.
 * </p>
 * <p>
 * Tarefas com agendamento mensal serão executadas na hora e dias definidos,
 * iniciando no dia em que a aplicação foi executada. Se a aplicação foi iniciada
 * após a hora e dia agendados a execução da tarefa somente ocorrerá no próximo
 * dia e hora do agendamento ou no próximo mês, o que acontecer primeiro. A
 * tarefa não se repetirá a cada mês, a menos que a aplicação seja reiniciada
 * durante esse período.
 * </p>
 * @since 2006
 */
public class Schedule {

  /**
   * Define a extensão dos arquivos de configuração de tarefas agendadas.
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
   * Retorna a instância de Scheduller. Caso ainda não tenha sido instanciada,
   * chamar o método getInstace(String tasksFilePath).
   * @return Schedule Retorna a instância de Scheduller.
   */
  static public Schedule getInstace() {
    return instance;
  }

  /**
   * Retorna a instância de Scheduller para ser usada pela aplicação.
   * @param tasksFilePath String Caminho onde se encontram os arquivos
   *        de tarefas agendadas.
   * @return Scheduller Retorna a instância de Scheduller para ser usada pela
   *         aplicação.
   */
  static public Schedule getInstace(String tasksFilePath) {
    if (instance == null)
      instance = new Schedule(tasksFilePath);
    return instance;
  }

  /**
   * Retorna a próxima data que ocorrerá o dia da semana indicado por 'day' e
   * define a hora como 'hour'.
   * @param day int Dia da semana que se deseja saber a próxima ocorrência.
   * @param hour String Hora para ser definida na data.
   * @return Date Retorna a próxima data que ocorrerá o dia da semana indicado
   *              por 'day' e define a hora como 'hour'.
   */
  static private Date getNextDayOfWeek(int    day,
                                       String hour) {
    // nosso resultado
    Date result = new Date();
    // nosso calendário
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(result);
    // procura pelo dia desejado
    while (calendar.get(Calendar.DAY_OF_WEEK) != day)
      calendar.add(Calendar.DATE, 1);
    // constrói a data com a hora informada
    result = DateTools.parseDateTime(DateTools.formatDate(calendar.getTime()) + " " + hour);
    // se já passou da hora agendada...só na próxima semana
    Date now = new Date();
    if (now.after(result))
      result = DateTools.getCalculatedDays(result, 7);
    // retorna
    return result;
  }

  /**
   * Carrega as tarefas agendadas. Caso as tarefas já tenho sido carregadas,
   * cancela todos os agendamentos existentes.
   * @throws Exception Em caso de exceção na tentativa de acesso aos arquivos
   *                   de tarefas agendadas.
   */
  public void loadTaskFiles() throws Exception {
    // se já carregamos as tarefas...descarta os agendamentos atuais
    if (tasksLoaded) {
      // desfaz os agendamentos
      timer.cancel();
      timer = new Timer();
      // limpa a lista de tarefas agendadas através de arquivo
      // nesse caso as tarefas agendadas pelas classes serão mantidas
      // e adicionadas ao Timer mais a frente
      for (int i=taskList.size()-1; i>=0; i--)
        if (taskList.get(i).getClassName().equals(""))
          taskList.remove(i);
    } // if

    // pega a lista de arquivos de configuração de conexões
    String[] fileExtensions = {TASK_FILE_EXTENSION};
    String[] fileNames = FileTools.getFileNames(tasksFilePath, fileExtensions, false);
    // limpa as tarefas atuais
    taskFiles.clear();
    // lista de tarefas temporária
    TaskList tempTaskList = new TaskList();
    // loop nos arquivos para carregá-los
    for (int i=0; i<fileNames.length; i++) {
      // arquivo da vez
      String fileName = fileNames[i];
      // nome da tarefa
      String taskName = fileName.substring(0, fileName.indexOf('.'));
      // carrega o arquivo
      TaskFile taskFile = new TaskFile(tasksFilePath + fileName);

      // agenda sua execução horária
      if (taskFile.schedule().getHourly().getActive()) {
        // data atual
        String today = DateTools.formatDate(DateTools.getActualDate());
        // loop nas horas
        for (int w=0; w<taskFile.schedule().getHourly().size(); w++) {
          // horário agendado hoje
          String[] dateParts = DateTools.splitDate(new Date());
          Date firstTime = DateTools.parseDateTime(today + " " + NumberTools.completeZero(taskFile.schedule().getHourly().getHour(w), 2) + ":00");
          // cria agendamento
          tempTaskList.add(new Task(taskName, firstTime, ONE_DAY));
        } // for
      } // if
      
      // agenda sua execução diária
      if (taskFile.schedule().getDaily().getActive()) {
        // data atual
        Date now = new Date();
        // data agendada no dia de hoje
        Date firstTime = DateTools.parseDateTime(DateTools.formatDate(DateTools.getActualDate()) + " " + taskFile.schedule().getDaily().getHour());
        // se já passou da hora agendada...só amanhã
        if (now.after(firstTime))
          firstTime = DateTools.getCalculatedDays(firstTime, 1);
        // cria agendamento
        tempTaskList.add(new Task(taskName, firstTime, ONE_DAY));
      } // if

      // agenda sua execução semanal
      if (taskFile.schedule().getWeekly().getActive()) {
        // cria agendamento para o próximo domingo
        if (taskFile.schedule().getWeekly().getSunday())
          tempTaskList.add(new Task(taskName, getNextDayOfWeek(Calendar.SUNDAY, taskFile.schedule().getWeekly().getHour()), ONE_DAY * 7));
        // cria agendamento para a próxima segunda
        if (taskFile.schedule().getWeekly().getMonday())
          tempTaskList.add(new Task(taskName, getNextDayOfWeek(Calendar.MONDAY, taskFile.schedule().getWeekly().getHour()), ONE_DAY * 7));
        // cria agendamento para a próxima terça
        if (taskFile.schedule().getWeekly().getTuesday())
          tempTaskList.add(new Task(taskName, getNextDayOfWeek(Calendar.TUESDAY, taskFile.schedule().getWeekly().getHour()), ONE_DAY * 7));
        // cria agendamento para a próxima quarta
        if (taskFile.schedule().getWeekly().getWednesday())
          tempTaskList.add(new Task(taskName, getNextDayOfWeek(Calendar.WEDNESDAY, taskFile.schedule().getWeekly().getHour()), ONE_DAY * 7));
        // cria agendamento para a próxima quinta
        if (taskFile.schedule().getWeekly().getThursday())
          tempTaskList.add(new Task(taskName, getNextDayOfWeek(Calendar.THURSDAY, taskFile.schedule().getWeekly().getHour()), ONE_DAY * 7));
        // cria agendamento para a próxima sexta
        if (taskFile.schedule().getWeekly().getFriday())
          tempTaskList.add(new Task(taskName, getNextDayOfWeek(Calendar.FRIDAY, taskFile.schedule().getWeekly().getHour()), ONE_DAY * 7));
        // cria agendamento para o próximo sábado
        if (taskFile.schedule().getWeekly().getSaturday())
          tempTaskList.add(new Task(taskName, getNextDayOfWeek(Calendar.SATURDAY, taskFile.schedule().getWeekly().getHour()), ONE_DAY * 7));
      } // if

      // agenda sua execução mensal
      if (taskFile.schedule().getMonthly().getActive()) {
        // data atual
        Date now = new Date();
        // loop nos dias
        for (int w=0; w<taskFile.schedule().getMonthly().size(); w++) {
          // dia agendado no mês atual
          String[] dateParts = DateTools.splitDate(new Date());
          Date firstTime = DateTools.parseDateTime(taskFile.schedule().getMonthly().getDay(w) + "/" + dateParts[1] + "/" + dateParts[2] + " " + taskFile.schedule().getDaily().getHour());
          // se já passou da hora agendada...só mês que vem
          if (now.after(firstTime))
            firstTime = DateTools.getCalculatedMonths(firstTime, 1);
          // cria agendamento sem recorrência
          tempTaskList.add(new Task(taskName, firstTime));
        } // for
      } // if

      // adiciona na lista
      taskFiles.add(taskFile, taskName);
    } // for

    // põe as tarefas para serem executadas
    for (int i=0; i<tempTaskList.size(); i++) {
      // tarefa da vez
      Task task = tempTaskList.get(i);
      // põe na fila de execução
      scheduleTask(task);
    } // for

    // marca as tarefas como carregadas
    tasksLoaded = true;
  }

  /**
   * Executa a tarefa agendada configurada em 'taskName' e utiliza 'facade'
   * para obter as instâncias dos BusinessObject's necessários.
   * <p>
   * As tarefas serão executadas em um bloco protegido contra exceções. O status
   * de execução, ou mensagem de exceção, de cada tarefa será gravado no arquivo
   * de log. Este log será enviado por e-mail, caso esteja configurado no trabalho
   * agendado.
   * </p>
   * @param facade Facade Fachada.
   * @param taskName String Nome da tarefa agendada para ser executada.
   */
  public synchronized void runScheduledTask(Facade facade,
                                            String taskName) {
    // se a aplicação ainda não está pronta...dispara
    if (Facade.getApplicationStatus() != Facade.APPLICATION_STATUS_READY)
      return;
    // mensagens de log
    String message = "";
    // tivemos alguma exceção?
    boolean hasException = false;
    // log de execução
    Vector runLog = new Vector();
    // identificação do Log
    String logId = "schedule_" + taskName;
    // tenta executar
    Task     task     = null;
    TaskFile taskFile = null;
    try {
      // pega a tarefa
      task = taskList.get(taskName);
      // se não encontramos...exceção
      if (task == null)
        throw new ExtendedException(getClass().getName(), "runScheduledTask", "Tarefa não encontrada: " + taskName + ".");
      // se a tarefa representa uma simples classe...
      if (!task.getClassName().equals("")) {
        // adiciona no log
        message = "Executando: " + task.getClassName() + "...";
        runLog.add(DateTools.formatDateTimePrecision(new Date()) + " -> " + message);
        facade.log().write(logId, message);
        // instancia o BusinessObject
        BusinessObject businessObject = facade.getBusinessObject(task.getClassName());
        // se é realmente um Schedulable...
        if (businessObject instanceof Scheduleable) {
          // executa
          Scheduleable.RunStatus runStatus = ((Scheduleable)businessObject).runScheduledTask();
          // adiciona no log
          message = "Executando: Sucesso=" + (runStatus.getSucess() ? "SIM" : "NÃO") + "; Mensagem=" + runStatus.getMessage();
          runLog.add(DateTools.formatDateTimePrecision(new Date()) + " -> " + message);
          facade.log().write(logId, message);
        }
        // se não é um Scheduleable...exceção
        else {
          // exceção
          throw new ExtendedException(getClass().getName(), "runScheduledTask", businessObject.getClass().getName() + " não implementa a interface " + Scheduleable.class.getName() + ".");
        } // if
      }
      // se a tarefa representa um arquivo de configurações...
      else {
        // pega as configurações da tarefa agendada
        taskFile = taskFiles.get(taskName);
        // se não achamos o arquivo de configurações...exceção
        if (taskFile == null)
          throw new ExtendedException(getClass().getName(), "runScheduledTask", "Tarefa não encontrada: " + taskName + ".");
        // loop nas opções de autenticação
        for (int i=0; i<taskFile.authentication().size(); i++) {
          try {
            // login da vez
            TaskFile.Login login = taskFile.authentication().item(i);
            // adiciona no log
            message = "Efetuando logon: DefaultConnectionName=" + login.getDefaultConnectionName() + "; UserName=" + login.getUserName() + "...";
            runLog.add(DateTools.formatDateTimePrecision(new Date()) + " -> " + message);
            facade.log().write(logId, message);
            // tenta efetuar logon com o usuário informado
            facade.logon(login.getDefaultConnectionName(),
                         login.getUserName(),
                         login.getPassword());
            // adiciona no log
            message = "Efetuando logon: OK.";
            runLog.add(DateTools.formatDateTimePrecision(new Date()) + " -> " + message);
            facade.log().write(logId, message);

            // loop nos objetos de negócio configurados
            for (int w=0; w<taskFile.businessObjects().size(); w++) {
              try {
                // BusinessObject da vez
                BusinessObject businessObject = facade.getBusinessObject(taskFile.businessObjects().item(w));
                // se é realmente um Schedulable...executa
                if (businessObject instanceof Scheduleable) {
                  // adiciona no log
                  message = "Executando: " + businessObject.getClass().getName() + "...";
                  runLog.add(DateTools.formatDateTimePrecision(new Date()) + " -> " + message);
                  facade.log().write(logId, message);
                  // executa
                  Scheduleable.RunStatus runStatus = ((Scheduleable)businessObject).runScheduledTask();
                  // adiciona no log
                  message = "Executando: Sucesso=" + (runStatus.getSucess() ? "SIM" : "NÃO") + "; Mensagem=" + runStatus.getMessage();
                  runLog.add(DateTools.formatDateTimePrecision(new Date()) + " -> " + message);
                  facade.log().write(logId, message);
                }
                // se não é um Scheduleable...exceção
                else {
                  // exceção
                  throw new ExtendedException(getClass().getName(), "runScheduledTask", businessObject.getClass().getName() + " não implementa a interface " + Scheduleable.class.getName() + ".");
                } // if
              }
              catch (Exception e) {
                // adiciona a exceção no log
                message = "Exceção: " + e.getMessage();
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
            // adiciona a exceção no log
            message = "Exceção: " + e.getMessage();
            runLog.add(DateTools.formatDateTimePrecision(new Date()) + " -> " + message);
            facade.log().write(logId, message);
            hasException = true;
          } // try-catch
        } // for i
      } // if
    }
    catch (Exception e) {
      // adiciona a exceção no log
      message = "Exceção: " + e.getMessage();
      runLog.add(DateTools.formatDateTimePrecision(new Date()) + " -> " + message);
      facade.log().write(logId, message);
      hasException = true;
    } // try-catch

    // tenta enviar o relatório por e-mail
    try {
      // se devemos e podemos enviar o log por e-mail...
      if ((taskFile != null) && taskFile.schedule().getReportStatus() && facade.mailService().getSmtpActive()) {
        // adiciona a exceção no log
        message = "Enviando relatório: " + taskFile.schedule().getReportAddress() + "...";
        runLog.add(DateTools.formatDateTimePrecision(new Date()) + " -> " + message);
        facade.log().write(logId, message);
        // mensagem com o log
        String[] messageLines = new String[runLog.size()];
        runLog.copyInto(messageLines);
        // assunto da mensagem
        String subject = "Schedule " + taskName + (hasException ? " EXCEÇÃO" : "");
        // envia o log por e-mail
        facade.mailService().sendMessage(taskFile.schedule().getReportAddress(),
                                         subject,
                                         StringTools.arrayStringToString(messageLines, "\r\n"));
        // adiciona a exceção no log
        message = "Enviando relatório: OK.";
        runLog.add(DateTools.formatDateTimePrecision(new Date()) + " -> " + message);
        facade.log().write(logId, message);
      } // if
    }
    catch (Exception e) {
      // adiciona a exceção no log
      message = "Exceção: " + e.getMessage();
      runLog.add(DateTools.formatDateTimePrecision(new Date()) + " -> " + message);
      facade.log().write(logId, message);
      hasException = true;
    } // try-catch
  }

  /**
   * Agenda 'task' para execução de acordo com suas configurações.
   * @param task Task que se deseja agendar para execução.
   */
  public void scheduleTask(Task task) {
    // se a tarefa já foi agendada...continua
    if (task.getScheduled())
      return;
    // se é uma tarefa recorrente...
    if (task.getPeriod() > 0)
      timer.scheduleAtFixedRate(task, task.getFirstTime(), task.getPeriod());
    // se não é recorrente...
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
