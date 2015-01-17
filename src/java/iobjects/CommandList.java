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
