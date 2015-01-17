package iobjects.security;

import iobjects.util.*;

/**
 * Representa a informação de segurança referente a uma ação da aplicação e seus comandos.
 */
public class ActionInfo {

  private String   name        = "";
  private String[] commandList = {};

  /**
   * Construtor padrão.
   * @param name String Nome da ação.
   * @param commandList String[] Lista de comandos que podem ser executados
   *                    pela ação indicada.
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
    // se o comando já existe na lista...dispara
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
   * Retorna a lista de comandos que podem ser executados pela ação indicada.
   * @return String[] Retorna a lista de comandos que podem ser executados pela
   *         ação indicada.
   */
  public String[] getCommandList() {
    return commandList;
  }

  /**
   * Retorna o nome da ação.
   * @return String Retorna o nome da ação.
   */
  public String getName() {
    return name;
  }

}
