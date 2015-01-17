package iobjects.ui;

import iobjects.*;
import iobjects.util.*;

/**
 * Representa um controle para agrupamento de controles de edi��o com um t�tulo
 * para sua identifica��o.
 * <p>
 *   Exemplo:
 * </p>
 * <pre>
 * &lt;!--aqui come�a nosso GroupBox--&gt;
 * &lt;%=GroupBox.begin("Configura��es de E-mail")%&gt;
 *     &lt;!--controles de edi��o--&gt;
 *     &lt;%=FormEdit.script(...)%&gt;
 *     &lt;%=FormSelect.script(...)%&gt;
 * &lt;!--aqui termina nosso GroupBox--&gt;
 * &lt;%=GroupBox.end()%&gt;
 * </pre>
 */
public class GroupBox {

  /**
   * Retorna o script contendo a representa��o HTML que inicia o GroupBox.
   * @param caption String T�tulo do GroupBox.
   * @param height int Altura do GroupBox na p�gina.
   * @return Retorna o script que contendo a representa��o HTML que inicia o
   *         GroupBox.
   */
  static public String begin(String caption) {
    return "<fieldset><legend>" + caption + "</legend>";
  }

  /**
   * Retorna o script contendo a representa��o HTML que finaliza o GroupBox.
   * @return Retorna o script contendo a representa��o HTML que finaliza o
   *         GroupBox.
   */
  static public String end() {
    return "</fieldset>";
  }

}
