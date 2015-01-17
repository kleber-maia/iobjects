package iobjects.process;

import java.util.*;

import iobjects.ui.process.*;

/**
 * Representa uma lista de ProcessStep's.
 */
public class ProcessStepList {

  private Vector vector = new Vector();

  /**
   * Construtor padr�o.
   */
  public ProcessStepList() {
  }

  /**
   * Adiciona o ProcessStep especificado � lista.
   * @param processStep ProcessStep para adicionar � lista.
   */
  public void add(ProcessStep processStep) {
    if (!contains(processStep.getCaption()))
      vector.add(processStep);
  }

  /**
   * Remove todos os ProcessStep�s da lista.
   */
  public void clear() {
    vector.clear();
  }

  /**
   * Retorna true se existir um ProcessStep com o name especificado.
   * @param name Nome do ProcessStep que se deseja localizar,
   *             desprezando letras mai�sculas e min�sculas.
   * @return Retorna true se existir um ProcessStep com o name especificado.
   */
  public boolean contains(String name) {
    return indexOf(name) >= 0;
  }

  /**
   * Retorna o ProcessStep na posi��o indicada por 'index'.
   * @param index �ndice do ProcessStep que se dejesa retornar.
   * @return Retorna o ProcessStep na posi��o indicada por 'index'.
   */
  public ProcessStep get(int index) {
    return (ProcessStep)vector.get(index);
  }

  /**
   * Retorna o ProcessStep com o name especificado.
   * @param name Nome do ProcessStep que se deseja localizar,
   *             desprezando letras mai�sculas e min�sculas.
   * @return Retorna o ProcessStep com o name especificado.
   */
  public ProcessStep get(String name) {
    int index = indexOf(name);
    if (index >= 0)
      return (ProcessStep)vector.get(index);
    else
      return null;
  }

  /**
   * Retorna o �ndice do ProcessStep com o name especificado.
   * @param name Nome do ProcessStep que se deseja localizar,
   *             desprezando letras mai�sculas e min�sculas.
   * @return Retorna o �ndice do ProcessStep com o name especificado.
   */
  public int indexOf(String name) {
    for (int i=0; i<size(); i++)
      if (get(i).getName().compareToIgnoreCase(name) == 0)
        return i;
    return -1;
  }

  /**
   * Remove o ProcessStep na posi��o indicada por index.
   * @param index Posi��o do ProcessStep para remover.
   */
  public void remove(int index) {
    vector.remove(index);
  }

  /**
   * Retorna a quantidade de ProcessStep�s existentes na lista.
   * @return Retorna a quantidade de ProcessStep�s existentes na lista.
   */
  public int size() {
    return vector.size();
  }

}
