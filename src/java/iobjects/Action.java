package iobjects;

import iobjects.servlet.*;
import iobjects.ui.*;
import iobjects.util.*;
import iobjects.help.*;

/**
 * Representa uma a��o na aplica��o realizada por um BusinessObject.
 * @since 2006
 */
public class Action {

  static public final int SHOW_TYPE_EMBEDED           = 0;
  static public final int SHOW_TYPE_POPUP_DEFAULT     = 1;
  static public final int SHOW_TYPE_POPUP_EXTENDED    = 2;
  static public final int SHOW_TYPE_POPUP_FULL_SCREEN = 3;

  private String      accessPath         = "";
  private boolean     beta               = false;
  private String      caption            = "";
  private int         category           = 0;
  private boolean     centralPoint       = false;
  private int         centralPointIndex  = 0;
  private CommandList commandList        = null;
  private String      declaringClassName = "";
  private String      description        = "";
  private String      help               = "";
  private String      jsp                = "";
  private boolean     justReleased       = false;
  private String      name               = "";
  private String      module             = "";
  private ActionList  nestedActionList   = new ActionList();
  private Action      parentAction       = null;
  private int         showType           = SHOW_TYPE_EMBEDED;
  private boolean     visible            = false;
  private boolean     mobile             = false;

  static public final int CATEGORY_NONE    = 0;
  static public final int CATEGORY_CARD    = 1;
  static public final int CATEGORY_ENTITY  = 2;
  static public final int CATEGORY_PROCESS = 3;
  static public final int CATEGORY_REPORT  = 4;

  static private final String DESCONHECIDO = "Desconhecido";
  static private final String OUTROS       = "Outros";

  private String[] categories = {"Outros",
                                 "Cart�es",
                                 "Cadastros",
                                 "Processos",
                                 "Relat�rios"};

  /**
   * Construtor padr�o.
   * @param name String Nome do Action.
   * @param caption String T�tulo do Action.
   * @param description String Descri��o detalhada do Action.
   * @param help String Ajuda extra do Action.
   * @param jsp String Arquivo JSP do Action.
   * @param module String M�dulo do Action.
   * @param accessPath String Caminho na �rvore do Action.
   * @param category int Categoria do Action.
   * @param mobile boolean True para o caso de uma a��o ser acess�vel por um dispositivo m�vel.
   * @param visible boolean True se a a��o for acessivel atrav�s da �rvore de objetos.
   */
  public Action(String  name,
                String  caption,
                String  description,
                String  help,
                String  jsp,
                String  module,
                String  accessPath,
                int     category,
                boolean mobile,
                boolean visible) {
    this.name = name;
    this.caption = caption;
    this.description = description;
    this.help = help;
    this.jsp = jsp;
    this.module = module;
    this.accessPath = accessPath;
    this.category = category;
    this.mobile = mobile;
    this.visible = visible;
    this.help = help;
    // nossa lista de comandos
    commandList = new CommandList(this);
    // define o tipo de exibi��o de acordo com a categoria
    if (category == CATEGORY_PROCESS)
      this.showType = SHOW_TYPE_POPUP_DEFAULT;
    else
      this.showType = SHOW_TYPE_EMBEDED;
  }

  /**
   * Adiciona 'action' a NestedActionList oferencendo uma forma c�moda de criar
   * uma nova a��o ao tempo em que a adiciona a lista de a��es aninhadas.
   * @param action Action para ser adicionada a NestedActionList.
   * @return Action Adiciona 'action' a NestedActionList oferencendo uma forma c�moda
   *                de criar uma nova a��o ao tempo em que a adiciona a lista
   *                de a��es aninhadas.
   */
  public Action addNestedAction(Action action) {
    // adiciona a nossa lista
    nestedActionList.add(action);
    // associa-nos como seu pai
    action.setParentAction(this);
    // retorna-o
    return action;
  }

  /**
   * Adiciona 'command' a CommandList oferencendo uma forma c�moda de criar
   * um novo comando ao tempo em que o adiciona a a��o.
   * @param command Command para ser adicionado a CommandList.
   * @return Command Adiciona 'command' a CommandList oferencendo uma forma
   *                 c�moda de criar um novo comando ao tempo em que adiciona-o
   *                 a a��o.
   */
  public Command addCommand(Command command) {
    // adiciona a nossa lista
    commandList.add(command);
    // retorna-o
    return command;
  }

