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
 * Representa o serviço de e-mail da aplicação.
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
   * Classe responsável pelo recebimento automático de mensagens.
   */
  static public class MailServiceReceiver extends BusinessObject implements Scheduleable, Pop3Callback {

    static public String CLASS_NAME = MailServiceReceiver.class.getName();

    public boolean onReceiveMessage(Message message) throws Exception {
      // por padrão...apaga a amensagem do servidor
      boolean result = true;
      // obtém as propriedades da mensagem
      Properties properties = MailService.getInstance().getMessageProperties(message);
      // nome da classe do destinatário
      String receiverClassName = properties.getProperty(PROPERTY_BUSINESS_OBJECT_TO);
      // se está na lista de Business Objects...
      if (MailService.getInstance().businessObjectsClassNames.contains(receiverClassName)) {
        // instancia o objeto
        BusinessObject businessObject = getFacade().getBusinessObject(receiverClassName);
        // se implementa a interface Pop3Callback...chama
        if (businessObject instanceof Pop3Callback) {
          result = ((Pop3Callback)businessObject).onReceiveMessage(message);
        }
        // se não implementa Pop3Callback...exceção
        else {
          // devolve a mensagem ao remetente
          MailService.getInstance().replyBusinessObjectMessage(message, "A mensagem a seguir foi recebida corretamente, mas o objeto de destino (" + receiverClassName + ") não é capaz de processá-la.\r\n\r\nMENSAGEM ORIGINAL\r\n-----------------\r\n\r\n" + message.getContent());
        } // if
      }
      // se não está na lista de Business Objects...
      else {
        // devolve a mensagem ao remetente
        MailService.getInstance().replyBusinessObjectMessage(message, "A mensagem a seguir foi recebida corretamente, mas o objeto de destino (" + receiverClassName + ") não está registrado como um possível destinatário.\r\n\r\nMENSAGEM ORIGINAL\r\n-----------------\r\n\r\n" + message.getContent());
      } // if
      // retorna
      return result;
    }

    public RunStatus runScheduledTask() throws Exception {
      // se o serviço POP3 não está ativo...exceção
      if (!MailService.getInstance().getPop3Active())
        throw new ExtendedException(getClass().getName(), "receiveBusinessObjectMessages", "Serviço de recebimento de mensagens inativo.");
      // recebe as mensagens
      int length = MailService.getInstance().pop3.receive(this);
      // nosso resultado
      return new RunStatus(true, "Processadas " + length + " mensagens.");
    }

  }

  /**
   * Propriedade adicionada nas mensagens enviadas informando o objeto de negócios
   * destinatário.
   */
  static public final String PROPERTY_BUSINESS_OBJECT_TO   = "@businessObjectTo";
  /**
   * Propriedade adicionada nas mensagens enviadas informando o objeto de negócios
   * remetente.
   */
  static public final String PROPERTY_BUSINESS_OBJECT_FROM = "@businessObjectFrom";
  /**
   * Propriedade adicionada nas mensagens enviadas informando o nome da aplicação.
   */
  static public final String PROPERTY_APPLICATION_NAME     = "@applicationName";
  /**
   * Propriedade adicionada nas mensagens enviadas informando o Id da aplicação.
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
   * Construtor padrão.
   * @param mailFilePath String Caminho onde se encontra o arquivo de configuração
   *                     de e-mail.
   */
  private MailService(String mailFilePath) {
    try {
      // adiciona nossa classe de Teste
      addBusinessObjectReceiver(Test.CLASS_NAME);

      // arquivo de configuração de e-mail
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
      // se o Pop3 está ativo...agenda o recebimento automático
      if (mailServiceFile.pop3().getActive()) {
        // calcula a primeira execução
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
   * Adiciona o BusinessObject indicado por 'className' na lista de possíveis
   * destinatários de mensagens de e-mail recebidas. <b>O objeto informado deve
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
    // se já temos propriedades...quebra a linha
    if (!message.getSubject().equals(""))
      message.setSubject(message.getSubject() + "\r\n");
    // adiciona a propriedade
    message.setSubject(message.getSubject() + name + "=" + value);
  }

  /**
   * Retorna a instância de MailService para a aplicação.
   * @return MailService Retorna a instância de MailService para a aplicação.
   */
  static public MailService getInstance() {
    return mailService;
  }

  /**
   * Retorna a instância de MailService para a aplicação.
   * @param mailFilePath String Caminho onde se encontra o arquivo de configuração
   *                     de e-mail.
   * @param applicationName String Nome da aplicação.
   * @param applicationId String Id da aplicação
   * @return MailService Retorna a instância de MailService para a aplicação.
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
   * somente conterá propriedades se a mesma tiver sido enviada pelo próprio
   * serviço de mensagens (MailService).
   * @param message Message Messagem cujas propriedades se deseja retornar.
   * @return Properties Retorna a lista de propriedades existentes na mensagem
   * informada. A mensagem somente conterá propriedades se a mesma tiver sido
   * enviada pelo próprio serviço de mensagens (MailService).
   */
  public Properties getMessageProperties(Message message) {
    // nosso resultado
    Properties result = new Properties();
    // informações contidas no assunto
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
   * Retorna true se o serviço de recebimento de e-mail estiver ativo.
   * @return boolean Retorna true se o serviço de recebimento de e-mail estiver ativo.
   */
  public boolean getPop3Active() {
    return mailServiceFile.pop3().getActive();
  }

  /**
   * Retorna as propriedades do serviço Pop3.
   * @return Retorna as propriedades do serviço Pop3.
   */
  public MailServiceFile.Pop3 getPop3Properties() {
    return mailServiceFile.pop3();
  }

  /**
   * Retorna true se o serviço de envio de e-mail estiver ativo.
   * @return boolean Retorna true se o serviço de envio de e-mail estiver ativo.
   */
  public boolean getSmtpActive() {
    return mailServiceFile.smtp().getActive();
  }

  /**
   * Retorna as propriedades do serviço Smtp.
   * @return Retorna as propriedades do serviço Smtp.
   */
  public MailServiceFile.Smtp getSmtpProperties() {
    return mailServiceFile.smtp();
  }

  /**
   * Recebe as mensagens destinadas aos objetos de negócio inscritos no serviço
   * de recebimento automático. <b>As mensagens cujo destinatário não esteja
   * na lista de possíveis destinatários serão devolvidas.</b>
   * @param facade Facade Fachada.
   * @return int Retorna a quantidade de mensagens processadas.
   * @throws Exception Em caso de exceção no recebimento das mensagens.
   */
  public int receiveBusinessObjectMessages(Facade facade) throws Exception {
    // instancia o recebedor de mensagens
    MailServiceReceiver mailServiceReceiver = (MailServiceReceiver)facade.getBusinessObject(MailServiceReceiver.CLASS_NAME);
    // recebe as mensagens
    return pop3.receive(mailServiceReceiver);
  }

  /**
   * Remove o BusinessObject indicado por 'className' da lista de possíveis
   * destinatários de mensagens de e-mail recebidas.
   * @param className String Nome da classe que se deseja remover.
   */
  public void removeBusinessObjectReceiver(String className) {
    businessObjectsClassNames.remove(className);
  }

  /**
   * Responde a mensagem recebida de um objeto de negócios.
   * @param message Message Mensagem recebida que se deseja responder.
   * @param content String Conteúdo da mensagem de resposta. Caso se deseje
   *        retornar o conteúdo da mensagem recebida este deverá ser adicionado
   *        a este parâmetro.
   * @throws Exception Em caso de exceção no envio da mensagem.
   */
  public void replyBusinessObjectMessage(Message message,
                                         String  content) throws Exception {
    // propriedades da mensagem
    Properties properties = getMessageProperties(message);
    // destinatário da mensagem original
    String businessObjectTo = properties.getProperty(PROPERTY_BUSINESS_OBJECT_TO, "");
    // remetente da mensagem original
    String businessObjectFrom = properties.getProperty(PROPERTY_BUSINESS_OBJECT_FROM, "");
    // responde a mensagem, com o objeto destinatário original como
    // remetente e o objeto remetente original como destinatário
    sendBusinessObjectMessage(message.getFrom()[0],
                              businessObjectFrom,
                              businessObjectTo,
                              content);
  }

  /**
   * Envia uma mensagem para o administrador do sistema.
   * @param subject String Assunto da mensagem.
   * @param content String Conteúdo da mensagem.
   * @throws Exception Em caso de exceção na tentativa de envio da mensagem.
   */
  public void sendAdministrativeMessage(String subject,
                                        String content) throws Exception {
    // se o serviço SMTP não está ativo...exceção
    if (!getSmtpActive())
      throw new ExtendedException(getClass().getName(), "sendAdministrativeMessage", "Serviço de envio de mensagens inativo.");
    // se não temos o endereço do administrador...exceção
    if (mailServiceFile.smtp().getAdminAddress().equals(""))
      throw new ExtendedException(getClass().getName(), "sendAdministrativeMessage", "Endereço do administrador desconhecido.");
    // constrói a mensagem
    Message message = new Message(mailServiceFile.smtp().getFromAddress(),
                                  mailServiceFile.smtp().getAdminAddress(),
                                  subject,
                                  content);
    // envia
    smtp.send(message);
  }

  /**
   * Envia uma mensagem para um objeto de negócio residente em outra aplicação.
   * @param applicationAddress String Endereço da aplicação de destino.
   * @param toClassName String Nome da classe do objeto de negócio de destino.
   * @param fromClassName String Nome da classe do objeto de negócio de origem.
   * @param content String Conteúdo da mensagem.
   * @throws Exception Em caso de exceção na tentativa de envio da mensagem.
   */
  public void sendBusinessObjectMessage(String applicationAddress,
                                        String toClassName,
                                        String fromClassName,
                                        String content) throws Exception {
    // se o serviço SMTP não está ativo...exceção
    if (!getSmtpActive())
      throw new ExtendedException(getClass().getName(), "sendBusinessObjectMessage", "Serviço de envio de mensagens inativo.");
    // constrói a mensagem
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
   * Envia uma mensagem para o destinatário informado.
   * @param to String Endereço do destinatário.
   * @param subject String Assunto da mensagem.
   * @param content String Conteúdo da mensagem.
   * @throws Exception Em caso de exceção na tentativa de envio da mensagem.
   */
  public void sendMessage(String to,
                          String subject,
                          String content) throws Exception {
    // se o serviço SMTP não está ativo...exceção
    if (!getSmtpActive())
      throw new ExtendedException(getClass().getName(), "sendMessage", "Serviço de envio de mensagens inativo.");
    // constrói a mensagem
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
   * @param content String Conteúdo da mensagem.
   * @throws Exception Em caso de exceção na tentativa de envio da mensagem.
   */
  public void sendSupportMessage(String subject,
                                 String content) throws Exception {
    // se o serviço SMTP não está ativo...exceção
    if (!getSmtpActive())
      throw new ExtendedException(getClass().getName(), "sendSupportMessage", "Serviço de envio de mensagens inativo.");
    // se não temos o endereço do administrador...exceção
    if (mailServiceFile.smtp().getSupportAddress().equals(""))
      throw new ExtendedException(getClass().getName(), "sendSupportMessage", "Endereço do suporte desconhecido.");
    // constrói a mensagem
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
