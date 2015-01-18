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

import java.lang.reflect.*;

import iobjects.*;

/**
 * Ferramenta para validação de dígitos verificadores em vários tipos de
 * inscrições.
 */
public class DigitVerifier {

  /**
   * Retorna o dígito verificador de value na metodologia Módulo 10.
   * @param value String Valor cujo dígito verificar será calculado.
   * @return int Retorna o dígito verificador de value na metodologia Módulo 10.
   */
  static public int calculateModulo10DigitVerifier(String value) {
    // retira os caracteres não numéricos
    value = StringTools.removeNotNumbers(value);
    // pesos que variam de 2 a 1 da direita para a esquerda
    int[] pesos = new int[value.length()];
    int peso = 2;
    for (int i=value.length()-1; i>=0; i--) {
      pesos[i] = peso;
      peso--;
      if (peso < 1)
        peso = 2;
    } // for
    // multiplica cada algarismo pelo seu respectivo peso
    for (int i=0; i<pesos.length; i++) {
      pesos[i] = pesos[i] * Integer.parseInt(value.charAt(i) + "");
      if (pesos[i] >= 10)
        pesos[i] = Integer.parseInt((pesos[i] + "").substring(0, 1)) + Integer.parseInt((pesos[i] + "").substring(1, 2));
    }
    // soma tudo
    int soma = 0;
    for (int i=0; i<pesos.length; i++) {
      soma += pesos[i];
    } // for
    // próximo múltiplo de 10 maior que a soma
    int proximoMultiplo = 10;
    if (soma >= 10)
      proximoMultiplo = (Integer.parseInt((soma + "").substring(0, 1)) + 1) * 10;
    // subtrai a soma do próximo múltiplo de 10
    int result = proximoMultiplo - soma;
    // retorna
    if (result == 10)
      return 0;
    else
      return result;
  }

  /**
   * Retorna o dígito verificador de value na metodologia Módulo 11 ou -1
   * informando que o dígito verificador é 'X'.
   * @param value String Valor cujo dígito verificar será calculado.
   * @return int Retorna o dígito verificador de value na metodologia Módulo 11
   *             ou -1 informando que o dígito verificador é 'X'.
   */
  static public int calculateModulo11DigitVerifier(String value) {
    // retira os caracteres não numéricos
    value = StringTools.removeNotNumbers(value);
    // pesos que variam de 9 a 2 da direita para a esquerda
    int[] pesos = new int[value.length()];
    int peso = 9;
    for (int i=value.length()-1; i>=0; i--) {
      pesos[i] = peso;
      peso--;
      if (peso < 2)
        peso = 9;
    } // for
    // multiplica cada algarismo pelo seu respectivo peso
    for (int i=0; i<pesos.length; i++)
      pesos[i] = pesos[i] * Integer.parseInt(value.charAt(i) + "");
    // soma tudo
    int soma = 0;
    for (int i=0; i<pesos.length; i++)
      soma += pesos[i];
    // o resto da divisão da soma por 11 é o dígito verificador
    int result = soma % 11;
    // retorna
    if (result == 10)
      return -1;
    else
      return result;
  }

  /**
   * Retorna o dígito verificador de value na metodologia Módulo 11X.
   * @param value String Valor cujo dígito verificar será calculado.
   * @return int Retorna o dígito verificador de value na metodologia Módulo 11X.
   */
  static public int calculateModulo11XDigitVerifier(String value) {
    // retira os caracteres não numéricos
    value = StringTools.removeNotNumbers(value);
    // pesos que variam de 2 a 7 da direita para a esquerda
    int[] pesos = new int[value.length()];
    int peso = 2;
    for (int i=value.length()-1; i>=0; i--) {
      pesos[i] = peso;
      peso++;
      if (peso > 7)
        peso = 2;
    } // for
    // multiplica cada algarismo pelo seu respectivo peso
    for (int i=0; i<pesos.length; i++)
      pesos[i] = pesos[i] * Integer.parseInt(value.charAt(i) + "");
    // soma tudo
    int soma = 0;
    for (int i=0; i<pesos.length; i++)
      soma += pesos[i];
    // obtém o resto da divisão da soma por 11
    int result = soma % 11;
    // retorna
    if (result > 1)
      return 11 - result;
    else
      return result;
  }

