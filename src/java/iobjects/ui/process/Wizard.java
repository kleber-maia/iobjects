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
package iobjects.ui.process;

import javax.servlet.http.*;

import iobjects.*;
import iobjects.process.*;
import iobjects.servlet.*;
import iobjects.ui.*;
import iobjects.ui.help.*;
import iobjects.util.*;

/**
 * Representa o utilit�rio para cria��o de um assistente para execu��o de
 * processos.
 * @since 3.2
 */
public class Wizard {

  static public String  STEP_INDEX       = "stepIndex";
  static public String  STEPS_DONE       = "stepsDone";
  static public Command COMMAND_NEXT     = new Command(Command.COMMAND_NEXT,     "Pr�xima >",   "Pr�xima etapa");
  static public Command COMMAND_PREVIOUS = new Command(Command.COMMAND_PREVIOUS, "< Anterior",  "Etapa anterior");
  static public Command COMMAND_RESTART  = new Command(Command.COMMAND_RESTART,  "Reiniciar", "Reinicia o assistente");

  private Action             action        = null;
  private Command            actualCommand = null;
  private Command            command       = null;
  private Facade             facade        = null;
  private Form               form          = null;
  private String             id            = "";
  private HttpServletRequest request       = null;
  private int                stepIndex     = -1;
  private ProcessStepList    stepList      = null;
  private String             stepsDone     = "";

  /**
   * Construtor padr�o.
   * @param facade Facade Fachada.
   * @param request HttpServletRequest Requisi��o atual.
   * @param id String Identifica��o do Wizard na p�gina.
   * @param action Action A��o de destino do Wizard para ser executada pelo Controller.
   * @param command Command Comando para ser executado na a��o de destino.
   * @param stepList ProcessStepList Lista de etapas a serem executadas pelo
   *                 Wizard. <b>S�o necess�rias pelo menos duas etapas.</b>
   */
  public Wizard(Facade             facade,
                HttpServletRequest request,
                String             id,
                Action             action,
                Command            command,
                ProcessStepList    stepList) {
    this.facade = facade;
    this.request = request;
    this.id = id;
    this.action = action;
    this.command = command;
    this.stepList = stepList;
    // se n�o temos pelo menos 2 etapas...exce��o
    if (stepList.size() < 2)
      throw new RuntimeException(new ExtendedException(getClass().getName(), "", "S�o necess�rias pelo menos duas etapas."));
    // l� o estado do Wizard da requisi��o atual
    getStateFromRequest();
  }

  /**
   * Retorna o ProcessStep que representa a etapa atual do assistente.
   * @return ProcessStep Retorna o ProcessStep que representa a etapa atual do assistente.
   */
  public ProcessStep actualStep() {
    // se n�o temos nenhuma etapa realizada...retorna pr�xima etapa
    if (stepIndex < 0)
      return next();
    // se j� temos...retorna-a
    else
      return stepList.get(stepIndex);
  }
  

