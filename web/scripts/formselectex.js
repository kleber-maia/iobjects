function FormSelectEx_changeValue(id, value) {
  // nossos objetos
  var input  = document.getElementById(id);
  var popup  = document.getElementById(id + "popup");
  var option = popup.childNodes;
  var user   = document.getElementById(id + "user");
  // se não temos opções...dispara
  if (option.length == 0)
    return;
  // loop nas nossas opções a procura de value
  for (i=0; i<option.length; i++) {
    // item da vez
    var span = option[i];
    // se tem o valor procurado...
    if (span.getAttribute("value") == value) {
      // seleciona o item
      FormSelectEx_optionSelect(id, i);
      // dispara
      break;
    } // if
  } // for
}

function FormSelectEx_clear(id) {
  // nossos objetos
  var input  = document.getElementById(id);
  var user   = document.getElementById(id + "user");
  // muda o índice da opção selecionada
  eval(id + "selectedIndex = -1;");
  // muda o valor selecionado
  input.value = "";
  // muda o texto selecionado
  user.value = "";
  // oculta o popup
  FormSelectEx_hidePopup(id);
  // chama o script OnChange
  var onChangeScript = eval(id + "onChangeScript");
  eval(onChangeScript);
}

function FormSelectEx_create(id, width, constraint, constraintMessage, onChangeScript, readOnly) {
  // publica nossas variáveis
  eval(id + "id = \"" + id + "\";");
  eval(id + "readOnly = " + readOnly + ";");
  eval(id + "newWidth = 0;");
  eval(id + "selectedIndex = -1;");
  eval(id + "highlightIndex = -1;");
  eval(id + "searchText = \"\";");
  eval(id + "onChangeScript = \"" + onChangeScript + "\"");
  // input oculto
  document.write("<input id=\"" + id + "\" name=\"" + id + "\" type=\"hidden\" />");
  // input visível e botão de pesquisa
  document.write("<table id=\"" + id + "container\" cellpadding=0 cellspacing=0 style=\"width:" + (width > 0 ? width + "px;" : "100%;") + "\">"
               +   "<tr>"
               +     "<td style=\"width:auto;\">"
               +       "<input id=\"" + id + "user\" "
               +              "name=\"" + id + "user\" "
               +              "type=\"text\" "
               +              "title=\"Clique no botão ao lado para pesquisar.\" "
               +              "onkeydown=\"FormSelectEx_keyDown(" + id + "id, event);\" "
               +       (!constraint == ""
                           ?  "ondblclick=\"if (!(" + constraint + ")) {alert('" + constraintMessage + "'); showDefaultCursor(); return false;} else return true;\" "
                           :  "")
               +              "style=\"width:100%;\" "
               +              "readonly=\"readonly\" />"
               +     "</td>"
               +     "<td style=\"width:22px;\">"
               +       "<button id=\"" + id + "button\" type=\"button\" class=\"button\" onclick=\"FormSelectEx_showHidePopup(" + id + "id);\" onmouseover=\"className='ButtonOver'\" onmouseout=\"className='Button'\" title=\"Pesquisar...\" style=\"width:22px; height:20px;\" " + (readOnly ? "disabled=disabled" : "") + "><img src=\"images/externallookup16x16.png\" /></button>"
               +     "</td>"
               +   "</tr>"
               + "</table>");
  // popup
  document.write("<div id=\"" + id + "popup\" class=\"FormSelectExPopup\" style=\"position:absolute; width:0px; height:0px; overflow:auto; overflow-x:hidden; visibility:hidden;\"></div>");
  // caixa de pesquisa
  document.write("<div id=\"" + id + "popupSearch\" class=\"FormSelectExPopupSearch\" style=\"position:absolute; height:16px; visibility:hidden;\">"
               +   "<table cellspacing=0 style=\"width:100%\">"
               +     "<tr>"
               +       "<td style=\"width:30px;\"><b>Procurar:</b></td>"
               +       "<td style=\"width:auto;\"><input id=\"" + id + "popupSearchText\" type=\"text\" style=\"border:none; background-color:transparent; width:100%; height:14px;\" onkeydown=\"FormSelectEx_keyDown(" + id + "id, event)\" onkeyup=\"FormSelectEx_optionSearch(" + id + "id, this.value);\" /></td>"
               +     "</tr>"
               +   "</table>"
               + "</div>");
}

