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
     * @param field EntityField cuja coluna ir� representar.
     * @param width int Largura da coluna.
     */
    public Column(EntityField  field,
                  int          width) {
      this.field = field;
      this.width = width;
    }
    /**
     * Adiciona uma coluna ao EntityGrid baseada em 'field'.
     * @param field EntityField cuja coluna ir� representar.
     * @param width int Largura da coluna.
     * @param action Action A��o de destino para ser executada pelo
     *               Controller.
     * @param command Command Comando para ser executado na a��o de destino.
     * @param target String Nome do Frame de destino.
     * @param addToRecents boolean True para adicionar a lista de itens recentes.
     *                     � necess�rio informar o Action e o Command.
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
   * @param action Action A��o de destino para ser executada pelo
   *               Controller.
   * @param command Command Comando para ser executado na a��o de destino.
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
     * Evento chamado quando uma c�lula � adicionada ao EntityGrid. Deve retornar
     * o novo valor a ser exibido na c�lula ou 'value'.
     * @param entityInfo EntityInfo referente � linha da c�lula que est� sendo
     *                   adicionada.
     * @param entityField EntityField referente ao campo da c�lula que esta sendo
     *                    adicionada, caso haja um.
     * @param entityLookup EntityLookup referente ao lookup da c�lula que esta sendo
     *                     adicionada, caso haja um.
     * @param value String Valor formatado que ser� exibido na c�lula.
     * @return String Evento chamado quando uma c�lula � adicionada ao EntityGrid.
     *                Deve retornar o novo valor a ser exibido na c�lula ou
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
   * Construtor padr�o.
   * @param facade Facade Fachada.
   * @param entity Entity Entidade de origem dos dados.
   * @param id String Identifica��o do EntityGrid para ser utilizada em scripts.
   * @param height Altura do Grid na p�gina em pixels ou 0 para se ajustar ao seu container.
   * @param width Largura do Grid na p�gina em pixels ou 0 para se ajustar ao seu container.
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
   * @param field EntityField cuja coluna ir� representar.
   * @param width int Largura da coluna.
   */
  public void addColumn(EntityField field,
                        int         width) {
    addColumn(field, width, null, null, "", false);
  }

  /**
   * Adiciona uma coluna ao EntityGrid baseada em 'field'.
   * @param field EntityField cuja coluna ir� representar.
   * @param width int Largura da coluna.
   * @param action Action A��o de destino para ser executada pelo
   *               Controller.
   * @param command Command Comando para ser executado na a��o de destino.
   * @param target String Nome do Frame de destino.
   * @param addToRecents boolean True para adicionar a lista de itens recentes.
   *                     � necess�rio informar o Action e o Command.
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
   * @param action Action A��o de destino para ser executada pelo
   *               Controller.
   * @param command Command Comando para ser executado na a��o de destino.
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
   * @param eventListener EventListener Inst�ncia do objeto que implementa a
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
   * Retorna o c�digo em JavaSript para adicionar uma linha ao final do Grid.
   * @param entityInfo EntityInfo que representa a linha de dados que se deseja
   *                   adicionar.
   * @return Retorna o c�digo em JavaSript para adicionar uma linha ao final do Grid.
   * @throws Exception Em caso de exce��o na tentativa de obter os valores para exibir.
   */
  public String addRow(EntityInfo entityInfo) throws Exception {
    return grid.addRow(values(this, entityInfo), keyValues(this, entityInfo)) + "Grid_changeStatus(\"" + grid.id() + "\", Grid_rowCount(\"" + grid.id() + "\") + \" registros\");";
  }

  /**
   * Retorna o c�digo em JavaScript que altera o conte�do da barra de status
   * do Grid.
   * <b>Se foram inseridos elementos HTML na barra de status, como TABLE, DIV,
   * etc, este m�todo n�o ser� capaz de substitui-los. Se desejar alterar apenas
   * textos contidos em tais elementos, proceda da seguinte forma:
   * document.getElementById("seuElemento").innerHTML = "Novo texto!".</b>
   * @param status Novo conte�do para ser exibido na barra de status do Grid.
   * @return Retorna o c�digo em JavaScript que altera o conte�do da barra de
   *         status do Grid.
   */
  public String changeStatus(String status) {
    return grid.changeStatus(status);
  }

  /**
   * Retorna o c�digo em JavaScript seleciona todas as linhas do Grid.
   * @return Retorna o c�digo em JavaScript seleciona todas as linhas do Grid.
   */
  public String checkAll() {
    return grid.checkAll();
  }

  /**
   * Retorna o c�digo em JavaScript que inverte a sele��o de todas as linhas do Grid.
   * @return Retorna o c�digo em JavaScript que inverte a sele��o de todas as linhas do Grid.
   */
  public String checkInvert() {
    return grid.checkInvert();
  }

  /**
   * Retorna o c�digo em JavaScript que copia todas as linhas e valores do Grid
   * para a �rea de transfer�ncia.
   * @return Retorna o c�digo em JavaScript que copia todas as linhas e valores
   *         do Grid para a �rea de transfer�ncia.
   */
  public String copyToClipboard() {
    return grid.copyToClipboard();
  }

  /**
   * Retorna o c�digo em JavaScript para exclus�o de uma linha espec�fica do Grid.
   * @param index �ndice da linha que se deseja excluir.
   * @return Retorna o c�digo em JavaScript para exclus�o de uma linha espec�fica
   *         do Grid.
   */
  public String deleteRow(int index) {
    return grid.deleteRow(index) + "Grid_changeStatus(\"" + grid.id() + "\", Grid_rowCount(\"" + grid.id() + "\") + \" registros\");";
  }

  /**
   * Retorna o c�digo em JavaScript para exclus�o de uma linha espec�fica do Grid.
   * @param index Nome da vari�vel local em JavaScript contendo o �ndice da
   *              linha que se deseja excluir.
   * @return Retorna o c�digo em JavaScript para exclus�o de uma linha espec�fica
   *         do Grid.
   */
  public String deleteRow(String index) {
    return grid.deleteRow(index) + "Grid_changeStatus(\"" + grid.id() + "\", Grid_rowCount(\"" + grid.id() + "\") + \" registros\");";
  }

  /**
   * Retorna o c�digo em JavaScript para exclus�o de uma linha de dados do Grid.
   * @param entityInfo EntityInfo que representa a linha de dados que se deseja
   *                   excluir.
   * @return Retorna o c�digo em JavaScript para exclus�o de uma linha de dados
   *         do Grid.
   * @throws Exception Em caso de exce��o na tentativa de obter os valores para exibir.
   */
  public String deleteRow(EntityInfo entityInfo) throws Exception {
    return grid.deleteRow(grid.rowIndex(keyValues(this, entityInfo))) + "Grid_changeStatus(\"" + grid.id() + "\", Grid_rowCount(\"" + grid.id() + "\") + \" registros\");";
  }

  /**
   * Retorna o c�digo em JavaScript para exclus�o da(s) linha(s) selecionada(s)
   * no Grid.
   * @return Retorna o c�digo em JavaScript para exclus�o da(s) linha(s) selecionada(s)
   *         no Grid.
   */
  public String deleteSelectedRow() {
    return grid.deleteSelectedRow() + "Grid_changeStatus(\"" + grid.id() + "\", Grid_rowCount(\"" + grid.id() + "\") + \" registros\");";
  }

  /**
   * Retorna uma inst�ncia existente de EntityGrid associada a 'entity'.
   * @param facade Facade Fachada.
   * @param entity Entity cuja inst�ncia associada de EntityGrid se deseja retornar.
   * @return Retorna uma inst�ncia existente de EntityGrid associada a 'entity'.
   */
  static public EntityGrid getInstance(Facade facade, Entity entity) {
    // procura por uma inst�ncia na sess�o
    return (EntityGrid)facade.session().getAttribute("grid" + entity.getTableName());
  }

  /**
   * Cria e retorna uma nova inst�ncia de EntityGrid.
   * @param facade Facade Fachada.
   * @param entity Entity Entidade de origem dos dados.
   * @param id String Identifica��o do EntityGrid para ser utilizada em scripts.
   * @param height Altura do Grid na p�gina em pixels ou 0 para se ajustar ao seu container.
   * @param width Largura do Grid na p�gina em pixels ou 0 para se ajustar ao seu container.
   * @param columns Column[] contendo a lista de colunas do Grid.
   */
  static public EntityGrid getInstance(Facade   facade,
                                       Entity   entity,
                                       int      height,
                                       int      width) {
    // procura por uma inst�ncia existente
    EntityGrid result = getInstance(facade, entity);
    // se achamos...dispara
    if (result != null)
      return result;
    // cria um novo Grid
    result = new EntityGrid(facade, entity, height, width);
    // adiciona na sess�o
    facade.session().setAttribute("grid" + entity.getTableName(), result);
    // retorna
    return result;
  }

  /**
   * Remove o 'eventListener' da lista de objetos que respondem aos eventos do
   * EntityGrid.
   * @param eventListener EventListener Inst�ncia do objeto que se deseja remover.
   */
  public void removeEventListener(EventListener eventListener) {
    eventListeners.remove(eventListener);
  }

  /**
   * Retorna 'value' como um link da coluna especificada por 'column' com
   * os valores chave de 'entityInfo'.
   * @param column Column referente � coluna que cont�m a a��o.
   * @param entityInfo EntityInfo referente � linha de dados cujos valores chave
   *                   ser�o obtidos.
   * @param value String Valor que ser� exibido como link.
   * @throws Exception Em caso de exce��o na tentativa de obter os valores
   *                   de 'entityInfo'.
   * @return String Retorna 'value' como um link da coluna especificada por
   *         'column' com os valores chave de 'entityInfo'.
   */
  static private String formatAction(EntityGrid entityGrid,
                                     Column     column,
                                     EntityInfo entityInfo,
                                     String     value) throws Exception {
    // se a colun�o n�o possui a��o...retorna o valor
    if (column.getAction() == null)
      return value;
    // direito do usu�rio de acesso � a��o e comando da coluna
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
   * Retorna o c�digo JavaScript que encaminha o usu�rio para a p�gina de consulta.
   * @param facade Fachada.
   * @param entity Entidade principal da consulta.
   * @param action Action que representa a p�gina da consulta.
   * @return Retorna o c�digo JavaScript que encaminha o usu�rio para a p�gina de consulta.
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
   * Retorna o c�digo JavaScript que encaminha o usu�rio para a p�gina de consulta
   * e realiza a chamada de 'function' nesta p�gina.
   * @param facade Fachada.
   * @param entity Entidade principal da consulta.
   * @param action Action que representa a p�gina da consulta.
   * @param function String Fun��o em JavaScript existente na p�gina de consulta
   *                 que se deseja chamar. Ex.: "updateAll('param1')".
   * @return Retorna o c�digo JavaScript que encaminha o usu�rio para a p�gina de consulta.
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
   * Retorna o c�digo JavaScript que encaminha o usu�rio para a p�gina de consulta
   * e atualiza a linha de dados do Grid com os valores de 'entityInfo' de acordo
   * com a opera��o informada.
   * @param facade Fachada.
   * @param entity Entidade principal da consulta.
   * @param action Action que representa a p�gina da consulta.
   * @param operation Opera��o de atualiza��o do Grid.
   * @param entityInfo Registro para ser atualizado de acordo com a opera��o
   *                   informada.
   * @return Retorna o c�digo JavaScript que encaminha o usu�rio para a p�gina
   *         de consulta e atualiza a linha de dados do Grid com os valores de
   *         'entityInfo' de acordo com a opera��o informada.
   * @throws Exception Em caso de exce��o na tentativa de obter valores de 'entityInfo'.
   */
  static public String forwardBrowse(Facade     facade,
                                     Entity     entity,
                                     Action     action,
                                     byte       operation,
                                     EntityInfo entityInfo) throws Exception {
    // obt�m nossa inst�ncia
    EntityGrid instance = getInstance(facade, entity);
    // se n�o temos ainda...apenas direciona para a consulta
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
      // comp�e a linha
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
   * Retorna o c�digo JavaScript que encaminha o usu�rio para a p�gina de edi��o.
   * @param facade Fachada.
   * @param entity Entidade principal de edi��o.
   * @param action Action que representa a p�gina da edi��o.
   * @param command Command que representa o comando a ser executado.
   * @return Retorna o c�digo JavaScript que encaminha o usu�rio para a p�gina de edi��o.
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
   * Retorna o c�digo JavaScript que encaminha o usu�rio para a p�gina de edi��o.
   * @param facade Fachada.
   * @param entity Entidade principal de edi��o.
   * @param action Action que representa a p�gina da edi��o.
   * @param command Command que representa o comando a ser executado.
   * @param �ntityInfo EntityInfo que ser� editado.
   * @throws Exception Em caso de exce��o na tentativa de obter valores de 'entityInfo'.
   * @return Retorna o c�digo JavaScript que encaminha o usu�rio para a p�gina de edi��o.
   */
  static public String forwardForm(Facade     facade,
                                   Entity     entity,
                                   Action     action,
                                   Command    command,
                                   EntityInfo entityInfo) throws Exception {
    // t�tulo para a lista de a��es recentes
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
   * Retorna o tipo de sele��o de linhas de dados no grid.
   * @return int Retorna o tipo de sele��o de linhas de dados no grid.
   */
  public int getSelectType() {
    return grid.getSelectType();
  }

  /**
   * Retorna o c�digo em JavaScript que oculta a coluna do Grid identificada por 'index'.
   * @param index �ndice da coluna que se deseja ocultar.
   * @return Retorna o c�digo em JavaScript que oculta a coluna do Grid identificada por 'index'.
   */
  public String hideColumn(int index) {
    return grid.hideColumn(index);
  }

  /**
   * Retorna o c�digo em JavaScript que oculta a coluna do Grid identificada por 'index'.
   * @param index Nome da vari�vel JavaScript que cont�m o �ndice da coluna que se deseja ocultar.
   * @return Retorna o c�digo em JavaScript que oculta a coluna do Grid identificada por 'index'.
   */
  public String hideColumn(String index) {
    return grid.hideColumn(index);
  }
  
  /**
   * Retorna o c�digo em JavaSript para inserir uma linha no in�cio do Grid.
   * @param entityInfo EntityInfo que representa a linha de dados que se deseja
   *                   inserir.
   * @return Retorna o c�digo em JavaSript para inserir uma linha no in�cio do Grid.
   * @throws Exception Em caso de exce��o na tentativa de obter os valores para exibir.
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
      // p�e na chave
      result[i] = entityInfo.getPropertyValue(field.getFieldAlias()).toString();
    } // for
    // retorna
    return result;
  }

  /**
   * Retorna o c�digo em JavaScript que retorna a quantidade de linhas do Grid.
   * @return Retorna o c�digo em JavaScript que retorna a quantidade de linhas do Grid.
   */
  public String rowCount() {
    return grid.rowCount();
  }

  /**
   * Retorna o c�digo em JavaScript que retorna o �ndice da linha de dados do Grid
   * representada por 'keyValues'.
   * @param keyValues Valores chaves que identificam a linha de dados.
   * @return Retorna o c�digo em JavaScript que retorna o �ndice da linha de dados
   *         do Grid representada por 'keyValues'.
   * @throws Exception Em caso de exce��o no acesso das propriedades de EntityInfo.
   */
  public String rowIndex(EntityInfo entityInfo) throws Exception {
    return grid.rowIndex(keyValues(this, entityInfo));
  }

  /**
   * Retorna o c�digo JavaScript que cria o EntityGrid com os dados contidos em
   * 'entityInfoList'.
   * @param entityInfoList EntityInfo[] contendo a lista de valores das linhas
   *        de dados.
   * @return String Retorna o c�digo JavaScript que cria o EntityGrid com os dados
   *                contidos em 'entityInfoList'.
   * @throws Exception Em caso de exce��o na tentativa de recuperar os valores
   *                   dos dados.
   */
  public String script(EntityInfo[] entityInfoList) throws Exception {
    return script(entityInfoList, "", "");
  }

  /**
   * Retorna o c�digo JavaScript que cria o EntityGrid com os dados contidos em
   * 'entityInfoList'.
   * @param entityInfoList EntityInfo[] contendo a lista de valores das linhas
   *        de dados.
   * @param statusBarMessage String contendo a mensagem para ser exibida na barra
   *                         de status do EntityGrid.
   * @param statusBarErrorMessage String contendo a mensagem de erro para ser
   *                              exibida na barra de status do EntityGrid.
   * @return String Retorna o c�digo JavaScript que cria o EntityGrid com os dados
   *                contidos em 'entityInfoList'.
   * @throws Exception Em caso de exce��o na tentativa de recuperar os valores
   *                   dos dados.
   */
  public String script(EntityInfo[] entityInfoList,
                       String       statusBarMessage,
                       String       statusBarErrorMessage) throws Exception {
    // limpa a lista de colunas atuais
    grid.columns().clear();
    // loop nas colunas adicionadas
    for (int i=0; i<columns.size(); i++) {
      // informa��o da coluna da vez
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
    // se n�o temos nada...mostra a quantidade de registros
    else
      statusMsg = entityInfoList.length + " registros";
    // finaliza o Grid
    result.append(grid.end(statusMsg));
    // retorna
    return result.toString();
  }

  /**
   * Define a imagem de exibi��o da coluna de �ndice 'columnIndex'.
   * @param columnIndex int �ndice da coluna.
   * @param image String URL da imagem de exibi��o.
   */
  public void setColumnImage(int    columnIndex,
                             String image) {
    ((Column)columns.get(columnIndex)).image = image;
  }

  /**
   * Define a largura de exibi��o da coluna de �ndice 'columnIndex'.
   * @param columnIndex int �ndice da coluna.
   * @param width int Largura da coluna.
   */
  public void setColumnWidth(int columnIndex,
                             int width) {
    ((Column)columns.get(columnIndex)).width = width;
  }

  /**
   * Define o tipo de sele��o de linhas de dados no grid.
   * @param selectType int Tipo de sele��o de linhas de dados no grid.
   */
  public void setSelectType(byte selectType) {
    grid.setSelectType(selectType);
  }

  /**
   * Retorna o c�digo em JavaScript que retorna o(s) �ndice(s) da(s) linha(s)
   * selecionada(s) no Grid.
   * <b>Se o tipo de sele��o for SELECT_NONE, sempre retorna -1. Se o
   * tipo de sele��o for SELECT_SINGLE, retorna o �ndice da linha selecionada ou -1
   * se nenhuma linha estiver selecionada. Se o tipo de sele��o for MULTIPLE,
   * sempre retorna um array contendo o(s) �ndice(s) da(s) linha(s) selecionada(s)
   * ou um array vazio se nenhuma linha estiver selecionada.</b>
   * @return Retorna o c�digo em JavaScript que retorna o(s) �ndice(s) da(s) linha(s)
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
   * @param request Requisi��o do envio dos dados.
   * @return Retorna um EneityInfo[] contendo os registros selecionados no
   *         EntityGrid associado a 'entity'.
   * @throws Exception Em caso de exce��o na tentativa de selecionar os registros.
   */
  static public EntityInfo[] selectedInfoList(Entity             entity,
                                              HttpServletRequest request) throws Exception {
    // campos chaves
    EntityField[] keyFields = entity.fieldList().getKeyFields();
    // chaves dos registros selecionados
    Object[] separatedKeys = new Object[keyFields.length];
    // quantidade de registros selecionados
    int selectedCount = -1;
    // obt�m as chaves em separado da requisi��o
    for (int i=0; i<keyFields.length; i++) {
      // campo chave da vez
      EntityField keyField = keyFields[i];
      // obt�m a lista de valores selecionados do campo chave da vez
      String[] keyFieldValues = request.getParameterValues(keyField.getFieldAlias());
      // se n�o temos nada selecionado...exce��o
      if (keyFieldValues.length == 0)
        throw new ExtendedException(EntityGrid.class.getName(), "selectedInfoList", "Os valores do campo chave " + keyField.getFieldAlias() + " est�o faltando.");
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
   * Retorna o c�digo em JavaScript que retorna a(s) chave(s) da(s) linha(s) de
   * dados selecionada(s) no Grid.
   * <b>Se o tipo de sele��o for SELECT_NONE, sempre retorna um array vazio.
   * Se o tipo de sele��o for SELECT_SINGLE, retorna um array contendo a(s)
   * chave(s) da linha selecionada ou um array vazio se nenhuma linha estiver
   * selecionada. Se o tipo de sele��o for SELECT_MULTIPLE, retorna um array
   * onde cada item � um array contendo a(s) chave(s) da(s) linha(s) selecionada(s)
   * ou um array vazio se nenhuma linha estiver selecionada.</b>
   * @return Retorna o c�digo em JavaScript que retorna a(s) cahve(s) da(s)
   *         linha(s) de dados selecionada(s) no Grid.
   */
  public String selectedKeyValues() {
    return grid.selectedKeyValues();
  }

  /**
   * Retorna o c�digo em JavaScript que exibe a coluna do Grid identificada por 'index'.
   * @param index �ndice da coluna que se deseja exibir.
   * @return Retorna o c�digo em JavaScript que exibe a coluna do Grid identificada por 'index'.
   */
  public String showColumn(int index) {
    return grid.showColumn(index);
  }

  /**
   * Retorna o c�digo em JavaScript que exibe a coluna do Grid identificada por 'index'.
   * @param index Nome da vari�vel JavaScript que cont�m o �ndice da coluna que se deseja exibir.
   * @return Retorna o c�digo em JavaScript que exibe a coluna do Grid identificada por 'index'.
   */
  public String showColumn(String index) {
    return grid.showColumn(index);
  }
  
  /**
   * Retorna o c�digo em JavaScript deseleciona todas as linhas do Grid.
   * @return Retorna o c�digo em JavaScript deseleciona todas as linhas do Grid.
   */
  public String uncheckAll() {
    return grid.uncheckAll();
  }

  /**
   * Retorna o c�digo JavaScript que atualiza as linhas do EntityGrid com os
   * dados contidos em 'entityInfoList'.
   * @param entityInfoList EntityInfo[] contendo a lista de valores das linhas
   *        de dados.
   * @return String Retorna o c�digo JavaScript que atualiza as linhas do EntityGrid
   *                com os dados contidos em 'entityInfoList'.
   * @throws Exception Em caso de exce��o na tentativa de recuperar os valores
   *                   dos dados.
   */
  public String update(EntityInfo[] entityInfoList) throws Exception {
    return update(entityInfoList, "", "");
  }

  /**
   * Retorna o c�digo JavaScript que atualiza as linhas do EntityGrid com os
   * dados contidos em 'entityInfoList'.
   * @param entityInfoList EntityInfo[] contendo a lista de valores das linhas
   *        de dados.
   * @param statusBarMessage String contendo a mensagem para ser exibida na barra
   *                         de status do EntityGrid.
   * @param statusBarErrorMessage String contendo a mensagem de erro para ser
   *                              exibida na barra de status do EntityGrid.
   * @return String Retorna o c�digo JavaScript que atualiza as linhas do EntityGrid
   *                com os dados contidos em 'entityInfoList'.
   * @throws Exception Em caso de exce��o na tentativa de recuperar os valores
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
    // se n�o temos nada...mostra a quantidade de registros
    else
      statusMsg = entityInfoList.length + " registros";
    // atualiza a quantidade de registros
    result.append("document.getElementById('" + grid.id() + "status').innerHTML = '" + statusMsg + "';");
    // retorna
    return result.toString();
  }

  /**
   * Retorna o c�digo JavaScript que atualiza a linha de dados do Grid com os
   * valores de 'entityInfo' de acordo com a opera��o informada.
   * @param facade Fachada.
   * @param entity Entidade principal da consulta.
   * @param operation Opera��o de atualiza��o do Grid.
   * @param entityInfo Registro para ser atualizado de acordo com a opera��o
   *                   informada.
   * @param function Fun��o para ser chamada ap�s a atualiza��o ou vazio.
   * @return Retorna o c�digo JavaScript que atualiza a linha de dados do Grid
   *         com os valores de 'entityInfo' de acordo com a opera��o informada.
   * @throws Exception Em caso de exce��o na tentativa de obter valores de 'entityInfo'.
   */
  static public String updateBrowse(Facade     facade,
                                    Entity     entity,
                                    byte       operation,
                                    EntityInfo entityInfo,
                                    String     function) throws Exception {
    // obt�m nossa inst�ncia
    EntityGrid instance = getInstance(facade, entity);
    // se temos um EntityInfo
    EntityField[] keyFields = null;
    StringBuffer  values    = null;
    StringBuffer  keyValues = null;
    if (entityInfo != null) {
      // atualiza o EntityInfo...principalmente por causa dos lookups
      entityInfo = entity.refresh(entityInfo);
      // comp�e a linha
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
   * Retorna o c�digo em JavaSript para atualizar uma linha de dados do Grid.
   * @param entityInfo EntityInfo que representa a linha de dados que se deseja
   *                   atualizar.
   * @return Retorna o c�digo em JavaSript para atualizar uma linha de dados do Grid.
   * @throws Exception Em caso de exce��o na tentativa de obter os valores para exibir.
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
      // se � um campo...mostra o valor do campo
      if (column.getField() != null)
        result[i] = column.getField().getFormatedFieldValue(entityInfo);
      // se � um m�todo lookup...mostra o valor lookup
      else if (column.getLookup() != null) {
        // valor do lookup
        EntityLookupValue lookupValue = entityInfo.lookupValueList().get(column.getLookup());
        // se o valor n�o foi encontrado...exce��o
        if (lookupValue == null)
          throw new ExtendedException(EntityGrid.class.getName(), "script", "Lookup n�o encontrado " + column.getLookup().getName() + ".");
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
      // imagem de exibi��o da coluna
      if (!column.getImage().equals(""))
        result[i] = "<img src='" + column.getImage() + "' align='absmiddle'>&nbsp;" + result[i];
    } // for i
    // retorna
    return result;
  }

}
