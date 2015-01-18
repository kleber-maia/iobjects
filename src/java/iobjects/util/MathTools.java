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

import java.math.*;

/**
 * Classe utilitária com fórmulas e rotinas matemáticas.
 * @since 2006
 */
public class MathTools {

  private MathTools() {
  }

  /**
   * Retorna a quantidade de combinações possíveis de 'n', 'k' em 'k', resultado
   * da fórmula: <b>n!/k!(n-k)!</b>.
   * @param n int Número de opções existentes.
   * @param k int Agrupamentos desejados.
   * @return int Retorna a quantidade de combinações possíveis de 'n', 'k' em 'k',
   *             resultado da fórmula: <b>n!/k!(n-k)!</b>.
   */
  static public int combination(int n,
                                int k) {
    return factorial(n).divide(factorial(k).multiply(factorial(n-k))).intValue();
  }

  /**
   * Retorna o fatorial de 'number', resultado da fórmula:
   * <b>n * (n-1) * (n-2) * ... * (n-n+1)</b>.
   * @param n int Número cujo fatorial se deseja obter.
   * @return BigInteger Retorna o fatorial de 'number', resultado da fórmula:
   * <b>n * (n-1) * (n-2) * ... * (n-n+1)</b>.
   */
  static public BigInteger factorial(int n) {
    BigInteger result = BigInteger.valueOf(n);
    for (int i=n-1; i>0; i--)
      result = result.multiply(BigInteger.valueOf(i));
    return result;
  }

}
