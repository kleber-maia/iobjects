var GRID_SELECT_TYPE_NONE     = 0;
var GRID_SELECT_TYPE_SINGLE   = 1;
var GRID_SELECT_TYPE_MULTIPLE = 2;

function Grid_addRow(id, values, keyValues) {
  Grid_row(id, values, keyValues, -1);
}

function Grid_begin(id) {
  // nossas variáveis
  var selectType      = eval(id + "selectType");
  var width           = eval(id + "width");
  var height          = eval(id + "height");
  var columnNames     = eval(id + "columnNames");
  var columnWidths    = eval(id + "columnWidths");
  var columnAligns    = eval(id + "columnAligns");
  var subtractPadding = !(BrowserDetect.browser == "MSIE" && BrowserDetect.version < 10);
  // largura total das colunas
  var totalColumnWidths = 0;
  for (var i=0; i<columnWidths.length; i++)
    totalColumnWidths += columnWidths[i] - (subtractPadding ? 8 : 0);
  // container do grid
  document.write("<table id=\"" + id + "\" cellpadding=\"0\" cellspacing=\"0\" class=\"Grid\" style=\"width:" + (width == 0 ? "100%;" : width + "px;") + " height:" + (height == 0 ? "100%;" : height + "px;") + " table-layout:fixed;\">");
  // container do cabeçalho
  document.write(  "<tr>");
  document.write(    "<td id=\"" + id + "HeaderContainer\" style=\"height:25px;\">");
  // cabeçalho
  document.write(      "<div id=\"" + id + "Header\" style=\"width:100%; height:100%; overflow:hidden;\">");
  document.write(        "<table class=\"GridHeader\" cellpadding=\"0\" cellspacing=\"0\" style=\"table-layout:fixed; width:" + totalColumnWidths + "px;\">");
  document.write(          "<tbody id=\"" + id + "HeaderDataTable\">");
  document.write(            "<tr>");
  document.write(              "<td align=\"center\" class=\"GridHeaderColumn\" style=\"width:" + (selectType == GRID_SELECT_TYPE_NONE ? 0 : 25 - (subtractPadding ? 8 : 0)) + "px; height:23px;\">!</td>");
  for (i=0; i<columnNames.length; i++)
    document.write  (          "<td align=\"" + columnAligns[i] + "\" class=\"GridHeaderColumn\" style=\"" + (columnWidths[i] == 0 ? "display:none;" : "") + "width:" + (columnWidths[i] - (subtractPadding ? 8 : 0)) + "; height:23px;\">" + columnNames[i] + "</td>");
  document.write(            "</tr>");
  document.write(          "</tbody>");
  document.write(        "</table>");
  document.write(      "</div>");
  // fim do container do cabeçalho
  document.write(    "</td>");
  document.write(  "</tr>");
  // container dos dados
  document.write(  "<tr>");
  document.write(    "<td id=\"" + id + "dataContainer\" style=\"height:auto;\">");
  document.write(      "<div id=\"" + id + "data\" class=\"GridRowOdd\" style=\"width:100%; height:100%; overflow:scroll;\" onscroll=\"" + id + "Header.scrollLeft = " + id + "data.scrollLeft;\">");
  // tabela de dados
  document.write(        "<table cellpadding=\"0\" cellspacing=\"0\" style=\"table-layout:fixed; width:" + totalColumnWidths + "px;\">");
  document.write(          "<tbody id=\"" + id + "dataTable\">");
}

function Grid_changeStatus(id, content) {
  // nossa barra de status
  var statusA = document.getElementById(id + "status");
  // altera
  statusA.innerHTML = content;
}

function Grid_checkAll(id) {
  // nossos seletores
  var selectors = document.getElementsByName(id + "selector");
  // loop
  for (var i=0; i<selectors.length; i++) {
    // seletor da vez
    var selector = selectors[i];
    // se não está marcado...marca
    if (!selector.checked)
      selector.click();
  } // for
}

