package iobjects.ui;

import iobjects.*;
import iobjects.servlet.*;

/**
 * Classe utilit�ria para abertura de janelas Popup.
 */
public class Popup {

  static public final int POSITION_CENTER       = 1;
  static public final int POSITION_LEFT_BOTTOM  = 2;
  static public final int POSITION_LEFT_TOP     = 3;
  static public final int POSITION_RIGHT_BOTTOM = 4;
  static public final int POSITION_RIGHT_TOP    = 5;

  /**
   * Retorna o Script que ir� abrir a janela popup exibindo a URL especificada.
   * @param url String URL para ser exibida na janela.
   * @param windowName String Nome da janela que ser� exibida.
   * @param height int Altura da janela em pixels ou 0 (zero) para que ela se
   *                   ajuste automaticamente � tela.
   * @param width int Largura da janela ou 0 (zero) para que ela se
   *                  ajuste automaticamente � tela.
   * @param position int Constante indicando a posi��o da janela na tela.
   * @return String Retorna o Script que ir� abrir a janela popup exibindo a URL
   *                especificada.
   */
  static public String script(String  url,
                              String  windowName,
                              int     height,
                              int     width,
                              int     position) {
    return script(url,
                  windowName,
                  height,
                  width,
                  position,
                  false,
                  false,
                  false,
                  false,
                  false,
                  false);
  }

  /**
   * Retorna o Script que ir� abrir a janela popup exibindo a URL especificada.
   * @param url String URL para ser exibida na janela.
   * @param windowName String Nome da janela que ser� exibida.
   * @param height int Altura da janela em pixels ou 0 (zero) para que ela se
   *                   ajuste automaticamente � tela.
   * @param width int Largura da janela ou 0 (zero) para que ela se
   *                  ajuste automaticamente � tela.
   * @param position int Constante indicando a posi��o da janela na tela.
   * @param showMenuBar boolean True para que a barra de menu seja exibida.
   * @param showToolBar boolean True para que a barra de ferramentas seja exibida.
   * @param showLocationBar boolean True para que a barra de endere�o seja exibida.
   * @param showStatusBar boolean True para que a barra de status seja exibida.
   * @param showScrollBars boolean True para que as barras de rolagem sejam exibidas.
   * @param resizable boolean True para que a jenale possa ser redimensionada pelo usu�rio.
   * @return String Retorna o Script que ir� abrir a janela popup exibindo a URL
   *                especificada.
   */
  static public String script(String  url,
                              String  windowName,
                              int     height,
                              int     width,
                              int     position,
                              boolean showMenuBar,
                              boolean showToolBar,
                              boolean showLocationBar,
                              boolean showStatusBar,
                              boolean showScrollBars,
                              boolean resizable) {
    // nosso resultado
    StringBuffer result = new StringBuffer();
    result.append("javascript:");
    // in�cio da fun�ao
    result.append("function showPopup" + windowName + "() {");
    // c�lculo da posi��o e dimens�o da janela
    result.append("var windowHeight=" + (height > 0 ? height + "" : "screen.availHeight-30") + "; ");
    result.append("var windowWidth=" + (width > 0 ? width + "" : "screen.availWidth-8") + "; ");
    switch (position) {
      case POSITION_CENTER: {
        result.append("var windowTop=(screen.availHeight-windowHeight)/2; ");
        result.append("var windowLeft=(screen.availWidth-windowWidth)/2; ");
        break;
      }
      case POSITION_LEFT_BOTTOM: {
        result.append("var windowTop=screen.availHeight-windowHeight-30; ");
        result.append("var windowLeft=0; ");
        break;
      }
      case POSITION_LEFT_TOP: {
        result.append("var windowTop=0; ");
        result.append("var windowLeft=0; ");
        break;
      }
      case POSITION_RIGHT_BOTTOM: {
        result.append("var windowTop=screen.availHeight-windowHeight-30; ");
        result.append("var windowLeft=screen.availWidth-windowWidth-8; ");
        break;
      }
      case POSITION_RIGHT_TOP: {
        result.append("var windowTop=0; ");
        result.append("var windowLeft=screen.availWidth-windowWidth-8; ");
        break;
      }
    } // switch
    // chamada da janela
    result.append("window.open('" + url + "','" + windowName + "','top=' + windowTop + ',left=' + windowLeft + ',width=' + windowWidth + ',height=' + windowHeight + ',");
    // op��es de exibi��o
    result.append("location="   + (showLocationBar ? "yes" : "no") + ",");
    result.append("menubar="    + (showMenuBar     ? "yes" : "no") + ",");
    result.append("toolbar="    + (showToolBar     ? "yes" : "no") + ",");
    result.append("status="     + (showStatusBar   ? "yes" : "no") + ",");
    result.append("scrollbars=" + (showScrollBars  ? "yes" : "no") + ",");
    result.append("resizable="  + (resizable       ? "yes" : "no"));
    // fim da chamada da janela
    result.append("');");
    // fim da fun��o
    result.append("}");
    // chamada da popup
    result.append("showPopup" + windowName + "();");
    // retorna
    return result.toString();
  }

}
