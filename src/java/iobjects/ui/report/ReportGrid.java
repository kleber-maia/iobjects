package iobjects.ui.report;

import java.sql.*;
import java.util.*;

import iobjects.entity.*;
import iobjects.ui.*;
import iobjects.util.*;

/**
 * Representa uma tabela de dados gerada a partir de um ResultSet para ser
 * exibida em um relatório.
 * @since 3.3
 */
public class ReportGrid {

  static public final int      ALIGN_LEFT   = Align.ALIGN_LEFT;
  static public final int      ALIGN_CENTER = Align.ALIGN_CENTER;
  static public final int      ALIGN_RIGHT  = Align.ALIGN_RIGHT;
  static public final String[] ALIGN        = {"left", "center", "right"};

  static public final int FORMAT_NONE      = Format.FORMAT_NONE;
  static public final int FORMAT_DATE      = Format.FORMAT_DATE;
  static public final int FORMAT_DATE_TIME = Format.FORMAT_DATE_TIME;
  static public final int FORMAT_TIME      = Format.FORMAT_TIME;
  static public final int FORMAT_INTEGER   = Format.FORMAT_INTEGER;
  static public final int FORMAT_DOUBLE    = Format.FORMAT_DOUBLE;
  static public final String[] FORMAT      = {"none", "date", "dateTime", "time", "integer", "double"};

  static public final int TOTAL_TYPE_NONE    = 0;
  static public final int TOTAL_TYPE_COUNT   = 1;
  static public final int TOTAL_TYPE_SUM     = 2;
  static public final int TOTAL_TYPE_AVERAGE = 3;
  static public final int TOTAL_TYPE_MAXIMUM = 4;
  static public final int TOTAL_TYPE_MINIMUM = 5;

  private class ColumnInfo {
    private int         align       = 0;
    private String      caption     = "";
    private String      columnName  = "";
    private EntityField entityField = null;
    private Entity      entity      = null;
    private int         format      = 0;
    private String      image       = "";
    private int         totalType   = 0;
    private int         width       = 0;
    public ColumnInfo(String caption,
                      String columnName,
                      int    width,
                      int    align,
                      int    format,
                      int    totalType) {
      this.caption = caption;
      this.columnName = columnName;
      this.width = width;
      this.align = align;
      this.format = format;
      this.totalType = totalType;
    }
    public ColumnInfo(EntityField entityField,
                      Entity      entity,
                      int         width,
                      int         totalType) {
      this.entityField = entityField;
      this.entity = entity;
      this.caption = entityField.getCaption();
      this.columnName = entityField.getFieldName(entity != null ? entity.getTableName() : "");
      this.width = width;
      this.align = entityField.getAlign();
      this.format = entityField.getFormat();
      this.totalType = totalType;
    }
    public int         getAlign()       {return align;}
    public String      getCaption()     {return caption;}
    public String      getColumnName()  {return columnName;}
    public EntityField getEntityField() {return entityField;}
    public Entity      getEntity()      {return entity;}
    public int         getFormat()      {return format;}
    public String      getImage()       {return image;}
    public int         getTotalType()   {return totalType;}
    public int         getWidth()       {return width;}
  }

  /**
   * Representa a interface de eventos da classe ReportGrid.
   */
  public interface EventListener {

    /**
     * Evento chamado quando uma célula é adicionada ao ReportGrid. Deve retornar
     * o novo valor a ser exibido na célula ou 'value'.
     * @param columnName String Nome da coluna no ResultSet referente à coluna
     *                   da célula que está sendo adicionada.
     * @param value String Valor formatado que será exibido na célula.
     * @return String Evento chamado quando uma célula é adicionada ao ReportGrid.
     *                Deve retornar o novo valor a ser exibido na célula ou
     *                'value'.
     */
    public String onAddCell(String columnName,
                            String value) throws Exception;

    /**
     * Evento chamado quando é posicionado em um novo registro. Pode ser utilizado
     * para totalizadores e obtenção de informações gerais em cada registro.
     * @param resultSet
     */
    public void onRecord(ResultSet resultSet) throws Exception;

    /**
     * Evento chamado quando uma coluna está sendo totalizada no ReportGrid.
     * Deve retornar o novo valor a ser exibido como total para a coluna ou
     * 'total'.
     * @param columnName String Nome da coluna no ResultSet referente à coluna
     *                   do ReportGrid que está sendo totalizada.
     * @param total double Valor que será exibido como total da coluna.
     * @return double Evento chamado quando uma coluna está sendo totalizada no
     *                ReportGrid. Deve retornar o novo valor a ser exibido como
     *                total para a coluna ou 'total'.
     */
    public double onTotalizeColumn(String columnName,
                                   double total) throws Exception;

  }

