package iobjects.sql;

import java.sql.*;
import java.util.*;

import iobjects.*;

/**
 * Representa uma lista itens de conexão com o banco de dados.
 */
public class ConnectionItems implements Comparator {

  private int     transactionCount  = 0;
  private TreeSet items             = new TreeSet(this);

  /**
   * Construtor padrão.
   */
  public ConnectionItems() {
  }

  /**
   * Adiciona o ConnectionItem especificado à lista.
   * @param connectionItem ConnectionItem para adicionar à lista.
   * @throws Exception Em caso de exceção na tentativa de iniciar a transação.
   */
  public void add(ConnectionItem connectionItem) throws Exception {
    // estamos em transação?
    connectionItem.connection().setAutoCommit(transactionCount == 0);
    // adiciona
    items.add(connectionItem);
  }

  /**
   * Incrementa a quantidade de transações e inicia uma transação
   * nas conexões utilizadas para acesso ao banco de dados.
   * @throws Exception Em caso de exceção na tentativa de iniciar a transação.
   */
  public void beginTransaction() throws Exception {
    // incrementa o número de transações iniciadas
    transactionCount++;
    // se já havia iniciado a transação nas conexões...dispara
    if (transactionCount > 1)
      return;
    // loop nas conexões
    for (int i=0; i<size(); i++) {
      // inicia a transação
      get(i).connection().setAutoCommit(false);
    } // for
  }

  /**
   * Remove todos os Connection´s da lista.
   * @throws Exception Em caso de haver uma transação iniciada.
   */
  public void clear() throws Exception {
    // remove tudo
    while (size() > 0)
      remove(0);
  }

  /**
   * Decrementa a quantidade de transações e caso tenha chegado a 0 (zero)
   * aplica as alterações nas conexões utilizadas para acesso ao banco de dados.
   * @throws Exception Em caso de exceção na tentativa de aplicar as alterações
   *         no banco de dados.
   */
  public void commitTransaction() throws Exception {
    // se não temos transações iniciadas...exceção
    if (transactionCount == 0)
      throw new ExtendedException(getClass().getName(), "commitTransaction", "Não existem transações iniciadas.");
    // decrementa o número de transações iniciadas
    transactionCount--;
    // se ainda não chegamos a 0...dispara
    if (transactionCount > 0)
      return;
    // loop nas conexões
    for (int i=0; i<size(); i++) {
      // conexão da vez
      ConnectionItem connectionItem = get(i);
      // aplica as alterações
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
   * Retorna o ConnectionItem na posição indicada por index.
   * @param index Índice do ConnectionItem que se dejesa retornar.
   * @return Retorna o ConnectionItem na posição indicada por index.
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
   *             maiúsculas e minúsculas.
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
   * Remove o ConnectionItem na posição indicada por index.
   * @param index Posição do ConnectionItem para remover.
   * @throws Exception Em caso de haver uma transação iniciada.
   */
  public void remove(int index) throws Exception {
    // se temos transações iniciadas...exceção
    if (transactionCount > 0)
      throw new ExtendedException(getClass().getName(), "remove", "Existem transações iniciadas.");
    // remove
    items.remove(get(index));
  }

  /**
   * Define a quantidade de transações como 0 (zero) e desfaz as alterações
   * realizadas nas conexões utilizadas para acesso ao banco de dados.
   * @throws Exception Em caso de exceção na tentativa de desfazer as alterações
   *         no banco de dados.
   */
  public void rollbackTransaction() throws Exception {
    // loop nas conexões
    for (int i=0; i<size(); i++) {
      // conexão da vez
      ConnectionItem connectionItem = get(i);
      // desfaz as alterações
      connectionItem.connection().rollback();
      connectionItem.connection().setAutoCommit(true);
    } // for
    // não temos mais transações em andamento
    transactionCount = 0;
  }

  /**
   * Retorna a quantidade de ConnectionItem´s existentes.
   * @return Retorna a quantidade de ConnectionItem´s existentes.
   */
  public int size() {
    return items.size();
  }

  /**
   * Retorna a quantidade de transações iniciadas.
   * @return int Retorna a quantidade de transações iniciadas.
   */
  public int transactionCount() {
    return transactionCount;
  }

  public int compare(Object o1, Object o2) {
    // itens para comparar
    ConnectionItem connectionItem1 = (ConnectionItem)o1;
    ConnectionItem connectionItem2 = (ConnectionItem)o2;
    // retorna a comparação dos nomes
    return connectionItem1.name().compareTo(connectionItem2.name());
  }

  public boolean equals(Object obj) {
    return false;
  }

}
