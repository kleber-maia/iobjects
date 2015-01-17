function Balloon_dontShowMeThisAgain(ID) {
  // chama a função Ajax do balão em questão
  eval(ID + "DontShowMeThisAgain();");
}

function Balloon_getAbsolutePos(ID, anchorID) {
  // localiza o objeto âncora, inclusive dentro de frames
  var anchorIDParts = (anchorID.indexOf(".") > 0 ? anchorID.split(".") : [anchorID]);
  var a     = document.getElementById(anchorIDParts[0]);
  var frame = null;
  if ((anchorIDParts.length > 1) && (a != null)) {
    frame = a;
    a = a.contentDocument.getElementById(anchorIDParts[1]);
  } // if
  // se não o encontramos...
  if (a == null) {
    var aWidth  = document.body.offsetWidth;
    var aHeight = document.body.offsetHeight;
    return {width:aWidth, height:aHeight};
  }
  // se o encontramos...
  else {
    var balloon = document.getElementById(ID);
    var aLeft   = a.offsetLeft + (frame != null ? $(frame).position().left : 0);
    var aTop    = a.offsetTop + (frame != null ? $(frame).position().top : 0);
    var aWidth  = a.offsetWidth;
    var aHeight = a.offsetHeight;
    var aParent = a.offsetParent;
    var bParent = balloon.offsetParent;
    while ((aParent != null) /*&& (aParent != bParent)*/) {
      aLeft  += aParent.offsetLeft;
      aTop   += aParent.offsetTop;
      aParent = aParent.offsetParent;
    } // while
    return {left:aLeft, top:aTop, width:aWidth, height:aHeight, anchor:a};
  } // if
}

function Balloon_hide(ID) {
  var Balloon      = document.getElementById(ID);
  var anchorTop    = document.getElementById(ID + "AnchorTop");
  var anchorBottom = document.getElementById(ID + "AnchorBottom");
  Balloon.style.visibility = "hidden";
  anchorTop.style.visibility = "hidden";
  anchorBottom.style.visibility = "hidden";
}

function Balloon_show(ID, anchorID, icon, caption, text, onClickScript, dontShowMeThisAgain) {
  // define o Ícone, Caption e o Texto
  var balloonIcon    = document.getElementById(ID + "Icon");
  var balloonCaption = document.getElementById(ID + "Caption");
  var balloonText    = document.getElementById(ID + "Text");
  balloonIcon.innerHTML    = "<img src=\"" + icon + "\"/>";
  balloonCaption.innerHTML = caption + (eval(ID + "StepCount") > 1 ? " (" + eval(ID + "Step") + " de " + eval(ID + "StepCount") + ")" : "");
  balloonText.innerHTML    = text;
  // mostrar a caixa "Não me mostre isso novamente"?
  var balloonExtra = document.getElementById(ID + "Extra");
  if (dontShowMeThisAgain) {
    balloonExtra.innerHTML = "<table align=right><tr><td><input type=\"checkbox\" style=\"width:16px;\" onclick=\"Balloon_dontShowMeThisAgain('" + ID + "');\" /></td><td>Não me avise novamente.</td></tr></table>";
  }
  else {
    balloonExtra = document.getElementById(ID + "Extra");
    balloonExtra.innerHTML = "";
  } // if
  // nosso balão e o apontador
  var Balloon      = document.getElementById(ID);
  var anchorTop    = document.getElementById(ID + "AnchorTop");
  var anchorBottom = document.getElementById(ID + "AnchorBottom");
  // obtém as dimensões do objeto âncora
  var objectAnchorDimensions = Balloon_getAbsolutePos(ID, anchorID);
  // se a âncora não foi encontrada...mostra no centro da tela
  if (objectAnchorDimensions.anchor == null) {
    Balloon.style.left = (objectAnchorDimensions.width - Balloon.offsetWidth) / 2;
    Balloon.style.top  = (objectAnchorDimensions.height - Balloon.offsetHeight) / 2;
    anchorTop.style.visibility = "hidden";
    anchorBottom.style.visibility = "hidden";
  }
  // se a âncora foi encontrada...
  else {
    // se o balão cabe acima da âncora...
    if (objectAnchorDimensions.top > Balloon.offsetHeight) {
      Balloon.style.top = objectAnchorDimensions.top - Balloon.offsetHeight;
      anchorTop.style.visibility = "hidden";
      anchorBottom.style.visibility = "visible";
    }
    // se não cabe acima...
    else {
      Balloon.style.top = objectAnchorDimensions.top + objectAnchorDimensions.height;
      anchorTop.style.visibility = "visible";
      anchorBottom.style.visibility = "hidden";
    } // if
    // se a âncora é maior que o balão...
    if (objectAnchorDimensions.width > Balloon.offsetWidth) {
      Balloon.style.left = objectAnchorDimensions.left + ((objectAnchorDimensions.width - Balloon.offsetWidth) / 2);
      anchorTop.align = "left";
      anchorBottom.align = "left";
    }
    // se o balão cabe a esquerda da âncora...
    else if (document.body.clientWidth - objectAnchorDimensions.left > Balloon.offsetWidth) {
      Balloon.style.left = objectAnchorDimensions.left;
      anchorTop.align = "left";
      anchorBottom.align = "left";
    }
    // se não cabe a esquerda...
    else {
      Balloon.style.left = objectAnchorDimensions.left + objectAnchorDimensions.width - Balloon.offsetWidth;
      anchorTop.align = "right";
      anchorBottom.align = "right";
    } // if
  } // if
  // OnClick
  Balloon.myClick = onClickScript;
  // mostra o balão
  Balloon.style.visibility = "visible";
}
