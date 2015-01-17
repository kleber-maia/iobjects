package iobjects.entity;

import java.util.*;

/**
 * Representa uma lista de EntityLookup's.
 * @since 3.1
 */
public class EntityLookupList {

  private Vector vector = new Vector();

  /**
   * Construtor padr�o.
   */
  public EntityLookupList() {
  }

  /**
   * Adiciona o EntityLookup especificado � lista.
   * @param entityLookup EntityLookup para adicionar � lista.
   */
  public void add(EntityLookup entityLookup) {
    if (!contains(entityLookup.getName()))
      vector.add(entityLookup);
  }

  /**
   * Remove todos os EntityLookup�s da lista.
   */
  public void clear() {
    vector.clear();
  }

  /**
   * Retorna true se existir um EntityLookup com o nome especificado.
   * @param name Nome do EntityLookup que se deseja localizar,
   *             desprezando letras mai�sculas e min�sculas.
   * @return Retorna true se existir um EntityLookup com o nome especificado.
   */
  public boolean contains(String name) {
    return indexOf(name) >= 0;
  }

  /**
   * Retorna o EntityLookup na posi��o indicada por 'index'.
   * @param index �ndice do EntityLookup que se dejesa retornar.
   * @return Retorna o EntityLookup na posi��o indicada por 'index'.
   */
  public EntityLookup get(int index) {
    return (EntityLookup)vector.get(index);
  }

  /**
   * Retorna o EntityLookup com o nome especificado.
   * @param name Nome do EntityLookup que se deseja localizar,
   *             desprezando letras mai�sculas e min�sculas.
   * @return Retorna o EntityLookup com o nome especificado.
   */
  public EntityLookup get(String name) {
    int index = indexOf(name);
    if (index >= 0)
      return (EntityLookup)vector.get(index);
    else
      return null;
  }

  /**
   * Retorna o �ndice do EntityLookup com o nome especificado.
   * @param name Nome do EntityLookup que se deseja localizar,
   *             desprezando letras mai�sculas e min�sculas.
   * @return Retorna o �ndice do EntityLookup com o nome especificado.
   */
  public int indexOf(String name) {
    for (int i=0; i<size(); i++)
      if (get(i).getName().compareToIgnoreCase(name) == 0)
        return i;
    return -1;
  }

  /**
   * Remove o EntityLookup na posi��o indicada por index.
   * @param index Posi��o do EntityLookup para remover.
   */
  public void remove(int index) {
    vector.remove(index);
  }

  /**
   * Retorna a quantidade de EntityLookup�s existentes na lista.
   * @return Retorna a quantidade de EntityLookup�s existentes na lista.
   */
  public int size() {
    return vector.size();
  }

}
