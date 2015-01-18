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
package iobjects;

import java.io.*;
import java.net.*;
import java.sql.*;
import java.util.*;
import javax.servlet.http.*;

import iobjects.card.*;
import iobjects.entity.*;
import iobjects.help.*;
import iobjects.misc.*;  
import iobjects.report.*;
import iobjects.schedule.*;
import iobjects.security.*;
import iobjects.servlet.*;
import iobjects.sql.*;
import iobjects.ui.*;
import iobjects.util.*;
import iobjects.util.mail.*;
import iobjects.xml.*;
import iobjects.xml.sql.*;
import iobjects.xml.ui.*;

/**
 * Representa a classe de fachada da aplica��o.
 */
public class Facade implements HttpSessionBindingListener {

  static private final String ADVERTISING_PATH      = "advertising";
  static private final String CONFIGURATION_PATH    = "configuration";
  static private final String CONNECTIONS_PATH      = "connections";
  static private final String DICTIONARY_PATH       = "dictionary";
  static private final String DOWNLOAD_PATH         = "~download";
  static private final String DEFAULT               = "default";
  static private final String EXTENSIONS_PATH       = "extensions";
  static private final String EXTENSIONS_CACHE_PATH = "~extensions.cache";
  static private final String FLOW_CHARTS_PATH      = "flowcharts";
  static private final String LOG_CACHE_PATH        = "~log";
  static private final String NOTICE_BOARD_PATH     = "noticeboard";
  static private final String PORTAL_CACHE_PATH     = "~portal.cache";
  static private final String PORTAL_PATH           = "portal";
  static private final String SCHEDULE_PATH         = "schedule";
  static private final String STYLES_PATH           = "css";
  static private final String TEMP_PATH             = "~temp";
  static private final String UPDATE_PATH           = "~update";
  static private final String UPLOAD_PATH           = "~upload";
  static private final String USER_DATA_PATH        = "~user.data";
  static private final String USER_PARAMS_EXTENSION = ".params";
  static private final String USER_NOTES_EXTENSION  = ".notes";
  static private final String WORK                  = "work";

  static public  final String FAQS_PATH             = "~faq";
  static public  final String IMAGES_PATH           = "images";

  /**
   * Controla se os Actions dos BusinessObjects foram inicializados pela fachada.
   */
  static private boolean actionsInitialized = false;
  /**
   * Mant�m a lista de Actions de todos os BusinessObjects da aplica��o,
   */
  static private ActionList applicationActionList = null;

  static public final int APPLICATION_STATUS_LOADED       = 0;
  static public final int APPLICATION_STATUS_INITIALIZING = 1;
  static public final int APPLICATION_STATUS_READY        = 2;
  /**
   * Determina o estado da aplica��o.
   */
  static private int applicationStatus = APPLICATION_STATUS_LOADED;

  private ActionList             actionList                 = null; // criado ao realizar logon
  private Advertising            advertising                = null; // criado ao realizar logon
  private String                 applicationId              = "";
  private ApplicationFile        applicationFile            = null;
  private String                 applicationLocalPath       = "";
  private String                 applicationLocalWorkPath   = "";
  private boolean                browserChecked             = false;
  private boolean                browserMobile              = false;
  private boolean                browserTablet              = false;
  private String                 classesLocalPath           = "";
  private ConnectionManager      connectionManager          = null;
  private ConnectionItems        connectionItems            = new ConnectionItems();
  private String                 defaultConnectionName      = "";
  private String                 defaultSiteName            = "";
  private DemoService            demoService                = null;
  private Dictionary             dictionary                 = new Dictionary();
  private DictionaryManager      dictionaryManager          = null;
  private String                 downloadUrl                = "";
  private ExtensionManager       extensionManager           = null;
  private String                 extensionsUrl              = "";
  private Vector                 flowChartList              = new Vector();
  private FlowChartManager       flowChartManager           = null;
  private HelpManager            helpManager                = null; // criado ao realizar logon
  private String                 iconUrl                    = "";
  private String                 logoUrl                    = "";
  private MailService            mailService                = null;
  private MasterRelation         masterRelation             = null;
  private MasterRelationFile     masterRelationFile         = null;
  private MasterRelationExFile   masterRelationExFile       = null;
  private NoticeBoard            noticeBoard                = null; // criado ao realizar logon
  private Log                    log                        = null;
  private User                   loggedUser                 = null;
  private java.util.Date         logonTime                  = new java.util.Date(0);
  private java.util.Date         logoffTime                 = new java.util.Date(0);
  private RecentActionList       recentActionList           = new RecentActionList();
  private Registry               registry                   = null;
  private String                 remoteAddress              = "";
  private String                 remoteAgent                = "";
  private String                 remoteHost                 = "";
  private HttpSession            session                    = null;
  private SecurityService        securityService            = null;
  private Schedule               schedule                   = null;
  private SiteManager            siteManager                = null;
  private String                 portalUrl                  = "";
  private StyleFile              style                      = null;
  private StyleManager           styleManager               = null;
  private String                 tempUrl                    = "";
  private int                    transactionAccounting      = 0;
  private boolean                transactionSuspended       = false;
  private Updater                updater                    = null;
  private String                 uploadUrl                  = "";
  private ParamList              userParamList              = new ParamList();
  private ParamList              userNoteList               = new ParamList();
  private String                 workConfiguration          = "";
  // *
  private Vector                 businessObjects            = new Vector();

  /**
   * Construtor padr�o.
   * @param workConfiguration String Nome da configura��o de trabalho desta
   *        inst�ncia da aplica��o. Este valor modifica o retorno do m�todo
   *        "applicationLocalWorkPath()" alterando o diret�rio de trabalho que
   *        a aplica��o ir� utilizar. Este valor � passado � aplica��o atrav�s
   *        do par�metro apropriado.
   * @see iobjects.servlet.Controller#PARAM_WORK_CONFIGURATION
   */
  public Facade(String workConfiguration) {
    // guarda a configura��o de trabalho desta inst�ncia da aplica��o
    this.workConfiguration = workConfiguration;
  }

  /**
   * Retorna a propaganda utilizada para o usu�rio.
   * @return Advertising Retorna a propaganda utilizada para o usu�rio.
   */
  public Advertising advertising() {
    return advertising;
  }

  /**
   * Retorna o caminho local onde se encontram os arquivos de propaganda.
   * @return Retorna o caminho local onde se encontram os arquivos de propaganda.
   */
  public String advertisingLocalPath() {
    // retorna o caminho local da aplica��o mais o caminho de configura��o
    return applicationLocalWorkPath()
         + ADVERTISING_PATH
         + File.separatorChar;
  }

  /**
   * Inicializa a fachada e os objetos nela contidos.
   * @throws Exception Em caso de exce��o na cria��o ou inicializa��o de um dos
   *         objetos da fachada.
   */
  public void initialize() throws Exception {
    try {
      // se a aplica��o ainda n�o est� pronta...marca como sendo inicializada
      if (applicationStatus != APPLICATION_STATUS_READY)
        applicationStatus = APPLICATION_STATUS_INITIALIZING;

      // nosso arquivo de Log
      log = new Log(logLocalPath());
      // carrega as configura��es de estilo
      styleManager = StyleManager.getInstance(stylesLocalPath(), stylesUrl());
      // guarda o estilo padr�o como sendo o atual
      style = styleManager.defaultStyle();
      // l� o arquivo de configura��o da aplica��o
      applicationFile = new ApplicationFile(configurationLocalPath());
      // l� o arquivo de configura��o da rela��o mestre
      masterRelationFile = new MasterRelationFile(configurationLocalPath());
      // l� o arquivo de configura��o da rela��o mestre ex
      masterRelationExFile = new MasterRelationExFile(configurationLocalPath());
      // l� o arquivo de configura��o de par�metros
      Param.setParamFile(new ParamFile(configurationLocalPath()));
      // cria a rela��o mestre
      masterRelation = new MasterRelation(masterRelationFile.information());
      // nosso ConnectionManager
      connectionManager = ConnectionManager.getInstance(connectionsLocalPath());
      // nosso SiteManager
      siteManager = SiteManager.getInstance(configurationLocalPath(),
                                            portalLocalPath(),
                                            portalCacheLocalPath(),
                                            classesLocalPath(),
                                            portalUrl());
      // nosso DictionaryManager
      dictionaryManager = DictionaryManager.getInstance(dictionaryLocalPath());
      // nosso FlowChartManager
      flowChartManager = FlowChartManager.getInstance(flowChartsLocalPath());
      // nosso Schedule
      schedule = Schedule.getInstace(scheduleLocalPath());
      // nosso Updater
      updater = (Updater)getBusinessObject(Updater.CLASS_NAME);
      // cria o servi�o de e-mail
      mailService = MailService.getInstance(configurationLocalPath(),
                                            applicationFile.information().getName(),
                                            applicationId());
      // carrega as extens�es
      extensionManager = ExtensionManager.getInstance(extensionsLocalPath(),
                                                      extensionsCacheLocalPath(),
                                                      classesLocalPath(),
                                                      extensionsUrl());
      // se n�o temos registro...exce��o
      if (extensionManager.registryClassName().equals(""))
        throw new ExtendedException(getClass().getName(), "checkRegistry", "Nenhuma classe encontrada implementando o registro.");
      // se n�o temos servi�o de seguran�a...exce��o
      if (extensionManager.securityServiceClassName().equals(""))
        throw new ExtendedException(getClass().getName(), "checkSecurityService", "Nenhuma classe encontrada implementando o servi�o de seguran�a.");
      // disponibiliza a logo
      checkLogo();

      // se estamos inicializando...limpa os logs antigos
      if (applicationStatus == APPLICATION_STATUS_INITIALIZING) {
        // meses de logs que iremos manter
        int months = Param.getParamFile().specialConstraints().getMaximumPeriod() / 30;
        if (months == 0)
          months = 3;
        final int monthsToMaintain = months;
        // limpa arquivos de log antigos
        new Thread(
          new Runnable() {
            public void run() {
              log.clean(monthsToMaintain);
            }
          }
        ).run();
      } // if

      // cria o agente de licenciamento
      // licenseAgent = LicenseAgent.getInstance(applicationId());
      // se chegou aqui...a aplica��o est� pronta
      applicationStatus = APPLICATION_STATUS_READY;
    }
    catch (Exception e) {
      // define a aplica��o como apenas carregada
      // assim poder� ser inicializada mais uma vez
      applicationStatus = APPLICATION_STATUS_LOADED;
      // imprime a exce�ao
      e.printStackTrace();
      // mostra exce��o
      throw e;
    } // try-catch
  }

