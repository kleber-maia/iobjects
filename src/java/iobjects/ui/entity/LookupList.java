package iobjects.ui.entity;

import java.sql.*;

import iobjects.*;
import iobjects.util.*;

/**
 * Implementa um mecanismo de pesquisa em tabelas. Seu princípio
 * básico é gerar um controle HTML para que o usuário selecione um registro de uma
 * tabela e seja retornado o valor chave do registro selecionado.
 * <b>Utilize esta classe para realizar pesquisas em tabelas que não possuam
 * mais que algumas dezenas de registros.</b>
 */
public class LookupList {

  static public final byte SELECT_TYPE_SINGLE   = 1;
  static public final byte SELECT_TYPE_MULTIPLE = 2;

  static private final String ON_CHANGE_SCRIPT = "style.color = '#0000FF';";
  static private final String SEPARATOR        = ";";

  /**
   * Construtor padrão, não pode ser chamado.
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
   * de 'tableName' para seleção.
   * @param facade Instância da fachada a ser utilizada.
   * @param className Nome da classe cuja conexão com o banco de dados será utilizada.
   * @param tableName Nome da tabela de pesquisa.
   * @param displayFieldNames Nomes dos campos que serão exibidos na listagem.
   * @param returnFieldNames Nomes dos campos cujos valores serão retornados para
   *                         serem utilizados como chave.
   * @param returnFieldValues Valores iniciais para 'returnFieldNames'.
   * @param orderFieldNames Nomes dos campos para ordenação da listagem.
   * @param filterExpression Expressão SQL para filtro padrão da listagem.
   * @param selectType Tipo de seleção que o usuário poderá realizar.
   * @param lookupId Nome do elemento HTML de exibição para referências
   *                 em scripts.
   * @param constraint String Script JavaScript de validação do valor selecionado
   *                   no LookupList.
   * @param constraintMessage String Mensagem de validação para ser exibida
   *                          ao usuário.
   * @param lookupStyle Estilo HTML para modificação do elemento HTML
   *                    de exibição.
   * @param onChangeScript Código JavaScript para ser executado quando o usuário
   *                       alterar o valor do elemento HTML.
   * @param includeBlank boolean True para que seja incluído o item em
   *                     branco como primeira opção de escolha.
   * @return Retorna o script contendo o elemento HTML com a lista de valores
   *         obtidos de 'tableName' para seleção.
   * @throws Exception Em caso de exceção na tentativa de acesso ao banco de
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
    // conexão a ser utilizada
    Connection connection = facade.getConnectionForClassName(className);
    // evento onChange...troca a cor da fonte quando o usuário alterar
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
