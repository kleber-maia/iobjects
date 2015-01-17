//
// openWYSIWYG v1.0 Copyright (c) 2006 openWebWare.com
// This copyright notice MUST stay intact for use.
//
// An open source WYSIWYG editor for use in web based applications.
// For full source code and docs, visit http://www.openwebware.com/
//
// This library is free software; you can redistribute it and/or modify
// it under the terms of the GNU Lesser General Public License as published
// by the Free Software Foundation; either version 2.1 of the License, or
// (at your option) any later version.
//
// This library is distributed in the hope that it will be useful, but
// WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
// or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
// License for more details.
//
// You should have received a copy of the GNU Lesser General Public License along
// with this library; if not, write to the Free Software Foundation, Inc., 59
// Temple Place, Suite 330, Boston, MA 02111-1307 USA


/* ---------------------------------------------------------------------- *\
  Global Variables: Set global variables such as images directory,
	                  WYSIWYG Height, Width, and CSS Directory.
\* ---------------------------------------------------------------------- */

// Images Directory
imagesDir = "images/wysiwyg/";

// Popups Directory
popupsDir = "ui/wysiwyg/";

/* ---------------------------------------------------------------------- *\
  Toolbar Settings: Set the features and buttons available in the WYSIWYG
	                  Toolbar.
\* ---------------------------------------------------------------------- */

var FontStyles = new Array();
    FontStyles[0] = "H1";
    FontStyles[1] = "H2";
    FontStyles[2] = "H3";
    FontStyles[3] = "NORMAL";
    
var FontStylesNames = new Array();
    FontStylesNames[0] = "Título 1";
    FontStylesNames[1] = "Título 2";
    FontStylesNames[2] = "Título 3";
    FontStylesNames[3] = "Normal";

var buttonName = new Array();
//  buttonName[0]  = "forecolor";
//  buttonName[1]  = "backcolor";
  buttonName[2]  = "seperator";
    buttonName[3]  = "bold";
    buttonName[4]  = "italic";
    buttonName[5]  = "underline";
    buttonName[6]  = "seperator";
      //buttonName[7]  = "cut";
      //buttonName[8]  = "copy";
      //buttonName[9]  = "paste";
      //buttonName[10]  = "seperator";
        buttonName[11]  = "inserttable";
        buttonName[12]  = "insertimage";
        buttonName[13]  = "createlink";
        buttonName[14]  = "seperator";
          buttonName[15] = "unorderedlist";
          buttonName[16] = "orderedlist";
          buttonName[17] = "outdent";
          buttonName[18] = "indent";
          buttonName[19]  = "seperator";
            buttonName[20]  = "justifyleft";
            buttonName[21]  = "justifycenter";
            buttonName[22] = "justifyright";
            buttonName[23] = "justifyfull";
            buttonName[24] = "seperator";
              buttonName[25]  = "viewSource";

