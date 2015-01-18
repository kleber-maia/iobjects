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
 * Responsável pelo gerenciamento das sessões de usuário no web server. Suas
 * principais características são:
 * <ul>
 *   <li>Elimina sessões duplicadas para o mesmo usuário.</li>
 *   <li>Impede que o limite de usuários conectados com a mesma conexão
 *       default seja excedido.</li>
 *   <li>Possibilita visualizar sessões em uso.</li>
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
   * Retorna a instância de SessionManager para ser utilizada pela aplicação.
   * @return SessionManager Retorna a instância de SessionManager para ser
   *         utilizada pela aplicação.
   */
  static public SessionManager getInstance() {
    if (sessionManager == null)
      sessionManager = new SessionManager();
    return sessionManager;
  }

  /**
   * Adiciona 'session' a lista caso ainda não exista.
   * @param session HttpSession Sessão que se deseja adicionar a lista.
   */
  private void addSession(HttpSession session) {
    // se ainda não temos a sessão na lista...adiciona
    if (!vector.contains(session))
      vector.add(session);
  }

  /**
   * Remove 'session' da lista e chama o Garbage Collector da JVM na tentativa
   * de liberar toda a memória associada à sessão que está sendo removida.
   * @param session HttpSession Sessão que se deseja remover da lista.
   */
  private void deleteSession(HttpSession session) {
    // remove a sessão da lista
    vector.remove(session);
    // chama o garbage collector da JVM
    System.gc();
  }

  /**
   * Dá um loop em todas as sessões gerenciadas e chama o método
   * garbageCollect(HttpSession).
   */
  private void garbageCollect() {
    // loop nas sessões
    HttpSession[] sessions = getSessionList();
    for (int i=0; i<sessions.length; i++) {
      // coleta o lixo dessa sessão
      garbageCollect(sessions[i]);
    } // for
  }

  /**
   * Faz as seguintes verificações em 'session':
   * <ul>
   *   <li>Se já foi invalidada: remove da lista.</li>
   *   <li>Se não tem Facade: invalida e remove da lista.</li>
   *   <li>Se não tem um usuário e existe há mais de 3 minutos: invalida e remove
   *       da lista.</li>
   *   <li>Invalida as sessões mais antigas que ultrapassem a quantidade máxima
   *       de conexões permitidas.</li>
   *   <li>Procura por outras sessões mais antigas para o mesmo usuário, as
   *       invalida e as remove da lista.</li>
   * </ul>
   * @param session HttpSession
   */
  private void garbageCollect(HttpSession session) {
    // se a sessão já foi invalidada...
    if (getInvalidated(session)) {
      // remove da lista
      deleteSession(session);
      // dispara
      return;
    }
    // obtém a Facade da sessão
    Facade facade = getFacade(session);
    // se não temos Facade...
    if (facade == null) {
      // invalida e remove da lista
      invalidateSession(session);
      // dispara
      return;
    } // if
    // se não tem um usuário que efetuou logon...
    if (facade.getLoggedUser() == null) {
      // se a sessão existe a mais de 3 minutos...invalida e remove da lista
      Date now       = new Date();
      Date validTime = new Date(session.getCreationTime() + (1000 * 60 * 3));
      if (now.after(validTime))
        invalidateSession(session);
    }
    // se tem um usuário que efetuou logon...
    else {
      // obtém o arquivo de configuração da conexão default
      iobjects.xml.sql.ConnectionFile defaultConnectionFile = facade.connectionManager().connectionFiles().get(facade.getDefaultConnectionName());
      // sessões da mesma conexão default
      Vector<HttpSession> sameDefaultConnectionSessions = new Vector<HttpSession>();
      // loop nas sessões
      HttpSession[] sessions = getSessionList();
      for (int i=0; i<sessions.length; i++) {
        // sessão da vez
        HttpSession sessionItem = sessions[i];
        // se já foi invalidada...continua
        if (getInvalidated(sessionItem))
          continue;
        // se é a mesma sessão que estamos coletando...continua
        if (sessionItem.getId().equals(session.getId()))
          continue;
        // se é mais nova que a sessão que estamos coletando...continua
        if (sessionItem.getCreationTime() > session.getCreationTime())
          continue;
        // Facade da sessão
        Facade facadeItem = getFacade(sessionItem);
        // se não tem Facade...continua
        if (facadeItem == null)
          continue;
        // se não é a mesma conexão default...continua
        if (!facade.getDefaultConnectionName().equals(facadeItem.getDefaultConnectionName()))
          continue;
        // se não permite o mesmo usuário e é o mesmo usuário...
        if (!defaultConnectionFile.sessionManager().getAllowSameUserLogon() &&
            (facadeItem.getLoggedUser() != null) &&
            (facade.getLoggedUser().getId() == facadeItem.getLoggedUser().getId()))
          // invalida e remove da lista
          invalidateSession(sessionItem);
        // se não...adiciona na lista de mesma conexão default
        else
          sameDefaultConnectionSessions.add(sessionItem);
      } // for
      // se temos limite de conexões...
      if (defaultConnectionFile.sessionManager().getMaxDefaultConnections() >= 0) {
        // quantidade de conexões a mais que o permitido na mesma conexão default...
        int sameDefaultConnectionsOver = (sameDefaultConnectionSessions.size() + 1 /* a própria session */) - defaultConnectionFile.sessionManager().getMaxDefaultConnections();
        // se temos mais conexões que o permitido...
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
   * @param session HttpSession Sessão que se deseja obter o estado.
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
   * Retorna a Facade contida em 'session' ou null caso não exista.
   * @param session HttpSession Sessão cuja Facade se deseja obter.
   * @return Facade Retorna a Facade contida em 'session' ou null caso não exista.
   */
  public Facade getFacade(HttpSession session) {
    if (getInvalidated(session))
      return null;
    else
      return (Facade)session.getAttribute(Controller.FACADE);
  }

  /**
   * Retorna um HttpSession[] contendo a lista de sessões mantidas.
   * @return HttpSession[] Retorna um HttpSession[] contendo a lista de sessões
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
   * Invalida 'session' e remove da lista de sessões.
   * @param session HttpSession Sessão que se deseja invalidar e remover da lista.
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
   * Notifica o SessionManager sobre a criação ou mudança de estado da sessão
   * identificada por 'session'.
   * @param session HttpSession Sessão em questão.
   * @param status int Status de notificação da sessão.
   */
  public synchronized void notify(HttpSession session,
                                  int         status) {
    // se é uma nova sessão...nos coloca lá
    if ((status == NOTIFY_STATUS_NEW) ||
        (status == NOTIFY_STATUS_UNKNOWN && session.isNew())) {
      session.setAttribute(SESSION_MANAGER, this);
    }
    // se está fazendo logon ou logoff...passa o coletor de lixo
    else if ((status == NOTIFY_STATUS_USER_LOGON) ||
             (status == NOTIFY_STATUS_USER_LOGOFF)) {
     garbageCollect();
    } // if
  }

  /**
   * Evento chamado pela sessão que está nos recebendo como atributo.
   * @param event HttpSessionBindingEvent
   */
  public void valueBound(HttpSessionBindingEvent event) {
    addSession(event.getSession());
  }

  /**
   * Evento chamado pela sessão que está nos descartando como atributo.
   * @param event HttpSessionBindingEvent
   */
  public void valueUnbound(HttpSessionBindingEvent event) {
    deleteSession(event.getSession());
  }

}
