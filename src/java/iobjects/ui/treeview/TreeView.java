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
package iobjects.ui.treeview;

import iobjects.*;
import iobjects.util.*;

/**
 * Representa um TreeView para ser usado em uma interface HTML.
 */
public class TreeView {

  private   boolean      autoAlign       = false;
  private   boolean      avoidWaitCursor = false;
  private   StringBuffer buffer          = new StringBuffer();
  private   String       checkBoxId      = "";
  private   Facade       facade          = null;
  private   String       folder          = "images/treeview";
  private   int          height          = 400;
  private   String       id              = "";
  protected NodeList     nodes           = new NodeList();
  private   String       target          = "";
  private   int          width           = 400;

  /**
   * Construtor padrão.
   */
  public TreeView() {
  }

  /**
   * Construtor estendido.
   * @param facade Facade Fachada.
   * @param id String Identificação do TreeView na página.
   * @param autoAlign true para que o TreeView ocupe toda área cliente do seu container.
   */
  public TreeView(Facade  facade,
                  String  id,
                  boolean autoAlign) {
    this.facade = facade;
    this.id = id;
    this.autoAlign = autoAlign;
  }

  /**
   * Construtor estendido.
   * @param id String Identificação do TreeView na página.
   * @param height int Altura do TreeView.
   * @param width int Largura do TreeView.
   */
  public TreeView(String  id,
                  int     height,
                  int     width) {
    this.id = id;
    this.height = height;
    this.width = width;
  }

  /**
   * Adiciona um nó à raiz do TreeView.
   * @param node Node Nó para ser adicionado.
   */
  public void add(Node node) {
    // adiciona o nó
    nodes.add(node);
  }

  /**
   * Limpa o buffer utilizado para guardar o último Script gerado.
   */
  public void clearBuffer() {
    buffer = new StringBuffer();
  }

  /**
   * Retorna true se o cursor de espera deve ser evitado quando um hyperlink for
   * executado.
   * @return boolean Retorna true se o cursor de espera deve ser evitado quando
   *                 um hyperlink for executado.
   */
  public boolean getAvoidWaitCursor() {
    return avoidWaitCursor;
  }

  /**
   * Retorna true se o TreeView deve se ajustar à área cliente do seu container.
   * @return boolean Retorna true se o TreeView deve se ajustar à área cliente
   *         do seu container.
   */
  public boolean getAutoAlign() {
    return autoAlign;
  }

  /**
   * Retorna a identificação usada para os CheckBoxes do TreeView.
   * @return String Retorna a identificação usada para os CheckBoxes do TreeView.
   */
  public String getCheckBoxId() {
    return checkBoxId;
  }

  public Facade getFacade() {
    return facade;
  }

  /**
   * Retorna a altura do TreeView.
   * @return Retorna a altura do TreeView.
   */
  public int getHeight() {
    return height;
  }

  /**
   * Retorna a identificação do TreeView na página.
   * @return String Retorna a identificação do TreeView na página.
   */
  public String getId() {
    return id;
  }

  /**
   * Retorna a largura do TreeView.
   * @return Retorna a largura do TreeView.
   */
  public int getWidth() {
    return width;
  }

