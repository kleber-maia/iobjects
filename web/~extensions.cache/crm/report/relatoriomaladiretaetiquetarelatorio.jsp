       
<%@page import="java.io.File"%>
<%@page import="java.sql.*"%>
<%@page import="java.util.*"%>

<%@page import="iobjects.*"%>
<%@page import="iobjects.entity.*"%>
<%@page import="iobjects.help.*"%>
<%@page import="iobjects.misc.*"%>
<%@page import="iobjects.process.*"%>
<%@page import="iobjects.report.*"%>
<%@page import="iobjects.servlet.*"%>
<%@page import="iobjects.sql.*"%>
<%@page import="iobjects.util.*"%>
<%@page import="iobjects.util.mail.*"%>
<%@page import="iobjects.util.print.*"%>
<%@page import="iobjects.ui.*"%>
<%@page import="iobjects.ui.ajax.*"%>
<%@page import="iobjects.ui.entity.*"%>
<%@page import="iobjects.ui.help.*"%>
<%@page import="iobjects.ui.param.*"%>
<%@page import="iobjects.ui.process.*"%>
<%@page import="iobjects.ui.report.*"%>
<%@page import="iobjects.ui.treeview.*"%>

<%
Facade facade2 = new Facade("");
%>

<%@page import="imanager.report.*"%>
<%@page import="imanager.process.*"%>

<%!
  public class EventListenerRelatorioMalaDiretaEtiqueta implements ReportGrid.EventListener {
    Chart     chart     = null;
    ResultSet resultSet = null;
    public EventListenerRelatorioMalaDiretaEtiqueta(Chart chart) {
      // nossos valores
      this.chart = chart;
    }
    public String onAddCell(String columnName,
                            String value) throws Exception {
      return value;
    }
    public void onRecord(ResultSet resultSet) throws Exception {
      // guarda o ResultSet
      this.resultSet = resultSet;
      // adiciona no gráfico
      chart.addValue(resultSet.getString(1), resultSet.getString(1), resultSet.getDouble(2)); 
    }
    public double onTotalizeColumn(String columnName,
                                   double total) throws Exception {
      return total;
    }
  }
%>

