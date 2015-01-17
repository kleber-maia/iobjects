package iobjects.ui.entity;

import javax.servlet.http.*;

import iobjects.*;
import iobjects.ui.*;
import iobjects.entity.*;
import iobjects.servlet.*;
import iobjects.util.*;

/**
 * Representa um r�tulo de um lookup, sendo utilizado para identifica��o do
 * seu objeto de edi��o.
 */
public class EntityLookupLabel {

  static public final String WAIT_CURSOR_SCRIPT = "document.body.style.cursor='wait';";

  private EntityLookupLabel() {
  }

  /**
   * Retorna o script HTML contendo a representa��o do EntityLookupLabel.
   * @param facade Facade Fachada.
   * @param entityLookup EntityLookup referente ao campo que se deseja
   *        representar.
   * @param request HttpServletRequest Requisi��o da p�gina atual.
   * @return String Retorna o script HTML contendo a representa��o do
   *         EntityLookupLabel.
   */
  static public String script(Facade             facade,
                              EntityLookup       entityLookup,
                              HttpServletRequest request) {
    return script(facade, entityLookup, request, "", "", false);
  }

  /**
   * Retorna o script HTML contendo a representa��o do EntityLookupLabel.
   * @param facade Facade Fachada.
   * @param entityLookup EntityLookup referente ao campo que se deseja
   *        representar.
   * @param request HttpServletRequest Requisi��o da p�gina atual.
   * @param id String Identifica��o do EntityLookupLabel na p�gina.
   * @param style String Estilo para modifica��o do elemento HTML apresentado.
   * @return String Retorna o script HTML contendo a representa��o do
   *         EntityLookupLabel.
   */
  static public String script(Facade             facade,
                              EntityLookup       entityLookup,
                              HttpServletRequest request,
                              String             id,
                              String             style) {
    return script(facade, entityLookup, request, id, style, null, null, "", "", false);
  }

  /**
   * Retorna o script HTML contendo a representa��o do EntityLookupLabel.
   * @param facade Facade Fachada.
   * @param entityLookup EntityLookup referente ao campo que se deseja
   *        representar.
   * @param request HttpServletRequest Requisi��o da p�gina atual.
   * @param id String Identifica��o do EntityLookupLabel na p�gina.
   * @param style String Estilo para modifica��o do elemento HTML apresentado.
   * @param disabled boolean True para que o EntityLookupLabel apare�a desabilitado.
   * @return String Retorna o script HTML contendo a representa��o do
   *         EntityLookupLabel.
   */
  static public String script(Facade             facade,
                              EntityLookup       entityLookup,
                              HttpServletRequest request,
                              String             id,
                              String             style,
                              boolean            disabled) {
    return script(facade, entityLookup, request, id, style, null, null, "", "", disabled);
  }

  /**
   * Retorna o script HTML contendo a representa��o do EntityLookupLabel em
   * forma de hyperlink para o 'action' informado.
   * @param facade Facade Fachada.
   * @param entityLookup EntityLookup referente ao campo que se deseja
   *        representar.
   * @param request HttpServletRequest Requisi��o da p�gina atual.
   * @param id String Identifica��o do EntityLookupLabel na p�gina.
   * @param style String Estilo para modifica��o do elemento HTML apresentado.
   * @param action Action A��o de destino.
   * @param disabled boolean True para que o EntityLookupLabel apare�a desabilitado.
   * @return String Retorna o script HTML contendo a representa��o do
   *         EntityLookupLabel.
   * @since 2006
   */
  static public String script(Facade             facade,
                              EntityLookup       entityLookup,
                              HttpServletRequest request,
                              String             id,
                              String             style,
                              Action             action,
                              boolean            disabled) {
    return script(facade, entityLookup, request, id, style, action, null, "", "", disabled);
  }

  /**
   * Retorna o script HTML contendo a representa��o do EntityLookupLabel em
   * forma de hyperlink para o 'action' informado.
   * @param facade Facade Fachada.
   * @param entityLookup EntityLookup referente ao campo que se deseja
   *        representar.
   * @param request HttpServletRequest Requisi��o da p�gina atual.
   * @param id String Identifica��o do EntityLookupLabel na p�gina.
   * @param style String Estilo para modifica��o do elemento HTML apresentado.
   * @param action Action A��o de destino.
   * @param command Command Comando para ser executado.
   * @param disabled boolean True para que o EntityLookupLabel apare�a desabilitado.
   * @return String Retorna o script HTML contendo a representa��o do
   *         EntityLookupLabel.
   * @since 2006
   */
  static public String script(Facade             facade,
                              EntityLookup       entityLookup,
                              HttpServletRequest request,
                              String             id,
                              String             style,
                              Action             action,
                              Command            command,
                              boolean            disabled) {
    return script(facade, entityLookup, request, id, style, action, command, "", "", disabled);
  }

