package iobjects.ui;

import java.util.*;
import javax.servlet.http.*;

import iobjects.*;
import iobjects.ui.*;
import iobjects.util.*;

/**
 * Representa uma tabela de dados para ser exibida em uma tela de dados.
 */
public class Grid {

  /**
   * Representa uma coluna no Grid.
   */
  static public class Column {
    private int    align = 0;
    private String title = "";
    private int    width = 0;
    // *
    private String  id                = "";
    private int     size              = 0;
    private String  mask              = "";
    private String  constraint        = "";
    private String  constraintMessage = "";
    private String  style             = "";
    private String  onChangeScript    = "";
    // *
    private String[] lookupList       = new String[0];
    private String[] lookupListValues = new String[0];
    /**
     * Construtor estendido.
     * @param title String T�tulo da coluna.
     * @param width int Largura da coluna.
     */
    public Column(String title,
                  int    width) {
      this.title = title;
      this.width = width;
    }
    /**
     * Construtor estendido.
     * @param title String T�tulo da coluna.
     * @param width int Largura da coluna.
     * @param align int Alinhamento da Coluna.
     */
    public Column(String title,
                  int    width,
                  int    align) {
      this.title = title;
      this.width = width;
      this.align = align;
    }

    /**
     * Construtor estendido. <b>Utilize este construtor para cria��o de um
     * controle de edi��o para os valores da coluna.</b>
     * @param title String T�tulo da coluna.
     * @param width int Largura da coluna.
     * @param align int Alinhamento da Coluna.
     * @param id Identifca��o do FormEdit na p�gina.
     * @param size int Tamanho m�ximo do valor digitado no FormEdit ou 0 (zero)
     *             para tamanho ilimitado.
     * @param mask String M�scara de edi��o do valor do FormEdit. Informe apenas "$"
     *                    para utilizar a m�scara de valores num�ricos com decimais.
     * @param constraint String Script JavaScript de valida��o do valor digitado
     *                   no FormEdit.
     * @param constraintMessage String Mensagem de valida��o para ser exibida
     *                          ao usu�rio.
     * @param lookupList Lista de valores para pesquisa.
     * @param lookupListValues Lista de valores para correspondentes para retorno da pesquisa.
     * @param style String Estilo de formata��o do FormEdit.
     * @param onChangeScript C�digo JavaScript para ser executado quando o usu�rio
     *                       alterar o valor do elemento HTML.
     */
    public Column(String   title,
                  int      width,
                  int      align,
                  String   id,
                  int      size,
                  String   mask,
                  String   constraint,
                  String   constraintMessage,
                  String   style,
                  String   onChangeScript) {
      this.title = title;
      this.width = width;
      this.align = align;
      this.id = id;
      this.size = size;
      this.mask = mask;
      this.constraint = constraint;
      this.constraintMessage = constraintMessage;
      this.style = style;
      this.onChangeScript = onChangeScript;
    }

    /**
     * Construtor estendido. <b>Utilize este construtor para cria��o de um
     * controle de edi��o para os valores da coluna.</b>
     * @param title String T�tulo da coluna.
     * @param width int Largura da coluna.
     * @param align int Alinhamento da Coluna.
     * @param id Identifca��o do FormEdit na p�gina.
     * @param size int Tamanho m�ximo do valor digitado no FormEdit ou 0 (zero)
     *             para tamanho ilimitado.
     * @param mask String M�scara de edi��o do valor do FormEdit. Informe apenas "$"
     *                    para utilizar a m�scara de valores num�ricos com decimais.
     * @param constraint String Script JavaScript de valida��o do valor digitado
     *                   no FormEdit.
     * @param constraintMessage String Mensagem de valida��o para ser exibida
     *                          ao usu�rio.
     * @param lookupList Lista de valores para pesquisa.
     * @param style String Estilo de formata��o do FormEdit.
     * @param onChangeScript C�digo JavaScript para ser executado quando o usu�rio
     *                       alterar o valor do elemento HTML.
     */
    public Column(String   title,
                  int      width,
                  int      align,
                  String   id,
                  int      size,
                  String   mask,
                  String   constraint,
                  String   constraintMessage,
                  String   style,
                  String   onChangeScript,
                  String[] lookupList) {
      this.title = title;
      this.width = width;
      this.align = align;
      this.id = id;
      this.size = size;
      this.mask = mask;
      this.constraint = constraint;
      this.constraintMessage = constraintMessage;
      this.style = style;
      this.onChangeScript = onChangeScript;
      this.lookupList = lookupList;
    }

