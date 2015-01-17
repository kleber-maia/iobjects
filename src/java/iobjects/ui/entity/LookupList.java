package iobjects.ui.entity;

import java.sql.*;

import iobjects.*;
import iobjects.util.*;

/**
 * Implementa um mecanismo de pesquisa em tabelas. Seu princ�pio
 * b�sico � gerar um controle HTML para que o usu�rio selecione um registro de uma
 * tabela e seja retornado o valor chave do registro selecionado.
 * <b>Utilize esta classe para realizar pesquisas em tabelas que n�o possuam
 * mais que algumas dezenas de registros.</b>
 */
public class LookupList {

  static public final byte SELECT_TYPE_SINGLE   = 1;
  static public final byte SELECT_TYPE_MULTIPLE = 2;

  static private final String ON_CHANGE_SCRIPT = "style.color = '#0000FF';";
  static private final String SEPARATOR        = ";";

  /**
   * Construtor padr�o, n�o pode ser chamado.
   */
  private LookupList() {
  }

  static private String getFieldValues(ResultSet resultSet,
                                       String[]  fieldNames) throws Exception {
    String result = "";
    for (int i=0; i<fieldNames.length; i++) {
      if (!result.equals(""))
        result += SEPARATOR;
      result += resultSet.getString(fieldNames[i]);
    } // for
    return result;
  }

  static private String getSelectOptions(Connection connection,
                                         String     tableName,
                                         String[]   displayFieldNames,
                                         String[]   returnFieldNames,
                                         String[]   returnFieldValues,
                                         String[]   orderFieldNames,
                                         String     filterExpression,
                                         byte       selectType,
                                         boolean    includeBlank) throws Exception {
    // nossos campos
    String[] fields = StringTools.arrayConcat(displayFieldNames, returnFieldNames);
    // dados para exibir
    PreparedStatement preparedSelect = SqlTools.prepareSelect(connection,
                                                              tableName,
                                                              fields,
                                                              orderFieldNames,
                                                              filterExpression);
    try {
      // nossos dados
      preparedSelect.execute();
      ResultSet resultSet = preparedSelect.getResultSet();
      // nosso resultado
      String result = "";
      // option em branco
      if (includeBlank)
        result += "<option value=\"\"></option>\r\n";
      // loop nos nossos dados
      while (resultSet.next()) {
        String optionValue = getFieldValues(resultSet, returnFieldNames);
        String selected = "";
        if (selectType == SELECT_TYPE_SINGLE)
          selected = (optionValue.equals(StringTools.arrayStringToString(returnFieldValues, ";")) ? "selected" : "");
        else
          selected = (StringTools.arrayContains(returnFieldValues, optionValue) ? "selected" : "");
        result += "<option value=\"" + optionValue + "\"" + selected + ">" + getFieldValues(resultSet, displayFieldNames) + "</option>\r\n";
      } // for
      return result;
    }
    finally {
      // libera recursos
      if (preparedSelect != null)
        preparedSelect.close();
    }
  }

  /**
   * Retorna o script contendo o elemento HTML com a lista de valores obtidos
   * de 'tableName' para sele��o.
   * @param facade Inst�ncia da fachada a ser utilizada.
   * @param className Nome da classe cuja conex�o com o banco de dados ser� utilizada.
   * @param tableName Nome da tabela de pesquisa.
   * @param displayFieldNames Nomes dos campos que ser�o exibidos na listagem.
   * @param returnFieldNames Nomes dos campos cujos valores ser�o retornados para
   *                         serem utilizados como chave.
   * @param returnFieldValues Valores iniciais para 'returnFieldNames'.
   * @param orderFieldNames Nomes dos campos para ordena��o da listagem.
   * @param filterExpression Express�o SQL para filtro padr�o da listagem.
   * @param selectType Tipo de sele��o que o usu�rio poder� realizar.
   * @param lookupId Nome do elemento HTML de exibi��o para refer�ncias
   *                 em scripts.
   * @param constraint String Script JavaScript de valida��o do valor selecionado
   *                   no LookupList.
   * @param constraintMessage String Mensagem de valida��o para ser exibida
   *                          ao usu�rio.
   * @param lookupStyle Estilo HTML para modifica��o do elemento HTML
   *                    de exibi��o.
   * @param onChangeScript C�digo JavaScript para ser executado quando o usu�rio
   *                       alterar o valor do elemento HTML.
   * @param includeBlank boolean True para que seja inclu�do o item em
   *                     branco como primeira op��o de escolha.
   * @return Retorna o script contendo o elemento HTML com a lista de valores
   *         obtidos de 'tableName' para sele��o.
   * @throws Exception Em caso de exce��o na tentativa de acesso ao banco de
   *                   dados.
   */
  static public String script(Facade   facade,
                              String   className,
                              String   tableName,
                              String[] displayFieldNames,
                              String[] returnFieldNames,
                              String[] returnFieldValues,
                              String[] orderFieldNames,
                              String   filterExpression,
                              byte     selectType,
                              String   lookupId,
                              String   constraint,
                              String   constraintMessage,
                              String   lookupStyle,
                              String   onChangeScript,
                              boolean  includeBlank) throws Exception {
    // conex�o a ser utilizada
    Connection connection = facade.getConnectionForClassName(className);
    // evento onChange...troca a cor da fonte quando o usu�rio alterar
    String onChange = ON_CHANGE_SCRIPT + onChangeScript;
    // nosso resultado
    String result = "<select size=\"" + (selectType == SELECT_TYPE_SINGLE ? 1 : 2) + "\""
                  +        " id=\"" + lookupId + "\""
                  +        " name=\"" + lookupId + "\""
                  +        " style=\"" + lookupStyle + "\" " + (selectType == SELECT_TYPE_MULTIPLE ? "multiple" : "")
                  +        " onchange=\"" + onChange + "\" "
                  +        (!constraint.equals("")
                             ? "ondblclick=\"if (eval(" + constraint + ")) { return true; } else { alert('" + constraintMessage + "'); return false; }\" "
                             : "") + ">\r\n"
                  + getSelectOptions(connection,
                                     tableName,
                                     displayFieldNames,
                                     returnFieldNames,
                                     returnFieldValues,
                                     orderFieldNames,
                                     filterExpression,
                                     selectType,
                                     includeBlank) + "\r\n"
                  + "</select>\r\n";
    // retorna o Script
    return result;
  }

}
