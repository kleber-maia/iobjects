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
package iobjects.xml.sql;

import java.io.*;
import java.util.*;

import javax.xml.parsers.*;

import org.xml.sax.*;
import org.xml.sax.helpers.*;

/**
 * Responsável pela leitura dos arquivos XML de parâmetros de conexão com o banco
 * de dados.
 * <p>
 * Exemplo do formato do arquivo:
 * </p>
 * <p>
 * <pre>
 * &lt;connection readonly="false" dictionary="name" advertising="name" &gt;
 *   &lt;driver name="" url="" /&gt;
 *   &lt;user name="" password="" /&gt;
 *   &lt;sessionmanager allowsameuserlogon="false" maxdefaultconnections="3" /&gt;
 *   &lt;network name="" update="false" /&gt;
 *   &lt;mapping&gt;
 *     &lt;class name="package.ClassName"/&gt;
 *   &lt;/mapping&gt;
 *   &lt;extensionfilter&gt;
 *     &lt;extension name="name" /&gt;
 *   &lt;/extensionfilter&gt;
 *   &lt;advertising
 * &lt;/connection&gt;
 * </pre>
 * </p>
 */
public class ConnectionFile extends DefaultHandler {

  /**
   * Representa o elemento "driver" no arquivo de conexão.
   */
  public class Driver {
    private String name = "";
    private String url  = "";
    public Driver(String name, String url) {
      this.name = name;
      this.url  = url;
    }
    public String getName() {
      return name;
    }
    public String getURL() {
      return url;
    }
  }

  /**
   * Representa o elemento "extensionfilter" no arquivo de conexão.
   */
  public class ExtensionFilter {
    private Vector vector = new Vector();
    public ExtensionFilter() {
    }
    public void add(String extensionName) {
      vector.add(extensionName);
    }
    public boolean contains(String extensionName) {
      for (int i=0; i<vector.size(); i++)
        if (item(i).equals(extensionName))
          return true;
      return false;
    }
    public String item(int index) {
      return (String)vector.elementAt(index);
    }
    public int size() {
      return vector.size();
    }
  }

  /**
   * Representa o elemento "mapping" no arquivo de conexão.
   */
  public class Mapping {
    private Vector vector = new Vector();
    public Mapping() {
    }
    public void add(String className) {
      vector.add(className);
    }
    public boolean contains(String className) {
      for (int i=0; i<vector.size(); i++)
        if (item(i).equals(className))
          return true;
      return false;
    }
    public String item(int index) {
      return (String)vector.elementAt(index);
    }
    public int size() {
      return vector.size();
    }
  }

  /**
   * Representa o elemento "network" no arquivo de conexão.
   */
  public class Network {
    private String  name   = "";
    private boolean update = false;
    public Network(String name, boolean update) {
      this.name   = name;
      this.update = update;
    }
    public String getName() {
      return name;
    }
    public boolean getUpdate() {
      return update;
    }
  }

  /**
   * Representa o elemento "sessionmaanger" no arquivo de conexão.
   */
  public class SessionManager {
    private boolean allowSameUserLogon    = false;
    private int     maxDefaultConnections = 0;
    public SessionManager(boolean allowSameUserLogon,
                          int     maxDefaultConnections) {
      this.allowSameUserLogon = allowSameUserLogon;
      this.maxDefaultConnections = maxDefaultConnections;
    }
    public boolean getAllowSameUserLogon()    {
      return allowSameUserLogon;
    }
    public int getMaxDefaultConnections() {
      return maxDefaultConnections;
    }
  }

  /**
   * Representa o elemento "user" no arquivo de conexão.
   */
  public class User {
    private String name = "";
    private String password = "";
    public User(String name, String password) {
      this.name = name;
      this.password = password;
    }
    public String getName() {
      return name;
    }
    public String getPassword() {
      return password;
    }
  }

  private static final String ADVERTISING             = "advertising";
  private static final String ALLOW_SAME_USER_LOGON   = "allowsameuserlogon";
  private static final String CLASS                   = "class";
  private static final String CONNECTION              = "connection";
  private static final String DRIVER                  = "driver";
  private static final String EXTENSION               = "extension";
  private static final String EXTENSION_FILTER        = "extensionfilter";
  private static final String MAPPING                 = "mapping";
  private static final String MAX_DEFAULT_CONNECTIONS = "maxdefaultconnections";
  private static final String NAME                    = "name";
  private static final String NETWORK                 = "network";
  private static final String PASSWORD                = "password";
  private static final String READ_ONLY               = "readonly";
  private static final String SESSION_MANAGER         = "sessionmanager";
  private static final String DICTIONARY              = "dictionary";
  private static final String UPDATE                  = "update";
  private static final String URL                     = "url";
  private static final String USER                    = "user";

  private String          advertising     = "";
  private Driver          driver          = new Driver("", "");
  private ExtensionFilter extensionFilter = new ExtensionFilter();
  private String          fileName        = "";
  private Mapping         mapping         = new Mapping();
  private Network         network         = new Network("", false);
  private boolean         readOnly        = false;
  private SessionManager  sessionManager  = new SessionManager(false, 0);
  private String          dictionary      = "";
  private User            user            = new User("", "");

