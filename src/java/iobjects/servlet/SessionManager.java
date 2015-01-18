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
package iobjects.servlet;

import java.util.*;

import javax.servlet.http.*;

import iobjects.*;
import iobjects.security.*;

/**
 * Respons�vel pelo gerenciamento das sess�es de usu�rio no web server. Suas
 * principais caracter�sticas s�o:
 * <ul>
 *   <li>Elimina sess�es duplicadas para o mesmo usu�rio.</li>
 *   <li>Impede que o limite de usu�rios conectados com a mesma conex�o
 *       default seja excedido.</li>
 *   <li>Possibilita visualizar sess�es em uso.</li>
 * </ul>
 */
public class SessionManager implements HttpSessionBindingListener {

  static public final int NOTIFY_STATUS_UNKNOWN     = -1;
  static public final int NOTIFY_STATUS_NEW         = 0;
  static public final int NOTIFY_STATUS_USER_LOGON  = 1;
  static public final int NOTIFY_STATUS_USER_LOGOFF = 2;

  static private final String SESSION_MANAGER = "sessionManager";

  static private SessionManager sessionManager  = null;

  private Vector vector = new Vector();

  private SessionManager() {
  }

  /**
   * Retorna a inst�ncia de SessionManager para ser utilizada pela aplica��o.
   * @return SessionManager Retorna a inst�ncia de SessionManager para ser
   *         utilizada pela aplica��o.
   */
  static public SessionManager getInstance() {
    if (sessionManager == null)
      sessionManager = new SessionManager();
    return sessionManager;
  }

  /**
   * Adiciona 'session' a lista caso ainda n�o exista.
   * @param session HttpSession Sess�o que se deseja adicionar a lista.
   */
  private void addSession(HttpSession session) {
    // se ainda n�o temos a sess�o na lista...adiciona
    if (!vector.contains(session))
      vector.add(session);
  }

  /**
   * Remove 'session' da lista e chama o Garbage Collector da JVM na tentativa
   * de liberar toda a mem�ria associada � sess�o que est� sendo removida.
   * @param session HttpSession Sess�o que se deseja remover da lista.
   */
  private void deleteSession(HttpSession session) {
    // remove a sess�o da lista
    vector.remove(session);
    // chama o garbage collector da JVM
    System.gc();
  }

  /**
   * D� um loop em todas as sess�es gerenciadas e chama o m�todo
   * garbageCollect(HttpSession).
   */
  private void garbageCollect() {
    // loop nas sess�es
    HttpSession[] sessions = getSessionList();
    for (int i=0; i<sessions.length; i++) {
      // coleta o lixo dessa sess�o
      garbageCollect(sessions[i]);
    } // for
  }

  /**
   * Faz as seguintes verifica��es em 'session':
   * <ul>
   *   <li>Se j� foi invalidada: remove da lista.</li>
   *   <li>Se n�o tem Facade: invalida e remove da lista.</li>
   *   <li>Se n�o tem um usu�rio e existe h� mais de 3 minutos: invalida e remove
   *       da lista.</li>
   *   <li>Invalida as sess�es mais antigas que ultrapassem a quantidade m�xima
   *       de conex�es permitidas.</li>
   *   <li>Procura por outras sess�es mais antigas para o mesmo usu�rio, as
   *       invalida e as remove da lista.</li>
   * </ul>
   * @param session HttpSession
   */
  private void garbageCollect(HttpSession session) {
    // se a sess�o j� foi invalidada...
    if (getInvalidated(session)) {
      // remove da lista
      deleteSession(session);
      // dispara
      return;
    }
    // obt�m a Facade da sess�o
    Facade facade = getFacade(session);
    // se n�o temos Facade...
    if (facade == null) {
      // invalida e remove da lista
      invalidateSession(session);
      // dispara
      return;
    } // if
    // se n�o tem um usu�rio que efetuou logon...
    if (facade.getLoggedUser() == null) {
      // se a sess�o existe a mais de 3 minutos...invalida e remove da lista
      Date now       = new Date();
      Date validTime = new Date(session.getCreationTime() + (1000 * 60 * 3));
      if (now.after(validTime))
        invalidateSession(session);
    }
    // se tem um usu�rio que efetuou logon...
    else {
      // obt�m o arquivo de configura��o da conex�o default
      iobjects.xml.sql.ConnectionFile defaultConnectionFile = facade.connectionManager().connectionFiles().get(facade.getDefaultConnectionName());
      // sess�es da mesma conex�o default
      Vector<HttpSession> sameDefaultConnectionSessions = new Vector<HttpSession>();
      // loop nas sess�es
      HttpSession[] sessions = getSessionList();
      for (int i=0; i<sessions.length; i++) {
        // sess�o da vez
        HttpSession sessionItem = sessions[i];
        // se j� foi invalidada...continua
        if (getInvalidated(sessionItem))
          continue;
        // se � a mesma sess�o que estamos coletando...continua
        if (sessionItem.getId().equals(session.getId()))
          continue;
        // se � mais nova que a sess�o que estamos coletando...continua
        if (sessionItem.getCreationTime() > session.getCreationTime())
          continue;
        // Facade da sess�o
        Facade facadeItem = getFacade(sessionItem);
        // se n�o tem Facade...continua
        if (facadeItem == null)
          continue;
        // se n�o � a mesma conex�o default...continua
        if (!facade.getDefaultConnectionName().equals(facadeItem.getDefaultConnectionName()))
          continue;
        // se n�o permite o mesmo usu�rio e � o mesmo usu�rio...
        if (!defaultConnectionFile.sessionManager().getAllowSameUserLogon() &&
            (facadeItem.getLoggedUser() != null) &&
            (facade.getLoggedUser().getId() == facadeItem.getLoggedUser().getId()))
          // invalida e remove da lista
          invalidateSession(sessionItem);
        // se n�o...adiciona na lista de mesma conex�o default
        else
          sameDefaultConnectionSessions.add(sessionItem);
      } // for
      // se temos limite de conex�es...
      if (defaultConnectionFile.sessionManager().getMaxDefaultConnections() >= 0) {
        // quantidade de conex�es a mais que o permitido na mesma conex�o default...
        int sameDefaultConnectionsOver = (sameDefaultConnectionSessions.size() + 1 /* a pr�pria session */) - defaultConnectionFile.sessionManager().getMaxDefaultConnections();
        // se temos mais conex�es que o permitido...
        if (sameDefaultConnectionsOver > 0) {
          // invalida as sobressalentes...
          for (int i=0; i<sameDefaultConnectionsOver; i++)
            invalidateSession((HttpSession)sameDefaultConnectionSessions.get(i));
        } // if
      } // if
    } // if
  }

