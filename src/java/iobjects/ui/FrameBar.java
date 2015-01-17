package iobjects.ui;

import iobjects.*;
import iobjects.ui.help.*;
import iobjects.util.*;

/**
 * Representa um controle baseado em menus no estilo frame. Com o FrameBar é
 * possível criar uma série de menus laterais e distribuir o resto do conteúdo
 * na área cliente restante.
 * @since 3.1
 * <p>
 *   Exemplo:
 * </p>
 * <pre>
 * &lt;%
 *   FrameBar frameBar = new FrameBar("frameBar", 200); // cria o FrameBar
 * %&gt;
 * &lt;!--aqui se inicia nosso FrameBar--&gt;
 * &lt;%=frameBar.begin()%&gt;
 *   &lt;!--inicia a área de frames--&gt;
 *   &lt;%=frameBar.beginFrameArea()%&gt;
 *     &lt;!--inicia o primeiro Frame--&gt;
 *     &lt;%=frameBar.beginFrame("Principal", true)%&gt;
 *       Conteúdo do Frame principal
 *     &lt;!--termina o Frame--&gt;
 *     &lt;%=frameBar.endFrame()%&gt;
 *     &lt;%=frameBar.beginFrame("Outro Frame")%&gt;
 *       Conteúdo do outro Frame
 *     &lt;%=frameBar.endFrame()%&gt;
 *   &lt;!--termina a área de frames--&gt;
 *   &lt;%=frameBar.endFrameArea()%&gt;
 *   &lt;!--inicia a área cliente--&gt;
 *   &lt;%=frameBar.beginClientArea()%&gt;
 *     Conteúdo da área cliente
 *   &lt;!--termina a área cliente--&gt;
 *   &lt;%=frameBar.endClientArea()%&gt;
 * &lt;!--aqui termina nosso FrameBar--&gt;
 * &lt;%=frameBar.end()%&gt;
 * </pre>
 */
public class FrameBar {

  static public final int CLIENT_AREA_OFFSET = 4;
  static public final int FRAME_BAR_OFFSET   = 2;
  static public final int FRAME_OFFSET       = 4;

  private Facade  facade                 = null;
  private int     frameCount             = 0;
  private String  id                     = "";
  private int     width                  = 200;
  private boolean clientAreaInserted     = false;
  private boolean frameBarInserted       = false;

  /**
   * Construtor padrão.
   * @param facade Facade Fachada.
   * @param id String Identificação do FrameBar na página.
   */
  public FrameBar(Facade facade,
                  String id) {
    this(facade, id, 200);
  }

  /**
   * Construtor estendido.
   * @param facade Facade Fachada.
   * @param id String Identificação do FrameBar na página.
   * @param width int Largura do FrameBar na página.
   */
  public FrameBar(Facade facade,
                  String  id,
                  int     width) {
    // se não informou o Id...exceção
    if ((id == null) || id.trim().equals(""))
      throw new RuntimeException(new ExtendedException(getClass().getName(), "", "Id não informado."));
    // nossos valores
    this.facade = facade;
    this.id = id;
    this.width = width;
  }

  public String actionFrame(Action action) {
    return actionFrame(action, null);
  }

  /**
   * Retorna o script contendo a representação HTML um Frame completo com a
   * identificação de um objeto de negócio a partir do seu 'action' informado.
   * @param action Action de identificação do objeto de negócio.
   * @param balloon Balloon que contém o roteiro de apresentação da tela
   *                para o usuário.
   * @return String Retorna o script contendo a representação HTML um Frame
   *                completo com a identificação de um objeto de negócio a
   *                partir do seu 'action' informado.
   */
  public String actionFrame(Action action, Balloon balloon) {

    // a implementaçao deste método deve ser substituída pela chamada do
    // método beginFrame atualizada após a inclusão do parâmetro opcional
    // de inclusão de um botão de comando para suportar o atual botão de ajuda

    // qde máxima de caracteres para o caption
    int    borderLeft  = 22;
    int    borderRight = 38;
    int    maxChars = (width - (borderLeft + borderRight)) / 6;
    String caption  = action.getCaption();
    if (caption.length() > maxChars)
      caption = caption.substring(0, maxChars-3) + "...";
    // traduz no dicionário de sinônimos
    String description = action.getDescription();

    frameCount++;
    boolean isMain = true;
    boolean ignoreSpace = false;
    int     buttonCount = 1 // help
          + (balloon != null ? 1 : 0);
    return /* ((frameCount > 1) && !ignoreSpace ? "<br />" : "") // a versão atual exibe os Frames colados */
           "<table class=\"" + (isMain ? "MainFrame" : "Frame") + "\" style=\"width:100%;\" cellpadding=\"" + FRAME_OFFSET + "\" cellspacing=\"0\">"
         +   "<tr>"
         +     "<td class=\"" + (isMain ? "MainFrame" : "Frame") + "Caption\" nowrap=\"nowrap\" style=\"width:100%; height:25px;\">"
         +        caption
         +       (action.getJustReleased()
                   ? "<img alt=\"Novidade, consulte a Ajuda.\" src=\"images/release16x16.png\" align=\"absmiddle\" />"
                   : "")
         +       (action.getBeta()
                   ? "<img alt=\"Beta, consulte a Ajuda.\" src=\"images/beta16x16.png\" align=\"absmiddle\" />"
                   : "")
         +     "</td>"
         +     "<td class=\"" + (isMain ? "MainFrame" : "Frame") + "Caption\" nowrap=\"nowrap\" style=\"border-left:none; padding:0px; width:" + (buttonCount * 25) + "px; height:25px;\">"
         +        HelpButton.script(facade, action, false, HelpButton.KIND_TOOLBUTTON)
         +        (balloon != null
                    ? balloon.scriptButton()
                    : "")
         +     "</td>"
         +   "</tr>"
         +   "<tr>"
         +     "<td class=\"" + (isMain ? "MainFrame" : "Frame") + "Body\" colspan=\"4\">"
         +       "<table style=\"width:100%;\">"
         +         "<tr>"
         +           "<td>"
         +             description
         + endFrame();
  }