// List of available actions and their respective ID and images
var ToolbarList = {
//Name              buttonID                 buttonTitle           buttonImage                            buttonImageRollover
  "bold":           ['Bold',                 'Negrito',               imagesDir + 'bold.gif',               imagesDir + 'bold_on.gif'],
  "italic":         ['Italic',               'Itálico',             imagesDir + 'italics.gif',            imagesDir + 'italics_on.gif'],
  "underline":      ['Underline',            'Sublinhado',          imagesDir + 'underline.gif',          imagesDir + 'underline_on.gif'],
	"strikethrough":  ['Strikethrough',        'Riscado',      imagesDir + 'strikethrough.gif',      imagesDir + 'strikethrough_on.gif'],
	"seperator":      ['',                     '',                   imagesDir + 'seperator.gif',          imagesDir + 'seperator.gif'],
	"subscript":      ['Subscript',            'Subescrito',          imagesDir + 'subscript.gif',          imagesDir + 'subscript_on.gif'],
	"superscript":    ['Superscript',          'Sobrescrito',        imagesDir + 'superscript.gif',        imagesDir + 'superscript_on.gif'],
	"justifyleft":    ['Justifyleft',          'Alinha à esquerda',        imagesDir + 'justify_left.gif',       imagesDir + 'justify_left_on.gif'],
	"justifycenter":  ['Justifycenter',        'Alinha ao centro',      imagesDir + 'justify_center.gif',     imagesDir + 'justify_center_on.gif'],
	"justifyright":   ['Justifyright',         'Alinha à direita',       imagesDir + 'justify_right.gif',      imagesDir + 'justify_right_on.gif'],
	"justifyfull":    ['Justifyfull',          'Justificado',           imagesDir + 'justify_full.gif',      imagesDir + 'justify_full_on.gif'],
	"unorderedlist":  ['InsertUnorderedList',  'Marcadores',imagesDir + 'list_unordered.gif',     imagesDir + 'list_unordered_on.gif'],
	"orderedlist":    ['InsertOrderedList',    'Numeração',  imagesDir + 'list_ordered.gif',       imagesDir + 'list_ordered_on.gif'],
	"outdent":        ['Outdent',              'Diminuir recuo',            imagesDir + 'indent_left.gif',        imagesDir + 'indent_left_on.gif'],
	"indent":         ['Indent',               'Aumentar recuo',             imagesDir + 'indent_right.gif',       imagesDir + 'indent_right_on.gif'],
	"cut":            ['Cut',                  'Recortar',                imagesDir + 'cut.gif',                imagesDir + 'cut_on.gif'],
	"copy":           ['Copy',                 'Copiar',               imagesDir + 'copy.gif',               imagesDir + 'copy_on.gif'],
  "paste":          ['Paste',                'Colar',              imagesDir + 'paste.gif',              imagesDir + 'paste_on.gif'],
	"forecolor":      ['ForeColor',            'Cor da fonte',          imagesDir + 'forecolor.gif',          imagesDir + 'forecolor_on.gif'],
	"backcolor":      ['BackColor',            'Realce',          imagesDir + 'backcolor.gif',          imagesDir + 'backcolor_on.gif'],
	"undo":           ['Undo',                 'Desfazer',               imagesDir + 'undo.gif',               imagesDir + 'undo_on.gif'],
	"redo":           ['Redo',                 'Refazer',               imagesDir + 'redo.gif',               imagesDir + 'redo_on.gif'],
	"inserttable":    ['InsertTable',          'Inserir tabela',        imagesDir + 'insert_table.gif',       imagesDir + 'insert_table_on.gif'],
	"insertimage":    ['InsertImage',          'Inserir imagem',        imagesDir + 'insert_picture.gif',     imagesDir + 'insert_picture_on.gif'],
	"createlink":     ['CreateLink',           'Inserir hyperlink',         imagesDir + 'insert_hyperlink.gif',   imagesDir + 'insert_hyperlink_on.gif'],
	"viewSource":     ['ViewSource',           'Ver código HTML',         imagesDir + 'view_source.gif',        imagesDir + 'view_source_on.gif'],
	"viewText":       ['ViewText',             'Ver texto',           imagesDir + 'view_text.gif',          imagesDir + 'view_text_on.gif'],
	"help":           ['Help',                 'Ajuda',               imagesDir + 'help.gif',               imagesDir + 'help_on.gif'],
	"selectstyle":     ['SelectStyle',           'Selecionar estilo',         imagesDir + 'select_size.gif',        imagesDir + 'select_size_on.gif']
};



/* ---------------------------------------------------------------------- *\
  Function    : insertAdjacentHTML(), insertAdjacentText() and insertAdjacentElement()
  Description : Emulates insertAdjacentHTML(), insertAdjacentText() and
	              insertAdjacentElement() three functions so they work with
								Netscape 6/Mozilla
  Notes       : by Thor Larholm me@jscript.dk
\* ---------------------------------------------------------------------- */
if(typeof HTMLElement!="undefined" && !HTMLElement.prototype.insertAdjacentElement){
  HTMLElement.prototype.insertAdjacentElement = function
  (where,parsedNode)
	{
	  switch (where){
		case 'beforeBegin':
			this.parentNode.insertBefore(parsedNode,this)
			break;
		case 'afterBegin':
			this.insertBefore(parsedNode,this.firstChild);
			break;
		case 'beforeEnd':
			this.appendChild(parsedNode);
			break;
		case 'afterEnd':
			if (this.nextSibling)
      this.parentNode.insertBefore(parsedNode,this.nextSibling);
			else this.parentNode.appendChild(parsedNode);
			break;
		}
	}

	HTMLElement.prototype.insertAdjacentHTML = function
  (where,htmlStr)
	{
		var r = this.ownerDocument.createRange();
		r.setStartBefore(this);
		var parsedHTML = r.createContextualFragment(htmlStr);
		this.insertAdjacentElement(where,parsedHTML)
	}


	HTMLElement.prototype.insertAdjacentText = function
  (where,txtStr)
	{
		var parsedText = document.createTextNode(txtStr)
		this.insertAdjacentElement(where,parsedText)
	}
};


