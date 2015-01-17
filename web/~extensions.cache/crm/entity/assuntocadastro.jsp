       
<%@include file="../include/beans.jsp"%>

<%@page import="imanager.entity.*"%>

<%
  // início do bloco protegido
  try {
    // nossa instância de Assunto
    Assunto assunto = (Assunto)facade.getEntity(Assunto.CLASS_NAME);

    // AssuntoInfo para editar
    AssuntoInfo assuntoInfo = null;

    // AssuntoInfo pai para inserir filho
    AssuntoInfo assuntoInfoPai = null;
    // define os valores dos parâmetros de usuário
    assunto.userParamList().setParamsValues(request);

    // se estamos inserindo...
    if (Controller.isInserting(request)) {
      // info em branco
      assuntoInfo = new AssuntoInfo();
      // se estamos incluindo um filho...
      if (assunto.USER_PARAM_ASSUNTO_PAI_ID.getIntValue() > 0) {
        // localiza o Assunto pai
        assuntoInfoPai = assunto.selectByPrimaryKey(assunto.USER_PARAM_ASSUNTO_PAI_ID.getIntValue());
        // põe o titulo do Assunto pai e o separador de nível no titulo do novo Assunto
        assuntoInfo.setNome(assuntoInfoPai.getNome() + Assunto.LEVEL_DELIMITER);
      } // if
    }
    // se estamos salvando...
    else if (Controller.isSaving(request)) {
      // info preenchido na página
      assuntoInfo = (AssuntoInfo)assunto.entityInfoFromRequest(request);
      // põe o caminho do Assunto pai junto ao titulo do filho
      if (!assunto.USER_PARAM_ASSUNTO_PAI.getValue().equals(""))
        assuntoInfo.setNome(assunto.USER_PARAM_ASSUNTO_PAI.getValue() + Assunto.LEVEL_DELIMITER + assuntoInfo.getNome());
      // se estamos salvando e é um novo...inclui
      if (Controller.isSavingNew(request)) {
        assunto.insert(assuntoInfo);
      }
      // se estamos salvando e é um existente...atualiza
      else {
        assunto.update(assuntoInfo);
      } // if
      // compõe a resposta
      Form.composeAjaxResponse(response, Form.COMPOSE_TYPE_JAVA_SCRIPT);
      // atualiza a consulta
      if (Controller.isSavingNew(request) && assuntoInfo.getNome().contains(Assunto.LEVEL_DELIMITER)) {%>
        <%=EntityGrid.forwardBrowse(facade, assunto, Assunto.ACTION, "search()")%>
      <%}
      else {%>
        <%=EntityGrid.forwardBrowse(facade, assunto, Assunto.ACTION, Controller.isSavingNew(request) ? EntityGrid.OPERATION_INSERT : EntityGrid.OPERATION_UPDATE, assuntoInfo)%>
      <%} // if
      // dispara
      return;
    }
    // se estamos editando...
    else if (Controller.isEditing(request)) {
      // info do registro para editar
      assuntoInfo =
        assunto.selectByPrimaryKey(
          NumberTools.parseInt(request.getParameter(Assunto.FIELD_ASSUNTO_ID.getFieldAlias()))
        );
    }
    // se o comando é desconhecido...
    else {
      throw new UnknownCommandException();
    } // if

    // nosso FrameBar
    FrameBar frameBar = new FrameBar(facade, "frameBarAssunto");
    // nosso Form de dados
    Form formData = new Form(request, "formDataAssunto", Assunto.ACTION_CADASTRO, Assunto.COMMAND_SAVE, "", true, true);
%>

<html>
  <head>
    <title><%=Assunto.ACTION.getCaption()%></title>
    <link href="<%=facade.getStyle().userInterface()%>" rel="stylesheet" type="text/css">
    <script src="include/scripts.js" type="text/javascript"></script>
  </head>
  <body style="margin:0px;" onselectstart="return true;" oncontextmenu="return false;">

    <!-- inicia o FrameBar -->
    <%=frameBar.begin()%>

      <!-- área de frames -->
      <%=frameBar.beginFrameArea()%>

        <!-- Frame de identificação do objeto -->
        <%=frameBar.actionFrame(Assunto.ACTION)%>

        <!-- Frame de comandos -->
        <%=frameBar.beginFrame("Comandos", false, false)%>
          <table style="width:100%;">
            <tr>
              <td><%=CommandControl.formHyperlink(facade, formData, ImageList.COMMAND_SAVE, "Deseja mesmo salvar o registro?", true, false)%></td>
            </tr>
            <tr>
              <td><%=CommandControl.entityFormHyperlink(facade, assunto, Assunto.ACTION_CADASTRO, Assunto.COMMAND_INSERT, ImageList.COMMAND_INSERT, false)%></td>
            </tr>
            <tr>
              <td><%=CommandControl.entityBrowseHyperlink(facade, assunto, Assunto.ACTION, ImageList.COMMAND_BACK, "Voltar", "Volta para a consulta.", false)%></td>
            </tr>
          </table>
        <%=frameBar.endFrame()%>
      <%=frameBar.endFrameArea()%>

      <!-- Form de dados -->
      <%=formData.begin()%>

        <!-- área de dados -->
        <%=frameBar.beginClientArea()%>
          <!-- dados ocultos -->
          <%=EntityFormEdit.script(facade, Assunto.FIELD_ASSUNTO_ID, assuntoInfo, request, -1, "", "")%>
          <%=ParamFormEdit.script(facade, assunto.USER_PARAM_ASSUNTO_PAI, -1, "", "")%>
          <!-- dados de Centro Custo -->
          <%if (assuntoInfoPai != null) {%>
            <table style="width:100%;">
              <tr style="height:10px;">
                <td class="Info"><img alt="" src="<%=ImageList.IMAGE_INFORMATION%>" align="absmiddle" /> Inserindo filho de '<%=assuntoInfoPai.getNome()%>'.</td>
              </tr>
            </table>
          <%} //if%>
          <!-- campo Nome -->
          <%=GroupBox.begin("Identificação")%>
            <table style="width:100%;">
              <tr>
                <td><%=EntityFieldLabel.script(facade, Assunto.FIELD_NOME, request)%></td>
              </tr>
              <tr>
                <td><%=EntityFormEdit.script(facade, Assunto.FIELD_NOME, assuntoInfo, request, 0, "", "")%></td>
              </tr>
            </table>
          <%=GroupBox.end()%>
        <%=frameBar.endClientArea()%>

        <script type="text/javascript">
          <%// se é um Assunto filho...
            if (assuntoInfo.getNome().indexOf(Assunto.LEVEL_DELIMITER) > 0) {
              // caminho do Assunto pai
              String tituloPai = assuntoInfo.getNome().substring(0, assuntoInfo.getNome().lastIndexOf(Assunto.LEVEL_DELIMITER));
              // titulo do Assunto
              String titulo = assuntoInfo.getNome().substring(assuntoInfo.getNome().lastIndexOf(Assunto.LEVEL_DELIMITER)+1);%>
              // guarda o caminho do Assunto pai no parâmetro
              formDataAssunto.<%=assunto.USER_PARAM_ASSUNTO_PAI.getName()%>.value = "<%=tituloPai%>";
              // guarda apenas o titulo do Assunto filho no campo
              formDataAssunto.<%=Assunto.FIELD_NOME.getFieldAlias()%>.value = "<%=titulo%>";
          <%}
            else {%>
            // não temos Assunto pai
            formDataAssunto.<%=assunto.USER_PARAM_ASSUNTO_PAI.getName()%>.value = "";
          <%} // if%>
        </script>

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