function FormSelectEx_hidePopup(id) {
  // nossos objetos
  var popup       = document.getElementById(id + "popup");
  var popupSearch = document.getElementById(id + "popupSearch");
  var user        = document.getElementById(id + "user");
  // oculta o popup
  popup.style.visibility = "hidden";
  // oculta a caixa de pesquisa
  popupSearch.style.visibility = "hidden";
  // foco no input
  try {user.focus();} catch (e) {}
}

function FormSelectEx_keyDown(id, event) {
  // se quer mostrar/ocultar o popup...
  if (event.altKey && (event.keyCode == 40)) {
    FormSelectEx_showHidePopup(id);
    return;
  } // if

  // nossos objetos
  var popup  = document.getElementById(id + "popup");
  var option = popup.childNodes;
  // se não estamos com o popup visível...dispara
  if (popup.style.visibility != "visible")
    return;
  // se não temos opções...dispara
  if (option.length == 0)
    return;
  // nossas variáveis
  var highlightIndex = eval(id + "highlightIndex");

  // se quer ocultar o popup...ESC
  if (event.keyCode == 27)
    FormSelectEx_hidePopup(id);
  // se quer selecionar o item destacado...ENTER
  else if ((event.keyCode == 13) && (highlightIndex >= 0))
    FormSelectEx_optionSelect(id, highlightIndex);
  // se quer destacar 4 itens acima...PG_UP
  else if (event.keyCode == 33) {
    var pageUpIndex = (highlightIndex >= 4 ? highlightIndex - 4 : 0);
    FormSelectEx_optionHighlight(id, pageUpIndex);
  }
  // se quer destacar 4 itens abaixo...PG_DN
  else if (event.keyCode == 34) {
    var pageDownIndex = (highlightIndex <= option.length - 5 ? highlightIndex + 4 : option.length-1);
    FormSelectEx_optionHighlight(id, pageDownIndex);
  }
  // se quer destacar o primeiro item...HOME
  else if (event.keyCode == 36)
    FormSelectEx_optionHighlight(id, 0);
  // se quer destacar o último item...END
  else if (event.keyCode == 35)
    FormSelectEx_optionHighlight(id, option.length-1);
  // se quer destacar o item de cima...UP
  else if ((event.keyCode == 38) && (highlightIndex > 0))
    FormSelectEx_optionHighlight(id, highlightIndex-1);
  // se quer destacar o item de baixo...DOWN
  else if ((event.keyCode == 40) && (highlightIndex+1 < option.length))
    FormSelectEx_optionHighlight(id, highlightIndex+1);
}

function FormSelectEx_optionHighlight(id, index) {
  // nossos objetos
  var popup  = document.getElementById(id + "popup");
  var option = popup.childNodes;
  // se não temos opções...dispara
  if (option.length == 0)
    return;
  // nossas variáveis
  var selectedIndex = eval(id + "selectedIndex");
  var highlightIndex = eval(id + "highlightIndex");
  // desmarca a seleção atual
  if (selectedIndex >= 0)
    option[selectedIndex].className = "FormSelectExOption";
  // desmarca o destaque atual
  if (highlightIndex >= 0)
    option[highlightIndex].className = "FormSelectExOption";
  // se não é um índice válido...dispara
  if ((index < 0) || (index > option.length))
    return;
  // define índice do destaque atual
  eval(id + "highlightIndex = " + index);
  // destaca a opção desejada
  option[index].className = "FormSelectExOptionSelected";
  // assegura que a posição da barra de rolagem permite visualizar o item e os itens abaixo
  option[index].scrollIntoView(false);
}

