     
<%@include file="../include/beans.jsp"%>

<%@page import="imanager.entity.*"%>

<%!
  public FaseInfo[] search(Fase fase, StringBuffer searchErrorMessage) {
    try {
      // resultado da pesquisa...
      return fase.selectByFilter("%", null);
    }
    catch (Exception e) {
      searchErrorMessage.append(ExtendedException.extractMessage(e));
      return new FaseInfo[0];
    } // try-catch
  }
%>

<%
  // início do bloco protegido
  try {
    // nossa instância de Fase
    Fase fase = (Fase)facade.getEntity(Fase.CLASS_NAME);

    // lista que iremos exibir
    FaseInfo[] faseInfoList = new FaseInfo[0];
    // mensagem de erro na pesquisa
    StringBuffer searchErrorMessage = new StringBuffer();

    // se estamos excluindo...
    if (Controller.isDeleting(request)) {
      // registros selecionados
      FaseInfo[] selectedInfoList = (FaseInfo[])EntityGrid.selectedInfoList(fase, request);
      // compõe a resposta
      Form.composeAjaxResponse(response, Form.COMPOSE_TYPE_JAVA_SCRIPT);
      // nosso Grid
      EntityGrid entityGrid = EntityGrid.getInstance(facade, fase);
      // exclui e apaga do Grid
      for (int i=0; i<selectedInfoList.length; i++) {
        fase.delete(selectedInfoList[i]);%>
        <%=entityGrid.deleteRow(selectedInfoList[i])%>
      <%} // for
      // dispara
      return;
    }
    // se estamos pesquisando...
    else if (Controller.isSearching(request)) {
      // define os valores dos parâmetros de usuário
      fase.userParamList().setParamsValues(request);
      // compõe a resposta
      Form.composeAjaxResponse(response, Form.COMPOSE_TYPE_JAVA_SCRIPT);
      // atualiza tudo
      faseInfoList = search(fase, searchErrorMessage);
      %><%=EntityGrid.getInstance(facade, fase).update(faseInfoList, "", searchErrorMessage.toString())%><%
      // dispara
      return;
    } // if

    // define os valores dos parâmetros de usuário
    fase.userParamList().setParamsValues(request);
    // realiza a pesquisa
    faseInfoList = search(fase, searchErrorMessage);

    // nosso Form de pesquisa
    Form formSearch = new Form(request, "formSearchFase", Fase.ACTION, Fase.COMMAND_SEARCH, "", true, true);
    // nosso Form de exclusão
    Form formDelete = new Form(request, "formDeleteFase", Fase.ACTION, Fase.COMMAND_DELETE, "", false, true, true);
    // nosso FrameBar
    FrameBar frameBar = new FrameBar(facade, "frameBarFase");
    // nosso Grid
    EntityGrid entityGrid = EntityGrid.getInstance(facade, fase, 0, 0);
    entityGrid.addColumn(Fase.FIELD_NOME, 500, Fase.ACTION_CADASTRO, Fase.COMMAND_EDIT, "", true);
    entityGrid.addColumn(Fase.FIELD_DIAS_ACOMPANHAMENTO, 90);
    entityGrid.addColumn(Fase.FIELD_PERCENTUAL_SUCESSO, 90);
  
%>

<html>
  <head>
    <title><%=Fase.ACTION.getCaption()%></title>
    <link href="<%=facade.getStyle().userInterface()%>" rel="stylesheet" type="text/css">
    <script src="include/scripts.js" type="text/javascript"></script>
  </head>
  <body style="margin:0px;" onselectstart="return true;" oncontextmenu="return false;">

    <!-- inicia o FrameBar -->
    <%=frameBar.begin()%>

      <!-- área de frames -->
      <%=frameBar.beginFrameArea()%>

        <!-- Frame de identificação do objeto -->
        <%=frameBar.actionFrame(Fase.ACTION)%>

        <!-- Frame de comandos -->
        <%=frameBar.beginFrame("Comandos", false, false)%>
          <table style="width:100%;">
            <tr>
              <td><%=CommandControl.entityFormHyperlink(facade, fase, Fase.ACTION_CADASTRO, Fase.COMMAND_INSERT, ImageList.COMMAND_INSERT, false)%></td>
            </tr>
            <tr>
              <td><%=CommandControl.formHyperlink(facade, formDelete, ImageList.COMMAND_DELETE, "Deseja mesmo excluir o(s) registro(s) selecionado(s)?", true, false)%></td>
            </tr>
          </table>
        <%=frameBar.endFrame()%>

      <%=frameBar.endFrameArea()%>

      <!-- área do Grid -->
      <%=frameBar.beginClientArea()%>
        <!-- Form de exclusão -->
        <%=formDelete.begin()%>

          <!-- Grid -->
          <%=entityGrid.script(faseInfoList, "", searchErrorMessage.toString())%>

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
