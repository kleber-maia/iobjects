package iobjects.ui.param;

import iobjects.*;
import iobjects.ui.*;

/**
 * Representa um controle de seleção de valores provenientes de um Param
 * em um Form.
 */
public class ParamFormSelect {

  private ParamFormSelect() {
  }

  /**
   * Retorna o script HTML representando o controle de seleção.
   * @param facade Facade Fachada.
   * @param param Param cujas propriedades irão gerar o controle de seleção.
   * @return String Retorna o script HTML representando o controle de seleção.
   * @throws Exception Em caso de exceção na verificação das opções e valores
   *         informados.
   */
  static public String script(Facade facade,
                              Param  param) throws Exception {
    return script(facade,
                  param,
                  0,
                  "",
                  "");
  }

  /**
   * Retorna o script HTML representando o controle de seleção.
   * @param facade Facade Fachada.
   * @param param Param cujas propriedades irão gerar o controle de seleção.
   * @param width int Largura do controle de seleção na página ou 0 (zero) para
   *              que ele se ajuste automaticamente ao seu conteiner.
   * @param style String Estilo de formatação do ParamFormSelect.
   * @param onChangeScript Código JavaScript para ser executado quando o usuário
   *                       alterar o valor do elemento HTML.
   * @return String Retorna o script HTML representando o controle de seleção.
   * @throws Exception Em caso de exceção na verificação das opções e valores
   *         informados.
   */
  static public String script(Facade facade,
                              Param  param,
                              int    width,
                              String style,
                              String onChangeScript) throws Exception {
    // verifica se o campo será somente leitura
    boolean readOnly = false;
    // retorna
    return FormSelect.script(facade,
                             param.getName(),
                             param.getLookupList(),
                             new String[0],
                             param.getValue(),
                             width,
                             0,
                             FormSelect.SELECT_TYPE_SINGLE,
                             param.getScriptConstraint(),
                             param.getScriptConstraintErrorMessage(),
                             style,
                             onChangeScript,
                             readOnly);
  }

}
