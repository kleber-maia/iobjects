package iobjects.ui.report;

/**
 * Representa um t�tulo de sess�o para ser exibido em um relat�rio.
 * @since 2006
 */
public class ReportTitle {

  static public final int LEVEL_1 = 1;
  static public final int LEVEL_2 = 2;
  static public final int LEVEL_3 = 3;

  private ReportTitle() {
  }

  /**
   * Retorna o script HTML contendo a representa��o do ReportTitle.
   * @param caption String T�tulo da sess�o.
   * @param level int N�vel do t�tulo da sess�o. Veja as contantes da classe.
   * @return String Retorna o script HTML contendo a representa��o do ReportTitle.
   */
  static public String script(String caption,
                              int    level) {
    return script(caption, level, "");
  }

  /**
   * Retorna o script HTML contendo a representa��o do ReportTitle.
   * @param caption String T�tulo da sess�o.
   * @param level int N�vel do t�tulo da sess�o. Veja as contantes da classe.
   * @param id String Identifica��o do ReportTitle na p�gina.
   * @return String Retorna o script HTML contendo a representa��o do ReportTitle.
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
