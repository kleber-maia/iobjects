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
 * Responsável pela leitura dos arquivos XML de dicionário de sinônimos.
 * <p>
 * Exemplo do formato do arquivo:
 * </p>
 * <p>
 * <pre>
 * &lt;noticeboard&gt;
 *   &lt;notice warning="false" date="01/01/2006" emphasize="10/01/2006" title="Título"&gt;
 *   Descrição do aviso com as possibilidades: {b}negrito{/b},
 *   {i}itálico{/i}, {u}sublinhado{/u}, {span style="color:#ff0000;"}colorido{/span},
 *   {a href="www.softgroup.com.br" target="__blank"}hyperlink{/a}, etc.
 *   &lt;/notice&gt;
 * &lt;/noticeboard&gt;
 * </pre>
 * </p>
 * @since 2006 R1
 */
public class NoticeBoardFile extends DefaultHandler {

  static public class Notice {
    private Date    date        = null;
    private Date    emphasize   = null;
    private String  description = "";
    private String  title       = "";
    private boolean warning    = false;
    public Notice(boolean warning, Date date, Date emphasize, String title) {
      this.warning = warning;
      this.date = date;
      this.emphasize = emphasize;
      this.title = title;
    }
    public Date    getDate()        {return date;}
    public Date    getEmphasize()   {return emphasize;}
    public String  getDescription() {return description;}
    public String  getTitle()       {return title;}
    public boolean getWarning()     {return warning;}
    public boolean isVisible()      {Date today = DateTools.getActualDate(); return !date.after(today) && !emphasize.before(today);}
    public void    setDescription(String description) {this.description = description;}
  }

  private static final String DATE      = "date";
  private static final String EMPHASIZE = "emphasize";
  private static final String NOTICE    = "notice";
  private static final String TITLE     = "title";
  private static final String WARNING   = "warning";

  private boolean isNotice = false;
  private String name      = "";
  private Vector vector    = new Vector();

  /**
   * Construtor padrão.
   * @param noticeBoardFileName Nome do arquivo de quadro de avisos.
   * @throws Exception Em caso de exceção na tentativa de abertura do arquivo
   *                   XML especificado ou de inicialização do parser.
   */
  public NoticeBoardFile(String noticeBoardFileName) throws Exception {
    // nossa fábrica de parsers
    SAXParserFactory parserFactory = SAXParserFactory.newInstance();
    // não validaremos o documento
    parserFactory.setValidating(false);
    // sem suporte para XML namespaces
    parserFactory.setNamespaceAware(false);
    // cria o parser
    SAXParser saxParser = parserFactory.newSAXParser();
    // inicia a análise
    saxParser.parse(new File(noticeBoardFileName), this);
    // guarda nosso nome
    File file = new File(noticeBoardFileName);
    name = file.getName().substring(0, file.getName().indexOf("."));
  }

  public void characters(char[] ch, int start, int length) throws SAXException {
    // se é um aviso...
    if (isNotice) {
      // pega sua descrição transformando os caracteres especiais
      String description = new String(ch, start, length).replace('{', '<').replace('}', '>');
      // soma a descrição ao último aviso inserido
      Notice notice = get(size() - 1);
      notice.setDescription(notice.getDescription() + description);
    } // if
  }

  public void finalize() {
    vector.clear();
    vector = null;
  }

  /**
   * Retorna o aviso identificado por 'index'.
   * @param index int Índice do aviso que se deseja retornar.
   * @return Notice Retorna o aviso identificado por 'index'.
   */
  public Notice get(int index) {
    return (Notice)vector.elementAt(index);
  }

  /**
   * Retorna o nome do dicionário de sinônimos.
   * @return String Retorna o nome do dicionário de sinônimos.
   */
  public String getName() {
    return name;
  }

  public void startElement(java.lang.String uri,
                           java.lang.String localName,
                           java.lang.String qName,
                           Attributes attributes) {
    // se encontramos o elemento "notice"
    if (qName.equalsIgnoreCase(NOTICE)) {
      isNotice = true;
      vector.add(new Notice(Boolean.valueOf(attributes.getValue(WARNING)).booleanValue(),
                            DateTools.parseDate(attributes.getValue(DATE)),
                            DateTools.parseDate(attributes.getValue(EMPHASIZE)),
                            attributes.getValue(TITLE)));
    }
    // se é outro elemento
    else
      isNotice = false;
  }

  /**
   * Retorna a quantidade de avisos existentes.
   * @return int Retorna a quantidade de avisos existentes.
   */
  public int size() {
    return vector.size();
  }

}