    /**
     * Construtor estendido. <b>Utilize este construtor para cria��o de um
     * controle de edi��o para os valores da coluna.</b>
     * @param title String T�tulo da coluna.
     * @param width int Largura da coluna.
     * @param align int Alinhamento da Coluna.
     * @param id Identifca��o do FormEdit na p�gina.
     * @param size int Tamanho m�ximo do valor digitado no FormEdit ou 0 (zero)
     *             para tamanho ilimitado.
     * @param mask String M�scara de edi��o do valor do FormEdit. Informe apenas "$"
     *                    para utilizar a m�scara de valores num�ricos com decimais.
     * @param constraint String Script JavaScript de valida��o do valor digitado
     *                   no FormEdit.
     * @param constraintMessage String Mensagem de valida��o para ser exibida
     *                          ao usu�rio.
     * @param lookupList Lista de valores para pesquisa.
     * @param lookupListValues Lista de valores para correspondentes para retorno
     *                         da pesquisa. Se omitido, ser� retornado o �ndice
     *                         do item selecionado.
     * @param style String Estilo de formata��o do FormEdit.
     * @param onChangeScript C�digo JavaScript para ser executado quando o usu�rio
     *                       alterar o valor do elemento HTML.
     */
    public Column(String   title,
                  int      width,
                  int      align,
                  String   id,
                  int      size,
                  String   mask,
                  String   constraint,
                  String   constraintMessage,
                  String   style,
                  String   onChangeScript,
                  String[] lookupList,
                  String[] lookupListValues) {
      this.title = title;
      this.width = width;
      this.align = align;
      this.id = id;
      this.size = size;
      this.mask = mask;
      this.constraint = constraint;
      this.constraintMessage = constraintMessage;
      this.style = style;
      this.onChangeScript = onChangeScript;
      this.lookupList = lookupList;
      this.lookupListValues = lookupListValues;
    }
    public int    getAlign() { return align; }
    public String getConstraint()        { return constraint; }
    public String getConstraintMessage() { return constraintMessage; }
    public String getId()    { return id; }
    public String getMask()  { return mask; }
    public String getOnChangeScript() { return onChangeScript; }
    public String getStyle() { return style; }
    public String getTitle() { return title; }
    public int    getSize()  { return size; }
    public int    getWidth() { return width; }
    public String[] getLookupList() { return lookupList; }
    public String[] getLookupListValues() { return lookupListValues; }
    public void   setAlign(int align)    { this.align = align; }
    public void   setTitle(String title) { this.title = title; }
    public void   setWidth(int width)    { this.width = width; }
  }

  /**
   * Representa a lista de colunas do Grid.
   */
  public class Columns {
    private Vector vector = new Vector();
    // *
    public Columns() { }
    // *
    public void add(Column column) { vector.add(column); }
    public void clear() { vector.clear(); }
    public Column get(int index) { return (Column)vector.elementAt(index); }
    public void remove(int index) { vector.remove(index); }
    public int size() { return vector.size(); }
    public int width() { int result = 0; for (int i=0; i<size(); i++) result += get(i).getWidth(); return result; }
  }

  static public final int      ALIGN_LEFT   = Align.ALIGN_LEFT;
  static public final int      ALIGN_CENTER = Align.ALIGN_CENTER;
  static public final int      ALIGN_RIGHT  = Align.ALIGN_RIGHT;
  static public final String[] ALIGN        = {"left", "center", "right"};

  static public final byte SELECT_NONE     = 0;
  static public final byte SELECT_SINGLE   = 1;
  static public final byte SELECT_MULTIPLE = 2;

  private Columns   columns           = new Columns();
  private String    constraint        = "";
  private String    constraintMessage = "";
  private Facade    facade            = null;
  private int       height            = 0;
  private String    id                = "";
  private String[]  keys              = {};
  private ParamList keyParams         = new ParamList();
  private byte      selectType        = 0;
  private int       width             = 0;

