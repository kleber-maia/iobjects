package iobjects.ui;

import iobjects.*;
import iobjects.util.*;

/**
 * Representa um controle de entrada de texto em um Form.
 */
public class FormEdit {

  static public final int ALIGN_LEFT   = Align.ALIGN_LEFT;
  static public final int ALIGN_CENTER = Align.ALIGN_CENTER;
  static public final int ALIGN_RIGHT  = Align.ALIGN_RIGHT;

  static private final String ON_CHANGE_SCRIPT = "style.color = '#0000FF';";

  private FormEdit() {
  }

  /**
   * Retorna o script HTML representando o FormEdit.
   * @param facade Facade Fachada.
   * @param id String Identificação do FormEdit na página.
   * @param value String Valor inicial do FormEdit.
   * @param hidden Boolean true para que o FormEdit seja oculto.
   * @return String Retorna o script HTML representando o FormEdit.
   */
  static public String script(Facade  facade,
                              String  id,
                              String  value,
                              boolean hidden) {
    return script(facade,
                  id,
                  value,
                  -1,
                  0,
                  ALIGN_LEFT,
                  "",
                  "",
                  "",
                  "",
                  "",
                  false);
  }

  /**
   * Retorna o script HTML representando o FormEdit.
   * @param facade Facade Fachada.
   * @param id String Identificação do FormEdit na página.
   * @param value String Valor inicial do FormEdit.
   * @param width int Largura do FormEdit na página ou 0 (zero) para que ele
   *              se ajuste automaticamente ao seu conteiner.
   * @param size int Tamanho máximo do valor digitado no FormEdit ou 0 (zero)
   *             para tamanho ilimitado.
   * @return String Retorna o script HTML representando o FormEdit.
   */
  static public String script(Facade  facade,
                              String id,
                              String value,
                              int    width,
                              int    size) {
    return script(facade,
                  id,
                  value,
                  width,
                  size,
                  ALIGN_LEFT,
                  "",
                  "",
                  "",
                  "",
                  "",
                  false);
  }

  /**
   * Retorna o script HTML representando o FormEdit.
   * @param facade Facade Fachada.
   * @param id String Identificação do FormEdit na página.
   * @param value String Valor inicial do FormEdit.
   * @param width int Largura do FormEdit na página ou 0 (zero) para que ele
   *              se ajuste automaticamente ao seu conteiner.
   * @param constraint String Script JavaScript de validação do valor digitado
   *                   no FormEdit.
   * @param constraintMessage String Mensagem de validação para ser exibida
   *                          ao usuário.
   * @return String Retorna o script HTML representando o FormEdit.
   */
  static public String script(Facade  facade,
                              String id,
                              String value,
                              int    width,
                              String constraint,
                              String constraintMessage) {
    return script(facade,
                  id,
                  value,
                  width,
                  0,
                  ALIGN_LEFT,
                  "",
                  constraint,
                  constraintMessage,
                  "",
                  "",
                  false);
  }

  /**
   * Retorna o script HTML representando o FormEdit.
   * @param facade Facade Fachada.
   * @param id String Identificação do FormEdit na página.
   * @param value String Valor inicial do FormEdit.
   * @param width int Largura do FormEdit na página ou 0 (zero) para que ele
   *              se ajuste automaticamente ao seu conteiner.
   * @param size int Tamanho máximo do valor digitado no FormEdit ou 0 (zero)
   *             para tamanho ilimitado.
   * @param align int Alinhamento do valor do FormEdit.
   * @param mask String Máscara de edição do valor do FormEdit. Informe apenas "$"
   *                    para utilizar a máscara de valores numéricos com decimais.
   * @param constraint String Script JavaScript de validação do valor digitado
   *                   no FormEdit.
   * @param constraintMessage String Mensagem de validação para ser exibida
   *                          ao usuário.
   * @param style String Estilo de formatação do FormEdit.
   * @param onChangeScript Código JavaScript para ser executado quando o usuário
   *                       alterar o valor do elemento HTML.
   * @param readOnly boolean True se o FormEdit for somente leitura.
   * @return String Retorna o script HTML representando o FormEdit.
   */
  static public String script(Facade  facade,
                              String  id,
                              String  value,
                              int     width,
                              int     size,
                              int     align,
                              String  mask,
                              String  constraint,
                              String  constraintMessage,
                              String  style,
                              String  onChangeScript,
                              boolean readOnly) {
    return script(facade,
                  id,
                  value,
                  width,
                  size,
                  align,
                  mask,
                  constraint,
                  constraintMessage,
                  style,
                  onChangeScript,
                  readOnly,
                  false);
  }

