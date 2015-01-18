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

import java.text.*;

import iobjects.*;

/**
 * Classe utilitária para manipulação de números.
 */
public class NumberTools {

  static {
    // define o formato padrão como sendo Brasil
    java.util.Locale.setDefault(new java.util.Locale("pt", "BR"));
  }
  
  /**
   * Retorna um int[] contendo os elementos de 'array' mais 'value'.
   * @param array int[] original.
   * @param value int para concatenar.
   * @return Retorna um int[] contendo os elementos de 'array' mais 'value'.
   */
  static public int[] arrayConcat(int[] array, int value) {
    int[] result = new int[array.length + 1];
    for (int i=0; i<array.length; i++)
      result[i] = array[i];
    array[array.length-1] = value;
    return result;
  }

  /**
   * Retorna um int[] contendo os elementos de 'array1' e 'array2'.
   * @param array1 int[] Primeiro int[] para concatenar.
   * @param array2 int[] Segundo int[] para concatenar.
   * @return int[] Retorna um int[] contendo os elementos de 'array1' e 'array2'.
   */
  static public int[] arrayConcat(int[] array1, int[] array2) {
    int[] result = new int[array1.length + array2.length];
    for (int i=0; i<array1.length; i++)
      result[i] = array1[i];
    for (int i=0; i<array2.length; i++)
      result[array1.length + i] = array2[i];
    return result;
  }

  /**
   * Retorna um double[] contendo os elementos de 'array' mais 'value'.
   * @param array double[] original.
   * @param value double para concatenar.
   * @return Retorna um double[] contendo os elementos de 'array' mais 'value'.
   */
  static public double[] arrayConcat(double[] array, double value) {
    double[] result = new double[array.length + 1];
    for (int i=0; i<array.length; i++)
      result[i] = array[i];
    array[array.length-1] = value;
    return result;
  }

  /**
   * Retorna um double[] contendo os elementos de 'array1' e 'array2'.
   * @param array1 double[] Primeiro double[] para concatenar.
   * @param array2 double[] Segundo double[] para concatenar.
   * @return double[] Retorna um double[] contendo os elementos de 'array1' e 'array2'.
   */
  static public double[] arrayConcat(double[] array1, double[] array2) {
    double[] result = new double[array1.length + array2.length];
    for (int i=0; i<array1.length; i++)
      result[i] = array1[i];
    for (int i=0; i<array2.length; i++)
      result[array1.length + i] = array2[i];
    return result;
  }

  /**
   * Retorna true se 'array' contém 'value'.
   * @param array int[] de valores para pesquisa.
   * @param value int para pesquisar em 'array'.
   * @return Retorna true se 'array' contém 'value'.
   */
  static public boolean arrayContains(int[] array, int value) {
    // retorna true se o índice for válido
    return arrayIndexOf(array, value) >= 0;
  }