  private String id             = "";
  private Vector columns        = new Vector();
  private Vector eventListeners = new Vector();
  private int    width          = 0;

  /**
   * Construtor padrão.
   * @param id String Identificação do ReportGrid para ser utilizada em scripts.
   * @param autoAlign true para que o EntityGrid ocupe toda área cliente do seu container.
   */
  public ReportGrid(String  id,
                    boolean autoAlign) {
    this.id = id;
  }

  /**
   * Construtor padrão.
   * @param id String Identificação do ReportGrid para ser utilizada em scripts.
   * @param width int Largura em pixels do ReportGrid.
   */
  public ReportGrid(String id,
                    int    width) {
    this.id = id;
    this.width = width;
  }

  /**
   * Adiciona uma coluna ao ReportGrid baseada em 'columnName' no ResultSet.
   * @param caption String Título da coluna.
   * @param columnName String Nome da coluna no ResultSet.
   * @param width int Largura da coluna. Caso a largura do ReportGrid tenha
   *              sido definida em pixels, este valor deve ser em pixels. Caso
   *              o ReportGrid tenha sido definido como 'autoAlign' este valor
   *              deve ser em softgroupual.
   * @param align int Alinhamento da coluna.
   * @param format int Formato de exibição da coluna.
   * @param totalType int Tipo do totalizador para a coluna.
   */
  public void addColumn(String caption,
                        String columnName,
                        int    width,
                        int    align,
                        int    format,
                        int    totalType) {
    columns.add(new ColumnInfo(caption,
                               columnName,
                               width,
                               align,
                               format,
                               totalType));
  }

  /**
   * Adiciona uma coluna ao ReportGrid baseada em 'entityField' no ResultSet.
   * @param entityField EntityField cujas propriedades servirão como base
   *                    para o nome, título, formatação e alinhamento da coluna.
   *                    O nome da coluna deverá ser o mesmo que o atributo
   *                    'fieldName' de 'entityField'.
   * @param width int Largura da coluna. Caso a largura do ReportGrid tenha
   *              sido definida em pixels, este valor deve ser em pixels. Caso
   *              o ReportGrid tenha sido definido como 'autoAlign' este valor
   *              deve ser em softgroupual.
   * @param totalType int Tipo do totalizador para a coluna.
   */
  public void addColumn(EntityField entityField,
                        int         width,
                        int         totalType) {
    columns.add(new ColumnInfo(entityField,
                               null,
                               width,
                               totalType));
  }

  /**
   * Adiciona uma coluna ao ReportGrid baseada em 'entityField' pertencente a
   * 'entity 'no ResultSet.
   * @param entityField EntityField cujas propriedades servirão como base
   *                    para o nome, título, formatação e alinhamento da coluna.
   *                    O nome da coluna deverá estar na forma 'tableName.fieldName',
   *                    onde 'tableName' é a propriedade de mesmo nome em 'entity'
   *                    e 'fieldName' é a propriedade de mesmo nome em 'entityField'.
   * @param entity Entity que mantém 'entityField' e cuja propriedade 'tableName'
   *               será utilizada para formar o nome da coluna.
   * @param width int Largura da coluna. Caso a largura do ReportGrid tenha
   *              sido definida em pixels, este valor deve ser em pixels. Caso
   *              o ReportGrid tenha sido definido como 'autoAlign' este valor
   *              deve ser em softgroupual.
   * @param totalType int Tipo do totalizador para a coluna.
   */
  public void addColumn(EntityField entityField,
                        Entity      entity,
                        int         width,
                        int         totalType) {
    columns.add(new ColumnInfo(entityField,
                               entity,
                               width,
                               totalType));
  }

  /**
   * Adiciona um objeto que implente a interface EventListener para ser responder
   * aos eventos do ReportGrid.
   * @param eventListener EventListener Instância do objeto que implementa a
   *                      interface EventListener.
   */
  public void addEventListener(EventListener eventListener) {
    if (!eventListeners.contains(eventListener))
      eventListeners.add(eventListener);
  }

  /**
   * Remove o 'eventListener' da lista de objetos que respondem aos eventos do
   * ReportGrid.
   * @param eventListener EventListener Instância do objeto que se deseja remover.
   */
  public void removeEventListener(EventListener eventListener) {
    eventListeners.remove(eventListener);
  }

