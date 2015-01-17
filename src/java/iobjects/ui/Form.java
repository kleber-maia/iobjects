package iobjects.ui;

import javax.servlet.http.*;

import iobjects.*;
import iobjects.servlet.*;
import iobjects.ui.ajax.*;

/**
 * Representa um formulário para o envio de dados ao servidor.
 */
public class Form {

  static public final String COMPOSE                  = "compose";
  static public final String COMPOSE_TYPE_HTML        = "html";
  static public final String COMPOSE_TYPE_JAVA_SCRIPT = "javascript";

  private Action             action         = null;
  private Command            command        = null;
  private String             encodeType     = "";
  private boolean            focusControl   = false;
  private String             id             = "";
  private String             onSubmitScript = "";
  private HttpServletRequest request        = null;
  private String             target         = "";
  private boolean            useAjax        = false;
  private boolean            autoAdjust     = false;

  /**
   * Construtor padrão.
   * @param request HttpServletRequest Requisição atual.
   * @param id String Identificação do Form na página.
   * @param action Action Ação de destino do Form para ser executada pelo Controller.
   * @param target String Nome do Frame de destino dos dados do Form.
   * @param focusControl boolean True para que o primeiro controle do Form receba
   *                     o foco.
   */
  public Form(HttpServletRequest request,
              String             id,
              Action             action,
              String             target,
              boolean            focusControl) {
    this(request, id, action, null, target, focusControl);
  }

  /**
   * Construtor padrão.
   * @param request HttpServletRequest Requisição atual.
   * @param id String Identificação do Form na página.
   * @param action Action Ação de destino do Form para ser executada pelo Controller.
   * @param command Command Comando para ser executado na ação de destino.
   * @param target String Nome do Frame de destino dos dados do Form.
   * @param focusControl boolean True para que o primeiro controle do Form receba
   *                     o foco.
   */
  public Form(HttpServletRequest request,
              String             id,
              Action             action,
              Command            command,
              String             target,
              boolean            focusControl) {
    // se não informou o Id...exceção
    if ((id == null) || id.trim().equals(""))
      throw new RuntimeException(new ExtendedException(getClass().getName(), "", "Id não informado."));
    this.request = request;
    this.id = id;
    this.action = action;
    this.command = command;
    this.target = target;
    this.focusControl = focusControl;
  }

  /**
   * Construtor padrão.
   * @param request HttpServletRequest Requisição atual.
   * @param id String Identificação do Form na página.
   * @param action Action Ação de destino do Form para ser executada pelo Controller.
   * @param command Command Comando para ser executado na ação de destino.
   * @param target String Nome do Frame de destino dos dados do Form.
   * @param focusControl boolean True para que o primeiro controle do Form receba
   *                     o foco.
   * @param useAjax boolean True para que o Form seja submetido através de Ajax.
   *                Neste caso 'target' deve identificar o elemento na página
   *                onde o resultado da submissão do Form será inserido, podendo
   *                ser qualquer container HTML válido, como: DIV, SPAN, TD, etc.
   */
  public Form(HttpServletRequest request,
              String             id,
              Action             action,
              Command            command,
              String             target,
              boolean            focusControl,
              boolean            useAjax) {
    // se não informou o Id...exceção
    if ((id == null) || id.trim().equals(""))
      throw new RuntimeException(new ExtendedException(getClass().getName(), "", "Id não informado."));
    this.request = request;
    this.id = id;
    this.action = action;
    this.command = command;
    this.target = target;
    this.focusControl = focusControl;
    this.useAjax = useAjax;
  }
  
  /**
   * Construtor padrão.
   * @param request HttpServletRequest Requisição atual.
   * @param id String Identificação do Form na página.
   * @param action Action Ação de destino do Form para ser executada pelo Controller.
   * @param command Command Comando para ser executado na ação de destino.
   * @param target String Nome do Frame de destino dos dados do Form.
   * @param focusControl boolean True para que o primeiro controle do Form receba
   *                     o foco.
   * @param useAjax boolean True para que o Form seja submetido através de Ajax.
   *                Neste caso 'target' deve identificar o elemento na página
   *                onde o resultado da submissão do Form será inserido, podendo
   *                ser qualquer container HTML válido, como: DIV, SPAN, TD, etc.
   */
  public Form(HttpServletRequest request,
              String             id,
              Action             action,
              Command            command,
              String             target,
              boolean            focusControl,
              boolean            useAjax,
              boolean            autoAdjust) {
    // se não informou o Id...exceção
    if ((id == null) || id.trim().equals(""))
      throw new RuntimeException(new ExtendedException(getClass().getName(), "", "Id não informado."));
    this.request = request;
    this.id = id;
    this.action = action;
    this.command = command;
    this.target = target;
    this.focusControl = focusControl;
    this.useAjax = useAjax;
    this.autoAdjust = autoAdjust;
  }