function Grid_checkInvert(id) {
  // nossos seletores
  var selectors = document.getElementsByName(id + "selector");
  // loop
  for (var i=0; i<selectors.length; i++) {
    // seletor da vez
    var selector = selectors[i];
    // marca/desmarca
    selector.click();
  } // for
}

function Grid_checkRow(id, index) {
  // nossos seletores
  var selectors = document.getElementsByName(id + "selector");
  // seletor da vez
  var selector = selectors[index];
  // se não está marcado...marca
  if (!selector.checked)
    selector.click();
}

function Grid_clearRows(id) {
  // tabela de dados
  var dataTable = document.getElementById(id + "dataTable");
  // apaga tudo
  while (dataTable.childNodes.length > 0)
    dataTable.deleteRow(0);
}

function Grid_copyToClipboard(id) {
  // nossas variáveis
  var columnNames  = eval(id + "columnNames");
  var columnWidths = eval(id + "columnWidths");
  var dataTable    = document.getElementById(id + "dataTable");
  var selectType   = eval(id + "selectType");
  // nosso resultado
  var result = "";
  // títulos das colunas
  for (var w=0; w<columnNames.length; w++) {
    if (columnWidths[w] == 0)
      continue;
    result += (result != "" ? "\t" : "") + columnNames[w];
  } // for
  // loop nas linhas
  for (var i=0; i<dataTable.childNodes.length; i++) {
    // linha da vez
    var dataRow = dataTable.childNodes[i];
    var line = "";
    // quebra de linha
    result += "\r\n";
    // loop nas colunas
    for (w=1; w<dataRow.childNodes.length; w++) {
      // se é uma coluna oculta...continua
      if (columnWidths[w-1] == 0)
        continue;
      // célula da vez
      var cel = dataRow.childNodes[w];
      // se possui um input...
      if ((cel.childNodes.length > 0) && (cel.childNodes[0].type == "text"))
        line += (line != "" ? "\t" : "") + cel.childNodes[0].value;
      // se possui um select...
      else if ((cel.childNodes.length > 0) && (cel.childNodes[0].type == "select-one"))
        line += (line != "" ? "\t" : "") + cel.childNodes[0].options[cel.childNodes[0].selectedIndex].text;
      // se possui HTML...
      else {
        // seu conteúdo
        var value = cel.innerHTML;
        // remove todas as marcações HTML
        var index = value.indexOf("<", 0);
        while (index >= 0) {
          if (index == 0)
            value = value.substring(value.indexOf(">", 0)+1, value.length);
          else
            value = value.substring(0, index);
          index = value.indexOf("<", 0);
        } // while
        // remove os caracteres HTML
        value = value.replace("&nbsp;", " ", "");
        // seu conteúdo + tab
        line += (line != "" ? "\t" : "") + value;
      } // if
    } // for w
    // adiciona a linha
    result += line;
  } // for i
  // copia
  copyToClipboard(result);
  // avisa
  alert("Os dados exibidos foram copiados para a área de transferência. Utilize o comando Colar ou as teclas CTRL+V no aplicativo de destino.");
}

function Grid_deleteRow(id, index) {
  // efeito de exclusão
  $("#" + id + "dataTable > tr:eq(" + index + ")").attr("class", "GridRowDeleted").hide('slow', function() {
    $(this).remove();
    // tabela de dados
//    var dataTable = document.getElementById(id + "dataTable");
    // apaga a linha
//    dataTable.deleteRow(index);
    // organiza as cores das linhas
    Grid_reorder(id);
  });
}

function Grid_deleteSelectedRow(id) {
  // nossas variáveis
  var dataTable  = document.getElementById(id + "dataTable");
  var selectType = eval(id + "selectType");
  // linhas selecionadas
  var selected = Grid_selectedIndex(id);
  // se a seleção é simples...
  if (selectType == GRID_SELECT_TYPE_SINGLE)
    // dataTable.deleteRow(selected); -- antigo
    Grid_deleteRow(id, selected);
  // se a seleção é múltipla...
  else if (selectType == GRID_SELECT_TYPE_MULTIPLE) {
    for (var i=selected.length-1; i>=0; i--)
      // dataTable.deleteRow(selected[i]); -- antigo
      Grid_deleteRow(id, selected[i]);
  } // if
  // organiza as cores das linhas
  // Grid_reorder(id); -- antigo
}