  /**
   * Retorna true se 'array' contém todos os elementos de 'subArray' informados.
   * @param array int[] de valores para pesquisa.
   * @param subArray int[] cujos valores se deseja pesquisar em 'array'.
   * @return Retorna true se 'array' contém todos os elementos de 'subArray'
   *         informados.
   */
  static public boolean arrayContains(int[] array, int[] subArray) {
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
   * Retorna a quantidade de vezes que 'array' contém de 'value'.
   * @param array int[] de valores para pesquisa.
   * @param value int que se deseja pesquisar em 'array'.
   * @return Retorna a quantidade de vezes que 'array' contém de 'value'.
   */
  static public int arrayContainsCount(int[] array, int value) {
    // nosso retorno
    int result = 0;
    // verifica todos os valores de array
    for (int i=0; i<array.length; i++) {
      // valor da vez
      int thisValue = array[i];
      if (value == thisValue)
        result++;
    } // for
    // retorna o que achamos
    return result;
  }

  /**
   * Retorna a quantidade de elementos que 'array' contém de 'subArray'.
   * @param array int[] de valores para pesquisa.
   * @param subArray int[] cujos valores se deseja pesquisar em 'array'.
   * @return Retorna a quantidade de elementos que 'array' contém de 'subArray'.
   */
  static public int arrayContainsCount(int[] array, int[] subArray) {
    // nosso retorno
    int result = 0;
    // verifica todos os valores de subArray
    for (int i=0; i<subArray.length; i++) {
      // valor da vez
      int[] value = {subArray[i]};
      if (arrayContains(array, value))
        result++;
    } // for
    // retorna o que achamos
    return result;
  }

  /**
   * Retorna um int[] contendo os elementos de 'array' entre os índices
   * 'fromIndex' e 'toIndex'.
   * @param array int[] contendo os elementos que serão copiados.
   * @param fromIndex int Índice do elemento inicial.
   * @param toIndex int Índice do elemento final.
   * @return int[] Retorna um int[] contendo os elementos de 'array' entre
   *         os índices 'fromIndex' e 'toIndex'.
   */
  static public int[] arrayGetElements(int[] array, int fromIndex, int toIndex) {
    int count = (toIndex - fromIndex) + 1;
    int[] result = new int[count];
    for (int i=0; i<count; i++)
      result[i] = array[fromIndex + i];
    return result;
  }

  /**
   * Retorna o índice de 'element' em 'array'. Retorna -1 se 'element' não for
   * encontrado.
   * @param array int[] contendo os elementos onde se deseja pesquisar.
   * @param element int que se deseja localizar em 'array'.
   * @return Retorna o índice de 'element' em 'array'. Retorna -1 se 'element'
   *         não for encontrado.
   */
  static public int arrayIndexOf(int[] array, int element) {
    for (int i=0; i<array.length; i++) {
      if (array[i] == element)
        return i;
    } // for
    return -1;
  }

  /**
   * Retorna um int[] contendo os elementos existentes em 'array1' e 'array2'.
   * @param array1 int[] Primeiro int[] para intersessão.
   * @param array2 int[] Segundo int[] para intersessão.
   * @return int[] Retorna um int[] contendo os elementos existentes em 'array1'
   *         e 'array2'.
   */
  static public int[] arrayIntercession(int[] array1, int[] array2) {
    // verifica quantos elementos de array1 existem em array2
    int intercession = arrayContainsCount(array1, array2);
    // nosso resultado
    int[] result = new int[intercession];
    // loop em um dos arrays
    int r = 0;
    for (int i=0; i<array1.length; i++) {
      // se o item está contido nos dois array...insere no resultado
      if (arrayContains(array2, array1[i])) {
        result[r] = array1[i];
        r++;
      } // if
    } // for
    // retorna
    return result;
  }

  /**
   * Retorna 'array' com seus elementos ordenados alfabeticamente.
   * @param array Array cujos elementos se deseja ordenar.
   * @return Retorna 'array' com seus elementos ordenados alfabeticamente.
   */
  static public int[] arraySort(int[] array) {
    // ordena
    java.util.Arrays.sort(array);
    // retorna
    return array;
  }

  /**
   * Retorna true se os dois Stirng[]´s informados contiverem valores iguais.
   * @param array1 Primeiro int[] de valores para comparar.
   * @param array2 Segundo int[] de valores para comparar.
   * @return Retorna true se os dois Stirng[]´s informados contiverem valores
   *         iguais.
   */
  static public boolean compareArrays(int[] array1, int[] array2) {
    // retorna falso para todo caso
    boolean result = false;
    // se não temos a mesma quantidade de valores nas duas listas...dispara
    if (array1.length != array2.length)
      return result;
    // verifica todos os valores
    for (int i=0; i<array1.length; i++) {
      // valor da vez
      result = array1[i] == array2[i];
      // se não bateu...já podemos disparar
      if (!result)
        return result;
    } // for
    // retorna o que achamos
    return result;
  }

  /**
   * Retorna true se 'array' contém 'value'.
   * @param array double[] de valores para pesquisa.
   * @param value double para pesquisar em 'array'.
   * @return Retorna true se 'array' contém 'value'.
   */
  static public boolean arrayContains(double[] array, double value) {
    // retorna true se o índice for válido
    return arrayIndexOf(array, value) >= 0;
  }

  /**
   * Retorna true se 'array' contém todos os elementos de 'subArray' informados.
   * @param array double[] de valores para pesquisa.
   * @param subArray double[] cujos valores se deseja pesquisar em 'array'.
   * @return Retorna true se 'array' contém todos os elementos de 'subArray'
   *         informados.
   */
  static public boolean arrayContains(double[] array, double[] subArray) {
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
   * Retorna a quantidade de vezes que 'array' contém de 'value'.
   * @param array double[] de valores para pesquisa.
   * @param value double que se deseja pesquisar em 'array'.
   * @return Retorna a quantidade de vezes que 'array' contém de 'value'.
   */
  static public double arrayContainsCount(double[] array, double value) {
    // nosso retorno
    double result = 0;
    // verifica todos os valores de array
    for (int i=0; i<array.length; i++) {
      // valor da vez
      double thisValue = array[i];
      if (value == thisValue)
        result++;
    } // for
    // retorna o que achamos
    return result;
  }

  /**
   * Retorna a quantidade de elementos que 'array' contém de 'subArray'.
   * @param array double[] de valores para pesquisa.
   * @param subArray double[] cujos valores se deseja pesquisar em 'array'.
   * @return Retorna a quantidade de elementos que 'array' contém de 'subArray'.
   */
  static public int arrayContainsCount(double[] array, double[] subArray) {
    // nosso retorno
    int result = 0;
    // verifica todos os valores de subArray
    for (int i=0; i<subArray.length; i++) {
      // valor da vez
      double[] value = {subArray[i]};
      if (arrayContains(array, value))
        result++;
    } // for
    // retorna o que achamos
    return result;
  }

  /**
   * Retorna um double[] contendo os elementos de 'array' entre os índices
   * 'fromIndex' e 'toIndex'.
   * @param array double[] contendo os elementos que serão copiados.
   * @param fromIndex int Índice do elemento inicial.
   * @param toIndex int Índice do elemento final.
   * @return double[] Retorna um double[] contendo os elementos de 'array' entre
   *         os índices 'fromIndex' e 'toIndex'.
   */
  static public double[] arrayGetElements(double[] array, int fromIndex, int toIndex) {
    int count = (toIndex - fromIndex) + 1;
    double[] result = new double[count];
    for (int i=0; i<count; i++)
      result[i] = array[fromIndex + i];
    return result;
  }

  /**
   * Retorna o índice de 'element' em 'array'. Retorna -1 se 'element' não for
   * encontrado.
   * @param array double[] contendo os elementos onde se deseja pesquisar.
   * @param element double que se deseja localizar em 'array'.
   * @return Retorna o índice de 'element' em 'array'. Retorna -1 se 'element'
   *         não for encontrado.
   */
  static public double arrayIndexOf(double[] array, double element) {
    for (int i=0; i<array.length; i++) {
      if (array[i] == element)
        return i;
    } // for
    return -1;
  }

  /**
   * Retorna um double[] contendo os elementos existentes em 'array1' e 'array2'.
   * @param array1 double[] Primeiro double[] para doubleersessão.
   * @param array2 double[] Segundo double[] para doubleersessão.
   * @return double[] Retorna um double[] contendo os elementos existentes em 'array1'
   *         e 'array2'.
   */
  static public double[] arraydoubleercession(double[] array1, double[] array2) {
    // verifica quantos elementos de array1 existem em array2
    int intercession = arrayContainsCount(array1, array2);
    // nosso resultado
    double[] result = new double[intercession];
    // loop em um dos arrays
    int r = 0;
    for (int i=0; i<array1.length; i++) {
      // se o item está contido nos dois array...insere no resultado
      if (arrayContains(array2, array1[i])) {
        result[r] = array1[i];
        r++;
      } // if
    } // for
    // retorna
    return result;
  }

  /**
   * Retorna 'array' com seus elementos ordenados alfabeticamente.
   * @param array Array cujos elementos se deseja ordenar.
   * @return Retorna 'array' com seus elementos ordenados alfabeticamente.
   */
  static public double[] arraySort(double[] array) {
    // ordena
    java.util.Arrays.sort(array);
    // retorna
    return array;
  }

  /**
   * Retorna true se os dois Stirng[]´s informados contiverem valores iguais.
   * @param array1 Primeiro double[] de valores para comparar.
   * @param array2 Segundo double[] de valores para comparar.
   * @return Retorna true se os dois Stirng[]´s informados contiverem valores
   *         iguais.
   */
  static public boolean compareArrays(double[] array1, double[] array2) {
    // retorna falso para todo caso
    boolean result = false;
    // se não temos a mesma quantidade de valores nas duas listas...dispara
    if (array1.length != array2.length)
      return result;
    // verifica todos os valores
    for (int i=0; i<array1.length; i++) {
      // valor da vez
      result = array1[i] == array2[i];
      // se não bateu...já podemos disparar
      if (!result)
        return result;
    } // for
    // retorna o que achamos
    return result;
  }

  /**
   * Retorna 'value' como uma String e adiciona zeros à esquerda, fazendo
   * que o valor retornado tenha o tamanho 'length'.
   * @param value Valor para ser convertido.
   * @param length Tamanho final da String de retorno com zeros a esquerda.
   * @return Retorna 'value' como uma String e adiciona zeros à esquerda, fazendo
   *         que o valor retornado tenha o tamanho 'length'.
   */
  static public String completeZero(int value, int length) {
    StringBuffer result = new StringBuffer(Integer.toString(value));
    while (result.length() < length)
      result.insert(0, '0');
    return result.toString();
  }

  /**
   * Retorna a quantidade de dígitos existentes na parte fracionária de 'value'.
   * @param value double Valor cuja quantidade de dígitos fracionários se deseja
   *                     obter.
   * @return int Retorna a quantidade de dígitos existentes na parte fracionária
   *             de 'value'.
   */
  static public int countFractionDigits(double value) {
    // representação em String do valor recebido
    String strValue = Double.toString(value);
    // procura pelo separador decimal que deverá ser
    // o primeiro caractere, da direita para a esquerda,
    // que não esteja entre 0 e 9
    for (int i=0; i<strValue.length(); i++) {
      // caractere da vez
      char chr = strValue.charAt(strValue.length()-i-1);
      // se não está entre 0 e 9...retorna a posição do caractere que
      // indica exatamente a qde de casas decimais
      if ((chr < '0') || (chr > '9'))
        return i;
    } // for
    // se chegou até aqui...não temos casas decimais
    return 0;
  }

  /**
   * Retorna 'value' formatado de acordo com as configurações da localidade
   * default.
   * @param value double Valor para ser formatado.
   * @param maxFractionDigits int Número máximo de casas decimais.
   * @param minimunFractionDigits int Número mínimo de casas decimais.
   * @return String Retorna 'value' formatado de acordo com as configurações da
   *                localidade default.
   */
  static public String format(double value,
                              int    maxFractionDigits,
                              int    minimunFractionDigits) {
    // nosso formatador
    NumberFormat format = DecimalFormat.getInstance();
    
    // configura
    format.setMaximumFractionDigits(maxFractionDigits);
    format.setMinimumFractionDigits(minimunFractionDigits);
    format.setGroupingUsed(true);
    // retorna o valor
    return format.format(value);
  }

  /**
   * Retorna 'value' formatado de acordo com as configurações da localidade
   * default com 2 casa decimais.
   * @param value double Valor para ser formatado.
   * @return String Retorna 'value' formatado de acordo com as configurações da
   *         localidade default com 2 casa decimais.
   */
  static public String format(double value) {
    return format(value, 2, 2);
  }

  /**
   * Retorna 'value' formatado de acordo com as configurações da localidade
   * default.
   * @param value int Valor para ser formatado.
   * @return String Retorna 'value' formatado de acordo com as configurações da
   *         localidade default.
   */
  static public String format(int value) {
    // nosso formatador
    NumberFormat format = NumberFormat.getInstance();
    // configura
    format.setMaximumFractionDigits(0);
    format.setMinimumFractionDigits(0);
    format.setGroupingUsed(true);
    // retorna o valor
    return format.format(value);
  }

  /**
   * Retorna o valor recebido com formatação interpretado como double.
   * @param value String Valor formatado para ser interpretado.
   * @param maxFractionDigits int Quantidade máxima de casas decimais aceitáveis.
   * @throws Exception No caso de exceção na tentativa de interpretar o valor
   *                   recebido ou a quantidade de casas decimais exceder o
   *                   máximo permitido.
   * @return double Retorna o valor recebido com formatação interpretado como double.
   */
  static public double parseDouble(String value,
                                   int    maxFractionDigits) throws Exception {
    // nosso formatador
    NumberFormat format = DecimalFormat.getInstance();
    // se recebemos um valor vazio...define como 0
    if (value.equals(""))
      value = "0";
    // faz o parser
    Number number = format.parse(value);
    // se excedeu a quantidade de casas decimais...exceção
    if (countFractionDigits(number.doubleValue()) > maxFractionDigits)
      throw new ExtendedException("NumberTools", "parseDouble", "A quantidade máxima de casas decimais (" + maxFractionDigits + ") " +" foi excedida em " + value + ".");
    // retorna o valor
    return number.doubleValue();
  }

  /**
   * Retorna o valor recebido com formatação interpretado como double e permitindo
   * 2 casas decimais.
   * @param value String Valor formatado para ser interpretado.
   * @throws Exception No caso de exceção na tentativa de interpretar o valor
   *                   recebido ou a quantidade de casas decimais exceder o
   *                   máximo permitido.
   * @return double Retorna o valor recebido com formatação interpretado como double.
   */
  static public double parseDouble(String value) throws Exception {
    return parseDouble(value, 2);
  }

  /**
   * Retorna o array recebido com valores formatados como um double[] e permitindo
   * 2 casas decimais.
   * @param array String[] Array de valores formatados para ser interpretado.
   * @throws Exception No caso de exceção na tentativa de interpretar um dos valores
   *                   recebidos ou a quantidade de casas decimais exceder o
   *                   máximo permitido.
   * @return int Retorna o array recebido com valores formatados como um double[]
   *             e permitindo 2 casas decimais.
   */
  static public double[] parseDoubleArray(String[] array) throws Exception {
    // nosso resultado
    double[] result = new double[array.length];
    // preenche
    for (int i=0; i<result.length; i++)
      result[i] = parseDouble(array[i]);
    // retorna
    return result;
  }

  /**
   * Retorna o valor recebido como um inteiro. Os caracteres não numéricos
   * são retirados antes da conversão evitando exeções.
   * @param value String Valor para ser convertido.
   * @throws Exception No caso de exceção na tentativa de interpretar o valor
   *                   recebido.
   * @return int Retorna o valor recebido como um inteiro. Os caracteres não
   *             numéricos são retirados antes da conversão evitando exeções.
   */
  static public int parseInt(String value) throws Exception {
    // compõe a String válida que será avaliada
    String newValue = "";
    for (int i=0; i<value.length(); i++) {
      // caracter da vez
      char number = value.charAt(i);
      // se está entre 0 e 9 ou é "-"...guarda
      if (((number >= '0') && (number <= '9')) || (number == '-'))
        newValue += number;
    } // for
    // se o valor a ser avaliado continua vazio...
    if (newValue.equals(""))
      return 0;
    else
      return Integer.parseInt(newValue);
  }

  /**
   * Retorna o array recebido como um int[]. Os caracteres não numéricos
   * são retirados antes da conversão evitando exeções.
   * @param array String[] Array de valores para ser convertido.
   * @throws Exception No caso de exceção na tentativa de interpretar um dos valores
   *                   recebidos.
   * @return int Retorna o array recebido como um int[]. Os caracteres não
   *             numéricos são retirados antes da conversão evitando exeções.
   */
  static public int[] parseIntArray(String[] array) throws Exception {
    // nosso resultado
    int[] result = new int[array.length];
    // preenche
    for (int i=0; i<result.length; i++)
      result[i] = parseInt(array[i]);
    // retorna
    return result;
  }

  /**
   * Retorna 'value' arrendondado para a precisão informada.
   * @param value double Valor que se deseja arredondar.
   * @param precision int Precisão do arredondamento.
   * @return double Retorna 'value' arrendondado para a precisão informada.
   */
  static public double round(double value,
                             int    precision) {
    // multiplicador de precisão
    double precisionMultiplier = Math.pow(10, precision);
    // multiplica o valor recebido
    value = value * precisionMultiplier;
    // arredonda
    value = Math.round(value);
    // divide pelo multiplicador retornando à pecisão desejada
    value = value / precisionMultiplier;
    // retorna
    return value;
  }

  /**
   * Retorna 'value' arredondado com precisão igual a 2.
   * @param value double Valor que se deseja arredondar.
   * @return double Retorna 'value' arredondado com precisão igual a 2.
   */
  static public double round(double value) {
    return round(value, 2);
  }

}
