package iobjects.ui;

import iobjects.*;
import iobjects.util.*;

/**
 * Representa um controle de navega��o baseado em p�ginas. Com o Ribbon � poss�vel
 * distribuir os controles de navega��o da aplica��o em p�ginas cujo acesso � 
 * feito atrav�s de guias nomeadas.
 * <p>
 *   Exemplo:
 * </p>
 * <pre>
 * &lt;%
 *   Ribbon ribbon = new Ribbon("Ribbon"); // cria o Ribbon
 * %&gt;
 * &lt;!--aqui se inicia nosso Ribbon--&gt;
 * &lt;%=Ribbon.begin()%&gt;
 *     &lt;!--inicia a primeira guia--&gt;
 *     &lt;%=Ribbon.beginTab("Primeira")%&gt;
 *       &lt;!--inicia um painel de controles--&gt;
 *       &lt;%=Ribbon.beginTabPanel("Controles")%&gt;
 *         &lt;h1&gt;Controles&lt;/h1&gt;
 *       &lt;%=Ribbon.endTabPanel()%&gt;
 *     &lt;!--termina primeira guia--&gt;
 *     &lt;%=Ribbon.endTab()%&gt;
 *     &lt;%=Ribbon.beginTab("Segunda")%&gt;
 *       &lt;%=Ribbon.beginTabPanel("Controles")%&gt;
 *         &lt;h1&gt;Controles&lt;/h1&gt;
 *       &lt;%=Ribbon.endTabPanel()%&gt;
 *     &lt;%=Ribbon.endTab()%&gt;
 * &lt;!--aqui termina nosso Ribbon--&gt;
 * &lt;%=Ribbon.end()%&gt;
 * </pre>
 */
public class Ribbon {

  static final public int ALIGN_LEFT  = Align.ALIGN_LEFT;
  static final public int ALIGN_RIGHT = Align.ALIGN_RIGHT;

  static final public int KIND_BIG_BUTTON  = 1;
  static final public int KIND_TOOL_BUTTON = 2;

  private Facade  facade           = null;
  private String  id               = "";
  private int     rewindTime       = 0;
  private int     tabCount         = 0;
  private String  tabCaption       = "";
  private int     tabPanelAlign    = ALIGN_LEFT;
  private String  tabPanelCaption  = "";

  /**
   * Construtor padr�o.
   * @param facade Facade Fachada.
   * @param id String Identifica��o do Ribbon na p�gina.
   */
  public Ribbon(Facade  facade,
                String  id) {
    // se n�o informou o Id...exce��o
    if ((id == null) || id.trim().equals(""))
      throw new RuntimeException(new ExtendedException(getClass().getName(), "",  "Id n�o informado."));
    // nossos valores
    this.facade = facade;
    this.id     = id;
  }

  /**
   * Retorna o script contendo a representa��o HTML que inicia o Ribbon.
   * @return Retorna o script que contendo a representa��o HTML que inicia o
   *         Ribbon.
   */
  public String begin() {
    String result = "";
    // nossas vari�veis locais
    result += "<script type=\"text/javascript\">";
    result +=   "var " + id + "ribbonRewindTime  = " + (rewindTime * 1000) + ";";
    result +=   "var " + id + "ribbonRewindTimer = 0;";
    result +=   "var " + id + "ribbonTabCount    = 0;";
    result +=   "var " + id + "ribbonTabSelected = 0;";
    result += "</script>";
    // inicia o Ribbon
    result += "<div id=\"" + id + "\" style=\"width:100%;\">";
    // insere o TabBar
    result +=   "<div id=\"" + id + "ribbonTabBar\" class=\"ribbonTabBar\"></div>";
    // inicia o Container
    result +=   "<div id=\"" + id + "ribbonContainer\" class=\"ribbonContainer\">";
    // retorna
    return result;
  }