function Grid_end(id, status) {
  // nossas variáveis
  var selectType = eval(id + "selectType");
  var columnIds  = eval(id + "columnIds");
  // temos campos editáveis?
  var isIE       = document.all != null;
  var hasText = false;
  for (var i=0; i<columnIds.length; i++) {
    hasText = columnIds[i] != "";
    if (hasText)
      break;
  } // for
  // fim da tabela de dados
  document.write(          "</tbody>");
  document.write(        "</table>");
  // fim do container dos dados
  document.write(      "</div>");
  document.write(    "</td>");
  document.write(  "</tr>");
  // container do status
  document.write(  "<tr>");
  document.write(    "<td id=\"" + id + "statusContainer\" style=\"height:22px;\">");
  // barra de status
  document.write(      "<table cellpading=\"0\" cellspacing=\"0\" style=\"width:100%; height:100%; overflow:hidden;\">");
  document.write(        "<tr>");
  document.write(          "<td class=\"GridStatus\" style=\"padding-left:2px; padding-right:2px;\" >");
  // ferramentas de status
  document.write(            "<table cellpadding=\"0\" cellspacing=\"0\" style=\"width:100%;\">");
  document.write(              "<tr>");
  document.write(                "<td id=\"" + id + "status\" style=\"width:50%;\">");
  document.write(                  status);
  document.write(                "</td>");
  document.write(                "<td style=\"width:50%;\" align=\"right\">");
  document.write(                  "<table><tr>");
  if (selectType == GRID_SELECT_TYPE_MULTIPLE) {
    document.write(                  "<td><input type=\"checkbox\" checked=\"checked\" disabled=\"disabled\" />&nbsp;</td><td><a href=\"javascript:Grid_checkAll(" + id + "id);\">Selecionar todos</a>&nbsp;&nbsp;&nbsp;</td>");
    document.write(                  "<td><input type=\"checkbox\" disabled=\"disabled\" />&nbsp;</td><td><a href=\"javascript:Grid_checkInvert(" + id + "id);\">Inverter seleção</a>&nbsp;&nbsp;&nbsp;</td>");
  } // if
  if (isIE) {
    document.write(                  "<td><img src=\"images/commands/copy.png\" align=\"absmiddle\" />&nbsp;</td><td><a href=\"javascript:Grid_copyToClipboard(" + id + "id);\">Copiar</a></td>");
  } // if
  if (hasText && isIE) {
    document.write(                  "<td>&nbsp;&nbsp;&nbsp;<img src=\"images/commands/paste.png\" align=\"absmiddle\" />&nbsp;</td><td><a href=\"javascript:Grid_pasteFromClipboard(" + id + "id);\">Colar</a></td>");
  } // if
  document.write(                  "</tr></table>");
  document.write(                "</td>");
  document.write(              "</tr>");
  document.write(            "</table>");
  document.write(          "</td>");
  document.write(        "</tr>");
  document.write("      </table>");
  // fim do container do status
  document.write(    "</td>");
  document.write(  "</tr>");
  // fim do container do grid
  document.write("</table>");
  // script para ajuste da área de dados
  /*
  document.write("<script type=\"text/javascript\">");
  document.write(  "var dataHeight = document.getElementById(\"" + id + "dataContainer\").offsetHeight;");
  document.write(  "if (dataHeight > 0) {");
  document.write(    "document.getElementById(\"" + id + "data\").style.height = dataHeight;");
  document.write(    "alert(dataHeight);");
  document.write(  "} else {");
  document.write(    "document.getElementById(\"" + id + "data\").style.height = \"100%\";");
  document.write(    "alert('100%');");
  document.write(  "}");
  document.write("</script>");
  */
}

