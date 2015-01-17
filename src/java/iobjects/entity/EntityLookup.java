package iobjects.entity;

/**
 * Representa uma referência de pesquisa de uma entidade em uma entidade
 * estrangeira. Essa pesquisa visa obter registros da entidade estrangeira
 * associados a campos que são chaves estrangeiras na entidade local.
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
   * Construtor padrão.
   */
  public EntityLookup() {
  }

  /**
   * Construtor estendido.
   * @param name String Nome do lookup para referência.
   * @param caption String Título do lookup.
   * @param description String Descrição do lookup.
   * @param lookupEntityClassName String Nome da classe da entidade de pesquisa.
   * @param keyField EntityField Campo chave na entidade local.
   * @param lookupDisplayField EntityField Campo na entidade de pesquisa
   *                           para exibição.
   * @param lookupOrderField EntityField Campo na entidade de pesquisa
   *                         para ordenação.
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
   * @param name String Nome do lookup para referência.
   * @param caption String Título do lookup.
   * @param description String Descrição do lookup.
   * @param lookupEntityClassName String Nome da classe da entidade de pesquisa.
   * @param keyFields EntityField[] Campos chave na entidade local.
   * @param lookupDisplayFields EntityField[] Campos na entidade de pesquisa
   *                            para exibição.
   * @param lookupOrderFields EntityField[] Campos na entidade de pesquisa
   *                          para ordenação.
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
   * @param name String Nome do lookup para referência.
   * @param caption String Título do lookup.
   * @param description String Descrição do lookup.
   * @param lookupEntityClassName String Nome da classe da entidade de pesquisa.
   * @param keyFields EntityField[] Campos chave na entidade local.
   * @param lookupDisplayFields EntityField[] Campos na entidade de pesquisa
   *                            para exibição.
   * @param lookupOrderFields EntityField[] Campos na entidade de pesquisa
   *                          para ordenação.
   * @param lookupFilterExpression String Expressão de filtro na entidade
   *                               de pesquisa.
   * @param enabledOnEdit boolean True se é ativo em tempo de edição.
   * @param enabledOnInsert boolean True se é ativo em tempo de inclusão.
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
   * @param entityInfo EntityInfo no qual serão definidos os valores dos campos chave.
   * @param keyFieldValues String Valores separados por ';' para serem definidos
   *        aos campos chave em 'entityInfo'.
   * @throws Exception Em caso de exceção na tentativa de definir os valores.
   */
  public void setKeyFieldValues(EntityInfo entityInfo,
                                String     keyFieldValues) throws Exception {
    // separa os valores recebidos
    String[] fieldValues = keyFieldValues.split(";");
    // se a quantidade de valores não é igual a quantidade de campos
    // chave definidas no lookup...exceção
    if (keyFields.length != fieldValues.length)
      throw new EntityException(getClass().getName(), "setLookupFieldValue", "A quantidade de campos chave (" + keyFields.length + ") não combina com a quantidade de valores recebidos (" + fieldValues.length + ").");
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
