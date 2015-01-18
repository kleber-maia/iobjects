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
package iobjects.ui.param;

import iobjects.*;
import iobjects.ui.*;

/**
 * Representa um controle de sele��o de valores provenientes de um Param
 * em um Form.
 */
public class ParamFormSelect {

  private ParamFormSelect() {
  }

  /**
   * Retorna o script HTML representando o controle de sele��o.
   * @param facade Facade Fachada.
   * @param param Param cujas propriedades ir�o gerar o controle de sele��o.
   * @return String Retorna o script HTML representando o controle de sele��o.
   * @throws Exception Em caso de exce��o na verifica��o das op��es e valores
   *         informados.
   */
  static public String script(Facade facade,
                              Param  param) throws Exception {
    return script(facade,
                  param,
                  0,
                  "",
                  "");
  }

  /**
   * Retorna o script HTML representando o controle de sele��o.
   * @param facade Facade Fachada.
   * @param param Param cujas propriedades ir�o gerar o controle de sele��o.
   * @param width int Largura do controle de sele��o na p�gina ou 0 (zero) para
   *              que ele se ajuste automaticamente ao seu conteiner.
   * @param style String Estilo de formata��o do ParamFormSelect.
   * @param onChangeScript C�digo JavaScript para ser executado quando o usu�rio
   *                       alterar o valor do elemento HTML.
   * @return String Retorna o script HTML representando o controle de sele��o.
   * @throws Exception Em caso de exce��o na verifica��o das op��es e valores
   *         informados.
   */
  static public String script(Facade facade,
                              Param  param,
                              int    width,
                              String style,
                              String onChangeScript) throws Exception {
    // verifica se o campo ser� somente leitura
    boolean readOnly = false;
    // retorna
    return FormSelect.script(facade,
                             param.getName(),
                             param.getLookupList(),
                             new String[0],
                             param.getValue(),
                             width,
                             0,
                             FormSelect.SELECT_TYPE_SINGLE,
                             param.getScriptConstraint(),
                             param.getScriptConstraintErrorMessage(),
                             style,
                             onChangeScript,
                             readOnly);
  }

}
