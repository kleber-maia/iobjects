package iobjects.misc;

import java.util.*;
import javax.servlet.http.*;

import iobjects.servlet.*;
import iobjects.*;

/**
 * Representa uma lista de ações recentes executadas pelo usuário.
 */

public class RecentActionList {

  private int    maxSize = 10;
  private Vector vector  = new Vector();

  /**
   * Construtor padrão.
   */
  public RecentActionList() {
  }

  /**
   * Construtor estendido.
   * @param maxSize int Tamanho máximo da lista.
   */
  public RecentActionList(int maxSize) {
    this.maxSize = maxSize;
  }

  /**
   * Adiciona a ação recente à lista.
   * @param recentAction RecentAction Ação recente para ser adicionada.
   */
  public void add(RecentAction recentAction) {
    // procura por um item recente de mesma ação
    int index = indexOf(recentAction);
    // se existe...apaga
    if (index >=0)
      delete(index);
    // adiciona no começo da lista
    vector.add(0, recentAction);
    // se passamos do tamanho máximo...apaga o último
    if (size() > maxSize)
      vector.remove(vector.size()-1);
  }

  /**
   * Apaga todos os itens da lista.
   */
  public void clear() {
    vector.clear();
  }

  /**
   * Exclui o RecentAction na posição indicado por 'index'.
   * @param index int Exclui o RecentAction na posição indicado por 'index'.
   */
  public void delete(int index) {
    vector.remove(index);
  }

  /**
   * Retorna o RecentAction na posição indicada por 'index'.
   * @param index int Índice do RecentAction que se deseja retornar.
   * @return RecentAction Retorna o índice na lista de 'recentAction'.
   */
  public RecentAction get(int index) {
    return (RecentAction)vector.elementAt(index);
  }

  /**
   * Retorna o índice na lista de 'recentAction'.
   * @param recentAction RecentAction que se deseja localizar.
   * @return int Retorna o índice na lista de 'recentAction'.
   */
  public int indexOf(RecentAction recentAction) {
    for (int i=0; i<size(); i++) {
      RecentAction thisAction = get(i);
      if (thisAction.equals(recentAction))
        return i;
    } // for
    return -1;
  }

  /**
   * Retorna o tamanho da lista.
   * @return int Retorna o tamanho da lista.
   */
  public int size() {
    return vector.size();
  }

}
