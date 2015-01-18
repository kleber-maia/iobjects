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

/**
 * Representa um comando que pode ser executado em um Action.
 * @since 3.2
 */
public class Command {

  /**
   * Nome padr�o para comandos cuja finalidade � excluir.
   */
  static public final String COMMAND_DELETE = "DELETE";
  /**
   * Nome padr�o para comandos cuja finalidade � editar.
   */
  static public final String COMMAND_EDIT = "EDIT";
  /**
   * Nome padr�o para comandos cuja finalidade � executar.
   */
  static public final String COMMAND_EXECUTE = "EXECUTE";
  /**
   * Nome padr�o para comandos cuja finalidade � inserir.
   */
  static public final String COMMAND_INSERT = "INSERT";
  /**
   * Nome padr�o para comandos cuja finalidade � criar relat�rio.
   */
  static public final String COMMAND_MAKE_REPORT = "MAKE_REPORT";
  /**
   * Nome padr�o para comandos cuja finalidade � avan�ar.
   */
  static public final String COMMAND_NEXT = "NEXT";
  /**
   * Nome padr�o para comandos cuja finalidade � retroceder.
   */
  static public final String COMMAND_PREVIOUS = "PREVIOUS";
  /**
   * Nome padr�o para comandos cuja finalidade � imprimir.
   */
  static public final String COMMAND_PRINT = "PRINT";
  /**
   * Nome padr�o para comandos cuja finalidade � reiniciar.
   */
  static public final String COMMAND_RESTART = "RESTART";
  /**
   * Nome padr�o para comandos cuja finalidade � salvar.
   */
  static public final String COMMAND_SAVE = "SAVE";
  /**
   * Nome padr�o para comandos cuja finalidade � copiar.
   */
  static public final String COMMAND_COPY = "COPY";
  /**
   * Nome padr�o para comandos cuja finalidade � pesquisar.
   */
  static public final String COMMAND_SEARCH = "SEARCH";

  private Action action      = null;
  private String caption     = "";
  private String description = "";
  private String name        = "";

  /**
   * Construtor padr�o.
   * @param name String Nome do comando.
   * @param caption String T�tulo do comando.
   * @param description String Descri��o do comando.
   */
  public Command(String name,
                 String caption,
                 String description) {
    this.name = name;
    this.caption = caption;
    this.description = description;
  }

  /**
   * Libera recursos.
   */
  public void finalize() {
    action      = null;
    caption     = null;
    description = null;
    name        = null;
  }

  /**
   * Retorna o Action que mant�m o comando.
   * @return Action que mant�m o comando.
   */
  public Action getAction() {
    return action;
  }

  /**
   * Retorna o t�tulo do comando.
   * @return String Retorna o t�tulo do comando.
   */
  public String getCaption() {
    return caption;
  }

  /**
   * Retorna a descri��o do comando.
   * @return String Retorna a descri��o do comando.
   */
  public String getDescription() {
    return description;
  }

  /**
   * Retorna o nome do comando.
   * @return String Retorna o nome do comando.
   */
  public String getName() {
    return name;
  }

  /**
   * Define o Action que mant�m o comando.
   * @param action Action que mant�m o comando.
   */
  public void setAction(Action action) {
    this.action = action;
  }

  /**
   * Define o titulo do comando.
   * @param caption String T�tulo do comando.
   */
  public void setCaption(String caption) {
    this.caption = caption;
  }

  /**
   * Define a descri��o do comando.
   * @param description String Descri��o do comando.
   */
  public void setDescription(String description) {
    this.description = description;
  }

}
