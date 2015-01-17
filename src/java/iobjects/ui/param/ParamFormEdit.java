package iobjects.ui.param;

import iobjects.*;
import iobjects.ui.*;

/**
 * Representa um controle de entrada de texto em um Form associado a um Param.
 * @since 2006
 */
public class ParamFormEdit {

  private ParamFormEdit() {
  }

  /**
   * Retorna o script HTML representando o controle de edição.
   * @param facade Facade Fachada.
   * @param param Param cujas propriedades irão gerar o controle de edição.
   * @return String Retorna o script HTML representando o controle de edição.
   * @throws Exception Em caso de exceção na tentativa de obter os valores
   *                   de 'param'.
   */
  static public String script(Facade facade,
                              Param  param) throws Exception {
    return script(facade, param, 0, "", "");
  }

  /**
   * Retorna o script HTML representando o controle de edição.
   * @param facade Facade Fachada.
   * @param param Param cujas propriedades irão gerar o controle de edição.
   * @param width int Largura do controle de edição na página, 0 (zero) para que ele
   *              se ajuste automaticamente ao seu conteiner ou -1 para que
   *              ele seja invisível.
   * @param style String Estilo de formatação do ParamEdit.
   * @param onChangeScript Código JavaScript para ser executado quando o usuário
   *                       alterar o valor do elemento HTML.
   * @return String Retorna o script HTML representando o controle de edição.
   * @throws Exception Em caso de exceção na tentativa de obter os valores
   *                   de 'param'.
   */
  static public String script(Facade facade,
                              Param  param,
                              int    width,
                              String style,
                              String onChangeScript) throws Exception {
    return script(facade, param, width, style, onChangeScript, false);
  }

  /**
   * Retorna o script HTML representando o controle de edição.
   * @param facade Facade Fachada.
   * @param param Param cujas propriedades irão gerar o controle de edição.
   * @param width int Largura do controle de edição na página, 0 (zero) para que ele
   *              se ajuste automaticamente ao seu conteiner ou -1 para que
   *              ele seja invisível.
   * @param style String Estilo de formatação do ParamFormEdit.
   * @param onChangeScript Código JavaScript para ser executado quando o usuário
   *                       alterar o valor do elemento HTML.
   * @param password boolean True para que o elemento HTML seja próprio para
   *                 digitação de senhas.
   * @return String Retorna o script HTML representando o controle de edição.
   * @throws Exception Em caso de exceção na tentativa de obter os valores
   *                   de 'param'.
   */
  static public String script(Facade  facade,
                              Param   param,
                              int     width,
                              String  style,
                              String  onChangeScript,
                              boolean password) throws Exception {
    return script(facade, param, width, style, onChangeScript, false, password);
  }

  /**
   * Retorna o script HTML representando o controle de edição.
   * @param facade Facade Fachada.
   * @param param Param cujas propriedades irão gerar o controle de edição.
   * @param width int Largura do controle de edição na página, 0 (zero) para que ele
   *              se ajuste automaticamente ao seu conteiner ou -1 para que
   *              ele seja invisível.
   * @param style String Estilo de formatação do ParamFormEdit.
   * @param onChangeScript Código JavaScript para ser executado quando o usuário
   *                       alterar o valor do elemento HTML.
   * @param readOnly boolean True se o ParamFormEdit for somente leitura.
   * @param password boolean True para que o elemento HTML seja próprio para
   *                 digitação de senhas.
   * @return String Retorna o script HTML representando o controle de edição.
   * @throws Exception Em caso de exceção na tentativa de obter os valores
   *                   de 'param'.
   */
  static public String script(Facade  facade,
                              Param   param,
                              int     width,
                              String  style,
                              String  onChangeScript,
                              boolean readOnly,
                              boolean password) throws Exception {
    return script(facade,
                  param,
                  width,
                  style,
                  onChangeScript,
                  "",
                  "",
                  "",
                  readOnly,
                  password);
  }