function Grid_hideColumn(id, index) {
  // nossas variáveis
  var headerDataTable = document.getElementById(id + "HeaderDataTable");
  var dataTable       = document.getElementById(id + "dataTable");
  var hiddenColumns   = eval(id + "hiddenColumns");
  // se a coluna já está oculta...dispara
  if (hiddenColumns.indexOf("<" + index + ">") >= 0)
    return;
  // obtém as linhas das tabelas
  var headerRows = headerDataTable.getElementsByTagName('tr');
  var dataRows   = dataTable.getElementsByTagName('tr');
  // ocultando as colunas
  for (var i=0; i<headerRows.length; i++) {
    var cels = headerRows[i].getElementsByTagName('td')
    cels[index].style.display = "none";
  } // for
  for (var i=0; i<dataRows.length; i++) {
    var cels = dataRows[i].getElementsByTagName('td')
    cels[index].style.display = "none";
  } // for
  // adiciona a coluna na lista de ocultas
  eval(id + "hiddenColumns += \"<" + index + ">\";");
  hiddenColumns = eval(id + "hiddenColumns");
}

function Grid_insertRow(id, values, keyValues) {
  Grid_row(id, values, keyValues, 0);
}

function Grid_pasteFromClipboard(id) {
  // valor recebido
  var value = pasteFromClipboard();
  // se não temos nada...dispara
  if (value == null) {
    alert("Não existem dados para serem colados.");
    return;
  }
  // se não confirmar...dispara
  if (!confirm("Deseja mesmo sobrescrever os dados editáveis pelo conteúdo da área de transferência?"))
    return;
  // nossas variáveis
  var columnNames  = eval(id + "columnNames");
  var columnWidths = eval(id + "columnWidths");
  var dataTable    = document.getElementById(id + "dataTable");
  var selectType   = eval(id + "selectType");
  // separa em linhas
  var valueLines = value.split("\r\n", 9999);
  var ignoreLastLine = valueLines[valueLines.length-1] == "";
  // se não temos a quantidade correta de linhas...dispara
  if (valueLines.length != dataTable.childNodes.length + 1 /* cabeçaho */ + (ignoreLastLine ? 1 : 0) /* última linha em branco */) {
    alert("Os dados colados devem ter o mesmo tamanho e formato que os dados copiados: quantidade de linhas não combina.");
    return;
  } // if
  // separa em colunas
  var valueCols = valueLines[0].split("\t", 9999);
  // colunas visíveis
  var hiddenCols = 0;
  for (var i=0; i<columnWidths.length; i++)
    if (columnWidths[i] == 0)
      hiddenCols++;
  // se não temos a quantidade correta de colunas...dispara
  if (valueCols.length != columnNames.length - hiddenCols) {
    alert("Os dados colados devem ter o mesmo tamanho e formato que os dados copiados: quantidade de colunas não combina.");
    return;
  } // if
  // loop nas linhas
  for (var i=0; i<dataTable.childNodes.length; i++) {
    // linha da vez
    var dataRow = dataTable.childNodes[i];
    // linha colada da vez
    var valueLine = valueLines[i+1];
    // valores da linha
    var values = valueLine.split("\t", 9999);
    // loop nas colunas
    hiddenCols = 0;
    for (var w=0; w<dataRow.childNodes.length - 1; w++) {
      // se a coluna é oculta...continua
      if (columnWidths[w] == 0) {
        hiddenCols++;
        continue;
      } // if
      // célula da vez
      var cel = dataRow.childNodes[w + 1];
      // se possui um input...
      if ((cel.childNodes.length > 0) && (cel.childNodes[0].type == "text"))
        cel.childNodes[0].value = values[w - hiddenCols];
      // se possui um select...
      else if ((cel.childNodes.length > 0) && (cel.childNodes[0].type == "select-one")) {
        var select = cel.childNodes[0];
        var searchValue = values[w - hiddenCols].toUpperCase();
        for (var k=0; k<select.options.length; k++) {
          if (select.options[k].text.toUpperCase() == searchValue) {
            select.selectedIndex = k;
            break;
          } // if
        } // for k
      }
      // se possui HTML...
      else
        continue;
    } // for w
  } // for i
}

