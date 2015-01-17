package iobjects.misc;

import java.util.*;

/**
 * Representa uma entrada no arquivo de log.
 */
public class LogEntry {

  static public final int EVENT_LOGON     = 0;
  static public final int EVENT_EXCEPTION = 1;
  static public final int EVENT_LOGOFF    = 2;

  private String defaultConnectionName = "";
  private String userName              = "";
  private Date   dateTime              = new Date();
  private Date   originalDateTime      = new Date();
  private int    event                 = -1;
  private String message               = "";

  /**
   * Construtor padrão.
   * @param defaultConnectionName String Nome da conexão padrão utilizada.
   * @param userName String Nome do usuário.
   * @param dateTime Date Date e hora.
   * @param originalDateTime Date Data e hora originais.
   * @param event int Evento.
   * @param message String Mensagem.
   */
  public LogEntry(String defaultConnectionName,
                  String userName,
                  Date   dateTime,
                  Date   originalDateTime,
                  int    event,
                  String message) {
    this.defaultConnectionName = defaultConnectionName;
    this.userName = userName;
    this.dateTime = dateTime;
    this.originalDateTime = originalDateTime;
    this.event = event;
    this.message = message;
  }

  public Date getDateTime() {
    return dateTime;
  }

  public String getDefaultConnectionName() {
    return defaultConnectionName;
  }

  public int getEvent() {
    return event;
  }

  public String getMessage() {
    return message;
  }

  public Date getOriginalDateTime() {
    return originalDateTime;
  }

  public String getUserName() {
    return userName;
  }

}
