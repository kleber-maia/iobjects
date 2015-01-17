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
