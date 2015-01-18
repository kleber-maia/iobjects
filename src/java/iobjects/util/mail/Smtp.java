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

/**
 * Ferramenta para envio de e-mails atrav�s do protocolo SMTP.
 * @since 3.1
 */
public class Smtp {

  static private final String ALTERNATIVE                = "alternative;charset=ISO-8859-1";
  static private final String CHARSET_EUROPEAN_OCIDENTAL = "ISO-8859-1";
  static private final String RELATED                    = "related;charset=ISO-8859-1";
  static private final String SSL_FACTORY                = "javax.net.ssl.SSLSocketFactory";

  private String  host     = "";
  private String  password = "";
  private int     port     = -1;
  private Session session  = null;
  private boolean ssl      = false;
  private String  username = "";

  /**
   * Construtor padr�o.
   * @param host String Endere�o do servidor SMTP.
   * @param port int Porta do servidor SMTP. -1 indica que a porta padr�o
   *             ser� usada.
   */
  public Smtp(String host,
              int    port) {
    this(host, port, "", "", false);
  }

  /**
   * Construtor estendido, para servidores SMTP que requerem autentica��o.
   * @param host String Endere�o do servidor SMTP.
   * @param port int Porta do servidor SMTP. -1 indica que a porta padr�o
   *             ser� usada.
   * @param username String Nome do usu�rio para autentica��o.
   * @param password String Senha para autentica��o.
   * @param ssl boolean True para que seja utilizada conex�o segura atrav�s de SSL.
   */
  public Smtp(String  host,
              int     port,
              String  username,
              String  password,
              boolean ssl) {
    // guarda nossos valores
    this.host     = host;
    this.port     = port;
    this.username = username;
    this.password = password;
    this.ssl      = ssl;
    // op��es para a nova sess�o
    Properties properties = new Properties();
    properties.put("mail.smtp.auth", Boolean.toString(!password.equals("")));
    // se requer conex�o segura
    if (ssl) {
      properties.put("mail.smtp.socketFactory.class", SSL_FACTORY);
      properties.put("mail.smtp.socketFactory.fallback", "false");
    } // if
    // cria a nova sess�o
    session = Session.getInstance(properties);
  }

