package iobjects.ui;

import iobjects.*;
import iobjects.util.*;

/**
 * Representa um controle de navegação baseado em páginas. Com o Ribbon é possível
 * distribuir os controles de navegação da aplicação em páginas cujo acesso é 
 * feito através de guias nomeadas.
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
   * Construtor padrão.
   * @param facade Facade Fachada.
   * @param id String Identificação do Ribbon na página.
   */
  public Ribbon(Facade  facade,
                String  id) {
    // se não informou o Id...exceção
    if ((id == null) || id.trim().equals(""))
      throw new RuntimeException(new ExtendedException(getClass().getName(), "",  "Id não informado."));
    // nossos valores
    this.facade = facade;
    this.id     = id;
  }

  /**
   * Retorna o script contendo a representação HTML que inicia o Ribbon.
   * @return Retorna o script que contendo a representação HTML que inicia o
   *         Ribbon.
   */
  public String begin() {
    String result = "";
    // nossas variáveis locais
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
   * Retorna o script para o início de um novo Tab.
   * @param caption Título do Tab.
   * @return Retorna o script para o início de um novo Tab.
   * @see Ribbon#endTab
   */
  public String beginTab(String caption) {
    // traduz no dicionário
    tabCaption = facade.getDictionary().translate(caption);
    // incrementa a qde. de guias adicionadas
    tabCount++;
    // inicia o tab
    String result = "<div id=\"" + id + "ribbonTabPanelContainer" + (tabCount -1) + "\" class=\"ribbonTabPanelContainer\">";
    // retorna o início do tab
    return result;
  }

  /**
   * Retorna o script para término de um Panel em um Tab.
   * @param caption String Título do Panel.
   * @return Retorna o script para término de um Panel em um Tab.
   * @see Ribbon#endTabPanel
   */
  public String beginTabPanel(String caption) {
    return beginTabPanel(caption, 0, ALIGN_LEFT);
  }

  /**
   * Retorna o script para término de um Panel em um Tab.
   * @param caption String Título do Panel.
   * @param width int Largura do painel ou 0 para ocupar todo espaço disponível.
   * @param align int Alinhamento do Panel.
   * @return Retorna o script para término de um Panel em um Tab.
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
    // inicia a área cliente
    result +=     "<td class=\"ribbonTabPanelClient\">";
    // retorna o início do painel
    return result;
  }

  /**
   * Retorna o script contendo a representação HTML do Button no Ribbon.
   * @param id String Identifação do Button na página.
   * @param caption String Título do Button.
   * @param description String Descrição do Button.
   * @param image String Imagem do Button.
   * @param accessChar String Caractere que poderá ser utilizado junto a tecla
   *                          Alt para acesso rápido ao Button.
   * @param style String Estilo extra para o elemento HTML do Button.
   * @param onClickScript String Script JavaScript para ser executado pelo Button.
   * @param popupMenu PopupMenu associado ao Button ou null caso não haja um.
   * @param disabled boolean True para que o Button seja desabilitado.
   * @return String Retorna o script contendo a representação HTML do
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
    // se não informou o Id...exceção
    if ((id == null) || id.trim().equals(""))
      throw new RuntimeException(new ExtendedException(getClass().getName(), "button", "Id não informado."));
    // traduz no dicionário
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
   * Retorna o script contendo a representação HTML que finaliza o Ribbon.
   * @return Retorna o script contendo a representação HTML que finaliza o
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
   * Retorna o script para o término de um Tab.
   * @return Retorna o script para o término de um Tab.
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
   * Retorna o script para o término de um novo Panel no Tab que foi iniciado.
   * @param caption Título do Tab.
   * @return Retorna o script para o início de um novo Panel no Tab que foi iniciado.
   * @see Ribbon#endTab
   */
  public String endTabPanel() {
    String result = "";
    // termina a área cliente
    result +=     "</td>";
    result +=   "</tr>";
    // título do painel
    result +=   "<tr>";
    result +=     "<td class=\"ribbonTabPanelTitle\">" + tabPanelCaption + "</td";
    result +=   "</tr>";
    // termina o painel
    result += "</table>";
    // divisor
    result += "<table cellpadding=\"0\" cellspacing=\"0\" class=\"ribbonTabPanelDivisor\" style=\"float:" + (tabPanelAlign == ALIGN_LEFT ? "left" : "right") + "; table-layout:fixed;\">";
    result +=   "<tr><td></td></tr>";
    result += "</table>";
    // retorna o início do painel
    return result;
  }

  /**
   * Retorna a identificação do Ribbon na página.
   * @return Retorna a identificação do Ribbon na página.
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
   * @param rewindTime Informe 0 (zero) para que não haja retorno automático.
   */
  public void setRewindTime(int rewindTime) {
    this.rewindTime = rewindTime;
  }

}
