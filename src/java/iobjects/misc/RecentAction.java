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

import iobjects.*;

/**
 * Representa uma ação recente executada pelo usuário.
 */
public class RecentAction {

  private Action action  = null;
  private String caption = "";
  private String query   = "";
  private String title   = "";

  /**
   * Construtor padrão.
   * @param action Action Ação executada.
   * @param query String Parâmetros passados à ação executada.
   * @param caption String Título da ação recente.
   * @param title String Título do item acessado pela ação recente.
   */
  public RecentAction(Action action,
                      String query,
                      String caption,
                      String title) {
    this.action = action;
    this.query = query;
    this.caption = caption;
    this.title = title;
  }

  public boolean equals(Object object) {
    return action.equals(((RecentAction)object).getAction()) &&
           caption.equals(((RecentAction)object).getCaption()) &&
           title.equals(((RecentAction)object).getTitle());
  }

  /**
   * Retorna a ação executada.
   * @return Action Retorna a ação executada.
   */
  public Action getAction() {
    return action;
  }

  /**
   * Retorna o titulo da ação recentes.
   * @return Retorna o titulo da ação recentes.
   */
  public String getCaption() {
    return caption;
  }

  /**
   * Retorna os parâmetros passados à ação.
   * @return String Retorna os parâmetros passados à ação.
   */
  public String getQuery() {
    return query;
  }

  /**
   * Retorna o titulo do item acessado pela da ação recentes.
   * @return Retorna o titulo do item acessado pela da ação recentes.
   */
  public String getTitle() {
    return title;
  }

}