  /**
   * Retorna o script HTML representando o FormEdit.
   * @param facade Facade Fachada.
   * @param id String Identificação do FormEdit na página.
   * @param value String Valor inicial do FormEdit.
   * @param width int Largura do FormEdit na página ou 0 (zero) para que ele
   *              se ajuste automaticamente ao seu conteiner.
   * @param size int Tamanho máximo do valor digitado no FormEdit ou 0 (zero)
   *             para tamanho ilimitado.
   * @param align int Alinhamento do valor do FormEdit.
   * @param mask String Máscara de edição do valor do FormEdit. Informe apenas "$"
   *                    para utilizar a máscara de valores numéricos com decimais.
   * @param constraint String Script JavaScript de validação do valor digitado
   *                   no FormEdit.
   * @param constraintMessage String Mensagem de validação para ser exibida
   *                          ao usuário.
   * @param style String Estilo de formatação do FormEdit.
   * @param onChangeScript Código JavaScript para ser executado quando o usuário
   *                       alterar o valor do elemento HTML.
   * @param readOnly boolean True se o FormEdit for somente leitura.
   * @param password boolean True se o FormEdit for próprio para digitação de
   *                 senha.
   * @return String Retorna o script HTML representando o FormEdit.
   */
  static public String script(Facade  facade,
                              String  id,
                              String  value,
                              int     width,
                              int     size,
                              int     align,
                              String  mask,
                              String  constraint,
                              String  constraintMessage,
                              String  style,
                              String  onChangeScript,
                              boolean readOnly,
                              boolean password) {
    return script(facade,
                  id,
                  value,
                  width,
                  size,
                  align,
                  mask,
                  constraint,
                  constraintMessage,
                  style,
                  onChangeScript,
                  "",
                  "",
                  "",
                  readOnly,
                  password);
  }

