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

import iobjects.*;
import iobjects.util.*;

/**
 * Responsável pela leitura dos arquivos XML de configurações de estilo.
 * <p>
 * Exemplo do formato do arquivo:
 * </p>
 * <p>
 * <pre>
 * &lt;style&gt;
 *   &lt;css name="nome do estilo" userinterface="arquivo1.css" reportinterface="arquivo2.css" /&gt;
 * &lt;/style&gt;
 * </pre>
 * </p>
 */
public class StyleFile extends DefaultHandler {

  public class Style {
    private String content = "";
    public Style(String content) {
      this.content = content;
    }
    public ParamList get(String name) {
      // onde está o estilo desejado?
      int index = content.indexOf(name);
      // se não encontramos...dispara
      if (index < 0)
        return null;
      // seu começo e seu fim
      int begin = content.indexOf('{', index);
      int end   = content.indexOf('}', begin);
      // se não encontramos...dispara
      if ((begin < 0) || (end < 0))
        return null;
      // separa as propriedades
      String[] properties = content.substring(begin+1, end).split(";");
      // nosso resultado
      ParamList result = new ParamList();
      // loop nas propriedades
      for (int i=0; i<properties.length; i++) {
        String[] property = properties[i].split(":");
        if (property.length == 2)
          result.add(new Param(property[0].trim(), property[1].trim()));
      } // for
      // retorna
      return result;
    }
  }

  public class Css {
    private String name                 = "";
    private String reportInterface      = "";
    private Style  reportInterfaceStyle = null;
    private String userInterface        = "";
    private Style  userInterfaceStyle   = null;
    public Css(String name, String userInterface, Style userInterfaceStyle, String reportInterface, Style reportInterfaceStyle) {
      this.name = name;
      this.userInterface = userInterface;
      this.userInterfaceStyle = userInterfaceStyle;
      this.reportInterface = reportInterface;
      this.reportInterfaceStyle = reportInterfaceStyle;
    }
    public String getName() { return name; }
    public String getReportInterface() { return reportInterface; }
    public Style  getReportInterfaceStyle() { return reportInterfaceStyle; }
    public String getUserInterface() { return userInterface; }
    public Style  getUserInterfaceStyle() { return userInterfaceStyle; }
  }

  private static final String CSS              = "css";
  private static final String NAME             = "name";
  private static final String REPORT_INTERFACE = "reportinterface";
  private static final String USER_INTERFACE   = "userinterface";
  // *
  private Css    css        = new Css("", "", null, "", null);
  private String stylesPath = "";
  private String stylesUrl  = "";

  /**
   * Construtor padrão.
   * @param styleFileName Nome do arquivo de configuração de estilo.
   * @param stylesUrl Caminho relativo onde os arquivos de estilo podem ser
   *                  localizados.
   * @throws Exception Em caso de exceção na tentativa de abertura do arquivo
   *                   XML especificado ou de inicialização do parser.
   */
  public StyleFile(String styleFileName,
                   String stylesUrl) throws Exception {
    // nossos valores
    this.stylesUrl = stylesUrl;
    this.stylesPath = styleFileName.substring(0, styleFileName.lastIndexOf(File.separatorChar)+1);
    // nossa fábrica de parsers
    SAXParserFactory parserFactory = SAXParserFactory.newInstance();
    // não validaremos o documento
    parserFactory.setValidating(false);
    // sem suporte para XML namespaces
    parserFactory.setNamespaceAware(false);
    // cria o parser
    SAXParser saxParser = parserFactory.newSAXParser();
    // inicia a análise
    saxParser.parse(new File(styleFileName), this);
  }

  public Css css() {
    return css;
  }

  public void finalize() {
    css       = null;
    stylesUrl = null;
  }

  /**
   * Retorna a URL do arquivo CSS de interface para relatório.
   * @return String Retorna a URL do arquivo CSS de interface para relatório.
   */
  public String reportInterface() {
    return css.getReportInterface();
  }

  /**
   * Retorna a URL do arquivo CSS de interface para usuário.
   * @return String Retorna a URL do arquivo CSS de interface para usuário.
   */
  public String userInterface() {
    return css.getUserInterface();
  }

  public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
    // achamos o nó de informações?
    if (qName.equalsIgnoreCase(CSS)) {
      try {
        // carrega os arquivos
        String[] reportInterfaceContent = FileTools.loadTextFile(stylesPath + attributes.getValue(REPORT_INTERFACE));
        String[] userInterfaceContent = FileTools.loadTextFile(stylesPath + attributes.getValue(USER_INTERFACE));
        // cria o objeto
        css = new Css(attributes.getValue(NAME),
                      stylesUrl + "/" + attributes.getValue(USER_INTERFACE),
                      new Style(StringTools.arrayStringToString(userInterfaceContent, " ")),
                      stylesUrl + "/" + attributes.getValue(REPORT_INTERFACE),
                      new Style(StringTools.arrayStringToString(reportInterfaceContent, " ")));
      }
      catch (Exception e) {
        throw new RuntimeException(e);
      } // try-catch
    } // if
  }

}
