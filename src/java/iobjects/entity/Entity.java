package iobjects.entity;

import java.lang.reflect.*;
import java.sql.*;
import java.util.*;
import javax.servlet.http.*;

import iobjects.*;
import iobjects.util.*;

/**
 * Representa a classe base de todas as classes que representam entidades
 * no banco de dados da aplica��o.
 */
public class Entity extends BusinessObject {

  private String           tableName  = "";
  private EntityFieldList  fieldList  = new EntityFieldList();
  private EntityLookupList lookupList = new EntityLookupList();

  /**
   * Construtor padr�o.
   */
  public Entity() {
  }

  /**
   * Exclui o registro representado por 'entifyInfo' da entidade. Este m�todo
   * se baseia nos campos da entidade (FieldList) e nas propriedades da classe
   * EntityInfo informada.
   * @param entityInfo EntityInfo referente ao registro que se deseja excluir.
   * @throws Exception Em caso de exce��o na tentativa de acesso ao banco de
   *         dados ou de leitura dos valores de 'entityInfo'.
   * @since 3.1
   */
  protected void delete(EntityInfo entityInfo) throws Exception {
    // se a conex�o � read only...exce��o
    if (getConnectionFile().readOnly())
      throw new EntityException(getClass().getName(), "delete", "N�o � poss�vel realizar esta opera��o em uma conex�o apenas para leitura.");
    // lista de campos chave
    EntityField[] keyFields = fieldList.getKeyFields();
    // se n�o temos campos chave...exce��o
    if (keyFields.length == 0)
      throw new EntityException(getClass().getName(), "delete", "Nenhum campo chave definido.");
    // cl�usula where
    String where = "";
    for (int i=0; i<keyFields.length; i++) {
      if (!where.equals(""))
        where += " AND ";
      where += "(" + keyFields[i].getFieldName() + " = ?)";
    } // for
    // prepara a exclus�o
    PreparedStatement statement = SqlTools.prepareDelete(getConnection(),
                                                         tableName,
                                                         where);
    try {
      // atribui os par�metros
      for (int i=0; i < keyFields.length; i++) {
        // campo da vez
        EntityField field = keyFields[i];
        // atribui o valor proveniente de 'entityInfo'
        statement.setObject(i+1,
                            entityInfo.getPropertyValue(field.getFieldAlias()),
                            field.getFieldType());
      } // for
      // executa
      statement.execute();
    }
    finally {
      // libera recursos
      if (statement != null)
        statement.close();
    } // try-finally
  }

  /**
   * Retorna um novo EntityInfo com suas propriedades preenchidas a partir dos
   * par�metros em 'request'. Somente as propriedades cujo nome forem encontradas
   * na requisi��o ser�o preenchidas, as demais ficar�o com o valor default
   * fornecido pela pr�pria classe EntityInfo. Os valores contidos nos par�metros
   * da requisi��o, sempre no tipo String, ser�o convertidos para o tipo
   * indicado pelo tipo (getFieldType()) e formato (getFormat()) indicados
   * no EntityField referente � cada propriedade.
   * @param request HttpServletRequest Requisi��o que cont�m os par�metros para
   *                preenchimento de EntityInfo.
   * @throws Exception Em caso de exce��o na convers�o dos par�metros para o
   *                   tipo da propriedades relacionada ou na atribu��o do
   *                   valor em EntityInfo.
   * @return EntityInfo Retorna um novo EntityInfo com suas propriedades preenchidas
   *         a partir dos par�metros em 'request'.
   */
  public EntityInfo entityInfoFromRequest(HttpServletRequest request) throws Exception {
    // carrega a classe de EntityInfo que ser� utilizada
    Class entityInfoClass = Class.forName(getClass().getName() + "Info");
    // nosso resultado
    EntityInfo result = (EntityInfo)entityInfoClass.newInstance();
    // preenche as propriedades com os par�metros da requisi��o
    result = entityInfoFromRequest(request, result);
    // retorna
    return result;
  }

  /**
   * Retorna 'entityInfo' com suas propriedades preenchidas a partir dos
   * par�metros em 'request'. Somente as propriedades cujo nome forem encontradas
   * na requisi��o ser�o preenchidas, as demais ficar�o com o valor default
   * fornecido pela pr�pria classe EntityInfo. Os valores contidos nos par�metros
   * da requisi��o, sempre no tipo String, ser�o convertidos para o tipo
   * indicado pelo tipo (getFieldType()) e formato (getFormat()) indicados
   * no EntityField referente � cada propriedade.
   * @param request HttpServletRequest Requisi��o que cont�m os par�metros para
   *                preenchimento de EntityInfo.
   * @param entityInfo EntityInfo cujas propriedades se deseja preencher a partir
   *                   dos par�metros em 'request'.
   * @throws Exception Em caso de exce��o na convers�o dos par�metros para o
   *                   tipo da propriedades relacionada ou na atribu��o do
   *                   valor em EntityInfo.
   * @return EntityInfo Retorna 'entityInfo' com suas propriedades preenchidas
   *         a partir dos par�metros em 'request'.
   */
  public EntityInfo entityInfoFromRequest(HttpServletRequest request,
                                          EntityInfo         entityInfo) throws Exception {
    // nosso resultado
    EntityInfo result = entityInfo;
    // loop nos campos
    for (int i=0; i<fieldList.size(); i++) {
      // campo da vez
      EntityField field = fieldList.get(i);
      // procura por seu par�metro na requisi��o
      String paramValue = request.getParameter(field.getFieldAlias());
      // se recebemos algo...define o valor no nosso resultado
      if (paramValue != null)
        field.setFormatedFieldValue(result, paramValue.trim());
    } // for
    // loop nos nossos lookups
    for (int i=0; i<lookupList.size(); i++) {
      // lookup da vez
      EntityLookup lookup = lookupList.get(i);
      // procura por seu par�metro na requisi��o
      String paramValue = request.getParameter(lookup.getName());
      // se recebemos algo...define o valor no nosso resultado,
      if (paramValue != null)
        lookup.setKeyFieldValues(result, paramValue);
    } // for
    // retorna
    return result;
  }

