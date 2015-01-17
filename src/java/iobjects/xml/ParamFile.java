package iobjects.xml;

import java.io.*;
import javax.xml.parsers.*;

import org.xml.sax.*;
import org.xml.sax.helpers.*;

import iobjects.*;

/**
 * Respons�vel pela leitura do arquivo XML de configura��es de constraints
 * especiais de par�metros indicado por PARAM_FILE_NAME.
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
   * Constante que indica o nome do arquivo de configura��o de par�metros.
   */
  private static final String PARAM_FILE_NAME = "param.xml";
  // *
  private static final String SPECIAL_CONSTRAINTS = "specialconstraints";
  private static final String MASK_CHARS          = "maskchars";
  private static final String MAXIMUM_PERIOD      = "maximumperiod";
  private static final String MINIMUM_LENGTH      = "minimumlength";

  private SpecialConstraints specialConstraints = null;

  /**
   * Construtor padr�o.
   * @param paramFilePath Caminho local onde se encontra o arquivo de
   *                      configura��es de par�metros. Veja PARAM_FILE_NAME.
   * @throws Exception Em caso de exce��o na tentativa de abertura do arquivo
   *                   XML especificado ou de inicializa��o do parser.
   */
  public ParamFile(String paramFilePath) throws Exception {
    // nossa f�brica de parsers
    SAXParserFactory parserFactory = SAXParserFactory.newInstance();
    // n�o validaremos o documento
    parserFactory.setValidating(false);
    // sem suporte para XML namespaces
    parserFactory.setNamespaceAware(false);
    // cria o parser
    SAXParser saxParser = parserFactory.newSAXParser();
    // inicia a an�lise
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