  /**
   * Retorna a lista de comandos associados a a��o.
   * @return CommandList Retorna a lista de comandos associados a a��o.
   */
  public CommandList commandList() {
    return commandList;
  }

  /**
   * Libera recursos.
   */
  public void finalize() {
    accessPath         = null;
    caption            = null;
    commandList        = null;
    declaringClassName = null;
    description        = null;
    help               = null;
    jsp                = null;
    name               = null;
    module             = null;
    nestedActionList   = null;
    parentAction       = null;
  }

  /**
   * Retorna o caminho de exibi��o do Action no TreeView.
   * @return String Retorna o caminho de exibi��o do Action no TreeView.
   */
  public String getTreeViewPath() {
    // se somos aninhados...
    if (parentAction != null)
      return parentAction.getTreeViewPath() + "/" + caption;
    // se n�o somos aninhados
    else
      return (!module.equals("") ? module : OUTROS)
           + "/" + categories[category]
           + "/" + (!accessPath.equals("") ? accessPath + "/" : "")
           + (!caption.equals("") ? caption : DESCONHECIDO);
  }

  /**
   * Retorna o caminho de acesso na �rvore de a��es.
   * @return String Retorna o caminho de acesso na �rvore de a��es.
   */
  public String getAccessPath() {
    return accessPath;
  }

  /**
   * Retorna true se o Action est� definido como Beta nas notas da vers�o mais
   * recente da aplica��o.
   * @return boolean Retorna true se o Action est� definido como Beta nas notas
   *                 da vers�o mais recente da aplica��o.
   */
  public boolean getBeta() {
    return beta;
  }

  /**
   * Retorna o titulo do Action.
   * @return String Retorna o titulo do Action.
   */
  public String getCaption() {
    return caption;
  }

  /**
   * Retorna a categoria do Action.
   * @return int Retorna a categoria do Action.
   */
  public int getCategory() {
    return category;
  }

  /**
   * Retorna true se o Action representa um ponto central do m�dulo a que pertence.
   * @return boolean Retorna true se o Action representa um ponto central do
   *                 m�dulo a que pertence.
   */
  public boolean getCentralPoint() {
    return centralPoint;
  }

  /**
   * Retorna o �ndice de ordena��o se o Action representa um ponto central do
   * m�dulo a que pertence.
   * @return int Retorna o �ndice de ordena��o se o Action representa um ponto
   *             central do m�dulo a que pertence.
   */
  public int getCentralPointIndex() {
    return centralPointIndex;
  }

  /**
   * Retorna o nome da classe na qual o Action foi originalmente declarado como
   * membro.
   * @return String Retorna o nome da classe na qual o Action foi originalmente
   *                declarado como membro.
   */
  public String getDeclaringClassName() {
    return declaringClassName;
  }

  /**
   * Retorna a descri��o detalhada do Action.
   * @return String Retorna a descri��o detalhada do Action.
   */
  public String getDescription() {
    return description;
  }

  /**
   * Retorna a ajuda extra do Action.
   * @return String Retorna a ajuda extra do Action.
   */
  public String getHelp() {
    return help;
  }

  /**
   * Retorna o arquivo JSP do Action.
   * @return String Retorna o arquivo JSP do Action.
   */
  public String getJsp() {
    return jsp;
  }

  /**
   * Retorna true se o Action est� contido nas notas da vers�o mais recente
   * da aplica��o.
   * @return boolean Retorna true se o Action est� contido nas notas da vers�o
   *                 mais recente da aplica��o.
   */
  public boolean getJustReleased() {
    return justReleased;
  }

  /**
   * Retorna o nome do m�dulo do Action.
   * @return String Retorna o nome do m�dulo do Action.
   */
  public String getModule() {
    return module;
  }

  /**
   * Retorna o nome do Action.
   * @return String Retorna o nome do Action.
   */
  public String getName() {
    return name;
  }

  /**
   * Retorna a a��o pai para o caso de este Action ser aninhado.
   * @return Action Retorna a a��o pai para o caso de este Action ser aninhado.
   */
  public Action getParentAction() {
    return parentAction;
  }

  /**
   * Retorna o tipo de exibi��o que ser� usada para o JSP configurado no Action.
   * @return int Retorna o tipo de exibi��o que ser� usada para o JSP configurado
   *             no Action.
   */
  public int getShowType() {
    return showType;
  }

