/*
The MIT License (MIT)

Copyright (c) 2008 Kleber Maia de Andrade

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/   
package iobjects.ui.help;

import iobjects.*;
import iobjects.help.*;
import iobjects.servlet.*;
import iobjects.ui.*;

/**
 * Utilitário para criação de botões de chamada de ajuda.
 * @since 2006
 */
public class HelpButton {

  static public int KIND_DEFAULT          = Button.KIND_DEFAULT;
  static public int KIND_DEFAULT_SELECTED = Button.KIND_DEFAULT_SELECTED;
  static public int KIND_TOOLBUTTON       = Button.KIND_TOOLBUTTON;

  private HelpButton() {
  }

  /**
   * Retorna o script contendo a representação HTML do botão de ajuda.
   * @param facade Facade Fachada.
   * @param showCaption boolean True para que o caption do botão seja exibido ou
   *                    false para que apenas a imagem seja exibida.
   * @param kind int Tipo do botão.
   * @return String Retorna o script contendo a representação HTML do botão de ajuda.
   */
  static public String script(Facade  facade,
                              boolean showCaption,
                              int     kind) {
    return script(facade, null, showCaption, kind);
  }

  /**
   * Retorna o script contendo a representação HTML do botão de ajuda.
   * @param facade Facade Fachada.
   * @param action Action cuja ajuda será exibida.
   * @param showCaption boolean True para que o caption do botão seja exibido ou
   *                    false para que apenas a imagem seja exibida.
   * @param kind int Tipo do botão.
   * @return String Retorna o script contendo a representação HTML do botão de ajuda.
   */
  static public String script(Facade  facade,
                              Action  action,
                              boolean showCaption,
                              int     kind) {
    return Button.script(facade,
                         "buttonHelp",
                        (showCaption ? "Ajuda" : ""),
                        (action != null ? "Mostra a ajuda para " + action.getCaption() + "." : "Mostra o centro de ajuda e suporte da aplicação."),
                        ImageList.COMMAND_HELP,
                        "",
                        kind,
                        (!showCaption ? "width:24px;" : ""),
                        Popup.script(Controller.ACTION_HELP.url((action != null ? HelpManager.ACTION_NAME + "=" + action.getName() : "")), HelpManager.WINDOW_NAME, 480, 700, Popup.POSITION_RIGHT_BOTTOM, false, false, false, false, false, true),
                        false);
  }

}