  /**
   * Construtor estendido.
   * @param facade Facade Fachada.
   * @param id String Identifica��o do Grid para ser utilizada em scripts.
   * @param height int Altura em pixels do Grid ou 0 para auto alinhar ao espa�o dispon�vel.
   * @param width int Largura em pixels do Grid ou 0 para auto alinhar ao espa�o dispon�vel.
   */
  public Grid(Facade facade,
              String id,
              int    height,
              int    width) {
    // nossos valores
    this.facade = facade;
    this.id = id;
    this.height = height;
    this.width = width;
  }

  /**
   * Construtor estendido.
   * @param facade Facade Fachada.
   * @param id String Identifica��o do Grid para ser utilizada em scripts.
   * @param keys String[] Nomes das chaves que ser�o utilizadas para identificar
   *             as linhas de dados. <b>Caso este par�metro seja informado,
   *             todas as linhas dever�o ter seus pr�prios valores chave.</b>
   *             <p>As chaves funcionam como um mecanismo para identifica��o
   *             �nica de cada linha de dados. Se o Grid estiver dentro de um
   *             formul�rio e este for submetido, ser�o enviados todos os valores
   *             associados aos registros selecionados, casa haja, com os nomes
   *             das informados nas chaves. As chaves tamb�m possibilitam
   *             localizar e realizar opera��es em linhas espec�ficas de dados.</p>
   * @param height int Altura em pixels do Grid.
   * @param width int Largura em pixels do Grid.
   */
  public Grid(Facade   facade,
              String   id,
              String[] keys,
              byte     selectType,
              int      height,
              int      width) {
    // nossos valores
    this.facade = facade;
    this.id = id;
    this.keys = keys;
    this.selectType = selectType;
    this.height = height;
    this.width = width;
    // lista de par�metros para as chaves
    for (int i=0; i<keys.length; i++)
      keyParams.add(new Param(keys[i], ""));
  }

  /**
   * Construtor estendido.
   * @param facade Facade Fachada.
   * @param id String Identifica��o do Grid para ser utilizada em scripts.
   * @param keys String[] Nomes das chaves que ser�o utilizadas para identificar
   *             as linhas de dados. <b>Caso este par�metro seja informado,
   *             todas as linhas dever�o ter seus pr�prios valores chave.</b>
   *             <p>As chaves funcionam como um mecanismo para identifica��o
   *             �nica de cada linha de dados. Se o Grid estiver dentro de um
   *             formul�rio e este for submetido, ser�o enviados todos os valores
   *             associados aos registros selecionados, casa haja, com os nomes
   *             das informados nas chaves. As chaves tamb�m possibilitam
   *             localizar e realizar opera��es em linhas espec�ficas de dados.</p>
   * @param height int Altura em pixels do Grid.
   * @param width int Largura em pixels do Grid.
   * @param constraint String Script JavaScript de valida��o do Grid.
   * @param constraintMessage String Mensagem de valida��o para ser exibida
   *                          ao usu�rio.
   */
  public Grid(Facade   facade,
              String   id,
              String[] keys,
              byte     selectType,
              int      height,
              int      width,
              String   constraint,
              String   constraintMessage) {
    // nossos valores
    this.facade = facade;
    this.id = id;
    this.keys = keys;
    this.selectType = selectType;
    this.height = height;
    this.width = width;
    this.constraint = constraint;
    this.constraintMessage = constraintMessage;
    // lista de par�metros para as chaves
    for (int i=0; i<keys.length; i++)
      keyParams.add(new Param(keys[i], ""));
  }

  /**
   * Retorna o c�digo em JavaSript para adicionar uma linha ao final do Grid.
   * <b>Utilize este m�todo para passar em 'values' o nome de uma vari�vel
   * local em JavaScript contendo um array de valores para a linha.</b>
   * @param values Nome da vari�vel local em JavaScript contendo um array de
   *              valores para a linha.
   * @return Retorna o c�digo em JavaSript para adicionar uma linha ao final do
   *         Grid.
   */
  public String addRow(String values) {
    return addRow(values, "");
  }

  /**
   * Retorna o c�digo em JavaSript para adicionar uma linha ao final do Grid.
   * <b>Utilize este m�todo para passar em 'values' e 'keyValues' o nome de
   * vari�veis locais em JavaScript contendo um array de valores e chaves para
   * a linha.</b>
   * @param values Nome da vari�vel local em JavaScript contendo um array de
   *               valores para a linha.
   * @param keyValues Nome da vari�vel local em JavaScript contendo um array de
   *                  chaves para a linha.
   * @return Retorna o c�digo em JavaSript para adicionar uma linha ao final do
   *         Grid.
   */
  public String addRow(String values, String keyValues) {
    return "Grid_addRow(" + id + "id, " + values + ", " + (!keyValues.equals("") ? keyValues : "null") + "); ";
  }

