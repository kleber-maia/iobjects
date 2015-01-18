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
package iobjects.util.print;

import java.util.*;

/**
 * Comp�e um formul�rio para impress�o do tipo pr�-impresso.
 */
public class FormComposer {

  private Vector lines = new Vector();

  /**
   * Construtor padr�o.
   */
  public FormComposer() {
  }

  /**
   * Adiciona a linha referenciada por 'line'.
   * @param line Linha que se deseja incluir ao formul�rio.
   */
  public void addLine(FormLine line) {
    lines.add(line);
  }

  /**
   * Apaga as linhas adicionadas ao formul�rio.
   */
  public void clear() {
    lines.clear();
  }

  /**
   * Retorna a linha da posi��o especificada por 'index'.
   * @param index Posi��o da linha que se deseja retornar.
   * @return Retorna a linha da posi��o especificada por 'index'.
   */
  public FormLine getLine(int index) {
    return (FormLine)lines.get(index);
  }

  /**
   * Exclui a linha da posi��o especificada por 'index';
   * @param index Posi��o da linha que se deseja excluir.
   */
  public void removeLine(int index) {
    lines.remove(index);
  }

  /**
   * Retorna o script de impress�o contendo as linhas adicionadas ao formul�rio.
   * @param scriptStartTag String contendo a tag que ser� inclu�da antes de
   *                       cada linha do formul�rio.
   * @param scriptEndTag String contendo a tag que ser� inclu�da ap�s cada linha
   *                     do formul�rio.
   * @return Retorna o script de impress�o contendo as linhas adicionadas ao
   *         formul�rio.
   */
  public String script(String scriptStartTag,
                       String scriptEndTag) {
    // nosso resultado
    StringBuffer result = new StringBuffer();
    // loop nas linhas
    for (int i=0; i<size(); i++) {
      result.append(scriptStartTag + getLine(i).getLine() + scriptEndTag + "\r\n");
    } // for
    // retorna
    return result.toString();
  }

  /**
   * Retorna a quantidade de linhas adicionadas no formul�rio.
   * @return Retorna a quantidade de linhas adicionadas no formul�rio.
   */
  public int size() {
    return lines.size();
  }

}