  /**
   * Retorna a lista de a��es encontradas nos objetos de neg�cio das extens�es
   * da aplica��o e cujo usu�rio que efetuou logon possui direito de acesso.
   * @return ActionList Retorna a lista de a��es encontradas nos objetos de
   *         neg�cio das extens�es da aplica��o e cujo usu�rio que efetuou logon
   *         possui direito de acesso.
   */
  public ActionList actionList() {
    return actionList;
  }

  /**
   * Retorna a lista de a��es encontradas nos objetos de neg�cio das extens�es
   * da aplica��o.
   * @return ActionList Retorna a lista de a��es encontradas nos objetos de
   *         neg�cio das extens�es da aplica��o.
   */
  static public ActionList applicationActionList() {
    return applicationActionList;
  }

  /**
   * Retorna um ApplicationFile.DefaultConnection contendo as informa��es de
   * obten��o da conex�o padr�o da aplica��o.
   * @return Retorna um ApplicationFile.DefaultConnection contendo as informa��es
   *         de obten��o da conex�o padr�o da aplica��o.
   */
  public ApplicationFile.DefaultConnection applicationDefaultConnection() {
    return applicationFile.defaultConnection();
  }

  /**
   * Retorna um ApplicationFile.DisplayMode contendo as informa��es de
   * modo de exibi��o da aplica��o.
   * @return Retorna um ApplicationFile.DisplayMode contendo as informa��es de
   *         modo de exibi��o da aplica��o.
   */
  public ApplicationFile.DisplayMode applicationDisplayMode() {
    return applicationFile.displayMode();
  }

  /**
   * Retorna a identifica��o �nica da aplica��o para o sistema onde a mesma
   * se encontra instalada.
   * @return String Retorna a identifica��o �nica da aplica��o para o sistema
   *         onde a mesma se encontra instalada.
   * @throws Exception Em caso de exce��o na tentativa de gerar a identifica��o
   *         da aplica��o.
   */
  public String applicationId() throws Exception {
    // se j� temos um Id...retorna-o
    if (!applicationId.equals(""))
      return applicationId;
    // nosso resultado
    StringBuffer result = new StringBuffer("");
    // combina��o reversa do nome da aplica��o
    String applicationName = StringTools.reverseCombination(applicationInformation().getName(), 5);
    // transforma em Hexadecimal
    for (int i=0; i<applicationName.length(); i++)
      result.append(Integer.toHexString((int)applicationName.charAt(i)));
    // adiciona o Mac Address da m�quina
    try {
      result.append(NetworkInfo.getMacAddress());
    }
    catch (Exception e) {
      result.append("00-00-00-00-00-00");
    } // try-catch
    // retira as "-" e ":"
    for (int i=result.length()-1; i>=0; i--)
      if (result.charAt(i) == '-')
        result.deleteCharAt(i);
    // p�e a m�scara
    applicationId = StringTools.formatCustomMask(result.toString(), "0000-0000-0000-0000-0000-0000", '0').toUpperCase();
    // retorna
    return applicationId;
  }

  /**
   * Retorna um ApplicationFile.Demo contendo as op��o de demonstra��o da aplica��o.
   * @return Retorna um ApplicationFile.Demo contendo as op��o de demonstra��o 
   *         da aplica��o.
   */
  public ApplicationFile.Demo applicationDemo() {
    return applicationFile.demo();
  }

  /**
   * Retorna um ApplicationFile.Information contendo as informa��es da aplica��o.
   * @return Retorna um ApplicationFile.Information contendo as informa��es da
   *         aplica��o.
   */
  public ApplicationFile.Information applicationInformation() {
    return applicationFile.information();
  }

  /**
   * Retorna um ApplicationFile.Logo contendo as informa��es da logo da aplica��o.
   * @return Retorna um ApplicationFile.Information contendo as informa��es da
   *         logo da aplica��o.
   */
  public ApplicationFile.Logo applicationLogo() {
    return applicationFile.logo();
  }

  /**
   * Retorna o caminho local de execu��o da aplica��o.
   * @return Retorna o caminho local de execu��o da aplica��o.
   */
  public String applicationLocalPath() {
    // se j� temos o caminho da aplica��o...retorna
    if (!applicationLocalPath.equals(""))
      return applicationLocalPath;
    // pega o caminho completo de nossa classe
    String result = getClass().getProtectionDomain().getCodeSource().getLocation().getPath().replaceAll("%20", " ");
    // se 'WEB-INF' est� contido no caminho...mant�m somente o que h� antes
    int webInfPos = result.indexOf("WEB-INF");
    if (webInfPos >= 0) {
      result = result.substring(0, webInfPos);
    } // if
    // se 'classes' est� contido no caminho...mant�m somente o que h� antes
    int classesPos = result.indexOf("classes");
    if (classesPos >= 0) {
      result = result.substring(0, classesPos);
    } // if
    // substitui '/' por separatorChar
    if (File.separatorChar != '/')
      result = result.replace('/', File.separatorChar);
    // se � um caminho do Windows (com :) e se inicia por '/' retira
    if ((result.indexOf(':') > 0) && (result.charAt(0) == File.separatorChar))
      result = result.substring(1, result.length()-1);
    // se termina com uma extens�o...retira
    if (result.indexOf('.') > 0)
      result = result.substring(0, result.lastIndexOf(File.separatorChar));
    // retorna com separatorChar no final
    if (result.charAt(result.length()-1) != File.separatorChar)
      result += File.separatorChar;
    // guarda o valor
    applicationLocalPath = result;
    // retorna
    return applicationLocalPath;
  }

  /**
   * Retorna o caminho local de trabalho da aplica��o.
   * @return Retorna o caminho local de trabalho da aplica��o.
   */
  public String applicationLocalWorkPath() {
    // se j� temos o caminho local de trabalho da aplica��o...retorna
    if (!applicationLocalWorkPath.equals(""))
      return applicationLocalWorkPath;
    // pega o caminho da aplica��o
    String result = applicationLocalPath();
    // retira a barra do final
    if (result.charAt(result.length()-1) == File.separatorChar)
      result = result.substring(0, result.length()-1);
    // se temos configura��o de trabalho para esta inst�ncia da aplica��o...usa
    if (!workConfiguration.equals(""))
      result += "." + workConfiguration + File.separatorChar;
    // se n�o temos...usaremos o padr�o
    else
      result += "." + WORK + File.separatorChar;
    // guarda o valor
    applicationLocalWorkPath = result;
    // retorna
    return applicationLocalWorkPath;
  }

  /**
   * Retorna um ApplicationFile.NoticeBoard contendo as informa��es de
   * exibi��o do quadro de avisos da aplica��o.
   * @return Retorna um ApplicationFile.NoticeBoard contendo as informa��es
   *         de exibi��o do quadro de avisos da aplica��o.
   */
  public ApplicationFile.NoticeBoard applicationNoticeBoard() {
    return applicationFile.noticeBoard();
  }

  /**
   * Retorna um ApplicationFile.SystemInformation contendo as informa��es de
   * exibi��o das informa��es do sistema da aplica��o.
   * @return Retorna um ApplicationFile.SystemInformation contendo as informa��es
   *         de exibi��o das informa��es do sistema da aplica��o.
   */
  public ApplicationFile.SystemInformation applicationSystemInformation() {
    return applicationFile.systemInformation();
  }

  /**
   * Inicia uma transa��o nas conex�es utilizadas para acesso ao banco de dados.
   * @throws Exception Em caso de exce��o na tentativa de iniciar a transa��o.
   */
  public void beginTransaction() throws Exception {
    // se as transa��es est�o suspensas...dispara
    if (transactionSuspended)
      return;
    // contador de transa��es
    if (connectionItems.transactionCount() == 0)
      transactionAccounting++;
    // inicia transa��o
    connectionItems.beginTransaction();
  }