  /**
   * @param sql String
   * @return boolean
   * @throws Exception
   * @deprecated
   */
  protected boolean execute(String sql) throws Exception {
    return SqlTools.execute(getConnection(), sql);
  }

  /**
   * @param sql String
   * @return ResultSet
   * @throws Exception
   * @deprecated
   */
  protected ResultSet executeQuery(String sql) throws Exception {
    return SqlTools.executeQuery(getConnection(), sql);
  }

  /**
   * Retorna a lista de campos mantidos pela entidade. A lista de campos da
   * entidade � a base do funcionamento para os m�todos de manipula��o de
   * registros.
   * @return EntityFieldList Retorna a lista de campos mantidos pela entidade.
   */
  public EntityFieldList fieldList() {
    return fieldList;
  }

  /**
   * Retorna 'fieldValue' formatado de acordo com a defini��o de formato de
   * exibi��o configurado em 'entityField'.
   * @param entityField EntityField que representa o campo cujo valor ser�
   *                    formatado.
   * @param fieldValue Object Valor do campo para ser formatado.
   * @return String Retorna 'value' formatado de acordo com a defini��o de
   *         formato de exibi��o configurado em 'entityField'.
   * @throws Exception Em caso de exce��o na formata��o do valor.
   */
  static public String formatFieldValue(EntityField entityField,
                                        Object      fieldValue) throws Exception {
    // se temos uma lista lookup e o campo � inteiro...
    if ((entityField.getLookupList().length > 0) && entityField.isInteger()) {
      // o valor do campo deve corresponder ao �ndice da lista de pesquisa
      int lookupIndex = (fieldValue != null ? Integer.parseInt(fieldValue.toString()) : 0);
      // retorna o valor correspondente na lista de pesquisa
      return entityField.getLookupList()[lookupIndex];
    }
    // se n�o temos uma lista lookup...
    else {
      // se temos uma m�scara...
      if (!entityField.getMask().equals(""))
        return (fieldValue != null ? StringTools.formatCustomMask(fieldValue.toString(), entityField.getMask()) : "");
      // se temos um formato...
      else if (entityField.getFormat() != EntityField.FORMAT_NONE) {
        switch (entityField.getFormat()) {
          case EntityField.FORMAT_DATE:
            return (fieldValue != null ? DateTools.formatDate((Timestamp)fieldValue) : DateTools.formatDate(DateTools.ZERO_DATE));
          case EntityField.FORMAT_DATE_TIME:
            return (fieldValue != null ? DateTools.formatDateTime((Timestamp)fieldValue) : DateTools.formatDateTime(DateTools.ZERO_DATE));
          case EntityField.FORMAT_TIME:
            return (fieldValue != null ? DateTools.formatTime((Timestamp)fieldValue) : DateTools.formatTime(DateTools.ZERO_DATE));
          case EntityField.FORMAT_INTEGER:
            return (fieldValue != null ? NumberTools.format(Integer.parseInt(fieldValue.toString())) : NumberTools.format(0));
          case EntityField.FORMAT_DOUBLE:
            return (fieldValue != null ? NumberTools.format(Double.parseDouble(fieldValue.toString()), entityField.getFieldPrecision(), entityField.getFieldPrecision()) : NumberTools.format(0, entityField.getFieldPrecision(), entityField.getFieldPrecision()));
          case EntityField.FORMAT_UPPER_CASE:
            return (fieldValue != null ? fieldValue.toString().toUpperCase() : "");
          default:
            return (fieldValue != null ? fieldValue.toString() : "");
        } // switch
      }
      // se n�o temos nada...
      else
        return (fieldValue != null ? fieldValue.toString() : "");
    } // if
  }

