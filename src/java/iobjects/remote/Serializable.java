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

/**
 * Interface que deve ser implementada pelas classes que terão a capacidade
 * de ser serializadas e deserializadas.
 * <b>A classe que implementar esta interface deve possuir um construtor
 * 'default' (sem parâmetros).</b>
 */
public interface Serializable {

  /**
   * Implementa a deserialização da classe que implementa esta interface.
   * @param reader SerializeReader para leitura das propriedades gravadas
   *              a partir do método serialize().
   * @exception IOException Em caso de exceção na tentativa de ler de 'input'.
   */
  public void deserialize(SerializeReader reader) throws IOException;

  /**
   * Implementa a serialização da classe que implementa esta interface.
   * @param writer SerializeWriter para escrita das propriedades para serem
   *               posteriormente lidas a partir do método deserialize().
   * @exception IOException Em caso de exceção na tentativa de escrever em 'output'.
   */
  public void serialize(SerializeWriter writer) throws IOException;

}
