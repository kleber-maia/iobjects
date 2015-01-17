package iobjects.ui;

import java.util.*;

import iobjects.*;
import iobjects.util.*;

/**
 * Utilitário para criação de fluxogramas baseados em Action's.
 * @since 2006 R3
 */
public class FlowChart {

  static public class FlowItem {
    private Action action      = null;
    private String caption     = "";
    private String description = "";
    private byte   kind        = 0;
    public FlowItem(Action action,
                    String caption,
                    String description,
                    byte   kind) {
      this.action      = action;
      this.caption     = caption;
      this.description = description;
      this.kind        = kind;
    }
    public Action getAction()      { return action; }
    public String getCaption()     { return caption; }
    public String getDescription() { return description; }
    public byte   getKind()        { return kind; }

  }

  static public final byte KIND_MAIN      = 0;
  static public final byte KIND_SECONDARY = 1;
  static public final byte KIND_ASSISTANT = 2;

  static private final String ENTITY            = "images/entity32x32.png";
  static private final String PROCESS           = "images/process32x32.png";
  static private final String REPORT            = "images/report32x32.png";

  private String       caption      = "";
  private String       description  = "";
  private String       name         = "";
  private StringBuffer script       = null;
  private Vector       vector       = new Vector();
  private Vector       seeAlso      = new Vector();

  private int levelsDown = 0;
  private int levelsUp   = 0;
  private int stepCount  = 0;

  /**
   * Construtor padrão.
   */
  public FlowChart() {
  }

  /**
   * Adiciona um item ao fluxograma.
   * <p><b>
   * O primeiro item adicionado deve ser um item principal.
   * </b></p>
   * <p><b>
   * Só é possível adicionar um item auxiliar após um item principal.
   * </b></p>
   * @param action Action cuja URL será utilizada como destino do item.
   * @param caption String Título do item.
   * @param description String Descrição do item.
   * @param kind byte Modo de exibição.
   */
  public void addItem(Action action,
                      String caption,
                      String description,
                      byte   kind) {
    // se não é um item principal e não temos itens...exceção
    if ((kind != KIND_MAIN) && (vector.size() == 0))
      throw new RuntimeException(getClass().getName() + ".addItem(): o primeiro item adicionado deve ser um item principal.");
    // se é um item auxiliar e o último não foi um item principal...exceção
    if ((kind == KIND_ASSISTANT) && ((FlowItem)vector.lastElement()).getKind() != KIND_MAIN)
      throw new RuntimeException(getClass().getName() + ".addItem(): só é possível adicionar um item auxiliar após um item principal.");
    // adiciona
    vector.add(new FlowItem(action, caption, description, kind));
    // se é um item principal
    if (kind == KIND_MAIN) {
      stepCount++;
    }
    // se é um item secundário
    else if (kind == KIND_SECONDARY) {
      stepCount++;
      levelsDown = 1;
    }
    // se é um item auxiliar
    else if (kind == KIND_ASSISTANT) {
      levelsUp = 1;
    } // if

    // se o item anterior é do mesmo tipo...precisaremos de um conector
    if (vector.size() > 1) {
      if (((FlowItem)vector.elementAt(vector.size()-2)).getKind() == kind) {
        stepCount++;
        return;
      } // if
    } // if
    // se o item anterior é um auxiliar e o item anterior a ele é do mesmo tipo...precisaremos de um conector
    if (vector.size() > 2) {
      if ((((FlowItem)vector.elementAt(vector.size()-2)).getKind() == KIND_ASSISTANT) &&
          ((FlowItem)vector.elementAt(vector.size()-3)).getKind() == kind) {
        stepCount++;
        return;
      } // if
    } // if
  }

  /**
   * Adiciona um item associado ao Fluxograma.
   * @param action Action Adiciona um item associado ao Fluxograma.
   */
  public void addSeeAlso(Action action) {
    seeAlso.add(action);
  }

  /**
   * Libera recursos.
   */
  public void finalize() {
    script = null;
    vector.clear();
    vector = null;
  }

  private String getButton(Facade facade,
                           Action action,
                           String caption,
                           String description,
                           String target) {
    // a url padrão é a do Action
    String url = (action != null ? action.url() : "");
    // se não está na forma de script...adiciona para funcionar no botão
    if (!url.equals("") && url.indexOf("javascript") < 0)
      url = (!target.equals("") ? target + ".location" : "window.location") + ".href='" + url + "';";
    // se não temos caption...
    if (caption.equals("") && (action == null))
        caption = "Inválido";
    // se tem mais de 10 caracteres...tenta quebrar em 2 linhas
    if (caption.length() > 10) {
      // primeiro espaço encontrado
      int space = caption.indexOf(' ');
      // se está no caracter 10 ou além dele...quebra
      if (space >= 10)
        caption = caption.replaceFirst(" ", "<br />");
      // se não está...procura pelo último espaço antes do 10o caracter e quebra
      else if (space >= 0) {
        space = caption.substring(0, 10).lastIndexOf(' ');
        caption = caption.substring(0, space) + "<br />" + caption.substring(space+1);
      } // if
    } // if
    // retorna
    return Button.script(facade,
                         "button" + (action != null ? action.getName() : "Dummy"),
                         caption,
                         description,
                         (action == null ? "" : action.getCategory() == Action.CATEGORY_ENTITY ? ENTITY : action.getCategory() == Action.CATEGORY_PROCESS ? PROCESS : action.getCategory() == Action.CATEGORY_REPORT ? REPORT : ""),
                         "",
                         Button.KIND_BIG,
                         "width:64px; height:64px;",
                         url,
                         (action == null) || !facade.getLoggedUser().hasAccessRight(action));
  }

  public String getCaption() {
    return caption;
  }

  public String getDescription() {
    return description;
  }

