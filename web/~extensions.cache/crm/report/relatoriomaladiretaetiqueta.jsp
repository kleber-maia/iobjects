     
<%@page import="java.net.URL"%>
<%@include file="../include/beans.jsp"%>

<%@page import="imanager.report.*"%>

<%
  // início do bloco protegido
  try {
    // nossa instância de Mala Direta Etiqueta
    RelatorioMalaDiretaEtiqueta malaDiretaEtiqueta = (RelatorioMalaDiretaEtiqueta)facade.getReport(RelatorioMalaDiretaEtiqueta.CLASS_NAME);

    // define os parâmetros, caso sejamos chamados por outro objeto
    malaDiretaEtiqueta.userParamList().setParamsValues(request);

    // nosso Form de relatório
    Form formReport = new Form(request, "formReportRelatorioMalaDiretaEtiqueta", RelatorioMalaDiretaEtiqueta.ACTION, RelatorioMalaDiretaEtiqueta.COMMAND_ETIQUETA, "", true);
    // nosso FrameBar
    FrameBar frameBar = new FrameBar(facade, "frameBarRelatorioMalaDiretaEtiqueta");
    // nosso ajax
    Ajax ajaxGerarPdf = new Ajax(facade, "ajaxGerarPdf", RelatorioMalaDiretaEtiqueta.ACTION_RELATORIO.getName(), RelatorioMalaDiretaEtiqueta.COMMAND_GERAR_PDF.getName());
    
    // nossa pagina do pdf
    String paginaPdf = "";
    
    if (Controller.getCommand(request).equals(malaDiretaEtiqueta.COMMAND_GERAR_PDF.getName())) {
      // action 
      String action = RelatorioMalaDiretaEtiqueta.ACTION_RELATORIO.getJsp().substring(RelatorioMalaDiretaEtiqueta.ACTION_RELATORIO.getJsp().lastIndexOf("/") + 1) + "?" + malaDiretaEtiqueta.CONTROL_PARAM_ENDERECO_LIST.getName() + "=" + malaDiretaEtiqueta.CONTROL_PARAM_ENDERECO_LIST.getValue() + "&" + malaDiretaEtiqueta.USER_PARAM_ESPACO_HORIZONTAL.getName() + "=" + malaDiretaEtiqueta.USER_PARAM_ESPACO_HORIZONTAL.getValue() + "&" + malaDiretaEtiqueta.USER_PARAM_ETIQUETAS_REGISTRO.getName() + "=" + malaDiretaEtiqueta.USER_PARAM_ETIQUETAS_REGISTRO.getValue() + "&" + malaDiretaEtiqueta.USER_PARAM_LINHA_INICIO.getName() + "=" + malaDiretaEtiqueta.USER_PARAM_LINHA_INICIO.getValue() + "&" + malaDiretaEtiqueta.USER_PARAM_MARGEM_LATERAL.getName() + "=" + malaDiretaEtiqueta.USER_PARAM_MARGEM_LATERAL.getValue() + "&" + malaDiretaEtiqueta.USER_PARAM_MARGEM_SUPERIOR.getName() + "=" + malaDiretaEtiqueta.USER_PARAM_MARGEM_SUPERIOR.getValue() + "&" + malaDiretaEtiqueta.USER_PARAM_QTD_COLUNAS.getName() + "=" + malaDiretaEtiqueta.USER_PARAM_QTD_COLUNAS.getValue() + "&" + malaDiretaEtiqueta.USER_PARAM_QTD_LINHAS.getName() + "=" + malaDiretaEtiqueta.USER_PARAM_QTD_LINHAS.getValue() + "&" + malaDiretaEtiqueta.USER_PARAM_TAMANHO_PAGINA.getName() + "=" + malaDiretaEtiqueta.USER_PARAM_TAMANHO_PAGINA.getValue() + "&" + malaDiretaEtiqueta.USER_PARAM_ESPACO_VERTICAL.getName() + "=" + malaDiretaEtiqueta.USER_PARAM_ESPACO_VERTICAL.getValue() + "&" + malaDiretaEtiqueta.USER_PARAM_TAMANHO_FONTE.getName() + "=" + malaDiretaEtiqueta.USER_PARAM_TAMANHO_FONTE.getValue();
      // altera nome da pagina
      paginaPdf = facade.getDefaultConnectionName() + "." + facade.getLoggedUser().getId() + ".pdf";
      // gera pdf
      PDFWriter pdf = new PDFWriter(new URL(request.getRequestURL().substring(0, request.getRequestURL().lastIndexOf("/") + 1) + action), facade.tempLocalPath() + paginaPdf); 
      // inclui endereço
      paginaPdf = request.getRequestURL().substring(0, (request.getRequestURL().toString().contains("http://") ? request.getRequestURL().substring(7).indexOf("/") + 8 : request.getRequestURL().indexOf("/") + 1)) + "~temp/" + paginaPdf; 
    }
%>

<html>
  <head>
    <title><%=RelatorioMalaDiretaEtiqueta.ACTION.getCaption()%></title>
    <link href="<%=facade.getStyle().userInterface()%>" rel="stylesheet" type="text/css">
    <script src="include/scripts.js" type="text/javascript"></script>
  </head>
  <body style="margin:0px;" onselectstart="return true;" oncontextmenu="return false;">

    <script type="text/javascript">
      function onChangeEtiquetas(){
        var campo = document.getElementById("<%=malaDiretaEtiqueta.USER_PARAM_ETIQUETAS.getName()%>");
        var tamanho_pagina = document.getElementById("<%=malaDiretaEtiqueta.USER_PARAM_TAMANHO_PAGINA.getName()%>");
        var qtd_linhas = document.getElementById("<%=malaDiretaEtiqueta.USER_PARAM_QTD_LINHAS.getName()%>");
        var qtd_colunas = document.getElementById("<%=malaDiretaEtiqueta.USER_PARAM_QTD_COLUNAS.getName()%>");
        var margem_superior = document.getElementById("<%=malaDiretaEtiqueta.USER_PARAM_MARGEM_SUPERIOR.getName()%>");
        var margem_lateral = document.getElementById("<%=malaDiretaEtiqueta.USER_PARAM_MARGEM_LATERAL.getName()%>");
        var espaco_horizontal = document.getElementById("<%=malaDiretaEtiqueta.USER_PARAM_ESPACO_HORIZONTAL.getName()%>"); 
        var espaco_vertical = document.getElementById("<%=malaDiretaEtiqueta.USER_PARAM_ESPACO_VERTICAL.getName()%>"); 
        // se o valor do campo é igual a etiquetas 10 x 3... altera valores
        if (campo.value == <%=RelatorioMalaDiretaEtiqueta.ETIQUETAS_NENHUM%>) {
          tamanho_pagina[0].selected = "1";
          qtd_linhas.value = "1";
          qtd_colunas.value = "1";
          margem_superior.value = "0,00";
          margem_lateral.value = "0,00";
          espaco_horizontal.value = "0,00";
          espaco_vertical.value = "0,00";
        }
        // se o valor do campo é igual a etiquetas 10 x 3... altera valores
        else if (campo.value == <%=RelatorioMalaDiretaEtiqueta.ETIQUETAS_10_3%>) {
          tamanho_pagina[1].selected = "1";
          qtd_linhas.value = "10";
          qtd_colunas.value = "3";
          margem_superior.value = "12,70";
          margem_lateral.value = "4,80";
          espaco_horizontal.value = "3,10";
          espaco_vertical.value = "0,00";
        }        
        // se o valor do campo é igual a etiquetas 10 x 2... altera valores
        else if (campo.value == <%=RelatorioMalaDiretaEtiqueta.ETIQUETAS_10_2%>) {
          tamanho_pagina[1].selected = "1";
          qtd_linhas.value = "10";
          qtd_colunas.value = "2";
          margem_superior.value = "12,70";
          margem_lateral.value = "3,77";
          espaco_horizontal.value = "5,20";
          espaco_vertical.value = "0,00";
        }
        // se o valor do campo é igual a etiquetas 7 x 2... altera valores
        else if (campo.value == <%=RelatorioMalaDiretaEtiqueta.ETIQUETAS_7_2%>) {
          tamanho_pagina[1].selected = "1";
          qtd_linhas.value = "7";
          qtd_colunas.value = "2";
          margem_superior.value = "21,20";
          margem_lateral.value = "3,77";
          espaco_horizontal.value = "5,16";
          espaco_vertical.value = "0,00";
        } // if             
        // se o valor do campo é igual a etiquetas 5 x 2... altera valores
        else if (campo.value == <%=RelatorioMalaDiretaEtiqueta.ETIQUETAS_5_2%>) {
          tamanho_pagina[1].selected = "1";
          qtd_linhas.value = "5";
          qtd_colunas.value = "2";
          margem_superior.value = "12,70";
          margem_lateral.value = "3,77";
          espaco_horizontal.value = "5,16";
          espaco_vertical.value = "0,00";
        } // if      
        // se o valor do campo é igual a etiquetas 15 x 4... altera valores
        else if (campo.value == <%=RelatorioMalaDiretaEtiqueta.ETIQUETAS_15_4%>) {
          tamanho_pagina[1].selected = "1";
          qtd_linhas.value = "15";
          qtd_colunas.value = "4";
          margem_superior.value = "12,70";
          margem_lateral.value = "14,50";
          espaco_horizontal.value = "3,05";
          espaco_vertical.value = "0,00";
        }        
        // se o valor do campo é igual a etiquetas 6 x 4... altera valores
        else if (campo.value == <%=RelatorioMalaDiretaEtiqueta.ETIQUETAS_6_4%>) {
          tamanho_pagina[1].selected = "1";
          qtd_linhas.value = "6";
          qtd_colunas.value = "4";
          margem_superior.value = "7,796";
          margem_lateral.value = "7,796";
          espaco_horizontal.value = "10,211";
          espaco_vertical.value = "1,981";
        }
        // se o valor do campo é igual a etiquetas 11 x 2... altera valores
        else if (campo.value == <%=RelatorioMalaDiretaEtiqueta.ETIQUETAS_A_11_2%>) {
          tamanho_pagina[0].selected = "1";
          qtd_linhas.value = "11";
          qtd_colunas.value = "2";
          margem_superior.value = "8,80";
          margem_lateral.value = "4,70";
          espaco_horizontal.value = "2,60";
          espaco_vertical.value = "0,00";
        }
        // se o valor do campo é igual a etiquetas 11 x 3... altera valores
        else if (campo.value == <%=RelatorioMalaDiretaEtiqueta.ETIQUETAS_A_11_3%>) {
          tamanho_pagina[0].selected = "1";
          qtd_linhas.value = "11";
          qtd_colunas.value = "3";
          margem_superior.value = "8,80";
          margem_lateral.value = "7,15";
          espaco_horizontal.value = "2,60";
          espaco_vertical.value = "0,00";
        }
        // se o valor do campo é igual a etiquetas 7 x 3... altera valores
        else if (campo.value == <%=RelatorioMalaDiretaEtiqueta.ETIQUETAS_A_7_3%>) {
          tamanho_pagina[0].selected = "1";
          qtd_linhas.value = "7";
          qtd_colunas.value = "3";
          margem_superior.value = "15,15";
          margem_lateral.value = "7,15";
          espaco_horizontal.value = "2,60";
          espaco_vertical.value = "0,00";
        }
        // se o valor do campo é igual a etiquetas 8 x 2... altera valores
        else if (campo.value == <%=RelatorioMalaDiretaEtiqueta.ETIQUETAS_A_8_2%>) {
          tamanho_pagina[0].selected = "1";
          qtd_linhas.value = "8";
          qtd_colunas.value = "2";
          margem_superior.value = "12,90";
          margem_lateral.value = "4,70";
          espaco_horizontal.value = "2,60";
          espaco_vertical.value = "0,00";
        }
        // se o valor do campo é igual a etiquetas 7 x 2... altera valores
        else if (campo.value == <%=RelatorioMalaDiretaEtiqueta.ETIQUETAS_A_7_2%>) {
          tamanho_pagina[0].selected = "1";
          qtd_linhas.value = "7";
          qtd_colunas.value = "2";
          margem_superior.value = "15,15";
          margem_lateral.value = "4,70";
          espaco_horizontal.value = "2,60";
          espaco_vertical.value = "0,00";
        }
      }      
    </script>

    <!-- Form de relatório -->
    <%=formReport.begin()%>
      <%=ParamFormEdit.script(facade, malaDiretaEtiqueta.CONTROL_PARAM_ENDERECO_LIST, -1, "", "")%>
      <table style="width:100%;" border="0">
        <tr>
          <td style="width: 150px"><%=ParamLabel.script(facade, malaDiretaEtiqueta.USER_PARAM_ETIQUETAS)%></td>
          <td style="width: auto"><%=ParamFormSelect.script(facade, malaDiretaEtiqueta.USER_PARAM_ETIQUETAS, 0, "", "javascript:onChangeEtiquetas();")%></td>
          <td style="width: 100px;">
            <%=Button.script(facade,
                           "actionButton" + formReport.getAction().getName(),
                           RelatorioMalaDiretaEtiqueta.COMMAND_GERAR_PDF.getCaption(),
                           RelatorioMalaDiretaEtiqueta.COMMAND_GERAR_PDF.getDescription(),
                           ImageList.COMMAND_PRINT,
                           "",
                           Button.KIND_DEFAULT,
                           "width: 100px;",
                           (true && !formReport.getUseAjax() ? "showWaitCursor();" : "") + formReport.submitCustomCommandScript(RelatorioMalaDiretaEtiqueta.COMMAND_GERAR_PDF, "", true, false),
                           false || !facade.getLoggedUser().hasAccessRight(formReport.getAction(), RelatorioMalaDiretaEtiqueta.COMMAND_GERAR_PDF))%>
          </td>                    
        </tr>
      </table>
      <%=GroupBox.begin("Parâmetros")%>
        <table style="width:400px;">
          <tr>
            <td style="width: 145px"><%=ParamLabel.script(facade, malaDiretaEtiqueta.USER_PARAM_TAMANHO_PAGINA)%></td>
            <td  style="width: auto;" align="left"><%=ParamFormSelect.script(facade, malaDiretaEtiqueta.USER_PARAM_TAMANHO_PAGINA, 140, "", "")%></td>
          </tr>
          <tr>
            <td><%=ParamLabel.script(facade, malaDiretaEtiqueta.USER_PARAM_TAMANHO_FONTE)%></td>
            <td align="left"><%=ParamFormSelect.script(facade, malaDiretaEtiqueta.USER_PARAM_TAMANHO_FONTE, 100, "", "")%></td>
          </tr>          
          <tr>
            <td><%=ParamLabel.script(facade, malaDiretaEtiqueta.USER_PARAM_QTD_LINHAS)%></td>
            <td align="left"><%=ParamFormEdit.script(facade, malaDiretaEtiqueta.USER_PARAM_QTD_LINHAS, 70, "", "")%></td>
          </tr>
          <tr>
            <td><%=ParamLabel.script(facade, malaDiretaEtiqueta.USER_PARAM_QTD_COLUNAS)%></td>
            <td align="left"><%=ParamFormEdit.script(facade, malaDiretaEtiqueta.USER_PARAM_QTD_COLUNAS, 70, "", "")%></td>
          </tr>
          <tr>
            <td><%=ParamLabel.script(facade, malaDiretaEtiqueta.USER_PARAM_ETIQUETAS_REGISTRO)%></td>
            <td align="left"><%=ParamFormEdit.script(facade, malaDiretaEtiqueta.USER_PARAM_ETIQUETAS_REGISTRO, 70, "", "")%></td>
          </tr>        
          <tr>
            <td><%=ParamLabel.script(facade, malaDiretaEtiqueta.USER_PARAM_LINHA_INICIO)%></td>
            <td align="left"><%=ParamFormEdit.script(facade, malaDiretaEtiqueta.USER_PARAM_LINHA_INICIO, 70, "", "")%></td>
          </tr>              
          <tr>
            <td><%=ParamLabel.script(facade, malaDiretaEtiqueta.USER_PARAM_MARGEM_SUPERIOR)%></td>
            <td align="left"><%=ParamFormEdit.script(facade, malaDiretaEtiqueta.USER_PARAM_MARGEM_SUPERIOR, 70, "", "")%></td>
          </tr>
          <tr>
            <td><%=ParamLabel.script(facade, malaDiretaEtiqueta.USER_PARAM_MARGEM_LATERAL)%></td>
            <td align="left"><%=ParamFormEdit.script(facade, malaDiretaEtiqueta.USER_PARAM_MARGEM_LATERAL, 70, "", "")%></td>
          </tr>
          <tr>
            <td><%=ParamLabel.script(facade, malaDiretaEtiqueta.USER_PARAM_ESPACO_HORIZONTAL)%></td>
            <td align="left"><%=ParamFormEdit.script(facade, malaDiretaEtiqueta.USER_PARAM_ESPACO_HORIZONTAL, 70, "", "")%></td>
          </tr>
          <tr>
            <td><%=ParamLabel.script(facade, malaDiretaEtiqueta.USER_PARAM_ESPACO_VERTICAL)%></td>
            <td align="left"><%=ParamFormEdit.script(facade, malaDiretaEtiqueta.USER_PARAM_ESPACO_VERTICAL, 70, "", "")%></td>
          </tr>        
        </table>
      <%=GroupBox.end()%>
    <%=formReport.end()%>

    <script type="text/javascript">
      onChangeEtiquetas();
      function executeCommand(command) {
        frameReport.document.execCommand(command);
      }
    <%if (paginaPdf.contains(".pdf")) {%>
      var url = '<%=paginaPdf%>?nocache=' + (new Date()).getTime();
      window.open(url, 'pdf', null);
    <%}%>
    </script>
  </body>
</html>

<%}
  // término do bloco protegido
  catch (Exception e) {
    Controller.forwardException(e, pageContext);
  } // try-catch
%>
