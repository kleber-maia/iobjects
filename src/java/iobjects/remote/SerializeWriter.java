package iobjects.remote;

import java.io.*;
import java.math.*;
import java.util.*;
import java.sql.*;

/**
 * Representa um OutputStream para serialização de objetos que implementam
 * a classe Serializable.
 */
public class SerializeWriter {

  static public final Class[] DATA_TYPE_LIST = {BigDecimal.class,
                                                BigInteger.class,
                                                Boolean.class,
                                                Byte.class,
                                                java.util.Date.class,
                                                Double.class,
                                                Integer.class,
                                                Long.class,
                                                Short.class,
                                                String.class,
                                                Timestamp.class};

  static public final byte DATA_TYPE_UNKNOWN      = -2;
  static public final byte DATA_TYPE_SERIALIZABLE = -1;
  static public final byte DATA_TYPE_BIG_DECIMAL  = 0;
  static public final byte DATA_TYPE_BIG_INTEGER  = 1;
  static public final byte DATA_TYPE_BOOLEAN      = 2;
  static public final byte DATA_TYPE_BYTE         = 3;
  static public final byte DATA_TYPE_DATE         = 4;
  static public final byte DATA_TYPE_DOUBLE       = 5;
  static public final byte DATA_TYPE_INTEGER      = 6;
  static public final byte DATA_TYPE_LONG         = 7;
  static public final byte DATA_TYPE_SHORT        = 8;
  static public final byte DATA_TYPE_STRING       = 9;
  static public final byte DATA_TYPE_TIMESTAMP    = 10;
  static public final byte DATA_TYPE_ARRAY        = 11;

  private OutputStream output = null;

  /**
   * Construtor padrão.
   * @param output OutputStream que será utilizado como saída para a serialização.
   */
  public SerializeWriter(OutputStream output) {
    this.output = output;
  }

  /**
   * Retorna o tipo de dado de 'value' ou DATA_TYPE_UNKNOWN.
   * @param value Object Objeto cujo tipo de dados se deseja obter.
   * @return byte Retorna o tipo de dado de 'value' ou DATA_TYPE_UNKNOWN.
   */
  static public byte getDataType(Object value) {
    // se implementa a interface Serializable...
    if (value instanceof Serializable)
      return DATA_TYPE_SERIALIZABLE;
    // se é um array...
    else if (value.getClass().isArray())
      return DATA_TYPE_ARRAY;
    // loop na lista de tipos de dados
    for (byte i=0; i<DATA_TYPE_LIST.length; i++) {
      // se é a classe procurada...
      if (value.getClass() == DATA_TYPE_LIST[i])
        // retorna
        return i;
    } // for
    // se chegou aqui...não sabemos o tipo
    return DATA_TYPE_UNKNOWN;
  }

  /**
   * Retorna o OutputStream utilizado como saída para a serialização.
   * @return OutputStream Retorna o OutputStream utilizado como saída para a
   *                      serialização.
   */
  public OutputStream getOutputStream() {
    return output;
  }
  
  /**
   * Escreve 'value' como um array.
   * @param value Object.
   * @throws IOException Em caso de exceção na tentativa de escrever o valor
   *                   informado.
   */
  public void writeArray(Object value) throws IOException {
    // se não é um array...dispara
    if (!value.getClass().isArray())
      throw new IOException(getClass().getName() + ".writeArray(): o valor passado não é um array.");
    // escreve o nome da classe
    writeString(value.getClass().getName());
    // quantidade de itens
    int length = ((Object[])value).length;
    // escreve a quantidade de itens
    writeInt(length);
    // loop nos itens
    for (int i=0; i<length; i++) {
      // item da vez
      Object item = ((Object[])value)[i];
      // escreve o item
      writeObject(item);
    } // for
  }
  
  /**
   * Escreve 'value' em 'output' como um int.
   * @param value int.
   * @throws IOException Em caso de exceção na tentativa de escrever o valor
   *                   informado.
   */
  public void writeBoolean(boolean value) throws IOException {
    // escreve como int
    output.write(value ? 1 : 0);
  }

  /**
   * Escreve 'value' em 'output' como um byte.
   * @param value byte
   * @throws IOException Em caso de exceção na tentativa de escrever o valor
   *                   informado.
   */
  public void writeByte(byte value) throws IOException {
    // escreve o byte
    output.write(value);
  }

