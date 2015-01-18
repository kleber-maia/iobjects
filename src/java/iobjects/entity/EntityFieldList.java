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
 * Representa uma lista de EntityField's.
 * @since 3.1
 */
public class EntityFieldList {

  private Vector vector = new Vector();

  /**
   * Construtor padrão.
   */
  public EntityFieldList() {
  }

  /**
   * Adiciona o EntityField especificado à lista.
   * @param entityField EntityField para adicionar à lista.
   */
  public void add(EntityField entityField) {
    if (!contains(entityField.getFieldName()))
      vector.add(entityField);
  }

  /**
   * Remove todos os EntityField´s da lista.
   */
  public void clear() {
    vector.clear();
  }

  /**
   * Retorna true se existir um EntityField com o fieldName especificado.
   * @param fieldName fieldName do EntityField que se deseja localizar,
   *                  desprezando letras maiúsculas e minúsculas.
   * @return Retorna true se existir um EntityField com o fieldName especificado.
   */
  public boolean contains(String fieldName) {
    return indexOf(fieldName) >= 0;
  }

  /**
   * Retorna o EntityField na posição indicada por 'index'.
   * @param index Índice do EntityField que se dejesa retornar.
   * @return Retorna o EntityField na posição indicada por 'index'.
   */
  public EntityField get(int index) {
    return (EntityField)vector.get(index);
  }

  /**
   * Retorna o EntityField com o fieldName especificado.
   * @param fieldName fieldName do EntityField que se deseja localizar,
   *                  desprezando letras maiúsculas e minúsculas.
   * @return Retorna o EntityField com o fieldName especificado.
   */
  public EntityField get(String fieldName) {
    int index = indexOf(fieldName);
    if (index >= 0)
      return (EntityField)vector.get(index);
    else
      return null;
  }

  /**
   * Retorna o EntityField com o alias especificado.
   * @param alias Alias do EntityField que se deseja localizar,
   *              desprezando letras maiúsculas e minúsculas.
   * @return Retorna o EntityField com o alias especificado.
   */
  public EntityField getByAlias(String alias) {
    // procura o EntityField pelo alias
    int index = -1;
    for (int i=0; i<size(); i++) {
      if (get(i).getFieldAlias().compareToIgnoreCase(alias) == 0) {
        index = i;
        break;
      } // if
    } // for
    // retorna
    if (index >= 0)
      return (EntityField)vector.get(index);
    else
      return null;
  }

  /**
   * Retorna um String[] contendo os nomes dos campos contidos na lista.
   * @return String[] Contendo os nomes dos campos contidos na lista.
   */
  public String[] getFieldNames() {
    return getFieldNames(true);
  }

  /**
   * Retorna um String[] contendo os nomes dos campos contidos na lista.
   * @param readOnly True para que os campos ReadOnly sejam incluídos.
   * @return String[] Contendo os nomes dos campos contidos na lista.
   */
  public String[] getFieldNames(boolean readOnly) {
    // loop nos campos
    Vector vector = new Vector();
    for (int i=0; i<size(); i++) {
      // campo da vez
      EntityField field = get(i);
      // readOnly?
      if (field.getReadOnly() && !readOnly)
        continue;
      // põe na lista
      vector.add(field.getFieldName());
    } // for
    // retorna
    String[] result = new String[vector.size()];
    vector.copyInto(result);
    return result;
  }

  /**
   * Retorna um EntityField[] contendo os campos chave da entidade.
   * @return EntityField[] contendo os campos chave da entidade.
   */
  public EntityField[] getKeyFields() {
    // loop nos campos
    Vector vector = new Vector();
    for (int i=0; i<size(); i++) {
      // campo da vez
      EntityField field = get(i);
      // se não é chave...continua
      if (!field.getIsKey())
        continue;
      // põe na lista
      vector.add(field);
    } // for
    // retorna
    EntityField[] result = new EntityField[vector.size()];
    vector.copyInto(result);
    return result;
  }

  /**
   * Retorna o índice do EntityField com o fieldName especificado.
   * @param fieldName fieldName do EntityField que se deseja localizar,
   *                  desprezando letras maiúsculas e minúsculas.
   * @return Retorna o índice do EntityField com o fieldName especificado.
   */
  public int indexOf(String fieldName) {
    for (int i=0; i<size(); i++)
      if (get(i).getFieldName().compareToIgnoreCase(fieldName) == 0)
        return i;
    return -1;
  }

  /**
   * Remove o EntityField na posição indicada por index.
   * @param index Posição do EntityField para remover.
   */
  public void remove(int index) {
    vector.remove(index);
  }

  /**
   * Retorna a quantidade de EntityField´s existentes na lista.
   * @return Retorna a quantidade de EntityField´s existentes na lista.
   */
  public int size() {
    return vector.size();
  }

}
