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
