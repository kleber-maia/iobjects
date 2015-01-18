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
package iobjects.ui;

import iobjects.*;
import iobjects.util.*;

/**
 * Representa um controle de seleção de valores em um Form.
 */
public class FormSelect {

  static public final String DEFAULT_CURSOR_SCRIPT = "document.body.style.cursor='default';";

  static public final byte SELECT_TYPE_SINGLE   = 1;
  static public final byte SELECT_TYPE_MULTIPLE = 2;

  static private final String ON_CHANGE_SCRIPT = "style.color = '#0000FF';";

  private FormSelect() {
  }

  static private String getSelectOptions(Facade   facade,
                                         String[] options,
                                         String[] values,
                                         String   selectedValue) throws Exception {
    // nossos resultado
    StringBuffer result = new StringBuffer("");
    // se não temos a mesma quantidade de opções e valores...exceção
    if ((values.length > 0) && (values.length != options.length))
      throw new ExtendedException("iobjects.ui.FormSelect", "getSelectOptions", "A quantidade de valores e opções informadas não combina.");
    // loop nas opções
    for (int i=0; i<options.length; i++) {
      // valor da vez
      String value = values.length > 0 ? values[i] : i + "";
      // opção da vez
      String option = options[i];
      // opção da vez
      result.append("<option value=\"" + value + "\""
                    +       (selectedValue.equals(value) || selectedValue.equals(option) ? "selected" : "")
                    + ">"
                    + option
                    + "</option>\r\n");
    } // for
    // retorna
    return result.toString();
  }

  /**
   * Retorna o script HTML representando o controle de seleção de valores.
   * @param facade Facade Fachada.
   * @param id String Identificação do FormSelect na página.
   * @param options String[] Lista de opções do FormSelect.
   * @param selectedValue String Valor referente à opção selecionada por padrão.
   * @param selectType int Tipo de seleção que o usuário poderá realizar.
   * @param style String Estilo de formatação do FormSelect.
   * @throws Exception Em caso de exceção na verificação das opções e valores
   *                   informados.
   * @return String Retorna o script HTML representando o controle de seleção de
   *         valores.
   */
  static public String script(Facade   facade,
                              String   id,
                              String[] options,
                              String   selectedValue,
                              int      selectType,
                              String   style) throws Exception {
    return script(facade,
                  id,
                  options,
                  selectedValue,
                  0,
                  0,
                  selectType,
                  "",
                  "",
                  style,
                  "",
                  false);
  }

  /**
   * Retorna o script HTML representando o controle de seleção de valores.
   * @param facade Facade Fachada.
   * @param id String Identificação do FormSelect na página.
   * @param options String[] Lista de opções do FormSelect.
   * @param selectedValue String Valor referente à opção selecionada por padrão.
   * @param width int Largura do FormSelect na página ou 0 (zero) para que ele
   *              se ajuste automaticamente ao seu conteiner.
   * @param height int Altura do FormSelect na página ou 0 (zero) para que ele
   *              se ajuste automaticamente ao seu conteiner.
   * @param selectType int Tipo de seleção que o usuário poderá realizar.
   * @param constraint String Script JavaScript de validação do valor selecionado
   *                   no FormSelect.
   * @param constraintMessage String Mensagem de validação para ser exibida
   *                          ao usuário.
   * @param style String Estilo de formatação do FormSelect.
   * @param onChangeScript Código JavaScript para ser executado quando o usuário
   *                       alterar o valor do elemento HTML.
   * @param readOnly boolean True se o FormSelect for somente leitura.
   * @throws Exception Em caso de exceção na verificação das opções e valores
   *                   informados.
   * @return String Retorna o script HTML representando o controle de seleção de
   *         valores.
   */
  static public String script(Facade   facade,
                              String   id,
                              String[] options,
                              String   selectedValue,
                              int      width,
                              int      height,
                              int      selectType,
                              String   constraint,
                              String   constraintMessage,
                              String   style,
                              String   onChangeScript,
                              boolean  readOnly) throws Exception {
    return script(facade,
                  id,
                  options,
                  new String[0],
                  selectedValue,
                  width,
                  height,
                  selectType,
                  constraint,
                  constraintMessage,
                  style,
                  onChangeScript,
                  readOnly);
  }