  /**
   * Retorna true se o Action for acessivel atrav�s da �rvore de objetos.
   * @return boolean Retorna true se o Action for acessivel atrav�s da �rvore de
   *                 objetos.
   */
  public boolean getVisible() {
    return visible;
  }

  /**
   * Retorna true para o caso do Action ser acess�vel por um dispositivo m�vel.
   * @return boolean Retorna true para o caso do Action ser acess�vel por um dispositivo m�vel.
   */
  public boolean getMobile() {
    return mobile;
  }

  /**
   * Retorna a URL que representa a ajuda da a��o na aplica��o.
   * @return String Retorna a URL que representa a ajuda da a��o na aplica��o.
   */
  public String helpURL() {
    return Controller.ACTION_HELP_BUSINESS_OBJECT.url(HelpManager.ACTION_NAME + "=" + getName());
  }

  /**
   * Retorna a lista de a��es aninhadas.
   * @return ActionList Retorna a lista de a��es aninhadas.
   */
  public ActionList nestedActionList() {
    return nestedActionList;
  }

  /**
   * Retorna a URL epec�fica para o envio, por e-mail, do conte�do apontado
   * pela a��o na aplica��o. <b>Ser�o utilizadas as configura��es padr�o
   * de envio do MailService (servi�o de e-mail da aplica��o).</b>
   * @param command Command Comando para ser executado na a��o.
   * @param params String Par�metros do tipo 'param1=value1&param2=value2' para
   *        serem adicionados a URL.
   * @return String Retorna a URL epec�fica para o envio, por e-mail, do conte�do
   *                apontado pela a��o na aplica��o. <b>Ser�o utilizadas as
   *                configura��es padr�o de envio do MailService (servi�o de
   *                e-mail da aplica��o).</b>
   * @throws Exception Em caso de exce��o na tentativa de codificar a URL.
   */
  public String sendMailURL(Command command,
                            String  params) throws Exception {
    return sendMailURL(command,
                       params,
                       "",
                       -1,
                       "",
                       "",
                       false,
                       new String[0],
                       new String[0],
                       new String[0],
                       new String[0],
                       new String[0],
                       caption);
  }

  /**
   * Retorna a URL epec�fica para o envio, por e-mail, do conte�do apontado
   * pela a��o na aplica��o. <b>Ser�o utilizadas as configura��es padr�o
   * de envio do MailService (servi�o de e-mail da aplica��o).</b>
   * @param command Command Comando para ser executado na a��o.
   * @param params String Par�metros do tipo 'param1=value1&param2=value2' para
   *        serem adicionados a URL.
   * @param to String[] Endere�os dos destinat�rios.
   * @param cc String[] Endere�os das c�pias carbono.
   * @param bcc String[] Endere�os das c�pias carbono ocultas.
   * @param subject String Assunto.
   * @return String Retorna a URL epec�fica para o envio, por e-mail, do conte�do
   *                apontado pela a��o na aplica��o. <b>Ser�o utilizadas as
   *                configura��es padr�o de envio do MailService (servi�o de
   *                e-mail da aplica��o).</b>
   * @throws Exception Em caso de exce��o na tentativa de codificar a URL.
   */
  public String sendMailURL(Command  command,
                            String   params,
                            String[] to,
                            String[] cc,
                            String[] bcc,
                            String   subject) throws Exception {
    return sendMailURL(command,
                       params,
                       "",
                       -1,
                       "",
                       "",
                       false,
                       new String[0],
                       new String[0],
                       to,
                       cc,
                       bcc,
                       subject);
  }