  /**
   * Disponibilia a logo informada em ApplicationFile no diret�rio tempor�rio
   * de imagens.
   * @throws Exception Em caso de exce��o na tentativa de copiar a logo
   *                   para o diret�rio tempor�rio de imagens.
   */
  private void checkLogo() throws Exception {
    // se temos um �cone para ser usado...
    if (!applicationFile.icon().getFileName().equals("")) {
      // arquivo da icon no diret�tio de configura��es
      File iconSourceFile = new File(configurationLocalPath() + applicationFile.icon().getFileName());
      // arquivo da icon no diret�tio tempor�rio
      File iconTargetFile = new File(tempLocalPath() + IMAGES_PATH + File.separatorChar + applicationFile.icon().getFileName());
      // copia o arquivo para o diret�rio tempor�rio
      FileTools.copyFile(iconSourceFile, iconTargetFile);
    } // if
    // se temos uma logo para ser usada...
    if (!applicationFile.logo().getFileName().equals("")) {
      // arquivo da logo no diret�tio de configura��es
      File logoSourceFile = new File(configurationLocalPath() + applicationFile.logo().getFileName());
      // arquivo da logo no diret�tio tempor�rio
      File logoTargetFile = new File(tempLocalPath() + IMAGES_PATH + File.separatorChar + applicationFile.logo().getFileName());
      // copia o arquivo para o diret�rio tempor�rio
      FileTools.copyFile(logoSourceFile, logoTargetFile);
    } // if
  }

  /**
   * Retorna a inst�ncia de ConnectionManager utilizada pela aplica��o para
   * gerenciamento das conex�es com banco de dados.
   * @return ConnectionManager Retorna a inst�ncia de ConnectionManager utilizada
   *         pela aplica��o para gerenciamento das conex�es com banco de dados.
   */
  public ConnectionManager connectionManager() {
    return connectionManager;
  }

  /**
   * Retorna o caminho local onde se encontram as classes da aplica��o.
   * @return String Retorna o caminho local onde se encontram as classes da aplica��o.
   */
  public String classesLocalPath() {
    // se j� temos o caminnho local das classes da aplica��o...retorna
    if (!classesLocalPath.equals(""))
      return classesLocalPath;
    // pega o caminho completo de nossa classe
    String result = getClass().getProtectionDomain().getCodeSource().getLocation().getPath().replaceAll("%20", " ");
    // se 'classes' est� contido no caminho...mant�m somente at� ai
    int classesPos = result.indexOf("classes");
    if (classesPos >= 0) {
      result = result.substring(0, classesPos + "classes".length()+1);
    } // if
    // substitui '/' por separatorChar
    if (File.separatorChar != '/')
      result = result.replace('/', File.separatorChar);
    // se � um caminho do Windows (com :) e se inicia por '/' retira
    if ((result.indexOf(':') > 0) && (result.charAt(0) == File.separatorChar))
      result = result.substring(1, result.length()-1);
    // se termina com uma extens�o...retira
    if (result.indexOf('.') > 0)
      result = result.substring(0, result.lastIndexOf(File.separatorChar));
    // retorna com separatorChar no final
    if (result.charAt(result.length()-1) != File.separatorChar)
      result += File.separatorChar;
    // se n�o termina com 'classes'...adiciona
    if (!result.endsWith("classes" + File.separatorChar))
      result += "classes" + File.separatorChar;
    // guarda o valor
    classesLocalPath = result;
    // retorna
    return classesLocalPath;
  }

  /**
   * Aplica as altera��es nas conex�es utilizadas para acesso ao banco de dados.
   * @throws Exception Em caso de exce��o na tentativa de aplicar as altera��es
   *         no banco de dados.
   */
  public void commitTransaction() throws Exception {
    // se as transa��es est�o suspensas...dispara
    if (transactionSuspended)
      return;
    // salva tudo
    connectionItems.commitTransaction();
    // se ainda n�o chegamos a 0...dispara
    if (connectionItems.transactionCount() > 0)
      return;
    // devolve as conex�es que pegamos emprestadas
    while (connectionItems.size() > 0) {
      connectionManager.returnSafeConnection(connectionItems.get(0));
      connectionItems.remove(0);
    } // while
  }

  /**
   * Retorna o caminho local onde se encontram as configura��es da aplica��o.
   * @return Retorna o caminho local onde se encontram as configura��es da
   *         aplica��o.
   */
  public String configurationLocalPath() {
    // retorna o caminho local da aplica��o mais o caminho de configura��o
    return applicationLocalWorkPath()
         + CONFIGURATION_PATH
         + File.separatorChar;
  }

  /**
   * Retorna a inst�ncia de ConnectionItems que a fachada utiliza para gerenciar
   * as conex�es de banco de dados.
   * @return 
   */
  public ConnectionItems connectionItems() {
    return connectionItems;
  }
  
  /**
   * Retorna o caminho local onde se encontram os arquivos de conex�o de banco
   * de dados da aplica��o.
   * @return Retorna o caminho local onde se encontram os arquivos de conex�o
   *         de banco de dados da aplica��o.
   */
  public String connectionsLocalPath() {
    return applicationLocalWorkPath()
         + CONNECTIONS_PATH
         + File.separatorChar;
  }

  /**
   * Retorna a interface DemoService respons�vel pelos servi�os de demonstra��o
   * da aplica��o.
   * @return DemoService Retorna a interface DemoService respons�vel
   * pelos servi�os de demonstra��o da aplica��o.
   * @throws Exception
   */
  public DemoService demoService() throws Exception {
    // retorna...
    return (DemoService)getBusinessObject(extensionManager.demoServiceClassName()); 
  }
 
  /**
   * Retorna a inst�ncia de DictionaryManager utilizada pela aplica��o para gerenciar
   * os arquivos de dicion�rios.
   * @return DictionaryManager Retorna a inst�ncia de Dictionary utilizada pela aplica��o
   *                   para gerenciar os arquivos de dicion�rios.
   */
  public DictionaryManager dictionaryManager() {
    return dictionaryManager;
  }

  /**
   * Retorna o caminho local onde se encontram os arquivos de dicion�rios 
   * da aplica��o.
   * @return Retorna o caminho local onde se encontram os arquivos de dicion�rios
   *         da aplica��o.
   */
  public String dictionaryLocalPath() {
    return applicationLocalWorkPath()
         + DICTIONARY_PATH
         + File.separatorChar;
  }

  /**
   * Retorna o caminho local onde estar�o dispon�veis os arquivos para download.
   * @return Retorna o caminho local onde estar�o dispon�veis os arquivos para
   *         download.
   */
  public String downloadLocalPath() {
    return applicationLocalPath()
         + DOWNLOAD_PATH
         + File.separatorChar;
  }

  /**
   * Retorna a URL onde os recursos das extens�es podem ser localizados.
   * @return String Retorna a URL onde os recursos das extens�es podem ser localizados.
   */
  public String downloadUrl() {
    // se j� temos a URL de download...retorna
    if (!downloadUrl.equals(""))
      return downloadUrl;
    // pega o caminho local de cache
    String result = downloadLocalPath();
    // retira o caminho da aplica��o do in�cio
    result = result.substring(applicationLocalPath().length(), result.length());
    // troca as \ por /
    result = result.replace('\\', '/');
    // retira a barra do in�cio e do final
    if (result.charAt(0) == '/')
      result = result.substring(1, result.length());
    if (result.charAt(result.length()-1) == '/')
      result = result.substring(0, result.length()-1);
    // guarda o valor
    downloadUrl = result;
    // retorna
    return downloadUrl;
  }

  /**
   * Retorna true se a extens�o informada por 'extensionName' foi carregada
   * e n�o se encontra na lista de filtro de extens�es da conex�o padr�o
   * do usu�rio que efetuou logon.
   * @param extensionName String Nome da extens�o. N�o � necess�rio utilizar
   *                      a extens�o do nome do arquivo.
   * @return boolean Retorna true se a extens�o informada por 'extensionName'
   *                 foi carregada e n�o se encontra na lista de filtro de
   *                 extens�es da conex�o padr�o do usu�rio que efetuou logon.
   */
  public boolean extensionLoaded(String extensionName) {
    return extensionManager.contains(extensionName) &&
           !connectionManager.connectionFiles().get(getDefaultConnectionName()).extensionFilter().contains(extensionName);
  }

  /**
   * Retorna o ExtensionManager respons�vel pelo gerenciamento das extens�es
   * da aplica�a�o.
   * @return ExtensionManager respons�vel pelo gerenciamento das extens�es
   *         da aplica�a�o.
   */
  public ExtensionManager extensionManager() {
    return extensionManager;
  }

  /**
   * Retorna o caminho local onde se encontram as extens�es da aplica��o.
   * @return Retorna o caminho local onde se encontram as extens�es da aplica��o.
   */
  public String extensionsLocalPath() {
    return applicationLocalWorkPath()
         + EXTENSIONS_PATH
         + File.separatorChar;
  }

  /**
   * Retorna o caminho local onde ser�o extra�dos os arquivos das extens�es.
   * @return Retorna o caminho local onde ser�o extra�dos os arquivos das extens�es.
   */
  public String extensionsCacheLocalPath() {
    return applicationLocalPath()
         + EXTENSIONS_CACHE_PATH
         + File.separatorChar;
  }

  /**
   * Retorna a URL onde os recursos das extens�es podem ser localizados.
   * @return String Retorna a URL onde os recursos das extens�es podem ser localizados.
   */
  public String extensionsUrl() {
    // se j� temos a URL da extens�es...retorna
    if (!extensionsUrl.equals(""))
      return extensionsUrl;
    // pega o caminho local de cache
    String result = extensionsCacheLocalPath();
    // retira o caminho da aplica��o do in�cio
    result = result.substring(applicationLocalPath().length(), result.length());
    // troca as \ por /
    result = result.replace('\\', '/');
    // retira a barra do in�cio e do final
    if (result.charAt(0) == '/')
      result = result.substring(1, result.length());
    if (result.charAt(result.length()-1) == '/')
      result = result.substring(0, result.length()-1);
    // guarda o valor
    extensionsUrl = result;
    // retorna
    return extensionsUrl;
  }