  /**
   * Retorna o script HTML contendo a representação de início do Form.
   * @return String Retorna o script HTML contendo a representação de início do Form.
   */
  public String begin() {
    // nosso resultado
    StringBuffer result = new StringBuffer();
    // nossos scripts
    result.append("<script type=\"text/javascript\">"
                +   "var " + id + "id = \"" + id + "\";"
                +   "var " + id + "submited = false;"
                +   "$(window).on(\"pageshow\", function(event){"
                +     "formSubmited = false;"
                +   "});"
                + "</script> ");
    // inicia o form
    result.append("<form id=\"" + id + "\" "
                 + "name=\"" + id + "\" " 
                 + "action=\"" + (useAjax 
                                   ? "javascript:Form_ajaxSubmit(" + id + "id);" 
                                   : encodeType.contains("multipart")
                                       ? "controller?" + Controller.ACTION + "=" + action.getName() + "&" + Controller.COMMAND + "=" + (command != null ? command.getName() : "")
                                       :  "controller") + "\" "
                 + "enctype=\"" + encodeType + "\" "
                 + "accept-charset=\"ISO-8859-1\" "
                 + "method=\"POST\" " + (!target.equals("") 
                                          ? (useAjax ? "" : "target=\"" + target + "\" ") 
                                          : " ") 
                 + (!onSubmitScript.equals("") ? "onsubmit=\"return " + onSubmitScript + ";\" " : "")
                 + (autoAdjust ? "style=\"width:100%; height:100%;\" " : "")
                 + " > ");
    // adiciona o input de recarga
    result.append("<input type=\"hidden\" id=\"" + id + Controller.RELOADED + "\" name=\"" + Controller.RELOADED + "\" value=\"" + Boolean.FALSE.toString() + "\" /> ");
    // adiciona o input de ação
    result.append("<input type=\"hidden\" id=\"" + id + Controller.ACTION + "\" name=\"" + Controller.ACTION + "\" value=\"" + action.getName() + "\" /> ");
    // adiciona o input de comando
    result.append("<input type=\"hidden\" id=\"" + id + Controller.COMMAND + "\" name=\"" + Controller.COMMAND + "\" value=\"" + (command != null ? command.getName() : "") + "\" /> ");
    // adiciona o input de último comando executado
    result.append("<input type=\"hidden\" id=\"" + id + Controller.LAST_COMMAND + "\" name=\"" + Controller.LAST_COMMAND + "\" value=\"" + Controller.getCommand(request) + "\" /> ");
    // retorna
    return result.toString();
  }

  /**
   * Compõe a resposta da submissão do Form através de Ajax no formato HTML ou
   * Javascript. Caso a resposta seja no formato HTML, seu conteúdo será inserido
   * no elemento informado por 'target' no construtor do Form.
   * Exemplo:
   * <code>
   *    &lt;%if (Controller.getCommand(request).equals("DUMMY")) {
   *        // executa processamento
   *        ...
   *        // compõe a resposta
   *        Form.composeAjaxRespose(response, Form.COMPOSE_TYPE_JAVA_SCRIPT);%&gt;
   *        alert("Processamento realizado!");
   *    &lt;%  // finalizamos
   *        return;
   *      } // if%&gt;
   * </code>
   * @param response HttpServletResponse Resposta.
   * @param type String Tipo de resposta que será retornada.
   */
  static public void composeAjaxResponse(HttpServletResponse response, String type) {
    // define o tipo como texto
    Ajax.setResponseTypeText(response);
    // marca como sendo HTML
    Ajax.setResponseHeader(response, COMPOSE, type);
  }

