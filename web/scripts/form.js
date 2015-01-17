var formFocus = null;

function Form_ajaxSubmit(id) {
  // nossos objetos
  var form              = document.getElementById(id);
  var formInputAction   = document.getElementById(id + "action");
  var formInputCommand  = document.getElementById(id + "command");
  // nossas variávels
  var ajaxRequest       = eval(id + "ajaxRequest");
  // loop nos elementos para montar a query
  var query = "";
  for (i=0; i<form.elements.length; i++) {
    // elemento da vez
    input = form.elements[i];
    // se não tem nome...continua
    if (input.name == "")
      continue;
    // se é uma caixa de texto...
    if ((input.type == "text") || (input.type == "textarea") || (input.type == "password") || (input.type == "hidden"))
      query += (i > 0 ? "&" : "") + input.name + "=" + escape(input.value);
    // se é um combo ou checkbox
    else if (((input.type == "combo") || (input.type == "checkbox")) && (input.checked))
      query += (i > 0 ? "&" : "") + input.name + "=" + escape(input.value);
    // se é um select-único
    else if (input.type == "select-one")
      query += (i > 0 ? "&" : "") + input.name + "=" + escape(input.options[input.selectedIndex].value);
    // se é um select-múltiplo
    else if (input.type == "select-multiple") {
      for (var w=0; w<input.options.length; w++) {
        var option = input.options[w];
        if (option.selected)
          query += (i > 0 ? "&" : "") + input.name + "=" + escape(option.value);
      } // for w
    } // if
  } // for
  // faz a requisição
  ajaxRequest = Ajax_request("controller", query, eval(id + "_ajaxSubmitCallback"));
  // torna o Callback válido evitando os problemas de duplicidade de chamada da função
  eval(id + "ajaxCallbackValid = true;");
  // guarda o objeto da requisição na variável global
  eval(id + "ajaxRequest = ajaxRequest");
}

function Form_ajaxSubmitCallback(id) {
  // se o Callback não é válido...dispara
  var ajaxCallbackValid = eval(id + "ajaxCallbackValid");
  if (!ajaxCallbackValid)
    return;
  // nossos objetos
  var form = document.getElementById(id);
  // nossas variávels
  var ajaxRequest         = eval(id + "ajaxRequest");
  var ajaxTarget          = eval(id + "ajaxTarget");
  var ajaxTargetContainer = eval(id + "ajaxTargetContainer");
  // se não recebemos a resposta...dispara
  if (!Ajax_isResponseReady(ajaxRequest))
    return;
  // torna o Callback inválido evitando os problemas de duplicidade de chamada da função
  eval(id + "ajaxCallbackValid = false;");
  // desmarca o form como submetido
  eval(id + "submited = false;");
  // se temos uma exceção...mostra a página de erro
  var lastException = Ajax_getResponseHeader(ajaxRequest, "lastException");
  if ((lastException != null) && (lastException != "")) {
    document.location.href = "controller?action=error";
    return;
  } // if
  // retorna os controles à aparência normal
  for (i=0; i<form.elements.length; i++) {
    // elemento da vez
    input = form.elements[i];
    // altera a aparência
    if ((input.type == "text") || (input.type == "textarea") || (input.type == "password") || (input.type == "hidden") || (input.type == "select-one") || (input.type == "select-multiple"))
      input.style.color = "black";
  } // for
  // se a resposta foi OK...insere o conteúdo
  if (Ajax_isResponseStatusOK(ajaxRequest)) {
    // composição da resposta
    var compose = Ajax_getResponseHeader(ajaxRequest, "compose");
    // se o tipo de resposta é HTML...
    if (compose == "html") {
      // container de destino
      var target = document.getElementById(ajaxTarget) || ajaxTargetContainer;
      // escreve o resultado
      target.innerHTML = Ajax_responseText(ajaxRequest);
    }
    // se o tipo de resposta é JavaScript...interpreta
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
  // guarda o form que iremos pôr o foco
  formFocus = document.getElementById(id);
  // evento onLoad do documento
  if (window.addEventListener)
    window.addEventListener("load", Form_focusShow, false);
  else
    window.attachEvent("onload", Form_focusShow);
}

function Form_focusShow() {
  // se não temos um form para pôr o foco...dispara
  if (formFocus == null)
    return;
  // procura pelo primeiro elemento válido
  for (i=0; i<formFocus.elements.length; i++) {
    // elemento da vez
    input = formFocus.elements[i];
    // se não é de um dos tipos desejados...continua
    if ((input.type != "text") && (input.type != "radio") && (input.type != "checkbox"))
      continue;
    // se está desabilitado ou somente leitura...continua
    if (input.disabled || input.readOnly)
      continue;
    // se a janela não está visível...força
    if ((window.frameElement != null) && (window.frameElement.style.display == "none"))
      window.frameElement.style.display = "block";
    // põe o foco
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
  // nossas variáveis
  var formSubmited = eval(id + "submited");
  // se já submetemos...
  if (formSubmited == true) {
    alert("Aguarde o término da operação. Clique em OK para continuar.");
    return;
  } // if
  // marca como recarregado
  formInputReloaded.value = "true";
  // define o comando como sendo o último comando executado
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
  // nossas variáveis
  var formSubmited = eval(id + "submited");
  // se ñ podemos submeter duas vezes...
  if (avoidRedundantSubmition && (formSubmited == true)) {
    alert("Aguarde o término da operação. Clique em OK para continuar.");
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
        // se não passou...dispara
        if (!result) {
          showDefaultCursor();
          return;
        } // if
      } // if
    } // for
  } // if
  // se temos uma mensagem de confirmação...
  if (confirmationMessage != null) {
    // se não confirmou...dispara
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
