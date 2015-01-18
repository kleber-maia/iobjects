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
package iobjects.util;

import java.net.*;
import java.util.*;

import iobjects.xml.ui.*;

/**
 * Ferramentas de formata��o e trabalho com Strings.
 */
public class StringTools {

  static public void main(String[] args) {
    System.out.println("<p>Ol�&nbsp;!</p><p>Doido<br>doido&nbsp;a�!</p>\"S�\"");
    System.out.println(htmlTextToText("<p>Ol�&nbsp;!</p><p>Doido<br>doido&nbsp;a�!</p>\"S�\""));
  }

  /**
   * Retorna um String[] contendo os elementos de 'array' mais 'value'.
   * @param array String[] original.
   * @param value String para concatenar.
   * @return Retorna um String[] contendo os elementos de 'array' mais 'value'.
   */
  static public String[] arrayConcat(String[] array, String value) {
    String[] result = new String[array.length + 1];
    for (int i=0; i<array.length; i++)
      result[i] = array[i];
    result[result.length-1] = value;
    return result;
  }

  /**
   * Retorna um String[] contendo os elementos de 'array1' e 'array2'.
   * @param array1 String[] Primeiro String[] para concatenar.
   * @param array2 String[] Segundo String[] para concatenar.
   * @return String[] Retorna um String[] contendo os elementos de 'array1' e 'array2'.
   */
  static public String[] arrayConcat(String[] array1, String[] array2) {
    String[] result = new String[array1.length + array2.length];
    for (int i=0; i<array1.length; i++)
      result[i] = array1[i];
    for (int i=0; i<array2.length; i++)
      result[array1.length + i] = array2[i];
    return result;
  }

  /**
   * Retorna true se 'array' cont�m 'value', desprezando letras mai�sculas e
   * min�sculas.
   * @param array String[] de valores para pesquisa.
   * @param value String para pesquisar em 'array'.
   * @return Retorna true se 'array' cont�m 'value', desprezando letras
   *         mai�sculas e min�sculas.
   */
  static public boolean arrayContains(String[] array, String value) {
    // retorna true se o �ndice for v�lido
    return arrayIndexOf(array, value) >= 0;
  }

  /**
   * Retorna true se 'array' cont�m todos os elementos de 'subArray' informados,
   * desprezando letras mai�sculas e min�sculas.
   * @param array String[] de valores para pesquisa.
   * @param subArray String[] cujos valores se deseja pesquisar em 'array'.
   * @return Retorna true se 'array' cont�m todos os elementos de 'subArray'
   *         informados, desprezando letras mai�sculas e min�sculas.
   */
  static public boolean arrayContains(String[] array, String[] subArray) {
    // retorna falso para todo caso
    boolean result = array.length == 0;
    // verifica todos os valores de subArray
    for (int i=0; i<subArray.length; i++) {
      // valor da vez
      result = arrayContains(array, subArray[i]);
      // se n�o bateu...j� podemos disparar
      if (!result)
        return result;
    } // for
    // retorna o que achamos
    return result;
  }

  /**
   * Retorna a quantidade de elementos que array cont�m de subArray,
   * desprezando letras mai�sculas e min�sculas.
   * @param array String[] de valores para pesquisa.
   * @param subArray String[] cujos valores se deseja pesquisar em 'array'.
   * @return Retorna a quantidade de elementos que array cont�m de subArray,
   *         desprezando letras mai�sculas e min�sculas.
   */
  static public int arrayContainsCount(String[] array, String[] subArray) {
    // nosso retorno
    int result = 0;
    // verifica todos os valores de subArray
    for (int i=0; i<subArray.length; i++) {
      // valor da vez
      String[] value = {subArray[i]};
      if (arrayContains(array, value))
        result++;
    } // for
    // retorna o que achamos
    return result;
  }

