package iobjects.util;

import java.util.*;

/**
 * Representa um pool de objetos reaproveit�veis. Cada objeto pode possuir
 * v�rias inst�ncias identificadas pelo mesmo nome.
 */
public class Pool {

  private Hashtable hashtable = new Hashtable();

  /**
   * Construtor padr�o.
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
   * Obt�m uma inst�ncia do objeto representado por 'name'. Se n�o houver uma
   * inst�ncia dispon�vel retorna null.
   * @param name String Nome do objeto cuja inst�ncia se deseja obter.
   * @return Object Obt�m uma inst�ncia do objeto representado por 'name'. Se
   *         n�o houver uma inst�ncia dispon�vel retorna null.
   */
  public synchronized Object getObject(String name) {
    // obt�m a lista de inst�ncias do objeto identificado
    Vector vector = (Vector)hashtable.get(name);
    // se ainda n�o temos a lista...tamb�m n�o temos objetos
    if (vector == null)
      return null;
    // se temos um objeto dispon�vel...retorna-o
    if (vector.size() > 0) {
      Object result = vector.get(0);
      vector.remove(0);
      return result;
    }
    // se n�o temos um objeto dispon�vel...
    else
      return null;
  }

  /**
   * Devolve 'instance' para a lista de objetos reaproveit�veis.
   * @param name String Nome do objeto cuja inst�ncia est� sendo devolvida.
   * @param instance Object Inst�ncia do objeto que est� sendo devolvido.
   */
  public synchronized void returnObject(String name, Object instance) {
    // obt�m a lista de inst�ncias do objeto identificado
    Vector vector = (Vector)hashtable.get(name);
    // se ainda n�o temos a lista...
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
