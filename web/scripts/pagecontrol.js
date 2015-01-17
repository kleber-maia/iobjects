function PageControl_CreateTab(id, title, width) {
  // qde de tab´s
  var pagecontrolTabCount = eval(id + "pagecontrolTabCount");
  // nosso tabBar
  var pagecontrolTabBar = document.getElementById(id + "pagecontrolTabBar");
  // tab
  var pagecontrolTab = document.createElement("SPAN");
  pagecontrolTab.id          = id + "pagecontrolTab" + pagecontrolTabCount;
  pagecontrolTab.className   = "pagecontrolTab";
  pagecontrolTab.innerHTML   = title;
  pagecontrolTab.style.width = width;
  pagecontrolTab.onclick     = new Function("PageControl_SelectTab('" + id + "', " + pagecontrolTabCount + ");");
  pagecontrolTab.onmouseover = new Function("PageControl_SelectOver('" + id + "', " + pagecontrolTabCount + ");");
  pagecontrolTab.onmouseout  = new Function("PageControl_SelectOut('" + id + "', " + pagecontrolTabCount + ");");
  pagecontrolTabBar.insertBefore(pagecontrolTab, null);
  // se criamos o primeiro tab...seleciona
  if (pagecontrolTabCount == 0)
    PageControl_SelectTab(id, 0);
  // temos um tab a mais
  eval(id + "pagecontrolTabCount++");
}

function PageControl_HideTab(id, index) {
  // objetos que iremos alterar
  pagecontrolTab = document.getElementById(id + "pagecontrolTab" + index);
  // nossas variáveis
  var pageControlTabCount    = eval(id + "pagecontrolTabCount");
  var pageControlTabSelected = eval(id + "pagecontrolTabSelected");
  // se é a mesma selecionada...
  if (pageControlTabSelected == index) {
    // se é a primeira...seleciona a próxima
    if ((index == 0) && (pageControlTabCount > 1))
      PageControl_SelectTab(id, index+1);
    // se é qualquer outra...seleciona a anterior
    else if (pageControlTabCount > 1)
      PageControl_SelectTab(id, index-1);
    // se só temos uma...dispara
    else
      return;
  } // if
  // oculta
  pagecontrolTab.style.display = "none";
}

function PageControl_SelectOver(id, index) {
  // tab selecionada
  var pagecontrolTabSelected = eval(id + "pagecontrolTabSelected");
  // se é o tab selecionado...dispara
  if (pagecontrolTabSelected == index)
    return;
  // objetos que iremos alterar
  var pagecontrolTab = document.getElementById(id + "pagecontrolTab" + index);
  // altera o estilo
  pagecontrolTab.className = "pagecontrolTabOver";
}

function PageControl_SelectOut(id, index) {
  // tab selecionada
  var pagecontrolTabSelected = eval(id + "pagecontrolTabSelected");
  // se é o tab selecionado...dispara
  if (pagecontrolTabSelected == index)
    return;
  // objetos que iremos alterar
  var pagecontrolTab = document.getElementById(id + "pagecontrolTab" + index);
  // altera o estilo

  pagecontrolTab.className = "pagecontrolTab";
}

function PageControl_SelectTab(id, index) {
  // tab selecionada
  var pagecontrolTabSelected = eval(id + "pagecontrolTabSelected");
  // objetos atualmente com estilo de selecionados
  var pagecontrolTab          = document.getElementById(id + "pagecontrolTab" + pagecontrolTabSelected);
  var pagecontrolTabContainer = document.getElementById(id + "pagecontrolTabPanelContainer" + pagecontrolTabSelected);
  // altera o estilo para parecerem normais
  pagecontrolTab.className              = "pagecontrolTab";
  pagecontrolTabContainer.style.display = "none";
  // objetos que iremos alterar
  pagecontrolTab          = document.getElementById(id + "pagecontrolTab" + index);
  pagecontrolTabContainer = document.getElementById(id + "pagecontrolTabPanelContainer" + index);
  // altera o estilo para parecer selecionado
  pagecontrolTab.className              = "pagecontrolTabSelected";
  pagecontrolTabContainer.style.display = "block";
  // define o novo índice selecionado
  eval(id + "pagecontrolTabSelected = index;");
}

function PageControl_ShowTab(id, index) {
  // objetos que iremos alterar
  pagecontrolTab = document.getElementById(id + "pagecontrolTab" + index);
  // mostra somente a guia
  pagecontrolTab.style.display = "block";
}
