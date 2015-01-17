package iobjects.entity;

import java.sql.*;

import iobjects.ui.*;

/**
 * Representa um campo mantido por uma entidade no banco de dados da aplicação.
 * @since 3.1
 */
public class EntityField {

  static public final int ALIGN_LEFT   = Align.ALIGN_LEFT;
  static public final int ALIGN_CENTER = Align.ALIGN_CENTER;
  static public final int ALIGN_RIGHT  = Align.ALIGN_RIGHT;

  static public final int FORMAT_NONE       = Format.FORMAT_NONE;
  static public final int FORMAT_DATE       = Format.FORMAT_DATE;
  static public final int FORMAT_DATE_TIME  = Format.FORMAT_DATE_TIME;
  static public final int FORMAT_TIME       = Format.FORMAT_TIME;
  static public final int FORMAT_INTEGER    = Format.FORMAT_INTEGER;
  static public final int FORMAT_DOUBLE     = Format.FORMAT_DOUBLE;
  static public final int FORMAT_UPPER_CASE = Format.FORMAT_UPPER_CASE;

  private int      align                        = ALIGN_LEFT;
  private String   caption                      = "";
  private String   defaultValue                 = "";
  private String   description                  = "";
  private boolean  enabledOnEdit                = true;
  private boolean  enabledOnInsert              = true;
  private String   fieldAlias                   = "";
  private String   fieldName                    = "";
  private int      fieldPrecision               = 2;
  private int      fieldSize                    = 0;
  private int      fieldType                    = Types.OTHER;
  private int      format                       = FORMAT_NONE;
  private boolean  isKey                        = false;
  private String[] lookupList                   = {};
  private String   mask                         = "";
  private boolean  readOnly                     = false;
  private boolean  required                     = false;
  private String   scriptConstraint             = "";
  private String   scriptConstraintErrorMessage = "";

  /**
   * Construtor padrão.
   * @param fieldName String Nome do campo na tabela.
   * @param caption String Título do campo.
   * @param description String Descrição do campo.
   * @param fieldAlias String Apelido do campo.
   * @param fieldType int Tipo do campo na tabela.
   * @param fieldSize int Tamanho do campo na tabela.
   * @param fieldPrecision int Precisão do campo na tabela.
   * @param isKey boolean True se o campo faz parte da chave primária.
   */
  public EntityField(String  fieldName,
                     String  caption,
                     String  description,
                     String  fieldAlias,
                     int     fieldType,
                     int     fieldSize,
                     int     fieldPrecision,
                     boolean isKey) {
    // nossos valores
    this.fieldName = fieldName;
    this.caption = caption;
    this.description = description;
    this.fieldAlias = fieldAlias;
    this.fieldType = fieldType;
    this.fieldSize = fieldSize;
    this.fieldPrecision = fieldPrecision;
    this.isKey = isKey;
  }

  /**
   * Construtor estendido.
   * @param fieldName String Nome do campo na tabela.
   * @param caption String Título do campo.
   * @param description String Descrição do campo.
   * @param fieldAlias String Apelido do campo.
   * @param fieldType int Tipo do campo na tabela.
   * @param fieldSize int Tamanho do campo na tabela.
   * @param fieldPrecision int Precisão do campo na tabela.
   * @param isKey boolean True se o campo faz parte da chave primária.
   * @param align int Alinhamento do campo.
   * @param enabledOnEdit boolean True se é ativo em tempo de edição.
   * @param enabledOnInsert boolean True se é ativo em tempo de inclusão.
   * @param format int Formato de edição do campo.
   * @param mask String Máscara de edição do campo, ex.: 000.000.000-00.
   * @param required boolean True se a seleção é obrigatória.
   */
  public EntityField(String  fieldName,
                     String  caption,
                     String  description,
                     String  fieldAlias,
                     int     fieldType,
                     int     fieldSize,
                     int     fieldPrecision,
                     boolean isKey,
                     int     align,
                     boolean enabledOnEdit,
                     boolean enabledOnInsert,
                     int     format,
                     String  mask,
                     boolean required) {
    // nossos valores
    this.fieldName = fieldName;
    this.caption = caption;
    this.description = description;
    this.fieldAlias = fieldAlias;
    this.fieldPrecision = fieldPrecision;
    this.isKey = isKey;
    this.fieldSize = fieldSize;
    this.fieldType = fieldType;
    this.align = align;
    this.enabledOnEdit = enabledOnEdit;
    this.enabledOnInsert = enabledOnInsert;
    this.format = format;
    this.mask = mask;
    this.required = required;
  }