<%
  // início do bloco protegido
  try {
    // nossa instância de Mala Direta Etiqueta
    RelatorioMalaDiretaEtiqueta malaDiretaEtiqueta = new RelatorioMalaDiretaEtiqueta();

    // define os valores dos parâmetros de usuário
    malaDiretaEtiqueta.userParamList().setParamsValues(request);
%>

<html>
  <head>
    <title><%=RelatorioMalaDiretaEtiqueta.ACTION.getCaption()%></title>
    <script src="include/scripts.js" type="text/javascript"></script>
    <%
    malaDiretaEtiqueta.CONTROL_PARAM_ENDERECO_LIST.setValue(request.getParameter(malaDiretaEtiqueta.CONTROL_PARAM_ENDERECO_LIST.getName()));
    double margemSuperior = NumberTools.round(NumberTools.parseDouble(malaDiretaEtiqueta.USER_PARAM_MARGEM_SUPERIOR.getValue(), 3));
    double margemInferior = NumberTools.round(NumberTools.parseDouble(malaDiretaEtiqueta.USER_PARAM_MARGEM_SUPERIOR.getValue(), 3));
    double margemLateral = NumberTools.round(NumberTools.parseDouble(malaDiretaEtiqueta.USER_PARAM_MARGEM_LATERAL.getValue(), 3));
    %>
    <style type="text/css">
      @page {
        size: <%=malaDiretaEtiqueta.USER_PARAM_TAMANHO_PAGINA.getIntValue() == RelatorioMalaDiretaEtiqueta.TAMANHO_PAGINA_A4 ? "210mm 297mm" : "215.9mm 279.4mm"%>;
        margin:           0mm 0mm 0mm 0mm;
        padding:          0mm 0mm 0mm 0mm;
        font-size:        <%=malaDiretaEtiqueta.USER_PARAM_TAMANHO_FONTE.getIntValue() == RelatorioMalaDiretaEtiqueta.TAMANHO_FONTE_PEQUENA ? "5.5" : (malaDiretaEtiqueta.USER_PARAM_TAMANHO_FONTE.getIntValue() == RelatorioMalaDiretaEtiqueta.TAMANHO_FONTE_NORMAL ? "10" : "15")%>px;                  
      }
      body {
        font-family:      tahoma, verdana, arial, helvetica, sans-serif;
        color:            #2a2a2a;
        margin:           0mm 0mm 0mm 0mm;
        padding:          0mm 0mm 0mm 0mm;
        font-size:        <%=malaDiretaEtiqueta.USER_PARAM_TAMANHO_FONTE.getIntValue() == RelatorioMalaDiretaEtiqueta.TAMANHO_FONTE_PEQUENA ? "5.5" : (malaDiretaEtiqueta.USER_PARAM_TAMANHO_FONTE.getIntValue() == RelatorioMalaDiretaEtiqueta.TAMANHO_FONTE_NORMAL ? "10" : "15")%>px;                  
      }
      table, tr, td {
        margin:           0 0 0 0;
        padding:          0 0 0 0;
        font-size:        <%=malaDiretaEtiqueta.USER_PARAM_TAMANHO_FONTE.getIntValue() == RelatorioMalaDiretaEtiqueta.TAMANHO_FONTE_PEQUENA ? "5.5" : (malaDiretaEtiqueta.USER_PARAM_TAMANHO_FONTE.getIntValue() == RelatorioMalaDiretaEtiqueta.TAMANHO_FONTE_NORMAL ? "10" : "15")%>px;                  
      }
      .table-page {
        font-size:        <%=malaDiretaEtiqueta.USER_PARAM_TAMANHO_FONTE.getIntValue() == RelatorioMalaDiretaEtiqueta.TAMANHO_FONTE_PEQUENA ? "5.5" : (malaDiretaEtiqueta.USER_PARAM_TAMANHO_FONTE.getIntValue() == RelatorioMalaDiretaEtiqueta.TAMANHO_FONTE_NORMAL ? "10" : "15")%>px;          
        margin-top:       <%=margemSuperior%>mm; 
        margin-bottom:    <%=margemInferior%>mm; 
        margin-left:      <%=margemLateral%>mm; 
        margin-right:     <%=margemLateral%>mm;         
        padding:          0mm 0mm 0mm 0mm;
        
      }
    </style>    
  </head>
  <body style="" oncontextmenu="return false;">
    <%// nosso tamanho da pagina
    double tamanhoPagina = NumberTools.round((malaDiretaEtiqueta.USER_PARAM_TAMANHO_PAGINA.getIntValue() == RelatorioMalaDiretaEtiqueta.TAMANHO_PAGINA_A4 ? (297 - (margemSuperior + margemInferior)) : (279.4 - (margemSuperior + margemInferior))));
    double larguraPagina = NumberTools.round((malaDiretaEtiqueta.USER_PARAM_TAMANHO_PAGINA.getIntValue() == RelatorioMalaDiretaEtiqueta.TAMANHO_PAGINA_A4 ? (210 - (margemLateral * 2)) : (215.9 - (margemLateral * 2))));
    String[] enderecoList = FileTools.loadTextFile(facade2.tempLocalPath() + malaDiretaEtiqueta.CONTROL_PARAM_ENDERECO_LIST.getValue(), "").split("\r\n");
    Vector<String> vectorLinhas = new Vector<String>();
    // loop nos endereços, primeira linha so tem rotulos...
    for(int i = 1; i < enderecoList.length; i++) {
      String endereco = enderecoList[i];
      vectorLinhas.add(endereco);
    } // for
    double numeroPaginas = (double)(vectorLinhas.size() * (malaDiretaEtiqueta.USER_PARAM_ETIQUETAS_REGISTRO.getIntValue() > 0 ? malaDiretaEtiqueta.USER_PARAM_ETIQUETAS_REGISTRO.getIntValue() : 1)) / (double)(malaDiretaEtiqueta.USER_PARAM_QTD_COLUNAS.getIntValue() * malaDiretaEtiqueta.USER_PARAM_QTD_LINHAS.getIntValue()); 
    int count = 0;
    int countRegistro = 1;
    // começa em 1 pois a primeira 
    for (int i = 0; i < numeroPaginas; i++) {%>  
      <table class="table-page" border="0" style="page-break-after: <%=(i >= (numeroPaginas - 1) ? "avoid" : "always")%>" cellspacing="0" cellpadding="0">
        <% // verifica se estamos na primeira pagina...
        if (i == 0) {
          // loop nas linhas que devemos pular
          for (int w = 0; w < malaDiretaEtiqueta.USER_PARAM_LINHA_INICIO.getIntValue() - 1; w++) {
          if (NumberTools.parseDouble(malaDiretaEtiqueta.USER_PARAM_ESPACO_VERTICAL.getValue(), 3) > 0 && w > 0 && w < (malaDiretaEtiqueta.USER_PARAM_QTD_LINHAS.getIntValue() - 1)) {%>
              <tr style="height: <%=NumberTools.parseDouble(malaDiretaEtiqueta.USER_PARAM_ESPACO_VERTICAL.getValue(), 3)%>mm;">
                <% // loop nas colunas
                  for(int j = 0; j < malaDiretaEtiqueta.USER_PARAM_QTD_COLUNAS.getIntValue(); j++) {
                    // se não estamos na primeira coluna... da o espaçamento.
                    if (j != 0){%>
                      <td style="height: <%=tamanhoPagina/malaDiretaEtiqueta.USER_PARAM_QTD_LINHAS.getIntValue()%>mm;width: <%=NumberTools.parseDouble(malaDiretaEtiqueta.USER_PARAM_ESPACO_HORIZONTAL.getValue(), 3)%>mm;"></td>
                    <%}%>
                    <td style="width: <%=larguraPagina/malaDiretaEtiqueta.USER_PARAM_QTD_COLUNAS.getIntValue()%>mm;"></td>
                <%} // for j%>
              </tr>            
          <%}%>   
            <tr>
              <% // loop nas colunas
              for(int j = 0; j < malaDiretaEtiqueta.USER_PARAM_QTD_COLUNAS.getIntValue(); j++) {
                // se não estamos na primeira coluna... da o espaçamento.
                if (j != 0){%>
                  <td style="height: <%=tamanhoPagina/malaDiretaEtiqueta.USER_PARAM_QTD_LINHAS.getIntValue()%>mm;width: <%=NumberTools.parseDouble(malaDiretaEtiqueta.USER_PARAM_ESPACO_HORIZONTAL.getValue(), 3)%>mm;"></td>
                <%}%>
                <td  valign="top" align="left" style="padding: 0;height: <%=tamanhoPagina/malaDiretaEtiqueta.USER_PARAM_QTD_LINHAS.getIntValue()%>mm; width:  <%=(larguraPagina - (NumberTools.parseDouble(malaDiretaEtiqueta.USER_PARAM_ESPACO_HORIZONTAL.getValue(), 3) * (malaDiretaEtiqueta.USER_PARAM_QTD_COLUNAS.getIntValue() - 1)))/malaDiretaEtiqueta.USER_PARAM_QTD_COLUNAS.getIntValue()%>mm;">&nbsp;&nbsp;</td>
              <%} // for j%>
            </tr>
          <%} // for w
        } // if%>
        <%// loop nas linhas
        for(int w = (i == 0 ? (malaDiretaEtiqueta.USER_PARAM_LINHA_INICIO.getIntValue() - (malaDiretaEtiqueta.USER_PARAM_LINHA_INICIO.getIntValue() > 0 ? 1 : 0)) : 0); w < malaDiretaEtiqueta.USER_PARAM_QTD_LINHAS.getIntValue(); w++){ 
          if (NumberTools.parseDouble(malaDiretaEtiqueta.USER_PARAM_ESPACO_VERTICAL.getValue(), 3) > 0 && w > 0 && w < (malaDiretaEtiqueta.USER_PARAM_QTD_LINHAS.getIntValue() - 1)) {%>
              <tr>
                <% // loop nas colunas
                for(int j = 0; j < malaDiretaEtiqueta.USER_PARAM_QTD_COLUNAS.getIntValue(); j++) {
                  // se não estamos na primeira coluna... da o espaçamento.
                  if (j != 0){%>
                    <td style="height: <%=tamanhoPagina/malaDiretaEtiqueta.USER_PARAM_QTD_LINHAS.getIntValue()%>mm;width: <%=NumberTools.parseDouble(malaDiretaEtiqueta.USER_PARAM_ESPACO_HORIZONTAL.getValue(), 3)%>mm;"></td>
                  <%}%>
                  <td style="height: <%=NumberTools.parseDouble(malaDiretaEtiqueta.USER_PARAM_ESPACO_VERTICAL.getValue(), 3)%>mm;width:  <%=larguraPagina/malaDiretaEtiqueta.USER_PARAM_QTD_COLUNAS.getIntValue()%>mm;"></td>
                <%} // for j%>
              </tr>            
          <%}%>        
            <tr>
              <%for(int j = 0; j < malaDiretaEtiqueta.USER_PARAM_QTD_COLUNAS.getIntValue(); j++){
              // se não estamos na primeira coluna... da o espaçamento.
              if (j != 0){%>
                <td style="height: <%=tamanhoPagina/malaDiretaEtiqueta.USER_PARAM_QTD_LINHAS.getIntValue()%>mm;width: <%=NumberTools.parseDouble(malaDiretaEtiqueta.USER_PARAM_ESPACO_HORIZONTAL.getValue(), 3)%>mm;"></td>
              <%}
              if (count < vectorLinhas.size()) {%>
                <td  valign="top" align="left" style="padding: 0;height: <%=tamanhoPagina/malaDiretaEtiqueta.USER_PARAM_QTD_LINHAS.getIntValue()%>mm; width:  <%=(larguraPagina - (NumberTools.parseDouble(malaDiretaEtiqueta.USER_PARAM_ESPACO_HORIZONTAL.getValue(), 3) * (malaDiretaEtiqueta.USER_PARAM_QTD_COLUNAS.getIntValue() - 1)))/malaDiretaEtiqueta.USER_PARAM_QTD_COLUNAS.getIntValue()%>mm;"><%=vectorLinhas.get(count)%></td>
                  <% // verifica se devemos repitir as etiquetas
                  if (malaDiretaEtiqueta.USER_PARAM_ETIQUETAS_REGISTRO.getIntValue() <= 1) 
                    // incrementa noss count
                    count++;
                  else {
                    if (countRegistro < malaDiretaEtiqueta.USER_PARAM_ETIQUETAS_REGISTRO.getIntValue()) {
                      countRegistro++;
                    } else {
                      count++;
                      countRegistro = 1;
                    }
                  } // if
                } else {%> 
                  <td style="height: <%=tamanhoPagina/malaDiretaEtiqueta.USER_PARAM_QTD_LINHAS.getIntValue()%>mm;width: <%=(larguraPagina - NumberTools.parseDouble(malaDiretaEtiqueta.USER_PARAM_ESPACO_HORIZONTAL.getValue(), 3))/malaDiretaEtiqueta.USER_PARAM_QTD_COLUNAS.getIntValue()%>mm;"></td>
                <%} // if
              } // for j%>
            </tr>  
        <%} // for w%>
      </table>
    <%} // for i%>
  </body>
</html>

<%}
  // término do bloco protegido
  catch (Exception e) {
    Controller.forwardException(e, pageContext);
  } // try-catch
%>
