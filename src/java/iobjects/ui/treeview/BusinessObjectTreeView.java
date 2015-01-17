package iobjects.ui.treeview;

import java.util.*;

import iobjects.*;
import iobjects.servlet.*;
import iobjects.util.*;

/**
 * Representa um TreeView para exibição e acesso a ações de BusinessObject's.
 */
public class BusinessObjectTreeView extends TreeView implements Comparator {

  class ActionInfo {
    public Action action   = null;
    public String path     = "";
    // *
    public ActionInfo(Action action,
                      String path) {
      this.action = action;
      this.path = path;
    }
  }

  private String     COMMAND_IMAGE        = "images/treeview/command.png";
  private String     CARD_IMAGE           = "images/treeview/card.png";
  private String     CARD_IMAGE_CLOSED    = "images/treeview/cardclosed.png";
  private String     CARD_IMAGE_OPENED    = "images/treeview/cardopened.png";
  private String     ENTITY_IMAGE         = "images/treeview/entity.png";
  private String     ENTITY_IMAGE_CLOSED  = "images/treeview/entityclosed.png";
  private String     ENTITY_IMAGE_OPENED  = "images/treeview/entityopened.png";
  private String     MODULE_IMAGE_CLOSED  = "images/treeview/moduleclosed.png";
  private String     MODULE_IMAGE_OPENED  = "images/treeview/moduleopened.png";
  private String     PROCESS_IMAGE        = "images/treeview/process.png";
  private String     PROCESS_IMAGE_CLOSED = "images/treeview/processclosed.png";
  private String     PROCESS_IMAGE_OPENED = "images/treeview/processopened.png";
  private String     REPORT_IMAGE         = "images/treeview/report.png";
  private String     REPORT_IMAGE_CLOSED  = "images/treeview/reportclosed.png";
  private String     REPORT_IMAGE_OPENED  = "images/treeview/reportopened.png";
  private String     MOBILE_IMAGE         = "images/treeview/mobile.png";

  private ActionList actionList           = null;
  private String[]   checkedActionNames   = {};
  private String[]   checkedCommandNames  = {};
  private boolean    linkToHelp           = false;
  private boolean    showCommands         = false;
  private boolean    showNestedActions    = false;
  private boolean    showMobileActions    = false;

  /**
   * Construtor padrão.
   */
  public BusinessObjectTreeView() {
  }

  /**
   * Construtor estendido.
   * @param facade Facade Fachada.
   * @param id String Identificação do BusinessObjectTreeView na página.
   * @param autoAlign true para que o BussinesObjectTreeView ocupe toda área cliente do seu container.
   */
  public BusinessObjectTreeView(Facade  facade,
                                String  id,
                                boolean autoAlign) {
    super(facade, id, autoAlign);
  }

  /**
   * Construtor estendido.
   * @param id String Identificação do BusinessObjectTreeView na página.
   * @param height int Altura do BusinessObjectTreeView.
   * @param width int Largura do BusinessObjectTreeView.
   */
  public BusinessObjectTreeView(String  id,
                                int     height,
                                int     width) {
    super(id, height, width);
  }