  /**
   * Envia uma mensagem.
   * @param message Message Mensagem para ser enviada.
   * @throws Exception Em caso de exce��o na tentativa de envio da mensagem.
   */
  public void send(Message message) throws Exception {
    // constr�i a mensagem que ser� enviada
    MimeMessage mimeMessage = new MimeMessage(session);
    // data de envio
    mimeMessage.setSentDate(message.getSentDate());
    // remetente
    String[] from = message.getFrom();
    for (int i=0; i<from.length; i++)
      mimeMessage.setFrom(new InternetAddress(from[i]));
    // destinat�rios
    InternetAddress[] to = new InternetAddress[message.getTo().length];
    for (int i=0; i<to.length; i++)
      to[i] = new InternetAddress(message.getTo()[i]);
    mimeMessage.setRecipients(MimeMessage.RecipientType.TO, to);
    // c�pias carbono
    InternetAddress[] cc = new InternetAddress[message.getCc().length];
    for (int i=0; i<cc.length; i++)
      cc[i] = new InternetAddress(message.getCc()[i]);
    mimeMessage.setRecipients(MimeMessage.RecipientType.CC, cc);
    // c�pias carbono oculta
    InternetAddress[] bcc = new InternetAddress[message.getBcc().length];
    for (int i=0; i<bcc.length; i++)
      bcc[i] = new InternetAddress(message.getBcc()[i]);
    mimeMessage.setRecipients(MimeMessage.RecipientType.BCC, bcc);
    // assunto
    mimeMessage.setSubject(message.getSubject());
    // conte�do
    mimeMessage.setText(message.getContent());

    // formato das mensagens com HTML e imagens embutidas
    //
    // +-- 1. mainMultiPart ------------------------------+
    // |                                                  |
    // |  +-- 1.1. textBodyPart -----------------------+  |
    // |  | Conte�do alternativo no formato texto.     |  |
    // |  +--------------------------------------------+  |
    // |                                                  |
    // |  +-- 1.2. relatedBodyPart --------------------+  |
    // |  |                                            |  |
    // |  |  +-- 1.2.1. relatedMultiPart -----------+  |  |
    // |  |  |                                      |  |  |
    // |  |  |  +-- 1.2.1.1. htmlBodyPart -------+  |  |  |
    // |  |  |  | Conte�do no formato HTML.      |  |  |  |
    // |  |  |  +--------------------------------+  |  |  |
    // |  |  |                                      |  |  |
    // |  |  |  +-- 1.2.1.2. imageBodyPart1------+  |  |  |
    // |  |  |  | Imagem No 1.                   |  |  |  |
    // |  |  |  +--------------------------------+  |  |  |
    // |  |  |                                      |  |  |
    // |  |  |  +-- 1.2.1.3. imageBodyPart-------+  |  |  |
    // |  |  |  | Imagem No 2.                   |  |  |  |
    // |  |  |  +--------------------------------+  |  |  |
    // |  |  |                                      |  |  |
    // |  |  |  +-- 1.2.1.n. imageBodyPartn------+  |  |  |
    // |  |  |  | Imagem No n.                   |  |  |  |
    // |  |  |  +--------------------------------+  |  |  |
    // |  |  |                                      |  |  |
    // |  |  +--------------------------------------+  |  |
    // |  |                                            |  |
    // |  +--------------------------------------------+  |
    // |                                                  |
    // +--------------------------------------------------+

    // se � uma mensagem Multipart...
    if (message instanceof HTMLMessage) {
      // faz um typecast
      HTMLMessage htmlMessage = (HTMLMessage)message;
      // 1. cria a Multipart principal da mensagem
      Multipart mainMultipart = new MimeMultipart(ALTERNATIVE);
        // 1.1. cria um BodyPart contendo o conte�do no formato texto da mensagem
        MimeBodyPart textBodyPart = new MimeBodyPart();
        textBodyPart.setText(message.getContent(), CHARSET_EUROPEAN_OCIDENTAL);
        // 1.2. cria um BodyPart para conter o conte�do da segunda Multipart (HTML e imagens)
        MimeBodyPart relatedBodyPart = new MimeBodyPart();
          // 1.2.1. cria a segunda Multipart da mensagem para HTML e imagens
          Multipart relatedMultipart = new MimeMultipart(RELATED);
            // 1.2.1.1. adiciona o BodyPart contendo o conte�do no formato HTML da mensagem
            relatedMultipart.addBodyPart(htmlMessage.getHTMLContent());
            // 1.2.1.2. adiciona os BodyParts contendo as imagens da mensagem
            MimeBodyPart[] htmlReferencedBodyPartList = htmlMessage.getHTMLReferencedContentList();
            for (int i=0; i<htmlReferencedBodyPartList.length; i++) {
              relatedMultipart.addBodyPart(htmlReferencedBodyPartList[i]);
            } // for
            // 1.2.1.3. define o conte�do da BodyPart de HTML e imagens
            relatedBodyPart.setContent(relatedMultipart);
        // 1.3. adiciona a parte de texto � mensagem
        mainMultipart.addBodyPart(textBodyPart);
        // 1.4. adiciona a parte HTML e de imagens � imagem
        mainMultipart.addBodyPart(relatedBodyPart);
      // 2. define o conte�do da mensagem
      mimeMessage.setContent(mainMultipart);
    } // if

    // salva a mensagem
    mimeMessage.saveChanges();

    // envia a mensagem
    Transport transport = session.getTransport("smtp");
    transport.connect(host, port, username, password);
    transport.sendMessage(mimeMessage, mimeMessage.getAllRecipients());
    transport.close();
  }

}