  /**
   * Retorna o script contendo a representação HTML do ReportGrid e dos dados
   * contidos em 'resultSet'.
   * @param resultSet ResultSet contendo os dados para serem exibidos.
   * @param releaseResources boolean True para que o ResultSet e seu Statement
   *                         sejam fechados após a criação do script a fim de
   *                         liberar recursos.
   * @return String Retorna o script contendo a representação HTML do ReportGrid
   *                e dos dados contidos em 'resultSet'.
   * @throws Exception Em caso de exceção na tentativa de recuperar os valores.
   */
  public String script(ResultSet resultSet,
                       boolean   releaseResources) throws Exception {
    return script(resultSet, releaseResources, false);
  }

  /**
   * Retorna o script contendo a representação HTML do ReportGrid e dos dados
   * contidos em 'resultSet'.
   * @param resultSet ResultSet contendo os dados para serem exibidos.
   * @param releaseResources boolean True para que o ResultSet e seu Statement
   *                         sejam fechados após a criação do script a fim de
   *                         liberar recursos.
   * @param ignoreFirstResultSetNext boolean True para que o primeiro next() na
   *                                 interação com 'resultSet' seja ignorado.
   *                                 Útil quando se deseja verificar se existem
   *                                 registros no ResultSet antes de se iniciar
   *                                 a construção do ReportGrid. <b>Importante:
   *                                 se for passado true o ResultSet deve
   *                                 conter pelo menos um registro ou será
   *                                 lançada uma exceção.</b>
   * @return String Retorna o script contendo a representação HTML do ReportGrid
   *                e dos dados contidos em 'resultSet'.
   * @throws Exception Em caso de exceção na tentativa de recuperar os valores.
   */
  public String script(ResultSet resultSet,
                       boolean   releaseResources,
                       boolean   ignoreFirstResultSetNext) throws Exception {
    // colunas
    StringBuffer columnNames   = new StringBuffer();
    StringBuffer columnWidths  = new StringBuffer();
    StringBuffer columnAligns  = new StringBuffer();
    StringBuffer columnFormats = new StringBuffer();
    for (int i=0; i<columns.size(); i++) {
      ColumnInfo column = (ColumnInfo)columns.get(i);
      columnNames.append((i > 0 ? "," : "") + "\"" + column.getCaption() + "\"");
      columnWidths.append((i > 0 ? "," : "") + column.getWidth());
      columnAligns.append((i > 0 ? "," : "") + "\"" + ALIGN[column.getAlign()] + "\"");
      columnFormats.append((i > 0 ? "," : "") + "\"" + FORMAT[column.getFormat()] + "\"");
    } // if
    // nosso resultado
    StringBuffer result = new StringBuffer();
    result.append("<script> ");
    result.append(  "var " + id + "id = \"" + id + "\"; ");
    result.append(  "var " + id + "columnNames = [" + columnNames.toString() + "]; ");
    result.append(  "var " + id + "columnWidths = [" + columnWidths.toString() + "]; ");
    result.append(  "var " + id + "columnAligns = [" + columnAligns.toString() + "]; ");
    result.append(  "var " + id + "columnFormats = [" + columnFormats.toString() + "]; ");
    result.append(  "var " + id + "width = " + width + "; ");
    result.append(  "ReportGrid_begin(" + id + "id); ");

    // ***************
    // linhas de dados

    // totalizadores das colunas
    double[] columnTotals = new double[columns.size()];
    // loop nos dados
    int rowIndex = 0;
    while ((ignoreFirstResultSetNext && (rowIndex == 0)) || resultSet.next()) {
      // chama os objetos que respondem aos eventos
      for (int z=0; z<eventListeners.size(); z++) {
        // objeto da vez
        EventListener eventListener = (EventListener)eventListeners.get(z);
        // chama o evento apropriado
        eventListener.onRecord(resultSet);
      } // for
      // loop nas colunas
      StringBuffer values = new StringBuffer();
      for (int i=0; i<columns.size(); i++) {
        // coluna da vez
        ColumnInfo columnInfo = (ColumnInfo)columns.elementAt(i);
        // acumula o total da coluna...mas somente se o ResultSet possuir um campo com seu nome
        switch (columnInfo.getTotalType()) {
          case TOTAL_TYPE_AVERAGE: columnTotals[i] += resultSet.getDouble(columnInfo.getColumnName()); break;
          case TOTAL_TYPE_COUNT  : columnTotals[i]++; break;
          case TOTAL_TYPE_MAXIMUM: if (columnTotals[i] < resultSet.getDouble(columnInfo.getColumnName())) columnTotals[i] = resultSet.getDouble(columnInfo.getColumnName()); break;
          case TOTAL_TYPE_MINIMUM: if ((rowIndex == 0) || columnTotals[i] > resultSet.getDouble(columnInfo.getColumnName())) columnTotals[i] = resultSet.getDouble(columnInfo.getColumnName()); break;
          case TOTAL_TYPE_SUM    : columnTotals[i] += resultSet.getDouble(columnInfo.getColumnName()); break;
        } // switch
        // valor da célula
        String value = "";
        // se temos valores de pesquisa...
        if ((columnInfo.getEntityField() != null) && (columnInfo.getEntityField().getLookupList().length > 0)) {
          value = columnInfo.getEntityField().getLookupList()[resultSet.getInt(columnInfo.getColumnName())];
        }
        // se não temos valores de pesquisa...
        else {
          // valor formatado
          switch (columnInfo.getFormat()) {
            case FORMAT_DATE:
              value = DateTools.formatDate(resultSet.getTimestamp(columnInfo.getColumnName())); break;
            case FORMAT_DATE_TIME:
              value = DateTools.formatDateTime(resultSet.getTimestamp(columnInfo.getColumnName())); break;
            case FORMAT_TIME:
              value = DateTools.formatTime(resultSet.getTimestamp(columnInfo.getColumnName())); break;
            case FORMAT_INTEGER:
              value = NumberTools.format(resultSet.getInt(columnInfo.getColumnName())); break;
            case FORMAT_DOUBLE:
              value = NumberTools.format(resultSet.getDouble(columnInfo.getColumnName())); break;
            default:
              value = resultSet.getString(columnInfo.getColumnName());
          } // switch
        } // if
        // chama os objetos que respondem aos eventos
        for (int z=0; z<eventListeners.size(); z++) {
          // objeto da vez
          EventListener eventListener = (EventListener)eventListeners.get(z);
          // chama o evento apropriado
          value = eventListener.onAddCell(columnInfo.getColumnName(), value);
        } // for
        // imagem de exibição da coluna
        if (!columnInfo.getImage().equals(""))
          value = "<img src=" + columnInfo.getImage() + " align=absmiddle>&nbsp" + value;
        // guarda o valor da vez
        values.append((i > 0 ? "," : "") + "\"" + value + "\"");
      } // for
      // adiciona a linha
      result.append("ReportGrid_addRow(" + id + "id, [" + values.toString() + "]);");
      rowIndex++;
    } // while

    // ******
    // rodapé

    // loop nas colunas
    StringBuffer statusValues = new StringBuffer();
    for (int i=0; i<columns.size(); i++) {
      // coluna da vez
      ColumnInfo columnInfo = (ColumnInfo)columns.elementAt(i);
      // total da coluna da vez
      double total = columnTotals[i];
      // o total foi fornecido pelo listener?
      boolean totalFromListener = false;
      // chama os objetos que respondem aos eventos
      for (int z=0; z<eventListeners.size(); z++) {
        // objeto da vez
        EventListener eventListener = (EventListener)eventListeners.get(z);
        // chama o evento apropriado
        total = eventListener.onTotalizeColumn(columnInfo.getColumnName(), total);
        // total fornecido pelo listener
        totalFromListener = true;
      } // for
      // seu total
      String totalValue = "";
      switch (columnInfo.getTotalType()) {
        case TOTAL_TYPE_AVERAGE:
          int rowCount = totalFromListener ? 1 : rowIndex + 1;
          totalValue = "Média: " + (columnInfo.getFormat() == FORMAT_INTEGER ? NumberTools.format((int)total/rowCount) : NumberTools.format(total/rowCount));
          break;
        case TOTAL_TYPE_COUNT: 
          totalValue = NumberTools.format((int)total) + " registros";
          break;
        case TOTAL_TYPE_MAXIMUM: 
          totalValue = "Máximo: " + (columnInfo.getFormat() == FORMAT_INTEGER ? NumberTools.format((int)total) : NumberTools.format(total));
          break;
        case TOTAL_TYPE_MINIMUM: 
          totalValue = "Mínimo: " + (columnInfo.getFormat() == FORMAT_INTEGER ? NumberTools.format((int)total) : NumberTools.format(total));
          break;
        case TOTAL_TYPE_SUM: 
          totalValue = (columnInfo.getFormat() == FORMAT_INTEGER ? NumberTools.format((int)total) : NumberTools.format(total));
          break;
      } // switch
      // guarda o valor da vez
      statusValues.append((i > 0 ? "," : "") + "\"" + totalValue + "\"");
    } // for
    // finaliza
    result.append(  "ReportGrid_end(" + id + "id, [" + statusValues.toString() + "]);");
    result.append("</script> ");
    // libera recursos
    if (releaseResources) {
      resultSet.getStatement().close();
      resultSet.close();
    } // if
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
    ((ColumnInfo)columns.get(columnIndex)).image = image;
  }

}
