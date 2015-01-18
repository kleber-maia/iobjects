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
package iobjects.util.mail;

import java.util.*;

import iobjects.*;
import iobjects.util.mail.*;
import iobjects.xml.mail.*;
import iobjects.schedule.*;
import iobjects.schedule.Scheduleable.RunStatus;

/**
 * Representa o servi�o de e-mail da aplica��o.
 * @since 3.1
 */
public class MailService {

  /**
   * Classe de teste.
   * Para enviar um e-mail para este objeto o assunto deve conter:
   * @businessObjectTo=iobjects.util.mail.MailService$Test
   */
  static public class Test extends BusinessObject implements Pop3Callback {
    static public String CLASS_NAME = Test.class.getName();
    public boolean onReceiveMessage(Message message) throws Exception {
      MailService.getInstance().replyBusinessObjectMessage(message, "A mensagem a seguir foi recebida corretamente e processada pela classe " + CLASS_NAME + ".\r\n\r\nMENSAGEM ORIGINAL\r\n-----------------\r\n\r\n" + message.getContent());
      return true;
    }
  }

  /**
   * Classe respons�vel pelo recebimento autom�tico de mensagens.
   */
  static public class MailServiceReceiver extends BusinessObject implements Scheduleable, Pop3Callback {

    static public String CLASS_NAME = MailServiceReceiver.class.getName();

    public boolean onReceiveMessage(Message message) throws Exception {
      // por padr�o...apaga a amensagem do servidor
      boolean result = true;
      // obt�m as propriedades da mensagem
      Properties properties = MailService.getInstance().getMessageProperties(message);
      // nome da classe do destinat�rio
      String receiverClassName = properties.getProperty(PROPERTY_BUSINESS_OBJECT_TO);
      // se est� na lista de Business Objects...
      if (MailService.getInstance().businessObjectsClassNames.contains(receiverClassName)) {
        // instancia o objeto
        BusinessObject businessObject = getFacade().getBusinessObject(receiverClassName);
        // se implementa a interface Pop3Callback...chama
        if (businessObject instanceof Pop3Callback) {
          result = ((Pop3Callback)businessObject).onReceiveMessage(message);
        }
        // se n�o implementa Pop3Callback...exce��o
        else {
          // devolve a mensagem ao remetente
          MailService.getInstance().replyBusinessObjectMessage(message, "A mensagem a seguir foi recebida corretamente, mas o objeto de destino (" + receiverClassName + ") n�o � capaz de process�-la.\r\n\r\nMENSAGEM ORIGINAL\r\n-----------------\r\n\r\n" + message.getContent());
        } // if
      }
      // se n�o est� na lista de Business Objects...
      else {
        // devolve a mensagem ao remetente
        MailService.getInstance().replyBusinessObjectMessage(message, "A mensagem a seguir foi recebida corretamente, mas o objeto de destino (" + receiverClassName + ") n�o est� registrado como um poss�vel destinat�rio.\r\n\r\nMENSAGEM ORIGINAL\r\n-----------------\r\n\r\n" + message.getContent());
      } // if
      // retorna
      return result;
    }

    public RunStatus runScheduledTask() throws Exception {
      // se o servi�o POP3 n�o est� ativo...exce��o
      if (!MailService.getInstance().getPop3Active())
        throw new ExtendedException(getClass().getName(), "receiveBusinessObjectMessages", "Servi�o de recebimento de mensagens inativo.");
      // recebe as mensagens
      int length = MailService.getInstance().pop3.receive(this);
      // nosso resultado
      return new RunStatus(true, "Processadas " + length + " mensagens.");
    }

  }

  /**
   * Propriedade adicionada nas mensagens enviadas informando o objeto de neg�cios
   * destinat�rio.
   */
  static public final String PROPERTY_BUSINESS_OBJECT_TO   = "@businessObjectTo";
  /**
   * Propriedade adicionada nas mensagens enviadas informando o objeto de neg�cios
   * remetente.
   */
  static public final String PROPERTY_BUSINESS_OBJECT_FROM = "@businessObjectFrom";
  /**
   * Propriedade adicionada nas mensagens enviadas informando o nome da aplica��o.
   */
  static public final String PROPERTY_APPLICATION_NAME     = "@applicationName";
  /**
   * Propriedade adicionada nas mensagens enviadas informando o Id da aplica��o.
   */
  static public final String PROPERTY_APPLICATION_ID       = "@applicationId";

