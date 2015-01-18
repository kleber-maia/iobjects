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
