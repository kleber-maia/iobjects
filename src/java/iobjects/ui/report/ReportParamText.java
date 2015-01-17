package iobjects.ui.report;

import iobjects.*;
import iobjects.ui.*;
import iobjects.util.*;

/**
 * Representa um controle de exibição de texto em um relatório associado a um Param.
 * @since 2006
 */
public class ReportParamText {

  private ReportParamText() {
  }

  /**
   * Retorna o script HTML contendo a representação do ReportParamText.
   * @param facade Facade Fachada.
   * @param param Param que se deseja representar.
   * @return String Retorna o script HTML contendo a representação do ReportParamText.
   * @throws Exception Em caso de exceção na tentativa de formatar o valor do
   *                   Param informado.
   */
  static public String script(Facade facade,
                              Param param) throws Exception {
    return script(facade, param, "", "");
  }

  /**
   * Retorna o script HTML contendo a representação do ReportParamText.
   * @param facade Facade Fachada.
   * @param param Param que se deseja representar.
   * @param id String Identificação do ReportParamText na página.
   * @param style String Estilo para modificação do elemento HTML apresentado.
   * @return String Retorna o script HTML contendo a representação do ReportParamText.
   * @throws Exception Em caso de exceção na tentativa de formatar o valor do
   *                   Param informado.
   */
  static public String script(Facade facade,
                              Param  param,
                              String id,
                              String style) throws Exception {
    // valor do rótulo
    String value = param.getFormatedValue();
    // retorna
    return "<span id=\"" + id + "\" "
         +       "class=\"ParamText\" "
         +       "style=\"" + style + "\">"
         + value
         + "</span> \r";
  }

}