  /**
   * Retorna a URL epec�fica para o envio, por e-mail, do conte�do apontado
   * pela a��o na aplica��o.
   * @param command Command Comando para ser executado na a��o.
   * @param params String Par�metros do tipo 'param1=value1&param2=value2' para
   *        serem adicionados a URL.
   * @param smtpHost String Endere�o do servidor SMTP.
   * @param smtpPort int Porta do servidor SMTP. -1 indica que a porta padr�o
   *             ser� usada.
   * @param smtpUsername String Nome do usu�rio para autentica��o.
   * @param smtpPassword String Senha para autentica��o.
   * @param smtpSSL boolean True para que seja utilizada conex�o segura atrav�s de SSL.
   * @param from String[] Endere�os dos remetentes.
   * @param replyTo String[] Endere�os de resposta.
   * @param to String[] Endere�os dos destinat�rios.
   * @param cc String[] Endere�os das c�pias carbono.
   * @param bcc String[] Endere�os das c�pias carbono ocultas.
   * @param subject String Assunto.
   * @return String Retorna a URL epec�fica para o envio, por e-mail, do conte�do
   *                apontado pela a��o na aplica��o.
   * @throws Exception Em caso de exce��o na tentativa de codificar a URL.
   */
  public String sendMailURL(Command  command,
                            String   params,
                            String   smtpHost,
                            int      smtpPort,
                            String   smtpUsername,
                            String   smtpPassword,
                            boolean  smtpSSL,
                            String[] from,
                            String[] replyTo,
                            String[] to,
                            String[] cc,
                            String[] bcc,
                            String   subject) throws Exception {
    // aponta para a a��o de envio de e-mail
    String result = Controller.ACTION_SEND_MAIL.url(Controller.PARAM_SEND_MAIL_SMTP_HOST + "=" + smtpHost + "&"
                                                  + Controller.PARAM_SEND_MAIL_SMTP_PORT + "=" + smtpPort + "&"
                                                  + Controller.PARAM_SEND_MAIL_SMTP_USERNAME + "=" + smtpUsername + "&"
                                                  + Controller.PARAM_SEND_MAIL_SMTP_PASSWORD + "=" + smtpPassword + "&"
                                                  + Controller.PARAM_SEND_MAIL_SMTP_SSL + "=" + smtpSSL + "&"
                                                  + Controller.PARAM_SEND_MAIL_FROM + "=" + StringTools.arrayStringToString(from, ";") + "&"
                                                  + Controller.PARAM_SEND_MAIL_REPLY_TO + "=" + StringTools.arrayStringToString(replyTo, ";") + "&"
                                                  + Controller.PARAM_SEND_MAIL_TO + "=" + StringTools.arrayStringToString(to, ";") + "&"
                                                  + Controller.PARAM_SEND_MAIL_CC + "=" + StringTools.arrayStringToString(cc, ";") + "&"
                                                  + Controller.PARAM_SEND_MAIL_BCC + "=" + StringTools.arrayStringToString(bcc, ";") + "&"
                                                  + Controller.PARAM_SEND_MAIL_SUBJECT + "=" + subject + "&"
                                                  + Controller.PARAM_SEND_MAIL_URL + "=" + StringTools.encodeURL(url(command, params)));
    // mostra em um Pop Up
    result = Popup.script(result, Controller.ACTION_SEND_MAIL.getName(), 240, 420, Popup.POSITION_CENTER);
    // retorna
    return result;
  }

  /**
   * Define o caminho de acesso na �rvore de a��es.
   * @param accessPath String Caminho de acesso na �rvore de a��es.
   */
  public void setAccessPath(String accessPath) {
    this.accessPath = accessPath;
  }

  /**
   * Define se o Action est� definido como Beta nas notas da vers�o mais
   * recente da aplica��o.
   * @param beta True se o Action est� definido como Beta nas notas da vers�o
   *             mais recente da aplica��o.
   */
  public void setBeta(boolean beta) {
    this.beta = beta;
  }

  /**
   * Define o titulo do Action.
   * @param caption String Titulo do Action.
   */
  public void setCaption(String caption) {
    this.caption = caption;
  }

  /**
   * Define a categoria do Action.
   * @param category int Categoria do Action.
   */
  public void setCategory(int category) {
    this.category = category;
  }

  /**
   * Define se o Action representa um ponto central do m�dulo a que pertence.
   * @param centralPoint boolean True para que o Action seja definido como
   *                     representando um ponto central do m�dulo a que pertence.
   */
  public void setCentralPoint(boolean centralPoint) {
    this.centralPoint = centralPoint;
  }

  /**
   * Define o �ndice de ordena��o se o Action representa um ponto central do
   * m�dulo a que pertence.
   * @param centralPointIndex int �ndice de ordena��o do Action.
   */
  public void setCentralPointIndex(int centralPointIndex) {
    this.centralPointIndex = centralPointIndex;
  }

  /**
   * Define o nome da classe na qual o Action foi originalmente declarado como
   * membro.
   * @param declaringClassName String Nome da classe na qual o Action foi
   *                           originalmente declarado como membro.
   */
  public void setDeclaringClassName(String declaringClassName) {
    this.declaringClassName = declaringClassName;
  }

  /**
   * Define a descri��o detalhada do Action.
   * @param description String Descri��o detalhada do Action.
   */
  public void setDescription(String description) {
    this.description = description;
  }

