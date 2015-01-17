package iobjects.ui;

import java.util.*;

import iobjects.*;

/**
 * Utilit�rio para cria��o de gr�ficos comerciais para telas e relat�rios.
 */
public class Chart {

  class PlotBandInfo {
    double fromIndex = 0;
    double toIndex = 0;
    String title = "";
    public PlotBandInfo(double fromIndex, double toIndex, String title) {
      this.fromIndex = fromIndex;
      this.toIndex = toIndex;
      this.title = title;
      // *
      if (fromIndex == toIndex) {
        this.fromIndex = this.fromIndex - 0.5;
        this.toIndex = this.toIndex + 0.5;
      } // if
    }
  }

  class PointInfo {
    double x = 0;
    double y = 0;
    public PointInfo(double x, double y) {
      this.x = x;
      this.y = y;
    }
  }
  
  class SerieInfo {
    String        name   = "";
    Vector<Param> values = new Vector<Param>(); 
    byte          type   = -1;
    public SerieInfo(String name) { this.name = name; }
    public SerieInfo(String name, byte type) { this.name = name; this.type = type; }
  }
  
  static public final byte TYPE_AREA   = 0;
  static public final byte TYPE_COLUMN = 1;
  static public final byte TYPE_LINE   = 2;
  static public final byte TYPE_PIE    = 3;
  
  static private String[] TYPE_ARRAY = {"area", "column", "line", "pie"};

  static public String STYLE_NAME = "Chart";

  static public String[] GRADIENT_COLORS = new String[]{"linearGradient: [0, 0, 0, '100%'], stops: [[0, '#dfeef9'], [1, '#94b7d2']]",
                                                        "linearGradient: [0, 0, 0, '100%'], stops: [[0, '#fce8ab'], [1, '#cda117']]",
                                                        "linearGradient: [0, 0, 0, '100%'], stops: [[0, '#d1e0a2'], [1, '#769e00']]",
                                                        "linearGradient: [0, 0, 0, '100%'], stops: [[0, '#fbd1b5'], [1, '#cd7f4d']]",
                                                        "linearGradient: [0, 0, 0, '100%'], stops: [[0, '#a0cece'], [1, '#0c7c7c']]",
                                                        "linearGradient: [0, 0, 0, '100%'], stops: [[0, '#ebb5b5'], [1, '#b53b3b']]",
                                                        "linearGradient: [0, 0, 0, '100%'], stops: [[0, '#d8bed8'], [1, '#783b78']]"};
  static public String[] SOLID_COLORS = new String[]{"#94b7d2",
                                                      "#cda117",
                                                      "#769e00",
                                                      "#cd7f4d",
                                                      "#0c7c7c",
                                                      "#b53b3b",
                                                      "#783b78"};
  
  static public final byte INTERFACE_STYLE_USER_INTERFACE   = 0;
  static public final byte INTERFACE_STYLE_REPORT_INTERFACE = 1;
  
  static public final byte     POSITION_TOP    = 0;
  static public final byte     POSITION_BOTTOM = 1;
  static public final String[] POSITION_ARRAY  = {"top", "bottom"};

  private Facade            facade             = null;
  private String            id                 = "";
  private byte              legendPosition     = POSITION_TOP;
  private Vector<PlotBandInfo> plotBands       = new Vector<PlotBandInfo>();
  private Vector<SerieInfo> series             = new Vector<SerieInfo>();
  private boolean           showDataLabels     = true;
  private boolean           showMarker         = true;
  private boolean           showLabels         = true;
  private boolean           showLegend         = false;
  private boolean           showTooltip        = true;
  private PointInfo         trendPoint0        = null;
  private PointInfo         trendPoint1        = null;
  
  /**
   * Construtor padr�o.
   * @param facade Facade.
   * @param id Identifica��o do Chart na p�gina.
   */
  public Chart(Facade facade,
               String id) {
    // nossos valores
    this.facade = facade;
    this.id = id;
  }

  /**
   * Adiciona uma banda de plotagem ao gr�fico. Uma banda de plotagem � uma regi�o
   * que vai de um valor a outro no eixo X do gr�fico.
   * @param fromIndex �ndice do valor de in�cio da banda de plotagem.
   * @param toIndex �ndice do valor de t�rmino da banda de plotagem.
   * @param title T�tulo da banda de plotagem.
   */
  public void addPlotBand(int fromIndex, int toIndex, String title) {
    plotBands.add(new PlotBandInfo(fromIndex, toIndex, title));
  }
  
