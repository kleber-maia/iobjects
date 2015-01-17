package iobjects.xml.ui;

import java.io.*;
import java.util.*;

import javax.xml.parsers.*;

import org.xml.sax.*;
import org.xml.sax.helpers.*;

import iobjects.util.*;

/**
 * Responsável pela leitura dos arquivos XML de propaganda.
 * <p>
 * Exemplo do formato do arquivo:
 * </p>
 * <p>
 * <pre>
 * &lt;!--
 *   Informações sobre a tag &lt;media /&gt;:
 *   filename.: nome do arquivo de mídia a ser exibido. Utilizar caminho
 *              relativo ao diretório deste XML.
 *   type.....: "image" ou "flash".
 *   interval.: tempo de exibição da mídia em segundos.
 *   href.....: url que será aberta se o usuário clicar na media.
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
   * Construtor padrão.
   * @param advertisingFileName Nome do arquivo de propaganda.
   * @throws Exception Em caso de exceção na tentativa de abertura do arquivo
   *                   XML especificado ou de inicialização do parser.
   */
  public AdvertisingFile(String advertisingFileName) throws Exception {
    // nossa fábrica de parsers
    SAXParserFactory parserFactory = SAXParserFactory.newInstance();
    // não validaremos o documento
    parserFactory.setValidating(false);
    // sem suporte para XML namespaces
    parserFactory.setNamespaceAware(false);
    // cria o parser
    SAXParser saxParser = parserFactory.newSAXParser();
    // inicia a análise
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
   * @param index int Índice da media que se deseja retornar.
   * @return Media Retorna a media identificada por 'index'.
   */
  public Media get(int index) {
    return (Media)vector.elementAt(index);
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
