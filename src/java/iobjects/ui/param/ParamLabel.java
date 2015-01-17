package iobjects.ui.param;

import javax.servlet.http.*;

import iobjects.*;
import iobjects.servlet.*;
import iobjects.util.*;

/**
 * Representa um rótulo de um Param, sendo utilizado para identificação do
 * seu objeto de edição.
 * @since 2006
 */
public class ParamLabel {

  private ParamLabel() {
  }

  /**
   * Retorna o script HTML contendo a representação do ParamLabel.
   * @param facade Facade Fachada.
   * @param param Param que se deseja representar.
   * @return String Retorna o script HTML contendo a representação do ParamLabel.
   */
  static public String script(Facade facade,
                              Param  param) {
    return script(facade, param, "", "", false);
  }

  /**
   * Retorna o script HTML contendo a representação do ParamLabel.
   * @param facade Facade Fachada.
   * @param param Param que se deseja representar.
   * @param id String Identificação do ParamLabel na página.
   * @param style String Estilo para modificação do elemento HTML apresentado.
   * @param disabled boolean True para que o ParamLabel apareça desabilitado.
   * @return String Retorna o script HTML contendo a representação do ParamLabel.
   */
  static public String script(Facade  facade,
                              Param   param,
                              String  id,
                              String  style,
                              boolean disabled) {
    // titulo do rótulo
    String caption = param.getCaption();
    // retorna
    return "<span id=\"" + id + "\" "
         +       "style=\"" + style + "\""
         + (disabled ? "disabled=\"disabled\">" : ">")
         + caption
         + "</span> \r";
  }

}
