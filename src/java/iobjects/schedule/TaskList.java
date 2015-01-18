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
package iobjects.schedule;

import java.util.*;

/**
 * Representa uma lista de Task's.
 * @since 2006
 */
public class TaskList {

  private Vector vector = new Vector();

  /**
   * Construtor padrão.
   */
  public TaskList() {
  }

  /**
   * Adiciona 'task' na lista.
   * @param task Task para ser adicionado.
   */
  public void add(Task task) {
    if (!contains(task))
      vector.add(task);
  }

  /**
   * Remove todos os Task´s da lista.
   */
  public void clear() {
    vector.clear();
  }

  /**
   * Retorna true se existir um Task com o 'name' especificado.
   * @param task Task que se deseja localizar.
   * @return Retorna true se existir um Task com o 'name' especificado.
   */
  public boolean contains(Task task) {
    return indexOf(task) >= 0;
  }

  /**
   * Libera recursos.
   */
  public void finalize() {
    clear();
    vector = null;
  }

  /**
   * Retorna o Task na posição indicada por 'index'.
   * @param index Índice do Task que se dejesa retornar.
   * @return Retorna o Task na posição indicada por 'index'.
   */
  public Task get(int index) {
    return (Task)vector.get(index);
  }

  /**
   * Retorna o Task indentificado por 'name'.
   * @param name String Nome do Task que se deseja retornar.
   * @return Retorna o Task indentificado por 'name'.
   */
  public Task get(String name) {
    for (int i=0; i<size(); i++)
      if (get(i).getName().equals(name))
        return get(i);
    return null;
  }

  /**
   * Retorna o índice do Task com o 'name' especificado.
   * @param task Task cujo índice se deseja obter.
   * @return Retorna o índice do Task com o 'name' especificado.
   */
  public int indexOf(Task task) {
    for (int i=0; i<size(); i++)
      if (get(i).equals(task))
        return i;
    return -1;
  }

  /**
   * Remove o Task na posição indicada por 'index'.
   * @param index Posição do Task para remover.
   */
  public void remove(int index) {
    vector.remove(index);
  }

  /**
   * Retorna a quantidade de Task´s existentes na lista.
   * @return Retorna a quantidade de Task´s existentes na lista.
   */
  public int size() {
    return vector.size();
  }

}
