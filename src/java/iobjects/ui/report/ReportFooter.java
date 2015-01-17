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