  /**
   * Retorna o script HTML contendo a representa��o de in�cio do Wizard. Todos
   * os elementos &lt;input&gt; inclu�dos ser�o submetidos para a p�gina, atrav�s
   * do &lt;form&gt; embutido no Wizard, quando o usu�rio executar qualquer dos
   * comandos existentes.
   * <b>O conte�do apresentado entre o in�cio do Wizard e seu t�rmino deve
   * corresponder apenas ao conte�do da etapa atual verificada atrav�s do
   * m�todo 'actualStep()'.</b>
   * @return String Retorna o script HTML contendo a representa��o de in�cio
   *                de uma etapa do Wizard.
   *                <b>O conte�do apresentado entre o in�cio do Wizard e seu
   *                t�rmino deve corresponder apenas ao conte�do da etapa atual
   *                verificada atrav�s do m�todo 'actualStep()'.</b>
   */
  public String begin() {
    // nosso resultado
    StringBuffer result = new StringBuffer();
    // in�cio e barra de t�tulo
    result.append("<!-- wizard --> \r");
    result.append("<table style=\"width:100%; height:100%; table-layout:fixed;\" cellpadding=\"0\" cellspacing=\"0\"> \r");
    result.append("  <tr> \r");
    result.append("    <td class=\"Window\" style=\"height:62px;\"> \r");
    result.append("      <!-- identifica��o e �cone --> \r");
    result.append("      <table style=\"width:100%; height:100%;\" cellpadding=\"4\"> \r");
    result.append("        <tr> \r");
    result.append("          <td style=\"width:auto;\"> \r");
    result.append("            <!-- identifica��o --> \r");
    result.append("            <table style=\"width:100%; height:100%;\"> \r");
    result.append("              <tr> \r");
    result.append("                <td colspan=\"2\"><b>" + actualStep().getCaption() + "</b></td> \r");
    result.append("              </tr> \r");
    result.append("              <tr> \r");
    result.append("                <td width=\"10\"></td> \r");
    result.append("                <td>" + actualStep().getDescription() + "</td> \r");
    result.append("              </tr> \r");
    result.append("            </table> \r");
    result.append("          </td> \r");
    result.append("          <td style=\"width:50px;\"> \r");
    result.append("            <!-- �cone --> \r");
    result.append("            <table class=\"ActiveCaption\" cellspacing=\"4\" style=\"width:100%; height:100%;\"> \r");
    result.append("              <tr> \r");
    result.append("                <td class=\"Window\" align=\"center\" valign=\"middle\"> \r");
    result.append("                  <img src=\"images/wizard32x32.png\" /> \r");
    result.append("                </td> \r");
    result.append("              </tr> \r");
    result.append("            </table> \r");
    result.append("          </td> \r");
    result.append("        </tr> \r");
    result.append("      </table> \r");
    result.append("    </td> \r");
    result.append("  </tr> \r");
    result.append("  <!-- divis�o da barra de t�tulo --> \r");
    result.append("  <tr> \r");
    result.append("    <td style=\"height:4px;\" valign=\"top\"> \r");
    result.append("      <table class=\"HorizontalLine\" style=\"width:100%;\"> \r");
    result.append("        <tr><td></td></tr> \r");
    result.append("      </table> \r");
    result.append("    </td> \r");
    result.append("  </tr> \r");
    result.append("  <tr> \r");
    result.append("    <td style=\"height:auto; padding:4px;\" valign=\"top\"> \r");
    result.append("      <!-- Form da etapa atual --> \r");
    // inicia o Form da etapa atual
    result.append(form.begin());
    // input para guardar o �ndice da etapa atual
    result.append(FormEdit.script(facade, STEP_INDEX, stepIndex + "", true));
    // input para guardar as etapas realizadas
    result.append(FormEdit.script(facade, STEPS_DONE, stepsDone, true));
    // retorna
    return result.toString();
  }

