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
 * Representa um rótulo de um campo, sendo utilizado para identificação do
 * seu objeto de edição.
 */
public class EntityFieldLabel {

  private EntityFieldLabel() {
  }

  /**
   * Retorna o script HTML contendo a representação do EntityFieldLabel.
   * @param facade Facade Fachada.
   * @param entityField EntityField referente ao campo que se deseja representar.
   * @param request HttpServletRequest Requisição da página atual.
   * @return String Retorna o script HTML contendo a representação do EntityFieldLabel.
   */
  static public String script(Facade             facade,
                              EntityField        entityField,
                              HttpServletRequest request) {
    return script(facade, entityField, request, "", "", false);
  }

  /**
   * Retorna o script HTML contendo a representação do EntityFieldLabel.
   * @param facade Facade Fachada.
   * @param entityField EntityField referente ao campo que se deseja representar.
   * @param request HttpServletRequest Requisição da página atual.
   * @param id String Identificação do EntityFieldLabel na página.
   * @param style String Estilo para modificação do elemento HTML apresentado.
   * @return String Retorna o script HTML contendo a representação do EntityFieldLabel.
   */
  static public String script(Facade             facade,
                              EntityField        entityField,
                              HttpServletRequest request,
                              String             id,
                              String             style) {
    return script(facade, entityField, request, id, style, false);
  }

  /**
   * Retorna o script HTML contendo a representação do EntityFieldLabel.
   * @param facade Facade Fachada.
   * @param entityField EntityField referente ao campo que se deseja representar.
   * @param request HttpServletRequest Requisição da página atual.
   * @param id String Identificação do EntityFieldLabel na página.
   * @param style String Estilo para modificação do elemento HTML apresentado.
   * @param disabled boolean True para que o EntityFieldLabel apareça desabilitado.
   * @return String Retorna o script HTML contendo a representação do EntityFieldLabel.
   */
  static public String script(Facade             facade,
                              EntityField        entityField,
                              HttpServletRequest request,
                              String             id,
                              String             style,
                              boolean            disabled) {
    // titulo do rótulo
    String caption = entityField.getCaption();
    // se está editando e o campo não é editável...
    if (Controller.isEditing(request) && !entityField.getEnabledOnEdit())
      disabled = true;
    // se está inserindo e o campo não é editável...
    if (Controller.isInserting(request) && !entityField.getEnabledOnInsert())
      disabled = true;
    // se é chave ou requerido e não está desabilitado...põe o sinal de obrigatório
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
