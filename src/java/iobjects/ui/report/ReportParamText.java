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
package iobjects.ui.report;

import iobjects.*;
import iobjects.ui.*;
import iobjects.util.*;

/**
 * Representa um controle de exibição de texto em um relatório associado a um Param.
 * @since 2006
 */
public class ReportParamText {

  private ReportParamText() {
  }

  /**
   * Retorna o script HTML contendo a representação do ReportParamText.
   * @param facade Facade Fachada.
   * @param param Param que se deseja representar.
   * @return String Retorna o script HTML contendo a representação do ReportParamText.
   * @throws Exception Em caso de exceção na tentativa de formatar o valor do
   *                   Param informado.
   */
  static public String script(Facade facade,
                              Param param) throws Exception {
    return script(facade, param, "", "");
  }

  /**
   * Retorna o script HTML contendo a representação do ReportParamText.
   * @param facade Facade Fachada.
   * @param param Param que se deseja representar.
   * @param id String Identificação do ReportParamText na página.
   * @param style String Estilo para modificação do elemento HTML apresentado.
   * @return String Retorna o script HTML contendo a representação do ReportParamText.
   * @throws Exception Em caso de exceção na tentativa de formatar o valor do
   *                   Param informado.
   */
  static public String script(Facade facade,
                              Param  param,
                              String id,
                              String style) throws Exception {
    // valor do rótulo
    String value = param.getFormatedValue();
    // retorna
    return "<span id=\"" + id + "\" "
         +       "class=\"ParamText\" "
         +       "style=\"" + style + "\">"
         + value
         + "</span> \r";
  }

}
