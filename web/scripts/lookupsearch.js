function LookupSearch_blur(id) {
  // nossos objetos
  var user = document.getElementById(id + "user");
  // nossas vari�veis
  var displayValue = eval(id + "displayValue");
  var readOnly     = eval(id + "readOnly");
  // oculta o popup
  LookupSearch_hidePopup(id);
  // se ainda n�o temos um valor...
  if (displayValue == "") {
    user.value       = "Pesquisar...";
    user.style.color = "#666666";
  }
  // se temos um valor...
  else {
    user.value       = displayValue;
    user.style.color = eval(id + "color");
    if (!readOnly)
      user.style.backgroundImage = "url(images/commands/delete.png)";
  } // if
}

function LookupSearch_changeValue(id, value, displayValue) {
  // nossos objetos
  var input = document.getElementById(id);
  var user  = document.getElementById(id + "user");
  // muda o valor
  input.value = value;
  eval(id + "value = \"" + value + "\"");
  // muda o texto
  user.value = displayValue;
  eval(id + "displayValue = \"" + displayValue + "\"");
  // chama o script OnChange
  var onChangeScript = eval(id + "onChangeScript");
  eval(onChangeScript);
  // muda a cor
  eval(id + "color = \"#0000FF\";");
  // mostra o valor
  LookupSearch_blur(id);
}

function LookupSearch_clear(id) {
  // nossos objetos
  var input  = document.getElementById(id);
  var user   = document.getElementById(id + "user");
  // muda o valor selecionado
  input.value = "";
  eval(id + "value = \"\";");
  // muda o valor exibido
  eval(id + "displayValue = \"\";");
  // muda o �cone
  user.style.backgroundImage = "url(images/externallookup16x16.png)";
  // reinicia a pesquisa
  eval(id + "lastSearch = \"\";");
  // chama o script OnChange
  var onChangeScript = eval(id + "onChangeScript");
  eval(onChangeScript);
  // simula sair do controle
  LookupSearch_blur(id);
  // simula entrar no controle
  LookupSearch_focus(id);
}

function LookupSearch_click(id, event) {
  // nossos objetos
  var input  = document.getElementById(id);
  // nossas vari�veis
  var readOnly = eval(id + "readOnly");
  // posi��o do clique
  var x = event.offsetX;
  // se clicou no �cone � esquerda...limpa o valor
  if ((x <= 16) && !readOnly)
    LookupSearch_clear(id);
}

function LookupSearch_focus(id) {
  // nossas vari�veis
  var displayValue = eval(id + "displayValue");
  var user         = document.getElementById(id + "user");
  // se ainda n�o temos um valor...
  if (displayValue == "") {
    user.value       = "";
    user.style.color = "black";
  } // if
}

function LookupSearch_hidePopup(id) {
  // nossos objetos
  var popup = document.getElementById(id + "popup");
  var user  = document.getElementById(id + "user");
  // oculta o popup
  popup.style.visibility = "hidden";
}

function LookupSearch_keyDown(id, event) {
  // se quer mostrar/ocultar o popup...
  if (event.altKey && (event.keyCode == 40)) {
    LookupSearch_showHidePopup(id);
    return;
  } // if

  // nossos objetos
  var popup  = document.getElementById(id + "popup");
  var option = popup.childNodes;
  // se n�o estamos com o popup vis�vel...dispara
  if (popup.style.visibility != "visible")
    return;
  // se n�o temos op��es...dispara
  if (option.length == 0)
    return;
  // nossas vari�veis
  var highlightIndex = eval(id + "highlightIndex");

  // tecla pressionada
  var key = (event.keyCode ? event.keyCode : event.which ? event.which : event.charCode);
  // se quer ocultar o popup...
  if (key == 27)
    LookupSearch_hidePopup(id);
  // se quer selecionar o item destacado...seleciona-o
  else if ((key == 13) && (highlightIndex >= 0))
    LookupSearch_optionSelect(id, highlightIndex);
  // se quer destacar 4 itens acima...destaca-o
  else if (key == 33) {
    var pageUpIndex = (highlightIndex >= 4 ? highlightIndex - 4 : 0);
    LookupSearch_optionHighlight(id, pageUpIndex);
  }
  // se quer destacar 4 itens abaixo...destaca-o
  else if (key == 34) {
    var pageDownIndex = (highlightIndex <= option.length - 5 ? highlightIndex + 4 : option.length-1);
    LookupSearch_optionHighlight(id, pageDownIndex);
  }
  // se quer destacar o primeiro item...destaca-o
  else if (key == 36)
    LookupSearch_optionHighlight(id, 0);
  // se quer destacar o �ltimo item...destaca-o
  else if (key == 35)
    LookupSearch_optionHighlight(id, option.length-1);
  // se quer destacar o item de cima...destaca-o
  else if ((key == 38) && (highlightIndex > 0))
    LookupSearch_optionHighlight(id, highlightIndex-1);
  // se quer destacar o item de baixo...destaca-o
  else if ((key == 40) && (highlightIndex+1 < option.length))
    LookupSearch_optionHighlight(id, highlightIndex+1);
}