  /**
   * Preenche 'entifyInfo' com os valores do registro atual de 'ResultSet'.
   * @param resultSet ResultSet cujo registro ser� lido.
   * @param entityInfo EntityInfo cujos valores ser�o preenchidos.
   * @throws Exception Em caso de exce��o na tentativa de ler os valores do
   *         ResultSet ou de definir as propriedades do EntityInfo.
   */
  private void fillEntityInfoFromResultSet(ResultSet  resultSet,
                                           EntityInfo entityInfo) throws Exception {
    // loop nos nossos campos
    for (int i=0; i<fieldList.size(); i++) {
      // campo da vez
      EntityField field = fieldList.get(i);
      // define seu valor no objeto
      Object value = null;
      switch (field.getFieldType()) {
        case Types.INTEGER  : value = resultSet.getInt(field.getFieldName()); break;
        case Types.SMALLINT : value = resultSet.getInt(field.getFieldName()); break;
        case Types.DOUBLE   : value = resultSet.getDouble(field.getFieldName()); break;
        case Types.NUMERIC  : value = resultSet.getDouble(field.getFieldName()); break;
        case Types.TIMESTAMP: value = resultSet.getTimestamp(field.getFieldName()); break;
        case Types.DATE     : value = resultSet.getTimestamp(field.getFieldName()); break;
        case Types.TIME     : value = resultSet.getTimestamp(field.getFieldName()); break;
        default:
          value = resultSet.getObject(field.getFieldName());
      } // switch
      entityInfo.setPropertyValue(field.getFieldAlias(), value);
    } // for
    // loop nos nossos lookups
    entityInfo.lookupValueList().clear();
    for (int i=0; i<lookupList.size(); i++) {
      // lookup da vez
      EntityLookup lookup = lookupList.get(i);
      // obt�m os valores dos campos de exibi��o do lookup no ResultSet
      String[] displayFieldValues = new String[lookup.getLookupDisplayFields().length];
      for (int w=0; w<lookup.getLookupDisplayFields().length; w++) {
        EntityField displayField = lookup.getLookupDisplayFields()[w];
        displayFieldValues[w] = formatFieldValue(displayField, resultSet.getObject(lookup.getName() + "_" + displayField.getFieldName()));
      } // for
      // adiciona o valor do lookup � lista
      entityInfo.lookupValueList().add(new EntityLookupValue(lookup, displayFieldValues));
    } // for
  }

  /**
   * @param tableName String
   * @param sequenceFieldName String
   * @return int
   * @throws Exception
   * @deprecated
   */
  protected int getNextSequence(String tableName,
                                String sequenceFieldName) throws Exception {
    return SqlTools.getNextSequence(getConnection(),
                                    tableName,
                                    sequenceFieldName,
                                    false);
  }

  /**
   * @param tableName String
   * @param sequenceFieldName String
   * @param where String
   * @return int
   * @throws Exception
   * @deprecated
   */
  protected int getNextSequence(String tableName,
                                String sequenceFieldName,
                                String where) throws Exception {
    return SqlTools.getNextSequence(getConnection(),
                                    tableName,
                                    sequenceFieldName,
                                    where,
                                    false);
  }

  /**
   * Retorna o pr�ximo valor da seq��ncia hier�rquica na entidade formada por
   * 'sequenceFieldName', de acordo com a express�o 'parentWhere'.
   * @param sequenceField Campo formador da seq��ncia hier�rquica.
   * @param where Express�o que limita a faixa da seq��ncia que ser� gerada.
   * @param parentSequence Valor da seq��ncia do registro pai. Esta � a refer�ncia
   *                       a partir da qual poder� ser determinado o pr�ximo valor
   *                       da seq��ncia do registro filho.
   * @return Retorna o pr�ximo valor da seq��ncia hier�rquica na entidade formada
   *         por 'sequenceFieldName', de acordo com a express�o 'parentWhere'.
   * @throws Exception Em caso de exce��o na tentativa de acesso ao banco de
   *         dados.
   */
  protected String getNextParentSequence(EntityField sequenceField,
                                         String      where,
                                         String      parentSequence) throws Exception {
    return SqlTools.getNextParentSequence(getConnection(),
                                          tableName,
                                          sequenceField.getFieldName(),
                                          where,
                                          parentSequence);
  }

  /**
   * Retorna o pr�ximo valor da seq��ncia na entidade formada por
   * 'sequenceFieldName'.
   * @param sequenceField Campo formador da seq��ncia.
   * @return Retorna o pr�ximo valor da seq��ncia na entidade formada por
   * 'sequenceFieldName'.
   * @throws Exception Em caso de exce��o na tentativa de acesso ao banco de
   *         dados.
   */
  protected int getNextSequence(EntityField sequenceField) throws Exception {
    return SqlTools.getNextSequence(getConnection(),
                                    tableName,
                                    sequenceField.getFieldName(),
                                    false);
  }

  /**
   * Retorna o pr�ximo valor da seq��ncia na entidade formada por
   * 'sequenceFieldName'.
   * @param sequenceField Campo formador da seq��ncia.
   * @param digitVerifier True para adicionar o d�gito verificador ao valor
   *                      retornado pela seq��ncia.
   * @return Retorna o pr�ximo valor da seq��ncia na entidade formada por
   * 'sequenceFieldName'.
   * @throws Exception Em caso de exce��o na tentativa de acesso ao banco de
   *         dados.
   */
  protected int getNextSequence(EntityField sequenceField,
                                boolean     digitVerifier) throws Exception {
    return SqlTools.getNextSequence(getConnection(),
                                    tableName,
                                    sequenceField.getFieldName(),
                                    digitVerifier);
  }

