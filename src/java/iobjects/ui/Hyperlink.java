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
 * Utilitário para criação de hyperlinks HTML.
 */
public class Hyperlink {

  private Hyperlink() {
  }
  
  /**
   * Retorna o script contendo a representação HTML do Hyperlink.
   * @param facade Facade Fachada.
   * @param id String Identifação do Hyperlink na página.
   * @param caption String Título do Hyperlink.
   * @param description String Descrição do Hyperlink.
   * @param image String Imagem do Hyperlink.
   * @param href String Referência para ser chamada pelo hyperlink.
   * @param target String Frame de destino da referência.
   * @param onClickScript String Script JavaScript para ser executado quando o
   *                      Hyperlink receber um clique.
   * @param disabled boolean True se o hyperlink deve estar desabilitado.
   * @return String Retorna o script contendo a representação HTML do Hyperlink.
   */
  static public String script(Facade  facade,
                              String  id,
                              String  caption,
                              String  description,
                              String  image,
                              String  href,
                              String  target,
                              String  onClickScript,
                              boolean disabled) {
    // se não informou o Id...exceção
    if ((id == null) || id.trim().equals(""))
      throw new RuntimeException(new ExtendedException("iobjects.ui.Hyperlink", "script", "Id não informado."));
    // se é mobile ou tablet...
    if (facade.getBrowserMobile() || facade.getBrowserTablet())
      return caption;
    else
      return (!image.equals("") ? "<img src=\"" + image + "\" align=\"absmiddle\" />&nbsp;" : "") +
             "<a id=\"" + id + "\" " +
                (disabled ? "" : "href=\"" + href + "\" ") +
                "target=\"" + target + "\" " +
                "title=\"" + description + "\" " +
                "onClick=\"" + onClickScript + "\" " +
                (disabled ? "disabled=\"disabled\"" : "") + ">" +
                caption +
                "</a>";
  }

}
