package iobjects.help;

import iobjects.*;

/**
 * Representa o gerenciador de ajuda da aplicação. O mecanismo de ajuda é capaz
 * de construir conteúdo de ajuda e suporte a partir dos objetos de negócio
 * da aplicação e suas ações.
 * @since 2006
 */
public class HelpManager {

  static public String ACTION_NAME = "actionName";
  static public String FAQ_NAME    = "faqName";
  static public String WINDOW_NAME = "help";

  private FAQList faqList = new FAQList();

  /**
   * Construtor padrão.
   */
  public HelpManager() {
  }

  /**
   * Retorna a lista de FAQ's da aplicação.
   * @return FAQList Retorna a lista de FAQ's da aplicação.
   */
  public FAQList faqList() {
    return faqList;
  }

}
