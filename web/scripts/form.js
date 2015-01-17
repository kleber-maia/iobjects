var formFocus = null;

function Form_ajaxSubmit(id) {
  // nossos objetos
  var form              = document.getElementById(id);
  var formInputAction   = document.getElementById(id + "action");
  var formInputCommand  = document.getElementById(id + "command");
  // nossas vari�vels
  var ajaxRequest       = eval(id + "ajaxRequest");
  // loop nos elementos para montar a query
  var query = "";
  for (i=0; i<form.elements.length; i++) {
    // elemento da vez
    input = form.elements[i];
    // se n�o tem nome...continua
    if (input.name == "")
      continue;
    // se � uma caixa de texto...
    if ((input.type == "text") || (input.type == "textarea") || (input.type == "password") || (input.type == "hidden"))
      query += (i > 0 ? "&" : "") + input.name + "=" + escape(input.value);
    // se � um combo ou checkbox
    else if (((input.type == "combo") || (input.type == "checkbox")) && (input.checked))
      query += (i > 0 ? "&" : "") + input.name + "=" + escape(input.value);
    // se � um select-�nico
    else if (input.type == "select-one")
      query += (i > 0 ? "&" : "") + input.name + "=" + escape(input.options[input.selectedIndex].value);
    // se � um select-m�ltiplo
    else if (input.type == "select-multiple") {
      for (var w=0; w<input.options.length; w++) {
        var option = input.options[w];
        if (option.selected)
          query += (i > 0 ? "&" : "") + input.name + "=" + escape(option.value);
      } // for w
    } // if
  } // for
  // faz a requisi��o
  ajaxRequest = Ajax_request("controller", query, eval(id + "_ajaxSubmitCallback"));
  // torna o Callback v�lido evitando os problemas de duplicidade de chamada da fun��o
  eval(id + "ajaxCallbackValid = true;");
  // guarda o objeto da requisi��o na vari�vel global
  eval(id + "ajaxRequest = ajaxRequest");
}

function Form_ajaxSubmitCallback(id) {
  // se o Callback n�o � v�lido...dispara
  var ajaxCallbackValid = eval(id + "ajaxCallbackValid");
  if (!ajaxCallbackValid)
    return;
  // nossos objetos
  var form = document.getElementById(id);
  // nossas vari�vels
  var ajaxRequest         = eval(id + "ajaxRequest");
  var ajaxTarget          = eval(id + "ajaxTarget");
  var ajaxTargetContainer = eval(id + "ajaxTargetContainer");
  // se n�o recebemos a resposta...dispara
  if (!Ajax_isResponseReady(ajaxRequest))
    return;
  // torna o Callback inv�lido evitando os problemas de duplicidade de chamada da fun��o
  eval(id + "ajaxCallbackValid = false;");
  // desmarca o form como submetido
  eval(id + "submited = false;");
  // se temos uma exce��o...mostra a p�gina de erro
  var lastException = Ajax_getResponseHeader(ajaxRequest, "lastException");
  if ((lastException != null) && (lastException != "")) {
    document.location.href = "controller?action=error";
    return;
  } // if
  // retorna os controles � apar�ncia normal
  for (i=0; i<form.elements.length; i++) {
    // elemento da vez
    input = form.elements[i];
    // altera a apar�ncia
    if ((input.type == "text") || (input.type == "textarea") || (input.type == "password") || (input.type == "hidden") || (input.type == "select-one") || (input.type == "select-multiple"))
      input.style.color = "black";
  } // for
  // se a resposta foi OK...insere o conte�do
  if (Ajax_isResponseStatusOK(ajaxRequest)) {
    // composi��o da resposta
    var compose = Ajax_getResponseHeader(ajaxRequest, "compose");
    // se o tipo de resposta � HTML...
    if (compose == "html") {
      // container de destino
      var target = document.getElementById(ajaxTarget) || ajaxTargetContainer;
      // escreve o resultado
      target.innerHTML = Ajax_responseText(ajaxRequest);
    }
    // se o tipo de resposta � JavaScript...interpreta
    else if (compose == "javascript") {
      eval(Ajax_responseText(ajaxRequest));
    } // if
  }
  // se ocorreu um erro...mostra
  else {
    alert(Ajax_responseText(ajaxRequest));
  } // if
}