function FormSelectEx_optionSearch(id, text) {
  // se não informou nada para pesquisar...dispara
  if (text == "")
    return;
  // última pesquisa
  var searchText = eval(id + "searchText");
  // se não mudou a pesquisa...dispara
  if (searchText == text)
    return;
  // guarda a última pesquisa
  eval(id + "searchText = \"" + text + "\"");
  // nossos objetos
  var popup  = document.getElementById(id + "popup");
  var option = popup.childNodes;
  // se não temos opções...dispara
  if (option.length == 0)
    return;
  // loop nas opções
  for (i=0; i<option.length; i++) {
    // se o item começa com o valor desejado...
    if (option[i].getAttribute("caption").toUpperCase().indexOf(text.toUpperCase()) == 0) {
      // destaca
      FormSelectEx_optionHighlight(id, i);
      // dispara
      return;
    } // if
  } // for
}

function FormSelectEx_optionClick(e) {
  // nosso evento
  var ev = e || event;
  // nosso span
  var span = ev.srcElement || ev.originalTarget;
  // se não temos nada...dispara
  if (span == null)
    return;
  // nosso id
  var id = span.id.substring(0, span.id.indexOf("option"));
  // índice do option
  var index = span.getAttribute("index");
  // seleciona
  FormSelectEx_optionSelect(id, index);
}

function FormSelectEx_optionSelect(id, index) {
  // se clicou em uma imagem...dispara
  if (id == "")
    return;
  // nossos objetos
  var input  = document.getElementById(id);
  var popup  = document.getElementById(id + "popup");
  var option = popup.childNodes;
  var user   = document.getElementById(id + "user");
  // muda o índice da opção selecionada
  eval(id + "selectedIndex = " + index);
  // item selecionado
  var span = option[index]
  // muda o valor selecionado
  input.value = span.getAttribute("value");
  // muda o texto selecionado
  user.value = span.getAttribute("caption");
  // oculta o popup
  FormSelectEx_hidePopup(id);
  // chama o script OnChange
  var onChangeScript = eval(id + "onChangeScript");
  eval(onChangeScript);
  // muda a cor
  user.style.color = "#0000FF";
}

function FormSelectEx_setOptions(id, options, selectedValue) {
  // nossos objetos
  var popup = document.getElementById(id + "popup");
  // nossas variáveis
  var newWidth      = eval(id + "newWidth");
  var selectedIndex = eval(id + "selectedIndex");
  // apaga as opções atuais
  while (popup.childNodes.length > 0)
    popup.removeChild(popup.childNodes[0]);
  // loop nas opções
  for (var i=0; i<options.length; i++) {
    // option da vez
    var option = options[i];
    // largura do option
    var optionWidth = (option[0].length * 8) + (option[2] * 22);
    if (optionWidth > newWidth)
      newWidth = optionWidth;
    // cria a opção
    var span = document.createElement("SPAN");
    span.id                = id + "option";
    span.className         = "FormSelectExOption";
    span.onclick           = FormSelectEx_optionClick;
    span.style.padding     = 1;
    span.style.paddingLeft = ((option[2] * 22) + 2);
    span.style.height      = "18px";
    span.style.width       = "100%";
    span.style.display     = "block";
    span.setAttribute("caption", option[0]);
    span.setAttribute("index", i);
    span.setAttribute("value", option[1]);
    if (option[0] != "")
      span.innerHTML = "<img align=\"absmiddle\" src=\"images/formselecttreeview16x16.png\" />" + option[0];
    popup.appendChild(span);
    // se devemos selecioná-lo...
    if ((selectedIndex < 0) && (option[1] == selectedValue)) {
      // define o índice selecionado
      eval(id + "selectedIndex = " + i);
      // põe os valores nos inputs
      document.getElementById(id).value = option[1];
      document.getElementById(id + "user").value = option[0];
    } // if
  } // for
}