  public String getName() {
    return name;
  }

  /**
   * Retorna a o script HTML contendo a representação gráfica do FlowChart.
   * <p><b>
   * O primeiro script gerado é armazenado em memória e retornado nas chamadas
   * seguintes deste método.
   * </b></p>
   * @param facade Facade Fachada da aplicação.
   * @param target String Nome do frame de destino do Action.
   * @return String Retorna a o script HTML contendo a representação gráfica do
   *                FlowChart.
   */
  public String script(Facade facade,
                       String target) {
    // se temos um script anterior...retorna
    if (script != null)
      return script.toString();

    // nossa tabela HTML em forma de matriz
    String[][] table = new String[(levelsUp > 0 ? levelsUp + 1 : 0) + (levelsDown > 0 ? levelsDown + 1 : 0) + 1][stepCount];
    // índice linha principal da tabela
    int tableRow = (levelsUp > 0 ? levelsUp + 1 : 0);
    // índice da coluna atual na tabela
    int tableCol = 0;

    // loop nos nossos itens
    for (int i=0; i<vector.size(); i++) {
      // item da vez
      FlowItem item = (FlowItem)vector.elementAt(i);

      // se o item anterior é do mesmo tipo...precisaremos de um conector
      if (i > 0) {
        if (((FlowItem)vector.elementAt(i-1)).getKind() == item.getKind()) {
          table[tableRow][tableCol] = "class=\"FlowChartPipeHorizontal\"";
          tableCol++;
        } // if
      } // if
      // se o item anterior é um auxiliar e o item anterior a ele é do mesmo tipo...precisaremos de um conector
      if (i > 1) {
        if ((((FlowItem)vector.elementAt(i-1)).getKind() == KIND_ASSISTANT) &&
            ((FlowItem)vector.elementAt(i-2)).getKind() == item.getKind()) {
          table[tableRow][tableCol] = "class=\"FlowChartPipeHorizontal\"";
          tableCol++;
        } // if
      } // if

      // linha e coluna do botão
      int buttonRow = -1;
      int buttonCol = -1;
      // se é um item principal...
      if (item.getKind() == KIND_MAIN) {
        // o botão ficará na posição atual
        buttonRow = tableRow;
        buttonCol = tableCol;
        // avança para a próxima coluna
        tableCol++;
      }
      // se é um item secundário...
      else if (item.getKind() == KIND_SECONDARY) {
        // põe o T na linha principal
        table[tableRow][tableCol] = "class=\"FlowChartPipeHorizontalTDown\"";
        // põe o separador vertical
        table[tableRow+1][tableCol] = "class=\"FlowChartPipeVertical\"";
        // o botão ficará na linha abaixo da conexão T
        buttonRow = tableRow+2;
        buttonCol = tableCol;
        // avança para a próxima coluna
        tableCol++;
      }
      // se é um item auxiliar...
      else if (item.getKind() == KIND_ASSISTANT) {
        // põe o conector acima do último item principal
        table[tableRow-1][tableCol-1] = "class=\"FlowChartPipeVertical\"";
        // o botão ficará na linha superior da conexão vertical
        buttonRow = tableRow-2;
        buttonCol = tableCol-1;
      } // if
      // põe o botão do item
      table[buttonRow][buttonCol] = getButton(facade, item.getAction(), caption, description, target);
    } // for

    // limpa tudo
    script = new StringBuffer();
    // tabela externa que contém o fluxograma e os botões associados
    script.append("<table cellpadding=\"0\" cellspacing=\"0\" style=\"width:100%; height:100%\";>");
    script.append(  "<tr style=\"height:auto;\">");
    script.append(    "<td align=\"center\" valign=\"middle\">");
    // inicia a tabela do fluxograma
    script.append(      "<table cellpadding=\"0\" cellspacing=\"0\">");
    // loop na tabela em forma de matriz para gerar a tabela HTML
    for (int row=0; row<table.length; row++) {
      // nova linha
      script.append("<tr>");
      for (int col=0; col<table[row].length; col++) {
        // conteúdo da célula
        String content = table[row][col];
        // se não temos nada...
        if (content == null)
          content = "&nbsp;";
        // se temos a classe...
        if (content.startsWith("class"))
          script.append("<td " + content + " style=\"width:64px; height:64px;\">&nbsp;</td>");
        // se temos outra coisa...
        else
          script.append("<td style=\"width:64px; height:64px;\">" + content + "</td>");
      } // for
      // termina linha
      script.append("</tr>");
    } // for i
    // termina a tabela do fluxograma
    script.append(      "</table>");
    script.append(    "</td>");
    script.append(  "</tr>");

    // botões associados
    StringBuffer seeAlsoButtons = new StringBuffer("");
    // insere os botões associados
    for (int i=0; i<seeAlso.size(); i++) {
      // Action da vez
      Action action = (Action)seeAlso.elementAt(i);
      // insere
      seeAlsoButtons.append(getButton(facade, action, "", "", target) + "<span style=\"width:" + FrameBar.FRAME_OFFSET + "px;\"></span>");
    } // for
    // temos botões associados?
    if (seeAlsoButtons.length() > 0) {
      script.append(  "<tr style=\"height:20px;\">");
      script.append(    "<td align=\"left\" valign=\"top\" class=\"BackgroundText\">Veja também:</td>");
      script.append(  "</tr>");
      script.append(  "<tr style=\"height:64px;\">");
      script.append(    "<td align=\"left\" valign=\"bottom\">");
      script.append(      seeAlsoButtons);
      script.append(    "</td>");
      script.append(  "</tr>");
    } // if
    script.append("</table>");

    // retorna
    return script.toString();
  }

  public void setCaption(String caption) {
    this.caption = caption;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public void setName(String name) {
    this.name = name;
  }

}
