     
<%@include file="../include/beans.jsp"%>

<%@page import="imanager.entity.*"%>

<%!
  public DepartamentoInfo[] search(Departamento departamento, int empresaId, StringBuffer searchErrorMessage) {
    try {
      // resultado da pesquisa...
      return departamento.selectByFilter(empresaId);
    }
    catch (Exception e) {
      searchErrorMessage.append(ExtendedException.extractMessage(e));
      return new DepartamentoInfo[0];
    } // try-catch
  }
%>

<%
  // início do bloco protegido
  try {
    // nossa instância de Departamento
    Departamento departamento = (Departamento)facade.getEntity(Departamento.CLASS_NAME);
    // nossa instância de Empresa
    Empresa empresa = (Empresa)facade.getEntity(Empresa.CLASS_NAME);

    // se a Empresa é apenas um Estoque...exceção
    if (empresa.isEstoque(selectedEmpresaId))
      throw new Exception("Operação não permitida em uma Empresa que é apenas estoque.");

    // lista que iremos exibir
    DepartamentoInfo[] departamentoInfoList = new DepartamentoInfo[0];
    // mensagem de erro na pesquisa
    StringBuffer searchErrorMessage = new StringBuffer();

    // se estamos excluindo...
    if (Controller.isDeleting(request)) {
      // registros selecionados
      DepartamentoInfo[] selectedInfoList = (DepartamentoInfo[])EntityGrid.selectedInfoList(departamento, request);
      // compõe a resposta
      Form.composeAjaxResponse(response, Form.COMPOSE_TYPE_JAVA_SCRIPT);
      // nosso Grid
      EntityGrid entityGrid = EntityGrid.getInstance(facade, departamento);
      // exclui e apaga do Grid
      for (int i=0; i<selectedInfoList.length; i++) {
        departamento.delete(selectedInfoList[i]);%>
        <%=entityGrid.deleteRow(selectedInfoList[i])%>
      <%} // for
      // dispara
      return;
    }
    // se estamos pesquisando...
    else if (Controller.isSearching(request)) {
      // define os valores dos parâmetros de usuário
      departamento.userParamList().setParamsValues(request);
      // compõe a resposta
      Form.composeAjaxResponse(response, Form.COMPOSE_TYPE_JAVA_SCRIPT);
      // atualiza tudo
      departamentoInfoList = search(departamento, selectedEmpresaId, searchErrorMessage);
      %><%=EntityGrid.getInstance(facade, departamento).update(departamentoInfoList, "", searchErrorMessage.toString())%><%
      // dispara
      return;
    } // if

    // define os valores dos parâmetros de usuário
    departamento.userParamList().setParamsValues(request);
    // realiza a pesquisa
    departamentoInfoList = search(departamento, selectedEmpresaId, searchErrorMessage);

    // nosso Form de pesquisa
    Form formSearch = new Form(request, "formSearchDepartamento", Departamento.ACTION, Departamento.COMMAND_SEARCH, "", true, true);
    // nosso Form de exclusão
    Form formDelete = new Form(request, "formDeleteDepartamento", Departamento.ACTION, Departamento.COMMAND_DELETE, "", false, true, true);
    // nosso FrameBar
    FrameBar frameBar = new FrameBar(facade, "frameBarDepartamento");
    // nosso Grid
    EntityGrid entityGrid = EntityGrid.getInstance(facade, departamento, 0, 0);
    entityGrid.addColumn(Departamento.FIELD_NOME, 500, Departamento.ACTION_CADASTRO, Departamento.COMMAND_EDIT, "", true);
%>

<html>
  <head>
    <title><%=Departamento.ACTION.getCaption()%></title>
    <link href="<%=facade.getStyle().userInterface()%>" rel="stylesheet" type="text/css">
    <script src="include/scripts.js" type="text/javascript"></script>
  </head>
  <body style="margin:0px;" onselectstart="return true;" oncontextmenu="return false;">

    <!-- inicia o FrameBar -->
    <%=frameBar.begin()%>

      <!-- área de frames -->
      <%=frameBar.beginFrameArea()%>

        <!-- Frame de identificação do objeto -->
        <%=frameBar.actionFrame(Departamento.ACTION)%>

        <!-- Frame de comandos -->
        <%=frameBar.beginFrame("Comandos", false, false)%>
          <table style="width:100%;">
            <tr>
              <td><%=CommandControl.entityFormHyperlink(facade, departamento, Departamento.ACTION_CADASTRO, Departamento.COMMAND_INSERT, ImageList.COMMAND_INSERT, false)%></td>
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
          <%=entityGrid.script(departamentoInfoList, "", searchErrorMessage.toString())%>

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