  /**
   * Retorna o script HTML contendo a representação de término do Form.
   * @return String Retorna o script HTML contendo a representação de término do Form.
   */
  public String end() {
    // termina o form
    return "</form>"
         +   (useAjax
                ? "<div id=\"" + id + "ajaxTargetContainer\" style=\"display:none;\"></div>"
                : "")
         + "<script type=\"text/javascript\">"
             // põe o foco no form
         +   "Form_focus(" + id + "id);"
             // controles Ajax
         +   (useAjax 
                ?      // variáveis locais para requisições Ajax
                       "var " + id + "ajaxRequest       = null; "
                   +   "var " + id + "ajaxTarget        = \"" + target + "\"; "
                   +   "var " + id + "ajaxCallbackValid = false; "
                       // função de triangulação para requisições Ajax
                   +   "function " + id + "_ajaxSubmitCallback() { "
                   +     "Form_ajaxSubmitCallback(" + id + "id); "
                   +   "} "
               : "")
         + "</script>";
  }

  /**
   * Retorna a ação de destino do Form para ser executada pelo Controller.
   * @return String Retorna a ação de destino do Form para ser executada
   *         pelo Controller.
   */
  public Action getAction() {
    return action;
  }

  /**
   * Retorna o comando para ser executado na ação de destino.
   * @return String Retorna o comando para ser executado na ação de destino.
   */
  public Command getCommand() {
    return command;
  }

  /**
   * Retorna o tipo de codificação do Form na página.
   * @return String Retorna o tipo de codificação do Form na página.
   */
  public String getEncodeType() {
    return encodeType;
  }
  
  /**
   * Retorna se o primeiro controle do Form receberá o foco.
   * @return boolean Retorna se o primeiro controle do Form receberá o foco.
   */
  public boolean getFocusControl() {
    return focusControl;
  }

  /**
   * Retorna a identificação do Form na página.
   * @return String Retorna a identificação do Form na página.
   */
  public String getId() {
    return id;
  }

  /**
   * Retorna o script JavaScript que será executado quando o Form for submetido.
   * Este script deve retornar true para o caso de o Form poder ser submetido
   * ou false em caso contrário.
   * @return String Retorna o script JavaScript que será executado quando o Form
   *                for submetido. Este script deve retornar true para o caso
   *                de o Form poder ser submetido ou false em caso contrário.
   */
  public String getOnSubmitScript() {
    return onSubmitScript;
  }

  /**
   * Retorna o nome do Frame de destino dos dados do Form.
   * @return String Retorna o nome do Frame de destino dos dados do Form.
   */
  public String getTarget() {
    return target;
  }

  /**
   * Retorna se o Form será submetido através de Ajax.
   * @return boolean Retorna se o Form será submetido através de Ajax.
   */
  public boolean getUseAjax() {
    return useAjax;
  }

  /**
   * Retorna true se o formulário foi recarregado a partir do script retornado
   * pelo método 'submitLastCommandScript()'.
   * @return boolean Retorna true se o formulário foi recarregado a partir do
   *                 script retornado pelo método 'submitLastCommandScript()'.
   * @see submitLastCommandScript()
   */
  public boolean reloaded() {
    // parâmetro da requisição
    String result = request.getParameter(Controller.RELOADED);
    // retorna se fomos recarregados
    return (result != null) && Boolean.valueOf(result).booleanValue();
  }

  /**
   * Define a ação de destino do Form para ser executada pelo Controller.
   * @param action Action Ação de destino do Form para ser executada pelo Controller.
   */
  public void setAction(Action action) {
    this.action = action;
  }

  /**
   * Define o comando para ser executado na ação de destino.
   * @param command Command comando para ser executado na ação de destino.
   */
  public void setCommand(Command command) {
    this.command = command;
  }

  /**
   * Define o tipo de codificação do Form na página.
   * @param id String Tipo de codificação do Form na página.
   */
  public void setEncodeType(String encodeType) {
    this.encodeType = encodeType;
  }
  
  /**
   * Define se o primeiro controle do Form receberá o foco.
   * @param focusControl boolean True para que o primeiro controle do Form
   *                     receba o foco.
   */
  public void setFocusControl(boolean focusControl) {
    this.focusControl = focusControl;
  }

  /**
   * Define a identificação do Form na página.
   * @param id String Identificação do Form na página.
   */
  public void setId(String id) {
    this.id = id;
  }

