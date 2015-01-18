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
package iobjects.ui;

import iobjects.*;
import iobjects.entity.*;
import iobjects.ui.entity.*;

/**
 * Utilit�rio para cria��o de controles para execu��o de comandos na aplica��o
 * relacionados a Action's, Command's e Form's.
 */
public class CommandControl {

  private CommandControl() {
  }

  /**
   * Retorna o script contendo a representa��o HTML de um bot�o para acesso
   * a a��o especificada.
   * @param facade Facade Fachada.
   * @param action Action A��o de destino.
   * @param image String Imagem do bot�o.
   * @param caption String T�tulo do bot�o.
   * @param description String Descri��o do bot�o.
   * @param disabled boolean True para que o bot�o seja apresentado desabilitado.
   * @return String Retorna o script contendo a representa��o HTML de um bot�o
   *                para acesso a a��o especificada.
   */
  static public String actionButton(Facade  facade,
                                    Action  action,
                                    String  image,
                                    String  caption,
                                    String  description,
                                    boolean disabled) {
    return actionButton(facade,
                        action,
                        new Command("", caption, description),
                        image,
                        disabled);
  }

  /**
   * Retorna o script contendo a representa��o HTML de um bot�o para acesso
   * a a��o e comando especificados.
   * @param facade Facade Fachada.
   * @param action Action A��o de destino.
   * @param command Command Comando para ser executado.
   * @param image String Imagem do bot�o.
   * @param disabled boolean True para que o bot�o seja apresentado desabilitado.
   * @return String Retorna o script contendo a representa��o HTML de um bot�o
   *                para acesso a a��o e comando especificados.
   */
  static public String actionButton(Facade  facade,
                                    Action  action,
                                    Command command,
                                    String  image,
                                    boolean disabled) {
    return Button.script(facade,
                         "actionButton" + action.getName(),
                         command != null ? command.getCaption() : action.getCaption(),
                         command != null ? command.getDescription() : action.getDescription(),
                         image,
                         "",
                         Button.KIND_DEFAULT,
                         "",
                         "showWaitCursor();" + (command != null ? "window.location.href='" + action.url(command) : action.url()) + "';",
                         disabled || !facade.getLoggedUser().hasAccessRight(action, command));
  }

  /**
   * Retorna o script contendo a representa��o HTML de um bot�o para execu��o
   * de 'customScript'.
   * @param facade Facade Fachada.
   * @param action Action A��o para verifica��o de direito de acesso.
   * @param command Command Comando para verifica��o de direito de acesso.
   * @param image String Imagem do bot�o.
   * @param onClickScript String Script JavaScript para ser executado quando o
   *                      Hyperlink receber um clique.
   * @param disabled boolean True para que o bot�o seja apresentado desabilitado.
   * @return String Retorna o script contendo a representa��o HTML de um bot�o
   *                para execu��o de 'customScript'.
   */
  static public String actionCustomScriptButton(Facade  facade,
                                                Action  action,
                                                Command command,
                                                String  image,
                                                String  onClickScript,
                                                boolean disabled) {
    return Button.script(facade,
                         "actionButton" + action.getName(),
                         command != null ? command.getCaption() : action.getCaption(),
                         command != null ? command.getDescription() : action.getDescription(),
                         image,
                         "",
                         Button.KIND_DEFAULT,
                         "",
                         onClickScript,
                         disabled || !facade.getLoggedUser().hasAccessRight(action, command));
  }

  /**
   * Retorna o script contendo a representa��o HTML de um hyperlink para execu��o
   * de 'customScript'.
   * @param facade Facade Fachada.
   * @param action Action A��o de destino.
   * @param command Command Comando para ser executado.
   * @param image String Imagem do bot�o.
   * @param href String Refer�ncia para ser chamada pelo hyperlink.
   * @param target String Frame de destino da refer�ncia.
   * @param onClickScript String Script JavaScript para ser executado quando o
   *                      Hyperlink receber um clique.
   * @param disabled boolean True para que o hyperlink seja apresentado desabilitado.
   * @return String Retorna o script contendo a representa��o HTML de um hyperlink
   *                para execu��o de 'customScript'.
   */
  static public String actionCustomScriptHyperlink(Facade  facade,
                                                   Action  action,
                                                   Command command,
                                                   String  image,
                                                   String  href,
                                                   String  target,
                                                   String  onClickScript,
                                                   boolean disabled) {
    return Hyperlink.script(facade,
                            "actionHyperlink" + action.getName(),
                            command != null ? command.getCaption() : action.getCaption(),
                            command != null ? command.getDescription() : action.getDescription(),
                            image,
                            href,
                            target,
                            onClickScript,
                            disabled || !facade.getLoggedUser().hasAccessRight(action, command));
  }

