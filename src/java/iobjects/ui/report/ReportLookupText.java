package iobjects.ui.report;

import java.sql.*;

import iobjects.entity.*;

/**
 * Representa um controle de exibi��o de texto em um relat�rio associado a um
 * lookup.
 * @since 2006
 */
public class ReportLookupText {

  private ReportLookupText() {
  }

  /**
   * Retorna o script HTML contendo a representa��o do ReportLookupText.
   * @param entityLookup EntityLookup referente ao lookup que se deseja representar.
   * @param entityInfo EntityInfo que mant�m as informa��es de 'entityLookup'.
   * @return String Retorna o script HTML contendo a representa��o do ReportLookupText.
   * @throws Exception Em caso de exce��o na tentativa de obten��o ou na
   *                   formata��o do valor do campo.
   */
  static public String script(EntityLookup entityLookup,
                              EntityInfo   entityInfo) throws Exception {
    return script(entityLookup, entityInfo, "", "");
  }

  /**
   * Retorna o script HTML contendo a representa��o do ReportLookupText.
   * @param entityLookup EntityLookup referente ao lookup que se deseja representar.
   * @param entityInfo EntityInfo que mant�m as informa��es de 'entityLookup'.
   * @param id String Identifica��o do ReportLookupText na p�gina.
   * @param style String Estilo para modifica��o do elemento HTML apresentado.
   * @return String Retorna o script HTML contendo a representa��o do ReportLookupText.
   * @throws Exception Em caso de exce��o na tentativa de obten��o ou na
   *                   formata��o do valor do campo.
   */
  static public String script(EntityLookup entityLookup,
                              EntityInfo   entityInfo,
                              String       id,
                              String       style) throws Exception {
    // valor do campo formatado
    String fieldValue = entityInfo.lookupValueList().get(entityLookup).getDisplayFieldValuesToString();
    // retorna
    return script(fieldValue, id, style);
  }

  /**
   * Retorna o script HTML contendo a representa��o do ReportLookupText.
   * @param fieldValue Valor do ReportLookupText na p�gina.
   * @param id String Identifica��o do ReportLookupText na p�gina.
   * @param style String Estilo para modifica��o do elemento HTML apresentado.
   * @return String Retorna o script HTML contendo a representa��o do ReportLookupText.
   */
  static private String script(String fieldValue,
                               String id,
                               String style) {
    // retorna
    return "<span id=\"" + id + "\" "
         +       "class=\"FieldText\" "
         +       "style=\"" + style + "\">"
         + fieldValue
         + "</span> \r";
  }

}