function Grid_reorder(id) {
/*  
  // nossos objetos
  var dataTable = document.getElementById(id + "dataTable");
  // organiza as cores das linhas
  for (var i=0; i<dataTable.childNodes.length; i++) {
    var row = dataTable.childNodes[i];
    row.className = (i % 2 > 0 ? "GridRowEven" : "GridRowOdd");
    row.setAttribute("oldClassName", row.className);
  } // for
*/
  // organiza as cores das linhas
  $("#" + id + "dataTable > tr:even").attr("class", "GridRowOdd");
  $("#" + id + "dataTable > tr:odd").attr("class", "GridRowEven");
}

function Grid_row(id, values, keyValues, index) {
  // nossas variáveis
  var selectType               = eval(id + "selectType");
  var selectKeys               = eval(id + "selectKeys");
  var columnNames              = eval(id + "columnNames");
  var columnWidths             = eval(id + "columnWidths");
  var columnAligns             = eval(id + "columnAligns");
  var dataTable                = document.getElementById(id + "dataTable");
  var columnIds                = eval(id + "columnIds");
  var columnMasks              = eval(id + "columnMasks");
  var columnSizes              = eval(id + "columnSizes");
  var columnConstraints        = eval(id + "columnConstraints");
  var columnConstraintMessages = eval(id + "columnConstraintMessages");
  var columnStyles             = eval(id + "columnStyles");
  var columnOnChangeScripts    = eval(id + "columnOnChangeScripts");
  var columnLookupLists        = eval(id + "columnLookupLists");
  var columnLookupListsValues  = eval(id + "columnLookupListsValues");
  var hiddenColumns            = eval(id + "hiddenColumns");
  var subtractPadding          = !(BrowserDetect.browser == "MSIE" && BrowserDetect.version < 10);
  // linha de referência
  var reference = (index < 0 ? null : dataTable.childNodes[index]);
  // estilo da linha
  var className = "";
  if (reference != null) {
    className = (reference.className == "GridRowOdd" ? "GridRowEven" : "GridRowOdd");
  }
  else {
    var lastRow = dataTable.lastChild;
    className = (lastRow == null ? "GridRowOdd" : lastRow.className == "GridRowOdd" ? "GridRowEven" : "GridRowOdd");
  } // if
  // linha
  var tr = document.createElement("TR");
  tr.className = className;
  //tr.setAttribute("oldClassName", className); -- antigo
  tr.onclick     = Grid_rowClick;
  tr.onmouseover = Grid_rowMouseOver;
  tr.onmouseout  = Grid_rowMouseOut;
  if (reference != null)
    dataTable.insertBefore(tr, reference);
  else
    dataTable.appendChild(tr);
  // seleção
  var td = document.createElement("TD");
  td.className = "GridRowColumn";
  td.style.textAlign = "center";
  td.style.width = (selectType == GRID_SELECT_TYPE_NONE ? 0 : 25 - (subtractPadding ? 8 : 0));
  if (hiddenColumns.indexOf("<0>") >= 0)
    td.style.display = "none";
  var content = "<input id=\"" + id + "selector\" name=\"" + id + "selector\" type=\"" + (selectType == GRID_SELECT_TYPE_SINGLE ? "radio" : "checkbox") + "\" " + (selectType == GRID_SELECT_TYPE_NONE ? "style=\"display:none;\"" : "") + " onclick=\"var node = this.nextSibling; while (node != null) { node.checked = this.checked; node = node.nextSibling; Grid_rowMouseOut(event);}\" " + (keyValues == null ? "disabled=disabled" : "") + " />";
  for (var i=0; i<selectKeys.length; i++)
    content += "<input id=\"" + selectKeys[i] + "\" name=\"" + selectKeys[i] + "\" value=\"" + (keyValues != null ? keyValues[i] : "") + "\" type=\"" + (selectType == GRID_SELECT_TYPE_SINGLE ? "radio" : "checkbox") + "\" style=\"display:none;\" />";
  td.innerHTML = content;
  tr.appendChild(td);
  // colunas
  for (i=0; i<columnNames.length; i++) {
    // cria e configura a coluna
    td = document.createElement("TD");
    td.className = "GridRowColumn";
    td.style.textAlign = columnAligns[i];
    if (columnWidths[i] == 0)
      td.style.display = 'none';
    td.style.width = columnWidths[i] - (subtractPadding ? 8 : 0);
    if (hiddenColumns.indexOf("<" + (i+1) + ">") >= 0)
      td.style.display = "none";
    // se é um Edit...
    if ((columnIds[i] != "") && (columnLookupLists[i].length == 0) && (values[i] != null)) {
      var dblClick = "return true;";
      var focus  = "";
      var keyUp    = "";
      var keyPress = "";
      if (columnMasks[i] == "$")
        focus = "$(this).maskMoney({thousands:'.', decimal:',', allowZero:true, allowNegative:true});";
      else if (!columnMasks[i] == "")
        focus = "$(this).inputmask('" + columnMasks[i].replaceAll('0', '9') + "');";
      /*
       antigo
      if (!columnMasks[i] == "") {
        keyUp    = "return inputMask(this, event, '" + columnMasks[i] + "', '0');";
        keyPress = "return validateMaskInput(this, event);";
      }
      else if (columnAligns[i] == "right") {
        keyPress += "return validateNumericInput(this, event);";
      } // if
      */
      if (!columnConstraints[i] == "")
        dblClick = "if (eval(" + columnConstraints[i] + ")) { return true; } else { alert('" + columnConstraintMessages[i] + "'); return false; }";
      td.innerHTML = "<input type=\"text\" id=\"" + columnIds[i] + "\" name=\"" + columnIds[i] + "\" value=\"" + values[i] + "\" " + (columnSizes[i] > 0 ? "maxlength=\"" + columnSizes[i] + "\"" : "") + "style=\"width:100%;text-align:" + columnAligns[i] + ";" + columnStyles[i] + "\" onchange=\"style.color='#0000ff';" + columnOnChangeScripts[i] + "\" onkeypress=\"" + keyPress + "\" onkeyup=\"" + keyUp + "\" ondblclick=\"" + dblClick + "\" onfocus=\"" + focus + "\" />";
    }
    // se é um Select...
    else if ((columnIds[i] != "") && (columnLookupLists[i].length > 0) && (values[i] != null)) {
      var dblClick = "return true;";
      if (!columnConstraints[i] == "")
        dblClick = "if (eval(" + columnConstraints[i] + ")) { return true; } else { alert('" + columnConstraintMessages[i] + "'); return false; }";
      var result = "<select size=\"1\" id=\"" + columnIds[i] + "\" name=\"" + columnIds[i] + "\" style=\"width:100%;text-align:" + columnAligns[i] + ";" + columnStyles[i] + "\" onchange=\"style.color='#0000ff';" + columnOnChangeScripts[i] + "\" ondblclick=\"" + dblClick + "\" />";
      for (var w=0; w<columnLookupLists[i].length; w++) {
        var value = (columnLookupListsValues[i].length == 0 ? w : columnLookupListsValues[i][w]);
        result += "<option value=\"" + value + "\" " + (values[i] == value ? "selected" : "") + ">" + columnLookupLists[i][w] + "</option>";
      } // for w
      result += "</select>"
      td.innerHTML = result;
    }
    // se não é um controle...
    else
      td.innerHTML = (values[i] != "" && values[i] != null ? values[i] : "&nbsp;");
    // adiciona
    tr.appendChild(td);
  } // for
}

