       
<%@include file="../include/beans.jsp"%>

<%@page import="imanager.entity.*"%>

<%
  // início do bloco protegido
  try {
    // nossa instância de Grupo Contato
    GrupoContato grupoContato = (GrupoContato)facade.getEntity(GrupoContato.CLASS_NAME);

    // GrupoContatoInfo para editar
    GrupoContatoInfo grupoContatoInfo = null;

    // GrupoContatoInfo para editar
    GrupoContatoInfo grupoContatoInfoPai = null;  

    // define os valores dos parâmetros de usuário
    grupoContato.userParamList().setParamsValues(request);        
    // se estamos inserindo...
    if (Controller.isInserting(request)) {
      // info em branco
      grupoContatoInfo = new GrupoContatoInfo();
      // se estamos incluindo um filho...
      if (grupoContato.USER_PARAM_GRUPO_CONTATO_PAI_ID.getIntValue() > 0) {
        // localiza o GrupoContato pai
        grupoContatoInfoPai = grupoContato.selectByPrimaryKey(grupoContato.USER_PARAM_GRUPO_CONTATO_PAI_ID.getIntValue());
        // põe o titulo do Categoria pai e o separador de nível no titulo do novo Categoria
        grupoContatoInfo.setNome(grupoContatoInfoPai.getNome() + "/");
      } // if
      
    }
    // se estamos salvando...
    else if (Controller.isSaving(request)) {
      // info preenchido na página
      grupoContatoInfo = (GrupoContatoInfo)grupoContato.entityInfoFromRequest(request);
      // põe o caminho do Categoria pai junto ao titulo do filho
      if (!grupoContato.USER_PARAM_GRUPO_CONTATO_PAI.getValue().equals(""))
        grupoContatoInfo.setNome(grupoContato.USER_PARAM_GRUPO_CONTATO_PAI.getValue() + "/" + grupoContatoInfo.getNome());      
      // se estamos salvando e é um novo...inclui
      if (Controller.isSavingNew(request)) {
        grupoContato.insert(grupoContatoInfo);
      }
      // se estamos salvando e é um existente...atualiza
      else {
        grupoContato.update(grupoContatoInfo);
      } // if
      // compõe a resposta
      Form.composeAjaxResponse(response, Form.COMPOSE_TYPE_JAVA_SCRIPT);
      // atualiza a consulta
      if (Controller.isSavingNew(request) && grupoContatoInfo.getNome().contains("/")) {%>
        <%=EntityGrid.forwardBrowse(facade, grupoContato, GrupoContato.ACTION, "search()")%>
      <%}
      else {%>
        <%=EntityGrid.forwardBrowse(facade, grupoContato, GrupoContato.ACTION, Controller.isSavingNew(request) ? EntityGrid.OPERATION_INSERT : EntityGrid.OPERATION_UPDATE, grupoContatoInfo)%>
      <%} // if
      // dispara
      return;
    }
    // se estamos editando...
    else if (Controller.isEditing(request)) {
      // info do registro para editar
      grupoContatoInfo =
        grupoContato.selectByPrimaryKey(
          NumberTools.parseInt(request.getParameter(GrupoContato.FIELD_GRUPO_CONTATO_ID.getFieldAlias()))
        );
    }
    // se o comando é desconhecido...
    else {
      throw new UnknownCommandException();
    } // if

    // nosso FrameBar
    FrameBar frameBar = new FrameBar(facade, "frameBarGrupoContato");
    // nosso Form de dados
    Form formData = new Form(request, "formDataGrupoContato", GrupoContato.ACTION_CADASTRO, GrupoContato.COMMAND_SAVE, "", true, true);
%>

<html>
  <head>
    <title><%=GrupoContato.ACTION.getCaption()%></title>
    <link href="<%=facade.getStyle().userInterface()%>" rel="stylesheet" type="text/css">
    <script src="include/scripts.js" type="text/javascript"></script>
  </head>
  <body style="margin:0px;" onselectstart="return true;" oncontextmenu="return false;">

    <!-- inicia o FrameBar -->
    <%=frameBar.begin()%>

      <!-- área de frames -->
      <%=frameBar.beginFrameArea()%>

        <!-- Frame de identificação do objeto -->
        <%=frameBar.actionFrame(GrupoContato.ACTION)%>

        <!-- Frame de comandos -->
        <%=frameBar.beginFrame("Comandos", false, false)%>
          <table style="width:100%;">
            <tr>
              <td><%=CommandControl.formHyperlink(facade, formData, ImageList.COMMAND_SAVE, "Deseja mesmo salvar o registro?", true, false)%></td>
            </tr>
            <tr>
              <td><%=CommandControl.entityFormHyperlink(facade, grupoContato, GrupoContato.ACTION_CADASTRO, GrupoContato.COMMAND_INSERT, ImageList.COMMAND_INSERT, false)%></td>
            </tr>
            <tr>
              <td><%=CommandControl.entityBrowseHyperlink(facade, grupoContato, GrupoContato.ACTION, ImageList.COMMAND_BACK, "Voltar", "Volta para a consulta.", false)%></td>
            </tr>
          </table>
        <%=frameBar.endFrame()%>
      <%=frameBar.endFrameArea()%>

      <!-- Form de dados -->
      <%=formData.begin()%>

        <!-- área de dados -->
        <%=frameBar.beginClientArea()%>
          <!-- dados ocultos -->
          <%=EntityFormEdit.script(facade, GrupoContato.FIELD_GRUPO_CONTATO_ID, grupoContatoInfo, request, -1, "", "")%>
          <%=ParamFormEdit.script(facade, grupoContato.USER_PARAM_GRUPO_CONTATO_PAI, -1, "", "")%>          
          <!-- dados de Grupo Contato -->
          <%=GroupBox.begin("Identificação")%>
            <%if (grupoContatoInfoPai != null) {%>
            <table style="width:100%;">
              <tr style="height:10px;">
                <td class="Info"><img alt="" src="<%=ImageList.IMAGE_INFORMATION%>" align="absmiddle" /> Inserindo filho de '<%=grupoContatoInfoPai.getNome()%>'.</td>
              </tr>
            </table>
            <%} //if%>
          
            <table style="width:100%;">
              <tr>
                <td><%=EntityFieldLabel.script(facade, GrupoContato.FIELD_NOME, request)%></td>
              </tr>
              <tr>
                <td><%=EntityFormEdit.script(facade, GrupoContato.FIELD_NOME, grupoContatoInfo, request, 0, "", "")%></td>
              </tr>
            </table>
          <%=GroupBox.end()%>
        <%=frameBar.endClientArea()%>
        
        <script type="text/javascript">
          <%// se é um Categoria filho...
            if (grupoContatoInfo.getNome().indexOf(GrupoContato.LEVEL_DELIMITER) > 0) {
              // caminho do Categoria pai
              String tituloPai = grupoContatoInfo.getNome().substring(0, grupoContatoInfo.getNome().lastIndexOf(GrupoContato.LEVEL_DELIMITER));
              // titulo do Categoria
              String titulo = grupoContatoInfo.getNome().substring(grupoContatoInfo.getNome().lastIndexOf(GrupoContato.LEVEL_DELIMITER)+1);%>
              // guarda o caminho do Categoria pai no parâmetro
              formDataGrupoContato.<%=grupoContato.USER_PARAM_GRUPO_CONTATO_PAI.getName()%>.value = "<%=tituloPai%>";
              // guarda apenas o titulo do Categoria filho no campo
              formDataGrupoContato.<%=GrupoContato.FIELD_NOME.getFieldAlias()%>.value = "<%=titulo%>";
          <%}
            else {%>
            // não temos Categoria pai
            formDataGrupoContato.<%=grupoContato.USER_PARAM_GRUPO_CONTATO_PAI.getName()%>.value = "";
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
