package iobjects.ui.report;

import iobjects.*;
import iobjects.entity.*;
import iobjects.util.*;

/**
 * Representa um r�tulo de um campo, sendo utilizado para identifica��o do
 * seu objeto de exibi��o.
 * @since 2006
 */
public class ReportFieldLabel {

  private ReportFieldLabel() {
  }

  /**
   * Retorna o script HTML contendo a representa��o do ReportFieldLabel.
   * @param facade Facade Fachada.
   * @param entityField EntityField referente ao campo que se deseja representar.
   * @return String Retorna o script HTML contendo a representa��o do ReportFieldLabel.
   */
  static public String script(Facade     facade,
                              EntityField entityField) {
    return script(facade, entityField, "", "");
  }

  /**
   * Retorna o script HTML contendo a representa��o do ReportFieldLabel.
   * @param facade Facade Fachada.
   * @param entityField EntityField referente ao campo que se deseja representar.
   * @param id String Identifica��o do ReportFieldLabel na p�gina.
   * @param style String Estilo para modifica��o do elemento HTML apresentado.
   * @return String Retorna o script HTML contendo a representa��o do ReportFieldLabel.
   */
  static public String script(Facade      facade,
                              EntityField entityField,
                              String      id,
                              String      style) {
    // titulo do r�tulo
    String caption = entityField.getCaption();
    // retorna
    return "<span id=\"" + id + "\" "
         +       "class=\"FieldLabel\" "
         +       "style=\"" + style + "\">"
         + caption
         + "</span> \r";
  }


}
