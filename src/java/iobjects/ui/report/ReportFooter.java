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
import iobjects.util.*;

/**
 * Representa o rodap� para ser exibido em um relat�rio.
 * @since 2006
 */
public class ReportFooter {

  private ReportFooter() {
  }

  /**
   * Retorna o script HTML contendo a representa��o do ReportFooter.
   * @param facade Facade Inst�ncia da fachada para obten��o de informa��es da
   *               aplica��o.
   * @param showApplicationName boolean True para que seja inclu�do o nome da
   *                        aplica��o.
   * @param showPoweredBy boolean True para que seja inclu�da a mensagem
   *                      Powered By Percent.
   * @return String Retorna o script HTML contendo a representa��o do ReportFooter.
   */
  static public String script(Facade  facade,
                              boolean showApplicationName,
                              boolean showPoweredBy) {
    return "<p>"
         +   "<table style=\"width:100%;\">"
         +     "<tr>"
         +       "<td align=\"left\">"
         +         (showApplicationName ? "<b>" + facade.applicationInformation().getName() + "</b>" : "")
         +       "</td>"
         +       "<td align=\"right\">"
         +         (showPoweredBy ? "<b>" + facade.applicationInformation().getURL() + "</b>" : "")
         +       "</td>"
         +     "</tr>"
         +   "</table>"
         + "</p>";
  }

}