  /**
   * Retorna um String[] contendo os elementos de 'array' entre os �ndices
   * 'fromIndex' e 'toIndex'.
   * @param array String[] contendo os elementos que ser�o copiados.
   * @param fromIndex int �ndice do elemento inicial.
   * @param toIndex int �ndice do elemento final.
   * @return String[] Retorna um String[] contendo os elementos de 'array' entre
   *         os �ndices 'fromIndex' e 'toIndex'.
   */
  static public String[] arrayGetElements(String[] array, int fromIndex, int toIndex) {
    int count = (toIndex - fromIndex) + 1;
    String[] result = new String[count];
    for (int i=0; i<count; i++)
      result[i] = array[fromIndex + i];
    return result;
  }

  /**
   * Retorna o �ndice de 'element' em 'array'. Retorna -1 se 'element' n�o for
   * encontrado.
   * @param array String[] contendo os elementos onde se deseja pesquisar.
   * @param element String que se deseja localizar em 'array'.
   * @return Retorna o �ndice de 'element' em 'array'. Retorna -1 se 'element'
   *         n�o for encontrado.
   */
  static public int arrayIndexOf(String[] array, String element) {
    for (int i=0; i<array.length; i++) {
      if (array[i].equalsIgnoreCase(element))
        return i;
    } // for
    return -1;
  }

  /**
   * Retorna 'array' com seus elementos ordenados alfabeticamente.
   * @param array Array cujos elementos se deseja ordenar.
   * @return Retorna 'array' com seus elementos ordenados alfabeticamente.
   */
  public static String[] arraySort(String[] array) {
    // TreeSet para ordenar
    TreeSet treeSet = new TreeSet(String.CASE_INSENSITIVE_ORDER);
    // inclui os itens do Array recebido
    for (int i=0; i<array.length; i++)
      treeSet.add(array[i]);
    // cria e preenche o Array de retorno
    Object[] sorted = treeSet.toArray();
    String[] result = new String[array.length];
    for (int i=0; i<result.length; i++)
      result[i] = (String)sorted[i];
    // retorna
    return result;
  }

  /**
   * Retorna uma String contendo todos os elementos de 'array' concatenados
   * e separados por 'separator'.
   * @param array String[] Array cujas Strings se deseja concatenar.
   * @param separator String Separador para usar entre as Strings.
   * @return String Retorna uma String contendo todos os elementos de 'array'
   *                concatenados e separados por 'separator'.
   */
  public static String arrayStringToString(String[] array, String separator) {
    StringBuffer result = new StringBuffer(array.length * 250);
    // loop no array
    for (int i=0; i<array.length; i++) {
      // concatena a String da vez ao resultado
      result.append(array[i]);
      // se ainda n�o chegamos ao �ltimo item...adiciona o separador
      if (i<array.length-1)
        result.append(separator);
    } // for
    // retorna
    return result.toString();
  }

  /**
   * Retorna true se os dois Stirng[]�s informados contiverem valores iguais
   * desprezando letras mai�sculas e min�sculas.
   * @param array1 Primeiro String[] de valores para comparar.
   * @param array2 Segundo String[] de valores para comparar.
   * @return Retorna true se os dois Stirng[]�s informados contiverem valores iguais
   * desprezando letras mai�sculas e min�sculas.
   */
  static public boolean compareArrays(String[] array1, String[] array2) {
    // retorna falso para todo caso
    boolean result = false;
    // se n�o temos a mesma quantidade de valores nas duas listas...dispara
    if (array1.length != array2.length)
      return result;
    // verifica todos os valores
    for (int i=0; i<array1.length; i++) {
      // valor da vez
      result = array1[i].equalsIgnoreCase(array2[i]);
      // se n�o bateu...j� podemos disparar
      if (!result)
        return result;
    } // for
    // retorna o que achamos
    return result;
  }
  
  /**
   * Retorna 'url' codificada no formato MIME application/x-www-form-urlencoded.
   * @param url String que se deseja codificar.
   * @return String Retorna 'url' codificada no formato MIME
   *                application/x-www-form-urlencoded.
   * @throws Exception Em caso de exce��o na tentativa de codificar a URL.
   */
  static public String decodeURL(String url) throws Exception {
    // nosso resultado decodificado
    String result = URLDecoder.decode(url, "ISO-8859-1");
    // retorna as '=', '&' e '%'
    result = result.replaceAll("<equal>", "=");
    result = result.replaceAll("<and>", "&");
    result = result.replaceAll("<perc>", "%");
    // retorna
    return result;
  }

