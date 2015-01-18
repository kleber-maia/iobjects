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

import javax.mail.*;
import javax.mail.internet.*;

import iobjects.util.*;

/**
 * Ferramenta para recebimento de e-mails atrav�s do protocolo POP3.
 * @since 3.1
 */
public class Pop3 implements Pop3Callback {

  /**
   * Nome da pasta padr�o de recebimentos de e-mails.
   */
  static public String INBOX = "inbox";
  /**
   * Conte�do retornado quando o tipo de conte�do da mensagem � inv�lido.
   */
  static public String INVALID_CONTENT_TYPE = "tipo de conteudo invalido";

  static private final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";

  private String  host     = "";
  private String  password = "";
  private int     port     = -1;
  private Session session  = null;
  private boolean ssl      = false;
  private String  username = "";

  // vari�veis de controle de recebimento de mensagens para o Pop3Callback
  Vector    receivedMessages            = new Vector();
  String[]  filterExpressionsForSubject = {};
  boolean   deleteFromServer            = false;

  /**
   * Construtor padr�o.
   * @param host String Endere�o do servidor POP3.
   * @param port int Porta do servidor POP3. -1 indica que a porta padr�o
   *             ser� usada.
   * @param username String Nome do usu�rio para autentica��o.
   * @param password String Senha para autentica��o.
   * @param ssl boolean True para que seja utilizada conex�o segura atrav�s de SSL.
   */
  public Pop3(String  host,
              int     port,
              String  username,
              String  password,
              boolean ssl) {
    // guarda nossos valores
    this.host     = host;
    this.port     = port;
    this.username = username;
    this.password = password;
    // op��es para a nova sess�o
    Properties properties = new Properties();
    // se requer conex�o segura
    if (ssl) {
      properties.put("mail.pop3.socketFactory.class", SSL_FACTORY);
      properties.put("mail.pop3.socketFactory.fallback", "false");
    } // if
    // cria a nova sess�o
    session = Session.getInstance(properties);
  }

  public boolean onReceiveMessage(Message message) throws Exception {
    // se temos express�es de filtro...
    if ((filterExpressionsForSubject != null) && ((filterExpressionsForSubject.length > 0))) {
      // loop nas express�es de filtro
      for (int i=0; i<filterExpressionsForSubject.length; i++)
        // se a express�o n�o est� contida no assunto...dispara
        if (!message.getSubject().contains(filterExpressionsForSubject[i]))
          return false;
    } // if
    // adiciona a mensagem recebida
    receivedMessages.add(message);
    // retorna se devemos apagar a mensagem
    return deleteFromServer;
  }

  /**
   * Retorna um Message[] contendo a lista de mensagens recebidas da pasta
   * padr�o INBOX.
   * @param deleteFromServer boolean True se as mensagens recebidas devem ser
   *                         exclu�das do servidor.
   * @throws Exception Em caso de exce��o na tentativa de recebimento das mensagens.
   * @return Message[] Retorna um Message[] contendo a lista de mensagens recebidas
   *         da pasta padr�o INBOX.
   */
  public Message[] receive(boolean deleteFromServer) throws Exception {
    return receive(INBOX, null, deleteFromServer);
  }

  /**
   * Retorna um Message[] contendo a lista de mensagens recebidas da pasta
   * padr�o INBOX e cujas express�es de filtro est�o contidas no assunto.
   * @param filterExpressionsForSubject String Express�es que devem estar contidas
   *        no assunto das mensagens ou null para retornar todas as mensagens.
   * @param deleteFromServer boolean True se as mensagens recebidas devem ser
   *                         exclu�das do servidor.
   * @throws Exception Em caso de exce��o na tentativa de recebimento das mensagens.
   * @return Message[] Retorna um Message[] contendo a lista de mensagens recebidas
   *         da pasta padr�o INBOX e cujas express�es de filtro est�o contidas no
   *         assunto.
   */
  synchronized public Message[] receive(String[] filterExpressionsForSubject,
                                        boolean  deleteFromServer) throws Exception {
    return receive(INBOX, filterExpressionsForSubject, deleteFromServer);
  }

  /**
   * Retorna um Message[] contendo a lista de mensagens recebidas da pasta indicada
   * e cujas express�es de filtro est�o contidas no assunto.
   * @param folderName String Nome da pasta cujas mensagens ser�o recebidas.
   * @param filterExpressionsForSubject String Express�es que devem estar contidas
   *        no assunto das mensagens ou null para retornar todas as mensagens.
   * @param deleteFromServer boolean True se as mensagens recebidas devem ser
   *                         exclu�das do servidor.
   * @return Message[] Retorna um Message[] contendo a lista de mensagens recebidas
   *         da pasta indicada.
   * @throws Exception Em caso de exce��o na tentativa de recebimento das mensagens.
   */
  synchronized public Message[] receive(String   folderName,
                                        String[] filterExpressionsForSubject,
                                        boolean  deleteFromServer) throws Exception {
    try {
      // guarda as vari�veis de controle
      this.filterExpressionsForSubject = filterExpressionsForSubject;
      this.deleteFromServer = deleteFromServer;
      // recebe as mensagens
      receive(folderName, this);
      // nosso resultado
      Message[] result = new Message[receivedMessages.size()];
      receivedMessages.copyInto(result);
      return result;
    }
    finally {
      // libera recursos
      receivedMessages.clear();
    } // try-finally
  }

