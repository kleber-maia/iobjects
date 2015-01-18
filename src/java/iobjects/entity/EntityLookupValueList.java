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
package iobjects.entity;

import java.util.*;

/**
 * Representa uma lista de EntityLookupValue's.
 * @since 3.1
 */
public class EntityLookupValueList {

  private Vector vector = new Vector();

  /**
   * Construtor padrão.
   */
  public EntityLookupValueList() {
  }

  /**
   * Adiciona o EntityLookupValue especificado à lista.
   * @param entityLookupValue EntityLookupValue para adicionar à lista.
   */
  public void add(EntityLookupValue entityLookupValue) {
    if (!contains(entityLookupValue.getLookup()))
      vector.add(entityLookupValue);
  }

  /**
   * Remove todos os EntityLookupValue´s da lista.
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
   * Retorna o EntityLookupValue na posição indicada por 'index'.
   * @param index Índice do EntityLookupValue que se dejesa retornar.
   * @return Retorna o EntityLookupValue na posição indicada por 'index'.
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
   * Retorna o índice do EntityLookupValue com o EntityLookup especificado.
   * @param entityLookup EntityLookup que se deseja localizar.
   * @return Retorna o índice do EntityLookup com o EntityLookup especificado.
   */
  public int indexOf(EntityLookup entityLookup) {
    for (int i=0; i<size(); i++)
      if (get(i).getLookup().getName().equals(entityLookup.getName()))
        return i;
    return -1;
  }

  /**
   * Remove o EntityLookupValue na posição indicada por index.
   * @param index Posição do EntityLookupValue para remover.
   */
  public void remove(int index) {
    vector.remove(index);
  }

  /**
   * Retorna a quantidade de EntityLookupValue´s existentes na lista.
   * @return Retorna a quantidade de EntityLookupValue´s existentes na lista.
   */
  public int size() {
    return vector.size();
  }

}
