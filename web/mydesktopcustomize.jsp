
<%@include file="include/beans.jsp"%>

<%@page import="java.util.*" %>
<%@page import="java.sql.*" %>

<%@page import="iobjects.card.*"%>

<%!
  String CARDS          = "cards";
  String CARDS_PER_LINE = "cardsPerLine";
%>

<html>
  <head>
    <title><%=facade.applicationInformation().getName()%></title>
    <link href="<%=facade.getStyle().userInterface()%>" rel="stylesheet" type="text/css">
    <link href="<%=facade.iconURL()%>" rel="shortcut icon" />
    <script src="include/scripts.js" type="text/javascript"></script>
  </head>

<%
  // pega a lista atual da fachada
  Param paramCards = facade.userParamList().get(CARDS);
  // pega a quantidade atual da fachada
  Param paramCardsPerLine = facade.userParamList().get(CARDS_PER_LINE);

  // se está salvando...
  if (Controller.getCommand(request).equals("save")) {
    // cartões visíveis
    String[] visiveis = request.getParameterValues("visiveis");
    String cards = (visiveis != null ? StringTools.arrayStringToString(visiveis, ";") : "");
    // salva os nomes dos cartões
    if (paramCards == null)
      facade.userParamList().add(new Param(CARDS, cards));
    else
      paramCards.setValue(cards);
    // cartões por linha
    String cardsPerLine = request.getParameter("porLinha");
    // salva os cartões por linha
    if (paramCardsPerLine == null)
      facade.userParamList().add(new Param(CARDS_PER_LINE, cardsPerLine));
    else
      paramCardsPerLine.setValue(cardsPerLine);
    // atualiza a área de trabalho
    %>
    <script type="text/javascript">
      // atualiza tudo
      window.opener.location.reload();
      // fecha nossa janela
      window.close();
    </script>
    <%
    // dispara
    return;
  } // if

  // organiza em um array
  String[] cards = (paramCards == null ? new String[]{} : !paramCards.getValue().equals("") ? paramCards.getValue().split(";") : new String[]{});
  // quantidade atual
  String cardsPerLine = (paramCardsPerLine == null ? "2" : paramCardsPerLine.getValue());
