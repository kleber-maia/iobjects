     
<%@include file="../include/beans.jsp"%>

<%@page import="imanager.entity.*"%>
<%@page import="imanager.ui.entity.*"%>

<%!
  public AtendimentoFrequenteInfo[] search(AtendimentoFrequente atendimentofrequente, int empresaId, StringBuffer searchErrorMessage) {
    try {
      // resultado da pesquisa...
      return atendimentofrequente.selectByFilter(
              empresaId,
              atendimentofrequente.USER_PARAM_NOME.getValue(),
              NumberTools.parseInt(atendimentofrequente.USER_PARAM_DEPARTAMENTO_ID.getValue()),
              NumberTools.parseInt(atendimentofrequente.USER_PARAM_ASSUNTO_ID.getValue()),
              null);
    }
    catch (Exception e) {
      searchErrorMessage.append(ExtendedException.extractMessage(e));
      return new AtendimentoFrequenteInfo[0];
    } // try-catch
  }
%>

<%
  // início do bloco protegido
  try {
    // nossa instância de AtendimentoFrequente
    AtendimentoFrequente atendimentofrequente = (AtendimentoFrequente)facade.getEntity(AtendimentoFrequente.CLASS_NAME);

    // lista que iremos exibir
    AtendimentoFrequenteInfo[] atendimentofrequenteInfoList = new AtendimentoFrequenteInfo[0];
    // mensagem de erro na pesquisa
    StringBuffer searchErrorMessage = new StringBuffer();

    // se estamos excluindo...
    if (Controller.isDeleting(request)) {
      // registros selecionados
      AtendimentoFrequenteInfo[] selectedInfoList = (AtendimentoFrequenteInfo[])EntityGrid.selectedInfoList(atendimentofrequente, request);
      // compõe a resposta
      Form.composeAjaxResponse(response, Form.COMPOSE_TYPE_JAVA_SCRIPT);
      // nosso Grid
      EntityGrid entityGrid = EntityGrid.getInstance(facade, atendimentofrequente);
      // exclui e apaga do Grid
      for (int i=0; i<selectedInfoList.length; i++) {
        atendimentofrequente.delete(selectedInfoList[i]);%>
        <%=entityGrid.deleteRow(selectedInfoList[i])%>
      <%} // for
      // dispara
      return;
    }
    // se estamos pesquisando...
    else if (Controller.isSearching(request)) {
      // define os valores dos parâmetros de usuário
      atendimentofrequente.userParamList().setParamsValues(request);
      // compõe a resposta
      Form.composeAjaxResponse(response, Form.COMPOSE_TYPE_JAVA_SCRIPT);
      // atualiza tudo
      atendimentofrequenteInfoList = search(atendimentofrequente, selectedEmpresaId, searchErrorMessage);
      %><%=EntityGrid.getInstance(facade, atendimentofrequente).update(atendimentofrequenteInfoList, "", searchErrorMessage.toString())%><%
      // dispara
      return;
    } // if

    // define os valores dos parâmetros de usuário
    atendimentofrequente.userParamList().setParamsValues(request);
    // realiza a pesquisa
    atendimentofrequenteInfoList = search(atendimentofrequente, selectedEmpresaId, searchErrorMessage);

    // nosso Form de pesquisa
    Form formSearch = new Form(request, "formSearchAtendimentoFrequente", AtendimentoFrequente.ACTION, AtendimentoFrequente.COMMAND_SEARCH, "", true, true);
    // nosso Form de exclusão
    Form formDelete = new Form(request, "formDeleteAtendimentoFrequente", AtendimentoFrequente.ACTION, AtendimentoFrequente.COMMAND_DELETE, "", false, true, true);
    // nosso FrameBar
    FrameBar frameBar = new FrameBar(facade, "frameBarAtendimentoFrequente");
    // nosso Grid
    EntityGrid entityGrid = EntityGrid.getInstance(facade, atendimentofrequente, 0, 0);
    entityGrid.addColumn(AtendimentoFrequente.FIELD_NOME, 300, AtendimentoFrequente.ACTION_CADASTRO, AtendimentoFrequente.COMMAND_EDIT, "", true);
    entityGrid.addColumn(AtendimentoFrequente.LOOKUP_DEPARTAMENTO, 100);
    entityGrid.addColumn(AtendimentoFrequente.LOOKUP_ASSUNTO, 100);
  
%>

<html>
  <head>
    <title><%=AtendimentoFrequente.ACTION.getCaption()%></title>
    <link href="<%=facade.getStyle().userInterface()%>" rel="stylesheet" type="text/css">
    <script src="include/scripts.js" type="text/javascript"></script>
  </head>
  <body style="margin:0px;" onselectstart="return true;" oncontextmenu="return false;">

    <!-- inicia o FrameBar -->
    <%=frameBar.begin()%>

      <!-- área de frames -->
      <%=frameBar.beginFrameArea()%>

        <!-- Frame de identificação do objeto -->
        <%=frameBar.actionFrame(AtendimentoFrequente.ACTION)%>

        <!-- Frame de comandos -->
        <%=frameBar.beginFrame("Comandos", false, false)%>
          <table style="width:100%;">
            <tr>
              <td><%=CommandControl.entityFormHyperlink(facade, atendimentofrequente, AtendimentoFrequente.ACTION_CADASTRO, AtendimentoFrequente.COMMAND_INSERT, ImageList.COMMAND_INSERT, false)%></td>
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
                  <td><%=ParamLabel.script(facade, atendimentofrequente.USER_PARAM_NOME)%></td>
                </tr>
                <tr>
                  <td><%=ParamFormEdit.script(facade, atendimentofrequente.USER_PARAM_NOME, 0, "", "")%></td>
                </tr>
                <tr>
                  <td><%=ParamLabel.script(facade, atendimentofrequente.USER_PARAM_DEPARTAMENTO_ID)%></td>
                </tr>
                <tr>
                  <td><%=DepartamentoUI.lookupListForParam(facade, selectedEmpresaId, atendimentofrequente.USER_PARAM_DEPARTAMENTO_ID, 0, "", "")%></td>
                </tr>
                <tr>
                  <td><%=ParamLabel.script(facade, atendimentofrequente.USER_PARAM_ASSUNTO_ID)%></td>
                </tr>
                <tr>
                  <td><%=AssuntoUI.lookupSelectExForParam(facade, atendimentofrequente.USER_PARAM_ASSUNTO_ID, 0, "", "")%></td>
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
          <%=entityGrid.script(atendimentofrequenteInfoList, "", searchErrorMessage.toString())%>

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