// Create viewTextMode global variable and set to 0
// enabling all toolbar commands while in HTML mode
viewTextMode = 0;



/* ---------------------------------------------------------------------- *\
  Function    : generate_wysiwyg()
  Description : replace textarea with wysiwyg editor
  Usage       : generate_wysiwyg("textarea_id");
  Arguments   : textarea_id - ID of textarea to replace
\* ---------------------------------------------------------------------- */
function generate_wysiwyg(textareaID) {

  	// Hide the textarea
	document.getElementById(textareaID).style.display = 'none';

      // Pass the textareaID to the "n" variable.
      var n = textareaID;

      // WYSIWYG Width and Height
      wysiwygWidth = document.getElementById(textareaID).style.width;
      wysiwygHeight = document.getElementById(textareaID).style.height;

      // Toolbars width is 2 pixels wider than the wysiwygs
      //toolbarWidth = parseFloat(wysiwygWidth) + 2;
      toolbarWidth = wysiwygWidth;

  // Generate WYSIWYG toolbar one
  var toolbar;
  toolbar =  '<table cellpadding="0" cellspacing="0" style="width:' + toolbarWidth + '; height:25px; border:0px;"><tr><td id="toolbar' + n + '"  nowrap="nowrap" class="Toolbar">';

	// Create IDs for inserting Font Styles drop down
	toolbar += '<span id="FontStyles'  + n + '" style="width: 74px;"></span>';

	// Output all command buttons that belong to toolbar one
	for (var i = 0; i <= buttonName.length;) {
   if (buttonName[i]) {
	    var buttonObj            = ToolbarList[buttonName[i]];
     var buttonID             = buttonObj[0];
	    var buttonTitle          = buttonObj[1];
     var buttonImage          = buttonObj[2];
     var buttonImageRollover  = buttonObj[3];

     if (buttonName[i] == "seperator") {
       toolbar += '<span style="width:12px;" align="center"><img src="' + buttonImage + '" border=0 unselectable="on" width="2" height="18" hspace="2" unselectable="on"></span>';
     }
	    else if (buttonName[i] == "viewSource"){
       toolbar += '<span style="width: 22px;">';
       toolbar += '<span id="HTMLMode' + n + '" style="width:22px; height:22px;" class="ToolButton" onmouseover="className=\'ToolButtonOver\';" onmouseout="className=\'ToolButton\';" unselectable="on"><img src="'  + buttonImage +  '" border=0 unselectable="on" title="' + buttonTitle + '" id="' + buttonID + '" onClick="formatText(this.id,\'' + n + '\');"></span>';
       toolbar += '<span id="textMode' + n + '" style="width:22px; height:22px;" class="ToolButton" onmouseover="className=\'ToolButtonOver\';" onmouseout="className=\'ToolButton\';" unselectable="on"><img src="' + imagesDir + 'view_text.gif" border=0 unselectable="on" title="Ver texto"  id="ViewText"       onClick="formatText(this.id,\'' + n + '\');"></span>';
	      toolbar += '</span>';
  	  }
	    else {
       toolbar += '<span id="' +buttonID+ '" style="width:22px; height:22px;" class="ToolButton" onClick="formatText(this.id,\'' + n + '\');" onmouseover="className=\'ToolButtonOver\';" onmouseout="className=\'ToolButton\';"><img src="' + buttonImage + '" border=0 unselectable="on" title="' +buttonTitle+ '"></span>';
	    }
   } // if
   i++;
 } // for

  toolbar += '</td></tr></table>';

	// Create iframe which will be used for rich text editing
	var iframe = '<table cellpadding="0" cellspacing="0" border="0" style="width:100%; height:100%; border: 1px solid #CCCCCC;"><tr><td valign="top">\n'
  + '<iframe frameborder="0" id="wysiwyg' + n + '" textarea="' + n + '" onblur="updateTextArea(textarea);"></iframe>\n'
  + '</td></tr></table>\n';

  // cria o container para ajustar as barras de ferramentas e o iframe
  var container = '<table cellpading=0 cellspacing=0 style="width:' + wysiwygWidth + '; height:' + wysiwygHeight + '">'
                  + '<tr style="height:25px;">'
                    + '<td>'
                      + toolbar
                    + '</td>'
                  + '</tr>'
                  + '<tr style="height:auto;">'
                    + '<td>'
                      + iframe
                    + '</td>'
                  + '</tr>'
                + '</table>';

  // Insert after the textArea both toolbar one and two
  document.getElementById(n).insertAdjacentHTML("afterEnd", container);

  // Insert the Font Styles drop down into the toolbar
	outputFontStyles(n);

  // Hide the dynamic drop down list for the Font Styles
	hideFontStyles(n);

	// Hide the "Text Mode" button
	document.getElementById("textMode" + n).style.display = 'none';

  // Give the iframe the global wysiwyg height and width
  document.getElementById("wysiwyg" + n).style.height = wysiwygHeight + "";
  document.getElementById("wysiwyg" + n).style.width = wysiwygWidth + "";

	// Pass the textarea's existing text over to the content variable
  var content = document.getElementById(n).value;

	var doc = document.getElementById("wysiwyg" + n).contentWindow.document;

	// Write the textarea's content into the iframe
  doc.open();
  doc.write(content);
  doc.close();

	// Make the iframe editable in both Mozilla and IE
  doc.body.contentEditable = true;
  doc.designMode = "on";
};