  /**
   * Preenche o nosso ActionList com as a��es existentes nos BusinessObjects
   * das extens�es da aplica��o e cujo o usu�rio que efetuou logon possui
   * direito de acesso.
   * Preenche ainda o FAQList de HelpManager com os FAQ's contidos nos
   * BusinessObjects cujo usu�rio possui direito de acesso.
   * @throws Exception Em caso de exce��o ao instanciar os objetos de neg�cio
   *         para extrair suas a��es.
   */
  private void fillActionList() throws Exception {

    /**
     * Cria um clone de um Action existente em um BusinessObject e adiciona-o
     * no ActionList de Facade.
     */
    class CloneAction {

      public CloneAction(BusinessObject object,
                         Action         action,
                         Action         parentAction) {
        // se estamos em um tablet e o Action � um cadastro ou processo...dispara
        if (browserTablet && !action.getName().equals("cockpit") && ((action.getCategory() == Action.CATEGORY_ENTITY) || (action.getCategory() == Action.CATEGORY_PROCESS)))
          return;
        // se o usu�rio � privilegiado, tem direito sobre o Action ou este
        // � um Action invis�vel...
        if (loggedUser.getPrivileged() ||
            loggedUser.getActionInfoList().contains(action.getName()) ||
            !action.getVisible()) {
          // loop nos FAQ's do objeto
          for (int i=0; i<object.faqList().size(); i++) {
            // FAQ da vez
            FAQ faq = object.faqList().get(i);
            // adiciona na nossa lista
            helpManager.faqList().add(new FAQ(faq.getName(),
                                              faq.getModule(),
                                              faq.getQuestion(),
                                              faq.getAnswer(),
                                              (!faq.getAnswerFileName().equals("") ? applicationLocalPath() + FAQS_PATH + File.separatorChar + object.extension().getName() + File.separatorChar + faq.getAnswerFileName() : ""),
                                              (!faq.getPresentationFileName().equals("") ? FAQS_PATH + "/" + object.extension().getName() + "/" + faq.getPresentationFileName() : "")));
          } // for
          // criando nova inst�ncia considerando que o objeto
          // de neg�cio dever� ser destru�do ap�s este m�todo
          Action cloneAction = new Action(action.getName(),
                                          action.getCaption(),
                                          action.getDescription(),
                                          action.getHelp(),
                                          action.getJsp(),
                                          action.getModule(),
                                          action.getAccessPath(),
                                          action.getCategory(),
                                          action.getMobile(),
                                          action.getVisible());
          cloneAction.setCentralPoint(action.getCentralPoint());
          cloneAction.setShowType(action.getShowType());
          // guarda o nome da classe na qual o Action foi declarado
          cloneAction.setDeclaringClassName(action.getDeclaringClassName());
          // se temos um pai...associa-nos a ele
          if (parentAction != null)
            parentAction.addNestedAction(cloneAction);
          // o Action foi rec�m lan�ado?
          cloneAction.setJustReleased(action.getJustReleased());
          // o Action � Beta?
          cloneAction.setBeta(action.getBeta());
          // informa��es de seguran�a sobre o Action clonado
          ActionInfo actionInfo = loggedUser.getActionInfoList().get(action.getName());
          // adiciona seus comandos
          for (int i=0; i<action.commandList().size(); i++) {
            // comando da vez
            Command command = action.commandList().get(i);
            // se o usu�rio � privilegiado, o Action � invis�vel ou
            // tem direito sobre o Command...adiciona
            if (loggedUser.getPrivileged() ||
                !action.getVisible() ||
                StringTools.arrayContains(actionInfo.getCommandList(), command.getName()))
              cloneAction.commandList().add(new Command(command.getName(),
                                                        command.getCaption(),
                                                        command.getDescription()));
          } // for i
          // adiciona na lista da fachada
          actionList.add(cloneAction);
          // loop nos Actions aninhados do Action original
          for (int i=0; i<action.nestedActionList().size(); i++) {
            // Action aninhado da vez
            Action nestedAction = action.nestedActionList().get(i);
            // clona e adiciona
            new CloneAction(object, nestedAction, cloneAction);
          } // for
        } // if
      } // public CloneAction

    } // class CloneAction

    // se n�o temos usu�rio que efetuou logon...dispara
    if (loggedUser == null)
      return;
    // cria uma nova lista de a��es
    actionList = new ActionList();
    // cria um novo gerenciador de ajuda
    helpManager = new HelpManager();

    /**
     * Fase 1 - inicializa os Actions da aplica��o
     */

    fillApplicationActionList(this);

    /**
     * Fase 2 - clona os Actions e guarda os FAQ's dos BusinessObjects
     */

    // obt�m a conex�o default
    ConnectionFile defaultConnectionFile = connectionManager.connectionFiles().get(getDefaultConnectionName());
    // loop nos Actions da aplica��o
    for (int i=0; i<applicationActionList.size(); i++) {
      // a��o da vez
      Action action = applicationActionList.get(i);
      // se o Action � aninhado...continua
      if (action.getParentAction() != null)
        continue;
      // cria uma inst�ncia da classe que o cont�m
      BusinessObject object = (BusinessObject)(Class.forName(action.getDeclaringClassName())).newInstance();
      // se o Action pertence a uma extens�o filtrada...continua
      if (defaultConnectionFile.extensionFilter().contains(object.extension().getName()))
        continue;
      // clona e adiciona na lista
      new CloneAction(object, action, null);
    } // for
    // ordena alfabeticamente e por categoria
    actionList.sort();
    // ordena os FAQs alfabeticamente
    helpManager.faqList().sort();
    // avisa ao garbage collector para fazer uma limpeza na mem�ria
    System.gc();
  }

  /**
   * Preenche o ActionList da aplica��o com os Action's encontrados nas extens�es.
   * @param instance Facade Inst�ncia de Facade utilizada para obten��o de
   *                 algumas informa��es necess�rias.
   * @throws Exception Em caso de exce��o na tentativa de instanciar BusinessObjects.
   */
  static private synchronized void fillApplicationActionList(Facade instance) throws Exception {
    // se os Actions j� foram inicializados...dispara
    if (actionsInitialized)
      return;

    // data de hoje
    java.util.Date today = DateTools.getActualDate();
    // lista de Actions da aplica��o
    applicationActionList = new ActionList();
    // loop nas extens�es
    for (int i=0; i<instance.extensionManager.extensions().length; i++) {
      // Extens�o da vez
      Extension extension = instance.extensionManager.extensions()[i];
      // constr�i a URL parcial para acesso aos JSPs da extens�o
      // enquanto extraidos no diret�rio de cache da mesma
      String extensionCachePath = extension.getCacheFilesPath();
      extensionCachePath = extensionCachePath.substring(instance.applicationLocalPath().length(), extensionCachePath.length());
      extensionCachePath = extensionCachePath.replace('\\', '/');
      // loop nos objetos de neg�cio
      for (int w=0; w<extension.getClassNames().length; w++) {
        // classe da vez
        String className = extension.getClassNames()[w];
        // cria uma inst�ncia da classe
        BusinessObject object = (BusinessObject)(Class.forName(className)).newInstance();
        // loop nas a��es
        for (int z=0; z<object.actionList().size(); z++) {
          // a��o da vez
          Action action = object.actionList().get(z);
          // guarda o nome da classe na qual o Action foi declarado
          action.setDeclaringClassName(object.getClass().getName());
          // ajusta o caminho do seu JSP
          action.setJsp(extensionCachePath + action.getJsp());
          // define se o Action foi rec�m lan�ado na vers�o mais recente da aplica��o
          if (today.compareTo(instance.releaseNotes().mostRecentRelease().getEmphasize()) <= 0)
            action.setJustReleased(instance.releaseNotes().mostRecentRelease().changeList().contains(action.getName()));
          // define se o Action � Beta na vers�o mais recente da aplica��o
          ReleaseNotesFile.Change change = instance.releaseNotes().mostRecentRelease().changeList().get(action.getName());
          action.setBeta((change != null) && change.isBeta());
          // adiciona na lista da aplica��o
          applicationActionList.add(action);
        } // for z
      } // for w
    } // for i
    // os Actions foram inicializados
    actionsInitialized = true;
    // avisa ao garbage collector para fazer uma limpeza na mem�ria
    System.gc();
  }

  public void finalize() {
    try {
      if (loggedUser != null)
        logoff();
    }
    catch (Exception e) {
    } // try-catch
  }


  /**
   * Procura por uma instancia de 'objectClass' no pool de objetos de neg�cio.
   * @param businessObjectClass Classe do objeto de neg�cios que se deseja localizar.
   * @return Procura por uma instancia de 'objectClass' no pool de objetos de neg�cio.
   */
  private BusinessObject findBusinessObject(Class businessObjectClass) {
    // loop nos objetos de neg�cio
    Object result = null;
    for (int i=0; i<businessObjects.size(); i++) {
      // objeto da vez
      result = businessObjects.elementAt(i);
      // se � da classe desejada...
      if (businessObjectClass.equals(result.getClass())) {
        // guarda nossa refer�ncia no objeto
        ((BusinessObject)result).setFacade(this);
        // retorna
        return (BusinessObject)result;
      } // if
    } // for
    // se n�o achamos...retorna nada
    return null;
  }

