package iobjects.ui;

import javax.servlet.http.*;

import iobjects.*;
import iobjects.servlet.*;
import iobjects.ui.ajax.*;

/**
 * Representa um formul�rio para o envio de dados ao servidor.
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
   * Construtor padr�o.
   * @param request HttpServletRequest Requisi��o atual.
   * @param id String Identifica��o do Form na p�gina.
   * @param action Action A��o de destino do Form para ser executada pelo Controller.
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
   * Construtor padr�o.
   * @param request HttpServletRequest Requisi��o atual.
   * @param id String Identifica��o do Form na p�gina.
   * @param action Action A��o de destino do Form para ser executada pelo Controller.
   * @param command Command Comando para ser executado na a��o de destino.
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
    // se n�o informou o Id...exce��o
    if ((id == null) || id.trim().equals(""))
      throw new RuntimeException(new ExtendedException(getClass().getName(), "", "Id n�o informado."));
    this.request = request;
    this.id = id;
    this.action = action;
    this.command = command;
    this.target = target;
    this.focusControl = focusControl;
  }

  /**
   * Construtor padr�o.
   * @param request HttpServletRequest Requisi��o atual.
   * @param id String Identifica��o do Form na p�gina.
   * @param action Action A��o de destino do Form para ser executada pelo Controller.
   * @param command Command Comando para ser executado na a��o de destino.
   * @param target String Nome do Frame de destino dos dados do Form.
   * @param focusControl boolean True para que o primeiro controle do Form receba
   *                     o foco.
   * @param useAjax boolean True para que o Form seja submetido atrav�s de Ajax.
   *                Neste caso 'target' deve identificar o elemento na p�gina
   *                onde o resultado da submiss�o do Form ser� inserido, podendo
   *                ser qualquer container HTML v�lido, como: DIV, SPAN, TD, etc.
   */
  public Form(HttpServletRequest request,
              String             id,
              Action             action,
              Command            command,
              String             target,
              boolean            focusControl,
              boolean            useAjax) {
    // se n�o informou o Id...exce��o
    if ((id == null) || id.trim().equals(""))
      throw new RuntimeException(new ExtendedException(getClass().getName(), "", "Id n�o informado."));
    this.request = request;
    this.id = id;
    this.action = action;
    this.command = command;
    this.target = target;
    this.focusControl = focusControl;
    this.useAjax = useAjax;
  }
  
  /**
   * Construtor padr�o.
   * @param request HttpServletRequest Requisi��o atual.
   * @param id String Identifica��o do Form na p�gina.
   * @param action Action A��o de destino do Form para ser executada pelo Controller.
   * @param command Command Comando para ser executado na a��o de destino.
   * @param target String Nome do Frame de destino dos dados do Form.
   * @param focusControl boolean True para que o primeiro controle do Form receba
   *                     o foco.
   * @param useAjax boolean True para que o Form seja submetido atrav�s de Ajax.
   *                Neste caso 'target' deve identificar o elemento na p�gina
   *                onde o resultado da submiss�o do Form ser� inserido, podendo
   *                ser qualquer container HTML v�lido, como: DIV, SPAN, TD, etc.
   */
  public Form(HttpServletRequest request,
              String             id,
              Action             action,
              Command            command,
              String             target,
              boolean            focusControl,
              boolean            useAjax,
              boolean            autoAdjust) {
    // se n�o informou o Id...exce��o
    if ((id == null) || id.trim().equals(""))
      throw new RuntimeException(new ExtendedException(getClass().getName(), "", "Id n�o informado."));
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
   * Retorna o script HTML contendo a representa��o de in�cio do Form.
   * @return String Retorna o script HTML contendo a representa��o de in�cio do Form.
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
    // adiciona o input de a��o
    result.append("<input type=\"hidden\" id=\"" + id + Controller.ACTION + "\" name=\"" + Controller.ACTION + "\" value=\"" + action.getName() + "\" /> ");
    // adiciona o input de comando
    result.append("<input type=\"hidden\" id=\"" + id + Controller.COMMAND + "\" name=\"" + Controller.COMMAND + "\" value=\"" + (command != null ? command.getName() : "") + "\" /> ");
    // adiciona o input de �ltimo comando executado
    result.append("<input type=\"hidden\" id=\"" + id + Controller.LAST_COMMAND + "\" name=\"" + Controller.LAST_COMMAND + "\" value=\"" + Controller.getCommand(request) + "\" /> ");
    // retorna
    return result.toString();
  }

  /**
   * Comp�e a resposta da submiss�o do Form atrav�s de Ajax no formato HTML ou
   * Javascript. Caso a resposta seja no formato HTML, seu conte�do ser� inserido
   * no elemento informado por 'target' no construtor do Form.
   * Exemplo:
   * <code>
   *    &lt;%if (Controller.getCommand(request).equals("DUMMY")) {
   *        // executa processamento
   *        ...
   *        // comp�e a resposta
   *        Form.composeAjaxRespose(response, Form.COMPOSE_TYPE_JAVA_SCRIPT);%&gt;
   *        alert("Processamento realizado!");
   *    &lt;%  // finalizamos
   *        return;
   *      } // if%&gt;
   * </code>
   * @param response HttpServletResponse Resposta.
   * @param type String Tipo de resposta que ser� retornada.
   */
  static public void composeAjaxResponse(HttpServletResponse response, String type) {
    // define o tipo como texto
    Ajax.setResponseTypeText(response);
    // marca como sendo HTML
    Ajax.setResponseHeader(response, COMPOSE, type);
  }

  /**
   * Retorna o script HTML contendo a representa��o de t�rmino do Form.
   * @return String Retorna o script HTML contendo a representa��o de t�rmino do Form.
   */
  public String end() {
    // termina o form
    return "</form>"
         +   (useAjax
                ? "<div id=\"" + id + "ajaxTargetContainer\" style=\"display:none;\"></div>"
                : "")
         + "<script type=\"text/javascript\">"
             // p�e o foco no form
         +   "Form_focus(" + id + "id);"
             // controles Ajax
         +   (useAjax 
                ?      // vari�veis locais para requisi��es Ajax
                       "var " + id + "ajaxRequest       = null; "
                   +   "var " + id + "ajaxTarget        = \"" + target + "\"; "
                   +   "var " + id + "ajaxCallbackValid = false; "
                       // fun��o de triangula��o para requisi��es Ajax
                   +   "function " + id + "_ajaxSubmitCallback() { "
                   +     "Form_ajaxSubmitCallback(" + id + "id); "
                   +   "} "
               : "")
         + "</script>";
  }

  /**
   * Retorna a a��o de destino do Form para ser executada pelo Controller.
   * @return String Retorna a a��o de destino do Form para ser executada
   *         pelo Controller.
   */
  public Action getAction() {
    return action;
  }

  /**
   * Retorna o comando para ser executado na a��o de destino.
   * @return String Retorna o comando para ser executado na a��o de destino.
   */
  public Command getCommand() {
    return command;
  }

  /**
   * Retorna o tipo de codifica��o do Form na p�gina.
   * @return String Retorna o tipo de codifica��o do Form na p�gina.
   */
  public String getEncodeType() {
    return encodeType;
  }
  
  /**
   * Retorna se o primeiro controle do Form receber� o foco.
   * @return boolean Retorna se o primeiro controle do Form receber� o foco.
   */
  public boolean getFocusControl() {
    return focusControl;
  }

  /**
   * Retorna a identifica��o do Form na p�gina.
   * @return String Retorna a identifica��o do Form na p�gina.
   */
  public String getId() {
    return id;
  }

  /**
   * Retorna o script JavaScript que ser� executado quando o Form for submetido.
   * Este script deve retornar true para o caso de o Form poder ser submetido
   * ou false em caso contr�rio.
   * @return String Retorna o script JavaScript que ser� executado quando o Form
   *                for submetido. Este script deve retornar true para o caso
   *                de o Form poder ser submetido ou false em caso contr�rio.
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
   * Retorna se o Form ser� submetido atrav�s de Ajax.
   * @return boolean Retorna se o Form ser� submetido atrav�s de Ajax.
   */
  public boolean getUseAjax() {
    return useAjax;
  }

  /**
   * Retorna true se o formul�rio foi recarregado a partir do script retornado
   * pelo m�todo 'submitLastCommandScript()'.
   * @return boolean Retorna true se o formul�rio foi recarregado a partir do
   *                 script retornado pelo m�todo 'submitLastCommandScript()'.
   * @see submitLastCommandScript()
   */
  public boolean reloaded() {
    // par�metro da requisi��o
    String result = request.getParameter(Controller.RELOADED);
    // retorna se fomos recarregados
    return (result != null) && Boolean.valueOf(result).booleanValue();
  }

  /**
   * Define a a��o de destino do Form para ser executada pelo Controller.
   * @param action Action A��o de destino do Form para ser executada pelo Controller.
   */
  public void setAction(Action action) {
    this.action = action;
  }

  /**
   * Define o comando para ser executado na a��o de destino.
   * @param command Command comando para ser executado na a��o de destino.
   */
  public void setCommand(Command command) {
    this.command = command;
  }

  /**
   * Define o tipo de codifica��o do Form na p�gina.
   * @param id String Tipo de codifica��o do Form na p�gina.
   */
  public void setEncodeType(String encodeType) {
    this.encodeType = encodeType;
  }
  
  /**
   * Define se o primeiro controle do Form receber� o foco.
   * @param focusControl boolean True para que o primeiro controle do Form
   *                     receba o foco.
   */
  public void setFocusControl(boolean focusControl) {
    this.focusControl = focusControl;
  }

  /**
   * Define a identifica��o do Form na p�gina.
   * @param id String Identifica��o do Form na p�gina.
   */
  public void setId(String id) {
    this.id = id;
  }

  /**
   * Define o script JavaScript que ser� executado quando o Form for submetido.
   * Este script deve retornar true para o caso de o Form poder ser submetido
   * ou false em caso contr�rio.
   * @param onSubmitScript String Define o script JavaScript que ser� executado
   *                       quando o Form for submetido. Este script deve retornar
   *                       true para o caso de o Form poder ser submetido ou
   *                       false em caso contr�rio.
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
   * Define se o Form ser� submetido atrav�s de Ajax.
   * @param useAjax True para que o Form ser� submetido atrav�s de Ajax.
   */
  public void setUseAjax(boolean useAjax) {
    this.useAjax = useAjax;
  }

  /**
   * Retorna o script JavaScript contendo a chamada para a fun��o de submiss�o
   * do Form alterando o seu comando original para o �ltimo comando executado.
   * <b>Este m�todo deve ser utilizado em casos onde � necess�rio enviar dados
   * ao servidor e continuar com a edi��o do Form.</b>
   * @return String Retorna o script JavaScript contendo a chamada para a fun��o
   *                de submiss�o do Form alterando o seu comando original para o
   *                �ltimo comando executado.
   *                <b>Este m�todo deve ser utilizado em casos onde � necess�rio
   *                enviar dados ao servidor e continuar com a edi��o do Form.</b>
   * @see reloaded()
   */
  public String submitLastCommandScript() {
    return "Form_submitLastCommand(" + id + "id);";
  }

  /**
   * Retorna o script JavaScript contendo a chamada para a fun��o de submiss�o
   * do Form alterando o seu comando original para 'command'.
   * <b>Este m�todo deve ser utilizado em casos onde � necess�rio enviar dados
   * ao servidor com um comando diferente do configurado originalmente no Form.</b>
   * @param command Command Comando para ser executado.
   * @return String Retorna o script JavaScript contendo a chamada para a fun��o
   *                de submiss�o do Form alterando o seu comando original para
   *                'command'.
   *                <b>Este m�todo deve ser utilizado em casos onde � necess�rio
   *                enviar dados ao servidor com um comando diferente do
   *                configurado originalmente no Form.</b>
   */
  public String submitCustomCommandScript(Command command) {
    return submitCustomCommandScript(command, "", true, false);
  }

  /**
   * Retorna o script JavaScript contendo a chamada para a fun��o de submiss�o
   * do Form alterando o seu comando original para 'command'.
   * <b>Este m�todo deve ser utilizado em casos onde � necess�rio enviar dados
   * ao servidor com um comando diferente do configurado originalmente no Form.</b>
   * @param command Command Comando para ser executado.
   * @param avoidRedundantSubmition boolean Evita que o formul�rio seja submetido
   *                                duas ou mais vezes, sobrecarregando o sistema.
   * @return String Retorna o script JavaScript contendo a chamada para a fun��o
   *                de submiss�o do Form alterando o seu comando original para
   *                'command'.
   *                <b>Este m�todo deve ser utilizado em casos onde � necess�rio
   *                enviar dados ao servidor com um comando diferente do
   *                configurado originalmente no Form.</b>
   */
  public String submitCustomCommandScript(Command command,
                                          boolean avoidRedundantSubmition) {
    return submitCustomCommandScript(command, "", avoidRedundantSubmition, false);
  }

  /**
   * Retorna o script JavaScript contendo a chamada para a fun��o de submiss�o
   * do Form alterando o seu comando original para 'command'.
   * <b>Este m�todo deve ser utilizado em casos onde � necess�rio enviar dados
   * ao servidor com um comando diferente do configurado originalmente no Form.</b>
   * @param command Command Comando para ser executado.
   * @param confirmationMessage String Mensagem que ser� exibida ao usu�rio
   *        confirmando a submiss�o do Form.
   * @return String Retorna o script JavaScript contendo a chamada para a fun��o
   *                de submiss�o do Form alterando o seu comando original para
   *                'command'.
   *                <b>Este m�todo deve ser utilizado em casos onde � necess�rio
   *                enviar dados ao servidor com um comando diferente do
   *                configurado originalmente no Form.</b>
   */
  public String submitCustomCommandScript(Command command,
                                          String  confirmationMessage) {
    return submitCustomCommandScript(command, confirmationMessage, true, false);
  }

  /**
   * Retorna o script JavaScript contendo a chamada para a fun��o de submiss�o
   * do Form alterando o seu comando original para 'command'.
   * <b>Este m�todo deve ser utilizado em casos onde � necess�rio enviar dados
   * ao servidor com um comando diferente do configurado originalmente no Form.</b>
   * @param command Command Comando para ser executado.
   * @param confirmationMessage String Mensagem que ser� exibida ao usu�rio
   *        confirmando a submiss�o do Form.
   * @param avoidRedundantSubmition boolean True para evitar que o formul�rio seja
   *                                submetido duas ou mais vezes,
   *                                sobrecarregando o sistema.
   * @param ignoreConstraints boolean True para que o formul�rio seja submetido
   *                                  sem que as constraints dos inputs sejam
   *                                  verificadas.
   * @return String Retorna o script JavaScript contendo a chamada para a fun��o
   *                de submiss�o do Form alterando o seu comando original para
   *                'command'.
   *                <b>Este m�todo deve ser utilizado em casos onde � necess�rio
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
   * Retorna o script JavaScript contendo a chamada para a fun��o de submiss�o
   * do Form.
   * @return String Retorna o script JavaScript contendo a chamada para a fun��o
   *                de submiss�o do Form.
   */
  public String submitScript() {
    return submitScript("", true);
  }

  /**
   * Retorna o script JavaScript contendo a chamada para a fun��o de submiss�o
   * do Form.
   * @param confirmationMessage String Mensagem que ser� exibida ao usu�rio
   *        confirmando a submiss�o do Form.
   * @return String Retorna o script JavaScript contendo a chamada para a fun��o
   *                de submiss�o do Form.
   */
  public String submitScript(String confirmationMessage) {
    return submitScript(confirmationMessage, true);
  }

  /**
   * Retorna o script JavaScript contendo a chamada para a fun��o de submiss�o
   * do Form.
   * @param avoidRedundantSubmition boolean Evita que o formul�rio seja submetido
   *                                duas ou mais vezes, sobrecarregando o sistema.
   * @return String Retorna o script JavaScript contendo a chamada para a fun��o
   *                de submiss�o do Form.
   */
  public String submitScript(boolean avoidRedundantSubmition) {
    return submitScript("", avoidRedundantSubmition);
  }

  /**
   * Retorna o script JavaScript contendo a chamada para a fun��o de submiss�o
   * do Form.
   * @param confirmationMessage String Mensagem que ser� exibida ao usu�rio
   *        confirmando a submiss�o do Form.
   * @param avoidRedundantSubmition boolean True para evitar que o formul�rio seja
   *                                submetido duas ou mais vezes,
   *                                sobrecarregando o sistema.
   * @return String Retorna o script JavaScript contendo a chamada para a fun��o
   *                de submiss�o do Form.
   */
  public String submitScript(String  confirmationMessage,
                             boolean avoidRedundantSubmition) {
    return submitScript(confirmationMessage, avoidRedundantSubmition, false);
  }

  /**
   * Retorna o script JavaScript contendo a chamada para a fun��o de submiss�o
   * do Form.
   * @param confirmationMessage String Mensagem que ser� exibida ao usu�rio
   *        confirmando a submiss�o do Form.
   * @param avoidRedundantSubmition boolean True para evitar que o formul�rio seja
   *                                submetido duas ou mais vezes,
   *                                sobrecarregando o sistema.
   * @param ignoreConstraints boolean True para que o formul�rio seja submetido
   *                                  sem que as constraints dos inputs sejam
   *                                  verificadas.
   * @return String Retorna o script JavaScript contendo a chamada para a fun��o
   *                de submiss�o do Form.
   */
  public String submitScript(String  confirmationMessage,
                             boolean avoidRedundantSubmition,
                             boolean ignoreConstraints) {
    return "Form_submit(" + id + "id, " + (!confirmationMessage.equals("") ? "'" + confirmationMessage + "'" : "null") + ", " + avoidRedundantSubmition + ", " + ignoreConstraints + ", " + (command != null ? "'" + command.getName() + "'" : "''") + ");";
  }

}
