package iobjects.ui.entity;

import java.util.*;

import javax.servlet.http.*;

import iobjects.*;
import iobjects.entity.*;
import iobjects.ui.*;

/**
 * Representa uma tabela de dados gerada a partir de um Entity para ser exibida
 * em uma tela de dados.
 */
public class EntityGrid {

  static public final byte SELECT_TYPE_NONE     = Grid.SELECT_NONE;
  static public final byte SELECT_TYPE_SINGLE   = Grid.SELECT_SINGLE;
  static public final byte SELECT_TYPE_MULTIPLE = Grid.SELECT_MULTIPLE;

  static public final byte OPERATION_NONE   = 0;
  static public final byte OPERATION_INSERT = 1;
  static public final byte OPERATION_UPDATE = 2;

  public class Column {
    private Action       action        = null;
    private Command      command       = null;
    private EntityField  field         = null;
    private String       image         = "";
    private EntityLookup lookup        = null;
    private String       target        = "";
    private boolean      addToRecents  = false;
    private int          width         = 0;
    /**
     * Adiciona uma coluna ao EntityGrid baseada em 'field'.
     * @param field EntityField cuja coluna irá representar.
     * @param width int Largura da coluna.
     */
    public Column(EntityField  field,
                  int          width) {
      this.field = field;
      this.width = width;
    }
    /**
     * Adiciona uma coluna ao EntityGrid baseada em 'field'.
     * @param field EntityField cuja coluna irá representar.
     * @param width int Largura da coluna.
     * @param action Action Ação de destino para ser executada pelo
     *               Controller.
     * @param command Command Comando para ser executado na ação de destino.
     * @param target String Nome do Frame de destino.
     * @param addToRecents boolean True para adicionar a lista de itens recentes.
     *                     É necessário informar o Action e o Command.
     */
    public Column(EntityField  field,
                  int          width,
                  Action       action,
                  Command      command,
                  String       target,
                  boolean      addToRecents) {
      this.field = field;
      this.width = width;
      this.action = action;
      this.command = command;
      this.target = target;
      this.addToRecents = addToRecents;
    }
  /**
   * Adiciona uma coluna ao EntityGrid baseada em 'lookup'.
   * @param lookup EntityLookup para pesquisa do valor a ser exibido.
   * @param width int Largura da coluna.
   */
    public Column(EntityLookup lookup,
                  int          width) {
      this.lookup = lookup;
      this.width = width;
    }
  /**
   * Adiciona uma coluna ao EntityGrid baseada em 'lookup'.
   * @param lookup EntityLookup para pesquisa do valor a ser exibido.
   * @param width int Largura da coluna.
   * @param action Action Ação de destino para ser executada pelo
   *               Controller.
   * @param command Command Comando para ser executado na ação de destino.
   * @param target String Nome do Frame de destino.
   */
    public Column(EntityLookup lookup,
                  int          width,
                  Action       action,
                  Command      command,
                  String       target,
                  boolean      addToRecents) {
      this.lookup = lookup;
      this.width = width;
      this.action = action;
      this.command = command;
      this.target = target;
      this.addToRecents = addToRecents;
    }
    public Action       getAction()  {return action;}
    public Command      getCommand() {return command;}
    public EntityField  getField()   {return field;}
    public String       getImage()   {return image;}
    public EntityLookup getLookup()  {return lookup;}
    public boolean      getAddToRecents() {return addToRecents;}
    public String       getTarget()  {return target;}
    public int          getWidth()   {return width;}
    // *
    public void         setImage(String image) {this.image = image;}
  }

  /**
   * Representa a interface de eventos da classe EntityGrid.
   */
  public interface EventListener {

    /**
     * Evento chamado quando uma célula é adicionada ao EntityGrid. Deve retornar
     * o novo valor a ser exibido na célula ou 'value'.
     * @param entityInfo EntityInfo referente à linha da célula que está sendo
     *                   adicionada.
     * @param entityField EntityField referente ao campo da célula que esta sendo
     *                    adicionada, caso haja um.
     * @param entityLookup EntityLookup referente ao lookup da célula que esta sendo
     *                     adicionada, caso haja um.
     * @param value String Valor formatado que será exibido na célula.
     * @return String Evento chamado quando uma célula é adicionada ao EntityGrid.
     *                Deve retornar o novo valor a ser exibido na célula ou
     *                'value'.
     */
    public String onAddCell(EntityInfo   entityInfo,
                            EntityField  entityField,
                            EntityLookup entityLookup,
                            String       value);

  }

  private Entity entity         = null;
  private Vector eventListeners = new Vector();
  private Facade facade         = null;
  private Grid   grid           = null;
  private Vector columns        = new Vector();

  /**
   * Construtor padrão.
   * @param facade Facade Fachada.
   * @param entity Entity Entidade de origem dos dados.
   * @param id String Identificação do EntityGrid para ser utilizada em scripts.
   * @param height Altura do Grid na página em pixels ou 0 para se ajustar ao seu container.
   * @param width Largura do Grid na página em pixels ou 0 para se ajustar ao seu container.
   */
  private EntityGrid(Facade  facade,
                     Entity  entity,
                     int     height,
                     int     width) {
    // nossos valores
    this.facade = facade;
    this.entity = entity;
    // nossa chave
    EntityField[] keyFields = entity.fieldList().getKeyFields();
    String[] keys = new String[keyFields.length];
    for (int i=0; i<keyFields.length; i++)
      keys[i] = keyFields[i].getFieldAlias();
    // nosso Grid
    grid = new Grid(facade, "grid" + entity.getTableName(), keys, Grid.SELECT_MULTIPLE, height, width);
    grid.setConstraint(grid.selectedIndex() + ".length > 0");
    grid.setConstraintMessage("Nenhum registro selecionado.");
  }