function LookupSearch_keyUp(id, event) {
  // tecla pressionada
  var key = (event.keyCode ? event.keyCode : event.which ? event.which : event.charCode);
  // se n�o � um caractere...dispara
  if (key <= 40)
    return;
  // cancela o timer existente
  window.clearTimeout(eval(id + "timer"));
  // agenda um novo timer
  var timer = window.setTimeout("LookupSearch_search('" + id + "')", 500);
  // guarda o timer
  eval(id + "timer = " + timer);
}

function LookupSearch_optionClick(e) {
  // nosso evento
  var ev = e || event;
  // nosso span
  var span = ev.srcElement || ev.originalTarget;
  // se n�o temos nada...dispara
  if (span == null)
    return;
  // nosso id
  var id = span.id.substring(0, span.id.indexOf("option"));
  // �ndice do option
  var index = span.getAttribute("index");
  // seleciona
  LookupSearch_optionSelect(id, index);
}

function LookupSearch_optionSelect(id, index) {
  // se clicou em uma imagem...dispara
  if (id == "")
    return;
  // nossos objetos
  var input  = document.getElementById(id);
  var popup  = document.getElementById(id + "popup");
  var option = popup.childNodes;
  var user   = document.getElementById(id + "user");
  // muda o �ndice da op��o selecionada
  eval(id + "selectedIndex = " + index);
  // item selecionado
  var span = option[index]
  // muda o valor selecionado
  input.value = span.getAttribute("value");
  eval(id + "value = \"" + span.getAttribute("value") + "\"");
  // muda o texto selecionado
  user.value = span.getAttribute("caption");
  eval(id + "displayValue = \"" + span.getAttribute("caption") + "\"");
  // oculta o popup
  LookupSearch_hidePopup(id);
  // chama o script OnChange
  var onChangeScript = eval(id + "onChangeScript");
  eval(onChangeScript);
  // muda a cor
  eval(id + "color = \"#0000FF\";");
}

function LookupSearch_optionHighlight(id, index) {
  // nossos objetos
  var popup  = document.getElementById(id + "popup");
  var option = popup.childNodes;
  // se n�o temos op��es...dispara
  if (option.length == 0)
    return;
  try {
    // nossas vari�veis
    var selectedIndex = eval(id + "selectedIndex");
    var highlightIndex = eval(id + "highlightIndex");
    // desmarca a sele��o atual
    if (selectedIndex >= 0)
      option[selectedIndex].className = "FormSelectExOption";
    // desmarca o destaque atual
    if (highlightIndex >= 0)
      option[highlightIndex].className = "FormSelectExOption";
  }
  catch (e) {
  } // try-catch
  // se n�o � um �ndice v�lido...dispara
  if ((index < 0) || (index > option.length))
    return;
  // define �ndice do destaque atual
  eval(id + "highlightIndex = " + index);
  // destaca a op��o desejada
  option[index].className = "FormSelectExOptionSelected";
  // assegura que a posi��o da barra de rolagem permite visualizar o item e os itens abaixo
  option[index].scrollIntoView(false);
}

