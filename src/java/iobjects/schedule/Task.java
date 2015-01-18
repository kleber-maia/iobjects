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

import java.net.*;
import java.util.*;

import iobjects.*;
import iobjects.servlet.*;

/**
 * Representa uma tarefa agendada pelo Schedule.
 * @since 2006
 */
public class Task extends TimerTask {

  private String  name      = "";
  private Date    firstTime = new Date(0);
  private long    period    = 0;
  private String  className = "";
  private boolean scheduled = false;

  /**
   * Construtor padrão. Utilizado quando a Tarefa representa um arquivo de
   * configurações de agendamento.
   * @param name String Nome da tarefa agendada.
   * @param firstTime Date da primeira execução da tarefa.
   */
  public Task(String  name,
              Date    firstTime) {
    this.name = name;
    this.firstTime = firstTime;
  }

  /**
   * Construtor padrão. Utilizado quando a Tarefa representa um arquivo de
   * configurações de agendamento.
   * @param name String Nome da tarefa agendada.
   * @param firstTime Date da primeira execução da tarefa.
   * @param period long Período de recorrência da tarefa.
   */
  public Task(String name,
              Date   firstTime,
              long   period) {
    this.name = name;
    this.firstTime = firstTime;
    this.period = period;
  }

  /**
   * Construtor estendido. Utilizando quando a Tarefa representa uma simples
   * classe e seu agendamento.
   * @param name String Nome da tarefa agendada.
   * @param className String Nome da classe que será executada pela tarefa.
   *                  <b>Esta classe deve ser descendente de BusinessObject e
   *                  implementar a interface Scheduleable.</b>
   * @param firstTime Date da primeira execução da tarefa.
   */
  public Task(String name,
              String className,
              Date   firstTime) {
    this.name = name;
    this.className = className;
    this.firstTime = firstTime;
    this.period = period;
  }

  /**
   * Construtor estendido. Utilizando quando a Tarefa representa uma simples
   * classe e seu agendamento.
   * @param name String Nome da tarefa agendada.
   * @param className String Nome da classe que será executada pela tarefa.
   *                  <b>Esta classe deve ser descendente de BusinessObject e
   *                  implementar a interface Scheduleable.</b>
   * @param firstTime Date da primeira execução da tarefa.
   * @param period long Período de recorrência da tarefa.
   */
  public Task(String name,
              String className,
              Date   firstTime,
              long   period) {
    this.name = name;
    this.className = className;
    this.firstTime = firstTime;
    this.period = period;
  }

  public boolean equals(Object object) {
    return (object instanceof Task) &&
           ((Task)object).getName().equals(name) &&
           ((Task)object).getFirstTime().equals(firstTime) &&
           (((Task)object).getPeriod() == period);
  }

  /**
   * Libera recursos.
   */
  public void finalize() {
    name = null;
    firstTime = null;
  }

  public Date getFirstTime() {
    return firstTime;
  }

  public String getName() {
    return name;
  }

  public long getPeriod() {
    return period;
  }

  /**
   * Retorna true se a tarefa já foi agendada.
   * @return boolean Retorna true se a tarefa já foi agendada.
   */
  public boolean getScheduled() {
    return scheduled;
  }

  /**
   * Retorna o nome da classe que será executada pela tarefa. <b>Esta classe
   * deve ser descendente de BusinessObject e implementar a interface
   * Scheduleable.</b>
   * @return String Retorna o nome da classe que será executada pela tarefa.
   *                <b>Esta classe deve ser descendente de BusinessObject e
   *                implementar a interface Scheduleable.</b>
   */
  public String getClassName() {
    return className;
  }

  /**
   * Executa a tarefa agendada. Efetua uma requisição HTTP à aplicação passando
   * o nome da tarefa agendada, delegando o restante da responsabilidade ao
   * Controller.
   */
  public void run() {
    try {
      // prepara a URL
      URL url = new URL(Controller.getRemoteURL() + "/" + Controller.ACTION_RUN_SCHEDULED_TASK.url(Controller.PARAM_RUN_SCHEDULED_TASK_NAME + "=" + URLEncoder.encode(name, "ISO-8859-1")));
      // conecta
      URLConnection connection = url.openConnection();
      // não tem pressa para executar a tarefa...evita que uma nova requisição
      // seja feita quando a tarefa demora para ser executada, causando uma
      // execução da tarefa em duplicidade da tarefa
      connection.setReadTimeout(0);
      // pedir ao servidor o conteúdo de retorno faz com que os dados
      // sejam enviados e a tarefa seja executada
      connection.getContent();
    }
    catch (Exception e) {
      // verifica se fomos chamados pelo Timer
      StackTraceElement[] stackTrace = e.getStackTrace();
      for (int i=0; i<stackTrace.length; i++) {
        if (stackTrace[i].getClassName().contains("TimerThread")) {
          e.printStackTrace();
          return;
        } // if
      } // for
      // se chegou aqui...podemos lançar a exceção
      throw new RuntimeException(e);
    } // try-catch
  }

  /**
   * Define se a tarefa já foi agendada.
   * @param scheduled boolean Define se a tarefa já foi agendada.
   */
  public void setScheduled(boolean scheduled) {
    this.scheduled = scheduled;
  }

}
