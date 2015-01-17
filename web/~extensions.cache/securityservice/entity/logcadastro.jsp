       
<%@include file="../include/beans.jsp"%>

<%@page import="securityservice.entity.*"%>

<%
  // início do bloco protegido
  try {
    // nossa instância de Log
    securityservice.entity.Log log = (securityservice.entity.Log)facade.getEntity(securityservice.entity.Log.CLASS_NAME);

    // LogInfo para editar
    LogInfo logInfo = null;

    // se estamos inserindo...
    if (Controller.isInserting(request)) {
      // info em branco
      logInfo = new LogInfo();
    }
    // se estamos salvando...
    else if (Controller.isSaving(request)) {
      // info preenchido na página
      logInfo = (LogInfo)log.entityInfoFromRequest(request);
      // se estamos salvando e é um novo...inclui
      if (Controller.isSavingNew(request)) {
        log.insert(logInfo);
      }
      // se estamos salvando e é um existente...atualiza
      else {
        log.update(logInfo);
      } // if
      // compõe a resposta
      Form.composeAjaxResponse(response, Form.COMPOSE_TYPE_JAVA_SCRIPT);
      // atualiza a consulta
      %><%=EntityGrid.forwardBrowse(facade, log, securityservice.entity.Log.ACTION, Controller.isSavingNew(request) ? EntityGrid.OPERATION_INSERT : EntityGrid.OPERATION_UPDATE, logInfo)%><%
      // dispara
      return;
    }
    // se estamos editando...
    else if (Controller.isEditing(request)) {
      // info do registro para editar
      logInfo =
        log.selectByPrimaryKey(
          NumberTools.parseInt(request.getParameter(securityservice.entity.Log.FIELD_LOG_ID.getFieldAlias()))
        );
    }
    // se o comando é desconhecido...
    else {
      throw new UnknownCommandException();
    } // if

    // nosso FrameBar
    FrameBar frameBar = new FrameBar(facade, "frameBarLog");
    // nosso Form de dados
    Form formData = new Form(request, "formDataLog", securityservice.entity.Log.ACTION_CADASTRO, securityservice.entity.Log.COMMAND_SAVE, "", true, true);
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
              <td><%=CommandControl.formHyperlink(facade, formData, ImageList.COMMAND_SAVE, "Deseja mesmo salvar o registro?", true, false)%></td>
            </tr>
            <tr>
              <td><%=CommandControl.entityFormHyperlink(facade, log, securityservice.entity.Log.ACTION_CADASTRO, securityservice.entity.Log.COMMAND_INSERT, ImageList.COMMAND_INSERT, false)%></td>
            </tr>
            <tr>
              <td><%=CommandControl.entityBrowseHyperlink(facade, log, securityservice.entity.Log.ACTION, ImageList.COMMAND_BACK, "Voltar", "Volta para a consulta.", false)%></td>
            </tr>
          </table>
        <%=frameBar.endFrame()%>
      <%=frameBar.endFrameArea()%>

      <!-- Form de dados -->
      <%=formData.begin()%>

        <!-- área de dados -->
        <%=frameBar.beginClientArea()%>
          <!-- dados de Log -->
          <table>
            <!-- campo Log ID -->
            <tr>
              <td><%=EntityFieldLabel.script(facade, securityservice.entity.Log.FIELD_LOG_ID, request)%></td>
              <td><%=EntityFormEdit.script(facade, securityservice.entity.Log.FIELD_LOG_ID, logInfo, request, 70, "", "")%></td>
            </tr>
            <!-- campo Data -->
            <tr>
              <td><%=EntityFieldLabel.script(facade, securityservice.entity.Log.FIELD_DATA, request)%></td>
              <td><%=EntityFormEdit.script(facade, securityservice.entity.Log.FIELD_DATA, logInfo, request, 70, "", "")%></td>
            </tr>
            <!-- campo Usuário ID -->
            <tr>
              <td><%=EntityFieldLabel.script(facade, securityservice.entity.Log.FIELD_USUARIO_ID, request)%></td>
              <td><%=EntityFormEdit.script(facade, securityservice.entity.Log.FIELD_USUARIO_ID, logInfo, request, 70, "", "")%></td>
            </tr>
            <!-- campo Operação -->
            <tr>
              <td><%=EntityFieldLabel.script(facade, securityservice.entity.Log.FIELD_OPERACAO, request)%></td>
              <td><%=EntityFormEdit.script(facade, securityservice.entity.Log.FIELD_OPERACAO, logInfo, request, 70, "", "")%></td>
            </tr>
            <!-- campo Objeto -->
            <tr>
              <td><%=EntityFieldLabel.script(facade, securityservice.entity.Log.FIELD_OBJETO, request)%></td>
              <td><%=EntityFormEdit.script(facade, securityservice.entity.Log.FIELD_OBJETO, logInfo, request, 250, "", "")%></td>
            </tr>
            <!-- campo Descrição -->
            <tr>
              <td><%=EntityFieldLabel.script(facade, securityservice.entity.Log.FIELD_DESCRICAO, request)%></td>
              <td><%=EntityFormEdit.script(facade, securityservice.entity.Log.FIELD_DESCRICAO, logInfo, request, 250, "", "")%></td>
            </tr>
          </table>
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