  /**
   * Adiciona os nós referentes aos Actions dos BusinessObjects existentes
   * nas extensões da aplicação.
   * @throws Exception Em caso de exceção na tentativa de instanciar BusinessObjects.
   */
  private void addNodesFromActions() throws Exception {
    // se já temos itens...dispara
    if (nodes.length() > 0)
      return;
    // lista de informações dos objetos de negócio
    Vector vector = new Vector();
    // loop nas ações
    for (int i=0; i<actionList.size(); i++) {
      // ação da vez
      Action action = actionList.get(i);
      // se é invisível continua
      if (!action.getVisible())
        continue;
      // se é mobile e não devemos incluí-los...continua
      if (action.getMobile() && !showMobileActions)
        continue;
      // se é aninhado e não devemos incluí-los...continua
      if ((action.getParentAction() != null) && !showNestedActions)
        continue;
      // adiciona na lista
      vector.add(new ActionInfo(action, action.getTreeViewPath()));
    } // for
    // ordena a lista de ações
    ActionInfo[] actionInfoList = new ActionInfo[vector.size()];
    vector.copyInto(actionInfoList);
    Arrays.sort(actionInfoList, this);
    // adiciona os nós
    for (int i=0; i<actionInfoList.length; i++) {
      // ação da vez
      ActionInfo actionInfo = actionInfoList[i];
      // pega somente o caminho na árvore
      String path = actionInfo.path.substring(0, actionInfo.path.lastIndexOf("/"));
      // pega o nó referente ao caminho
      Node pathNode = getNode(path);
      // adiciona o nó refere ao Action
      Node actionNode = pathNode.add(actionInfo.action.getCaption(),
                                     linkToHelp ? actionInfo.action.helpURL() : actionInfo.action.url(),
                                     actionInfo.action.getDescription());
      // se é um Action Mobile
      String extraImageUrl = "";
      if (actionInfo.action.getMobile())
        extraImageUrl = MOBILE_IMAGE;
      // imagem para Cartões
      if (actionInfo.action.getCategory() == Action.CATEGORY_CARD) {
        if (showCommands && actionInfo.action.commandList().size() > 0)
          actionNode.setImages(CARD_IMAGE_CLOSED, CARD_IMAGE_OPENED, extraImageUrl);
        else
          actionNode.setImages(CARD_IMAGE, CARD_IMAGE, extraImageUrl);
      }
      // imagem para Entidade
      else if (actionInfo.action.getCategory() == Action.CATEGORY_ENTITY) {
        if (showCommands && actionInfo.action.commandList().size() > 0)
          actionNode.setImages(ENTITY_IMAGE_CLOSED, ENTITY_IMAGE_OPENED, extraImageUrl);
        else
          actionNode.setImages(ENTITY_IMAGE, ENTITY_IMAGE, extraImageUrl);
      }
      // imagem para Processo
      else if (actionInfo.action.getCategory() == Action.CATEGORY_PROCESS) {
        if (showCommands && actionInfo.action.commandList().size() > 0)
          actionNode.setImages(PROCESS_IMAGE_CLOSED, PROCESS_IMAGE_OPENED, extraImageUrl);
        else
          actionNode.setImages(PROCESS_IMAGE, PROCESS_IMAGE, extraImageUrl);
      }
      // imagem para Relatório
      else if (actionInfo.action.getCategory() == Action.CATEGORY_REPORT) {
        if (showCommands && actionInfo.action.commandList().size() > 0)
          actionNode.setImages(REPORT_IMAGE_CLOSED, REPORT_IMAGE_OPENED, extraImageUrl);
        else
          actionNode.setImages(REPORT_IMAGE, REPORT_IMAGE, extraImageUrl);
      } // if
      // o nó deve ser marcado?
      actionNode.value = actionInfo.action.getName();
      actionNode.checked = StringTools.arrayContains(checkedActionNames, actionNode.value);
      // se devemos adicionar os comandos...
      if (showCommands) {
        // adiciona os nós dos comandos
        for (int w=0; w<actionInfo.action.commandList().size(); w++) {
          // comando da vez
          Command command = actionInfo.action.commandList().get(w);
          // adiciona o nó refere ao Comando
          Node commandNode = actionNode.add(command.getCaption(),
                                            "",
                                            command.getDescription());
          // sua imagens
          commandNode.setImages(COMMAND_IMAGE, COMMAND_IMAGE);
          // o nó deve ser marcado?
          commandNode.value = actionInfo.action.getName() + ":" + command.getName();
          commandNode.checked = StringTools.arrayContains(checkedCommandNames, commandNode.value);
        } // for w
      } // if
    } // for i
  }

  /**
   * Implementação do método da interface Comparator utilizada para ordenar
   * alfabeticamente listas de ActionInfo.
   * @param object1 Primeiro ActionInfo para comparar.
   * @param object2 Segundo ActionInfo para comparar.
   * @return Utiliza o método compareTo de String para o retorno.
   */
  public int compare(Object object1, Object object2) {
    // Actions comparados
    ActionInfo actionInfo1 = (ActionInfo)object1;
    ActionInfo actionInfo2 = (ActionInfo)object2;
    // qde de níveis dos dois Actions
    int actionLevels1 = actionInfo1.path.split("/").length;
    int actionLevels2 = actionInfo2.path.split("/").length;
    // põe na frente o Action com mais níveis (pois possui uma sub-pasta)
    // e por ordem alfabética do seu caminho
    if (actionLevels1 != actionLevels2)
      return (actionLevels1 > actionLevels2 ? -1 : 1);
    else
      return actionInfo1.path.compareTo(actionInfo2.path);
  }

  /**
   * Retorna a lista de ações encontradas nos objetos de negócio das extensões
   * da aplicação.
   * @return ActionList Retorna a lista de ações encontradas nos objetos de
   *         negócio das extensões da aplicação.
   */
  public ActionList getActionList() {
    return actionList;
  }

  /**
   * Retorna os nomes das ações que estarão selecionadas no TreeView. Requer
   * o uso de CheckBoxes.
   * @return String[] Retorna os nomes das ações que estarão selecionadas no
   *         TreeView. Requer o uso de CheckBoxes.
   */
  public String[] getCheckedActionNames() {
    return checkedActionNames;
  }

  /**
   * Retorna os nomes dos comandos que estarão selecionados no TreeView. Requer
   * o uso de CheckBoxes. Os nomes dos comandos estarão precedidos do nome da
   * sua respectiva ação e separados por ':', ex:: Ação:Comando
   * @return String[] Retorna os nomes das ações que estarão selecionadas no
   *         TreeView. Requer o uso de CheckBoxes.
   */
  public String[] getCheckedCommandNames() {
    return checkedCommandNames;
  }

