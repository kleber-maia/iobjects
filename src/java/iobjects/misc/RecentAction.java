package iobjects.misc;

import iobjects.*;

/**
 * Representa uma ação recente executada pelo usuário.
 */
public class RecentAction {

  private Action action  = null;
  private String caption = "";
  private String query   = "";
  private String title   = "";

  /**
   * Construtor padrão.
   * @param action Action Ação executada.
   * @param query String Parâmetros passados à ação executada.
   * @param caption String Título da ação recente.
   * @param title String Título do item acessado pela ação recente.
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
   * Retorna a ação executada.
   * @return Action Retorna a ação executada.
   */
  public Action getAction() {
    return action;
  }

  /**
   * Retorna o titulo da ação recentes.
   * @return Retorna o titulo da ação recentes.
   */
  public String getCaption() {
    return caption;
  }

  /**
   * Retorna os parâmetros passados à ação.
   * @return String Retorna os parâmetros passados à ação.
   */
  public String getQuery() {
    return query;
  }

  /**
   * Retorna o titulo do item acessado pela da ação recentes.
   * @return Retorna o titulo do item acessado pela da ação recentes.
   */
  public String getTitle() {
    return title;
  }

}