  /**
   * Adiciona uma coluna ao EntityGrid baseada em 'field'.
   * @param field EntityField cuja coluna irá representar.
   * @param width int Largura da coluna.
   */
  public void addColumn(EntityField field,
                        int         width) {
    addColumn(field, width, null, null, "", false);
  }

  /**
   * Adiciona uma coluna ao EntityGrid baseada em 'field'.
   * @param field EntityField cuja coluna irá representar.
   * @param width int Largura da coluna.
   * @param action Action Ação de destino para ser executada pelo
   *               Controller.
   * @param command Command Comando para ser executado na ação de destino.
   * @param target String Nome do Frame de destino.
   * @param addToRecents boolean True para adicionar a lista de itens recentes.
   *                     É necessário informar o Action e o Command.
   */
  public void addColumn(EntityField field,
                        int         width,
                        Action      action,
                        Command     command,
                        String      target,
                        boolean     addToRecents) {
    for (int i=0; i<columns.size(); i++)
      if (((Column)columns.elementAt(i)).getField() == field)
        return;
    Column column = new Column(field, width, action, command, target, addToRecents);
    columns.add(column);
  }

  /**
   * Adiciona uma coluna ao EntityGrid baseada em 'lookup'.
   * @param lookup EntityLookup para pesquisa do valor a ser exibido.
   * @param width int Largura da coluna.
   */
  public void addColumn(EntityLookup lookup,
                        int          width) {
    addColumn(lookup, width, null, null, "");
  }

  /**
   * Adiciona uma coluna ao EntityGrid baseada em 'lookup'.
   * @param lookup EntityLookup para pesquisa do valor a ser exibido.
   * @param width int Largura da coluna.
   * @param action Action Ação de destino para ser executada pelo
   *               Controller.
   * @param command Command Comando para ser executado na ação de destino.
   * @param target String Nome do Frame de destino.
   */
  public void addColumn(EntityLookup lookup,
                        int          width,
                        Action       action,
                        Command      command,
                        String       target) {
    for (int i=0; i<columns.size(); i++)
      if (((Column)columns.elementAt(i)).getLookup() == lookup)
        return;
    Column column = new Column(lookup,
                               width,
                               action,
                               command,
                               target,
                               false);
    columns.add(column);
  }
  
  /**
   * Adiciona um objeto que implente a interface EventListener para ser responder
   * aos eventos do EntityGrid.
   * @param eventListener EventListener Instância do objeto que implementa a
   *                      interface EventListener.
   */
  public void addEventListener(EventListener eventListener) {
    String className = eventListener.getClass().getName();
    for (int i=0; i<eventListeners.size(); i++)
      if (eventListeners.elementAt(i).getClass().getName().equals(className))
        return;
    eventListeners.add(eventListener);
  }

  /**
   * Retorna o código em JavaSript para adicionar uma linha ao final do Grid.
   * @param entityInfo EntityInfo que representa a linha de dados que se deseja
   *                   adicionar.
   * @return Retorna o código em JavaSript para adicionar uma linha ao final do Grid.
   * @throws Exception Em caso de exceção na tentativa de obter os valores para exibir.
   */
  public String addRow(EntityInfo entityInfo) throws Exception {
    return grid.addRow(values(this, entityInfo), keyValues(this, entityInfo)) + "Grid_changeStatus(\"" + grid.id() + "\", Grid_rowCount(\"" + grid.id() + "\") + \" registros\");";
  }

  /**
   * Retorna o código em JavaScript que altera o conteúdo da barra de status
   * do Grid.
   * <b>Se foram inseridos elementos HTML na barra de status, como TABLE, DIV,
   * etc, este método não será capaz de substitui-los. Se desejar alterar apenas
   * textos contidos em tais elementos, proceda da seguinte forma:
   * document.getElementById("seuElemento").innerHTML = "Novo texto!".</b>
   * @param status Novo conteúdo para ser exibido na barra de status do Grid.
   * @return Retorna o código em JavaScript que altera o conteúdo da barra de
   *         status do Grid.
   */
  public String changeStatus(String status) {
    return grid.changeStatus(status);
  }

  /**
   * Retorna o código em JavaScript seleciona todas as linhas do Grid.
   * @return Retorna o código em JavaScript seleciona todas as linhas do Grid.
   */
  public String checkAll() {
    return grid.checkAll();
  }

  /**
   * Retorna o código em JavaScript que inverte a seleção de todas as linhas do Grid.
   * @return Retorna o código em JavaScript que inverte a seleção de todas as linhas do Grid.
   */
  public String checkInvert() {
    return grid.checkInvert();
  }

  /**
   * Retorna o código em JavaScript que copia todas as linhas e valores do Grid
   * para a área de transferência.
   * @return Retorna o código em JavaScript que copia todas as linhas e valores
   *         do Grid para a área de transferência.
   */
  public String copyToClipboard() {
    return grid.copyToClipboard();
  }