  /**
   * Realiza o loop na lista de nós informadas criando seu Script.
   * @param nodeList NodeList Lista de nós para efetuar o loop.
   * @param parent String Realiza o loop na lista de nós informadas criando seu Script.
   */
  private void loopThru(NodeList nodeList, String parent) {
    boolean hasChild = false;
    boolean hasImage = false;
    String  style    = "";
    // se temos um pai...
    if (!parent.equals("0"))
      print("<ul class=tree id=\"N" + parent + "\">");
    // se não temos...
    else
      print("<ul id=\"N" + parent + "\">");
    // loop nos itens
    for (int i=0; i<nodeList.length(); i++) {
      // nó da vez
      Node node = nodeList.item(i);
      // texto do nó
      String nodeText = node.text;
      // descrição do nó
      String nodeToolTip = node.toolTip;
      // temos filhos?
      hasChild = node.childNodes.length() > 0;
      // temos imagem?
      hasImage = !node.imageUrl.equals("");
      // se informou uma imagem...
      if (hasImage)
        style = "list-style-image: url("+ node.imageUrl +");";
      // se temos filhos...
      if (hasChild) {
        // script do nó
        print("<li style=\"" + style + "\"" +
              "    class=folder id='P" + parent + i + "'>" +
              (!checkBoxId.equals("") && !node.value.equals("") ?
                "<input type=\"checkbox\" id=\"checkbox" + parent + "_" + i + "\" onclick=\"checkChildren(this);\" name=\"" + checkBoxId + "\" value=\"" + node.value + "\" " + (node.checked ? "checked" : "") + ">"
               :
               "") +
              "    <a class=treeview " +
              (hasImage ?
               "href=\"javascript:toggleWithImage('N" + parent + "_" + i + "', 'P" + parent + i + "', '" + node.imageUrl + "', '" +
               node.imageOpenedUrl + "')\">"
               :
               "href=\"javascript:toggle('N" + parent + "_" + i + "', 'P" + parent + i + "')\">"
               ) +
              nodeText +
              (!node.extraImageUrl.equals("") ? "<img src=\"" + node.extraImageUrl + "\">" : "") +
              "    </a>");
        // loop nos filhos
        loopThru(node.childNodes,parent + "_" + i);
        // termina o nó
        print("</li>");
        // script para ler o estado do nó
        print("<script>" +
              "if (" + id + "State.indexOf('N" + parent + "_" + i + "_P" + parent + i + "') >= 0) " +
              (hasImage ?
               "toggleWithImage('N" + parent + "_" + i + "', 'P" + parent + i + "', '" + node.imageUrl + "', '" + node.imageOpenedUrl + "')"
               :
               "toggle('N" + parent + "_" + i + "', 'P" + parent + i + "');") +
               "</script>"
             );
      }
      // se não temos filhos...
      else {
        // se o nó não tem um target...usa o target do Treeview
        if (node.target.equals(""))
          node.target = target;
        // script do nó
        print("<li style=\""+ style +"\" class=file>");
        // usaremos CheckBox?
        if (!checkBoxId.equals("") && !node.value.equals(""))
          print("<input type=\"checkbox\" id=\"checkbox" + parent + "_" + i + "\" onclick=\"checkChildren(this);\" name=\"" + checkBoxId + "\" value=\"" + node.value + "\" " + (node.checked ? "checked" : "") + "><span>" + nodeText + (!node.extraImageUrl.equals("") ? "<img src=\"" + node.extraImageUrl + "\">" : "") + "</span>");
        // usaremos link
        else
          print("<a class=treeview href=\"" + node.href + "\" " + (((node.href.indexOf("javascript") < 0) || node.target.equals("")) && !avoidWaitCursor ? "onClick=\"treeViewWait();\"" : "") + " target=\"" + node.target + "\" title=\"" + nodeToolTip + "\">" + nodeText + (!node.extraImageUrl.equals("") ? "<img src=\"" + node.extraImageUrl + "\">" : "") + "</a>");
        // termina o nó
        print("</li>");
      } // if
    } // for
    // termina a raiz
    print("</ul>");
  }

  /**
   * Adiciona o texto informado ao buffer do Script.
   * @param text String Texto para ser adicionado ao buffer do script.
   */
  private void print(String text) {
    buffer.append(text);
  }

