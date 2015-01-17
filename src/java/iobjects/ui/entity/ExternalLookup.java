package iobjects.ui.entity;

import java.net.*;

import iobjects.*;
import iobjects.servlet.*;
import iobjects.ui.*;
import iobjects.ui.param.*;
import iobjects.util.*;

/**
 * Implementa um mecanismo de pesquisa em tabelas. Seu princípio básico
 * é realizar consultas de registros em uma tabela, exibindo uma interface
 * de seleção, única ou múltipla, para o usuário e retornando tal seleção em
 * um controle HTML gerado.
 */
public class ExternalLookup {

  static public final byte SELECT_TYPE_SINGLE   = 1;
  static public final byte SELECT_TYPE_MULTIPLE = 2;

  static public final int FORMAT_NONE       = Format.FORMAT_NONE;
  static public final int FORMAT_DATE       = Format.FORMAT_DATE;
  static public final int FORMAT_DATE_TIME  = Format.FORMAT_DATE_TIME;
  static public final int FORMAT_TIME       = Format.FORMAT_TIME;
  static public final int FORMAT_INTEGER    = Format.FORMAT_INTEGER;
  static public final int FORMAT_DOUBLE     = Format.FORMAT_DOUBLE;
  static public final int FORMAT_UPPER_CASE = Format.FORMAT_UPPER_CASE;

  static public final byte SEARCH_LIKE_NONE   = 0;
  static public final byte SEARCH_LIKE_PREFIX = 1;
  static public final byte SEARCH_LIKE_SUFFIX = 2;
  static public final byte SEARCH_LIKE_BOTH   = 3;

  static public final String CLASS_NAME              = "className";
  static public final String TABLE_NAME              = "tableName";
  static public final String SEARCH_FIELD_NAMES      = "searchFieldNames";
  static public final String SEARCH_FIELD_TITLES     = "searchFieldTitles";
  static public final String SEARCH_FIELD_FORMATS    = "searchFieldFormats";
  static public final String SEARCH_LIKE             = "searchLike";
  static public final String SEARCH_VALUE            = "searchValue";
  static public final String DISPLAY_FIELD_NAMES     = "displayFieldNames";
  static public final String DISPLAY_FIELD_TITLES    = "displayFieldTitles";
  static public final String DISPLAY_FIELD_WIDTHS    = "displayFieldWidths";
  static public final String RETURN_FIELD_NAMES      = "returnFieldNames";
  static public final String RETURN_FIELD_VALUES     = "returnFieldValues";
  static public final String RETURN_USER_FIELD_NAMES = "returnUserFieldNames";
  static public final String ORDER_FIELD_NAMES       = "orderFieldNames";
  static public final String FILTER_EXPRESSION       = "filterExpression";
  static public final String SELECT_TYPE             = "selectType";
  static public final String WINDOW_TITLE            = "windowTitle";
  static public final String USER                    = "user";
  static public final String EXTERNAL_LOOKUP_ID      = "externalLookupId";
  static public final String SEARCH                  = "search";
  static public final String DELETE                  = "delete";

  private ExternalLookup() {
  }

  static private String getSelectOptions(String[] values, String[] texts) {
    StringBuffer result = new StringBuffer((values.length + texts.length) * 250);
    for (int i=0; i<values.length; i++) {
      result.append("<option value=\"" + values[i] + "\">" + texts[i] + "</option>\r\n");
    } // for
    return result.toString();
  }

  static private String getValuesFromArray(String[] array) {
    StringBuffer result = new StringBuffer(array.length * 250);
    for (int i=0; i<array.length; i++) {
      if (result.length() > 0)
        result.append(";");
      result.append(array[i]);
    } // for
    return result.toString();
  }

  static private String getValuesFromArray(int[] array) {
    StringBuffer result = new StringBuffer(array.length * 250);
    for (int i=0; i<array.length; i++) {
      if (result.length() > 0)
        result.append(";");
      result.append(array[i]);
    } // for
    return result.toString();
  }