  /**
   * Retorna o código em JavaScript para exclusão de uma linha específica do Grid.
   * @param index Índice da linha que se deseja excluir.
   * @return Retorna o código em JavaScript para exclusão de uma linha específica
   *         do Grid.
   */
  public String deleteRow(int index) {
    return grid.deleteRow(index) + "Grid_changeStatus(\"" + grid.id() + "\", Grid_rowCount(\"" + grid.id() + "\") + \" registros\");";
  }

  /**
   * Retorna o código em JavaScript para exclusão de uma linha específica do Grid.
   * @param index Nome da variável local em JavaScript contendo o índice da
   *              linha que se deseja excluir.
   * @return Retorna o código em JavaScript para exclusão de uma linha específica
   *         do Grid.
   */
  public String deleteRow(String index) {
    return grid.deleteRow(index) + "Grid_changeStatus(\"" + grid.id() + "\", Grid_rowCount(\"" + grid.id() + "\") + \" registros\");";
  }

  /**
   * Retorna o código em JavaScript para exclusão de uma linha de dados do Grid.
   * @param entityInfo EntityInfo que representa a linha de dados que se deseja
   *                   excluir.
   * @return Retorna o código em JavaScript para exclusão de uma linha de dados
   *         do Grid.
   * @throws Exception Em caso de exceção na tentativa de obter os valores para exibir.
   */
  public String deleteRow(EntityInfo entityInfo) throws Exception {
    return grid.deleteRow(grid.rowIndex(keyValues(this, entityInfo))) + "Grid_changeStatus(\"" + grid.id() + "\", Grid_rowCount(\"" + grid.id() + "\") + \" registros\");";
  }

  /**
   * Retorna o código em JavaScript para exclusão da(s) linha(s) selecionada(s)
   * no Grid.
   * @return Retorna o código em JavaScript para exclusão da(s) linha(s) selecionada(s)
   *         no Grid.
   */
  public String deleteSelectedRow() {
    return grid.deleteSelectedRow() + "Grid_changeStatus(\"" + grid.id() + "\", Grid_rowCount(\"" + grid.id() + "\") + \" registros\");";
  }

  /**
   * Retorna uma instância existente de EntityGrid associada a 'entity'.
   * @param facade Facade Fachada.
   * @param entity Entity cuja instância associada de EntityGrid se deseja retornar.
   * @return Retorna uma instância existente de EntityGrid associada a 'entity'.
   */
  static public EntityGrid getInstance(Facade facade, Entity entity) {
    // procura por uma instância na sessão
    return (EntityGrid)facade.session().getAttribute("grid" + entity.getTableName());
  }

  /**
   * Cria e retorna uma nova instância de EntityGrid.
   * @param facade Facade Fachada.
   * @param entity Entity Entidade de origem dos dados.
   * @param id String Identificação do EntityGrid para ser utilizada em scripts.
   * @param height Altura do Grid na página em pixels ou 0 para se ajustar ao seu container.
   * @param width Largura do Grid na página em pixels ou 0 para se ajustar ao seu container.
   * @param columns Column[] contendo a lista de colunas do Grid.
   */
  static public EntityGrid getInstance(Facade   facade,
                                       Entity   entity,
                                       int      height,
                                       int      width) {
    // procura por uma instância existente
    EntityGrid result = getInstance(facade, entity);
    // se achamos...dispara
    if (result != null)
      return result;
    // cria um novo Grid
    result = new EntityGrid(facade, entity, height, width);
    // adiciona na sessão
    facade.session().setAttribute("grid" + entity.getTableName(), result);
    // retorna
    return result;
  }

  /**
   * Remove o 'eventListener' da lista de objetos que respondem aos eventos do
   * EntityGrid.
   * @param eventListener EventListener Instância do objeto que se deseja remover.
   */
  public void removeEventListener(EventListener eventListener) {
    eventListeners.remove(eventListener);
  }

  /**
   * Retorna 'value' como um link da coluna especificada por 'column' com
   * os valores chave de 'entityInfo'.
   * @param column Column referente à coluna que contém a ação.
   * @param entityInfo EntityInfo referente à linha de dados cujos valores chave
   *                   serão obtidos.
   * @param value String Valor que será exibido como link.
   * @throws Exception Em caso de exceção na tentativa de obter os valores
   *                   de 'entityInfo'.
   * @return String Retorna 'value' como um link da coluna especificada por
   *         'column' com os valores chave de 'entityInfo'.
   */
  static private String formatAction(EntityGrid entityGrid,
                                     Column     column,
                                     EntityInfo entityInfo,
                                     String     value) throws Exception {
    // se a colunão não possui ação...retorna o valor
    if (column.getAction() == null)
      return value;
    // direito do usuário de acesso à ação e comando da coluna
    boolean hasAccessRight = entityGrid.facade.getLoggedUser().hasAccessRight(column.getAction(), column.getCommand());
    // title
    String title = (column.getCommand() != null ? column.getCommand().getDescription() : column.getAction() != null ? column.getAction().getDescription() : "");
    // retorna
    return "<a "
         + (hasAccessRight ? "href='javascript:void(0);' " : " ")
         + "title='" + title + "' "
         + (!column.target.equals("") ? "target='" + column.target  + "' ": "")
         + (column.target.equals("")  ? "onclick=" + forwardForm(entityGrid.facade, entityGrid.entity, column.getAction(), column.getCommand(), entityInfo) : " ")
         + (hasAccessRight ? "" : "disabled='disabled'")
         + ">"
         + value
         + "</a>";
  }