function FormSelectEx_setPosition(id) {
  // nossos objetos
  var container   = document.getElementById(id + "container");
  var popup       = document.getElementById(id + "popup");
  var popupSearch = document.getElementById(id + "popupSearch");
  var option      = popup.childNodes;
  var user        = document.getElementById(id + "user");
  var button      = document.getElementById(id + "button");
  // posição
  var containerLeft   = container.offsetLeft;
  var containerTop    = container.offsetTop;
  var containerParent = container.offsetParent;
  var popupParent     = popup.offsetParent;
  while ((containerParent != null) && (containerParent != popupParent)) {
    containerLeft  += containerParent.offsetLeft;
    containerTop   += containerParent.offsetTop;
    containerParent = containerParent.offsetParent;
  } // while
  // obtém a altura ideal da janela popup baseado na qde de itens existentes
  var desiredHeight = 20;
  if (option.length > 0)
    desiredHeight = (option.length * option[0].offsetHeight) + 2;
  // espaço disponível acima e abaixo do container
  var availHeightUp = (containerTop > popupSearch.offsetHeight ? containerTop - popupSearch.offsetHeight : 0);
  var availHeightDown = (document.body.offsetHeight > containerTop + container.offsetHeight + popupSearch.offsetHeight ? document.body.offsetHeight - containerTop - container.offsetHeight - popupSearch.offsetHeight : 0);
  // se temos espaço suficiente abaixo do container ou
  // o espaço abaixo é pelo menos 30% maior que o espaço acima...
  if ((availHeightDown >= desiredHeight) || (availHeightDown >= availHeightUp * 0.7)) {
    // mostra a janela popup logo abaixo do container
    popup.style.top = containerTop + container.offsetHeight;
    // se o espaço disponível é menor que o espaço desejado...redimensiona
    if (availHeightDown < desiredHeight)
      popup.style.height = availHeightDown;
    else
      popup.style.height = desiredHeight;
  }
  // se o espaço acima é maior...
  else {
    // se o espaço disponível é menor que o espaço desejado...redimensiona
    if (availHeightUp < desiredHeight)
      popup.style.height = availHeightUp;
    else
      popup.style.height = desiredHeight;
    // mostra a janela popup logo acima do container
    popup.style.top = containerTop - popup.offsetHeight - popupSearch.offsetHeight;
  } // if

  // alinha a janela popup a esquerda do container
  popup.style.left = containerLeft;

//    // ajusta a largura da janela popup
//    " + id + "Popup.style.width = " + newWidth + ";
//    // se a largura do popup for menor que a do edit...ajusta
//    var totalWidth = " + id + "User.offsetWidth + " + id + BUTTON + ".offsetWidth;
//    if (" + id + "Popup.offsetWidth < totalWidth)
//      " + id + "Popup.style.width = totalWidth;

  // ajusta a largura do popup igual a largura do edit
  // para evitar problemas ocorridos com quebra dos elementos
  popup.style.width = user.offsetWidth + button.offsetWidth;

  // ajusta a posição e a largura da janela de pesquisa
  popupSearch.style.top = popup.offsetTop + popup.offsetHeight - 1;
  popupSearch.style.left = popup.offsetLeft;
  popupSearch.style.width = popup.offsetWidth;
}

function FormSelectEx_showHidePopup(id) {
  // nossos objetos
  var popup = document.getElementById(id + "popup");
  // se está visível...oculta
  if (popup.style.visibility == "visible")
    FormSelectEx_hidePopup(id);
  // se está oculto...mostra
  else
    FormSelectEx_showPopup(id);
}

function FormSelectEx_showPopup(id) {
  // nossos objetos
  var popup           = document.getElementById(id + "popup");
  var popupSearch     = document.getElementById(id + "popupSearch");
  var popupSearchText = document.getElementById(id + "popupSearchText");
  // nossas variáveis
  var readOnly = eval(id + "readOnly");
  // se estamos read-only...dispara
  if (readOnly)
    return;
  // define a posição do popup na tela
  FormSelectEx_setPosition(id);
  // mostra na frente dos outros objetos
  popup.style.zIndex = 1000;
  popupSearch.style.zIndex = 1000;
  // mostra o popup
  popup.style.visibility = "visible";
  // mostra a caixa de pesquisa
  popupSearch.style.visibility = "visible";
  // foco na caixa de pesquisa
  popupSearchText.focus();
  popupSearchText.value = "";
  // destaca o item selecionado
  FormSelectEx_optionHighlight(id, eval(id + "selectedIndex"));
}