function LookupSearch_search(id) {
  // se est� em requisi��o...dispara
  var ajaxRequested = eval(id + "ajaxRequested");
  if (ajaxRequested)
    return;
  // nossos objetos
  var user  = document.getElementById(id + "user");
  var popup = document.getElementById(id + "popup");
  // se n�o precisamos pesquisar novamente...dispara
  var value = trim(user.value).toUpperCase().replace("\\", "");
  if (value == "") {
    // reinicia a pesquisa
    eval(id + "lastSearch = \"\";");
    // dispara
    return;
  } // if
  // obt�m a �ltima pesquisa
  var lastSearch = eval(id + "lastSearch");
  if (lastSearch != "") {
    if (lastSearch.indexOf(value) == 0) {
      LookupSearch_showPopup(id);
      return;
    } // if
    if ((value.indexOf(lastSearch) == 0) && (popup.childNodes.length <= 1))
      return;
  } // if
  // evita requisi��es concorrentes
  eval(id + "ajaxRequested = true;");
  // nossas vari�veis
  var className        = eval(id + "className");
  var searchFields     = eval(id + "searchFields");
  var displayField     = eval(id + "displayField");
  var keyExpression    = eval(id + "keyExpression");
  var filterExpression = eval(id + "filterExpression");
  var returnField      = eval(id + "returnField");
  // �ltima pesquisa
  eval(id + "lastSearch = \"" + value + "\";");
  // prepara a requisi��o
  ajaxRequest = Ajax_requestForBusinessObject("iobjects.ui.entity.LookupSearch", true, "search", [escape(className), escape(searchFields), escape(displayField), escape(keyExpression), escape(filterExpression), escape(returnField), escape(value.replace("%", ""))], [5, 5, 5, 5, 5, 5, 5], eval(id + "_searchCallback"));
  // guarda o objeto da requisi��o na vari�vel global
  eval(id + "ajaxRequest = ajaxRequest;");
}

function LookupSearch_searchCallback(id) {
  // nossas vari�vels
  var ajaxRequest = eval(id + "ajaxRequest");
  // se n�o recebemos a resposta...dispara
  if (!Ajax_isResponseReady(ajaxRequest))
    return;
  // se a resposta foi OK...insere o conte�do
  if (Ajax_isResponseStatusOK(ajaxRequest)) {
    // adiciona as op��es
    LookupSearch_setOptions(id, eval(Ajax_responseText(ajaxRequest)));
    // mostra o popup
    LookupSearch_showPopup(id);
  }
  // se ocorreu um erro...mostra
  else {
    alert(Ajax_responseErrorMessage(ajaxRequest));
  } // if
  // permite nova pesquisa
  eval(id + "ajaxRequested = false;");
}

function LookupSearch_script(id) {
  // nossas vari�veis
  var className         = eval(id + "className");
  var searchFields      = eval(id + "searchFields");
  var displayField      = eval(id + "displayField");
  var keyExpression     = eval(id + "keyExpression");
  var filterExpression  = eval(id + "filterExpression");
  var returnField       = eval(id + "returnField");
  var value             = eval(id + "value");
  var displayValue      = eval(id + "displayValue");
  var constraint        = eval(id + "constraint");
  var constraintMessage = eval(id + "constraintMessage");
  var width             = eval(id + "width");
  var style             = eval(id + "style");
  var onChangeScript    = eval(id + "onChangeScript");
  var readOnly          = eval(id + "readOnly");
  // input para o campo de retorno
  document.write("<input type=\"hidden\" id=\"" + id + "\" autocomplete=\"off\" name=\"" + id + "\" value=\"" + value + "\" onchange=\"" + onChangeScript + "\" " + (constraint != "" ? "ondblclick=\"if (eval(" + constraint + ")) { return true; } else { alert('" + constraintMessage + "'); return false; }\"" : "") + " />");
  // input para a pesquisa
  document.write("<input type=\"text\" autocomplete=\"off\" id=\"" + id + "user\" name=\"" + id + "user\" style=\"width:" + (width == 0 ? "100%" : width + "px") + ";text-transform:uppercase;padding-left:20px;background-image:url(images/externallookup16x16.png);background-position:0 0;background-repeat:no-repeat;" + style + "\" onfocus=\"LookupSearch_focus(" + id + "id);\" onblur=\"LookupSearch_blur(" + id + "id);\" onclick=\"LookupSearch_click(" + id + "id, event);\" onkeydown=\"LookupSearch_keyDown(" + id + "id, event);\" " + (readOnly ? "readonly=readonly" : "") + " onkeyup=\"LookupSearch_keyUp(" + id + "id, event);\" />");
  // popup
  document.write("<div id=\"" + id + "popup\" class=\"FormSelectExPopup\" style=\"position:absolute; width:0px; height:0px; overflow:auto; overflow-x:hidden; visibility:hidden;\"></div>");
  // declara vari�veis p�blicas
  eval(id + "ajaxRequest = null;");
  eval(id + "ajaxRequested = false;");
  eval(id + "selectedIndex = -1;");
  eval(id + "highlightIndex = -1;");
  eval(id + "color = \"" + document.getElementById(id).style.color + "\";");
  eval(id + "lastSearch = \"\";");
  // mostra o valor
  LookupSearch_blur(id);
}

