package iobjects.servlet;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

import iobjects.*;
import iobjects.misc.*;
import iobjects.schedule.*;
import iobjects.util.*;
import iobjects.xml.ui.*;

/**
 * Representa o Servlet Controller da aplicação responável pelo redirecionamento
 * das requisições para os JSP´s e para a fachada dependendo da ação recebida.
 */
public class Controller extends HttpServlet {

  static private ActionList actionList        = new ActionList();
  static private String     workConfiguration = null;
  static private String     remoteURL         = "";
  static private boolean    goingToInitialize = false;
  static private Object     locker            = new Object();

  static public final String ACTION                 = "action";
  static public final String COMMAND                = "command";
  static public final String FACADE                 = "facade";
  static public final String DEFAULT_SITE_NAME      = "defaultsitename";
  static public final String HTTP_HEADER_USER_AGENT = "user-agent";
  static public final String LAST_COMMAND           = "lastCommand";
  static public final String LAST_EXCEPTION         = "lastException";
  static public final String PAGE                   = "page";
  static public final String RELOADED               = "reloaded";
  // *
  static public final String RECENT_EDIT_CAPTION = "RECENT_EDIT_CAPTION";
  static public final String RECENT_EDIT_TITLE   = "RECENT_EDIT_TITLE";
  // *
  static public final Action ACTION_BLANK                               = actionList.add(new Action("blank", "", "", "", "blank.jsp", "", "", Action.CATEGORY_NONE, false, false));
  static public final Action ACTION_CHANGE_PASSWORD                     = actionList.add(new Action("changePassword", "", "", "", "changepassword.jsp", "", "", Action.CATEGORY_NONE, false, false));
  static public final Action ACTION_CHANGE_STYLE                        = actionList.add(new Action("changeStyle", "", "", "", "changestyle.jsp", "", "", Action.CATEGORY_NONE, false, false));
  static public final Action ACTION_ERROR                               = actionList.add(new Action("error", "", "", "", "error.jsp", "", "", Action.CATEGORY_NONE, false, false));
  static public final Action ACTION_FLOW_CHART                          = actionList.add(new Action("flowChart", "", "", "", "flowchart.jsp", "", "", Action.CATEGORY_NONE, false, false));
  static public final Action ACTION_EXTERNAL_LOOKUP                     = actionList.add(new Action("externalLookup", "", "", "", "ui/entity/externallookup.jsp", "", "", Action.CATEGORY_NONE, false, false));
  static public final Action ACTION_HELP                                = actionList.add(new Action("help", "", "", "", "ui/help/help.jsp", "", "", Action.CATEGORY_NONE, false, false));
  static public final Action ACTION_HELP_FAQ                            = actionList.add(new Action("helpFAQ", "", "", "", "ui/help/helpfaq.jsp", "", "", Action.CATEGORY_NONE, false, false));
  static public final Action ACTION_HELP_BUSINESS_OBJECT                = actionList.add(new Action("helpBusinessObject", "", "", "", "ui/help/helpbusinessobject.jsp", "", "", Action.CATEGORY_NONE, false, false));
  static public final Action ACTION_HELP_TOP                            = actionList.add(new Action("helpTop", "", "", "", "ui/help/helptop.jsp", "", "", Action.CATEGORY_NONE, false, false));
  static public final Action ACTION_HELP_TREEVIEW                       = actionList.add(new Action("helpTreeview", "", "", "", "ui/help/helptreeview.jsp", "", "", Action.CATEGORY_NONE, false, false));
  static public final Action ACTION_HELP_WELCOME                        = actionList.add(new Action("helpWelcome", "", "", "", "ui/help/helpwelcome.jsp", "", "", Action.CATEGORY_NONE, false, false));
  static public final Action ACTION_HELP_WHATS_BETA                     = actionList.add(new Action("helpWhatsBeta", "", "", "", "ui/help/helpwhatsbeta.jsp", "", "", Action.CATEGORY_NONE, false, false));
  static public final Action ACTION_HELP_WHATS_NEW                      = actionList.add(new Action("helpWhatsNew", "", "", "", "ui/help/helpwhatsnew.jsp", "", "", Action.CATEGORY_NONE, false, false));
  static public final Action ACTION_HOME                                = actionList.add(new Action("home", "", "", "", "home.jsp", "", "", Action.CATEGORY_NONE, false, false));
  static public final Action ACTION_INITIALIZING                        = actionList.add(new Action("initializing", "", "", "", "initializing.jsp", "", "", Action.CATEGORY_NONE, false, false));
  static public final Action ACTION_LOGIN                               = actionList.add(new Action("login", "", "", "", "login.jsp", "", "", Action.CATEGORY_NONE, false, false));
  static public final Action ACTION_MY_DESKTOP                          = actionList.add(new Action("myDesktop", "", "", "", "mydesktop.jsp", "", "", Action.CATEGORY_NONE, false, false));
  static public final Action ACTION_MY_DESKTOP_CUSTOMIZE                = actionList.add(new Action("myDesktopCustomize", "", "", "", "mydesktopcustomize.jsp", "", "", Action.CATEGORY_NONE, false, false));
  static public final Action ACTION_PROCESS_WIZARD                      = actionList.add(new Action("processWizard", "", "", "", "ui/process/processwizard.jsp", "", "", Action.CATEGORY_NONE, false, false));
  static public final Action ACTION_POPUP_NOTICE_BOARD                  = actionList.add(new Action("popupNoticeBoard", "", "", "", "popupnoticeboard.jsp", "", "", Action.CATEGORY_NONE, false, false));
  static public final Action ACTION_RUN_SCHEDULED_TASK                  = actionList.add(new Action("runScheduledTask", "", "", "", "", "", "", Action.CATEGORY_NONE, false, false));
  static public final Action ACTION_SEND_MAIL                           = actionList.add(new Action("sendMail", "", "", "", "ui/util/mail/sendmail.jsp", "", "", Action.CATEGORY_NONE, false, false));
  static public final Action ACTION_SYSTEM_INFORMATION                  = actionList.add(new Action("systemInformation", "", "", "", "systeminformation.jsp", "", "", Action.CATEGORY_NONE, false, false));
  static public final Action ACTION_SYSTEM_INFORMATION_DATABASES        = actionList.add(new Action("systemInformationDatabases", "", "", "", "systeminformationdatabases.jsp", "", "", Action.CATEGORY_NONE, false, false));
  static public final Action ACTION_SYSTEM_INFORMATION_EXTENSIONS       = actionList.add(new Action("systemInformationExtensions", "", "", "", "systeminformationextensions.jsp", "", "", Action.CATEGORY_NONE, false, false));
  static public final Action ACTION_SYSTEM_INFORMATION_FILES            = actionList.add(new Action("systeminformationfiles", "", "", "", "systeminformationfiles.jsp", "", "", Action.CATEGORY_NONE, false, false));
  static public final Action ACTION_SYSTEM_INFORMATION_FILES_EDIT       = actionList.add(new Action("systeminformationfilesEdit", "", "", "", "systeminformationfilesedit.jsp", "", "", Action.CATEGORY_NONE, false, false));
  static public final Action ACTION_SYSTEM_INFORMATION_FILES_UPLOAD     = actionList.add(new Action("systemInformationFilesUpload", "", "", "", "systeminformationfilesupload.jsp", "", "", Action.CATEGORY_NONE, false, false));
  static public final Action ACTION_SYSTEM_INFORMATION_LOG              = actionList.add(new Action("systemInformationLog", "", "", "", "systeminformationlog.jsp", "", "", Action.CATEGORY_NONE, false, false));
  static public final Action ACTION_SYSTEM_INFORMATION_LOG_DETAIL       = actionList.add(new Action("systemInformationLogDetail", "", "", "", "systeminformationlogdetail.jsp", "", "", Action.CATEGORY_NONE, false, false));
  static public final Action ACTION_SYSTEM_INFORMATION_MAIN             = actionList.add(new Action("systemInformationMain", "", "", "", "systeminformationmain.jsp", "", "", Action.CATEGORY_NONE, false, false));
  static public final Action ACTION_SYSTEM_INFORMATION_SCHEDULE         = actionList.add(new Action("systemInformationSchedule", "", "", "", "systeminformationschedule.jsp", "", "", Action.CATEGORY_NONE, false, false));
  static public final Action ACTION_SYSTEM_INFORMATION_SESSIONS         = actionList.add(new Action("systemInformationSessions", "", "", "", "systeminformationsessions.jsp", "", "", Action.CATEGORY_NONE, false, false));
  static public final Action ACTION_SYSTEM_INFORMATION_UPDATER          = actionList.add(new Action("systemInformationUpdater", "", "", "", "systeminformationupdater.jsp", "", "", Action.CATEGORY_NONE, false, false));
  // *
  static public final Action ACTION_HOME_MOBILE                         = actionList.add(new Action("home", "", "", "", "mobile/home.jsp", "", "", Action.CATEGORY_NONE, true, false));
  static public final Action ACTION_LOGIN_MOBILE                        = actionList.add(new Action("login", "", "", "", "mobile/login.jsp", "", "", Action.CATEGORY_NONE, true, false));
  // *
  static public final Command COMMAND_EXTERNAL_LOOKUP_SEARCH = new Command(Command.COMMAND_SEARCH, "Pesquisar", "Pesquisa pelo valor informado.");
  // *
  static public final Command COMMAND_SEND_MAIL_EXECUTE = new Command(Command.COMMAND_EXECUTE, "Enviar", "Envia a mensagem.");
  // *
  static public final String COOKIE_USER_STYLE = "userStyle";
  // *
  static public final String BEAN_USER = "user";
  // *
  static public final String PARAM_EXCEPTION = "exception";
  // *
  static public final String PARAM_RUN_SCHEDULED_TASK_NAME = "name";
  // *
  static public final String PARAM_SEND_MAIL_SMTP_HOST     = "host";
  static public final String PARAM_SEND_MAIL_SMTP_PORT     = "port";
  static public final String PARAM_SEND_MAIL_SMTP_USERNAME = "username";
  static public final String PARAM_SEND_MAIL_SMTP_PASSWORD = "password";
  static public final String PARAM_SEND_MAIL_SMTP_SSL      = "ssl";
  static public final String PARAM_SEND_MAIL_FROM          = "from";
  static public final String PARAM_SEND_MAIL_REPLY_TO      = "replyTo";
  static public final String PARAM_SEND_MAIL_TO            = "to";
  static public final String PARAM_SEND_MAIL_CC            = "cc";
  static public final String PARAM_SEND_MAIL_BCC           = "bcc";
  static public final String PARAM_SEND_MAIL_SUBJECT       = "subject";
  static public final String PARAM_SEND_MAIL_URL           = "url";
  /**
   * Define o nome da configuração de trabalho que a instância da aplicação
   * irá utilizar. Utilize o parâmetro "workconfiguration=nome" para modificar
   * o diretório de trabalho que a aplicação irá utilizar.
   */
  static public final String PARAM_WORK_CONFIGURATION = "workconfiguration";
  // *
  static public final String STATUS_OK = "OK";

