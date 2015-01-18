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

/**
 * Representa um título de sessão para ser exibido em um relatório.
 * @since 2006
 */
public class ReportTitle {

  static public final int LEVEL_1 = 1;
  static public final int LEVEL_2 = 2;
  static public final int LEVEL_3 = 3;

  private ReportTitle() {
  }

  /**
   * Retorna o script HTML contendo a representação do ReportTitle.
   * @param caption String Título da sessão.
   * @param level int Nível do título da sessão. Veja as contantes da classe.
   * @return String Retorna o script HTML contendo a representação do ReportTitle.
   */
  static public String script(String caption,
                              int    level) {
    return script(caption, level, "");
  }

  /**
   * Retorna o script HTML contendo a representação do ReportTitle.
   * @param caption String Título da sessão.
   * @param level int Nível do título da sessão. Veja as contantes da classe.
   * @param id String Identificação do ReportTitle na página.
   * @return String Retorna o script HTML contendo a representação do ReportTitle.
   */
  static public String script(String caption,
                              int    level,
                              String id) {
    return "<p>"
         +   "<table id=\"" + id + "\" class=\"Title" + level + "\" style=\"width:100%;\">"
         +     "<tr>"
         +       "<td>" + caption + "</td>"
         +     "</tr>"
         +   "</table>"
         + "</p>";
  }

}