/* ---------------------------------------------------------------------- *\
  Function    : formatText()
  Description : replace textarea with wysiwyg editor
  Usage       : formatText(id, n, selected);
  Arguments   : id - The execCommand (e.g. Bold)
                n  - The editor identifier that the command
		     affects (the textarea's ID)
                selected - The selected value when applicable (e.g. Arial)
\* ---------------------------------------------------------------------- */
function formatText(id, n, selected) {

  // When user clicks toolbar button make sure it always targets its respective WYSIWYG
  document.getElementById("wysiwyg" + n).contentWindow.focus();

	// When in Text Mode these execCommands are disabled
	var formatIDs = new Array("FontStyle","Bold","Italic","Underline","Subscript","Superscript","Strikethrough","Justifyleft","Justifyright","Justifycenter","Justifyfull","InsertUnorderedList","InsertOrderedList","Indent","Outdent","ForeColor","BackColor","InsertImage","InsertTable","CreateLink");

	// Check if button clicked is in disabled list
	for (var i = 0; i <= formatIDs.length;) {
		if (formatIDs[i] == id) {
			 var disabled_id = 1;
		}
	  i++;
	}

	// Check if in Text Mode and disabled button was clicked
	if (viewTextMode == 1 && disabled_id == 1) {
	  alert ("You are in HTML Mode. This feature has been disabled.");
	}

	else {

	  // FontStyle
	  if (id == "FontStyle") {
              //document.getElementById("wysiwyg" + n).contentWindow.document.execCommand("FontStyle", false, selected);
              if (selected == FontStyles[3])
                insertHTML('<p>Texto</p>', n);
              else
                insertHTML('<' + selected + '>Título</' + selected + '>', n);
	  }

		// FontName
	  else if (id == "FontName") {
              document.getElementById("wysiwyg" + n).contentWindow.document.execCommand("FontName", false, selected);
	  }

	  // ForeColor and BackColor
    else if (id == 'ForeColor' || id == 'BackColor') {
      var w = screen.availWidth;
      var h = screen.availHeight;
      var popW = 210, popH = 165;
      var leftPos = (w-popW)/2, topPos = (h-popH)/2;
      var currentColor = _dec_to_rgb(document.getElementById("wysiwyg" + n).contentWindow.document.queryCommandValue(id));

	    window.open(popupsDir + 'select_color.html?color=' + currentColor + '&command=' + id + '&wysiwyg=' + n,'popup','location=0,status=0,scrollbars=0,width=' + popW + ',height=' + popH + ',top=' + topPos + ',left=' + leftPos);
    }

	// InsertImage
        else if (id == "InsertImage") {
          var imagesPath = document.getElementById(n).imagesPath;
          var imagesUrl = document.getElementById(n).imagesUrl;
          var height = ((imagesPath != '') && (imagesUrl != '')) ? 406 : 210;
          window.open(popupsDir + 'insert_image.jsp?wysiwyg=' + n + '&imagesPath=' + imagesPath + '&imagesUrl=' + imagesUrl,'popupImage','location=0,status=0,scrollbars=0,resizable=0,width=400,height=' + height);
        }

	  // InsertTable
	  else if (id == "InsertTable") {
	    window.open(popupsDir + 'create_table.jsp?wysiwyg=' + n,'popupTable','location=0,status=0,scrollbars=0,resizable=0,width=400,height=362');
	  }

	  // CreateLink
	  else if (id == "CreateLink") {
            var documentsPath = document.getElementById(n).imagesPath;
            var documentsUrl = document.getElementById(n).imagesUrl;
            var height = ((imagesPath != '') && (imagesUrl != '')) ? 299 : 93;
	    window.open(popupsDir + 'insert_hyperlink.jsp?wysiwyg=' + n + '&documentsPath=' + documentsPath + '&documentsUrl=' + documentsUrl, 'popupHyperlink', 'location=0,status=0,scrollbars=0,resizable=0,width=400,height=' + height);
	  }

		// ViewSource
    else if (id == "ViewSource") {
	    viewSource(n);
	  }

		// ViewText
		else if (id == "ViewText") {
	    viewText(n);
	  }

		// Help
		else if (id == "Help") {
	    window.open(popupsDir + 'about.html','popup','location=0,status=0,scrollbars=0,resizable=0,width=400,height=330');
	  }

		// Every other command
	  else {
      document.getElementById("wysiwyg" + n).contentWindow.document.execCommand(id, false, null);
		}
  }
};



