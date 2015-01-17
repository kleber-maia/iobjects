package iobjects.misc;

import iobjects.*;

/**
 * Representa uma a��o recente executada pelo usu�rio.
 */
public class RecentAction {

  private Action action  = null;
  private String caption = "";
  private String query   = "";
  private String title   = "";

  /**
   * Construtor padr�o.
   * @param action Action A��o executada.
   * @param query String Par�metros passados � a��o executada.
   * @param caption String T�tulo da a��o recente.
   * @param title String T�tulo do item acessado pela a��o recente.
   */
  public RecentAction(Action action,
                      String query,
                      String caption,
                      String title) {
    this.action = action;
    this.query = query;
    this.caption = caption;
    this.title = title;
  }

  public boolean equals(Object object) {
    return action.equals(((RecentAction)object).getAction()) &&
           caption.equals(((RecentAction)object).getCaption()) &&
           title.equals(((RecentAction)object).getTitle());
  }

  /**
   * Retorna a a��o executada.
   * @return Action Retorna a a��o executada.
   */
  public Action getAction() {
    return action;
  }

  /**
   * Retorna o titulo da a��o recentes.
   * @return Retorna o titulo da a��o recentes.
   */
  public String getCaption() {
    return caption;
  }

  /**
   * Retorna os par�metros passados � a��o.
   * @return String Retorna os par�metros passados � a��o.
   */
  public String getQuery() {
    return query;
  }

  /**
   * Retorna o titulo do item acessado pela da a��o recentes.
   * @return Retorna o titulo do item acessado pela da a��o recentes.
   */
  public String getTitle() {
    return title;
  }

}
