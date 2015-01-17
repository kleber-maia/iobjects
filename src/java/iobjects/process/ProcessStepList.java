package iobjects.process;

import java.util.*;

import iobjects.ui.process.*;

/**
 * Representa uma lista de ProcessStep's.
 */
public class ProcessStepList {

  private Vector vector = new Vector();

  /**
   * Construtor padrão.
   */
  public ProcessStepList() {
  }

  /**
   * Adiciona o ProcessStep especificado à lista.
   * @param processStep ProcessStep para adicionar à lista.
   */
  public void add(ProcessStep processStep) {
    if (!contains(processStep.getCaption()))
      vector.add(processStep);
  }

  /**
   * Remove todos os ProcessStep´s da lista.
   */
  public void clear() {
    vector.clear();
  }

  /**
   * Retorna true se existir um ProcessStep com o name especificado.
   * @param name Nome do ProcessStep que se deseja localizar,
   *             desprezando letras maiúsculas e minúsculas.
   * @return Retorna true se existir um ProcessStep com o name especificado.
   */
  public boolean contains(String name) {
    return indexOf(name) >= 0;
  }

  /**
   * Retorna o ProcessStep na posição indicada por 'index'.
   * @param index Índice do ProcessStep que se dejesa retornar.
   * @return Retorna o ProcessStep na posição indicada por 'index'.
   */
  public ProcessStep get(int index) {
    return (ProcessStep)vector.get(index);
  }

  /**
   * Retorna o ProcessStep com o name especificado.
   * @param name Nome do ProcessStep que se deseja localizar,
   *             desprezando letras maiúsculas e minúsculas.
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
   * Retorna o índice do ProcessStep com o name especificado.
   * @param name Nome do ProcessStep que se deseja localizar,
   *             desprezando letras maiúsculas e minúsculas.
   * @return Retorna o índice do ProcessStep com o name especificado.
   */
  public int indexOf(String name) {
    for (int i=0; i<size(); i++)
      if (get(i).getName().compareToIgnoreCase(name) == 0)
        return i;
    return -1;
  }

  /**
   * Remove o ProcessStep na posição indicada por index.
   * @param index Posição do ProcessStep para remover.
   */
  public void remove(int index) {
    vector.remove(index);
  }

  /**
   * Retorna a quantidade de ProcessStep´s existentes na lista.
   * @return Retorna a quantidade de ProcessStep´s existentes na lista.
   */
  public int size() {
    return vector.size();
  }

}