  /**
   * Define a ajuda extra do Action.
   * @param help String Ajuda extra do Action.
   */
  public void setHelp(String help) {
    this.help = help;
  }

  /**
   * Define o arquivo JSP do Action.
   * @param jsp String Arquivo JSP do Action.
   */
  public void setJsp(String jsp) {
    this.jsp = jsp;
  }

  /**
   * Define se o Action est� contido nas notas da vers�o mais recente da
   * aplica��o.
   * @param justReleased boolean True se o Action est� contido nas notas da
   *                     vers�o mais recente da aplica��o.
   */
  public void setJustReleased(boolean justReleased) {
    this.justReleased = justReleased;
  }

  /**
   * Define o nome do m�dulo do Action.
   * @param module String Nome do m�dulo do Action.
   */
  public void setModule(String module) {
    this.module = module;
  }

  /**
   * Define o nome do Action.
   * @param name String Nome do Action.
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Define a a��o pai para o caso de este Action ser aninhado.
   * @param parentAction Action a��o pai para o caso de este Action ser aninhado.
   */
  public void setParentAction(Action parentAction) {
    this.parentAction = parentAction;
  }

  /**
   * Define o tipo de exibi��o que ser� usada para o JSP configurado no Action.
   * Para utilizar mais de uma op��o basta som�-las.
   * @param showType int Tipo de exibi��o que ser� usada para o JSP configurado
   *             no Action. Para utilizar mais de uma op��o basta som�-las.
   */
  public void setShowType(int showType) {
    this.showType = showType;
  }

  /**
   * Define se a a��o ser� acessivel atrav�s da �rvore de objetos.
   * @param visible boolean True se a a��o for acessivel atrav�s da �rvore de objetos.
   */
  public void setVisible(boolean visible) {
    this.visible = visible;
  }

  /**
   * Define se a a��o ser� acessivel atrav�s de um dispositivo m�vel
   * @param mobile boolean True se a a��o for acessivel atrav�s de um dispositivo m�vel.
   */
  public void setMobile(boolean mobile) {
    this.mobile = mobile;
  }

  /**
   * Retorna a URL que representa a a��o na aplica��o.
   * @return String Retorna a URL que representa a a��o na aplica��o.
   */
  public String url() {
    return url(null, "");
  }

  /**
   * Retorna a URL que representa a a��o na aplica��o.
   * @param command Command Comando para ser executado na a��o.
   * @return String Retorna a URL que representa a a��o na aplica��o.
   */
  public String url(Command command) {
    return url(command, "");
  }

  /**
   * Retorna a URL que representa a a��o na aplica��o.
   * @param params String Par�metros do tipo 'param1=value1&param2=value2' para
   *        serem adicionados a URL.
   * @return String Retorna a URL que representa a a��o na aplica��o.
   */
  public String url(String params) {
    return url(null, params);
  }

  /**
   * Retorna a URL que representa a a��o na aplica��o.
   * @param command Command Comando para ser executado na a��o.
   * @param params String Par�metros do tipo 'param1=value1&param2=value2' para
   *        serem adicionados a URL.
   * @return String Retorna a URL que representa a a��o na aplica��o.
   */
  public String url(Command command, String params) {
    // redireciona para o Controller...
    String result = "controller?" + Controller.ACTION + "=" + name;
    // adiciona o comando
    if (command != null)
      result += "&" + Controller.COMMAND + "=" + command.getName();
    // adiciona os par�metros
    if (params != null && !params.equals(""))
      result += "&" + params;
    // o tipo � Popup default?
    if (showType == SHOW_TYPE_POPUP_DEFAULT)
      result = Popup.script(result, name, 480, 640, Popup.POSITION_CENTER, false, false, false, false, (category == CATEGORY_REPORT ? true : false), false);
    // o tipo � Popup estendido?
    else if (showType == SHOW_TYPE_POPUP_EXTENDED)
      result = Popup.script(result, name, 600, 800, Popup.POSITION_CENTER, false, false, false, false, (category == CATEGORY_REPORT ? true : false), false);
    // o tipo � Popup full screen?
    else if (showType == SHOW_TYPE_POPUP_FULL_SCREEN)
      result = Popup.script(result, name, 0, 0, Popup.POSITION_LEFT_TOP, false, false, false, false, (category == CATEGORY_REPORT ? true : false), false);
    // retorna
    return result;
  }

}
