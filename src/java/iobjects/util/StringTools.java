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
 * Ferramentas de formatação e trabalho com Strings.
 */
public class StringTools {

  static public void main(String[] args) {
    System.out.println("<p>Olá&nbsp;!</p><p>Doido<br>doido&nbsp;aê!</p>\"Só\"");
    System.out.println(htmlTextToText("<p>Olá&nbsp;!</p><p>Doido<br>doido&nbsp;aê!</p>\"Só\""));
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
   * Retorna true se 'array' contém 'value', desprezando letras maiúsculas e
   * minúsculas.
   * @param array String[] de valores para pesquisa.
   * @param value String para pesquisar em 'array'.
   * @return Retorna true se 'array' contém 'value', desprezando letras
   *         maiúsculas e minúsculas.
   */
  static public boolean arrayContains(String[] array, String value) {
    // retorna true se o índice for válido
    return arrayIndexOf(array, value) >= 0;
  }

  /**
   * Retorna true se 'array' contém todos os elementos de 'subArray' informados,
   * desprezando letras maiúsculas e minúsculas.
   * @param array String[] de valores para pesquisa.
   * @param subArray String[] cujos valores se deseja pesquisar em 'array'.
   * @return Retorna true se 'array' contém todos os elementos de 'subArray'
   *         informados, desprezando letras maiúsculas e minúsculas.
   */
  static public boolean arrayContains(String[] array, String[] subArray) {
    // retorna falso para todo caso
    boolean result = array.length == 0;
    // verifica todos os valores de subArray
    for (int i=0; i<subArray.length; i++) {
      // valor da vez
      result = arrayContains(array, subArray[i]);
      // se não bateu...já podemos disparar
      if (!result)
        return result;
    } // for
    // retorna o que achamos
    return result;
  }

  /**
   * Retorna a quantidade de elementos que array contém de subArray,
   * desprezando letras maiúsculas e minúsculas.
   * @param array String[] de valores para pesquisa.
   * @param subArray String[] cujos valores se deseja pesquisar em 'array'.
   * @return Retorna a quantidade de elementos que array contém de subArray,
   *         desprezando letras maiúsculas e minúsculas.
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
   * Retorna um String[] contendo os elementos de 'array' entre os índices
   * 'fromIndex' e 'toIndex'.
   * @param array String[] contendo os elementos que serão copiados.
   * @param fromIndex int Índice do elemento inicial.
   * @param toIndex int Índice do elemento final.
   * @return String[] Retorna um String[] contendo os elementos de 'array' entre
   *         os índices 'fromIndex' e 'toIndex'.
   */
  static public String[] arrayGetElements(String[] array, int fromIndex, int toIndex) {
    int count = (toIndex - fromIndex) + 1;
    String[] result = new String[count];
    for (int i=0; i<count; i++)
      result[i] = array[fromIndex + i];
    return result;
  }

  /**
   * Retorna o índice de 'element' em 'array'. Retorna -1 se 'element' não for
   * encontrado.
   * @param array String[] contendo os elementos onde se deseja pesquisar.
   * @param element String que se deseja localizar em 'array'.
   * @return Retorna o índice de 'element' em 'array'. Retorna -1 se 'element'
   *         não for encontrado.
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
      // se ainda não chegamos ao último item...adiciona o separador
      if (i<array.length-1)
        result.append(separator);
    } // for
    // retorna
    return result.toString();
  }

  /**
   * Retorna true se os dois Stirng[]´s informados contiverem valores iguais
   * desprezando letras maiúsculas e minúsculas.
   * @param array1 Primeiro String[] de valores para comparar.
   * @param array2 Segundo String[] de valores para comparar.
   * @return Retorna true se os dois Stirng[]´s informados contiverem valores iguais
   * desprezando letras maiúsculas e minúsculas.
   */
  static public boolean compareArrays(String[] array1, String[] array2) {
    // retorna falso para todo caso
    boolean result = false;
    // se não temos a mesma quantidade de valores nas duas listas...dispara
    if (array1.length != array2.length)
      return result;
    // verifica todos os valores
    for (int i=0; i<array1.length; i++) {
      // valor da vez
      result = array1[i].equalsIgnoreCase(array2[i]);
      // se não bateu...já podemos disparar
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
   * @throws Exception Em caso de exceção na tentativa de codificar a URL.
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
   * @throws Exception Em caso de exceção na tentativa de codificar a URL.
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
   * Fornece um único método simplificado para chamada das diversas funções de
   * formatação de String.
   * @param value Valor que se deseja formatar.
   * @param toCamelCase true se 'value' deve ser colocado no formato camelo.
   * @param toCaptalCase true se 'value' deve ser captalizado.
   * @param removeAccents true se 'value' deve ter seus acentos removidos.
   * @param removeSpaces true se 'value' deve ter seus espaços removidos
   * @param spacesToUnderline true se 'value' deve ter seus espaços trocados
   *                          por '_'.
   * @return Fornece um único método simplificado para chamada das diversas
   *         funções de formatação de String.
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
   * Retorna 'value' com a máscara de CPF ou CNPJ.
   * @param value CPF ou CNPJ para ser formatado.
   * @return Retorna 'value' com a máscara de CPF ou CNPJ.
   * @deprecated
   */
  static public String formatCPFCNPJ(String value) {
    // se é um CPF
    if (value.length() == 11)
      return formatCustomMask(value, "000.000.000-00");
    // se é um CNPJ
    else if (value.length() == 14)
      return formatCustomMask(value, "00.000.000/0000-00");
    // se é desconhecido
    else
      return value;
  }

  /**
   * Retorna 'value' formatado com a máscara informada em 'mask'. O caractere
   * de máscara utilizado será o '0' (zero).
   * @param value Valor para ser formatado, ex.: 12345678901.
   * @param mask Máscara desejada, ex.: 000.000.000-00.
   * @return Retorna 'value' formatado com a máscara informada em 'mask'. O
   *         caractere de máscara utilizado será o '0' (zero).
   */
  static public String formatCustomMask(String value, String mask) {
    return formatCustomMask(value, mask, '0');
  }

  /**
   * Retorna 'value' formatado com a máscara informada em 'mask'.
   * @param value Valor para ser formatado.
   * @param mask Máscara desejada.
   * @param maskChar Caractere em 'mask' que indica o espaço que deve ser
   *                 preenchido com os caracteres de 'value'.
   * @return Retorna 'value' formatado com a máscara informada em 'mask'.
   */
  static public String formatCustomMask(String value, String mask, char maskChar) {
    // remove os espaços
    value = value.trim();
    // se não temos valor para formatar...dispara
    if (value.equals(""))
      return "";
    // resultado = value + mask
    StringBuffer result = new StringBuffer(mask.length());
    // loop nos algarismos da máscara
    while (!mask.equals("")) {
      // se a posição atual requer um algarismo do valor...
      if (mask.charAt(0) == maskChar) {
        result.append(value.charAt(0)); // adiciona o algarismo
        value = value.substring(1);     // apaga do valor
      }
      // se requer um caractere da máscara...
      else
        result.append(mask.charAt(0)); // adiciona o caractere
      // apaga o caractere da máscara
      mask = mask.substring(1);
      // se não temos mais algarismos no valor...dispara
      if (value.equals(""))
        break;
    }; // while
    // retorna o valor mascarado
    return result.toString();
  }

  /**
   * Faz as transformações necessárias em 'htmlText' para sua correta exibição
   * em formato texto simples. <p>
   * Ajustes realizados: <br>
   * <li> &lt;br /&gt; são substituídos por \r\n
   * <li> %xx são sobstituídos pelo caracter correspondete
   * @param htmlText Texto HTML para ser transformado em texto simples.
   * @return Retorna 'htmlText' com as transformações necessárias em para sua
   *         correta exibição em formato texto simples.
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
      // substitui em todas as ocorrências
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
      // minúsculas
      if ("áàãâ".indexOf(chChar) >= 0)
        chChar = 'a';
      else if ("éèê".indexOf(chChar) >= 0)
        chChar = 'e';
      else if ("íìî".indexOf(chChar) >= 0)
        chChar = 'i';
      else if ("óòôõ".indexOf(chChar) >= 0)
        chChar = 'o';
      else if ("úùûü".indexOf(chChar) >= 0)
        chChar = 'u';
      else if ("ç".indexOf(chChar) >= 0)
        chChar = 'c';
      // maiúsculas
      else if ("ÁÀÂÃÂ".indexOf(chChar) >= 0)
        chChar = 'A';
      else if ("ÉÊÈ".indexOf(chChar) >= 0)
        chChar = 'E';
      else if ("ÍÌÎ".indexOf(chChar) >= 0)
        chChar = 'I';
      else if ("ÓÒÔÕ".indexOf(chChar) >= 0)
        chChar = 'O';
      else if ("ÚÙÛÜ".indexOf(chChar) >= 0)
        chChar = 'U';
      else if ("Ç".indexOf(chChar) >= 0)
        chChar = 'C';
      // substitui o caractere
      result.setCharAt(i, chChar);
    } // for
    return result.toString();
  }

  /**
   * Retorna 'maskedValue' sem a máscara informada em 'mask'. O caractere
   * de máscara utilizado será o '0' (zero).
   * @param maskedValue Valor cuja máscara será removida, ex.: 123.456.789-01.
   * @param mask Máscara utilizada para formatação de 'maskedValue', ex.:
   *             000.000.000-00.
   * @return Retorna 'value' sem a máscara informada em 'maskedValue'.
   */
  static public String removeCustomMask(String maskedValue, String mask) {
    return removeCustomMask(maskedValue, mask, '0');
  }

  /**
   * Retorna 'maskedValue' sem a máscara informada em 'mask'.
   * @param maskedValue Valor cuja máscara será removida.
   * @param mask Máscara utilizada para formatação de 'maskedValue'.
   * @param maskChar Caractere em 'mask' que indica o espaço que foi
   *                 preenchido com os caracteres de 'maskedValue'.
   * @return Retorna 'value' sem a máscara informada em 'maskedValue'.
   * @since 3.3
   */
  static public String removeCustomMask(String maskedValue, String mask, char maskChar) {
    // se não temos valor para remover a máscara...dispara
    if (maskedValue.equals(""))
      return "";
    // resultado = value - mask
    StringBuffer result = new StringBuffer(mask.length());
    // loop nos caracteres do valor
    for (int i=0; i<maskedValue.length(); i++) {
      // se ainda temos máscara para verificar...
      if (i <= mask.length()-1) {
        // se o caractere do valor faz parte da máscara...continua
        if (maskedValue.charAt(i) == mask.charAt(i) &&
            maskedValue.charAt(i) != maskChar)
          continue;
      } // if
      // soma o caractere no resultado
      result.append(maskedValue.charAt(i));
    } // for
    // retorna o valor sem a máscara
    return result.toString();
  }

  /**
   * Retorna 'value' sem todos os caracteres não numéricos.
   * @param value String Valor que se deseja remover os caracteres não numéricos.
   * @return String Retorna 'value' sem todos os caracteres não numéricos.
   */
  static public String removeNotNumbers(String value) {
    // nosso resultado
    StringBuffer result = new StringBuffer(value);
    // loop nos caracteres do resultado do último para o primeiro
    for (int i=result.length()-1; i>=0; i--) {
      // caractere da vez
      char chChar = result.charAt(i);
      // se não é um número...remove do resultado
      if ((chChar < '0') || (chChar > '9'))
        result.deleteCharAt(i);
    } // for
    // retorna
    return result.toString();
  }

  /**
   * Retorna 'value' sem espaços entre os nomes.
   * @param value Valor que se deseja remover os espaços.
   * @return Retorna 'value' sem espaços entre os nomes.
   */
  static public String removeSpaces(String value) {
    return value.replaceAll(" ", "");
  }

  /**
   * Retorna uma String gerada a partir do algoritimo de combinação inversa
   * com o tamanho indicado por 'length'.
   * @param value String String de origem.
   * @param length int Tamanho da String gerada.
   * @return String Retorna uma String gerada a partir do algoritimo de combinação
   *         inversa com o tamanho indicado por 'length'.
   */
  static public String reverseCombination(String value, int length) {
    // nossa origem
    StringBuffer source = new StringBuffer(value);
    // nosso resultado
    StringBuffer result = new StringBuffer(length);
    // construindo a combinação
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
   * Retorna 'value' com '_' no lugar dos espaços.
   * @param value Valor que se desejar trocar os espaços por '_'.
   * @return Retorna 'value' com '_' no lugar dos espaços.
   */
  static public String spacesToUnderline(String value) {
    return value.replaceAll(" ", "_");
  }

  /**
   * Retorna uma nova String formada por 'count' caracteres de 'ch'.
   * @param ch Caractere que serão usados para preencher a String.
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
   * Faz as transformações necessárias em 'text' para sua correta exibição em
   * páginas HTML. <p>
   * Ajustes realizados: <br>
   * <li> \r\n, \n\r, \r e \n são substituídos por &lt;br /&gt;
   * <li> " são substituíddas por '
   * @param text Texto para ser transformado em texto HTML.
   * @return Retorna 'text' com as transformações necessárias para
   *         sua correta exibição em páginas HTML.
   */
  static public String textToHTMLText(String text) {
    // se não recebemos nada...dispara
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
    // se não temos nada
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
      // se não é a primeira palavra...põe o primeiro caractere da palavra em maiúsculo
      result.append((i == 0 ? firstChar.toLowerCase() : firstChar.toUpperCase()) + name.substring(1, name.length()).toLowerCase());
    } // for
    // retorna
    return result.toString();
  }

  /**
   * Retorna 'value' com as primeiras letras de cada nome em maiúsculo.
   * @param value Valor que se deseja captalizar.
   * @return Retorna 'value' com as primeiras letras de cada nome em maiúsculo.
   */
  static public String toCaptalCase(String value) {
    return toCaptalCase(value, false);
  }

  /**
   * Retorna 'value' com as primeiras letras de cada nome em maiúsculo.
   * @param value Valor que se deseja captalizar.
   * @param preserveSpaces True para que os espaços entre as palavras sejam
   *                       preservados.
   * @return Retorna 'value' com as primeiras letras de cada nome em maiúsculo.
   */
  static public String toCaptalCase(String value, boolean preserveSpaces) {
    // se não temos nada
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
      // se é um conectivo...
      if ("de-do-dos-da-das-em-os-as".indexOf(name) >= 0)
        // mantém em minúsculo
        result.append(name);
      // se não é...
      else {
        // primeiro caractere
        String firstChar = name.charAt(0) + "";
        // põe o primeiro caractere da palavra em maiúsculo
        result.append(firstChar.toUpperCase() + name.substring(1, name.length()));
      } // if
      // se é para preservar os espaços...
      if (preserveSpaces && (i<names.length-1))
        result.append(" ");
    } // for
    // retorna
    return result.toString();
  }

}
