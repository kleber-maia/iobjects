package iobjects.security;

import java.util.*;

/**
 * Representa uma lista de ActionInfo.
 */
public class ActionInfoList {

  private Vector vector = new Vector();

  /**
   * Construtor padr�o.
   */
  public ActionInfoList() {
  }

  /**
   * Adiciona 'actionInfo' a lista.
   * @param actionInfo ActionInfo para ser adicionado a lista.
   */
  public void add(ActionInfo actionInfo) {
    vector.add(actionInfo);
  }

  /**
   * Limpa a lista de ActionInfo.
   */
  public void clear() {
    vector.clear();
  }

  /**
   * Retorna true se existir um ActionInfo com nome igual a 'name', desprezando
   * letras mai�sculas e min�sculas.
   * @param name String Nome do ActionInfo que se deseja verificar a exist�ncia.
   * @return boolean Retorna true se existir um ActionInfo com nome igual a 'name',
   *                 desprezando letras mai�sculas e min�sculas.
   */
  public boolean contains(String name) {
    return indexOf(name) >= 0;
  }

  /**
   * Excluir o ActionInfo referenciado por 'index'.
   * @param index int �ndice do ActionInfo que se deseja excluir.
   */
  public void delete(int index) {
    vector.remove(index);
  }

  /**
   * Retorna o ActionInfo referenciado por 'index'.
   * @param index int �ndice do ActionInfo que se deseja retornar.
   * @return ActionInfo Retorna o ActionInfo referenciado por 'index'.
   */
  public ActionInfo get(int index) {
    return (ActionInfo)vector.elementAt(index);
  }

  /**
   * Retorna o ActionInfo cujo nome seja igual a 'name', desprezando letras
   * mai�sculas e min�sculas.
   * @param name String Nome do ActionInfo que se deseja retornar.
   * @return ActionInfo Retorna o ActionInfo cujo nome seja igual a 'name',
   *                    desprezando letras mai�sculas e min�sculas.
   */
  public ActionInfo get(String name) {
    int index = indexOf(name);
    if (index >= 0)
      return get(index);
    else
      return null;
  }

  /**
   * Retorna o �ndice do ActionInfo cujo nome seja igual a 'name', desprezando
   * letras mai�sculas e min�sculas.
   * @param name String Nome do ActionInfo que se deseja obter o �ndice.
   * @return int Retorna o �ndice do ActionInfo cujo nome seja igual a 'name',
   *         desprezando letras mai�sculas e min�sculas.
   */
  public int indexOf(String name) {
    for (int i=0; i<size(); i++)
      if (get(i).getName().equalsIgnoreCase(name))
        return i;
    return -1;
  }

  /**
   * Retorna a quantidade de ActionInfo existentes na lista.
   * @return int Retorna a quantidade de ActionInfo existentes na lista.
   */
  public int size() {
    return vector.size();
  }

}