  /**
   * Retorna o pr�ximo valor da seq��ncia na entidade formada por 'sequenceFieldName',
   * de acordo com a express�o 'where'.
   * @param sequenceField Campo formador da seq��ncia.
   * @param where Express�o que limita a faixa da seq��ncia que ser� gerada.
   * @return Retorna o pr�ximo valor da seq��ncia na entidade formada por
   *         'sequenceFieldName', de acordo com a express�o 'where'.
   * @throws Exception Em caso de exce��o na tentativa de acesso ao banco de
   *         dados.
   */
  protected int getNextSequence(EntityField sequenceField,
                                String      where) throws Exception {
    return SqlTools.getNextSequence(getConnection(),
                                    tableName,
                                    sequenceField.getFieldName(),
                                    where,
                                    false);
  }

  /**
   * Retorna o pr�ximo valor da seq��ncia na entidade formada por 'sequenceFieldName',
   * de acordo com a express�o 'where'.
   * @param sequenceField Campo formador da seq��ncia.
   * @param where Express�o que limita a faixa da seq��ncia que ser� gerada.
   * @param digitVerifier True para adicionar o d�gito verificador ao valor
   *                      retornado pela seq��ncia.
   * @return Retorna o pr�ximo valor da seq��ncia na entidade formada por
   *         'sequenceFieldName', de acordo com a express�o 'where'.
   * @throws Exception Em caso de exce��o na tentativa de acesso ao banco de
   *         dados.
   */
  protected int getNextSequence(EntityField sequenceField,
                                String      where,
                                boolean     digitVerifier) throws Exception {
    return SqlTools.getNextSequence(getConnection(),
                                    tableName,
                                    sequenceField.getFieldName(),
                                    where,
                                    digitVerifier);
  }

  /**
   * Retorna o nome da tabela gerenciada pela entidade.
   * @return String Retorna o nome da tabela gerenciada pela entidade.
   */
  public String getTableName() {
    return tableName;
  }

  /**
   * Insere o registro representado por 'entifyInfo' na entidade. Este m�todo
   * se baseia nos campos da entidade (FieldList) e nas propriedades da classe
   * EntityInfo informada.
   * @param entityInfo EntityInfo referente ao registro que se deseja inserir.
   * @throws Exception Em caso de exce��o na tentativa de acesso ao banco de
   *         dados ou de leitura dos valores de 'entityInfo'.
   * @since 3.1
   */
  protected void insert(EntityInfo entityInfo) throws Exception {
    // se a conex�o � read only...exce��o
    if (getConnectionFile().readOnly())
      throw new EntityException(getClass().getName(), "insert", "N�o � poss�vel realizar esta opera��o em uma conex�o apenas para leitura.");
    // nomes dos campos
    String[] fieldNames = fieldList.getFieldNames();
    // prepara a inclus�o
    PreparedStatement statement = SqlTools.prepareInsert(getConnection(),
                                                         tableName,
                                                         fieldNames);
    try {
      // p�e os valores
      for (int i=0; i<fieldList.size(); i++) {
        // campo da vez
        EntityField field = fieldList.get(i);
        // p�e seu valor no Statement
        statement.setObject(i+1,
                            entityInfo.getPropertyValue(field.getFieldAlias()),
                            field.getFieldType());
      } // for
      // executa
      statement.execute();
    }
    finally {
      // libera recursos
      if (statement != null)
        statement.close();
    } // try-finally
  }

  /**
   * Retorna a lista de lookup mantidos pela entidade.
   * @return EntityLookupList Retorna a lista de lookups mantidos pela entidade.
   */
  public EntityLookupList lookupList() {
    return lookupList;
  }