  /**
   * Retorna a inst�ncia de FlowChartManager utilizada pela aplica��o para
   * gerenciamento dos fluxogramas.
   * @return FlowChartManager Retorna a inst�ncia de FlowChartManager utilizada
   *         pela aplica��o para gerenciamento dos fluxogramas.
   */
  public FlowChartManager flowChartManager() {
    return flowChartManager;
  }

  /**
   * Retorna o caminho local onde se encontram os arquivos de fluxogramas
   * da aplica��o.
   * @return Retorna o caminho local onde se encontram os arquivos de fluxogramas
   *         da aplica��o.
   */
  public String flowChartsLocalPath() {
    return applicationLocalWorkPath()
         + FLOW_CHARTS_PATH
         + File.separatorChar;
  }

  /**
   * Retorna o status da aplica��o.
   * @return int Retorna o status da aplica��o.
   */
  static public int getApplicationStatus() {
    return applicationStatus;
  }

  /**
   * Retorna true se o browser que criou a sess�o foi verificado.
   * @return boolean Retorna true se o browser que criou a sess�o foi verificado.
   */
  public boolean getBrowserChecked() {
    return browserChecked;
  }

  /**
   * Retorna true se o browser que criou a sess�o � mobile.
   * @return boolean Retorna true se o browser que criou a sess�o � mobile.
   */
  public boolean getBrowserMobile() {
    return browserMobile;
  }

  /**
   * Retorna true se o browser que criou a sess�o � tablet.
   * @return boolean Retorna true se o browser que criou a sess�o � tablet.
   */
  public boolean getBrowserTablet() {
    return browserTablet;
  }

  /**
   * Retorna uma inst�ncia de 'className' configurada e pronta para uso.
   * @param className Nome da classe que se deseja uma inst�ncia.
   * @return Retorna uma inst�ncia de 'className' configurada e pronta para uso.
   * @throws Exception Em caso de exce��o na tentativa de instanciar o objeto
   *                   de neg�cio.
   */
  public BusinessObject getBusinessObject(String className) throws Exception {
    // tenta carregar a classe desejada
    Class businessObjectClass = Class.forName(className);
    // j� existe uma inst�ncia desta classe?
    BusinessObject result = (BusinessObject)findBusinessObject(businessObjectClass);
    // se temos...retorna
    if (result != null)
      return result;
    // se n�o temos...cria
    else {
      // nova inst�ncia
      result = (BusinessObject)businessObjectClass.newInstance();
      // nome da conex�o para ser usada
      String connectionName = connectionManager.getConnectionNameForClassMapping(className, getDefaultConnectionName());
      // configura o nome da conex�o
      result.setConnectionName(connectionName);
      // configura o arquivo de conex�o
      result.setConnectionFile(connectionManager.connectionFiles().get(connectionName));
      // nossa refer�ncia
      result.setFacade(this);
      // inicializa
      result.initialize();
      // p�e na nossa lista
      businessObjects.add(result);
      // retorna
      return result;
    } // if
  }

  /**
   * Retorna uma inst�ncia de 'className' configurada e pronta para uso.
   * @param className Nome da classe que se deseja uma inst�ncia.
   * @return Retorna uma inst�ncia de 'className' configurada e pronta para uso.
   * @throws Exception Em caso de exce��o na tentativa de instanciar o objeto
   *                   de neg�cio.
   */
  public Card getCard(String className) throws Exception {
    return (Card)getBusinessObject(className);
  }

  /**
   * Retorna o Connection referenciado por 'connectionName'.
   * <b>De modo a trabalhar com o pool de conex�es de forma eficiente, � necess�rio
   * ter uma transa��o iniciada para usar este m�doto.</b>
   * @param connectionName Nome da conex�o que se deseja retornar.
   * @return Retorna o Connection referenciado por 'connectionName'.
   * @throws Exception Em caso de exce��o na tentativa de localizar uma conex�o
   *                   com o nome informado ou de instanci�-la.
   */
  public Connection getConnection(String connectionName) throws Exception {
    // se n�o estamos em transa��o...retorna uma conex�o da lista insegura
    if (connectionItems.transactionCount() == 0)
      return connectionManager.getUnsafeConnection(connectionName).connection();
    // se estamos em transa��o...
    else {
      // procura a conex�o desejada em nossa lista
      ConnectionItem result = connectionItems.get(connectionName);
      // se j� temos...retorna
      if (result != null)
        return result.connection();
      // se ainda n�o temos...
      else {
        // pede uma emprestada ao ConnectionManager
        result = connectionManager.borrowSafeConnection(connectionName);
        // se n�o havia nenhuma para emprestar...cria uma nova
        if (result == null)
          result = connectionManager.createSafeConnection(connectionName);
        // guarda na nossa lista
        connectionItems.add(result);
        // retorna
        return result.connection();
      } // if
    } // if
  }

  /**
   * Retorna o ConnectionFile contendo as configura��es da conex�o que dever�
   * ser utilizada para a classe referenciada por 'className' levando em
   * considera��o os mapeamentos.
   * @param className String Nome da classe cujo arquivo de conex�ose deseja retornar.
   * @return Retorna o ConnectionFile contendo as configura��es da conex�o que
   *         dever� ser utilizada para a classe referenciada por 'className'
   *         levando em considera��o os mapeamentos.
   */
  public ConnectionFile getConnectionFileForClassName(String className) {
    // nome da conex�o para ser usada
    String connectionName = connectionManager.getConnectionNameForClassMapping(className, getDefaultConnectionName());
    // retorna o arquivo de conex�o
    return connectionManager.connectionFiles().get(connectionName);
  }

  /**
   * Retorna o Connection que dever� ser utilizada para a classe referenciada por
   * 'className' levando em considera��o os mapeamentos.
   * <b>De modo a trabalhar com o pool de conex�es de forma eficiente, � necess�rio
   * ter uma transa��o iniciada para usar este m�doto.</b>
   * @param className String Nome da classe cuja conex�o se deseja retornar.
   * @throws Exception Em caso de exce��o na tentativa de criar uma nova
   *         conex�o com o banco de dados.
   * @return Retorna o Connection que dever� ser utilizada para a classe
   *         referenciada por 'className' levando em considera��o os mapeamentos.
   */
  public Connection getConnectionForClassName(String className) throws Exception {
    // nome da conex�o para ser usada
    String connectionName = connectionManager.getConnectionNameForClassMapping(className, getDefaultConnectionName());
    // retorna a conex�o
    return getConnection(connectionName);
  }

  /**
   * Retorna o nome da conex�o default com o banco de dados.
   * @return String Retorna o nome da conex�o default com o banco de dados.
   */
  public String getDefaultConnectionName() {
    if (!defaultConnectionName.equals(""))
      return defaultConnectionName;
    else
      return DEFAULT;
  }

  /**
   * Retorna o nome do site default no portal.
   * @return String Retorna o nome do site default no portal.
   */
  public String getDefaultSiteName() {
    return defaultSiteName;
  }

  /**
   * Retorna o dicion�rio apropriado para a sess�o.
   * @return Retorna o dicion�rio apropriado para a sess�o.
   */
  public Dictionary getDictionary() {
    return dictionary;
  }

  /**
   * Retorna uma inst�ncia de 'className' configurada e pronta para uso.
   * @param className Nome da classe que se deseja uma inst�ncia.
   * @return Retorna uma inst�ncia de 'className' configurada e pronta para uso.
   * @throws Exception Em caso de exce��o na tentativa de instanciar o objeto
   *                   de neg�cio.
   */
  public Entity getEntity(String className) throws Exception {
    return (Entity)getBusinessObject(className);
  }

  /**
   * Retorna o FlowChart referenciado por 'flowChartName'. Se nenhum FlowChart
   * for localizado um novo ser� criado.
   * @param flowChartName Nome do FlowChart que se deseja retornar.
   * @return Retorna o FlowChart referenciado por 'flowChartNaneName'. Se nenhum
   *         Connection for localizado um novo ser� criado.
   * @throws Exception Em caso de exce��o na tentativa de localizar um FlowChart
   *                   com o nome informado ou de instanci�-lo.
   */
  public FlowChart getFlowChart(String flowChartName) throws Exception {
    // nosso resultado
    FlowChart result = null;
    // localiza  na nossa lista
    for (int i=0; i<flowChartList.size(); i++) {
      FlowChart flowChart = (FlowChart)flowChartList.elementAt(i);
      if (flowChart.getName().equals(flowChartName)) {
        result = flowChart;
        break;
      } // if
    } // for
    // se n�o encontramos...
    if (result == null) {
      // pede ao FlowChartManager
      result = flowChartManager.getFlowChart(flowChartName);
      // adiciona na lista
      flowChartList.add(result);
    } // if
    // retorna
    return result;
  }

  /**
   * Retorna uma inst�ncia de 'className' configurada e pronta para uso.
   * @param className Nome da classe que se deseja uma inst�ncia.
   * @return Retorna uma inst�ncia de 'className' configurada e pronta para uso.
   * @throws Exception Em caso de exce��o na tentativa de instanciar o objeto
   *                   de neg�cio.
   */
  public iobjects.process.Process getProcess(String className) throws Exception {
    return (iobjects.process.Process)getBusinessObject(className);
  }

  /**
   * Retorna uma inst�ncia de 'className' configurada e pronta para uso.
   * @param className Nome da classe do relat�rio que se deseja uma inst�ncia.
   * @return Retorna uma inst�ncia de 'className' configurada e pronta para uso.
   * @throws Exception Em caso de exce��o na tentativa de instanciar o objeto
   *                   de neg�cio.
   */
  public Report getReport(String className) throws Exception {
    return (Report)getBusinessObject(className);
  }