  /**
   * Retorna o script contendo o botão de consulta de dados e o elemento HTML
   * responsável pela exibição dos dados selecionados.
   * @param facade Facade Fachada.
   * @param className Nome da classe cuja conexão com o banco de dados será utilizada.
   * @param tableName Nome da tabela de pesquisa.
   * @param displayFieldNames Nomes dos campos que serão exibidos na listagem.
   * @param displayFieldTitles Títulos dos campos que serão exibidos na listagem.
   * @param displayFieldWidths Larguras, em pixel, das colunas dos campos que
   *                           serão exibidos na listagem.
   * @param returnFieldNames Nomes dos campos cujos valores serão retornados para
   *                         serem utilizados como chave.
   * @param returnFieldValues Valores iniciais para 'returnFieldNames'.
   * @param returnUserFieldNames Nomes dos campos cujos valores serão retornados
   *                             para serem exibidos ao usuário.
   * @param returnUserFieldValues Valores iniciais para 'returnUserFieldNames'.
   * @param orderFieldNames Nomes dos campos para ordenação da listagem.
   * @param filterExpression Expressão SQL para filtro padrão da listagem.
   * @param selectType Tipo de seleção que o usuário poderá realizar.
   * @param windowTitle Título da janela de pesquisa.
   * @param externalLookupId Nome do elemento HTML de exibição para referências
   *                         em scripts.
   * @param constraint String Script JavaScript de validação do valor selecionado
   *                   no ExternalLookup.
   * @param constraintMessage String Mensagem de validação para ser exibida
   *                          ao usuário.
   * @param externalLookupStyle Estilo HTML para modificação do elemento HTML
   *                            de exibição.
   * @param onChangeScript Código JavaScript para ser executado quando o usuário
   *                       alterar o valor do elemento HTML.
   * @throws Exception Em caso de exceção na tentativa de geraro script.
   * @return Retorna o script contendo o botão de consulta de dados e o elemento
   *         HTML responsável pela exibição dos dados selecionados.
   */
  static public String script(Facade   facade,
                              String   className,
                              String   tableName,
                              String[] displayFieldNames,
                              String[] displayFieldTitles,
                              int[]    displayFieldWidths,
                              String[] returnFieldNames,
                              String[] returnFieldValues,
                              String[] returnUserFieldNames,
                              String[] returnUserFieldValues,
                              String[] orderFieldNames,
                              String   filterExpression,
                              byte     selectType,
                              String   windowTitle,
                              String   externalLookupId,
                              String   constraint,
                              String   constraintMessage,
                              String   externalLookupStyle,
                              String   onChangeScript) throws Exception {
    return script(facade,
                  className,
                  tableName,
                  new String[0],
                  new String[0],
                  new int[0],
                  displayFieldNames,
                  displayFieldTitles,
                  displayFieldWidths,
                  returnFieldNames,
                  returnFieldValues,
                  returnUserFieldNames,
                  returnUserFieldValues,
                  orderFieldNames,
                  filterExpression,
                  selectType,
                  windowTitle,
                  externalLookupId,
                  constraint,
                  constraintMessage,
                  externalLookupStyle,
                  onChangeScript);
  }

