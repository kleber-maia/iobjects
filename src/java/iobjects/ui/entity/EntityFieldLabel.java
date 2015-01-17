package iobjects.ui.entity;

import javax.servlet.http.*;

import iobjects.*;
import iobjects.entity.*;
import iobjects.servlet.*;
import iobjects.util.*;

/**
 * Representa um r�tulo de um campo, sendo utilizado para identifica��o do
 * seu objeto de edi��o.
 */
public class EntityFieldLabel {

  private EntityFieldLabel() {
  }

  /**
   * Retorna o script HTML contendo a representa��o do EntityFieldLabel.
   * @param facade Facade Fachada.
   * @param entityField EntityField referente ao campo que se deseja representar.
   * @param request HttpServletRequest Requisi��o da p�gina atual.
   * @return String Retorna o script HTML contendo a representa��o do EntityFieldLabel.
   */
  static public String script(Facade             facade,
                              EntityField        entityField,
                              HttpServletRequest request) {
    return script(facade, entityField, request, "", "", false);
  }

  /**
   * Retorna o script HTML contendo a representa��o do EntityFieldLabel.
   * @param facade Facade Fachada.
   * @param entityField EntityField referente ao campo que se deseja representar.
   * @param request HttpServletRequest Requisi��o da p�gina atual.
   * @param id String Identifica��o do EntityFieldLabel na p�gina.
   * @param style String Estilo para modifica��o do elemento HTML apresentado.
   * @return String Retorna o script HTML contendo a representa��o do EntityFieldLabel.
   */
  static public String script(Facade             facade,
                              EntityField        entityField,
                              HttpServletRequest request,
                              String             id,
                              String             style) {
    return script(facade, entityField, request, id, style, false);
  }

  /**
   * Retorna o script HTML contendo a representa��o do EntityFieldLabel.
   * @param facade Facade Fachada.
   * @param entityField EntityField referente ao campo que se deseja representar.
   * @param request HttpServletRequest Requisi��o da p�gina atual.
   * @param id String Identifica��o do EntityFieldLabel na p�gina.
   * @param style String Estilo para modifica��o do elemento HTML apresentado.
   * @param disabled boolean True para que o EntityFieldLabel apare�a desabilitado.
   * @return String Retorna o script HTML contendo a representa��o do EntityFieldLabel.
   */
  static public String script(Facade             facade,
                              EntityField        entityField,
                              HttpServletRequest request,
                              String             id,
                              String             style,
                              boolean            disabled) {
    // titulo do r�tulo
    String caption = entityField.getCaption();
    // se est� editando e o campo n�o � edit�vel...
    if (Controller.isEditing(request) && !entityField.getEnabledOnEdit())
      disabled = true;
    // se est� inserindo e o campo n�o � edit�vel...
    if (Controller.isInserting(request) && !entityField.getEnabledOnInsert())
      disabled = true;
    // se � chave ou requerido e n�o est� desabilitado...p�e o sinal de obrigat�rio
    if ((entityField.getIsKey() || entityField.getRequired()) && !disabled)
      caption = caption + "*";
    // retorna
    return "<span id=\"" + id + "\" "
         +       "style=\"" + style + "\" "
         + (disabled ? "disabled=\"disabled\">" : ">")
         + caption
         + "</span> \r";
  }

}
