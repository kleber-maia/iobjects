/**
 * Utilitário de detecção de browser.
 */
var BrowserDetect = {
	init: function () {
		this.browser = this.searchString(this.dataBrowser) || "An unknown browser";
		this.version = this.searchVersion(navigator.userAgent)
			|| this.searchVersion(navigator.appVersion)
			|| "an unknown version";
		this.OS = this.searchString(this.dataOS) || "an unknown OS";
	},
	searchString: function (data) {
		for (var i=0;i<data.length;i++)	{
			var dataString = data[i].string;
			var dataProp = data[i].prop;
			this.versionSearchString = data[i].versionSearch || data[i].identity;
			if (dataString) {
				if (dataString.indexOf(data[i].subString) != -1)
					return data[i].identity;
			}
			else if (dataProp)
				return data[i].identity;
		}
	},
	searchVersion: function (dataString) {
		var index = dataString.indexOf(this.versionSearchString);
		if (index == -1) return;
		return parseFloat(dataString.substring(index+this.versionSearchString.length+1));
	},
	dataBrowser: [
		{
			string: navigator.userAgent,
			subString: "Chrome",
			identity: "Chrome"
		},
		{
			string: navigator.vendor,
			subString: "Apple",
			identity: "Safari",
			versionSearch: "Version"
		},
		{
			prop: window.opera,
			identity: "Opera",
			versionSearch: "Version"
		},
		{
			string: navigator.userAgent,
			subString: "Firefox",
			identity: "Firefox"
		},
		{
			string: navigator.userAgent,
			subString: "MSIE",
			identity: "MSIE",
			versionSearch: "MSIE"
		}
	],
	dataOS : [
		{
			string: navigator.platform,
			subString: "Win",
			identity: "Windows"
		},
		{
			string: navigator.platform,
			subString: "Mac",
			identity: "Mac"
		},
		{
			   string: navigator.userAgent,
			   subString: "iPhone",
			   identity: "iPhone/iPod"
                },
		{
			   string: navigator.userAgent,
			   subString: "iPad",
			   identity: "iPad"
                },
		{
			string: navigator.platform,
			subString: "Linux",
			identity: "Linux"
		}
	]

};
BrowserDetect.init();





/**
 * Funções de Interface
 */

function copyToClipboard(value) {
  if (window.clipboardData)
    window.clipboardData.setData("Text", value);
  else {
    var copyDiv = document.createElement('div');
    copyDiv.contentEditable = true;
    document.body.appendChild(copyDiv);
    copyDiv.innerHTML = value;
    copyDiv.unselectable = "off";
    copyDiv.focus();
    document.execCommand('SelectAll', false, null);
    document.execCommand("Copy", false, null);
    document.body.removeChild(copyDiv);
  } // if
}

function pasteFromClipboard() {
  if (window.clipboardData)
    return window.clipboardData.getData("Text");
  else {
    var pasteDiv = document.createElement('div');
    pasteDiv.contentEditable = true;
    document.body.appendChild(pasteDiv);
    document.execCommand("Paste", false, null);
    var result = pasteDiv.innerHTML;
    document.body.removeChild(copyDiv);
    return result;
  } // if
}

function trim(value) {
  value = value.replace(new RegExp("^[\\s]+", "g"), "");
  value = value.replace(new RegExp("[\\s]+$", "g"), "");
  return value;
}

function showDefaultCursor() {
  document.body.style.cursor = "default";
}

function showWaitCursor() {
  document.body.style.cursor = "wait";
}

String.prototype.replaceAll = function(value, replacement) {
    var str = this;
    var pos = str.indexOf(value);
    while (pos > -1){
		str = str.replace(value, replacement);
		pos = str.indexOf(value);
	}
    return (str);
}




/**
 * Funções de Popup
 */

function windowClose() {
  // localiza a janela inicial
  var home = window;
  while (home.frameElement != null)
    home = home.parent;
  // localiza nossos objetos
  var modalBackground = home.document.getElementById("modalBackground");
  var modalWindow     = home.document.getElementById("modalWindow");
  // oculta tudo
  modalBackground.style.display = "none";
  modalWindow.style.display = "none";
}

function windowFrameLoad() {
  // localiza a janela inicial
  var home = window;
  while (home.frameElement != null)
    home = home.parent;
  // nossos objetos
  var modalWait          = home.document.getElementById("modalWait");
  var modalWindow        = home.document.getElementById("modalWindow");
  var modalWindowCaption = home.document.getElementById("modalWindowCaption");
  var modalWindowFrame   = home.document.getElementById("modalWindowFrame");
  // se não temos conteúdo...dispara
  if (modalWindowFrame.src == "")
    return;
  // oculta a espera
  modalWait.style.visibility = "hidden";
  // mostra a janela
  modalWindow.style.display = "block";
  // mostra o título
  modalWindowCaption.innerHTML = (modalWindowFrame.contentWindow ? modalWindowFrame.contentWindow.document.title : modalWindowFrame.contentDocument.title);
}

