     
<%@include file="../include/beans.jsp"%>

<%@page import="imanager.entity.*"%>
<%@page import="imanager.ui.entity.*"%>

<%!
  public ModeloAtendimentoInfo[] search(ModeloAtendimento modeloAtendimento, int empresaId, StringBuffer searchErrorMessage) {
    try {
      // resultado da pesquisa...
      return modeloAtendimento.selectByFilter(
              empresaId,
              "%",
              NumberTools.parseInt(modeloAtendimento.USER_PARAM_DEPARTAMENTO_ID.getValue()),
              NumberTools.parseInt(modeloAtendimento.USER_PARAM_ASSUNTO_ID.getValue()),
              null
            );
    }
    catch (Exception e) {
      searchErrorMessage.append(ExtendedException.extractMessage(e));
      return new ModeloAtendimentoInfo[0];
    } // try-catch
  }
%>

<%
  // início do bloco protegido
  try {
    // nossa instância de Modelo Atendimento
    ModeloAtendimento modeloAtendimento = (ModeloAtendimento)facade.getEntity(ModeloAtendimento.CLASS_NAME);

    // lista que iremos exibir
    ModeloAtendimentoInfo[] modeloAtendimentoInfoList = new ModeloAtendimentoInfo[0];
    // mensagem de erro na pesquisa
    StringBuffer searchErrorMessage = new StringBuffer();

    // se estamos excluindo...
    if (Controller.isDeleting(request)) {
      // registros selecionados
      ModeloAtendimentoInfo[] selectedInfoList = (ModeloAtendimentoInfo[])EntityGrid.selectedInfoList(modeloAtendimento, request);
      // compõe a resposta
      Form.composeAjaxResponse(response, Form.COMPOSE_TYPE_JAVA_SCRIPT);
      // nosso Grid
      EntityGrid entityGrid = EntityGrid.getInstance(facade, modeloAtendimento);
      // exclui e apaga do Grid
      for (int i=0; i<selectedInfoList.length; i++) {
        modeloAtendimento.delete(selectedInfoList[i]);%>
        <%=entityGrid.deleteRow(selectedInfoList[i])%>
      <%} // for
      // dispara
      return;
    }
    // se estamos pesquisando...
    else if (Controller.isSearching(request)) {
      // define os valores dos parâmetros de usuário
      modeloAtendimento.userParamList().setParamsValues(request);
      // compõe a resposta
      Form.composeAjaxResponse(response, Form.COMPOSE_TYPE_JAVA_SCRIPT);
      // atualiza tudo
      modeloAtendimentoInfoList = search(modeloAtendimento, selectedEmpresaId, searchErrorMessage);
      %><%=EntityGrid.getInstance(facade, modeloAtendimento).update(modeloAtendimentoInfoList, "", searchErrorMessage.toString())%><%
      // dispara
      return;
    } // if

    // define os valores dos parâmetros de usuário
    modeloAtendimento.userParamList().setParamsValues(request);
    // realiza a pesquisa
    modeloAtendimentoInfoList = search(modeloAtendimento, selectedEmpresaId, searchErrorMessage);

    // nosso Form de pesquisa
    Form formSearch = new Form(request, "formSearchModeloAtendimento", ModeloAtendimento.ACTION, ModeloAtendimento.COMMAND_SEARCH, "", true, true);
    // nosso Form de exclusão
    Form formDelete = new Form(request, "formDeleteModeloAtendimento", ModeloAtendimento.ACTION, ModeloAtendimento.COMMAND_DELETE, "", false, true, true);
    // nosso FrameBar
    FrameBar frameBar = new FrameBar(facade, "frameBarModeloAtendimento");
    // nosso Grid
    EntityGrid entityGrid = EntityGrid.getInstance(facade, modeloAtendimento, 0, 0);
    entityGrid.addColumn(ModeloAtendimento.FIELD_NOME, 250, ModeloAtendimento.ACTION_CADASTRO, ModeloAtendimento.COMMAND_EDIT, "", true);
    entityGrid.addColumn(ModeloAtendimento.LOOKUP_DEPARTAMENTO, 250);
    entityGrid.addColumn(ModeloAtendimento.LOOKUP_ASSUNTO, 250);
%>

<html>
  <head>
    <title><%=ModeloAtendimento.ACTION.getCaption()%></title>
    <link href="<%=facade.getStyle().userInterface()%>" rel="stylesheet" type="text/css">
    <script src="include/scripts.js" type="text/javascript"></script>
  </head>
  <body style="margin:0px;" onselectstart="return true;" oncontextmenu="return false;">

    <!-- inicia o FrameBar -->
    <%=frameBar.begin()%>

      <!-- área de frames -->
      <%=frameBar.beginFrameArea()%>

        <!-- Frame de identificação do objeto -->
        <%=frameBar.actionFrame(ModeloAtendimento.ACTION)%>

        <!-- Frame de comandos -->
        <%=frameBar.beginFrame("Comandos", false, false)%>
          <table style="width:100%;">
            <tr>
              <td><%=CommandControl.entityFormHyperlink(facade, modeloAtendimento, ModeloAtendimento.ACTION_CADASTRO, ModeloAtendimento.COMMAND_INSERT, ImageList.COMMAND_INSERT, false)%></td>
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
                  <td><%=ParamLabel.script(facade, modeloAtendimento.USER_PARAM_DEPARTAMENTO_ID)%></td>
                </tr>
                <tr>
                  <td><%=DepartamentoUI.lookupListForParam(facade, selectedEmpresaId, modeloAtendimento.USER_PARAM_DEPARTAMENTO_ID, 0, "", "")%></td>
                </tr>
                <tr>
                  <td><%=ParamLabel.script(facade, modeloAtendimento.USER_PARAM_ASSUNTO_ID)%></td>
                </tr>
                <tr>
                  <td><%=AssuntoUI.lookupSelectExForParam(facade, modeloAtendimento.USER_PARAM_ASSUNTO_ID, 0, "", "")%></td>
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
          <%=entityGrid.script(modeloAtendimentoInfoList, "", searchErrorMessage.toString())%>

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