  /**
   * Retorna o c�digo em JavaSript para adicionar uma linha ao final do Grid.
   * <b>Utilize este m�todo para passar em 'values' um array de valores para a
   * linha.</b>
   * @param values Array de valores para a linha.
   * @return Retorna o c�digo em JavaSript para adicionar uma linha ao final do
   *         Grid.
   */
  public String addRow(String[] values) {
    return addRow(values, new String[]{});
  }

  /**
   * Retorna o c�digo em JavaSript para adicionar uma linha ao final do Grid.
   * <b>Utilize este m�todo para passar em 'values' e 'keyValues' um array de
   * valores e chaves para a linha.</b>
   * @param values Array de valores para a linha.
   * @param values Array de chaves para a linha.
   * @return Retorna o c�digo em JavaSript para adicionar uma linha ao final do
   *         Grid.
   */
  public String addRow(String[] values, String[] keyValues) {
    StringBuffer result = new StringBuffer();
    result.append("Grid_addRow(" + id + "id, [");
    for (int i=0; i<values.length; i++) {
      result.append((i > 0 ? ", " : "") + "\"" + values[i] + "\"");
    } // for
    result.append("]");
    if (keyValues.length == 0)
      result.append(", null);");
    else {
      result.append(", [");
      for (int i=0; i<keyValues.length; i++) {
        result.append((i > 0 ? ", " : "") + "\"" + keyValues[i] + "\"");
      } // for
      result.append("]);");
    } // if
    return result.toString();
  }

  /**
   * Retorna o c�digo em JavaScript que inicia o Grid.
   * @return Retorna o c�digo em JavaScript que inicia o Grid.
   */
  public String begin() {
    // nossas chaves
    StringBuffer selectKeys = new StringBuffer();
    for (int i=0; i<keys.length; i++) {
      selectKeys.append((i > 0 ? "," : "") + "\"" + keys[i] + "\"");
    } // if
    // colunas
    StringBuffer columnNames  = new StringBuffer();
    StringBuffer columnWidths = new StringBuffer();
    StringBuffer columnAligns = new StringBuffer();
    // *
    StringBuffer columnIds                = new StringBuffer();
    StringBuffer columnMasks              = new StringBuffer();
    StringBuffer columnSizes              = new StringBuffer();
    StringBuffer columnConstraints        = new StringBuffer();
    StringBuffer columnConstraintMessages = new StringBuffer();
    StringBuffer columnStyles             = new StringBuffer();
    StringBuffer columnOnChangeScripts    = new StringBuffer();
    StringBuffer columnLookupLists        = new StringBuffer();
    StringBuffer columnLookupListsValues  = new StringBuffer();
    for (int i=0; i<columns.size(); i++) {
      Column column = columns.get(i);
      columnNames.append((i > 0 ? "," : "") + "\"" + column.getTitle() + "\"");
      columnWidths.append((i > 0 ? "," : "") + column.getWidth());
      columnAligns.append((i > 0 ? "," : "") + "\"" + ALIGN[column.getAlign()] + "\"");
      // *
      columnIds.append((i > 0 ? "," : "") + "\"" + column.getId() + "\"");
      columnMasks.append((i > 0 ? "," : "") + "\"" + column.getMask() + "\"");
      columnSizes.append((i > 0 ? "," : "") + "" + column.getSize() + "");
      columnConstraints.append((i > 0 ? "," : "") + "\"" + column.getConstraint() + "\"");
      columnConstraintMessages.append((i > 0 ? "," : "") + "\"" + column.getConstraintMessage() + "\"");
      columnStyles.append((i > 0 ? "," : "") + "\"" + column.getStyle() + "\"");
      columnOnChangeScripts.append((i > 0 ? "," : "") + "\"" + column.getOnChangeScript() + "\"");
      // *
      columnLookupLists.append((i > 0 ? "," : "") + "[");
      for (int w=0; w<column.lookupList.length; w++)
        columnLookupLists.append((w > 0 ? "," : "") + "\"" + column.lookupList[w] + "\"");
      columnLookupLists.append("]");
      // *
      columnLookupListsValues.append((i > 0 ? "," : "") + "[");
      for (int w=0; w<column.lookupListValues.length; w++)
        columnLookupListsValues.append((w > 0 ? "," : "") + "\"" + column.lookupListValues[w] + "\"");
      columnLookupListsValues.append("]");
    } // if
    // nosso resultado
    StringBuffer result = new StringBuffer();
    // input oculto para validar se existe alguma linha de dados selecionada
    result.append(FormEdit.script(facade, id + "Constraint", "", -1, constraint, constraintMessage));
    result.append("<script> ");
    result.append(  "var " + id + "id = \"" + id + "\"; ");
    result.append(  "var " + id + "selectType = " + selectType + "; ");
    result.append(  "var " + id + "selectKeys = [" + selectKeys.toString() + "]; ");
    result.append(  "var " + id + "width = " + width + "; ");
    result.append(  "var " + id + "height = " + height + "; ");
    result.append(  "var " + id + "columnNames = [" + columnNames.toString() + "]; ");
    result.append(  "var " + id + "columnWidths = [" + columnWidths.toString() + "]; ");
    result.append(  "var " + id + "columnAligns = [" + columnAligns.toString() + "]; ");
    result.append(  "var " + id + "columnIds = [" + columnIds.toString() + "]; ");
    result.append(  "var " + id + "columnMasks = [" + columnMasks.toString() + "]; ");
    result.append(  "var " + id + "columnSizes = [" + columnSizes.toString() + "]; ");
    result.append(  "var " + id + "columnConstraints = [" + columnConstraints.toString() + "]; ");
    result.append(  "var " + id + "columnConstraintMessages = [" + columnConstraintMessages.toString() + "]; ");
    result.append(  "var " + id + "columnStyles = [" + columnStyles.toString() + "]; ");
    result.append(  "var " + id + "columnOnChangeScripts = [" + columnOnChangeScripts.toString() + "]; ");
    result.append(  "var " + id + "columnLookupLists = [" + columnLookupLists.toString() + "]; ");
    result.append(  "var " + id + "columnLookupListsValues = [" + columnLookupListsValues.toString() + "]; ");
    result.append(  "var " + id + "hiddenColumns = \"\"; ");
    result.append(  "Grid_begin(" + id + "id); ");
    // retorna tudo
    return result.toString();
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
    return "Grid_changeStatus(" + id + "id,\"" + status + "\");";
  }

