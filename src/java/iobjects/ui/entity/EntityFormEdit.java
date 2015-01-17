package iobjects.ui.entity;

import java.util.*;
import javax.servlet.http.*;

import iobjects.*;
import iobjects.entity.*;
import iobjects.servlet.*;
import iobjects.util.*;
import iobjects.ui.*;

/**
 * Representa um controle de entrada de texto em um Form associado a uma entidade.
 */
public class EntityFormEdit {

  private EntityFormEdit() {
  }

  /**
   * Retorna o script HTML representando o FormMemo.
   * @param facade Facade Fachada.
   * @param entityField EntityField cujas propriedades ir�o gerar
   *                    o FormMemo.
   * @param entityInfo EntityInfo contendo os dados que ser�o exibidos e editados.
   * @param request HttpServletRequest Requisi��o da p�gina atual.
   * @return String Retorna o script HTML representando o FormMemo.
   * @throws Exception Em caso de exce��o na tentativa de obter os valores
   *                   de 'entityInfo'.
   */
  static public String script(Facade             facade,
                              EntityField        entityField,
                              EntityInfo         entityInfo,
                              HttpServletRequest request) throws Exception {
    return script(facade, entityField, entityInfo, request, 0, "", "");
  }

  /**
   * Retorna o script HTML representando o FormMemo.
   * @param facade Facade Fachada.
   * @param entityField EntityField cujas propriedades ir�o gerar
   *                    o FormMemo.
   * @param entityInfo EntityInfo contendo os dados que ser�o exibidos e editados.
   * @param request HttpServletRequest Requisi��o da p�gina atual.
   * @param width int Largura do FormMemo na p�gina, 0 (zero) para que ele
   *              se ajuste automaticamente ao seu conteiner ou -1 para que
   *              ele seja invis�vel.
   * @param style String Estilo de formata��o do FormEdit.
   * @param onChangeScript C�digo JavaScript para ser executado quando o usu�rio
   *                       alterar o valor do elemento HTML.
   * @return String Retorna o script HTML representando o FormMemo.
   * @throws Exception Em caso de exce��o na tentativa de obter os valores
   *                   de 'entityInfo'.
   */
  static public String script(Facade             facade,
                              EntityField        entityField,
                              EntityInfo         entityInfo,
                              HttpServletRequest request,
                              int                width,
                              String             style,
                              String             onChangeScript) throws Exception {
    return script(facade, entityField, entityInfo, request, width, style, onChangeScript, false, false);
  }

  /**
   * Retorna o script HTML representando o FormMemo.
   * @param facade Facade Fachada.
   * @param entityField EntityField cujas propriedades ir�o gerar
   *                    o FormMemo.
   * @param entityInfo EntityInfo contendo os dados que ser�o exibidos e editados.
   * @param request HttpServletRequest Requisi��o da p�gina atual.
   * @param width int Largura do FormMemo na p�gina, 0 (zero) para que ele
   *              se ajuste automaticamente ao seu conteiner ou -1 para que
   *              ele seja invis�vel.
   * @param style String Estilo de formata��o do FormEdit.
   * @param onChangeScript C�digo JavaScript para ser executado quando o usu�rio
   *                       alterar o valor do elemento HTML.
   * @param readOnly boolean True para que o elemento HTML seja somente leitura.
   * @param password boolean True para que o elemento HTML seja pr�prio para
   *                 digita��o de senhas.
   * @return String Retorna o script HTML representando o FormMemo.
   * @throws Exception Em caso de exce��o na tentativa de obter os valores
   *                   de 'entityInfo'.
   */
  static public String script(Facade             facade,
                              EntityField        entityField,
                              EntityInfo         entityInfo,
                              HttpServletRequest request,
                              int                width,
                              String             style,
                              String             onChangeScript,
                              boolean            readOnly,
                              boolean            password) throws Exception {
    // se est� editando e o campo n�o � edit�vel...
    if (Controller.isEditing(request) && !entityField.getEnabledOnEdit())
      readOnly = true;
    // se est� inserindo e o campo n�o � edit�vel...
    if (Controller.isInserting(request) && !entityField.getEnabledOnInsert())
      readOnly = true;

    // m�scara
    String mask = entityField.getMask();
    // se n�o tem m�scara...define para o formato conhecido
    if (mask.equals("")) {
      switch (entityField.getFormat()) {
        case Format.FORMAT_DATE:      mask = "00/00/0000"; break;
        case Format.FORMAT_DATE_TIME: mask = "00/00/0000 00:00"; break;
        case Format.FORMAT_TIME:      mask = "00:00"; break;
        case Format.FORMAT_DOUBLE:    mask = "$"; break;
      } // switch
    } // if
    // se a formata��o � UPPER_CASE
    if (entityField.getFormat() == Format.FORMAT_UPPER_CASE)
      style = "text-transform:uppercase;" + style;

    // tamanho m�ximo do valor digitado
    int size = entityField.getFieldSize();
    // se o tamanho da m�scara � diferente do tamanho do campo...redefine o tamanho de digita��o
    if (mask.length() > size)
      size = mask.length();

    // retorna
    return FormEdit.script(facade,
                           entityField.getFieldAlias(),
                           entityField.getFormatedFieldValue(entityInfo),
                           width,
                           size,
                           entityField.getAlign(),
                           mask,
                           entityField.getScriptConstraint(),
                           entityField.getScriptConstraintErrorMessage(),
                           style,
                           onChangeScript,
                           readOnly,
                           password);
  }

}