  /**
   * Adiciona uma s�rie de valores ao gr�fico.
   * @param name Nome da s�rie de valores.
   */
  public void addSerie(String name) {
    series.add(new SerieInfo(name));
  }
  
  /**
   * Adiciona uma s�rie de valores ao gr�fico.
   * @param name Nome da s�rie de valores.
   * @param type Tipo de gr�fico da s�rie. OBS: Apenas o tipo linha � aceito no momento.
   */
  public void addSerie(String name, byte type) throws Exception {
    if (type != TYPE_LINE)
      throw new ExtendedException(getClass().getName(), "addSerie", "Tipo n�o suportado na s�rie: " + TYPE_ARRAY[type] + ".");
    series.add(new SerieInfo(name, type));
  }

  /**
   * Adiciona uma linha de tend�ncia ao gr�fico com base nos valores contidos
   * na s�rie informada por 'serieName'.
   * @param serieName Nome da s�rie que ser� utilizada como base para a linha 
   *                  de tend�ncia.
   */
  public void addTrendLine(String serieName) throws Exception {
    // obt�m a s�rie
    SerieInfo serie = null;
    for (int i=0; i<series.size(); i++) {
      if (series.get(i).name.equals(serieName)) {
        serie = series.get(i);
        break;
      } // if
    } // for
    // se n�o achamos a s�rie...exce��o
    if (serie == null)
      throw new ExtendedException(getClass().getName(), "addTrendLine", "S�rie n�o encontrada: " + serieName + ".");
    // se n�o temos valores suficientes...dispara
    if (serie.values.size() < 2)
      return;
    // c�lculo da reta de tend�ncia
    double sumXY = 0, avgX = 0, avgY = 0, sumX2 = 0, avgX2, b, a;
    int    n = serie.values.size();
    int    count = 0;
    int    indexBegin = -1;
    for (int i=1; i<=n; i++) {
      double value = ((Double)serie.values.get(i-1).getObject()).doubleValue();
      // se j� temos uma posi��o ini
      if (indexBegin >= 0 || value > 0) {
        if (indexBegin < 0) {
          indexBegin = i;
        } // if
        sumXY += (i * value);
        avgX  += i;
        avgY  += value;
        sumX2 += i * i;
      } // trend
      count++;
    } // for
    if (indexBegin >= 0) {
      avgX          = avgX / count;
      avgY          = avgY / count;
      avgX2         = avgX * avgX;
      b             = (sumXY - (count * avgX * avgY)) / (sumX2 - (count * avgX2));
      a             = avgY - (b * avgX);
      trendPoint0   = new PointInfo(indexBegin-1, a + (b*0));
      trendPoint1   = new PointInfo(count-1, a + (b*count));
    } // if
  }

  /**
   * Adiciona um valor a �ltima s�rie do gr�fico.
   * @param name Nome do valor.
   * @param name Title do valor.
   * @param value Valor num�rico.
   */
  public void addValue(String name,
                       String title,
                       double value) {
    // se n�o temos a s�rie...cria
    if (series.size() == 0)
      addSerie("");
    // nossa s�rie atual
    SerieInfo serieInfo = series.elementAt(series.size()-1);
    // adiciona o valor
    Param param = new Param(name, new Double(value));
    param.setCaption(title);
    serieInfo.values.add(param);
  }
  
  @Override public void finalize() {
    // libera recursos
    series.clear();
  }

  /**
   * Insere um valor no in�cio da �ltima s�rie do gr�fico.
   * @param name Nome ou t�tulo do valor.
   * @param name Title do valor.
   * @param value Valor num�rico.
   */
  public void insertValue(String name,
                          String title,
                          double value) {
    // se n�o temos a s�rie...cria
    if (series.size() == 0)
      addSerie("");
    // nossa s�rie atual
    SerieInfo serieInfo = series.elementAt(series.size()-1);
    // adiciona o valor
    Param param = new Param(name, new Double(value));
    param.setCaption(title);
    serieInfo.values.insertElementAt(param, 0);
  }
  
  /**
   * Define a posi��o da legenda das s�ries.
   * @param showLegendPosition  
   */
  public void setLegendPosition(byte legendPosition) {
    this.legendPosition = legendPosition;
  }
  