/* ---------------------------------------------------------------------- *\
  Function    : insertHTML()
  Description : insert HTML into WYSIWYG in rich text
  Usage       : insertHTML(<b>hello</b>, "textareaID")
  Arguments   : html - The HTML being inserted (e.g. <b>hello</b>)
                n  - The editor identifier that the HTML
								     will be inserted into (the textarea's ID)
\* ---------------------------------------------------------------------- */
function insertHTML(html, n) {

  var browserName = navigator.appName;

	 if (browserName == "Microsoft Internet Explorer") {
	   document.getElementById('wysiwyg' + n).contentWindow.document.selection.createRange().pasteHTML(html);
	 }
	 else {
	   var div = document.getElementById('wysiwyg' + n).contentWindow.document.createElement("div");
 	 	div.innerHTML = html;
	  	var node = insertNodeAtSelection(div, n);
	 }
}


/* ---------------------------------------------------------------------- *\
  Function    : insertNodeAtSelection()
  Description : insert HTML into WYSIWYG in rich text (mozilla)
  Usage       : insertNodeAtSelection(insertNode, n)
  Arguments   : insertNode - The HTML being inserted (must be innerHTML
	                           inserted within a div element)
                n          - The editor identifier that the HTML will be
								             inserted into (the textarea's ID)
\* ---------------------------------------------------------------------- */
function insertNodeAtSelection(insertNode, n) {
  // get current selection
  var sel = document.getElementById('wysiwyg' + n).contentWindow.getSelection();

  // get the first range of the selection
  // (there's almost always only one range)
  var range = sel.getRangeAt(0);

  // deselect everything
  sel.removeAllRanges();

  // remove content of current selection from document
  range.deleteContents();

  // get location of current selection
  var container = range.startContainer;
  var pos = range.startOffset;

  // make a new range for the new selection
  range=document.createRange();

  if (container.nodeType==3 && insertNode.nodeType==3) {

    // if we insert text in a textnode, do optimized insertion
    container.insertData(pos, insertNode.nodeValue);

    // put cursor after inserted text
    range.setEnd(container, pos+insertNode.length);
    range.setStart(container, pos+insertNode.length);
  }

	else {
    var afterNode;

		if (container.nodeType==3) {
      // when inserting into a textnode
      // we create 2 new textnodes
      // and put the insertNode in between

      var textNode = container;
      container = textNode.parentNode;
      var text = textNode.nodeValue;

      // text before the split
      var textBefore = text.substr(0,pos);
      // text after the split
      var textAfter = text.substr(pos);

      var beforeNode = document.createTextNode(textBefore);
      afterNode = document.createTextNode(textAfter);

      // insert the 3 new nodes before the old one
      container.insertBefore(afterNode, textNode);
      container.insertBefore(insertNode, afterNode);
      container.insertBefore(beforeNode, insertNode);

      // remove the old node
      container.removeChild(textNode);
    }

	  else {
      // else simply insert the node
      afterNode = container.childNodes[pos];
      container.insertBefore(insertNode, afterNode);
    }

    range.setEnd(afterNode, 0);
    range.setStart(afterNode, 0);
  }

  sel.addRange(range);
};



