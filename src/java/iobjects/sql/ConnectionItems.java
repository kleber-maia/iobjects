package iobjects.sql;

import java.sql.*;
import java.util.*;

import iobjects.*;

/**
 * Representa uma lista itens de conex�o com o banco de dados.
 */
public class ConnectionItems implements Comparator {

  private int     transactionCount  = 0;
  private TreeSet items             = new TreeSet(this);

  /**
   * Construtor padr�o.
   */
  public ConnectionItems() {
  }

  /**
   * Adiciona o ConnectionItem especificado � lista.
   * @param connectionItem ConnectionItem para adicionar � lista.
   * @throws Exception Em caso de exce��o na tentativa de iniciar a transa��o.
   */
  public void add(ConnectionItem connectionItem) throws Exception {
    // estamos em transa��o?
    connectionItem.connection().setAutoCommit(transactionCount == 0);
    // adiciona
    items.add(connectionItem);
  }

  /**
   * Incrementa a quantidade de transa��es e inicia uma transa��o
   * nas conex�es utilizadas para acesso ao banco de dados.
   * @throws Exception Em caso de exce��o na tentativa de iniciar a transa��o.
   */
  public void beginTransaction() throws Exception {
    // incrementa o n�mero de transa��es iniciadas
    transactionCount++;
    // se j� havia iniciado a transa��o nas conex�es...dispara
    if (transactionCount > 1)
      return;
    // loop nas conex�es
    for (int i=0; i<size(); i++) {
      // inicia a transa��o
      get(i).connection().setAutoCommit(false);
    } // for
  }

  /**
   * Remove todos os Connection�s da lista.
   * @throws Exception Em caso de haver uma transa��o iniciada.
   */
  public void clear() throws Exception {
    // remove tudo
    while (size() > 0)
      remove(0);
  }

  /**
   * Decrementa a quantidade de transa��es e caso tenha chegado a 0 (zero)
   * aplica as altera��es nas conex�es utilizadas para acesso ao banco de dados.
   * @throws Exception Em caso de exce��o na tentativa de aplicar as altera��es
   *         no banco de dados.
   */
  public void commitTransaction() throws Exception {
    // se n�o temos transa��es iniciadas...exce��o
    if (transactionCount == 0)
      throw new ExtendedException(getClass().getName(), "commitTransaction", "N�o existem transa��es iniciadas.");
    // decrementa o n�mero de transa��es iniciadas
    transactionCount--;
    // se ainda n�o chegamos a 0...dispara
    if (transactionCount > 0)
      return;
    // loop nas conex�es
    for (int i=0; i<size(); i++) {
      // conex�o da vez
      ConnectionItem connectionItem = get(i);
      // aplica as altera��es
      connectionItem.connection().commit();
      connectionItem.connection().setAutoCommit(true);
    } // for
  }

  public void finalize() {
    try {
      clear();
    }
    catch (Exception e) {
    } // try-catch
  }

  /**
   * Retorna o ConnectionItem na posi��o indicada por index.
   * @param index �ndice do ConnectionItem que se dejesa retornar.
   * @return Retorna o ConnectionItem na posi��o indicada por index.
   */
  public ConnectionItem get(int index) {
    Iterator iterator = items.iterator();
    for (int i=0; i<index; i++)
      iterator.next();
    if (iterator.hasNext())
      return (ConnectionItem)iterator.next();
    else
      return null;
  }

  /**
   * Retorna o ConnectionItem com o 'name' especificado.
   * @param name Nome do ConnectionItem que se deseja localizar, desprezando
   *             mai�sculas e min�sculas.
   * @return Retorna o ConnectionItem com o 'name' especificado.
   */
  public ConnectionItem get(String name) {
    Iterator iterator = items.iterator();
    while (iterator.hasNext()) {
      ConnectionItem result = (ConnectionItem) iterator.next();
      if (result.name().equalsIgnoreCase(name))
        return result;
    } // while
    return null;
  }

  /**
   * Remove o ConnectionItem na posi��o indicada por index.
   * @param index Posi��o do ConnectionItem para remover.
   * @throws Exception Em caso de haver uma transa��o iniciada.
   */
  public void remove(int index) throws Exception {
    // se temos transa��es iniciadas...exce��o
    if (transactionCount > 0)
      throw new ExtendedException(getClass().getName(), "remove", "Existem transa��es iniciadas.");
    // remove
    items.remove(get(index));
  }

  /**
   * Define a quantidade de transa��es como 0 (zero) e desfaz as altera��es
   * realizadas nas conex�es utilizadas para acesso ao banco de dados.
   * @throws Exception Em caso de exce��o na tentativa de desfazer as altera��es
   *         no banco de dados.
   */
  public void rollbackTransaction() throws Exception {
    // loop nas conex�es
    for (int i=0; i<size(); i++) {
      // conex�o da vez
      ConnectionItem connectionItem = get(i);
      // desfaz as altera��es
      connectionItem.connection().rollback();
      connectionItem.connection().setAutoCommit(true);
    } // for
    // n�o temos mais transa��es em andamento
    transactionCount = 0;
  }

  /**
   * Retorna a quantidade de ConnectionItem�s existentes.
   * @return Retorna a quantidade de ConnectionItem�s existentes.
   */
  public int size() {
    return items.size();
  }

  /**
   * Retorna a quantidade de transa��es iniciadas.
   * @return int Retorna a quantidade de transa��es iniciadas.
   */
  public int transactionCount() {
    return transactionCount;
  }

  public int compare(Object o1, Object o2) {
    // itens para comparar
    ConnectionItem connectionItem1 = (ConnectionItem)o1;
    ConnectionItem connectionItem2 = (ConnectionItem)o2;
    // retorna a compara��o dos nomes
    return connectionItem1.name().compareTo(connectionItem2.name());
  }

  public boolean equals(Object obj) {
    return false;
  }

}