  /**
   * Retorna 'formatedFieldValue' convertido para o tipo correto indicado pelo
   * tipo e formato configurados em 'entityField'.
   * @param entityField EntityField que representa o campo cujo valor ser�
   *                    convertido.
   * @param formatedFieldValue String Valor formatado para ser convertido no
   *                           tipo correto.
   * @throws Exception Em caso de exce��o na tentativa de converter o valor
   *                   no tipo relacionado.
   * @return String Retorna 'formatedFieldValue' convertido para o tipo correto
   *                indicado pelo tipo e formato configurados em 'entityField'.
   */
  static public Object parseFormatedFieldValue(EntityField entityField,
                                               String      formatedFieldValue) throws Exception {
    // se temos uma lista lookup e o campo � inteiro...
    if ((entityField.getLookupList().length > 0) && entityField.isInteger()) {
      // o valor informado deve corresponder a um valor na lista de pesquisa...
      int result = StringTools.arrayIndexOf(entityField.getLookupList(),
                                            formatedFieldValue);
      // se encontramos um �ndice v�lido...retorna
      if (result >= 0)
        return new Integer(result);
      // se n�o encontramos um �ndice v�lido...retorna o pr�prio valor recebido
      else
        return new Integer(NumberTools.parseInt(formatedFieldValue));
    }
    // se � um tipo String...retorna
    else if (entityField.isString()) {
      // se temos uma m�scara...
      if (!entityField.getMask().equals(""))
        return StringTools.removeCustomMask(formatedFieldValue, entityField.getMask());
      // se tem um formato UPPER_CASE...sempre usa esse formato
      if (entityField.getFormat() == EntityField.FORMAT_UPPER_CASE)
        return formatedFieldValue.replaceAll("'", "").replaceAll("\"", "").toUpperCase();
      // se n�o temos nada...
      else
        return formatedFieldValue;
    }
    // se � um tipo inteiro...retorna
    else if (entityField.isInteger())
      return new Integer(NumberTools.parseInt(formatedFieldValue));
    // se � um tipo double...retorna
    else if (entityField.isDouble())
      return new Double(NumberTools.parseDouble(formatedFieldValue, entityField.getFieldPrecision()));
    // se � um tipo l�gico...retorna
    else if (entityField.isBoolean())
      return Boolean.valueOf(formatedFieldValue);
    // se � um tipo data...retorna
    else if (entityField.isDate()) {
      if (entityField.getFormat() == EntityField.FORMAT_DATE) return DateTools.parseDate(formatedFieldValue);
      else if (entityField.getFormat() == EntityField.FORMAT_TIME) return DateTools.parseTime(formatedFieldValue);
      else return DateTools.parseDateTime(formatedFieldValue);
    }
    // se � um tipo hora...retorna
    else if (entityField.isTime()) {
      if (entityField.getFormat() == EntityField.FORMAT_DATE) return DateTools.parseDate(formatedFieldValue);
      else if (entityField.getFormat() == EntityField.FORMAT_TIME) return DateTools.parseTime(formatedFieldValue);
      else return DateTools.parseDateTime(formatedFieldValue);
    }
    // se � um tipo timestamp...retorna
    else if (entityField.isTimestamp()) {
      if (entityField.getFormat() == EntityField.FORMAT_DATE) return DateTools.parseDate(formatedFieldValue);
      else if (entityField.getFormat() == EntityField.FORMAT_TIME) return DateTools.parseTime(formatedFieldValue);
      else return DateTools.parseDateTime(formatedFieldValue);
    }
    // se � um tipo desconhecido...exce��o
    else
      throw new EntityException(Entity.class.getName(), "parseFormatedFieldValue", "Tipo do campo (" + entityField.getCaption() + ") desconhecido.");
  }

  /**
   * @param sql String
   * @return String
   * @throws Exception
   * @deprecated
   */
  protected PreparedStatement prepare(String sql) throws Exception {
    return SqlTools.prepare(getConnection(), sql);
  }

  /**
   * @param tableName String
   * @param where String
   * @return String
   * @throws Exception
   * @deprecated
   */
  protected PreparedStatement prepareDelete(String   tableName,
                                            String   where) throws Exception {
    return SqlTools.prepareDelete(getConnection(), tableName, where);
  }

  /**
   * @param tableName String
   * @param fields String[]
   * @return PreparedStatement
   * @throws Exception
   * @deprecated
   */
  protected PreparedStatement prepareInsert(String   tableName,
                                            String[] fields) throws Exception {
    return SqlTools.prepareInsert(getConnection(),
                                  tableName,
                                  fields);
  }

  /**
   * @param tableName String
   * @param fields Nomes dos campos cujos valores ser�o inclu�dos.
   * @param values String[]
   * @return PreparedStatement
   * @throws Exception
   * @deprecated
   */
  protected PreparedStatement prepareInsert(String   tableName,
                                            String[] fields,
                                            String[] values) throws Exception {
    return SqlTools.prepareInsert(getConnection(),
                                  tableName,
                                  fields,
                                  values);
  }

  /**
   * @param tableName String
   * @param fields String[]
   * @param orderFields String[]
   * @param where String
   * @return PreparedStatement
   * @throws Exception
   * @deprecated
   */
  protected PreparedStatement prepareSelect(String   tableName,
                                            String[] fields,
                                            String[] orderFields,
                                            String   where) throws Exception {
    return SqlTools.prepareSelect(getConnection(),
                                  tableName,
                                  fields,
                                  orderFields,
                                  where);
  }

  /**
   * Retorna um PreparedStatement que ir� retornar os registros da entidade
   * que atendam � express�o 'where' informada.
   * @param where String Express�o SQL que ir� limitar os registros retornados
   *              pela consulta.
   * @throws Exception Em caso de exce��o na tentativa de preparar a consulta.
   * @return PreparedStatement Retorna um PreparedStatement que ir� retornar os
   *         registros da entidade que atendam � express�o 'where' informada.
   */
  public PreparedStatement prepareSelect(String where) throws Exception {
    String[] orderFields = {fieldList.get(0).getFieldName(tableName)};
    return prepareSelect(orderFields, where);
  }

