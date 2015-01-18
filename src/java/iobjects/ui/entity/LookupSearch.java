/*
The MIT License (MIT)

Copyright (c) 2008 Kleber Maia de Andrade

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/   
package iobjects.ui.entity;

import java.sql.*;

import iobjects.*;
import iobjects.entity.*;
import iobjects.ui.ajax.*;
import iobjects.util.*;

/**
 * Implementa um mecanismo de pesquisa em tabelas atrav�s de Ajax. Seu princ�pio
 * b�sico � gerar um controle HTML para que o usu�rio selecione um registro de uma
 * tabela e seja retornado o valor chave do registro selecionado.
 * <b>Utilize esta classe para realizar pesquisas em tabelas que possuam centenas
 * de registros ou mais.</b>
 */
public class LookupSearch extends BusinessObject {

  /**
   * Retorna o script contendo o c�digo JavaScript para altera��o do valor
   * do LookupSearch na p�gina.
   * @param facade Fachada.
   * @param id Identifica��o do LookupSearch na p�gina.
   * @param value Valor (entre '') ou nome da vari�vel em JavaScript que ser�
   *              passado como novo valor do LookupSearch na p�gina.
   * @param displayValue Valor (entre '') ou nome da vari�vel em JavaScript que
   *                     ser� exibido ao usu�rio pelo LookupSearch na p�gina.
   * @return Retorna o script contendo o c�digo JavaScript para altera��o do valor
   *         do LookupSearch na p�gina.
   */
  static public String changeValue(Facade facade,
                                   String id,
                                   String value,
                                   String displayValue) {
    return "LookupSearch_changeValue(" + id + "id, " + value + ", " + displayValue + ");";
  }

  /**
   * Retorna o script contendo o c�digo JavaScript para limpeza do valor
   * selecionado no LookupSearch.
   * @param facade Fachada.
   * @param id Identifica��o do LookupSearch na p�gina.
   * @return Retorna o script contendo o c�digo JavaScript para limpeza do valor
   *         selecionado no LookupSearch.
   */
  static public String clear(Facade facade,
                             String id) {
    return "LookupSearch_clear(" + id + "id);";
  }

  /**
   * Retorna o script contendo o c�digo JavaScript para cria��o do LookupSearch
   * na p�gina.
   * @param facade Fachada.
   * @param id Identifica��o do LookupSearch na p�gina.
   * @param lookupEntityClassName Nome da classe da entidade onde ser� realizada a pesquisa.
   * @param lookupSearchFields Campo(s) que ser�(�o) utilizados na pesquisa.
   * @param lookupDisplayField Campo que ser� mostrado ao usu�rio como resultado da pesquisa.
   * @param lookupKeyExpression String Express�o de filtro na entidade de pesquisa.
   *                                   Deve ser utilizada quando a entidade de pesquisa
   *                                   � detalhe de outra entidade.
   * @param lookupFilterExpression String Express�o de filtro na entidade de pesquisa.
   *                                      Deve ser usada para limitar a exibi��o de
   *                                      determinados registros na entidade de pesquisa.
   * @param lookupReturnField Campo para ser retornado como resultado da pesquisa.
   * @param value Valor atual do campo de retorno da pesquisa.
   * @param constraint Script JavaScript de valida��o do Grid.
   * @param constraintMessage Mensagem de valida��o para ser exibida
   *                          ao usu�rio.
   * @param width Largura do LookupSearch na p�gina.
   * @param style Estilo adicional para o LookupSearch na p�gina.
   * @param onChangeScript Script JavaScript quando um novo valor for selecionado.
   * @param readOnly True se o controle est� ativo para edi��o.
   * @return Retorna o script contendo o c�digo JavaScript para cria��o do
   *         LookupSearch na p�gina.
   * @throws Exception Em caso de exce��o na tentativa de acesso ao banco de dados.
   */
  static public String script(Facade        facade,
                              String        id,
                              String        lookupEntityClassName,
                              EntityField[] lookupSearchFields,
                              EntityField   lookupDisplayField,
                              String        lookupKeyExpression,
                              String        lookupFilterExpression,
                              EntityField   lookupReturnField,
                              String        value,
                              String        constraint,
                              String        constraintMessage,
                              int           width,
                              String        style,
                              String        onChangeScript,
                              boolean       readOnly) throws Exception {
    // nomes dos campos de pesquisa
    StringBuffer strSearchFields = new StringBuffer();
    for (int i=0; i<lookupSearchFields.length; i++)
      strSearchFields.append((i > 0 ? "," : "") + "\"" + lookupSearchFields[i].getFieldName() + "\"");
    // nossa inst�ncia da entidade
    Entity entity = facade.getEntity(lookupEntityClassName);
    // faz a pesquisa para obter o valor de exibi��o
    String displayValue = getDisplayValue(facade, lookupEntityClassName, lookupDisplayField.getFieldName(), lookupKeyExpression, lookupReturnField.getFieldName(entity.getTableName()), value);
    // nosso resultado
    StringBuffer result = new StringBuffer();
    result.append("<script type=\"text/javascript\"> ");
    result.append(  "var " + id + "id = \"" + id + "\"; ");
    result.append(  "var " + id + "timer = 0; ");
    result.append(  "var " + id + "className = [\"" + lookupEntityClassName + "\"]; ");
    result.append(  "var " + id + "searchFields = [" + strSearchFields.toString() + "]; ");
    result.append(  "var " + id + "displayField = \"" + lookupDisplayField.getFieldName() + "\"; ");
    result.append(  "var " + id + "keyExpression = \"" + lookupKeyExpression + "\"; ");
    result.append(  "var " + id + "filterExpression = \"" + lookupFilterExpression + "\"; ");
    result.append(  "var " + id + "returnField = \"" + lookupReturnField.getFieldName() + "\"; ");
    result.append(  "var " + id + "value = \"" + value + "\"; ");
    result.append(  "var " + id + "displayValue = \"" + displayValue + "\"; ");
    result.append(  "var " + id + "constraint = \"" + constraint + "\"; ");
    result.append(  "var " + id + "constraintMessage = \"" + constraintMessage + "\"; ");
    result.append(  "var " + id + "width = " + width + "; ");
    result.append(  "var " + id + "style = \"" + style + "\"; ");
    result.append(  "var " + id + "onChangeScript = \"" + onChangeScript + "\"; ");
    result.append(  "var " + id + "readOnly = " + readOnly + "; ");
    result.append(  "function " + id + "_searchCallback() { ");
    result.append(    "LookupSearch_searchCallback(" + id + "id); ");
    result.append(  "} ");
    result.append(  "LookupSearch_script(" + id + "id); ");
    result.append("</script> ");
    return result.toString();
  }

