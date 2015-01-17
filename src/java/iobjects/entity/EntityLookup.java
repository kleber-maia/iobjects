package iobjects.entity;

/**
 * Representa uma refer�ncia de pesquisa de uma entidade em uma entidade
 * estrangeira. Essa pesquisa visa obter registros da entidade estrangeira
 * associados a campos que s�o chaves estrangeiras na entidade local.
 * @since 3.1
 */
public class EntityLookup {

  private String           caption                  = "";
  private String           description              = "";
  private boolean          enabledOnEdit            = true;
  private boolean          enabledOnInsert          = true;
  private EntityField[]    keyFields                = {};
  private EntityField[]    lookupDisplayFields      = {};
  private String           lookupEntityClassName    = "";
  private String           lookupFilterExpression   = "";
  private EntityField[]    lookupOrderFields        = {};
  private String           name                     = "";

  /**
   * Construtor padr�o.
   */
  public EntityLookup() {
  }

  /**
   * Construtor estendido.
   * @param name String Nome do lookup para refer�ncia.
   * @param caption String T�tulo do lookup.
   * @param description String Descri��o do lookup.
   * @param lookupEntityClassName String Nome da classe da entidade de pesquisa.
   * @param keyField EntityField Campo chave na entidade local.
   * @param lookupDisplayField EntityField Campo na entidade de pesquisa
   *                           para exibi��o.
   * @param lookupOrderField EntityField Campo na entidade de pesquisa
   *                         para ordena��o.
   */
  public EntityLookup(String      name,
                      String      caption,
                      String      description,
                      String      lookupEntityClassName,
                      EntityField keyField,
                      EntityField lookupDisplayField,
                      EntityField lookupOrderField) {
    this.name = name;
    this.caption = caption;
    this.description = description;
    this.lookupEntityClassName = lookupEntityClassName;
    this.keyFields = new EntityField[1];
    this.keyFields[0] = keyField;
    this.lookupDisplayFields = new EntityField[1];
    this.lookupDisplayFields[0] = lookupDisplayField;
    this.lookupOrderFields = new EntityField[1];
    this.lookupOrderFields[0] = lookupOrderField;
  }

  /**
   * Construtor estendido.
   * @param name String Nome do lookup para refer�ncia.
   * @param caption String T�tulo do lookup.
   * @param description String Descri��o do lookup.
   * @param lookupEntityClassName String Nome da classe da entidade de pesquisa.
   * @param keyFields EntityField[] Campos chave na entidade local.
   * @param lookupDisplayFields EntityField[] Campos na entidade de pesquisa
   *                            para exibi��o.
   * @param lookupOrderFields EntityField[] Campos na entidade de pesquisa
   *                          para ordena��o.
   */
  public EntityLookup(String        name,
                      String        caption,
                      String        description,
                      String        lookupEntityClassName,
                      EntityField[] keyFields,
                      EntityField[] lookupDisplayFields,
                      EntityField[] lookupOrderFields) {
    this.name = name;
    this.caption = caption;
    this.description = description;
    this.lookupEntityClassName = lookupEntityClassName;
    this.keyFields = keyFields;
    this.lookupDisplayFields = lookupDisplayFields;
    this.lookupOrderFields = lookupOrderFields;
  }

  /**
   * Construtor estendido.
   * @param name String Nome do lookup para refer�ncia.
   * @param caption String T�tulo do lookup.
   * @param description String Descri��o do lookup.
   * @param lookupEntityClassName String Nome da classe da entidade de pesquisa.
   * @param keyFields EntityField[] Campos chave na entidade local.
   * @param lookupDisplayFields EntityField[] Campos na entidade de pesquisa
   *                            para exibi��o.
   * @param lookupOrderFields EntityField[] Campos na entidade de pesquisa
   *                          para ordena��o.
   * @param lookupFilterExpression String Express�o de filtro na entidade
   *                               de pesquisa.
   * @param enabledOnEdit boolean True se � ativo em tempo de edi��o.
   * @param enabledOnInsert boolean True se � ativo em tempo de inclus�o.
   */
  public EntityLookup(String        name,
                      String        caption,
                      String        description,
                      String        lookupEntityClassName,
                      EntityField[] keyFields,
                      EntityField[] lookupDisplayFields,
                      EntityField[] lookupOrderFields,
                      String        lookupFilterExpression,
                      boolean       enabledOnEdit,
                      boolean       enabledOnInsert) {
    this.name = name;
    this.caption = caption;
    this.lookupEntityClassName = lookupEntityClassName;
    this.keyFields = keyFields;
    this.lookupDisplayFields = lookupDisplayFields;
    this.lookupOrderFields = lookupOrderFields;
    this.lookupFilterExpression = lookupFilterExpression;
    this.description = description;
    this.enabledOnEdit = enabledOnEdit;
    this.enabledOnInsert = enabledOnInsert;
  }

  public String getCaption() {
    return caption;
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

  public EntityField[] getKeyFields() {
    return keyFields;
  }

  public EntityField[] getLookupDisplayFields() {
    return lookupDisplayFields;
  }

  public String getLookupEntityClassName() {
    return lookupEntityClassName;
  }

  public String getLookupFilterExpression() {
    return lookupFilterExpression;
  }

  public EntityField[] getLookupOrderFields() {
    return lookupOrderFields;
  }

  public String getName() {
    return name;
  }

  public void setCaption(String caption) {
    this.caption = caption;
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

  /**
   * Define o valor dos campos representados pelos nossos campos chave (KeyFields)
   * em 'entityInfo'.
   * @param entityInfo EntityInfo no qual ser�o definidos os valores dos campos chave.
   * @param keyFieldValues String Valores separados por ';' para serem definidos
   *        aos campos chave em 'entityInfo'.
   * @throws Exception Em caso de exce��o na tentativa de definir os valores.
   */
  public void setKeyFieldValues(EntityInfo entityInfo,
                                String     keyFieldValues) throws Exception {
    // separa os valores recebidos
    String[] fieldValues = keyFieldValues.split(";");
    // se a quantidade de valores n�o � igual a quantidade de campos
    // chave definidas no lookup...exce��o
    if (keyFields.length != fieldValues.length)
      throw new EntityException(getClass().getName(), "setLookupFieldValue", "A quantidade de campos chave (" + keyFields.length + ") n�o combina com a quantidade de valores recebidos (" + fieldValues.length + ").");
    // loop nos campos chave
    for (int i=0; i<keyFields.length; i++) {
      // campo da vez
      EntityField keyField = keyFields[i];
      // define seu valor
      keyField.setFormatedFieldValue(entityInfo, fieldValues[i]);
    } // for
  }

  public void setKeyFields(EntityField[] keyFields) {
    this.keyFields = keyFields;
  }

  public void setLookupDisplayFields(EntityField[] lookupDisplayFields) {
    this.lookupDisplayFields = lookupDisplayFields;
  }

  public void setLookupEntityClassName(String lookupEntityClassName) {
    this.lookupEntityClassName = lookupEntityClassName;
  }

  public void setLookupFilterExpression(String lookupFilterExpression) {
    this.lookupFilterExpression = lookupFilterExpression;
  }

  public void setLookupOrderFields(EntityField[] lookupOrderFields) {
    this.lookupOrderFields = lookupOrderFields;
  }

  public void setName(String name) {
    this.name = name;
  }

}