  /**
   * Retorna o script para o in�cio de um novo Tab.
   * @param caption T�tulo do Tab.
   * @return Retorna o script para o in�cio de um novo Tab.
   * @see Ribbon#endTab
   */
  public String beginTab(String caption) {
    // traduz no dicion�rio
    tabCaption = facade.getDictionary().translate(caption);
    // incrementa a qde. de guias adicionadas
    tabCount++;
    // inicia o tab
    String result = "<div id=\"" + id + "ribbonTabPanelContainer" + (tabCount -1) + "\" class=\"ribbonTabPanelContainer\">";
    // retorna o in�cio do tab
    return result;
  }

  /**
   * Retorna o script para t�rmino de um Panel em um Tab.
   * @param caption String T�tulo do Panel.
   * @return Retorna o script para t�rmino de um Panel em um Tab.
   * @see Ribbon#endTabPanel
   */
  public String beginTabPanel(String caption) {
    return beginTabPanel(caption, 0, ALIGN_LEFT);
  }

  /**
   * Retorna o script para t�rmino de um Panel em um Tab.
   * @param caption String T�tulo do Panel.
   * @param width int Largura do painel ou 0 para ocupar todo espa�o dispon�vel.
   * @param align int Alinhamento do Panel.
   * @return Retorna o script para t�rmino de um Panel em um Tab.
   * @see Ribbon#endTabPanel
   */
  public String beginTabPanel(String caption,
                              int    width,
                              int    align) {
    String result = "";
    // guarda o caption
    tabPanelAlign = align;
    tabPanelCaption = facade.getDictionary().translate(caption);
    // inicia o painel
    result += "<table cellpadding=\"0\" cellspacing=\"0\" class=\"ribbonTabPanel\" style=\"float:" + (align == ALIGN_LEFT ? "left" : "right") + "; width:" + (width > 0 ? width + "px;" : "auto") + ";\">";
    result +=   "<tr>";
    // inicia a �rea cliente
    result +=     "<td class=\"ribbonTabPanelClient\">";
    // retorna o in�cio do painel
    return result;
  }

  /**
   * Retorna o script contendo a representa��o HTML do Button no Ribbon.
   * @param id String Identifa��o do Button na p�gina.
   * @param caption String T�tulo do Button.
   * @param description String Descri��o do Button.
   * @param image String Imagem do Button.
   * @param accessChar String Caractere que poder� ser utilizado junto a tecla
   *                          Alt para acesso r�pido ao Button.
   * @param style String Estilo extra para o elemento HTML do Button.
   * @param onClickScript String Script JavaScript para ser executado pelo Button.
   * @param popupMenu PopupMenu associado ao Button ou null caso n�o haja um.
   * @param disabled boolean True para que o Button seja desabilitado.
   * @return String Retorna o script contendo a representa��o HTML do
   *                Button no Ribbon.
   */
  public String button(String    id,
                       String    caption,
                       String    description,
                       String    image,
                       String    accessChar,
                       String    style,
                       String    onClickScript,
                       PopupMenu popupMenu,
                       int       kind,
                       boolean   disabled) {
    // se n�o informou o Id...exce��o
    if ((id == null) || id.trim().equals(""))
      throw new RuntimeException(new ExtendedException(getClass().getName(), "button", "Id n�o informado."));
    // traduz no dicion�rio
    caption     = facade.getDictionary().translate(caption);
    description = facade.getDictionary().translate(description);
    // nossa classe
    String className = (kind == KIND_BIG_BUTTON ? "ribbonBigButton" : "ribbonToolButton");
    // nosso estilo
    style = (kind == KIND_BIG_BUTTON ? "float:left; width:68px; height:63px;" : "height:16px;") + style;
    // retorna
    return "<div class=\"" + className + "\" " +
                "id=\"" + id + "\" " +
                "title=\"" + description + (!accessChar.equals("") ? " (Alt+" + accessChar.toUpperCase() + ")" : "") + "\" " +
                "accesskey=\"" + accessChar + "\" " +
                "style=\"" + style + "\" " +
                "onclick=\"" + (popupMenu != null ? popupMenu.showScript() : "") + onClickScript + "\" " +
                "onmousedown=\"" + (!disabled ? "this.className='" + className + "Down';\"" : "") + "\" " +
                "onmouseup=\"" + (popupMenu == null ? "this.className='" + className + "Over';" : "") + "\"" +
                "onmouseover=\"this.className='" + className + "Over'\" " +
                "onmouseout=\"var parent = document.elementFromPoint(event.clientX, event.clientY); while (parent != null) {if (parent == this) return; parent = parent.offsetParent;} this.className='" + className + "'; " + (popupMenu != null ? popupMenu.hideScript() : "") + "\" " +
                (disabled ? "disabled=\"disabled\"" : "") + ">" +
            (!image.equals("") ? "<img src=\"" + image + "\" align=\"absmiddle\" />" : "") +
            (kind == KIND_BIG_BUTTON
                   ? (!image.equals("") && !caption.equals("") ? "<br />" : "")
                   : (!image.equals("") && !caption.equals("") ? "&nbsp;" : "")) +
            caption +
            (popupMenu != null ? "<img src=\"images/menu8x8.png\" align=\"absmiddle\" />" : "") +
            "</div>";
  }
  
