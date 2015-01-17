     
<%@include file="../include/beans.jsp"%>

<%@page import="imanager.entity.*"%>

<%!
  public AgendaFeriadoInfo[] search(AgendaFeriado agendaFeriado, StringBuffer searchErrorMessage) {
    try {
      // resultado da pesquisa...
      return agendaFeriado.selectByFilter(
              agendaFeriado.USER_PARAM_NOME.getValue(),
              NumberTools.parseInt(agendaFeriado.USER_PARAM_BLOQUEIO.getValue()),
              NumberTools.parseInt(agendaFeriado.USER_PARAM_ANO.getValue()),
              null);
    }
    catch (Exception e) {
      searchErrorMessage.append(ExtendedException.extractMessage(e));
      return new AgendaFeriadoInfo[0];
    } // try-catch
  }
%>

<%
  // início do bloco protegido
  try {
    // nossa instância de AgendaFeriado
    AgendaFeriado agendaFeriado = (AgendaFeriado)facade.getEntity(AgendaFeriado.CLASS_NAME);

    // lista que iremos exibir
    AgendaFeriadoInfo[] agendaFeriadoInfoList = new AgendaFeriadoInfo[0];
    // mensagem de erro na pesquisa
    StringBuffer searchErrorMessage = new StringBuffer();

    // se estamos excluindo...
    if (Controller.isDeleting(request)) {
      // registros selecionados
      AgendaFeriadoInfo[] selectedInfoList = (AgendaFeriadoInfo[])EntityGrid.selectedInfoList(agendaFeriado, request);
      // compõe a resposta
      Form.composeAjaxResponse(response, Form.COMPOSE_TYPE_JAVA_SCRIPT);
      // nosso Grid
      EntityGrid entityGrid = EntityGrid.getInstance(facade, agendaFeriado);
      // exclui e apaga do Grid
      for (int i=0; i<selectedInfoList.length; i++) {
        agendaFeriado.delete(selectedInfoList[i]);%>
        <%=entityGrid.deleteRow(selectedInfoList[i])%>
      <%} // for
      // dispara
      return;
    }
    // se estamos pesquisando...
    else if (Controller.isSearching(request)) {
      // define os valores dos parâmetros de usuário
      agendaFeriado.userParamList().setParamsValues(request);
      // compõe a resposta
      Form.composeAjaxResponse(response, Form.COMPOSE_TYPE_JAVA_SCRIPT);
      // atualiza tudo
      agendaFeriadoInfoList = search(agendaFeriado, searchErrorMessage);
      %><%=EntityGrid.getInstance(facade, agendaFeriado).update(agendaFeriadoInfoList, "", searchErrorMessage.toString())%><%
      // dispara
      return;
    } // if

    // define os valores dos parâmetros de usuário
    agendaFeriado.userParamList().setParamsValues(request);
    // realiza a pesquisa
    agendaFeriadoInfoList = search(agendaFeriado, searchErrorMessage);

    // nosso Form de pesquisa
    Form formSearch = new Form(request, "formSearchAgendaFeriado", AgendaFeriado.ACTION, AgendaFeriado.COMMAND_SEARCH, "", true, true);
    // nosso Form de exclusão
    Form formDelete = new Form(request, "formDeleteAgendaFeriado", AgendaFeriado.ACTION, AgendaFeriado.COMMAND_DELETE, "", false, true, true);
    // nosso FrameBar
    FrameBar frameBar = new FrameBar(facade, "frameBarAgendaFeriado");
    // nosso Grid
    EntityGrid entityGrid = EntityGrid.getInstance(facade, agendaFeriado, 0, 0);
    entityGrid.addColumn(AgendaFeriado.FIELD_NOME, 250, AgendaFeriado.ACTION_CADASTRO, AgendaFeriado.COMMAND_EDIT, "", true);
    entityGrid.addColumn(AgendaFeriado.FIELD_FERIADO, 90);
    entityGrid.addColumn(AgendaFeriado.FIELD_BLOQUEIO, 90);
%>

<html>
  <head>
    <title><%=AgendaFeriado.ACTION.getCaption()%></title>
    <link href="<%=facade.getStyle().userInterface()%>" rel="stylesheet" type="text/css">
    <script src="include/scripts.js" type="text/javascript"></script>
  </head>
  <body style="margin:0px;" onselectstart="return true;" oncontextmenu="return false;">

    <!-- inicia o FrameBar -->
    <%=frameBar.begin()%>

      <!-- área de frames -->
      <%=frameBar.beginFrameArea()%>

        <!-- Frame de identificação do objeto -->
        <%=frameBar.actionFrame(AgendaFeriado.ACTION)%>

        <!-- Frame de comandos -->
        <%=frameBar.beginFrame("Comandos", false, false)%>
          <table style="width:100%;">
            <tr>
              <td><%=CommandControl.entityFormHyperlink(facade, agendaFeriado, AgendaFeriado.ACTION_CADASTRO, AgendaFeriado.COMMAND_INSERT, ImageList.COMMAND_INSERT, false)%></td>
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
                  <td><%=ParamLabel.script(facade, agendaFeriado.USER_PARAM_NOME)%></td>
                </tr>
                <tr>
                  <td><%=ParamFormEdit.script(facade, agendaFeriado.USER_PARAM_NOME, 0, "", "")%></td>
                </tr>
                <tr>
                  <td><%=ParamLabel.script(facade, agendaFeriado.USER_PARAM_BLOQUEIO)%></td>
                </tr>
                <tr>
                  <td><%=ParamFormSelect.script(facade, agendaFeriado.USER_PARAM_BLOQUEIO, 0, "", "")%></td>
                </tr>
                <tr>
                  <td><%=ParamLabel.script(facade, agendaFeriado.USER_PARAM_ANO)%></td>
                </tr>
                <tr>
                  <td><%=ParamFormEdit.script(facade, agendaFeriado.USER_PARAM_ANO, 0, "", "")%></td>
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
          <%=entityGrid.script(agendaFeriadoInfoList, "", searchErrorMessage.toString())%>

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
