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
package iobjects.util.mail;

/**
 * Interface que deve ser implementada pelas classes que terão a capacidade
 * de receber mensagens através do serviço de e-mail da aplicação.
 */
public interface Pop3Callback {

  /**
   * Evento chamado sempre que uma mensagem for recebida.
   * @param message Message recebida.
   * @return boolean <b>True para que a mensagem seja apagada definitivamente do
   *                 servidor. Essa é a ação normal a ser tomada após cada
   *                 mensagem ter sido processada com sucesso.</b>
   * @throws Exception Em caso de exceção na tentativa de processamento da mensagem.
   */
  public boolean onReceiveMessage(Message message) throws Exception;

}
