
<%@include file="../include/beans.jsp"%>

<%@page import="imanager.entity.*"%>
<%@page import="imanager.ui.entity.*"%>

<%
  // início do bloco protegido
  try {
    // nossa instância de Modelo Atendimento
    ModeloAtendimento modeloAtendimento = (ModeloAtendimento)facade.getEntity(ModeloAtendimento.CLASS_NAME);

    // ModeloAtendimentoInfo para editar
    ModeloAtendimentoInfo modeloAtendimentoInfo = null;

    // se estamos inserindo...
    if (Controller.isInserting(request)) {
      // info em branco
      modeloAtendimentoInfo = new ModeloAtendimentoInfo();
      // valores padrão
      modeloAtendimentoInfo.setEmpresaId(selectedEmpresaId);
    }
    // se estamos salvando...
    else if (Controller.isSaving(request)) {
      // info preenchido na página
      modeloAtendimentoInfo = (ModeloAtendimentoInfo)modeloAtendimento.entityInfoFromRequest(request);
      // se estamos salvando e é um novo...inclui
      if (Controller.isSavingNew(request)) {
        modeloAtendimento.insert(modeloAtendimentoInfo);
      }
      // se estamos salvando e é um existente...atualiza
      else {
        modeloAtendimento.update(modeloAtendimentoInfo);
      } // if
      // compõe a resposta
      Form.composeAjaxResponse(response, Form.COMPOSE_TYPE_JAVA_SCRIPT);
      // atualiza a consulta
      %><%=EntityGrid.forwardBrowse(facade, modeloAtendimento, ModeloAtendimento.ACTION, Controller.isSavingNew(request) ? EntityGrid.OPERATION_INSERT : EntityGrid.OPERATION_UPDATE, modeloAtendimentoInfo)%><%
      // dispara
      return;
    }
    // se estamos editando...
    else if (Controller.isEditing(request)) {
      // info do registro para editar
      modeloAtendimentoInfo =
        modeloAtendimento.selectByPrimaryKey(
          NumberTools.parseInt(request.getParameter(ModeloAtendimento.FIELD_EMPRESA_ID.getFieldAlias())),
          NumberTools.parseInt(request.getParameter(ModeloAtendimento.FIELD_MODELO_ATENDIMENTO_ID.getFieldAlias()))
        );
    }
    // se o comando é desconhecido...
    else {
      throw new UnknownCommandException();
    } // if

    // nosso FrameBar
    FrameBar frameBar = new FrameBar(facade, "frameBarModeloAtendimento");
    // nosso Form de dados
    Form formData = new Form(request, "formDataModeloAtendimento", ModeloAtendimento.ACTION_CADASTRO, ModeloAtendimento.COMMAND_SAVE, "", true, true);
    // nosso PageControl
    PageControl pageControl = new PageControl(facade, "pageControl", true);
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
              <td><%=CommandControl.formHyperlink(facade, formData, ImageList.COMMAND_SAVE, "Deseja mesmo salvar o registro?", true, false)%></td>
            </tr>
            <tr>
              <td><%=CommandControl.entityFormHyperlink(facade, modeloAtendimento, ModeloAtendimento.ACTION_CADASTRO, ModeloAtendimento.COMMAND_INSERT, ImageList.COMMAND_INSERT, false)%></td>
            </tr>
            <tr>
              <td><%=CommandControl.entityBrowseHyperlink(facade, modeloAtendimento, ModeloAtendimento.ACTION, ImageList.COMMAND_BACK, "Voltar", "Volta para a consulta.", false)%></td>
            </tr>
          </table>
        <%=frameBar.endFrame()%>
      <%=frameBar.endFrameArea()%>

      <!-- Form de dados -->
      <%=formData.begin()%>

        <!-- área de dados -->
        <%=frameBar.beginClientArea()%>
          <!-- dados ocultos -->
          <%=EntityFormEdit.script(facade, ModeloAtendimento.FIELD_EMPRESA_ID, modeloAtendimentoInfo, request, -1, "", "")%>
          <%=EntityFormEdit.script(facade, ModeloAtendimento.FIELD_MODELO_ATENDIMENTO_ID, modeloAtendimentoInfo, request, -1, "", "")%>
          <!-- dados de Modelo Atendimento -->
          <table style="width:100%; height:100%;">
            <tr style="height:50px;">
              <td>
                <!-- campos Nome, Departamento e Assunto -->
                <%=GroupBox.begin("Identificação")%>
                  <table style="width:100%; table-layout: fixed;">
                    <tr style="height:10px;">
                      <td style="width:auto;"><%=EntityFieldLabel.script(facade, ModeloAtendimento.FIELD_NOME, request)%></td>
                      <td style="width:250px;"><%=EntityLookupLabel.script(facade, ModeloAtendimento.LOOKUP_DEPARTAMENTO, request)%></td>
                      <td style="width:250px;"><%=EntityLookupLabel.script(facade, ModeloAtendimento.LOOKUP_ASSUNTO, request)%></td>
                    </tr>
                    <tr style="height:20px;">
                      <td><%=EntityFormEdit.script(facade, ModeloAtendimento.FIELD_NOME, modeloAtendimentoInfo, request, 0, "", "")%></td>
                      <td><%=DepartamentoUI.lookupListForLookup(facade, ModeloAtendimento.LOOKUP_DEPARTAMENTO, modeloAtendimentoInfo, selectedEmpresaId, 0, "", "")%></td>
                      <td><%=EntityFormLookupSelectEx.script(facade, ModeloAtendimento.CLASS_NAME, ModeloAtendimento.LOOKUP_ASSUNTO, Assunto.LEVEL_DELIMITER, modeloAtendimentoInfo, request, 0, "", "", true)%></td>
                    </tr>
                  </table>
                <%=GroupBox.end()%>
              </td>
            </tr>
            <!-- campo Descrição -->
            <tr style="height:auto;">
              <td>
                <%=pageControl.begin()%>
                  <%=pageControl.beginTabSheet("Descrição")%>
                    <%=EntityFormMemo.script(facade, ModeloAtendimento.FIELD_DESCRICAO, modeloAtendimentoInfo, request, 0, 0, "", "", false, false)%>
                  <%=pageControl.endTabSheet()%>
                <%=pageControl.end()%>
              </td>
            </tr>
          </table>
        <%=frameBar.endClientArea()%>

      <%=formData.end()%>
    <%=frameBar.end()%>

    <script type="text/javascript">
      // nosso Id
      var wysiwygId = "<%=ModeloAtendimento.FIELD_DESCRICAO.getFieldAlias()%>";
      // toolbar do Wysiwyg
      var toolbar = document.getElementById("toolbar" + wysiwygId);
      // wysiwyg
      var wysiwyg = document.getElementById("wysiwyg" + wysiwygId);
      // evento keyDown
      if (wysiwyg.addEventListener)
        wysiwyg.addEventListener("keydown", wysiwygKeyDown, false);
      else
        wysiwyg.contentWindow.document.attachEvent("onkeydown", wysiwygKeyDown);
      // adiciona os novos botões
      toolbar.innerHTML = "<button style='width:125px; height:24px; text-align:center;' title='Inicia um diálogo do cliente. (Alt+C)' class='ToolButton' onClick='clienteClick();' onmouseover='className=\"ToolButtonOver\";' onmouseout='className=\"ToolButton\";' unselectable='on'><img src=\"images/balloon16x16.png\" align=\"absmiddle\" border=0>&nbsp;Diálogo do Cliente</button>"
                        + "<button style='width:125px; height:24px; text-align:center;' title='Inicia um diálogo do usuário. (Alt+U)' class='ToolButton' onClick='usuarioClick();' onmouseover='className=\"ToolButtonOver\";' onmouseout='className=\"ToolButton\";' unselectable='on'><img src=\"images/user16x16.png\" align=\"absmiddle\" border=0>&nbsp;Diálogo do Usuário</button>";

      function clienteClick() {
        format("Cliente: ", "black");
      }

      function usuarioClick() {
        format("Usuário: ", "red");
      }

      function format(text, color) {
        // põe o foco
        wysiwyg.contentWindow.focus();
        // insere o html
        insertHTML("<p>", wysiwygId);
        wysiwyg.contentWindow.document.execCommand("ForeColor", false, color);
        insertHTML("<span style='color:" + color + ";'>" + text + "</span>", wysiwygId);
      }

      function wysiwygKeyDown(event) {
        // tecla pressionada
        var key = (event.keyCode ? event.keyCode : event.which ? event.which : event.charCode);
        // se teclou Alt
        var isAlt = event.altKey;
        // se teclou Alt+C
     	  if (isAlt && (key == 67))
    	  	  clienteClick();
     	  else if (isAlt && (key == 85))
    	  	  usuarioClick();
      }
    </script>

  </body>
</html>

<%}
  // término do bloco protegido
  catch (Exception e) {
    Controller.forwardException(e, pageContext);
  } // try-catch
%>
