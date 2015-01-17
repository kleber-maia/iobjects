     
<%@include file="../include/beans.jsp"%>

<%@page import="imanager.entity.*"%>

<%!
  public CampanhaInfo[] search(Campanha campanha, StringBuffer searchErrorMessage) {
    try {
      // resultado da pesquisa...
      return campanha.selectByFilter(
              "%",
              DateTools.ZERO_DATE,
              DateTools.ZERO_DATE,
              NumberTools.parseInt(campanha.USER_PARAM_ARQUIVO.getValue()),
              null
            );
    }
    catch (Exception e) {
      searchErrorMessage.append(ExtendedException.extractMessage(e));
      return new CampanhaInfo[0];
    } // try-catch
  }
%>

<%
  // início do bloco protegido
  try {
    // nossa instância de Campanha
    Campanha campanha = (Campanha)facade.getEntity(Campanha.CLASS_NAME);

    // lista que iremos exibir
    CampanhaInfo[] campanhaInfoList = new CampanhaInfo[0];
    // mensagem de erro na pesquisa
    StringBuffer searchErrorMessage = new StringBuffer();

    // se estamos excluindo...
    if (Controller.isDeleting(request)) {
      // registros selecionados
      CampanhaInfo[] selectedInfoList = (CampanhaInfo[])EntityGrid.selectedInfoList(campanha, request);
      // compõe a resposta
      Form.composeAjaxResponse(response, Form.COMPOSE_TYPE_JAVA_SCRIPT);
      // nosso Grid
      EntityGrid entityGrid = EntityGrid.getInstance(facade, campanha);
      // exclui e apaga do Grid
      for (int i=0; i<selectedInfoList.length; i++) {
        campanha.delete(selectedInfoList[i]);%>
        <%=entityGrid.deleteRow(selectedInfoList[i])%>
      <%} // for
      // dispara
      return;
    }
    // se estamos pesquisando...
    else if (Controller.isSearching(request)) {
      // define os valores dos parâmetros de usuário
      campanha.userParamList().setParamsValues(request);
      // compõe a resposta
      Form.composeAjaxResponse(response, Form.COMPOSE_TYPE_JAVA_SCRIPT);
      // atualiza tudo
      campanhaInfoList = search(campanha, searchErrorMessage);
      %><%=EntityGrid.getInstance(facade, campanha).update(campanhaInfoList, "", searchErrorMessage.toString())%><%
      // dispara
      return;
    } // if

    // define os valores dos parâmetros de usuário
    campanha.userParamList().setParamsValues(request);
    // realiza a pesquisa
    campanhaInfoList = search(campanha, searchErrorMessage);

    // nosso Form de pesquisa
    Form formSearch = new Form(request, "formSearchCampanha", Campanha.ACTION, Campanha.COMMAND_SEARCH, "", true, true);
    // nosso Form de exclusão
    Form formDelete = new Form(request, "formDeleteCampanha", Campanha.ACTION, Campanha.COMMAND_DELETE, "", false, true, true);
    // nosso FrameBar
    FrameBar frameBar = new FrameBar(facade, "frameBarCampanha");
    // nosso Grid
    EntityGrid entityGrid = EntityGrid.getInstance(facade, campanha, 0, 0);
    entityGrid.addColumn(Campanha.FIELD_NOME, 500, Campanha.ACTION_CADASTRO, Campanha.COMMAND_EDIT, "", true);
    entityGrid.addColumn(Campanha.FIELD_ARQUIVO, 80);
  
%>

<html>
  <head>
    <title><%=Campanha.ACTION.getCaption()%></title>
    <link href="<%=facade.getStyle().userInterface()%>" rel="stylesheet" type="text/css">
    <script src="include/scripts.js" type="text/javascript"></script>
  </head>
  <body style="margin:0px;" onselectstart="return true;" oncontextmenu="return false;">

    <!-- inicia o FrameBar -->
    <%=frameBar.begin()%>

      <!-- área de frames -->
      <%=frameBar.beginFrameArea()%>

        <!-- Frame de identificação do objeto -->
        <%=frameBar.actionFrame(Campanha.ACTION)%>

        <!-- Frame de comandos -->
        <%=frameBar.beginFrame("Comandos", false, false)%>
          <table style="width:100%;">
            <tr>
              <td><%=CommandControl.entityFormHyperlink(facade, campanha, Campanha.ACTION_CADASTRO, Campanha.COMMAND_INSERT, ImageList.COMMAND_INSERT, false)%></td>
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
                  <td><%=ParamLabel.script(facade, campanha.USER_PARAM_ARQUIVO)%></td>
                </tr>
                <tr>
                  <td><%=ParamFormSelect.script(facade, campanha.USER_PARAM_ARQUIVO, 0, "", "")%></td>
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
          <%=entityGrid.script(campanhaInfoList, "", searchErrorMessage.toString())%>

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
