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
package iobjects.remote;

import java.io.*;
import java.math.*;
import java.util.*;
import java.sql.*;

import iobjects.*;

/**
 * Representa um InputStream para deserialização de objetos que implementam
 * a classe Serializable.
 */
public class SerializeReader {

  private InputStream input = null;

  /**
   * Construtor padrão.
   * @param input InputStream que será utilizado como entrada da deserialização.
   */
  public SerializeReader(InputStream input) {
    this.input = input;
  }

  /**
   * Retorna o InputStream utilizado como entrada da deserialização.
   * @return InputStream Retorna o InputStream utilizado como entrada da
   *         deserialização.
   */
  public InputStream getInputStream() {
    return input;
  }

  /**
   * Lê o próximo valor em 'input' como um array.
   * @return boolean Lê o próximo valor em 'input' como um array.
   * @throws IOException Em caso de exceção na tentativa de ler de InputStream
   *                     ou de converter para o valor desejado.
   */
  public Object readArray() throws IOException {
    // lê o nome da classe serializada
    String className = readString();
    // se o nome da classe contém a especificação de array...elimina
    if (className.startsWith("["))
      className = className.substring(className.lastIndexOf('[')+2, className.length()-1);
    // lê a quantidade de itens
    int length = readInt();
    Object array;
    try {
      // cria o array
      Class componentType = Class.forName(className);
      array = java.lang.reflect.Array.newInstance(componentType, length);
      // loop nos itens
      for (int i=0; i<length; i++) {
        Object item = readObject();
        java.lang.reflect.Array.set(array, i, item);
      } // for
    }
    catch (Exception e) {
      throw new RuntimeException(e);
    }
    // retorna
    return array;
  }

  /**
   * Lê o próximo valor em 'input' como um boolean.
   * @return boolean Lê o próximo valor em 'input' como um boolean.
   * @throws IOException Em caso de exceção na tentativa de ler de InputStream
   *                   ou de converter para o valor desejado.
   */
  public boolean readBoolean() throws IOException {
    // espera por 1 byte
    waitForBytes(1);
    // lê como um int
    return input.read() == 1;
  }

  /**
   * Lê o próximo valor em 'input' como um byte.
   * @return byte Lê o próximo valor em 'input' como um byte.
   * @throws IOException Em caso de exceção na tentativa de ler de InputStream
   *                   ou de converter para o valor desejado.
   */
  public byte readByte() throws IOException {
    // espera por 1 byte
    waitForBytes(1);
    // lê o bytes
    return (byte)input.read();
  }

  /**
   * Lê o próximo valor em 'input' como um Date.
   * @return Date Lê o próximo valor em 'input' como um Date.
   * @throws IOException Em caso de exceção na tentativa de ler de InputStream
   *                   ou de converter para o valor desejado.
   */
  public java.util.Date readDate() throws IOException {
    // lê o Date
    return new java.util.Date(readLong());
  }

  /**
   * Lê o próximo valor em 'input' como um double.
   * @return byte Lê o próximo valor em 'input' como um double.
   * @throws IOException Em caso de exceção na tentativa de ler de InputStream
   *                   ou de converter para o valor desejado.
   */
  public double readDouble() throws IOException {
    // lê o double
    return Double.longBitsToDouble(readLong());
  }

  /**
   * Lê o próximo valor em 'input' como um int.
   * @return int Lê o próximo valor em 'input' como um int.
   * @throws IOException Em caso de exceção na tentativa de ler de InputStream
   *                   ou de converter para o valor desejado.
   */
  public int readInt() throws IOException {
    // espera por 4 bytes
    waitForBytes(4);
    // lê os 4 bytes que formam o int
    return (input.read() << 24)
         | (input.read() << 16)
         | (input.read() << 8)
         | (input.read() << 0);
  }

  /**
   * Lê o próximo valor em 'input' como um long.
   * @return long Lê o próximo valor em 'input' como um long.
   * @throws IOException Em caso de exceção na tentativa de ler de InputStream
   *                   ou de converter para o valor desejado.
   */
  public long readLong() throws IOException {
    // lê o long na forma Hexadecimal
    return Long.parseLong(readString(), Character.MAX_RADIX);
  }

