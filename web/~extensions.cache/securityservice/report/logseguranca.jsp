     
<%@page import="securityservice.misc.TipoOperacaoLog"%>
<%@page import="securityservice.ui.entity.UsuarioUI"%>
<%@include file="../include/beans.jsp"%>

<%@page import="securityservice.report.*"%>

<%
  // início do bloco protegido
  try {
    // nossa instância de LogSeguranca
    LogSeguranca logseguranca = (LogSeguranca)facade.getReport(LogSeguranca.CLASS_NAME);

    // define os parâmetros, caso sejamos chamados por outro objeto
    logseguranca.userParamList().setParamsValues(request);

    // nosso Form de relatório
    Form formReport = new Form(request, "formReportLogSeguranca", LogSeguranca.ACTION_RELATORIO, LogSeguranca.COMMAND_MAKE_REPORT, "frameReport", true);
    // nosso FrameBar
    FrameBar frameBar = new FrameBar(facade, "frameBarLogSeguranca");
%>

<html>
  <head>
    <title><%=LogSeguranca.ACTION.getCaption()%></title>
    <link href="<%=facade.getStyle().userInterface()%>" rel="stylesheet" type="text/css">
    <script src="include/scripts.js" type="text/javascript"></script>
  </head>
  <body style="margin:0px;" onselectstart="return true;" oncontextmenu="return false;">

    <!-- inicia o FrameBar -->
    <%=frameBar.begin()%>

      <!-- área de frames -->
      <%=frameBar.beginFrameArea()%>

        <!-- Frame de identificação do objeto -->
        <%=frameBar.actionFrame(LogSeguranca.ACTION)%>

        <!-- Frame de parâmetros -->
        <%=frameBar.beginFrame("Parâmetros", false, true)%>
          <!-- Form de relatório -->
          <%=formReport.begin()%>
            <table style="width:100%;">
              <tr>
                <td><%=ParamLabel.script(facade, logseguranca.USER_PARAM_DATA)%></td>
              </tr>
              <tr>
                <td><%=ParamFormEdit.script(facade, logseguranca.USER_PARAM_DATA, 0, "", "")%></td>
              </tr>
              <tr>
                <td><%=ParamLabel.script(facade, logseguranca.USER_PARAM_USUARIO)%></td>
              </tr>
              <tr>
                <td><%=UsuarioUI.lookupSearchForParam(facade, logseguranca.USER_PARAM_USUARIO, 0, "", "", false)%></td>
              </tr>
              <tr>
                <td><%=ParamLabel.script(facade, logseguranca.USER_PARAM_OPERACAO)%></td>
              </tr>
              <tr>
                <td><%=ParamFormSelect.script(facade, logseguranca.USER_PARAM_OPERACAO, 0, "", "")%></td>
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
              <td><%=CommandControl.actionCustomScriptHyperlink(facade, LogSeguranca.ACTION, LogSeguranca.COMMAND_PRINT, ImageList.COMMAND_PRINT, "javascript:executeCommand('PRINT');", "", "", false)%></td>
            </tr>
            <tr>
              <td><%=CommandControl.actionCustomScriptHyperlink(facade, LogSeguranca.ACTION, LogSeguranca.COMMAND_COPY, ImageList.COMMAND_COPY, "javascript:executeCommand('SELECTALL');executeCommand('COPY');", "", "", false)%></td>
            </tr>
          </table>
        <%=frameBar.endFrame()%>

      <%=frameBar.endFrameArea()%>

      <!-- área do relatório -->
      <%=frameBar.beginClientArea()%>
        <iframe name="frameReport" id="frameReport" src="<%=LogSeguranca.ACTION_RELATORIO.url()%>" frameborder="false" style="width:100%; height:100%;" />
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