  /**
   * Retorna o script contendo a representa��o HTML que finaliza o Ribbon.
   * @return Retorna o script contendo a representa��o HTML que finaliza o
   *         Ribbon.
   */
  public String end() {
    String result = "";
    // termina o Container
    result +=   "</div>";
    // termina o Ribbon
    result += "</div>";
    // retorna
    return result;
  }

  /**
   * Retorna o script para o t�rmino de um Tab.
   * @return Retorna o script para o t�rmino de um Tab.
   * @see Ribbon#beginTab
   */
  public String endTab() {
    String result = "";
    // termina o tab
    result += "</div>";
    // script para inserir o tab em TabBar
    result += "<script type=\"text/javascript\">ribbonCreateTab(\"" + id + "\", \"" + tabCaption + "\");</script>";
    // retorna o resultado
    return result;
  }

  /**
   * Retorna o script para o t�rmino de um novo Panel no Tab que foi iniciado.
   * @param caption T�tulo do Tab.
   * @return Retorna o script para o in�cio de um novo Panel no Tab que foi iniciado.
   * @see Ribbon#endTab
   */
  public String endTabPanel() {
    String result = "";
    // termina a �rea cliente
    result +=     "</td>";
    result +=   "</tr>";
    // t�tulo do painel
    result +=   "<tr>";
    result +=     "<td class=\"ribbonTabPanelTitle\">" + tabPanelCaption + "</td";
    result +=   "</tr>";
    // termina o painel
    result += "</table>";
    // divisor
    result += "<table cellpadding=\"0\" cellspacing=\"0\" class=\"ribbonTabPanelDivisor\" style=\"float:" + (tabPanelAlign == ALIGN_LEFT ? "left" : "right") + "; table-layout:fixed;\">";
    result +=   "<tr><td></td></tr>";
    result += "</table>";
    // retorna o in�cio do painel
    return result;
  }

  /**
   * Retorna a identifica��o do Ribbon na p�gina.
   * @return Retorna a identifica��o do Ribbon na p�gina.
   */
  public String getId() {
    return id;
  }

  /**
   * Retorna a quantidade de guias mantidas pelo Ribbon.
   * @return Retorna a quantidade de guias mantidas pelo Ribbon.
   */
  public int getTabCount() {
    return tabCount;
  }

  public int getRewindTime() {
    return rewindTime;
  }

  /**
   * Define o tempo em segundos para que o Ribbon retorne a guia inicial.
   * @param rewindTime Informe 0 (zero) para que n�o haja retorno autom�tico.
   */
  public void setRewindTime(int rewindTime) {
    this.rewindTime = rewindTime;
  }

}
