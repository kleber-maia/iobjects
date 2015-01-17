package iobjects.ui;

import iobjects.*;
import iobjects.util.*;

/**
 * Representa um controle de seleção de valores em um Form. Os valores podem
 * ser exibidos na forma de árvore e o usuário pode realizar pesquisas através
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
     * Construtor padrão.
     * @param caption String Título da opção.
     * @param value String Valor da opção.
     * @param level int Nível hierárquico da opção.
     */
    public Option(String caption,
                  String value,
                  int    level) {
      this.caption = caption;
      this.value   = value;
      this.level   = level;
    }
    /**
     * Construtor padrão.
     * @param caption String Título da opção.
     * @param value String Valor da opção.
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
   * Retorna o script contendo o código JavaScript para alteração do valor
   * do FormSelectEx na página.
   * @param facade Fachada.
   * @param id Identificação do FormSelectEx na página.
   * @param value Valor (entre '') ou nome da variável em JavaScript que será
   *              passado como novo valor do FormSelectEx na página.
   * @return Retorna o script contendo o código JavaScript para alteração do valor
   *         do FormSelectEx na página.
   */
  static public String changeValue(Facade facade,
                                   String id,
                                   String value) {
    return "FormSelectEx_changeValue(" + id + "id, " + value + ");";
  }

  /**
   * Retorna o script contendo o código JavaScript para limpeza do valor
   * selecionado no FormSelectEx.
   * @param facade Facade Fachada.
   * @param id String Identificação do FormSelectEx na página.
   * @return String Retorna o script contendo o código JavaScript para limpeza
   *                do valor selecionado no FormSelectEx.
   */
  static public String clear(Facade   facade,
                             String   id) {
    return "FormSelectEx_clear(" + id + "id);";
  }

  /**
   * Retorna o script HTML representando o controle de seleção de valores.
   * @param facade Facade Fachada.
   * @param id String Identificação do FormSelectEx na página.
   * @param options Option[] Lista de opções do FormSelectEx.
   * @param selectedValue String Valor referente à opção selecionada por padrão.
   * @param width int Largura do FormSelect na página ou 0 (zero) para que ele
   *              se ajuste automaticamente ao seu conteiner.
   * @param constraint String Script JavaScript de validação do valor selecionado
   *                   no FormSelectEx.
   * @param constraintMessage String Mensagem de validação para ser exibida
   *                          ao usuário.
   * @param style String Estilo de formatação do FormSelectEx.
   * @param onChangeScript Código JavaScript para ser executado quando o usuário
   *                       alterar o valor do elemento HTML.
   * @param readOnly boolean True se o FormSelectEx for somente leitura.
   * @return String Retorna o script HTML representando o controle de seleção de
   *         valores.
   * @throws Exception Em caso de exceção na verificação das opções e valores
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
    // se não informou o Id...exceção
    if ((id == null) || id.trim().equals(""))
      throw new ExtendedException(FormSelectEx.class.getName(), "script", "Id não informado.");
    // nossas opções
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
