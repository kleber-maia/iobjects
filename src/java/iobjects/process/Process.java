package iobjects.process;

import iobjects.*;

/**
 * Representa a classe base de todas as classes que representam processos
 * na aplicação.
 */
public class Process extends BusinessObject {

  private ProcessStepList processStepList = new ProcessStepList();

  /**
   * Construtor padrão.
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
