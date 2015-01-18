/*
The MIT License (MIT)

Copyright (c) 2008 Kleber Maia de Andrade

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/   
package iobjects.ui;

import iobjects.*;
import iobjects.util.*;

/**
 * Representa um controle de entrada de texto no formato memo em um Form. Tamb�m
 * pode ser exibido no formato "wysiwyg", permitindo que sejam alteradas fontes
 * e cores, inseridas tabelas, imagens e editado o c�digo fonte em HTML.
 * @since 2006
 */
public class FormMemo {

  static public final String DEFAULT_CURSOR_SCRIPT = "document.body.style.cursor='default';";

  static private final String ON_CHANGE_SCRIPT = "style.color = '#0000FF';";

  private FormMemo() {
  }

  /**
   * Retorna o script HTML representando o FormMemo.
   * @param facade Facade Fachada.
   * @param id String Identifica��o do FormMemo na p�gina.
   * @param value String Valor inicial do FormMemo.
   * @param height int Altura do FormMemo na p�gina ou 0 (zero) para que ele
   *               se ajuste automaticamente ao seu conteiner.
   * @param width int Largura do FormMemo na p�gina ou 0 (zero) para que ele
   *              se ajuste automaticamente ao seu conteiner.
   * @param size int Tamanho m�ximo do valor digitado no FormMemo ou 0 (zero)
   *             para tamanho ilimitado.
   * @return String Retorna o script HTML representando o FormMemo.
   */
  static public String script(Facade  facade,
                              String id,
                              String value,
                              int    height,
                              int    width,
                              int    size) {
    return script(facade,
                  id,
                  value,
                  height,
                  width,
                  size,
                  "",
                  "",
                  "",
                  "",
                  false,
                  false);
  }

  /**
   * Retorna o script HTML representando o FormMemo.
   * @param facade Facade Fachada.
   * @param id String Identifica��o do FormMemo na p�gina.
   * @param value String Valor inicial do FormMemo.
   * @param height int Altura do FormMemo na p�gina ou 0 (zero) para que ele
   *               se ajuste automaticamente ao seu conteiner.
   * @param width int Largura do FormMemo na p�gina ou 0 (zero) para que ele
   *              se ajuste automaticamente ao seu conteiner.
   * @param constraint String Script JavaScript de valida��o do valor digitado
   *                   no FormMemo.
   * @param constraintMessage String Mensagem de valida��o para ser exibida
   *                          ao usu�rio.
   * @return String Retorna o script HTML representando o FormMemo.
   */
  static public String script(Facade  facade,
                              String id,
                              String value,
                              int    height,
                              int    width,
                              String constraint,
                              String constraintMessage) {
    return script(facade,
                  id,
                  value,
                  height,
                  width,
                  0,
                  constraint,
                  constraintMessage,
                  "",
                  "",
                  false,
                  false);
  }

  /**
   * Retorna o script HTML representando o FormMemo.
   * @param facade Facade Fachada.
   * @param id String Identifica��o do FormMemo na p�gina.
   * @param value String Valor inicial do FormMemo.
   * @param height int Altura do FormMemo na p�gina ou 0 (zero) para que ele
   *              se ajuste automaticamente ao seu conteiner.
   * @param width int Largura do FormMemo na p�gina ou 0 (zero) para que ele
   *              se ajuste automaticamente ao seu conteiner.
   * @param size int Tamanho m�ximo do valor digitado no FormMemo ou 0 (zero)
   *             para tamanho ilimitado.
   * @param constraint String Script JavaScript de valida��o do valor digitado
   *                   no FormMemo.
   * @param constraintMessage String Mensagem de valida��o para ser exibida
   *                          ao usu�rio.
   * @param style String Estilo de formata��o do FormMemo.
   * @param onChangeScript C�digo JavaScript para ser executado quando o usu�rio
   *                       alterar o valor do elemento HTML.
   * @param readOnly boolean True se o FormMemo for somente leitura.
   * @param wysiwyg boolean True para que o FormMemo seja exibido como editor
   *                        HTML.
   * @return String Retorna o script HTML representando o FormMemo.
   */
  static public String script(Facade  facade,
                              String  id,
                              String  value,
                              int     height,
                              int     width,
                              int     size,
                              String  constraint,
                              String  constraintMessage,
                              String  style,
                              String  onChangeScript,
                              boolean readOnly,
                              boolean wysiwyg) {
    return script(facade,
                  id,
                  value,
                  height,
                  width,
                  size,
                  constraint,
                  constraintMessage,
                  style,
                  onChangeScript,
                  readOnly,
                  wysiwyg,
                  "",
                  "");
  }