  /**
   * Retorna o script contendo o botão de consulta de dados e o elemento HTML
   * responsável pela exibição dos dados selecionados.
   * @param facade Facade Fachada.
   * @param className Nome da classe cuja conexão com o banco de dados será utilizada.
   * @param tableName Nome da tabela de pesquisa.
   * @param searchFieldNames Nomes dos campos que possibilitarão ao usuário
   *                         realizar pesquisas interativas.
   * @param searchFieldTitles Títulos dos campos de pesquisa.
   * @param searchFieldFormats Formatos de edição dos campos de pesquisa.
   * @param displayFieldNames Nomes dos campos que serão exibidos na listagem.
   * @param displayFieldTitles Títulos dos campos que serão exibidos na listagem.
   * @param displayFieldWidths Larguras, em pixel, das colunas dos campos que
   *                           serão exibidos na listagem.
   * @param returnFieldNames Nomes dos campos cujos valores serão retornados para
   *                         serem utilizados como chave.
   * @param returnFieldValues Valores iniciais para 'returnFieldNames'.
   * @param returnUserFieldNames Nomes dos campos cujos valores serão retornados
   *                             para serem exibidos ao usuário.
   * @param returnUserFieldValues Valores iniciais para 'returnUserFieldNames'.
   * @param orderFieldNames Nomes dos campos para ordenação da listagem.
   * @param filterExpression Expressão SQL para filtro padrão da listagem.
   * @param selectType Tipo de seleção que o usuário poderá realizar.
   * @param windowTitle Título da janela de pesquisa.
   * @param externalLookupId Nome do elemento HTML de exibição para referências
   *                         em scripts.
   * @param constraint String Script JavaScript de validação do valor selecionado
   *                   no ExternalLookup.
   * @param constraintMessage String Mensagem de validação para ser exibida
   *                          ao usuário.
   * @param externalLookupStyle Estilo HTML para modificação do elemento HTML
   *                            de exibição.
   * @param onChangeScript Código JavaScript para ser executado quando o usuário
   *                       alterar o valor do elemento HTML.
   * @throws Exception Em caso de exceção na tentativa de geraro script.
   * @return Retorna o script contendo o botão de consulta de dados e o elemento
   *         HTML responsável pela exibição dos dados selecionados.
   */
  static public String script(Facade   facade,
                              String   className,
                              String   tableName,
                              String[] searchFieldNames,
                              String[] searchFieldTitles,
                              int[]    searchFieldFormats,
                              String[] displayFieldNames,
                              String[] displayFieldTitles,
                              int[]    displayFieldWidths,
                              String[] returnFieldNames,
                              String[] returnFieldValues,
                              String[] returnUserFieldNames,
                              String[] returnUserFieldValues,
                              String[] orderFieldNames,
                              String   filterExpression,
                              byte     selectType,
                              String   windowTitle,
                              String   externalLookupId,
                              String   constraint,
                              String   constraintMessage,
                              String   externalLookupStyle,
                              String   onChangeScript) throws Exception {
    return script(facade,
                  className,
                  tableName,
                  searchFieldNames,
                  searchFieldTitles,
                  searchFieldFormats,
                  SEARCH_LIKE_PREFIX,
                  displayFieldNames,
                  displayFieldTitles,
                  displayFieldWidths,
                  returnFieldNames,
                  returnFieldValues,
                  returnUserFieldNames,
                  returnUserFieldValues,
                  orderFieldNames,
                  filterExpression,
                  selectType,
                  windowTitle,
                  externalLookupId,
                  constraint,
                  constraintMessage,
                  externalLookupStyle,
                  onChangeScript);
  }