  /**
   * Retorna 'url' codificada no formato MIME application/x-www-form-urlencoded.
   * @param url String que se deseja codificar.
   * @return String Retorna 'url' codificada no formato MIME
   *                application/x-www-form-urlencoded.
   * @throws Exception Em caso de exce��o na tentativa de codificar a URL.
   */
  static public String encodeURL(String url) throws Exception {
    // nosso resultado
    String result = url;
    // substitui as '=', '&' e '%'
    result = result.replaceAll("=", "<equal>");
    result = result.replaceAll("&", "<and>");
    result = result.replaceAll("%", "<perc>");
    // codifica o restante
    return URLEncoder.encode(result, "ISO-8859-1");
  }

  /**
   * Fornece um �nico m�todo simplificado para chamada das diversas fun��es de
   * formata��o de String.
   * @param value Valor que se deseja formatar.
   * @param toCamelCase true se 'value' deve ser colocado no formato camelo.
   * @param toCaptalCase true se 'value' deve ser captalizado.
   * @param removeAccents true se 'value' deve ter seus acentos removidos.
   * @param removeSpaces true se 'value' deve ter seus espa�os removidos
   * @param spacesToUnderline true se 'value' deve ter seus espa�os trocados
   *                          por '_'.
   * @return Fornece um �nico m�todo simplificado para chamada das diversas
   *         fun��es de formata��o de String.
   */
  static public String format(String  value,
                              boolean toCamelCase,
                              boolean toCaptalCase,
                              boolean removeAccents,
                              boolean removeSpaces,
                              boolean spacesToUnderline) {
    String result = value;
    if (toCamelCase)
      result = toCamelCase(result);
    if (toCaptalCase)
      result = toCaptalCase(result);
    if (removeAccents)
      result = removeAccents(result);
    if (removeSpaces)
      result = removeSpaces(result);
    if (spacesToUnderline)
      result = spacesToUnderline(result);
    return result;
  }

  /**
   * Retorna 'value' com a m�scara de CPF ou CNPJ.
   * @param value CPF ou CNPJ para ser formatado.
   * @return Retorna 'value' com a m�scara de CPF ou CNPJ.
   * @deprecated
   */
  static public String formatCPFCNPJ(String value) {
    // se � um CPF
    if (value.length() == 11)
      return formatCustomMask(value, "000.000.000-00");
    // se � um CNPJ
    else if (value.length() == 14)
      return formatCustomMask(value, "00.000.000/0000-00");
    // se � desconhecido
    else
      return value;
  }

  /**
   * Retorna 'value' formatado com a m�scara informada em 'mask'. O caractere
   * de m�scara utilizado ser� o '0' (zero).
   * @param value Valor para ser formatado, ex.: 12345678901.
   * @param mask M�scara desejada, ex.: 000.000.000-00.
   * @return Retorna 'value' formatado com a m�scara informada em 'mask'. O
   *         caractere de m�scara utilizado ser� o '0' (zero).
   */
  static public String formatCustomMask(String value, String mask) {
    return formatCustomMask(value, mask, '0');
  }

