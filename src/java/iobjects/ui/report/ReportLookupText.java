package iobjects.ui.report;

import java.sql.*;

import iobjects.entity.*;

/**
 * Representa um controle de exibição de texto em um relatório associado a um
 * lookup.
 * @since 2006
 */
public class ReportLookupText {

  private ReportLookupText() {
  }

  /**
   * Retorna o script HTML contendo a representação do ReportLookupText.
   * @param entityLookup EntityLookup referente ao lookup que se deseja representar.
   * @param entityInfo EntityInfo que mantém as informações de 'entityLookup'.
   * @return String Retorna o script HTML contendo a representação do ReportLookupText.
   * @throws Exception Em caso de exceção na tentativa de obtenção ou na
   *                   formatação do valor do campo.
   */
  static public String script(EntityLookup entityLookup,
                              EntityInfo   entityInfo) throws Exception {
    return script(entityLookup, entityInfo, "", "");
  }

  /**
   * Retorna o script HTML contendo a representação do ReportLookupText.
   * @param entityLookup EntityLookup referente ao lookup que se deseja representar.
   * @param entityInfo EntityInfo que mantém as informações de 'entityLookup'.
   * @param id String Identificação do ReportLookupText na página.
   * @param style String Estilo para modificação do elemento HTML apresentado.
   * @return String Retorna o script HTML contendo a representação do ReportLookupText.
   * @throws Exception Em caso de exceção na tentativa de obtenção ou na
   *                   formatação do valor do campo.
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
   * Retorna o script HTML contendo a representação do ReportLookupText.
   * @param fieldValue Valor do ReportLookupText na página.
   * @param id String Identificação do ReportLookupText na página.
   * @param style String Estilo para modificação do elemento HTML apresentado.
   * @return String Retorna o script HTML contendo a representação do ReportLookupText.
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
