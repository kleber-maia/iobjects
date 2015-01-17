package iobjects.ui.report;

import iobjects.*;
import iobjects.util.*;

/**
 * Representa um r�tulo de um Param, sendo utilizado para identifica��o do
 * seu objeto de exibi��o.
 * @since 2006
 */
public class ReportParamLabel {

  private ReportParamLabel() {
  }

  /**
   * Retorna o script HTML contendo a representa��o do ReportParamLabel.
   * @param facade Facade Fachada.
   * @param param Param que se deseja representar.
   * @return String Retorna o script HTML contendo a representa��o do ReportParamLabel.
   */
  static public String script(Facade facade,
                              Param  param) {
    return script(facade, param, "", "");
  }

  /**
   * Retorna o script HTML contendo a representa��o do ReportParamLabel.
   * @param facade Facade Fachada.
   * @param param Param que se deseja representar.
   * @param id String Identifica��o do ReportParamLabel na p�gina.
   * @param style String Estilo para modifica��o do elemento HTML apresentado.
   * @return String Retorna o script HTML contendo a representa��o do ReportParamLabel.
   */
  static public String script(Facade facade,
                              Param  param,
                              String id,
                              String style) {
    // titulo do r�tulo
    String caption = param.getCaption();
    // retorna
    return "<span id=\"" + id + "\" "
         +       "class=\"ParamLabel\" "
         +       "style=\"" + style + "\">"
         + caption
         + "</span> \r";
  }

}
