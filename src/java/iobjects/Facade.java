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
 * Representa a classe de fachada da aplicação.
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
   * Mantém a lista de Actions de todos os BusinessObjects da aplicação,
   */
  static private ActionList applicationActionList = null;

  static public final int APPLICATION_STATUS_LOADED       = 0;
  static public final int APPLICATION_STATUS_INITIALIZING = 1;
  static public final int APPLICATION_STATUS_READY        = 2;
  /**
   * Determina o estado da aplicação.
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
   * Construtor padrão.
   * @param workConfiguration String Nome da configuração de trabalho desta
   *        instância da aplicação. Este valor modifica o retorno do método
   *        "applicationLocalWorkPath()" alterando o diretório de trabalho que
   *        a aplicação irá utilizar. Este valor é passado à aplicação através
   *        do parâmetro apropriado.
   * @see iobjects.servlet.Controller#PARAM_WORK_CONFIGURATION
   */
  public Facade(String workConfiguration) {
    // guarda a configuração de trabalho desta instância da aplicação
    this.workConfiguration = workConfiguration;
  }

  /**
   * Retorna a propaganda utilizada para o usuário.
   * @return Advertising Retorna a propaganda utilizada para o usuário.
   */
  public Advertising advertising() {
    return advertising;
  }

  /**
   * Retorna o caminho local onde se encontram os arquivos de propaganda.
   * @return Retorna o caminho local onde se encontram os arquivos de propaganda.
   */
  public String advertisingLocalPath() {
    // retorna o caminho local da aplicação mais o caminho de configuração
    return applicationLocalWorkPath()
         + ADVERTISING_PATH
         + File.separatorChar;
  }

  /**
   * Inicializa a fachada e os objetos nela contidos.
   * @throws Exception Em caso de exceção na criação ou inicialização de um dos
   *         objetos da fachada.
   */
  public void initialize() throws Exception {
    try {
      // se a aplicação ainda não está pronta...marca como sendo inicializada
      if (applicationStatus != APPLICATION_STATUS_READY)
        applicationStatus = APPLICATION_STATUS_INITIALIZING;

      // nosso arquivo de Log
      log = new Log(logLocalPath());
      // carrega as configurações de estilo
      styleManager = StyleManager.getInstance(stylesLocalPath(), stylesUrl());
      // guarda o estilo padrão como sendo o atual
      style = styleManager.defaultStyle();
      // lê o arquivo de configuração da aplicação
      applicationFile = new ApplicationFile(configurationLocalPath());
      // lê o arquivo de configuração da relação mestre
      masterRelationFile = new MasterRelationFile(configurationLocalPath());
      // lê o arquivo de configuração da relação mestre ex
      masterRelationExFile = new MasterRelationExFile(configurationLocalPath());
      // lê o arquivo de configuração de parâmetros
      Param.setParamFile(new ParamFile(configurationLocalPath()));
      // cria a relação mestre
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
      // cria o serviço de e-mail
      mailService = MailService.getInstance(configurationLocalPath(),
                                            applicationFile.information().getName(),
                                            applicationId());
      // carrega as extensões
      extensionManager = ExtensionManager.getInstance(extensionsLocalPath(),
                                                      extensionsCacheLocalPath(),
                                                      classesLocalPath(),
                                                      extensionsUrl());
      // se não temos registro...exceção
      if (extensionManager.registryClassName().equals(""))
        throw new ExtendedException(getClass().getName(), "checkRegistry", "Nenhuma classe encontrada implementando o registro.");
      // se não temos serviço de segurança...exceção
      if (extensionManager.securityServiceClassName().equals(""))
        throw new ExtendedException(getClass().getName(), "checkSecurityService", "Nenhuma classe encontrada implementando o serviço de segurança.");
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
      // se chegou aqui...a aplicação está pronta
      applicationStatus = APPLICATION_STATUS_READY;
    }
    catch (Exception e) {
      // define a aplicação como apenas carregada
      // assim poderá ser inicializada mais uma vez
      applicationStatus = APPLICATION_STATUS_LOADED;
      // imprime a exceçao
      e.printStackTrace();
      // mostra exceção
      throw e;
    } // try-catch
  }

  /**
   * Retorna a lista de ações encontradas nos objetos de negócio das extensões
   * da aplicação e cujo usuário que efetuou logon possui direito de acesso.
   * @return ActionList Retorna a lista de ações encontradas nos objetos de
   *         negócio das extensões da aplicação e cujo usuário que efetuou logon
   *         possui direito de acesso.
   */
  public ActionList actionList() {
    return actionList;
  }

  /**
   * Retorna a lista de ações encontradas nos objetos de negócio das extensões
   * da aplicação.
   * @return ActionList Retorna a lista de ações encontradas nos objetos de
   *         negócio das extensões da aplicação.
   */
  static public ActionList applicationActionList() {
    return applicationActionList;
  }

  /**
   * Retorna um ApplicationFile.DefaultConnection contendo as informações de
   * obtenção da conexão padrão da aplicação.
   * @return Retorna um ApplicationFile.DefaultConnection contendo as informações
   *         de obtenção da conexão padrão da aplicação.
   */
  public ApplicationFile.DefaultConnection applicationDefaultConnection() {
    return applicationFile.defaultConnection();
  }

  /**
   * Retorna um ApplicationFile.DisplayMode contendo as informações de
   * modo de exibição da aplicação.
   * @return Retorna um ApplicationFile.DisplayMode contendo as informações de
   *         modo de exibição da aplicação.
   */
  public ApplicationFile.DisplayMode applicationDisplayMode() {
    return applicationFile.displayMode();
  }

  /**
   * Retorna a identificação única da aplicação para o sistema onde a mesma
   * se encontra instalada.
   * @return String Retorna a identificação única da aplicação para o sistema
   *         onde a mesma se encontra instalada.
   * @throws Exception Em caso de exceção na tentativa de gerar a identificação
   *         da aplicação.
   */
  public String applicationId() throws Exception {
    // se já temos um Id...retorna-o
    if (!applicationId.equals(""))
      return applicationId;
    // nosso resultado
    StringBuffer result = new StringBuffer("");
    // combinação reversa do nome da aplicação
    String applicationName = StringTools.reverseCombination(applicationInformation().getName(), 5);
    // transforma em Hexadecimal
    for (int i=0; i<applicationName.length(); i++)
      result.append(Integer.toHexString((int)applicationName.charAt(i)));
    // adiciona o Mac Address da máquina
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
    // põe a máscara
    applicationId = StringTools.formatCustomMask(result.toString(), "0000-0000-0000-0000-0000-0000", '0').toUpperCase();
    // retorna
    return applicationId;
  }

  /**
   * Retorna um ApplicationFile.Demo contendo as opção de demonstração da aplicação.
   * @return Retorna um ApplicationFile.Demo contendo as opção de demonstração 
   *         da aplicação.
   */
  public ApplicationFile.Demo applicationDemo() {
    return applicationFile.demo();
  }

  /**
   * Retorna um ApplicationFile.Information contendo as informações da aplicação.
   * @return Retorna um ApplicationFile.Information contendo as informações da
   *         aplicação.
   */
  public ApplicationFile.Information applicationInformation() {
    return applicationFile.information();
  }

  /**
   * Retorna um ApplicationFile.Logo contendo as informações da logo da aplicação.
   * @return Retorna um ApplicationFile.Information contendo as informações da
   *         logo da aplicação.
   */
  public ApplicationFile.Logo applicationLogo() {
    return applicationFile.logo();
  }

  /**
   * Retorna o caminho local de execução da aplicação.
   * @return Retorna o caminho local de execução da aplicação.
   */
  public String applicationLocalPath() {
    // se já temos o caminho da aplicação...retorna
    if (!applicationLocalPath.equals(""))
      return applicationLocalPath;
    // pega o caminho completo de nossa classe
    String result = getClass().getProtectionDomain().getCodeSource().getLocation().getPath().replaceAll("%20", " ");
    // se 'WEB-INF' está contido no caminho...mantém somente o que há antes
    int webInfPos = result.indexOf("WEB-INF");
    if (webInfPos >= 0) {
      result = result.substring(0, webInfPos);
    } // if
    // se 'classes' está contido no caminho...mantém somente o que há antes
    int classesPos = result.indexOf("classes");
    if (classesPos >= 0) {
      result = result.substring(0, classesPos);
    } // if
    // substitui '/' por separatorChar
    if (File.separatorChar != '/')
      result = result.replace('/', File.separatorChar);
    // se é um caminho do Windows (com :) e se inicia por '/' retira
    if ((result.indexOf(':') > 0) && (result.charAt(0) == File.separatorChar))
      result = result.substring(1, result.length()-1);
    // se termina com uma extensão...retira
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
   * Retorna o caminho local de trabalho da aplicação.
   * @return Retorna o caminho local de trabalho da aplicação.
   */
  public String applicationLocalWorkPath() {
    // se já temos o caminho local de trabalho da aplicação...retorna
    if (!applicationLocalWorkPath.equals(""))
      return applicationLocalWorkPath;
    // pega o caminho da aplicação
    String result = applicationLocalPath();
    // retira a barra do final
    if (result.charAt(result.length()-1) == File.separatorChar)
      result = result.substring(0, result.length()-1);
    // se temos configuração de trabalho para esta instância da aplicação...usa
    if (!workConfiguration.equals(""))
      result += "." + workConfiguration + File.separatorChar;
    // se não temos...usaremos o padrão
    else
      result += "." + WORK + File.separatorChar;
    // guarda o valor
    applicationLocalWorkPath = result;
    // retorna
    return applicationLocalWorkPath;
  }

  /**
   * Retorna um ApplicationFile.NoticeBoard contendo as informações de
   * exibição do quadro de avisos da aplicação.
   * @return Retorna um ApplicationFile.NoticeBoard contendo as informações
   *         de exibição do quadro de avisos da aplicação.
   */
  public ApplicationFile.NoticeBoard applicationNoticeBoard() {
    return applicationFile.noticeBoard();
  }

  /**
   * Retorna um ApplicationFile.SystemInformation contendo as informações de
   * exibição das informações do sistema da aplicação.
   * @return Retorna um ApplicationFile.SystemInformation contendo as informações
   *         de exibição das informações do sistema da aplicação.
   */
  public ApplicationFile.SystemInformation applicationSystemInformation() {
    return applicationFile.systemInformation();
  }

  /**
   * Inicia uma transação nas conexões utilizadas para acesso ao banco de dados.
   * @throws Exception Em caso de exceção na tentativa de iniciar a transação.
   */
  public void beginTransaction() throws Exception {
    // se as transações estão suspensas...dispara
    if (transactionSuspended)
      return;
    // contador de transações
    if (connectionItems.transactionCount() == 0)
      transactionAccounting++;
    // inicia transação
    connectionItems.beginTransaction();
  }

  /**
   * Disponibilia a logo informada em ApplicationFile no diretório temporário
   * de imagens.
   * @throws Exception Em caso de exceção na tentativa de copiar a logo
   *                   para o diretório temporário de imagens.
   */
  private void checkLogo() throws Exception {
    // se temos um ícone para ser usado...
    if (!applicationFile.icon().getFileName().equals("")) {
      // arquivo da icon no diretótio de configurações
      File iconSourceFile = new File(configurationLocalPath() + applicationFile.icon().getFileName());
      // arquivo da icon no diretótio temporário
      File iconTargetFile = new File(tempLocalPath() + IMAGES_PATH + File.separatorChar + applicationFile.icon().getFileName());
      // copia o arquivo para o diretório temporário
      FileTools.copyFile(iconSourceFile, iconTargetFile);
    } // if
    // se temos uma logo para ser usada...
    if (!applicationFile.logo().getFileName().equals("")) {
      // arquivo da logo no diretótio de configurações
      File logoSourceFile = new File(configurationLocalPath() + applicationFile.logo().getFileName());
      // arquivo da logo no diretótio temporário
      File logoTargetFile = new File(tempLocalPath() + IMAGES_PATH + File.separatorChar + applicationFile.logo().getFileName());
      // copia o arquivo para o diretório temporário
      FileTools.copyFile(logoSourceFile, logoTargetFile);
    } // if
  }

  /**
   * Retorna a instância de ConnectionManager utilizada pela aplicação para
   * gerenciamento das conexões com banco de dados.
   * @return ConnectionManager Retorna a instância de ConnectionManager utilizada
   *         pela aplicação para gerenciamento das conexões com banco de dados.
   */
  public ConnectionManager connectionManager() {
    return connectionManager;
  }

  /**
   * Retorna o caminho local onde se encontram as classes da aplicação.
   * @return String Retorna o caminho local onde se encontram as classes da aplicação.
   */
  public String classesLocalPath() {
    // se já temos o caminnho local das classes da aplicação...retorna
    if (!classesLocalPath.equals(""))
      return classesLocalPath;
    // pega o caminho completo de nossa classe
    String result = getClass().getProtectionDomain().getCodeSource().getLocation().getPath().replaceAll("%20", " ");
    // se 'classes' está contido no caminho...mantém somente até ai
    int classesPos = result.indexOf("classes");
    if (classesPos >= 0) {
      result = result.substring(0, classesPos + "classes".length()+1);
    } // if
    // substitui '/' por separatorChar
    if (File.separatorChar != '/')
      result = result.replace('/', File.separatorChar);
    // se é um caminho do Windows (com :) e se inicia por '/' retira
    if ((result.indexOf(':') > 0) && (result.charAt(0) == File.separatorChar))
      result = result.substring(1, result.length()-1);
    // se termina com uma extensão...retira
    if (result.indexOf('.') > 0)
      result = result.substring(0, result.lastIndexOf(File.separatorChar));
    // retorna com separatorChar no final
    if (result.charAt(result.length()-1) != File.separatorChar)
      result += File.separatorChar;
    // se não termina com 'classes'...adiciona
    if (!result.endsWith("classes" + File.separatorChar))
      result += "classes" + File.separatorChar;
    // guarda o valor
    classesLocalPath = result;
    // retorna
    return classesLocalPath;
  }

  /**
   * Aplica as alterações nas conexões utilizadas para acesso ao banco de dados.
   * @throws Exception Em caso de exceção na tentativa de aplicar as alterações
   *         no banco de dados.
   */
  public void commitTransaction() throws Exception {
    // se as transações estão suspensas...dispara
    if (transactionSuspended)
      return;
    // salva tudo
    connectionItems.commitTransaction();
    // se ainda não chegamos a 0...dispara
    if (connectionItems.transactionCount() > 0)
      return;
    // devolve as conexões que pegamos emprestadas
    while (connectionItems.size() > 0) {
      connectionManager.returnSafeConnection(connectionItems.get(0));
      connectionItems.remove(0);
    } // while
  }

  /**
   * Retorna o caminho local onde se encontram as configurações da aplicação.
   * @return Retorna o caminho local onde se encontram as configurações da
   *         aplicação.
   */
  public String configurationLocalPath() {
    // retorna o caminho local da aplicação mais o caminho de configuração
    return applicationLocalWorkPath()
         + CONFIGURATION_PATH
         + File.separatorChar;
  }

  /**
   * Retorna a instância de ConnectionItems que a fachada utiliza para gerenciar
   * as conexões de banco de dados.
   * @return 
   */
  public ConnectionItems connectionItems() {
    return connectionItems;
  }
  
  /**
   * Retorna o caminho local onde se encontram os arquivos de conexão de banco
   * de dados da aplicação.
   * @return Retorna o caminho local onde se encontram os arquivos de conexão
   *         de banco de dados da aplicação.
   */
  public String connectionsLocalPath() {
    return applicationLocalWorkPath()
         + CONNECTIONS_PATH
         + File.separatorChar;
  }

  /**
   * Retorna a interface DemoService responsável pelos serviços de demonstração
   * da aplicação.
   * @return DemoService Retorna a interface DemoService responsável
   * pelos serviços de demonstração da aplicação.
   * @throws Exception
   */
  public DemoService demoService() throws Exception {
    // retorna...
    return (DemoService)getBusinessObject(extensionManager.demoServiceClassName()); 
  }
 
  /**
   * Retorna a instância de DictionaryManager utilizada pela aplicação para gerenciar
   * os arquivos de dicionários.
   * @return DictionaryManager Retorna a instância de Dictionary utilizada pela aplicação
   *                   para gerenciar os arquivos de dicionários.
   */
  public DictionaryManager dictionaryManager() {
    return dictionaryManager;
  }

  /**
   * Retorna o caminho local onde se encontram os arquivos de dicionários 
   * da aplicação.
   * @return Retorna o caminho local onde se encontram os arquivos de dicionários
   *         da aplicação.
   */
  public String dictionaryLocalPath() {
    return applicationLocalWorkPath()
         + DICTIONARY_PATH
         + File.separatorChar;
  }

  /**
   * Retorna o caminho local onde estarão disponíveis os arquivos para download.
   * @return Retorna o caminho local onde estarão disponíveis os arquivos para
   *         download.
   */
  public String downloadLocalPath() {
    return applicationLocalPath()
         + DOWNLOAD_PATH
         + File.separatorChar;
  }

  /**
   * Retorna a URL onde os recursos das extensões podem ser localizados.
   * @return String Retorna a URL onde os recursos das extensões podem ser localizados.
   */
  public String downloadUrl() {
    // se já temos a URL de download...retorna
    if (!downloadUrl.equals(""))
      return downloadUrl;
    // pega o caminho local de cache
    String result = downloadLocalPath();
    // retira o caminho da aplicação do início
    result = result.substring(applicationLocalPath().length(), result.length());
    // troca as \ por /
    result = result.replace('\\', '/');
    // retira a barra do início e do final
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
   * Retorna true se a extensão informada por 'extensionName' foi carregada
   * e não se encontra na lista de filtro de extensões da conexão padrão
   * do usuário que efetuou logon.
   * @param extensionName String Nome da extensão. Não é necessário utilizar
   *                      a extensão do nome do arquivo.
   * @return boolean Retorna true se a extensão informada por 'extensionName'
   *                 foi carregada e não se encontra na lista de filtro de
   *                 extensões da conexão padrão do usuário que efetuou logon.
   */
  public boolean extensionLoaded(String extensionName) {
    return extensionManager.contains(extensionName) &&
           !connectionManager.connectionFiles().get(getDefaultConnectionName()).extensionFilter().contains(extensionName);
  }

  /**
   * Retorna o ExtensionManager responsável pelo gerenciamento das extensões
   * da aplicaçaão.
   * @return ExtensionManager responsável pelo gerenciamento das extensões
   *         da aplicaçaão.
   */
  public ExtensionManager extensionManager() {
    return extensionManager;
  }

  /**
   * Retorna o caminho local onde se encontram as extensões da aplicação.
   * @return Retorna o caminho local onde se encontram as extensões da aplicação.
   */
  public String extensionsLocalPath() {
    return applicationLocalWorkPath()
         + EXTENSIONS_PATH
         + File.separatorChar;
  }

  /**
   * Retorna o caminho local onde serão extraídos os arquivos das extensões.
   * @return Retorna o caminho local onde serão extraídos os arquivos das extensões.
   */
  public String extensionsCacheLocalPath() {
    return applicationLocalPath()
         + EXTENSIONS_CACHE_PATH
         + File.separatorChar;
  }

  /**
   * Retorna a URL onde os recursos das extensões podem ser localizados.
   * @return String Retorna a URL onde os recursos das extensões podem ser localizados.
   */
  public String extensionsUrl() {
    // se já temos a URL da extensões...retorna
    if (!extensionsUrl.equals(""))
      return extensionsUrl;
    // pega o caminho local de cache
    String result = extensionsCacheLocalPath();
    // retira o caminho da aplicação do início
    result = result.substring(applicationLocalPath().length(), result.length());
    // troca as \ por /
    result = result.replace('\\', '/');
    // retira a barra do início e do final
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
   * Preenche o nosso ActionList com as ações existentes nos BusinessObjects
   * das extensões da aplicação e cujo o usuário que efetuou logon possui
   * direito de acesso.
   * Preenche ainda o FAQList de HelpManager com os FAQ's contidos nos
   * BusinessObjects cujo usuário possui direito de acesso.
   * @throws Exception Em caso de exceção ao instanciar os objetos de negócio
   *         para extrair suas ações.
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
        // se estamos em um tablet e o Action é um cadastro ou processo...dispara
        if (browserTablet && !action.getName().equals("cockpit") && ((action.getCategory() == Action.CATEGORY_ENTITY) || (action.getCategory() == Action.CATEGORY_PROCESS)))
          return;
        // se o usuário é privilegiado, tem direito sobre o Action ou este
        // é um Action invisível...
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
          // criando nova instância considerando que o objeto
          // de negócio deverá ser destruído após este método
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
          // o Action foi recém lançado?
          cloneAction.setJustReleased(action.getJustReleased());
          // o Action é Beta?
          cloneAction.setBeta(action.getBeta());
          // informações de segurança sobre o Action clonado
          ActionInfo actionInfo = loggedUser.getActionInfoList().get(action.getName());
          // adiciona seus comandos
          for (int i=0; i<action.commandList().size(); i++) {
            // comando da vez
            Command command = action.commandList().get(i);
            // se o usuário é privilegiado, o Action é invisível ou
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

    // se não temos usuário que efetuou logon...dispara
    if (loggedUser == null)
      return;
    // cria uma nova lista de ações
    actionList = new ActionList();
    // cria um novo gerenciador de ajuda
    helpManager = new HelpManager();

    /**
     * Fase 1 - inicializa os Actions da aplicação
     */

    fillApplicationActionList(this);

    /**
     * Fase 2 - clona os Actions e guarda os FAQ's dos BusinessObjects
     */

    // obtém a conexão default
    ConnectionFile defaultConnectionFile = connectionManager.connectionFiles().get(getDefaultConnectionName());
    // loop nos Actions da aplicação
    for (int i=0; i<applicationActionList.size(); i++) {
      // ação da vez
      Action action = applicationActionList.get(i);
      // se o Action é aninhado...continua
      if (action.getParentAction() != null)
        continue;
      // cria uma instância da classe que o contém
      BusinessObject object = (BusinessObject)(Class.forName(action.getDeclaringClassName())).newInstance();
      // se o Action pertence a uma extensão filtrada...continua
      if (defaultConnectionFile.extensionFilter().contains(object.extension().getName()))
        continue;
      // clona e adiciona na lista
      new CloneAction(object, action, null);
    } // for
    // ordena alfabeticamente e por categoria
    actionList.sort();
    // ordena os FAQs alfabeticamente
    helpManager.faqList().sort();
    // avisa ao garbage collector para fazer uma limpeza na memória
    System.gc();
  }

  /**
   * Preenche o ActionList da aplicação com os Action's encontrados nas extensões.
   * @param instance Facade Instância de Facade utilizada para obtenção de
   *                 algumas informações necessárias.
   * @throws Exception Em caso de exceção na tentativa de instanciar BusinessObjects.
   */
  static private synchronized void fillApplicationActionList(Facade instance) throws Exception {
    // se os Actions já foram inicializados...dispara
    if (actionsInitialized)
      return;

    // data de hoje
    java.util.Date today = DateTools.getActualDate();
    // lista de Actions da aplicação
    applicationActionList = new ActionList();
    // loop nas extensões
    for (int i=0; i<instance.extensionManager.extensions().length; i++) {
      // Extensão da vez
      Extension extension = instance.extensionManager.extensions()[i];
      // constrói a URL parcial para acesso aos JSPs da extensão
      // enquanto extraidos no diretório de cache da mesma
      String extensionCachePath = extension.getCacheFilesPath();
      extensionCachePath = extensionCachePath.substring(instance.applicationLocalPath().length(), extensionCachePath.length());
      extensionCachePath = extensionCachePath.replace('\\', '/');
      // loop nos objetos de negócio
      for (int w=0; w<extension.getClassNames().length; w++) {
        // classe da vez
        String className = extension.getClassNames()[w];
        // cria uma instância da classe
        BusinessObject object = (BusinessObject)(Class.forName(className)).newInstance();
        // loop nas ações
        for (int z=0; z<object.actionList().size(); z++) {
          // ação da vez
          Action action = object.actionList().get(z);
          // guarda o nome da classe na qual o Action foi declarado
          action.setDeclaringClassName(object.getClass().getName());
          // ajusta o caminho do seu JSP
          action.setJsp(extensionCachePath + action.getJsp());
          // define se o Action foi recém lançado na versão mais recente da aplicação
          if (today.compareTo(instance.releaseNotes().mostRecentRelease().getEmphasize()) <= 0)
            action.setJustReleased(instance.releaseNotes().mostRecentRelease().changeList().contains(action.getName()));
          // define se o Action é Beta na versão mais recente da aplicação
          ReleaseNotesFile.Change change = instance.releaseNotes().mostRecentRelease().changeList().get(action.getName());
          action.setBeta((change != null) && change.isBeta());
          // adiciona na lista da aplicação
          applicationActionList.add(action);
        } // for z
      } // for w
    } // for i
    // os Actions foram inicializados
    actionsInitialized = true;
    // avisa ao garbage collector para fazer uma limpeza na memória
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
   * Procura por uma instancia de 'objectClass' no pool de objetos de negócio.
   * @param businessObjectClass Classe do objeto de negócios que se deseja localizar.
   * @return Procura por uma instancia de 'objectClass' no pool de objetos de negócio.
   */
  private BusinessObject findBusinessObject(Class businessObjectClass) {
    // loop nos objetos de negócio
    Object result = null;
    for (int i=0; i<businessObjects.size(); i++) {
      // objeto da vez
      result = businessObjects.elementAt(i);
      // se é da classe desejada...
      if (businessObjectClass.equals(result.getClass())) {
        // guarda nossa referência no objeto
        ((BusinessObject)result).setFacade(this);
        // retorna
        return (BusinessObject)result;
      } // if
    } // for
    // se não achamos...retorna nada
    return null;
  }

  /**
   * Retorna a instância de FlowChartManager utilizada pela aplicação para
   * gerenciamento dos fluxogramas.
   * @return FlowChartManager Retorna a instância de FlowChartManager utilizada
   *         pela aplicação para gerenciamento dos fluxogramas.
   */
  public FlowChartManager flowChartManager() {
    return flowChartManager;
  }

  /**
   * Retorna o caminho local onde se encontram os arquivos de fluxogramas
   * da aplicação.
   * @return Retorna o caminho local onde se encontram os arquivos de fluxogramas
   *         da aplicação.
   */
  public String flowChartsLocalPath() {
    return applicationLocalWorkPath()
         + FLOW_CHARTS_PATH
         + File.separatorChar;
  }

  /**
   * Retorna o status da aplicação.
   * @return int Retorna o status da aplicação.
   */
  static public int getApplicationStatus() {
    return applicationStatus;
  }

  /**
   * Retorna true se o browser que criou a sessão foi verificado.
   * @return boolean Retorna true se o browser que criou a sessão foi verificado.
   */
  public boolean getBrowserChecked() {
    return browserChecked;
  }

  /**
   * Retorna true se o browser que criou a sessão é mobile.
   * @return boolean Retorna true se o browser que criou a sessão é mobile.
   */
  public boolean getBrowserMobile() {
    return browserMobile;
  }

  /**
   * Retorna true se o browser que criou a sessão é tablet.
   * @return boolean Retorna true se o browser que criou a sessão é tablet.
   */
  public boolean getBrowserTablet() {
    return browserTablet;
  }

  /**
   * Retorna uma instância de 'className' configurada e pronta para uso.
   * @param className Nome da classe que se deseja uma instância.
   * @return Retorna uma instância de 'className' configurada e pronta para uso.
   * @throws Exception Em caso de exceção na tentativa de instanciar o objeto
   *                   de negócio.
   */
  public BusinessObject getBusinessObject(String className) throws Exception {
    // tenta carregar a classe desejada
    Class businessObjectClass = Class.forName(className);
    // já existe uma instância desta classe?
    BusinessObject result = (BusinessObject)findBusinessObject(businessObjectClass);
    // se temos...retorna
    if (result != null)
      return result;
    // se não temos...cria
    else {
      // nova instância
      result = (BusinessObject)businessObjectClass.newInstance();
      // nome da conexão para ser usada
      String connectionName = connectionManager.getConnectionNameForClassMapping(className, getDefaultConnectionName());
      // configura o nome da conexão
      result.setConnectionName(connectionName);
      // configura o arquivo de conexão
      result.setConnectionFile(connectionManager.connectionFiles().get(connectionName));
      // nossa referência
      result.setFacade(this);
      // inicializa
      result.initialize();
      // põe na nossa lista
      businessObjects.add(result);
      // retorna
      return result;
    } // if
  }

  /**
   * Retorna uma instância de 'className' configurada e pronta para uso.
   * @param className Nome da classe que se deseja uma instância.
   * @return Retorna uma instância de 'className' configurada e pronta para uso.
   * @throws Exception Em caso de exceção na tentativa de instanciar o objeto
   *                   de negócio.
   */
  public Card getCard(String className) throws Exception {
    return (Card)getBusinessObject(className);
  }

  /**
   * Retorna o Connection referenciado por 'connectionName'.
   * <b>De modo a trabalhar com o pool de conexões de forma eficiente, é necessário
   * ter uma transação iniciada para usar este médoto.</b>
   * @param connectionName Nome da conexão que se deseja retornar.
   * @return Retorna o Connection referenciado por 'connectionName'.
   * @throws Exception Em caso de exceção na tentativa de localizar uma conexão
   *                   com o nome informado ou de instanciá-la.
   */
  public Connection getConnection(String connectionName) throws Exception {
    // se não estamos em transação...retorna uma conexão da lista insegura
    if (connectionItems.transactionCount() == 0)
      return connectionManager.getUnsafeConnection(connectionName).connection();
    // se estamos em transação...
    else {
      // procura a conexão desejada em nossa lista
      ConnectionItem result = connectionItems.get(connectionName);
      // se já temos...retorna
      if (result != null)
        return result.connection();
      // se ainda não temos...
      else {
        // pede uma emprestada ao ConnectionManager
        result = connectionManager.borrowSafeConnection(connectionName);
        // se não havia nenhuma para emprestar...cria uma nova
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
   * Retorna o ConnectionFile contendo as configurações da conexão que deverá
   * ser utilizada para a classe referenciada por 'className' levando em
   * consideração os mapeamentos.
   * @param className String Nome da classe cujo arquivo de conexãose deseja retornar.
   * @return Retorna o ConnectionFile contendo as configurações da conexão que
   *         deverá ser utilizada para a classe referenciada por 'className'
   *         levando em consideração os mapeamentos.
   */
  public ConnectionFile getConnectionFileForClassName(String className) {
    // nome da conexão para ser usada
    String connectionName = connectionManager.getConnectionNameForClassMapping(className, getDefaultConnectionName());
    // retorna o arquivo de conexão
    return connectionManager.connectionFiles().get(connectionName);
  }

  /**
   * Retorna o Connection que deverá ser utilizada para a classe referenciada por
   * 'className' levando em consideração os mapeamentos.
   * <b>De modo a trabalhar com o pool de conexões de forma eficiente, é necessário
   * ter uma transação iniciada para usar este médoto.</b>
   * @param className String Nome da classe cuja conexão se deseja retornar.
   * @throws Exception Em caso de exceção na tentativa de criar uma nova
   *         conexão com o banco de dados.
   * @return Retorna o Connection que deverá ser utilizada para a classe
   *         referenciada por 'className' levando em consideração os mapeamentos.
   */
  public Connection getConnectionForClassName(String className) throws Exception {
    // nome da conexão para ser usada
    String connectionName = connectionManager.getConnectionNameForClassMapping(className, getDefaultConnectionName());
    // retorna a conexão
    return getConnection(connectionName);
  }

  /**
   * Retorna o nome da conexão default com o banco de dados.
   * @return String Retorna o nome da conexão default com o banco de dados.
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
   * Retorna o dicionário apropriado para a sessão.
   * @return Retorna o dicionário apropriado para a sessão.
   */
  public Dictionary getDictionary() {
    return dictionary;
  }

  /**
   * Retorna uma instância de 'className' configurada e pronta para uso.
   * @param className Nome da classe que se deseja uma instância.
   * @return Retorna uma instância de 'className' configurada e pronta para uso.
   * @throws Exception Em caso de exceção na tentativa de instanciar o objeto
   *                   de negócio.
   */
  public Entity getEntity(String className) throws Exception {
    return (Entity)getBusinessObject(className);
  }

  /**
   * Retorna o FlowChart referenciado por 'flowChartName'. Se nenhum FlowChart
   * for localizado um novo será criado.
   * @param flowChartName Nome do FlowChart que se deseja retornar.
   * @return Retorna o FlowChart referenciado por 'flowChartNaneName'. Se nenhum
   *         Connection for localizado um novo será criado.
   * @throws Exception Em caso de exceção na tentativa de localizar um FlowChart
   *                   com o nome informado ou de instanciá-lo.
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
    // se não encontramos...
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
   * Retorna uma instância de 'className' configurada e pronta para uso.
   * @param className Nome da classe que se deseja uma instância.
   * @return Retorna uma instância de 'className' configurada e pronta para uso.
   * @throws Exception Em caso de exceção na tentativa de instanciar o objeto
   *                   de negócio.
   */
  public iobjects.process.Process getProcess(String className) throws Exception {
    return (iobjects.process.Process)getBusinessObject(className);
  }

  /**
   * Retorna uma instância de 'className' configurada e pronta para uso.
   * @param className Nome da classe do relatório que se deseja uma instância.
   * @return Retorna uma instância de 'className' configurada e pronta para uso.
   * @throws Exception Em caso de exceção na tentativa de instanciar o objeto
   *                   de negócio.
   */
  public Report getReport(String className) throws Exception {
    return (Report)getBusinessObject(className);
  }

  /**
   * Retorna o User referente ao Usuário que efetuou logon. Retorna null
   * se nenhum Usuário efetuou logon.
   * @return Retorna o User referente ao Usuário que efetuou logon.
   *         Retorna null se nenhum Usuário efetuou logon.
   */
  public User getLoggedUser() {
    return loggedUser;
  }

  /**
   * Retorna um Date referente a data e hora em que o usuário efetuou logon.
   * @return Date Retorna um Date referente a data e hora em que o usuário
   *         efetuou logon.
   */
  public java.util.Date getLogonTime() {
    return logonTime;
  }

  /**
   * Retorna um Date referente a data e hora em que o usuário efetuou logoff.
   * @return Date Retorna um Date referente a data e hora em que o usuário
   *         efetuou logoff.
   */
  public java.util.Date getLogoffTime() {
    return logoffTime;
  }

  /**
   * Retorna o endereço IP do usuário remoto.
   * @return String Retorna o endereço IP do usuário remoto.
   */
  public String getRemoteAddress() {
    return remoteAddress;
  }

  /**
   * Retorna o nome do agente do usuário remoto.
   * @return String Retorna o nome do agente do usuário remoto.
   */
  public String getRemoteAgent() {
    return remoteAgent;
  }

  /**
   * Retorna o nome do host do usuário remoto.
   * @return String Retorna o nome do host do usuário remoto.
   */
  public String getRemoteHost() {
    return remoteHost;
  }

  /**
   * Retorna a configuração de estilo definida para o usuário atual.
   * @return StyleFile Retorna a configuração de estilo definida para o usuário
   *         atual.
   */
  public StyleFile getStyle() {
    return style;
  }
  
  /**
   * Retorna o nome da configuração de trabalho utilizada por esta instância
   * da aplicação. Este valor modifica o retorno do método
   * "applicationLocalWorkPath()" alterando o diretório de trabalho que
   * a aplicação irá utilizar. Este valor é passado à aplicação através
   * do parâmetro apropriado.
   * @return String Retorna o nome da configuração de trabalho utilizada por esta
   *                instância da aplicação.
   * @see iobjects.servlet.Controller#PARAM_WORK_CONFIGURATION
   */
  public String getWorkConfiguration() {
    return workConfiguration;
  }

  /**
   * Retorna o HelpManager responsável pelo gerenciamento da ajuda da aplicação.
   * @return HelpManager Retorna o HelpManager responsável pelo gerenciamento
   *                     da ajuda da aplicação.
   */
  public HelpManager helpManager() {
    return helpManager;
  }

  /**
   * Retorna a URL que dá acesso ao ícone da aplicação. Retorna "" para o caso de
   * nenhum ícone ter sido configurada.
   * @return String Retorna a URL que dá acesso ao ícone da aplicação. Retorna ""
   *                para o caso de nenhum ícone ter sido configurada.
   */
  public String iconURL() {
    if (applicationFile.icon().getFileName().equals(""))
      return "";
    else {
      // se já temos a URL da icon...retorna
      if (!iconUrl.equals(""))
        return iconUrl;
      // pega o caminho local temporário
      String result = tempLocalPath();
      // retira o caminho da aplicação do início
      result = result.substring(applicationLocalPath().length(), result.length());
      // troca as \ por /
      result = result.replace('\\', '/');
      // retira a barra do início e do final
      if (result.charAt(0) == '/')
        result = result.substring(1, result.length());
      if (result.charAt(result.length() - 1) == '/')
        result = result.substring(0, result.length() - 1);
      // adiciona o diretório de imagens
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
   * Retorna a instância de Log para gravação de mensagens de log em arquivo.
   * @return Retorna a instância de Log para gravação de mensagens de log em arquivo.
   * @since 2006
   */
  public Log log() {
    return log;
  }

  /**
   * Retorna o caminho local onde serão gerados os arquivos de log da aplicação.
   * @return Retorna o caminho local onde serão gerados os arquivos de log da
   *         aplicação.
   * @since 2006
   */
  public String logLocalPath() {
    return applicationLocalPath()
         + LOG_CACHE_PATH
         + File.separatorChar;
  }

  /**
   * Efetua logoff do usuário atual da aplicação.
   * @throws Exception Em caso de exceção na tentativa de salvar as configurações
   *         atuais do usuário.
   */
  public void logoff() throws Exception {
    // apaga a hora de logon
    logonTime = new java.util.Date(0);
    // guarda a hora de logoff
    logoffTime = new java.util.Date();
    // apaga a propaganda
    advertising = null;
    // apaga a lista de ações
    actionList = null;
    // apaga o gerenciador de ajuda
    helpManager = null;
    // limpa a lista de ações recentes
    recentActionList.clear();
    // a conexão é readOnly?
    boolean isReadOnly = connectionManager().connectionFiles().get(getDefaultConnectionName()).readOnly();
    // salva a relação mestre
    try {
      if (!isReadOnly)
        masterRelation.store(userParamList);
    }
    catch (Exception e) {
      // evita a exceção que é lançada quando se tenta efetuar logoff
      // após a sessão ter expirado ou após um Schedulle ter sido executado
    }
    // salva os atuais parâmetros e notas de usuário
    if ((loggedUser != null) && !isReadOnly) {
      userParamList.store(userDataLocalPath() + getDefaultConnectionName() + loggedUser.getId() + USER_PARAMS_EXTENSION);
      userNoteList.store(userDataLocalPath() + getDefaultConnectionName() + loggedUser.getId() + USER_NOTES_EXTENSION);
    } // if
    // desfaz qualquer transação em andamento
    rollbackTransaction();
    // apaga os atuais parâmetros de usuário
    userParamList.clear();
    // limpa a relação mestre
    masterRelation.clear();
    // limpa o cache de objetos de negócio
    businessObjects.clear();
    // limpa o dicionário
    dictionary.setDictionaryFile(null);
    // limpa a lista de fluxogramas
    flowChartList.clear();
    // limpa o quadro de avisos
    noticeBoard = null;
    // limpa o registro
    registry = null;
    // limpa o serviço de segurança
    securityService = null;
    // escreve o evento no log
    writeLogMessage("Contador: " + NumberTools.format(transactionAccounting) + " transações realizadas.");
    writeLogMessage("Logoff efetuado.");
    // notifica o SessionManager
    SessionManager.getInstance().notify(session, SessionManager.NOTIFY_STATUS_USER_LOGOFF);
    // remove o usuário atual...
    loggedUser = null;
  }

  /**
   * Efetua logon na aplicação. <b>O valor informado para o nome da conexão padrão
   * terá os acentos e os espaços removidos e será convertido para minúsculo.</b>
   * @param defaultConnectionName String Nome da conexão padrão com o banco de dados.
   * @param username String Nome informado pelo usuário.
   * @param password String Senha informada pelo usuário ou a senha salva.
   *                 <b>Senha salva é o Hashcode do nome do usuário
   *                 mais o caractere '$' mais o Hashcode da senha do usuário.</b>
   * @throws Exception Em caso de exceção na tentativa de acesso ao banco de
   *         dados, verificação do usuário ou carregar suas configurações.
   */
  public void logon(String defaultConnectionName,
                    String username,
                    String password) throws Exception {
    // se não mudou o usuário...dispara
    if ((loggedUser != null) && (loggedUser.getName().equals(username)))
      return;
    // renova nosso arquivo de Log
    log = new Log(logLocalPath());
    // inicia transação
    beginTransaction();
    try {
      // precisamos limpar todas as conexões e business objects antes de uma nova tentativa
      businessObjects.clear();
      // define o nome da conexão default: remove os acentos, os espaços e põe tudo em minúsculo
      this.defaultConnectionName = StringTools.format(defaultConnectionName, false, false, true, true, false).toLowerCase();
      // obtém a instância do registro
      registry = (Registry)getBusinessObject(extensionManager.registryClassName());
      // obtém a instância do serviço de demonstração e segurança
      securityService = (SecurityService)getBusinessObject(extensionManager.securityServiceClassName());
      // pede ao serviço de segurança que efetue logon
      User result = securityService.logon(username, password);
      // define o novo usuário...
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
      // inicia o contador de transações
      transactionAccounting = 0;
      // preenche sua a lista de ações
      fillActionList();
      // carrega a propaganda
      advertising = new Advertising(advertisingLocalPath(), connectionManager().connectionFiles().get(getDefaultConnectionName()).advertising(), tempLocalPath(), tempUrl());
      // carrega sua última lista de parâmetros de usuário
      userParamList.load(userDataLocalPath() + getDefaultConnectionName() + loggedUser.getId() + USER_PARAMS_EXTENSION);
      // carrega sua última lista de anotações de usuário
      userNoteList.load(userDataLocalPath() + getDefaultConnectionName() + loggedUser.getId() + USER_NOTES_EXTENSION);
      // carrega a relação mestre
      masterRelation.load(userParamList);
      // define o dicionário
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
      // grava a exceção no log
      StackTraceElement[] stackTrace = e.getStackTrace();
      StringBuffer stack = new StringBuffer();
      for (int i=0; i<stackTrace.length; i++)
        stack.append(stackTrace[i].toString() + "\r\n");
      writeLogMessage("Exceção: " + stack.toString());
      // continua com a exceção
      throw e;
    } // try-catch
  }

  /**
   * Retorna a URL que dá acesso à logo da aplicação. Retorna "" para o caso de
   * nenhuma logo ter sido configurada.
   * @return String Retorna a URL que dá acesso à logo da aplicação. Retorna ""
   *                para o caso de nenhuma logo ter sido configurada.
   */
  public String logoURL() {
    if (applicationFile.logo().getFileName().equals(""))
      return "";
    else {
      // se já temos a URL da logo...retorna
      if (!logoUrl.equals(""))
        return logoUrl;
      // pega o caminho local temporário
      String result = tempLocalPath();
      // retira o caminho da aplicação do início
      result = result.substring(applicationLocalPath().length(), result.length());
      // troca as \ por /
      result = result.replace('\\', '/');
      // retira a barra do início e do final
      if (result.charAt(0) == '/')
        result = result.substring(1, result.length());
      if (result.charAt(result.length() - 1) == '/')
        result = result.substring(0, result.length() - 1);
      // adiciona o diretório de imagens
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
   * Retorna o serviço de e-mail da aplicação.
   * @return MailService Retorna o serviço de e-mail da aplicação.
   * @since 3.1
   */
  public MailService mailService() {
    return mailService;
  }

  /**
   * Retorna a relação mestre em seu estado atual na sessão.
   * @return MasterRelation Relação mestre em seu estado atual na sessão.
   */
  public MasterRelation masterRelation() {
    return masterRelation;
  }

  /**
   * Retorna um ApplicationFile.Information contendo as informações da aplicação.
   * @return Retorna um ApplicationFile.Information contendo as informações da
   *         aplicação.
   */
  public MasterRelationFile.Information masterRelationInformation() {
    return masterRelationFile.information();
  }

  /**
   * Retorna um ApplicationFile.Information contendo as informações da aplicação.
   * @return Retorna um ApplicationFile.Information contendo as informações da
   *         aplicação.
   */
  public MasterRelationExFile.Information masterRelationExInformation() {
    return masterRelationExFile.information();
  }

  /**
   * Retorna o quadro de avisos contendo os itens do quadro de avisos padrão
   * e do quadro de avisos da conexão padrão que o usuário está utilizando.
   * @return NoticeBoard Retorna o quadro de avisos contendo os itens do quadro
   *         de avisos padrão e do quadro de avisos da conexão padrão que o
   *         usuário está utilizando.
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
   * Retorna a lista de ações recentes executadas pelo usuário.
   * @return RecentActionList Retorna a lista de ações recentes executadas pelo
   *         usuário.
   */
  public RecentActionList recentActionList() {
    return recentActionList;
  }

  /**
   * Retorna a interface Registry responsável pelo registro da aplicação.
   * @return Registry Retorna a interface Registry responsável pelo registro da 
   *                  aplicação.
   */
  public Registry registry() {
    return registry;
  }

  /**
   * Retorna a instância de ReleaseNotes da aplicação.
   * @return ReleaseNotes Retorna a instância de ReleaseNotes da aplicação.
   */
  public ReleaseNotes releaseNotes() {
    return ReleaseNotes.getInstace(configurationLocalPath());
  }

  /**
   * Libera a transação para permitir o funcionamento normal das tentativas de
   * iniciar, finalizar ou desfazer a transação.
   * @throws Exception Em caso de exceção na tentativa de liberar a transação.
   */
  public void releaseTransaction() throws Exception {
    // suspende a transação
    transactionSuspended = false;
    // inicia transação
    connectionItems.commitTransaction();
  }

  /**
   * Desfaz as alterações realizadas nas conexões utilizadas para acesso ao banco
   * de dados.
   * @throws Exception Em caso de exceção na tentativa de desfazer as alterações
   *         no banco de dados.
   * @see iobjects.sql.ConnectionList
   */
  public void rollbackTransaction() throws Exception {
    // se as transações estão suspensas...dispara
    if (transactionSuspended)
      return;
    // desfaz transação
    connectionItems.rollbackTransaction();
    // devolve as conexões que pegamos emprestadas
    while (connectionItems.size() > 0) {
      connectionManager.returnSafeConnection(connectionItems.get(0));
      connectionItems.remove(0);
    } // while
  }

  /**
   * Retorna a interface SecurityService responsável pelos serviços de segurança
   * da aplicação.
   * @return SecurityService Retorna a interface SecurityService responsável
   * pelos serviços de segurança da aplicação.
   */
  public SecurityService securityService() {
    return securityService;
  }

  /**
   * Retorna a sessão que mantém a Facade como atributo.
   * @return HttpSession Retorna a sessão que mantém a Facade como atributo.
   */
  public HttpSession session() {
    return session;
  }

  /**
   * Define se o browser que criou a sessão foi verificado.
   * @param browserChecked boolean True para identificar que o browser foi
   *                       verificado.
   */
  public void setBrowserChecked(boolean browserChecked) {
    this.browserChecked = browserChecked;
  }

  /**
   * Define se o browser que criou a sessão é mobile.
   * @param browserMobile boolean True para identificar que o browser é mobile.
   */
  public void setBrowserMobile(boolean browserMobile) {
    this.browserMobile = browserMobile;
  }

  /**
   * Define se o browser que criou a sessão é tablet.
   * @param browserMobile boolean True para identificar que o browser é tablet.
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
   * Define o endereço IP do usuário remoto.
   * @param remoteAddress String Define o endereço IP do usuário remoto.
   */
  public void setRemoteAddress(String remoteAddress) {
    this.remoteAddress = remoteAddress;
  }

  /**
   * Define o nome do agente do usuário remoto.
   * @param remoteAgent String Define o nome do agente do usuário remoto.
   */
  public void setRemoteAgent(String remoteAgent) {
    this.remoteAgent = remoteAgent;
  }

  /**
   * Define o nome do host do usuário remoto.
   * @param remoteHost String Define o nome do host do usuário remoto.
   */
  public void setRemoteHost(String remoteHost) {
    this.remoteHost = remoteHost;
  }

  /**
   * Define a configuração de estilo para o usuário atual.
   * @param style StyleFile Configuração de estilo para o usuário atual.
   */
  public void setStyle(StyleFile style) {
    this.style = style;
  }

  /**
   * Retorna a instância de Schedule utilizada pela aplicação.
   * @return Schedule Retorna a instância de Schedule utilizada pela aplicação.
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
   * Retorna a instância de SirteManager utilizada pela aplicação.
   * @return PortalManager Retorna a instância de SiteManager utilizada pela
   *                       aplicação.
   */
  public SiteManager siteManager() {
    return siteManager;
  }

  /**
   * Retorna o caminho local onde serão extraídos os arquivos dos sites do portal.
   * @return Retorna o caminho local onde serão extraídos os arquivos dos sites do portal.
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
    // se já temos a URL dos sites...retorna
    if (!portalUrl.equals(""))
      return portalUrl;
    // pega o caminho local de cache
    String result = portalCacheLocalPath();
    // retira o caminho da aplicação do início
    result = result.substring(applicationLocalPath().length(), result.length());
    // troca as \ por /
    result = result.replace('\\', '/');
    // retira a barra do início e do final
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
   * Retorna a instância de StyleManager responsável pelo gerenciamento dos
   * arquivos de configuração de estilo.
   * @return StyleManager Retorna a instância de StyleManager responsável pelo
   *         gerenciamento dos arquivos de configuração de estilo.
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
   * Suspende a transação para assegurar que nada aconteça na tentativa de
   * iniciar, finalizar ou desfazer a transação.
   * @throws Exception Em caso de exceção na tentativa de suspender a transação.
   */
  public void suspendTransaction() throws Exception {
    // se as transações estão suspensas...dispara
    if (transactionSuspended)
      return;
    // suspende a transação
    transactionSuspended = true;
    // inicia transação
    connectionItems.beginTransaction();
  }

  /**
   * Retorna o caminho local onde serão copiados os arquivos temporários.
   * @return Retorna o caminho local onde serão copiados os arquivos temporários.
   */
  public String tempLocalPath() {
    return applicationLocalPath()
         + TEMP_PATH
         + File.separatorChar;
  }

  /**
   * Retorna a URL onde os recursos das extensões podem ser localizados.
   * @return String Retorna a URL onde os recursos das extensões podem ser localizados.
   */
  public String tempUrl() {
    // se já temos a URL de temp...retorna
    if (!tempUrl.equals(""))
      return tempUrl;
    // pega o caminho local de cache
    String result = tempLocalPath();
    // retira o caminho da aplicação do início
    result = result.substring(applicationLocalPath().length(), result.length());
    // troca as \ por /
    result = result.replace('\\', '/');
    // retira a barra do início e do final
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
   * Retorna a quantidade de beginTransaction() chamados na transação em andamento.
   * @return 
   */
  public int getTransactionCount() {
    return connectionItems.transactionCount();
  }
  
  /**
   * Retorna a quantidade de transações contabilizadas durante a seção atual
   * deste usuário.
   * @return Retorna a quantidade de transações contabilizadas durante a seção
   *         atual deste usuário.
   */
  public int transactionAccounting() {
    return transactionAccounting;
  }

  /**
   * Evento chamado pela sessão que está nos recebendo como atributo.
   * @param event HttpSessionBindingEvent
   */
  public void valueBound(HttpSessionBindingEvent event) {
    // guarda nossa sessão
    session = event.getSession();
  }

  /**
   * Evento chamado pela sessão que está nos descartando como atributo. Efetua
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
    // não temos mais sessão
    session = null;
  }

  /**
   * Retorna o caminho local onde serão copiados os arquivos de upload.
   * @return Retorna o caminho local onde serão copiados os arquivos de
   *         upload.
   */
  public String uploadLocalPath() {
    return applicationLocalPath()
         + UPLOAD_PATH
         + File.separatorChar;
  }

  /**
   * Retorna o caminho local onde serão copiados os pacotes do repositório de
   * atualização para serem instalados.
   * @return Retorna o caminho local onde serão copiados os pacotes do
   *         repositório de atualização para serem instalados.
   * @since 2006 R1
   */
  public String updateLocalPath() {
    return applicationLocalPath()
         + UPDATE_PATH
         + File.separatorChar;
  }

  /**
   * Retorna a URL onde os recursos das extensões podem ser localizados.
   * @return String Retorna a URL onde os recursos das extensões podem ser localizados.
   */
  public String uploadUrl() {
    // se já temos a URL de upload...retorna
    if (!uploadUrl.equals(""))
      return uploadUrl;
    // pega o caminho local de cache
    String result = uploadLocalPath();
    // retira o caminho da aplicação do início
    result = result.substring(applicationLocalPath().length(), result.length());
    // troca as \ por /
    result = result.replace('\\', '/');
    // retira a barra do início e do final
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
   * Retorna a instância do atualizador da aplicação.
   * @return Updater Retorna a instância do atualizador da aplicação.
   * @since 2006 R1
   * @throws Exception Em caso de exceção na tentativa de inicializar o Updater.
   */
  public Updater updater() throws Exception {
    if (updater == null)
      updater = (Updater)getBusinessObject(Updater.CLASS_NAME);
    return updater;
  }

  /**
   * Retorna o caminho local onde serão salvos os dados de usuário.
   * @return Retorna o caminho local onde serão salvos os dados de usuário.
   */
  public String userDataLocalPath() {
    return applicationLocalPath()
         + USER_DATA_PATH
         + File.separatorChar;
  }

  /**
   * Retorna a lista de parâmetros de usuário da fachada. Esta lista deve ser
   * utilizada como repositório de opções e dados do usuário que efetuou logon
   * a fim de promover persistência de estado bem como o uso de cookies.
   * @return ParamList Retorna a lista de parâmetros de usuário da fachada.
   * @since 3.1
   */
  public ParamList userParamList() {
    return userParamList;
  }

  /**
   * Retorna a lista de anotações de usuário da fachada.
   * @return ParamList Retorna a lista de anotações de usuário da fachada.
   * @since 2006 R1
   */
  public ParamList userNoteList() {
    return userNoteList;
  }

  /**
   * Escreve 'message' no arquivo de log do usuário que efetuou logon e
   * retorna true em caso de sucesso.
   * @param message String Mensagem de log.
   * @return boolean Escreve 'message' no arquivo de log do usuário que efetuou
   *                 logon e retorna true em caso de sucesso.
   * @since 2006
   */
  public boolean writeLogMessage(String message) {
    // identificação do log
    String logId = "user_"
                 + defaultConnectionName + (!defaultConnectionName.equals("") ? "_" : "")
                 + (getLoggedUser() != null ? getLoggedUser().getName() : "~loggedoff");
    // escreve a mensagem
    return log.write(logId, message);
  }

}
