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
import java.util.*;

import iobjects.*;
import iobjects.entity.*;
import iobjects.ui.*;
import iobjects.util.*;

/**
 * Implementa um mecanismo de pesquisa em tabelas. Seu princípio básico
 * é gerar um controle HTML para que o usuário selecione um registro de uma
 * tabela e seja retornado o valor chave do registro selecionado.
 * Os valores podem ser exibidos na forma de árvore e o usuário pode
 * realizar pesquisas através do teclado.
 */
public class LookupSelectEx {

  private LookupSelectEx() {
  }

  /**
   * Retorna o script contendo o código JavaScript para alteração do valor
   * do LookupSelectEx na página.
   * @param facade Fachada.
   * @param id Identificação do LookupSelectEx na página.
   * @param value Valor (entre '') ou nome da variável em JavaScript que será
   *              passado como novo valor do LookupSelectEx na página.
   * @return Retorna o script contendo o código JavaScript para alteração do valor
   *         do LookupSelectEx na página.
   */
  static public String changeValue(Facade facade,
                                   String id,
                                   String value) {
    return FormSelectEx.changeValue(facade, id, value);
  }

  /**
   * Retorna o script contendo o código JavaScript para limpeza do valor
   * selecionado no LookupSelectEx.
   * @param facade Facade Fachada.
   * @param id String Identificação do LookupSelectEx na página.
   * @return String Retorna o script contendo o código JavaScript para limpeza
   *                do valor selecionado no LookupSelectEx.
   */
  static public String clear(Facade   facade,
                             String   id) {
    return FormSelectEx.clear(facade, id);
  }

  /**
   * Retorna uma String contendo os valores dos campos informados por 'fields',
   * separados por 'separator' e obtidos a partir de 'resultSet'.
   * @param resultSet ResultSet cujos dados do registro atual serão lidos.
   * @param fields EntityField[] cujos valores serão lidos e formatados a
   *               partir do ResultSet.
   * @param separator String para ser usada como separador dos valores.
   * @throws Exception Em caso de exceção na tentativa de formatar os valores
   *                   dos campos das entidades ou de acesso ao banco de dados.
   * @return String Retorna uma String contendo os valores dos campos informados
   *                por 'fields', separados por 'separator' e obtidos a partir
   *                de 'resultSet'.
   */
  static private String getFieldValues(ResultSet     resultSet,
                                       EntityField[] fields,
                                       String        separator) throws Exception {
    // nosso resultado
    String result = "";
    // loop nos campos
    for (int i=0; i<fields.length; i++) {
      // põe o separador
      if (!result.equals(""))
        result += separator;
      // campo da vez
      EntityField field = fields[i];
      // valor do campo da vez
      result += Entity.formatFieldValue(field, resultSet.getObject(field.getFieldName()));
    } // for
    return result;
  }

  /**
   * Retorna os objetos 'Option' para o SelectEx.
   * @param facade Instância da fachada a ser utilizada.
   * @param className Nome da classe da entidade que mantém o EntityLookup.
   * @param displayFields Campos que serão exibidos na listagem.
   * @param levelDelimiter String Delimitador de níveis encontrado nos valores
   *                       do primeiro campo de exibição, exemplo: "/". Quando
   *                       este caractere for encontrado nos valores do
   *                       primeiro campo de exibição, significa que um novo
   *                       nível está sendo definido: Nível 1/Nível 2/Nível 3.
   * @param returnFields Campos cujos valores serão retornados para
   *                     serem utilizados como chave.
   * @param orderFields Campos para ordenação da listagem.
   * @param filterExpression Expressão SQL para filtro padrão da listagem.
   * @param includeBlank boolean True se a primeira opção de seleção será uma
   *                     opção em branco.
   * @throws Exception Em caso de exceção na tentativa de formatar os valores
   *                   dos campos das entidades ou de acesso ao banco de dados.
   * @return String Retorna os elementos 'option' para o elemento 'select'.
   */
  static private FormSelectEx.Option[] getSelectExOptions(Facade        facade,
                                                          String        className,
                                                          EntityField[] displayFields,
                                                          String        levelDelimiter,
                                                          EntityField[] returnFields,
                                                          EntityField[] orderFields,
                                                          String        filterExpression,
                                                          boolean       includeBlank) throws Exception {
    // instância da entidade que mantém o lookup
    Entity entity = facade.getEntity(className);
    // conexão a ser utilizada
    Connection connection = entity.getConnection();
    // nomes dos campos de exibição
    String[] displayFieldNames = new String[displayFields.length];
    for (int i=0; i<displayFields.length; i++)
      displayFieldNames[i] = displayFields[i].getFieldName();

    // nomes dos campos de retorno
    String[] returnFieldNames = new String[returnFields.length];
    for (int i=0; i<returnFields.length; i++)
      returnFieldNames[i] = returnFields[i].getFieldName();

    // nossos campos
    String[] fields = StringTools.arrayConcat(displayFieldNames, returnFieldNames);

    // campos de ordenação
    String[] orderFieldNames = new String[orderFields.length];
    for (int i=0; i<orderFields.length; i++)
      orderFieldNames[i] = orderFields[i].getFieldName();
    // dados para exibir
    PreparedStatement preparedSelect = SqlTools.prepareSelect(connection,
                                                              entity.getTableName(),
                                                              fields,
                                                              orderFieldNames,
                                                              filterExpression);
    try {
      // nossos dados
      preparedSelect.execute();
      ResultSet resultSet = preparedSelect.getResultSet();
      // nosso resultado
      Vector vector = new Vector();
      // opção em branco
      if (includeBlank)
        vector.add(new FormSelectEx.Option("", "", 0));
      // loop nos dados da tabela de pesquisa
      while (resultSet.next()) {
        // caption da opção
        String optionCaption = Entity.formatFieldValue(displayFields[0], resultSet.getObject(displayFields[0].getFieldName()));
        // valor da opção
        String optionValue = getFieldValues(resultSet, returnFields, ";");
        // nível da opção
        int optionLevel = 0;
        // se temos uma hierarquia simples...
        if (!levelDelimiter.equals("")) {
          // divide o caption em partes
          String[] optionCaptionParts = optionCaption.split(levelDelimiter);
          // define o nível da opção
          optionLevel = optionCaptionParts.length - 1;
          // mostra no caption apenas o último nível
          optionCaption = optionCaptionParts[optionCaptionParts.length-1];
        } // if
        // cria a nova opção
        vector.add(new FormSelectEx.Option(optionCaption,
                                           optionValue,
                                           optionLevel));
      } // for
      // retorna
      FormSelectEx.Option[] result = new FormSelectEx.Option[vector.size()];
      vector.copyInto(result);
      return result;
    }
    finally {
      // libera recursos
      if (preparedSelect.getResultSet() != null) {
        preparedSelect.getResultSet().close();
        preparedSelect.close();
      } // if
    }
  }