  /**
   * Retorna o script HTML contendo a representa��o do gr�fico.
   * <b>O retorno deste m�todo deve ser inserido diretamente no corpo da p�gina,
   * pois n�o funcionar� se inserido em outros elementos HTML.</b>
   * @param type Tipo do gr�fico.
   * @param containerId ID do elemento HTML onde o gr�fico ser� inserido. O
   *                    gr�fico ter� as dimens�es do container no momento em
   *                    que for gerado.
   *                    <b>Esta abordagem permite que o mesmo conjunto de dados
   *                    seja utilizado para cria��o do gr�fico e exibi��o dos
   *                    dados na tela. Para isso � poss�vel declarar um
   *                    container onde o gr�fico ser� exibido, efetuar o loop
   *                    nos dados mostrando-os na tela e adicionando ao gr�fico e,
   *                    por fim, chamando este m�doto que ir� exibir o gr�fico
   *                    no container previamente declarado.</b>
   * @param caption T�tulo do gr�fico.
   * @param xAxisCaption T�tulo do eixo X.
   * @param yAxisCaption T�tulo do eixo Y.
   * @param interfaceStyle Estilo da interface para definir a apar�ncia do gr�fico.
   * @return Retorna o script HTML contendo a representa��o do gr�fico.
   */
  public String script(byte    type,
                       String  containerId,
                       String  caption,
                       String  xAxisCaption,
                       String  yAxisCaption,
                       byte    interfaceStyle) {
    return script(type, containerId, caption, xAxisCaption, yAxisCaption, interfaceStyle, false, true);
  }

