     
<%@include file="../include/beans.jsp"%>

<%@page import="securityservice.entity.LogInfo"%>
<%@page import="securityservice.entity.Log"%>

<%!
  public LogInfo[] search(securityservice.entity.Log log, StringBuffer searchErrorMessage) {
    try {
      // resultado da pesquisa...
      return log.selectByFilter(
              DateTools.parseDate(log.USER_PARAM_DATA.getValue()),
              NumberTools.parseInt(log.USER_PARAM_USUARIO_ID.getValue()),
              NumberTools.parseInt(log.USER_PARAM_OPERACAO.getValue()),
              log.USER_PARAM_OBJETO.getValue(),
              null);
    }
    catch (Exception e) {
      searchErrorMessage.append(ExtendedException.extractMessage(e));
      return new LogInfo[0];
    } // try-catch
  }
%>

<%
  // início do bloco protegido
  try {
    // nossa instância de Log
    securityservice.entity.Log log = (securityservice.entity.Log)facade.getEntity(securityservice.entity.Log.CLASS_NAME);

    // lista que iremos exibir
    LogInfo[] logInfoList = new LogInfo[0];
    // mensagem de erro na pesquisa
    StringBuffer searchErrorMessage = new StringBuffer();

    // se estamos excluindo...
    if (Controller.isDeleting(request)) {
      // registros selecionados
      LogInfo[] selectedInfoList = (LogInfo[])EntityGrid.selectedInfoList(log, request);
      // compõe a resposta
      Form.composeAjaxResponse(response, Form.COMPOSE_TYPE_JAVA_SCRIPT);
      // nosso Grid
      EntityGrid entityGrid = EntityGrid.getInstance(facade, log);
      // exclui e apaga do Grid
      for (int i=0; i<selectedInfoList.length; i++) {
        log.delete(selectedInfoList[i]);%>
        <%=entityGrid.deleteRow(selectedInfoList[i])%>
      <%} // for
      // dispara
      return;
    }
    // se estamos pesquisando...
    else if (Controller.isSearching(request)) {
      // define os valores dos parâmetros de usuário
      log.userParamList().setParamsValues(request);
      // compõe a resposta
      Form.composeAjaxResponse(response, Form.COMPOSE_TYPE_JAVA_SCRIPT);
      // atualiza tudo
      logInfoList = search(log, searchErrorMessage);
      %><%=EntityGrid.getInstance(facade, log).update(logInfoList, "", searchErrorMessage.toString())%><%
      // dispara
      return;
    } // if

    // define os valores dos parâmetros de usuário
    log.userParamList().setParamsValues(request);
    // realiza a pesquisa
    logInfoList = search(log, searchErrorMessage);

    // nosso Form de pesquisa
    Form formSearch = new Form(request, "formSearchLog", securityservice.entity.Log.ACTION, securityservice.entity.Log.COMMAND_SEARCH, "", true, true);
    // nosso Form de exclusão
    Form formDelete = new Form(request, "formDeleteLog", securityservice.entity.Log.ACTION, securityservice.entity.Log.COMMAND_DELETE, "", false, true);
    // nosso FrameBar
    FrameBar frameBar = new FrameBar(facade, "frameBarLog");
    // nosso Grid
    EntityGrid entityGrid = EntityGrid.getInstance(facade, log, 0, 0);
    entityGrid.addColumn(securityservice.entity.Log.FIELD_LOG_ID, 70);
    entityGrid.addColumn(securityservice.entity.Log.FIELD_DATA, 70, securityservice.entity.Log.ACTION_CADASTRO, securityservice.entity.Log.COMMAND_EDIT, "", true);
    entityGrid.addColumn(securityservice.entity.Log.FIELD_USUARIO_ID, 70);
    entityGrid.addColumn(securityservice.entity.Log.FIELD_OPERACAO, 70);
    entityGrid.addColumn(securityservice.entity.Log.FIELD_OBJETO, 250);
    entityGrid.addColumn(securityservice.entity.Log.FIELD_DESCRICAO, 250);
  
%>

<html>
  <head>
    <title><%=securityservice.entity.Log.ACTION.getCaption()%></title>
    <link href="<%=facade.getStyle().userInterface()%>" rel="stylesheet" type="text/css">
    <script src="include/scripts.js" type="text/javascript"></script>
  </head>
  <body style="margin:0px;" onselectstart="return true;" oncontextmenu="return false;">

    <!-- inicia o FrameBar -->
    <%=frameBar.begin()%>

      <!-- área de frames -->
      <%=frameBar.beginFrameArea()%>

        <!-- Frame de identificação do objeto -->
        <%=frameBar.actionFrame(securityservice.entity.Log.ACTION)%>

        <!-- Frame de comandos -->
        <%=frameBar.beginFrame("Comandos", false, false)%>
          <table style="width:100%;">
            <tr>
              <td><%=CommandControl.entityFormHyperlink(facade, log, securityservice.entity.Log.ACTION_CADASTRO, securityservice.entity.Log.COMMAND_INSERT, ImageList.COMMAND_INSERT, false)%></td>
            </tr>
            <tr>
              <td><%=CommandControl.formHyperlink(facade, formDelete, ImageList.COMMAND_DELETE, "Deseja mesmo excluir o(s) registro(s) selecionado(s)?", true, false)%></td>
            </tr>
          </table>
        <%=frameBar.endFrame()%>

        <!-- Frame de pesquisa -->
        <%=frameBar.beginFrame("Pesquisa", false, true)%>
          <!-- Form de pesquisa -->
          <%=formSearch.begin()%>
              <table style="width:100%;">
                <tr>
                  <td><%=ParamLabel.script(facade, log.USER_PARAM_DATA)%></td>
                </tr>
                <tr>
                  <td><%=ParamFormEdit.script(facade, log.USER_PARAM_DATA, 0, "", "")%></td>
                </tr>
                <tr>
                  <td><%=ParamLabel.script(facade, log.USER_PARAM_USUARIO_ID)%></td>
                </tr>
                <tr>
                  <td><%=ParamFormEdit.script(facade, log.USER_PARAM_USUARIO_ID, 0, "", "")%></td>
                </tr>
                <tr>
                  <td><%=ParamLabel.script(facade, log.USER_PARAM_OPERACAO)%></td>
                </tr>
                <tr>
                  <td><%=ParamFormEdit.script(facade, log.USER_PARAM_OPERACAO, 0, "", "")%></td>
                </tr>
                <tr>
                  <td><%=ParamLabel.script(facade, log.USER_PARAM_OBJETO)%></td>
                </tr>
                <tr>
                  <td><%=ParamFormEdit.script(facade, log.USER_PARAM_OBJETO, 0, "", "")%></td>
                </tr>
                <tr>
                  <td><%=CommandControl.formButton(facade, formSearch, ImageList.COMMAND_SEARCH, "", true, false)%></td>
                </tr>
              </table>
          <%=formSearch.end()%>
        <%=frameBar.endFrame()%>

      <%=frameBar.endFrameArea()%>

      <!-- área do Grid -->
      <%=frameBar.beginClientArea()%>
        <!-- Form de exclusão -->
        <%=formDelete.begin()%>

          <!-- Grid -->
          <%=entityGrid.script(logInfoList, "", searchErrorMessage.toString())%>

        <%=formDelete.end()%>
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
