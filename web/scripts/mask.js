var BACKSPACE = 8;
var TAB       = 9;
var SHIFT     = 16;
var CTRL      = 17;
var ALT       = 18;
var SPACE     = 32;
var END       = 35;
var HOME      = 36;
var LEFT      = 37;
var RIGHT     = 39;
var DEL       = 46;

/**
 * P�e a m�scara definada por 'mask' e 'maskChar' em 'input' de acordo com a
 * tecla pressionada em 'event'. Esta fun��o deve ser chamada a partir do m�todo
 * onKeyUp de um Input.
 */
function inputMask(input, event, mask, maskChar) {
  // se est� apenas leitura...dispara
  if (input.readOnly)
    return false;
  // tecla pressionada
  var key = (event.keyCode ? event.keyCode : event.which ? event.which : event.charCode);
  // se est� se movendo pelo input...dispara
  if ((key == LEFT) ||
      (key == RIGHT) ||
      (key == CTRL) ||
      (key == ALT) ||
      (key == END) ||
      (key == HOME) ||
      (key == TAB))
    return true;
  // se est� tentando alterar...apaga tudo e dispara
  if ((key == BACKSPACE) ||
      (key == DEL)) {
    input.value = "";
    return false;
  } // if
  // retira a m�scara do valor
  var unmasked = removeCustomMask(input.value, mask, maskChar);
  // p�e o valor com m�scara
  input.value = formatCustomMask(unmasked, mask, maskChar);
}

/**
 * Retorna 'value' formatado com a m�scara definida por 'mask' e 'maskChar'.
 */
function formatCustomMask(value, mask, maskChar) {
  // se n�o temos valor para formatar...dispara
  if (value == "")
    return "";
  // resultado = value + mask
  var result = "";
  // loop nos algarismos da m�scara
  while (!mask == "") {
    // se a posi��o atual requer um algarismo do valor...
    if (mask.charAt(0) == maskChar) {
      result = result + value.charAt(0); // adiciona o algarismo
      value  = value.substring(1);       // apaga do valor
    }
    // se requer um caractere da m�scara...
    else
      result += mask.charAt(0); // adiciona o caractere
    // apaga o caractere da m�scara
    mask = mask.substring(1);
    // se n�o temos mais algarismos no valor...dispara
    if (value == "")
      break;
  }; // while
  // retorna o valor mascarado
  return result;
}

/**
 * Retorna 'maskedValue' sem a m�scara definida por 'mask' e 'maskChar'.
 */
function removeCustomMask(maskedValue, mask, maskChar) {
  // se n�o temos valor para remover a m�scara...dispara
  if (maskedValue == "")
    return "";
  // resultado = value - mask
  var result = "";
  // loop nos caracteres do valor
  for (i=0; i<maskedValue.length; i++) {
    // se ainda temos m�scara para verificar...
    if (i <= mask.length-1) {
      // se o caractere do valor faz parte da m�scara...continua
      if (maskedValue.charAt(i) == mask.charAt(i) &&
          maskedValue.charAt(i) != maskChar)
        continue;
    } // if
    // soma o caractere no resultado
    result = result + maskedValue.charAt(i);
  } // for
  // retorna o valor sem a m�scara
  return result;
}

/**
 * Valida se o caractere digitado � valido para 'input' que est� usando uma m�scara.
 * Se o caractere digitado n�o for um n�mero...descarta-o.
 */
function validateMaskInput(input, event) {
  // tecla pressionada
  var key = (event.keyCode ? event.keyCode : event.which ? event.which : event.charCode);
  // se n�o � um n�mero...dispara
  if ((key < 48) || (key > 57))
    return false;
  else
    return true;
}

/**
 * Valida se o caractere digitado � valido para 'input' que est� usando formato num�rico.
 * Se o caractere digitado n�o for um n�mero ou ','...descarta-o.
 */
function validateNumericInput(input, event) {
  // tecla pressionada
  var key = (event.keyCode ? event.keyCode : event.which ? event.which : event.charCode);
  // se n�o � um n�mero, '-' ou ','...dispara
  if ((key != 45) && (key != 44) && !((key >= 48) && (key <= 57)))
    return false;
  else
    return true;
}
