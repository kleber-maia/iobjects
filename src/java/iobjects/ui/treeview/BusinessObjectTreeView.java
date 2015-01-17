package iobjects.ui.treeview;

import java.util.*;

import iobjects.*;
import iobjects.servlet.*;
import iobjects.util.*;

/**
 * Representa um TreeView para exibi��o e acesso a a��es de BusinessObject's.
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
   * Construtor padr�o.
   */
  public BusinessObjectTreeView() {
  }

  /**
   * Construtor estendido.
   * @param facade Facade Fachada.
   * @param id String Identifica��o do BusinessObjectTreeView na p�gina.
   * @param autoAlign true para que o BussinesObjectTreeView ocupe toda �rea cliente do seu container.
   */
  public BusinessObjectTreeView(Facade  facade,
                                String  id,
                                boolean autoAlign) {
    super(facade, id, autoAlign);
  }

  /**
   * Construtor estendido.
   * @param id String Identifica��o do BusinessObjectTreeView na p�gina.
   * @param height int Altura do BusinessObjectTreeView.
   * @param width int Largura do BusinessObjectTreeView.
   */
  public BusinessObjectTreeView(String  id,
                                int     height,
                                int     width) {
    super(id, height, width);
  }

  /**
   * Adiciona os n�s referentes aos Actions dos BusinessObjects existentes
   * nas extens�es da aplica��o.
   * @throws Exception Em caso de exce��o na tentativa de instanciar BusinessObjects.
   */
  private void addNodesFromActions() throws Exception {
    // se j� temos itens...dispara
    if (nodes.length() > 0)
      return;
    // lista de informa��es dos objetos de neg�cio
    Vector vector = new Vector();
    // loop nas a��es
    for (int i=0; i<actionList.size(); i++) {
      // a��o da vez
      Action action = actionList.get(i);
      // se � invis�vel continua
      if (!action.getVisible())
        continue;
      // se � mobile e n�o devemos inclu�-los...continua
      if (action.getMobile() && !showMobileActions)
        continue;
      // se � aninhado e n�o devemos inclu�-los...continua
      if ((action.getParentAction() != null) && !showNestedActions)
        continue;
      // adiciona na lista
      vector.add(new ActionInfo(action, action.getTreeViewPath()));
    } // for
    // ordena a lista de a��es
    ActionInfo[] actionInfoList = new ActionInfo[vector.size()];
    vector.copyInto(actionInfoList);
    Arrays.sort(actionInfoList, this);
    // adiciona os n�s
    for (int i=0; i<actionInfoList.length; i++) {
      // a��o da vez
      ActionInfo actionInfo = actionInfoList[i];
      // pega somente o caminho na �rvore
      String path = actionInfo.path.substring(0, actionInfo.path.lastIndexOf("/"));
      // pega o n� referente ao caminho
      Node pathNode = getNode(path);
      // adiciona o n� refere ao Action
      Node actionNode = pathNode.add(actionInfo.action.getCaption(),
                                     linkToHelp ? actionInfo.action.helpURL() : actionInfo.action.url(),
                                     actionInfo.action.getDescription());
      // se � um Action Mobile
      String extraImageUrl = "";
      if (actionInfo.action.getMobile())
        extraImageUrl = MOBILE_IMAGE;
      // imagem para Cart�es
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
      // imagem para Relat�rio
      else if (actionInfo.action.getCategory() == Action.CATEGORY_REPORT) {
        if (showCommands && actionInfo.action.commandList().size() > 0)
          actionNode.setImages(REPORT_IMAGE_CLOSED, REPORT_IMAGE_OPENED, extraImageUrl);
        else
          actionNode.setImages(REPORT_IMAGE, REPORT_IMAGE, extraImageUrl);
      } // if
      // o n� deve ser marcado?
      actionNode.value = actionInfo.action.getName();
      actionNode.checked = StringTools.arrayContains(checkedActionNames, actionNode.value);
      // se devemos adicionar os comandos...
      if (showCommands) {
        // adiciona os n�s dos comandos
        for (int w=0; w<actionInfo.action.commandList().size(); w++) {
          // comando da vez
          Command command = actionInfo.action.commandList().get(w);
          // adiciona o n� refere ao Comando
          Node commandNode = actionNode.add(command.getCaption(),
                                            "",
                                            command.getDescription());
          // sua imagens
          commandNode.setImages(COMMAND_IMAGE, COMMAND_IMAGE);
          // o n� deve ser marcado?
          commandNode.value = actionInfo.action.getName() + ":" + command.getName();
          commandNode.checked = StringTools.arrayContains(checkedCommandNames, commandNode.value);
        } // for w
      } // if
    } // for i
  }

  /**
   * Implementa��o do m�todo da interface Comparator utilizada para ordenar
   * alfabeticamente listas de ActionInfo.
   * @param object1 Primeiro ActionInfo para comparar.
   * @param object2 Segundo ActionInfo para comparar.
   * @return Utiliza o m�todo compareTo de String para o retorno.
   */
  public int compare(Object object1, Object object2) {
    // Actions comparados
    ActionInfo actionInfo1 = (ActionInfo)object1;
    ActionInfo actionInfo2 = (ActionInfo)object2;
    // qde de n�veis dos dois Actions
    int actionLevels1 = actionInfo1.path.split("/").length;
    int actionLevels2 = actionInfo2.path.split("/").length;
    // p�e na frente o Action com mais n�veis (pois possui uma sub-pasta)
    // e por ordem alfab�tica do seu caminho
    if (actionLevels1 != actionLevels2)
      return (actionLevels1 > actionLevels2 ? -1 : 1);
    else
      return actionInfo1.path.compareTo(actionInfo2.path);
  }

  /**
   * Retorna a lista de a��es encontradas nos objetos de neg�cio das extens�es
   * da aplica��o.
   * @return ActionList Retorna a lista de a��es encontradas nos objetos de
   *         neg�cio das extens�es da aplica��o.
   */
  public ActionList getActionList() {
    return actionList;
  }

  /**
   * Retorna os nomes das a��es que estar�o selecionadas no TreeView. Requer
   * o uso de CheckBoxes.
   * @return String[] Retorna os nomes das a��es que estar�o selecionadas no
   *         TreeView. Requer o uso de CheckBoxes.
   */
  public String[] getCheckedActionNames() {
    return checkedActionNames;
  }

  /**
   * Retorna os nomes dos comandos que estar�o selecionados no TreeView. Requer
   * o uso de CheckBoxes. Os nomes dos comandos estar�o precedidos do nome da
   * sua respectiva a��o e separados por ':', ex:: A��o:Comando
   * @return String[] Retorna os nomes das a��es que estar�o selecionadas no
   *         TreeView. Requer o uso de CheckBoxes.
   */
  public String[] getCheckedCommandNames() {
    return checkedCommandNames;
  }

  /**
   * Retorna o n� referente ao caminho informado, criando os que forem necess�rios.
   * @param path String Caminho cujo n� se deseja retornar.
   * @return Node Retorna o n� referente ao caminho informado, criando os que forem necess�rios.
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
      // n� da vez
      Node node = null;
      // se j� temos um n�...
      if (result != null) {
        // procura nos seus filhos
        node = result.childNodes.item(name);
        // se n�o existe...cria
        if (node == null)
          node = result.add(name);
      }
      // se ainda n�o temos um n�...
      else {
        // procura nos itens da raiz
        node = nodes.item(name);
        // se n�o existe...cria
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
   * Retorna true se os hyperlinks ser�o ligados � ajuda dos Actions.
   * @return Retorna true se os hyperlinks ser�o ligados � ajuda dos Actions.
   */
  public boolean getLinkToHelp() {
    return linkToHelp;
  }

  /**
   * Retorna true se os comandos dos Actions ser�o exibidos.
   * @return Retorna true se os comandos dos Actions ser�o exibidos.
   */
  public boolean getShowCommands() {
    return showCommands;
  }

  /**
   * Retorna true se as a��es aninhadas dos Actions ser�o exibidas.
   * @return Retorna true se as a��es aninhadas dos Actions ser�o exibidas.
   */
  public boolean getShowNestedActions() {
    return showNestedActions;
  }

  /**
   * Retorna true se os Actions do tipo mobile ser�o exibidos na �rvore.
   * @return boolean Retorna true se os Actions do tipo mobile ser�o exibidos na �rvore.
   */
  public boolean getShowMobileActions() {
    return showMobileActions;
  }

  /**
   * Retorna o script que representa o TreeView de objetos de neg�cio.
   * @return String Retorna o script que representa o TreeView de objetos de neg�cio.
   */
  public String script() {
    // adiciona os n�s referentes aos objetos
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
   * Define a lista de a��es encontradas nos objetos de neg�cio das extens�es
   * da aplica��o.
   * @param actionList Lista de a��es encontradas nos objetos de neg�cio das
   *        extens�es da aplica��o.
   */
  public void setActionList(ActionList actionList) {
    // se n�o mudou a propriedade...dispara
    if (actionList == this.actionList)
      return;
    // altera nosso valor
    this.actionList = actionList;
    // apaga o buffer
    clearBuffer();
    // apaga a lista de n�s
    nodes.clear();
  }

  /**
   * Define os nomes das a��es que estar�o selecionadas no TreeView. Requer
   * o uso de CheckBoxes.
   * @param checkedActionNames String[] Nomes das a��es que estar�o selecionadas no
   *        TreeView. Requer o uso de CheckBoxes.
   */
  public void setCheckedActionNames(String[] checkedActionNames) {
    this.checkedActionNames = checkedActionNames;
  }

  /**
   * Define os nomes dos comandos que estar�o selecionados no TreeView. Requer
   * o uso de CheckBoxes. Os nomes dos comandos devem estar precedidos do nome da
   * sua respectiva a��o e separados por ':', ex: A��o:Comando
   * @param checkedCommandNames String[] Nomes dos comandos que estar�o selecionados
   *                            no TreeView. Requer o uso de CheckBoxes. Os nomes
   *                            dos comandos devem estar precedidos do nome da
   *                            sua respectiva a��o e separados por ':', ex:
   *                            A��o:Comando
   */
  public void setCheckedCommandNames(String[] checkedCommandNames) {
    this.checkedCommandNames = checkedCommandNames;
  }

  /**
   * Define se os hyperlinks ser�o ligados � ajuda dos Actions.
   * @param linkToHelp boolean True para que os hyperlinks ser�o ligados � ajuda
   *                   dos Actions.
   */
  public void setLinkToHelp(boolean linkToHelp) {
    this.linkToHelp = linkToHelp;
  }

  /**
   * Define se os Actions do tipo mobile ser�o exibidos na �rvore.
   * @param showMobileActions boolean true se os Actions do tipo mobile ser�o exibidos na �rvore.
   */
  public void setShowMobileActions(boolean showMobileActions) {
    this.showMobileActions = showMobileActions;
  }

  /**
   * Define se os comandos dos Actions ser�o exibidos.
   * @param showCommands boolean true se os comandos dos Actions ser�o exibidos.
   */
  public void setShowCommands(boolean showCommands) {
    this.showCommands = showCommands;
  }

  /**
   * Define se as a��es aninhadas dos Actions ser�o exibidas.
   * @param showNestedActions boolean true se as a��es aninhadas dos Actions ser�o
   *                          exibidas.
   */
  public void setShowNestedActions(boolean showNestedActions) {
    this.showNestedActions = showNestedActions;
  }

}