  /**
   * Retorna o script contendo a representação HTML que inicia o container do
   * FrameBar.
   * @return Retorna o script que contendo a representação HTML que inicia o
   *         container do FrameBar.
   */
  public String begin() {
    // inicia o container do FrameBar e da área cliente
    frameCount = 0;
    return "<table cellpadding=\"0\" cellspacing=\"0\" style=\"width:100%; height:100%; table-layout:fixed;\">"
          +   "<tr>";
  }

  /**
   * Retorna o script contendo a representação HTML que inicia a área cliente
   * lateral ao FrameBar.
   * @return String Retorna o script contendo a representação HTML que inicia a
   *                área cliente lateral ao FrameBar.
   */
  public String beginClientArea() {
    // avisa que já inserimos a ClientArea
    clientAreaInserted = true;
    return "<td class=\"FrameBar\" valign=\"top\" style=\"width:auto; padding:" + CLIENT_AREA_OFFSET + "px;" + (frameBarInserted ? "padding-left:0px;" : "") + "\">"
        +    "<table class=\"FrameBarClientArea\" style=\"width:100%; height:100%;\">"
        +      "<tr>"
        +        "<td valign=\"top\">";
  }

  /**
   * Retorna o script contendo a representação HTML que inicia um novo Frame
   * no FrameBar.
   * @param caption String Título do novo Frame.
   * @param isMain boolean True se o novo Frame é o Frame principal.
   * @return String Retorna o script contendo a representação HTML que inicia um
   *                novo Frame no FrameBar.
   */
  public String beginFrame(String  caption,
                           boolean isMain) {
    return beginFrame(caption, isMain, false, new String[]{});
  }

  /**
   * Retorna o script contendo a representação HTML que inicia um novo Frame
   * no FrameBar.
   * @param caption String Título do novo Frame.
   * @param isMain boolean True se o novo Frame é o Frame principal.
   * @param ignoreSpace boolean True para que o espaço do novo Frame para o anterior
   *                    seja ignorado.
   * @return String Retorna o script contendo a representação HTML que inicia um
   *                novo Frame no FrameBar.
   */
  public String beginFrame(String   caption,
                           boolean  isMain,
                           boolean  ignoreSpace) {
    return beginFrame(caption, isMain, ignoreSpace, new String[]{});
  }

  /**
   * Retorna o script contendo a representação HTML que inicia um novo Frame
   * no FrameBar.
   * @param caption String Título do novo Frame.
   * @param isMain boolean True se o novo Frame é o Frame principal.
   * @param ignoreSpace boolean True para que o espaço do novo Frame para o anterior
   *                    seja ignorado.
   * @param toolButtons String[] contendo os scripts HTML de Buttons com do tipo
   *                    ToolButton para serem adicionados a barra de título do
   *                    Frame. Os Button devem ter ser estilo modificado para
   *                    possuírem as dimensões de 24x24 pixels.
   * @return String Retorna o script contendo a representação HTML que inicia um
   *                novo Frame no FrameBar.
   */
  public String beginFrame(String   caption,
                           boolean  isMain,
                           boolean  ignoreSpace,
                           String[] toolButtons) {
    // scripts dos ToolButtons
    StringBuffer toolButtonsScripts = new StringBuffer();
    for (int i=0; i<toolButtons.length; i++)
      toolButtonsScripts.append(toolButtons[i]);
    // inicia o Frame
    frameCount++;
    return /* ((frameCount > 1) && !ignoreSpace ? "<br />" : "") // a versão atual exibe os Frames colados */
           "<table class=\"" + (isMain ? "MainFrame" : "Frame") + "\" style=\"width:100%;\" cellpadding=\"" + FRAME_OFFSET + "\" cellspacing=\"0\">"
         +   "<tr>"
         +     "<td class=\"" + (isMain ? "MainFrame" : "Frame") + "Caption\" nowrap=\"nowrap\" style=\"width:100%; height:25px; " + (frameBarInserted ? "border-top:none;" : "") + "\">" + caption + "</td>"
               + (toolButtons.length > 0
                 ? "<td class=\"" + (isMain ? "MainFrame" : "Frame") + "Caption\" nowrap=\"nowrap\" style=\"border-left:none; padding:0px; width:" + (toolButtons.length * 25) + "px; height:25px;\">"
                   + toolButtonsScripts.toString()
                 : "")
         +     "</td>"
         +   "</tr>"
         +   "<tr>"
         +     "<td class=\"" + (isMain ? "MainFrame" : "Frame") + "Body\" " + (toolButtons.length > 0 ? "colspan=\"2\"" : "") + ">"
         +       "<table style=\"width:100%;\">"
         +         "<tr>"
         +           "<td>";
  }

