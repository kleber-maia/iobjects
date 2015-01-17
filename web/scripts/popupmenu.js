function popupMenuHide(popupId) {
  // nosso popup
  var popup = document.getElementById(popupId);
  // oculta
  popup.style.visibility = 'hidden';
}

function popupMenuShow(popupId, anchorId) {
  // nosso popup
  var popup = document.getElementById(popupId);
  // nossa âncora
  var anchor = document.getElementById(anchorId);
  // posição da âncora
  var aLeft   = anchor.offsetLeft;
  var aTop    = anchor.offsetTop;
  var aWidth  = anchor.offsetWidth;
  var aHeight = anchor.offsetHeight;
  var aParent = anchor.offsetParent;
  var pParent = popup.offsetParent;
  while ((aParent != null) && (aParent != pParent)) {
    aLeft  += aParent.offsetLeft;
    aTop   += aParent.offsetTop;
    aParent = aParent.offsetParent;
  } // while
  // posiciona a esquerda
  popup.style.left = aLeft;
  // espaço disponível abaixo e acima
  var belowHeight = document.body.offsetHeight - aTop - aHeight;
  var aboveHeight = aTop;
  // se temos espaço suficiente abaixo...
  if (belowHeight >= popup.offsetHeight)
    popup.style.top = aTop + aHeight;
  // se temos espaço suficiente acima...
  else if (aboveHeight >= popup.offsetHeight)
    popup.style.top = aTop - popup.offsetHeight;
  // se devemos redimensionar o popup
  else {
    // temos mais espaço abaixo?
    if (belowHeight > aboveHeight) {
      popup.style.height = belowHeight;
      popup.style.top    = aTop + aHeight;
    }
    // temos mais espaço acima?
    else {
      popup.style.height = aTop;
      popup.style.top    = aTop - popup.offsetHeight;
    } // if
  } // if
  // põe na frente de todos
  popup.style.zIndex = 123456;
  // mostra
  popup.style.visibility = 'visible';
}
