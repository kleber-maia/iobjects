package iobjects;

import iobjects.servlet.*;
import iobjects.ui.*;
import iobjects.util.*;
import iobjects.help.*;

/**
 * Representa uma ação na aplicação realizada por um BusinessObject.
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
                                 "Cartões",
                                 "Cadastros",
                                 "Processos",
                                 "Relatórios"};

  /**
   * Construtor padrão.
   * @param name String Nome do Action.
   * @param caption String Título do Action.
   * @param description String Descrição detalhada do Action.
   * @param help String Ajuda extra do Action.
   * @param jsp String Arquivo JSP do Action.
   * @param module String Módulo do Action.
   * @param accessPath String Caminho na árvore do Action.
   * @param category int Categoria do Action.
   * @param mobile boolean True para o caso de uma ação ser acessível por um dispositivo móvel.
   * @param visible boolean True se a ação for acessivel através da árvore de objetos.
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
    // define o tipo de exibição de acordo com a categoria
    if (category == CATEGORY_PROCESS)
      this.showType = SHOW_TYPE_POPUP_DEFAULT;
    else
      this.showType = SHOW_TYPE_EMBEDED;
  }

  /**
   * Adiciona 'action' a NestedActionList oferencendo uma forma cômoda de criar
   * uma nova ação ao tempo em que a adiciona a lista de ações aninhadas.
   * @param action Action para ser adicionada a NestedActionList.
   * @return Action Adiciona 'action' a NestedActionList oferencendo uma forma cômoda
   *                de criar uma nova ação ao tempo em que a adiciona a lista
   *                de ações aninhadas.
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
   * Adiciona 'command' a CommandList oferencendo uma forma cômoda de criar
   * um novo comando ao tempo em que o adiciona a ação.
   * @param command Command para ser adicionado a CommandList.
   * @return Command Adiciona 'command' a CommandList oferencendo uma forma
   *                 cômoda de criar um novo comando ao tempo em que adiciona-o
   *                 a ação.
   */
  public Command addCommand(Command command) {
    // adiciona a nossa lista
    commandList.add(command);
    // retorna-o
    return command;
  }

  /**
   * Retorna a lista de comandos associados a ação.
   * @return CommandList Retorna a lista de comandos associados a ação.
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
   * Retorna o caminho de exibição do Action no TreeView.
   * @return String Retorna o caminho de exibição do Action no TreeView.
   */
  public String getTreeViewPath() {
    // se somos aninhados...
    if (parentAction != null)
      return parentAction.getTreeViewPath() + "/" + caption;
    // se não somos aninhados
    else
      return (!module.equals("") ? module : OUTROS)
           + "/" + categories[category]
           + "/" + (!accessPath.equals("") ? accessPath + "/" : "")
           + (!caption.equals("") ? caption : DESCONHECIDO);
  }

  /**
   * Retorna o caminho de acesso na árvore de ações.
   * @return String Retorna o caminho de acesso na árvore de ações.
   */
  public String getAccessPath() {
    return accessPath;
  }

  /**
   * Retorna true se o Action está definido como Beta nas notas da versão mais
   * recente da aplicação.
   * @return boolean Retorna true se o Action está definido como Beta nas notas
   *                 da versão mais recente da aplicação.
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
   * Retorna true se o Action representa um ponto central do módulo a que pertence.
   * @return boolean Retorna true se o Action representa um ponto central do
   *                 módulo a que pertence.
   */
  public boolean getCentralPoint() {
    return centralPoint;
  }

  /**
   * Retorna o índice de ordenação se o Action representa um ponto central do
   * módulo a que pertence.
   * @return int Retorna o índice de ordenação se o Action representa um ponto
   *             central do módulo a que pertence.
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
   * Retorna a descrição detalhada do Action.
   * @return String Retorna a descrição detalhada do Action.
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
   * Retorna true se o Action está contido nas notas da versão mais recente
   * da aplicação.
   * @return boolean Retorna true se o Action está contido nas notas da versão
   *                 mais recente da aplicação.
   */
  public boolean getJustReleased() {
    return justReleased;
  }

  /**
   * Retorna o nome do módulo do Action.
   * @return String Retorna o nome do módulo do Action.
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
   * Retorna a ação pai para o caso de este Action ser aninhado.
   * @return Action Retorna a ação pai para o caso de este Action ser aninhado.
   */
  public Action getParentAction() {
    return parentAction;
  }

  /**
   * Retorna o tipo de exibição que será usada para o JSP configurado no Action.
   * @return int Retorna o tipo de exibição que será usada para o JSP configurado
   *             no Action.
   */
  public int getShowType() {
    return showType;
  }

  /**
   * Retorna true se o Action for acessivel através da árvore de objetos.
   * @return boolean Retorna true se o Action for acessivel através da árvore de
   *                 objetos.
   */
  public boolean getVisible() {
    return visible;
  }

  /**
   * Retorna true para o caso do Action ser acessível por um dispositivo móvel.
   * @return boolean Retorna true para o caso do Action ser acessível por um dispositivo móvel.
   */
  public boolean getMobile() {
    return mobile;
  }

  /**
   * Retorna a URL que representa a ajuda da ação na aplicação.
   * @return String Retorna a URL que representa a ajuda da ação na aplicação.
   */
  public String helpURL() {
    return Controller.ACTION_HELP_BUSINESS_OBJECT.url(HelpManager.ACTION_NAME + "=" + getName());
  }

  /**
   * Retorna a lista de ações aninhadas.
   * @return ActionList Retorna a lista de ações aninhadas.
   */
  public ActionList nestedActionList() {
    return nestedActionList;
  }

  /**
   * Retorna a URL epecífica para o envio, por e-mail, do conteúdo apontado
   * pela ação na aplicação. <b>Serão utilizadas as configurações padrão
   * de envio do MailService (serviço de e-mail da aplicação).</b>
   * @param command Command Comando para ser executado na ação.
   * @param params String Parâmetros do tipo 'param1=value1&param2=value2' para
   *        serem adicionados a URL.
   * @return String Retorna a URL epecífica para o envio, por e-mail, do conteúdo
   *                apontado pela ação na aplicação. <b>Serão utilizadas as
   *                configurações padrão de envio do MailService (serviço de
   *                e-mail da aplicação).</b>
   * @throws Exception Em caso de exceção na tentativa de codificar a URL.
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
   * Retorna a URL epecífica para o envio, por e-mail, do conteúdo apontado
   * pela ação na aplicação. <b>Serão utilizadas as configurações padrão
   * de envio do MailService (serviço de e-mail da aplicação).</b>
   * @param command Command Comando para ser executado na ação.
   * @param params String Parâmetros do tipo 'param1=value1&param2=value2' para
   *        serem adicionados a URL.
   * @param to String[] Endereços dos destinatários.
   * @param cc String[] Endereços das cópias carbono.
   * @param bcc String[] Endereços das cópias carbono ocultas.
   * @param subject String Assunto.
   * @return String Retorna a URL epecífica para o envio, por e-mail, do conteúdo
   *                apontado pela ação na aplicação. <b>Serão utilizadas as
   *                configurações padrão de envio do MailService (serviço de
   *                e-mail da aplicação).</b>
   * @throws Exception Em caso de exceção na tentativa de codificar a URL.
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
   * Retorna a URL epecífica para o envio, por e-mail, do conteúdo apontado
   * pela ação na aplicação.
   * @param command Command Comando para ser executado na ação.
   * @param params String Parâmetros do tipo 'param1=value1&param2=value2' para
   *        serem adicionados a URL.
   * @param smtpHost String Endereço do servidor SMTP.
   * @param smtpPort int Porta do servidor SMTP. -1 indica que a porta padrão
   *             será usada.
   * @param smtpUsername String Nome do usuário para autenticação.
   * @param smtpPassword String Senha para autenticação.
   * @param smtpSSL boolean True para que seja utilizada conexão segura através de SSL.
   * @param from String[] Endereços dos remetentes.
   * @param replyTo String[] Endereços de resposta.
   * @param to String[] Endereços dos destinatários.
   * @param cc String[] Endereços das cópias carbono.
   * @param bcc String[] Endereços das cópias carbono ocultas.
   * @param subject String Assunto.
   * @return String Retorna a URL epecífica para o envio, por e-mail, do conteúdo
   *                apontado pela ação na aplicação.
   * @throws Exception Em caso de exceção na tentativa de codificar a URL.
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
    // aponta para a ação de envio de e-mail
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
   * Define o caminho de acesso na árvore de ações.
   * @param accessPath String Caminho de acesso na árvore de ações.
   */
  public void setAccessPath(String accessPath) {
    this.accessPath = accessPath;
  }

  /**
   * Define se o Action está definido como Beta nas notas da versão mais
   * recente da aplicação.
   * @param beta True se o Action está definido como Beta nas notas da versão
   *             mais recente da aplicação.
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
   * Define se o Action representa um ponto central do módulo a que pertence.
   * @param centralPoint boolean True para que o Action seja definido como
   *                     representando um ponto central do módulo a que pertence.
   */
  public void setCentralPoint(boolean centralPoint) {
    this.centralPoint = centralPoint;
  }

  /**
   * Define o índice de ordenação se o Action representa um ponto central do
   * módulo a que pertence.
   * @param centralPointIndex int Índice de ordenação do Action.
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
   * Define a descrição detalhada do Action.
   * @param description String Descrição detalhada do Action.
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
   * Define se o Action está contido nas notas da versão mais recente da
   * aplicação.
   * @param justReleased boolean True se o Action está contido nas notas da
   *                     versão mais recente da aplicação.
   */
  public void setJustReleased(boolean justReleased) {
    this.justReleased = justReleased;
  }

  /**
   * Define o nome do módulo do Action.
   * @param module String Nome do módulo do Action.
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
   * Define a ação pai para o caso de este Action ser aninhado.
   * @param parentAction Action ação pai para o caso de este Action ser aninhado.
   */
  public void setParentAction(Action parentAction) {
    this.parentAction = parentAction;
  }

  /**
   * Define o tipo de exibição que será usada para o JSP configurado no Action.
   * Para utilizar mais de uma opção basta somá-las.
   * @param showType int Tipo de exibição que será usada para o JSP configurado
   *             no Action. Para utilizar mais de uma opção basta somá-las.
   */
  public void setShowType(int showType) {
    this.showType = showType;
  }

  /**
   * Define se a ação será acessivel através da árvore de objetos.
   * @param visible boolean True se a ação for acessivel através da árvore de objetos.
   */
  public void setVisible(boolean visible) {
    this.visible = visible;
  }

  /**
   * Define se a ação será acessivel através de um dispositivo móvel
   * @param mobile boolean True se a ação for acessivel através de um dispositivo móvel.
   */
  public void setMobile(boolean mobile) {
    this.mobile = mobile;
  }

  /**
   * Retorna a URL que representa a ação na aplicação.
   * @return String Retorna a URL que representa a ação na aplicação.
   */
  public String url() {
    return url(null, "");
  }

  /**
   * Retorna a URL que representa a ação na aplicação.
   * @param command Command Comando para ser executado na ação.
   * @return String Retorna a URL que representa a ação na aplicação.
   */
  public String url(Command command) {
    return url(command, "");
  }

  /**
   * Retorna a URL que representa a ação na aplicação.
   * @param params String Parâmetros do tipo 'param1=value1&param2=value2' para
   *        serem adicionados a URL.
   * @return String Retorna a URL que representa a ação na aplicação.
   */
  public String url(String params) {
    return url(null, params);
  }

  /**
   * Retorna a URL que representa a ação na aplicação.
   * @param command Command Comando para ser executado na ação.
   * @param params String Parâmetros do tipo 'param1=value1&param2=value2' para
   *        serem adicionados a URL.
   * @return String Retorna a URL que representa a ação na aplicação.
   */
  public String url(Command command, String params) {
    // redireciona para o Controller...
    String result = "controller?" + Controller.ACTION + "=" + name;
    // adiciona o comando
    if (command != null)
      result += "&" + Controller.COMMAND + "=" + command.getName();
    // adiciona os parâmetros
    if (params != null && !params.equals(""))
      result += "&" + params;
    // o tipo é Popup default?
    if (showType == SHOW_TYPE_POPUP_DEFAULT)
      result = Popup.script(result, name, 480, 640, Popup.POSITION_CENTER, false, false, false, false, (category == CATEGORY_REPORT ? true : false), false);
    // o tipo é Popup estendido?
    else if (showType == SHOW_TYPE_POPUP_EXTENDED)
      result = Popup.script(result, name, 600, 800, Popup.POSITION_CENTER, false, false, false, false, (category == CATEGORY_REPORT ? true : false), false);
    // o tipo é Popup full screen?
    else if (showType == SHOW_TYPE_POPUP_FULL_SCREEN)
      result = Popup.script(result, name, 0, 0, Popup.POSITION_LEFT_TOP, false, false, false, false, (category == CATEGORY_REPORT ? true : false), false);
    // retorna
    return result;
  }

}
