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

import iobjects.*;
import iobjects.util.*;

/**
 * Representa um controle baseado em páginas. Com o PageControl é possível
 * distribuir os controles da interface em páginas cujo acesso é feito através
 * de guias nomeadas.
 * <p>
 *   Exemplo:
 * </p>
 * <pre>
 * &lt;%
 *   PageControl pageControl = new PageControl("pageControl", true); // cria o PageControl
 * %&gt;
 * &lt;!--aqui se inicia nosso PageControl--&gt;
 * &lt;%=pageControl.begin()%&gt;
 *     &lt;!--inicia a primeira guia--&gt;
 *     &lt;%=pageControl.beginTabSheet("Primeira")%&gt;
 *       &lt;h1&gt;Conteúdo da primeira guia&lt;/h1&gt;
 *     &lt;!--termina primeira guia--&gt;
 *     &lt;%=pageControl.endTabSheet()%&gt;
 *     &lt;%=pageControl.beginTabSheet("Segunda")%&gt;
 *       &lt;h1&gt;Conteúdo da segunda guia&lt;/h1&gt;
 *     &lt;%=pageControl.endTabSheet()%&gt;
 *     &lt;%=pageControl.beginTabSheet("Terceira")%&gt;
 *       &lt;h1&gt;Conteúdo da terceira guia&lt;/h1&gt;
 *     &lt;%=pageControl.endTabSheet()%&gt;
 * &lt;!--aqui termina nosso PageControl--&gt;
 * &lt;%=pageControl.end()%&gt;
 * </pre>
 */
public class PageControl {

  private boolean autoAlign  = false;
  private Facade  facade     = null;
  private int     height     = 400;
  private String  id         = "";
  private String  tabCaption = "";
  private int     tabCount   = 0;
  private int     tabWidth   = 90;
  private int     width      = 400;

  /**
   * Construtor padrão.
   * @param facade Facade Fachada.
   * @param id String Identificação do PageControl na página.
   * @param autoAlign true para que o PageControl ocupe toda área cliente do seu container.
   */
  public PageControl(Facade  facade,
                     String  id,
                     boolean autoAlign) {
    this(facade, id, 0, 0);
    // nossos valores
    this.autoAlign = autoAlign;
  }

  /**
   * Construtor estendido.
   * @param facade Facade Fachada.
   * @param id String Identificação do PageControl na página.
   * @param height int Altura do PageControl.
   * @param width int Largura do PageControl.
   */
  public PageControl(Facade facade,
                     String id,
                     int    height,
                     int    width) {
    // se não informou o Id...exceção
    if ((id == null) || id.trim().equals(""))
      throw new RuntimeException(new ExtendedException(getClass().getName(), "",  "Id não informado."));
    // nossos valores
    this.facade = facade;
    this.id     = id;
    this.height = height;
    this.width  = width;
  }


  /**
   * Retorna o script contendo a representação HTML que inicia o PageControl.
   * @return Retorna o script que contendo a representação HTML que inicia o
   *         PageControl.
   */
  public String begin() {
    String result = "";
    // nossas variáveis locais
    result += "<script type=\"text/javascript\">";
    result +=   "var " + id + "Id = \"" + id + "\"; ";
    result +=   "var " + id + "pagecontrolTabCount = 0; ";
    result +=   "var " + id + "pagecontrolTabSelected = 0; ";
    result += "</script>";
    // inicia o PageControl
    result += "<table id=\"" + id + "\" cellpadding=\"0\" cellspacing=\"0\" style=\"width:" + (autoAlign ? "100%" : width + "px") + "; height:" + (autoAlign ? "100%" : height + "px") + ";\">";
    // insere o TabBar
    result +=   "<tr><td id=\"" + id + "pagecontrolTabBar\" class=\"pagecontrolTabBar\" style=\"height:24px;\"></td></tr>";
    // inicia o Container
    result +=   "<tr><td id=\"" + id + "pagecontrolContainer\" class=\"pagecontrolContainer\" style=\"height:auto;\">";
    // retorna
    return result;
  }

  /**
   * Retorna o script para o início de um novo TabSheet.
   * @param caption Título do TabSheet.
   * @return Retorna o script para o início de um novo TabSheet.
   * @see PageControl#endTabSheet
   */
  public String beginTabSheet(String caption) {
    // guarda o caption
    tabCaption = caption;
    // incrementa a qde. de guias adicionadas
    tabCount++;
    // inicia o tab
    String result = "<div id=\"" + id + "pagecontrolTabPanelContainer" + (tabCount -1) + "\" class=\"pagecontrolTabPanelContainer\">";
    // retorna o início do tab
    return result;
  }

