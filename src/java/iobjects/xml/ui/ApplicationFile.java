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
 * indicado por APPLICATION_FILE_NAME.
 * <p>
 * Exemplo do formato do arquivo:
 * </p>
 * <p>
 * <pre>
 * &lt;application&gt;
 *   &lt;information name="nome" title="titulo" url="www.site.com.br" /&gt;
 *   &lt;logo filename="logo.png" width="54" height="54" /&gt;
 *   &lt;icon filename="icon.png" /&gt;
 *   &lt;systeminformation showtoallusers="false" showtoprivilegedusers="true" /&gt;
 *   &lt;noticeboard showtoallusers="false" showtoprivilegedusers="true" /&gt;
 *   &lt;defaultconnection getonlogontime="true" caption="Empresa" /&gt;
 *   &lt;displaymode allowbasicmode="false" /&gt;
 *   &lt;demo showonlogontime="false" caption="titulo" /&gt;
 * &lt;/application&gt;
 * </pre>
 * </p>
 */
public class ApplicationFile extends DefaultHandler {

  public class Demo {
    private String  caption         = "";
    private boolean showOnLogonTime = false;
    public Demo(boolean showOnLogonTime,
                String  caption) {
      this.showOnLogonTime = showOnLogonTime;
      this.caption = caption;
    }
    public String  getCaption() { return caption; }
    public boolean showOnLogonTime() { return showOnLogonTime; }
  }

  public class DefaultConnection {
    private String  caption        = "";
    private boolean getOnLogonTime = false;
    public DefaultConnection(boolean getOnLogonTime,
                             String  caption) {
      this.getOnLogonTime = getOnLogonTime;
      this.caption = caption;
    }
    public String  getCaption() { return caption; }
    public boolean getOnLogonTime() { return getOnLogonTime; }
  }

  public class DisplayMode {
    private boolean allowBasicMode = false;
    public DisplayMode(boolean allowBasicMode) {
      this.allowBasicMode = allowBasicMode;
    }
    public boolean getAllowBasicMode() { return allowBasicMode; }
  }

  public class Information {
    private String name  = "";
    private String title = "";
    private String url   = "";
    public Information(String name, String title, String url) {
      this.name         = name;
      this.title        = title;
      this.url          = url;
    }
    public String getName() { return name; }
    public String getTitle() { return title; }
    public String getURL() { return url; }
  }

  public class Icon {
    private String fileName = "";
    public Icon(String fileName) {
      this.fileName = fileName;
    }
    public String getFileName() { return fileName; }
  }

  public class Logo {
    private String fileName = "";
    private int    height   = 0;
    private int    width    = 0;
    public Logo(String fileName, int width, int height) {
      this.fileName = fileName;
      this.width    = width;
      this.height   = height;
    }
    public String getFileName() { return fileName; }
    public int    getHeight() { return height; }
    public int    getWidth() { return width; }
  }

  public class NoticeBoard {
    private boolean showToAllUsers        = false;
    private boolean showToPrivilegedUsers = false;
    public NoticeBoard(boolean showToAllUsers,
                       boolean showToPrivilegedUsers) {
      this.showToAllUsers = showToAllUsers;
      this.showToPrivilegedUsers = showToPrivilegedUsers;
    }
    public boolean getShowToAllUsers() { return showToAllUsers; }
    public boolean getShowToPrivilegedUsers() { return showToPrivilegedUsers; }
  }

  public class Schedule {
    private boolean active = false;
    public Schedule(boolean active) {
      this.active = active;
    }
    public boolean getActive() { return active; }
  }

  public class SystemInformation {
    private boolean showToAllUsers        = false;
    private boolean showToPrivilegedUsers = false;
    public SystemInformation(boolean showToAllUsers,
                             boolean showToPrivilegedUsers) {
      this.showToAllUsers = showToAllUsers;
      this.showToPrivilegedUsers = showToPrivilegedUsers;
    }
    public boolean getShowToAllUsers() { return showToAllUsers; }
    public boolean getShowToPrivilegedUsers() { return showToPrivilegedUsers; }
  }

  /**
   * Constante que indica o nome do arquivo de configuração da aplicação.
   */
  static private final String APPLICATION_FILE_NAME = "application.xml";
  // *
  static private final String ALLOW_BASIC_MODE         = "allowbasicmode";
  static private final String CAPTION                  = "caption";
  static private final String DEFAULT_CONNECTION       = "defaultconnection";
  static private final String DEMO                     = "demo";
  static private final String DISPLAY_MODE             = "displaymode";
  static private final String FILE_NAME                = "filename";
  static private final String GET_ON_LOGON_TIME        = "getonlogontime";
  static private final String INFORMATION              = "information";
  static private final String HEIGHT                   = "height";
  static private final String ICON                     = "icon";
  static private final String LOGO                     = "logo";
  static private final String NAME                     = "name";
  static private final String NOTICE_BOARD             = "noticeboard";
  static private final String SHOW_ON_LOGON_TIME       = "showonlogontime";
  static private final String SHOW_TO_ALL_USERS        = "showtoallusers";
  static private final String SHOW_TO_PRIVILEGED_USERS = "showtoprivilegedusers";
  static private final String SYSTEM_INFORMATION       = "systeminformation";
  static private final String TITLE                    = "title";
  static private final String URL                      = "url";
  static private final String WIDTH                    = "width";
  // *
  private DefaultConnection defaultConnection = new DefaultConnection(false, "");
  private Demo              demo              = new Demo(false, "");
  private DisplayMode       displayMode       = new DisplayMode(false);
  private Information       information       = new Information("", "", "");
  private Icon              icon              = new Icon("");
  private Logo              logo              = new Logo("", 0, 0);
  private NoticeBoard       noticeBoard       = new NoticeBoard(false, false);
  private SystemInformation systemInformation = new SystemInformation(false, false);