  /**
   * Construtor estendido.
   * @param fieldName String Nome do campo na tabela.
   * @param caption String Título do campo.
   * @param description String Descrição do campo.
   * @param fieldAlias String Apelido do campo.
   * @param fieldType int Tipo do campo na tabela.
   * @param fieldSize int Tamanho do campo na tabela.
   * @param fieldPrecision int Precisão do campo na tabela.
   * @param isKey boolean True se o campo faz parte da chave primária.
   * @param align int Alinhamento do campo.
   * @param enabledOnEdit boolean Ativo em tempo de edição.
   * @param enabledOnInsert boolean Ativo em tempo de inclusão.
   * @param format int Formato de edição do campo.
   * @param mask String Máscara de edição do campo, ex.: 000.000.000-00.
   * @param required boolean O preenchimento do campo é obrigatório.
   * @param defaultValue String Valor padrão do campo.
   * @param scriptConstraint String Script de validação do campo.
   * @param scriptConstraintErrorMessage String Mensagem de validação do campo.
   */
  public EntityField(String  fieldName,
                     String  caption,
                     String  description,
                     String  fieldAlias,
                     int     fieldType,
                     int     fieldSize,
                     int     fieldPrecision,
                     boolean isKey,
                     int     align,
                     boolean enabledOnEdit,
                     boolean enabledOnInsert,
                     int     format,
                     String  mask,
                     boolean required,
                     String  defaultValue,
                     String  scriptConstraint,
                     String  scriptConstraintErrorMessage) {
    // nossos valores
    this.fieldName = fieldName;
    this.caption = caption;
    this.description = description;
    this.fieldAlias = fieldAlias;
    this.fieldPrecision = fieldPrecision;
    this.isKey = isKey;
    this.fieldSize = fieldSize;
    this.fieldType = fieldType;
    this.align = align;
    this.enabledOnEdit = enabledOnEdit;
    this.enabledOnInsert = enabledOnInsert;
    this.format = format;
    this.mask = mask;
    this.required = required;
    this.defaultValue = defaultValue;
    this.scriptConstraint = scriptConstraint;
    this.scriptConstraintErrorMessage = scriptConstraintErrorMessage;
  }

  public int getAlign() {
    return align;
  }

  public String getCaption() {
    return caption;
  }

  public String getDefaultValue() {
    return defaultValue;
  }

  public String getDescription() {
    return description;
  }

  public boolean getEnabledOnEdit() {
    return enabledOnEdit;
  }

  public boolean getEnabledOnInsert() {
    return enabledOnInsert;
  }

  public String getFieldAlias() {
    return fieldAlias;
  }

  public String getFieldName() {
    return fieldName;
  }

  public String getFieldName(String tableName) {
    if (!tableName.equals(""))
      return tableName + "." + fieldName;
    else
      return fieldName;
  }

  public int getFieldPrecision() {
    return fieldPrecision;
  }

  public int getFieldSize() {
    return fieldSize;
  }

  public int getFieldType() {
    return fieldType;
  }

  /**
   * Retorna o valor do campo obtido a partir de 'entityInfo'.
   * @param entityInfo EntityInfo a partir do qual será obtido o valor do campo.
   * @throws Exception Em caso de exceção na tentativa de obter o valor do
   *                   campo.
   * @return Object Retorna o valor do campo obtido a partir de 'entityInfo'.
   */
  public Object getFieldValue(EntityInfo entityInfo) throws Exception {
    // nosso resultado
    Object result = entityInfo.getPropertyValue(fieldAlias);
    // leva em consideração o formato UPPER_CASE
    return (format == FORMAT_UPPER_CASE ? result.toString().toUpperCase() : result);
  }

  public int getFormat() {
    return format;
  }

  /**
   * Retorna o valor do campo obtido a partir de 'entityInfo' formatado para
   * exibição.
   * @param entityInfo EntityInfo a partir do qual será obtido o valor do campo.
   * @throws Exception Em caso de exceção na tentativa de obter o valor do
   *                   campo.
   * @return String Retorna o valor do campo obtido a partir de 'entityInfo'
   *         formatado para exibição.
   */
  public String getFormatedFieldValue(EntityInfo entityInfo) throws Exception {
    return Entity.formatFieldValue(this, getFieldValue(entityInfo));
  }

  public boolean getIsKey() {
    return isKey;
  }

  public boolean getRequired() {
    return required;
  }

  public boolean getReadOnly() {
    return readOnly;
  }

  /**
   * Retorna a lista de valores de pesquisa do campo.
   * @return String[] Retorna a lista de valores de pesquisa do campo.
   */
  public String[] getLookupList() {
    return lookupList;
  }

  public String getMask() {
    return mask;
  }

  public String getScriptConstraint() {
    return scriptConstraint;
  }

  public String getScriptConstraintErrorMessage() {
    return scriptConstraintErrorMessage;
  }

  /**
   * Retorna true se o tipo do campo é um tipo lógico.
   * @return boolean Retorna true se o tipo do campo é um tipo lógico.
   */
  public boolean isBoolean() {
    return (fieldType == Types.BIT) ||
           (fieldType == Types.BOOLEAN);
  }

  /**
   * Retorna true se o tipo do campo é um tipo data.
   * @return boolean Retorna true se o tipo do campo é um tipo data.
   */
  public boolean isDate() {
    return (fieldType == Types.DATE);
  }

