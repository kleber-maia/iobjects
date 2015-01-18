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
package iobjects;

import java.util.*;

import iobjects.util.*;

/**
 * Representa uma lista de Action's.
 */
public class ActionList {

  class CategoryComparator implements Comparator {
    public int compare(Object o1, Object o2) {
      Action action1 = (Action)o1;
      Action action2 = (Action)o2;
      // ordena pela categoria
      int result = action1.getCategory() - action2.getCategory();
      // ordena pelo título
      if (result == 0)
        result = action1.getCaption().compareTo(action2.getCaption());
      // retorna
      return result;
    }
  }

  class CentralPointComparator implements Comparator {
    public int compare(Object o1, Object o2) {
      Action action1 = (Action)o1;
      Action action2 = (Action)o2;
      if (action1.getCentralPointIndex() == action2.getCentralPointIndex())
        return action1.getCaption().compareTo(action2.getCaption());
      else
        return action1.getCentralPointIndex() - action2.getCentralPointIndex();
    }
  }

  class ModuleComparator implements Comparator {
    public int compare(Object o1, Object o2) {
      Module module1 = (Module)o1;
      Module module2 = (Module)o2;
      return module1.getName().compareTo(module2.getName());
    }
  }

  public class Module {
    private Vector  actions                = new Vector();
    private Vector  cardActions            = new Vector();
    private Vector  centralPointActions    = new Vector();
    private String  name                   = "";
    private boolean hasJustReleasedActions = false;
    public Module (String  name,
                   boolean hasJustReleasedActions) {
      this.name = name;
      this.hasJustReleasedActions = hasJustReleasedActions;
    }
    public int     countCategory(int category) { int result = 0; for (int i=0; i<actions.size(); i++) if (((Action)actions.elementAt(i)).getCategory() == category) result++; return result; }
    public Vector  getActions()                { return actions; }
    public Vector  getCardActions()            { return cardActions; }
    public Vector  getCentralPointActions()    { return centralPointActions; }
    public boolean getHasJustReleasedActions() { return hasJustReleasedActions; }
    public String  getName()                   { return name; }
    // *
    public void    setHasJustReleasedActions(boolean hasJustReleasedActions) { this.hasJustReleasedActions = hasJustReleasedActions; }
  }

  private Vector    actions             = new Vector();
  private Vector    cardActions         = new Vector();
  private Vector    centralPointActions = new Vector();
  private Hashtable modules             = new Hashtable();
  private Module[]  moduleList          = new Module[0];

  /**
   * Construtor padrão.
   */
  public ActionList() {
  }

  /**
   * Adiciona o Action a lista e retorna-o.
   * @param action Action para ser adicionado a lista.
   * @return Action Adiciona o Action a lista e retorna-o.
   */
  public Action add(Action action) {
    // adiciona na nossa lista
    actions.add(action);

    // se o Action é visível, não é mobile e tem um módulo...
    if (action.getVisible() && !action.getMobile() && !action.getModule().equals("")) {
      // verifica se já existe seu módulo
      Module module = (Module)modules.get(action.getModule());
      // se não existe...cria-o
      if (module == null) {
        module = new Module(action.getModule(), false);
        modules.put(action.getModule(), module);
      } // if
      // adiciona o Action
      module.actions.add(action);
      // adiciona a lista de Card
      if (action.getCategory() == Action.CATEGORY_CARD) {
        cardActions.add(action);         // lista geral
        module.cardActions.add(action);  // lista do seu módulo
      } // if
      // adiciona a lista de Central Points
      if (action.getCentralPoint()) {
        centralPointActions.add(action);         // lista geral
        module.centralPointActions.add(action);  // lista do seu módulo
      } // if
      // define se o módulo tem Actions recentemente liberados
      if (action.getJustReleased() && action.getVisible() && (action.getCategory() != Action.CATEGORY_CARD))
        module.setHasJustReleasedActions(true);
    } // if

    // retorna
    return action;
  }

  /**
   * Limpa a lista de ações.
   */
  public void clear() {
    actions.clear();
    cardActions.clear();
    centralPointActions.clear();
    modules.clear();
    moduleList = new Module[0];
  }

  /**
   * Libera recursos.
   */
  public void finalize() {
    clear();
    actions             = null;
    centralPointActions = null;
    modules             = null;
    moduleList          = null;
  }

  /**
   * Retorna o Action referente ao índice informado.
   * @param index int Índice do Action que se deseja retornar.
   * @return Action Retorna o Action referente ao índice informado.
   */
  public Action get(int index) {
    return (Action)actions.elementAt(index);
  }

  /**
   * Retorna o Action referente ao nome informado.
   * @param name String Nome do Action que se deseja retornar.
   * @param mobile boolean True se a ação que se deseja retornar for acessivel a patir de um dispositivo móvel.
   * @return Action Retorna o Action referente ao nome informado.
   */
  public Action get(String name, boolean mobile) {
    Action result = null;
    for (int i=0; i<actions.size(); i++) {
      result = get(i);
      if (result.getName().equalsIgnoreCase(name) && result.getMobile() == mobile)
        return result;
    } // for
    return null;
  }

  /**
   * Retorna um Action[] contendo a lista de todos os Action´s definidos como
   * Card.
   * @return Action[] Retorna um Action[] contendo a lista de todos os Action´s
   *                  definidos como Card.
   */
  public Action[] getCard() {
    // nosso resultado
    Action[] result = new Action[cardActions.size()];
    // retorna
    cardActions.copyInto(result);
    return result;
  }

  /**
   * Retorna um Action[] contendo a lista de todos os Action´s definidos como
   * Central Point.
   * @return Action[] Retorna um Action[] contendo a lista de todos os Action´s
   *                  definidos como Central Point.
   */
  public Action[] getCentralPoint() {
    // nosso resultado
    Action[] result = new Action[centralPointActions.size()];
    // retorna
    centralPointActions.copyInto(result);
    return result;
  }

