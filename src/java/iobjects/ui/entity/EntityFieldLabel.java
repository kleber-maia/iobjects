/*
The MIT License (MIT)

Copyright (c) 2008 Kleber Maia de Andrade

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/   
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