  static public final String TASK_NAME = "Mail Service";

  static private MailService mailService = null;

  private String          applicationName           = "";
  private String          applicationId             = "";
  private MailServiceFile mailServiceFile           = null;
  private Pop3            pop3                      = null;
  private Smtp            smtp                      = null;
  private Vector          businessObjectsClassNames = new Vector();

  /**
   * Construtor padr�o.
   * @param mailFilePath String Caminho onde se encontra o arquivo de configura��o
   *                     de e-mail.
   */
  private MailService(String mailFilePath) {
    try {
      // adiciona nossa classe de Teste
      addBusinessObjectReceiver(Test.CLASS_NAME);

      // arquivo de configura��o de e-mail
      mailServiceFile = new MailServiceFile(mailFilePath);
      // configura o Pop3
      if (mailServiceFile.pop3().getActive()) {
        pop3 = new Pop3(mailServiceFile.pop3().getHostName(),
                        mailServiceFile.pop3().getPort(),
                        mailServiceFile.pop3().getUserName(),
                        mailServiceFile.pop3().getPassword(),
                        mailServiceFile.pop3().getSSL());
      } // if
      // configura o Smtp
      if (mailServiceFile.smtp().getActive()) {
        smtp = new Smtp(mailServiceFile.smtp().getHostName(),
                        mailServiceFile.smtp().getPort(),
                        mailServiceFile.smtp().getUserName(),
                        mailServiceFile.smtp().getPassword(),
                        mailServiceFile.smtp().getSSL());
      } // if
      // se o Pop3 est� ativo...agenda o recebimento autom�tico
      if (mailServiceFile.pop3().getActive()) {
        // calcula a primeira execu��o
        Calendar calendar = Calendar.getInstance();
        calendar.roll(Calendar.MINUTE, mailServiceFile.pop3().getInterval());
        // nossa tarefa
        Task task = new Task(TASK_NAME, MailServiceReceiver.CLASS_NAME, calendar.getTime(), mailServiceFile.pop3().getInterval() * 1000 * 60);
        // agenda
        Schedule.getInstace().scheduleTask(task);
      } // if
    }
    catch (Exception e) {
      throw new RuntimeException(new ExtendedException(getClass().getName(), "", e));
    } // try-catch
  }

  /**
   * Adiciona o BusinessObject indicado por 'className' na lista de poss�veis
   * destinat�rios de mensagens de e-mail recebidas. <b>O objeto informado deve
   * ser descendente de BusinessObject e implementar a interface Pop3Callback.</b>
   * @param className String Nome da classe que se deseja adicionar.
   */
  public void addBusinessObjectReceiver(String className) {
    businessObjectsClassNames.add(className);
  }

  /**
   * Adiciona a propriedade indicada por 'name' e 'value' a 'message'.
   * @param message Message Mensagem cuja propriedade se deseja adicionar.
   * @param name String Nome da propriedade.
   * @param value String Valor da propriedade.
   */
  private void addMessageProperty(Message message,
                                  String  name,
                                  String  value) {
    // se j� temos propriedades...quebra a linha
    if (!message.getSubject().equals(""))
      message.setSubject(message.getSubject() + "\r\n");
    // adiciona a propriedade
    message.setSubject(message.getSubject() + name + "=" + value);
  }

  /**
   * Retorna a inst�ncia de MailService para a aplica��o.
   * @return MailService Retorna a inst�ncia de MailService para a aplica��o.
   */
  static public MailService getInstance() {
    return mailService;
  }

  /**
   * Retorna a inst�ncia de MailService para a aplica��o.
   * @param mailFilePath String Caminho onde se encontra o arquivo de configura��o
   *                     de e-mail.
   * @param applicationName String Nome da aplica��o.
   * @param applicationId String Id da aplica��o
   * @return MailService Retorna a inst�ncia de MailService para a aplica��o.
   */
  static public MailService getInstance(String mailFilePath,
                                        String applicationName,
                                        String applicationId) {
    if (mailService == null)
      mailService = new MailService(mailFilePath);
    return mailService;
  }

