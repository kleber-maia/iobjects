package iobjects.ui;

/**
 * Utilit�rio para cria��o de menus suspensos contendo op��es para o usu�rio. 
 */
public class PopupMenu {

  private String anchorId = "";
  private String id       = "";

  /**
   * Construtor padr�o.
   * @param id String Identifica��o do PopupMenu na p�gina.
   * @param anchorId String Identifica��o do objeto que servir� como �ncora para
   *        o PopupMenu na p�gina. O PopupMenu ser� exibido imediatamente abaixo
   *        ou acima deste objeto, dependendo do sey tamanho e do espa�o 
   *        dispon�vel na p�gina.
   */
  public PopupMenu(String id,
                   String anchorId) {
    // nossos valores
    this.id       = id;
    this.anchorId = anchorId;
  }

  /**
   * Retorna a representa��o HTML do in�cio do PopupMenu.
   * @return String Retorna a representa��o HTML do in�cio do PopupMenu.
   */
  public String begin() {
    String result = "";
    // nosso container
    result += "<div id=\"" + id + "\" class=\"popupMenu\" style=\"position:absolute; padding:4px; visibility:hidden; overflow-y:auto;\" onmouseover=\"this.style.visibility = 'visible';\" onmouseout=\"this.style.visibility = 'hidden';\">";
    // retorna
    return result;
  }

  /**
   * Retorna a representa��o HTML do t�rmino do PopupMenu.
   * @return String Retorna a representa��o HTML do t�rmino do PopupMenu.
   */
  public String end() {
    return "</div>";
  }

  /**
   * Retorna a representa��o JavaScript da fun��o para ocultar o PopupMenu.
   * Exemplo:
   * <code>
   *   &lt;button onclick="&lt;%=popupMenu.hideScript()%&gt;"&gt;Op��es&lt;/button&gt;
   * </code>
   * @return Retorna a representa��o JavaScript da fun��o para ocultar o PopupMenu.
   */
  public String hideScript() {
    return "popupMenuHide('" + id + "');";
  }

  /**
   * Retorna a representa��o JavaScript da fun��o para exibir o PopupMenu.
   * Exemplo:
   * <code>
   *   &lt;button onclick="&lt;%=popupMenu.showScript()%&gt;"&gt;Op��es&lt;/button&gt;
   * </code>
   * @return Retorna a representa��o JavaScript da fun��o para exibir o PopupMenu.
   */
  public String showScript() {
    return "popupMenuShow('" + id + "', '" + anchorId + "');";
  }

}
