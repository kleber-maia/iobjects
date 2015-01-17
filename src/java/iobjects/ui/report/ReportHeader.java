package iobjects.ui.report;

import iobjects.*;
import iobjects.util.*;
import iobjects.xml.*;

/**
 * Representa o cabeçalho para ser exibido em um relatório.
 * @since 2006
 */
public class ReportHeader {

  private ReportHeader() {
  }

  /**
   * Retorna o script HTML contendo a representação do ReportHeader.
   * @param facade Facade Instância da fachada para obtenção de informações da
   *               aplicação.
   * @param action Action que representa o relatório.
   * @param showReportTitle boolean True para que seja incluído o título do
   *                        relatório.
   * @param showMasterRelation boolean True para que seja incluída a seleção
   *                           atual da MasterRelation.
   * @param showUserName boolean True para que seja incluído o nome do usuário
   *                     atual do sistema.
   * @param showDate boolean True para que seja incluída a data do sistema.
   * @param showTime boolean True para que seja incluída a hora do sistema.
   * @return String Retorna o script HTML contendo a representação do ReportHeader.
   * @throws Exception Em caso de exceção na tentativa de obtenção da seleção
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
