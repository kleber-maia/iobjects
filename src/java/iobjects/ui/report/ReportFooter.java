package iobjects.ui.report;

import iobjects.*;
import iobjects.util.*;

/**
 * Representa o rodapé para ser exibido em um relatório.
 * @since 2006
 */
public class ReportFooter {

  private ReportFooter() {
  }

  /**
   * Retorna o script HTML contendo a representação do ReportFooter.
   * @param facade Facade Instância da fachada para obtenção de informações da
   *               aplicação.
   * @param showApplicationName boolean True para que seja incluído o nome da
   *                        aplicação.
   * @param showPoweredBy boolean True para que seja incluída a mensagem
   *                      Powered By Percent.
   * @return String Retorna o script HTML contendo a representação do ReportFooter.
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