  /**
   * Retorna o script HTML representando o FormEdit.
   * @param facade Facade Fachada.
   * @param id String Identificação do FormEdit na página.
   * @param value String Valor inicial do FormEdit.
   * @param width int Largura do FormEdit na página ou 0 (zero) para que ele
   *              se ajuste automaticamente ao seu conteiner.
   * @param size int Tamanho máximo do valor digitado no FormEdit ou 0 (zero)
   *             para tamanho ilimitado.
   * @param align int Alinhamento do valor do FormEdit.
   * @param mask String Máscara de edição do valor do FormEdit. Informe apenas "$"
   *                    para utilizar a máscara de valores numéricos com decimais.
   * @param constraint String Script JavaScript de validação do valor digitado
   *                   no FormEdit.
   * @param constraintMessage String Mensagem de validação para ser exibida
   *                          ao usuário.
   * @param style String Estilo de formatação do FormEdit.
   * @param onChangeScript Código JavaScript para ser executado quando o usuário
   *                       alterar o valor do elemento HTML.
   * @param onKeyDownScript Código JavaScript para ser executado quando o usuário
   *                       baixar uma tecla dentro do elemento HTML.
   * @param onKeyUpScript Código JavaScript para ser executado quando o usuário
   *                      soltar uma tecla dentro do elemento HTML.
   * @param onKeyPressScript Código JavaScript para ser executado quando o usuário
   *                         pressionar uma tecla dentro do elemento HTML.
   * @param readOnly boolean True se o FormEdit for somente leitura.
   * @param password boolean True se o FormEdit for próprio para digitação de
   *                 senha.
   * @return String Retorna o script HTML representando o FormEdit.
   */
  static public String script(Facade  facade,
                              String  id,
                              String  value,
                              int     width,
                              int     size,
                              int     align,
                              String  mask,
                              String  constraint,
                              String  constraintMessage,
                              String  style,
                              String  onChangeScript,
                              String  onKeyDownScript,
                              String  onKeyUpScript,
                              String  onKeyPressScript,
                              boolean readOnly,
                              boolean password) {
    // se não informou o Id...exceção
    if ((id == null) || id.trim().equals(""))
      throw new RuntimeException(new ExtendedException(FormEdit.class.getName(), "script", "Id não informado."));
    // nosso resultado
    StringBuffer result = new StringBuffer("");
    // se vamos editar uma data...
    boolean isDate = mask.equals("00/00/0000") && (width >= 0);
    // se é uma data...inicia a tabela
    if (isDate)
      result.append("<table cellpadding=0 cellspacing=0 style=\"width:" + (width > 0 ? width + "px" : "100%") + "; \"><tr><td style=\"width:auto;\">");
    // inicia o input
    result.append("<input type=\"" + (width >= 0 ? (password ? "password" : "text") : "hidden") + "\" ");
    // identificação
    result.append("id=\"" + id + "\" name=\"" + id + "\" ");
    // valor
    result.append("value=\"" + value + "\" ");
    // se temos máscara...
    /*
     * antigo
    if (!mask.equals("")) {
      onKeyUpScript    += "return inputMask(this, event, '" + mask + "', '0');";
      onKeyPressScript += "return validateMaskInput(this, event);";
    }
    // se temos alinhamento à direita, assume q nossa formatação é numérica
    else if (align == ALIGN_RIGHT) {
      onKeyPressScript += "return validateNumericInput(this, event);";
    } // if
    */
    // constraint
    if (!constraint.equals(""))
      result.append("ondblclick=\"if (eval(" + constraint + ")) { return true; } else { alert('" + constraintMessage + "'); return false; }\" ");
    // alinhamento
    String textAlign = (align == ALIGN_RIGHT ? "text-align:right; " : ""); // result.append("dir=\"" + (align == ALIGN_RIGHT ? "rtl" : "ltr") + "\" ");
    // tamanho máximo do texto
    if (size > 0)
      result.append("maxlength=\"" + size + "\" ");
    // estilo
    result.append("style=\"width:" + (width > 0 ? width + "px" : "100%") + "; " + textAlign + style + "\" ");
    // evento onChange...troca a cor da fonte quando o usuário alterar
    result.append("onchange=\"" + ON_CHANGE_SCRIPT + onChangeScript + "\" ");
    // eventos onKeyDown, onKeyUp e onKeyPress
    result.append("onkeydown=\"" + onKeyDownScript + "\" ");
    result.append("onkeyup=\"" + onKeyUpScript + "\" ");
    result.append("onkeypress=\"" + onKeyPressScript + "\" ");
    // é somente leitura?
    if (readOnly)
      result.append("readOnly=\"readOnly\" ");
    // termina
    result.append("></input>");
    // se é uma data...termina a tabela
    if (isDate) {
      result.append("</td><td style=\"width:22px;\">");
      result.append(Button.script(facade, "button" + id, "", "Mostra o calendário.", "images/calendar16x16.png", "", Button.KIND_DEFAULT, "width:22px; height:20px;", "div" + id + ".select(" + id + ", '" + id + "','dd/MM/yyyy'); return false;", readOnly));
      result.append("</td></tr></table>");
      result.append("<div class=\"calendar\" id=\"div" + id + "\" style=\"position:absolute; visibility:hidden;\"></div>");
      result.append("<script type=text/javascript>");
      result.append(  "var div" + id + " = new CalendarPopup(\"div" + id + "\");");
      result.append("</script>");
    } // if
    // se temos máscara...
    if (mask.equals("$")) {
      result.append("<script type=text/javascript>");
      result.append(  "$('#" + id + "').maskMoney({thousands:'.', decimal:',', allowZero:true, allowNegative:true});");
      result.append("</script>");
    }
    else if (!mask.equals("")) {
      result.append("<script type=text/javascript>");
      result.append(  "$('#" + id + "').inputmask('" + mask.replaceAll("0", "9") + "');");
      result.append("</script>");
    } // if
    // retorna
    return result.toString();
  }

}
