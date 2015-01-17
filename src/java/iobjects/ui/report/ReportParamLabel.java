package iobjects.ui.report;

import iobjects.*;
import iobjects.util.*;

/**
 * Representa um rótulo de um Param, sendo utilizado para identificação do
 * seu objeto de exibição.
 * @since 2006
 */
public class ReportParamLabel {

  private ReportParamLabel() {
  }

  /**
   * Retorna o script HTML contendo a representação do ReportParamLabel.
   * @param facade Facade Fachada.
   * @param param Param que se deseja representar.
   * @return String Retorna o script HTML contendo a representação do ReportParamLabel.
   */
  static public String script(Facade facade,
                              Param  param) {
    return script(facade, param, "", "");
  }

  /**
   * Retorna o script HTML contendo a representação do ReportParamLabel.
   * @param facade Facade Fachada.
   * @param param Param que se deseja representar.
   * @param id String Identificação do ReportParamLabel na página.
   * @param style String Estilo para modificação do elemento HTML apresentado.
   * @return String Retorna o script HTML contendo a representação do ReportParamLabel.
   */
  static public String script(Facade facade,
                              Param  param,
                              String id,
                              String style) {
    // titulo do rótulo
    String caption = param.getCaption();
    // retorna
    return "<span id=\"" + id + "\" "
         +       "class=\"ParamLabel\" "
         +       "style=\"" + style + "\">"
         + caption
         + "</span> \r";
  }

}
