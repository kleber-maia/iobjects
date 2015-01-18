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
package iobjects.security;

import java.util.*;

/**
 * Representa uma lista de ActionInfo.
 */
public class ActionInfoList {

  private Vector vector = new Vector();

  /**
   * Construtor padrão.
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
   * letras maiúsculas e minúsculas.
   * @param name String Nome do ActionInfo que se deseja verificar a existência.
   * @return boolean Retorna true se existir um ActionInfo com nome igual a 'name',
   *                 desprezando letras maiúsculas e minúsculas.
   */
  public boolean contains(String name) {
    return indexOf(name) >= 0;
  }

  /**
   * Excluir o ActionInfo referenciado por 'index'.
   * @param index int Índice do ActionInfo que se deseja excluir.
   */
  public void delete(int index) {
    vector.remove(index);
  }

  /**
   * Retorna o ActionInfo referenciado por 'index'.
   * @param index int Índice do ActionInfo que se deseja retornar.
   * @return ActionInfo Retorna o ActionInfo referenciado por 'index'.
   */
  public ActionInfo get(int index) {
    return (ActionInfo)vector.elementAt(index);
  }

  /**
   * Retorna o ActionInfo cujo nome seja igual a 'name', desprezando letras
   * maiúsculas e minúsculas.
   * @param name String Nome do ActionInfo que se deseja retornar.
   * @return ActionInfo Retorna o ActionInfo cujo nome seja igual a 'name',
   *                    desprezando letras maiúsculas e minúsculas.
   */
  public ActionInfo get(String name) {
    int index = indexOf(name);
    if (index >= 0)
      return get(index);
    else
      return null;
  }

  /**
   * Retorna o índice do ActionInfo cujo nome seja igual a 'name', desprezando
   * letras maiúsculas e minúsculas.
   * @param name String Nome do ActionInfo que se deseja obter o índice.
   * @return int Retorna o índice do ActionInfo cujo nome seja igual a 'name',
   *         desprezando letras maiúsculas e minúsculas.
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