  /**
   * Construtor padrão.
   * @param connectionFileName Nome do arquivo de conexão.
   * @throws Exception Em caso de exceção na tentativa de abertura do arquivo
   *                   XML especificado ou de inicialização do parser.
   */
  public ConnectionFile(String connectionFileName) throws Exception {
    // nossa fábrica de parsers
    SAXParserFactory parserFactory = SAXParserFactory.newInstance();
    // não validaremos o documento
    parserFactory.setValidating(false);
    // sem suporte para XML namespaces
    parserFactory.setNamespaceAware(false);
    // cria o parser
    SAXParser saxParser = parserFactory.newSAXParser();
    // inicia a análise
    saxParser.parse(new File(connectionFileName), this);
    // guarda o nome do arquivo
    this.fileName = connectionFileName;
  }

  /**
   * Retorna o nome da propaganda para ser utilizada.
   * @return String Retorna o nome da propaganda para ser utilizada.
   */
  public String advertising() {
    return advertising;
  }

  /**
   * Retorna um Driver contendo as informações do elemento "driver" do arquivo
   * de configuração.
   * @return Retorna um Driver contendo as informações do elemento "driver" do
   *         arquivo de configuração.
   */
  public Driver driver() {
    return driver;
  }

  public void finalize() {
    driver         = null;
    mapping        = null;
    sessionManager = null;
    user           = null;
  }

  /**
   * Retorna um ExtensionFilter contendo as informações do elemento
   * "extensionfilter" do arquivo de configuração.
   * @return ExtensionFilter Retorna um ExtensionFilter contendo as informações
   *                         do elemento "extensionfilter" do arquivo de
   *                         configuração.
   */
  public ExtensionFilter extensionFilter() {
    return extensionFilter;
  }

  /**
   * Retorna o nome do arquivo de origem do ConnectionFile.
   * @return String Retorna o nome do arquivo de origem do ConnectionFile.
   */
  public String getFileName() {
    return fileName;
  }

  /**
   * Retorna um Mapping contendo as informações do elemento "mapping" do
   * arquivo de configuração.
   * @return Mapping Retorna um Mapping contendo as informações do elemento
   *         "mapping" do arquivo de configuração.
   */
  public Mapping mapping() {
    return mapping;
  }

  public void startElement(java.lang.String uri,
                           java.lang.String localName,
                           java.lang.String qName,
                           Attributes attributes) {
    // se encontramos o elemento "connection"
    if (qName.equalsIgnoreCase(CONNECTION)) {
      readOnly = Boolean.valueOf(attributes.getValue(READ_ONLY)).booleanValue();
      dictionary = attributes.getValue(DICTIONARY);
      advertising = attributes.getValue(ADVERTISING);
    }
    // se encontramos o elemento "driver"
    else if (qName.equalsIgnoreCase(DRIVER))
      driver = new Driver(attributes.getValue(NAME),
                          attributes.getValue(URL));
    // se encontramos o elemento "network"
    else if (qName.equalsIgnoreCase(NETWORK))
      network = new Network(attributes.getValue(NAME),
                            Boolean.valueOf(attributes.getValue(UPDATE)).booleanValue());
    // se encontramos o elemento "user"
    else if (qName.equalsIgnoreCase(USER))
      user = new User(attributes.getValue(NAME),
                      attributes.getValue(PASSWORD));
    // se encontramos o elemento "sessionmanager"
    else if (qName.equalsIgnoreCase(SESSION_MANAGER))
      sessionManager = new SessionManager(Boolean.valueOf(attributes.getValue(ALLOW_SAME_USER_LOGON)).booleanValue(),
                                          Integer.parseInt(attributes.getValue(MAX_DEFAULT_CONNECTIONS)));
    // se encontramos o elemento "class" de "mapping"
    else if (qName.equals(CLASS))
      mapping.add(attributes.getValue(NAME));
    // se encontramos o elemento "extension" de "extensionfilter"
    else if (qName.equals(EXTENSION))
      extensionFilter.add(attributes.getValue(NAME));
  }

  /**
   * Retorna true se a conexão for apenas leitura.
   * @return boolean Retorna true se a conexão for apenas leitura.
   * @since 2006 R1
   */
  public boolean readOnly() {
    return readOnly;
  }

  /**
   * Retorna um SessionManager contendo as informações do elemento
   * "sessionmanager" do arquivo de configuração.
   * @return SessionManager Retorna um SessionManager contendo as informações
   *                        do elemento "sessionmanager" do arquivo de
   *                        configuração.
   */
  public SessionManager sessionManager() {
    return sessionManager;
  }

  /**
   * Retorna o nome do dicionário de sinônimos para ser utilizado.
   * @return String Retorna o nome do dicionário de sinônimos para ser utilizado.
   */
  public String dictionary() {
    return dictionary;
  }

  /**
   * Retorna um User contendo as informações do elemento "user" do arquivo
   * de configuração.
   * @return Retorna um User contendo as informações do elemento "user" do
   *         arquivo de configuração.
   */
  public User user() {
    return user;
  }

}
