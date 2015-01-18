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
 * Responsável pela leitura do arquivo XML de configurações da aplicação
 * indicado por RELEASE_NOTES_FILE_NAME.
 * <p>
 * Exemplo do formato do arquivo:
 * </p>
 * <p>
 * <pre>
 *   &lt;releasenotes&gt;
 *     &lt;release label="1.0" date="30/06/2006" emphasize="10/07/2006"&gt;
 *       &lt;change actionname="produto" beta="true"&gt;
 *         Incluído o campo Custo Médio do Produto.
 *       &lt;/change&gt;
 *     &lt;/release&gt;
 *   &lt;/releasenotes&gt;
 * </pre>
 * </p>
 */
public class ReleaseNotesFile extends DefaultHandler {

  static public class Change {
    private String  actionName  = "";
    private boolean beta        = false;
    private String  description = "";
    public Change(String actionName, boolean beta, String description) {
      this.actionName  = actionName;
      this.beta        = beta;
      this.description = description;
    }
    public String  getActionName()  { return actionName; }
    public String  getDescription() { return description; }
    public boolean isBeta()         { return beta; }
    public void    setDescription(String description) { this.description = description; }
  }

  static public class ChangeList {
    private Vector vector = new Vector();
    public ChangeList() {
    }
    public Change add(Change change) {
      if (!contains(change.getActionName()))
        vector.add(change);
      return change;
    }
    public boolean contains(String actionName) {
      return indexOf(actionName) >= 0;
    }
    public Change get(int index) {
      return (Change)vector.elementAt(index);
    }
    public Change get(String actionName) {
      int index = indexOf(actionName);
      if (index >=0)
        return get(index);
      else
        return null;
    }
    public int indexOf(String actionName) {
      for (int i=0; i<vector.size(); i++) {
        if (get(i).getActionName().equals(actionName))
          return i;
      } // for
      return -1;
    }
    public int size() {
      return vector.size();
    }
  }

  static public class Release {
    private String     label      = "";
    private Date       date       = null;
    private Date       emphasize  = null;
    private ChangeList changeList = new ChangeList();
    public Release(String label,
                   Date   date,
                   Date   emphasize) {
      this.label     = label;
      this.date      = date;
      this.emphasize = emphasize;
    }
    public ChangeList changeList()   { return changeList; }
    public Date       getDate()      { return date; }
    public Date       getEmphasize() { return emphasize; }
    public String     getLabel()     { return label; }
  }

  /**
   * Constante que indica o nome do arquivo de notas de versão da aplicação.
   */
  static private final String RELEASE_NOTES_FILE_NAME = "releasenotes.xml";
  // *
  static private final String ACTION_NAME             = "actionname";
  static private final String BETA                    = "beta";
  static private final String CHANGE                  = "change";
  static private final String DATE                    = "date";
  static private final String EMPHASIZE               = "emphasize";
  static private final String LABEL                   = "label";
  static private final String RELEASE                 = "release";
  // *
  private boolean   isChange    = false;
  private Change    change      = null;
  private Release   release     = null;
  private Vector    vector      = new Vector();

  /**
   * Construtor padrão.
   * @param releaseNotesFilePath Caminho local onde se encontra o arquivo de
   *                             notas de versão da aplicação.
   *                             Veja RELEASE_NOTES_FILE_NAME.
   * @throws Exception Em caso de exceção na tentativa de abertura do arquivo
   *                   XML especificado ou de inicialização do parser.
   */
  public ReleaseNotesFile(String releaseNotesFilePath) throws Exception {
    // nossa fábrica de parsers
    SAXParserFactory parserFactory = SAXParserFactory.newInstance();
    // não validaremos o documento
    parserFactory.setValidating(false);
    // sem suporte para XML namespaces
    parserFactory.setNamespaceAware(false);
    // cria o parser
    SAXParser saxParser = parserFactory.newSAXParser();
    // inicia a análise
    saxParser.parse(new File(releaseNotesFilePath + RELEASE_NOTES_FILE_NAME), this);
  }

  public void characters(char[] ch, int start, int length) throws SAXException {
    // se é uma mudança...
    if (isChange) {
      // pega sua descrição transformando os caracteres especiais
      String description = new String(ch, start, length).replace('{', '<').replace('}', '>');
      // soma a descrição ao último aviso inserido
      change.setDescription(change.getDescription() + description);
    } // if
  }

  public void finalize() {
    change = null;
    release = null;
    vector.clear();
    vector = null;
  }

  public Release get(int index) {
    return (Release)vector.elementAt(index);
  }

  public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
    // achamos uma nova versão?
    if (qName.equalsIgnoreCase(RELEASE)) {
      isChange = false;
      release = new Release(attributes.getValue(LABEL),
                            DateTools.parseDate(attributes.getValue(DATE)),
                            DateTools.parseDate(attributes.getValue(EMPHASIZE)));
      vector.add(release);
    }
    // achamos uma nova mudança?
    else if (qName.equalsIgnoreCase(CHANGE)) {
      isChange = true;
      String strBeta = attributes.getValue(BETA);
      change = release.changeList().add(new Change(attributes.getValue(ACTION_NAME), (strBeta != null && strBeta.equalsIgnoreCase("true")), ""));
    } // if
  }

  public int size() {
    return vector.size();
  }

}