  /**
   * Lê o próximo valor em 'input' como um Object.
   * @return Object Lê o próximo valor em 'input' como um Object.
   * @throws IOException Em caso de exceção na tentativa de ler o valor
   *                     informado ou de sua classe não ser conhecida.
   */
  public Object readObject() throws IOException {
    // lê o índice da classe
    byte dataType = readByte();
    // lê o objeto
    switch (dataType) {
      case SerializeWriter.DATA_TYPE_SERIALIZABLE: {
        try {
          String className = readString(); // nome da classe
          Object result = Class.forName(className).newInstance();
          ((Serializable)result).deserialize(this);
          return result;
        }
        catch (Exception e) {
          throw new IOException(e.getMessage());
        } // try-catch
      }
      case SerializeWriter.DATA_TYPE_ARRAY      : return readArray();
      case SerializeWriter.DATA_TYPE_BIG_DECIMAL: return new BigDecimal(readDouble());
      case SerializeWriter.DATA_TYPE_BIG_INTEGER: return new BigInteger(readInt() + "");
      case SerializeWriter.DATA_TYPE_BOOLEAN    : return new Boolean(readBoolean());
      case SerializeWriter.DATA_TYPE_BYTE       : return new Byte(readByte());
      case SerializeWriter.DATA_TYPE_DATE       : return readDate();
      case SerializeWriter.DATA_TYPE_DOUBLE     : return new Double(readDouble());
      case SerializeWriter.DATA_TYPE_INTEGER    : return new Integer(readInt());
      case SerializeWriter.DATA_TYPE_SHORT      : return new Short(readShort());
      case SerializeWriter.DATA_TYPE_STRING     : return readString();
      case SerializeWriter.DATA_TYPE_TIMESTAMP  : return readTimestamp();
    } // switch
    // se chegou até aqui...não conhecemos o tipo
    throw new IOException(getClass().getName() + ".readObject(): tipo do objeto desconhecido: " + dataType + ".");
  }

  /**
   * Lê o próximo valor em 'input' como um short.
   * @return short Lê o próximo valor em 'input' como um short.
   * @throws IOException Em caso de exceção na tentativa de ler de InputStream
   *                   ou de converter para o valor desejado.
   */
  public short readShort() throws IOException {
    // espera por 2 bytes
    waitForBytes(2);
    // lê os 2 bytes que formam o short
    return (short)((input.read() << 8)
                 | (input.read() << 0));
  }

  /**
   * Lê o próximo valor em 'input' como um String.
   * @return String Lê o próximo valor em 'input' como um String.
   * @throws IOException Em caso de exceção na tentativa de ler de InputStream
   *                   ou de converter para o valor desejado.
   */
  public String readString() throws IOException {
    // tamanho da String
    int length = readInt();
    // espera por 'length' bytes
    waitForBytes(length);
    // lê seus bytes
    byte[] result = new byte[length];
    input.read(result);
    // retorna
    return new String(result);
  }

  /**
   * Lê o próximo valor em 'input' como um Timestamp.
   * @return Timestamp Lê o próximo valor em 'input' como um Timestamp.
   * @throws IOException Em caso de exceção na tentativa de ler de InputStream
   *                   ou de converter para o valor desejado.
   */
  public Timestamp readTimestamp() throws IOException {
    // lê o Timestamp
    return new Timestamp(readLong());
  }

  /**
   * Este método foi criado para evitar o "bug" do recebimento dos dados através
   * das conexões HTTP. Aparentemente os dados são recebidos em uma Thread com
   * baixa prioridade. Quando o InputStream é lido muito rapidamente, os dados
   * não chegam a tempo. Desta forma alguns métodos desta classe se utilizam
   * desse método que verifica há dados suficientes para serem lidos e caso
   * contrário espera até 3 segundos para sua chegada.
   * @param bytes int Quantidade de bytes que se deseja ler.
   */
  private void waitForBytes(int bytes) {
    synchronized (this) {
      try {
        int timeout = 30;
        while ((timeout > 0) && (input.available() < bytes)) {
          wait(100);
          timeout--;
        } // while
      }
      catch (Exception e) {
        throw new RuntimeException(e);
      } // try-catch
    } // synchronized
  }

}
