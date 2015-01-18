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
 * Representa uma exceção remota.
 */
public class RemoteException extends Exception implements Serializable {

  String causeClassName = "";
  String causeMessage   = "";

  /**
   * Construtor estendido: não deve ser chamado pela aplicação.
   */
  public RemoteException() {
  }

  /**
   * Construtor padrão.
   * @param cause Throwable Causa da exceção.
   */
  public RemoteException(Throwable cause) {
    super(cause);
    // *
    this.causeClassName = cause.getClass().getName();
    this.causeMessage = cause.getMessage();
  }

  public void deserialize(SerializeReader reader) throws IOException {
    // lê o nome da classe
    causeClassName = reader.readString();
    // lê a mensagem
    causeMessage = reader.readString();
  }

  /**
   * Retorna o nome da classe da causa da exceção.
   * @return String Retorna o nome da classe da causa da exceção.
   */
  public String getCauseClassName() {
    return causeClassName;
  }

  /**
   * Retorna a mensagem da causa da exceção.
   * @return String Retorna a mensagem da causa da exceção.
   */
  public String getCauseMessage() {
    return causeMessage;
  }

  public void serialize(SerializeWriter writer) throws IOException {
    // escreve o nome da classe
    writer.writeString(causeClassName);
    // escreve a mensagem
    writer.writeString(causeMessage);
  }

}
