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
