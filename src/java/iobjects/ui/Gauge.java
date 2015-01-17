package iobjects.ui;

import java.util.*;

import iobjects.*;

/**
 * Utilit�rio para cria��o de gr�ficos comerciais para telas e relat�rios.
 */
public class Gauge {

  static private String[] GRADIENT_COLORS = new String[]{"[0, '#FF654F'], [1, '#600000']",
                                                         "[0, '#fce8ab'], [1, '#cda117']",
                                                         "[0, '#769e00'], [1, '#d1e0a2']"};
  
  static public final byte INTERFACE_STYLE_USER_INTERFACE   = 0;
  static public final byte INTERFACE_STYLE_REPORT_INTERFACE = 1;
  
  private Facade            facade             = null;
  private String            id                 = "";
  
  /**
   * Construtor padr�o.
   * @param facade Facade.
   * @param id Identifica��o do Gauge na p�gina.
   */
  public Gauge(Facade facade,
               String id) {
    // nossos valores
    this.facade = facade;
    this.id = id;
  }

  /**
   * Retorna o script HTML contendo a representa��o do gauge.
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
   * @param criticalPercent Percentual cr�tico do gauge.
   * @param satisfactoryPercent Percentual satisfat�rio do gauge.
   * @param actualPercent Percentual realizado para ser exibido.
   * @param forecastPercent Percentual projetado para ser exibido.
   * @param interfaceStyle Estilo da interface para definir a apar�ncia do gr�fico.
   * @return Retorna o script HTML contendo a representa��o do gr�fico.
   */
  public String script(String  containerId,
                       String  caption,
                       int     criticalPercent,
                       int     satisfactoryPercent,
                       double  actualPercent,
                       double  forecastPercent,
                       byte    interfaceStyle) throws Exception {
    // verifica os percentuais cr�tico e satisfat�rio
    if (criticalPercent < 0 || criticalPercent > 100)
      throw new ExtendedException(getClass().getName(), "script", "O Percentual Cr�tico deve estar entre 0% e 100%.");
    if (satisfactoryPercent < 0 || satisfactoryPercent > 100)
      throw new ExtendedException(getClass().getName(), "script", "O Percentual Satisfat�rio deve estar entre 0% e 100%.");
    if (satisfactoryPercent <= criticalPercent)
      throw new ExtendedException(getClass().getName(), "script", "O Percentual Satisfat�rio deve ser maior que o Percentual Cr�tico.");
    
    // nosso resultado
    StringBuffer result = new StringBuffer();
    // obt�m os par�metros de estilo do gr�fico
    iobjects.xml.ui.StyleFile.Style style = (interfaceStyle == INTERFACE_STYLE_USER_INTERFACE ? facade.getStyle().css().getUserInterfaceStyle() : facade.getStyle().css().getReportInterfaceStyle());
    ParamList chartParamList = style.get(Chart.STYLE_NAME);
    Param paramBackgroundColor    = chartParamList.get("backgroundColor");
    Param paramAlternateGridColor = chartParamList.get("alternateGridColor");
    Param paramGridLineColor      = chartParamList.get("gridLineColor");

    // inicia o gr�fico
    result.append("<script type=\"text/javascript\">");
    result.append(  "var " + id + ";");
    result.append(  "Highcharts.setOptions({");
    result.append(  "lang: {decimalPoint: ',', thousandsSep: '.', loading: 'Carregando...', resetZoom: 'Sem zoom'}");
    result.append(  "});");
    result.append(  "$(document).ready(function() {");
    result.append(    id + " = new Highcharts.Chart({");
    result.append(      "chart: {");
    result.append(      "animation: true,");
    result.append(      "backgroundColor: '" + paramBackgroundColor.getValue() + "',");
    result.append(      "renderTo: '" + containerId + "',");
    result.append(      "type: 'gauge',");
    result.append(      "plotBackgroundColor: null,");
    result.append(      "plotBackgroundImage: null,");
    result.append(      "plotBorderWidth: 0,");
    result.append(      "plotShadow: false");
    result.append(      "},");
    result.append(      "title: {");
    result.append(        "text: ''"); // caption vazio
    result.append(      "},");
    result.append(      "pane: [{");
    result.append(        "startAngle: -90,");
    result.append(        "endAngle: 90,");
    result.append(        "background: null,");
    result.append(        "center: ['50%', '100%'],");
    result.append(        "size: '150%'");
    result.append(      "}],");
    result.append(      "yAxis: [{");
    result.append(          "min: 0,");
    result.append(          "max: 100,");
    result.append(          "offset: 0,");
    result.append(          "minorTickPosition: 'inside',");
    result.append(          "tickPosition: 'inside',");
    result.append(          "minorTickWidth: 1,");
    result.append(          "tickInterval: 10,");
    result.append(          "tickWidth: 1,");
    result.append(          "minorTickLength: 5,");
    result.append(          "tickLength: 10,");
    result.append(          "minorTickColor: '#666666',");
    result.append(          "tickColor: '#666666',");
    result.append(          "labels: {");
    result.append(              "rotation: 'auto',");
    result.append(              "distance: 10");
    result.append(          "},");
    result.append(          "plotBands: [{");
    result.append(              "from: 0,");
    result.append(              "to: " + criticalPercent + ",");
    result.append(              "color: {");
    result.append(                  "linearGradient: { x1: 0, y1: 0, x2: 1, y2: 1 },");
    result.append(                  "stops: [");
    result.append(                         GRADIENT_COLORS[0]);
    result.append(                  "]");
    result.append(                  "},");
    result.append(                  "innerRadius: '70%',");
    result.append(                  "outerRadius: '100%'");
    result.append(          "}, {");
    result.append(              "from: " + criticalPercent + ",");
    result.append(              "to: " + satisfactoryPercent + ",");
    result.append(              "color: {");
    result.append(                      "linearGradient: { x1: 0, y1: 0, x2: 0, y2: 1 },");
    result.append(                      "stops: [");
    result.append(                         GRADIENT_COLORS[1]);
    result.append(                      "]");
    result.append(              "},");
    result.append(              "innerRadius: '70%',");
    result.append(              "outerRadius: '100%'");
    result.append(          "}, {");
    result.append(              "from: " + satisfactoryPercent + ",");
    result.append(              "to: 100,");
    result.append(              "color: {");
    result.append(                     "linearGradient: { x1:0, y1: 1, x2: 1, y2: 0 },");
    result.append(                     "stops: [");
    result.append(                         GRADIENT_COLORS[2]);
    result.append(                     "]");
    result.append(              "},");
    result.append(          "innerRadius: '70%',");
    result.append(          "outerRadius: '100%'");
    result.append(          "}],");
    result.append(          "pane: 0,");
    result.append(          "title: {");
    result.append(              "text: '',");
    result.append(              "y: 0");
    result.append(          "}");
    result.append(      "}],");
    result.append(      "plotOptions: {");
    result.append(          "gauge: {");
    result.append(              "enableMouseTracking: false,");
    result.append(              "dataLabels: {");
    result.append(                  "enabled: false");
    result.append(              "},");
    result.append(              "dial: {");
    result.append(                  "radius: '90%',");
    result.append(                  "backgroundColor: {");
    result.append(                      "linearGradient: { x1: 0, y1: 0, x2: 0, y2: 1 },");
    result.append(                      "stops: [");
    result.append(                          "[0, '#FFFFFF'],");
    result.append(                          "[1, '#C0C0C0']");
    result.append(                      "]");
    result.append(              "},");
    result.append(                 "borderColor: 'gray',");
    result.append(                 "borderWidth: 1,");
    result.append(                 "baseWidth: 15,");
    result.append(                 "topWidth: 1,");
    result.append(                 "baseLength: '0%', "); // of radius
    result.append(                 "rearLength: '10%'");
    result.append(              "}");
    result.append(          "}");
    result.append(      "},");
    result.append(      "series: [{");
    result.append(          "data: [");
    result.append(            "{");
    result.append(                  "id: 'projetado',");
    result.append(                  "y: (" + forecastPercent + " > 100 ? 100 : " + forecastPercent + "),");
    result.append(                  "dial: {");
    result.append(                      "radius: '100%',");
    result.append(                      "baseWidth: 1,");
    result.append(                      "rearLength: '0%',");
    result.append(                      "borderColor: 'black',");
    result.append(                      "borderWidth: (" + forecastPercent + " == 0 ? 0 : 1)");
    result.append(                  "}");
    result.append(          "}, {");
    result.append(              "id: 'realizado',");
    result.append(              "y: (" + actualPercent + " > 100 ? 100 : " + actualPercent + ")");
    result.append(          "}],");
    result.append(          "animation: true");
    result.append(      "}]");
    result.append(      "},");
    result.append(      "function(chart) {");
    result.append(      "});");
    result.append(      "});");
    result.append("</script>");
    // retorna
    return result.toString();
  }

}