  /**
   * Retorna um Action[] contendo a lista de Action´s definidos como Card
   * no módulo informado.
   * @param moduleName String Módulo cujos Central Point Action´s serão retornados.
   * @return Action[] Retorna um Action[] contendo a lista de Action´s definidos
   *                  como Card no módulo informado.
   */
  public Action[] getCard(String moduleName) {
    // localiza o módulo desejado
    Module module = (Module)modules.get(moduleName);
    // se não temos o módulo...retorna vazio
    if (module == null)
      return new Action[0];
    // nosso resultado
    Action[] result = new Action[module.cardActions.size()];
    // retorna
    module.cardActions.copyInto(result);
    return result;
  }

  /**
   * Retorna um Action[] contendo a lista de Action´s definidos como Central Point
   * no módulo informado.
   * @param moduleName String Módulo cujos Central Point Action´s serão retornados.
   * @return Action[] Retorna um Action[] contendo a lista de Action´s definidos
   *                  como Central Point no módulo informado.
   */
  public Action[] getCentralPoint(String moduleName) {
    // localiza o módulo desejado
    Module module = (Module)modules.get(moduleName);
    // se não temos o módulo...retorna vazio
    if (module == null)
      return new Action[0];
    // nosso resultado
    Action[] result = new Action[module.centralPointActions.size()];
    // retorna
    module.centralPointActions.copyInto(result);
    return result;
  }

  /**
   * Retorna um Action[] contendo a lista de Action's do módulo identificado
   * por 'moduleName'.
   * @param moduleName String Nome do módulo cujos Action's se deseja obter.
   * @return Action[] Retorna um Action[] contendo a lista de Action's do módulo
   *                  identificado por 'moduleName'.
   * @since 2006 R1
   */
  public Action[] get(String moduleName) {
    // localiza o módulo desejado
    Module module = (Module)modules.get(moduleName);
    // se não temos o módulo...retorna vazio
    if (module == null)
      return new Action[0];
    // nosso resultado
    Action[] result = new Action[module.actions.size()];
    // retorna
    module.actions.copyInto(result);
    return result;
  }

  /**
   * Retorna a lista de de módulos obtidos a partir da lista de Action's.
   * @return Module[] Retorna a lista de módulos obtidos a partir da
   *                  lista de Action's.
   * @since 2006 R1
   */
  public Module[] getModuleList() {
    // se ainda não obtivemos os nomes dos módulos...
    if (moduleList.length != modules.size()) {
      // nosso resultado
      moduleList = new Module[modules.size()];
      // põe os nomes dos módulos
      Enumeration enu = modules.elements();
      for (int i = 0; i < moduleList.length; i++)
        moduleList[i] = (Module)enu.nextElement();
      // ordena pelo nome
      Arrays.sort(moduleList, new ModuleComparator());
    } // if
    // retorna
    return moduleList;
  }

  /**
   * Retorna o tamanho da lista.
   * @return int Retorna o tamanho da lista.
   */
  public int size() {
    return actions.size();
  }

  /**
   * Remove o Action indicado por 'index'.
   * @param index int Índice do Action que se deseja remover.
   */
  public void remove(int index) {
    actions.remove(index);
  }

  /**
   * Retorna 'actionNames' ordenados por módulo, categoria e por ordem alfabética dos
   * seus títulos.
   * @param actionNames String[] Nomes dos Action's que se deseja ordenar.
   * @return String[] Retorna 'actionNames' ordenados por categoria e por ordem
   *         alfabética dos seus títulos.
   */
  public String[] sortActionNamesByCategory(String[] actionNames) {
    // obtém a lista de Actions pelos seus nomes
    Action[] actions = new Action[actionNames.length];
    for (int i=0; i<actionNames.length; i++)
      actions[i] = get(actionNames[i], false);
    // ordena
    Arrays.sort(actions, new CategoryComparator());
    // reconstrói a lista de nomes
    String[] result = new String[actions.length];
    for (int i=0; i<actions.length; i++)
      result[i] = actions[i].getName();
    // retorna
    return result;
  }

  /**
   * Ordena os Action's por módulo, categoria e por ordem alfabética dos seus títulos.
   * Ordena os Central Point Actions por Índice e por ordem alfabética.
   */
  public void sort() {
    // ------------------
    // ordena os Action´s

    // copia a lista atual para um Array
    Action[] actions = new Action[size()];
    this.actions.copyInto(actions);
    // ordena
    Arrays.sort(actions, new CategoryComparator());
    // refaz nossa lista
    this.actions.clear();
    for (int i=0; i<actions.length; i++)
      this.actions.add(actions[i]);

    // ----------------
    // ordena os Card´s

    // copia a lista atual para um Array
    Action[] cardActions = new Action[this.cardActions.size()];
    this.cardActions.copyInto(cardActions);
    // ordena
    Arrays.sort(cardActions, new CategoryComparator());
    // refaz nossa lista
    this.cardActions.clear();
    for (int i=0; i<cardActions.length; i++)
      this.cardActions.add(cardActions[i]);

    // --------------------------------
    // ordena os Central Point Action´s

    // copia a lista atual para um Array
    Action[] centralPointActions = new Action[this.centralPointActions.size()];
    this.centralPointActions.copyInto(centralPointActions);
    // ordena
    Arrays.sort(centralPointActions, new CentralPointComparator());
    // refaz nossa lista
    this.centralPointActions.clear();
    for (int i=0; i<centralPointActions.length; i++)
      this.centralPointActions.add(centralPointActions[i]);
  }

}