  /**
   * Adiciona a 'action' à lista de ações recentes executadas pelo usuário.
   * @param facade Facade Fachada.
   * @param request HttpServletRequest Request.
   * @param action Action Ação para ser adicionada à lista de ações recentes.
   * @param caption String Título da ação acessada.
   * @param title String Título do elemento acessado pela ação.
   */
  static public void addRecentAction(Facade             facade,
                                     HttpServletRequest request,
                                     Action             action,
                                     String             caption,
                                     String             title) {
    // se não temos os titulos...dispara
    if ((caption == null) || caption.equals("") ||
        (title == null) || title.equals(""))
      return;
    // adiciona na lista de ações recentes
    facade.recentActionList().add(new RecentAction(action,
                                                   HttpTools.rebuildQueryString(request),
                                                   caption,
                                                   title));
  }

  /**
   * Verifica o browser e sua versão.
   * @param facade Facade
   * @param request HttpServletRequest
   * @throws Exception Caso o browser e/ou sua versão não sejam suportados.
   */
  private void checkBrowser(Facade             facade,
                            HttpServletRequest request) throws Exception {
    // define se é um Browser Mobile
    facade.setBrowserMobile(isMobileRequest(request));
    // define se é um Browser Tablet
    facade.setBrowserTablet(isTabletRequest(request));
    // se chegou até aqui...verificação OK
    facade.setBrowserChecked(true);
  }