  /**
   * Retorna 'value' formatado com a m�scara informada em 'mask'.
   * @param value Valor para ser formatado.
   * @param mask M�scara desejada.
   * @param maskChar Caractere em 'mask' que indica o espa�o que deve ser
   *                 preenchido com os caracteres de 'value'.
   * @return Retorna 'value' formatado com a m�scara informada em 'mask'.
   */
  static public String formatCustomMask(String value, String mask, char maskChar) {
    // remove os espa�os
    value = value.trim();
    // se n�o temos valor para formatar...dispara
    if (value.equals(""))
      return "";
    // resultado = value + mask
    StringBuffer result = new StringBuffer(mask.length());
    // loop nos algarismos da m�scara
    while (!mask.equals("")) {
      // se a posi��o atual requer um algarismo do valor...
      if (mask.charAt(0) == maskChar) {
        result.append(value.charAt(0)); // adiciona o algarismo
        value = value.substring(1);     // apaga do valor
      }
      // se requer um caractere da m�scara...
      else
        result.append(mask.charAt(0)); // adiciona o caractere
      // apaga o caractere da m�scara
      mask = mask.substring(1);
      // se n�o temos mais algarismos no valor...dispara
      if (value.equals(""))
        break;
    }; // while
    // retorna o valor mascarado
    return result.toString();
  }

  /**
   * Faz as transforma��es necess�rias em 'htmlText' para sua correta exibi��o
   * em formato texto simples. <p>
   * Ajustes realizados: <br>
   * <li> &lt;br /&gt; s�o substitu�dos por \r\n
   * <li> %xx s�o sobstitu�dos pelo caracter correspondete
   * @param htmlText Texto HTML para ser transformado em texto simples.
   * @return Retorna 'htmlText' com as transforma��es necess�rias em para sua
   *         correta exibi��o em formato texto simples.
   */
  static String[][] HTML_REPLACES = {{"<br>",   "\r\n"},
                                     {"<br />", "\r\n"},
                                     {"%20",    " "},
                                     {"&nbsp;", " "},
                                     {"<p>",    ""},
                                     {"</p>",   "\r\n"},
                                     {"<P>",    ""},
                                     {"</P>",   "\r\n"},                                     
                                     {"\"", "'"}};
  static public String htmlTextToText(String htmlText) {
    // nossas trocas
    // nosso resultado
    StringBuffer result = new StringBuffer(htmlText);
    // loop nas trocas a serem feitas
    for (int i=0; i<HTML_REPLACES.length; i++) {
      // substitui em todas as ocorr�ncias
      int pos = 0;
      while ((pos = result.indexOf(HTML_REPLACES[i][0])) >= 0) {
        result.replace(pos, pos + HTML_REPLACES[i][0].length(), HTML_REPLACES[i][1]);
      } // while
    } // for
    // retorna
    return result.toString();
  }

  /**
   * Retorna 'value' sem acentos.
   * @param value Valor que se deseja retirar os acentos.
   * @return Retorna 'value' sem acentos.
   */
  static public String removeAccents(String value) {
    StringBuffer result = new StringBuffer(value);
    for (int i=0; i<result.length(); i++) {
      char chChar = result.charAt(i);
      // min�sculas
      if ("����".indexOf(chChar) >= 0)
        chChar = 'a';
      else if ("���".indexOf(chChar) >= 0)
        chChar = 'e';
      else if ("���".indexOf(chChar) >= 0)
        chChar = 'i';
      else if ("����".indexOf(chChar) >= 0)
        chChar = 'o';
      else if ("����".indexOf(chChar) >= 0)
        chChar = 'u';
      else if ("�".indexOf(chChar) >= 0)
        chChar = 'c';
      // mai�sculas
      else if ("�����".indexOf(chChar) >= 0)
        chChar = 'A';
      else if ("���".indexOf(chChar) >= 0)
        chChar = 'E';
      else if ("���".indexOf(chChar) >= 0)
        chChar = 'I';
      else if ("����".indexOf(chChar) >= 0)
        chChar = 'O';
      else if ("����".indexOf(chChar) >= 0)
        chChar = 'U';
      else if ("�".indexOf(chChar) >= 0)
        chChar = 'C';
      // substitui o caractere
      result.setCharAt(i, chChar);
    } // for
    return result.toString();
  }

