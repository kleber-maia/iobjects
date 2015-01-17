package iobjects.help;

import iobjects.*;

/**
 * Representa o gerenciador de ajuda da aplica��o. O mecanismo de ajuda � capaz
 * de construir conte�do de ajuda e suporte a partir dos objetos de neg�cio
 * da aplica��o e suas a��es.
 * @since 2006
 */
public class HelpManager {

  static public String ACTION_NAME = "actionName";
  static public String FAQ_NAME    = "faqName";
  static public String WINDOW_NAME = "help";

  private FAQList faqList = new FAQList();

  /**
   * Construtor padr�o.
   */
  public HelpManager() {
  }

  /**
   * Retorna a lista de FAQ's da aplica��o.
   * @return FAQList Retorna a lista de FAQ's da aplica��o.
   */
  public FAQList faqList() {
    return faqList;
  }

}