  /**
   * Retorna o c�digo em JavaScript que marca todas as linhas do Grid.
   * @return Retorna o c�digo em JavaScript que marca todas as linhas do Grid.
   */
  public String checkAll() {
    return "Grid_checkAll(" + id + "id);";
  }

  /**
   * Retorna o c�digo em JavaScript que inverte a sele��o de todas as linhas do Grid.
   * @return Retorna o c�digo em JavaScript que inverte a sele��o de todas as linhas do Grid.
   */
  public String checkInvert() {
    return "Grid_checkInvert(" + id + "id);";
  }

  /**
   * Retorna o c�digo em JavaScript seleciona a linha do Grid identificada por 'index'.
   * @param index �ndice da linha que se deseja selecionar.
   * @return Retorna o c�digo em JavaScript seleciona a linha do Grid identificada por 'index'.
   */
  public String checkRow(int index) {
    return "Grid_checkRow(" + id + "id, " + index + ");";
  }

  /**
   * Retorna o c�digo em JavaScript seleciona a linha do Grid identificada por 'index'.
   * @param index Nome da vari�vel JavaScript que cont�m o �ndice da linha que se deseja selecionar.
   * @return Retorna o c�digo em JavaScript seleciona a linha do Grid identificada por 'index'.
   */
  public String checkRow(String index) {
    return "Grid_checkRow(" + id + "id, " + index + ");";
  }

  /**
   * Retorna o c�digo em JavaScript que apaga todas as linhas do Grid.
   * @return Retorna o c�digo em JavaScript que apaga todas as linhas do Grid.
   */
  public String clearRows() {
    return "Grid_clearRows(" + id + "id);";
  }

  /**
   * Permite acesso a cole��o de colunas do Grid.
   * @return Permite acesso a cole��o de colunas do Grid.
   */
  public Columns columns() {
    return columns;
  }

