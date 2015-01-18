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
 * Utilitário para criação de botões HTML.
 * @since 3.2
 */
public class Button {

  static public int KIND_BIG              = 1;
  static public int KIND_BIG_SELECTED     = 2;
  static public int KIND_DEFAULT          = 3;
  static public int KIND_DEFAULT_SELECTED = 4;
  static public int KIND_TOOLBUTTON       = 5;

  private Button() {
  }

  /**
   * Retorna o script contendo a representação HTML do Button.
   * @param facade Facade Fachada.
   * @param id String Identifação do Button na página.
   * @param caption String Título do Button.
   * @param description String Descrição do Button.
   * @param image String Imagem do Button.
   * @param accessChar String Caractere que poderá ser utilizado junto a tecla
   *                          Alt para acesso rápido ao Button.
   * @param kind int Tipo do Button.
   * @param style String Estilo extra para o elemento HTML do Button.
   * @param onClickScript String Script JavaScript para ser executado pelo Button.
   * @param disabled boolean True para que o Button seja desabilitado.
   * @return String Retorna o script contendo a representação HTML do
   *                Button.
   */
  static public String script(Facade  facade,
                              String  id,
                              String  caption,
                              String  description,
                              String  image,
                              String  accessChar,
                              int     kind,
                              String  style,
                              String  onClickScript,
                              boolean disabled) {
    // traduz
    caption     = facade.getDictionary().translate(caption);
    description = facade.getDictionary().translate(description);
    // se não informou o Id...exceção
    if ((id == null) || id.trim().equals(""))
      throw new RuntimeException(new ExtendedException("iobjects.ui.Button", "script", "Id não informado."));
    // se é o tipo Big...
    if ((kind == KIND_BIG) || (kind == KIND_BIG_SELECTED)) {
      String className = (kind == KIND_BIG ? "bigButton" : "bigButtonSelected");
      return "<button class=\"" + className + "\" " +
                     "type=\"button\" " +
                     "id=\"" + id + "\" " +
                     "title=\"" + description + (!accessChar.equals("") ? " (Alt+" + accessChar.toUpperCase() + ")" : "") + "\" " +
                     "accesskey=\"" + accessChar + "\" " +
                     "style=\"" + style + "\" " +
                     "onclick=\"" + onClickScript + "\" " +
                     "onmouseover=\"className='" + className + "Over'\" " +
                     "onmouseout=\"className='" + className + "'\" " +
                     (disabled ? "disabled=\"disabled\"" : "") + ">" +
              (!image.equals("") ? "<img src=\"" + image + "\" align=\"absmiddle\" />" : "") +
              (!image.equals("") && !caption.equals("") ? "<br />" : "") +
              caption +
              "</button>";
    }
    // se é o tipo Default...
    else if ((kind == KIND_DEFAULT) || (kind == KIND_DEFAULT_SELECTED)) {
      String className = (kind == KIND_DEFAULT ? "button" : "buttonSelected");
      return "<button class=\"" + className + "\"" +
                     "type=\"button\" " +
                     "id=\"" + id + "\" " +
                     "title=\"" + description + (!accessChar.equals("") ? " (Alt+" + accessChar.toUpperCase() + ")" : "") + "\" " +
                     "accesskey=\"" + accessChar + "\" " +
                     "style=\"" + style + "\" " +
                     "onclick=\"" + onClickScript + "\"" +
                     "onmouseover=\"className='" + className + "Over'\" " +
                     "onmouseout=\"className='" + className + "'\" " +
                     (disabled ? "disabled=\"disabled\"" : "") + ">" +
              (!image.equals("") ? "<img src=\"" + image + "\" align=\"absmiddle\" />" : "") +
              (!image.equals("") && !caption.equals("") ? "&nbsp;" : "") +
              caption +
              "</button>";
    }
    // se é do tipo ToolButton...
    else {
      return "<button class=\"ToolButton\" " +
                     "type=\"button\" " +
                     "id=\"" + id + "\" " +
                     "title=\"" + description + (!accessChar.equals("") ? "(Alt+" + accessChar.toUpperCase() + ")" : "") + "\" " +
                     "accesskey=\"" + accessChar + "\" " +
                     "style=\"height=22px; width=100px; " + style + "\" " +
                     "onclick=\"" + onClickScript + "\" " +
                     "onfocus=\"blur()\" " +
                     "onmouseover=\"className='ToolButtonOver'\" " +
                     "onmouseout=\"className='ToolButton'\" " +
                     "onmousedown=\"className='ToolButtonDown'\" " +
                     "onmouseup=\"className='ToolButton'\"" +
                     (disabled ? "disabled=\"disabled\"" : "") + ">" +
              (!image.equals("") ? "<img src=\"" + image + "\" align=\"absmiddle\" />" : "") +
              (!image.equals("") && !caption.equals("") ? "&nbsp;" : "") +
              caption +
             "</button>";
    } // if
  }

}
