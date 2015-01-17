package iobjects.util.mail;

import java.util.*;

/**
 * Representa uma mensagem recebida ou que será enviada.
 * @since 3.1
 */
public class Message {

  private Vector bcc           = new Vector();
  private Vector cc            = new Vector();
  private String content       = "";
  private Vector from          = new Vector();
  private int    messageNumber = 0;
  private Date   receivedDate  = new Date();
  private Vector replyTo       = new Vector();
  private Date   sentDate      = new Date();
  private String subject       = "";
  private Vector to            = new Vector();

  /**
   * Construtor padrão.
   */
  public Message() {
  }

  /**
   * Construtor estendido.
   * @param from String Endereço(s) do(s) remetente(s) separados por ';'.
   * @param to String Endereço(s) do(s) destinatário(s) separados por ';'.
   * @param subject String Assunto.
   * @param content String Conteúdo.
   */
  public Message(String from,
                 String to,
                 String subject,
                 String content) {
    setFrom(from.split(";"));
    setTo(to.split(";"));
    this.subject = subject;
    this.content = content;
  }

  /**
   * Construtor estendido.
   * @param from String[] Endereços dos remetentes.
   * @param replyTo String[] Endereços de resposta.
   * @param to String[] Endereços dos destinatários.
   * @param cc String[] Endereços das cópias carbono.
   * @param bcc String[] Endereços das cópias carbono ocultas.
   * @param subject String Assunto.
   * @param content String Conteúdo.
   */
  public Message(String[] from,
                 String[] replyTo,
                 String[] to,
                 String[] cc,
                 String[] bcc,
                 String   subject,
                 String   content) {
    setFrom(from);
    setReplyTo(replyTo);
    setTo(to);
    setCc(cc);
    setBcc(bcc);
    this.subject = subject;
    this.content = content;
  }

  /**
   * Retorna os endereços das cópias carbono ocultas.
   * @return String[] Retorna os endereços das cópias carbono ocultas.
   */
  public String[] getBcc() {
    String[] result = new String[bcc.size()];
    bcc.copyInto(result);
    return result;
  }

  /**
   * Retorna os endereços das cópias carbono.
   * @return String[] Retorna os endereços das cópias carbono.
   */
  public String[] getCc() {
    String[] result = new String[cc.size()];
    cc.copyInto(result);
    return result;
  }

  /**
   * Retorna o conteúdo no formato texto.
   * @return String Retorno o conteúdo no formato texto.
   */
  public String getContent() {
    return content;
  }

  /**
   * Retorna os endereços dos remetentes.
   * @return String[] Retorna os endereços dos remetentes.
   */
  public String[] getFrom() {
    String[] result = new String[from.size()];
    from.copyInto(result);
    return result;
  }

  /**
   * Retorna o número da mensagem.
   * @return int Retorna o número da mensagem.
   */
  public int getMessageNumber() {
    return messageNumber;
  }

  /**
   * Retorna a data de recebimento.
   * @return Date Retorna a data de recebimento.
   */
  public Date getReceivedDate() {
    return receivedDate;
  }

  /**
   * Retorna os endereços de resposta.
   * @return String[] Retorna os endereços de resposta.
   */
  public String[] getReplyTo() {
    String[] result = new String[replyTo.size()];
    replyTo.copyInto(result);
    return result;
  }

  /**
   * Retorna a data de envio.
   * @return Date Retorna a data de envio.
   */
  public Date getSentDate() {
    return sentDate;
  }

  /**
   * Retorna o assunto.
   * @return String Retorna o assunto.
   */
  public String getSubject() {
    return subject;
  }

  /**
   * Retorna os endereços dos destinatários.
   * @return String[] Retorna os endereços dos destinatários.
   */
  public String[] getTo() {
    String[] result = new String[to.size()];
    to.copyInto(result);
    return result;
  }

  /**
   * Define os endereços das cópias carbono ocultas.
   * @param value String[] Endereços das cópias carbono ocultas.
   */
  public void setBcc(String[] value) {
    this.bcc.clear();
    for (int i=0; i<value.length; i++)
      this.bcc.add(value[i]);
  }

  /**
   * Define os endereços das cópias carbono.
   * @param value String[] Endereços das cópias carbono.
   */
  public void setCc(String[] value) {
    this.cc.clear();
    for (int i=0; i<value.length; i++)
      this.cc.add(value[i]);
  }

  /**
   * Define o conteúdo no formato texto.
   * @param content String Conteúdo no formato texto.
   */
  public void setContent(String content) {
    this.content = content;
  }

  /**
   * Define os endereços dos remetentes.
   * @param value String[] Endereços dos remetentes.
   */
  public void setFrom(String[] value) {
    this.from.clear();
    for (int i=0; i<value.length; i++)
      this.from.add(value[i]);
  }

  /**
   * Define o número da mensagem.
   * @param value int Número da mensagem.
   */
  public void setMessageNumber(int value) {
    this.messageNumber = value;
  }

  /**
   * Define a data de recebimento.
   * @param value Date Data de recebimento.
   */
  public void setReceivedDate(Date value) {
    this.receivedDate = value;
  }

  /**
   * Define os endereços de resposta.
   * @param value String[] Endereços de resposta.
   */
  public void setReplyTo(String[] value) {
    this.replyTo.clear();
    for (int i=0; i<value.length; i++)
      this.replyTo.add(value[i]);
  }

  /**
   * Define a data de envio.
   * @param value Date Data de envio.
   */
  public void setSentDate(Date value) {
    this.sentDate = value;
  }

  /**
   * Define o assuntio.
   * @param subject String Assunto.
   */
  public void setSubject(String subject) {
    this.subject = subject;
  }

  /**
   * Define os endereços dos destinatários.
   * @param value String[] Endereços dos destinatários.
   */
  public void setTo(String[] value) {
    this.to.clear();
    for (int i=0; i<value.length; i++)
      this.to.add(value[i]);
  }

}