  /**
   * Retorna o script HTML representando o controle de seleção de valores.
   * @param facade Facade Fachada.
   * @param id String Identificação do FormSelect na página.
   * @param options String[] Lista de opções do FormSelect.
   * @param values String[] Lista de valores associados às opções do FormSelect.
   * @param selectedValue String Valor referente à opção selecionada por padrão.
   * @param selectType int Tipo de seleção que o usuário poderá realizar.
   * @param style String Estilo de formatação do FormSelect.
   * @throws Exception Em caso de exceção na verificação das opções e valores
   *                   informados.
   * @return String Retorna o script HTML representando o controle de seleção de
   *         valores.
   */
  static public String script(Facade   facade,
                              String   id,
                              String[] options,
                              String[] values,
                              String   selectedValue,
                              int      selectType,
                              String   style) throws Exception {
    return script(facade,
                  id,
                  options,
                  values,
                  selectedValue,
                  0,
                  0,
                  selectType,
                  "",
                  "",
                  style,
                  "",
                  false);
  }

  /**
   * Retorna o script HTML representando o controle de seleção de valores.
   * @param facade Facade Fachada.
   * @param id String Identificação do FormSelect na página.
   * @param options String[] Lista de opções do FormSelect.
   * @param values String[] Lista de valores associados às opções do FormSelect.
   * @param selectedValue String Valor referente à opção selecionada por padrão.
   * @param width int Largura do FormSelect na página ou 0 (zero) para que ele
   *              se ajuste automaticamente ao seu conteiner.
   * @param height int Altura do FormSelect na página ou 0 (zero) para que ele
   *              se ajuste automaticamente ao seu conteiner.
   * @param selectType int Tipo de seleção que o usuário poderá realizar.
   * @param constraint String Script JavaScript de validação do valor selecionado
   *                   no FormSelect.
   * @param constraintMessage String Mensagem de validação para ser exibida
   *                          ao usuário.
   * @param style String Estilo de formatação do FormSelect.
   * @param onChangeScript Código JavaScript para ser executado quando o usuário
   *                       alterar o valor do elemento HTML.
   * @param readOnly boolean True se o FormSelect for somente leitura.
   * @return String Retorna o script HTML representando o controle de seleção de
   *         valores.
   * @throws Exception Em caso de exceção na verificação das opções e valores
   *                   informados.
   */
  static public String script(Facade   facade,
                              String   id,
                              String[] options,
                              String[] values,
                              String   selectedValue,
                              int      width,
                              int      height,
                              int      selectType,
                              String   constraint,
                              String   constraintMessage,
                              String   style,
                              String   onChangeScript,
                              boolean  readOnly) throws Exception {
    // se não informou o Id...exceção
    if ((id == null) || id.trim().equals(""))
      throw new ExtendedException("iobjects.ui.FormSelect", "script", "Id não informado.");
    // evento onChange...troca a cor da fonte quando o usuário alterar
    String onChange = ON_CHANGE_SCRIPT + onChangeScript;
    // retorna
    return "<select size=\"" + (selectType == SELECT_TYPE_SINGLE ? 1 : 2) + "\" "
          +        "id=\"" + id + "\" "
          +        "name=\"" + id + "\" "
          + ((!constraint.equals(""))
            ?  "onDblClick=\"if (!(" + constraint + ")) {alert('" + constraintMessage + "'); " + DEFAULT_CURSOR_SCRIPT + " return false;} else return true;\" "
            :  "")
          +        "style=\"width:" + (width > 0 ? width + "px" : "100%") + "; height:" + (height > 0 ? height + "px" : "100%") + ";" + style + "\" "
          +        (readOnly ? " readOnly=\"readOnly\" " : "")
          +        "onchange=\"" + onChange + "\" >\r\n"
          + getSelectOptions(facade, options, values, selectedValue) + "\r\n"
          + "</select>\r\n";
  }

}