  /**
   * Retorna o User referente ao Usu�rio que efetuou logon. Retorna null
   * se nenhum Usu�rio efetuou logon.
   * @return Retorna o User referente ao Usu�rio que efetuou logon.
   *         Retorna null se nenhum Usu�rio efetuou logon.
   */
  public User getLoggedUser() {
    return loggedUser;
  }

  /**
   * Retorna um Date referente a data e hora em que o usu�rio efetuou logon.
   * @return Date Retorna um Date referente a data e hora em que o usu�rio
   *         efetuou logon.
   */
  public java.util.Date getLogonTime() {
    return logonTime;
  }

  /**
   * Retorna um Date referente a data e hora em que o usu�rio efetuou logoff.
   * @return Date Retorna um Date referente a data e hora em que o usu�rio
   *         efetuou logoff.
   */
  public java.util.Date getLogoffTime() {
    return logoffTime;
  }

  /**
   * Retorna o endere�o IP do usu�rio remoto.
   * @return String Retorna o endere�o IP do usu�rio remoto.
   */
  public String getRemoteAddress() {
    return remoteAddress;
  }

  /**
   * Retorna o nome do agente do usu�rio remoto.
   * @return String Retorna o nome do agente do usu�rio remoto.
   */
  public String getRemoteAgent() {
    return remoteAgent;
  }

  /**
   * Retorna o nome do host do usu�rio remoto.
   * @return String Retorna o nome do host do usu�rio remoto.
   */
  public String getRemoteHost() {
    return remoteHost;
  }

  /**
   * Retorna a configura��o de estilo definida para o usu�rio atual.
   * @return StyleFile Retorna a configura��o de estilo definida para o usu�rio
   *         atual.
   */
  public StyleFile getStyle() {
    return style;
  }
  
  /**
   * Retorna o nome da configura��o de trabalho utilizada por esta inst�ncia
   * da aplica��o. Este valor modifica o retorno do m�todo
   * "applicationLocalWorkPath()" alterando o diret�rio de trabalho que
   * a aplica��o ir� utilizar. Este valor � passado � aplica��o atrav�s
   * do par�metro apropriado.
   * @return String Retorna o nome da configura��o de trabalho utilizada por esta
   *                inst�ncia da aplica��o.
   * @see iobjects.servlet.Controller#PARAM_WORK_CONFIGURATION
   */
  public String getWorkConfiguration() {
    return workConfiguration;
  }

  /**
   * Retorna o HelpManager respons�vel pelo gerenciamento da ajuda da aplica��o.
   * @return HelpManager Retorna o HelpManager respons�vel pelo gerenciamento
   *                     da ajuda da aplica��o.
   */
  public HelpManager helpManager() {
    return helpManager;
  }

  /**
   * Retorna a URL que d� acesso ao �cone da aplica��o. Retorna "" para o caso de
   * nenhum �cone ter sido configurada.
   * @return String Retorna a URL que d� acesso ao �cone da aplica��o. Retorna ""
   *                para o caso de nenhum �cone ter sido configurada.
   */
  public String iconURL() {
    if (applicationFile.icon().getFileName().equals(""))
      return "";
    else {
      // se j� temos a URL da icon...retorna
      if (!iconUrl.equals(""))
        return iconUrl;
      // pega o caminho local tempor�rio
      String result = tempLocalPath();
      // retira o caminho da aplica��o do in�cio
      result = result.substring(applicationLocalPath().length(), result.length());
      // troca as \ por /
      result = result.replace('\\', '/');
      // retira a barra do in�cio e do final
      if (result.charAt(0) == '/')
        result = result.substring(1, result.length());
      if (result.charAt(result.length() - 1) == '/')
        result = result.substring(0, result.length() - 1);
      // adiciona o diret�rio de imagens
      result += "/" + IMAGES_PATH;
      // adiciona o nome do arquivo
      result += "/" + applicationFile.icon().getFileName();
      // guarda o valor
      iconUrl = result;
      // retorna
      return iconUrl;
    } // if
  }

  /**
   * Retorna a inst�ncia de Log para grava��o de mensagens de log em arquivo.
   * @return Retorna a inst�ncia de Log para grava��o de mensagens de log em arquivo.
   * @since 2006
   */
  public Log log() {
    return log;
  }

  /**
   * Retorna o caminho local onde ser�o gerados os arquivos de log da aplica��o.
   * @return Retorna o caminho local onde ser�o gerados os arquivos de log da
   *         aplica��o.
   * @since 2006
   */
  public String logLocalPath() {
    return applicationLocalPath()
         + LOG_CACHE_PATH
         + File.separatorChar;
  }

  /**
   * Efetua logoff do usu�rio atual da aplica��o.
   * @throws Exception Em caso de exce��o na tentativa de salvar as configura��es
   *         atuais do usu�rio.
   */
  public void logoff() throws Exception {
    // apaga a hora de logon
    logonTime = new java.util.Date(0);
    // guarda a hora de logoff
    logoffTime = new java.util.Date();
    // apaga a propaganda
    advertising = null;
    // apaga a lista de a��es
    actionList = null;
    // apaga o gerenciador de ajuda
    helpManager = null;
    // limpa a lista de a��es recentes
    recentActionList.clear();
    // a conex�o � readOnly?
    boolean isReadOnly = connectionManager().connectionFiles().get(getDefaultConnectionName()).readOnly();
    // salva a rela��o mestre
    try {
      if (!isReadOnly)
        masterRelation.store(userParamList);
    }
    catch (Exception e) {
      // evita a exce��o que � lan�ada quando se tenta efetuar logoff
      // ap�s a sess�o ter expirado ou ap�s um Schedulle ter sido executado
    }
    // salva os atuais par�metros e notas de usu�rio
    if ((loggedUser != null) && !isReadOnly) {
      userParamList.store(userDataLocalPath() + getDefaultConnectionName() + loggedUser.getId() + USER_PARAMS_EXTENSION);
      userNoteList.store(userDataLocalPath() + getDefaultConnectionName() + loggedUser.getId() + USER_NOTES_EXTENSION);
    } // if
    // desfaz qualquer transa��o em andamento
    rollbackTransaction();
    // apaga os atuais par�metros de usu�rio
    userParamList.clear();
    // limpa a rela��o mestre
    masterRelation.clear();
    // limpa o cache de objetos de neg�cio
    businessObjects.clear();
    // limpa o dicion�rio
    dictionary.setDictionaryFile(null);
    // limpa a lista de fluxogramas
    flowChartList.clear();
    // limpa o quadro de avisos
    noticeBoard = null;
    // limpa o registro
    registry = null;
    // limpa o servi�o de seguran�a
    securityService = null;
    // escreve o evento no log
    writeLogMessage("Contador: " + NumberTools.format(transactionAccounting) + " transa��es realizadas.");
    writeLogMessage("Logoff efetuado.");
    // notifica o SessionManager
    SessionManager.getInstance().notify(session, SessionManager.NOTIFY_STATUS_USER_LOGOFF);
    // remove o usu�rio atual...
    loggedUser = null;
  }

  /**
   * Efetua logon na aplica��o. <b>O valor informado para o nome da conex�o padr�o
   * ter� os acentos e os espa�os removidos e ser� convertido para min�sculo.</b>
   * @param defaultConnectionName String Nome da conex�o padr�o com o banco de dados.
   * @param username String Nome informado pelo usu�rio.
   * @param password String Senha informada pelo usu�rio ou a senha salva.
   *                 <b>Senha salva � o Hashcode do nome do usu�rio
   *                 mais o caractere '$' mais o Hashcode da senha do usu�rio.</b>
   * @throws Exception Em caso de exce��o na tentativa de acesso ao banco de
   *         dados, verifica��o do usu�rio ou carregar suas configura��es.
   */
  public void logon(String defaultConnectionName,
                    String username,
                    String password) throws Exception {
    // se n�o mudou o usu�rio...dispara
    if ((loggedUser != null) && (loggedUser.getName().equals(username)))
      return;
    // renova nosso arquivo de Log
    log = new Log(logLocalPath());
    // inicia transa��o
    beginTransaction();
    try {
      // precisamos limpar todas as conex�es e business objects antes de uma nova tentativa
      businessObjects.clear();
      // define o nome da conex�o default: remove os acentos, os espa�os e p�e tudo em min�sculo
      this.defaultConnectionName = StringTools.format(defaultConnectionName, false, false, true, true, false).toLowerCase();
      // obt�m a inst�ncia do registro
      registry = (Registry)getBusinessObject(extensionManager.registryClassName());
      // obt�m a inst�ncia do servi�o de demonstra��o e seguran�a
      securityService = (SecurityService)getBusinessObject(extensionManager.securityServiceClassName());
      // pede ao servi�o de seguran�a que efetue logon
      User result = securityService.logon(username, password);
      // define o novo usu�rio...
      loggedUser = result;
      // notifica o SessionManager
      SessionManager.getInstance().notify(session, SessionManager.NOTIFY_STATUS_USER_LOGON);
      // guarda a hora de logon
      logonTime = new java.util.Date();
      // guarda a hora de logoff
      logoffTime = new java.util.Date(0);
      // grava o evento no log
      writeLogMessage("Logon efetuado.");
      writeLogMessage("Host: " + remoteAddress + "(" + remoteHost + ")");
      writeLogMessage("Agent: " + remoteAgent);
      // inicia o contador de transa��es
      transactionAccounting = 0;
      // preenche sua a lista de a��es
      fillActionList();
      // carrega a propaganda
      advertising = new Advertising(advertisingLocalPath(), connectionManager().connectionFiles().get(getDefaultConnectionName()).advertising(), tempLocalPath(), tempUrl());
      // carrega sua �ltima lista de par�metros de usu�rio
      userParamList.load(userDataLocalPath() + getDefaultConnectionName() + loggedUser.getId() + USER_PARAMS_EXTENSION);
      // carrega sua �ltima lista de anota��es de usu�rio
      userNoteList.load(userDataLocalPath() + getDefaultConnectionName() + loggedUser.getId() + USER_NOTES_EXTENSION);
      // carrega a rela��o mestre
      masterRelation.load(userParamList);
      // define o dicion�rio
      dictionary.setDictionaryFile(dictionaryManager.getDictionaryFile(connectionManager.connectionFiles().get(getDefaultConnectionName()).dictionary()));
      // carrega o quadro de avisos
      noticeBoard = new NoticeBoard(noticeBoardLocalPath(), getDefaultConnectionName());
      // salva tudo
      commitTransaction();
    }
    catch (Exception e) {
      // desfaz tudo
      rollbackTransaction();
      // limpa nosso estado
      loggedUser = null;
      // grava a exce��o no log
      StackTraceElement[] stackTrace = e.getStackTrace();
      StringBuffer stack = new StringBuffer();
      for (int i=0; i<stackTrace.length; i++)
        stack.append(stackTrace[i].toString() + "\r\n");
      writeLogMessage("Exce��o: " + stack.toString());
      // continua com a exce��o
      throw e;
    } // try-catch
  }

