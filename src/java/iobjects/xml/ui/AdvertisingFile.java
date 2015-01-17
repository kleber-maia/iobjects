package iobjects.xml.ui;

import java.io.*;
import java.util.*;

import javax.xml.parsers.*;

import org.xml.sax.*;
import org.xml.sax.helpers.*;

import iobjects.util.*;

/**
 * Respons�vel pela leitura dos arquivos XML de propaganda.
 * <p>
 * Exemplo do formato do arquivo:
 * </p>
 * <p>
 * <pre>
 * &lt;!--
 *   Informa��es sobre a tag &lt;media /&gt;:
 *   filename.: nome do arquivo de m�dia a ser exibido. Utilizar caminho
 *              relativo ao diret�rio deste XML.
 *   type.....: "image" ou "flash".
 *   interval.: tempo de exibi��o da m�dia em segundos.
 *   href.....: url que ser� aberta se o usu�rio clicar na media.
 * --&gt;
 *
 * &lt;advertising&gt;
 *   &lt;media filename="" type="" interval="" href="" /&gt;
 * &lt;/advertising&gt;
 * </pre>
 * </p>
 * @since 2006 R1
 */
public class AdvertisingFile extends DefaultHandler {

  static public final String TYPE_FLASH = "flash";
  static public final String TYPE_IMAGE = "image";

  public class Media {
    private String  filename = "";
    private String  type     = "";
    private int     interval = 0;
    private String  href     = "";
    private String  url      = "";
    public Media(String filename, String type, int interval, String href) {
      this.filename = filename;
      this.type     = type;
      this.interval = interval;
      this.href     = href;
    }
    public String  getFilename()      {return filename;}
    public String  getType()          {return type;}
    public int     getInterval()      {return interval;}
    public String  getHref()          {return href;}
    public String  getUrl()           {return url;}
    public void    setUrl(String url) {this.url = url;}
  }

  private static final String MEDIA     = "media";
  private static final String FILENAME  = "filename";
  private static final String TYPE      = "type";
  private static final String INTERVAL  = "interval";
  private static final String HREF      = "href";

  private String name   = "";
  private Vector vector = new Vector();

  /**
   * Construtor padr�o.
   * @param advertisingFileName Nome do arquivo de propaganda.
   * @throws Exception Em caso de exce��o na tentativa de abertura do arquivo
   *                   XML especificado ou de inicializa��o do parser.
   */
  public AdvertisingFile(String advertisingFileName) throws Exception {
    // nossa f�brica de parsers
    SAXParserFactory parserFactory = SAXParserFactory.newInstance();
    // n�o validaremos o documento
    parserFactory.setValidating(false);
    // sem suporte para XML namespaces
    parserFactory.setNamespaceAware(false);
    // cria o parser
    SAXParser saxParser = parserFactory.newSAXParser();
    // inicia a an�lise
    saxParser.parse(new File(advertisingFileName), this);
    // guarda nosso nome
    File file = new File(advertisingFileName);
    name = file.getName().substring(0, file.getName().indexOf("."));
  }

  public void finalize() {
    vector.clear();
    vector = null;
  }

  /**
   * Retorna a media  identificada por 'index'.
   * @param index int �ndice da media que se deseja retornar.
   * @return Media Retorna a media identificada por 'index'.
   */
  public Media get(int index) {
    return (Media)vector.elementAt(index);
  }

  /**
   * Retorna o nome do dicion�rio de sin�nimos.
   * @return String Retorna o nome do dicion�rio de sin�nimos.
   */
  public String getName() {
    return name;
  }

  public void startElement(java.lang.String uri,
                           java.lang.String localName,
                           java.lang.String qName,
                           Attributes attributes) {
    // se encontramos o elemento "notice"
    try {
      if (qName.equalsIgnoreCase(MEDIA)) {
        vector.add(new Media(attributes.getValue(FILENAME),
                             attributes.getValue(TYPE),
                             NumberTools.parseInt(attributes.getValue(INTERVAL)),
                             attributes.getValue(HREF)));
      } // if
    }
    catch (Exception e) {
      throw new RuntimeException(e);
    } // try-catch
  }

  /**
   * Retorna a quantidade de avisos existentes.
   * @return int Retorna a quantidade de avisos existentes.
   */
  public int size() {
    return vector.size();
  }

}
