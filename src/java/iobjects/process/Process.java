package iobjects.process;

import iobjects.*;

/**
 * Representa a classe base de todas as classes que representam processos
 * na aplica��o.
 */
public class Process extends BusinessObject {

  private ProcessStepList processStepList = new ProcessStepList();

  /**
   * Construtor padr�o.
   */
  public Process() {
  }

  /**
   * Retorna a lista de etapas a serem executadas pelo assistente do processo.
   * @return WizardStepList Retorna a lista de etapas a serem executadas pelo
   *         assistente do processo.
   */
  public ProcessStepList processStepList() {
    return processStepList;
  }

}