  /**
   * Construtor padrão.
   * @param applicationFilePath Caminho local onde se encontra o arquivo de
   *                        configurações da aplicação. Veja APPLICATION_FILE_NAME.
   * @throws Exception Em caso de exceção na tentativa de abertura do arquivo
   *                   XML especificado ou de inicialização do parser.
   */
  public ApplicationFile(String applicationFilePath) throws Exception {
    // nossa fábrica de parsers
    SAXParserFactory parserFactory = SAXParserFactory.newInstance();
    // não validaremos o documento
    parserFactory.setValidating(false);
    // sem suporte para XML namespaces
    parserFactory.setNamespaceAware(false);
    // cria o parser
    SAXParser saxParser = parserFactory.newSAXParser();
    // inicia a análise
    saxParser.parse(new File(applicationFilePath + APPLICATION_FILE_NAME), this);
  }

  public DefaultConnection defaultConnection() {
    return defaultConnection;
  }

  public Demo demo() {
    return demo;
  }

  public DisplayMode displayMode() {
    return displayMode;
  }

  public void finalize() {
    defaultConnection = null;
    information       = null;
    icon              = null;
    logo              = null;
    systemInformation = null;
  }

  public Information information() {
    return information;
  }

  public Icon icon() {
    return icon;
  }

  public Logo logo() {
    return logo;
  }

  public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
    // achamos o nó de conexão default?
    if (qName.equalsIgnoreCase(DEFAULT_CONNECTION)) {
      defaultConnection = new DefaultConnection(Boolean.valueOf(attributes.getValue(GET_ON_LOGON_TIME)).booleanValue(),
                                                attributes.getValue(CAPTION));
    } // if
    // achamos o nó de demonstração?
    else if (qName.equalsIgnoreCase(DEMO)) {
      demo = new Demo(Boolean.valueOf(attributes.getValue(SHOW_ON_LOGON_TIME)).booleanValue(),
                      attributes.getValue(CAPTION));
    } // if
    // achamos o nó de informações?
    else if (qName.equalsIgnoreCase(INFORMATION)) {
      information = new Information(attributes.getValue(NAME),
                                    attributes.getValue(TITLE),
                                    attributes.getValue(URL));
    } // if
    // achamos o nó de icon?
    else if (qName.equalsIgnoreCase(ICON)) {
      try {
        icon = new Icon(attributes.getValue(FILE_NAME));
      }
      catch (Exception e) {
        throw new RuntimeException(new ExtendedException(getClass().getName(), "startElement", e));
      } // try-catch
    } // if
    // achamos o nó de logo?
    else if (qName.equalsIgnoreCase(LOGO)) {
      try {
        logo = new Logo(attributes.getValue(FILE_NAME),
                        NumberTools.parseInt(attributes.getValue(WIDTH)),
                        NumberTools.parseInt(attributes.getValue(HEIGHT)));
      }
      catch (Exception e) {
        throw new RuntimeException(new ExtendedException(getClass().getName(), "startElement", e));
      } // try-catch
    } // if
    // achamos o nó de quadro de avisos?
    else if (qName.equalsIgnoreCase(NOTICE_BOARD)) {
      noticeBoard = new NoticeBoard(Boolean.valueOf(attributes.getValue(SHOW_TO_ALL_USERS)).booleanValue(),
                                    Boolean.valueOf(attributes.getValue(SHOW_TO_PRIVILEGED_USERS)).booleanValue());
    } // if
    // achamos o nó de informações do sistema?
    else if (qName.equalsIgnoreCase(SYSTEM_INFORMATION)) {
      systemInformation = new SystemInformation(Boolean.valueOf(attributes.getValue(SHOW_TO_ALL_USERS)).booleanValue(),
                                                Boolean.valueOf(attributes.getValue(SHOW_TO_PRIVILEGED_USERS)).booleanValue());
    } // if
    // achamos o nó de modo de exibição?
    else if (qName.equalsIgnoreCase(DISPLAY_MODE)) {
      displayMode = new DisplayMode(Boolean.valueOf(attributes.getValue(ALLOW_BASIC_MODE)).booleanValue());
    } // if
  }

  public NoticeBoard noticeBoard() {
    return noticeBoard;
  }

  public SystemInformation systemInformation() {
    return systemInformation;
  }

}
