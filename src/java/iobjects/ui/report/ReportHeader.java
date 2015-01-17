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