  /**
   * Retorna o script HTML representando o controle de edição.
   * @param facade Instância da fachada a ser utilizada.
   * @param id Identificação do LookupSelectEx na página.
   * @param className Nome da classe da entidade que será utilizada.
   * @param displayFields Campos que serão exibidos na listagem.
   * @param levelDelimiter String Delimitador de níveis encontrado nos valores
   *                       do primeiro campo de exibição, exemplo: "/". Quando
   *                       este caractere for encontrado nos valores do
   *                       primeiro campo de exibição, significa que um novo
   *                       nível está sendo definido: Nível 1/Nível 2/Nível 3.
   * @param returnFields Campos cujos valores serão retornados para
   *                     serem utilizados como chave.
   * @param returnFieldValues Valores iniciais para 'returnFieldNames'.
   * @param orderFields Campos para ordenação da listagem.
   * @param filterExpression Expressão SQL para filtro padrão da listagem.
   * @return Retorna o script HTML representando o controle de edição.
   * @throws Exception Em caso de exceção na tentativa de acesso ao banco de
   *                   dados.
   */
  static public String script(Facade        facade,
                              String        id,
                              String        className,
                              EntityField[] displayFields,
                              String        levelDelimiter,
                              EntityField[] returnFields,
                              String[]      returnFieldValues,
                              EntityField[] orderFields,
                              String        filterExpression) throws Exception {
    return script(facade,
                  id,
                  className,
                  displayFields,
                  levelDelimiter,
                  returnFields,
                  returnFieldValues,
                  orderFields,
                  filterExpression,
                  0,
                  "",
                  "",
                  "",
                  "",
                  false,
                  true);
  }

  /**
   * Retorna o script HTML representando o controle de edição.
   * @param facade Instância da fachada a ser utilizada.
   * @param id Identificação do LookupSelectEx na página.
   * @param className Nome da classe da entidade que será utilizada.
   * @param displayFields Campos que serão exibidos na listagem.
   * @param levelDelimiter String Delimitador de níveis encontrado nos valores
   *                       do primeiro campo de exibição, exemplo: "/". Quando
   *                       este caractere for encontrado nos valores do
   *                       primeiro campo de exibição, significa que um novo
   *                       nível está sendo definido: Nível 1/Nível 2/Nível 3.
   * @param returnFields Campos cujos valores serão retornados para
   *                     serem utilizados como chave.
   * @param returnFieldValues Valores iniciais para 'returnFieldNames'.
   * @param orderFields Campos para ordenação da listagem.
   * @param filterExpression Expressão SQL para filtro padrão da listagem.
   * @param width int Largura do LookupSelectEx na página ou 0 (zero) para que ele
   *              se ajuste automaticamente ao seu conteiner.
   * @param constraint String Script JavaScript de validação do valor selecionado
   *                   no LookupList.
   * @param constraintMessage String Mensagem de validação para ser exibida
   *                          ao usuário.
   * @param style Estilo HTML para modificação do elemento HTML
   *              de exibição.
   * @param onChangeScript Código JavaScript para ser executado quando o usuário
   *                       alterar o valor do elemento HTML.
   * @param readOnly boolean True se o LookupSelectEx for somente leitura.
   * @param includeBlank boolean True para que seja incluído o item em
   *                     branco como primeira opção de escolha.
   * @return Retorna o script HTML representando o controle de edição.
   * @throws Exception Em caso de exceção na tentativa de acesso ao banco de
   *                   dados.
   */
  static public String script(Facade        facade,
                              String        id,
                              String        className,
                              EntityField[] displayFields,
                              String        levelDelimiter,
                              EntityField[] returnFields,
                              String[]      returnFieldValues,
                              EntityField[] orderFields,
                              String        filterExpression,
                              int           width,
                              String        constraint,
                              String        constraintMessage,
                              String        style,
                              String        onChangeScript,
                              boolean       readOnly,
                              boolean       includeBlank) throws Exception {

    // valor da seleção atual
    String selectedValue = StringTools.arrayStringToString(returnFieldValues, ";");

    // retorna
    return FormSelectEx.script(facade,
                               id,
                               getSelectExOptions(facade,
                                                  className,
                                                  displayFields,
                                                  levelDelimiter,
                                                  returnFields,
                                                  orderFields,
                                                  filterExpression,
                                                  includeBlank),
                               selectedValue,
                               width,
                               constraint,
                               constraintMessage,
                               style,
                               onChangeScript,
                               readOnly);
  }

}
