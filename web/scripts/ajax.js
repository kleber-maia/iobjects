/**
 *
 * Fun��es para realiza��o de requisi��es AJAX.
 *
**/

var lastCursor = null;

function Ajax_forwardException() {
  document.location.href = "controller?action=error";
}

function Ajax_getResponseHeader(request, header) {
  return request.getResponseHeader(header);
}

function Ajax_isResponseStatusError(request) {
  // retorna
  return (request != null) && ((request.status == 500) || (Ajax_responseErrorMessage(request) != ""));
}

function Ajax_isResponseStatusOK(request) {
  // retorna
  return (request != null) && (request.status == 200) && (Ajax_responseErrorMessage(request) == "");
}

function Ajax_isResponseReady(request) {
  // nosso resultado
  var result = (request != null) && (request.readyState == 4);
  // retorna o cursor
  if (result)
    document.body.style.cursor = lastCursor;
  // retorna
  return result;
}

function Ajax_isResponseTypeText(request) {
  // retorna
  return (request != null) && (request.getResponseHeader("Content-Type").indexOf("text/plain") >= 0);
}

function Ajax_isResponseTypeXML(request) {
  // retorna
  return (request != null) && (request.getResponseHeader("Content-Type").indexOf("text/xml") >= 0);
}

function Ajax_request(url, query, callbackFunction) {
  // muda o cursor
  var async = false;
  lastCursor = document.body.style.cursor;
  document.body.style.cursor = "wait";
  try {
    // cria requisi��o
    var request = null;
    try {
      request = new XMLHttpRequest();
    }
    catch (e) {
      request = new ActiveXObject("MSXML2.XMLHTTP.3.0");
    } // try-catch
    // ser� uma requisi��o ass�ncrona?
    async = callbackFunction != null;
    // abre conex�o
    request.open("post", encodeURI(url), async);
    // forma de codifica��o
    request.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
    request.setRequestHeader("Encode", "charset=ISO-8859-1");
    // define a fun��o de callback
    if (async)
      request.onreadystatechange = callbackFunction;
    // envia
    request.send(query + "&nocache=" + (new Date()).getTime());
    // retorna a requisi��o
    return request;
  }
  finally {
    // retorna o cursor
    if (!async)
      document.body.style.cursor = lastCursor;
  } // try-finally
}

function Ajax_requestForAction(action,
                               command,
                               parameters,
                               callbackFunction) {
  // prepara nossa URL
  var url = "http://"
          + window.location.host
          + "/controller"
          + "?action=" + action
          + "&command=" + command;
  // constr�i a query
  var query = "";
  for (i=0; i<parameters.length; i++) {
    query += (query != "" ? "&" : "") + parameters[i];
  } // for
  // faz a requisi��o
  return Ajax_request(url, query, callbackFunction);
}

function Ajax_requestForBusinessObject(className,
                                       isBusinessObject,
                                       methodName,
                                       parameterValues,
                                       parameterTypes,
                                       callbackFunction) {
  // prepara nossa URL
  var url = "http://"
          + window.location.host
          + "/ajaxserver"
          + "?className=" + className
          + "&isBusinessObject=" + isBusinessObject
          + "&methodName=" + methodName;
  // loop nos par�metros
  var query = "";
  for (i=0; i<parameterValues.length; i++) {
    query += (query != "" ? "&" : "") + "&parameterValues=" + parameterValues[i];
  } // for
  // loop nos tipos de par�metros
  if (parameterTypes != null) {
    for (i=0; i<parameterTypes.length; i++) {
      query += (query != "" ? "&" : "") + "&parameterTypes=" + parameterTypes[i];
    } // for
  } // if
  // faz a requisi��o
  return Ajax_request(url, query, callbackFunction);
}

function Ajax_responseErrorMessage(request) {
  // se a respsota � um erro...
  var result = Ajax_getResponseHeader(request, "lastException");
  if (result == null)
    return "";
  else
    return result;
  //if (Ajax_isResponseStatusError(request))
  //  return request.responseText;
  // se n�o...retorna nada
  //else
  //  return "";
}

