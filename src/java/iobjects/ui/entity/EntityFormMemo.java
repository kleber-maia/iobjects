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
   * @param entityField EntityField cujas propriedades irão gerar
   *                    o FormMemo.
   * @param entityInfo EntityInfo contendo os dados que serão exibidos e editados.
   * @param request HttpServletRequest Requisição da página atual.
   * @return String Retorna o script HTML representando o EntityFormMemo.
   * @throws Exception Em caso de exceção na tentativa de obter os valores
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
   * @param entityField EntityField cujas propriedades irão gerar
   *                    o EntityFormMemo.
   * @param entityInfo EntityInfo contendo os dados que serão exibidos e editados.
   * @param request HttpServletRequest Requisição da página atual.
   * @param height int Altura do EntityFormMemo na página ou 0 (zero) para que ele
   *               se ajuste automaticamente ao seu conteiner.
   * @param width int Largura do EntityFormMemo na página, 0 (zero) para que ele
   *              se ajuste automaticamente ao seu conteiner.
   * @param style String Estilo de formatação do EntityFormMemo.
   * @param onChangeScript Código JavaScript para ser executado quando o usuário
   *                       alterar o valor do elemento HTML.
   * @param readOnly boolean True se o EntityFormMemo for somente leitura.
   * @param wysiwyg boolean True para que o FormMemo seja exibido como editor
   *                        HTML.
   * @return String Retorna o script HTML representando o EntityFormMemo.
   * @throws Exception Em caso de exceção na tentativa de obter os valores
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
   * @param entityField EntityField cujas propriedades irão gerar
   *                    o EntityFormMemo.
   * @param entityInfo EntityInfo contendo os dados que serão exibidos e editados.
   * @param request HttpServletRequest Requisição da página atual.
   * @param height int Altura do EntityFormMemo na página ou 0 (zero) para que ele
   *               se ajuste automaticamente ao seu conteiner.
   * @param width int Largura do EntityFormMemo na página, 0 (zero) para que ele
   *              se ajuste automaticamente ao seu conteiner.
   * @param style String Estilo de formatação do EntityFormMemo.
   * @param onChangeScript Código JavaScript para ser executado quando o usuário
   *                       alterar o valor do elemento HTML.
   * @param readOnly boolean True se o EntityFormMemo for somente leitura.
   * @param wysiwyg boolean True para que o FormMemo seja exibido como editor
   *                        HTML.
   * @param imagesPath String Caminho local no servidor onde se encontram as
   *                          imagens para serem inseridas no editor HTML.
   * @param imagesUrl String Url do servidor onde se encontram as imagens para
   *                         serem inseridas no editor HTML.
   * @return String Retorna o script HTML representando o EntityFormMemo.
   * @throws Exception Em caso de exceção na tentativa de obter os valores
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
    // se está editando e o campo não é editável...
    if (Controller.isEditing(request) && !entityField.getEnabledOnEdit())
      readOnly = true;
    // se está inserindo e o campo não é editável...
    if (Controller.isInserting(request) && !entityField.getEnabledOnInsert())
      readOnly = true;
    // se a formatação é UPPER_CASE
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