  /**
   * Recebe as mensagens do servidor existentes na pasta padr�o INBOX.
   * @param pop3Callback Pop3Callback respons�vel pelo processamento das mensagens.
   * @return int Retorna a quantidade de mensagens processadas.
   * @throws Exception Em caso de exce��o na tentativa de recebimento das mensagens.
   */
  synchronized public int receive(Pop3Callback pop3Callback) throws Exception {
    return receive(INBOX, pop3Callback);
  }

  /**
   * Recebe as mensagens do servidor existentes na pasta indicada por 'folderName'.
   * @param folderName String Nome da pasta cujas mensagens ser�o recebidas.
   * @param pop3Callback Pop3Callback respons�vel pelo processamento das mensagens.
   * @return int Retorna a quantidade de mensagens processadas.
   * @throws Exception Em caso de exce��o na tentativa de recebimento das mensagens.
   */
  synchronized public int receive(String       folderName,
                                  Pop3Callback pop3Callback) throws Exception {
    Store store = session.getStore("pop3");
    try {
      // conecta ao servidor
      store.connect(host, port, username, password);
      // abre a pasta
      Folder folder = store.getFolder(folderName);
      // abre o folder
      folder.open(Folder.READ_WRITE);
      // recebe as mensagens
      javax.mail.Message[] mimeMessages = folder.getMessages();
      // loop nas mensagens recebidas
      for (int i=0; i<mimeMessages.length; i++) {
        // mensagem recebida da vez
        javax.mail.Message mimeMessage = mimeMessages[i];
        // transforma em uma inst�ncia de Message
        Message message = new Message();
        message.setBcc(stringArrayFromInternetAddresses(mimeMessage.getRecipients(javax.mail.Message.RecipientType.BCC)));
        message.setCc(stringArrayFromInternetAddresses(mimeMessage.getRecipients(javax.mail.Message.RecipientType.CC)));
        message.setContent(retrieveContent(mimeMessage));
        message.setFrom(stringArrayFromInternetAddresses(mimeMessage.getFrom()));
        message.setReceivedDate(mimeMessage.getReceivedDate());
        message.setReplyTo(stringArrayFromInternetAddresses(mimeMessage.getReplyTo()));
        message.setSentDate(mimeMessage.getSentDate());
        message.setSubject(mimeMessage.getSubject());
        message.setTo(stringArrayFromInternetAddresses(mimeMessage.getRecipients(javax.mail.Message.RecipientType.TO)));
        // se devemos exclu�-la do servidor...
        if (pop3Callback.onReceiveMessage(message))
          mimeMessage.setFlag(Flags.Flag.DELETED, true);
      } // for
      // fecha o folder
      folder.close(true);
      // retorna
      return mimeMessages.length;
    }
    finally {
      // desconecta do servidor
      store.close();
    } // try-finally
  }

  /**
   * Retorna o conte�do de 'message' de acordo com o seu tipo de conte�do. Se
   * o tipo de conte�do n�o possuir o conjunto de caracteres informado (charset)
   * INVALID_CONTENT ser� retornado.
   * @param message Message Mensagem cujo conte�do se deseja retornar.
   * @return String Retorna o conte�do de 'message' de acordo com o seu tipo de
   *         conte�do.
   * @throws Exception Em caso de exce��o na tentativa de receber o conte�do
   *         da mensagem.
   */
  private String retrieveContent(javax.mail.Message message) throws Exception {
    if (message.getContentType().indexOf("charset") >= 0)
      return (String)message.getContent();
    else
      return INVALID_CONTENT_TYPE;
  }

  /**
   * Retorna um String[] contendo os endere�os contidos em 'value'.
   * @param value Address[] Lista de valores para serem retornados na forma
   *              de String.
   * @return String[] Retorna um String[] contendo os endere�os contidos em
   *         'value'.
   */
  private String[] stringArrayFromInternetAddresses(Address[] value) {
    if (value == null)
      return new String[0];
    String[] result = new String[value.length];
    for (int i=0; i<value.length; i++)
      result[i] = ((InternetAddress)value[i]).getAddress();
    return result;
  }

}
