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
 * Representa o Servlet Controller da aplica��o respon�vel pelo redirecionamento
 * das requisi��es para os JSP�s e para a fachada dependendo da a��o recebida.
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
   * Define o nome da configura��o de trabalho que a inst�ncia da aplica��o
   * ir� utilizar. Utilize o par�metro "workconfiguration=nome" para modificar
   * o diret�rio de trabalho que a aplica��o ir� utilizar.
   */
  static public final String PARAM_WORK_CONFIGURATION = "workconfiguration";
  // *
  static public final String STATUS_OK = "OK";

  /**
   * Adiciona a 'action' � lista de a��es recentes executadas pelo usu�rio.
   * @param facade Facade Fachada.
   * @param request HttpServletRequest Request.
   * @param action Action A��o para ser adicionada � lista de a��es recentes.
   * @param caption String T�tulo da a��o acessada.
   * @param title String T�tulo do elemento acessado pela a��o.
   */
  static public void addRecentAction(Facade             facade,
                                     HttpServletRequest request,
                                     Action             action,
                                     String             caption,
                                     String             title) {
    // se n�o temos os titulos...dispara
    if ((caption == null) || caption.equals("") ||
        (title == null) || title.equals(""))
      return;
    // adiciona na lista de a��es recentes
    facade.recentActionList().add(new RecentAction(action,
                                                   HttpTools.rebuildQueryString(request),
                                                   caption,
                                                   title));
  }

  /**
   * Verifica o browser e sua vers�o.
   * @param facade Facade
   * @param request HttpServletRequest
   * @throws Exception Caso o browser e/ou sua vers�o n�o sejam suportados.
   */
  private void checkBrowser(Facade             facade,
                            HttpServletRequest request) throws Exception {
    // define se � um Browser Mobile
    facade.setBrowserMobile(isMobileRequest(request));
    // define se � um Browser Tablet
    facade.setBrowserTablet(isTabletRequest(request));
    // se chegou at� aqui...verifica��o OK
    facade.setBrowserChecked(true);
  }

  /**
   * Verifica a configura��o de trabalho utilizada para esta inst�ncia da aplica��o.
   * @param request HttpServletRequest Request.
   * @param defaultValue String Nome da configura��o default.
   */
  static public void checkWorkConfiguration(HttpServletRequest request,
                                            String             defaultValue) {
    // se j� temos a configura��o de trabalho...dispara
    if (workConfiguration != null)
      return;
    // nome do atributo para procurar na mem�ria
    String attributeName = "iobjects.servlet.Controller." + PARAM_WORK_CONFIGURATION;
    // procura no contexto do container
    workConfiguration = (String)request.getSession().getServletContext().getAttribute(attributeName);
    // se n�o temos...tenta pegar da requisi��o
    if (workConfiguration == null)
      workConfiguration = request.getParameter(PARAM_WORK_CONFIGURATION);
    // se n�o temos nada...associa branco
    if (workConfiguration == null)
      workConfiguration = defaultValue;
    // guarda no contexto do container
    request.getSession().getServletContext().setAttribute(attributeName, workConfiguration);
  }

  /**
   * Processa as requisi��es Get recebidas.
   * @param request HttpServletRequest referente � requisi��o.
   * @param response HttpServletResponse referente � resposta.
   * @throws ServletException Em caso de exce��o na tentativa de realizar uma
   *                          das opera��es de redirecionamento e/ou execu��o
   *                          de comandos.
   * @throws IOException Em caso de exce��o na tentativa de realizar uma
   *                     das opera��es de redirecionamento e/ou execu��o
   *                     de comandos.
   */
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    doPost(request, response);
  }

  /**
   * Processa as requisi��es Post recebidas.
   * @param request HttpServletRequest referente � requisi��o.
   * @param response HttpServletResponse referente � resposta.
   * @throws ServletException Em caso de exce��o na tentativa de realizar uma
   *                          das opera��es de redirecionamento e/ou execu��o
   *                          de comandos.
   * @throws IOException Em caso de exce��o na tentativa de realizar uma
   *                     das opera��es de redirecionamento e/ou execu��o
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

      // qual o status da aplica��o?
      switch (Facade.getApplicationStatus()) {
        // -----------------
        // apenas carregados
        // -----------------
        case Facade.APPLICATION_STATUS_LOADED: {
          synchronized(locker) {
            // se a inicializa��o j� foi providenciada...dispara
            if (goingToInitialize) {
              // encaminha para a mensagem de inicializa��o
              HttpTools.forward(ACTION_INITIALIZING.getJsp(),
                                getServletContext(),
                                request,
                                response);
              // dispara
              return;
            }
            // se a inicializa��o ainda n�o foi providenciada...
            else {
              // providenciaremos a inicializa��o
              goingToInitialize = true;
            } // if
          } // synchronized
          try {
            // verifica a configura��o de trabalho para esta inst�ncia da aplica��o
            checkWorkConfiguration(request, "");
            // obt�m nossa fachada
            facade = getFacade(request);
          }
          finally {
            // a inicializa��o j� foi providenciada
            goingToInitialize = false;
          } // try-finally
          // acabamos
          break;
        }
        // -------------
        // inicializando
        // -------------
        case Facade.APPLICATION_STATUS_INITIALIZING: {
          // encaminha para a mensagem de inicializa��o
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
          // obt�m nossa fachada
          facade = getFacade(request);
          // acabamos
          break;
        }
      } // switch

      // a��o desejada
      String action = getParameter(request, ACTION);
      // se o browser ainda n�o foi verificado...
      if (!facade.getBrowserChecked())
        checkBrowser(facade, request);

      // se quer executar uma tarefa agendada...
      if (action.equals(ACTION_RUN_SCHEDULED_TASK.getName())) {
        runScheduledTask(facade, response, getParameter(request, PARAM_RUN_SCHEDULED_TASK_NAME));
      }
      // em outro caso...processa a requisi��o
      else {
        commons.Http.processRequest(request, response, getServletContext());
      } // if
    }
    // em caso de exce��o...
    catch (Exception e) {
      // tenta redirecionar para a p�gina de exce��o
      forwardException(e, getServletContext(), request, response);
    } // try-catch
  }

  /**
   * Redireciona a requisi��o para a p�gina de tratamento de exce��o.
   * @param e Exception Exce��o que ser� tratada.
   * @param context PageContext Contexto da p�gina.
   * @throws ServletException Em caso de exce��o na tentativa de localizar o
   *                          caminho especificado.
   * @throws IOException Em caso de exce��o na tentativa de escrever para a sa�da
   *                     padr�o.
   */
  static public void forwardException(Exception                     e,
                                      javax.servlet.jsp.PageContext context) throws ServletException, IOException {
    // obt�m a fachada
    Facade facade = (Facade)context.getSession().getAttribute(FACADE);
    // grava a exce��o no log do usu�rio
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
      facade.writeLogMessage("Exce��o: " + message
                           + (!source.equals("") ? " - Origem: " + source : "")
                           + (!exceptionClassName.equals("") ? " - Tipo: " + exceptionClassName : "")
                           + " - Host: " + host);
    } // if
    // p�e a exce��o na sess�o
    context.getSession().setAttribute(LAST_EXCEPTION, e);
    // se for uma requisi��o Ajax...avisa
    ((HttpServletResponse)context.getResponse()).addHeader(LAST_EXCEPTION, "Exce��o: " + message
                                                                         + (!source.equals("") ? " - Origem: " + source : "")
                                                                         + (!exceptionClassName.equals("") ? " - Tipo: " + exceptionClassName : ""));
    // redireciona para a p�gina de erro!
    HttpTools.forward(ACTION_ERROR.getJsp(), context);
  }

  /**
   * Redireciona a requisi��o para a p�gina de tratamento de exce��o.
   * @param e Exception Exce��o que ser� tratada.
   * @param context ServletContext Contexto do Servlet.
   * @param request HttpServletRequest Requisi��o.
   * @param response HttpServletResponse Resposta.
   * @throws ServletException Em caso de exce��o na tentativa de localizar o
   *                          caminho especificado.
   * @throws IOException Em caso de exce��o na tentativa de escrever para a sa�da
   *                     padr�o.
   */
  static private void forwardException(Exception           e,
                                       ServletContext      context,
                                       HttpServletRequest  request,
                                       HttpServletResponse response) throws ServletException, IOException {
    // obt�m a fachada
    Facade facade = (Facade)request.getSession().getAttribute(FACADE);
    // se temos fachada...
    if (facade != null) {
      // grava a exce��o no log do usu�rio
      String message = ExtendedException.extractMessage(e);
      String source = ExtendedException.extractClassName(e);
             source += (!source.equals("") ? "." : "") + ExtendedException.extractMethodName(e);
      String exceptionClassName = ExtendedException.extractExceptionClassName(e);
      String host = request.getRemoteAddr() + " (" + request.getRemoteHost() + ")";
      facade.writeLogMessage("Exce��o: " + e.getClass().getName() + ": " + message
                           + (!source.equals("") ? " - Origem: " + source : "")
                           + (!exceptionClassName.equals("") ? " - Tipo: " + exceptionClassName : "")
                           + " - Host: " + host);
      // p�e a exce��o na sess�o
      request.getSession().setAttribute(LAST_EXCEPTION, e);
      // ...redireciona para a p�gina de erro!
      HttpTools.forward(ACTION_ERROR.getJsp(),
                        context,
                        request,
                        response);
    }
    // se n�o temos fachada...
    else {
      throw new ServletException(e);
    } // if
  }

  /**
   * Retorna a a��o que est� sendo executada em 'request'.
   * @param request HttpServletRequest Requisi��o atual.
   * @return String Retorna a a��o que est� sendo executada em 'request'.
   */
  static public String getAction(HttpServletRequest request) {
    // retorna
    return commons.Http.getAction(request);
  }

  /**
   * Retorna o ActionList contendo as a��es registradas.
   * @return Retorna o ActionList contendo as a��es registradas.
   */
  static public ActionList getActionList() {
    return actionList;
  }
  
  /**
   * Retorna o comando que est� sendo executado em 'request'.
   * @param request HttpServletRequest Requisi��o atual.
   * @return String Retorna o comando que est� sendo executado em 'request'.
   */
  static public String getCommand(HttpServletRequest request) {
    // retorna
    return commons.Http.getCommand(request);
  }

  /**
   * Verifica a exist�ncia do Facade na sess�o e retorna sua refer�ncia. Caso
   * n�o exista uma inst�ncia ser� criada e colocada na sess�o.
   * @param request HttpServletRequest Requisi��o.
   * @return Verifica a exist�ncia do Facade na sess�o e retorna sua refer�ncia.
   *         Caso n�o exista uma inst�ncia ser� criada e colocada na sess�o.
   * @throws Exception Em caso de exce��o na tentativa de criar ou inicializar
   *         a fachada.
   */
  static public Facade getFacade(HttpServletRequest request) throws Exception {
    try {
      // obt�m a Facade
      Facade result = commons.Http.getFacade(request, workConfiguration);
      // l� o �ltimo estilo do usu�rio gravado
      getLastUserStyle(request, result);
      // retorna
      return result;
    }
    catch (Exception e) {
      throw new ExtendedException("iobjects.servlet.Controller", "getFacade", e);
    } // try-catch
  }

  /**
   * Retorna o �ltimo comando executado em 'request'.
   * @param request HttpServletRequest Requisi��o atual.
   * @return String Retorna o �ltimo comando executado em 'request'.
   */
  static public String getLastCommand(HttpServletRequest request) {
    // retorna
    return commons.Http.getLastCommand(request);
  }

  /**
   * Retorna o valor do par�metro indicado por 'name' em request.
   * @param request HttpServletRequest Request.
   * @param name Nome do par�metro cujo valor se deseja retornar.
   * @return Retorna o valor do par�metro indicado por 'name' em request.
   */
  static public String getParameter(HttpServletRequest request,
                                    String             name) {
    // retorna
    return commons.Http.getParameter(request, name);
  }

  /**
   * Retorna a URL remota atrav�s da qual a aplica��o � acess�vel. Ex.:
   * 'http://localhost:8080/iobjects' .
   * @return String Retorna a URL remota atrav�s da qual a aplica��o � acess�vel.
   *         Ex.: 'http://localhost:8080/iobjects'.
   */
  static public String getRemoteURL() {
    return remoteURL;
  }

  /**
   * Retorna o nome do agente do usu�rio.
   * @param request HttpServletRequest Requisi��o.
   * @return String Retorna o nome do agente do usu�rio.
   */
  static public String getUserAgentName(HttpServletRequest request) {
    // retorna
    return commons.Http.getUserAgentName(request);
  }

  /**
   * Retorna a vers�o do agente do usu�rio.
   * @param request HttpServletRequest Requisi��o.
   * @return double Retorna a vers�o do agente do usu�rio.
   */
  static public double getUserAgentVersion(HttpServletRequest request) {
    // retorna
    return commons.Http.getUserAgentVersion(request);
  }

  /**
   * Define o estilo do usu�rio atual pelo nome gravado no cookie.
   * @param request HttpServletRequest Requisi��o atual.
   * @param facade Facade Fachada do usu�rio atual.
   */
  static public void getLastUserStyle(HttpServletRequest request,
                                      Facade             facade) {
    // l� o cookie da �ltima op��o do usu�rio
    String cookieStyleName = iobjects.util.HttpTools.getCookieValue(request,
                                                                    COOKIE_USER_STYLE,
                                                                    "");
    // se temos um nome de estilo...procura no gerenciador de estilos
    if (!cookieStyleName.equals("")) {
      // estilo referente ao nome
      StyleFile style = facade.styleManager().getStyle(cookieStyleName);
      // se temos o estilo...define como sendo o atual do usu�rio
      if (style != null)
        facade.setStyle(style);
    } // if
  }

  /**
   * Retorna o nome do diret�rio de trabalho que est� sendo utilizado pela aplica��o.
   * @return String Retorna o nome do diret�rio de trabalho que est� sendo
   *         utilizado pela aplica��o.
   */
  static public String getWorkConfiguratoin() {
    return workConfiguration;
  }

  /**
   * Retorna true se o comando da requisi��o � exclus�o.
   * @param request HttpServletRequest Requisi��o atual.
   * @return boolean Retorna true se o comando da requisi��o � exclus�o.
   */
  static public boolean isDeleting(HttpServletRequest request) {
    // retorna
    return commons.Http.isDeleting(request);
  }

  /**
   * Retorna true se o comando da requisi��o � edi��o.
   * @param request HttpServletRequest Requisi��o atual.
   * @return boolean Retorna true se o comando da requisi��o � edi��o.
   */
  static public boolean isEditing(HttpServletRequest request) {
    // retorna
    return commons.Http.isEditing(request);
  }

  /**
   * Retorna true se o comando da requisi��o � execu��o.
   * @param request HttpServletRequest Requisi��o atual.
   * @return boolean Retorna true se o comando da requisi��o � execu��o.
   */
  static public boolean isExecuting(HttpServletRequest request) {
    // retorna
    return commons.Http.isExecuting(request);
  }

  /**
   * Retorna true se o comando da requisi��o � avan�ar.
   * @param request HttpServletRequest Requisi��o atual.
   * @return boolean Retorna true se o comando da requisi��o � avan�ar.
   */
  static public boolean isGoingNext(HttpServletRequest request) {
    // retorna
    return commons.Http.isGoingNext(request);
  }

  /**
   * Retorna true se o comando da requisi��o � retroceder.
   * @param request HttpServletRequest Requisi��o atual.
   * @return boolean Retorna true se o comando da requisi��o � retroceder.
   */
  static public boolean isGoingPrevious(HttpServletRequest request) {
    // retorna
    return commons.Http.isGoingPrevious(request);
  }

  /**
   * Retorna true se o comando da requisi��o � inclus�o.
   * @param request HttpServletRequest Requisi��o atual.
   * @return boolean Retorna true se o comando da requisi��o � inclus�o.
   */
  static public boolean isInserting(HttpServletRequest request) {
    // retorna
    return commons.Http.isInserting(request);
  }

  /**
   * Retorna true se o comando da requisi��o � criar relat�rio.
   * @param request HttpServletRequest Requisi��o atual.
   * @return boolean Retorna true se o comando da requisi��o � criar relat�rio.
   */
  static public boolean isMakingReport(HttpServletRequest request) {
    // retorna
    return commons.Http.isMakingReport(request);
  }

  /**
   * Retorna true se o comando da requisi��o � reiniciar.
   * @param request HttpServletRequest Requisi��o atual.
   * @return boolean Retorna true se o comando da requisi��o � reiniciar.
   */
  static public boolean isRestarting(HttpServletRequest request) {
    // retorna
    return commons.Http.isRestarting(request);
  }

  /**
   * Retorna true se o comando da requisi��o � salvar.
   * @param request HttpServletRequest Requisi��o atual.
   * @return boolean Retorna true se o comando da requisi��o � salvar.
   */
  static public boolean isSaving(HttpServletRequest request) {
    // retorna
    return commons.Http.isSaving(request);
  }

  /**
   * Retorna true se o comando da requisi��o � salvar e o �ltimo comando
   * executado foi inserir.
   * @param request HttpServletRequest Requisi��o atual.
   * @return boolean Retorna true se o comando da requisi��o � salvar e o
   *         �ltimo comando executado foi inserir.
   */
  static public boolean isSavingNew(HttpServletRequest request) {
    // retorna
    return commons.Http.isSavingNew(request);
  }

  /**
   * Retorna true se o comando da requisi��o � localizar.
   * @param request HttpServletRequest Requisi��o da p�gina atual.
   * @return boolean Retorna true se o comando da requisi��o � localizar.
   */
  static public boolean isSearching(HttpServletRequest request) {
    // retorna
    return commons.Http.isSearching(request);
  }

  /**
   * Retorna true se a requisi��o informada for de um dispositivo Mobile.
   * @param request HttpServletRequest Requisi��o.
   * @return boolean Retorna true se a requisi��o informada for de um dispositivo
   *                 Mobile.
   */
  static public boolean isMobileRequest(HttpServletRequest request) {
    // retorna true se � um browser de um dispositivo m�vel
    return commons.Http.isMobileRequest(request);
  }

  /**
   * Retorna true se a requisi��o informada for de um dispositivo Tablet.
   * @param request HttpServletRequest Requisi��o.
   * @return boolean Retorna true se a requisi��o informada for de um dispositivo
   *                 Tablet.
   */
  static public boolean isTabletRequest(HttpServletRequest request) {
    // retorna true se � um browser de um dispositivo m�vel
    return commons.Http.isTabletRequest(request);
  }

  /**
   * Executa a tarefa agendada 'taskName' e retorna uma mensagem padr�o.
   * @param facade Facade Fachada.
   * @param response HttpServletResponse Response.
   * @param taskName String Nome da tarefa agendada.
   * @throws Exception Em caso de exce��o na tentativa de escrever na saida
   *                   da resposta.
   * @since 2006
   */
  private void runScheduledTask(Facade              facade,
                                HttpServletResponse response,
                                String              taskName) throws Exception {
    // retorna a resposta padr�o antes que a agenda seja executada
    // para evitar que a conex�o seja refeita e a agenda seja executada
    // em duplicidade
    response.setContentType("text/plain;");
    response.getWriter().println("A tarefa agendada " + taskName + " foi executada. Os detalhes da execu��o foram gravados no log do sistema.");
    response.setStatus(response.SC_ACCEPTED);
    response.flushBuffer();
    // executa a tarefa agendada
    facade.schedule().runScheduledTask(facade, taskName);
  }

  /**
   * Define o estilo informado como sendo o �ltimo estilo do usu�rio atual de
   * 'facade' e salva um cookie para que o estilo seja selecionado na pr�xima
   * sess�o.
   * @param response HttpServletResponse Resposta da requisi��o atual.
   * @param facade Facade Fachada do usu�rio atual.
   * @param style StyleFile Estilo selecionado.
   */
  static public void setLastUserStyle(HttpServletResponse response,
                                      Facade              facade,
                                      StyleFile           style) {
    // define o estilo na fachada
    facade.setStyle(style);
    // salva a op��o do usu�rio em um Cookie
    iobjects.util.HttpTools.setCookieValue(response,
                                           COOKIE_USER_STYLE,
                                           style.css().getName(),
                                           false);
  }

  /**
   * Retorna o nome da configura��o de trabalho utilizada para esta inst�ncia
   * da aplica��o.
   * @return String Retorna o nome da configura��o de trabalho utilizada para
   *         esta inst�ncia da aplica��o.
   */
  static public String workConfiguration() {
    return workConfiguration;
  }

}