  /**
   * Retorna a URL que d� acesso � logo da aplica��o. Retorna "" para o caso de
   * nenhuma logo ter sido configurada.
   * @return String Retorna a URL que d� acesso � logo da aplica��o. Retorna ""
   *                para o caso de nenhuma logo ter sido configurada.
   */
  public String logoURL() {
    if (applicationFile.logo().getFileName().equals(""))
      return "";
    else {
      // se j� temos a URL da logo...retorna
      if (!logoUrl.equals(""))
        return logoUrl;
      // pega o caminho local tempor�rio
      String result = tempLocalPath();
      // retira o caminho da aplica��o do in�cio
      result = result.substring(applicationLocalPath().length(), result.length());
      // troca as \ por /
      result = result.replace('\\', '/');
      // retira a barra do in�cio e do final
      if (result.charAt(0) == '/')
        result = result.substring(1, result.length());
      if (result.charAt(result.length() - 1) == '/')
        result = result.substring(0, result.length() - 1);
      // adiciona o diret�rio de imagens
      result += "/" + IMAGES_PATH;
      // adiciona o nome do arquivo
      result += "/" + applicationFile.logo().getFileName();
      // guarda o valor
      logoUrl = result;
      // retorna
      return logoUrl;
    } // if
  }

  /**
   * Retorna o servi�o de e-mail da aplica��o.
   * @return MailService Retorna o servi�o de e-mail da aplica��o.
   * @since 3.1
   */
  public MailService mailService() {
    return mailService;
  }

  /**
   * Retorna a rela��o mestre em seu estado atual na sess�o.
   * @return MasterRelation Rela��o mestre em seu estado atual na sess�o.
   */
  public MasterRelation masterRelation() {
    return masterRelation;
  }

  /**
   * Retorna um ApplicationFile.Information contendo as informa��es da aplica��o.
   * @return Retorna um ApplicationFile.Information contendo as informa��es da
   *         aplica��o.
   */
  public MasterRelationFile.Information masterRelationInformation() {
    return masterRelationFile.information();
  }

  /**
   * Retorna um ApplicationFile.Information contendo as informa��es da aplica��o.
   * @return Retorna um ApplicationFile.Information contendo as informa��es da
   *         aplica��o.
   */
  public MasterRelationExFile.Information masterRelationExInformation() {
    return masterRelationExFile.information();
  }

  /**
   * Retorna o quadro de avisos contendo os itens do quadro de avisos padr�o
   * e do quadro de avisos da conex�o padr�o que o usu�rio est� utilizando.
   * @return NoticeBoard Retorna o quadro de avisos contendo os itens do quadro
   *         de avisos padr�o e do quadro de avisos da conex�o padr�o que o
   *         usu�rio est� utilizando.
   */
  public NoticeBoard noticeBoard() {
    return noticeBoard;
  }

  /**
   * Retorna o caminho local onde se encontram as mensagens do quadro de avisos.
   * @return Retorna o caminho local onde se encontram as mensagens do quadro
   *         de avisos.
   */
  public String noticeBoardLocalPath() {
    return applicationLocalWorkPath()
         + NOTICE_BOARD_PATH
         + File.separatorChar;
  }

  /**
   * Retorna a lista de a��es recentes executadas pelo usu�rio.
   * @return RecentActionList Retorna a lista de a��es recentes executadas pelo
   *         usu�rio.
   */
  public RecentActionList recentActionList() {
    return recentActionList;
  }

  /**
   * Retorna a interface Registry respons�vel pelo registro da aplica��o.
   * @return Registry Retorna a interface Registry respons�vel pelo registro da 
   *                  aplica��o.
   */
  public Registry registry() {
    return registry;
  }

  /**
   * Retorna a inst�ncia de ReleaseNotes da aplica��o.
   * @return ReleaseNotes Retorna a inst�ncia de ReleaseNotes da aplica��o.
   */
  public ReleaseNotes releaseNotes() {
    return ReleaseNotes.getInstace(configurationLocalPath());
  }

  /**
   * Libera a transa��o para permitir o funcionamento normal das tentativas de
   * iniciar, finalizar ou desfazer a transa��o.
   * @throws Exception Em caso de exce��o na tentativa de liberar a transa��o.
   */
  public void releaseTransaction() throws Exception {
    // suspende a transa��o
    transactionSuspended = false;
    // inicia transa��o
    connectionItems.commitTransaction();
  }

  /**
   * Desfaz as altera��es realizadas nas conex�es utilizadas para acesso ao banco
   * de dados.
   * @throws Exception Em caso de exce��o na tentativa de desfazer as altera��es
   *         no banco de dados.
   * @see iobjects.sql.ConnectionList
   */
  public void rollbackTransaction() throws Exception {
    // se as transa��es est�o suspensas...dispara
    if (transactionSuspended)
      return;
    // desfaz transa��o
    connectionItems.rollbackTransaction();
    // devolve as conex�es que pegamos emprestadas
    while (connectionItems.size() > 0) {
      connectionManager.returnSafeConnection(connectionItems.get(0));
      connectionItems.remove(0);
    } // while
  }

  /**
   * Retorna a interface SecurityService respons�vel pelos servi�os de seguran�a
   * da aplica��o.
   * @return SecurityService Retorna a interface SecurityService respons�vel
   * pelos servi�os de seguran�a da aplica��o.
   */
  public SecurityService securityService() {
    return securityService;
  }

  /**
   * Retorna a sess�o que mant�m a Facade como atributo.
   * @return HttpSession Retorna a sess�o que mant�m a Facade como atributo.
   */
  public HttpSession session() {
    return session;
  }

  /**
   * Define se o browser que criou a sess�o foi verificado.
   * @param browserChecked boolean True para identificar que o browser foi
   *                       verificado.
   */
  public void setBrowserChecked(boolean browserChecked) {
    this.browserChecked = browserChecked;
  }

  /**
   * Define se o browser que criou a sess�o � mobile.
   * @param browserMobile boolean True para identificar que o browser � mobile.
   */
  public void setBrowserMobile(boolean browserMobile) {
    this.browserMobile = browserMobile;
  }

  /**
   * Define se o browser que criou a sess�o � tablet.
   * @param browserMobile boolean True para identificar que o browser � tablet.
   */
  public void setBrowserTablet(boolean browserTablet) {
    this.browserTablet = browserTablet;
  }

  /**
   * Define o nome do site default no portal.
   * @param defaultSiteName String Nome do site default no portal.
   */
  public void setDefaultSiteName(String defaultSiteName) {
    this.defaultSiteName = defaultSiteName;
  }

  /**
   * Define o endere�o IP do usu�rio remoto.
   * @param remoteAddress String Define o endere�o IP do usu�rio remoto.
   */
  public void setRemoteAddress(String remoteAddress) {
    this.remoteAddress = remoteAddress;
  }

  /**
   * Define o nome do agente do usu�rio remoto.
   * @param remoteAgent String Define o nome do agente do usu�rio remoto.
   */
  public void setRemoteAgent(String remoteAgent) {
    this.remoteAgent = remoteAgent;
  }

  /**
   * Define o nome do host do usu�rio remoto.
   * @param remoteHost String Define o nome do host do usu�rio remoto.
   */
  public void setRemoteHost(String remoteHost) {
    this.remoteHost = remoteHost;
  }

  /**
   * Define a configura��o de estilo para o usu�rio atual.
   * @param style StyleFile Configura��o de estilo para o usu�rio atual.
   */
  public void setStyle(StyleFile style) {
    this.style = style;
  }

  /**
   * Retorna a inst�ncia de Schedule utilizada pela aplica��o.
   * @return Schedule Retorna a inst�ncia de Schedule utilizada pela aplica��o.
   */
  public Schedule schedule() {
    return schedule;
  }

