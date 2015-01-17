package iobjects;

/**
 * Representa um comando que pode ser executado em um Action.
 * @since 3.2
 */
public class Command {

  /**
   * Nome padrão para comandos cuja finalidade é excluir.
   */
  static public final String COMMAND_DELETE = "DELETE";
  /**
   * Nome padrão para comandos cuja finalidade é editar.
   */
  static public final String COMMAND_EDIT = "EDIT";
  /**
   * Nome padrão para comandos cuja finalidade é executar.
   */
  static public final String COMMAND_EXECUTE = "EXECUTE";
  /**
   * Nome padrão para comandos cuja finalidade é inserir.
   */
  static public final String COMMAND_INSERT = "INSERT";
  /**
   * Nome padrão para comandos cuja finalidade é criar relatório.
   */
  static public final String COMMAND_MAKE_REPORT = "MAKE_REPORT";
  /**
   * Nome padrão para comandos cuja finalidade é avançar.
   */
  static public final String COMMAND_NEXT = "NEXT";
  /**
   * Nome padrão para comandos cuja finalidade é retroceder.
   */
  static public final String COMMAND_PREVIOUS = "PREVIOUS";
  /**
   * Nome padrão para comandos cuja finalidade é imprimir.
   */
  static public final String COMMAND_PRINT = "PRINT";
  /**
   * Nome padrão para comandos cuja finalidade é reiniciar.
   */
  static public final String COMMAND_RESTART = "RESTART";
  /**
   * Nome padrão para comandos cuja finalidade é salvar.
   */
  static public final String COMMAND_SAVE = "SAVE";
  /**
   * Nome padrão para comandos cuja finalidade é copiar.
   */
  static public final String COMMAND_COPY = "COPY";
  /**
   * Nome padrão para comandos cuja finalidade é pesquisar.
   */
  static public final String COMMAND_SEARCH = "SEARCH";

  private Action action      = null;
  private String caption     = "";
  private String description = "";
  private String name        = "";

  /**
   * Construtor padrão.
   * @param name String Nome do comando.
   * @param caption String Título do comando.
   * @param description String Descrição do comando.
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
   * Retorna o Action que mantém o comando.
   * @return Action que mantém o comando.
   */
  public Action getAction() {
    return action;
  }

  /**
   * Retorna o título do comando.
   * @return String Retorna o título do comando.
   */
  public String getCaption() {
    return caption;
  }

  /**
   * Retorna a descrição do comando.
   * @return String Retorna a descrição do comando.
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
   * Define o Action que mantém o comando.
   * @param action Action que mantém o comando.
   */
  public void setAction(Action action) {
    this.action = action;
  }

  /**
   * Define o titulo do comando.
   * @param caption String Título do comando.
   */
  public void setCaption(String caption) {
    this.caption = caption;
  }

  /**
   * Define a descrição do comando.
   * @param description String Descrição do comando.
   */
  public void setDescription(String description) {
    this.description = description;
  }

}
