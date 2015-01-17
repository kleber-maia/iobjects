package iobjects.ui;

/**
 * Utilitário para criação de menus suspensos contendo opções para o usuário. 
 */
public class PopupMenu {

  private String anchorId = "";
  private String id       = "";

  /**
   * Construtor padrão.
   * @param id String Identificação do PopupMenu na página.
   * @param anchorId String Identificação do objeto que servirá como âncora para
   *        o PopupMenu na página. O PopupMenu será exibido imediatamente abaixo
   *        ou acima deste objeto, dependendo do sey tamanho e do espaço 
   *        disponível na página.
   */
  public PopupMenu(String id,
                   String anchorId) {
    // nossos valores
    this.id       = id;
    this.anchorId = anchorId;
  }

  /**
   * Retorna a representação HTML do início do PopupMenu.
   * @return String Retorna a representação HTML do início do PopupMenu.
   */
  public String begin() {
    String result = "";
    // nosso container
    result += "<div id=\"" + id + "\" class=\"popupMenu\" style=\"position:absolute; padding:4px; visibility:hidden; overflow-y:auto;\" onmouseover=\"this.style.visibility = 'visible';\" onmouseout=\"this.style.visibility = 'hidden';\">";
    // retorna
    return result;
  }

  /**
   * Retorna a representação HTML do término do PopupMenu.
   * @return String Retorna a representação HTML do término do PopupMenu.
   */
  public String end() {
    return "</div>";
  }

  /**
   * Retorna a representação JavaScript da função para ocultar o PopupMenu.
   * Exemplo:
   * <code>
   *   &lt;button onclick="&lt;%=popupMenu.hideScript()%&gt;"&gt;Opções&lt;/button&gt;
   * </code>
   * @return Retorna a representação JavaScript da função para ocultar o PopupMenu.
   */
  public String hideScript() {
    return "popupMenuHide('" + id + "');";
  }

  /**
   * Retorna a representação JavaScript da função para exibir o PopupMenu.
   * Exemplo:
   * <code>
   *   &lt;button onclick="&lt;%=popupMenu.showScript()%&gt;"&gt;Opções&lt;/button&gt;
   * </code>
   * @return Retorna a representação JavaScript da função para exibir o PopupMenu.
   */
  public String showScript() {
    return "popupMenuShow('" + id + "', '" + anchorId + "');";
  }

}
