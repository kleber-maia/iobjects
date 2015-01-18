
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
<%@include file="../include/beans.jsp"%>

<%@page import="java.util.*"%>
<%@page import="java.sql.*"%>

<%@page import="iobjects.xml.ui.*"%>

<%!
  String CARDS                = "cards";
  String LOGOFF               = "logoff";
  String MASTER_RELATION      = "masterRelation";
  String MASTER_RELATION_USER = "masterRelationUser";

   String COMMAND_AJAX_CHANGE_MASTER_RELATION = "changeMasterRelation";

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
   * Retorna o Param que contém a lista de Cards exibidos.
   */
  public Param getParamCards(Facade facade) {
    // pega a lista atual da fachada
    Param paramCards = facade.userParamList().get(CARDS);
    // se não achamos...cria
    if (paramCards == null) {
      paramCards = new Param(CARDS, "");
      facade.userParamList().add(paramCards);
    } // if
    // retorna
    return paramCards;
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
      result.append("<tr><td style=\"height:32px;\"><button class=\"iPhoneMenu" + (i == 0 ? "First" : "") + "\" onclick=\"changeMasterRelation('" + masterRelationItem.getValue() + "', '" + masterRelationItem.getName() + "');\" onclick=\"\" style=\"width:100%; height:32px; padding-left:12px; padding-right:12px;\">" + masterRelationItem.getName() + "</button></td></tr>");
    } // while
    // se não temos nada...
    if (result.equals(""))
      result.append("<tr><td style=\"height:32px;\"><img src=\"images/error16x16.png\" align=\"absmiddle\">&nbsp;(vazio)</td></tr>");
    // retorna
    return "<table cellspacing=0 cellpadding=0>"
             + result.toString()
         + "</table>";
  }
%>

<%-- ---------------------- --%>
<%-- Comandos Pré-Definidos --%>
<%-- ---------------------- --%>

