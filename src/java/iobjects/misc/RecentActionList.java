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