  /**
   * Retorna 'maskedValue' sem a m�scara informada em 'mask'. O caractere
   * de m�scara utilizado ser� o '0' (zero).
   * @param maskedValue Valor cuja m�scara ser� removida, ex.: 123.456.789-01.
   * @param mask M�scara utilizada para formata��o de 'maskedValue', ex.:
   *             000.000.000-00.
   * @return Retorna 'value' sem a m�scara informada em 'maskedValue'.
   */
  static public String removeCustomMask(String maskedValue, String mask) {
    return removeCustomMask(maskedValue, mask, '0');
  }

  /**
   * Retorna 'maskedValue' sem a m�scara informada em 'mask'.
   * @param maskedValue Valor cuja m�scara ser� removida.
   * @param mask M�scara utilizada para formata��o de 'maskedValue'.
   * @param maskChar Caractere em 'mask' que indica o espa�o que foi
   *                 preenchido com os caracteres de 'maskedValue'.
   * @return Retorna 'value' sem a m�scara informada em 'maskedValue'.
   * @since 3.3
   */
  static public String removeCustomMask(String maskedValue, String mask, char maskChar) {
    // se n�o temos valor para remover a m�scara...dispara
    if (maskedValue.equals(""))
      return "";
    // resultado = value - mask
    StringBuffer result = new StringBuffer(mask.length());
    // loop nos caracteres do valor
    for (int i=0; i<maskedValue.length(); i++) {
      // se ainda temos m�scara para verificar...
      if (i <= mask.length()-1) {
        // se o caractere do valor faz parte da m�scara...continua
        if (maskedValue.charAt(i) == mask.charAt(i) &&
            maskedValue.charAt(i) != maskChar)
          continue;
      } // if
      // soma o caractere no resultado
      result.append(maskedValue.charAt(i));
    } // for
    // retorna o valor sem a m�scara
    return result.toString();
  }

  /**
   * Retorna 'value' sem todos os caracteres n�o num�ricos.
   * @param value String Valor que se deseja remover os caracteres n�o num�ricos.
   * @return String Retorna 'value' sem todos os caracteres n�o num�ricos.
   */
  static public String removeNotNumbers(String value) {
    // nosso resultado
    StringBuffer result = new StringBuffer(value);
    // loop nos caracteres do resultado do �ltimo para o primeiro
    for (int i=result.length()-1; i>=0; i--) {
      // caractere da vez
      char chChar = result.charAt(i);
      // se n�o � um n�mero...remove do resultado
      if ((chChar < '0') || (chChar > '9'))
        result.deleteCharAt(i);
    } // for
    // retorna
    return result.toString();
  }

  /**
   * Retorna 'value' sem espa�os entre os nomes.
   * @param value Valor que se deseja remover os espa�os.
   * @return Retorna 'value' sem espa�os entre os nomes.
   */
  static public String removeSpaces(String value) {
    return value.replaceAll(" ", "");
  }

  /**
   * Retorna uma String gerada a partir do algoritimo de combina��o inversa
   * com o tamanho indicado por 'length'.
   * @param value String String de origem.
   * @param length int Tamanho da String gerada.
   * @return String Retorna uma String gerada a partir do algoritimo de combina��o
   *         inversa com o tamanho indicado por 'length'.
   */
  static public String reverseCombination(String value, int length) {
    // nossa origem
    StringBuffer source = new StringBuffer(value);
    // nosso resultado
    StringBuffer result = new StringBuffer(length);
    // construindo a combina��o
    int i=0;
    while (result.length() < length) {
      if (i % 2 > 0)
        result.append(source.reverse());
      else
        result.append(source);
      i++;
    } // while
    // se passamos do tamanho desejado...apaga o excedente
    if (result.length() > length)
      result.delete(length+1, result.length());
    // retorna
    return result.toString();
  }

  /**
   * Retorna 'value' com '_' no lugar dos espa�os.
   * @param value Valor que se desejar trocar os espa�os por '_'.
   * @return Retorna 'value' com '_' no lugar dos espa�os.
   */
  static public String spacesToUnderline(String value) {
    return value.replaceAll(" ", "_");
  }