function Grid_rowCount(id) {
  // tabela de dados
  var dataTable = document.getElementById(id + "dataTable");
  // quantidade de linhas
  return dataTable.childNodes.length;
}

function Grid_rowClick(e) {
  // procura pela linha
  var ev = e || event;
  var tr = ev.srcElement || ev.originalTarget;
  if (tr.tagName != "TD")
    return;
  while ((tr != null) && (tr.tagName != "TR"))
    tr = tr.parentNode;
  if (tr == null)
    return;
  // primeira coluna
  var td = tr.firstChild;
  // seletor
  var selector = td.firstChild;
  if ((selector != null) && (selector.style.display != 'none') && (selector.tagName == "INPUT"))
    selector.click();
}

function Grid_rowIndex(id, keyValues) {
  // nossas variáveis
  var selectKeys = eval(id + "selectKeys");
  // se não temos chaves...dispara
  if (selectKeys.length == 0)
    return new Array(0);
  // obtém os seletores e as chaves
  var selectors = new Array(selectKeys.length);
  for (var w=0; w<selectKeys.length; w++)
    selectors[w] = document.getElementsByName(selectKeys[w]);
  // procura por uma linha que combine com a chave informada
  for (var i=0; i<selectors[0].length; i++) {
    // compara a chave da linha e a informada
    var result = false;
    for (var w=0; w<selectKeys.length; w++) {
      result = selectors[w][i].value == keyValues[w];
      // se não combina...próxima linha
      if (!result)
        break;
    } // for w
    // se achamos...retorna
    if (result)
      return i;
  } // for i
  // se chegou aqui...não achamos nada
  return -1;
}