  /**
   * Retorna o script contendo a representação HTML que finaliza o PageControl.
   * @return Retorna o script contendo a representação HTML que finaliza o
   *         PageControl.
   */
  public String end() {
    String result = "";
    // termina o Container
    result +=   "</td></tr>";
    // termina o Ribbon
    result += "</table>";
    // retorna
    return result;
  }

  /**
   * Retorna o script para o término de um TabSheet.
   * @return Retorna o script para o término de um TabSheet.
   * @see PageControl#beginTabSheet
   */
  public String endTabSheet() {
    String result = "";
    // termina o tab
    result += "</div>";
    // script para inserir o tab em TabBar
    result += "<script type=\"text/javascript\">PageControl_CreateTab(" + id + "Id, \"" + tabCaption + "\", \"" + tabWidth + "px\");</script>";
    // retorna o resultado
    return result;
  }

  /**
   * Retorna true se o PageControl deve se ajustar à área cliente do seu container.
   * @return boolean Retorna true se o PageControl deve se ajustar à área cliente
   *         do seu container.
   */
  public boolean getAutoAlign() {
    return autoAlign;
  }

  /**
   * Retorna a altura do PageControl.
   * @return Retorna a altura do PageControl.
   */
  public int getHeight() {
    return height;
  }

  /**
   * Retorna a identificação do PageControl na página.
   * @return Retorna a identificação do PageControl na página.
   */
  public String getId() {
    return id;
  }

  /**
   * Retorna a largura de uma guia.
   * @return Retorna a largura de uma guia.
   */
  public int getTabWidth() {
    return  tabWidth;
  }

  /**
   * Retorna a largura do PageControl.
   * @return Retorna a largura do PageControl.
   */
  public int getWidth() {
    return width;
  }

  /**
   * Retorna o código JavaScript que oculta o TabSheet identificado por 'index'.
   * @param index Índice do TabSheet que se deseja ocultar.
   * @return Retorna o código JavaScript que oculta o TabSheet identificado por 'index'.
   */
  public String hideTabSheet(int index) {
    // retorna o resultado
    return "PageControl_HideTab(" + id + "Id, " + index + ");";
  }

  /**
   * Retorna o script para um novo TabSheet que contgendo um iFrame em seu
   * interior.
   * @param caption Título do TabSheet.
   * @param url String URL que será exibida no iFrame contido no TabSheet.
   * @return String Retorna o script para um novo TabSheet que contgendo um
   *         iFrame em seu interior.
   */
  public String iFrameTabSheet(String caption,
                               String url) {
    return iFrameTabSheet(caption, url, false);
  }

  /**
   * Retorna o script para um novo TabSheet que contgendo um iFrame em seu
   * interior.
   * @param caption Título do TabSheet.
   * @param url String URL que será exibida no iFrame contido no TabSheet.
   * @param scrolling boolean True para que as barras de rolagem possam ser
   *                  exibidas.
   * @return String Retorna o script para um novo TabSheet que contgendo um
   *         iFrame em seu interior.
   */
  public String iFrameTabSheet(String  caption,
                               String  url,
                               boolean scrolling) {
    // id da guia
    String iFrameId = id + "iframe" + (tabCount+1);
    // retorna
    return beginTabSheet(caption)
         +   "<iframe id=\"" + iFrameId + "\" src=\"" + url + "\" frameborder=\"0\" scrolling=\"" + (scrolling ? "auto" : "no") + "\" style=\"width:100%; height:100%;\"></iframe>"
         + endTabSheet();
  }

  /**
   * Define se o PageControl deve se ajustar à área cliente do seu container.
   * @param autoAlign boolean True para que o PageControl deve se ajustar à área
   *                  cliente do seu container.
   */
  public void setAutoAlign(boolean autoAlign) {
    this.autoAlign = autoAlign;
  }

  /**
   * Define a altura do PageControl.
   * @param value Altura do PageControl.
   */
  public void setHeight(int value) {
    this.height = value;
  }

  /**
   * Define a largura de uma guia.
   * @param value Largura de uma guia.
   */
  public void setTabWidth(int value) {
    this.tabWidth = value;
  }

  /**
   * Define a largura do PageControl.
   * @param value Largura do PageControl.
   */
  public void setWidth(int value) {
    this.width = value;
  }

  /**
   * Retorna o código JavaScript que mostra o TabSheet identificado por 'index'.
   * @param index Índice do TabSheet que se deseja mostra.
   * @return Retorna o código JavaScript que mostra o TabSheet identificado por 'index'.
   */
  public String showTabSheet(int index) {
    // retorna o resultado
    return "PageControl_ShowTab(" + id + "Id," + index + ");";
  }

}
