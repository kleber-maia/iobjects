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
import javax.servlet.http.*;

import iobjects.*;
import iobjects.entity.*;
import iobjects.servlet.*;
import iobjects.util.*;

/**
 * Representa um controle de sele��o de valores em um Form associado a uma
 * entidade. Esses valores s�o provenientes de uma consulta realizada a partir
 * das defini��es de um EntityLookup.
 */
public class EntityFormLookupSelect {

  static private final String ON_CHANGE_SCRIPT = "style.color = '#0000FF';";

  private EntityFormLookupSelect() {
  }

  /**
   * Retorna uma String contendo os valores dos campos informados por 'fields',
   * separados por 'separator' e obtidos a partir de 'resultSet'.
   * @param resultSet ResultSet cujos dados do registro atual ser�o lidos.
   * @param fields EntityField[] cujos valores ser�o lidos e formatados a
   *               partir do ResultSet.
   * @param separator String para ser usada como separador dos valores.
   * @throws Exception Em caso de exce��o na tentativa de formatar os valores
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
      // p�e o separador
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
   * Retorna os elementos 'option' para o elemento 'select'.
   * @param facade Inst�ncia da fachada a ser utilizada.
   * @param entityClassName Nome da classe da entidade que mant�m o EntityLookup.
   * @param entityLookup EntityLookup cujas propriedades ir�o gerar o controle
   *                     de edi��o.
   * @param entityInfo EntityInfo contendo os dados que ser�o exibidos e editados.
   * @param includeBlank boolean True se a primeira op��o de sele��o ser� uma
   *                     op��o em branco.
   * @throws Exception Em caso de exce��o na tentativa de formatar os valores
   *                   dos campos das entidades ou de acesso ao banco de dados.
   * @return String Retorna os elementos 'option' para o elemento 'select'.
   */
  static private String getSelectOptions(Facade       facade,
                                         String       entityClassName,
                                         EntityLookup entityLookup,
                                         EntityInfo   entityInfo,
                                         boolean      includeBlank) throws Exception {
    // inst�ncia da entidade que mant�m o lookup
    Entity entity = facade.getEntity(entityClassName);
    // entidade na qual ser� realizado o lookup
    Entity lookupEntity = entity.getFacade().getEntity(entityLookup.getLookupEntityClassName());
    // conex�o a ser utilizada
    Connection connection = lookupEntity.getConnection();
    // nomes dos campos de exibi��o
    String[] displayFieldNames = new String[entityLookup.getLookupDisplayFields().length];
    for (int i=0; i<entityLookup.getLookupDisplayFields().length; i++)
      displayFieldNames[i] = entityLookup.getLookupDisplayFields()[i].getFieldName();

    // nomes dos campos chave na entidade de pesquisa
    String[] lookupKeyFieldNames = new String[lookupEntity.fieldList().getKeyFields().length];
    for (int i=0; i<lookupEntity.fieldList().getKeyFields().length; i++)
      lookupKeyFieldNames[i] = lookupEntity.fieldList().getKeyFields()[i].getFieldName();

    // nossos campos
    String[] fields = StringTools.arrayConcat(displayFieldNames, lookupKeyFieldNames);
    // valores dos campos de retorno
    String[] returnFieldValues = new String[entityLookup.getKeyFields().length];
    for (int i=0; i<entityLookup.getKeyFields().length; i++)
      returnFieldValues[i] = entityLookup.getKeyFields()[i].getFieldValue(entityInfo).toString();

    // campos de ordena��o
    String[] orderFieldNames = new String[entityLookup.getLookupOrderFields().length];
    for (int i=0; i<entityLookup.getLookupOrderFields().length; i++)
      orderFieldNames[i] = entityLookup.getLookupOrderFields()[i].getFieldName();
    // dados para exibir
    PreparedStatement preparedSelect = SqlTools.prepareSelect(connection,
                                                              lookupEntity.getTableName(),
                                                              fields,
                                                              orderFieldNames,
                                                              entityLookup.getLookupFilterExpression());
    try {
      // nossos dados
      preparedSelect.execute();
      ResultSet resultSet = preparedSelect.getResultSet();
      // nosso resultado
      StringBuffer result = new StringBuffer("");
      // option em branco
      if (includeBlank)
        result.append("<option value=\"\"></option>\r\n");
      // loop nos dados da tabela de pesquisa
      while (resultSet.next()) {
        String optionValue = getFieldValues(resultSet, lookupEntity.fieldList().getKeyFields(), ";");
        result.append("<option value=\"" + optionValue + "\""
                    + (optionValue.equals(StringTools.arrayStringToString(returnFieldValues, ";")) ? "selected" : "") + ">"
                    + Entity.formatFieldValue(entityLookup.getLookupDisplayFields()[0],
                                              resultSet.getObject(entityLookup.getLookupDisplayFields()[0].getFieldName()))
                    + "</option>\r\n");
      } // for
      return result.toString();
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
   * Retorna o script HTML representando o controle de edi��o.
   * @param facade Inst�ncia da fachada a ser utilizada.
   * @param entityClassName Nome da classe da entidade que mant�m o EntityLookup.
   * @param entityLookup EntityLookup cujas propriedades ir�o gerar o controle
   *                     de edi��o.
   * @param entityInfo EntityInfo contendo os dados que ser�o exibidos e editados.
   * @param request HttpServletRequest Requisi��o da p�gina atual.
   * @return String Retorna o script HTML representando o controle de edi��o.
   * @throws Exception Em caso de exce��o na tentativa de obter os valores
   *                   de 'entityInfo'.
   */
  static public String script(Facade             facade,
                              String             entityClassName,
                              EntityLookup       entityLookup,
                              EntityInfo         entityInfo,
                              HttpServletRequest request) throws Exception {
    return script(facade,
                  entityClassName,
                  entityLookup,
                  entityInfo,
                  request,
                  0,
                  "",
                  "",
                  true);
  }

  /**
   * Retorna o script HTML representando o controle de edi��o.
   * @param facade Inst�ncia da fachada a ser utilizada.
   * @param entityClassName Nome da classe da entidade que mant�m o EntityLookup.
   * @param entityLookup EntityLookup cujas propriedades ir�o gerar o controle
   *                     de edi��o.
   * @param entityInfo EntityInfo contendo os dados que ser�o exibidos e editados.
   * @param request HttpServletRequest Requisi��o da p�gina atual.
   * @param width int Largura do controle de edi��o na p�gina ou 0 (zero) para
   *              que ele se ajuste automaticamente ao seu conteiner.
   * @param style String Estilo de formata��o do FormEdit.
   * @param onChangeScript String C�digo JavaScript para ser executado quando o
   *                       usu�rio alterar o valor do elemento HTML.
   * @param includeBlank boolean True para que seja inclu�do o item em
   *                     branco como primeira op��o de escolha.
   * @return String Retorna o script HTML representando o controle de edi��o.
   * @throws Exception Em caso de exce��o na tentativa de obter os valores
   *                   de 'entityInfo'.
   */
  static public String script(Facade             facade,
                              String             entityClassName,
                              EntityLookup       entityLookup,
                              EntityInfo         entityInfo,
                              HttpServletRequest request,
                              int                width,
                              String             style,
                              String             onChangeScript,
                              boolean            includeBlank) throws Exception {
    // verifica se o campo ser� somente leitura
    boolean readOnly = false;
    // se est� editando e o campo n�o � edit�vel...
    if (Controller.isEditing(request) && !entityLookup.getEnabledOnEdit())
      readOnly = true;
    // se est� inserindo e o campo n�o � edit�vel...
    if (Controller.isInserting(request) && !entityLookup.getEnabledOnInsert())
      readOnly = true;
    // constraints dos campos chaves do lookup...
    StringBuffer constraint = new StringBuffer();
    StringBuffer constraintMessage = new StringBuffer();
    for (int i=0; i<entityLookup.getKeyFields().length; i++) {
      // campo chave da vez
      EntityField keyField = entityLookup.getKeyFields()[i];
      // se n�o tem constraint...continua
      if (keyField.getScriptConstraint().equals(""))
        continue;
      // concatena as constraints e as mensagens
      if (constraint.length() > 0)
        constraint.append(" && ");
      if (constraintMessage.length() > 0)
        constraintMessage.append("\r");
      // *
      constraint.append("(" + keyField.getScriptConstraint() + ")");
      constraintMessage.append(keyField.getScriptConstraintErrorMessage());
    } // for
    // evento onChange...troca a cor da fonte quando o usu�rio alterar
    String onChange = ON_CHANGE_SCRIPT + onChangeScript;
    // nosso resultado
    return "<select size=\"1\" "
          +        "name=\"" + entityLookup.getName() + "\" "
          + (constraint.length() > 0
          ?
            // evento de verifica��o da constraint
            "onDblClick=\"if (!eval(" + constraint + ")) {alert('" + constraintMessage + "'); return false;} else return true;\" "
          : "")
          +        "style=\"width:" + (width > 0 ? width + "px" : "100%") + "; " + style + "\" "
          +        (readOnly ? " readOnly=\"readOnly\" " : " ")
          +        "onchange=\"" + onChange + "\" >\r\n"
          + getSelectOptions(facade,
                             entityClassName,
                             entityLookup,
                             entityInfo,
                             includeBlank) + "\r\n"
          + "</select>\r\n";
  }

}