  /**
   * Retorna 'value' acrescido do dígito verificador simples.
   * @param value int Valor cujo dígito verificador se deseja calcular.
   * @return int Retorna 'value' acrescido do dígito verificador simples.
   * @since 2006
   */
  static public int calculateSimpleDigitVerifier(int value) {
    // nosso resultado
    int result = 0;
    int factor = 0;
    // se o valor é par...o fator é 6
    if (value % 2 == 0)
      factor = 6;
    // se o valor é ímpar...o fator é 9
    else
      factor = 9;
    // multiplica cada algarismo pelo seu índice + factor
    String strValue = Integer.toString(value);
    for (int i=0; i<strValue.length(); i++) {
      result += Integer.parseInt(strValue.charAt(i) + "") * i + factor;
    } // for
    // o dígito verificador será o último algarismo
    String strResult = Integer.toString(result);
    result = Integer.parseInt(strResult.charAt(strResult.length()-1) + "");
    // retorna
    return Integer.parseInt(value + "" + result);
  }

  /**
   * Retorna true se 'value' for um CNPJ válido.
   * @param value String CNPJ para ser verificado.
   * @return boolean Retorna true se 'value' for um CNPJ válido.
   */
  static public boolean isCnpjValid(String value) {
    // retira todos os caracteres não numéricos
    value = StringTools.removeNotNumbers(value);
    // se o tamanho não é válido...dispara
    if (value.length() != 14)
      return false;
    // dígito verificador informado
    String digitoInformado = value.substring(12);
    // primeiro digito verificador
    int a = Integer.parseInt(value.charAt(11) + "") * 2;
    int b = Integer.parseInt(value.charAt(10) + "") * 3;
    int c = Integer.parseInt(value.charAt(9) + "") * 4;
    int d = Integer.parseInt(value.charAt(8) + "") * 5;
    int e = Integer.parseInt(value.charAt(7) + "") * 6;
    int f = Integer.parseInt(value.charAt(6) + "") * 7;
    int g = Integer.parseInt(value.charAt(5) + "") * 8;
    int h = Integer.parseInt(value.charAt(4) + "") * 9;
    int i = Integer.parseInt(value.charAt(3) + "") * 2;
    int j = Integer.parseInt(value.charAt(2) + "") * 3;
    int k = Integer.parseInt(value.charAt(1) + "") * 4;
    int l = Integer.parseInt(value.charAt(0) + "") * 5;
    int total = a + b + c + d + e + f + g + h + i + j + k + l;
    int resto = total % 11;
    int digito1 = (resto == 0 || resto == 1) ? 0 : 11 - resto;
    // segundo dígito verificador
    a = digito1 * 2;
    b = Integer.parseInt(value.charAt(11) + "") * 3;
    c = Integer.parseInt(value.charAt(10) + "") * 4;
    d = Integer.parseInt(value.charAt(9) + "") * 5;
    e = Integer.parseInt(value.charAt(8) + "") * 6;
    f = Integer.parseInt(value.charAt(7) + "") * 7;
    g = Integer.parseInt(value.charAt(6) + "") * 8;
    h = Integer.parseInt(value.charAt(5) + "") * 9;
    i = Integer.parseInt(value.charAt(4) + "") * 2;
    j = Integer.parseInt(value.charAt(3) + "") * 3;
    k = Integer.parseInt(value.charAt(2) + "") * 4;
    l = Integer.parseInt(value.charAt(1) + "") * 5;
    int m = Integer.parseInt(value.charAt(0) + "") * 6;
    total = a + b + c + d + e + f + g + h + i + j + k + l + m;
    resto = total % 11;
    int digito2 = (resto == 0 || resto == 1) ? 0 : 11 - resto;
    // retorna
    return digitoInformado.equals(digito1 + "" + digito2);
  }

