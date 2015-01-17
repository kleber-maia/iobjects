package iobjects.entity;

import iobjects.util.*;

/**
 * Representa um valor de um EntityLookup obtido a partir de uma consulta ao
 * banco de dados.
 * @since 3.1
 */
public class EntityLookupValue {

  private EntityLookup entityLookup       = null;
  private String[]     displayFieldValues = {};

  /**
   * Construtor padrão.
   * @param entityLookup EntityLookup que representa o valor.
   * @param displayFieldValues String[] Valores que representam o EntityLookup.
   */
  public EntityLookupValue(EntityLookup entityLookup,
                           String[]     displayFieldValues) {
    this.entityLookup = entityLookup;
    this.displayFieldValues = displayFieldValues;
  }

  /**
   * Retorna o EntityLookup que representa o valor.
   * @return EntityLookup Retorna o EntityLookup que representa o valor.
   */
  public EntityLookup getLookup() {
    return entityLookup;
  }

  /**
   * Retorna os valores que representam o EntityLookup.
   * @return String[] Retorna os valores que representam o EntityLookup.
   */
  public String[] getDisplayFieldValues() {
    return displayFieldValues;
  }

  /**
   * Retorna os valores que representam o EntityLookup separados por ' - '.
   * @return String Retorna os valores que representam o EntityLookup separados
   * por ' - '.
   */
  public String getDisplayFieldValuesToString() {
    // nosso resultado
    String result = StringTools.arrayStringToString(displayFieldValues, " - ");
    // exclui os separadores que não possuírem valor entre si
    result = result.replaceAll(" -  - ", "");
    // retorna
    return result;
  }

}