/* ---------------------------------------------------------------------- *\
  Function    : _dec_to_rgb
  Description : convert a decimal color value to rgb hexadecimal
  Usage       : var hex = _dec_to_rgb('65535');   // returns FFFF00
  Arguments   : value   - dec value
\* ---------------------------------------------------------------------- */

function _dec_to_rgb(value) {
  var hex_string = "";
  for (var hexpair = 0; hexpair < 3; hexpair++) {
    var myByte = value & 0xFF;            // get low byte
    value >>= 8;                          // drop low byte
    var nybble2 = myByte & 0x0F;          // get low nybble (4 bits)
    var nybble1 = (myByte >> 4) & 0x0F;   // get high nybble
    hex_string += nybble1.toString(16);   // convert nybble to hex
    hex_string += nybble2.toString(16);   // convert nybble to hex
  }
  return hex_string.toUpperCase();
};



/* ---------------------------------------------------------------------- *\
  Function    : outputFontStyles()
  Description : creates the Font Styles drop down and inserts it into
	              the toolbar
  Usage       : outputFontStyles(n)
  Arguments   : n   - The editor identifier that the Font Styles will update
	                    when making font changes (the textarea's ID)
\* ---------------------------------------------------------------------- */
function outputFontStyles(n) {

  var FontStyleObj       = ToolbarList['selectstyle'];
  var FontStyle          = FontStyleObj[2];
  var FontStyleOn        = FontStyleObj[3];

	FontStyles.sort();
	var FontStylesDropDown = new Array;
	FontStylesDropDown[n] = '<table border="0" cellpadding="0" cellspacing="0"><tr><td onMouseOver="document.getElementById(\'selectStyle' + n + '\').src=\'' + FontStyleOn + '\';" onMouseOut="document.getElementById(\'selectStyle' + n + '\').src=\'' + FontStyle + '\';"><img src="' + FontStyle + '" id="selectStyle' + n + '" width="70" height="20" onClick="showFontStyles(\'' + n + '\');" unselectable="on"><br>';
  FontStylesDropDown[n] += '<span id="Styles' + n + '" style="width: 290px; background-color: #FFFFFF; border: 1px solid #333333; height: 140px; overflow: auto; padding: 1px;">';

  for (var i=0; i<=FontStyles.length;i++) {
    if (FontStyles[i])
      FontStylesDropDown[n] += '<button onClick="formatText(\'FontStyle\',\'' + n + '\',\'' + FontStyles[i] + '\')\;hideFontStyles(\'' + n + '\');" style="width:100%; height:auto; border: 0px solid transparent; margin: 1px; padding: 0px; background: transparent; cursor:hand; text-align:left;"><table cellpadding="0" cellspacing="0" border="0"><tr><td align="left"><' + FontStyles[i] + '>' + FontStylesNames[i] + '</' + FontStyles[i] + '></td></tr></table></button><br>';
  } // for
	FontStylesDropDown[n] += '</span></td></tr></table>';
	document.getElementById('FontStyles' + n).insertAdjacentHTML("afterBegin", FontStylesDropDown[n]);
};