  /**
   * Retorna o script contendo a representação HTML que inicia a área de Frames
   * do FrameBar.
   * @return String Retorna o script contendo a representação HTML que inicia a
   *                área de Frames do FrameBar.
   */
  public String beginFrameArea() {
    // avisa que já inserimos a FrameBarArea
    frameBarInserted = true;
    return "<td class=\"FrameBar\" style=\"width:" + width + "px;\">"
         +   "<div style=\"width:100%; height:100%; overflow-y:auto;\">"
         +     "<table style=\"width:100%; layout:fixed;\">"
         +       "<tr>"
         +         "<td valign=\"top\" style=\"padding:" + FRAME_BAR_OFFSET + "px;" + (clientAreaInserted ? "padding-left:0px;" : "") + "\">";
  }

  /**
   * Retorna o script contendo a representação HTML que finaliza a área cliente
   * lateral ao FrameBar.
   * @return String Retorna o script contendo a representação HTML que finaliza a
   *                área cliente lateral ao FrameBar.
   */
  public String endClientArea() {
    return       "</td>"
         +     "</tr>"
         +   "</table>"
         + "</td>";
  }

  /**
   * Retorna o script contendo a representação HTML que finaliza um Frame
   * no FrameBar.
   * @return String Retorna o script contendo a representação HTML que finaliza
   *                um Frame no FrameBar.
   */
  public String endFrame() {
    return           "</td>"
         +         "</tr>"
         +       "</table>"
         +     "</td>"
         +   "</tr>"
         + "</table>";
  }

  /**
   * Retorna o script contendo a representação HTML que finaliza a área de Frames
   * do FrameBar.
   * @return String Retorna o script contendo a representação HTML que finaliza a
   *                área de Frames do FrameBar.
   */
  public String endFrameArea() {
    return         "</td>"
         +       "</tr>"
         +     "</table>"
         +   "</div>"
         + "</td>";
  }

  /**
   * Retorna o script contendo a representação HTML que finaliza o container do
   * FrameBar.
   * @return Retorna o script que contendo a representação HTML que finaliza o
   *         container do FrameBar.
   */
  public String end() {
    // inicia o container do FrameBar e da área cliente
    return    "</tr>"
          + "</table>"
          + "<script type=text/javascript>"
              // oculta todos os <tr> após <tr class=frameBarHideNext>
          +   "$(\"tr.frameBarHideNext ~ tr\").hide();"
              // mostra o última <tr> após <tr class=frameBarShowLast>
          +   "$(\"tr.frameBarShowLast ~ tr :last\").show();"
              // associa o evento click a esse <tr>
          +   "$(\"tr.frameBarHideNext\").show().on(\"click\", function(event) {"
          +     "$(this).hide();"
          +     "$(this).nextAll(\"tr\").show(\"slow\");"
          +   "})"
          + "</script>";
  }

  /**
   * Retorna a identificação do FrameBar na página.
   * @return String Retorna a identificação do FrameBar na página.
   */
  public String getId() {
    return id;
  }

  /**
   * Retorna a largura do FrameBar na página.
   * @return int Retorna a largura do FrameBar na página.
   */
  public int getWidth() {
    return width;
  }

  /**
   * Defina a largura do FrameBar na página.
   * @param width int largura do FrameBar na página.
   */
  public void setWidth(int width) {
    this.width = width;
  }

  /**
   * Retorna o script contendo a representação HTML de uma nova linha de tabela
   * contendo "Mostrar mais...". Todas as próximas linhas da tabela são ocultadas
   * quando a página é exibida e se tornam visíveis quando o usuário clica nesta
   * linha.
   * @param showLastRow True para que a última <tr> da tabela seja exibida.
   * @return 
   */
  public String showMoreRow(boolean showLastRow) {
    return "<tr class=\"frameBarHideNext" + (showLastRow ? " frameBarShowLast" : "") + "\">"
         +   "<td class=frameBarShowMore>Mostrar mais...</td>"
         + "</tr>";
  }
  
}
