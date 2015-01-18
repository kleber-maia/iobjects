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
import javax.xml.parsers.*;

import org.xml.sax.*;
import org.xml.sax.helpers.*;

import iobjects.*;

/**
 * Responsável pela leitura do arquivo XML de configurações de constraints
 * especiais de parâmetros indicado por PARAM_FILE_NAME.
 * <p>
 * Exemplo do formato do arquivo:
 * </p>
 * <p>
 * <pre>
 * &lt;param&gt;
 *   &lt;specialconstraints maskchars="%*?"
 *                          minimumlength="3"
 *                          maximumperiod="60" /&gt;
 * &lt;/param&gt;
 * </pre>
 * </p>
* @since 2006
 */
public class ParamFile extends DefaultHandler {

  public class SpecialConstraints {
    private String maskChars     = "";
    private int    minimumLength = 0;
    private int    maximumPeriod = 0;
    // *
    public SpecialConstraints(String   maskChars,
                              int      minimumLength,
                              int      maximumPeriod) {
      this.maskChars = maskChars;
      this.minimumLength = minimumLength;
      this.maximumPeriod = maximumPeriod;
    }
    // *
    public String getMaskChars() {return maskChars;};
    public int    getMaximumPeriod() {return maximumPeriod;};
    public int    getMinimumLength() {return minimumLength;};
  }

  /**
   * Constante que indica o nome do arquivo de configuração de parâmetros.
   */
  private static final String PARAM_FILE_NAME = "param.xml";
  // *
  private static final String SPECIAL_CONSTRAINTS = "specialconstraints";
  private static final String MASK_CHARS          = "maskchars";
  private static final String MAXIMUM_PERIOD      = "maximumperiod";
  private static final String MINIMUM_LENGTH      = "minimumlength";

  private SpecialConstraints specialConstraints = null;

  /**
   * Construtor padrão.
   * @param paramFilePath Caminho local onde se encontra o arquivo de
   *                      configurações de parâmetros. Veja PARAM_FILE_NAME.
   * @throws Exception Em caso de exceção na tentativa de abertura do arquivo
   *                   XML especificado ou de inicialização do parser.
   */
  public ParamFile(String paramFilePath) throws Exception {
    // nossa fábrica de parsers
    SAXParserFactory parserFactory = SAXParserFactory.newInstance();
    // não validaremos o documento
    parserFactory.setValidating(false);
    // sem suporte para XML namespaces
    parserFactory.setNamespaceAware(false);
    // cria o parser
    SAXParser saxParser = parserFactory.newSAXParser();
    // inicia a análise
    saxParser.parse(new File(paramFilePath + PARAM_FILE_NAME), this);
  }

  public SpecialConstraints specialConstraints() {
    return specialConstraints;
  }

  public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
    if (qName.equalsIgnoreCase(SPECIAL_CONSTRAINTS)) {
      try {
        specialConstraints = new SpecialConstraints(attributes.getValue(MASK_CHARS),
                                                    Integer.parseInt(attributes.getValue(MINIMUM_LENGTH)),
                                                    Integer.parseInt(attributes.getValue(MAXIMUM_PERIOD)));
      }
      catch (Exception e) {
        throw new RuntimeException(new ExtendedException(getClass().getName(), "startElement", e));
      } // try-catch
    } // if
  }

}