/* ---------------------------------------------------------------------- *\
  Function    : hideFonts()
  Description : Hides the list of font names in the font select drop down
  Usage       : hideFonts(n)
  Arguments   : n   - The editor identifier (the textarea's ID)
\* ---------------------------------------------------------------------- */
function hideFonts(n) {
  document.getElementById('Fonts' + n).style.display = 'none';
};



/* ---------------------------------------------------------------------- *\
  Function    : hideFontStyles()
  Description : Hides the list of font sizes in the font sizes drop down
  Usage       : hideFontStyles(n)
  Arguments   : n   - The editor identifier (the textarea's ID)
\* ---------------------------------------------------------------------- */
function hideFontStyles(n) {
  document.getElementById('Styles' + n).style.display = 'none';
};



/* ---------------------------------------------------------------------- *\
  Function    : showFontStyles()
  Description : Shows the list of font sizes in the font sizes drop down
  Usage       : showFonts(n)
  Arguments   : n   - The editor identifier (the textarea's ID)
\* ---------------------------------------------------------------------- */
function showFontStyles(n) {
  if (document.getElementById('Styles' + n).style.display == 'block') {
    document.getElementById('Styles' + n).style.display = 'none';
	}
  else {
    document.getElementById('Styles' + n).style.display = 'block';
    document.getElementById('Styles' + n).style.position = 'absolute';
    document.getElementById('Styles' + n).style.height = 200;
  }
};



/* ---------------------------------------------------------------------- *\
  Function    : viewSource()
  Description : Shows the HTML source code generated by the WYSIWYG editor
  Usage       : showFonts(n)
  Arguments   : n   - The editor identifier (the textarea's ID)
\* ---------------------------------------------------------------------- */
function viewSource(n) {
  var getDocument = document.getElementById("wysiwyg" + n).contentWindow.document;
  var browserName = navigator.appName;

	// View Source for IE
  if (browserName == "Microsoft Internet Explorer") {
    var iHTML = getDocument.body.innerHTML;
    getDocument.body.innerText = iHTML;
	}

  // View Source for Mozilla/Netscape
  else {
    var html = document.createTextNode(getDocument.body.innerHTML);
    getDocument.body.innerHTML = "";
    getDocument.body.appendChild(html);
	}

	// Hide the HTML Mode button and show the Text Mode button
  document.getElementById('HTMLMode' + n).style.display = 'none';
	document.getElementById('textMode' + n).style.display = 'block';

	// set the font values for displaying HTML source
	getDocument.body.style.fontSize = "12px";
	getDocument.body.style.fontFamily = "Courier New";

  viewTextMode = 1;
};



/* ---------------------------------------------------------------------- *\
  Function    : viewSource()
  Description : Shows the HTML source code generated by the WYSIWYG editor
  Usage       : showFonts(n)
  Arguments   : n   - The editor identifier (the textarea's ID)
\* ---------------------------------------------------------------------- */
function viewText(n) {
  var getDocument = document.getElementById("wysiwyg" + n).contentWindow.document;
  var browserName = navigator.appName;

	// View Text for IE
  if (browserName == "Microsoft Internet Explorer") {
    var iText = getDocument.body.innerText;
    getDocument.body.innerHTML = iText;
	}

	// View Text for Mozilla/Netscape
  else {
    var html = getDocument.body.ownerDocument.createRange();
    html.selectNodeContents(getDocument.body);
    getDocument.body.innerHTML = html.toString();
	}

	// Hide the Text Mode button and show the HTML Mode button
  document.getElementById('textMode' + n).style.display = 'none';
	document.getElementById('HTMLMode' + n).style.display = 'block';

	// reset the font values
  getDocument.body.style.fontSize = "";
	getDocument.body.style.fontFamily = "";
	viewTextMode = 0;
};



/* ---------------------------------------------------------------------- *\
  Function    : updateTextArea()
  Description : Updates the text area value with the HTML source of the WYSIWYG
  Usage       : updateTextArea(n)
  Arguments   : n   - The editor identifier (the textarea's ID)
\* ---------------------------------------------------------------------- */
function updateTextArea(n) {
  document.getElementById(n).value = document.getElementById("wysiwyg" + n).contentWindow.document.body.innerHTML;
};
