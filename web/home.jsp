
<%--
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
--%>
<%@include file="include/beans.jsp"%>

<%@page import="java.util.*"%>
<%@page import="java.sql.*"%>

<%@page import="iobjects.xml.ui.*"%>

<%!
  Command COMMAND_CHANGE_DISPLAY_MODE = new Command("changeDisplayMode", "", "");

  String FAVORITES            = "favorites";
  String LOGOFF               = "logoff";
  String MASTER_RELATION      = "masterRelation";
  String MASTER_RELATION_USER = "masterRelationUser";

  String COMMAND_AJAX_ADD_TO_FAVORITES       = "addToFavorite";
  String COMMAND_AJAX_CHANGE_MASTER_RELATION = "changeMasterRelation";
  String HEADER_MESSAGE                      = "message";
  String PARAM_ACTION_NAME                   = "actionName";

  /**
   * Adiciona um novo Action a lista de favoritos na fachada.
   */
  public boolean addToFavorites(Facade facade,
                                String actionName) {
    // localiza o Action
    Action action = facade.actionList().get(actionName, false);
     // se não é um Action válido...dispara
    if ((action == null) || (action.getCategory() == Action.CATEGORY_NONE))
      return false;
    // pega a lista atual da fachada
    Param paramFavorites = facade.userParamList().get(FAVORITES);
    // se não achamos...cria
    if (paramFavorites == null) {
      paramFavorites = new Param(FAVORITES, "");
      facade.userParamList().add(paramFavorites);
    } // if
    // Actions existentes
    String favoritesValue = paramFavorites.getValue() + ";";
    // se já existe...dispara
    if (favoritesValue.indexOf(actionName + ";") >= 0)
      return false;
    // adiciona o Action recebido
    favoritesValue = paramFavorites.getValue() + (paramFavorites.getValue().equals("") ? "" : ";") + actionName;
    // transforma em array
    String[] favorites = favoritesValue.split(";");
    // põe em ordem por categoria e alfabética
    favorites = facade.actionList().sortActionNamesByCategory(favorites);
    // salva no parâmetro da fachada
    paramFavorites.setValue(StringTools.arrayStringToString(favorites, ";"));
    // se chegou aqui...OK
    return true;
  }

  /**
   * Retorna o nome do módulo com o tamanho máximo de 22 caracteres.
   */
  public String adjustModuleName(String value) {
    if (value.length() > 22)
      return value.substring(0, 19) + "...";
    else
      return value;
  }

  public void changeMasterRelation(Facade facade,
                                   String masterRelationValue,
                                   String masterRelationUserValue) throws Exception {
    // associa os novos valores a relação mestre
    if ((!masterRelationValue.equals("")) &&
        (!masterRelationValue.replace(';', ' ').trim().equals(""))) {
      facade.masterRelation().setValues(masterRelationValue.split(";"));
    } // if
    if ((!masterRelationUserValue.equals("")) &&
        (!masterRelationUserValue.replace(';', ' ').trim().equals(""))) {
      facade.masterRelation().setUserValues(masterRelationUserValue.split(";"));
    } // if
  }

  public String getMasterRelationFilter(Facade facade) {
    // começamos com o filtro configurado na relação mestre
    StringBuffer result = new StringBuffer(facade.masterRelationInformation().getFilterExpression());
    // se o usuário atual não é privilegiado...
    // ...vamos construir o filtro baseado nos papéis que ele exerce
    if (!facade.getLoggedUser().getPrivileged()) {
      // impede que todos os registros de Relação Mestre apareçam
      // dessa forma o filtro que será construído a seguir será o verdadeiro
      // responsável pela exibição apenas dos registros permitidos
      if (result.length() > 0)
        result.append(" AND ");
      result.append("(1=2)");
      // direitos de acesso do usuário atual sobre as Relações Mestre
      MasterRelationInfoList masterRelationInfoList = facade.getLoggedUser().getMasterRelationInfoList();
      // campos chave da Relação Mestre para as expressões de filtro
      String[] keyFieldNames = facade.masterRelationInformation().getReturnFieldNames();
      // loop nas informações de Relações Mestre
      for (int i=0; i<masterRelationInfoList.size(); i++) {
        // informação da vez
        MasterRelationInfo masterRelationInfo = masterRelationInfoList.get(i);
        // valores chave da Relação Mestre da vez
        String[] masterRelationValues = masterRelationInfo.getValue().split(";");
        // constrói a expressão de filtro da relação mestre atual
        String masterRelationExpression = "";
        for (int w=0; w<keyFieldNames.length; w++) {
          if (w > 0)
            masterRelationExpression += " AND ";
          masterRelationExpression += "(" + keyFieldNames[w] + "=" + masterRelationValues[w] + ")";
        } // for
        // adiciona a expressão ao resultado
        if (result.length() > 0)
          result.append(" OR ");
        result.append(masterRelationExpression);
      } // for
    } // if
    // retorna
    return result.toString();
  }

  /**
   * Retorna um ParamList contendo os itens disponíveis para seleção do MasterRelation.
   */
  public ParamList getMasterRelationList(Facade facade) throws Exception {
    // nossos objetos
    PreparedStatement statement = null;
    try {
      // inicia transação
      facade.beginTransaction();
      // instância da entidade MasterRelation
      Entity entity = facade.getEntity(facade.masterRelationInformation().getClassName());
      // faz a consulta no banco
      statement = SqlTools.prepareSelect(entity.getConnection(),
                                         entity.getTableName(),
                                         StringTools.arrayConcat(facade.masterRelationInformation().getReturnFieldNames(), facade.masterRelationInformation().getDisplayFieldNames()),
                                         facade.masterRelationInformation().getOrderFieldNames(),
                                         getMasterRelationFilter(facade));
      statement.execute();
      ResultSet resultSet = statement.getResultSet();
      // nosso resultado
      ParamList result = new ParamList();
      // loop nos registros
      while (resultSet.next()) {
        // valor e valor do usuário
        String value     = resultSet.getString(facade.masterRelationInformation().getReturnFieldNames()[0]);
        String userValue = StringTools.toCaptalCase(resultSet.getString(facade.masterRelationInformation().getDisplayFieldNames()[0]), true);
        // se o master relation está vazio...configura o primeiro valor encontrado
        if (facade.masterRelation().isEmpty())
          changeMasterRelation(facade, value, userValue);
        // adiciona ao resultado
        result.add(new Param(userValue, value));
      } // while
      // salva tudo
      facade.commitTransaction();
      // retorna
      return result;
    }
    catch (Exception e) {
      // desfaz tudo
      facade.rollbackTransaction();
      // mostra exceção
      throw e;
    } // try-catch
    finally {
      // libera recursos
      if (statement != null) {
        statement.getResultSet().close();
        statement.close();
      } // if
    } // try-finally
  }

  /**
   * Retorna true se o módulo possui actions visíveis.
   */
  public boolean hasVisibleActions(Facade facade, ActionList.Module module) {
    // loop nos actions
    for (int i=0; i<module.getActions().size(); i++) {
      Action action = (Action)module.getActions().elementAt(i);
      if (action.getCategory() == Action.CATEGORY_CARD)
        continue;
      if (action.getVisible() && (action.getMobile() == facade.getBrowserMobile()))
        return true;
    } // for
    return false;
  }

  /**
   * Retorna um item de menu que representa o Action informado.
   */
  public String menuItem(Facade facade,
                         Action action) {
    // retorna
//    return "<img src=\""+ (action.getCentralPoint() ? "images/centralpoint16x16.png" : ImageList.getImageUrl(action, true)) + "\" align=\"absmiddle\">&nbsp;<a href=\"javascript:void(0);\" onclick=\"" + (action.getShowType() == Action.SHOW_TYPE_EMBEDED ? "showAction('" + action.getName() + "');" : action.url()) + "\" target=\"frameContent\" title=\"" + action.getDescription() + "\">" + action.getCaption() + "</a>";
      return "<img src=\""+ (action.getCentralPoint() ? "images/centralpoint16x16.png" : ImageList.getImageUrl(action, true)) + "\" align=\"absmiddle\">&nbsp;<a href=\"javascript:void(0);\" onclick=\"" + (action.getShowType() == Action.SHOW_TYPE_EMBEDED ? "showAction('" + action.getName() + "');" : action.url()) + "\" title=\"" + action.getDescription() + "\">" + action.getCaption() + "</a>";
  }

  /**
   * Retorna uma lista de itens de menu representando os Actions favoritos da fachada.
   */
  public String menuItemsFavorites(Facade facade) {
    // lista de favoritos
    String[] favorites = new String[0];
    // pega a lista atual da fachada
    Param paramFavorites = facade.userParamList().get(FAVORITES);
    // se achamos...transforma em array
    if (paramFavorites != null)
      favorites = paramFavorites.getValue().split(";");
    // nosso resultado
    StringBuffer result = new StringBuffer();
    // loop nos favoritos
    for (int i=0; i<favorites.length; i++) {
      // Action da vez
      Action action = facade.actionList().get(favorites[i], facade.getBrowserMobile());
      // se é inválido...continua
      if (action == null)
        continue;
      // adiciona ao resultado
      result.append("<tr><td nowrap=\"nowrap\" style=\"height:20px;\">" + menuItem(facade, action) + "</td></tr>");
    } // for
    // retorna
    return "<table cellspacing=\"0\" cellpadding=\"2\">"
           + "<tr><td style=\"height:20px;\">"
           +   "<img src=\"images/favorites16x16.png\" align=\"absmiddle\">&nbsp;<a href=\"javascript:void(0);\" onclick=\"addToFavorites();\" title=\"Adiciona a tela atualmente exibida a sua lista de favoritos.\">Adicionar aos favoritos</a>"
           + "</td></tr>"
           + result.toString()
           + "</table>";
  }

  /**
   * Retorna uma lista de itens de menu representando os itens de faqList.
   */
  public String menuItemsFAQ(FAQList faqList) {
    // nosso resultado
    StringBuffer result = new StringBuffer();
    // loop nos itens
    for (int i=0; i<faqList.size(); i++) {
      // item da vez
      FAQ faq = faqList.get(i);
      // adiciona ao resultado
      result.append("<tr><td nowrap=\"nowrap\" style=\"height:20px;\"><img src=\"images/help16x16.png\" align=\"absmiddle\">&nbsp;<a href=\"javascript:void(0);\" onclick=\"showFAQ('" + faq.getName() + "');\">" + faq.getQuestion() + "</a></td></tr>");
    } // while
    // retorna
    return "<table cellspacing=\"0\" cellpadding=\"2\">"
           + result.toString()
           + "</table>";
  }

  /**
   * Retorna uma lista de itens de menu representando os itens de Master Relation.
   */
  public String menuItemsMasterRelation(ParamList masterRelationList) {
    // nosso resultado
    StringBuffer result = new StringBuffer();
    // loop nos itens
    for (int i=0; i<masterRelationList.size(); i++) {
      // item da vez
      Param masterRelationItem = masterRelationList.get(i);
      // adiciona ao resultado
      result.append("<tr><td nowrap=\"nowrap\" style=\"height:20px;\"><img src=\"images/masterrelation16x16.png\" align=\"absmiddle\">&nbsp;<a href=\"javascript:void(0);\" onclick=\"changeMasterRelation('" + masterRelationItem.getValue() + "', '" + masterRelationItem.getName() + "');\">" + masterRelationItem.getName() + "</a></td></tr>");
    } // while
    // se não temos nada...
    if (result.equals(""))
      result.append("<tr><td style=\"height:20px;\"><img src=\"images/error16x16.png\" align=\"absmiddle\">&nbsp;(vazio)</td></tr>");
    // retorna
    return "<table cellspacing=\"0\" cellpadding=\"2\">"
           + result.toString()
           + "</table>";
  }

  /**
   * Retorna um TabPanel para ser adicionado ao Tab atual do Ribbon contendo
   * todos os botões que representam todos os Actions do módulo informado.
   */
  public String ribbonTabPanel(Facade            facade,
                               Ribbon            ribbon,
                               ActionList.Module module,
                               int               category,
                               String            caption) {
    // Actions da categoria desejada
    int actionCount = module.countCategory(category);
    // se não temos nada...dispara
    if (actionCount == 0)
      return "";
    // itens exibidos
    StringBuffer visible = new StringBuffer();
    // itens ocultos
    Vector       hiddenAccessPaths = new Vector();
    StringBuffer hiddenButtons     = new StringBuffer();
    Vector       hiddenPopupMenus  = new Vector();
    // loop nos Actions
    for (int i=0; i<module.getActions().size(); i++) {
      // Action da vez
      Action action = (Action)module.getActions().get(i);
      // se não é da categoria...continua
      if (action.getCategory() != category)
        continue;
      // se é filho...continua
      if (action.getParentAction() != null)
        continue;
      // se não está de acordo com o dispositivo...continua
      if (action.getMobile() != facade.getBrowserMobile())
        continue;
      // se é visível...
      if (action.getAccessPath().equals(""))
        // adiciona o item
        visible.append(ribbon.button("button" + action.getName(), action.getCaption(), action.getDescription(), (action.getCentralPoint() ? "images/centralpoint32x32.png" : ImageList.getImageUrl(action, false)), "", "", (action.getShowType() == Action.SHOW_TYPE_EMBEDED ? "showAction('" + action.getName() + "');" : action.url()), null, Ribbon.KIND_BIG_BUTTON, false));
      // se é oculto...
      else {
        // se ainda não criamos seu botão...
        if (!hiddenAccessPaths.contains(category + action.getAccessPath())) {
          // id para o botão e o popup
          String id = StringTools.format(module.getName() + category + action.getAccessPath(), false, true, true, true, false);
          // cria um novo menu popup
          PopupMenu hiddenPopupMenu = new PopupMenu("popup" + id, "button" + id);
          // adiciona na lista
          hiddenPopupMenus.add(hiddenPopupMenu);
          // cria um novo botão
          hiddenButtons.append(ribbon.button("button" + id, action.getAccessPath(), "", "images/folder32x32.png", "", "", "", hiddenPopupMenu, Ribbon.KIND_BIG_BUTTON, false));
          // adiciona seu nome na lista
          hiddenAccessPaths.add(category + action.getAccessPath());
        } // if
      } // if
    } // for
    // nosso resultado final
    StringBuffer result = new StringBuffer();
    // itens visíveis
    result.append(visible.toString());
    // botões de acesso aos itens ocultos
    result.append(hiddenButtons.toString());
    // gera os menus popup
    for (int i=0; i<hiddenPopupMenus.size(); i++) {
      // PopupMenu da vez
      PopupMenu popupMenu = (PopupMenu)hiddenPopupMenus.elementAt(i);
      // Categoria e AccessPath da vez
      String accessPath = (String)hiddenAccessPaths.elementAt(i);
      // inicia o PopupMenu
      result.append(popupMenu.begin());
      result.append("<table cellspacing=\"0\" cellpadding=\"2\">");
      // loop nos Actions a procura dos que irão compor o menu popup
      for (int w=0; w<module.getActions().size(); w++) {
        // Action da vez
        Action action = (Action)module.getActions().get(w);
        // se não é da Categoria e AccessPath procurados...continua
        if (!(action.getCategory() + action.getAccessPath()).equals(accessPath))
          continue;
        // adiciona o item ao PopupMenu
        result.append("<tr><td nowrap=\"nowrap\">" + menuItem(facade, action) + "</td></tr>");
      } // for w
      // finaliza o PopupMenu
      result.append("</table>");
      result.append(popupMenu.end());
    } // for i
    // retorna
    return ribbon.beginTabPanel(caption) + result.toString() + ribbon.endTabPanel();
  }
