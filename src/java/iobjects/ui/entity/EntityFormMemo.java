package iobjects.ui.entity;

import javax.servlet.http.*;

import iobjects.*;
import iobjects.entity.*;
import iobjects.servlet.*;
import iobjects.ui.*;

/**
 * Representa um controle de entrada de texto no formato memo em um Form
 * associado a uma entidade.
 * @since 2006
 */
public class EntityFormMemo {

  private EntityFormMemo() {
  }

  /**
   * Retorna o script HTML representando o EntityFormMemo.
   * @param facade Facade Fachada.
   * @param entityField EntityField cujas propriedades ir�o gerar
   *                    o FormMemo.
   * @param entityInfo EntityInfo contendo os dados que ser�o exibidos e editados.
   * @param request HttpServletRequest Requisi��o da p�gina atual.
   * @return String Retorna o script HTML representando o EntityFormMemo.
   * @throws Exception Em caso de exce��o na tentativa de obter os valores
   *                   de 'entityInfo'.
   */
  static public String script(Facade             facade,
                              EntityField        entityField,
                              EntityInfo         entityInfo,
                              HttpServletRequest request) throws Exception {
    return script(facade, entityField, entityInfo, request, 0, 0, "", "", false, false);
  }

  /**
   * Retorna o script HTML representando o EntityFormMemo.
   * @param facade Facade Fachada.
   * @param entityField EntityField cujas propriedades ir�o gerar
   *                    o EntityFormMemo.
   * @param entityInfo EntityInfo contendo os dados que ser�o exibidos e editados.
   * @param request HttpServletRequest Requisi��o da p�gina atual.
   * @param height int Altura do EntityFormMemo na p�gina ou 0 (zero) para que ele
   *               se ajuste automaticamente ao seu conteiner.
   * @param width int Largura do EntityFormMemo na p�gina, 0 (zero) para que ele
   *              se ajuste automaticamente ao seu conteiner.
   * @param style String Estilo de formata��o do EntityFormMemo.
   * @param onChangeScript C�digo JavaScript para ser executado quando o usu�rio
   *                       alterar o valor do elemento HTML.
   * @param readOnly boolean True se o EntityFormMemo for somente leitura.
   * @param wysiwyg boolean True para que o FormMemo seja exibido como editor
   *                        HTML.
   * @return String Retorna o script HTML representando o EntityFormMemo.
   * @throws Exception Em caso de exce��o na tentativa de obter os valores
   *                   de 'entityInfo'.
   */
  static public String script(Facade             facade,
                              EntityField        entityField,
                              EntityInfo         entityInfo,
                              HttpServletRequest request,
                              int                height,
                              int                width,
                              String             style,
                              String             onChangeScript,
                              boolean            readOnly,
                              boolean            wysiwyg) throws Exception {
    return script(facade,
                  entityField,
                  entityInfo,
                  request,
                  height,
                  width,
                  style,
                  onChangeScript,
                  readOnly,
                  wysiwyg,
                  "",
                  "");
  }

  /**
   * Retorna o script HTML representando o EntityFormMemo.
   * @param facade Facade Fachada.
   * @param entityField EntityField cujas propriedades ir�o gerar
   *                    o EntityFormMemo.
   * @param entityInfo EntityInfo contendo os dados que ser�o exibidos e editados.
   * @param request HttpServletRequest Requisi��o da p�gina atual.
   * @param height int Altura do EntityFormMemo na p�gina ou 0 (zero) para que ele
   *               se ajuste automaticamente ao seu conteiner.
   * @param width int Largura do EntityFormMemo na p�gina, 0 (zero) para que ele
   *              se ajuste automaticamente ao seu conteiner.
   * @param style String Estilo de formata��o do EntityFormMemo.
   * @param onChangeScript C�digo JavaScript para ser executado quando o usu�rio
   *                       alterar o valor do elemento HTML.
   * @param readOnly boolean True se o EntityFormMemo for somente leitura.
   * @param wysiwyg boolean True para que o FormMemo seja exibido como editor
   *                        HTML.
   * @param imagesPath String Caminho local no servidor onde se encontram as
   *                          imagens para serem inseridas no editor HTML.
   * @param imagesUrl String Url do servidor onde se encontram as imagens para
   *                         serem inseridas no editor HTML.
   * @return String Retorna o script HTML representando o EntityFormMemo.
   * @throws Exception Em caso de exce��o na tentativa de obter os valores
   *                   de 'entityInfo'.
   */
  static public String script(Facade             facade,
                              EntityField        entityField,
                              EntityInfo         entityInfo,
                              HttpServletRequest request,
                              int                height,
                              int                width,
                              String             style,
                              String             onChangeScript,
                              boolean            readOnly,
                              boolean            wysiwyg,
                              String             imagesPath,
                              String             imagesUrl) throws Exception {
    // se est� editando e o campo n�o � edit�vel...
    if (Controller.isEditing(request) && !entityField.getEnabledOnEdit())
      readOnly = true;
    // se est� inserindo e o campo n�o � edit�vel...
    if (Controller.isInserting(request) && !entityField.getEnabledOnInsert())
      readOnly = true;
    // se a formata��o � UPPER_CASE
    if (entityField.getFormat() == Format.FORMAT_UPPER_CASE)
      style = "text-transform:uppercase;" + style;
    // retorna
    return FormMemo.script(facade,
                           entityField.getFieldAlias(),
                           entityField.getFormatedFieldValue(entityInfo),
                           height,
                           width,
                           entityField.getFieldSize(),
                           entityField.getScriptConstraint(),
                           entityField.getScriptConstraintErrorMessage(),
                           style,
                           onChangeScript,
                           readOnly,
                           wysiwyg,
                           imagesPath,
                           imagesUrl);
  }

}
