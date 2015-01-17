function ReportGrid_addRow(id, values) {
  // nossas vari�veis
  var columnAligns  = eval(id + "columnAligns");
  var columnFormats = eval(id + "columnFormats");
  // nossos objetos
  var dataTable = document.getElementById(id + "dataTable");
  // adiciona a linha
  document.write("<tr>");
  for (var i=0; i<columnAligns.length; i++) {
    // valor da vez
    var value = values[i];
    // se � um valor negativo...p�e me vermelho
    if (value.substr(0, 1) == "-" && ((columnFormats[i] == "integer") || (columnFormats[i] == "double")))
      value = "<span style=color:red>" + value + "</span>";
    // adiciona a linha
    document.write(  "<td class=\"" + (dataTable.childNodes.length % 2 > 0 ? "GridRowEven" : "GridRowOdd") + "\" "
                        + "align=\"" + columnAligns[i] + "\" "
                        + "valign=\"top\">"
                        + value
                        + "</td>");
  } // for
  document.write("</tr>");
}

function ReportGrid_begin(id) {
  // nossas vari�veis
  var columnNames  = eval(id + "columnNames");
  var columnWidths = eval(id + "columnWidths");
  var columnAligns = eval(id + "columnAligns");
  var width = eval(id + "width");
  // largura total das colunas
  var totalColumnWidths = 0;
  for (var i=0; i<columnWidths.length; i++)
    totalColumnWidths += columnWidths[i];
  // inicia o grid
  document.write("<p>");
  document.write("<table id=\"" + id + "\" class=\"Grid\" style=\"width:" + (width > 0 ? width + "px;" : "100%;") + "\">");
  // cabe�alho
  document.write(  "<tr>");
  for (var i=0; i<columnNames.length; i++) {
    document.write(  "<th class=\"GridHeader\" "
                        + "align=\"" + columnAligns[i] + "\" "
                        + "style=\"width:" + (width > 0 ? columnWidths[i] + "px;" : columnWidths[i] + "%;") + "\">"
                        + columnNames[i]
                        + "</td>");
  } // for
  document.write(  "</tr>");
  // tabela de dados
  document.write(  "<tbody id=\"" + id + "dataTable\">");
}

function ReportGrid_end(id, statusValues) {
  // nossas vari�veis
  var columnAligns = eval(id + "columnAligns");
  // fim da tabela de dados
  document.write(  "</tbody>");
  // rodap�
  document.write(  "<tfoot>");
  for (var i=0; i<columnAligns.length; i++) {
    document.write(  "<th class=\"GridFooter\" "
                        + "align=\"" + columnAligns[i] + "\">"
                        + statusValues[i]
                        + "</th>");
  } // for
  document.write(  "</tfoot>");
  // fim do grid
  document.write("</table>");
  document.write("</p>");
}