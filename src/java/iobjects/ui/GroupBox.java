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
package iobjects.ui;

import iobjects.*;
import iobjects.util.*;

/**
 * Representa um controle para agrupamento de controles de edição com um título
 * para sua identificação.
 * <p>
 *   Exemplo:
 * </p>
 * <pre>
 * &lt;!--aqui começa nosso GroupBox--&gt;
 * &lt;%=GroupBox.begin("Configurações de E-mail")%&gt;
 *     &lt;!--controles de edição--&gt;
 *     &lt;%=FormEdit.script(...)%&gt;
 *     &lt;%=FormSelect.script(...)%&gt;
 * &lt;!--aqui termina nosso GroupBox--&gt;
 * &lt;%=GroupBox.end()%&gt;
 * </pre>
 */
public class GroupBox {

  /**
   * Retorna o script contendo a representação HTML que inicia o GroupBox.
   * @param caption String Título do GroupBox.
   * @param height int Altura do GroupBox na página.
   * @return Retorna o script que contendo a representação HTML que inicia o
   *         GroupBox.
   */
  static public String begin(String caption) {
    return "<fieldset><legend>" + caption + "</legend>";
  }

  /**
   * Retorna o script contendo a representação HTML que finaliza o GroupBox.
   * @return Retorna o script contendo a representação HTML que finaliza o
   *         GroupBox.
   */
  static public String end() {
    return "</fieldset>";
  }

}