  /**
   * Retorna a lista de propriedades existentes na mensagem informada. A mensagem
   * somente conter� propriedades se a mesma tiver sido enviada pelo pr�prio
   * servi�o de mensagens (MailService).
   * @param message Message Messagem cujas propriedades se deseja retornar.
   * @return Properties Retorna a lista de propriedades existentes na mensagem
   * informada. A mensagem somente conter� propriedades se a mesma tiver sido
   * enviada pelo pr�prio servi�o de mensagens (MailService).
   */
  public Properties getMessageProperties(Message message) {
    // nosso resultado
    Properties result = new Properties();
    // informa��es contidas no assunto
    String[] subjectProperties = message.getSubject().split("\r\n");
    // adiciona as propriedades encontradas
    for (int i=0; i<subjectProperties.length; i++) {
      // propriedade da vez
      String[] subjectPropertyValues = subjectProperties[i].split("=");
      // adiciona
      result.put(subjectPropertyValues[0], (subjectPropertyValues.length > 1 ? subjectPropertyValues[1] : ""));
    } // for
    // retorna
    return result;
  }

  /**
   * Retorna true se o servi�o de recebimento de e-mail estiver ativo.
   * @return boolean Retorna true se o servi�o de recebimento de e-mail estiver ativo.
   */
  public boolean getPop3Active() {
    return mailServiceFile.pop3().getActive();
  }

  /**
   * Retorna as propriedades do servi�o Pop3.
   * @return Retorna as propriedades do servi�o Pop3.
   */
  public MailServiceFile.Pop3 getPop3Properties() {
    return mailServiceFile.pop3();
  }

  /**
   * Retorna true se o servi�o de envio de e-mail estiver ativo.
   * @return boolean Retorna true se o servi�o de envio de e-mail estiver ativo.
   */
  public boolean getSmtpActive() {
    return mailServiceFile.smtp().getActive();
  }

  /**
   * Retorna as propriedades do servi�o Smtp.
   * @return Retorna as propriedades do servi�o Smtp.
   */
  public MailServiceFile.Smtp getSmtpProperties() {
    return mailServiceFile.smtp();
  }

  /**
   * Recebe as mensagens destinadas aos objetos de neg�cio inscritos no servi�o
   * de recebimento autom�tico. <b>As mensagens cujo destinat�rio n�o esteja
   * na lista de poss�veis destinat�rios ser�o devolvidas.</b>
   * @param facade Facade Fachada.
   * @return int Retorna a quantidade de mensagens processadas.
   * @throws Exception Em caso de exce��o no recebimento das mensagens.
   */
  public int receiveBusinessObjectMessages(Facade facade) throws Exception {
    // instancia o recebedor de mensagens
    MailServiceReceiver mailServiceReceiver = (MailServiceReceiver)facade.getBusinessObject(MailServiceReceiver.CLASS_NAME);
    // recebe as mensagens
    return pop3.receive(mailServiceReceiver);
  }

  /**
   * Remove o BusinessObject indicado por 'className' da lista de poss�veis
   * destinat�rios de mensagens de e-mail recebidas.
   * @param className String Nome da classe que se deseja remover.
   */
  public void removeBusinessObjectReceiver(String className) {
    businessObjectsClassNames.remove(className);
  }

  /**
   * Responde a mensagem recebida de um objeto de neg�cios.
   * @param message Message Mensagem recebida que se deseja responder.
   * @param content String Conte�do da mensagem de resposta. Caso se deseje
   *        retornar o conte�do da mensagem recebida este dever� ser adicionado
   *        a este par�metro.
   * @throws Exception Em caso de exce��o no envio da mensagem.
   */
  public void replyBusinessObjectMessage(Message message,
                                         String  content) throws Exception {
    // propriedades da mensagem
    Properties properties = getMessageProperties(message);
    // destinat�rio da mensagem original
    String businessObjectTo = properties.getProperty(PROPERTY_BUSINESS_OBJECT_TO, "");
    // remetente da mensagem original
    String businessObjectFrom = properties.getProperty(PROPERTY_BUSINESS_OBJECT_FROM, "");
    // responde a mensagem, com o objeto destinat�rio original como
    // remetente e o objeto remetente original como destinat�rio
    sendBusinessObjectMessage(message.getFrom()[0],
                              businessObjectFrom,
                              businessObjectTo,
                              content);
  }

