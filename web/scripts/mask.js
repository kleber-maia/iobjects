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
 * Põe a máscara definada por 'mask' e 'maskChar' em 'input' de acordo com a
 * tecla pressionada em 'event'. Esta função deve ser chamada a partir do método
 * onKeyUp de um Input.
 */
function inputMask(input, event, mask, maskChar) {
  // se está apenas leitura...dispara
  if (input.readOnly)
    return false;
  // tecla pressionada
  var key = (event.keyCode ? event.keyCode : event.which ? event.which : event.charCode);
  // se está se movendo pelo input...dispara
  if ((key == LEFT) ||
      (key == RIGHT) ||
      (key == CTRL) ||
      (key == ALT) ||
      (key == END) ||
      (key == HOME) ||
      (key == TAB))
    return true;
  // se está tentando alterar...apaga tudo e dispara
  if ((key == BACKSPACE) ||
      (key == DEL)) {
    input.value = "";
    return false;
  } // if
  // retira a máscara do valor
  var unmasked = removeCustomMask(input.value, mask, maskChar);
  // põe o valor com máscara
  input.value = formatCustomMask(unmasked, mask, maskChar);
}

/**
 * Retorna 'value' formatado com a máscara definida por 'mask' e 'maskChar'.
 */
function formatCustomMask(value, mask, maskChar) {
  // se não temos valor para formatar...dispara
  if (value == "")
    return "";
  // resultado = value + mask
  var result = "";
  // loop nos algarismos da máscara
  while (!mask == "") {
    // se a posição atual requer um algarismo do valor...
    if (mask.charAt(0) == maskChar) {
      result = result + value.charAt(0); // adiciona o algarismo
      value  = value.substring(1);       // apaga do valor
    }
    // se requer um caractere da máscara...
    else
      result += mask.charAt(0); // adiciona o caractere
    // apaga o caractere da máscara
    mask = mask.substring(1);
    // se não temos mais algarismos no valor...dispara
    if (value == "")
      break;
  }; // while
  // retorna o valor mascarado
  return result;
}

/**
 * Retorna 'maskedValue' sem a máscara definida por 'mask' e 'maskChar'.
 */
function removeCustomMask(maskedValue, mask, maskChar) {
  // se não temos valor para remover a máscara...dispara
  if (maskedValue == "")
    return "";
  // resultado = value - mask
  var result = "";
  // loop nos caracteres do valor
  for (i=0; i<maskedValue.length; i++) {
    // se ainda temos máscara para verificar...
    if (i <= mask.length-1) {
      // se o caractere do valor faz parte da máscara...continua
      if (maskedValue.charAt(i) == mask.charAt(i) &&
          maskedValue.charAt(i) != maskChar)
        continue;
    } // if
    // soma o caractere no resultado
    result = result + maskedValue.charAt(i);
  } // for
  // retorna o valor sem a máscara
  return result;
}

/**
 * Valida se o caractere digitado é valido para 'input' que está usando uma máscara.
 * Se o caractere digitado não for um número...descarta-o.
 */
function validateMaskInput(input, event) {
  // tecla pressionada
  var key = (event.keyCode ? event.keyCode : event.which ? event.which : event.charCode);
  // se não é um número...dispara
  if ((key < 48) || (key > 57))
    return false;
  else
    return true;
}

/**
 * Valida se o caractere digitado é valido para 'input' que está usando formato numérico.
 * Se o caractere digitado não for um número ou ','...descarta-o.
 */
function validateNumericInput(input, event) {
  // tecla pressionada
  var key = (event.keyCode ? event.keyCode : event.which ? event.which : event.charCode);
  // se não é um número, '-' ou ','...dispara
  if ((key != 45) && (key != 44) && !((key >= 48) && (key <= 57)))
    return false;
  else
    return true;
}
