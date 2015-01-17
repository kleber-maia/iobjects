var ENTITYGRID_OPERATION_NONE   = 0;
var ENTITYGRID_OPERATION_INSERT = 1;
var ENTITYGRID_OPERATION_UPDATE = 2;

function EntityGrid_forwardBrowse(id, action, func, operation, values, keyValues) {
  // se estamos no frameForm...
  if (window.name == (id + "frameForm")) {
    // oculta-nos
    window.frameElement.style.display = "none";
    // atualiza a consulta
    EntityGrid_updateBrowse(id, func, operation, values, keyValues);
  }
  // se não estamos no frameForm...apenas mostra o action
  else {
    // constrói a url
    var url = "controller?action=" + action;
    // carrega a consulta
    document.location.href = url;
  } // if
}

function EntityGrid_forwardForm(id, action, command, keys, keyValues) {
  // aguarde
  showWaitCursor();
  // nosso frame
  var frameForm = (window.name == (id + "frameForm") ? window.frameElement : document.getElementById(id + "frameForm"));
  // se não temos...usa a própria janela
  if (frameForm == null)
    frameForm = window.frameElement || window;
  // constrói a url
  var url = "controller?action=" + action;
  if (command != null)
    url += "&command=" + command;
  if ((keys != null) && (keys.length > 0)) {
    for (var i=0; i<keys.length; i++)
      url += "&" + keys[i] + "=" + keyValues[i];
  } // if
  // forçando o browser a carregar novamente
  // a URL, evitando milhares de erros de cache
  url += "&dummy=" + Math.random();
  // mostra
  frameForm.style.display = "block";
  // ajusta a tela
  frameForm.style.top    = -10000;
  frameForm.style.left   = -10000;
  frameForm.style.width  = document.body.offsetWidth;
  frameForm.style.height = document.body.offsetHeight;
  // carrega o form
  frameForm.src = url;
  // mostra quando estiver pronto
  if (frameForm.addEventListener)
    frameForm.addEventListener("load", EntityGrid_formLoad, false);
  else
    frameForm.attachEvent("onload", EntityGrid_formLoad);
}

function EntityGrid_formLoad(e) {
  // evento
  var ev = e || event;
  // frame
  var frameForm = ev.srcElement || ev.originalTarget;
  // mostra
  frameForm.style.top  = 0;
  frameForm.style.left = 0;
  // retorna o cursor
  showDefaultCursor();
  // retira-nos do onLoad
  if (frameForm.addEventListener)
    frameForm.removeEventListener("load", 0, "EntityGrid_formLoad");
  else
    frameForm.detachEvent("onload", EntityGrid_formLoad);
}

function EntityGrid_updateBrowse(id, func, operation, values, keyValues) {
  // procura pela janela que contém o grid
  var wnd = window;
  while (wnd != window.top) {
    if (wnd.document.getElementById(id) != null)
      break;
    wnd = wnd.parent;
  } // while
  // se não achamos a janela do grid...dispara
  if (wnd == window.top) {
    //alert(id + " não encontrado.");
    return;
  } // if
  // se temos valores
  var strValues    = "";
  var strKeyValues = "";
  if ((values != null) && (keyValues != null)) {
    for (var i=0; i<values.length; i++)
      strValues += (strValues != "" ? "," : "") + "\"" + values[i] + "\"";
    for (var i=0; i<keyValues.length; i++)
      strKeyValues += (strKeyValues != "" ? "," : "") + "\"" + keyValues[i] + "\"";
  } // if
  // se devemos atualizar...atualiza no grid da página pai
  if (operation == ENTITYGRID_OPERATION_UPDATE)
    wnd.eval("Grid_updateRow(\"" + id + "\", [" + strValues + "], Grid_rowIndex(\"" + id + "\", [" + strKeyValues + "]))");
  // se devemos inserir...insere no grid da página pai
  else if (operation == ENTITYGRID_OPERATION_INSERT)
    wnd.eval("Grid_addRow(\"" + id + "\", [" + strValues + "], [" + strKeyValues + "])");
  // quantidade de registros
  wnd.eval("Grid_changeStatus(\"" + id + "\", \"" + wnd.eval("Grid_rowCount(\"" + id + "\")") + " registros\")");
  // se temos uma função para executar...
  if (func != null)
    wnd.eval(func);
}
