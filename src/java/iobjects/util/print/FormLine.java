package iobjects.util.print;

import java.util.*;

/**
 * Representa uma linha de valores para preencherem um FormComposer.
 */
public class FormLine {

  private int    length;
  private Vector fields = new Vector();

  /**
   * Construtor padr�o.
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
   * Retorna o FormField na posi��o indicada por 'index'.
   * @param index �ndice do FormField que se deseja retornar.
   * @return Retorna o FormField na posi��o indicada por 'index'.
   */
  public FormField getField(int index) {
    return (FormField)fields.get(index);
  }

  /**
   * Retorna a linha com seus campos posicionados e formatados para impress�o.
   * @return Retorna a linha com seus campos posicionados e formatados para
   *         impress�o.
   */
  public String getLine() {
    // linha em branco
    StringBuffer result = new StringBuffer(length);
    // preenche com espa�os
    for (int i=0; i<length; i++)
      result.append(" ");
    // loop nos campos
    for (int i=0; i<size(); i++) {
      // campo da vez
      FormField field = getField(i);
      // p�e o campo na linha
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
   * Remove o FormField da posi��o indicada por 'index'.
   * @param index �ndice do FormField que se deseja remover da linha.
   */
  public void removeField(int index) {
    fields.remove(index);
  }

  /**
   * Retorna o tamanho da lista de FormFields que ser�o impressos na linha.
   * @return Retorna o tamanho da lista de FormFields que ser�o impressos na linha.
   */
  public int size() {
    return fields.size();
  }

}
