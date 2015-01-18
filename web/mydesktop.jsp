
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

<%!
  String CARDS          = "cards";
  String CARDS_PER_LINE = "cardsPerLine";

  /**
   * Retorna o Param que cont�m a lista de Cards exibidos.
   */
  public Param getParamCards(Facade facade) {
    // pega a lista atual da fachada
    Param paramCards = facade.userParamList().get(CARDS);
    // se n�o achamos...cria
    if (paramCards == null) {
      paramCards = new Param(CARDS, "");
      facade.userParamList().add(paramCards);
    } // if
    // retorna
    return paramCards;
  }
%>

<%
  // obt�m os cards adicionados
  Param paramCards = getParamCards(facade);
  String[] cards = (!paramCards.getValue().equals("") ? paramCards.getValue().split(";") : new String[]{});
  // cards por linha
  Param paramCardsPerLine = facade.userParamList().get(CARDS_PER_LINE);
  int cardsPerLine = (paramCardsPerLine != null && !facade.getBrowserTablet() ? NumberTools.parseInt(paramCardsPerLine.getValue()) : 2);
%>

<html>
  <head>
    <title><%=facade.applicationInformation().getName()%></title>
    <link href="<%=facade.getStyle().userInterface()%>" rel="stylesheet" type="text/css">
    <link href="<%=facade.iconURL()%>" rel="shortcut icon" />
    <script src="include/scripts.js" type="text/javascript"></script>
  </head>

  <style type="text/css">
    
    .card {
      float:  left;
      margin: 2px;
      width:  49.6%;
    }
    
  </style>

  <script type="text/javascript">

    var forceUpdate = false;
    var lastUpdate  = new Date();
    
    function showUpdateTime() {
      // mostra a hora de atualiza��o
      var lastUpdateTime = document.getElementById("lastUpdateTime");
      // se n�o temos cards...dispara
      if (lastUpdateTime == null)
        return;
      // atualiza a hora
      lastUpdateTime.innerHTML = "Atualizado em " + lastUpdate.getHours() + ":" + (lastUpdate.getMinutes() < 10 ? "0" : "") + lastUpdate.getMinutes();
    }

    function updateCards() {
      // se n�o devemos for�ar e atualizou h� menos de 10 min...dispara
      var tenMinutesAgo = new Date();
      tenMinutesAgo.setMinutes(tenMinutesAgo.getMinutes()-10, tenMinutesAgo.getSeconds(), tenMinutesAgo.getMilliseconds());
      if (!forceUpdate && (lastUpdate > tenMinutesAgo))
        return;
      // guarda �ltima atualiza��o
      lastUpdate = new Date();
      // nossos cards
      var cards = document.getElementsByName("card");
      // se n�o temos cards...dispara
      if (cards == null)
        return;
      // atualiza todos
      for (var i=0; i<cards.length; i++) {
        cards[i].contentWindow.location.reload();
      } // for
      // j� atualizamos
      forceUpdate = false;
      // mostra a hora da atualiza��o
      showUpdateTime();
    }

  </script>

  <body class="FrameBar" style="margin:4px;" onselectstart="return false;" oncontextmenu="return false;" onload="showUpdateTime();">

    <!-- tabela organizadora -->
    <table cellpadding="0" cellspacing="0" style="width:100%; height:100%;">
      <tr>
        <td style="height:54px;">

          <!-- logo e comandos -->
          <table cellpadding="0" cellspacing="0" style="width:100%;">
            <tr>
              <td style="width:50%;">

                <!-- logo -->
                <%// se temos uma logo para a aplica��o...
                  if (!facade.applicationLogo().getFileName().equals("")) {
                    // tamanho da logo
                    int logoWidth = facade.applicationLogo().getWidth();
                    int logoHeight = facade.applicationLogo().getHeight();
                    // raz�o entre a altura de exibi��o (48 pixels) com a altura da imagem
                    int logoHeightProportion = 48 * 100 / logoHeight;
                    int newLogoWidth = logoWidth * logoHeightProportion / 100;
                %>
                  <img src="<%=facade.logoURL()%>" height="48" width="<%=newLogoWidth%>" align="absmiddle" alt=""><%if (!facade.applicationInformation().getTitle().equals("")) {%><span class="BackgroundText" style="font-size:30pt;">&nbsp;<%=facade.applicationInformation().getTitle()%></span><%} // if%>
                <%}
                  // se n�o temos uma logo...usa a padr�o
                  else {%>
                  <img src="images/iobjects.png" width="48" height="48" align="absmiddle" alt=""><span class="BackgroundText" style="font-size:30pt;">&nbsp;<%=facade.applicationInformation().getTitle()%></span>
                <%} // if%>

              </td>
              <td align="right" style="width:50%;">

                <!-- bot�es -->
                <%// actions dos cart�es para serem adicionados
                  Action[] cardActions = facade.actionList().getCard();
                  // actions que j� foram adicionados
                  String addedCards = getParamCards(facade).getValue() + ";";
                  // se temos cart�es dispon�veis...
                  if (cardActions.length > 0) {
                      // temos algum cart�o rec�m lan�ado?
                      boolean hasJustReleased = false;
                      // loop nos actions
                      for (int i=0; i<cardActions.length; i++) {
                        // � rec�m lan�ado?
                        hasJustReleased = cardActions[i].getJustReleased();
                        if (hasJustReleased)
                          break;
                      } // for%>
                    <%if (!facade.getBrowserTablet()) {%>
                    <%=Button.script(facade, "buttonPersonalizar", "Personalizar", "Personaliza as informa��es exibidas na sua �rea de trabalho.", "images/card" + (hasJustReleased ? "release" : "") + "16x16.png", "", Button.KIND_DEFAULT, "width:135px;", Popup.script(Controller.ACTION_MY_DESKTOP_CUSTOMIZE.url(), "customize", 360, 580, Popup.POSITION_CENTER), false)%>&nbsp;&nbsp;&nbsp;
                    <%} // if%>
                    <%=Button.script(facade, "buttonAtualizar", "<span id=\"lastUpdateTime\">Atualizar</span>", "Atualiza todos os cart�es exibidos.", "images/history16x16.png", "", Button.KIND_DEFAULT, "width:" + (!facade.getBrowserTablet() ? 135 : 165) + "px;", "forceUpdate=true;updateCards();", false)%>
                <%} // if%>

              </td>
            </tr>
          </table>

        </td>
      </tr>
      <tr>
        <td id="cardContainer" style="height:auto;">
          &nbsp;
        </td>
      </tr>
    </table>

    <script type="text/javascript">
      var cards        = new Array();
      var cardsPerLine = <%=cardsPerLine%>;

      function addCard(caption, action) {
        cards[cards.length] = [caption, action];
      }

      function build() {
        // nosso container
        var cardContainer = document.getElementById("cardContainer");
        // altura dispon�vel
        var cardHeight = (cardContainer.offsetHeight - 14) / 3;
        // nosso HTML
        var html = "";
        html += "<div style=\"width:100%; height:100%; overflow:auto;\">";
        html +=   "<table cellpadding=2 cellspacing=0 style=\"width:100%;\">";
        // loop nos cards
        var cardsInLine = 0;
        for (var i=0; i<cards.length; i++) {
          if (cardsInLine == 0) {
            html += "<tr>";
            html +=   "<td>";
          }
          else {
            html += "<td>";
          } // if

          html += "<table id=\"dummy" + i + "\" cellpadding=2 cellspacing=0 style=\"width:100%; height:" + cardHeight +"px; tableLayout:fixed;\" >";
          html +=   "<tr>";
          html +=     "<td class=\"frameCaption\" style=\"height:25px;\">";
          html +=       cards[i][0];
          html +=     "</td>";
          html +=   "</tr>";
          html +=   "<tr>";
          html +=     "<td class=\"frameBody\" style=\"height:" + (cardHeight - 25 - 4) +";\">";
          html +=     "<div style=width:100%;height:100%;overflow:auto;>";
          html +=       "<iframe name=\"card\" src=\"controller?action=" + cards[i][1] + "\" style=\"width:100%; height:100%;\" scrolling=\"auto\" frameborder=\"0\"></iframe>";
          html +=     "</div>";
          html +=     "</td>";
          html +=   "</tr>";
          html += "</table>";

          cardsInLine++;
          if (cardsInLine == cardsPerLine) {
            cardsInLine = 0;
            html += "</td>";
            html += "</tr>";
          }
          // se � mais um card na linha...
          else {
            html += "</td>";
          } // if

        } // for
        html +=   "</table>";
        html += "</div>";
        // escreve tudo
        cardContainer.innerHTML = html;
      }

      <%// loop nos cards
        int cardsCount = cards.length;
        if (cardsCount > 6 && cardsPerLine == 2)
          cardsCount = 6;
        if (cardsCount > 9 && cardsPerLine == 3)
          cardsCount = 9;
        for (int i=0; i < cardsCount; i++) {
          // Action da vez
          String card = cards[i];
          Action action = facade.actionList().get(card, false);
          // se n�o existe...continua
          if (action == null)
            continue;%>
          addCard("<%=action.getCaption()%>", "<%=action.getName()%>");
      <%} // for%>
      // constr�i
      build();

    </script>

    <%--
    <!-- Advertising -->
    <%if (facade.advertising().mediaList().length > 0) {%>
    <iframe src="advertising.jsp" style="width:700px; height:100px;" scrolling="no" frameborder="0"></iframe>
    <%} // if%>
    --%>
  </body>

</html>
