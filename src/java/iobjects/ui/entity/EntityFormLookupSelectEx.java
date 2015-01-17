package iobjects.ui.entity;

import java.sql.*;
import java.util.*;

import javax.servlet.http.HttpServletRequest;

import iobjects.*;
import iobjects.entity.*;
import iobjects.servlet.*;
import iobjects.ui.*;
import iobjects.util.*;

/**
 * Representa um controle de sele��o de valores em um Form associado a uma
 * entidade. Os valores podem ser exibidos na forma de �rvore e o usu�rio pode
 * realizar pesquisas atrav�s do teclado. Esses valores s�o provenientes de uma
 * consulta realizada a partir das defini��es de um EntityLookup.
 * @since 2006 R1
 */
public class EntityFormLookupSelectEx {

  private EntityFormLookupSelectEx() {
  }

  /**
   * Retorna o script contendo o c�digo JavaScript para altera��o do valor
   * do EntityFormLookupSelectEx na p�gina.
   * @param facade Fachada.
   * @param entityLookup EntityLookup cujas propriedades ir�o gerar o controle
   *                     de edi��o.
   * @param value Valor (entre '') ou nome da vari�vel em JavaScript que ser�
   *              passado como novo valor do LookupSelectEx na p�gina.
   * @return Retorna o script contendo o c�digo JavaScript para altera��o do valor
   *         do EntityFormLookupSelectEx na p�gina.
   */
  static public String changeValue(Facade       facade,
                                   EntityLookup entityLookup,
                                   String       value) {
    return FormSelectEx.changeValue(facade, entityLookup.getName(), value);
  }

  /**
   * Retorna o script contendo o c�digo JavaScript para limpeza do valor
   * selecionado no EntityFormLookupSelectEx.
   * @param facade Facade Fachada.
   * @param entityLookup EntityLookup cujas propriedades ir�o gerar o controle
   *                     de edi��o.
   * @return String Retorna o script contendo o c�digo JavaScript para limpeza
   *                do valor selecionado no EntityFormLookupSelectEx.
   */
  static public String clear(Facade       facade,
                             EntityLookup entityLookup) {
    return FormSelectEx.clear(facade, entityLookup.getName());
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
   * Retorna os objetos 'Option' para o SelectEx.
   * @param facade Inst�ncia da fachada a ser utilizada.
   * @param entityClassName Nome da classe da entidade que mant�m o EntityLookup.
   * @param entityLookup EntityLookup cujas propriedades ir�o gerar o controle
   *                     de edi��o.
   * @param levelDelimiter String Delimitador de n�veis encontrado nos valores
   *                       do primeiro campo de exibi��o, exemplo: "/". Quando
   *                       este caractere for encontrado nos valores do
   *                       primeiro campo de exibi��o, significa que um novo
   *                       n�vel est� sendo definido: N�vel 1/N�vel 2/N�vel 3.
   * @param includeBlank boolean True se a primeira op��o de sele��o ser� uma
   *                     op��o em branco.
   * @throws Exception Em caso de exce��o na tentativa de formatar os valores
   *                   dos campos das entidades ou de acesso ao banco de dados.
   * @return String Retorna os elementos 'option' para o elemento 'select'.
   */
  static private FormSelectEx.Option[] getSelectExOptions(Facade       facade,
                                                          String       entityClassName,
                                                          EntityLookup entityLookup,
                                                          String       levelDelimiter,
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
      Vector vector = new Vector();
      // op��o em branco
      if (includeBlank)
        vector.add(new FormSelectEx.Option("", "", 0));
      // loop nos dados da tabela de pesquisa
      while (resultSet.next()) {
        // caption da op��o
        String optionCaption = Entity.formatFieldValue(entityLookup.getLookupDisplayFields()[0], resultSet.getObject(entityLookup.getLookupDisplayFields()[0].getFieldName()));
        // valor da op��o
        String optionValue = getFieldValues(resultSet, lookupEntity.fieldList().getKeyFields(), ";");
        // n�vel da op��o
        int optionLevel = 0;
        // se temos uma hierarquia simples...
        if (!levelDelimiter.equals("")) {
          // divide o caption em partes
          String[] optionCaptionParts = optionCaption.split(levelDelimiter);
          // define o n�vel da op��o
          optionLevel = optionCaptionParts.length - 1;
          // mostra no caption apenas o �ltimo n�vel
          optionCaption = optionCaptionParts[optionCaptionParts.length-1];
        } // if
        // cria a nova op��o
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
   * Retorna o script HTML representando o controle de edi��o.
   * @param facade Inst�ncia da fachada a ser utilizada.
   * @param entityClassName Nome da classe da entidade que mant�m o EntityLookup.
   * @param entityLookup EntityLookup cujas propriedades ir�o gerar o controle
   *                     de edi��o.
   * @param levelDelimiter String Delimitador de n�veis encontrado nos valores
   *                       do primeiro campo de exibi��o, exemplo: "/". Quando
   *                       este caractere for encontrado nos valores do
   *                       primeiro campo de exibi��o, significa que um novo
   *                       n�vel est� sendo definido: N�vel 1/N�vel 2/N�vel 3.
   * @param entityInfo EntityInfo contendo os dados que ser�o exibidos e editados.
   * @param request HttpServletRequest Requisi��o da p�gina atual.
   * @return String Retorna o script HTML representando o controle de edi��o.
   * @throws Exception Em caso de exce��o na tentativa de obter os valores
   *                   de 'entityInfo'.
   */
  static public String script(Facade             facade,
                              String             entityClassName,
                              EntityLookup       entityLookup,
                              String             levelDelimiter,
                              EntityInfo         entityInfo,
                              HttpServletRequest request) throws Exception {
    return script(facade,
                  entityClassName,
                  entityLookup,
                  levelDelimiter,
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
   * @param levelDelimiter String Delimitador de n�veis encontrado nos valores
   *                       do primeiro campo de exibi��o, exemplo: "/". Quando
   *                       este caractere for encontrado nos valores do
   *                       primeiro campo de exibi��o, significa que um novo
   *                       n�vel est� sendo definido: N�vel 1/N�vel 2/N�vel 3.
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
                              String             levelDelimiter,
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

    // valor da sele��o atual
    String[] selectedFieldValues = new String[entityLookup.getKeyFields().length];
    for (int i=0; i<entityLookup.getKeyFields().length; i++)
      selectedFieldValues[i] = entityLookup.getKeyFields()[i].getFieldValue(entityInfo).toString();
    String selectedValue = StringTools.arrayStringToString(selectedFieldValues, ";");

    // retorna
    return FormSelectEx.script(facade,
                               entityLookup.getName(),
                               getSelectExOptions(facade,
                                                  entityClassName,
                                                  entityLookup,
                                                  levelDelimiter,
                                                  includeBlank),
                               selectedValue,
                               width,
                               constraint.toString(),
                               constraintMessage.toString(),
                               style,
                               onChangeScript,
                               readOnly);
  }

}