%>

  <body style="margin:8px;" onselectstart="return false;" oncontextmenu="return true;">

    <script type="text/javascript">

      var formSubmited = false;

      function addCards() {
        // nossos objetos
        var disponiveis = document.getElementById('disponiveis');
        var visiveis    = document.getElementById('visiveis');
        // loop nos itens
        for (var i=disponiveis.options.length-1; i>=0; i--) {
          // se está selecionado...
          if (disponiveis.options[i].selected) {
            // item da vez
            var disponivel = disponiveis.options[i];
            // se já está visível..
            var visivelIndex = getVisivelIndex(disponivel.value);
            if (visivelIndex >= 0) {
              visiveis.options[visivelIndex].selected = true;
              continue;
            } // if
            // adiciona nos visíveis
            var visivel = document.createElement("OPTION");
            visivel.value = disponivel.value;
            visivel.text = disponivel.text;
            visivel.selected = true;
            visiveis.options.add(visivel);
          } // if
        } // for
      }

      function deleteCards() {
        // nossos objetos
        var disponiveis = document.getElementById('disponiveis');
        var visiveis = document.getElementById('visiveis');
        // loop nos itens
        for (var i=visiveis.options.length-1; i>=0; i--) {
          // se está selecionado...
          if (visiveis.options[i].selected) {
            // seleciona
            disponiveis.options[getDisponivelIndex(visiveis.options[i].value)].selected = true;
            // remove
            visiveis.options.remove(i);
          } // if
        } // for
      }

      function getDisponivelIndex(value) {
        // nossos objetos
        var disponiveis = document.getElementById('disponiveis');
        // loop nos itens
        for (var i=disponiveis.options.length-1; i>=0; i--) {
          // se é o que procuramos...retorna
          if (disponiveis.options[i].value == value)
            return i;
        } // for
        // não encontramos
        return -1;
      }

      function getVisivelIndex(value) {
        // nossos objetos
        var visiveis = document.getElementById('visiveis');
        // loop nos itens
        for (var i=visiveis.options.length-1; i>=0; i--) {
          // se é o que procuramos...retorna
          if (visiveis.options[i].value == value)
            return i;
        } // for
        // não encontramos
        return -1;
      }

      function formSubmit() {
        // se já submetemos...dispara
        if (formSubmited) {
          alert("Aguarde o término do processamento. Clique em OK para continuar;");
          return;
        } // if
        // seleciona todos os cartões visíveis
        visiveis = document.getElementById("visiveis");
        for (var i=0; i<visiveis.options.length; i++)
          visiveis.options[i].selected = true;
        // submete
        formSubmited = true;
        formCustomize.submit();
      }

      function moveDown() {
        // lista de visíveis
        var visiveis = document.getElementById("visiveis");
        // move acima
        if ((visiveis.selectedIndex >= 0) && (visiveis.selectedIndex < visiveis.options.length - 1)) {
          var index = visiveis.selectedIndex;
          var o1 = visiveis.options[index];
          var o2 = document.createElement("OPTION");
          o2.value = o1.value;
          o2.text = o1.text;
          visiveis.options.remove(index);
          visiveis.options.add(o2, index+1);
          visiveis.selectedIndex = index+1;
        } // if
      }

      function moveUp() {
        // lista de visíveis
        var visiveis = document.getElementById("visiveis");
        // move acima
        if (visiveis.selectedIndex > 0) {
          var index = visiveis.selectedIndex;
          var o1 = visiveis.options[index];
          var o2 = document.createElement("OPTION");
          o2.value = o1.value;
          o2.text = o1.text;
          visiveis.options.remove(index);
          visiveis.options.add(o2, index-1);
          visiveis.selectedIndex = index-1;
        } // if
      }

    </script>

    <form id="formCustomize" method="post">
      <input type="hidden" name="command" value="save" />
      <table style="width:100%; height:100%;">
        <tr>
          <td align="center" valign="top" style="width:54px; height:auto;">
            <img src="images/mydesktop32x32.png" />
          </td>
          <td valign="top" style="height:auto;">
            <p>
              A lista a seguir contém todos os cartões de informação disponíveis.
              Selecione os cartões que deseja que sejam exibidos na área de trabalho
              e a ordem desejada.
            </p>
            <table style="width:100%; height:200px;">
              <tr>
                <td style="width:45%; height:10px;">Disponíveis</td>
                <td style="width:auto;"></td>
                <td style="width:45%;">Visíveis</td>
              </tr>
              <tr>
                <td style="height:auto;">
                  <select id="disponiveis" name="disponiveis" size="10" multiple ondblclick="addCards();" style="width:100%; height:100%;">
                    <%// loop nos módulos
                      ActionList.Module[] moduleList = facade.actionList().getModuleList();
                      for (int i=0; i<moduleList.length; i++) {
                        // seus cards
                        Vector cardList = moduleList[i].getCardActions();
                        // se não temos nada...continua
                        if (cardList.size() == 0)
                          continue;
                        // adiciona o módulo%>
                        <optgroup label="<%=moduleList[i].getName()%>">
                      <%// loop nos cards
                        for (int w=0; w<cardList.size(); w++) {
                          // action da vez
                          Action action = (Action)cardList.elementAt(w);%>
                          <option value="<%=action.getName()%>" title="<%=action.getDescription()%>"><%=action.getCaption()%><%=action.getJustReleased() ? " (novo)" : ""%></option>
                      <%}%>
                        </optgroup>
                    <%} // for i%>
                  </select>
                </td>
                <td align="center" style="width:auto;">
                  <%=Button.script(facade, "buttonAdicionar", "", "Adiciona os cartões disponíveis selecionados.", "images/commands/right.png", "", Button.KIND_DEFAULT, "width:24px; height:24px;", "addCards();", false)%><br><br>
                  <%=Button.script(facade, "buttonRemover", "", "Remove os cartões visíveis selecionados.", "images/commands/left.png", "", Button.KIND_DEFAULT, "width:24px; height:24px;", "deleteCards();", false)%><br><br><br><br>
                  <%=Button.script(facade, "buttonAcima", "", "Move o cartão selecionado para cima.", "images/commands/up.png", "", Button.KIND_DEFAULT, "width:24px; height:24px;", "moveUp();", false)%><br><br>
                  <%=Button.script(facade, "buttonAbaixo", "", "Move o cartão selecionado para baixo.", "images/commands/down.png", "", Button.KIND_DEFAULT, "width:24px; height:24px;", "moveDown();", false)%>
                </td>
                <td>
                  <select id="visiveis" name="visiveis" size="10" ondblclick="deleteCards();"  multiple style="width:100%; height:100%;">
                    <%// loop nos cards visíveis
                      for (int i=0; i<cards.length; i++) {
                        // obtém seu Action
                        Action action = facade.actionList().get(cards[i], false);
                        // se não achamos...dispara
                        if (action == null)
                          continue;
                        %>
                      <option value="<%=action.getName()%>"><%=action.getCaption()%><%=action.getJustReleased() ? " (novo)" : ""%></option>
                    <%}%>
                  </select>
                </td>
              </tr>
            </table>
            <br>
            <span>Quantos cartões deseja exibir lado a lado em uma mesma linha?</span>
            <table>
              <tr>
                <td><input type="radio" name="porLinha" id="porLinha2" value="2" <%=cardsPerLine.equals("2") ? "checked" : ""%>></td>
                <td><label for="porLinha2">2 cartões</label></td>
                <td><input type="radio" name="porLinha" id="porLinha3" value="3" <%=cardsPerLine.equals("3") ? "checked" : ""%>></td>
                <td><label for="porLinha3">3 cartões</label></td>
              </tr>
            </table>

          </td>
        </tr>
        <tr>
          <td colspan="2" align="right" style="height:20px;">
            <%=Button.script(facade, "buttonOK", "OK", "", "", "", Button.KIND_DEFAULT, "", "formSubmit();", false)%>&nbsp;&nbsp;&nbsp;
            <%=Button.script(facade, "buttonCancelar", "Cancelar", "", "", "", Button.KIND_DEFAULT, "", "window.close();", false)%>
          </td>
        </tr>
      </table>
    </form>

  </body>
</html>