  /**
   * Retorna o script contendo a representa��o HTML de um hyperlink para acesso
   * a a��o especificada.
   * @param facade Facade Fachada.
   * @param action Action A��o de destino.
   * @param image String Imagem do bot�o.
   * @param caption String T�tulo do bot�o.
   * @param description String Descri��o do bot�o.
   * @param disabled boolean True para que o hyperlink seja apresentado desabilitado.
   * @return String Retorna o script contendo a representa��o HTML de um hyperlink
   *                para acesso a a��o especificada.
   */
  static public String actionHyperlink(Facade  facade,
                                       Action  action,
                                       String  image,
                                       String  caption,
                                       String  description,
                                       boolean disabled) {
    return actionHyperlink(facade,
                           action,
                           new Command("", caption, description),
                           image,
                           disabled);
  }

  /**
   * Retorna o script contendo a representa��o HTML de um hyperlink para acesso
   * a a��o e comando especificados.
   * @param facade Facade Fachada.
   * @param action Action A��o de destino.
   * @param command Command Comando para ser executado.
   * @param image String Imagem do hyperlink.
   * @param disabled boolean True para que o hyperlink seja apresentado desabilitado.
   * @return String Retorna o script contendo a representa��o HTML de um hyperlink
   *                para acesso a a��o e comando especificados.
   */
  static public String actionHyperlink(Facade  facade,
                                       Action  action,
                                       Command command,
                                       String  image,
                                       boolean disabled) {
    return Hyperlink.script(facade,
                            "actionHyperlink" + action.getName(),
                            command != null ? command.getCaption() : action.getCaption(),
                            command != null ? command.getDescription() : action.getDescription(),
                            image,
                            command != null ? action.url(command) : action.url(),
                            "",
                            action.getShowType() == Action.SHOW_TYPE_EMBEDED ? "showWaitCursor();" : "",
                            disabled || !facade.getLoggedUser().hasAccessRight(action, command));
  }

  /**
   * Retorna o script contendo a representa��o HTML de um bot�o para envio por
   * e-mail da a��o especificada.
   * @param facade Facade Fachada.
   * @param action Action A��o de destino.
   * @param command Command Comando para ser executado.
   * @param params String Par�metros do tipo 'param1=value1&param2=value2' para
   *        serem adicionados a URL.
   * @param image String Imagem do bot�o.
   * @param disabled boolean True para que o bot�o seja apresentado desabilitado.
   * @return String Retorna o script contendo a representa��o HTML de um bot�o
   *                para envio por e-mail da a��o especificada.
   * @throws Exception Em caso de exce��o na tentativa de codificar a URL
   *                   de envio de e-mail.
   */
  static public String actionSendMailButton(Facade  facade,
                                            Action  action,
                                            Command command,
                                            String  params,
                                            String  image,
                                            boolean disabled) throws Exception {
    return actionSendMailButton(facade,
                                action,
                                command,
                                params,
                                image,
                                "E-mail",
                                "Envia " + action.getCaption() + " por e-mail.",
                                disabled);
  }

  /**
   * Retorna o script contendo a representa��o HTML de um bot�o para envio por
   * e-mail da a��o especificada.
   * @param facade Facade Fachada.
   * @param action Action A��o de destino.
   * @param command Command Comando para ser executado.
   * @param params String Par�metros do tipo 'param1=value1&param2=value2' para
   *        serem adicionados a URL.
   * @param image String Imagem do bot�o.
   * @param caption String T�tulo do bot�o.
   * @param description String Descri��o do bot�o.
   * @param disabled boolean True para que o bot�o seja apresentado desabilitado.
   * @return String Retorna o script contendo a representa��o HTML de um bot�o
   *                para envio por e-mail da a��o especificada.
   * @throws Exception Em caso de exce��o na tentativa de codificar a URL
   *                   de envio de e-mail.
   */
  static public String actionSendMailButton(Facade  facade,
                                            Action  action,
                                            Command command,
                                            String  params,
                                            String  image,
                                            String  caption,
                                            String  description,
                                            boolean disabled) throws Exception {
    return Button.script(facade,
                         "actionEmailButton" + action.getName(),
                         caption,
                         description,
                         image,
                         "",
                         Button.KIND_DEFAULT,
                         "",
                         action.sendMailURL(command, params),
                         disabled || !facade.getLoggedUser().hasAccessRight(action, command));
  }

