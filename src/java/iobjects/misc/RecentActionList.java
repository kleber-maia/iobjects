package iobjects.misc;

import java.util.*;
import javax.servlet.http.*;

import iobjects.servlet.*;
import iobjects.*;

/**
 * Representa uma lista de a��es recentes executadas pelo usu�rio.
 */

public class RecentActionList {

  private int    maxSize = 10;
  private Vector vector  = new Vector();

  /**
   * Construtor padr�o.
   */
  public RecentActionList() {
  }

  /**
   * Construtor estendido.
   * @param maxSize int Tamanho m�ximo da lista.
   */
  public RecentActionList(int maxSize) {
    this.maxSize = maxSize;
  }

  /**
   * Adiciona a a��o recente � lista.
   * @param recentAction RecentAction A��o recente para ser adicionada.
   */
  public void add(RecentAction recentAction) {
    // procura por um item recente de mesma a��o
    int index = indexOf(recentAction);
    // se existe...apaga
    if (index >=0)
      delete(index);
    // adiciona no come�o da lista
    vector.add(0, recentAction);
    // se passamos do tamanho m�ximo...apaga o �ltimo
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
   * Exclui o RecentAction na posi��o indicado por 'index'.
   * @param index int Exclui o RecentAction na posi��o indicado por 'index'.
   */
  public void delete(int index) {
    vector.remove(index);
  }

  /**
   * Retorna o RecentAction na posi��o indicada por 'index'.
   * @param index int �ndice do RecentAction que se deseja retornar.
   * @return RecentAction Retorna o �ndice na lista de 'recentAction'.
   */
  public RecentAction get(int index) {
    return (RecentAction)vector.elementAt(index);
  }

  /**
   * Retorna o �ndice na lista de 'recentAction'.
   * @param recentAction RecentAction que se deseja localizar.
   * @return int Retorna o �ndice na lista de 'recentAction'.
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