  /**
   * Retorna uma nova String formada por 'count' caracteres de 'ch'.
   * @param ch Caractere que ser�o usados para preencher a String.
   * @param count Quantidade de caracteres para preencher a String.
   * @return Retorna uma nova String formada por 'count' caracteres de 'ch'.
   */
  static public String stringOfChar(char ch, int count) {
    StringBuffer result = new StringBuffer(count);
    for (int i=0; i<count; i++)
      result.append(ch);
    return result.toString();
  }
  
  /**
   * Faz as transforma��es necess�rias em 'text' para sua correta exibi��o em
   * p�ginas HTML. <p>
   * Ajustes realizados: <br>
   * <li> \r\n, \n\r, \r e \n s�o substitu�dos por &lt;br /&gt;
   * <li> " s�o substitu�ddas por '
   * @param text Texto para ser transformado em texto HTML.
   * @return Retorna 'text' com as transforma��es necess�rias para
   *         sua correta exibi��o em p�ginas HTML.
   */
  static public String textToHTMLText(String text) {
    // se n�o recebemos nada...dispara
    if ((text == null) || (text.equals("")))
        return text;
    // nosso resultado
    String result = text;
    // troca as quebras de linha
    result = result.replaceAll("\r\n", "<br />");
    result = result.replaceAll("\n\r", "<br />");
    result = result.replaceAll("\r", "<br />");
    result = result.replaceAll("\n", "<br />");
    // ajusta as aspas
    result = result.replace('"', '\'');
    // retorna
    return result;
  }

  /**
   * Retorna 'value' no formato camelo.
   * @param value Valor que se deseja formatar.
   * @return Retorna 'value' no formato camelo.
   */
  static public String toCamelCase(String value) {
    // se n�o temos nada
    if ((value == null) || (value.equals("")))
      return value;
    // nosso resultado
    StringBuffer result = new StringBuffer(value.length());
    // separa os nomes do valor que recebemos
    String[] names = value.split(" ");
    // loop nos nomes
    for (int i=0; i<names.length; i++) {
      // nome da vez
      String name = names[i];
      // primeiro caractere
      String firstChar = name.charAt(0) + "";
      // se n�o � a primeira palavra...p�e o primeiro caractere da palavra em mai�sculo
      result.append((i == 0 ? firstChar.toLowerCase() : firstChar.toUpperCase()) + name.substring(1, name.length()).toLowerCase());
    } // for
    // retorna
    return result.toString();
  }

  /**
   * Retorna 'value' com as primeiras letras de cada nome em mai�sculo.
   * @param value Valor que se deseja captalizar.
   * @return Retorna 'value' com as primeiras letras de cada nome em mai�sculo.
   */
  static public String toCaptalCase(String value) {
    return toCaptalCase(value, false);
  }

  /**
   * Retorna 'value' com as primeiras letras de cada nome em mai�sculo.
   * @param value Valor que se deseja captalizar.
   * @param preserveSpaces True para que os espa�os entre as palavras sejam
   *                       preservados.
   * @return Retorna 'value' com as primeiras letras de cada nome em mai�sculo.
   */
  static public String toCaptalCase(String value, boolean preserveSpaces) {
    // se n�o temos nada
    if ((value == null) || (value.equals("")))
      return value;
    // nosso resultado
    StringBuffer result = new StringBuffer(value.length());
    // separa os nomes do valor que recebemos
    String[] names = value.toLowerCase().split(" ");
    // loop nos nomes
    for (int i=0; i<names.length; i++) {
      // nome da vez
      String name = names[i];
      // se � um conectivo...
      if ("de-do-dos-da-das-em-os-as".indexOf(name) >= 0)
        // mant�m em min�sculo
        result.append(name);
      // se n�o �...
      else {
        // primeiro caractere
        String firstChar = name.charAt(0) + "";
        // p�e o primeiro caractere da palavra em mai�sculo
        result.append(firstChar.toUpperCase() + name.substring(1, name.length()));
      } // if
      // se � para preservar os espa�os...
      if (preserveSpaces && (i<names.length-1))
        result.append(" ");
    } // for
    // retorna
    return result.toString();
  }

}