  /**
   * Retorna o script contendo a representa��o HTML de um hyperlink para envio
   * por e-mail da a��o especificada.
   * @param facade Facade Fachada.
   * @param action Action A��o de destino.
   * @param command Command Comando para ser executado.
   * @param params String Par�metros do tipo 'param1=value1&param2=value2' para
   *        serem adicionados a URL.
   * @param image String Imagem do hyperlink.
   * @param disabled boolean True para que o bot�o seja apresentado desabilitado.
   * @return String Retorna o script contendo a representa��o HTML de um hyperlink
   *                para envio por e-mail da a��o especificada.
   * @throws Exception Em caso de exce��o na tentativa de codificar a URL
   *                   de envio de e-mail.
   */
  static public String actionSendMailHyperlink(Facade  facade,
                                               Action  action,
                                               Command command,
                                               String  params,
                                               String  image,
                                               boolean disabled) throws Exception {
    return actionSendMailHyperlink(facade,
                                   action,
                                   command,
                                   params,
                                   image,
                                   "E-mail",
                                   "Envia " + action.getCaption() + " por e-mail.",
                                   disabled);
  }

  /**
   * Retorna o script contendo a representa��o HTML de um hyperlink para envio
   * por e-mail da a��o especificada.
   * @param facade Facade Fachada.
   * @param action Action A��o de destino.
   * @param command Command Comando para ser executado.
   * @param params String Par�metros do tipo 'param1=value1&param2=value2' para
   *        serem adicionados a URL.
   * @param image String Imagem do hyperlink.
   * @param caption String T�tulo do hyperlink.
   * @param description String Descri��o do hyperlink.
   * @param disabled boolean True para que o hyperlink seja apresentado desabilitado.
   * @return String Retorna o script contendo a representa��o HTML de um hyperlink
   *                para envio por e-mail da a��o especificada.
   * @throws Exception Em caso de exce��o na tentativa de codificar a URL
   *                   de envio de e-mail.
   */
  static public String actionSendMailHyperlink(Facade  facade,
                                               Action  action,
                                               Command command,
                                               String  params,
                                               String  image,
                                               String  caption,
                                               String  description,
                                               boolean disabled) throws Exception {
    return Hyperlink.script(facade,
                            "actionEmailHyperlink" + action.getName(),
                            caption,
                            description,
                            image,
                            action.sendMailURL(command, params),
                            "",
                            "",
                            disabled || !facade.getLoggedUser().hasAccessRight(action, command));
  }

  /**
   * Retorna o script contendo a representa��o HTML de um bot�o para encaminhar
   * o usu�rio para a p�gina de consulta da entidade.
   * @param facade Facade Fachada.
   * @param entity Entity principal da consulta.
   * @param action Action que representa a p�gina da consulta.
   * @param image String Imagem do bot�o.
   * @param disabled boolean True para que o bot�o seja apresentado desabilitado.
   * @return Retorna o script contendo a representa��o HTML de um bot�o para encaminhar
   * o usu�rio para a p�gina de consulta da entidade.
   */
  static public String entityBrowseButton(Facade  facade,
                                          Entity  entity,
                                          Action  action,
                                          String  image,
                                          boolean disabled) {
    return entityBrowseButton(facade,
                              entity,
                              action,
                              image,
                              action.getCaption(),
                              action.getDescription(),
                              disabled);
  }

