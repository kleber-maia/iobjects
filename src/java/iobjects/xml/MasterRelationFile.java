package iobjects.xml;

import java.io.*;

import javax.xml.parsers.*;

import org.xml.sax.*;
import org.xml.sax.helpers.*;

import iobjects.*;
import iobjects.util.*;

/**
 * Responsável pela leitura do arquivo XML de configurações da relação mestre
 * indicado por MASTER_RELATION_FILE_NAME.
 * <p>
 * Exemplo do formato do arquivo:
 * </p>
 * <p>
 * <pre>
 * &lt;masterrelation&gt;
 *   &lt;information active="true"
 *                classname="package.ClassName"
 *                tablename="tabela"
 *                searchfieldnames=""
 *                searchfieldtitles=""
 *                searchfieldformats=""
 *                displayfieldnames="campo1,campo2"
 *                displayfieldtitles="Título 1,Título 2"
 *                displayfieldwidths="100,300"
 *                returnfieldnames="campo1"
 *                returnuserfieldnames="campo2"
 *                orderfieldnames="campo3"
 *                filterexpression=""
 *                caption="Título"
 *                description="Descrição" /&gt;
 * &lt;/masterrelation&gt;
 * </pre>
 * </p>
 */
public class MasterRelationFile extends DefaultHandler {

  public class Information {
    private boolean  active               = false;
    private String   className            = "";
    private String   tableName            = "";
    private String[] searchFieldNames     = {};
    private String[] searchFieldTitles    = {};
    private int[]    searchFieldFormats   = {};
    private String[] displayFieldNames    = {};
    private String[] displayFieldTitles   = {};
    private int[]    displayFieldWidths   = {};
    private String[] returnFieldNames     = {};
    private String[] returnUserFieldNames = {};
    private String[] orderFieldNames      = {};
    private String   filterExpression     = "";
    private String   caption              = "";
    private String   description          = "";
    // *
    public Information(boolean  active,
                       String   className,
                       String   tableName,
                       String[] searchFieldNames,
                       String[] searchFieldTitles,
                       int[]    searchFieldFormats,
                       String[] displayFieldNames,
                       String[] displayFieldTitles,
                       int[]    displayFieldWidths,
                       String[] returnFieldNames,
                       String[] returnUserFieldNames,
                       String[] orderFieldNames,
                       String   filterExpression,
                       String   caption,
                       String   description) throws Exception {
      // verifica as quantidades de campos dos vetores
      if (active) {
        if (className.equals(""))
          throw new ExtendedException(getClass().getName(), "", "ClassName não informado.");
        if (tableName.equals(""))
          throw new ExtendedException(getClass().getName(), "", "TableName não informado.");
        if (searchFieldNames.length != searchFieldTitles.length)
          throw new ExtendedException(getClass().getName(), "", "Quantidade em searchFieldNames e searchFieldTitles não combina.");
        if (searchFieldNames.length != searchFieldFormats.length)
          throw new ExtendedException(getClass().getName(), "", "Quantidade em searchFieldNames e searchFieldFormats não combina.");
        if (displayFieldNames.length != displayFieldTitles.length)
          throw new ExtendedException(getClass().getName(), "", "Quantidade em displayFieldNames e displayFieldTitles não combina.");
        if (displayFieldNames.length != displayFieldWidths.length)
          throw new ExtendedException(getClass().getName(), "", "Quantidade em displayFieldNames e displayFieldWidths não combina.");
        if (returnFieldNames.length == 0)
          throw new ExtendedException(getClass().getName(), "", "Nenhum valor informado em returnFieldNames.");
        if (returnUserFieldNames.length == 0)
          throw new ExtendedException(getClass().getName(), "", "Nenhum valor informado em returnUserFieldNames.");
      } // if
      this.active               = active;
      this.className            = className;
      this.tableName            = tableName;
      this.searchFieldNames     = searchFieldNames;
      this.searchFieldTitles    = searchFieldTitles;
      this.searchFieldFormats   = searchFieldFormats;
      this.displayFieldNames    = displayFieldNames;
      this.displayFieldTitles   = displayFieldTitles;
      this.displayFieldWidths   = displayFieldWidths;
      this.returnFieldNames     = returnFieldNames;
      this.returnUserFieldNames = returnUserFieldNames;
      this.orderFieldNames      = orderFieldNames;
      this.filterExpression     = filterExpression;
      this.caption              = caption;
      this.description          = description;
    }
    // *
    public boolean  getActive() {return active;};
    public String   getClassName() {return className;};
    public String   getTableName() {return tableName;};
    public String[] getSearchFieldNames() {return searchFieldNames;};
    public String[] getSearchFieldTitles() {return searchFieldTitles;};
    public int[]    getSearchFieldFormats() {return searchFieldFormats;};
    public String[] getDisplayFieldNames() {return displayFieldNames;};
    public String[] getDisplayFieldTitles() {return displayFieldTitles;};
    public int[]    getDisplayFieldWidths() {return displayFieldWidths;};
    public String[] getReturnFieldNames() {return returnFieldNames;};
    public String[] getReturnUserFieldNames() {return returnUserFieldNames;};
    public String[] getOrderFieldNames() {return orderFieldNames;};
    public String   getFilterExpression() {return filterExpression;};
    public String   getCaption() {return caption;};
    public String   getDescription() {return description;};
  }