  /**
   * Retorna o Script que representa o TreeView.
   * @return String Retorna o Script que representa o TreeView.
   */
  public String script() {
    // se não mudamos a quantidade de nós...retorna o Script atual
    if (buffer.length() > 0)
      return buffer.toString();

//

    // gerando...
    print("<script> \r" +
          "function treeViewWait() {" +
          "  document.body.style.cursor='wait';" +
          "}" +
          "function treeViewReadCookieValue(name) { \r" +
          "  if (document.cookie.length == 0) \r" +
          "    return null; \r" +
          "  begin = document.cookie.indexOf(name + '='); \r" +
          "  if (begin < 0) \r" +
          "    return null; \r" +
          "  begin += name.length + 1; \r" +
          "  end = document.cookie.indexOf(';', begin); \r" +
          "  if (end == -1) \r" +
          "    end = document.cookie.length; \r" +
          "  return document.cookie.substring(begin, end); \r" +
          "} \r" +
          "function treeViewReadCookieDefaultValue(name, defaultValue) { \r" +
          "  result = treeViewReadCookieValue(name); \r" +
          "  if (result == null) \r" +
          "    return defaultValue; \r" +
          "  else \r" +
          "    return result; \r" +
          "} \r" +
          "function treeViewWriteSecureCookieValue(name, value) { \r" +
          "  var argv = treeViewWriteSecureCookieValue.arguments; \r" +
          "  var argc = treeViewWriteSecureCookieValue.arguments.length; \r" +
          "  var expires = (argc > 2) ? argv[2] : null; \r" +
          "  var path = '/'; \r" +
          "  var domain = (argc > 4) ? argv[4] : null; \r" +
          "  var secure = (argc > 5) ? argv[5] : false; \r" +
          "  document.cookie = name + '=' + escape (value) + \r" +
          "  ((expires == null) ? '' : ('; expires=' + expires.toGMTString())) + \r" +
          "  ((path == null) ? '' : ('; path=' + path)) + \r" +
          "  ((domain == null) ? '' : ('; domain=' + domain)) + \r" +
          "  ((secure == true) ? '; secure' : ''); \r" +
          "} \r" +
          "var " + id + "State = treeViewReadCookieDefaultValue(\"" + id + "\", \"\"); \r" +
          "function saveTreeViewState(node, nodeState) { \r" +
          "  var state = treeViewReadCookieDefaultValue(\"" + id + "\", \"\"); \r" +
          "  if (nodeState == \"block\") { \r" +
          "    if (state.indexOf(node) >= 0) \r" +
          "      return; \r" +
          "    state += node + \".\"; \r" +
          "  } \r" +
          "  else { \r" +
          "    if (state.indexOf(node) < 0) \r" +
          "      return; \r" +
          "    var stateArray = state.split(\".\"); \r" +
          "    state = \"\"; \r" +
          "    for (i=0; i<stateArray.length; i++) { \r" +
          "      var temp = stateArray[i]; \r" +
          "      if (temp != node) \r" +
          "        state += temp + \".\"; \r" +
          "    } // for \r" +
          "  } // if \r" +
          "  treeViewWriteSecureCookieValue(\"" + id + "\", state); \r" +
          "} \r" +
          "  \r" +
          "function checkChildren(checkbox) {  \r" +
          "  for (var i=0; true; i++) {\r" +
          "    var checkboxChild = document.getElementById(checkbox.id + '_' + i); \r" +
          "    if (checkboxChild == null) \r" +
          "      break; \r" +
          "    else { \r" +
          "      checkboxChild.checked = checkbox.checked; \r" +
          "      checkChildren(checkboxChild); \r" +
          "    } \r" +
          "  } \r" +
          "}  \r" +
          "  \r" +
          "function toggle(id, p) {\r" +
          "  var myChild = document.getElementById(id);\r" +
          "  if (myChild.style.display != 'block') {\r" +
          "    myChild.style.display = 'block';\r" +
          "    document.getElementById(p).className = 'folderOpen';\r" +
          "  }\r" +
          "  else {\r" +
          "    myChild.style.display = 'none';\r" +
          "    document.getElementById(p).className = 'folder';\r" +
          "  } // if\r" +
          "  saveTreeViewState(id + \"_\" + p, myChild.style.display);\r" +
          "}\r" +
          "function toggleWithImage(id, p, imageUrl, imageOpenedUrl) {\r" +
          "  toggle(id, p);\r" +
          "  var myChild = document.getElementById(id);\r" +
          "  if (myChild.style.display != 'block') {\r" +
          "    document.getElementById(p).style.listStyleImage = 'url(' + imageUrl + ')';\r" +
          "  }\r" +
          "  else {\r" +
          "    document.getElementById(p).style.listStyleImage = 'url(' + imageOpenedUrl + ')';\r" +
          "  } // if\r" +
          "}\r" +
          "</script>\r");
    print("<style>" +
          "  ul {margin-left:30px;}" +
          "  ul.tree {display:none; margin-left:15px;}" +
          "  li.folder {list-style-image: url("+ folder +"/folderclosed.png);}" +
          "  li.folderOpen {list-style-image: url("+ folder +"/folderopened.png);}" +
          "  li.file {list-style-image: url("+ folder +"/unknown.png);}" +
          "  a.treeview {}" +
          "  a.treeview:link {text-decoration:none;}" +
          "  a.treeview:visited {text-decoration:none;}" +
          "  a.treeview:hover {text-decoration:underline;}" +
          "</style>");

    // adiciona os nós
    print("<div id=\"" + id + "\" class=\"TreeViewPanel\" style=\"width:" + (autoAlign ? "100%;" : width + "px;") + " height:" + (autoAlign ? "100%;" : height + "px;") + " overflow:scroll;\">");
    print("  <table class=\"TreeView\" style=\"layout:fixed;\">");
    print("    <tr>");
    print("      <td valign=\"top\" nowrap=\"nowrap\">");
    loopThru(nodes, "0");
    print("      </td>");
    print("    </tr>");
    print("  </table>");
    print("</div");
    // retorna
    return buffer.toString();
  }

