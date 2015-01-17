/**
 * Retorna a coordenada X atual do ponteiro do mouse.
 * @return Retorna a coordenada X atual do ponteiro do mouse.
 */
function getMouseX() {
  return mouseX;
}

/**
 * Retorna a coordenada Y atual do ponteiro do mouse.
 * @return Retorna a coordenada Y atual do ponteiro do mouse.
 */
function getMouseY() {
  return mouseY;
}








// ------------------------------------------------------------------
// Evento para manter a posição do mouse
// ------------------------------------------------------------------


// verifica se estamos no isIE
var isIE = document.all ? true : false

// se não é o IE, vamos capturar os eventos do mouse
if (!isIE) document.captureEvents(Event.MOUSEMOVE)
  document.onmousemove = mouseMove;

// variáveis para armazenar a posição do mouse
var mouseX = 0;
var mouseY = 0;

// evento para guardar a posição do mouse
function mouseMove(e) {
  // se estamos no IE
  if (isIE) {
    mouseX = event.clientX + document.body.scrollLeft
    mouseY = event.clientY + document.body.scrollTop
  // se estamos em outro
  } else {
    mouseX = e.pageX
    mouseY = e.pageY
  }
  // se temos valores inválidos...ajusta
  if (mouseX < 0){mouseX = 0}
  if (mouseY < 0){mouseY = 0}

  return true
}
