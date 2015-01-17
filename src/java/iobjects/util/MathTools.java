package iobjects.util;

import java.math.*;

/**
 * Classe utilit�ria com f�rmulas e rotinas matem�ticas.
 * @since 2006
 */
public class MathTools {

  private MathTools() {
  }

  /**
   * Retorna a quantidade de combina��es poss�veis de 'n', 'k' em 'k', resultado
   * da f�rmula: <b>n!/k!(n-k)!</b>.
   * @param n int N�mero de op��es existentes.
   * @param k int Agrupamentos desejados.
   * @return int Retorna a quantidade de combina��es poss�veis de 'n', 'k' em 'k',
   *             resultado da f�rmula: <b>n!/k!(n-k)!</b>.
   */
  static public int combination(int n,
                                int k) {
    return factorial(n).divide(factorial(k).multiply(factorial(n-k))).intValue();
  }

  /**
   * Retorna o fatorial de 'number', resultado da f�rmula:
   * <b>n * (n-1) * (n-2) * ... * (n-n+1)</b>.
   * @param n int N�mero cujo fatorial se deseja obter.
   * @return BigInteger Retorna o fatorial de 'number', resultado da f�rmula:
   * <b>n * (n-1) * (n-2) * ... * (n-n+1)</b>.
   */
  static public BigInteger factorial(int n) {
    BigInteger result = BigInteger.valueOf(n);
    for (int i=n-1; i>0; i--)
      result = result.multiply(BigInteger.valueOf(i));
    return result;
  }

}