  /**
   * Retorna um PreparedStatement que ir� retornar os registros da entidade,
   * na ordem indicada por 'orderFields' e que atendam � express�o 'where' informada.
   * @param orderFieldNames Nomes dos campos para ordena��o. Utilize 'DESC' ap�s
   *                        o nome do campo para ordena��o descendente.
   *                        <b>Se o Entity possuir EntityLookup's � poss�vel
   *                        utilizar quaisquer campos da tabela de lookup para
   *                        ordena��o, desde que os nomes dos campos estejam na
   *                        forma: 'tableName.fieldName'.</b>
   * @param where String Express�o SQL que ir� limitar os registros retornados
   *              pela consulta.
   * @throws Exception Em caso de exce��o na tentativa de preparar a consulta.
   * @return PreparedStatement Retorna um PreparedStatement que ir� retornar os
   *         registros da entidade, na ordem indicada por 'orderFields' e que
   *         atendam � express�o 'where' informada.
   */
  public PreparedStatement prepareSelect(String[] orderFieldNames,
                                         String   where) throws Exception {
    return prepareSelect(orderFieldNames, where, new String[0]);
  }

  /**
   * Retorna um PreparedStatement que ir� retornar os registros da entidade,
   * na ordem indicada por 'orderFields' e que atendam � express�o 'where' informada.
   * @param orderFieldNames Nomes dos campos para ordena��o. Utilize 'DESC' ap�s
   *                        o nome do campo para ordena��o descendente.
   *                        <b>Se o Entity possuir EntityLookup's � poss�vel
   *                        utilizar quaisquer campos da tabela de lookup para
   *                        ordena��o, desde que os nomes dos campos estejam na
   *                        forma: 'tableName.fieldName'.</b>
   * @param where String Express�o SQL que ir� limitar os registros retornados
   *              pela consulta.
   * @param extraFieldNames Nomes dos campos extras para adicionar a cl�sula SELECT.
   * @throws Exception Em caso de exce��o na tentativa de preparar a consulta.
   * @return PreparedStatement Retorna um PreparedStatement que ir� retornar os
   *         registros da entidade, na ordem indicada por 'orderFields' e que
   *         atendam � express�o 'where' informada.
   */
  public PreparedStatement prepareSelect(String[] orderFieldNames,
                                         String   where,
                                         String[] extraFieldNames) throws Exception {
    return prepareSelect(orderFieldNames, where, extraFieldNames, null);
  }

  /**
   * Retorna um PreparedStatement que ir� retornar os registros da entidade,
   * na ordem indicada por 'orderFields' e que atendam � express�o 'where' informada.
   * @param orderFieldNames Nomes dos campos para ordena��o. Utilize 'DESC' ap�s
   *                        o nome do campo para ordena��o descendente.
   *                        <b>Se o Entity possuir EntityLookup's � poss�vel
   *                        utilizar quaisquer campos da tabela de lookup para
   *                        ordena��o, desde que os nomes dos campos estejam na
   *                        forma: 'tableName.fieldName'.</b>
   * @param where String Express�o SQL que ir� limitar os registros retornados
   *              pela consulta.
   * @param extraFieldNames Nomes dos campos extras para adicionar a cl�sula SELECT.
   * @param paginate Op��es de pagina��o dos registros da entidade.
   *                 <p><b>Esta propriedade � espec�fica para PostgreSQL e MySQL. 
   *                 Para SQLServer e Oracle deve ser usada a fun��o RowNumber 
   *                 na cl�usula SELECT.</b></p>
   *                 <p><b>O m�doto Paginate.hasMorePages() n�o funciona a partir 
   *                 deste m�todo. Contudo, ser� selecionado um registro a mais
   *                 da quantidade informada em Paginate.getPageSize(), permitindo
   *                 que se determine se existem mais p�ginas de registros 
   *                 dispon�veis.</b></p>
   * @throws Exception Em caso de exce��o na tentativa de preparar a consulta.
   * @return PreparedStatement Retorna um PreparedStatement que ir� retornar os
   *         registros da entidade, na ordem indicada por 'orderFields' e que
   *         atendam � express�o 'where' informada.
   */
  public PreparedStatement prepareSelect(String[] orderFieldNames,
                                         String   where,
                                         String[] extraFieldNames,
                                         Paginate paginate) throws Exception {
    // express�o SQL
    StringBuffer sql = new StringBuffer("SELECT ");
    // loop nos nossos campos
    for (int i=0; i<fieldList.size(); i++) {
      if (i>0)
        sql.append(",");
      sql.append(fieldList.get(i).getFieldName(tableName));
    } // for
    // loop nos lookups
    for (int i=0; i<lookupList.size(); i++) {
      // lookup da vez
      EntityLookup lookup = lookupList.get(i);
      // inst�ncia da entidade de lookup
      Entity lookupEntity = getFacade().getEntity(lookup.getLookupEntityClassName());
      // se a entidade lookup utiliza uma conex�o diferente
      // da nossa...n�o podemos realizar o join
      if (!getConnectionName().equals(lookupEntity.getConnectionName()))
        throw new EntityException(getClass().getName(), "select", "Entidade " + lookupEntity.getTableName() + " n�o pode ser inclu�da na consulta por utilizar outra conex�o.");
      // adiciona o nome dos campos de retorno do lookup
      for (int w=0; w<lookup.getLookupDisplayFields().length; w++) {
        // campo de retorno da vez
        EntityField lookupDisplayField = lookup.getLookupDisplayFields()[w];
        // adiciona no SELECT utilizando o ALIAS da tabela e criando um ALIAS para o campo
        sql.append("," + lookupDisplayField.getFieldName(lookup.getName()) + " AS " + lookup.getName() + "_" + lookupDisplayField.getFieldName());
      } // for
    } // for
    // loop nos campos extras
    for (int i=0; i<extraFieldNames.length; i++) {
      sql.append("," + extraFieldNames[i]);
    } // for
    // nome tabela principal
    sql.append(" FROM " + tableName);
    // constr�i os joins
    for (int i=0; i<lookupList.size(); i++) {
      // lookup da vez
      EntityLookup lookup = lookupList.get(i);
      // inst�ncia da entidade de lookup
      Entity lookupEntity = getFacade().getEntity(lookup.getLookupEntityClassName());
      // chave prim�ria da entidade de lookup
      EntityField[] lookupKeyFields = lookupEntity.fieldList.getKeyFields();
      // verifica se a chave estrangeira possui a mesma quantidade
      // de campos da chave prim�ria da tabela lookup
      if (lookup.getKeyFields().length != lookupEntity.fieldList.getKeyFields().length)
        throw new EntityException(getClass().getName(), "select", "A chave do lookup " + lookup.getName() + " n�o combina com a chave prim�ria da entidade " + lookupEntity.getTableName() + ".");
      // adiciona o join
      sql.append(" LEFT JOIN " + lookupEntity.getTableName() + " AS " + lookup.getName() + " ON (");
      // rela��o entre a tabela principal e a lookup
      for (int w=0; w<lookup.getKeyFields().length; w++) {
        if (w > 0)
          sql.append(" AND ");
        sql.append(lookup.getKeyFields()[w].getFieldName(getTableName()) + "=" + lookupKeyFields[w].getFieldName(lookup.getName()));
      } // for
      // finaliza o join
      sql.append(") ");
    } // for
    // temos where?
    if ((where != null) && (!where.equals("")))
      sql.append(" WHERE (" + where + ")");
    // loop nos campos de ordena��o
    if (orderFieldNames.length > 0) {
      sql.append(" ORDER BY ");
      for (int i=0; i<orderFieldNames.length; i++) {
        if (i>0)
          sql.append(",");
        sql.append(orderFieldNames[i]);
      } // for
    } // if
    // pagina��o
    if ((paginate != null) && (paginate.getPageSize() > 0)) {
      sql.append(" LIMIT " + (paginate.getPageSize() + 1) + " OFFSET " + (paginate.getPageSize() * paginate.getPageNumber()));
    } // if
    // executa o SQL
    return SqlTools.prepare(getConnection(), sql.toString());
  }