  /**
   * Constante que indica o nome do arquivo de configuração da relação mestre.
   */
  private static final String MASTER_RELATION_FILE_NAME = "masterrelation.xml";
  // *
  private static final String INFORMATION = "information";
  private static final String ACTIVE = "active";
  private static final String CLASS_NAME = "classname";
  private static final String TABLE_NAME = "tablename";
  private static final String SEARCH_FIELD_NAMES = "searchfieldnames";
  private static final String SEARCH_FIELD_TITLES = "searchfieldtitles";
  private static final String SEARCH_FIELD_FORMATS = "searchfieldformats";
  private static final String DISPLAY_FIELD_NAMES = "displayfieldnames";
  private static final String DISPLAY_FIELD_TITLES = "displayfieldtitles";
  private static final String DISPLAY_FIELD_WIDTHS = "displayfieldwidths";
  private static final String RETURN_FIELD_NAMES = "returnfieldnames";
  private static final String RETURN_USER_FIELD_NAMES = "returnuserfieldnames";
  private static final String ORDER_FIELD_NAMES = "orderfieldnames";
  private static final String FILTER_EXPRESSION = "filterexpression";
  private static final String CAPTION = "caption";
  private static final String DESCRIPTION = "description";
  // *
  private Information information = null;

  /**
   * Construtor padrão.
   * @param masterRelationFilePath Caminho local onde se encontra o arquivo de
   *                        configurações da relação mestre. Veja MASTER_RELATION_FILE_NAME.
   * @throws Exception Em caso de exceção na tentativa de abertura do arquivo
   *                   XML especificado ou de inicialização do parser.
   */
  public MasterRelationFile(String masterRelationFilePath) throws Exception {
    // nossa fábrica de parsers
    SAXParserFactory parserFactory = SAXParserFactory.newInstance();
    // não validaremos o documento
    parserFactory.setValidating(false);
    // sem suporte para XML namespaces
    parserFactory.setNamespaceAware(false);
    // cria o parser
    SAXParser saxParser = parserFactory.newSAXParser();
    // inicia a análise
    saxParser.parse(new File(masterRelationFilePath + MASTER_RELATION_FILE_NAME), this);
  }

  public Information information() {
    return information;
  }

  public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
    if (qName.equalsIgnoreCase(INFORMATION)) {
      try {
        information = new Information(Boolean.valueOf(attributes.getValue(ACTIVE)).booleanValue(),
                                      attributes.getValue(CLASS_NAME),
                                      attributes.getValue(TABLE_NAME),
                                      attributes.getValue(SEARCH_FIELD_NAMES).split(","),
                                      attributes.getValue(SEARCH_FIELD_TITLES).split(","),
                                      stringToInt(attributes.getValue(SEARCH_FIELD_FORMATS).split(",")),
                                      attributes.getValue(DISPLAY_FIELD_NAMES).split(","),
                                      attributes.getValue(DISPLAY_FIELD_TITLES).split(","),
                                      stringToInt(attributes.getValue(DISPLAY_FIELD_WIDTHS).split(",")),
                                      attributes.getValue(RETURN_FIELD_NAMES).split(","),
                                      attributes.getValue(RETURN_USER_FIELD_NAMES).split(","),
                                      attributes.getValue(ORDER_FIELD_NAMES).split(","),
                                      attributes.getValue(FILTER_EXPRESSION),
                                      attributes.getValue(CAPTION),
                                      attributes.getValue(DESCRIPTION));
      }
      catch (Exception e) {
        throw new RuntimeException(new ExtendedException(getClass().getName(), "startElement", e));
      } // try-catch
    } // if
  }

  public int[] stringToInt(String[] array) throws Exception {
    int[] result = new int[array.length];
    for (int i=0; i<array.length; i++)
      result[i] = NumberTools.parseInt(array[i]);
    return result;
  }

}