  /**
   * Escreve 'value' em 'output' como um Date.
   * @param value Date
   * @throws IOException Em caso de exceção na tentativa de escrever o valor
   *                   informado.
   */
  public void writeDate(java.util.Date value) throws IOException {
    // escreve os bytes
    writeLong(value.getTime());
  }

  /**
   * Escreve 'value' em 'output' como um double.
   * @param value double
   * @throws IOException Em caso de exceção na tentativa de escrever o valor
   *                   informado.
   */
  public void writeDouble(double value) throws IOException {
    // escreve como um Long
    writeLong(Double.doubleToLongBits(value));
  }

  /**
   * Escreve 'value' em 'output' como um int.
   * @param value int
   * @throws IOException Em caso de exceção na tentativa de escrever o valor
   *                   informado.
   */
  public void writeInt(int value) throws IOException {
    // escreve os 4 bytes do int
    output.write(value >> 24);
    output.write(value >> 16);
    output.write(value >> 8);
    output.write(value >> 0);
  }

  /**
   * Escreve 'value' em 'output' como um long.
   * @param value long
   * @throws IOException Em caso de exceção na tentativa de escrever o valor
   *                   informado.
   */
  public void writeLong(long value) throws IOException {
    // escreve o long na forma Hexadecimal
    writeString(Long.toString(value, Character.MAX_RADIX));
  }

  /**
   * Escreve 'value' ìdentificando seu tipo. O objeto pode ser de um dos tipos
   * suportados ou implementar a interface Serializable. Neste último caso a
   * classe deve possuir um construtor padrão sem argumentos.
   * @param value Object.
   * @throws IOException Em caso de exceção na tentativa de escrever o valor
   *                   informado ou de sua classe não ser conhecida.
   */
  public void writeObject(Object value) throws IOException {
    // índice da classe do value
    byte dataType = getDataType(value);
    // se não achamos a classe do valor...exceção
    if (dataType == DATA_TYPE_UNKNOWN)
      throw new IOException(getClass().getName() + ".writeObject(): tipo não suportado: " + value.getClass().getName() + ".");
    // grava o tipo do dado
    writeByte(dataType);
    // grava o valor
    switch (dataType) {
      case DATA_TYPE_SERIALIZABLE: {
        writeString(value.getClass().getName()); // nome da classe
        ((Serializable)value).serialize(this);   // atributos da instância
        break;
      }
      case DATA_TYPE_ARRAY      : writeArray(value);                              break;
      case DATA_TYPE_BIG_DECIMAL: writeDouble(((BigDecimal)value).doubleValue()); break;
      case DATA_TYPE_BIG_INTEGER: writeInt(((BigInteger)value).intValue());       break;
      case DATA_TYPE_BOOLEAN    : writeBoolean(((Boolean)value).booleanValue());  break;
      case DATA_TYPE_BYTE       : writeByte(((Byte)value).byteValue());           break;
      case DATA_TYPE_DOUBLE     : writeDouble(((Double)value).doubleValue());     break;
      case DATA_TYPE_INTEGER    : writeInt(((Integer)value).intValue());          break;
      case DATA_TYPE_SHORT      : writeShort(((Short)value).shortValue());        break;
      case DATA_TYPE_STRING     : writeString((String)value);                     break;
      case DATA_TYPE_TIMESTAMP  : writeTimestamp((Timestamp)value);               break;
    } // switch
  }

  /**
   * Escreve 'value' em 'output' como um short.
   * @param value short
   * @throws IOException Em caso de exceção na tentativa de escrever o valor
   *                   informado.
   */
  public void writeShort(short value) throws IOException {
    // escreve os 2 bytes do int
    output.write(value >> 8);
    output.write(value >> 0);
  }

  /**
   * Escreve 'value' como uma String.
   * @param value String.
   * @throws IOException Em caso de exceção na tentativa de escrever o valor
   *                   informado.
   */
  public void writeString(String value) throws IOException {
    // escreve o tamanho da String
    writeInt(value.length());
    // escreve a String
    output.write(value.getBytes());
  }

  /**
   * Escreve 'value' em 'output' como um Timestamp.
   * @param value Timestamp
   * @throws IOException Em caso de exceção na tentativa de escrever o valor
   *                   informado.
   */
  public void writeTimestamp(Timestamp value) throws IOException {
    // escreve os bytes
    writeLong(value.getTime());
  }

}
