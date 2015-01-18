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
package iobjects;

import java.util.*;

/**
 * Representa uma lista de comandos.
 * @since 3.2
 */
public class CommandList {

  private Action action = null;
  private Vector vector = new Vector();

  /**
   * Construtor padrão.
   * @param action Action Ação que mantém a lista de comandos.
   */
  public CommandList(Action action) {
    this.action = action;
  }

  /**
   * Adiciona o Command à lista.
   * @param command Command para ser adicionado à lista.
   */
  public void add(Command command) {
    command.setAction(action);
    vector.add(command);
  }

  /**
   * Limpa a lista de comandos.
   */
  public void clear() {
    vector.clear();
  }

  /**
   * Libera recursos.
   */
  public void finalize() {
    action = null;
    vector = null;
  }

  /**
   * Retorna o Command referente ao índice informado.
   * @param index int Índice do Command que se deseja retornar.
   * @return Command Retorna o Command referente ao índice informado.
   */
  public Command get(int index) {
    return (Command)vector.elementAt(index);
  }

  /**
   * Retorna o Command referente ao nome informado.
   * @param name String Nome do Command que se deseja retornar.
   * @return Command Retorna o Command referente ao nome informado.
   */
  public Command get(String name) {
    Command result = null;
    for (int i=0; i<vector.size(); i++) {
      result = get(i);
      if (result.getName().equalsIgnoreCase(name))
        return result;
    } // for
    return null;
  }

  /**
   * Retorna o tamanho da lista.
   * @return int Retorna o tamanho da lista.
   */
  public int size() {
    return vector.size();
  }

}
