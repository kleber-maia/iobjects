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
import iobjects.xml.*;

/**
 * Representa o cabe�alho para ser exibido em um relat�rio.
 * @since 2006
 */
public class ReportHeader {

  private ReportHeader() {
  }

  /**
   * Retorna o script HTML contendo a representa��o do ReportHeader.
   * @param facade Facade Inst�ncia da fachada para obten��o de informa��es da
   *               aplica��o.
   * @param action Action que representa o relat�rio.
   * @param showReportTitle boolean True para que seja inclu�do o t�tulo do
   *                        relat�rio.
   * @param showMasterRelation boolean True para que seja inclu�da a sele��o
   *                           atual da MasterRelation.
   * @param showUserName boolean True para que seja inclu�do o nome do usu�rio
   *                     atual do sistema.
   * @param showDate boolean True para que seja inclu�da a data do sistema.
   * @param showTime boolean True para que seja inclu�da a hora do sistema.
   * @return String Retorna o script HTML contendo a representa��o do ReportHeader.
   * @throws Exception Em caso de exce��o na tentativa de obten��o da sele��o
   *                   atual da MasterRelation.
   */
  static public String script(Facade  facade,
                              Action  action,
                              boolean showReportTitle,
                              boolean showMasterRelation,
                              boolean showUserName,
                              boolean showDate,
                              boolean showTime) throws Exception {
    return "<p>"
         +   "<table style=\"width:100%;\">"
         +     "<tr>"
         +       "<td class=\"Title\">"
         +         (showReportTitle ? action.getCaption() : "")
         +       "</td>"
         +       "<td align=\"right\">"
         +         (showMasterRelation && facade.masterRelationInformation().getActive() ? StringTools.arrayStringToString(facade.masterRelation().getUserValues(), ", ") + "<br/>" : "")
         +         (showUserName ? facade.getLoggedUser().getName() + "<br/>" : "")
         +         (showDate ? DateTools.formatDate(DateTools.getActualDate()) + "\r" : "")
         +         (showTime ? DateTools.formatTime(DateTools.getActualDateTime()) : "")
         +       "</td>"
         +     "</tr>"
         +   "</table>"
         + "</p>";
  }

}