  /**
   * Retorna o c�digo em JavaScript que copia todas as linhas e valores do Grid
   * para a �rea de transfer�ncia.
   * @return Retorna o c�digo em JavaScript que copia todas as linhas e valores
   *         do Grid para a �rea de transfer�ncia.
   */
  public String copyToClipboard() {
    return "Grid_copyToClipboard(" + id + "id);";
  }

  /**
   * Retorna o c�digo em JavaScript para exclus�o de uma linha espec�fica do Grid.
   * @param index �ndice da linha que se deseja excluir.
   * @return Retorna o c�digo em JavaScript para exclus�o de uma linha espec�fica
   *         do Grid.
   */
  public String deleteRow(int index) {
    return "Grid_deleteRow(" + id + "id," + index + ");";
  }

  /**
   * Retorna o c�digo em JavaScript para exclus�o de uma linha espec�fica do Grid.
   * @param index Nome da vari�vel local em JavaScript contendo o �ndice da
   *              linha que se deseja excluir.
   * @return Retorna o c�digo em JavaScript para exclus�o de uma linha espec�fica
   *         do Grid.
   */
  public String deleteRow(String index) {
    return "Grid_deleteRow(" + id + "id," + index + ");";
  }

  /**
   * Retorna o c�digo em JavaScript para exclus�o da(s) linha(s) selecionada(s)
   * no Grid.
   * @return Retorna o c�digo em JavaScript para exclus�o da(s) linha(s) selecionada(s)
   *         no Grid.
   */
  public String deleteSelectedRow() {
    return "Grid_deleteSelectedRow(" + id + "id);";
  }

  /**
   * Retorna o c�digo em JavaScript que finaliza o Grid.
   * @return Retorna o c�digo em JavaScript que finaliza o Grid.
   */
  public String end() {
    return end("");
  }

  /**
   * Retorna o c�digo em JavaScript que finaliza o Grid.
   * @param status Conte�do para ser exibido na barra de status do Grid.
   * @return Retorna o c�digo em JavaScript que finaliza o Grid.
   */
  public String end(String status) {
    return   "Grid_end(" + id + "id,\"" + status + "\");"
         + "</script>";
  }

  public String getConstraint() {
    return constraint;
  }

  public String getConstraintMessage() {
    return constraintMessage;
  }

  /**
   * Retorna o tipo de sele��o de linhas do Grid.
   * @return Retorna o tipo de sele��o de linhas do Grid.
   */
  public byte getSelectType() {
    return this.selectType;
  }

  /**
   * Retorna o c�digo em JavaScript que oculta a coluna do Grid identificada por 'index'.
   * @param index �ndice da coluna que se deseja ocultar.
   * @return Retorna o c�digo em JavaScript que oculta a coluna do Grid identificada por 'index'.
   */
  public String hideColumn(int index) {
    return "Grid_hideColumn(" + id + "id, " + index + ");";
  }

  /**
   * Retorna o c�digo em JavaScript que oculta a coluna do Grid identificada por 'index'.
   * @param index Nome da vari�vel JavaScript que cont�m o �ndice da coluna que se deseja ocultar.
   * @return Retorna o c�digo em JavaScript que oculta a coluna do Grid identificada por 'index'.
   */
  public String hideColumn(String index) {
    return "Grid_hideColumn(" + id + "id, " + index + ");";
  }

  /**
   * Retorna a identifica��o do Grid.
   * @return int Retorna a identifica��o do Grid.
   */
  public String id() {
    return id;
  }

  /**
   * Retorna o c�digo em JavaSript para inserir uma linha no in�cio do Grid.
   * <b>Utilize este m�todo para passar em 'values' o nome de uma vari�vel
   * local em JavaScript contendo um array de valores para a linha.</b>
   * @param values Nome da vari�vel local em JavaScript contendo um array de
   *               valores para a linha.
   * @return Retorna o c�digo em JavaSript para inserir uma linha no in�cio do
   *         Grid.
   */
  public String insertRow(String values) {
    return insertRow(values, "");
  }