  /**
   * Retorna o código JavaScript que encaminha o usuário para a página de consulta.
   * @param facade Fachada.
   * @param entity Entidade principal da consulta.
   * @param action Action que representa a página da consulta.
   * @return Retorna o código JavaScript que encaminha o usuário para a página de consulta.
   */
  static public String forwardBrowse(Facade facade,
                                     Entity entity,
                                     Action action) {
    return "EntityGrid_forwardBrowse("
                                   + "'grid" + entity.getTableName() + "',"
                                   + "'" + action.getName() + "'"
                                   + ");";
  }

  /**
   * Retorna o código JavaScript que encaminha o usuário para a página de consulta
   * e realiza a chamada de 'function' nesta página.
   * @param facade Fachada.
   * @param entity Entidade principal da consulta.
   * @param action Action que representa a página da consulta.
   * @param function String Função em JavaScript existente na página de consulta
   *                 que se deseja chamar. Ex.: "updateAll('param1')".
   * @return Retorna o código JavaScript que encaminha o usuário para a página de consulta.
   */
  static public String forwardBrowse(Facade facade,
                                     Entity entity,
                                     Action action,
                                     String function) {
    return "EntityGrid_forwardBrowse("
                                   + "\"grid" + entity.getTableName() + "\","
                                   + "\"" + action.getName() + "\","
                                   + "\"" + function + "\""
                                   + ");";
  }

  /**
   * Retorna o código JavaScript que encaminha o usuário para a página de consulta
   * e atualiza a linha de dados do Grid com os valores de 'entityInfo' de acordo
   * com a operação informada.
   * @param facade Fachada.
   * @param entity Entidade principal da consulta.
   * @param action Action que representa a página da consulta.
   * @param operation Operação de atualização do Grid.
   * @param entityInfo Registro para ser atualizado de acordo com a operação
   *                   informada.
   * @return Retorna o código JavaScript que encaminha o usuário para a página
   *         de consulta e atualiza a linha de dados do Grid com os valores de
   *         'entityInfo' de acordo com a operação informada.
   * @throws Exception Em caso de exceção na tentativa de obter valores de 'entityInfo'.
   */
  static public String forwardBrowse(Facade     facade,
                                     Entity     entity,
                                     Action     action,
                                     byte       operation,
                                     EntityInfo entityInfo) throws Exception {
    // obtém nossa instância
    EntityGrid instance = getInstance(facade, entity);
    // se não temos ainda...apenas direciona para a consulta
    if (instance == null) {
      return "EntityGrid_forwardBrowse("
                                     + "'grid" + entity.getTableName() + "',"
                                     + "'" + action.getName() + "'"
                                     + ");";
    } // if
    // se temos um EntityInfo
    EntityField[] keyFields = null;
    StringBuffer  values    = null;
    StringBuffer  keyValues = null;
    if (entityInfo != null) {
      // atualiza o EntityInfo...principalmente por causa dos lookups
      entityInfo = entity.refresh(entityInfo);
      // compõe a linha
      keyFields = entity.fieldList().getKeyFields();
      values    = new StringBuffer();
      keyValues = new StringBuffer();
      // valores
      String[] strValues = values(instance, entityInfo);
      for (int i=0; i<strValues.length; i++) {
        values.append((values.length() > 0 ? "," : "") + "\"" + strValues[i] + "\"");
      } // for
      // valores das chaves
      for (int i=0; i<keyFields.length; i++) {
        EntityField keyField = keyFields[i];
        keyValues.append((keyValues.length() > 0 ? "," : "") + "'" + entityInfo.getPropertyValue(keyField.getFieldAlias()) + "'");
      } // for
    } // if
    return "EntityGrid_forwardBrowse("
                                   + "'grid" + entity.getTableName() + "',"
                                   + "'" + action.getName() + "',"
                                   + "null,"
                                   + operation + ","
                                   + "[" + (values != null ? values.toString() : "") + "],"
                                   + "[" + (keyValues != null ? keyValues.toString() : "") + "]"
                                   + ");";
  }

  /**
   * Retorna o código JavaScript que encaminha o usuário para a página de edição.
   * @param facade Fachada.
   * @param entity Entidade principal de edição.
   * @param action Action que representa a página da edição.
   * @param command Command que representa o comando a ser executado.
   * @return Retorna o código JavaScript que encaminha o usuário para a página de edição.
   */
  static public String forwardForm(Facade  facade,
                                   Entity  entity,
                                   Action  action,
                                   Command command) {
    try {
      return forwardForm(facade, entity, action, command, null);
    }
    catch (Exception e) {
      throw new RuntimeException(e);
    } // try-catch
  }

