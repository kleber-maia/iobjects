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

import iobjects.util.*;

/**
 * Responsável pela leitura dos arquivos XML de configurações de portal.
 * <p>
 * Exemplo do formato do arquivo:
 * </p>
 * <p>
 * <pre>
 * &lt;portal&gt;
 *   &lt;site name="" sourceurl="" /&gt;
 * &lt;/portal&gt;
 * </pre>
 * </p>
 */
public class PortalFile extends DefaultHandler {

  public class Site {
    private String name      = "";
    private String sourceURL = "";
    public Site(String name, String sourceURL) {
      this.name = name;
      this.sourceURL = sourceURL;
    }
    public String getName()      { return name; }
    public String getSourceURL() { return sourceURL; }
  }

  /**
   * Constante que indica o nome do arquivo de configuração de portal.
   */
  static private final String PORTAL_FILE_NAME = "portal.xml";
  // *
  private static final String SITE       = "site";
  private static final String NAME       = "name";
  private static final String SOURCE_URL = "sourceurl";
  // *
  private Vector vector = new Vector();

  /**
   * Construtor padrão.
   * @param portalFilePath Caminho do arquivo de configuração de portal.
   * @throws Exception Em caso de exceção na tentativa de abertura do arquivo
   *                   XML especificado ou de inicialização do parser.
   */
  public PortalFile(String portalFilePath) throws Exception {
    // nossa fábrica de parsers
    SAXParserFactory parserFactory = SAXParserFactory.newInstance();
    // não validaremos o documento
    parserFactory.setValidating(false);
    // sem suporte para XML namespaces
    parserFactory.setNamespaceAware(false);
    // cria o parser
    SAXParser saxParser = parserFactory.newSAXParser();
    // inicia a análise
    saxParser.parse(new File(portalFilePath + PORTAL_FILE_NAME), this);
  }

  public void finalize() {
    vector.clear();
    vector = null;
  }

  /**
   * Retorna o site identificado por 'index'.
   * @param index int Índice do Site que se deseja retornar.
   * @return Site Retorna o site identificado por 'index'.
   */
  public Site get(int index) {
    return (Site)vector.get(index);
  }

  /**
   * Retorna o site identificado por 'name'.
   * @param name String Nome do Site que se deseja retornar.
   * @return Site Retorna o site identificado por 'name'.
   */
  public Site get(String name) {
    for (int i=0; i<vector.size(); i++) {
      Site site = get(i);
      if (site.getName().equals(name))
        return site;
    } // for
    return null;
  }

  /**
   * Retorna o Site cujo 'sourceUrl' está contido em 'url'.
   * @param url String URL cujo Site de origem se deseja localizar.
   * @return Site Retorna o Site cujo 'sourceUrl' está contido em 'url'.
   */
  public Site getByUrl(String url) {
    for (int i=0; i<vector.size(); i++) {
      Site site = get(i);
      if (url.indexOf(site.getSourceURL()) >= 0)
        return site;
    } // for
    return null;
  }

  /**
   * Retorna a quantidade de sites configurados no portal.
   * @return int Retorna a quantidade de sites configurados no portal.
   */
  public int size() {
    return vector.size();
  }

  public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
    // achamos um site?
    if (qName.equalsIgnoreCase(SITE)) {
      vector.add(new Site(attributes.getValue(NAME), attributes.getValue(SOURCE_URL)));
    } // if
  }

}