  /**
   * Retorna o c�digo em JavaSript para inserir uma linha no in�cio do Grid.
   * <b>Utilize este m�todo para passar em 'values' e 'keyValues' o nome de
   * vari�veis locais em JavaScript contendo um array de valores e chaves para
   * a linha.</b>
   * @param values Nome da vari�vel local em JavaScript contendo um array de
   *               valores para a linha.
   * @param keyValues Nome da vari�vel local em JavaScript contendo um array de
   *                  chaves para a linha.
   * @return Retorna o c�digo em JavaSript para inserir uma linha no in�cio do
   *         Grid.
   */
  public String insertRow(String values, String keyValues) {
    return "Grid_insertRow(" + id + "id, " + values + "," + (!keyValues.equals("") ? keyValues : "null") + ");";
  }

  /**
   * Retorna o c�digo em JavaSript para inserir uma linha no in�cio do Grid.
   * <b>Utilize este m�todo para passar em 'values' um array de valores para a
   * linha.</b>
   * @param values Array de valores para a linha.
   * @return Retorna o c�digo em JavaSript para inserir uma linha no in�cio do
   *         Grid.
   */
  public String insertRow(String[] values) {
    return insertRow(values, new String[]{});
  }

  /**
   * Retorna o c�digo em JavaSript para inserir uma linha no in�cio do Grid.
   * <b>Utilize este m�todo para passar em 'values' e 'keyValues' um array de
   * valores e chaves para a linha.</b>
   * @param values Array de valores para a linha.
   * @param keyValues Array de chaves para a linha.
   * @return Retorna o c�digo em JavaSript para inserir uma linha no in�cio do
   *         Grid.
   */
  public String insertRow(String[] values, String[] keyValues) {
    StringBuffer result = new StringBuffer();
    result.append("Grid_insertRow(" + id + "id,[");
    for (int i=0; i<values.length; i++) {
      result.append((i > 0 ? ", " : "") + "\"" + values[i] + "\"");
    } // for
    result.append("],");
    if (keyValues.length == 0)
      result.append(" null);");
    else {
      result.append(" [");
      for (int i=0; i<keyValues.length; i++) {
        result.append((i > 0 ? "," : "") + "\"" + keyValues[i] + "\"");
      } // for
      result.append("]);");
    } // if
    return result.toString();
  }

  /**
   * Retorna um String[] contendo os nomes das chaves das linhas de dados.
   * @return Retorna um String[] contendo os nomes das chaves das linhas de dados.
   */
  public String[] keys() {
    return keys;
  }

  /**
   * Retorna o c�digo em JavaScript que retorna a quantidade de linhas do Grid.
   * @return Retorna o c�digo em JavaScript que retorna a quantidade de linhas do Grid.
   */
  public String rowCount() {
    return "Grid_rowCount(" + id + "id)";
  }

  /**
   * Retorna o c�digo em JavaScript que retorna o �ndice da linha de dados do Grid
   * representada por 'keyValues'.
   * @param keyValues Valores chaves que identificam a linha de dados.
   * @return Retorna o c�digo em JavaScript que retorna o �ndice da linha de dados
   *         do Grid representada por 'keyValues'.
   */
  public String rowIndex(String[] keyValues) {
    StringBuffer result = new StringBuffer();
    result.append("Grid_rowIndex(" + id + "id,[");
    for (int i=0; i<keyValues.length; i++) {
      result.append((i > 0 ? "," : "") + "\"" + keyValues[i] + "\"");
    } // for
    result.append("])");
    return result.toString();
  }

  /**
   * Retorna o c�digo em JavaScript que retorna o �ndice da linha de dados do Grid
   * representada por 'keyValues' ou -1 se nenhuma linha de dados for encontrada.
   * @param keyValues Nome da vari�vel local em JavaScript contendo os valores
   *                  chaves que identificam a linha de dados.
   * @return Retorna o c�digo em JavaScript que retorna o �ndice da linha de dados
   *         do Grid representada por 'keyValues' ou -1 se nenhuma linha de dados
   *         for encontrada.
   */
  public String rowIndex(String keyValues) {
    return "Grid_rowIndex(" + id + "id," + keyValues + ");";
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
    return "Grid_selectedIndex(" + id + "id)";
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
    return "Grid_selectedKeyValues(" + id + "id)";
  }

  public void setConstraint(String constraint) {
    this.constraint = constraint;
  }

  public void setConstraintMessage(String constraintMessage) {
    this.constraintMessage = constraintMessage;
  }

  /**
   * Define o tipo de sele��o de linhas do Grid.
   * @param selectType Define o tipo de sele��o de linhas do Grid.
   */
  public void setSelectType(byte selectType) {
    this.selectType = selectType;
  }