  /**
   * Define o script JavaScript que será executado quando o Form for submetido.
   * Este script deve retornar true para o caso de o Form poder ser submetido
   * ou false em caso contrário.
   * @param onSubmitScript String Define o script JavaScript que será executado
   *                       quando o Form for submetido. Este script deve retornar
   *                       true para o caso de o Form poder ser submetido ou
   *                       false em caso contrário.
   */
  public void setOnSubmitScript(String onSubmitScript) {
    this.onSubmitScript = onSubmitScript;
  }

  /**
   * Define o nome do Frame de destino dos dados do Form.
   * @param target String Nome do Frame de destino dos dados do Form.
   */
  public void setTarget(String target) {
    this.target = target;
  }

  /**
   * Define se o Form será submetido através de Ajax.
   * @param useAjax True para que o Form será submetido através de Ajax.
   */
  public void setUseAjax(boolean useAjax) {
    this.useAjax = useAjax;
  }

  /**
   * Retorna o script JavaScript contendo a chamada para a função de submissão
   * do Form alterando o seu comando original para o último comando executado.
   * <b>Este método deve ser utilizado em casos onde é necessário enviar dados
   * ao servidor e continuar com a edição do Form.</b>
   * @return String Retorna o script JavaScript contendo a chamada para a função
   *                de submissão do Form alterando o seu comando original para o
   *                último comando executado.
   *                <b>Este método deve ser utilizado em casos onde é necessário
   *                enviar dados ao servidor e continuar com a edição do Form.</b>
   * @see reloaded()
   */
  public String submitLastCommandScript() {
    return "Form_submitLastCommand(" + id + "id);";
  }

  /**
   * Retorna o script JavaScript contendo a chamada para a função de submissão
   * do Form alterando o seu comando original para 'command'.
   * <b>Este método deve ser utilizado em casos onde é necessário enviar dados
   * ao servidor com um comando diferente do configurado originalmente no Form.</b>
   * @param command Command Comando para ser executado.
   * @return String Retorna o script JavaScript contendo a chamada para a função
   *                de submissão do Form alterando o seu comando original para
   *                'command'.
   *                <b>Este método deve ser utilizado em casos onde é necessário
   *                enviar dados ao servidor com um comando diferente do
   *                configurado originalmente no Form.</b>
   */
  public String submitCustomCommandScript(Command command) {
    return submitCustomCommandScript(command, "", true, false);
  }

  /**
   * Retorna o script JavaScript contendo a chamada para a função de submissão
   * do Form alterando o seu comando original para 'command'.
   * <b>Este método deve ser utilizado em casos onde é necessário enviar dados
   * ao servidor com um comando diferente do configurado originalmente no Form.</b>
   * @param command Command Comando para ser executado.
   * @param avoidRedundantSubmition boolean Evita que o formulário seja submetido
   *                                duas ou mais vezes, sobrecarregando o sistema.
   * @return String Retorna o script JavaScript contendo a chamada para a função
   *                de submissão do Form alterando o seu comando original para
   *                'command'.
   *                <b>Este método deve ser utilizado em casos onde é necessário
   *                enviar dados ao servidor com um comando diferente do
   *                configurado originalmente no Form.</b>
   */
  public String submitCustomCommandScript(Command command,
                                          boolean avoidRedundantSubmition) {
    return submitCustomCommandScript(command, "", avoidRedundantSubmition, false);
  }

  /**
   * Retorna o script JavaScript contendo a chamada para a função de submissão
   * do Form alterando o seu comando original para 'command'.
   * <b>Este método deve ser utilizado em casos onde é necessário enviar dados
   * ao servidor com um comando diferente do configurado originalmente no Form.</b>
   * @param command Command Comando para ser executado.
   * @param confirmationMessage String Mensagem que será exibida ao usuário
   *        confirmando a submissão do Form.
   * @return String Retorna o script JavaScript contendo a chamada para a função
   *                de submissão do Form alterando o seu comando original para
   *                'command'.
   *                <b>Este método deve ser utilizado em casos onde é necessário
   *                enviar dados ao servidor com um comando diferente do
   *                configurado originalmente no Form.</b>
   */
  public String submitCustomCommandScript(Command command,
                                          String  confirmationMessage) {
    return submitCustomCommandScript(command, confirmationMessage, true, false);
  }

