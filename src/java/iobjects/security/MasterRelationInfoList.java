package iobjects.security;

import java.util.*;

/**
 * Representa uma lista de MasterRelationInfo.
 */
public class MasterRelationInfoList {

  private Vector vector = new Vector();

  /**
   * Construtor padr�o.
   */
  public MasterRelationInfoList() {
  }

  /**
   * Adiciona 'masterRelationInfo' a lista.
   * @param masterRelationInfo MasterRelationInfo para ser adicionado a lista.
   */
  public void add(MasterRelationInfo masterRelationInfo) {
    vector.add(masterRelationInfo);
  }

  /**
   * Limpa a lista de MasterRelationInfo.
   */
  public void clear() {
    vector.clear();
  }

  /**
   * Retorna true se existir um MasterRelationInfo com valor igual a 'value',
   * desprezando letras mai�sculas e min�sculas.
   * @param value String Nome do MasterRelationInfo que se deseja verificar a
   *              exist�ncia.
   * @return boolean Retorna true se existir um MasterRelationInfo com valor
   *                 igual a 'value', desprezando letras mai�sculas e min�sculas.
   */
  public boolean contains(String value) {
    return indexOf(value) >= 0;
  }

  /**
   * Excluir o MasterRelationInfo referenciado por 'index'.
   * @param index int �ndice do MasterRelationInfo que se deseja excluir.
   */
  public void delete(int index) {
    vector.remove(index);
  }

  /**
   * Retorna o MasterRelationInfo referenciado por 'index'.
   * @param index int �ndice do MasterRelationInfo que se deseja retornar.
   * @return MasterRelationInfo Retorna o MasterRelationInfo referenciado por
   *                            'index'.
   */
  public MasterRelationInfo get(int index) {
    return (MasterRelationInfo)vector.elementAt(index);
  }

  /**
   * Retorna o MasterRelationInfo cujo valor seja igual a 'value', desprezando
   * letras mai�sculas e min�sculas.
   * @param value String Nome do MasterRelationInfo que se deseja retornar.
   * @return MasterRelationInfo Retorna o MasterRelationInfo cujo valor seja
   *                            igual a 'value', desprezando letras mai�sculas
   *                            e min�sculas.
   */
  public MasterRelationInfo get(String value) {
    int index = indexOf(value);
    if (index >= 0)
      return get(index);
    else
      return null;
  }

  /**
   * Retorna o �ndice do MasterRelationInfo cujo valor seja igual a 'value',
   * desprezando letras mai�sculas e min�sculas.
   * @param value String Nome do MasterRelationInfo que se deseja obter o �ndice.
   * @return int Retorna o �ndice do MasterRelationInfo cujo valor seja igual a
   *             'value', desprezando letras mai�sculas e min�sculas.
   */
  public int indexOf(String value) {
    for (int i=0; i<size(); i++)
      if (get(i).getValue().equalsIgnoreCase(value))
        return i;
    return -1;
  }

  /**
   * Retorna a quantidade de MasterRelationInfo existentes na lista.
   * @return int Retorna a quantidade de MasterRelationInfo existentes na lista.
   */
  public int size() {
    return vector.size();
  }

}