  /**
   * Retorna o script contendo a representa��o HTML de um bot�o para encaminhar
   * o usu�rio para a p�gina de consulta da entidade.
   * @param facade Facade Fachada.
   * @param entity Entity principal da consulta.
   * @param action Action que representa a p�gina da consulta.
   * @param image String Imagem do bot�o.
   * @param caption String T�tulo do bot�o.
   * @param description String Descri��o do bot�o.
   * @param disabled boolean True para que o bot�o seja apresentado desabilitado.
   * @return Retorna o script contendo a representa��o HTML de um bot�o para encaminhar
   * o usu�rio para a p�gina de consulta da entidade.
   */
  static public String entityBrowseButton(Facade  facade,
                                          Entity  entity,
                                          Action  action,
                                          String  image,
                                          String  caption,
                                          String  description,
                                          boolean disabled) {
    return Button.script(facade, 
                         "entityBrowseButton" + action.getName(),
                         caption,
                         description,
                         image,
                         "",
                         Button.KIND_DEFAULT,
                         "",
                         EntityGrid.forwardBrowse(facade, entity, action),
                         disabled);
  }

  /**
   * Retorna o script contendo a representa��o HTML de um hyperlink para encaminhar
   * o usu�rio para a p�gina de consulta da entidade.
   * @param facade Facade Fachada.
   * @param entity Entity principal da consulta.
   * @param action Action que representa a p�gina da consulta.
   * @param image String Imagem do hyperlink.
   * @param disabled boolean True para que o hyperlink seja apresentado desabilitado.
   * @return Retorna o script contendo a representa��o HTML de um hyperlink para encaminhar
   * o usu�rio para a p�gina de consulta da entidade.
   */
  static public String entityBrowseHyperlink(Facade  facade,
                                             Entity  entity,
                                             Action  action,
                                             String  image,
                                             boolean disabled) {
    return entityBrowseHyperlink(facade,
                                 entity,
                                 action,
                                 image,
                                 action.getCaption(),
                                 action.getDeclaringClassName(),
                                 disabled);
  }

  /**
   * Retorna o script contendo a representa��o HTML de um hyperlink para encaminhar
   * o usu�rio para a p�gina de consulta da entidade.
   * @param facade Facade Fachada.
   * @param entity Entity principal da consulta.
   * @param action Action que representa a p�gina da consulta.
   * @param image String Imagem do hyperlink.
   * @param caption String T�tulo do hyperlink.
   * @param description String Descri��o do hyperlink.
   * @param disabled boolean True para que o hyperlink seja apresentado desabilitado.
   * @return Retorna o script contendo a representa��o HTML de um hyperlink para encaminhar
   * o usu�rio para a p�gina de consulta da entidade.
   */
  static public String entityBrowseHyperlink(Facade  facade,
                                             Entity  entity,
                                             Action  action,
                                             String  image,
                                             String  caption,
                                             String  description,
                                             boolean disabled) {
    return Hyperlink.script(facade,
                            "entityBroserHyperlink" + action.getName(),
                            caption,
                            description,
                            image,
                            "javascript:void(0);",
                            "",
                            EntityGrid.forwardBrowse(facade, entity, action),
                            disabled);
  }

  /**
   * Retorna o script contendo a representa��o HTML de um bot�o para encaminhar
   * o usu�rio para a p�gina de edi��o da entidade.
   * @param facade Facade Fachada.
   * @param entity Entity principal da consulta.
   * @param action Action que representa a p�gina da consulta.
   * @param command Command que representa o comando a ser executado.
   * @param image String Imagem do bot�o.
   * @param disabled boolean True para que o bot�o seja apresentado desabilitado.
   * @return Retorna o script contendo a representa��o HTML de um bot�o para encaminhar
   * o usu�rio para a p�gina de edi��o da entidade.
   */
  static public String entityFormButton(Facade  facade,
                                        Entity  entity,
                                        Action  action,
                                        Command command,
                                        String  image,
                                        boolean disabled) {
    return entityFormButton(facade,
                            entity,
                            action,
                            command,
                            image,
                            (command != null ? command.getCaption() : action.getCaption()),
                            (command != null ? command.getDescription() : action.getDescription()),
                            disabled);
  }