  /**
   * Retorna o nó referente ao caminho informado, criando os que forem necessários.
   * @param path String Caminho cujo nó se deseja retornar.
   * @return Node Retorna o nó referente ao caminho informado, criando os que forem necessários.
   */
  private Node getNode(String path) {
    Node result = null;
    // loop no caminho
    while (!path.equals("")) {
      // nome do item da vez
      String name = "";
      int    barPos = path.indexOf("/");
      if (barPos > 0) {
        name = path.substring(0, barPos);
        path = path.substring(barPos+1, path.length());
      }
      else {
        name = path;
        path = "";
      } // if
      // nó da vez
      Node node = null;
      // se já temos um nó...
      if (result != null) {
        // procura nos seus filhos
        node = result.childNodes.item(name);
        // se não existe...cria
        if (node == null)
          node = result.add(name);
      }
      // se ainda não temos um nó...
      else {
        // procura nos itens da raiz
        node = nodes.item(name);
        // se não existe...cria
        if (node == null) {
          node = nodes.add(name);
          node.setImages(MODULE_IMAGE_CLOSED, MODULE_IMAGE_OPENED);
        } // if
      } // if
      // nosso resultado
      result = node;
    } // while
    // retorna o que encontramos
    return result;
  }

  /**
   * Retorna true se os hyperlinks serão ligados à ajuda dos Actions.
   * @return Retorna true se os hyperlinks serão ligados à ajuda dos Actions.
   */
  public boolean getLinkToHelp() {
    return linkToHelp;
  }

  /**
   * Retorna true se os comandos dos Actions serão exibidos.
   * @return Retorna true se os comandos dos Actions serão exibidos.
   */
  public boolean getShowCommands() {
    return showCommands;
  }

  /**
   * Retorna true se as ações aninhadas dos Actions serão exibidas.
   * @return Retorna true se as ações aninhadas dos Actions serão exibidas.
   */
  public boolean getShowNestedActions() {
    return showNestedActions;
  }

  /**
   * Retorna true se os Actions do tipo mobile serão exibidos na árvore.
   * @return boolean Retorna true se os Actions do tipo mobile serão exibidos na árvore.
   */
  public boolean getShowMobileActions() {
    return showMobileActions;
  }

  /**
   * Retorna o script que representa o TreeView de objetos de negócio.
   * @return String Retorna o script que representa o TreeView de objetos de negócio.
   */
  public String script() {
    // adiciona os nós referentes aos objetos
    try {
      addNodesFromActions();
    }
    catch (Exception e) {
      throw new RuntimeException(new ExtendedException(getClass().getName(), "script", e));
    } // try-finally
    // retorna o script
    return super.script();
  }

  /**
   * Define a lista de ações encontradas nos objetos de negócio das extensões
   * da aplicação.
   * @param actionList Lista de ações encontradas nos objetos de negócio das
   *        extensões da aplicação.
   */
  public void setActionList(ActionList actionList) {
    // se não mudou a propriedade...dispara
    if (actionList == this.actionList)
      return;
    // altera nosso valor
    this.actionList = actionList;
    // apaga o buffer
    clearBuffer();
    // apaga a lista de nós
    nodes.clear();
  }

  /**
   * Define os nomes das ações que estarão selecionadas no TreeView. Requer
   * o uso de CheckBoxes.
   * @param checkedActionNames String[] Nomes das ações que estarão selecionadas no
   *        TreeView. Requer o uso de CheckBoxes.
   */
  public void setCheckedActionNames(String[] checkedActionNames) {
    this.checkedActionNames = checkedActionNames;
  }

  /**
   * Define os nomes dos comandos que estarão selecionados no TreeView. Requer
   * o uso de CheckBoxes. Os nomes dos comandos devem estar precedidos do nome da
   * sua respectiva ação e separados por ':', ex: Ação:Comando
   * @param checkedCommandNames String[] Nomes dos comandos que estarão selecionados
   *                            no TreeView. Requer o uso de CheckBoxes. Os nomes
   *                            dos comandos devem estar precedidos do nome da
   *                            sua respectiva ação e separados por ':', ex:
   *                            Ação:Comando
   */
  public void setCheckedCommandNames(String[] checkedCommandNames) {
    this.checkedCommandNames = checkedCommandNames;
  }

  /**
   * Define se os hyperlinks serão ligados à ajuda dos Actions.
   * @param linkToHelp boolean True para que os hyperlinks serão ligados à ajuda
   *                   dos Actions.
   */
  public void setLinkToHelp(boolean linkToHelp) {
    this.linkToHelp = linkToHelp;
  }

  /**
   * Define se os Actions do tipo mobile serão exibidos na árvore.
   * @param showMobileActions boolean true se os Actions do tipo mobile serão exibidos na árvore.
   */
  public void setShowMobileActions(boolean showMobileActions) {
    this.showMobileActions = showMobileActions;
  }

  /**
   * Define se os comandos dos Actions serão exibidos.
   * @param showCommands boolean true se os comandos dos Actions serão exibidos.
   */
  public void setShowCommands(boolean showCommands) {
    this.showCommands = showCommands;
  }

  /**
   * Define se as ações aninhadas dos Actions serão exibidas.
   * @param showNestedActions boolean true se as ações aninhadas dos Actions serão
   *                          exibidas.
   */
  public void setShowNestedActions(boolean showNestedActions) {
    this.showNestedActions = showNestedActions;
  }

}
