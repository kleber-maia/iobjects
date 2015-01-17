     
<%@page import="imanager.misc.StatusOportunidade"%>
<%@page import="imanager.ui.entity.FaseUI"%>
<%@page import="imanager.ui.entity.DepartamentoUI"%>
<%@include file="../include/beans.jsp"%>

<%@page import="imanager.report.*"%>

<%@page import="securityservice.ui.entity.*"%>

<%
  // início do bloco protegido
  try {
    // nossa instância de RelatorioRankingOportunidadeCampanha
    RelatorioRankingOportunidadeCampanha relatorioRankingOportunidadeCampanha = (RelatorioRankingOportunidadeCampanha)facade.getReport(RelatorioRankingOportunidadeCampanha.CLASS_NAME);

    // define os parâmetros, caso sejamos chamados por outro objeto
    relatorioRankingOportunidadeCampanha.userParamList().setParamsValues(request);

    // nosso Form de relatório
    Form formReport = new Form(request, "formReportRelatorioRankingOportunidadeCampanha", RelatorioRankingOportunidadeCampanha.ACTION_RELATORIO, RelatorioRankingOportunidadeCampanha.COMMAND_MAKE_REPORT, "frameReport", true);
    // nosso FrameBar
    FrameBar frameBar = new FrameBar(facade, "frameBarRelatorioRankingOportunidadeCampanha");
%>

<html>
  <head>
    <title><%=RelatorioRankingOportunidadeCampanha.ACTION.getCaption()%></title>
    <link href="<%=facade.getStyle().userInterface()%>" rel="stylesheet" type="text/css">
    <script src="include/scripts.js" type="text/javascript"></script>
  </head>
  <body style="margin:0px;" onselectstart="return true;" oncontextmenu="return false;">

    <!-- inicia o FrameBar -->
    <%=frameBar.begin()%>

      <!-- área de frames -->
      <%=frameBar.beginFrameArea()%>

        <!-- Frame de identificação do objeto -->
        <%=frameBar.actionFrame(RelatorioRankingOportunidadeCampanha.ACTION)%>

        <!-- Frame de parâmetros -->
        <%=frameBar.beginFrame("Parâmetros", false, true)%>
          <!-- Form de relatório -->
          <%=formReport.begin()%>
            <table style="width:100%;">
              <tr>
                <td><%=ParamLabel.script(facade, relatorioRankingOportunidadeCampanha.USER_PARAM_DATA_INICIAL)%></td>
              </tr>
              <tr>
                <td><%=ParamFormEdit.script(facade, relatorioRankingOportunidadeCampanha.USER_PARAM_DATA_INICIAL, 0, "", "")%></td>
              </tr>
              <tr>
                <td><%=ParamLabel.script(facade, relatorioRankingOportunidadeCampanha.USER_PARAM_DATA_FINAL)%></td>
              </tr>
              <tr>
                <td><%=ParamFormEdit.script(facade, relatorioRankingOportunidadeCampanha.USER_PARAM_DATA_FINAL, 0, "", "")%></td>
              </tr>
              <tr>
                <td><%=ParamLabel.script(facade, relatorioRankingOportunidadeCampanha.USER_PARAM_USUARIO_ID)%></td>
              </tr>
              <tr>
                <td><%=UsuarioUI.lookupSearchForParam(facade, relatorioRankingOportunidadeCampanha.USER_PARAM_USUARIO_ID, 0, "", "", false) %></td>
              </tr>
              <tr>
                <td><%=ParamLabel.script(facade, relatorioRankingOportunidadeCampanha.USER_PARAM_DEPARTAMENTO_ID)%></td>
              </tr>
              <tr>
                <td><%=DepartamentoUI.lookupListForParam(facade, selectedEmpresaId, relatorioRankingOportunidadeCampanha.USER_PARAM_DEPARTAMENTO_ID, 0, "", "") %></td>
              </tr>
              <tr>
                <td><%=ParamLabel.script(facade, relatorioRankingOportunidadeCampanha.USER_PARAM_FASE_ID)%></td>
              </tr>
              <tr>
                <td><%=FaseUI.lookupListForParam(facade, relatorioRankingOportunidadeCampanha.USER_PARAM_FASE_ID, 0, "", "") %></td>
              </tr>
              <tr>
                <td><%=ParamLabel.script(facade, relatorioRankingOportunidadeCampanha.USER_PARAM_STATUS)%></td>
              </tr>
              <tr>
                <td><%=ParamFormSelect.script(facade, relatorioRankingOportunidadeCampanha.USER_PARAM_STATUS, 0, "", "")%></td>
              </tr>
              <tr>
                <td><%=ParamLabel.script(facade, relatorioRankingOportunidadeCampanha.USER_PARAM_CONSOLIDADO)%></td>
              </tr>
              <tr>
                <td><%=ParamFormSelect.script(facade, relatorioRankingOportunidadeCampanha.USER_PARAM_CONSOLIDADO, 0, "", "")%></td>
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
              <td><%=CommandControl.actionCustomScriptHyperlink(facade, RelatorioRankingOportunidadeCampanha.ACTION, RelatorioRankingOportunidadeCampanha.COMMAND_PRINT, ImageList.COMMAND_PRINT, "javascript:executeCommand('PRINT');", "", "", false)%></td>
            </tr>
            <tr>
              <td><%=CommandControl.actionCustomScriptHyperlink(facade, RelatorioRankingOportunidadeCampanha.ACTION, RelatorioRankingOportunidadeCampanha.COMMAND_COPY, ImageList.COMMAND_COPY, "javascript:executeCommand('SELECTALL');executeCommand('COPY');", "", "", false)%></td>
            </tr>
          </table>
        <%=frameBar.endFrame()%>

      <%=frameBar.endFrameArea()%>

      <!-- área do relatório -->
      <%=frameBar.beginClientArea()%>
        <iframe name="frameReport" id="frameReport" src="<%=RelatorioRankingOportunidadeCampanha.ACTION_RELATORIO.url()%>" frameborder="false" style="width:100%; height:100%;" />
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