  /**
   * Define se o cursor de espera deve ser evitado quando um hyperlink for
   * executado.
   * @param avoidWaitCursor boolean True se o cursor de espera deve ser evitado
   *                        quando um hyperlink for executado.
   */
  public void setAvoidWaitCursor(boolean avoidWaitCursor) {
    this.avoidWaitCursor = avoidWaitCursor;
  }

  /**
   * Define se o TreeView deve se ajustar à área cliente do seu container.
   * @param autoAlign boolean True para que o TreeView deve se ajustar à área
   *                  cliente do seu container.
   */
  public void setAutoAlign(boolean autoAlign) {
    this.autoAlign = autoAlign;
  }

  /**
   * Define a identificação usada para os CheckBoxes do TreeView. Se este valor
   * for informado o TreeView exibirá CheckBoxes ao lado dos nós que não possuírem
   * filhos.
   * @param checkBoxId String Identificação usada para os CheckBoxes do TreeView.
   */
  public void setCheckBoxId(String checkBoxId) {
    this.checkBoxId = checkBoxId;
  }

  public void setFacade(Facade facade) {
    this.facade = facade;
  }

  /**
   * Define a altura do TreeView.
   * @param value Altura do TreeView.
   */
  public void setHeight(int value) {
    this.height = value;
  }

  /**
   * Define a identificação do TreeView na página HTML.
   * @param id String Identificação do TreeView na página HTML.
   */
  public void setId(String id) {
    this.id = id;
  }

  /**
   * Define a URL onde se encontram as imagens do TreeView.
   * @param url String URL onde se encontram as imagens do TreeView.
   */
  public void setImagesUrl(String url) {
    this.folder = url;
  }

  /**
   * Define o destino dos hyperlinks do TreeView.
   * @param target String Destino dos hyperlinks do TreeView.
   */
  public void setTarget(String target) {
    this.target = target;
  }

  /**
   * Define a largura do TreeView.
   * @param value Largura do TreeView.
   */
  public void setWidth(int value) {
    this.width = value;
  }

}