  /**
   * Retorna o código JavaScript que encaminha o usuário para a página de edição.
   * @param facade Fachada.
   * @param entity Entidade principal de edição.
   * @param action Action que representa a página da edição.
   * @param command Command que representa o comando a ser executado.
   * @param èntityInfo EntityInfo que será editado.
   * @throws Exception Em caso de exceção na tentativa de obter valores de 'entityInfo'.
   * @return Retorna o código JavaScript que encaminha o usuário para a página de edição.
   */
  static public String forwardForm(Facade     facade,
                                   Entity     entity,
                                   Action     action,
                                   Command    command,
                                   EntityInfo entityInfo) throws Exception {
    // título para a lista de ações recentes
    /*
    if (column.addToRecents && (column.getCommand() != null)) {
      link += "&" + Controller.RECENT_EDIT_CAPTION + "=" + value;
      link += "&" + Controller.RECENT_EDIT_TITLE + "=" + column.getCommand().getDescription();
    } // if
    */
    // se temos um EntityInfo
    EntityField[] keyFields = null;
    StringBuffer  keys      = null;
    StringBuffer  keyValues = null;
    if (entityInfo != null) {
      keyFields = entity.fieldList().getKeyFields();
      keys      = new StringBuffer();
      keyValues = new StringBuffer();
      // nomes das chaves
      for (int i=0; i<keyFields.length; i++) {
        keys.append((keys.length() > 0 ? "," : "") + "'" + keyFields[i].getFieldAlias() + "'");
      } // for
      // valores das chaves
      for (int i=0; i<keyFields.length; i++) {
        EntityField keyField = keyFields[i];
        keyValues.append((keyValues.length() > 0 ? "," : "") + "'" + entityInfo.getPropertyValue(keyField.getFieldAlias()) + "'");
      } // for
    } // if
    // retorna
    return "EntityGrid_forwardForm("
                                 + "'grid" + entity.getTableName() + "',"
                                 + "'" + action.getName() + "',"
                                 + "'" + (command != null ? command.getName() : "") + "',"
                                 + "[" + (keys != null ? keys.toString() : "") + "],"
                                 + "[" + (keyValues != null ? keyValues.toString() : "") + "]"
                                 + ");";
  }

  /**
   * Retorna o tipo de seleção de linhas de dados no grid.
   * @return int Retorna o tipo de seleção de linhas de dados no grid.
   */
  public int getSelectType() {
    return grid.getSelectType();
  }

  /**
   * Retorna o código em JavaScript que oculta a coluna do Grid identificada por 'index'.
   * @param index Índice da coluna que se deseja ocultar.
   * @return Retorna o código em JavaScript que oculta a coluna do Grid identificada por 'index'.
   */
  public String hideColumn(int index) {
    return grid.hideColumn(index);
  }

  /**
   * Retorna o código em JavaScript que oculta a coluna do Grid identificada por 'index'.
   * @param index Nome da variável JavaScript que contém o índice da coluna que se deseja ocultar.
   * @return Retorna o código em JavaScript que oculta a coluna do Grid identificada por 'index'.
   */
  public String hideColumn(String index) {
    return grid.hideColumn(index);
  }
  
  /**
   * Retorna o código em JavaSript para inserir uma linha no início do Grid.
   * @param entityInfo EntityInfo que representa a linha de dados que se deseja
   *                   inserir.
   * @return Retorna o código em JavaSript para inserir uma linha no início do Grid.
   * @throws Exception Em caso de exceção na tentativa de obter os valores para exibir.
   */
  public String insertRow(EntityInfo entityInfo) throws Exception {
    return grid.insertRow(values(this, entityInfo), keyValues(this, entityInfo));
  }

  static private String[] keyValues(EntityGrid entityGrid,
                                    EntityInfo entityInfo) throws Exception {
    // campos chave
    EntityField[] keyFields = entityGrid.entity.fieldList().getKeyFields();
    // nosso resultado
    String[] result = new String[keyFields.length];
    // loop nos campos chave
    for (int i=0; i<keyFields.length; i++) {
      // campo da vez
      EntityField field = keyFields[i];
      // põe na chave
      result[i] = entityInfo.getPropertyValue(field.getFieldAlias()).toString();
    } // for
    // retorna
    return result;
  }

  /**
   * Retorna o código em JavaScript que retorna a quantidade de linhas do Grid.
   * @return Retorna o código em JavaScript que retorna a quantidade de linhas do Grid.
   */
  public String rowCount() {
    return grid.rowCount();
  }

  /**
   * Retorna o código em JavaScript que retorna o índice da linha de dados do Grid
   * representada por 'keyValues'.
   * @param keyValues Valores chaves que identificam a linha de dados.
   * @return Retorna o código em JavaScript que retorna o índice da linha de dados
   *         do Grid representada por 'keyValues'.
   * @throws Exception Em caso de exceção no acesso das propriedades de EntityInfo.
   */
  public String rowIndex(EntityInfo entityInfo) throws Exception {
    return grid.rowIndex(keyValues(this, entityInfo));
  }

  /**
   * Retorna o código JavaScript que cria o EntityGrid com os dados contidos em
   * 'entityInfoList'.
   * @param entityInfoList EntityInfo[] contendo a lista de valores das linhas
   *        de dados.
   * @return String Retorna o código JavaScript que cria o EntityGrid com os dados
   *                contidos em 'entityInfoList'.
   * @throws Exception Em caso de exceção na tentativa de recuperar os valores
   *                   dos dados.
   */
  public String script(EntityInfo[] entityInfoList) throws Exception {
    return script(entityInfoList, "", "");
  }

