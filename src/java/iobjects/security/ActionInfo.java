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

import iobjects.util.*;

/**
 * Representa a informa��o de seguran�a referente a uma a��o da aplica��o e seus comandos.
 */
public class ActionInfo {

  private String   name        = "";
  private String[] commandList = {};

  /**
   * Construtor padr�o.
   * @param name String Nome da a��o.
   * @param commandList String[] Lista de comandos que podem ser executados
   *                    pela a��o indicada.
   */
  public ActionInfo(String   name,
                    String[] commandList) {
    this.name = name;
    this.commandList = commandList;
  }

  /**
   * Adiciona 'command' a lista de comandos.
   * @param command String Comando para ser adicionado a lista de comandos.
   */
  public void addCommand(String command) {
    // se o comando j� existe na lista...dispara
    if (StringTools.arrayContains(commandList, command))
      return;
    // adiciona a lista de comandos
    commandList = StringTools.arrayConcat(commandList, command);
  }

  /**
   * Adiciona 'commands' a lista de comandos.
   * @param commands String[] Comandos para serem adicionados a lista de comandos.
   */
  public void addCommands(String[] commands) {
    // adiciona os comandos a lista
    for (int i=0; i<commands.length; i++)
      addCommand(commands[i]);
  }

  /**
   * Retorna a lista de comandos que podem ser executados pela a��o indicada.
   * @return String[] Retorna a lista de comandos que podem ser executados pela
   *         a��o indicada.
   */
  public String[] getCommandList() {
    return commandList;
  }

  /**
   * Retorna o nome da a��o.
   * @return String Retorna o nome da a��o.
   */
  public String getName() {
    return name;
  }

}