  /**
   * Retorna true se 'session' tiver sido invalidada.
   * @param session HttpSession Sess�o que se deseja obter o estado.
   * @return boolean Retorna true se 'session' tiver sido invalidada.
   */
  public boolean getInvalidated(HttpSession session) {
    try {
      session.getAttribute(Controller.FACADE);
      return false;
    }
    catch (IllegalStateException e) {
      return true;
    } // try-catch
  }

  /**
   * Retorna a Facade contida em 'session' ou null caso n�o exista.
   * @param session HttpSession Sess�o cuja Facade se deseja obter.
   * @return Facade Retorna a Facade contida em 'session' ou null caso n�o exista.
   */
  public Facade getFacade(HttpSession session) {
    if (getInvalidated(session))
      return null;
    else
      return (Facade)session.getAttribute(Controller.FACADE);
  }

  /**
   * Retorna um HttpSession[] contendo a lista de sess�es mantidas.
   * @return HttpSession[] Retorna um HttpSession[] contendo a lista de sess�es
   * mantidas.
   */
  public HttpSession[] getSessionList() {
    // copia no array de retorno
    HttpSession[] result = new HttpSession[vector.size()];
    vector.copyInto(result);
    // retorna
    return result;
  }

  /**
   * Invalida 'session' e remove da lista de sess�es.
   * @param session HttpSession Sess�o que se deseja invalidar e remover da lista.
   */
  private void invalidateSession(HttpSession session) {
    try {
      // invalida
      session.invalidate();
    }
    catch (IllegalStateException e) {
    } // try-catch
    // remove da lista
    deleteSession(session);
  }

  /**
   * Notifica o SessionManager sobre a cria��o ou mudan�a de estado da sess�o
   * identificada por 'session'.
   * @param session HttpSession Sess�o em quest�o.
   * @param status int Status de notifica��o da sess�o.
   */
  public synchronized void notify(HttpSession session,
                                  int         status) {
    // se � uma nova sess�o...nos coloca l�
    if ((status == NOTIFY_STATUS_NEW) ||
        (status == NOTIFY_STATUS_UNKNOWN && session.isNew())) {
      session.setAttribute(SESSION_MANAGER, this);
    }
    // se est� fazendo logon ou logoff...passa o coletor de lixo
    else if ((status == NOTIFY_STATUS_USER_LOGON) ||
             (status == NOTIFY_STATUS_USER_LOGOFF)) {
     garbageCollect();
    } // if
  }

  /**
   * Evento chamado pela sess�o que est� nos recebendo como atributo.
   * @param event HttpSessionBindingEvent
   */
  public void valueBound(HttpSessionBindingEvent event) {
    addSession(event.getSession());
  }

  /**
   * Evento chamado pela sess�o que est� nos descartando como atributo.
   * @param event HttpSessionBindingEvent
   */
  public void valueUnbound(HttpSessionBindingEvent event) {
    deleteSession(event.getSession());
  }

}