  /**
   * Retorna o script HTML representando o FormMemo.
   * @param facade Facade Fachada.
   * @param id String Identifica��o do FormMemo na p�gina.
   * @param value String Valor inicial do FormMemo.
   * @param height int Altura do FormMemo na p�gina ou 0 (zero) para que ele
   *              se ajuste automaticamente ao seu conteiner.
   * @param width int Largura do FormMemo na p�gina ou 0 (zero) para que ele
   *              se ajuste automaticamente ao seu conteiner.
   * @param size int Tamanho m�ximo do valor digitado no FormMemo ou 0 (zero)
   *             para tamanho ilimitado.
   * @param constraint String Script JavaScript de valida��o do valor digitado
   *                   no FormMemo.
   * @param constraintMessage String Mensagem de valida��o para ser exibida
   *                          ao usu�rio.
   * @param style String Estilo de formata��o do FormMemo.
   * @param onChangeScript C�digo JavaScript para ser executado quando o usu�rio
   *                       alterar o valor do elemento HTML.
   * @param readOnly boolean True se o FormMemo for somente leitura.
   * @param wysiwyg boolean True para que o FormMemo seja exibido como editor
   *                        HTML.
   * @param imagesPath String Caminho local no servidor onde se encontram as
   *                          imagens para serem inseridas no editor HTML.
   * @param imagesUrl String Url do servidor onde se encontram as imagens para
   *                         serem inseridas no editor HTML.
   * @return String Retorna o script HTML representando o FormMemo.
   */
  static public String script(Facade  facade,
                              String  id,
                              String  value,
                              int     height,
                              int     width,
                              int     size,
                              String  constraint,
                              String  constraintMessage,
                              String  style,
                              String  onChangeScript,
                              boolean readOnly,
                              boolean wysiwyg,
                              String  imagesPath,
                              String  imagesUrl) {
    // se n�o informou o Id...exce��o
    if ((id == null) || id.trim().equals(""))
      throw new RuntimeException(new ExtendedException("iobjects.ui.FormMemo", "script", "Id n�o informado."));
    // nosso resultado
    StringBuffer result = new StringBuffer("");
    // inicia o textarea
    result.append("<textarea ");
    // identifica��o
    result.append("id=\"" + id + "\" name=\"" + id + "\" ");
    // constraint
    if (!constraint.equals("")) {
      // evento de verifica��o da constraint
      result.append("onDblClick=\"if (!(" + constraint + ")) {alert('" + constraintMessage + "'); " + DEFAULT_CURSOR_SCRIPT + " return false;} else return true;\" ");
    } // if
    // tamanho m�ximo do texto
    if (size > 0)
      result.append("maxlength=\"" + size + "\" ");
    // estilo
    result.append("style=\"width:" + (width > 0 ? width + "px" : "100%") + "; height:" + (height > 0 ? height + "px" : "100%") + "; " + style + "\" ");
    // evento onChange...troca a cor da fonte quando o usu�rio alterar
    result.append("onChange=\"" + ON_CHANGE_SCRIPT + onChangeScript + "\" ");
    // evento onKeyPress: verifica se o tamanho m�ximo foi alcan�ado descarta o que foi digitado
    //result.append("onKeyPress=\"if ((maxLength > 0) && (value.length >= maxLength)) {return false;}\" ");
    // evento onKeyUp: verifica se o tamanho m�ximo foi alcan�ado e apaga o sobressalente
    //result.append("onKeyUp=\"if ((maxLength > 0) && (value.length > maxLength)) {value = value.substring(0, maxLength);}\" ");
    // � somente leitura?
    if (readOnly)
      result.append("readOnly=\"readOnly\" ");
    // termina
    result.append(">" + value + "</textarea>");
    // se vamos usar o editor wysiwyg...
    if (wysiwyg) {
      // caminho e url das imagens para serem inseridas no editor wysiwyg
      result.append("<script type=\"text/javascript\">");
      result.append(  "$(document).ready(function() { ");
      result.append(    "var " + id + "imagesPath = '" + imagesPath.replace('\\', '/') + "'; ");
      result.append(    "var " + id + "imagesUrl = '" + imagesUrl.replace('\\', '/') + "'; ");
      result.append(    "var " + id + "height = $('#" + id + "').height() - 34; ");
      result.append(    "var " + id + "editor = CKEDITOR.replace('" + id + "', {allowedContent: true, height:" + id + "height}); ");
      result.append(  "});");
      result.append("</script>");
    } // if
    // retorna
    return result.toString();
  }

}