  /**
   * Retorna o script HTML representando o controle de edição.
   * @param facade Facade Fachada.
   * @param param Param cujas propriedades irão gerar o controle de edição.
   * @param width int Largura do controle de edição na página, 0 (zero) para que ele
   *              se ajuste automaticamente ao seu conteiner ou -1 para que
   *              ele seja invisível.
   * @param style String Estilo de formatação do ParamFormEdit.
   * @param onChangeScript Código JavaScript para ser executado quando o usuário
   *                       alterar o valor do elemento HTML.
   * @param onKeyDownScript Código JavaScript para ser executado quando o usuário
   *                       baixar uma tecla dentro do elemento HTML.
   * @param onKeyUpScript Código JavaScript para ser executado quando o usuário
   *                      soltar uma tecla dentro do elemento HTML.
   * @param onKeyPressScript Código JavaScript para ser executado quando o usuário
   *                         pressionar uma tecla dentro do elemento HTML.
   * @param readOnly boolean True se o ParamFormEdit for somente leitura.
   * @param password boolean True para que o elemento HTML seja próprio para
   *                 digitação de senhas.
   * @return String Retorna o script HTML representando o controle de edição.
   * @throws Exception Em caso de exceção na tentativa de obter os valores
   *                   de 'param'.
   */
  static public String script(Facade  facade,
                              Param   param,
                              int     width,
                              String  style,
                              String  onChangeScript,
                              String  onKeyDownScript,
                              String  onKeyUpScript,
                              String  onKeyPressScript,
                              boolean readOnly,
                              boolean password) throws Exception {
    // máscara
    String mask = param.getMask();
    // se não tem máscara...define para o formato conhecido
    if (mask.equals("")) {
      switch (param.getFormat()) {
        case Format.FORMAT_DATE:      mask = "00/00/0000"; break;
        case Format.FORMAT_DATE_TIME: mask = "00/00/0000 00:00"; break;
        case Format.FORMAT_TIME:      mask = "00:00"; break;
        case Format.FORMAT_DOUBLE:    mask = "$"; break;
      } // switch
    } // if
    // se a formatação é UPPER_CASE
    if (param.getFormat() == Format.FORMAT_UPPER_CASE)
      style = "text-transform:uppercase;" + style;

    // tamanho máximo do valor digitado
    int size = param.getSize();
    // se o tamanho da máscara é diferente do tamanho do Param...redefine o tamanho de digitação
    if (mask.length() > size)
      size = mask.length();

    // temos constraint especial de caracteres coringa?
    if (param.getAccordingToMaskChars() &&
        (Param.getParamFile() != null) &&
        (Param.getParamFile().specialConstraints().getMaskChars().length() > 0)) {
      // adiciona a verificação
      onKeyPressScript += "var maskChars = '" + Param.getParamFile().specialConstraints().getMaskChars() + "';"
                        + "for (var i=0; i<maskChars.length; i++) {"
                        +   "if (maskChars.charCodeAt(i) == event.keyCode) {"
                        +     "alert('" + param.getCaption() + " não pode conter os caracteres: ' + maskChars + '.');"
                        +     "return false;"
                        +   "}"
                        + "}";
    } // if
    // constraint do Param
    String scriptConstraint = param.getScriptConstraint();
    String scriptConstraintErrorMessage = param.getScriptConstraintErrorMessage();
    // temos constraint especial de tamanho mínimo?
    if (param.getAccordingToMinimumLength() &&
        (Param.getParamFile() != null) &&
        (Param.getParamFile().specialConstraints().getMinimumLength() > 0)) {
      // adiciona uma constraint...
      if (scriptConstraint.length() > 0)
        scriptConstraint = "(" + scriptConstraint + ") && ";
      scriptConstraint += "((value.length == 0) || (value.length >= " + Param.getParamFile().specialConstraints().getMinimumLength() + "))";
      // adiciona uma mensagem de erro
      if (scriptConstraintErrorMessage.length() > 0)
        scriptConstraintErrorMessage += " ";
      scriptConstraintErrorMessage += param.getCaption() + " deve conter pelo menos " + Param.getParamFile().specialConstraints().getMinimumLength() + " caracteres.";
    } // if

    // retorna
    return FormEdit.script(facade,
                           param.getName(),
                           param.getFormatedValue(),
                           width,
                           size,
                           param.getAlign(),
                           mask,
                           scriptConstraint,
                           scriptConstraintErrorMessage,
                           style,
                           onChangeScript,
                           onKeyDownScript,
                           onKeyUpScript,
                           onKeyPressScript,
                           readOnly,
                           password);
  }

}