  /**
   * Retorna o código JavaScript que cria o EntityGrid com os dados contidos em
   * 'entityInfoList'.
   * @param entityInfoList EntityInfo[] contendo a lista de valores das linhas
   *        de dados.
   * @param statusBarMessage String contendo a mensagem para ser exibida na barra
   *                         de status do EntityGrid.
   * @param statusBarErrorMessage String contendo a mensagem de erro para ser
   *                              exibida na barra de status do EntityGrid.
   * @return String Retorna o código JavaScript que cria o EntityGrid com os dados
   *                contidos em 'entityInfoList'.
   * @throws Exception Em caso de exceção na tentativa de recuperar os valores
   *                   dos dados.
   */
  public String script(EntityInfo[] entityInfoList,
                       String       statusBarMessage,
                       String       statusBarErrorMessage) throws Exception {
    // limpa a lista de colunas atuais
    grid.columns().clear();
    // loop nas colunas adicionadas
    for (int i=0; i<columns.size(); i++) {
      // informação da coluna da vez
      Column column = (Column)columns.get(i);
      // adiciona a coluna no Grid
      if (column.getField() != null)
        grid.columns().add(new Grid.Column(column.getField().getCaption(),
                                           column.getWidth(),
                                           column.getField().getAlign()));
      else if (column.getLookup() != null)
        grid.columns().add(new Grid.Column(column.getLookup().getCaption(),
                                           column.getWidth()));
    } // for

    // nosso resultado
    StringBuffer result = new StringBuffer("");
    // nosso FrameForm
    result.append("<iframe id=\"" + grid.id() + "frameForm\" name=\"" + grid.id() + "frameForm\" frameborder=\"0\" scrolling=\"no\" src=\"\" style=\"position:absolute; display:none; top:-1000px; left:-1000px;\"></iframe>");
    // inicia o Grid
    result.append(grid.begin());
    // loop nos dados
    for (int i=0; i<entityInfoList.length; i++) {
      // registro da vez
      EntityInfo entityInfo = entityInfoList[i];
      // adiciona a linha do Grid
      result.append(grid.addRow(values(this, entityInfo), keyValues(this, entityInfo)));
    } // for

    // mensagem de status do Grid
    String statusMsg = null;
    // se temos uma mensagem de erro para exibir...mostra
    if (!statusBarErrorMessage.equals("")) {
      statusMsg = "<img src=images/warning16x16.png align=absmiddle />&nbsp;" + statusBarErrorMessage.replaceAll("\"", "").replaceAll("'", "");
    }
    // se temos uma mensagem para exibir...mostra
    else if (!statusBarMessage.equals("")) {
      statusMsg = statusBarMessage.replaceAll("\"", "").replaceAll("'", "");
    }
    // se não temos nada...mostra a quantidade de registros
    else
      statusMsg = entityInfoList.length + " registros";
    // finaliza o Grid
    result.append(grid.end(statusMsg));
    // retorna
    return result.toString();
  }

  /**
   * Define a imagem de exibição da coluna de índice 'columnIndex'.
   * @param columnIndex int Índice da coluna.
   * @param image String URL da imagem de exibição.
   */
  public void setColumnImage(int    columnIndex,
                             String image) {
    ((Column)columns.get(columnIndex)).image = image;
  }

  /**
   * Define a largura de exibição da coluna de índice 'columnIndex'.
   * @param columnIndex int Índice da coluna.
   * @param width int Largura da coluna.
   */
  public void setColumnWidth(int columnIndex,
                             int width) {
    ((Column)columns.get(columnIndex)).width = width;
  }

  /**
   * Define o tipo de seleção de linhas de dados no grid.
   * @param selectType int Tipo de seleção de linhas de dados no grid.
   */
  public void setSelectType(byte selectType) {
    grid.setSelectType(selectType);
  }

  /**
   * Retorna o código em JavaScript que retorna o(s) índice(s) da(s) linha(s)
   * selecionada(s) no Grid.
   * <b>Se o tipo de seleção for SELECT_NONE, sempre retorna -1. Se o
   * tipo de seleção for SELECT_SINGLE, retorna o índice da linha selecionada ou -1
   * se nenhuma linha estiver selecionada. Se o tipo de seleção for MULTIPLE,
   * sempre retorna um array contendo o(s) índice(s) da(s) linha(s) selecionada(s)
   * ou um array vazio se nenhuma linha estiver selecionada.</b>
   * @return Retorna o código em JavaScript que retorna o(s) índice(s) da(s) linha(s)
   *         selecionada(s) no Grid.
   */
  public String selectedIndex() {
    return grid.selectedIndex();
  }