<%
  // se quer mudar a relação mestre...
  if (Controller.getCommand(request).equals(COMMAND_AJAX_CHANGE_MASTER_RELATION)) {
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

%>

<%-- -------------------- --%>
<%-- Processamento Normal --%>
<%-- -------------------- --%>

<%
  try {
    // nossos métodos Ajax
    Ajax ajaxChangeMasterRelation = new Ajax(facade, "ajaxChangeMasterRelation", Controller.ACTION_HOME_MOBILE.getName(), COMMAND_AJAX_CHANGE_MASTER_RELATION);
    // nosso Ribbon
    Ribbon ribbon = new Ribbon(facade, "ribbon");
    // nossos PopupMenus
    PopupMenu popupMenuMasterRelation = new PopupMenu("popupMenuMasterRelation", "buttonMasterRelation");

    // obtém os cards adicionados
    Param paramCards = getParamCards(facade);
    String[] cards = (!paramCards.getValue().equals("") ? paramCards.getValue().split(";") : new String[]{});

    // card para exibir
    String card = Controller.getParameter(request, "card");
%>

<html>
  <head>
    <title><%=facade.applicationInformation().getName()%></title>
    <link href="<%=facade.getStyle().userInterface()%>" rel="stylesheet" type="text/css">
    <link href="<%=facade.iconURL()%>" rel="shortcut icon" />
    <script src="include/scripts.js" type="text/javascript"></script>
    <!-- iPod/iPhone -->
    <meta name="apple-mobile-web-app-capable" content="yes" />
    <meta name="apple-mobile-web-app-status-bar-style" content=" black" />
    <meta name="viewport" content="initial-scale=1; maximum-scale=1; user-scalable=0;" />
    <meta name="apple-touch-fullscreen" content="yes" />
    <link rel="apple-touch-icon" href="<%=facade.iconURL()%>" />
    <style type="text/css">
      body, div, table, td, p, spam, button, h1, h2, h3 {
        font-family:                       Sans-Serif, Serif;
        font-size:                         Small;
      }
      .iphoneButton {
        background-color:                  #f7f7f7;
        border-color:                      silver;
        border-top:                        none;
        padding:                           8px;
        text-align:                        left;
        -webkit-border-radius:             0px;
      }
      .iphoneButtonFirst {
        background-color:                  #f7f7f7;
        border-color:                      silver;
        padding:                           8px;
        text-align:                        left;
        -webkit-border-top-left-radius:    16px;
        -webkit-border-top-right-radius:   16px;
      }
      .iphoneButtonLast {
        background-color:                  #f7f7f7;
        border-color:                      silver;
        border-top:                         none;
        padding:                            8px;
        text-align:                         left;
        -webkit-border-bottom-left-radius:  16px;
        -webkit-border-bottom-right-radius: 16px;
      }
      .iphonePopupMenu {
        background-color:                  #f7f7f7;
        -webkit-box-shadow:                8px 8px 15px gray;
        -webkit-border-radius:             8px;
      }
      .iphoneMenu {
        background-color:                  #f7f7f7;
        border:                            none;
        border-top:                        1px solid silver;
        padding:                           8px;
        text-align:                        center;
        -webkit-border-radius:             0px;
      }
      .iphoneMenuFirst {
        background-color:                  #f7f7f7;
        border:                            none;
        padding:                           8px;
        text-align:                        center;
        -webkit-border-radius:             0px;
      }
    </style>
  </head>

  <script type="text/javascript">
    // variável que controla se o usuário efetuou logoff
    var logedOff = false;

    <%// se a relação mestre está ativa...
      if (facade.masterRelationInformation().getActive()) {%>
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
          // oculta o popup menu
          popupMenuHide("popupMenuMasterRelation");
          // mostra o novo valor da relação mestre...
          document.getElementById("spanMasterRelation").innerHTML = <%=ajaxChangeMasterRelation.responseText()%>;
          
          // enquanto não achamos uma solução melhor para exibição do
          // card, precisamos recarregar tudo para atualizar os dados
          //window.location.reload();
        }
        // se houve um erro...mostra
        else
          alert(<%=ajaxChangeMasterRelation.responseErrorMessage()%>);
      }
    <%}%>

    function logoff() {
      // se o usuário não confirmou...dispara
      if (!confirm("Tem certeza que deseja efetuar logoff?"))
        return;
      // informa nossa janela pai que efetuamos logoff
      logedOff = true;
      // realiza logoff
      window.location.href = "<%=Controller.ACTION_LOGIN.url(LOGOFF + "=true")%>";
    }
  </script>

  <body class="FrameBar" style="margin:0px;" onselectstart="return false;" oncontextmenu="return false;" onload="documentLoad();" onunload="hideAll();">

    <!-- coloca os atributos da tela de login na memória -->
    <div class="login" style="position:absolute; visibility:hidden;"></div>

    <%// se temos um card para exibir
      if (!card.equals("")) {
        // obtém o Action
        Action action = facade.actionList().get(card, false);%>
        
        <!-- cartão -->
        <div class="frameBody" style="width:100%;">
           <%pageContext.include("../" + action.getJsp());%>
        </div>

        <script>
          function documentLoad() {
            document.title = "<%=action.getCaption()%>";
          }
          
          function hideAll() {
          }
        </script>
        
    <%}
      // se não...
      else {%>
    
        <!-- layout -->
        <table id="tableLayout" cellpadding="0" cellspacing="0" style="width:100%; height:100px; table-layout:fixed;">
          <tr id="rowRibbon" >
            <td style="height:118px; overflow:hidden;">
              <div style="position:fixed; top:0px; width:100%; height:100px;">
                <!-- Ribbon -->
                <%=ribbon.begin()%>
                  <!-- início -->
                  <%=ribbon.beginTab("Início")%>
                    <!-- Master Relation -->
                    <%if (facade.masterRelationInformation().getActive()) {
                        ParamList masterRelationList = getMasterRelationList(facade);
                        String masterRelationDescription = (facade.masterRelation().isEmpty() ? "(vazio)" : StringTools.arrayStringToString(facade.masterRelation().getUserValues(), "; "));%>
                      <%=ribbon.beginTabPanel(facade.masterRelationInformation().getCaption())%>
                        <%=ribbon.button("buttonMasterRelation", "<font id=\"spanMasterRelation\">" + StringTools.toCaptalCase(masterRelationDescription, true) + "</font>", "", "images/masterrelation32x32.png", "", "", "", popupMenuMasterRelation, Ribbon.KIND_BIG_BUTTON, false)%>
                          <%=popupMenuMasterRelation.begin()%>
                            <%=menuItemsMasterRelation(masterRelationList)%>
                          <%=popupMenuMasterRelation.end()%>
                      <%=ribbon.endTabPanel()%>
                    <%}%>
                    <%=ribbon.beginTabPanel("Cartões")%>
                      <%=ribbon.button("buttonCards", "Meus cartões", "", "images/card32x32.png", "", "", "build(myCards);", null, Ribbon.KIND_BIG_BUTTON, false)%>
                      <%=ribbon.button("buttonAllCards", "Mostrar todos", "", "images/card32x32.png", "", "", "build(allCards);", null, Ribbon.KIND_BIG_BUTTON, false)%>
                    <%=ribbon.endTabPanel()%>
                    <!-- Sair -->
                    <%=ribbon.beginTabPanel("Sair")%>
                      <%=ribbon.button("buttonLogoff", "Efetuar Logoff", "Salva as preferências e fecha a aplicação com segurança.", "images/logoff32x32.png", "", "", "logoff();", null, Ribbon.KIND_BIG_BUTTON, false)%>
                    <%=ribbon.endTabPanel()%>
                  <%=ribbon.endTab()%>
                <%=ribbon.end()%>
              </div>
            </td>
          </tr>
          <tr>
            <td id="cellLogo">
              <!-- logo -->
              <%// se temos uma logo para a aplicação...
                if (!facade.applicationLogo().getFileName().equals("")) {
                  // tamanho da logo
                  int logoWidth = facade.applicationLogo().getWidth();
                  int logoHeight = facade.applicationLogo().getHeight();
                  // razão entre a altura de exibição (48 pixels) com a altura da imagem
                  int logoHeightProportion = 48 * 100 / logoHeight;
                  int newLogoWidth = logoWidth * logoHeightProportion / 100;
              %>
                <img id="imgLogo" src="<%=facade.logoURL()%>" height="48" width="<%=newLogoWidth%>" style="margin:4px;" align="absmiddle" alt=""><%if (!facade.applicationInformation().getTitle().equals("")) {%><span class="BackgroundText" style="font-size:30pt;">&nbsp;<%=facade.applicationInformation().getTitle()%></span><%} // if%>
              <%}
                // se não temos uma logo...usa a padrão
                else {%>
                <img id="imgLogo" src="images/iobjects.png" width="48" height="48" style="margin:4px;" align="absmiddle" alt=""><span class="BackgroundText" style="font-size:30pt;">&nbsp;<%=facade.applicationInformation().getTitle()%></span>
              <%} // if%>
              <div id="cardContainer" style="display:<%=card.equals("") ? "block" : "none"%>; width:100%;"></div>
            </td>
          </tr>
        </table>

        <script type="text/javascript">
          var allCards         = new Array();
          var myCards          = new Array();
          var actualCards      = null;
          var orientationWidth = document.body.offsetWidth;

          function addAllCard(caption, action) {
            allCards[allCards.length] = [caption, action];
          }

          function addMyCard(caption, action) {
            myCards[myCards.length] = [caption, action];
          }

          function build(cards) {
            // nosso container
            var cardContainer = document.getElementById("cardContainer");
            // mostra os botões
            document.getElementById("cardContainer").style.display = "block";
            // se não mudou os cartões...dispara
            if (actualCards == cards)
              return;
            // guarda os cards utilizados
            actualCards = cards;
            // apaga tudo o que houver
            while (cardContainer.hasChildNodes())
              cardContainer.removeChild(cardContainer.firstChild);
            // nosso HTML
            var html = "<div style=\"width:304px; padding:8px;\">";
            // loop nos cards
            var firstLast   = "";
            for (var i=0; i<cards.length; i++) {
              if (i == 0)
                firstLast = "First";
              else if (i == cards.length-1)
                firstLast = "Last";
              else
                firstLast = "";
              html += "<button class=\"iphoneButton" + firstLast + "\" onclick=\"loadCard('" + cards[i][1] + "','" + cards[i][0] + "');\" style=\"width:100%; height:48px;\"><table style=width:100%;><tr><td style=width:85%;>" + cards[i][0] + "</td><td style=width:15%;text-align:right;><img src='mobile/images/detail.gif'/></td></tr></table></button>";
            } // for
            html += "</div>";
            // escreve tudo
            cardContainer.innerHTML = html;
          }

          function documentLoad() {
            // constroi os cartões
            build(myCards);
            // muda o estilo do popupmenu
            document.getElementById("popupMenuMasterRelation").className = "iPhonePopupMenu";
          }

          function hideAll() {
            document.getElementById("rowRibbon").style.display     = "none";
            document.getElementById("cardContainer").style.display = "none";
            document.getElementById("cellLogo").style.textAlign    = "center";
            document.body.className = "login";
          }

          function loadCard(card, title) {
            // carrega
            window.location.href = "controller?action=home&card=" + card;
          }

          <%// cria a lista de myCards
            for (int i=0; i<cards.length; i++) {
              // Action da vez
              card = cards[i];
              Action action = facade.actionList().get(card, false);
              // se não existe...continua
              if (action == null)
                continue;%>
              addMyCard("<%=action.getCaption()%>", "<%=action.getName()%>");
          <%} // for%>

          <%// cria a lista de allCards
            Action[] actions = facade.actionList().getCard();
            for (int i=0; i<actions.length; i++) {
              // Action da vez
              Action action = actions[i];%>
              addAllCard("<%=action.getCaption()%>", "<%=action.getName()%>");
          <%} // for%>

        </script>
    <%} // if%>

  </body>

</html>

<%}
  catch (Exception e) {
    Controller.forwardException(e, pageContext);
  } // try-catch
%>
