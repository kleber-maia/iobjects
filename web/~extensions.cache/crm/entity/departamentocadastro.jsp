       
<%@include file="../include/beans.jsp"%>

<%@page import="imanager.entity.*"%>

<%
  // início do bloco protegido
  try {
    // nossa instância de Departamento
    Departamento departamento = (Departamento)facade.getEntity(Departamento.CLASS_NAME);

    // DepartamentoInfo para editar
    DepartamentoInfo departamentoInfo = null;

    // se estamos inserindo...
    if (Controller.isInserting(request)) {
      // info em branco
      departamentoInfo = new DepartamentoInfo();
      // valores padrão
      departamentoInfo.setEmpresaId(selectedEmpresaId);
    }
    // se estamos salvando...
    else if (Controller.isSaving(request)) {
      // info preenchido na página
      departamentoInfo = (DepartamentoInfo)departamento.entityInfoFromRequest(request);
      // se estamos salvando e é um novo...inclui
      if (Controller.isSavingNew(request)) {
        departamento.insert(departamentoInfo);
      }
      // se estamos salvando e é um existente...atualiza
      else {
        departamento.update(departamentoInfo);
      } // if
      // compõe a resposta
      Form.composeAjaxResponse(response, Form.COMPOSE_TYPE_JAVA_SCRIPT);
      // atualiza a consulta
      %><%=EntityGrid.forwardBrowse(facade, departamento, Departamento.ACTION, Controller.isSavingNew(request) ? EntityGrid.OPERATION_INSERT : EntityGrid.OPERATION_UPDATE, departamentoInfo)%><%
      // dispara
      return;
    }
    // se estamos editando...
    else if (Controller.isEditing(request)) {
      // info do registro para editar
      departamentoInfo =
        departamento.selectByPrimaryKey(
          NumberTools.parseInt(request.getParameter(Departamento.FIELD_EMPRESA_ID.getFieldAlias())),
          NumberTools.parseInt(request.getParameter(Departamento.FIELD_DEPARTAMENTO_ID.getFieldAlias()))
        );
    }
    // se o comando é desconhecido...
    else {
      throw new UnknownCommandException();
    } // if

    // nosso FrameBar
    FrameBar frameBar = new FrameBar(facade, "frameBarDepartamento");
    // nosso Form de dados
    Form formData = new Form(request, "formDataDepartamento", Departamento.ACTION_CADASTRO, Departamento.COMMAND_SAVE, "", true, true);
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
              <td><%=CommandControl.formHyperlink(facade, formData, ImageList.COMMAND_SAVE, "Deseja mesmo salvar o registro?", true, false)%></td>
            </tr>
            <tr>
              <td><%=CommandControl.entityFormHyperlink(facade, departamento, Departamento.ACTION_CADASTRO, Departamento.COMMAND_INSERT, ImageList.COMMAND_INSERT, false)%></td>
            </tr>
            <tr>
              <td><%=CommandControl.entityBrowseHyperlink(facade, departamento, Departamento.ACTION, ImageList.COMMAND_BACK, "Voltar", "Volta para a consulta.", false)%></td>
            </tr>
          </table>
        <%=frameBar.endFrame()%>
      <%=frameBar.endFrameArea()%>

      <!-- Form de dados -->
      <%=formData.begin()%>

        <!-- área de dados -->
        <%=frameBar.beginClientArea()%>
          <!-- dados ocultos -->
          <%=EntityFormEdit.script(facade, Departamento.FIELD_EMPRESA_ID, departamentoInfo, request, -1, "", "")%>
          <%=EntityFormEdit.script(facade, Departamento.FIELD_DEPARTAMENTO_ID, departamentoInfo, request, -1, "", "")%>
          <!-- dados de Departamento -->
          <%=GroupBox.begin("Identificação")%>
            <table style="width:100%;">
              <tr>
                <td><%=EntityFieldLabel.script(facade, Departamento.FIELD_NOME, request)%></td>
              </tr>
              <tr>
                <td><%=EntityFormEdit.script(facade, Departamento.FIELD_NOME, departamentoInfo, request, 0, "", "")%></td>
              </tr>
            </table>
          <%=GroupBox.end()%>
        <%=frameBar.endClientArea()%>
        
      <%=formData.end()%>
    <%=frameBar.end()%>

  </body>
</html>

<%}
  // término do bloco protegido
  catch (Exception e) {
    Controller.forwardException(e, pageContext);
  } // try-catch
%>
