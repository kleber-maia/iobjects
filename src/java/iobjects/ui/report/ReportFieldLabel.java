package iobjects.ui.report;

import iobjects.*;
import iobjects.entity.*;
import iobjects.util.*;

/**
 * Representa um rótulo de um campo, sendo utilizado para identificação do
 * seu objeto de exibição.
 * @since 2006
 */
public class ReportFieldLabel {

  private ReportFieldLabel() {
  }

  /**
   * Retorna o script HTML contendo a representação do ReportFieldLabel.
   * @param facade Facade Fachada.
   * @param entityField EntityField referente ao campo que se deseja representar.
   * @return String Retorna o script HTML contendo a representação do ReportFieldLabel.
   */
  static public String script(Facade     facade,
                              EntityField entityField) {
    return script(facade, entityField, "", "");
  }

  /**
   * Retorna o script HTML contendo a representação do ReportFieldLabel.
   * @param facade Facade Fachada.
   * @param entityField EntityField referente ao campo que se deseja representar.
   * @param id String Identificação do ReportFieldLabel na página.
   * @param style String Estilo para modificação do elemento HTML apresentado.
   * @return String Retorna o script HTML contendo a representação do ReportFieldLabel.
   */
  static public String script(Facade      facade,
                              EntityField entityField,
                              String      id,
                              String      style) {
    // titulo do rótulo
    String caption = entityField.getCaption();
    // retorna
    return "<span id=\"" + id + "\" "
         +       "class=\"FieldLabel\" "
         +       "style=\"" + style + "\">"
         + caption
         + "</span> \r";
  }


}