  /**
   * @param tableName String
   * @param fields String[]
   * @param where String
   * @return PreparedStatement
   * @throws Exception
   * @deprecated
   */
  protected PreparedStatement prepareUpdate(String   tableName,
                                            String[] fields,
                                            String   where) throws Exception {
    return SqlTools.prepareUpdate(getConnection(),
                                  tableName,
                                  fields,
                                  where);
  }

  /**
   * @param tableName String
   * @param fields String[]
   * @param values String[]
   * @param where String
   * @return PreparedStatement
   * @throws Exception
   * @deprecated
   */
  protected PreparedStatement prepareUpdate(String   tableName,
                                            String[] fields,
                                            String[] values,
                                            String   where) throws Exception {
    return SqlTools.prepareUpdate(getConnection(),
                                  tableName,
                                  fields,
                                  values,
                                  where);
  }

  /**
   * Retorna 'entityInfo' com os dados atualizados a partir do banco de dados.
   * @param entityInfo EntityInfo que se deseja retornar atualizado.
   * @return Retorna 'entityInfo' com os dados atualizados a partir do banco de dados.
   * @throws Exception Em caso de exce��o na tentativa de acesso ao banco de
   *                   dados ou de escrita dos valores de EntityInfo.
   */
  public EntityInfo refresh(EntityInfo entityInfo) throws Exception {
    // lista de campos chave
    EntityField[] keyFields = fieldList.getKeyFields();
    // se n�o temos campos chave...exce��o
    if (keyFields.length == 0)
      throw new EntityException(getClass().getName(), "refresh", "Nenhum campo chave definido.");
    // cl�usula where
    String where = "";
    for (int i=0; i<keyFields.length; i++) {
      if (!where.equals(""))
        where += " AND ";
      where += "(" + keyFields[i].getFieldName(tableName) + " = ?)";
    } // for
    // prepara a consulta
    PreparedStatement statement = prepareSelect(where);
    try {
      // atribui os par�metros do where
      for (int i=0; i<keyFields.length; i++) {
        // campo da vez
        EntityField field = keyFields[i];
        // atribui o valor proveniente de 'entityInfo'
        statement.setObject(i+1,
                            entityInfo.getPropertyValue(field.getFieldAlias()),
                            field.getFieldType());
      } // for
      // retorna
      return select(statement)[0];
    }
    finally {
      // libera recursos
      if (statement != null)
        statement.close();
    } // try-finally
  }

