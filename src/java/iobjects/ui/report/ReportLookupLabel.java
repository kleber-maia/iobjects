package iobjects.ui.report;

import iobjects.*;
import iobjects.entity.*;
import iobjects.util.*;

/**
 * Representa um rótulo de um lookup, sendo utilizado para identificação do
 * seu objeto de exibição.
 * @since 2006
 */
public class ReportLookupLabel {

  private ReportLookupLabel() {
  }

  /**
   * Retorna o script HTML contendo a representação do ReportLookupLabel.
   * @param facade Facade Fachada.
   * @param entityLookup EntityLookup referente ao campo que se deseja representar.
   * @return String Retorna o script HTML contendo a representação do ReportLookupLabel.
   */
  static public String script(Facade       facade,
                              EntityLookup entityLookup) {
    return script(facade, entityLookup, "", "");
  }

  /**
   * Retorna o script HTML contendo a representação do ReportLookupLabel.
   * @param facade Facade Fachada.
   * @param entityLookup EntityLookup referente ao campo que se deseja representar.
   * @param id String Identificação do ReportLookupLabel na página.
   * @param style String Estilo para modificação do elemento HTML apresentado.
   * @return String Retorna o script HTML contendo a representação do ReportLookupLabel.
   */
  static public String script(Facade       facade,
                              EntityLookup entityLookup,
                              String       id,
                              String       style) {
    // titulo do rótulo
    String caption = entityLookup.getCaption();
    // retorna
    return "<span id=\"" + id + "\" "
         +       "class=\"FieldLabel\" "
         +       "style=\"" + style + "\">"
         + caption
         + "</span> \r";
  }

}