function LookupSearch_setOptions(id, options) {
  // nossos objetos
  var input = document.getElementById(id);
  var popup = document.getElementById(id + "popup");
  // apaga as op��es atuais
  while (popup.childNodes.length > 0)
    popup.removeChild(popup.childNodes[0]);
  // loop nas op��es
  for (var i=0; i<options.length; i++) {
    // option da vez
    var option = options[i];
    // cria a op��o
    var span = document.createElement("SPAN");
    span.id                = id + "option";
    span.className         = "FormSelectExOption";
    span.onclick           = LookupSearch_optionClick;
    span.style.padding     = 1;
    span.style.height      = "18px";
    span.style.width       = "100%";
    span.style.display     = "block";
    span.setAttribute("caption", option[1]);
    span.setAttribute("index", i);
    span.setAttribute("value", option[0]);
    if (option[0] != "")
      span.innerHTML = "<img align=\"absmiddle\" src=\"images/entity16x16.png\" /> " + option[1];
    popup.appendChild(span);
    // marca o primeiro...
    eval(id + "selectedIndex = 0;");
  } // for
}

function LookupSearch_setPosition(id) {
  // nossos objetos
  var popup  = document.getElementById(id + "popup");
  var option = popup.childNodes;
  var user   = document.getElementById(id + "user");
  // posi��o
  var userLeft   = user.offsetLeft;
  var userTop    = user.offsetTop;
  var userParent = user.offsetParent;
  var popupParent= popup.offsetParent;
  while ((userParent != null) && (userParent != popupParent)) {
    userLeft  += userParent.offsetLeft;
    userTop   += userParent.offsetTop;
    userParent = userParent.offsetParent;
  } // while
  // obt�m a altura ideal da janela popup baseado na qde de itens existentes
  var desiredHeight = 20;
  if (option.length > 0)
    desiredHeight = (option.length * option[0].offsetHeight) + 2;
  // espa�o dispon�vel acima e abaixo do container
  var availHeightUp = userTop;
  var availHeightDown = (document.body.offsetHeight > userTop + user.offsetHeight ? document.body.offsetHeight - userTop - user.offsetHeight : 0);
  // se temos espa�o suficiente abaixo do container ou
  // o espa�o abaixo � pelo menos 30% maior que o espa�o acima...
  if ((availHeightDown >= desiredHeight) || (availHeightDown >= availHeightUp * 0.7)) {
    // mostra a janela popup logo abaixo do container
    popup.style.top = userTop + user.offsetHeight;
    // se o espa�o dispon�vel � menor que o espa�o desejado...redimensiona
    if (availHeightDown < desiredHeight)
      popup.style.height = availHeightDown;
    else
      popup.style.height = desiredHeight;
  }
  // se o espa�o acima � maior...
  else {
    // se o espa�o dispon�vel � menor que o espa�o desejado...redimensiona
    if (availHeightUp < desiredHeight)
      popup.style.height = availHeightUp;
    else
      popup.style.height = desiredHeight;
    // mostra a janela popup logo acima do container
    popup.style.top = userTop - popup.offsetHeight;
  } // if

  // alinha a janela popup a esquerda do container
  popup.style.left = userLeft;
  // ajusta a largura do popup igual a largura do edit
  // para evitar problemas ocorridos com quebra dos elementos
  popup.style.width = user.offsetWidth;
}

function LookupSearch_showHidePopup(id) {
  // nossos objetos
  var popup = document.getElementById(id + "popup");
  // se est� vis�vel...oculta
  if (popup.style.visibility == "visible")
    LookupSearch_hidePopup(id);
  // se est� oculto...mostra
  else
    LookupSearch_showPopup(id);
}

function LookupSearch_showPopup(id) {
  // nossos objetos
  var popup = document.getElementById(id + "popup");
  // nossas vari�veis
  var readOnly = eval(id + "readOnly");
  // se estamos read-only...dispara
  if (readOnly)
    return;
  // define a posi��o do popup na tela
  LookupSearch_setPosition(id);
  // mostra na frente dos outros objetos
  popup.style.zIndex = 1000;
  // mostra o popup
  popup.style.visibility = "visible";
  // destaca o item selecionado
  LookupSearch_optionHighlight(id, eval(id + "selectedIndex"));
}