  /**
   * Retorna um Vector contendo os registros da entidade retornados pelo
   * 'preparedStatement' informado.
   * @param preparedStatement PreparedStatement referente � pesquisa cujos
   *                          registros se deseja retornar.
   * @throws Exception Em caso de exce��o na tentativa de acesso ao banco de
   *                   dados ou de escrita dos valores de EntityInfo.
   * @return Vector contendo os registros da entidade retornados pelo
   *         'preparedStatement' informado.
   */
  public EntityInfo[] select(PreparedStatement preparedStatement) throws Exception {
    return select(preparedStatement, null);
  }

  /**
   * Retorna um EntityInfo[] contendo os registros da entidade retornados pelo
   * 'preparedStatement' informado.
   * @param preparedStatement PreparedStatement referente � pesquisa cujos
   *                          registros se deseja retornar.
   * @param paginate Op��es de pagina��o dos registros da entidade.
   *                 <p><b>Esta propriedade � espec�fica para PostgreSQL e MySQL. 
   *                 Para SQLServer e Oracle deve ser usada a fun��o RowNumber 
   *                 na cl�usula SELECT.</b></p>
   * @throws Exception Em caso de exce��o na tentativa de acesso ao banco de
   *                   dados ou de escrita dos valores de EntityInfo.
   * @return EntityInfo[] contendo os registros da entidade retornados pelo
   *         'preparedStatement' informado.
   */
  public EntityInfo[] select(PreparedStatement preparedStatement,
                             Paginate          paginate) throws Exception {
    // carrega a classe de EntityInfo que ser� utilizada
    Class entityInfoClass = Class.forName(getClass().getName() + "Info");
    try {
      // executa o Statement
      preparedStatement.execute();
      // pega o ResultSet
      ResultSet resultSet = preparedStatement.getResultSet();
      // loop nos registros limitando a quantidade desejada
      Vector vector = new Vector();
      while ((paginate != null ? vector.size() < paginate.getPageSize() : true) && resultSet.next()) {
        // EntityInfo da vez
        EntityInfo entityInfo = (EntityInfo)entityInfoClass.newInstance();
        // preenche suas propriedades com o registro atual
        fillEntityInfoFromResultSet(resultSet, entityInfo);
        // p�e na lista
        vector.add(entityInfo);
      } // while
      // temos mais p�ginas?
      if (paginate != null)
        paginate.morePages = resultSet.next();
      // retorna
      EntityInfo[] result = (EntityInfo[])java.lang.reflect.Array.newInstance(entityInfoClass, vector.size());
      vector.copyInto(result);
      return result;
    }
    finally {
      if (preparedStatement != null) {
        preparedStatement.close();
      } // if
    } // try-finally
  }

  /**
   * Define o nome da tabela gerenciada pela entidade.
   * @param tableName String Nome da tabela gerenciada pela entidade.
   */
  public void setTableName(String tableName) {
    this.tableName = tableName;
  }

  /**
   * Atualiza o registro representado por 'entifyInfo' na entidade. Este m�todo
   * se baseia nos campos da entidade (FieldList) e nas propriedades da classe
   * EntityInfo informada.
   * @param entityInfo EntityInfo referente ao registro que se deseja atualizar.
   * @throws Exception Em caso de exce��o na tentativa de acesso ao banco de
   *         dados ou de leitura dos valores de 'entityInfo'.
   */
  protected void update(EntityInfo entityInfo) throws Exception {
    // se a conex�o � read only...exce��o
    if (getConnectionFile().readOnly())
      throw new EntityException(getClass().getName(), "update", "N�o � poss�vel realizar esta opera��o em uma conex�o apenas para leitura.");
    // lista de campos chave
    EntityField[] keyFields = fieldList.getKeyFields();
    // se n�o temos campos chave...exce��o
    if (keyFields.length == 0)
      throw new EntityException(getClass().getName(), "update", "Nenhum campo chave definido.");
    // cl�usula where
    String where = "";
    for (int i=0; i<keyFields.length; i++) {
      if (!where.equals(""))
        where += " AND ";
      where += "(" + keyFields[i].getFieldName() + " = ?)";
    } // for
    // prepara a atualiza��o
    PreparedStatement statement = SqlTools.prepareUpdate(getConnection(),
                                                         tableName,
                                                         fieldList.getFieldNames(false),
                                                         where);
    try {
      // �ndice de par�metros
      int index = 1;
      // atribui os par�metros dos campos
      for (int i=0; i<fieldList.size(); i++) {
        // campo da vez
        EntityField field = fieldList.get(i);
        // se � readOnly...continua
        if (field.getReadOnly())
          continue;
        // atribui o valor proveniente de 'entityInfo'
        statement.setObject(index++,
                            entityInfo.getPropertyValue(field.getFieldAlias()),
                            field.getFieldType());
      } // for
      // atribui os par�metros do where
      for (int i=0; i<keyFields.length; i++) {
        // campo da vez
        EntityField field = keyFields[i];
        // atribui o valor proveniente de 'entityInfo'
        statement.setObject(index++,
                            entityInfo.getPropertyValue(field.getFieldAlias()),
                            field.getFieldType());
      } // for
      // executa
      statement.execute();
    }
    finally {
      // libera recursos
      if (statement != null)
        statement.close();
    } // try-finally
  }

}