%>

<%-- ---------------------- --%>
<%-- Comandos Pré-Definidos --%>
<%-- ---------------------- --%>

<%
  // se quer adicionar um Favorito...
  if (Controller.getCommand(request).equals(COMMAND_AJAX_ADD_TO_FAVORITES)) {
    try {
      // Action que vamos adicionar
      String actionName = Controller.getParameter(request, PARAM_ACTION_NAME);
      // define nosso tipo de resposta
      Ajax.setResponseTypeText(response);
      // se adicionou aos favoritos...retorna o novo menu
      if (addToFavorites(facade, actionName)) {
        %><%=menuItemsFavorites(facade)%><%
      }
      // se não adicionou nada...não temos o que retornar
      else {
        Ajax.setResponseHeader(response, HEADER_MESSAGE, "A tela atual já foi ou não pode ser adicionada aos favoritos.");
      } // if
    }
    catch (Exception e) {
      Ajax.setResponseHeader(response, HEADER_MESSAGE, "Exceção: " + e.getClass().getName() + " - " + e.getMessage());
    } // try-catch
    // dispara
    return;
  }

  // se quer mudar a relação mestre...
  else if (Controller.getCommand(request).equals(COMMAND_AJAX_CHANGE_MASTER_RELATION)) {
    // valores da relação mestre
    String masterRelationValue     = Controller.getParameter(request, MASTER_RELATION);
    String masterRelationUserValue = Controller.getParameter(request, MASTER_RELATION_USER);
    // define nosso tipo de resposta
    Ajax.setResponseTypeText(response);
    // muda a relação mestre
    changeMasterRelation(facade, masterRelationValue, masterRelationUserValue);
    // retorna o novo nome da relação mestre
    %><%=StringTools.toCaptalCase(masterRelationUserValue, true)%><%
    // dispara
    return;
  } // if

  // devemos mostrar o quadro de avisos?
  boolean showNoticeBoard = facade.applicationNoticeBoard().getShowToAllUsers() ||
                            (facade.applicationNoticeBoard().getShowToPrivilegedUsers() &&
                             facade.getLoggedUser().getPrivileged());
