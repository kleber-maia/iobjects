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
package iobjects.xml.ui;

import java.io.*;
import java.util.*;

import javax.xml.parsers.*;

import org.xml.sax.*;
import org.xml.sax.helpers.*;

import iobjects.ui.FlowChart;

/**
 * Responsável pela leitura dos arquivos XML de fluxogramas da aplicação.
 * <p>
 * Exemplo do formato do arquivo:
 * </p>
 * <p>
 * <pre>
 *  &lt;flowchart&gt;
 *    &lt;information caption="Exemplo" description="Fluxograma de negócios." /&gt;
 *    &lt;flow&gt;
 *      &lt;action name="action1" caption="" description="" kind="main" /&gt;
 *      &lt;action name="action2" caption="" description="" kind="secondary" /&gt;
 *      &lt;action name="action3" caption="" description="" kind="main" /&gt;
 *      &lt;action name="action4" caption="" description="" kind="assistant" /&gt;
 *      &lt;action name="action5" caption="" description="" kind="main" /&gt;
 *    &lt;/flow&gt;
 *    &lt;seealso&gt;
 *      &lt;action name="action6" /&gt;
 *      &lt;action name="action7" /&gt;
 *      &lt;action name="action8" /&gt;
 *    &lt;/seealso&gt;
 *  &lt;/flowchart&gt;
 * </pre>
 * </p>
 */
public class FlowChartFile extends DefaultHandler {

  /**
   * Representa o elemento "information" no arquivo.
   */
  public class Information {
    private String caption     = "";
    private String description = "";
    public Information(String caption, String description) {
      this.caption     = caption;
      this.description = description;
    }
    public String getCaption() {
      return caption;
    }
    public String getDescription() {
      return description;
    }
  }

  /**
   * Representa o elemento "action" no arquivo.
   */
  static public class Action {
    private String name        = "";
    private String caption     = "";
    private String description = "";
    private byte   kind        = 0;
    public Action(String name,
                  String caption,
                  String description,
                  byte   kind) {
      this.name        = name;
      this.caption     = caption;
      this.description = description;
      this.kind        = kind;
    }
    public String getName()        { return name; }
    public String getCaption()     { return caption; }
    public String getDescription() { return description; }
    public byte   getKind()        { return kind; }

  }

  /**
   * Representa o elemento "flow" no arquivo.
   */
  public class Flow {
    Vector vector = new Vector();
    public Flow() {
    }
    public void   add(Action action) { vector.add(action); }
    public void   clear() { vector.clear(); }
    public Action get(int index) { return (Action)vector.elementAt(index); }
    public int    size() { return vector.size(); }
  }

  private static final String FLOW_CHART  = "flowchart";
  private static final String INFORMATION = "information";
  private static final String CAPTION     = "caption";
  private static final String DESCRIPTION = "description";
  private static final String FLOW        = "flow";
  private static final String ACTION      = "action";
  private static final String NAME        = "name";
  private static final String KIND        = "kind";
  private static final String SECONDARY   = "secondary";
  private static final String ASSISTANT   = "assistant";
  private static final String SEE_ALSO    = "seealso";

  private Flow        flow        = new Flow();
  private Information information = null;
  private Vector      seeAlso     = new Vector();

  private String      parentNode  = "";

  /**
   * Construtor padrão.
   * @param flowChartFileName Nome do arquivo de conexão.
   * @throws Exception Em caso de exceção na tentativa de abertura do arquivo
   *                   XML especificado ou de inicialização do parser.
   */
  public FlowChartFile(String flowChartFileName) throws Exception {
    // nossa fábrica de parsers
    SAXParserFactory parserFactory = SAXParserFactory.newInstance();
    // não validaremos o documento
    parserFactory.setValidating(false);
    // sem suporte para XML namespaces
    parserFactory.setNamespaceAware(false);
    // cria o parser
    SAXParser saxParser = parserFactory.newSAXParser();
    // inicia a análise
    saxParser.parse(new File(flowChartFileName), this);
  }

  /**
   * Retorna um Information contendo as informações do elemento "information" do
   * arquivo de configuração.
   * @return Retorna um Information contendo as informações do elemento
   *         "information" do arquivo de configuração.
   */
  public Information information() {
    return information;
  }

  public void finalize() {
    flow.clear();
    flow        = null;
    information = null;
  }

  /**
   * Retorna um Flow contendo as informações do elemento "flow" do arquivo
   * de configuração.
   * @return FlowChart Retorna um Flow contendo as informações do elemento
   *                   "flow" do arquivo de configuração.
   */
  public Flow flow() {
    return flow;
  }

  /**
   * Retorna um String[] contendo as informações do elemento "seealso" do
   * arquivo de configuração.
   * @return String[] Retorna um String[] contendo as informações do elemento
   *                  "seealso" do arquivo de configuração.
   */
  public String[] seeAlso() {
    String[] result = new String[seeAlso.size()];
    seeAlso.copyInto(result);
    return result;
  }

  public void startElement(java.lang.String uri,
                           java.lang.String localName,
                           java.lang.String qName,
                           Attributes attributes) {
    // se encontramos o elemento "flowChart"
    if (qName.equalsIgnoreCase(INFORMATION)) {
      information = new Information(attributes.getValue(CAPTION), attributes.getValue(DESCRIPTION));
    }
    // se encontramos o elemento "flow"
    else if (qName.equalsIgnoreCase(FLOW)) {
      parentNode = FLOW;
    }
    // se encontramos o elemento "seealso"
    else if (qName.equalsIgnoreCase(SEE_ALSO)) {
      parentNode = SEE_ALSO;
    }
    // se encontramos um elemento "action" em "flow"
    else if (qName.equalsIgnoreCase(ACTION) && parentNode.equals(FLOW)) {
      // tipo do item
      byte kind = (attributes.getValue(KIND).equals(SECONDARY) ? FlowChart.KIND_SECONDARY : attributes.getValue(KIND).equals(ASSISTANT) ? FlowChart.KIND_ASSISTANT : FlowChart.KIND_MAIN);
      // adiciona o item
      flow.add(new Action(attributes.getValue(NAME),
                          attributes.getValue(CAPTION),
                          attributes.getValue(DESCRIPTION),
                          kind));
    } // if
    // se encontramos um elemento "action" em "seealso"
    else if (qName.equalsIgnoreCase(ACTION) && parentNode.equals(SEE_ALSO)) {
      // adiciona o item
      seeAlso.add(attributes.getValue(NAME));
    } // if
  }

}
