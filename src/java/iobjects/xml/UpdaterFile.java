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
package iobjects.xml;

import java.io.*;
import java.util.*;
import java.net.*;
import javax.xml.parsers.*;

import org.xml.sax.*;
import org.xml.sax.helpers.*;

import iobjects.*;
import iobjects.util.*;

import iobjects.xml.*;

/**
 * Responsável pela leitura do arquivo XML de configurações do atualizador
 * da aplicação, indicado por UPDATER_FILE_NAME.
 * <p>
 * Exemplo do formato do arquivo:
 * </p>
 * <p>
 * <pre>
 * &lt;updater&gt;
 *   &lt;repository url="http://www.softgroup.com.br/update.repository/" /&gt;
 *   &lt;installed&gt;
 *     &lt;update type="" applicationpackage="" workpackage="" date="" /&gt;
 *   &lt;/installed&gt;
 * &lt;/updater&gt;
 * </pre>
 * </p>
 * @since 2006 R1
 */
public class UpdaterFile extends DefaultHandler {

  /**
   * Constante que indica o nome do arquivo de configurações do atualizador.
   */
  static public final String UPDATER_FILE_NAME = "updater.xml";
  // *
  static private final String REPOSITORY          = "repository";
  static private final String URL                 = "url";
  // *
  static private final String[] TYPES = {"complete", "incremental"};
  static public  final int TYPE_COMPLETE    = 0;
  static public  final int TYPE_INCREMENTAL = 1;
  // *
  private String repositoryURL   = "";
  private Vector vector          = new Vector();
  private String updaterFileName = "";

  /**
   * Construtor padrão.
   * @param updaterFilePath Caminho onde se encontra o arquivo de configurações
   *                        do atualizador da aplicação. Veja UPDATER_FILE_NAME.
   * @throws Exception Em caso de exceção na tentativa de abertura do arquivo
   *                   XML especificado ou de inicialização do parser.
   */
  public UpdaterFile(String updaterFilePath) throws Exception {
    // guarda o nome do nosso arquivo
    updaterFileName = updaterFilePath + UPDATER_FILE_NAME;
    // faz a análise
    parse();
  }

  /**
   * Insere uma tag &lt;update&gt; no arquivo de configurações do atualizador,
   * indicando que a atualização foi instalada.
   * @param type int Tipo da atualização.
   * @param applicationPackage String Nome do pacote instalado.
   * @param workPackage String Nome do pacote instalado.
   * @param date Date Date de instalação.
   * @throws Exception Em caso de exceção na tentativa de ler ou escrever o
   *                   arquivo de configurações.
   */
  public void insertInstalledUpdate(int    type,
                                    String applicationPackage,
                                    String workPackage,
                                    Date   date) throws Exception {
    // carrega o XML atual
    String[] lines = FileTools.loadTextFile(updaterFileName);
    // insere a linha referente a atualização instalada
    String[] firstLines = StringTools.arrayGetElements(lines, 0, lines.length-3);
    // *
    String   newLine    = "    <" + RepositoryFile.UPDATE + " " + RepositoryFile.TYPE + "=\"" + RepositoryFile.TYPES[type] + "\" " + RepositoryFile.APPLICATION_PACKAGE + "=\"" + applicationPackage + "\" " + RepositoryFile.WORK_PACKAGE + "=\"" + workPackage + "\" " + RepositoryFile.DATE + "=\"" + DateTools.formatDate(date) + "\" />";
    // *
    String[] lastLines = new String[3];
             lastLines[0] = newLine;
             lastLines[1] = lines[lines.length-2];
             lastLines[2] = lines[lines.length-1];
    // salva tudo
    FileTools.saveTextFile(updaterFileName, StringTools.arrayConcat(firstLines, lastLines));
    // refaz a análise
    vector.clear();
    parse();
  }

  public void finalize() {
    vector.clear();
    vector = null;
    repositoryURL = null;
  }

  private void parse() throws Exception {
    // nossa fábrica de parsers
    SAXParserFactory parserFactory = SAXParserFactory.newInstance();
    // não validaremos o documento
    parserFactory.setValidating(false);
    // sem suporte para XML namespaces
    parserFactory.setNamespaceAware(false);
    // cria o parser
    SAXParser saxParser = parserFactory.newSAXParser();
    // guarda o nome do nosso arquivo
    updaterFileName = updaterFileName;
    // inicia a análise
    saxParser.parse(new File(updaterFileName), this);
  }

  public String repositoryURL() {
    return repositoryURL;
  }

  public RepositoryFile.Update[] installedUpdateList() {
    RepositoryFile.Update[] result = new RepositoryFile.Update[vector.size()];
    vector.copyInto(result);
    return result;
  }

  public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
    // achamos o nó de repository?
    if (qName.equalsIgnoreCase(REPOSITORY))
      repositoryURL = attributes.getValue(URL);
    // achamos um nó de update?
    else if (qName.equalsIgnoreCase(RepositoryFile.UPDATE)) {
      vector.add(new RepositoryFile.Update(StringTools.arrayIndexOf(RepositoryFile.TYPES, attributes.getValue(RepositoryFile.TYPE)),
                                           attributes.getValue(RepositoryFile.APPLICATION_PACKAGE),
                                           attributes.getValue(RepositoryFile.WORK_PACKAGE),
                                           DateTools.parseDate(attributes.getValue(RepositoryFile.DATE))));
    } // if
  }

}
