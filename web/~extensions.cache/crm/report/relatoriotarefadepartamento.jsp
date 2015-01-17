     
<%@include file="../include/beans.jsp"%>

<%@page import="imanager.report.*"%>
<%@page import="imanager.ui.entity.*"%>

<%@page import="securityservice.ui.entity.*"%>

<%
  // início do bloco protegido
  try {
    // nossa instância de Tarefa Usuário
    RelatorioTarefaDepartamento relatorioTarefaDepartamento = (RelatorioTarefaDepartamento)facade.getReport(RelatorioTarefaDepartamento.CLASS_NAME);

    // define os parâmetros, caso sejamos chamados por outro objeto
    relatorioTarefaDepartamento.userParamList().setParamsValues(request);

    // nosso Form de relatório
    Form formReport = new Form(request, "formReportRelatorioTarefaDepartamento", RelatorioTarefaDepartamento.ACTION_RELATORIO, RelatorioTarefaDepartamento.COMMAND_MAKE_REPORT, "frameReport", true);
    // nosso FrameBar
    FrameBar frameBar = new FrameBar(facade, "frameBarRelatorioTarefaDepartamento");
%>

<html>
  <head>
    <title><%=RelatorioTarefaDepartamento.ACTION.getCaption()%></title>
    <link href="<%=facade.getStyle().userInterface()%>" rel="stylesheet" type="text/css">
    <script src="include/scripts.js" type="text/javascript"></script>
  </head>
  <body style="margin:0px;" onselectstart="return true;" oncontextmenu="return false;">

    <!-- inicia o FrameBar -->
    <%=frameBar.begin()%>

      <!-- área de frames -->
      <%=frameBar.beginFrameArea()%>

        <!-- Frame de identificação do objeto -->
        <%=frameBar.actionFrame(RelatorioTarefaDepartamento.ACTION)%>

        <!-- Frame de parâmetros -->
        <%=frameBar.beginFrame("Parâmetros", false, true)%>
          <!-- Form de relatório -->
          <%=formReport.begin()%>
            <table style="width:100%;">
              <tr>
                <td><%=ParamLabel.script(facade, relatorioTarefaDepartamento.USER_PARAM_DATA_INICIAL)%></td>
              </tr>
              <tr>
                <td><%=ParamFormEdit.script(facade, relatorioTarefaDepartamento.USER_PARAM_DATA_INICIAL, 0, "", "")%></td>
              </tr>
              <tr>
                <td><%=ParamLabel.script(facade, relatorioTarefaDepartamento.USER_PARAM_DATA_FINAL)%></td>
              </tr>
              <tr>
                <td><%=ParamFormEdit.script(facade, relatorioTarefaDepartamento.USER_PARAM_DATA_FINAL, 0, "", "")%></td>
              </tr>
              <tr>
                <td><%=ParamLabel.script(facade, relatorioTarefaDepartamento.USER_PARAM_STATUS)%></td>
              </tr>
              <tr>
                <td><%=ParamFormSelect.script(facade, relatorioTarefaDepartamento.USER_PARAM_STATUS, 0, "", "")%></td>
              </tr>
              <tr>
                <td><%=ParamLabel.script(facade, relatorioTarefaDepartamento.USER_PARAM_ASSUNTO)%></td>
              </tr>
              <tr>
                <td><%=AssuntoUI.lookupSelectExForParam(facade, relatorioTarefaDepartamento.USER_PARAM_ASSUNTO, 0, "", "")%></td>
              </tr>
              <tr>
                <td><%=ParamLabel.script(facade, relatorioTarefaDepartamento.USER_PARAM_USUARIO)%></td>
              </tr>
              <tr>
                <td><%=UsuarioUI.lookupSearchForParam(facade, relatorioTarefaDepartamento.USER_PARAM_USUARIO, 0, "", "", false) %></td>
              </tr>
              <tr>
                <td><%=ParamLabel.script(facade, relatorioTarefaDepartamento.USER_PARAM_PRORROGADO)%></td>
              </tr>
              <tr>
                <td><%=ParamFormSelect.script(facade, relatorioTarefaDepartamento.USER_PARAM_PRORROGADO, 0, "", "")%></td>
              </tr>
              <tr>
                <td><%=ParamLabel.script(facade, relatorioTarefaDepartamento.USER_PARAM_CONSOLIDADO)%></td>
              </tr>
              <tr>
                <td><%=ParamFormSelect.script(facade, relatorioTarefaDepartamento.USER_PARAM_CONSOLIDADO, 0, "", "")%></td>
              </tr>
              <tr>
                <td><%=CommandControl.formButton(facade, formReport, ImageList.COMMAND_PRINT, "", false, false)%></td>
              </tr>
            </table>
          <%=formReport.end()%>
        <%=frameBar.endFrame()%>

        <!-- Frame de comandos -->
        <%=frameBar.beginFrame("Comandos", false, true)%>
          <script type="text/javascript">
            function executeCommand(command) {
              frameReport.document.execCommand(command);
            }
          </script>
          <table style="width:100%;">
            <tr>
              <td><%=CommandControl.actionCustomScriptHyperlink(facade, RelatorioTarefaDepartamento.ACTION, RelatorioTarefaDepartamento.COMMAND_PRINT, ImageList.COMMAND_PRINT, "javascript:executeCommand('PRINT');", "", "", false)%></td>
            </tr>
            <tr>
              <td><%=CommandControl.actionCustomScriptHyperlink(facade, RelatorioTarefaDepartamento.ACTION, RelatorioTarefaDepartamento.COMMAND_COPY, ImageList.COMMAND_COPY, "javascript:executeCommand('SELECTALL');executeCommand('COPY');", "", "", false)%></td>
            </tr>
          </table>
        <%=frameBar.endFrame()%>

      <%=frameBar.endFrameArea()%>

      <!-- área do relatório -->
      <%=frameBar.beginClientArea()%>
        <iframe name="frameReport" id="frameReport" src="<%=RelatorioTarefaDepartamento.ACTION_RELATORIO.url()%>" frameborder="false" style="width:100%; height:100%;" />
      <%=frameBar.endClientArea()%>

    <%=frameBar.end()%>

  </body>
</html>

<%}
  // término do bloco protegido
  catch (Exception e) {
    Controller.forwardException(e, pageContext);
  } // try-catch
%>