  /**
   * Retorna o script HTML contendo a representa��o do EntityLookupLabel em
   * forma de hyperlink para o 'action' informado.
   * @param entityLookup EntityLookup referente ao campo que se deseja
   *        representar.
   * @param facade Facade Fachada.
   * @param request HttpServletRequest Requisi��o da p�gina atual.
   * @param id String Identifica��o do EntityLookupLabel na p�gina.
   * @param style String Estilo para modifica��o do elemento HTML apresentado.
   * @param action Action A��o de destino.
   * @param params String Par�metros do tipo 'param1=value1&param2=value2' para
   *               serem enviados ao Action de destino.
   * @param disabled boolean True para que o EntityLookupLabel apare�a desabilitado.
   * @return String Retorna o script HTML contendo a representa��o do
   *         EntityLookupLabel.
   * @since 2006
   */
  static public String script(Facade             facade,
                              EntityLookup       entityLookup,
                              HttpServletRequest request,
                              String             id,
                              String             style,
                              Action             action,
                              String             params,
                              boolean            disabled) {
    return script(facade, entityLookup, request, id, style, action, null, params, "", disabled);
  }

  /**
   * Retorna o script HTML contendo a representa��o do EntityLookupLabel em
   * forma de hyperlink para o 'action' informado.
   * @param facade Facade Fachada.
   * @param entityLookup EntityLookup referente ao campo que se deseja
   *        representar.
   * @param request HttpServletRequest Requisi��o da p�gina atual.
   * @param id String Identifica��o do EntityLookupLabel na p�gina.
   * @param style String Estilo para modifica��o do elemento HTML apresentado.
   * @param action Action A��o de destino.
   * @param command Command Comando para ser executado.
   * @param params String Par�metros do tipo 'param1=value1&param2=value2' para
   *               serem enviados ao Action de destino.
   * @param target String Frame de destino do 'action'.
   * @param disabled boolean True para que o EntityLookupLabel apare�a desabilitado.
   * @return String Retorna o script HTML contendo a representa��o do
   *         EntityLookupLabel.
   * @since 2006
   */
  static public String script(Facade             facade,
                              EntityLookup       entityLookup,
                              HttpServletRequest request,
                              String             id,
                              String             style,
                              Action             action,
                              Command            command,
                              String             params,
                              String             target,
                              boolean            disabled) {
    // titulo do r�tulo
    String caption = entityLookup.getCaption();
    // se est� editando e o campo n�o � edit�vel...
    if (Controller.isEditing(request) && !entityLookup.getEnabledOnEdit())
      disabled = true && !disabled;
    // se est� inserindo e o campo n�o � edit�vel...
    if (Controller.isInserting(request) && !entityLookup.getEnabledOnInsert())
      disabled = true && !disabled;
    // verifica se seus campos chave s�o requeridos
    boolean required = false;
    for (int i=0; i<entityLookup.getKeyFields().length; i++) {
      required = entityLookup.getKeyFields()[i].getRequired();
      if (required)
        break;
    } // for
    // se � chave � requerida e n�o est� desabilitado...p�e o sinal de obrigat�rio
    if (required && !disabled)
      caption = caption + "*";
    // se temos a fachada e o Action...transforma em hyperlink
    if ((facade != null) && (action != null)) {
      return Hyperlink.script(facade,
                              id,
                              caption,
                              "Ir para " + action.getCaption(),
                              "",
                              command != null ? action.url(command, params) : action.url(params),
                              target,
                              action.getShowType() == Action.SHOW_TYPE_EMBEDED ? WAIT_CURSOR_SCRIPT : "",
                              disabled || !facade.getLoggedUser().hasAccessRight(action, command));
    }
    // se n�o temos a fachada e o Action...retorna apenas o label
    else {
      // retorna
      return "<span id=\"" + id + "\" "
           + "style=\"" + style + "\" "
           + (disabled ? "disabled=\"disabled\">" : ">")
           + caption
           + "</span> \r";
    } // if
  }

}