  /**
   * Retorna um EneityInfo[] contendo os registros selecionados no EntityGrid
   * associado a 'entity'.
   * @param entity Entity associado ao EntityGrid onde os registros foram
   *        selecionados.
   * @param request Requisição do envio dos dados.
   * @return Retorna um EneityInfo[] contendo os registros selecionados no
   *         EntityGrid associado a 'entity'.
   * @throws Exception Em caso de exceção na tentativa de selecionar os registros.
   */
  static public EntityInfo[] selectedInfoList(Entity             entity,
                                              HttpServletRequest request) throws Exception {
    // campos chaves
    EntityField[] keyFields = entity.fieldList().getKeyFields();
    // chaves dos registros selecionados
    Object[] separatedKeys = new Object[keyFields.length];
    // quantidade de registros selecionados
    int selectedCount = -1;
    // obtém as chaves em separado da requisição
    for (int i=0; i<keyFields.length; i++) {
      // campo chave da vez
      EntityField keyField = keyFields[i];
      // obtém a lista de valores selecionados do campo chave da vez
      String[] keyFieldValues = request.getParameterValues(keyField.getFieldAlias());
      // se não temos nada selecionado...exceção
      if (keyFieldValues.length == 0)
        throw new ExtendedException(EntityGrid.class.getName(), "selectedInfoList", "Os valores do campo chave " + keyField.getFieldAlias() + " estão faltando.");
      // quantidade de chaves selecionadas = quantidade de registros selecionados
      selectedCount = (selectedCount < 0 ? keyFieldValues.length : selectedCount);
      // adiciona na lista
      separatedKeys[i] = keyFieldValues;
    } // for
    // prepara o where da consulta
    StringBuffer where = new StringBuffer();
    for (int i=0; i<selectedCount; i++) {
      where.append((i > 0 ? " OR " : "") + "(");
      for (int w=0; w<keyFields.length; w++) {
        where.append((w > 0 ? " AND " : "") + "("
                   + keyFields[w].getFieldName(entity.getTableName()) + " = '" + ((String[])separatedKeys[w])[i] + "'"
                   + ")");
      } // for w
      where.append(")");
    } // for i
    // retorna
    return entity.select(entity.prepareSelect(where.toString()));
  }

  /**
   * Retorna o código em JavaScript que retorna a(s) chave(s) da(s) linha(s) de
   * dados selecionada(s) no Grid.
   * <b>Se o tipo de seleção for SELECT_NONE, sempre retorna um array vazio.
   * Se o tipo de seleção for SELECT_SINGLE, retorna um array contendo a(s)
   * chave(s) da linha selecionada ou um array vazio se nenhuma linha estiver
   * selecionada. Se o tipo de seleção for SELECT_MULTIPLE, retorna um array
   * onde cada item é um array contendo a(s) chave(s) da(s) linha(s) selecionada(s)
   * ou um array vazio se nenhuma linha estiver selecionada.</b>
   * @return Retorna o código em JavaScript que retorna a(s) cahve(s) da(s)
   *         linha(s) de dados selecionada(s) no Grid.
   */
  public String selectedKeyValues() {
    return grid.selectedKeyValues();
  }

  /**
   * Retorna o código em JavaScript que exibe a coluna do Grid identificada por 'index'.
   * @param index Índice da coluna que se deseja exibir.
   * @return Retorna o código em JavaScript que exibe a coluna do Grid identificada por 'index'.
   */
  public String showColumn(int index) {
    return grid.showColumn(index);
  }

  /**
   * Retorna o código em JavaScript que exibe a coluna do Grid identificada por 'index'.
   * @param index Nome da variável JavaScript que contém o índice da coluna que se deseja exibir.
   * @return Retorna o código em JavaScript que exibe a coluna do Grid identificada por 'index'.
   */
  public String showColumn(String index) {
    return grid.showColumn(index);
  }
  
  /**
   * Retorna o código em JavaScript deseleciona todas as linhas do Grid.
   * @return Retorna o código em JavaScript deseleciona todas as linhas do Grid.
   */
  public String uncheckAll() {
    return grid.uncheckAll();
  }

  /**
   * Retorna o código JavaScript que atualiza as linhas do EntityGrid com os
   * dados contidos em 'entityInfoList'.
   * @param entityInfoList EntityInfo[] contendo a lista de valores das linhas
   *        de dados.
   * @return String Retorna o código JavaScript que atualiza as linhas do EntityGrid
   *                com os dados contidos em 'entityInfoList'.
   * @throws Exception Em caso de exceção na tentativa de recuperar os valores
   *                   dos dados.
   */
  public String update(EntityInfo[] entityInfoList) throws Exception {
    return update(entityInfoList, "", "");
  }

  /**
   * Retorna o código JavaScript que atualiza as linhas do EntityGrid com os
   * dados contidos em 'entityInfoList'.
   * @param entityInfoList EntityInfo[] contendo a lista de valores das linhas
   *        de dados.
   * @param statusBarMessage String contendo a mensagem para ser exibida na barra
   *                         de status do EntityGrid.
   * @param statusBarErrorMessage String contendo a mensagem de erro para ser
   *                              exibida na barra de status do EntityGrid.
   * @return String Retorna o código JavaScript que atualiza as linhas do EntityGrid
   *                com os dados contidos em 'entityInfoList'.
   * @throws Exception Em caso de exceção na tentativa de recuperar os valores
   *                   dos dados.
   */
  public String update(EntityInfo[] entityInfoList,
                       String       statusBarMessage,
                       String       statusBarErrorMessage) throws Exception {
    // nosso resultado
    StringBuffer result = new StringBuffer("");
    // apaga as linhas atuais
    result.append(grid.clearRows());
    // loop nos dados
    for (int i=0; i<entityInfoList.length; i++) {
      // registro da vez
      EntityInfo entityInfo = entityInfoList[i];
      // adiciona a linha do Grid
      result.append(grid.addRow(values(this, entityInfo), keyValues(this, entityInfo)));
    } // for
    // mensagem de status do Grid
    String statusMsg = null;
    // se temos uma mensagem de erro para exibir...mostra
    if (!statusBarErrorMessage.equals("")) {
      statusMsg = "<img src=images/warning16x16.png align=absmiddle />&nbsp;" + statusBarErrorMessage.replaceAll("\"", "").replaceAll("'", "");
    }
    // se temos uma mensagem para exibir...mostra
    else if (!statusBarMessage.equals("")) {
      statusMsg = statusBarMessage.replaceAll("\"", "").replaceAll("'", "");
    }
    // se não temos nada...mostra a quantidade de registros
    else
      statusMsg = entityInfoList.length + " registros";
    // atualiza a quantidade de registros
    result.append("document.getElementById('" + grid.id() + "status').innerHTML = '" + statusMsg + "';");
    // retorna
    return result.toString();
  }

