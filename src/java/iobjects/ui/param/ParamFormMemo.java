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
 * Representa um controle de entrada de texto no formato memo em um Form
 * associado a um Param.
 * @since 2006
 */
public class ParamFormMemo {

  private ParamFormMemo() {
  }

  /**
   * Retorna o script HTML representando o ParamFormMemo.
   * @param facade Facade Fachada.
   * @param param Param cujas propriedades irão gerar o ParamFormMemo.
   * @return String Retorna o script HTML representando o ParamFormMemo.
   * @throws Exception Em caso de exceção na tentativa de obter os valores
   *                   de 'param'.
   */
  static public String script(Facade facade,
                              Param  param) throws Exception {
    return script(facade, param, 0, 0, "", "", false, false);
  }

  /**
   * Retorna o script HTML representando o ParamFormMemo.
   * @param facade Facade Fachada.
   * @param param Param cujas propriedades irão gerar o ParamFormMemo.
   * @param height int Altura do ParamFormMemo na página ou 0 (zero) para que ele
   *               se ajuste automaticamente ao seu conteiner.
   * @param width int Largura do ParamFormMemo na página, 0 (zero) para que ele
   *              se ajuste automaticamente ao seu conteiner.
   * @param style String Estilo de formatação do ParamFormMemo.
   * @param onChangeScript Código JavaScript para ser executado quando o usuário
   *                       alterar o valor do elemento HTML.
   * @param readOnly boolean True se o FormMemo for somente leitura.
   * @param wysiwyg boolean True para que o FormMemo seja exibido como editor
   *                        HTML.
   * @return String Retorna o script HTML representando o ParamFormMemo.
   * @throws Exception Em caso de exceção na tentativa de obter os valores
   *                   de 'param'.
   */
  static public String script(Facade  facade,
                              Param   param,
                              int     height,
                              int     width,
                              String  style,
                              String  onChangeScript,
                              boolean readOnly,
                              boolean wysiwyg) throws Exception {
    return script(facade,
                  param,
                  height,
                  width,
                  style,
                  onChangeScript,
                  readOnly,
                  wysiwyg,
                  "",
                  "");
  }

  /**
   * Retorna o script HTML representando o ParamFormMemo.
   * @param facade Facade Fachada.
   * @param param Param cujas propriedades irão gerar o ParamFormMemo.
   * @param height int Altura do ParamFormMemo na página ou 0 (zero) para que ele
   *               se ajuste automaticamente ao seu conteiner.
   * @param width int Largura do ParamFormMemo na página, 0 (zero) para que ele
   *              se ajuste automaticamente ao seu conteiner.
   * @param style String Estilo de formatação do ParamFormMemo.
   * @param onChangeScript Código JavaScript para ser executado quando o usuário
   *                       alterar o valor do elemento HTML.
   * @param readOnly boolean True se o FormMemo for somente leitura.
   * @param wysiwyg boolean True para que o FormMemo seja exibido como editor
   *                        HTML.
   * @param imagesPath String Caminho local no servidor onde se encontram as
   *                          imagens para serem inseridas no editor HTML.
   * @param imagesUrl String Url do servidor onde se encontram as imagens para
   *                         serem inseridas no editor HTML.
   * @return String Retorna o script HTML representando o ParamFormMemo.
   * @throws Exception Em caso de exceção na tentativa de obter os valores
   *                   de 'param'.
   */
  static public String script(Facade  facade,
                              Param   param,
                              int     height,
                              int     width,
                              String  style,
                              String  onChangeScript,
                              boolean readOnly,
                              boolean wysiwyg,
                              String  imagesPath,
                              String  imagesUrl) throws Exception {
    // se a formatação é UPPER_CASE
    if (param.getFormat() == Format.FORMAT_UPPER_CASE)
      style = "text-transform:uppercase;" + style;
    // retorna
    return FormMemo.script(facade,
                           param.getName(),
                           param.getFormatedValue(),
                           height,
                           width,
                           param.getSize(),
                           param.getScriptConstraint(),
                           param.getScriptConstraintErrorMessage(),
                           style,
                           onChangeScript,
                           readOnly,
                           wysiwyg,
                           imagesPath,
                           imagesUrl);
  }

}
