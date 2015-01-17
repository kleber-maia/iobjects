package iobjects.entity;

import java.util.*;

/**
 * Representa uma lista de EntityLookupValue's.
 * @since 3.1
 */
public class EntityLookupValueList {

  private Vector vector = new Vector();

  /**
   * Construtor padr�o.
   */
  public EntityLookupValueList() {
  }

  /**
   * Adiciona o EntityLookupValue especificado � lista.
   * @param entityLookupValue EntityLookupValue para adicionar � lista.
   */
  public void add(EntityLookupValue entityLookupValue) {
    if (!contains(entityLookupValue.getLookup()))
      vector.add(entityLookupValue);
  }

  /**
   * Remove todos os EntityLookupValue�s da lista.
   */
  public void clear() {
    vector.clear();
  }

  /**
   * Retorna true se existir um EntityLookupValue com o EntityLookup especificado.
   * @param entityLookup EntityLookup que se deseja localizar.
   * @return Retorna true se existir um EntityLookupValue com o EntityLookup
   * especificado.
   */
  public boolean contains(EntityLookup entityLookup) {
    return indexOf(entityLookup) >= 0;
  }

  /**
   * Retorna o EntityLookupValue na posi��o indicada por 'index'.
   * @param index �ndice do EntityLookupValue que se dejesa retornar.
   * @return Retorna o EntityLookupValue na posi��o indicada por 'index'.
   */
  public EntityLookupValue get(int index) {
    return (EntityLookupValue)vector.get(index);
  }

  /**
   * Retorna o EntityLookupValue com o EntityLookup especificado.
   * @param entityLookup EntityLookup que se deseja localizar.
   * @return Retorna o EntityLookupValue com o EntityLookup especificado.
   */
  public EntityLookupValue get(EntityLookup entityLookup) {
    int index = indexOf(entityLookup);
    if (index >= 0)
      return (EntityLookupValue)vector.get(index);
    else
      return null;
  }

  /**
   * Retorna o �ndice do EntityLookupValue com o EntityLookup especificado.
   * @param entityLookup EntityLookup que se deseja localizar.
   * @return Retorna o �ndice do EntityLookup com o EntityLookup especificado.
   */
  public int indexOf(EntityLookup entityLookup) {
    for (int i=0; i<size(); i++)
      if (get(i).getLookup().getName().equals(entityLookup.getName()))
        return i;
    return -1;
  }

  /**
   * Remove o EntityLookupValue na posi��o indicada por index.
   * @param index Posi��o do EntityLookupValue para remover.
   */
  public void remove(int index) {
    vector.remove(index);
  }

  /**
   * Retorna a quantidade de EntityLookupValue�s existentes na lista.
   * @return Retorna a quantidade de EntityLookupValue�s existentes na lista.
   */
  public int size() {
    return vector.size();
  }

}
