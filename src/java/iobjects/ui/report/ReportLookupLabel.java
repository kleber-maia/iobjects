package iobjects.ui.report;

import iobjects.*;
import iobjects.entity.*;
import iobjects.util.*;

/**
 * Representa um r�tulo de um lookup, sendo utilizado para identifica��o do
 * seu objeto de exibi��o.
 * @since 2006
 */
public class ReportLookupLabel {

  private ReportLookupLabel() {
  }

  /**
   * Retorna o script HTML contendo a representa��o do ReportLookupLabel.
   * @param facade Facade Fachada.
   * @param entityLookup EntityLookup referente ao campo que se deseja representar.
   * @return String Retorna o script HTML contendo a representa��o do ReportLookupLabel.
   */
  static public String script(Facade       facade,
                              EntityLookup entityLookup) {
    return script(facade, entityLookup, "", "");
  }

  /**
   * Retorna o script HTML contendo a representa��o do ReportLookupLabel.
   * @param facade Facade Fachada.
   * @param entityLookup EntityLookup referente ao campo que se deseja representar.
   * @param id String Identifica��o do ReportLookupLabel na p�gina.
   * @param style String Estilo para modifica��o do elemento HTML apresentado.
   * @return String Retorna o script HTML contendo a representa��o do ReportLookupLabel.
   */
  static public String script(Facade       facade,
                              EntityLookup entityLookup,
                              String       id,
                              String       style) {
    // titulo do r�tulo
    String caption = entityLookup.getCaption();
    // retorna
    return "<span id=\"" + id + "\" "
         +       "class=\"FieldLabel\" "
         +       "style=\"" + style + "\">"
         + caption
         + "</span> \r";
  }

}
