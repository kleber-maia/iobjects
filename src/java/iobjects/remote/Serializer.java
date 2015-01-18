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
import java.lang.reflect.*;

import iobjects.*;
import iobjects.util.StringTools;

/**
 * Classe responsável pela serialização e deserialização em um Stream de um
 * objeto ou array que implemente a interface Serializable .
 */
public class Serializer {

  static public final int DATA_TYPE_UNKNOWN      = -1;
  static public final int DATA_TYPE_SERIALIZABLE = 0;
  static public final int DATA_TYPE_PRIMITIVE    = 1;
  static public final int DATA_TYPE_NULL         = 2;

  static public void main(String[] args) {
    try {
      /*
      ByteArrayOutputStream out = new ByteArrayOutputStream();
      Serializer.serialize(new String[]{"Item 1", "Item 2", "Item 3"}, out);

      ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
      String[] arr = (String[])Serializer.deserialize(in);
       * 
      System.out.println(StringTools.arrayStringToString(arr, ", "));
      */
      
      /*
      // serializa
      ByteArrayOutputStream out = new ByteArrayOutputStream();
      SerializeWriter serializeWriter = new SerializeWriter(out);
      serializeWriter.writeObject(new String[]{"Item 1", "Item 2", "Item 3"});
      // deserializa
      ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
      SerializeReader serializeReader = new SerializeReader(in);
      String[] arr = (String[])serializeReader.readObject();
      // imprime na tela
      System.out.println(StringTools.arrayStringToString(arr, ", "));
      */
      
      RemoteMethod rm = new RemoteMethod("imanager", "Suporte", "@iManager", "i", new String[]{"1"}, new String[]{"Comteq"}, "saidaProduto", "selectByFilter", new Object[]{1, new String[]{"1", "2", "3"}});

      ByteArrayOutputStream out = new ByteArrayOutputStream();
      Serializer.serialize(rm, out);
      
      ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
      RemoteMethod rm2 = (RemoteMethod)Serializer.deserialize(in);
      
      System.out.println(StringTools.arrayStringToString((String[])rm2.getParameterValues()[1], ", "));
      
    }
    catch (Exception e) {
      System.out.println(e);
    }
  }

  /**
   * Construtor padrão.
   */
  private Serializer() {
  }

  /**
   * Retorna a instância do objeto ou array serializado através do método
   * 'serialize()'. Se for um array será retornado um Object[] contendo os
   * objetos que implementem a interface Serializable.
   * <b>A classe que implementar esta interface deve possuir um construtor
   * 'default' (sem parâmetros).</b>
   * @param input InputStream.
   * @return Retorna a instância do objeto ou array serializado através do método
   *         'serialize()'. Se for um array será retornado um Object[] contendo os
   *         objetos que implementem a interface Serializable.
   * @throws IOException Em caso de exceção na tentativa de instanciar o objeto,
   *                     o array ou ler de InputStream.
   * @throws ClassNotFoundException Em caso de exceção na tentativa de instanciar
   *                                o objeto.
   * @throws IllegalAccessException Em caso de exceção na tentativa de instanciar
   *                                o objeto.
   * @throws InstantiationException Em caso de exceção na tentativa de instanciar
   *                                o objeto.
   * @throws Exception
   */
  static public Object deserialize(InputStream input) throws IOException, ClassNotFoundException, IllegalAccessException, InstantiationException, Exception {
    // nossa instância de SerializeReader
    SerializeReader serializeReader = new SerializeReader(input);
    // lê o tipo de dado que foi gravado
    byte dataType = serializeReader.readByte();

    // se é null...
    if (dataType == DATA_TYPE_NULL) {
      // retorna um objeto vazio
      return null;
    }
    // se é um tipo serializável...
    else if (dataType == DATA_TYPE_SERIALIZABLE) {
      // lê o nome da classe serializada
      String className = serializeReader.readString();
      // instancia o objeto de retorno
      Object result = Class.forName(className).newInstance();
      // delega ao objeto a responsabilidade de ler suas propriedades
      if (result instanceof Serializable)
        ((Serializable)result).deserialize(serializeReader);
      else
        throw new IOException(result.getClass().getName() + " não implementa a interface " + Serializable.class.getName() + ".");
      // retorna
      return result;
    }
    // se é um tipo primitivo...
    else if (dataType == DATA_TYPE_PRIMITIVE) {
      // pede para que SerializeOutputStream faça o trabalho
      return serializeReader.readObject();
    }
    // se não é um tipo conhecido...exceção
    else
      throw new IOException("Serializer.deserialize(): tipo desconhecido: " + dataType + ".");
  }

  /**
   * Escreve 'object' em 'output'.
   * @param value Object que será serializado. Deve ser um objeto que implemente
   *        a interface Serializable, um array de objetos que implementem a mesma
   *        interface ou um dos tipos de dados suportados pela classe
   *        SerializeOuputStream.
   * @param output OutputStream.
   * @throws IOException Em caso de exceção na tentativa de obter de escrever
   *                     em OutputStream.
   */
  static public void serialize(Object       value,
                               OutputStream output) throws IOException {
    // verifica o tipo de dado recebido
    int dataType;
    if (value == null)
      dataType = DATA_TYPE_NULL;
    else if (value instanceof Serializable)
      dataType = DATA_TYPE_SERIALIZABLE;
    else if (SerializeWriter.getDataType(value) != SerializeWriter.DATA_TYPE_UNKNOWN)
      dataType = DATA_TYPE_PRIMITIVE;
    else
      dataType = DATA_TYPE_UNKNOWN;
    // se não conhecemos o tipo de dado...exceção
    if (dataType == DATA_TYPE_UNKNOWN)
      throw new IOException("Serializer.serialize(): tipo não suportado: " + value.getClass().getName() + ".");

    // nossa instância de SerializeWriter
    SerializeWriter serializeWriter = new SerializeWriter(output);
    // escreve o tipo de dado
    serializeWriter.writeByte((byte)dataType);
    // se é um tipo serializável...
    if (dataType == DATA_TYPE_SERIALIZABLE) {
      // escreve o nome da classe
      serializeWriter.writeString(value.getClass().getName());
      // delega ao objeto a responsabilidade de escrever suas propriedades
      if (value instanceof Serializable)
        ((Serializable)value).serialize(serializeWriter);
      else
        throw new IOException(value.getClass().getName() + " não implementa a interface " + Serializable.class.getName() + ".");
    }
    // se é um tipo primitivo...
    else if (dataType == DATA_TYPE_PRIMITIVE) {
      // pede para que SerializeOutputStream faça o trabalho
      serializeWriter.writeObject(value);
    }
    // se é null...
    else if (dataType == DATA_TYPE_NULL) {
      // não precisa escrever nada
    } // if
  }

}
