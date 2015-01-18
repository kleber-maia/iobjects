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
 * Representa um controle de sele��o de valores em um Form.
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
    // se n�o temos a mesma quantidade de op��es e valores...exce��o
    if ((values.length > 0) && (values.length != options.length))
      throw new ExtendedException("iobjects.ui.FormSelect", "getSelectOptions", "A quantidade de valores e op��es informadas n�o combina.");
    // loop nas op��es
    for (int i=0; i<options.length; i++) {
      // valor da vez
      String value = values.length > 0 ? values[i] : i + "";
      // op��o da vez
      String option = options[i];
      // op��o da vez
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
   * Retorna o script HTML representando o controle de sele��o de valores.
   * @param facade Facade Fachada.
   * @param id String Identifica��o do FormSelect na p�gina.
   * @param options String[] Lista de op��es do FormSelect.
   * @param selectedValue String Valor referente � op��o selecionada por padr�o.
   * @param selectType int Tipo de sele��o que o usu�rio poder� realizar.
   * @param style String Estilo de formata��o do FormSelect.
   * @throws Exception Em caso de exce��o na verifica��o das op��es e valores
   *                   informados.
   * @return String Retorna o script HTML representando o controle de sele��o de
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
   * Retorna o script HTML representando o controle de sele��o de valores.
   * @param facade Facade Fachada.
   * @param id String Identifica��o do FormSelect na p�gina.
   * @param options String[] Lista de op��es do FormSelect.
   * @param selectedValue String Valor referente � op��o selecionada por padr�o.
   * @param width int Largura do FormSelect na p�gina ou 0 (zero) para que ele
   *              se ajuste automaticamente ao seu conteiner.
   * @param height int Altura do FormSelect na p�gina ou 0 (zero) para que ele
   *              se ajuste automaticamente ao seu conteiner.
   * @param selectType int Tipo de sele��o que o usu�rio poder� realizar.
   * @param constraint String Script JavaScript de valida��o do valor selecionado
   *                   no FormSelect.
   * @param constraintMessage String Mensagem de valida��o para ser exibida
   *                          ao usu�rio.
   * @param style String Estilo de formata��o do FormSelect.
   * @param onChangeScript C�digo JavaScript para ser executado quando o usu�rio
   *                       alterar o valor do elemento HTML.
   * @param readOnly boolean True se o FormSelect for somente leitura.
   * @throws Exception Em caso de exce��o na verifica��o das op��es e valores
   *                   informados.
   * @return String Retorna o script HTML representando o controle de sele��o de
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
   * Retorna o script HTML representando o controle de sele��o de valores.
   * @param facade Facade Fachada.
   * @param id String Identifica��o do FormSelect na p�gina.
   * @param options String[] Lista de op��es do FormSelect.
   * @param values String[] Lista de valores associados �s op��es do FormSelect.
   * @param selectedValue String Valor referente � op��o selecionada por padr�o.
   * @param selectType int Tipo de sele��o que o usu�rio poder� realizar.
   * @param style String Estilo de formata��o do FormSelect.
   * @throws Exception Em caso de exce��o na verifica��o das op��es e valores
   *                   informados.
   * @return String Retorna o script HTML representando o controle de sele��o de
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
   * Retorna o script HTML representando o controle de sele��o de valores.
   * @param facade Facade Fachada.
   * @param id String Identifica��o do FormSelect na p�gina.
   * @param options String[] Lista de op��es do FormSelect.
   * @param values String[] Lista de valores associados �s op��es do FormSelect.
   * @param selectedValue String Valor referente � op��o selecionada por padr�o.
   * @param width int Largura do FormSelect na p�gina ou 0 (zero) para que ele
   *              se ajuste automaticamente ao seu conteiner.
   * @param height int Altura do FormSelect na p�gina ou 0 (zero) para que ele
   *              se ajuste automaticamente ao seu conteiner.
   * @param selectType int Tipo de sele��o que o usu�rio poder� realizar.
   * @param constraint String Script JavaScript de valida��o do valor selecionado
   *                   no FormSelect.
   * @param constraintMessage String Mensagem de valida��o para ser exibida
   *                          ao usu�rio.
   * @param style String Estilo de formata��o do FormSelect.
   * @param onChangeScript C�digo JavaScript para ser executado quando o usu�rio
   *                       alterar o valor do elemento HTML.
   * @param readOnly boolean True se o FormSelect for somente leitura.
   * @return String Retorna o script HTML representando o controle de sele��o de
   *         valores.
   * @throws Exception Em caso de exce��o na verifica��o das op��es e valores
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
    // se n�o informou o Id...exce��o
    if ((id == null) || id.trim().equals(""))
      throw new ExtendedException("iobjects.ui.FormSelect", "script", "Id n�o informado.");
    // evento onChange...troca a cor da fonte quando o usu�rio alterar
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