  /**
   * Retorna o script HTML contendo a representa��o do gr�fico.
   * <b>O retorno deste m�todo deve ser inserido diretamente no corpo da p�gina,
   * pois n�o funcionar� se inserido em outros elementos HTML.</b>
   * @param type Tipo do gr�fico.
   * @param containerId ID do elemento HTML onde o gr�fico ser� inserido. O 
   *                    gr�fico ter� as dimens�es do container no momento em
   *                    que for gerado.
   *                    <b>Esta abordagem permite que o mesmo conjunto de dados
   *                    seja utilizado para cria��o do gr�fico e exibi��o dos
   *                    dados na tela. Para isso � poss�vel declarar um
   *                    container onde o gr�fico ser� exibido, efetuar o loop
   *                    nos dados mostrando-os na tela e adicionando ao gr�fico e,
   *                    por fim, chamando este m�doto que ir� exibir o gr�fico
   *                    no container previamente declarado.</b>
   * @param caption T�tulo do gr�fico.
   * @param xAxisCaption T�tulo do eixo X.
   * @param yAxisCaption T�tulo do eixo Y.
   * @param interfaceStyle Estilo da interface para definir a apar�ncia do gr�fico.
   * @param showDecimals True para que as decimais dos valores sejam exibidos.
   *                     Caso contr�rio, apresenta sem casas decimais.
   * @param allowZoom True para que seja permitido ao usu�rio interagir com o
   *                  gr�fico atrav�s do recurso de zooom.
   * @return Retorna o script HTML contendo a representa��o do gr�fico.
   */
  public String script(byte    type,
                       String  containerId,
                       String  caption,
                       String  xAxisCaption,
                       String  yAxisCaption,
                       byte    interfaceStyle,
                       boolean showDecimals,
                       boolean allowZoom) {
    // nosso resultado
    StringBuffer result = new StringBuffer();

    // se n�o temos s�ries...adiciona um valor
    if (series.size() == 0)
      addValue("", "", 0);

    // obt�m os par�metros de estilo do gr�fico
    iobjects.xml.ui.StyleFile.Style style = (interfaceStyle == INTERFACE_STYLE_USER_INTERFACE ? facade.getStyle().css().getUserInterfaceStyle() : facade.getStyle().css().getReportInterfaceStyle());
    ParamList chartParamList = style.get(STYLE_NAME);
    Param paramBackgroundColor    = chartParamList.get("backgroundColor");
    Param paramAlternateGridColor = chartParamList.get("alternateGridColor");
    Param paramGridLineColor      = chartParamList.get("gridLineColor");

    // inicia o gr�fico
    result.append("<script type=\"text/javascript\">");
    result.append(  "var " + id + ";");
    result.append(  "Highcharts.setOptions({");
    result.append(  "lang: {decimalPoint: ',', thousandsSep: '.', loading: 'Carregando...', resetZoom: 'Sem zoom'},");
    if ((type != TYPE_LINE) && (type != TYPE_AREA)) {
      result.append(  "colors: [{" + GRADIENT_COLORS[0] + "},");
      result.append(           "{" + GRADIENT_COLORS[1] + "},");
      result.append(           "{" + GRADIENT_COLORS[2] + "},");
      result.append(           "{" + GRADIENT_COLORS[3] + "},");
      result.append(           "{" + GRADIENT_COLORS[4] + "},");
      result.append(           "{" + GRADIENT_COLORS[5] + "},");
      result.append(           "{" + GRADIENT_COLORS[6] + "}");
      result.append(           "]");
    }
    else {
      result.append(  "colors: ['" + SOLID_COLORS[0] + "','" + SOLID_COLORS[1] + "','" + SOLID_COLORS[2] + "','" + SOLID_COLORS[3] + "','" + SOLID_COLORS[4] + "','" + SOLID_COLORS[5] + "','" + SOLID_COLORS[6] + "']");
    } // if
    result.append(  "});");
    result.append(  "$(document).ready(function() {");
    result.append(    id + " = new Highcharts.Chart({");
    result.append(      "chart: {");
    result.append(        "backgroundColor: '" + paramBackgroundColor.getValue() + "',");
    result.append(        "renderTo: '" + containerId + "',");
    result.append(        "defaultSeriesType: '" + TYPE_ARRAY[type] + "',");
    result.append(        "marginLeft: 10,");
    result.append(        "marginTop: 10,");
    result.append(        "marginRight: 10,");
    result.append(        "marginBottom: 20,");
    result.append(        "zoomType: '" + (allowZoom ? "x" : "") + "'");
    result.append(      "},");
    result.append(      "title: {");
    result.append(        "text: ''"); // caption vazio
    result.append(      "},");
    result.append(      "legend: {");
    result.append(        "enabled: " + showLegend + ",");
    result.append(        "verticalAlign: '" + POSITION_ARRAY[legendPosition] + "'");
    result.append(      "},");
    result.append(      "tooltip: {");
    result.append(        "enabled: false");
    result.append(      "},");
    result.append(      "plotOptions: {");
    result.append(        "series: {");
    result.append(          "allowPointSelect: true,");
    if (type == TYPE_LINE) {
      result.append(          "marker: {");
      result.append(            "color: '#000000',");
      result.append(            "enabled: true");
      result.append(          "},");
    } // if
    result.append(          "dataLabels: {");
    result.append(            "enabled: " + showDataLabels + ",");
    result.append(            "formatter: function() {");
    result.append(              "return Highcharts.numberFormat(this.y, " + (showDecimals ? "2" : "0") + ");");
    result.append(            "},");
    result.append(            "color: '#000000'");
    result.append(          "}");
    result.append(        "}");
    result.append(      "},");
    result.append(      "xAxis: {");
    result.append(        "lineColor: '#ffffff',");
    result.append(        "lineWidth: 2,");
    result.append(        "categories: [");
    // constroi o eixo X
    SerieInfo serieInfo = series.get(0);
    for (int w=0; w<serieInfo.values.size(); w++) {
      Param value = serieInfo.values.get(w);
      result.append(        (w > 0 ? "," : "") + "'" + value.getName() + "'");
    } // for w
    result.append(        "],");
    result.append(        "plotBands: [");
    // constroi as bandas de plotagem
    for (int w=0; w<plotBands.size(); w++) {
      PlotBandInfo ploBandInfo = plotBands.get(w);
      result.append(        (w > 0 ? "," : ""));
      result.append(      "{");
      result.append(        "from: '" + ploBandInfo.fromIndex + "',");
      result.append(        "to: '" + ploBandInfo.toIndex + "',");
      result.append(        "color: 'rgba(0,0,0,0.10)',");
      result.append(        "label: {");
      result.append(          "text: '" + ploBandInfo.title + "'");
      result.append(        "},");
      result.append(        "zIndex: 0");
      result.append(      "}");
    } // for w
    result.append(        "]");
    result.append(      "},");
    // constr�i o primeiro eixo Y
    result.append(      "yAxis: [");
    result.append(      "{");
    result.append(        "alternateGridColor: '" + paramAlternateGridColor.getValue() + "',");
    result.append(        "gridLineColor: '" + paramGridLineColor.getValue() + "',");
    result.append(        "lineColor: '#ffffff',");
    result.append(        "lineWidth: 2,");
    result.append(        "title: '" + serieInfo.name + "',");
    result.append(        "labels: {");
    result.append(          "enabled: false");
    result.append(        "}");
    result.append(      "}");
    // constroi os demais eixos Y
    for (int w=1; w<series.size(); w++) {
      // se o tipo do gr�fico n�o mudou...n�o precisamos de outro eixo
      if (series.get(w).type < 0 || series.get(w).type == type)
        continue;
    result.append(      ",{");
    result.append(        "oposite: true,");
    result.append(        "alternateGridColor: '" + paramAlternateGridColor.getValue() + "',");
    result.append(        "gridLineColor: '" + paramGridLineColor.getValue() + "',");
    result.append(        "lineColor: '#ffffff',");
    result.append(        "lineWidth: 2,");
    result.append(        "title: '" + series.get(w).name + "',");
    result.append(        "labels: {");
    result.append(          "enabled: false");
    result.append(        "}");
    result.append(      "}");
    } // for
    result.append(      "],");
    result.append(      "tooltip: {");
    result.append(        "enabled: " + showTooltip + ",");
    result.append(        "formatter: function() {return (this.point.name ? this.point.name + ': ' : this.x ? this.x + ': ' : '') + Highcharts.numberFormat(this.y, 0);}");
    result.append(      "},");
    result.append(      "series: [");
    // loop nas s�ries
    int yAxisCount = 0;
    int colorCount = 0;
    for (int i=0; i<series.size(); i++) {
      // s�rie da vez
      serieInfo = series.get(i);
      // adiciona seus dados, valores, etc
      result.append(      (i > 0 ? "," : "") + "{");
      result.append(        "name: '" + serieInfo.name + "',");
      // exibe os valores no gr�fico?
      if (!showMarker)
      result.append(        "marker: {enabled: false},");
      // exibe os nomes no eixo?
      if (!showLabels)
      result.append(        "dataLabels: {enabled: false},");
      // se a s�rie tem um tipo de gr�fico diferente...
      if (serieInfo.type >= 0 && serieInfo.type != type) {
      result.append(        "type: '" + TYPE_ARRAY[serieInfo.type] + "',");
      result.append(        "yAxis: " + (++yAxisCount) + ",");
      result.append(        "color: '" + SOLID_COLORS[colorCount] + "',");
      } // if
      result.append(        "data: [");
      for (int w=0; w<serieInfo.values.size(); w++) {
        Param value = serieInfo.values.get(w);
        result.append(        (w > 0 ? "," : "") + "{name: '" + value.getCaption() + "', y:" + ((Double)value.getObject()).doubleValue() + "}");
      } // for w
      result.append(        "]");
      result.append(      "}");
      // contador de cores
      colorCount++; 
      if (colorCount > SOLID_COLORS.length-1) 
        colorCount = 0;
    } // for i
    // se temos uma linha de tend�ncia...
    if (trendPoint0 != null && trendPoint1 != null) {
      result.append(        ",");
      result.append(        "{name: 'Tend�ncia',");
      result.append(        "marker: {enabled: false},");
      result.append(        "dataLabels: {enabled: false},");
      result.append(        "enableMouseTracking: false,");
      result.append(        "type: 'line',");
      result.append(        "color: 'gray',");
      result.append(        "lineWidth: 2,");
      result.append(        "shadow: false,");
      result.append(        "dashStyle: 'dash',");
      result.append(        "data: [{x:" + trendPoint0.x + ", y:" + trendPoint0.y + "}, {x:" + trendPoint1.x + ", y:" + trendPoint1.y + "}]");
      result.append(        "}");
    } // if
    result.append(      "] ");
    result.append(    "}); ");
    result.append(  "}); ");
    result.append("</script>");
    // retorna
    return result.toString();
  }

  /**
   * Define se os valores ser�o exibidos nos pontos do gr�fico.
   * @param showDataLabels  
   */
  public void setShowDataLabels(boolean showDataLabels) {
    this.showDataLabels = showDataLabels;
  }
  
  /**
   * Define se os nomes dos valores ser�o exibidos no gr�fico.
   * @param showLabels  
   */
  public void setShowLabels(boolean showLabels) {
    this.showLabels = showLabels;
  }
  
  /**
   * Define se a legenda das s�ries ser� exibida com o gr�fico.
   * @param showLegend  
   */
  public void setShowLegend(boolean showLegend) {
    this.showLegend = showLegend;
  }
  
  /**
   * Define se os marcadores com os valores ser�o exibidos no gr�fico.
   * @param showMarker 
   */
  public void setShowMarker(boolean showMarker) {
    this.showMarker = showMarker;
  }
  
  /**
   * Define se as legendas com os valores ser�o exibidas sob o mouse.
   * @param showTooltip  
   */
  public void setShowTooltip(boolean showTooltip) {
    this.showTooltip = showTooltip;
  }
  
}
