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
package iobjects.help;

import iobjects.*;

/**
 * Representa o gerenciador de ajuda da aplicação. O mecanismo de ajuda é capaz
 * de construir conteúdo de ajuda e suporte a partir dos objetos de negócio
 * da aplicação e suas ações.
 * @since 2006
 */
public class HelpManager {

  static public String ACTION_NAME = "actionName";
  static public String FAQ_NAME    = "faqName";
  static public String WINDOW_NAME = "help";

  private FAQList faqList = new FAQList();

  /**
   * Construtor padrão.
   */
  public HelpManager() {
  }

  /**
   * Retorna a lista de FAQ's da aplicação.
   * @return FAQList Retorna a lista de FAQ's da aplicação.
   */
  public FAQList faqList() {
    return faqList;
  }

}