  /**
   * Retorna o caminho local onde se encontram as tarefas agendadas.
   * @return Retorna o caminho local onde se encontram as tarefas agendadas.
   */
  public String scheduleLocalPath() {
    return applicationLocalWorkPath()
         + SCHEDULE_PATH
         + File.separatorChar;
  }

  /**
   * Retorna a inst�ncia de SirteManager utilizada pela aplica��o.
   * @return PortalManager Retorna a inst�ncia de SiteManager utilizada pela
   *                       aplica��o.
   */
  public SiteManager siteManager() {
    return siteManager;
  }

  /**
   * Retorna o caminho local onde ser�o extra�dos os arquivos dos sites do portal.
   * @return Retorna o caminho local onde ser�o extra�dos os arquivos dos sites do portal.
   */
  public String portalCacheLocalPath() {
    return applicationLocalPath()
         + PORTAL_CACHE_PATH
         + File.separatorChar;
  }

  /**
   * Retorna o caminho local onde se encontram os sites do portal.
   * @return Retorna o caminho local onde se encontram os sites do portal.
   */
  public String portalLocalPath() {
    return applicationLocalWorkPath()
         + PORTAL_PATH
         + File.separatorChar;
  }

  /**
   * Retorna a URL onde os recursos dos sites do portal podem ser localizados.
   * @return String Retorna a URL onde os recursos dos sites do portal podem ser localizados.
   */
  public String portalUrl() {
    // se j� temos a URL dos sites...retorna
    if (!portalUrl.equals(""))
      return portalUrl;
    // pega o caminho local de cache
    String result = portalCacheLocalPath();
    // retira o caminho da aplica��o do in�cio
    result = result.substring(applicationLocalPath().length(), result.length());
    // troca as \ por /
    result = result.replace('\\', '/');
    // retira a barra do in�cio e do final
    if (result.charAt(0) == '/')
      result = result.substring(1, result.length());
    if (result.charAt(result.length()-1) == '/')
      result = result.substring(0, result.length()-1);
    // guarda o valor
    portalUrl = result;
    // retorna
    return portalUrl;
  }

  /**
   * Retorna a inst�ncia de StyleManager respons�vel pelo gerenciamento dos
   * arquivos de configura��o de estilo.
   * @return StyleManager Retorna a inst�ncia de StyleManager respons�vel pelo
   *         gerenciamento dos arquivos de configura��o de estilo.
   */
  public StyleManager styleManager() {
    return styleManager;
  }

  /**
   * Retorna o caminho local onde se encontram os arquivos de estilo.
   * @return Retorna o caminho local onde se encontram os arquivos de estilo.
   */
  public String stylesLocalPath() {
    return applicationLocalPath()
         + STYLES_PATH
         + File.separatorChar;
  }

  /**
   * Retorna a URL onde os arquivos de estilo podem ser localizados.
   * @return String Retorna a URL onde os arquivos de estilo podem ser localizados.
   */
  public String stylesUrl() {
    // retorna
    return STYLES_PATH;
  }

  /**
   * Suspende a transa��o para assegurar que nada aconte�a na tentativa de
   * iniciar, finalizar ou desfazer a transa��o.
   * @throws Exception Em caso de exce��o na tentativa de suspender a transa��o.
   */
  public void suspendTransaction() throws Exception {
    // se as transa��es est�o suspensas...dispara
    if (transactionSuspended)
      return;
    // suspende a transa��o
    transactionSuspended = true;
    // inicia transa��o
    connectionItems.beginTransaction();
  }

  /**
   * Retorna o caminho local onde ser�o copiados os arquivos tempor�rios.
   * @return Retorna o caminho local onde ser�o copiados os arquivos tempor�rios.
   */
  public String tempLocalPath() {
    return applicationLocalPath()
         + TEMP_PATH
         + File.separatorChar;
  }

  /**
   * Retorna a URL onde os recursos das extens�es podem ser localizados.
   * @return String Retorna a URL onde os recursos das extens�es podem ser localizados.
   */
  public String tempUrl() {
    // se j� temos a URL de temp...retorna
    if (!tempUrl.equals(""))
      return tempUrl;
    // pega o caminho local de cache
    String result = tempLocalPath();
    // retira o caminho da aplica��o do in�cio
    result = result.substring(applicationLocalPath().length(), result.length());
    // troca as \ por /
    result = result.replace('\\', '/');
    // retira a barra do in�cio e do final
    if (result.charAt(0) == '/')
      result = result.substring(1, result.length());
    if (result.charAt(result.length()-1) == '/')
      result = result.substring(0, result.length()-1);
    // guarda o valor
    tempUrl = result;
    // retorna
    return tempUrl;
  }

  /**
   * Retorna a quantidade de beginTransaction() chamados na transa��o em andamento.
   * @return 
   */
  public int getTransactionCount() {
    return connectionItems.transactionCount();
  }
  
  /**
   * Retorna a quantidade de transa��es contabilizadas durante a se��o atual
   * deste usu�rio.
   * @return Retorna a quantidade de transa��es contabilizadas durante a se��o
   *         atual deste usu�rio.
   */
  public int transactionAccounting() {
    return transactionAccounting;
  }

  /**
   * Evento chamado pela sess�o que est� nos recebendo como atributo.
   * @param event HttpSessionBindingEvent
   */
  public void valueBound(HttpSessionBindingEvent event) {
    // guarda nossa sess�o
    session = event.getSession();
  }

  /**
   * Evento chamado pela sess�o que est� nos descartando como atributo. Efetua
   * logoff.
   * @param event HttpSessionBindingEvent
   */
  public void valueUnbound(HttpSessionBindingEvent event) {
    try {
      if (loggedUser != null)
        logoff();
    }
    catch (Exception e) {
    } // try-catch
    // n�o temos mais sess�o
    session = null;
  }

  /**
   * Retorna o caminho local onde ser�o copiados os arquivos de upload.
   * @return Retorna o caminho local onde ser�o copiados os arquivos de
   *         upload.
   */
  public String uploadLocalPath() {
    return applicationLocalPath()
         + UPLOAD_PATH
         + File.separatorChar;
  }

  /**
   * Retorna o caminho local onde ser�o copiados os pacotes do reposit�rio de
   * atualiza��o para serem instalados.
   * @return Retorna o caminho local onde ser�o copiados os pacotes do
   *         reposit�rio de atualiza��o para serem instalados.
   * @since 2006 R1
   */
  public String updateLocalPath() {
    return applicationLocalPath()
         + UPDATE_PATH
         + File.separatorChar;
  }

  /**
   * Retorna a URL onde os recursos das extens�es podem ser localizados.
   * @return String Retorna a URL onde os recursos das extens�es podem ser localizados.
   */
  public String uploadUrl() {
    // se j� temos a URL de upload...retorna
    if (!uploadUrl.equals(""))
      return uploadUrl;
    // pega o caminho local de cache
    String result = uploadLocalPath();
    // retira o caminho da aplica��o do in�cio
    result = result.substring(applicationLocalPath().length(), result.length());
    // troca as \ por /
    result = result.replace('\\', '/');
    // retira a barra do in�cio e do final
    if (result.charAt(0) == '/')
      result = result.substring(1, result.length());
    if (result.charAt(result.length()-1) == '/')
      result = result.substring(0, result.length()-1);
    // guarda o valor
    uploadUrl = result;
    // retorna
    return uploadUrl;
  }

  /**
   * Retorna a inst�ncia do atualizador da aplica��o.
   * @return Updater Retorna a inst�ncia do atualizador da aplica��o.
   * @since 2006 R1
   * @throws Exception Em caso de exce��o na tentativa de inicializar o Updater.
   */
  public Updater updater() throws Exception {
    if (updater == null)
      updater = (Updater)getBusinessObject(Updater.CLASS_NAME);
    return updater;
  }

  /**
   * Retorna o caminho local onde ser�o salvos os dados de usu�rio.
   * @return Retorna o caminho local onde ser�o salvos os dados de usu�rio.
   */
  public String userDataLocalPath() {
    return applicationLocalPath()
         + USER_DATA_PATH
         + File.separatorChar;
  }

  /**
   * Retorna a lista de par�metros de usu�rio da fachada. Esta lista deve ser
   * utilizada como reposit�rio de op��es e dados do usu�rio que efetuou logon
   * a fim de promover persist�ncia de estado bem como o uso de cookies.
   * @return ParamList Retorna a lista de par�metros de usu�rio da fachada.
   * @since 3.1
   */
  public ParamList userParamList() {
    return userParamList;
  }

  /**
   * Retorna a lista de anota��es de usu�rio da fachada.
   * @return ParamList Retorna a lista de anota��es de usu�rio da fachada.
   * @since 2006 R1
   */
  public ParamList userNoteList() {
    return userNoteList;
  }

  /**
   * Escreve 'message' no arquivo de log do usu�rio que efetuou logon e
   * retorna true em caso de sucesso.
   * @param message String Mensagem de log.
   * @return boolean Escreve 'message' no arquivo de log do usu�rio que efetuou
   *                 logon e retorna true em caso de sucesso.
   * @since 2006
   */
  public boolean writeLogMessage(String message) {
    // identifica��o do log
    String logId = "user_"
                 + defaultConnectionName + (!defaultConnectionName.equals("") ? "_" : "")
                 + (getLoggedUser() != null ? getLoggedUser().getName() : "~loggedoff");
    // escreve a mensagem
    return log.write(logId, message);
  }

}
