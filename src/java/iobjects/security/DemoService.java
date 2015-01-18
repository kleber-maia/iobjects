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
package iobjects.security;

import iobjects.*;

/**
 * Representa o serviço de demonstração da aplicação.
 * <b>Esta interface deve ser implementada por apenas uma classe descendente
 * de BusinessObject na aplicação.</b>
 */
public interface DemoService {

  /**
   * Requisita o acesso a demonstração para o visitante identificado por 'name' 
   * e 'email' e retorna a mensagem que será apresentada ao usuário.
   * @param name String Nome do visitante.
   * @param email String Email do visitante.
   * @return 
   * @throws Exception Em caso de exceção na tentativa de realizar a operação.
   */
  public String requestDemo(String name,
                            String email) throws Exception;

}
