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
package iobjects.xml.schedule;

import java.sql.*;
import java.util.*;

import iobjects.xml.sql.*;

/**
 * Representa uma lista arquivos de tarefas agendadas.
 */
public class TaskFiles {

  private Vector vectorNames     = new Vector();
  private Vector vectorTaskFiles = new Vector();

  /**
   * Construtor padrão.
   */
  public TaskFiles() {
  }

  /**
   * Adiciona o TaskFile especificado à lista.
   * @param taskFile TaskFile para adicionar à lista.
   * @param taskName Nome da conexão para futuras referencias.
   * @return Retorna true em caso de sucesso.
   */
  public boolean add(TaskFile taskFile, String taskName) {
    return vectorTaskFiles.add(taskFile) && vectorNames.add(taskName);
  }

  /**
   * Remove todos os TaskFile´s da lista.
   */
  public void clear() {
    while (size() > 0)
      remove(0);
  }

  /**
   * Retorna true se existir um TaskFile com o nome especificado.
   * @param name taskName do Task que se deseja localizar.
   * @return Retorna true se existir um Task com o nome especificado.
   */
  public boolean contains(String name) {
    return indexOf(name) >= 0;
  }

  /**
   * Libera recursos.
   * @throws Throwable
   */
  public void finalize() throws Throwable {
    clear();
    vectorNames = null;
    vectorTaskFiles = null;
  }

  /**
   * Retorna o Task na posição indicada por index.
   * @param index Índice do Task que se dejesa retornar.
   * @return Retorna o Task na posição indicada por index.
   */
  public TaskFile get(int index) {
    return (TaskFile)vectorTaskFiles.get(index);
  }

  /**
   * Retorna o nome da conexão na posição indicada por index.
   * @param index Índice do nome da conexão que se dejesa retornar.
   * @return Retorna o nome da conexão na posição indicada por index.
   */
  public String getName(int index) {
    return (String)vectorNames.get(index);
  }

  /**
   * Retorna o TaskFile com o nome especificado.
   * @param name Nome do TaskFile que se deseja localizar, desprezando
   *             maiúsculas e minúsculas.
   * @return Retorna o TaskFile com o nome especificado.
   */
  public TaskFile get(String name) {
    int index = indexOf(name);
    if (index >= 0)
      return (TaskFile)vectorTaskFiles.get(index);
    else
      return null;
  }

  /**
   * Retorna o índice do TaskFile com o nome especificado.
   * @param name Nome do TaskFile que se deseja localizar,
   *                       desprezando letras maiúsculas e minúsculas.
   * @return Retorna o índice do TaskFile com o nome especificado.
   */
  public int indexOf(String name) {
    for (int i=0; i<size(); i++) {
      if (((String)vectorNames.elementAt(i)).compareToIgnoreCase(name) == 0)
        return i;
    } // for
    return -1;
  }

  /**
   * Remove o TaskFile na posição indicada por index.
   * @param index Posição do TaskFile para remover.
   */
  public void remove(int index) {
    vectorTaskFiles.remove(index);
    vectorNames.remove(index);
  }

  /**
   * Retorna a quantidade de Task´s existentes na lista.
   * @return Retorna a quantidade de Task´s existentes na lista.
   */
  public int size() {
    return vectorTaskFiles.size();
  }

}
