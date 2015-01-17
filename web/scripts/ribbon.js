function ribbonCreateTab(id, title) {
  // qde de tab´s
  var ribbonTabCount = eval(id + "ribbonTabCount");
  // nosso tabBar
  var ribbonTabBar = document.getElementById(id + "ribbonTabBar");
  // separador da esquerda
  var ribbonTabSeparatorLeft = document.createElement("SPAN");
  ribbonTabSeparatorLeft.id        = id + "ribbonTabSeparator" + (ribbonTabCount * 2);
  ribbonTabSeparatorLeft.className = "ribbonTabSeparator";
  ribbonTabBar.insertBefore(ribbonTabSeparatorLeft, null);
  // tab
  var ribbonTab = document.createElement("SPAN");
  ribbonTab.id          = id + "ribbonTab" + ribbonTabCount;
  ribbonTab.className   = "ribbonTab";
  ribbonTab.innerHTML   = title;
  ribbonTab.onclick     = new Function("ribbonSelectTab('" + id + "', " + ribbonTabCount + ");");
  ribbonTab.onmouseover = new Function("ribbonSelectOver('" + id + "', " + ribbonTabCount + ");");
  ribbonTab.onmouseout  = new Function("ribbonSelectOut('" + id + "', " + ribbonTabCount + ");");
  ribbonTabBar.insertBefore(ribbonTab, null);
  // separador da direita
  var ribbonTabSeparatorRight = document.createElement("SPAN");
  ribbonTabSeparatorRight.id                = id + "ribbonTabSeparator" + ((ribbonTabCount * 2) + 1);
  ribbonTabSeparatorRight.className         = "ribbonTabSeparator";
  ribbonTabSeparatorRight.style.marginRight = "4px";
  ribbonTabBar.insertBefore(ribbonTabSeparatorRight, null);
  // se criamos o primeiro tab...seleciona
  if (ribbonTabCount == 0)
    ribbonSelectTab(id, 0);
  // temos um tab a mais
  eval(id + "ribbonTabCount++");
}

function ribbonSelectOver(id, index) {
  // tab selecionada
  var ribbonTabSelected = eval(id + "ribbonTabSelected");
  // se é o tab selecionado...dispara
  if (ribbonTabSelected == index)
    return;
  // objetos que iremos alterar
  var ribbonTabSeparatorLeft  = document.getElementById(id + "ribbonTabSeparator" + (index * 2));
  var ribbonTab               = document.getElementById(id + "ribbonTab" + index);
  var ribbonTabSeparatorRight = document.getElementById(id + "ribbonTabSeparator" + ((index * 2) + 1));
  // altera o estilo
  ribbonTabSeparatorLeft.className  = "ribbonTabOverLeft";
  ribbonTab.className               = "ribbonTabOver";
  ribbonTabSeparatorRight.className = "ribbonTabOverRight";
}

function ribbonSelectOut(id, index) {
  // tab selecionada
  var ribbonTabSelected = eval(id + "ribbonTabSelected");
  // se é o tab selecionado...dispara
  if (ribbonTabSelected == index)
    return;
  // objetos que iremos alterar
  var ribbonTabSeparatorLeft  = document.getElementById(id + "ribbonTabSeparator" + (index * 2));
  var ribbonTab               = document.getElementById(id + "ribbonTab" + index);
  var ribbonTabSeparatorRight = document.getElementById(id + "ribbonTabSeparator" + ((index * 2) + 1));
  // altera o estilo
  ribbonTabSeparatorLeft.className  = "ribbonTabSeparator";
  ribbonTab.className               = "ribbonTab";
  ribbonTabSeparatorRight.className = "ribbonTabSeparator";
}

function ribbonSelectTab(id, index) {
  // tab selecionada
  var ribbonTabSelected = eval(id + "ribbonTabSelected");
  // objetos atualmente com estilo de selecionados
  var ribbonTabSeparatorLeft  = document.getElementById(id + "ribbonTabSeparator" + (ribbonTabSelected * 2));
  var ribbonTab               = document.getElementById(id + "ribbonTab" + ribbonTabSelected);
  var ribbonTabSeparatorRight = document.getElementById(id + "ribbonTabSeparator" + ((ribbonTabSelected * 2) + 1));
  var ribbonTabContainer      = document.getElementById(id + "ribbonTabPanelContainer" + ribbonTabSelected);
  // altera o estilo para parecerem normais
  ribbonTabSeparatorLeft.className    = "ribbonTabSeparator";
  ribbonTab.className                 = "ribbonTab";
  ribbonTabSeparatorRight.className   = "ribbonTabSeparator";
  ribbonTabContainer.style.display    = "none";
  // objetos que iremos alterar
  ribbonTabSeparatorLeft  = document.getElementById(id + "ribbonTabSeparator" + (index * 2));
  ribbonTab               = document.getElementById(id + "ribbonTab" + index);
  ribbonTabSeparatorRight = document.getElementById(id + "ribbonTabSeparator" + ((index * 2) + 1));
  ribbonTabContainer      = document.getElementById(id + "ribbonTabPanelContainer" + index);
  // altera o estilo para parecer selecionado
  ribbonTabSeparatorLeft.className    = "ribbonTabSelectedLeft";
  ribbonTab.className                 = "ribbonTabSelected";
  ribbonTabSeparatorRight.className   = "ribbonTabSelectedRight";
  ribbonTabContainer.style.display    = "block";
  // define o novo índice selecionado
  eval(id + "ribbonTabSelected = index;");
/*
  // se temos um tempo de auto retorno...
  if (eval(id + "ribbonRewindTime") > 0) {
    // cancela o timer existente
    window.clearTimeout(eval(id + "ribbonRewindTimer"));
    // agenda um novo timer
    var timer = window.setTimeout("ribbonSelectTab('" + id + "', 0)", eval(id + "ribbonRewindTime"));
    // guarda o timer
    eval(id + "ribbonRewindTimer = " + timer);
  } // if
*/
}