  /**
   * Retorna o c�digo em JavaScript que exibe a coluna do Grid identificada por 'index'.
   * @param index �ndice da coluna que se deseja exibir.
   * @return Retorna o c�digo em JavaScript que exibe a coluna do Grid identificada por 'index'.
   */
  public String showColumn(int index) {
    return "Grid_showColumn(" + id + "id, " + index + ");";
  }

  /**
   * Retorna o c�digo em JavaScript que exibe a coluna do Grid identificada por 'index'.
   * @param index Nome da vari�vel JavaScript que cont�m o �ndice da coluna que se deseja exibir.
   * @return Retorna o c�digo em JavaScript que exibe a coluna do Grid identificada por 'index'.
   */
  public String showColumn(String index) {
    return "Grid_showColumn(" + id + "id, " + index + ");";
  }
  
  /**
   * Retorna o c�digo em JavaScript desmarca todas as linhas do Grid.
   * @return Retorna o c�digo em JavaScript desmarca todas as linhas do Grid.
   */
  public String uncheckAll() {
    return "Grid_uncheckAll(" + id + "id);";
  }

  /**
   * Retorna o c�digo em JavaSript para atualizar uma linha no Grid.
   * <b>Utilize este m�todo para passar em 'values' e 'index' o nome de
   * vari�veis locais em JavaScript contendo um array de valores e o �ndice da
   * linha.</b>
   * @param values Nome da vari�vel local em JavaScript contendo um array de
   *               valores para a linha.
   * @param index Nome da vari�vel local em JavaScript contendo o �ndice da
   *               linha que se deseja atualizar.
   * @return Retorna o c�digo em JavaSript para atualizar uma linha no Grid.
   */
  public String updateRow(String values, String index) {
    return "Grid_updateRow(" + id + "id," + values + "," + index + ");";
  }

  /**
   * Retorna o c�digo em JavaSript para atualizar uma linha no Grid.
   * <b>Utilize este m�todo para passar em 'values' e 'keyValues' o nome de
   * vari�veis locais em JavaScript contendo um array de valores e chaves para
   * a linha.</b>
   * @param values Nome da vari�vel local em JavaScript contendo um array de
   *               valores para a linha.
   * @param keyValues Nome da vari�vel local em JavaScript contendo as chaves da
   *                  linha que se deseja atualizar.
   * @return Retorna o c�digo em JavaSript para atualizar uma linha no Grid.
   */
  public String updateRowEx(String values, String keyValues) {
    return "Grid_updateRow(" + id + "id," + values + ",Grid_rowIndex(" + id + "id," + keyValues + "));";
  }

  /**
   * Retorna o c�digo em JavaSript para atualizar uma linha no Grid.
   * <b>Utilize este m�todo para passar em 'values' um array de
   * valores para a linha.</b>
   * @param values Array de valores para a linha.
   * @param index �ndice da linha que se deseja atualizar.
   * @return Retorna o c�digo em JavaSript para atualizar uma linha no Grid.
   */
  public String updateRow(String[] values, int index) {
    StringBuffer result = new StringBuffer();
    result.append("Grid_updateRow(" + id + "id,[");
    for (int i=0; i<values.length; i++) {
      result.append((i > 0 ? "," : "") + "\"" + values[i] + "\"");
    } // for
    result.append("]," + index + ");");
    return result.toString();
 }

   /**
   * Retorna o c�digo em JavaSript para atualizar uma linha no Grid.
   * <b>Utilize este m�todo para passar em 'values' e 'keyValues' um array de
   * valores e chaves para a linha.</b>
   * @param values Array de valores para a linha.
   * @param keyValues Array de chaves da linha que se deseja atualizar.
   * @return Retorna o c�digo em JavaSript para atualizar uma linha no Grid.
   */
 public String updateRowEx(String[] values, String[] keyValues) {
    StringBuffer result = new StringBuffer();
    result.append("Grid_updateRow(" + id + "id, [");
    for (int i=0; i<values.length; i++) {
      result.append((i > 0 ? "," : "") + "\"" + values[i] + "\"");
    } // for
    result.append("],Grid_rowIndex(" + id + "id,[");
    for (int i=0; i<keyValues.length; i++) {
      result.append((i > 0 ? "," : "") + "\"" + keyValues[i] + "\"");
    } // for
    result.append("]));");
    return result.toString();
 }

}