function Ajax_responseText(request) {
  // se a resposta � do tipo Texto...
  if (Ajax_isResponseTypeText(request))
    return request.responseText;
  else
    return request.responseXML.text;
}

function Ajax_responseXML(request) {
  // se a resposta � do tipo XML...retorna
  if (Ajax_isResponseTypeXML(request))
    return request.responseXML;
  else
    return null;
}










/**
 *
 * Fun��es de leitura de dados XML recebidos atrav�s de requisi��es AJAX.
 *
**/

function Ajax_xmlGetElementAttribute(element, name) {
  // atributos do elemento
  var attributes = element.attributes;
  // se temos atributos... retorna o item desejado
  if (attributes != null) {
    // atributo desejado
    var attribute = attributes.getNamedItem(name);
    // se temos...retornar seu valor
    if (attribute != null)
      return attribute.value;
  } // if
  // se chegou aqui...n�o encontramos
  return null;
}

function Ajax_xmlGetElementByIndex(rootElement, index) {
  // retorna o item desejado
  return rootElement.childNodes[i];
}

function Ajax_xmlGetElementByName(rootElement, name) {
  // retorna o item desejado
  var result;
  for (var i=0; i<rootElement.childNodes.length; i++) {
    if (rootElement.childNodes[i].tagName == name) {
      result = rootElement.childNodes[i];
      break;
    } // if
  } // for
  return result;
}

function Ajax_xmlGetElementName(element) {
  // retorna seu nome
  return element.nodeName;
}

function Ajax_xmlGetElementSize(element) {
  // retorna a qde de filhos
  return element.childNodes.length;
}

function Ajax_xmlGetElementText(element) {
  // retorna seu texto
  return element.text;
}

function Ajax_xmlGetRootElement(request) {
  // retorna a raiz
  return Ajax_responseXML(request).documentElement;
}










/**
 *
 * ParamList
 *
 * Fun��es de leitura de dados XML recebidos atrav�s de requisi��es AJAX.
 *
 * <paramlist>
 *   <!-- qdo o Param possui valor formatado -->
 *   <paramname value="" formatedvalue="" />
 *   <!-- qdo o Param n�o possui valor formatado -->
 *   <paramname>value</paramname>
 * </paramlist>
 *
**/

function ParamList_xmlCheck(request) {
  // nossa raiz
  var root = Ajax_xmlGetRootElement(request);
  // retorna OK se temos a raiz e � da classe ParamList...
  return ((root != null) && (Ajax_xmlGetElementName(root) == "iobjects.ParamList"));
}

function ParamList_xmlGet(request, index, name) {
  // nossa raiz
  var root = Ajax_xmlGetRootElement(request);

  // nosso resultado
  var result = new Object();

  // se temos o �ndice...
  if (index != null)
    // nosso resultado
    result.element = Ajax_xmlGetElementByIndex(root, index);
  // se temos o nome...
  else if (name != null)
    // nosso resultado
    result.element = Ajax_xmlGetElementByName(root, name);
  // se n�o achamos nada...dispara
  if (result.element == null)
    return null;

  // nome do Param
  result.name = Ajax_xmlGetElementName(result.element);
  // valor do Param, caso tenha valor formatado
  result.value = Ajax_xmlGetElementAttribute(result.element, "value");
  // se encontramos o valor nos atributos...
  if (result.value != null)
    // obt�m tamb�m o valor formatado
    result.formatedValue = Ajax_xmlGetElementAttribute(result.element, "formatedValue");
  // se n�o encontramos o valor nos atributos...
  else {
    // obt�m o valor no seu texto
    result.value = Ajax_xmlGetElementText(result.element);
    // define o valor formatado no nosso resultado
    result.formatedValue = result.value;
  } // if

  // retorna
  return result;
}

function ParamList_xmlSize(request) {
  // nossa raiz
  var root = Ajax_xmlGetRootElement(request);
  // retorna a qde de Param's
  return Ajax_xmlGetElementSize(root);
}
