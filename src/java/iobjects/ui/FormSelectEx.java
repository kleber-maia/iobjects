package iobjects.ui;

import iobjects.*;
import iobjects.util.*;

/**
 * Representa um controle de sele��o de valores em um Form. Os valores podem
 * ser exibidos na forma de �rvore e o usu�rio pode realizar pesquisas atrav�s
 * do teclado.
 * @since 2006 R1
 */
public class FormSelectEx {

  static public  final String DEFAULT_CURSOR_SCRIPT = "document.body.style.cursor='default';";
  static public  final String BUTTON = "Button";

  static private final String ON_CHANGE_SCRIPT = ".style.color='#0000FF';";

  static public class Option {
    private String caption  = "";
    private int    level    = 0;
    private String value    = "";
    /**
     * Construtor padr�o.
     * @param caption String T�tulo da op��o.
     * @param value String Valor da op��o.
     * @param level int N�vel hier�rquico da op��o.
     */
    public Option(String caption,
                  String value,
                  int    level) {
      this.caption = caption;
      this.value   = value;
      this.level   = level;
    }
    /**
     * Construtor padr�o.
     * @param caption String T�tulo da op��o.
     * @param value String Valor da op��o.
     */
    public Option(String caption,
                  String value) {
      this.caption  = caption;
      this.value    = value;
    }
    public String getCaption()  {return caption;}
    public int    getLevel()    {return level;}
    public String getValue()    {return value;}
  }

  private FormSelectEx() {
  }

  /**
   * Retorna o script contendo o c�digo JavaScript para altera��o do valor
   * do FormSelectEx na p�gina.
   * @param facade Fachada.
   * @param id Identifica��o do FormSelectEx na p�gina.
   * @param value Valor (entre '') ou nome da vari�vel em JavaScript que ser�
   *              passado como novo valor do FormSelectEx na p�gina.
   * @return Retorna o script contendo o c�digo JavaScript para altera��o do valor
   *         do FormSelectEx na p�gina.
   */
  static public String changeValue(Facade facade,
                                   String id,
                                   String value) {
    return "FormSelectEx_changeValue(" + id + "id, " + value + ");";
  }

  /**
   * Retorna o script contendo o c�digo JavaScript para limpeza do valor
   * selecionado no FormSelectEx.
   * @param facade Facade Fachada.
   * @param id String Identifica��o do FormSelectEx na p�gina.
   * @return String Retorna o script contendo o c�digo JavaScript para limpeza
   *                do valor selecionado no FormSelectEx.
   */
  static public String clear(Facade   facade,
                             String   id) {
    return "FormSelectEx_clear(" + id + "id);";
  }

  /**
   * Retorna o script HTML representando o controle de sele��o de valores.
   * @param facade Facade Fachada.
   * @param id String Identifica��o do FormSelectEx na p�gina.
   * @param options Option[] Lista de op��es do FormSelectEx.
   * @param selectedValue String Valor referente � op��o selecionada por padr�o.
   * @param width int Largura do FormSelect na p�gina ou 0 (zero) para que ele
   *              se ajuste automaticamente ao seu conteiner.
   * @param constraint String Script JavaScript de valida��o do valor selecionado
   *                   no FormSelectEx.
   * @param constraintMessage String Mensagem de valida��o para ser exibida
   *                          ao usu�rio.
   * @param style String Estilo de formata��o do FormSelectEx.
   * @param onChangeScript C�digo JavaScript para ser executado quando o usu�rio
   *                       alterar o valor do elemento HTML.
   * @param readOnly boolean True se o FormSelectEx for somente leitura.
   * @return String Retorna o script HTML representando o controle de sele��o de
   *         valores.
   * @throws Exception Em caso de exce��o na verifica��o das op��es e valores
   *                   informados.
   */
  static public String script(Facade   facade,
                              String   id,
                              Option[] options,
                              String   selectedValue,
                              int      width,
                              String   constraint,
                              String   constraintMessage,
                              String   style,
                              String   onChangeScript,
                              boolean  readOnly) throws Exception {
    // se n�o informou o Id...exce��o
    if ((id == null) || id.trim().equals(""))
      throw new ExtendedException(FormSelectEx.class.getName(), "script", "Id n�o informado.");
    // nossas op��es
    StringBuffer strOptions = new StringBuffer();
    for (int i=0; i<options.length; i++) {
      Option option = options[i];
      strOptions.append((i > 0 ? "," : "") + "[\"" + option.getCaption() + "\",\"" + option.getValue() + "\"," + option.getLevel() + "]");
    } // for
    // nosso retorno
    StringBuffer result = new StringBuffer();
    result.append("<script type=\"text/javascript\">"
                +   "FormSelectEx_create(\"" + id + "\","
                +                        width + ","
                +                        "\"" + constraint + "\","
                +                        "\"" + constraintMessage + "\","
                +                        "\"" + onChangeScript + "\","
                +                        readOnly
                +                      ");"
                +  "FormSelectEx_setOptions(\"" + id + "\",[" + strOptions.toString() + "],\"" + selectedValue + "\");"
                + "</script>");
    // retorna
    return result.toString();
  }

}