  /**
   * Retorna o script contendo a representa��o HTML de um bot�o para encaminhar
   * o usu�rio para a p�gina de edi��o da entidade.
   * @param facade Facade Fachada.
   * @param entity Entity principal da consulta.
   * @param action Action que representa a p�gina da consulta.
   * @param command Command que representa o comando a ser executado.
   * @param image String Imagem do bot�o.
   * @param caption String T�tulo do bot�o.
   * @param description String Descri��o do bot�o.
   * @param disabled boolean True para que o bot�o seja apresentado desabilitado.
   * @return Retorna o script contendo a representa��o HTML de um bot�o para encaminhar
   * o usu�rio para a p�gina de edi��o da entidade.
   */
  static public String entityFormButton(Facade  facade,
                                        Entity  entity,
                                        Action  action,
                                        Command command,
                                        String  image,
                                        String  caption,
                                        String  description,
                                        boolean disabled) {
    return Button.script(facade,
                         "entityFormButton" + action.getName(),
                         caption,
                         description,
                         image,
                         "",
                         Button.KIND_DEFAULT,
                         "",
                         EntityGrid.forwardForm(facade, entity, action, command),
                         disabled);
  }

  /**
   * Retorna o script contendo a representa��o HTML de um hyperlink para encaminhar
   * o usu�rio para a p�gina de edi��o da entidade.
   * @param facade Facade Fachada.
   * @param entity Entity principal da consulta.
   * @param action Action que representa a p�gina da consulta.
   * @param command Command que representa o comando a ser executado.
   * @param image String Imagem do hyperlink.
   * @param disabled boolean True para que o hyperlink seja apresentado desabilitado.
   * @return Retorna o script contendo a representa��o HTML de um hyperlink para encaminhar
   * o usu�rio para a p�gina de edi��o da entidade.
   */
  static public String entityFormHyperlink(Facade  facade,
                                           Entity  entity,
                                           Action  action,
                                           Command command,
                                           String  image,
                                           boolean disabled) {
    return entityFormHyperlink(facade,
                               entity,
                               action,
                               command,
                               image,
                               (command != null ? command.getCaption() : action.getCaption()),
                               (command != null ? command.getDescription() : action.getDescription()),
                               disabled);
  }

  /**
   * Retorna o script contendo a representa��o HTML de um hyperlink para encaminhar
   * o usu�rio para a p�gina de edi��o da entidade.
   * @param facade Facade Fachada.
   * @param entity Entity principal da consulta.
   * @param action Action que representa a p�gina da consulta.
   * @param command Command que representa o comando a ser executado.
   * @param image String Imagem do hyperlink.
   * @param caption String T�tulo do hyperlink.
   * @param description String Descri��o do hyperlink.
   * @param disabled boolean True para que o hyperlink seja apresentado desabilitado.
   * @return Retorna o script contendo a representa��o HTML de um hyperlink para encaminhar
   * o usu�rio para a p�gina de edi��o da entidade.
   */
  static public String entityFormHyperlink(Facade  facade,
                                           Entity  entity,
                                           Action  action,
                                           Command command,
                                           String  image,
                                           String  caption,
                                           String  description,
                                           boolean disabled) {
    return Hyperlink.script(facade,
                            "entityFormHyperlink" + action.getName(),
                            caption,
                            description,
                            image,
                            "javascript:void(0);",
                            "",
                            EntityGrid.forwardForm(facade, entity, action, command),
                            disabled);
  }

  /**
   * Retorna o script contendo a representa��o HTML de um bot�o para submiss�o
   * do formul�rio especificado.
   * @param facade Facade Fachada.
   * @param form Form Formul�rio para ser submetido.
   * @param image String Imagem do bot�o.
   * @param disabled boolean True para que o bot�o seja apresentado desabilitado.
   * @return String Retorna o script contendo a representa��o HTML de um bot�o
   *                para submiss�o do formul�rio especificado.
   */
  static public String formButton(Facade  facade,
                                  Form    form,
                                  String  image,
                                  boolean disabled) {
    return formButton(facade, form, image, "", true, disabled);
  }