function Grid_rowMouseOver(e) {
  // procura pela linha
  var ev = e || event;
  var tr = ev.srcElement || ev.originalTarget;
  while ((tr != null) && (tr.tagName != "TR"))
    tr = tr.parentNode;
  // altera a aparência
  if (tr != null)
    $(tr).addClass("GridRowOver");
}

function Grid_rowMouseOut(e) {
  // procura pela linha
  var ev = e || event;
  var tr = ev.srcElement || ev.originalTarget;
  while ((tr != null) && (tr.tagName != "TR"))
    tr = tr.parentNode;
  // primeira coluna
  var td = tr.firstChild;
  // seletor
  var selector = td.firstChild;
  // está selecionada?
  var selected = ((selector != null) && (selector.tagName == "INPUT") && (selector.type == "checkbox") && (selector.checked));
  // se está selecionada...
  if (selected)
    $(tr).addClass("GridRowSelected");
  else
    $(tr).removeClass("GridRowSelected");
  // retorna a aparência
  $(tr).removeClass("GridRowOver");
}

/**
 * Retorna o(s) índice(s) da(s) linha(s) selecionada(s) dependendo do tipo
 * de seleção do Grid. Se o tipo de seleção for NONE, sempre retorna -1. Se o
 * tipo de seleção for SINGLE, retorna o índice da linha selecionada ou -1
 * se nenhuma linha estiver selecionada. Se o tipo de seleção for MULTIPLE,
 * sempre retorna um array contendo o(s) índice(s) da(s) linha(s) selecionada(s)
 * ou um array vazio se nenhuma linha estiver selecionada.
 */
function Grid_selectedIndex(id) {
  // nossas variáveis
  var selectType   = eval(id + "selectType");
  // se não temos seletor...dispara
  if (selectType == GRID_SELECT_TYPE_NONE)
    return -1;
  // se o seletor é simples...
  else if (selectType == GRID_SELECT_TYPE_SINGLE) {
    // obtém os seletores
    var selectors = document.getElementsByName(id + "selector");
    // procura por um selecionado
    for (var i=0; i<selectors.length; i++) {
      if (selectors[i].checked)
        return i;
    } // for
    // se chegou aqui...não tem nada selecionado
    return -1;
  }
  // se o seletor é múltiplo...
  else {
    // nosso resultado
    var count  = 0;
    var result = new Array(0);
    // obtém os seletores
    var selectors = document.getElementsByName(id + "selector");
    // procura pelos seletores selecionados
    for (var i=0; i<selectors.length; i++) {
      if (selectors[i].checked)
        result[count++] = i;
    } // for
    // retorna
    return result;
  }
}

