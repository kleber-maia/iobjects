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
package iobjects.ui.report;

import java.util.*;

import iobjects.*;
import iobjects.ui.*;

/**
 * Representa uma tabela de dados gerada a partir de um 'loop' para ser
 * exibida em um relatório.
 * @since 2006
 */
public class ReportGridEx {

  static public final int ALIGN_LEFT   = Align.ALIGN_LEFT;
  static public final int ALIGN_CENTER = Align.ALIGN_CENTER;
  static public final int ALIGN_RIGHT  = Align.ALIGN_RIGHT;

  private String id      = "";
  private Vector columns = new Vector();
  private int    width   = 0;

  // variáveis de controle de linhas e células
  private int     cellCount = 0;
  private int     rowIndex  = 0;
  private boolean footer    = false;

  private class ColumnInfo {
    private int    align   = 0;
    private String caption = "";
    private int    width   = 0;
    public ColumnInfo(String caption,
                      int    width,
                      int    align) {
      this.caption = caption;
      this.width = width;
      this.align = align;
    }
    public int    getAlign()   {return align;}
    public String getCaption() {return caption;}
    public int    getWidth()   {return width;}
  }

  /**
   * Construtor padrão.
   * @param id String Identificação do ReportGrid para ser utilizada em scripts.
   * @param autoAlign true para que o EntityGrid ocupe toda área cliente do seu container.
   */
  public ReportGridEx(String  id,
                      boolean autoAlign) {
    this.id = id;
  }

  /**
   * Construtor padrão.
   * @param id String Identificação do ReportGrid para ser utilizada em scripts.
   * @param width int Largura em pixels do ReportGrid.
   */
  public ReportGridEx(String id,
                      int    width) {
    this.id = id;
    this.width = width;
  }

  /**
   * Adiciona uma coluna ao ReportGridEx.
   * @param caption String Título da coluna.
   * @param width int Largura da coluna. Caso a largura do ReportGridEx tenha
   *              sido definida em pixels, este valor deve ser em pixels. Caso
   *              o ReportGrid tenha sido definido como 'autoAlign' este valor
   *              deve ser em softgroupual.
   * @param align int Alinhamento da coluna.
   */
  public void addColumn(String caption,
                        int    width,
                        int    align) {
    columns.add(new ColumnInfo(caption,
                               width,
                               align));
  }

  /**
   * Retorna o script contento a representação HTML que inicia o ReportGridEx.
   * @return String Retorna o Script contento a representação HTML que inicia o
   *                ReportGridEx.
   */
  public String begin() {
    // linha atual
    rowIndex = 0;
    // nosso resultado
    StringBuffer result = new StringBuffer();
    // inicia o ReportGrid
    result.append("<p> \r");
    result.append("<table id=\"" + id + "\" class=\"Grid\" style=\"width:" + (width > 0 ? width + "px;" : "100%;") + "\"> \r");
    // cabeçalho
    result.append("  <tr> \r");
    for (int i=0; i<columns.size(); i++) {
      ColumnInfo columnInfo = (ColumnInfo)columns.elementAt(i);
      result.append("    <th class=\"GridHeader\" "
                          + "align=\"" + (columnInfo.getAlign() == ALIGN_LEFT ? "left" : columnInfo.getAlign() == ALIGN_CENTER ? "center" : "right") + "\" "
                          + "style=\"width:" + (width > 0 ? columnInfo.getWidth() + "px;" : columnInfo.getWidth() + "%;") + "\">"
                          + columnInfo.getCaption()
                          + "</td> \r");
    } // for
    result.append("  </tr> \r");
    // retorna
    return result.toString();
  }

  /**
   * Retorna o script contento a representação HTML que inicia um célula de
   * dados de uma linha do ReportGridEx.
   * @return String Retorna o Script contento a representação HTML que inicia
   *                uma célula de dados de uma linha do ReportGridEx.
   */
  public String beginCell() {
    return beginCell(1);
  }

  /**
   * Retorna o script contento a representação HTML que inicia um célula de
   * dados de uma linha do ReportGridEx.
   * @param span int Quantidade de células cujo espaço a célula que está sendo
   *                 iniciada irá ocupar ou 1 para ocupar sua própria posição.
   * @return String Retorna o Script contento a representação HTML que inicia
   *                uma célula de dados de uma linha do ReportGridEx.
   */
  public String beginCell(int span) {
    // coluna referente
    ColumnInfo columnInfo = (ColumnInfo)columns.elementAt(cellCount);
    // incrementa a quantidade de células da linha
    cellCount += span;
    // alinhamento
    String align = "";
    switch (columnInfo.getAlign()) {
      case ALIGN_LEFT  : align = "align=\"left\""; break;
      case ALIGN_CENTER: align = "align=\"center\""; break;
      case ALIGN_RIGHT : align = "align=\"right\""; break;
    } // switch
    // seu HTML
    String result = "  <" + (footer ? "th" : "td") + " class=\"" + (footer ? "GridFooter" : rowIndex % 2 > 0 ? "GridRowOdd" : "GridRowEven") + "\"" + (span > 1 ? "colspan=\"" + span + "\" " : "") + " " + align + ">";
    // retorna
    return result;
  }

  /**
   * Retorna o script contento a representação HTML que inicia uma linha do
   * ReportGridEx.
   * @return String Retorna o Script contento a representação HTML que inicia
   *                uma linha do ReportGridEx.
   */
  public String beginRow() {
    return beginRow(false);
  }

  /**
   * Retorna o script contento a representação HTML que inicia uma linha do
   * ReportGridEx.
   * @param footer boolean True para que a linha iniciada seja o rodapé do
   *               ReportGridEx.
   * @return String Retorna o Script contento a representação HTML que inicia
   *                uma linha do ReportGridEx.
   */
  public String beginRow(boolean footer) {
    // incrementa o índice da linha atual
    rowIndex++;
    // quantidade de células
    cellCount = 0;
    // é o rodapé?
    this.footer = footer;
    // início da linha
    return (footer ? "  <tfoot> \r" : "  <tr> \r");
  }

  /**
   * Retorna o script contento a representação HTML que finaliza uma célula de
   * dados de uma linha do ReportGridEx.
   * @return String Retorna o Script contento a representação HTML que finaliza
   *                uma célula de dados de uma linha do ReportGridEx.
   */
  public String endCell() {
    // término da célula
    String result = (footer ? "</th>  \r" : "</td> \r");
    // retorna
    return result;
  }

  /**
   * Retorna o script contento a representação HTML que finaliza uma linha do ReportGridEx.
   * @return String Retorna o Script contento a representação HTML que finaliza
   *                uma linha do ReportGridEx.
   */
  public String endRow() {
    // se a qde de células é diferente da qde. de colunas do Grid...exceção
    if (cellCount != columns.size())
      throw new RuntimeException(new ExtendedException(getClass().getName(), "endRow", "A quantidade de células não combina com a quantidade de colunas."));
    // fim da linha
    return (footer ? "  </tfoot> \r" : "  </tr> \r");
  }

  /**
   * Retorna o script contento a representação HTML que finaliza o ReportGridEx.
   * @return String Retorna o Script contento a representação HTML que finaliza o
   *                ReportGridEx.
   */
  public String end() {
    // nosso resultado
    StringBuffer result = new StringBuffer();
    // finaliza o ReportGrid
    result.append("</table> \r");
    result.append("</p> \r");
    // retorna
    return result.toString();
  }

}