  /**
   * Retorna true se 'value' for um CPF válido.
   * @param value String CPF para ser verificado.
   * @return boolean Retorna true se 'value' for um CPF válido.
   */
  static public boolean isCpfValid(String value) {
    // retira todos os caracteres não numéricos
    value = StringTools.removeNotNumbers(value);
    // completa o valor com zeros a esquerda
    value = "000" + value;
    // se o tamanho não é válido...dispara
    if (value.length() != 14)
      return false;
    // dígito verificador informado
    String digitoInformado = value.substring(12);
    // primeiro digito verificador
    int a = Integer.parseInt(value.charAt(11) + "") * 2;
    int b = Integer.parseInt(value.charAt(10) + "") * 3;
    int c = Integer.parseInt(value.charAt(9) + "") * 4;
    int d = Integer.parseInt(value.charAt(8) + "") * 5;
    int e = Integer.parseInt(value.charAt(7) + "") * 6;
    int f = Integer.parseInt(value.charAt(6) + "") * 7;
    int g = Integer.parseInt(value.charAt(5) + "") * 8;
    int h = Integer.parseInt(value.charAt(4) + "") * 9;
    int i = Integer.parseInt(value.charAt(3) + "") * 10;
    int total = a + b + c + d + e + f + g + h + i;
    int resto = total % 11;
    int digito1 = (resto == 0 || resto == 1) ? 0 : 11 - resto;
    // segundo dígito verificador
    a = digito1 * 2;
    b = Integer.parseInt(value.charAt(11) + "") * 3;
    c = Integer.parseInt(value.charAt(10) + "") * 4;
    d = Integer.parseInt(value.charAt(9) + "") * 5;
    e = Integer.parseInt(value.charAt(8) + "") * 6;
    f = Integer.parseInt(value.charAt(7) + "") * 7;
    g = Integer.parseInt(value.charAt(6) + "") * 8;
    h = Integer.parseInt(value.charAt(5) + "") * 9;
    i = Integer.parseInt(value.charAt(4) + "") * 10;
    int j = Integer.parseInt(value.charAt(3) + "") * 11;
    total = a + b + c + d + e + f + g + h + i + j;
    resto = total % 11;
    int digito2 = (resto == 0 || resto == 1) ? 0 : 11 - resto;
    // retorna
    return digitoInformado.equals(digito1 + "" + digito2);
  }

  /**
   * Retorna true se 'value' for uma inscrição válida para o estado 'uf'.
   * @param value String IE para ser verificada.
   * @param uf String Unidade da Federação.
   * @return boolean Retorna true se 'value' for uma inscrição válida para o estado 'uf'.
   * @since 2006
   */
  static public boolean isIeValid(String value,
                                  String uf) {
    // constrói o nome do método que iremos chamar
    String methodName = uf.toUpperCase() + DigitVerifierIe.VERIFIER_METHOD_SUFIX_NAME;
    // localiza o método de verificação da IE que tem
    // como argumentos apenas uma String
    Method method = null;
    try {
      // nossa instancia da classe atual
      DigitVerifierIe digitVerifierIe = new DigitVerifierIe();
      // localiza o método
      Class[] classArray = {"".getClass()};
      method = digitVerifierIe.getClass().getMethod(methodName, classArray);
      // chama o método, verificando a IE e retornando seu valor
      Object[] args = {value};
      Object result = method.invoke(digitVerifierIe, args);
      return ((Boolean)result).booleanValue();
    }
    // se não achamos o método...avisa
    catch (NoSuchMethodException e) {
      throw new RuntimeException(new ExtendedException("iobjects.util.DigitVerifier", "isIeValid", "O método de verificação para o estado " + uf.toUpperCase() + " não foi encontrado."));
    }
    // em caso de outra exceção...avisa
    catch (Exception e) {
      throw new RuntimeException(new ExtendedException("iobjects.util.DigitVerifier", "isIeValid", e));
    } // try-catch
  }

}