  /**
   * Retorna o valor para ser exibido.
   * @return Retorna o valor para ser exibido.
   * @throws Exception
   */
  static private String getDisplayValue(Facade facade,
                                        String lookupEntityClassName,
                                        String lookupDisplayField,
                                        String lookupKeyExpression,
                                        String lookupReturnField,
                                        String value) throws Exception {
    // se n�o recebemos nada...dispara
    if (value.equals(""))
      return "";
    // inicia transa��o
    facade.beginTransaction();
    PreparedStatement statement = null;
    try {
      // nossa inst�ncia da entidade
      Entity lookupEntity = facade.getEntity(lookupEntityClassName);
      // prepara a consulta
      statement = lookupEntity.prepareSelect(new String[]{lookupDisplayField},
                                             (!lookupKeyExpression.equals("") ? "(" + lookupKeyExpression + ") AND " :  "") + "(" + lookupReturnField + " = " + value + ")",
                                             new String[]{});
      statement.execute();
      ResultSet resultSet = statement.getResultSet();
      String result = "";
      if (resultSet.next())
        result = resultSet.getString(lookupDisplayField);
      // salva tudo
      facade.commitTransaction();
      // retorna
      return result;
    }
    catch (Exception e) {
      // desfaz tudo
      facade.rollbackTransaction();
      // lan�a exce��o
      throw e;
    }
    finally {
      if (statement != null)
        statement.close();
    } // try-finally
  }

  /**
   * M�todo respons�vel pela pesquisa de valores atrav�s de Ajax. N�o deve ser
   * utilizado.
   */
  public String search(String lookupEntityClassName,
                       String lookupSearchFields,
                       String lookupDisplayField,
                       String lookupKeyExpression,
                       String lookupFilterExpression,
                       String lookupReturnField,
                       String searchValue) throws Exception {
    // inicia transa��o
    getFacade().beginTransaction();
    PreparedStatement statement = null;
    try {
      // nossa inst�ncia da entidade
      Entity entity = getFacade().getEntity(lookupEntityClassName);
      // prepara o where
      StringBuffer where = new StringBuffer();
      String[] searchFields = lookupSearchFields.split(",");
      for (int i=0; i<searchFields.length; i++) {
        if (where.length() > 0)
          where.append(" OR ");
        where.append("(UPPER(" + entity.getTableName() + "." + searchFields[i] + ") LIKE '" + searchValue.toUpperCase() + "%')");
      } // for
      // prepara a consulta
      statement = entity.prepareSelect(new String[]{lookupDisplayField},
                                       (!lookupKeyExpression.equals("") ? "(" + lookupKeyExpression + ") AND " :  "") + (!lookupFilterExpression.equals("") ? "(" + lookupFilterExpression + ") AND " : "") + "(" + where.toString() + ")",
                                       new String[]{},
                                       new Paginate(19, 0));
      statement.execute();
      ResultSet resultSet = statement.getResultSet();
      // nosso resultado
      StringBuffer result = new StringBuffer();
      while (resultSet.next()) {
        if (result.length() > 0)
          result.append(",");
        result.append("[\"" + resultSet.getString(lookupReturnField) + "\",\"" + resultSet.getString(lookupDisplayField) + "\"]");
      } // while
      // salva tudo
      getFacade().commitTransaction();
      // retorna
      return "[" + result.toString() + "]";
    }
    catch (Exception e) {
      // desfaz tudo
      getFacade().rollbackTransaction();
      // lan�a exce��o
      throw e;
    }
    finally {
      if (statement != null) {
        statement.getResultSet().close();
        statement.close();
      } // if
    } // try-finally
  }
}