/**
 * Retorna a(s) chave(s) da(s) linha(s) selecionada(s) dependendo do tipo
 * de seleção do Grid. Se o tipo de seleção for NONE, sempre retorna um array vazio.
 * Se o tipo de seleção for SINGLE, retorna um array contendo a(s) chave(s) da linha
 * selecionada ou um array vazio se nenhuma linha estiver selecionada.
 * Se o tipo de seleção for MULTIPLE, retorna um array onde cada item é um array
 * contendo a(s) chave(s) da(s) linha(s) selecionada(s) ou um array vazio se
 * nenhuma linha estiver selecionada.
 */
function Grid_selectedKeyValues(id) {
  // nossas variáveis
  var selectType = eval(id + "selectType");
  var selectKeys = eval(id + "selectKeys");
  // se não temos chaves...dispara
  if (selectKeys.length == 0)
    return new Array(0);
  // se não temos seletor...dispara
  if (selectType == GRID_SELECT_TYPE_NONE)
    return new Array(0);
  // se o seletor é simples...
  else if (selectType == GRID_SELECT_TYPE_SINGLE) {
    // obtém os seletores e as chaves
    var selectors = new Array(selectKeys.length);
    for (var w=0; w<selectKeys.length; w++)
      selectors[w] = document.getElementsByName(selectKeys[w]);
    // procura por um selecionado
    for (var i=0; i<selectors[0].length; i++) {
      if (selectors[0][i].checked) {
        var result = new Array(selectKeys.length);
        for (var w=0; w<selectKeys.length; w++)
          result[w] = selectors[w][i].value;
        return result;
      } // if
    } // for i
    // se chegou aqui...não tem nada selecionado
    return new Array(0);
  }
  // se o seletor é múltiplo...
  else {
    // nosso resultado
    var count  = 0;
    var result = new Array(0);
    // obtém os seletores
    var selectors = new Array(selectKeys.length);
    for (var w=0; w<selectKeys.length; w++)
      selectors[w] = document.getElementsByName(selectKeys[w]);
    // procura pelos seletores selecionados
    for (var i=0; i<selectors[0].length; i++) {
      if (selectors[0][i].checked) {
        var keyValues = new Array(selectKeys.length);
        for (var w=0; w<selectKeys.length; w++)
          keyValues[w] = selectors[w][i].value;
        result[count++] = keyValues;
      } // if
    } // for i
    // retorna
    return result;
  }
}

function Grid_showColumn(id, index) {
  // nossas variáveis
  var headerDataTable = document.getElementById(id + "HeaderDataTable");
  var dataTable       = document.getElementById(id + "dataTable");
  var hiddenColumns   = eval(id + "hiddenColumns");
  // se a coluna não está oculta...dispara
  if (hiddenColumns.indexOf("<" + index + ">") < 0)
    return;
  // obtém as linhas das tabelas
  var headerRows = headerDataTable.getElementsByTagName('tr');
  var dataRows   = dataTable.getElementsByTagName('tr');
  // exibindo as colunas
  for (var i=0; i<headerRows.length; i++) {
    var cels = headerRows[i].getElementsByTagName('td')
    cels[index].style.display = "block";
  } // for
  for (var i=0; i<dataRows.length; i++) {
    var cels = dataRows[i].getElementsByTagName('td')
    cels[index].style.display = "block";
  } // for
  // remove a coluna na lista de ocultas
  eval(id + "hiddenColumns = \"" + hiddenColumns.replace("<" + index + ">", "") + "\";");
  hiddenColumns = eval(id + "hiddenColumns");
}

function Grid_uncheckAll(id) {
  // nossos seletores
  var selectors = document.getElementsByName(id + "selector");
  // loop
  for (var i=0; i<selectors.length; i++) {
    // seletor da vez
    var selector = selectors[i];
    // se está marcado...desmarca
    if (selector.checked)
      selector.click();
  } // for
}

function Grid_updateRow(id, values, index) {
  // nossas variáveis
  var dataTable  = document.getElementById(id + "dataTable");
  var selectType = eval(id + "selectType");
  // obtém a linha desejada
  var dataRow = dataTable.childNodes[index];
  // loop nas colunas
  for (var i=0; i<dataRow.childNodes.length - 1; i++) {
    var col = dataRow.childNodes[i + 1];
    col.innerHTML = values[i];
  } // for
}
