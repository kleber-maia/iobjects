package iobjects.ui.help;

import iobjects.*;
import iobjects.help.*;
import iobjects.servlet.*;
import iobjects.ui.*;

/**
 * Utilit�rio para cria��o de bot�es de chamada de ajuda.
 * @since 2006
 */
public class HelpButton {

  static public int KIND_DEFAULT          = Button.KIND_DEFAULT;
  static public int KIND_DEFAULT_SELECTED = Button.KIND_DEFAULT_SELECTED;
  static public int KIND_TOOLBUTTON       = Button.KIND_TOOLBUTTON;

  private HelpButton() {
  }

  /**
   * Retorna o script contendo a representa��o HTML do bot�o de ajuda.
   * @param facade Facade Fachada.
   * @param showCaption boolean True para que o caption do bot�o seja exibido ou
   *                    false para que apenas a imagem seja exibida.
   * @param kind int Tipo do bot�o.
   * @return String Retorna o script contendo a representa��o HTML do bot�o de ajuda.
   */
  static public String script(Facade  facade,
                              boolean showCaption,
                              int     kind) {
    return script(facade, null, showCaption, kind);
  }

  /**
   * Retorna o script contendo a representa��o HTML do bot�o de ajuda.
   * @param facade Facade Fachada.
   * @param action Action cuja ajuda ser� exibida.
   * @param showCaption boolean True para que o caption do bot�o seja exibido ou
   *                    false para que apenas a imagem seja exibida.
   * @param kind int Tipo do bot�o.
   * @return String Retorna o script contendo a representa��o HTML do bot�o de ajuda.
   */
  static public String script(Facade  facade,
                              Action  action,
                              boolean showCaption,
                              int     kind) {
    return Button.script(facade,
                         "buttonHelp",
                        (showCaption ? "Ajuda" : ""),
                        (action != null ? "Mostra a ajuda para " + action.getCaption() + "." : "Mostra o centro de ajuda e suporte da aplica��o."),
                        ImageList.COMMAND_HELP,
                        "",
                        kind,
                        (!showCaption ? "width:24px;" : ""),
                        Popup.script(Controller.ACTION_HELP.url((action != null ? HelpManager.ACTION_NAME + "=" + action.getName() : "")), HelpManager.WINDOW_NAME, 480, 700, Popup.POSITION_RIGHT_BOTTOM, false, false, false, false, false, true),
                        false);
  }

}