function windowOpen(url, width, height, position, showScrollBars) {
  // fecha o modal
  windowClose();
  // localiza a janela inicial
  var home = window;
  while (home.frameElement != null)
    home = home.parent;
  // localiza nossos objetos
  var modalBackground    = home.document.getElementById("modalBackground");
  var modalWait          = home.document.getElementById("modalWait");
  var modalWindow        = home.document.getElementById("modalWindow");
  var modalWindowCaption = home.document.getElementById("modalWindowCaption");
  var modalWindowFrame   = home.document.getElementById("modalWindowFrame");
  // área disponível na janela
  var availHeight = $(home).height() || (screen.availHeight - 32);
  var availWidth  = $(home).width() || (screen.availWidth - 10);
  // dimensões do plano de fundo
  modalBackground.style.width  = availWidth;
  modalBackground.style.height = availHeight;
  // exibe o plano de fundo
  $(modalBackground).fadeTo("fast", 0.30);
  modalWait.style.visibility = "visible";
  // tamanho da janela
  modalWindow.style.width  = width != null ? width : 640;
  modalWindow.style.height = width != null ? height + 33 : 480 + 33;
  // CENTER
  if (position == null || position == 1) {
    modalWindow.style.top  = (availHeight - $(modalWindow).height()) / 2;
    modalWindow.style.left = (availWidth - $(modalWindow).width()) / 2;
  }
  // LEFT BOTTOM
  else if (position == 2) {
    modalWindow.style.top  = (availHeight - $(modalWindow).height());
    modalWindow.style.left = 0;
  }
  // LEFT TOP
  else if (position == 3) {
    modalWindow.style.top  = 0;
    modalWindow.style.left = 0;
  }
  // RIGHT BOTTOM
  else if (position == 4) {
    modalWindow.style.top  = (availHeight - $(modalWindow).height());
    modalWindow.style.left = (availWidth - $(modalWindow).width());
  }
  // RIGHT TOP
  else if (position == 5) {
    modalWindow.style.top  = 0;
    modalWindow.style.left = (availWidth - $(modalWindow).width());
  } // if
  // guarda a janela que chamou a abertura do modal
  modalWindow.windowOpener = window;
  // carrega a URL
  modalWindowFrame.src = url;
}

function windowOpener() {
  // localiza a janela inicial
  var home = window;
  while (home.frameElement != null)
    home = home.parent;
  // localiza nosso objeto
  var modalWindow = home.document.getElementById("modalWindow");
  // retorna
  return modalWindow.windowOpener;
}






/**
 * Funções de Coockies
 */

function cookiesEnabled() {
  writeCookieValue("cookiesEnabled", "true");
  return readCookieValue("cookiesEnabled") == "true";
}

function readCookieValue(name) {
  if (document.cookie.length == 0)
    return null;
  begin = document.cookie.indexOf(name + "=");
  if (begin < 0)
    return null;
  begin += name.length + 1;
  end = document.cookie.indexOf(";", begin);
  if (end == -1)
    end = document.cookie.length;
  return document.cookie.substring(begin, end);
}

function readCookieDefaultValue(name, defaultValue) {
  result = readCookieValue(name);
  if (result == null)
    return defaultValue;
  else
    return result;
}

      
function removeAccents(texto, upperCase) {
  var chrEspeciais = new Array("á", "à", "â", "ã", "ä", "é", "è", "ê", "ë", 
             "í", "ì", "î", "ï", "ó", "ò", "ô", "õ", "ö", 
             "ú", "ù", "û", "ü", "ç", 
             "Á", "À", "Â", "Ã", "Ä", "É", "È", "Ê", "Ë", 
             "Í", "Ì", "Î", "Ï", "Ó", "Ò", "Ô", "Õ", "Ö", 
             "Ú", "Ù", "Û", "Ü", "Ç");
  var chrNormais = new Array("a", "a", "a", "a", "a", "e", "e", "e", "e", 
           "i", "i", "i", "i", "o", "o", "o", "o", "o",
           "u", "u", "u", "u", "c", 
           "A", "A", "A", "A", "A", "E", "E", "E", "E",
           "I", "I", "I", "I", "O", "O", "O", "O", "O",
           "U", "U", "U", "U", "C");
  for (index in chrEspeciais) {
    texto = texto.replace(chrEspeciais[index], chrNormais[index]);
  }
  if(upperCase)
    return texto.toUpperCase();
  else 
    return texto;
}

function writeCookieValue(name, value) {
  var ExpireDate = new Date();
  ExpireDate.setTime(ExpireDate.getTime() + (365 * 24 * 3600 * 1000));
  document.cookie = name + "=" + value + ";expires=" + ExpireDate.toGMTString();
}

function writeSecureCookieValue(name, value) {
  var argv = writeSecureCookieValue.arguments;
  var argc = writeSecureCookieValue.arguments.length;
  var expires = (argc > 2) ? argv[2] : null;
  var path = "/";
  var domain = (argc > 4) ? argv[4] : null;
  var secure = (argc > 5) ? argv[5] : false;
  document.cookie = name + "=" + escape (value) +
  ((expires == null) ? "" : ("; expires=" + expires.toGMTString())) +
  ((path == null) ? "" : ("; path=" + path)) +
  ((domain == null) ? "" : ("; domain=" + domain)) +
  ((secure == true) ? "; secure" : "");
}