  /**
   * Retorna o código JavaScript que atualiza a linha de dados do Grid com os
   * valores de 'entityInfo' de acordo com a operação informada.
   * @param facade Fachada.
   * @param entity Entidade principal da consulta.
   * @param operation Operação de atualização do Grid.
   * @param entityInfo Registro para ser atualizado de acordo com a operação
   *                   informada.
   * @param function Função para ser chamada após a atualização ou vazio.
   * @return Retorna o código JavaScript que atualiza a linha de dados do Grid
   *         com os valores de 'entityInfo' de acordo com a operação informada.
   * @throws Exception Em caso de exceção na tentativa de obter valores de 'entityInfo'.
   */
  static public String updateBrowse(Facade     facade,
                                    Entity     entity,
                                    byte       operation,
                                    EntityInfo entityInfo,
                                    String     function) throws Exception {
    // obtém nossa instância
    EntityGrid instance = getInstance(facade, entity);
    // se temos um EntityInfo
    EntityField[] keyFields = null;
    StringBuffer  values    = null;
    StringBuffer  keyValues = null;
    if (entityInfo != null) {
      // atualiza o EntityInfo...principalmente por causa dos lookups
      entityInfo = entity.refresh(entityInfo);
      // compõe a linha
      keyFields = entity.fieldList().getKeyFields();
      values    = new StringBuffer();
      keyValues = new StringBuffer();
      // valores
      String[] strValues = values(instance, entityInfo);
      for (int i=0; i<strValues.length; i++) {
        values.append((values.length() > 0 ? "," : "") + "\"" + strValues[i] + "\"");
      } // for
      // valores das chaves
      for (int i=0; i<keyFields.length; i++) {
        EntityField keyField = keyFields[i];
        keyValues.append((keyValues.length() > 0 ? "," : "") + "'" + entityInfo.getPropertyValue(keyField.getFieldAlias()) + "'");
      } // for
    } // if
    return "EntityGrid_updateBrowse("
                                   + "'grid" + entity.getTableName() + "',"
                                   + "'" + (!function.equals("") ? function : "null" ) + "',"
                                   + operation + ","
                                   + "[" + (values != null ? values.toString() : "") + "],"
                                   + "[" + (keyValues != null ? keyValues.toString() : "") + "]"
                                   + ");";
  }

  /**
   * Retorna o código em JavaSript para atualizar uma linha de dados do Grid.
   * @param entityInfo EntityInfo que representa a linha de dados que se deseja
   *                   atualizar.
   * @return Retorna o código em JavaSript para atualizar uma linha de dados do Grid.
   * @throws Exception Em caso de exceção na tentativa de obter os valores para exibir.
   */
  public String updateRow(EntityInfo entityInfo) throws Exception {
    return grid.updateRowEx(values(this, entityInfo), keyValues(this, entityInfo));
  }

  static private String[] values(EntityGrid entityGrid,
                                 EntityInfo entityInfo) throws Exception {
    // nosso resultado
    String[] result = new String[entityGrid.columns.size()];
    // loop nas colunas
    for (int i=0; i<entityGrid.columns.size(); i++) {
      // coluna da vez
      Column column = (Column)entityGrid.columns.get(i);
      // se é um campo...mostra o valor do campo
      if (column.getField() != null)
        result[i] = column.getField().getFormatedFieldValue(entityInfo);
      // se é um método lookup...mostra o valor lookup
      else if (column.getLookup() != null) {
        // valor do lookup
        EntityLookupValue lookupValue = entityInfo.lookupValueList().get(column.getLookup());
        // se o valor não foi encontrado...exceção
        if (lookupValue == null)
          throw new ExtendedException(EntityGrid.class.getName(), "script", "Lookup não encontrado " + column.getLookup().getName() + ".");
        // mostra o valor
        result[i] = lookupValue.getDisplayFieldValuesToString();
      } // if
      // se temos um Action para o valor...adiciona o link
      if (column.getAction() != null)
        result[i] = formatAction(entityGrid, column, entityInfo, result[i]);
      // chama os objetos que respondem aos eventos
      for (int w=0; w<entityGrid.eventListeners.size(); w++) {
        // objeto da vez
        EventListener eventListener = (EventListener)entityGrid.eventListeners.get(w);
        // chama o evento apropriado
        result[i] = eventListener.onAddCell(entityInfo, column.getField(), column.getLookup(), result[i]);
      } // for w
      // imagem de exibição da coluna
      if (!column.getImage().equals(""))
        result[i] = "<img src='" + column.getImage() + "' align='absmiddle'>&nbsp;" + result[i];
    } // for i
    // retorna
    return result;
  }

}
