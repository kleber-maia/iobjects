
<%@include file="../include/beans.jsp"%>

<%@page import="imanager.entity.*"%>

<%
  // início do bloco protegido
  try {
    // nossa instância de Meio
    Meio meio = (Meio)facade.getEntity(Meio.CLASS_NAME);

    // MeioInfo para editar
    MeioInfo meioInfo = null;

    // se estamos inserindo...
    if (Controller.isInserting(request)) {
      // info em branco
      meioInfo = new MeioInfo();
    }
    // se estamos salvando...
    else if (Controller.isSaving(request)) {
      // info preenchido na página
      meioInfo = (MeioInfo)meio.entityInfoFromRequest(request);
      // se estamos salvando e é um novo...inclui
      if (Controller.isSavingNew(request)) {
        meio.insert(meioInfo);
      }
      // se estamos salvando e é um existente...atualiza
      else {
        meio.update(meioInfo);
      } // if
      // compõe a resposta
      Form.composeAjaxResponse(response, Form.COMPOSE_TYPE_JAVA_SCRIPT);
      // atualiza a consulta
      %><%=EntityGrid.forwardBrowse(facade, meio, Meio.ACTION, Controller.isSavingNew(request) ? EntityGrid.OPERATION_INSERT : EntityGrid.OPERATION_UPDATE, meioInfo)%><%
      // dispara
      return;
    }
    // se estamos editando...
    else if (Controller.isEditing(request)) {
      // info do registro para editar
      meioInfo =
        meio.selectByPrimaryKey(
          NumberTools.parseInt(request.getParameter(Meio.FIELD_MEIO_ID.getFieldAlias()))
        );
    }
    // se o comando é desconhecido...
    else {
      throw new UnknownCommandException();
    } // if

    // nosso FrameBar
    FrameBar frameBar = new FrameBar(facade, "frameBarMeio");
    // nosso Form de dados
    Form formData = new Form(request, "formDataMeio", Meio.ACTION_CADASTRO, Meio.COMMAND_SAVE, "", true, true);
%>

<html>
  <head>
    <title><%=Meio.ACTION.getCaption()%></title>
    <link href="<%=facade.getStyle().userInterface()%>" rel="stylesheet" type="text/css">
    <script src="include/scripts.js" type="text/javascript"></script>
  </head>
  <body style="margin:0px;" onselectstart="return true;" oncontextmenu="return false;">

    <!-- inicia o FrameBar -->
    <%=frameBar.begin()%>

      <!-- área de frames -->
      <%=frameBar.beginFrameArea()%>

        <!-- Frame de identificação do objeto -->
        <%=frameBar.actionFrame(Meio.ACTION)%>

        <!-- Frame de comandos -->
        <%=frameBar.beginFrame("Comandos", false, false)%>
          <table style="width:100%;">
            <tr>
              <td><%=CommandControl.formHyperlink(facade, formData, ImageList.COMMAND_SAVE, "Deseja mesmo salvar o registro?", true, false)%></td>
            </tr>
            <tr>
              <td><%=CommandControl.entityFormHyperlink(facade, meio, Meio.ACTION_CADASTRO, Meio.COMMAND_INSERT, ImageList.COMMAND_INSERT, false)%></td>
            </tr>
            <tr>
              <td><%=CommandControl.entityBrowseHyperlink(facade, meio, Meio.ACTION, ImageList.COMMAND_BACK, "Voltar", "Volta para a consulta.", false)%></td>
            </tr>
          </table>
        <%=frameBar.endFrame()%>
      <%=frameBar.endFrameArea()%>

      <!-- Form de dados -->
      <%=formData.begin()%>
        <!-- dados ocultos -->
        <%=EntityFormEdit.script(facade, Meio.FIELD_MEIO_ID, meioInfo, request, -1, "", "")%>
        <!-- área de dados -->
        <%=frameBar.beginClientArea()%>
          <!-- dados de Meio -->
          <%=GroupBox.begin("Identificação")%>
            <!-- campo Nome -->
            <table style="width:100%;">
              <tr>
                <td><%=EntityFieldLabel.script(facade, Meio.FIELD_NOME, request)%></td>
              </tr>
              <tr>
                <td><%=EntityFormEdit.script(facade, Meio.FIELD_NOME, meioInfo, request, 0, "", "")%></td>
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
