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

import java.security.spec.*;
import javax.crypto.*;
import javax.crypto.spec.*;

import sun.misc.*;

import iobjects.*;

/**
 * Ferramenta para criptografia de Strings.
 * @since 3.1
 */
public class Encrypter {

  /**
   * Chave padrão utilizada para criptografia.
   */
  public static final String KEY_DEFAULT = "EsTa E uMa ChAvE pAdRaO dE cRiPtOgRaFiA!";
  /**
   * Tamanho mínimo da chave de criptografia.
   */
  public static final int KEY_LENGTH = 32;
  /**
   * Nome do esquema de criptografia DES.
   */
  public static final String SCHEME_DES = "DES";
  /**
   * Nome do esquema de criptografia triple-DES.
   */
  public static final String SCHEME_DESEDE = "DESede";

  private static final String UNICODE_FORMAT = "UTF8";

  private KeySpec          keySpec    = null;
  private SecretKeyFactory keyFactory = null;
  private Cipher           cipher     = null;

  /**
   * Construtor padrão.
   */
  public Encrypter() {
    this(SCHEME_DES, KEY_DEFAULT);
  }

  /**
   * Construtor estendido.
   * @param scheme String Nome do esquema de criptografia.
   * @see DES_ENCRYPTION_SCHEME
   * @see DESEDE_ENCRYPTION_SCHEME
   */
  public Encrypter(String scheme) {
    this(scheme, KEY_DEFAULT);
  }

  /**
   * Construtor estendido.
   * @param scheme String Nome do esquema de criptografia.
   * @param key String Chave de criptografia que não pode ser menor
   *            que KEY_LENGTH.
   * @see DES_ENCRYPTION_SCHEME
   * @see DESEDE_ENCRYPTION_SCHEME
   * @see DEFAULT_ENCRYPTION_KEY
   * @see KEY_LENGTH
   */
  public Encrypter(String scheme,
                   String key) {
    try {
      // se a chave não foi informada...exceção
      if (key == null)
        throw new ExtendedException(getClass().getName(), "", "Chave de criptografia não informada.");
      // se o tamanho da chave é inválida...exceção
      if (key.trim().length() < KEY_LENGTH)
        throw new ExtendedException(getClass().getName(), "", "Chave de criptografia menor que o permitido: " + KEY_LENGTH + ".");
      // transaforma a chave em um array de bytes
      byte[] keyAsBytes = key.getBytes(UNICODE_FORMAT);
      // se o esquema selecionado foi DESEDE_ENCRYPTION_SCHEME...
      if (scheme.equals(SCHEME_DESEDE))
        keySpec = new DESedeKeySpec(keyAsBytes);
      // se o esquema selecionado foi DES_ENCRYPTION_SCHEME...
      else if (scheme.equals(SCHEME_DES))
        keySpec = new DESKeySpec( keyAsBytes );
      // se o esquema selecionado é desconhecido...
      else
        throw new RuntimeException(new ExtendedException(getClass().getName(), "", "Esquema de criptografia desconhecido: " + scheme + "."));
      // nosso Key Factory
      keyFactory = SecretKeyFactory.getInstance(scheme);
      // nosso Cipher
      cipher = Cipher.getInstance(scheme);
    }
    catch (Exception e) {
      throw new RuntimeException(e);
    } // try-catch
  }

  /**
   * Retorna um String obtida a partir do array de bytes informado.
   * @param bytes byte[] Array de bytes para ser convertido em String.
   * @return String Retorna um String obtida a partir do array de bytes informado.
   */
  private static String bytesToString(byte[] bytes) {
    StringBuffer stringBuffer = new StringBuffer();
    for (int i=0; i<bytes.length; i++)
      stringBuffer.append((char)bytes[i]);
    return stringBuffer.toString();
  }

  /**
   * Retorna 'value' como uma String legível. 'value' precisa ser uma String
   * criptografada com o mesmo esquema e chave informados no construtor desta
   * classe.
   * @param value String Valor para ser descriptografado.
   * @return String Retorna 'value' como uma String legível. 'value' precisa
   *                ser uma String criptografada com o mesmo esquema e chave
   *                informados no construtor desta classe.
   */
  public String decrypt(String value) {
    try {
      // se o valor é inválido...exceção
      if (value == null || value.trim().length() <= 0)
        throw new ExtendedException(getClass().getName(), "decrypt", "Valor inválido.");
      // descriptografa
      SecretKey key = keyFactory.generateSecret(keySpec);
      cipher.init(Cipher.DECRYPT_MODE, key);
      BASE64Decoder base64decoder = new BASE64Decoder();
      byte[] cleartext = base64decoder.decodeBuffer(value);
      byte[] ciphertext = cipher.doFinal(cleartext);
      // retorna como String legível
      return bytesToString(ciphertext);
    }
    catch (Exception e) {
      throw new RuntimeException(e);
    } // try-catch
  }

  /**
   * Retorna 'value' como uma String criptografada. Se existirem caracteres
   * acentuados em 'value' o valor descriptografado não será igual ao valor
   * original criptografado.
   * @param value String String para ser criptografada.
   * @return String Retorna 'value' como uma String criptografada.
   */
  public String encrypt(String value) {
    try {
      // se o valor é inválido...exceção
      if (value == null || value.trim().length() == 0)
        throw new ExtendedException(getClass().getName(), "encrypt", "Valor inválido.");
      // criptografa
      SecretKey key = keyFactory.generateSecret(keySpec);
      cipher.init(Cipher.ENCRYPT_MODE, key);
      byte[] cleartext = value.getBytes(UNICODE_FORMAT);
      byte[] ciphertext = cipher.doFinal(cleartext);
      // retorna na base 64
      BASE64Encoder base64encoder = new BASE64Encoder();
      return base64encoder.encode(ciphertext);
    }
    catch (Exception e) {
      throw new RuntimeException(e);
    } // try-catch
  }

}