%>

<%-- -------------------- --%>
<%-- Processamento Normal --%>
<%-- -------------------- --%>

<%
  try {
    // nossos métodos Ajax
    Ajax ajaxAddToFavorites       = new Ajax(facade, "ajaxAddToFavorites", Controller.ACTION_HOME.getName(), COMMAND_AJAX_ADD_TO_FAVORITES);
    Ajax ajaxChangeMasterRelation = new Ajax(facade, "ajaxChangeMasterRelation", Controller.ACTION_HOME.getName(), COMMAND_AJAX_CHANGE_MASTER_RELATION);
    // nosso Ribbon
    Ribbon ribbon = new Ribbon(facade, "ribbon");
    ribbon.setRewindTime(60);
    // nossos PopupMenus
    PopupMenu popupMenuCentralPoints  = new PopupMenu("popupMenuCentralPoints", "buttonCentralPoints");
    PopupMenu popupMenuFavoritos      = new PopupMenu("popupMenuFavoritos", "buttonFavoritos");
    PopupMenu popupMenuFlowCharts     = new PopupMenu("popupMenuFlowCharts", "buttonFlowCharts");
    PopupMenu popupMenuMasterRelation = new PopupMenu("popupMenuMasterRelation", "buttonMasterRelation");
    // nosso Balloon
    Balloon balloon = new Balloon(facade, request, "balloonHome");
%>

<html>
  <head>
    <title><%=facade.applicationInformation().getName()%></title>
    <link href="<%=facade.getStyle().userInterface()%>" rel="stylesheet" type="text/css">
    <link href="<%=facade.iconURL()%>" rel="shortcut icon" />
    <script src="include/scripts.js" type="text/javascript"></script>
    <!-- iPod, iPhone e iPad -->
    <meta name="apple-mobile-web-app-capable" content="yes" />
    <meta name="apple-mobile-web-app-status-bar-style" content="black" />
    <meta name="viewport" content="initial-scale=1 maximum-scale=1 user-scalable=0" />
    <meta name="apple-touch-fullscreen" content="yes" />
    <link rel="apple-touch-icon" href="<%=facade.iconURL()%>" />
  </head>

  <script type="text/javascript">
    // variável que controla se estamos sendo substituídos por login.jsp
    // isso ocorre quando a sessão expirou ou foi cancelada
    var replacedByLogin = false;
    // variável que controla se o usuário efetuou logoff
    var logedOff = false;

    // se não fomos carregados em um Popup...
    if (window.opener == null) {
      // altura e largura
      var inWidth  = screen.availWidth - 10;
      var inHeight = screen.availHeight - 32;
      // posição
      var inLeft = 0;
      var inTop  = 0;
      // carrega-nos em um Popup principal
      var wnd = window.open(window.location.href, '<%=facade.applicationInformation().getName().replace(' ', '_')%>', 'top=' + inTop + ',left=' + inLeft + ',width=' + inWidth + ',height=' + inHeight + ',menubar=no,location=no,scrollbars=no,status=no,resizable=no');
      if (wnd != null) {
        // põe o foco
        wnd.focus();
        // mostra o aviso de popup em nosso lugar
        window.location.href = "popup.jsp";
        // dispara
        document.close();
      } // if
    } // if

    /**
     * Adiciona a página atualmente exibida aos Favoritos através de Ajax.
     */
    function addToFavorites() {
      // nossos objetos
      var frameDesktop = document.getElementById("frameDesktop");
      var frameContent = document.getElementById("frameContent");
      // tenta obter o nome do Action
      var actionName = frameContent.contentWindow.location.href;
      var actionPos  = actionName.indexOf("action=");
      var commandPos = actionName.indexOf("command=");
      // se não temos nada...dispara
      if ((frameContent.contentWindow.document.title == "") || (frameDesktop.style.display != "none")) {
        alert("Por favor, acesse a tela que deseja adicionar aos favoritos.");
        return;
      }
      // se não temos Action...dispara
      if (actionPos < 0) {
        alert("Por favor, acesse " + frameContent.contentWindow.document.title + " novamente e em seguida adicione aos favoritos.");
        return;
      }
      // se temos Command...dispara
      else if (commandPos >= 0) {
        alert("Talvez a tela atual não possa ser adicionada aos favoritos.");
        return;
      } // if
      // obtém somente o nome do Action
      actionName = actionName.substring(actionPos + 7, actionName.length);
      var andPos = actionName.indexOf("&");
      if (andPos > 0)
        actionName = actionName.substring(0, andPos);
      var paramActionName = "<%=PARAM_ACTION_NAME%>=" + actionName;
      // adiciona a favoritos
      <%=ajaxAddToFavorites.request(new String[]{"paramActionName"}, "addToFavoritesCallback")%>
    }

    function addToFavoritesCallback() {
      // se ainda não temos nada...dispara
      if (!<%=ajaxAddToFavorites.isResponseReady()%>)
        return;
      // se ocorreu tudo OK...
      if (<%=ajaxAddToFavorites.isResponseStatusOK()%>) {
        // se temos uma mensagem para exibir...
        var message = <%=ajaxAddToFavorites.getResponseHeader(HEADER_MESSAGE)%>;
        if (message != "") {
          alert(message);
        }
        // se não...recebemos o novo menu de favoritos...
        else {
          document.getElementById("popupMenuFavoritos").innerHTML = <%=ajaxAddToFavorites.responseText()%>;
        }
      }
      // se houve um erro...mostra
      else
        alert(<%=ajaxAddToFavorites.responseErrorMessage()%>);
    }

    function changePassword() {
      <%=Popup.script(Controller.ACTION_CHANGE_PASSWORD.url(),
                      Controller.ACTION_CHANGE_PASSWORD.getName(),
                      250,
                      400,
                      Popup.POSITION_CENTER)%>
    }

    function changeStyle() {
      <%=Popup.script(Controller.ACTION_CHANGE_STYLE.url(),
                      Controller.ACTION_CHANGE_STYLE.getName(),
                      250,
                      400,
                      Popup.POSITION_CENTER)%>
    }

    function showNoticeBoard(index) {
      <%=Popup.script("popupnoticeboard.jsp?noticeIndex=' + index + '",
                      "noticeBoard",
                      250,
                      400,
                      Popup.POSITION_CENTER)%>
    }

    /**
     * Verifica se o usuário clicou em Efetuar Logoff ou apenas fechou a janela
     * e o avisa sobre o ocorrido.
     */
    function checkLogoff() {
      // se o usuário efetuou logoff...retorna OK
      if (logedOff)
        return true;
      // se perdemos a sessão...
      if (replacedByLogin)
        alert("Essa sessão expirou porque ficou inativa durante um longo período ou foi cancelada porque outra pessoa efetuou Logon com o mesmo nome de usuário. Talvez suas preferências não tenham sido salvas.");
      // se apenas fechou a janela...
      else
        alert("A aplicação foi fechada de forma inadequada porque não foi efetuado Logoff. Talvez suas preferências não tenham sido salvas.");
      // retorna falha
      return false;
    }

    function logoff() {
      // se o usuário não confirmou...dispara
      if (!confirm("Tem certeza que deseja efetuar logoff?"))
        return;
      // informa nossa janela pai que efetuamos logoff
      logedOff = true;
      // realiza logoff
      window.location.href = "<%=Controller.ACTION_LOGIN.url(LOGOFF + "=true")%>";
    }

    function showAction(action) {
      // nossos objetos
      var frameContent = document.getElementById("frameContent");
      // mostra
      showFrameContent();
      // tenta obter o nome do Action
      var actionName = frameContent.contentWindow.location.href;
      var actionPos  = actionName.indexOf("action=");
      actionName = actionName.substring(actionPos + 7, actionName.length);
      var andPos = actionName.indexOf("&");
      if (andPos > 0)
        actionName = actionName.substring(0, andPos);
      // se mudou o Action...
      if (actionName != action) {
        frameContent.contentWindow.document.body.style.cursor = "wait";
        frameContent.contentWindow.location.href = "controller?action=" + action;
      } // if
    }

    function showFAQ(faqName) {
      <%=Popup.script(Controller.ACTION_HELP.url(HelpManager.FAQ_NAME + "=' + faqName + '"),
                      HelpManager.WINDOW_NAME,
                      480,
                      700,
                      Popup.POSITION_RIGHT_BOTTOM,
                      false, false, false, false, false, true)%>
    }

    function showFrameContent() {
      var frameContent = document.getElementById("frameContent");
      var frameDesktop = document.getElementById("frameDesktop");
      frameContent.style.display = "block";
      frameDesktop.style.display = "none";
    }

    function showFrameDesktop() {
      var frameContent = document.getElementById("frameContent");
      var frameDesktop = document.getElementById("frameDesktop");
      frameContent.style.display = "none";
      frameDesktop.style.display = "block";
      // sugere atualização
      frameDesktop.contentWindow.updateCards();
    }

    function frameDesktopForceUpdate() {
      var frameDesktop = document.getElementById("frameDesktop");
      frameDesktop.contentWindow.forceUpdate = true;
    }

    function showHelp() {
      <%=Popup.script(Controller.ACTION_HELP.url(""),
                      HelpManager.WINDOW_NAME,
                      480,
                      700,
                      Popup.POSITION_RIGHT_BOTTOM,
                      false, false, false, false, false, true)%>
    }

    /**
     * Atualiza a lista de navegação localizada em top.jsp.
     */
    function updateHistory() {
      // nossos objetos
      var frameContent = document.getElementById("frameContent");
      // título e caminho da página atual
      var documentTitle    = frameContent.contentWindow.document.title;
      var documentLocation = frameContent.contentWindow.location.href;
      // se é uma Exceção ou não tem título...dispara
      if ((documentTitle == "Exceção") || (documentTitle == ""))
        return;
      // nossa lista de navegação
      var history = document.getElementById("history");
      // se não achamos a lista de navegação...dispara
      if (history == null)
        return;
      // procura pelo item na lista
      for (i=0; i<history.options.length; i++) {
        // se já existe na lista...
        if (history.options[i].text == documentTitle) {
          // seleciona-o
          history.selectedIndex = i;
          // dispara
          return;
        } // if
      } // for
      // se chegou até aqui...não encontramos a página na lista
      var oOption = document.createElement("option");
      oOption.text = documentTitle;
      oOption.value = documentLocation;
      // adiciona e seleciona
      history.add(oOption);
      history.selectedIndex = history.options.length-1;
      // se já tem mais de 10 itens...remove o primeiro
      if (history.options.length > 10)
        history.options.remove(0);
    }

  </script>

  <body class="ApplicationTop" style="margin:0px; overflow:hidden;" onselectstart="return false;" oncontextmenu="return false;">
    <%--
    <!-- estrutra para windowOpen - substituto para o Popup -->
    <div id="modalBackground" onclick="windowClose();" style="position:absolute; z-index:9000; background-color:#000; display:none; vertical-align:middle;"><table style="width:100%;height:100%;"><tr><td style="text-align:center;"><img id="modalWait" src="images/wait.gif" style="position:absolute; visibility:hidden; z-index:9001; width:32px; height:32px;" /></td></tr></table></div>
    <table id="modalWindow" class="frameBody" cellpadding="2" cellspacing="0" style="position:absolute; z-index:9999; display:none; box-shadow:0px 25px 35px 15px #444444;">
      <tr>
        <td id="modalWindowCaption" class="frameCaption" style="width:auto; height:25px;">
          Título
        </td>
        <td id="modalWindowCaption" class="frameCaption" style="width:24px; border-left:none;">
          <%=Button.script(facade, "modalButtonClose", "", "Fechar", ImageList.IMAGE_NO, "", Button.KIND_TOOLBUTTON, "width:24px; height:24px;", "windowClose();", false)%>
        </td>
      </tr>
      <tr>
        <td colspan="2" class="frameBody" style="height:auto;">
          <div style="width:100%; height:100%; overflow:auto;">
            <iframe id="modalWindowFrame" onload="windowFrameLoad();" style="width:100%; height:100%;" scrolling="no" frameborder="0"></iframe>
          </div>
        </td>
      </tr>
    </table>
    --%>
    <!-- Ribbon e FrameContent -->
    <table cellpadding="0" cellspacing="0" style="width:100%; height:100%;">
      <tr>
        <td style="height:118px;">

          <table cellpadding="0" cellspacing="0" style="position:fixed; top:0px; left:0px; width:100%; height:118px;">
            <tr>
              <!-- Master Relation -->
              <%if (facade.masterRelationInformation().getActive()) {
                  ParamList masterRelationList = getMasterRelationList(facade);
                  String masterRelationDescription = (facade.masterRelation().isEmpty() ? "(vazio)" : StringTools.arrayStringToString(facade.masterRelation().getUserValues(), "; "));%>
              <td style="width:77px;">
                <div class=ribbonTabBar></div>
                <div class=ribbonContainer>
                    <div style="float:left;">
                    <%=ribbon.beginTabPanel(facade.masterRelationInformation().getCaption())%>
                      <%=ribbon.button("buttonMasterRelation", "<font id=\"spanMasterRelation\">" + StringTools.toCaptalCase(masterRelationDescription, true) + "</font>", "", "images/masterrelation32x32.png", "", "", "", popupMenuMasterRelation, Ribbon.KIND_BIG_BUTTON, false)%>
                        <%=popupMenuMasterRelation.begin()%>
                          <%=menuItemsMasterRelation(masterRelationList)%>
                        <%=popupMenuMasterRelation.end()%>
                    <%=ribbon.endTabPanel()%>
                    </div>
                </div>
              </td>
              <%} // if%>
              <td style="width:auto;">
          
                <!-- Ribbon -->
                <%=ribbon.begin()%>
                  <!-- Read Only -->
                  <%if (facade.connectionManager().connectionFiles().get(facade.getDefaultConnectionName()).readOnly()) {%>
                    <%=ribbon.beginTabPanel("Aviso", 0, Ribbon.ALIGN_RIGHT)%>
                      <%=ribbon.button("buttonReadOnly", "Somente leitura", "Saiba o que significa o modo somente leitura.", "images/warning32x32.png", "", "", "alert('A aplicação está configurada no modo somente leitura. Neste modo, as operações de inclusão, alteração e exclusão de registros, bem como a execução de processos, não são permitidas. Apesar disto, todos os comandos continuam habilitados de acordo com seus direitos de usuário e os dados disponíveis para consulta.');", null, Ribbon.KIND_BIG_BUTTON, false)%>
                    <%=ribbon.endTabPanel()%>
                  <%}%>
                  <!-- início -->
                  <%=ribbon.beginTab("Início")%>
                    <%=ribbon.beginTabPanel("Principal")%>
                      <%=ribbon.button("buttonMyDesktop", "Área de Trabalho", "Exibe a sua área de trabalho.", "images/mydesktop32x32.png", "", "", "showFrameDesktop();", null, Ribbon.KIND_BIG_BUTTON, false)%>
                      <%Action actionCockpit = facade.actionList().get("cockpit", false);
                        if (actionCockpit != null) {%>
                        <%=ribbon.button("buttonCockpit", actionCockpit.getCaption(), actionCockpit.getDescription(), "images/cockpit32x32.png", "", "", "showAction('cockpit');", null, Ribbon.KIND_BIG_BUTTON, false)%>
                      <%}%>
                      <%Action[] centralPointActions = facade.actionList().getCentralPoint();
                        if (centralPointActions.length > 0 && !facade.getBrowserTablet()) {%>
                      <%=ribbon.button("buttonCentralPoints", "Centrais", "", "images/centralpoint32x32.png", "", "", "", popupMenuCentralPoints, Ribbon.KIND_BIG_BUTTON, false)%>
                          <%=popupMenuCentralPoints.begin()%>
                            <table cellspacing="0" cellpadding="2">
                              <%for (int i=0; i<centralPointActions.length; i++) {
                                  Action action = centralPointActions[i];%>
                              <tr>
                                <td style="height:20px;"><%=menuItem(facade, action)%></td>
                              </tr>
                              <%}%>
                            </table>
                          <%=popupMenuCentralPoints.end()%>
                      <%}%>
                      <%if (facade.flowChartManager().flowChartFiles().size() > 0) {%>
                      <%=ribbon.button("buttonFlowCharts", "Rotinas do Sistema", "", "images/flowchart32x32.png", "", "", "", popupMenuFlowCharts, Ribbon.KIND_BIG_BUTTON, false)%>
                          <%=popupMenuFlowCharts.begin()%>
                            <table cellspacing="0" cellpadding="2">
                              <%for (int i=0; i<facade.flowChartManager().flowChartFiles().size(); i++) {
                                  FlowChartFile flowChartFile = facade.flowChartManager().flowChartFiles().get(i);
                                  String name = facade.flowChartManager().flowChartFiles().getName(i);%>
                              <tr>
                                <td style="height:20px;"><img src="images/flowchart16x16.png" align="absmiddle">&nbsp;<a href="<%=Controller.ACTION_FLOW_CHART.url("flowChartName=" + name)%>" target="frameContent" title="<%=flowChartFile.information().getDescription()%>"><%=flowChartFile.information().getCaption()%></a></td>
                              </tr>
                              <%}%>
                            </table>
                          <%=popupMenuFlowCharts.end()%>
                      <%} // if%>
                      <%--
                      <%=ribbon.button("buttonFavoritos", "Favoritos", "", "images/favorites32x32.png", "", "", "", popupMenuFavoritos, Ribbon.KIND_BIG_BUTTON, false)%>
                        <%=popupMenuFavoritos.begin()%>
                          <%=menuItemsFavorites(facade)%>
                        <%=popupMenuFavoritos.end()%>
                      --%>
                      <%if (!facade.getBrowserTablet()) {%>
                      <div style="float:left; vertical-align:top; width:100px; height:64px;">
                        <%=ribbon.button("buttonStyle", "Cores e Estilos", "Exibe os esquemas de cores da aplicação.", "images/style16x16.png", "", "", "changeStyle();", null, Ribbon.KIND_TOOL_BUTTON, false)%>
                        <%=ribbon.button("buttonHelp", "Ajuda", "Exibe a ajuda e as novidades da aplicação.", "images/help16x16.png", "", "", "showHelp();", null, Ribbon.KIND_TOOL_BUTTON, false)%>
                      </div>
                      <%} // if%>
                    <%=ribbon.endTabPanel()%>
                    <!-- Ferramentas -->
                    <%=ribbon.beginTabPanel("Ferramentas")%>
                      <%=ribbon.button("buttonUser", facade.getLoggedUser().getName(), "Altera sua senha de usuário.", "images/user32x32.png", "", "", (facade.getLoggedUser().getCannotChangePassword() ? "alert('Sua senha de usuário não pode ser alterada.');" : !facade.getBrowserTablet() ? "changePassword();" : ""), null, Ribbon.KIND_BIG_BUTTON, false)%>
                      <%if (((facade.getLoggedUser() != null) && facade.getLoggedUser().getSuperUser()) ||
                             facade.applicationSystemInformation().getShowToAllUsers() ||
                            (facade.applicationSystemInformation().getShowToPrivilegedUsers() && (facade.getLoggedUser() != null) && facade.getLoggedUser().getPrivileged())) {%>
                        <%=ribbon.button("buttonSystemInformation", "Informações do Sistema", "Exibe as informações e configurações do sistema.", "images/systeminformation32x32.png", "", "", "showAction('" + Controller.ACTION_SYSTEM_INFORMATION.getName() + "');", null, Ribbon.KIND_BIG_BUTTON, false)%>
                      <%}%>
                      <%if ((facade.getLoggedUser() != null) && facade.getLoggedUser().getPrivileged() && (facade.securityService().getSecurityServiceAction() != null) && !facade.getBrowserTablet()) {%>
                        <%=ribbon.button("buttonSecurity", facade.securityService().getSecurityServiceAction().getCaption(), facade.securityService().getSecurityServiceAction().getDescription(), "images/securityservice32x32.png", "", "", "showAction('" + facade.securityService().getSecurityServiceAction().getName() + "');", null, Ribbon.KIND_BIG_BUTTON, false)%>
                      <%}%>
                    <%=ribbon.endTabPanel()%>
                    <!-- Quadro de Avisos -->
                    <%if (showNoticeBoard) {%>
                      <%=ribbon.beginTabPanel("Quadro de Avisos", 210, Align.ALIGN_LEFT)%>
                        <div id="divNoticeBoard" style="width:100%; height:100%; overflow:auto;">
                          <table>
                            <%for (int i=0; i<facade.noticeBoard().size(); i++) {
                                NoticeBoardFile.Notice notice = facade.noticeBoard().get(i);
                                if (!notice.isVisible())
                                  continue;
                                String[] dateParts = DateTools.splitDate(notice.getDate());%>
                            <tr>
                              <td valign="top"><img src="<%=notice.getWarning() ? "images/warning16x16.png" : "images/information16x16.png"%>" align="absmiddle" alt=""></td>
                              <td valign="top"><%=dateParts[0] + "/" + dateParts[1]%></td>
                              <td valign="top"><a href="javascript:showNoticeBoard(<%=i%>);" title="Mostra o conteúdo do aviso."><%=notice.getTitle()%></a></td>
                            </tr>
                            <%}%>
                          </table>
                        </div>
                      <%=ribbon.endTabPanel()%>
                    <%}%>
                    <!-- Sair -->
                    <%=ribbon.beginTabPanel("Sair")%>
                      <%=ribbon.button("buttonLogoff", "Efetuar Logoff", "Salva as preferências e fecha a aplicação com segurança.", "images/logoff32x32.png", "", "", "logoff();", null, Ribbon.KIND_BIG_BUTTON, false)%>
                    <%=ribbon.endTabPanel()%>
                  <%=ribbon.endTab()%>
                  <!-- módulos -->
                  <%// módulos existentes
                    ActionList.Module[] moduleList = facade.actionList().getModuleList();
                    // loop nos módulos
                    for (int i=0; i<moduleList.length; i++) {
                      // módulo da vez
                      ActionList.Module module = moduleList[i];
                      // se não temos actions visíveis...continua
                      if (!hasVisibleActions(facade, module))
                        continue;%>
                    <%=ribbon.beginTab(module.getName() + (module.getHasJustReleasedActions() ? "&nbsp;<img alt='Novidades, consulte a Ajuda.' src='images/release16x16.png' align='absmiddle'>" : ""))%>
                      <%=ribbonTabPanel(facade, ribbon, module, Action.CATEGORY_NONE,  "Centrais")%>
                      <%=ribbonTabPanel(facade, ribbon, module, Action.CATEGORY_ENTITY,  "Cadastros")%>
                      <%=ribbonTabPanel(facade, ribbon, module, Action.CATEGORY_PROCESS, "Processos")%>
                      <%=ribbonTabPanel(facade, ribbon, module, Action.CATEGORY_REPORT,  "Relatórios")%>
                      <%// se não é um Tablet...
                        if (!Controller.isTabletRequest(request)) {%>
                        <%// FAQs do módulo
                          FAQList faqList = facade.helpManager().faqList().getModuleFAQList(module.getName());
                          // se temos FAQs...
                          if ((faqList != null) && (faqList.size() > 0)) {
                          PopupMenu popupMenuFaq = new PopupMenu("popupMenuFAQ" + i, "buttonFAQ" + i);%>
                          <%=ribbon.beginTabPanel("Ajuda")%>
                            <%=ribbon.button("buttonFAQ" + i, "Perguntas Frequentes", "", "images/help32x32.png", "", "", "", popupMenuFaq, Button.KIND_BIG, false)%>
                          <%=ribbon.endTabPanel()%>
                          <%=popupMenuFaq.begin()%>
                            <%=menuItemsFAQ(faqList)%>
                          <%=popupMenuFaq.end()%>
                        <%} // if%>
                      <%} // if%>
                    <%=ribbon.endTab()%>
                  <%} // for%>
                <%=ribbon.end()%>

              </td>
            </tr>
          </table>
          
        </td>
      </tr>
      <tr>
        <td style="height:auto;">

          <!-- FrameDesktop -->
          <iframe id="frameDesktop" name="frameDesktop" frameborder="0" scrolling="no" src="controller?action=<%=Controller.ACTION_MY_DESKTOP.getName()%>" style="width:100%; height:100%;"></iframe>
          <!-- FrameContent -->
          <iframe id="frameContent" name="frameContent" frameborder="0" scrolling="no" src="blank.jsp" onload="if (contentWindow.location.href.indexOf('blank.jsp') < 0) showFrameContent();" style="width:100%; height:100%; display:none;"></iframe>

        </td>
      </tr>
    </table>

    <%// se temos quadro de avisos...
      if (showNoticeBoard && 
          (facade.noticeBoard().size() > 0) && 
          (facade.noticeBoard().get(0).getDate().compareTo(DateTools.getActualDate()) <= 0) &&
          (facade.noticeBoard().get(0).getEmphasize().compareTo(DateTools.getActualDate()) >= 0) ) {
        Balloon balloonNoticeBoard = new Balloon(facade, request, "balloonNoticeBoard");%>
        <%=balloonNoticeBoard.standAlone(ImageList.IMAGE_INFORMATION,
                                         "Quadro de Avisos",
                                         "Existem novos avisos para serem lidos por você.",
                                         "divNoticeBoard",
                                         "",
                                         true,
                                         facade.noticeBoard().size() + "")%>
    <%} // if%>

    <script type="text/javascript">
      // determina se é um dos browsers suportados
      var browserOK      = (BrowserDetect.browser == "MSIE" && BrowserDetect.version >= 7 && BrowserDetect.version <= 9) || // MSIE 7, 8 e 9
                           (BrowserDetect.browser == "Safari" && BrowserDetect.version >= 6) ||  // Safari 6 ou superior
                           (BrowserDetect.browser == "Chrome" && BrowserDetect.version >= 20) || // Chrome 20 ou superior
                           (navigator.userAgent.toUpperCase().indexOf("MOBILE") > 0) ||          // Qualquer Mobile (somente visualização)
                           (navigator.userAgent.toUpperCase().indexOf("ANDROID") > 0);           // Qualquer Android (somente visualização)
      if (!browserOK)
        alert("ATENÇÃO - O seu navegador (" + BrowserDetect.browser + " " + BrowserDetect.version + " para " + BrowserDetect.OS + ") não é compatível com o <%=facade.applicationInformation().getName()%>. Para evitar a perda de dados, é altamente recomendada a utilização de um dos seguintes navegadores:\r\n"
            + "\r\n- Internet Explorer 7, 8 ou 9 para Windows."
            + "\r\n- Safari 6 ou superior para Windows ou Mac;"
            + "\r\n- Chrome 20 ou superior para Windows ou Mac.");
    </script>

    <%// se a relação mestre está ativa...
      if (facade.masterRelationInformation().getActive()) {%>

        <script type="text/javascript">
          function changeMasterRelation(value, userValue) {
            // forma os parâmetros
            var paramMasterRelationValue     = "<%=MASTER_RELATION%>=" + escape(value);
            var paramMasterRelationUserValue = "<%=MASTER_RELATION_USER%>=" + escape(userValue);
            // altera a relação mestre
            <%=ajaxChangeMasterRelation.request(new String[]{"paramMasterRelationValue", "paramMasterRelationUserValue"}, "changeMasterRelationCallback")%>
          }

          function changeMasterRelationCallback() {
            // se ainda não temos nada...dispara
            if (!<%=ajaxChangeMasterRelation.isResponseReady()%>)
              return;
            // se ocorreu tudo OK...
            if (<%=ajaxChangeMasterRelation.isResponseStatusOK()%>) {
              // mostra o novo valor da relação mestre...
              document.getElementById("spanMasterRelation").innerHTML = <%=ajaxChangeMasterRelation.responseText()%>;
              // nossos objetos
              var frameContent = document.getElementById("frameContent");
              var frameDesktop = document.getElementById("frameDesktop");
              // se estamos mostrando a área de trabalho...
              if (frameContent.style.display == "none") {
                // força atualização da área de trabalho
                frameDesktopForceUpdate();
                // msotra a área de trabalho
                showFrameDesktop();
                // remove o conteúdo de frameContent
                frameContent.contentWindow.location.href = "blank.jsp";
              }
              // se estamos mostrando o frameContent...
              else {
                // força atualização da área de trabalho
                frameDesktopForceUpdate();
                // atualiza o frameContent
                frameContent.contentWindow.location.reload();
              } // if
            }
            // se houve um erro...mostra
            else
              alert(<%=ajaxChangeMasterRelation.responseErrorMessage()%>);
          }

          <%if (facade.getLoggedUser().getChangePassword()) {%>
            changePassword();
          <%} // if%>
        </script>
    <%} // if%>
  <%--
    <%=balloon.scriptBegin(true, "balloonHome")%>
    <%=balloon.scriptStep("images/mydesktop32x32.png", "Área de Trabalho", "Esta é a área de trabalho da aplicação. Aqui você<br>poderá visualizar rapidamente todas as informações<br>relevantes a sua rotina diária.", "", "")%>
    <%=balloon.scriptStep("images/mydesktop32x32.png", "Personalizar", "Clique em personalizar para escolher os cartões<br>que deseja visualizar na sua área de trabalho.", "frameDesktop.buttonPersonalizar", "")%>
    <%=balloon.scriptStep("", "Faixa de Acesso", "A faixa de acesso exibe todas as funções que você<br>possui direito de acesso, onde cada aba é um módulo.", "ribbon", "")%>
    <%if (facade.masterRelationInformation().getActive()) {%>
      <%=balloon.scriptStep("images/masterrelation32x32.png", facade.masterRelationInformation().getCaption(), "Todos os dados exibidos e operações realizadas são<br>referentes a(o) " + facade.masterRelationInformation().getCaption() + " selecionado(a). Para operar<br>com outro(a) " + facade.masterRelationInformation().getCaption() + ", basta selecioná-lo(a) a<br>qualquer momento.", "buttonMasterRelation", "")%>
    <%} // if%>
    <%=balloon.scriptStep("images/mydesktop32x32.png", "Área de Trabalho", "Clique aqui para voltar para sua área de trabalho.", "buttonMyDesktop", "")%>
    <%if (actionCockpit != null) {%>
      <%=balloon.scriptStep("images/cockpit32x32.png", "Cockpit", "Clique aqui para visualizar o cockpit<br>empresarial com indicadores de desempenho.", "buttonCockpit", "")%>
    <%} // if%>
    <%if (!facade.getBrowserTablet()) {%>
      <%=balloon.scriptStep("images/style32x32.png", "Cores e estilos", "Clique aqui para alterar o esquema de cores da aplicação.", "buttonStyle", "")%>
      <%=balloon.scriptStep("images/help32x32.png", "Ajuda", "Clique aqui para visualizar as perguntas<br>frequentes e novidades da aplicação.", "buttonHelp", "")%>
    <%} // if%>
    <%=balloon.scriptStep("images/user32x32.png", "Alterar senha", "Clique aqui para alterar a sua senha de<br>acesso a aplicação.", "buttonUser", "")%>
    <%if (((facade.getLoggedUser() != null) && facade.getLoggedUser().getSuperUser()) ||
           facade.applicationSystemInformation().getShowToAllUsers() ||
          (facade.applicationSystemInformation().getShowToPrivilegedUsers() && (facade.getLoggedUser() != null) && facade.getLoggedUser().getPrivileged())) {%>
      <%=balloon.scriptStep("images/systeminformation32x32.png", "Informações do sistema", "Clique aqui para alterar a sua senha de<br>acesso a aplicação.", "buttonSystemInformation", "")%>
    <%} // if%>
    <%if ((facade.getLoggedUser() != null) && facade.getLoggedUser().getPrivileged() && (facade.securityService().getSecurityServiceAction() != null) && !facade.getBrowserTablet()) {%>
      <%=balloon.scriptStep("images/securityservice32x32.png", "Segurança", "Clique aqui para alterar a sua senha de<br>acesso a aplicação.", "buttonSecurity", "")%>
    <%} // if%>
    <%=balloon.scriptEnd()%>
    --%>
  </body>

</html>

<%}
  catch (Exception e) {
    e.printStackTrace();
    Controller.forwardException(e, pageContext);
  } // try-catch
%>