  /**
   * Retorna o script contendo o botão de consulta de dados e o elemento HTML
   * responsável pela exibição dos dados selecionados.
   * @param facade Facade Fachada.
   * @param className Nome da classe cuja conexão com o banco de dados será utilizada.
   * @param tableName Nome da tabela de pesquisa.
   * @param searchFieldNames Nomes dos campos que possibilitarão ao usuário
   *                         realizar pesquisas interativas.
   * @param searchFieldTitles Títulos dos campos de pesquisa.
   * @param searchFieldFormats Formatos de edição dos campos de pesquisa.
   * @param searchLike Forma de utilização do argumento LIKE de pesquisa.
   * @param displayFieldNames Nomes dos campos que serão exibidos na listagem.
   * @param displayFieldTitles Títulos dos campos que serão exibidos na listagem.
   * @param displayFieldWidths Larguras, em pixel, das colunas dos campos que
   *                           serão exibidos na listagem.
   * @param returnFieldNames Nomes dos campos cujos valores serão retornados para
   *                         serem utilizados como chave.
   * @param returnFieldValues Valores iniciais para 'returnFieldNames'.
   * @param returnUserFieldNames Nomes dos campos cujos valores serão retornados
   *                             para serem exibidos ao usuário.
   * @param returnUserFieldValues Valores iniciais para 'returnUserFieldNames'.
   * @param orderFieldNames Nomes dos campos para ordenação da listagem.
   * @param filterExpression Expressão SQL para filtro padrão da listagem.
   * @param selectType Tipo de seleção que o usuário poderá realizar.
   * @param windowTitle Título da janela de pesquisa.
   * @param externalLookupId Nome do elemento HTML de exibição para referências
   *                         em scripts.
   * @param constraint String Script JavaScript de validação do valor selecionado
   *                   no ExternalLookup.
   * @param constraintMessage String Mensagem de validação para ser exibida
   *                          ao usuário.
   * @param externalLookupStyle Estilo HTML para modificação do elemento HTML
   *                            de exibição.
   * @param onChangeScript Código JavaScript para ser executado quando o usuário
   *                       alterar o valor do elemento HTML.
   * @throws Exception Em caso de exceção na tentativa de geraro script.
   * @return Retorna o script contendo o botão de consulta de dados e o elemento
   *         HTML responsável pela exibição dos dados selecionados.
   */
  static public String script(Facade   facade,
                              String   className,
                              String   tableName,
                              String[] searchFieldNames,
                              String[] searchFieldTitles,
                              int[]    searchFieldFormats,
                              byte     searchLike,
                              String[] displayFieldNames,
                              String[] displayFieldTitles,
                              int[]    displayFieldWidths,
                              String[] returnFieldNames,
                              String[] returnFieldValues,
                              String[] returnUserFieldNames,
                              String[] returnUserFieldValues,
                              String[] orderFieldNames,
                              String   filterExpression,
                              byte     selectType,
                              String   windowTitle,
                              String   externalLookupId,
                              String   constraint,
                              String   constraintMessage,
                              String   externalLookupStyle,
                              String   onChangeScript) throws Exception {
    // URL do external lookup
    String url = Controller.ACTION_EXTERNAL_LOOKUP.url(Controller.COMMAND_EXTERNAL_LOOKUP_SEARCH,
                                                       CLASS_NAME + "=" + className
                                                       + "&" + TABLE_NAME + "=" + tableName
                                                       + "&" + SEARCH_FIELD_NAMES + "=" + getValuesFromArray(searchFieldNames)
                                                       + "&" + SEARCH_FIELD_TITLES + "=" + getValuesFromArray(searchFieldTitles)
                                                       + "&" + SEARCH_FIELD_FORMATS + "=" + getValuesFromArray(searchFieldFormats)
                                                       + "&" + SEARCH_LIKE + "=" + searchLike
                                                       + "&" + DISPLAY_FIELD_NAMES + "=" + getValuesFromArray(displayFieldNames)
                                                       + "&" + DISPLAY_FIELD_TITLES + "=" + getValuesFromArray(displayFieldTitles)
                                                       + "&" + DISPLAY_FIELD_WIDTHS + "=" + getValuesFromArray(displayFieldWidths)
                                                       + "&" + RETURN_FIELD_NAMES + "=" + getValuesFromArray(returnFieldNames)
                                                       + "&" + RETURN_USER_FIELD_NAMES + "=" + getValuesFromArray(returnUserFieldNames)
                                                       + "&" + ORDER_FIELD_NAMES + "=" + getValuesFromArray(orderFieldNames)
                                                       + "&" + FILTER_EXPRESSION + "=" + StringTools.encodeURL(filterExpression)
                                                       + "&" + SELECT_TYPE + "=" + selectType
                                                       + "&" + EXTERNAL_LOOKUP_ID + "=" + externalLookupId
                                                       + "&" + WINDOW_TITLE + "=" + windowTitle);

    // permite pesquisa digitada?
    boolean typedSearch = (selectType == SELECT_TYPE_SINGLE) && (searchFieldNames.length > 0);
    // script para apagar valores
    StringBuffer deleteScript = new StringBuffer();
    if (selectType == SELECT_TYPE_SINGLE)
      deleteScript.append(externalLookupId + ".value='';"
                        + externalLookupId + USER + ".value='';"
                        + (typedSearch ? externalLookupId + USER + ".readOnly=false; " + externalLookupId + USER + ".focus();" : ""));
    else
      deleteScript.append("for (i=" + externalLookupId + ".options.length-1; i>=0; i--) {"
                        + "if (" + externalLookupId + ".options[i].selected) "
                        + externalLookupId + ".options.remove(i);"
                        + "};");

    // botão de pesquisa
    String searchButton = Button.script(facade,
                                        externalLookupId + SEARCH,
                                        "",
                                        (typedSearch
                                          ? "Informe o(a) " + searchFieldTitles[0] + " e tecle ENTER ou clique para pesquisar."
                                          : "Pesquisar..."),
                                        "images/externallookup16x16.png",
                                        "",
                                        Button.KIND_DEFAULT,
                                        "width:22px; height:20px;",
                                        Popup.script(url + "&" + RETURN_FIELD_VALUES + "=' + " + externalLookupId + ".value + '" + (typedSearch ? "&" + SEARCH_VALUE + "=' + " + externalLookupId + USER + ".value + '" : ""), "externalLookup", 480, 640, Popup.POSITION_CENTER),
                                        false);
    // botão apagar
    String deleteButton = Button.script(facade,
                                        externalLookupId + DELETE,
                                        "",
                                        "Limpa a seleção para uma nova pesquisa.",
                                        "images/commands/delete.png",
                                        "",
                                        Button.KIND_DEFAULT,
                                        "width:22px; height:20px;",
                                        "javascript:" + deleteScript,
                                        false);

    // nosso resultado
    StringBuffer result = new StringBuffer();
    // tipo de elemento HTML que iremos utilizar
    switch (selectType) {
      case SELECT_TYPE_SINGLE: {
        Param paramSearch = new Param(externalLookupId + USER,
                                      "",
                                      "",
                                      StringTools.arrayStringToString(returnUserFieldValues, ";"),
                                      0,
                                      Param.ALIGN_LEFT,
                                      (searchFieldNames.length == 0
                                        ? Param.FORMAT_NONE
                                        : searchFieldFormats[0]),
                                      "",
                                      constraint,
                                      constraintMessage);
        paramSearch.setSpecialConstraint(true, true);
        // *
        result.append("<table cellpadding=\"0\" cellspacing=\"0\" style=\"" + externalLookupStyle + "\">");
        result.append(  "<tr>");
        result.append(    "<td>");
        result.append(      "<input type=\"hidden\" "
                     +             "id=\"" + externalLookupId + "\" "
                     +             "name=\"" + externalLookupId + "\" "
                     +             "value=\"" + StringTools.arrayStringToString(returnFieldValues, ";") + "\" />"
                     +      ParamFormEdit.script(facade,
                                                 paramSearch,
                                                 0,
                                                 "",
                                                 "",
                                                 "if (event.keyCode == 13) " + externalLookupId + SEARCH + ".click();",
                                                 "",
                                                 "",
                                                 (searchFieldNames.length == 0) || !paramSearch.getValue().equals(""),
                                                 false)
                     );
        result.append(    "</td>");
        result.append(    "<td align=\"right\" style=\"width:44px;\">");
        result.append(searchButton);
        result.append(deleteButton);
        result.append(    "</td>");
        result.append(  "</tr>");
        result.append("</table>");
        break;
      }
      case SELECT_TYPE_MULTIPLE: {
        result.append("<select size=\"2\" "
                    +         "id=\"" + externalLookupId + "\" "
                    +         "name=\"" + externalLookupId + "\" "
                    +         "title=\"Clique no botão ao lado para pesquisar.\" "
                    +         "style=\"" + externalLookupStyle + "\" "
                    +         "multiple>\r"
                    + getSelectOptions(returnFieldValues, returnUserFieldValues) + ""
                    + "</select>");
        result.append(searchButton);
        result.append(deleteButton);
        break;
      }
    } // swtich
    // script ao alterar
    result.append("<script language=\"javascript\">\r\n"
                + "  function " + externalLookupId + "OnChange() {\r\n"
                +      onChangeScript + "\r\n"
                +    "}\r\n"
                + "</script>\r\n");
    // retorna o Script
    return result.toString();
  }

}
