package iobjects.ui.param;

import iobjects.*;
import iobjects.ui.*;

/**
 * Representa um controle de sele��o de valores provenientes de um Param
 * em um Form.
 */
public class ParamFormSelect {

  private ParamFormSelect() {
  }

  /**
   * Retorna o script HTML representando o controle de sele��o.
   * @param facade Facade Fachada.
   * @param param Param cujas propriedades ir�o gerar o controle de sele��o.
   * @return String Retorna o script HTML representando o controle de sele��o.
   * @throws Exception Em caso de exce��o na verifica��o das op��es e valores
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
   * Retorna o script HTML representando o controle de sele��o.
   * @param facade Facade Fachada.
   * @param param Param cujas propriedades ir�o gerar o controle de sele��o.
   * @param width int Largura do controle de sele��o na p�gina ou 0 (zero) para
   *              que ele se ajuste automaticamente ao seu conteiner.
   * @param style String Estilo de formata��o do ParamFormSelect.
   * @param onChangeScript C�digo JavaScript para ser executado quando o usu�rio
   *                       alterar o valor do elemento HTML.
   * @return String Retorna o script HTML representando o controle de sele��o.
   * @throws Exception Em caso de exce��o na verifica��o das op��es e valores
   *         informados.
   */
  static public String script(Facade facade,
                              Param  param,
                              int    width,
                              String style,
                              String onChangeScript) throws Exception {
    // verifica se o campo ser� somente leitura
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
