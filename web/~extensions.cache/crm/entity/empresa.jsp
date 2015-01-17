
<%@include file="../include/beans.jsp"%>

<%@page import="imanager.entity.*"%>
<%@page import="imanager.misc.*"%>

<%!
  public EmpresaInfo[] search(Empresa empresa, StringBuffer searchErrorMessage) {
    try {
      // resultado da pesquisa...
      return empresa.selectByFilter("%", "%", NaoSim.TODOS);
    }
    catch (Exception e) {
      searchErrorMessage.append(ExtendedException.extractMessage(e));
      return new EmpresaInfo[0];
    } // try-catch
  }
%>

<%
  // início do bloco protegido
  try {
    // nossa instância de Empresa
    Empresa empresa = (Empresa)facade.getEntity(Empresa.CLASS_NAME);

    // lista que iremos exibir
    EmpresaInfo[] empresaInfoList = new EmpresaInfo[0];
    // mensagem de erro na pesquisa
    StringBuffer searchErrorMessage = new StringBuffer();

    // se estamos excluindo...
    if (Controller.isDeleting(request)) {
      // registros selecionados
      EmpresaInfo[] selectedInfoList = (EmpresaInfo[])EntityGrid.selectedInfoList(empresa, request);
      // compõe a resposta
      Form.composeAjaxResponse(response, Form.COMPOSE_TYPE_JAVA_SCRIPT);
      // nosso Grid
      EntityGrid entityGrid = EntityGrid.getInstance(facade, empresa);
      // exclui e apaga do Grid
      for (int i=0; i<selectedInfoList.length; i++) {
        empresa.delete(selectedInfoList[i]);%>
        <%=entityGrid.deleteRow(selectedInfoList[i])%>
      <%} // for
      // dispara
      return;
    }
    // se estamos pesquisando...
    else if (Controller.isSearching(request)) {
      // compõe a resposta
      Form.composeAjaxResponse(response, Form.COMPOSE_TYPE_JAVA_SCRIPT);
      // atualiza tudo
      empresaInfoList = search(empresa, searchErrorMessage);
      %><%=EntityGrid.getInstance(facade, empresa).update(empresaInfoList, "", searchErrorMessage.toString())%><%
      // dispara
      return;
    } // if

    // realiza a pesquisa
    empresaInfoList = search(empresa, searchErrorMessage);

    // nosso Form de pesquisa
    Form formSearch = new Form(request, "formSearchEmpresa", Empresa.ACTION, Empresa.COMMAND_SEARCH, "", true, true);
    // nosso Form de exclusão
    Form formDelete = new Form(request, "formDeleteEmpresa", Empresa.ACTION, Empresa.COMMAND_DELETE, "", false, true, true);
    // nosso FrameBar
    FrameBar frameBar = new FrameBar(facade, "frameBarEmpresa");
    // nosso Grid
    EntityGrid entityGrid = EntityGrid.getInstance(facade, empresa, 0, 0);
    entityGrid.addColumn(Empresa.FIELD_NOME, 250, Empresa.ACTION_CADASTRO, Empresa.COMMAND_EDIT, "", true);
    entityGrid.addColumn(Empresa.FIELD_CIDADE, 250);
    entityGrid.addColumn(Empresa.FIELD_UF, 50);
    entityGrid.addColumn(Empresa.FIELD_ESTOQUE, 80);
    entityGrid.addColumn(Empresa.FIELD_CONSOLIDADORA, 80);
    entityGrid.addColumn(Empresa.FIELD_ARQUIVO, 80);
    // apenas para Super Usuário...
    entityGrid.setColumnWidth(4, facade.getLoggedUser().getSuperUser() ? 80 : 0);
%>

<html>
  <head>
    <title><%=Empresa.ACTION.getCaption()%></title>
    <link href="<%=facade.getStyle().userInterface()%>" rel="stylesheet" type="text/css">
    <script src="include/scripts.js" type="text/javascript"></script>
  </head>
  <body style="margin:0px;" onselectstart="return true;" oncontextmenu="return false;">

    <!-- inicia o FrameBar -->
    <%=frameBar.begin()%>

      <!-- área de frames -->
      <%=frameBar.beginFrameArea()%>

        <!-- Frame de identificação do objeto -->
        <%=frameBar.actionFrame(Empresa.ACTION)%>

        <!-- Frame de comandos -->
        <%=frameBar.beginFrame("Comandos", false, false)%>
          <table style="width:100%;">
            <%if (facade.getLoggedUser().getSuperUser()) {%>
            <tr>
              <td><%=CommandControl.entityFormHyperlink(facade, empresa, Empresa.ACTION_CADASTRO, Empresa.COMMAND_INSERT, ImageList.COMMAND_INSERT, false)%></td>
            </tr>
            <%} // if%>
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
          <%=entityGrid.script(empresaInfoList, "", searchErrorMessage.toString())%>

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
