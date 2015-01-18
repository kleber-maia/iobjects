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
 * Representa uma linha de valores para preencherem um FormComposer.
 */
public class FormLine {

  private int    length;
  private Vector fields = new Vector();

  /**
   * Construtor padrão.
   * @param length Largura da linha.
   */
  public FormLine(int length) {
    // nossos valores
    this.length = length;
  }

  /**
   * Adiciona o campo referenciado por 'field' para ser impresso na linha.
   * @param field FormField para ser impresso na linha.
   */
  public void addField(FormField field) {
    fields.add(field);
  }

  /**
   * Apaga os campos para serem impressos na linha.
   */
  public void clear() {
    fields.clear();
  }

  /**
   * Retorna o FormField na posição indicada por 'index'.
   * @param index Índice do FormField que se deseja retornar.
   * @return Retorna o FormField na posição indicada por 'index'.
   */
  public FormField getField(int index) {
    return (FormField)fields.get(index);
  }

  /**
   * Retorna a linha com seus campos posicionados e formatados para impressão.
   * @return Retorna a linha com seus campos posicionados e formatados para
   *         impressão.
   */
  public String getLine() {
    // linha em branco
    StringBuffer result = new StringBuffer(length);
    // preenche com espaços
    for (int i=0; i<length; i++)
      result.append(" ");
    // loop nos campos
    for (int i=0; i<size(); i++) {
      // campo da vez
      FormField field = getField(i);
      // põe o campo na linha
      result.replace(field.getLeft(),
                     field.getLeft() + field.getLength(),
                     field.getValue());
    } // for
    // retorna a linha
    return result.toString();
  }

  /**
   * Retorna a largura da linha.
   * @return Retorna a largura da linha.
   */
  public int length() {
    return length;
  }

  /**
   * Remove o FormField da posição indicada por 'index'.
   * @param index Índice do FormField que se deseja remover da linha.
   */
  public void removeField(int index) {
    fields.remove(index);
  }

  /**
   * Retorna o tamanho da lista de FormFields que serão impressos na linha.
   * @return Retorna o tamanho da lista de FormFields que serão impressos na linha.
   */
  public int size() {
    return fields.size();
  }

}
