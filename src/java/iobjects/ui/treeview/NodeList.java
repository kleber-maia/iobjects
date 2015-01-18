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
package iobjects.ui.treeview;

import java.util.*;

/**
 * Representa uma lista de n�s do TreeView.
 */
public class NodeList {

  private Vector vector = new Vector();

  /**
   * Construtor padr�o.
   */
  public NodeList() {
  }

  /**
   * Adiciona um n� na lista.
   * @param node Node N� para ser adicionado.
   */
  public void add(Node node) {
    // adiciona na lista
    vector.add(node);
  }

  /**
   * Adiciona um n� na lista.
   * @param text String Texto do n� para ser adicionado.
   * @return Node N� adicionado.
   */
  public Node add(String text) {
    Node result = new Node(text);
    add(result);
    return result;
  }

  /**
   * Adiciona um n� na lista.
   * @param text String Texto do n� para ser adicionado.
   * @param href String Refer�ncia.
   * @return Node N� adicionado.
   */
  public Node add(String text,
                  String href) {
    Node result = new Node(text, href);
    add(result);
    return result;
  }

  /**
   * Adiciona um n� na lista.
   * @param text String Texto do n� para ser adicionado.
   * @param href String Refer�ncia.
   * @param toolTip String Informa��es adicionais.
   * @return Node N� adicionado.
   */
  public Node add(String text,
                  String href,
                  String toolTip) {
    Node result = new Node(text, href, toolTip);
    add(result);
    return result;
  }

  /**
   * Limpa a lista atual.
   */
  public void clear() {
    vector.clear();
  }

  /**
   * Retorna o n� referente ao �ndice indicado.
   * @param index int �ndice do n� que se deseja retornar.
   * @return Node Retorna o n� referente ao �ndice indicado.
   */
  public Node item(int index) {
    return (Node)vector.get(index);
  }

  /**
   * Retorna o n� referente ao texto informado.
   * @param text String Texto do n� que se deseja retornar.
   * @return Node Retorna o n� referente ao texto informado.
   */
  public Node item(String text) {
    Node result = null;
    for (int i=0; i<vector.size(); i++) {
      result = item(i);
      if (result.text.equalsIgnoreCase(text))
        return result;
    } // for
    return null;
  }

  /**
   * Retorna a quantidade de n�s existentes.
   * @return int Retorna a quantidade de n�s existentes.
   */
  public int length() {
    return vector.size();
  }

}