  /**
   * Retorna o script contendo a representa��o HTML de um bot�o para submiss�o
   * do formul�rio especificado.
   * @param facade Facade Fachada.
   * @param form Form Formul�rio para ser submetido.
   * @param image String Imagem do bot�o.
   * @param confirmationMessage String Mensagem que ser� exibida ao usu�rio
   *                            confirmando a submiss�o do Form.
   * @param avoidRedundantSubmition boolean Evita que o formul�rio seja submetido
   *                                duas ou mais vezes, sobrecarregando o sistema.
   * @param disabled boolean True para que o bot�o seja apresentado desabilitado.
   * @return String Retorna o script contendo a representa��o HTML de um bot�o
   *                para submiss�o do formul�rio especificado.
   */
  static public String formButton(Facade  facade,
                                  Form    form,
                                  String  image,
                                  String  confirmationMessage,
                                  boolean avoidRedundantSubmition,
                                  boolean disabled) {
    return Button.script(facade,
                         "actionButton" + form.getAction().getName(),
                         form.getCommand() != null ? form.getCommand().getCaption() : form.getAction().getCaption(),
                         form.getCommand() != null ? form.getCommand().getDescription() : form.getAction().getDescription(),
                         image,
                         "",
                         Button.KIND_DEFAULT,
                         "",
                         (avoidRedundantSubmition && !form.getUseAjax() ? "showWaitCursor();" : "") + form.submitScript(confirmationMessage, avoidRedundantSubmition),
                         disabled || !facade.getLoggedUser().hasAccessRight(form.getAction(), form.getCommand()));
  }

  /**
   * Retorna o script contendo a representa��o HTML de um bot�o para submiss�o
   * do formul�rio especificado.
   * @param facade Facade Fachada.
   * @param form Form Formul�rio para ser submetido.
   * @param command Command Comando para ser executado.
   * @param image String Imagem do bot�o.
   * @param disabled boolean True para que o bot�o seja apresentado desabilitado.
   * @return String Retorna o script contendo a representa��o HTML de um bot�o
   *                para submiss�o do formul�rio especificado.
   * @since 2006
   */
  static public String formButton(Facade  facade,
                                  Form    form,
                                  Command command,
                                  String  image,
                                  boolean disabled) {
    return formButton(facade, form, command, image, "", true, false, disabled);
  }

  /**
   * Retorna o script contendo a representa��o HTML de um bot�o para submiss�o
   * do formul�rio especificado.
   * @param facade Facade Fachada.
   * @param form Form Formul�rio para ser submetido.
   * @param command Command Comando para ser executado.
   * @param image String Imagem do bot�o.
   * @param confirmationMessage String Mensagem que ser� exibida ao usu�rio
   *                            confirmando a submiss�o do Form.
   * @param avoidRedundantSubmition boolean True para evitar que o formul�rio seja
   *                                submetido duas ou mais vezes,
   *                                sobrecarregando o sistema.
   * @param ignoreConstraints boolean True para que o formul�rio seja submetido
   *                                  sem que as constraints dos inputs sejam
   *                                  verificadas.
   * @param disabled boolean True para que o bot�o seja apresentado desabilitado.
   * @return String Retorna o script contendo a representa��o HTML de um bot�o
   *                para submiss�o do formul�rio especificado.
   * @since 2006
   */
  static public String formButton(Facade  facade,
                                  Form    form,
                                  Command command,
                                  String  image,
                                  String  confirmationMessage,
                                  boolean avoidRedundantSubmition,
                                  boolean ignoreConstraints,
                                  boolean disabled) {
    return Button.script(facade,
                         "actionButton" + form.getAction().getName(),
                         command.getCaption(),
                         command.getDescription(),
                         image,
                         "",
                         Button.KIND_DEFAULT,
                         "",
                         (avoidRedundantSubmition && !form.getUseAjax() ? "showWaitCursor();" : "") + form.submitCustomCommandScript(command, confirmationMessage, avoidRedundantSubmition, ignoreConstraints),
                         disabled || !facade.getLoggedUser().hasAccessRight(form.getAction(), command));
  }

  /**
   * Retorna o script contendo a representa��o HTML de um hyperlink para submiss�o
   * do formul�rio especificado.
   * @param facade Facade Fachada.
   * @param form Form Formul�rio para ser submetido.
   * @param image String Imagem do bot�o.
   * @param disabled boolean True para que o hyperlink seja apresentado desabilitado.
   * @return String Retorna o script contendo a representa��o HTML de um hyperlink
   *                para submiss�o do formul�rio especificado.
   */
  static public String formHyperlink(Facade  facade,
                                     Form    form,
                                     String  image,
                                     boolean disabled) {
    return formHyperlink(facade, form, image, "", true, disabled);
  }

