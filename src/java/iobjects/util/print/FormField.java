package iobjects.util.print;

import iobjects.util.DateTools;

import java.sql.*;
import java.text.*;

/**
 * Representa um campo de uma linha de valores para preencher um FormComposer.
 */
public class FormField {

  static public final int ALIGN_LEFT   = 0;
  static public final int ALIGN_CENTER = 1;
  static public final int ALIGN_RIGHT  = 2;

  private String value;
  private int    left;
  private int    length;
  private int    align;

  /**
   * Construtor padrão.
   * @param value Valor do campo.
   * @param left Posição do campo na linha.
   * @param length Tamanho que o campo irá ocupar na linha.
   * @param align Alinhamento do valor.
   */
  public FormField(String value,
                   int    left,
                   int    length,
                   int    align) {
    // guarda nossos valores
    this.value  = value;
    this.left   = left;
    this.length = length;
    this.align  = align;
  }

  /**
   * Construtor estendido.
   * @param value Valor do campo.
   * @param left Posição do campo na linha.
   * @param length Tamanho que o campo irá ocupar na linha.
   */
  public FormField(int value,
                   int left,
                   int length) {
    // chama o construtor padrão
    this(value + "",
         left,
         length,
         ALIGN_RIGHT);
  }

  /**
   * Construtor estendido.
   * @param value Valor do campo.
   * @param left Posição do campo na linha.
   * @param length Tamanho que o campo irá ocupar na linha.
   */
  public FormField(float value,
                   int   left,
                   int   length) {
    // chama o construtor padrão
    this(formatFloat(value),
         left,
         length,
         ALIGN_RIGHT);
  }

  /**
   * Construtor estendido.
   * @param value Valor do campo.
   * @param left Posição do campo na linha.
   * @param length Tamanho que o campo irá ocupar na linha.
   * @param align Alinhamento do valor.
   * @param date true se a data deve ser impressa.
   * @param hour true se a hora deve ser impressa.
   */
  public FormField(Timestamp value,
                   int       left,
                   int       length,
                   int       align,
                   boolean   date,
                   boolean   hour) {
    // chama o construtor padrão
    this(formatTimestamp(value, date, hour),
         left,
         length,
         align);
  }

  /**
   * Retorna uma String do tamanho 'length' com 'value' alinhado ao centro.
   * @param value Valor para alinhar.
   * @param length Tamanho final da String.
   * @return Retorna uma String do tamanho 'length' com 'value' alinhado ao centro.
   */
  private String center(String value, int length) {
    // nosso resultado
    String result = value;
    // quantidade de espaços para completar o tamanho
    int count = length - value.length();
    // adiciona a métade dos espaços a frente do valor
    for (int i=0; i<count/2; i++)
      result = " " + result;
    // completa o restando do tamanho após o valor
    while (result.length() < length)
      result = result + " ";
    // retorna
    return result;
  }

  /**
   * Retorna 'value' no formato para ser impresso.
   * @param value Valor float para ser impresso.
   * @return Retorna 'value' no formato para ser impresso.
   */
  static private String formatFloat(float value) {
    // formatador decimal
    NumberFormat format = DecimalFormat.getInstance();
    format.setMaximumFractionDigits(2);
    format.setMinimumFractionDigits(2);
    // retorna o valor formatado
    return format.format(value);
  }

  /**
   * Retorna o 'value' no formato indicado.
   * @param value Timestamp para ser formatado.
   * @param date true se deve incluir a data.
   * @param hour true se deve incluir a hora.
   * @return Retorna o 'value' no formato indicado.
   */
  static private String formatTimestamp(Timestamp value,
                                        boolean   date,
                                        boolean   hour) {
    // nosso resultado
    String result = "";
    // quer a data?
    if (date)
      result = DateTools.formatDate(value);
    else if (hour)
      result = DateTools.formatTime(value);
    else if (date && hour)
      result = DateTools.formatDateTime(value);
    // retorna
    return result;
  }

  /**
   * Retorna a posição do campo na linha.
   * @return Retorna a posição do campo na linha.
   */
  public int getLeft() {
    return left;
  }

  /**
   * Retorna o tamanho do campo na linha.
   * @return Retorna o tamanho do campo na linha.
   */
  public int getLength() {
    return length;
  }

  /**
   * Retorna o valor do campo formatado.
   * @return Retorna o valor do campo formatado.
   */
  public String getValue() {
    // nosso valor
    String result = removeAccents(value);
    // se o resultado é maior que a largura do campo...trunca
    if (result.length() > length)
      result = result.substring(0, length-1);
    // alinha
    switch (align) {
      case ALIGN_LEFT  : result = left(result, length);   break;
      case ALIGN_CENTER: result = center(result, length); break;
      case ALIGN_RIGHT : result = right(result, length);  break;
    } // switch
    // retorna
    return result;
  }


  /**
   * Retorna uma String do tamanho 'length' com 'value' alinhado à esquerda.
   * @param value Valor para alinhar.
   * @param length Tamanho final da String.
   * @return Retorna uma String do tamanho 'length' com 'value' alinhado ao centro.
   */
  private String left(String value, int length) {
    // nosso resultado
    String result = value;
    // completa com espaços após o valor
    while (result.length() < length)
      result = result + " ";
    // retorna
    return result;
  }

  private char removeAccent(char value) {
         if ("áàã".indexOf(value) >= 0) return 'a';
    else if ("éè".indexOf(value) >= 0) return 'e';
    else if ("íì".indexOf(value) >= 0) return 'i';
    else if ("óò".indexOf(value) >= 0) return 'o';
    else if ("úù".indexOf(value) >= 0) return 'u';
    else if ("ç".indexOf(value) >= 0) return 'c';
    else if ("ÁÀÃ".indexOf(value) >= 0) return 'A';
    else if ("ÉÈ".indexOf(value) >= 0) return 'E';
    else if ("ÍÌ".indexOf(value) >= 0) return 'I';
    else if ("ÓÒ".indexOf(value) >= 0) return 'O';
    else if ("ÚÙ".indexOf(value) >= 0) return 'U';
    else if ("Ç".indexOf(value) >= 0) return 'C';
    else return value;
  }

  private String removeAccents(String value) {
    // nosso resultado
    StringBuffer result = new StringBuffer(value);
    // loop no valor
    for (int i=0; i<result.length(); i++)
      result.setCharAt(i, removeAccent(result.charAt(i)));
    // retorna
    return result.toString();
  }

  /**
   * Retorna uma String do tamanho 'length' com 'value' alinhado à direita.
   * @param value Valor para alinhar.
   * @param length Tamanho final da String.
   * @return Retorna uma String do tamanho 'length' com 'value' alinhado ao centro.
   */
  private String right(String value, int length) {
    // nosso resultado
    String result = value;
    // completa com espaços antes do valor
    while (result.length() < length)
      result = " " + result;
    // retorna
    return result;
  }

}
