package iobjects.ui.treeview;

import java.util.*;

/**
 * Representa uma lista de nós do TreeView.
 */
public class NodeList {

  private Vector vector = new Vector();

  /**
   * Construtor padrão.
   */
  public NodeList() {
  }

  /**
   * Adiciona um nó na lista.
   * @param node Node Nó para ser adicionado.
   */
  public void add(Node node) {
    // adiciona na lista
    vector.add(node);
  }

  /**
   * Adiciona um nó na lista.
   * @param text String Texto do nó para ser adicionado.
   * @return Node Nó adicionado.
   */
  public Node add(String text) {
    Node result = new Node(text);
    add(result);
    return result;
  }

  /**
   * Adiciona um nó na lista.
   * @param text String Texto do nó para ser adicionado.
   * @param href String Referência.
   * @return Node Nó adicionado.
   */
  public Node add(String text,
                  String href) {
    Node result = new Node(text, href);
    add(result);
    return result;
  }

  /**
   * Adiciona um nó na lista.
   * @param text String Texto do nó para ser adicionado.
   * @param href String Referência.
   * @param toolTip String Informações adicionais.
   * @return Node Nó adicionado.
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
   * Retorna o nó referente ao índice indicado.
   * @param index int Índice do nó que se deseja retornar.
   * @return Node Retorna o nó referente ao índice indicado.
   */
  public Node item(int index) {
    return (Node)vector.get(index);
  }

  /**
   * Retorna o nó referente ao texto informado.
   * @param text String Texto do nó que se deseja retornar.
   * @return Node Retorna o nó referente ao texto informado.
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
   * Retorna a quantidade de nós existentes.
   * @return int Retorna a quantidade de nós existentes.
   */
  public int length() {
    return vector.size();
  }

}