  /**
   * Envia uma mensagem para o administrador do sistema.
   * @param subject String Assunto da mensagem.
   * @param content String Conte�do da mensagem.
   * @throws Exception Em caso de exce��o na tentativa de envio da mensagem.
   */
  public void sendAdministrativeMessage(String subject,
                                        String content) throws Exception {
    // se o servi�o SMTP n�o est� ativo...exce��o
    if (!getSmtpActive())
      throw new ExtendedException(getClass().getName(), "sendAdministrativeMessage", "Servi�o de envio de mensagens inativo.");
    // se n�o temos o endere�o do administrador...exce��o
    if (mailServiceFile.smtp().getAdminAddress().equals(""))
      throw new ExtendedException(getClass().getName(), "sendAdministrativeMessage", "Endere�o do administrador desconhecido.");
    // constr�i a mensagem
    Message message = new Message(mailServiceFile.smtp().getFromAddress(),
                                  mailServiceFile.smtp().getAdminAddress(),
                                  subject,
                                  content);
    // envia
    smtp.send(message);
  }

  /**
   * Envia uma mensagem para um objeto de neg�cio residente em outra aplica��o.
   * @param applicationAddress String Endere�o da aplica��o de destino.
   * @param toClassName String Nome da classe do objeto de neg�cio de destino.
   * @param fromClassName String Nome da classe do objeto de neg�cio de origem.
   * @param content String Conte�do da mensagem.
   * @throws Exception Em caso de exce��o na tentativa de envio da mensagem.
   */
  public void sendBusinessObjectMessage(String applicationAddress,
                                        String toClassName,
                                        String fromClassName,
                                        String content) throws Exception {
    // se o servi�o SMTP n�o est� ativo...exce��o
    if (!getSmtpActive())
      throw new ExtendedException(getClass().getName(), "sendBusinessObjectMessage", "Servi�o de envio de mensagens inativo.");
    // constr�i a mensagem
    Message message = new Message(mailServiceFile.smtp().getFromAddress(),
                                  applicationAddress,
                                  "",
                                  content);
    // propriedades da mensagem
    addMessageProperty(message, PROPERTY_BUSINESS_OBJECT_TO,   toClassName);
    addMessageProperty(message, PROPERTY_BUSINESS_OBJECT_FROM, fromClassName);
    // envia
    smtp.send(message);
  }

  /**
   * Envia uma mensagem para o destinat�rio informado.
   * @param to String Endere�o do destinat�rio.
   * @param subject String Assunto da mensagem.
   * @param content String Conte�do da mensagem.
   * @throws Exception Em caso de exce��o na tentativa de envio da mensagem.
   */
  public void sendMessage(String to,
                          String subject,
                          String content) throws Exception {
    // se o servi�o SMTP n�o est� ativo...exce��o
    if (!getSmtpActive())
      throw new ExtendedException(getClass().getName(), "sendMessage", "Servi�o de envio de mensagens inativo.");
    // constr�i a mensagem
    Message message = new Message("\"" + mailServiceFile.smtp().getFromName() + "\"<" + mailServiceFile.smtp().getFromAddress() + ">",
                                  to,
                                  subject,
                                  content);
    // envia
    smtp.send(message);
  }

  /**
   * Envia uma mensagem para o suporte do sistema.
   * @param subject String Assunto da mensagem.
   * @param content String Conte�do da mensagem.
   * @throws Exception Em caso de exce��o na tentativa de envio da mensagem.
   */
  public void sendSupportMessage(String subject,
                                 String content) throws Exception {
    // se o servi�o SMTP n�o est� ativo...exce��o
    if (!getSmtpActive())
      throw new ExtendedException(getClass().getName(), "sendSupportMessage", "Servi�o de envio de mensagens inativo.");
    // se n�o temos o endere�o do administrador...exce��o
    if (mailServiceFile.smtp().getSupportAddress().equals(""))
      throw new ExtendedException(getClass().getName(), "sendSupportMessage", "Endere�o do suporte desconhecido.");
    // constr�i a mensagem
    Message message = new Message(mailServiceFile.smtp().getFromAddress(),
                                  mailServiceFile.smtp().getSupportAddress(),
                                  subject,
                                  content);
    // propriedades da mensagem
    addMessageProperty(message, PROPERTY_APPLICATION_NAME, applicationName);
    addMessageProperty(message, PROPERTY_APPLICATION_ID,   applicationId);
    // envia
    smtp.send(message);
  }

}