  /**
   * Verifica a configuração de trabalho utilizada para esta instância da aplicação.
   * @param request HttpServletRequest Request.
   * @param defaultValue String Nome da configuração default.
   */
  static public void checkWorkConfiguration(HttpServletRequest request,
                                            String             defaultValue) {
    // se já temos a configuração de trabalho...dispara
    if (workConfiguration != null)
      return;
    // nome do atributo para procurar na memória
    String attributeName = "iobjects.servlet.Controller." + PARAM_WORK_CONFIGURATION;
    // procura no contexto do container
    workConfiguration = (String)request.getSession().getServletContext().getAttribute(attributeName);
    // se não temos...tenta pegar da requisição
    if (workConfiguration == null)
      workConfiguration = request.getParameter(PARAM_WORK_CONFIGURATION);
    // se não temos nada...associa branco
    if (workConfiguration == null)
      workConfiguration = defaultValue;
    // guarda no contexto do container
    request.getSession().getServletContext().setAttribute(attributeName, workConfiguration);
  }

  /**
   * Processa as requisições Get recebidas.
   * @param request HttpServletRequest referente à requisição.
   * @param response HttpServletResponse referente à resposta.
   * @throws ServletException Em caso de exceção na tentativa de realizar uma
   *                          das operações de redirecionamento e/ou execução
   *                          de comandos.
   * @throws IOException Em caso de exceção na tentativa de realizar uma
   *                     das operações de redirecionamento e/ou execução
   *                     de comandos.
   */
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    doPost(request, response);
  }

  /**
   * Processa as requisições Post recebidas.
   * @param request HttpServletRequest referente à requisição.
   * @param response HttpServletResponse referente à resposta.
   * @throws ServletException Em caso de exceção na tentativa de realizar uma
   *                          das operações de redirecionamento e/ou execução
   *                          de comandos.
   * @throws IOException Em caso de exceção na tentativa de realizar uma
   *                     das operações de redirecionamento e/ou execução
   *                     de comandos.
   */
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    try {
      // guarda nossa URL remota
      if (remoteURL.equals("")) {
        remoteURL = request.getRequestURL().toString();
        remoteURL = remoteURL.substring(0, remoteURL.lastIndexOf('/'));
      } // if

      // nossa fachada
      Facade facade = null;

      // qual o status da aplicação?
      switch (Facade.getApplicationStatus()) {
        // -----------------
        // apenas carregados
        // -----------------
        case Facade.APPLICATION_STATUS_LOADED: {
          synchronized(locker) {
            // se a inicialização já foi providenciada...dispara
            if (goingToInitialize) {
              // encaminha para a mensagem de inicialização
              HttpTools.forward(ACTION_INITIALIZING.getJsp(),
                                getServletContext(),
                                request,
                                response);
              // dispara
              return;
            }
            // se a inicialização ainda não foi providenciada...
            else {
              // providenciaremos a inicialização
              goingToInitialize = true;
            } // if
          } // synchronized
          try {
            // verifica a configuração de trabalho para esta instância da aplicação
            checkWorkConfiguration(request, "");
            // obtém nossa fachada
            facade = getFacade(request);
          }
          finally {
            // a inicialização já foi providenciada
            goingToInitialize = false;
          } // try-finally
          // acabamos
          break;
        }
        // -------------
        // inicializando
        // -------------
        case Facade.APPLICATION_STATUS_INITIALIZING: {
          // encaminha para a mensagem de inicialização
          HttpTools.forward(ACTION_INITIALIZING.getJsp(),
                            getServletContext(),
                            request,
                            response);
          // dispara
          return;
        }
        // -----------
        // tudo pronto
        // -----------
        case Facade.APPLICATION_STATUS_READY: {
          // obtém nossa fachada
          facade = getFacade(request);
          // acabamos
          break;
        }
      } // switch

      // ação desejada
      String action = getParameter(request, ACTION);
      // se o browser ainda não foi verificado...
      if (!facade.getBrowserChecked())
        checkBrowser(facade, request);

      // se quer executar uma tarefa agendada...
      if (action.equals(ACTION_RUN_SCHEDULED_TASK.getName())) {
        runScheduledTask(facade, response, getParameter(request, PARAM_RUN_SCHEDULED_TASK_NAME));
      }
      // em outro caso...processa a requisição
      else {
        commons.Http.processRequest(request, response, getServletContext());
      } // if
    }
    // em caso de exceção...
    catch (Exception e) {
      // tenta redirecionar para a página de exceção
      forwardException(e, getServletContext(), request, response);
    } // try-catch
  }

  /**
   * Redireciona a requisição para a página de tratamento de exceção.
   * @param e Exception Exceção que será tratada.
   * @param context PageContext Contexto da página.
   * @throws ServletException Em caso de exceção na tentativa de localizar o
   *                          caminho especificado.
   * @throws IOException Em caso de exceção na tentativa de escrever para a saída
   *                     padrão.
   */
  static public void forwardException(Exception                     e,
                                      javax.servlet.jsp.PageContext context) throws ServletException, IOException {
    // obtém a fachada
    Facade facade = (Facade)context.getSession().getAttribute(FACADE);
    // grava a exceção no log do usuário
    String message            = "";
    String source             = "";
    String exceptionClassName = "";
    String host               = "";
    if (facade != null) {
      message = ExtendedException.extractMessage(e);
      source = ExtendedException.extractClassName(e);
      source += (!source.equals("") ? "." : "") + ExtendedException.extractMethodName(e);
      exceptionClassName = ExtendedException.extractExceptionClassName(e);
      host = context.getRequest().getRemoteAddr() + " (" + context.getRequest().getRemoteHost() + ")";
      facade.writeLogMessage("Exceção: " + message
                           + (!source.equals("") ? " - Origem: " + source : "")
                           + (!exceptionClassName.equals("") ? " - Tipo: " + exceptionClassName : "")
                           + " - Host: " + host);
    } // if
    // põe a exceção na sessão
    context.getSession().setAttribute(LAST_EXCEPTION, e);
    // se for uma requisição Ajax...avisa
    ((HttpServletResponse)context.getResponse()).addHeader(LAST_EXCEPTION, "Exceção: " + message
                                                                         + (!source.equals("") ? " - Origem: " + source : "")
                                                                         + (!exceptionClassName.equals("") ? " - Tipo: " + exceptionClassName : ""));
    // redireciona para a página de erro!
    HttpTools.forward(ACTION_ERROR.getJsp(), context);
  }

  /**
   * Redireciona a requisição para a página de tratamento de exceção.
   * @param e Exception Exceção que será tratada.
   * @param context ServletContext Contexto do Servlet.
   * @param request HttpServletRequest Requisição.
   * @param response HttpServletResponse Resposta.
   * @throws ServletException Em caso de exceção na tentativa de localizar o
   *                          caminho especificado.
   * @throws IOException Em caso de exceção na tentativa de escrever para a saída
   *                     padrão.
   */
  static private void forwardException(Exception           e,
                                       ServletContext      context,
                                       HttpServletRequest  request,
                                       HttpServletResponse response) throws ServletException, IOException {
    // obtém a fachada
    Facade facade = (Facade)request.getSession().getAttribute(FACADE);
    // se temos fachada...
    if (facade != null) {
      // grava a exceção no log do usuário
      String message = ExtendedException.extractMessage(e);
      String source = ExtendedException.extractClassName(e);
             source += (!source.equals("") ? "." : "") + ExtendedException.extractMethodName(e);
      String exceptionClassName = ExtendedException.extractExceptionClassName(e);
      String host = request.getRemoteAddr() + " (" + request.getRemoteHost() + ")";
      facade.writeLogMessage("Exceção: " + e.getClass().getName() + ": " + message
                           + (!source.equals("") ? " - Origem: " + source : "")
                           + (!exceptionClassName.equals("") ? " - Tipo: " + exceptionClassName : "")
                           + " - Host: " + host);
      // põe a exceção na sessão
      request.getSession().setAttribute(LAST_EXCEPTION, e);
      // ...redireciona para a página de erro!
      HttpTools.forward(ACTION_ERROR.getJsp(),
                        context,
                        request,
                        response);
    }
    // se não temos fachada...
    else {
      throw new ServletException(e);
    } // if
  }

  /**
   * Retorna a ação que está sendo executada em 'request'.
   * @param request HttpServletRequest Requisição atual.
   * @return String Retorna a ação que está sendo executada em 'request'.
   */
  static public String getAction(HttpServletRequest request) {
    // retorna
    return commons.Http.getAction(request);
  }

  /**
   * Retorna o ActionList contendo as ações registradas.
   * @return Retorna o ActionList contendo as ações registradas.
   */
  static public ActionList getActionList() {
    return actionList;
  }
  
  /**
   * Retorna o comando que está sendo executado em 'request'.
   * @param request HttpServletRequest Requisição atual.
   * @return String Retorna o comando que está sendo executado em 'request'.
   */
  static public String getCommand(HttpServletRequest request) {
    // retorna
    return commons.Http.getCommand(request);
  }

  /**
   * Verifica a existência do Facade na sessão e retorna sua referência. Caso
   * não exista uma instância será criada e colocada na sessão.
   * @param request HttpServletRequest Requisição.
   * @return Verifica a existência do Facade na sessão e retorna sua referência.
   *         Caso não exista uma instância será criada e colocada na sessão.
   * @throws Exception Em caso de exceção na tentativa de criar ou inicializar
   *         a fachada.
   */
  static public Facade getFacade(HttpServletRequest request) throws Exception {
    try {
      // obtém a Facade
      Facade result = commons.Http.getFacade(request, workConfiguration);
      // lê o último estilo do usuário gravado
      getLastUserStyle(request, result);
      // retorna
      return result;
    }
    catch (Exception e) {
      throw new ExtendedException("iobjects.servlet.Controller", "getFacade", e);
    } // try-catch
  }

  /**
   * Retorna o último comando executado em 'request'.
   * @param request HttpServletRequest Requisição atual.
   * @return String Retorna o último comando executado em 'request'.
   */
  static public String getLastCommand(HttpServletRequest request) {
    // retorna
    return commons.Http.getLastCommand(request);
  }

  /**
   * Retorna o valor do parâmetro indicado por 'name' em request.
   * @param request HttpServletRequest Request.
   * @param name Nome do parâmetro cujo valor se deseja retornar.
   * @return Retorna o valor do parâmetro indicado por 'name' em request.
   */
  static public String getParameter(HttpServletRequest request,
                                    String             name) {
    // retorna
    return commons.Http.getParameter(request, name);
  }

  /**
   * Retorna a URL remota através da qual a aplicação é acessível. Ex.:
   * 'http://localhost:8080/iobjects' .
   * @return String Retorna a URL remota através da qual a aplicação é acessível.
   *         Ex.: 'http://localhost:8080/iobjects'.
   */
  static public String getRemoteURL() {
    return remoteURL;
  }

  /**
   * Retorna o nome do agente do usuário.
   * @param request HttpServletRequest Requisição.
   * @return String Retorna o nome do agente do usuário.
   */
  static public String getUserAgentName(HttpServletRequest request) {
    // retorna
    return commons.Http.getUserAgentName(request);
  }

  /**
   * Retorna a versão do agente do usuário.
   * @param request HttpServletRequest Requisição.
   * @return double Retorna a versão do agente do usuário.
   */
  static public double getUserAgentVersion(HttpServletRequest request) {
    // retorna
    return commons.Http.getUserAgentVersion(request);
  }

  /**
   * Define o estilo do usuário atual pelo nome gravado no cookie.
   * @param request HttpServletRequest Requisição atual.
   * @param facade Facade Fachada do usuário atual.
   */
  static public void getLastUserStyle(HttpServletRequest request,
                                      Facade             facade) {
    // lê o cookie da última opção do usuário
    String cookieStyleName = iobjects.util.HttpTools.getCookieValue(request,
                                                                    COOKIE_USER_STYLE,
                                                                    "");
    // se temos um nome de estilo...procura no gerenciador de estilos
    if (!cookieStyleName.equals("")) {
      // estilo referente ao nome
      StyleFile style = facade.styleManager().getStyle(cookieStyleName);
      // se temos o estilo...define como sendo o atual do usuário
      if (style != null)
        facade.setStyle(style);
    } // if
  }

  /**
   * Retorna o nome do diretório de trabalho que está sendo utilizado pela aplicação.
   * @return String Retorna o nome do diretório de trabalho que está sendo
   *         utilizado pela aplicação.
   */
  static public String getWorkConfiguratoin() {
    return workConfiguration;
  }

  /**
   * Retorna true se o comando da requisição é exclusão.
   * @param request HttpServletRequest Requisição atual.
   * @return boolean Retorna true se o comando da requisição é exclusão.
   */
  static public boolean isDeleting(HttpServletRequest request) {
    // retorna
    return commons.Http.isDeleting(request);
  }

  /**
   * Retorna true se o comando da requisição é edição.
   * @param request HttpServletRequest Requisição atual.
   * @return boolean Retorna true se o comando da requisição é edição.
   */
  static public boolean isEditing(HttpServletRequest request) {
    // retorna
    return commons.Http.isEditing(request);
  }

  /**
   * Retorna true se o comando da requisição é execução.
   * @param request HttpServletRequest Requisição atual.
   * @return boolean Retorna true se o comando da requisição é execução.
   */
  static public boolean isExecuting(HttpServletRequest request) {
    // retorna
    return commons.Http.isExecuting(request);
  }

  /**
   * Retorna true se o comando da requisição é avançar.
   * @param request HttpServletRequest Requisição atual.
   * @return boolean Retorna true se o comando da requisição é avançar.
   */
  static public boolean isGoingNext(HttpServletRequest request) {
    // retorna
    return commons.Http.isGoingNext(request);
  }

  /**
   * Retorna true se o comando da requisição é retroceder.
   * @param request HttpServletRequest Requisição atual.
   * @return boolean Retorna true se o comando da requisição é retroceder.
   */
  static public boolean isGoingPrevious(HttpServletRequest request) {
    // retorna
    return commons.Http.isGoingPrevious(request);
  }

  /**
   * Retorna true se o comando da requisição é inclusão.
   * @param request HttpServletRequest Requisição atual.
   * @return boolean Retorna true se o comando da requisição é inclusão.
   */
  static public boolean isInserting(HttpServletRequest request) {
    // retorna
    return commons.Http.isInserting(request);
  }

  /**
   * Retorna true se o comando da requisição é criar relatório.
   * @param request HttpServletRequest Requisição atual.
   * @return boolean Retorna true se o comando da requisição é criar relatório.
   */
  static public boolean isMakingReport(HttpServletRequest request) {
    // retorna
    return commons.Http.isMakingReport(request);
  }

  /**
   * Retorna true se o comando da requisição é reiniciar.
   * @param request HttpServletRequest Requisição atual.
   * @return boolean Retorna true se o comando da requisição é reiniciar.
   */
  static public boolean isRestarting(HttpServletRequest request) {
    // retorna
    return commons.Http.isRestarting(request);
  }

  /**
   * Retorna true se o comando da requisição é salvar.
   * @param request HttpServletRequest Requisição atual.
   * @return boolean Retorna true se o comando da requisição é salvar.
   */
  static public boolean isSaving(HttpServletRequest request) {
    // retorna
    return commons.Http.isSaving(request);
  }

  /**
   * Retorna true se o comando da requisição é salvar e o último comando
   * executado foi inserir.
   * @param request HttpServletRequest Requisição atual.
   * @return boolean Retorna true se o comando da requisição é salvar e o
   *         último comando executado foi inserir.
   */
  static public boolean isSavingNew(HttpServletRequest request) {
    // retorna
    return commons.Http.isSavingNew(request);
  }

  /**
   * Retorna true se o comando da requisição é localizar.
   * @param request HttpServletRequest Requisição da página atual.
   * @return boolean Retorna true se o comando da requisição é localizar.
   */
  static public boolean isSearching(HttpServletRequest request) {
    // retorna
    return commons.Http.isSearching(request);
  }

  /**
   * Retorna true se a requisição informada for de um dispositivo Mobile.
   * @param request HttpServletRequest Requisição.
   * @return boolean Retorna true se a requisição informada for de um dispositivo
   *                 Mobile.
   */
  static public boolean isMobileRequest(HttpServletRequest request) {
    // retorna true se é um browser de um dispositivo móvel
    return commons.Http.isMobileRequest(request);
  }

  /**
   * Retorna true se a requisição informada for de um dispositivo Tablet.
   * @param request HttpServletRequest Requisição.
   * @return boolean Retorna true se a requisição informada for de um dispositivo
   *                 Tablet.
   */
  static public boolean isTabletRequest(HttpServletRequest request) {
    // retorna true se é um browser de um dispositivo móvel
    return commons.Http.isTabletRequest(request);
  }

  /**
   * Executa a tarefa agendada 'taskName' e retorna uma mensagem padrão.
   * @param facade Facade Fachada.
   * @param response HttpServletResponse Response.
   * @param taskName String Nome da tarefa agendada.
   * @throws Exception Em caso de exceção na tentativa de escrever na saida
   *                   da resposta.
   * @since 2006
   */
  private void runScheduledTask(Facade              facade,
                                HttpServletResponse response,
                                String              taskName) throws Exception {
    // retorna a resposta padrão antes que a agenda seja executada
    // para evitar que a conexão seja refeita e a agenda seja executada
    // em duplicidade
    response.setContentType("text/plain;");
    response.getWriter().println("A tarefa agendada " + taskName + " foi executada. Os detalhes da execução foram gravados no log do sistema.");
    response.setStatus(response.SC_ACCEPTED);
    response.flushBuffer();
    // executa a tarefa agendada
    facade.schedule().runScheduledTask(facade, taskName);
  }

  /**
   * Define o estilo informado como sendo o último estilo do usuário atual de
   * 'facade' e salva um cookie para que o estilo seja selecionado na próxima
   * sessão.
   * @param response HttpServletResponse Resposta da requisição atual.
   * @param facade Facade Fachada do usuário atual.
   * @param style StyleFile Estilo selecionado.
   */
  static public void setLastUserStyle(HttpServletResponse response,
                                      Facade              facade,
                                      StyleFile           style) {
    // define o estilo na fachada
    facade.setStyle(style);
    // salva a opção do usuário em um Cookie
    iobjects.util.HttpTools.setCookieValue(response,
                                           COOKIE_USER_STYLE,
                                           style.css().getName(),
                                           false);
  }

  /**
   * Retorna o nome da configuração de trabalho utilizada para esta instância
   * da aplicação.
   * @return String Retorna o nome da configuração de trabalho utilizada para
   *         esta instância da aplicação.
   */
  static public String workConfiguration() {
    return workConfiguration;
  }

}