function Form_focus(id) {
  // guarda o form que iremos p�r o foco
  formFocus = document.getElementById(id);
  // evento onLoad do documento
  if (window.addEventListener)
    window.addEventListener("load", Form_focusShow, false);
  else
    window.attachEvent("onload", Form_focusShow);
}

function Form_focusShow() {
  // se n�o temos um form para p�r o foco...dispara
  if (formFocus == null)
    return;
  // procura pelo primeiro elemento v�lido
  for (i=0; i<formFocus.elements.length; i++) {
    // elemento da vez
    input = formFocus.elements[i];
    // se n�o � de um dos tipos desejados...continua
    if ((input.type != "text") && (input.type != "radio") && (input.type != "checkbox"))
      continue;
    // se est� desabilitado ou somente leitura...continua
    if (input.disabled || input.readOnly)
      continue;
    // se a janela n�o est� vis�vel...for�a
    if ((window.frameElement != null) && (window.frameElement.style.display == "none"))
      window.frameElement.style.display = "block";
    // p�e o foco
    try {
      input.focus();
    }
    catch (e) {}
    // dispara
    break;
  } // for
  // trabalho feito
  formFocus = null;
}

function Form_submitLastCommand(id) {
  // nossos objetos
  var form                  = document.getElementById(id);
  var formInputCommand      = document.getElementById(id + "command");
  var formInputLastCommand  = document.getElementById(id + "lastCommand");
  var formInputReloaded     = document.getElementById(id + "reloaded");
  // nossas vari�veis
  var formSubmited = eval(id + "submited");
  // se j� submetemos...
  if (formSubmited == true) {
    alert("Aguarde o t�rmino da opera��o. Clique em OK para continuar.");
    return;
  } // if
  // marca como recarregado
  formInputReloaded.value = "true";
  // define o comando como sendo o �ltimo comando executado
  formInputCommand.value = formInputLastCommand.value;
  // submete
  eval(id + "submited = true;");
  form.submit();
}

function Form_submit(id, confirmationMessage, avoidRedundantSubmition, ignoreConstraints, command) {
  // nossos objetos
  var form              = document.getElementById(id);
  var formInputCommand  = document.getElementById(id + "command");
  var formInputReloaded = document.getElementById(id + "reloaded");
  // nossas vari�veis
  var formSubmited = eval(id + "submited");
  // se � podemos submeter duas vezes...
  if (avoidRedundantSubmition && (formSubmited == true)) {
    alert("Aguarde o t�rmino da opera��o. Clique em OK para continuar.");
    return;
  }
  // se devemos verificar as constraints...
  if (!ignoreConstraints) {
    // nosso resultado
    var result = false;
    // loop nos elementos
    for (var i=0; i<form.elements.length; i++) {
      // elemento da vez
      var input = form.elements[i];
      // se tem uma constraint...
      if (input.ondblclick != null) {
        // verifica a constraint
        result = input.ondblclick();
        // se n�o passou...dispara
        if (!result) {
          showDefaultCursor();
          return;
        } // if
      } // if
    } // for
  } // if
  // se temos uma mensagem de confirma��o...
  if (confirmationMessage != null) {
    // se n�o confirmou...dispara
    if (!confirm(confirmationMessage)) {
      showDefaultCursor();
      return;
    } // if
  } // if
  // desmarca como recarregado
  if (formInputReloaded != null)
    formInputReloaded.value = "false";
  // define o comando
  formInputCommand.value = command;
  // submete
  eval(id + "submited = true;");
  form.submit();
}