  /**
   * Retorna o script JavaScript contendo a chamada para a função de submissão
   * do Form alterando o seu comando original para 'command'.
   * <b>Este método deve ser utilizado em casos onde é necessário enviar dados
   * ao servidor com um comando diferente do configurado originalmente no Form.</b>
   * @param command Command Comando para ser executado.
   * @param confirmationMessage String Mensagem que será exibida ao usuário
   *        confirmando a submissão do Form.
   * @param avoidRedundantSubmition boolean True para evitar que o formulário seja
   *                                submetido duas ou mais vezes,
   *                                sobrecarregando o sistema.
   * @param ignoreConstraints boolean True para que o formulário seja submetido
   *                                  sem que as constraints dos inputs sejam
   *                                  verificadas.
   * @return String Retorna o script JavaScript contendo a chamada para a função
   *                de submissão do Form alterando o seu comando original para
   *                'command'.
   *                <b>Este método deve ser utilizado em casos onde é necessário
   *                enviar dados ao servidor com um comando diferente do
   *                configurado originalmente no Form.</b>
   */
  public String submitCustomCommandScript(Command command,
                                          String  confirmationMessage,
                                          boolean avoidRedundantSubmition,
                                          boolean ignoreConstraints) {
    return "Form_submit(" + id + "id, " + (!confirmationMessage.equals("") ? "'" + confirmationMessage + "'" : "null") + ", " + avoidRedundantSubmition + ", " + ignoreConstraints + ", '" + command.getName() + "');";
  }

  /**
   * Retorna o script JavaScript contendo a chamada para a função de submissão
   * do Form.
   * @return String Retorna o script JavaScript contendo a chamada para a função
   *                de submissão do Form.
   */
  public String submitScript() {
    return submitScript("", true);
  }

  /**
   * Retorna o script JavaScript contendo a chamada para a função de submissão
   * do Form.
   * @param confirmationMessage String Mensagem que será exibida ao usuário
   *        confirmando a submissão do Form.
   * @return String Retorna o script JavaScript contendo a chamada para a função
   *                de submissão do Form.
   */
  public String submitScript(String confirmationMessage) {
    return submitScript(confirmationMessage, true);
  }

  /**
   * Retorna o script JavaScript contendo a chamada para a função de submissão
   * do Form.
   * @param avoidRedundantSubmition boolean Evita que o formulário seja submetido
   *                                duas ou mais vezes, sobrecarregando o sistema.
   * @return String Retorna o script JavaScript contendo a chamada para a função
   *                de submissão do Form.
   */
  public String submitScript(boolean avoidRedundantSubmition) {
    return submitScript("", avoidRedundantSubmition);
  }

  /**
   * Retorna o script JavaScript contendo a chamada para a função de submissão
   * do Form.
   * @param confirmationMessage String Mensagem que será exibida ao usuário
   *        confirmando a submissão do Form.
   * @param avoidRedundantSubmition boolean True para evitar que o formulário seja
   *                                submetido duas ou mais vezes,
   *                                sobrecarregando o sistema.
   * @return String Retorna o script JavaScript contendo a chamada para a função
   *                de submissão do Form.
   */
  public String submitScript(String  confirmationMessage,
                             boolean avoidRedundantSubmition) {
    return submitScript(confirmationMessage, avoidRedundantSubmition, false);
  }

  /**
   * Retorna o script JavaScript contendo a chamada para a função de submissão
   * do Form.
   * @param confirmationMessage String Mensagem que será exibida ao usuário
   *        confirmando a submissão do Form.
   * @param avoidRedundantSubmition boolean True para evitar que o formulário seja
   *                                submetido duas ou mais vezes,
   *                                sobrecarregando o sistema.
   * @param ignoreConstraints boolean True para que o formulário seja submetido
   *                                  sem que as constraints dos inputs sejam
   *                                  verificadas.
   * @return String Retorna o script JavaScript contendo a chamada para a função
   *                de submissão do Form.
   */
  public String submitScript(String  confirmationMessage,
                             boolean avoidRedundantSubmition,
                             boolean ignoreConstraints) {
    return "Form_submit(" + id + "id, " + (!confirmationMessage.equals("") ? "'" + confirmationMessage + "'" : "null") + ", " + avoidRedundantSubmition + ", " + ignoreConstraints + ", " + (command != null ? "'" + command.getName() + "'" : "''") + ");";
  }

}
