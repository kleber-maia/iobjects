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
 * Representa um controle de seleção de valores em um Form associado a uma
 * entidade. Os valores podem ser exibidos na forma de árvore e o usuário pode
 * realizar pesquisas através do teclado. Esses valores são provenientes de uma
 * consulta realizada a partir das definições de um EntityLookup.
 * @since 2006 R1
 */
public class EntityFormLookupSelectEx {

  private EntityFormLookupSelectEx() {
  }

  /**
   * Retorna o script contendo o código JavaScript para alteração do valor
   * do EntityFormLookupSelectEx na página.
   * @param facade Fachada.
   * @param entityLookup EntityLookup cujas propriedades irão gerar o controle
   *                     de edição.
   * @param value Valor (entre '') ou nome da variável em JavaScript que será
   *              passado como novo valor do LookupSelectEx na página.
   * @return Retorna o script contendo o código JavaScript para alteração do valor
   *         do EntityFormLookupSelectEx na página.
   */
  static public String changeValue(Facade       facade,
                                   EntityLookup entityLookup,
                                   String       value) {
    return FormSelectEx.changeValue(facade, entityLookup.getName(), value);
  }

  /**
   * Retorna o script contendo o código JavaScript para limpeza do valor
   * selecionado no EntityFormLookupSelectEx.
   * @param facade Facade Fachada.
   * @param entityLookup EntityLookup cujas propriedades irão gerar o controle
   *                     de edição.
   * @return String Retorna o script contendo o código JavaScript para limpeza
   *                do valor selecionado no EntityFormLookupSelectEx.
   */
  static public String clear(Facade       facade,
                             EntityLookup entityLookup) {
    return FormSelectEx.clear(facade, entityLookup.getName());
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
   * @param entityClassName Nome da classe da entidade que mantém o EntityLookup.
   * @param entityLookup EntityLookup cujas propriedades irão gerar o controle
   *                     de edição.
   * @param levelDelimiter String Delimitador de níveis encontrado nos valores
   *                       do primeiro campo de exibição, exemplo: "/". Quando
   *                       este caractere for encontrado nos valores do
   *                       primeiro campo de exibição, significa que um novo
   *                       nível está sendo definido: Nível 1/Nível 2/Nível 3.
   * @param includeBlank boolean True se a primeira opção de seleção será uma
   *                     opção em branco.
   * @throws Exception Em caso de exceção na tentativa de formatar os valores
   *                   dos campos das entidades ou de acesso ao banco de dados.
   * @return String Retorna os elementos 'option' para o elemento 'select'.
   */
  static private FormSelectEx.Option[] getSelectExOptions(Facade       facade,
                                                          String       entityClassName,
                                                          EntityLookup entityLookup,
                                                          String       levelDelimiter,
                                                          boolean      includeBlank) throws Exception {
    // instância da entidade que mantém o lookup
    Entity entity = facade.getEntity(entityClassName);
    // entidade na qual será realizado o lookup
    Entity lookupEntity = entity.getFacade().getEntity(entityLookup.getLookupEntityClassName());
    // conexão a ser utilizada
    Connection connection = lookupEntity.getConnection();
    // nomes dos campos de exibição
    String[] displayFieldNames = new String[entityLookup.getLookupDisplayFields().length];
    for (int i=0; i<entityLookup.getLookupDisplayFields().length; i++)
      displayFieldNames[i] = entityLookup.getLookupDisplayFields()[i].getFieldName();

    // nomes dos campos chave na entidade de pesquisa
    String[] lookupKeyFieldNames = new String[lookupEntity.fieldList().getKeyFields().length];
    for (int i=0; i<lookupEntity.fieldList().getKeyFields().length; i++)
      lookupKeyFieldNames[i] = lookupEntity.fieldList().getKeyFields()[i].getFieldName();

    // nossos campos
    String[] fields = StringTools.arrayConcat(displayFieldNames, lookupKeyFieldNames);

    // campos de ordenação
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
      // opção em branco
      if (includeBlank)
        vector.add(new FormSelectEx.Option("", "", 0));
      // loop nos dados da tabela de pesquisa
      while (resultSet.next()) {
        // caption da opção
        String optionCaption = Entity.formatFieldValue(entityLookup.getLookupDisplayFields()[0], resultSet.getObject(entityLookup.getLookupDisplayFields()[0].getFieldName()));
        // valor da opção
        String optionValue = getFieldValues(resultSet, lookupEntity.fieldList().getKeyFields(), ";");
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
   * @param entityClassName Nome da classe da entidade que mantém o EntityLookup.
   * @param entityLookup EntityLookup cujas propriedades irão gerar o controle
   *                     de edição.
   * @param levelDelimiter String Delimitador de níveis encontrado nos valores
   *                       do primeiro campo de exibição, exemplo: "/". Quando
   *                       este caractere for encontrado nos valores do
   *                       primeiro campo de exibição, significa que um novo
   *                       nível está sendo definido: Nível 1/Nível 2/Nível 3.
   * @param entityInfo EntityInfo contendo os dados que serão exibidos e editados.
   * @param request HttpServletRequest Requisição da página atual.
   * @return String Retorna o script HTML representando o controle de edição.
   * @throws Exception Em caso de exceção na tentativa de obter os valores
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
   * Retorna o script HTML representando o controle de edição.
   * @param facade Instância da fachada a ser utilizada.
   * @param entityClassName Nome da classe da entidade que mantém o EntityLookup.
   * @param entityLookup EntityLookup cujas propriedades irão gerar o controle
   *                     de edição.
   * @param levelDelimiter String Delimitador de níveis encontrado nos valores
   *                       do primeiro campo de exibição, exemplo: "/". Quando
   *                       este caractere for encontrado nos valores do
   *                       primeiro campo de exibição, significa que um novo
   *                       nível está sendo definido: Nível 1/Nível 2/Nível 3.
   * @param entityInfo EntityInfo contendo os dados que serão exibidos e editados.
   * @param request HttpServletRequest Requisição da página atual.
   * @param width int Largura do controle de edição na página ou 0 (zero) para
   *              que ele se ajuste automaticamente ao seu conteiner.
   * @param style String Estilo de formatação do FormEdit.
   * @param onChangeScript String Código JavaScript para ser executado quando o
   *                       usuário alterar o valor do elemento HTML.
   * @param includeBlank boolean True para que seja incluído o item em
   *                     branco como primeira opção de escolha.
   * @return String Retorna o script HTML representando o controle de edição.
   * @throws Exception Em caso de exceção na tentativa de obter os valores
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
    // verifica se o campo será somente leitura
    boolean readOnly = false;
    // se está editando e o campo não é editável...
    if (Controller.isEditing(request) && !entityLookup.getEnabledOnEdit())
      readOnly = true;
    // se está inserindo e o campo não é editável...
    if (Controller.isInserting(request) && !entityLookup.getEnabledOnInsert())
      readOnly = true;

    // constraints dos campos chaves do lookup...
    StringBuffer constraint = new StringBuffer();
    StringBuffer constraintMessage = new StringBuffer();
    for (int i=0; i<entityLookup.getKeyFields().length; i++) {
      // campo chave da vez
      EntityField keyField = entityLookup.getKeyFields()[i];
      // se não tem constraint...continua
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

    // valor da seleção atual
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
