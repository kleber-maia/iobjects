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
import iobjects.ui.*;

/**
 * Representa um controle de seleção de valores provenientes de um EntityField
 * em um Form.
 */
public class EntityFormSelect {

  private EntityFormSelect() {
  }

  /**
   * Retorna o script HTML representando o controle de seleção.
   * @param facade Facade Fachada.
   * @param entityField EntityField cujas propriedades irão gerar
   *                    o controle de seleção.
   * @param entityInfo EntityInfo contendo os dados que serão exibidos e editados.
   * @param request HttpServletRequest Requisição da página atual.
   * @return String Retorna o script HTML representando o controle de seleção.
   * @throws Exception Em caso de exceção na tentativa de obter os valores
   *                   de 'entityInfo'.
   */
  static public String script(Facade             facade,
                              EntityField        entityField,
                              EntityInfo         entityInfo,
                              HttpServletRequest request) throws Exception {
    return script(facade,
                  entityField,
                  entityInfo,
                  request,
                  0,
                  "",
                  "",
                  false);
  }

  /**
   * Retorna o script HTML representando o controle de seleção.
   * @param facade Facade Fachada.
   * @param entityField EntityField cujas propriedades irão gerar
   *                    o controle de seleção.
   * @param entityInfo EntityInfo contendo os dados que serão exibidos e editados.
   * @param request HttpServletRequest Requisição da página atual.
   * @param width int Largura do controle de seleção na página ou 0 (zero) para
   *              que ele se ajuste automaticamente ao seu conteiner.
   * @param style String Estilo de formatação do EntityFormSelect.
   * @param readOnly boolean True se o EntityFormSelect for somente leitura.
   * @param onChangeScript Código JavaScript para ser executado quando o usuário
   *                       alterar o valor do elemento HTML.
   * @return String Retorna o script HTML representando o controle de seleção.
   * @throws Exception Em caso de exceção na tentativa de obter os valores
   *                   de 'entityInfo'.
   */
  static public String script(Facade             facade,
                              EntityField        entityField,
                              EntityInfo         entityInfo,
                              HttpServletRequest request,
                              int                width,
                              String             style,
                              String             onChangeScript,
                              boolean            readOnly) throws Exception {
    // se está editando e o campo não é editável...
    if (Controller.isEditing(request) && !entityField.getEnabledOnEdit())
      readOnly = true;
    // se está inserindo e o campo não é editável...
    if (Controller.isInserting(request) && !entityField.getEnabledOnInsert())
      readOnly = true;
    // retorna
    return FormSelect.script(facade,
                             entityField.getFieldAlias(),
                             entityField.getLookupList(),
                             new String[0],
                             entityField.getFormatedFieldValue(entityInfo),
                             width,
                             0,
                             FormSelect.SELECT_TYPE_SINGLE,
                             entityField.getScriptConstraint(),
                             entityField.getScriptConstraintErrorMessage(),
                             style,
                             onChangeScript,
                             readOnly);
  }

}
