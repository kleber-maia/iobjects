package iobjects.ui.param;

import javax.servlet.http.*;

import iobjects.*;
import iobjects.servlet.*;
import iobjects.util.*;

/**
 * Representa um r�tulo de um Param, sendo utilizado para identifica��o do
 * seu objeto de edi��o.
 * @since 2006
 */
public class ParamLabel {

  private ParamLabel() {
  }

  /**
   * Retorna o script HTML contendo a representa��o do ParamLabel.
   * @param facade Facade Fachada.
   * @param param Param que se deseja representar.
   * @return String Retorna o script HTML contendo a representa��o do ParamLabel.
   */
  static public String script(Facade facade,
                              Param  param) {
    return script(facade, param, "", "", false);
  }

  /**
   * Retorna o script HTML contendo a representa��o do ParamLabel.
   * @param facade Facade Fachada.
   * @param param Param que se deseja representar.
   * @param id String Identifica��o do ParamLabel na p�gina.
   * @param style String Estilo para modifica��o do elemento HTML apresentado.
   * @param disabled boolean True para que o ParamLabel apare�a desabilitado.
   * @return String Retorna o script HTML contendo a representa��o do ParamLabel.
   */
  static public String script(Facade  facade,
                              Param   param,
                              String  id,
                              String  style,
                              boolean disabled) {
    // titulo do r�tulo
    String caption = param.getCaption();
    // retorna
    return "<span id=\"" + id + "\" "
         +       "style=\"" + style + "\""
         + (disabled ? "disabled=\"disabled\">" : ">")
         + caption
         + "</span> \r";
  }

}