  /**
   * Retorna true se o tipo do campo é um tipo double.
   * @return boolean Retorna true se o tipo do campo é um tipo double.
   */
  public boolean isDouble() {
    return (fieldType == Types.DECIMAL) ||
           (fieldType == Types.DOUBLE) ||
           (fieldType == Types.FLOAT) ||
           (fieldType == Types.NUMERIC) ||
           (fieldType == Types.REAL);
  }

  /**
   * Retorna true se o tipo do campo é um tipo inteiro.
   * @return boolean Retorna true se o tipo do campo é um tipo inteiro.
   */
  public boolean isInteger() {
    return (fieldType == Types.BIGINT) ||
           (fieldType == Types.INTEGER) ||
           (fieldType == Types.SMALLINT) ||
           (fieldType == Types.TINYINT);
  }

  /**
   * Retorna true se o tipo do campo é um tipo String.
   * @return boolean Retorna true se o tipo do campo é um tipo String.
   */
  public boolean isString() {
    return (fieldType == Types.BLOB) ||
           (fieldType == Types.CHAR) ||
           (fieldType == Types.CLOB) ||
           (fieldType == Types.LONGVARCHAR) ||
           (fieldType == Types.VARCHAR);
  }

  /**
   * Retorna true se o tipo do campo é um tipo hora.
   * @return boolean Retorna true se o tipo do campo é um tipo hora.
   */
  public boolean isTime() {
    return (fieldType == Types.TIME);
  }

  /**
   * Retorna true se o tipo do campo é um tipo timestamp.
   * @return boolean Retorna true se o tipo do campo é um tipo timestamp.
   */
  public boolean isTimestamp() {
    return (fieldType == Types.TIMESTAMP);
  }

  /**
   * Retorna true se o tipo do campo é um tipo desconhecido.
   * @return boolean Retorna true se o tipo do campo é um tipo desconhecido.
   */
  public boolean isUnkown() {
    return !isBoolean() &&
           !isDate() &&
           !isDouble() &&
           !isInteger() &&
           !isString() &&
           !isTime() &&
           !isTimestamp();
  }

  public void setAlign(int align) {
    this.align = align;
  }

  public void setCaption(String caption) {
    this.caption = caption;
  }

  public void setDefaultValue(String defaultValue) {
    this.defaultValue = defaultValue;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public void setEnabledOnEdit(boolean enabledOnEdit) {
    this.enabledOnEdit = enabledOnEdit;
  }

  public void setEnabledOnInsert(boolean enabledOnInsert) {
    this.enabledOnInsert = enabledOnInsert;
  }

  public void setFieldAlias(String fieldAlias) {
    this.fieldAlias = fieldAlias;
  }

  public void setFieldName(String fieldName) {
    this.fieldName = fieldName;
  }

  public void setFieldPrecision(int fieldPrecision) {
    this.fieldPrecision = fieldPrecision;
  }

  public void setFieldSize(int fieldSize) {
    this.fieldSize = fieldSize;
  }

  public void setFieldType(int fieldType) {
    this.fieldType = fieldType;
  }

  /**
   * Define o valor do campo em 'entityInfo'.
   * @param entityInfo EntityInfo no qual será definido o valor do campo.
   * @param fieldValue Object Valor para ser definido ao campo.
   * @throws Exception Em caso de exceção na tentativa de definir o valor.
   */
  public void setFieldValue(EntityInfo entityInfo,
                            Object     fieldValue) throws Exception {
    // leva em consideração o formato UPPER_CASE
    entityInfo.setPropertyValue(fieldAlias,
                                (format == FORMAT_UPPER_CASE ? fieldValue.toString().toUpperCase() : fieldValue));
  }

  public void setFormat(int format) {
    this.format = format;
  }

  /**
   * Define o valor do campo em 'entityInfo'.
   * @param entityInfo EntityInfo no qual será definido o valor do campo.
   * @param formatedFieldValue String Valor formatado para ser definido ao campo.
   * @throws Exception Em caso de exceção na tentativa de definir o valor.
   */
  public void setFormatedFieldValue(EntityInfo entityInfo,
                                    String     formatedFieldValue) throws Exception {
    // leva em consideração o formato UPPER_CASE
    entityInfo.setPropertyValue(fieldAlias,
                                Entity.parseFormatedFieldValue(this, formatedFieldValue));
  }

  public void setIsKey(boolean isKey) {
    this.isKey = isKey;
  }

  public void setRequired(boolean required) {
    this.required = required;
  }

  public void setReadOnly(boolean readOnly) {
    this.readOnly = readOnly;
  }

  /**
   * Define a lista de valores de pesquisa do campo.
   * @param lookupList String[] lista de valores de pesquisa do campo.
   */
  public void setLookupList(String[] lookupList) {
    this.lookupList = lookupList;
  }

  public void setMask(String mask) {
    this.mask = mask;
  }

  public void setScriptConstraint(String scriptConstraint) {
    this.scriptConstraint = scriptConstraint;
  }

  public void setScriptConstraintErrorMessage(String scriptConstraintErrorMessage) {
    this.scriptConstraintErrorMessage = scriptConstraintErrorMessage;
  }

}
