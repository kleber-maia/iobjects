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

import java.util.*;

/**
 * Representa um pool de objetos reaproveitáveis. Cada objeto pode possuir
 * várias instâncias identificadas pelo mesmo nome.
 */
public class Pool {

  private Hashtable hashtable = new Hashtable();

  /**
   * Construtor padrão.
   */
  public Pool() {
  }

  /**
   * Libera recursos.
   */
  public void finalize() {
    hashtable.clear();
    hashtable = null;
  }

  /**
   * Obtém uma instância do objeto representado por 'name'. Se não houver uma
   * instância disponível retorna null.
   * @param name String Nome do objeto cuja instância se deseja obter.
   * @return Object Obtém uma instância do objeto representado por 'name'. Se
   *         não houver uma instância disponível retorna null.
   */
  public synchronized Object getObject(String name) {
    // obtém a lista de instâncias do objeto identificado
    Vector vector = (Vector)hashtable.get(name);
    // se ainda não temos a lista...também não temos objetos
    if (vector == null)
      return null;
    // se temos um objeto disponível...retorna-o
    if (vector.size() > 0) {
      Object result = vector.get(0);
      vector.remove(0);
      return result;
    }
    // se não temos um objeto disponível...
    else
      return null;
  }

  /**
   * Devolve 'instance' para a lista de objetos reaproveitáveis.
   * @param name String Nome do objeto cuja instância está sendo devolvida.
   * @param instance Object Instância do objeto que está sendo devolvido.
   */
  public synchronized void returnObject(String name, Object instance) {
    // obtém a lista de instâncias do objeto identificado
    Vector vector = (Vector)hashtable.get(name);
    // se ainda não temos a lista...
    if (vector == null) {
      // cria a lista
      vector = new Vector();
      // adiciona no pool
      hashtable.put(name, vector);
    } // if
    // devolve o objeto para a lista
    vector.add(instance);
  }

}