  /**
   * Retorna o script contendo a representa��o HTML de um hyperlink para submiss�o
   * do formul�rio especificado.
   * @param facade Facade Fachada.
   * @param form Form Formul�rio para ser submetido.
   * @param image String Imagem do bot�o.
   * @param confirmationMessage String Mensagem que ser� exibida ao usu�rio
   *                            confirmando a submiss�o do Form.
   * @param avoidRedundantSubmition boolean Evita que o formul�rio seja submetido
   *                                duas ou mais vezes, sobrecarregando o sistema.
   * @param disabled boolean True para que o hyperlink seja apresentado desabilitado.
   * @return String Retorna o script contendo a representa��o HTML de um hyperlink
   *                para submiss�o do formul�rio especificado.
   */
  static public String formHyperlink(Facade  facade,
                                     Form    form,
                                     String  image,
                                     String  confirmationMessage,
                                     boolean avoidRedundantSubmition,
                                     boolean disabled) {
    return Hyperlink.script(facade,
                            "actionButton" + form.getAction().getName(),
                            form.getCommand() != null ? form.getCommand().getCaption() : form.getAction().getCaption(),
                            form.getCommand() != null ? form.getCommand().getDescription() : form.getAction().getDescription(),
                            image,
                            "javascript:" + (avoidRedundantSubmition && !form.getUseAjax() ? "showWaitCursor();" : "") + form.submitScript(confirmationMessage, avoidRedundantSubmition) + ";",
                            "",
                            "",
                            disabled || !facade.getLoggedUser().hasAccessRight(form.getAction(), form.getCommand()));
  }

  /**
   * Retorna o script contendo a representa��o HTML de um hyperlink para submiss�o
   * do formul�rio especificado.
   * @param facade Facade Fachada.
   * @param form Form Formul�rio para ser submetido.
   * @param command Command Comando para ser executado.
   * @param image String Imagem do bot�o.
   * @param disabled boolean True para que o hyperlink seja apresentado desabilitado.
   * @return String Retorna o script contendo a representa��o HTML de um hyperlink
   *                para submiss�o do formul�rio especificado.
   * @since 2006
   */
  static public String formHyperlink(Facade  facade,
                                     Form    form,
                                     Command command,
                                     String  image,
                                     boolean disabled) {
    return formHyperlink(facade, form, command, image, "", true, false, disabled);
  }

  /**
   * Retorna o script contendo a representa��o HTML de um hyperlink para submiss�o
   * do formul�rio especificado.
   * @param facade Facade Fachada.
   * @param form Form Formul�rio para ser submetido.
   * @param command Command Comando para ser executado.
   * @param image String Imagem do bot�o.
   * @param confirmationMessage String Mensagem que ser� exibida ao usu�rio
   *                            confirmando a submiss�o do Form.
   * @param avoidRedundantSubmition boolean True para evitar que o formul�rio seja
   *                                submetido duas ou mais vezes,
   *                                sobrecarregando o sistema.
   * @param ignoreConstraints boolean True para que o formul�rio seja submetido
   *                                  sem que as constraints dos inputs sejam
   *                                  verificadas.
   * @param disabled boolean True para que o hyperlink seja apresentado desabilitado.
   * @return String Retorna o script contendo a representa��o HTML de um hyperlink
   *                para submiss�o do formul�rio especificado.
   * @since 2006
   */
  static public String formHyperlink(Facade  facade,
                                     Form    form,
                                     Command command,
                                     String  image,
                                     String  confirmationMessage,
                                     boolean avoidRedundantSubmition,
                                     boolean ignoreConstraints,
                                     boolean disabled) {
    return Hyperlink.script(facade,
                            "actionButton" + form.getAction().getName(),
                            command.getCaption(),
                            command.getDescription(),
                            image,
                            "javascript:" + (avoidRedundantSubmition && !form.getUseAjax() ? "showWaitCursor();" : "") + form.submitCustomCommandScript(command, confirmationMessage, avoidRedundantSubmition, ignoreConstraints) + ";",
                            "",
                            "",
                            disabled || !facade.getLoggedUser().hasAccessRight(form.getAction(), command));
  }

}