  /**
   * Retorna o script HTML contendo a representa��o de t�rmino do Wizard.
   * @return String Retorna o script HTML contendo a representa��o de t�rmino do Wizard.
   */
  public String end() {
    // nosso resultado
    StringBuffer result = new StringBuffer();
    // termina o Form da etapa atual
    result.append(form.end());
    result.append("    </td> \r");
    result.append("  </tr> \r");
    // barra de comandos
    result.append("  <!-- divis�o da barra de comandos --> \r");
    result.append("  <tr> \r");
    result.append("    <td style=\"height:4px;\"> \r");
    result.append("      <table class=\"HorizontalLine\" style=\"width:100%;\"> \r");
    result.append("        <tr><td></td></tr> \r");
    result.append("      </table> \r");
    result.append("    </td> \r");
    result.append("  </tr> \r");
    result.append("  <!-- bot�es de comando --> \r");
    result.append("  <tr> \r");
    result.append("    <td style=\"height:40px;\"> \r");
    result.append("      <table cellpadding=\"4\" style=\"width:100%;\"> \r");
    result.append("        <tr> \r");
    result.append("          <td align=\"left\" style=\"width:30%\"> \r");
    // bot�o de ajuda
    result.append("          " + HelpButton.script(facade, action, true, HelpButton.KIND_DEFAULT));
    result.append("          </td> \r");
    result.append("          <td align=\"right\" style=\"width:70%\"> \r");
    // comando reiniciar
    result.append("            " + CommandControl.formButton(facade, form, COMMAND_RESTART, "", "", true, true, stepIndex == 0) + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; \r");
    // comando retornar
    result.append("            " + CommandControl.formButton(facade, form, COMMAND_PREVIOUS, "", "", true, true, (stepIndex == 0) || (form.getCommand() == null)) + "&nbsp; \r");
    // comando avan�ar ou executar
    result.append("            " + CommandControl.formButton(facade, form, (form.getCommand() != null ? form.getCommand() : command ), "", (form.getCommand() == command ? "Deseja mesmo executar o processo?" : ""), true, false, form.getCommand() == null) + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; \r");
    // bot�o fechar
    result.append("            " + Button.script(facade, "buttonClose", "Fechar", "", "", "", Button.KIND_DEFAULT, "", "window.close();", false) + " \r");
    result.append("          </td> \r");
    result.append("        </tr> \r");
    result.append("      </table> \r");
    result.append("    </td> \r");
    result.append("  </tr> \r");
    result.append("</table> \r");
    // retorna
    return result.toString();
  }

  /**
   * Avan�a para a etapa identificada por 'processStep'. Se a etapa informada
   * for anterior a etapa atual nada ser� feito. Neste caso o m�todo 'previous()'
   * deve ser executado.
   * @param processStep ProcessStep para a qual deseja avan�ar.
   */
  public void forward(ProcessStep processStep) {
    // pega o �ndice da etapa informada
    int index = stepList.indexOf(processStep.getName());
    // se o �ndice � maior que o �ndice atual...
    if (index > stepIndex) {
      // sempre retrocede uma etapa, considerando que a intere��o com o
      // assistente sempre ocorre depois do Next
      previous();
      // avan�a para a etapa especificada
      stepIndex = index;
      // adiciona a etapa � lista de etapas realizadas
      if (stepsDone.length() > 0)
        stepsDone += ";";
      stepsDone += stepIndex;
    } // if
    // atualiza nosso comando
    updateCommand();
  }

  /**
   * Retorna a a��o de destino do Wizard para ser executada pelo Controller.
   * @return String Retorna a a��o de destino do Wizard para ser executada
   *         pelo Controller.
   */
  public Action getAction() {
    return action;
  }

  /**
   * Retorna o comando para ser executado na etapa atual do assistente.
   * @return Retorna o comando para ser executado na etapa atual do assistente.
   */
  public Command getActualCommand() {
    return actualCommand;
  }

  /**
   * Retorna o comando para ser executado na a��o de destino.
   * @return String Retorna o comando para ser executado na a��o de destino.
   */
  public Command getCommand() {
    return command;
  }

  /**
   * Retorna o Form interno do Wizard utilizado para submeter os par�metros
   * e realizar a navega��o.
   * @return Form Retorna o Form interno do Wizard utilizado para submeter os
   *         par�metros e realizar a navega��o.
   */
  public Form getForm() {
    return form;
  }

  /**
   * Retorna a identifica��o do Wizard na p�gina.
   * @return String Retorna a identifica��o do Wizard na p�gina.
   */
  public String getId() {
    return id;
  }

  /**
   * Retorna a pr�xima etapa do assistente ou null caso n�o haja uma.
   * @return Retorna a pr�xima etapa do assistente ou null caso n�o haja uma.
   */
  public ProcessStep getNext() {
    // se ainda podemos avan�ar...
    if (stepIndex < stepList.size()-1)
      return stepList.get(stepIndex+1);
    // se n�o podemos mais avan�ar...
    else
      return null;
  }
  
  /**
   * Retorna a etapa anterior do assistente ou null caso n�o haja uma.
   * @return Retorna a etapa anterior do assistente ou null caso n�o haja uma.
   */
  public ProcessStep getPrevious() {
    // se ainda podemos voltar...
    if (stepIndex > 0)
      return stepList.get(stepIndex-1);
    // se n�o podemos mais voltar...
    else
      return null;
  }
  
  /**
   * Retorna true se o Wizard est� sendo iniciado.
   * @return boolean Retorna true se o Wizard est� sendo iniciado.
   */
  public boolean isStarting() {
    return stepIndex < 0;
  }

  /**
   * Obt�m o estado atual do Wizard a partir da requisi��o recebida.
   */
  private void getStateFromRequest() {
    try {
      // etapas realizadas
      String stepsDoneStr = request.getParameter(STEPS_DONE);
      if (stepsDoneStr != null)
        stepsDone = stepsDoneStr;
      // etapa atual
      String stepIndexStr = request.getParameter(STEP_INDEX);
      if (stepIndexStr != null)
        stepIndex = NumberTools.parseInt(stepIndexStr);
      // se devemos reiniciar o assistente...
      if (Controller.isRestarting(request))
        restart();
      // se devemos avan�ar � pr�xima etapa...
      else if (Controller.isGoingNext(request))
        next();
      // se devemos voltar � etapa anterior...
      else if (Controller.isGoingPrevious(request))
        previous();
      // se estamos executando...vai para a �ltima etapa
      else if (Controller.getCommand(request).equals(command.getName()))
        forward(stepList.get(stepList.size()-1));
      // se n�o vamos fazer nada...atualiza o comando atual
      else
        updateCommand();
    }
    catch (Exception e) {
      throw new RuntimeException(new ExtendedException(getClass().getName(), "getStateFromRequest", e));
    } // try-catch
  }

  /**
   * Avan�a para a pr�xima etapa do assistente e retorna o ProcessStep
   * correspondente.
   * @return ProcessStep Avan�a para a pr�xima etapa do assistente e retorna o
   *                    ProcessStep correspondente.
   */
  public ProcessStep next() {
    // se ainda podemos avan�ar...
    if (stepIndex < stepList.size()-1) {
      // avan�a
      stepIndex++;
      // adiciona a etapa � lista de etapas realizadas
      if (stepsDone.length() > 0)
        stepsDone += ";";
      stepsDone += stepIndex;
    } // if
    // atualiza nosso comando
    updateCommand();
    // retorna
    return actualStep();
  }

  /**
   * Volta para a etapa anterior realizada pelo assistente e retorna o ProcessStep
   * correspondente.
   * @return ProcessStep Volta para a etapa anterior realizada pelo assistente e
   *                    retorna o ProcessStep correspondente.
   */
  public ProcessStep previous() {
    // se ainda podemos voltar...
    if (stepIndex > 0) {
      // recria a lista de etapas realizadas retirando a �ltima
      stepsDone = stepsDone.substring(0, stepsDone.lastIndexOf(';'));
      // array de etapas realizadas
      String[] stepsDoneArray = stepsDone.split(";");
      // volta para a �ltima etapa restante
      if (stepsDoneArray.length > 1)
        stepIndex = Integer.parseInt(stepsDoneArray[stepsDoneArray.length-1]);
      else
        stepIndex = Integer.parseInt(stepsDoneArray[0]);
    } // if
    // atualiza nosso comando
    updateCommand();
    // retorna
    return actualStep();
  }

  /**
   * Retorna o assistente ao seu ponto inicial.
   */
  public void restart() {
    // volta ao ponto inicial
    stepIndex = -1;
    stepsDone = "";
    // atualiza nosso comando
    updateCommand();
  }

  /**
   * Define a a��o de destino do Wizard para ser executada pelo Controller.
   * @param action Action A��o de destino do Wizard para ser executada pelo Controller.
   */
  public void setAction(Action action) {
    this.action = action;
  }

  /**
   * Define o comando para ser executado na etapa atual do assistente.
   * @param actualCommand Command comando para ser executado na etapa atual do assistente.
   */
  public void setActualCommand(Command actualCommand) {
    this.actualCommand = actualCommand;
    updateCommand();
  }

  /**
   * Define o comando para ser executado na a��o de destino.
   * @param command Command comando para ser executado na a��o de destino.
   */
  public void setCommand(Command command) {
    this.command = command;
  }

  /**
   * Define a identifica��o do Wizard na p�gina.
   * @param id String Identifica��o do Wizard na p�gina.
   */
  public void setId(String id) {
    this.id = id;
  }

  /**
   * Retorna o ProcessStepList que representa as etapas do assistente.
   * @return ProcessStepList Retorna o ProcessStepList que representa as etapas do assistente.
   */
  public ProcessStepList stepList() {
    return stepList;
  }

  /**
   * Atualiza o pr�ximo comando para ser executado.
   * @since 2006
   */
  private void updateCommand() {
    // comando que iremos utilizar por padr�o
    Command actualCommand = this.actualCommand;
    // se n�o temos um comando para a etapa atual...
    if (actualCommand == null) {
      // se estamos na �ltima etapa...j� finalizamos
      if (stepIndex == stepList.size() - 1)
        actualCommand = null;
      // se estamos na pen�ltima etapa...o comando � o configurado
      else if ((stepIndex == stepList.size() - 2) ||
               ((stepIndex == -1) && (stepList.size() == 2)))
        actualCommand = command;
      // em outro caso...o comando � NEXT
      else
        actualCommand = COMMAND_NEXT;
    } // if
    // cria nosso Form
    form = new Form(request,
                    "form" + id,
                    action,
                    actualCommand,
                    "",
                    true, 
                    false,
                    true);
  }

}
