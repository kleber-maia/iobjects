package iobjects.xml.mail;

import java.io.*;
import java.util.*;
import javax.xml.parsers.*;

import org.xml.sax.*;
import org.xml.sax.helpers.*;

import iobjects.util.*;
import iobjects.schedule.*;
import iobjects.schedule.Scheduleable.RunStatus;

/**
 * Responsável pela leitura do arquivo XML de configurações de e-mail
 * indicado por MAIL_SERVICE_FILE_NAME.
 * <p>
 * Exemplo do formato do arquivo:
 * </p>
 * <p>
 * <pre>
 * &lt;mailservice&gt;
 *   &lt;pop3 active="true"
 *         interval="5"
 *         hostname="pop3.hostname.com.br"
 *         port="-1"
 *         username="username"
 *         password="password"
 *         ssl="false" /&gt;
 *   &lt;smtp active="true"
 *         hostname="smtp.hostname.com.br"
 *         port="-1"
 *         username="username"
 *         password="password"
 *         ssl="false"
 *         fromaddress="from@hostname.com.br"
 *         adminaddress="admin@hostname.com.br"
 *         supportaddress="support@hostname.com.br" /&gt;
 * &lt;/mailservice&gt;
 * </pre>
 * </p>
 */
public class MailServiceFile extends DefaultHandler {

  public class Pop3 {
    private boolean active    = false;
    private int     interval  = 5;
    private String  hostName  = "";
    private String  password  = "";
    private int     port      = -1;
    private boolean ssl       = false;
    private String  userName  = "";
    public Pop3() {
    }
    public Pop3(boolean active,
                int     interval,
                String  hostName,
                int     port,
                String  userName,
                String  password,
                boolean ssl) {
      this.active = active;
      this.interval = interval;
      this.hostName = hostName;
      this.port = port;
      this.userName = userName;
      this.password = password;
      this.ssl = ssl;
    }
    public boolean getActive() { return active; }
    public int getInterval() { return interval; }
    public String getHostName() { return hostName; }
    public String getPassword() { return password; }
    public int getPort() { return port; }
    public boolean getSSL() { return ssl; }
    public String getUserName() { return userName; }
  }

  public class Smtp {
    private boolean active         = false;
    private String  adminAddress   = "";
    private String  fromAddress    = "";
    private String  fromName       = "";
    private String  hostName       = "";
    private String  password       = "";
    private int     port           = -1;
    private boolean ssl            = false;
    private String  supportAddress = "";
    private String  userName       = "";
    public Smtp() {
    }
    public Smtp(boolean active,
                String  hostName,
                int     port,
                String  userName,
                String  password,
                boolean ssl,
                String  fromName,
                String  fromAddress,
                String  adminAddress,
                String  supportAddress) {
      this.active = active;
      this.hostName = hostName;
      this.port = port;
      this.userName = userName;
      this.password = password;
      this.ssl = ssl;
      this.fromName = fromName;
      this.fromAddress = fromAddress;
      this.adminAddress = adminAddress;
      this.supportAddress =supportAddress;
    }
    public boolean getActive() { return active; }
    public String getAdminAddress() { return adminAddress; }
    public String getFromAddress() { return fromAddress; }
    public String getFromName() { return fromName; }
    public String getHostName() { return hostName; }
    public String getPassword() { return password; }
    public int getPort() { return port; }
    public boolean getSSL() { return ssl; }
    public String getSupportAddress() { return supportAddress; }
    public String getUserName() { return userName; }
  }

  /**
   * Constante que indica o nome do arquivo de configuração de e-mail.
   */
  static private final String MAIL_SERVICE_FILE_NAME = "mailservice.xml";
  // *
  static private final String ACTIVE          = "active";
  static private final String ADMIN_ADDRESS   = "adminaddress";
  static private final String FROM_ADDRESS    = "fromaddress";
  static private final String FROM_NAME       = "fromname";
  static private final String HOST_NAME       = "hostname";
  static private final String INTERVAL        = "interval";
  static private final String PASSWORD        = "password";
  static private final String POP3            = "pop3";
  static private final String PORT            = "port";
  static private final String SMTP            = "smtp";
  static private final String SSL             = "ssl";
  static private final String SUPPORT_ADDRESS = "supportaddress";
  static private final String USER_NAME       = "username";
  // *
  private Pop3 pop3 = new Pop3();
  private Smtp smtp = new Smtp();

  /**
   * Construtor padrão.
   * @param mailFilePath Caminho local onde se encontra o arquivo de
   *                        configurações de e=mail. Veja MAIL_FILE_NAME.
   * @throws Exception Em caso de exceção na tentativa de abertura do arquivo
   *                   XML especificado ou de inicialização do parser.
   */
  public MailServiceFile(String mailFilePath) throws Exception {
    // nossa fábrica de parsers
    SAXParserFactory parserFactory = SAXParserFactory.newInstance();
    // não validaremos o documento
    parserFactory.setValidating(false);
    // sem suporte para XML namespaces
    parserFactory.setNamespaceAware(false);
    // cria o parser
    SAXParser saxParser = parserFactory.newSAXParser();
    // inicia a análise
    saxParser.parse(new File(mailFilePath + MAIL_SERVICE_FILE_NAME), this);
  }

  public Pop3 pop3() {
    return pop3;
  }

  public Smtp smtp() {
    return smtp;
  }

  public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
    // achamos o nó Pop3?
    if (qName.equalsIgnoreCase(POP3)) {
      pop3 = new Pop3(Boolean.valueOf(attributes.getValue(ACTIVE)).booleanValue(),
                      Integer.parseInt(attributes.getValue(INTERVAL)),
                      attributes.getValue(HOST_NAME),
                      Integer.parseInt(attributes.getValue(PORT)),
                      attributes.getValue(USER_NAME),
                      attributes.getValue(PASSWORD),
                      Boolean.valueOf(attributes.getValue(SSL)).booleanValue());
    } // if
    // achamos o nó Smtp?
    else if (qName.equalsIgnoreCase(SMTP)) {
      smtp = new Smtp(Boolean.valueOf(attributes.getValue(ACTIVE)).booleanValue(),
                      attributes.getValue(HOST_NAME),
                      Integer.parseInt(attributes.getValue(PORT)),
                      attributes.getValue(USER_NAME),
                      attributes.getValue(PASSWORD),
                      Boolean.valueOf(attributes.getValue(SSL)).booleanValue(),
                      attributes.getValue(FROM_NAME),
                      attributes.getValue(FROM_ADDRESS),
                      attributes.getValue(ADMIN_ADDRESS),
                      attributes.getValue(SUPPORT_ADDRESS));
    } // if
  }

}
