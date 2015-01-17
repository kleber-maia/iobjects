       
<%@include file="../include/beans.jsp"%>

<%@page import="imanager.entity.*"%>
<%@page import="imanager.ui.entity.*"%>

<%
  // início do bloco protegido
  try {
    // nossa instância de AtendimentoFrequente
    AtendimentoFrequente atendimentofrequente = (AtendimentoFrequente)facade.getEntity(AtendimentoFrequente.CLASS_NAME);

    // AtendimentoFrequenteInfo para editar
    AtendimentoFrequenteInfo atendimentofrequenteInfo = null;

    // se estamos inserindo...
    if (Controller.isInserting(request)) {
      // info em branco
      atendimentofrequenteInfo = new AtendimentoFrequenteInfo();
      // valor padrão
      atendimentofrequenteInfo.setEmpresaId(selectedEmpresaId);
    }
    // se estamos salvando...
    else if (Controller.isSaving(request)) {
      // info preenchido na página
      atendimentofrequenteInfo = (AtendimentoFrequenteInfo)atendimentofrequente.entityInfoFromRequest(request);
      // se estamos salvando e é um novo...inclui
      if (Controller.isSavingNew(request)) {
        atendimentofrequente.insert(atendimentofrequenteInfo);
      }
      // se estamos salvando e é um existente...atualiza
      else {
        atendimentofrequente.update(atendimentofrequenteInfo);
      } // if
      // compõe a resposta
      Form.composeAjaxResponse(response, Form.COMPOSE_TYPE_JAVA_SCRIPT);
      // atualiza a consulta
      %><%=EntityGrid.forwardBrowse(facade, atendimentofrequente, AtendimentoFrequente.ACTION, Controller.isSavingNew(request) ? EntityGrid.OPERATION_INSERT : EntityGrid.OPERATION_UPDATE, atendimentofrequenteInfo)%><%
      // dispara
      return;
    }
    // se estamos editando...
    else if (Controller.isEditing(request)) {
      // info do registro para editar
      atendimentofrequenteInfo =
        atendimentofrequente.selectByPrimaryKey(
          NumberTools.parseInt(request.getParameter(AtendimentoFrequente.FIELD_EMPRESA_ID.getFieldAlias())),
          NumberTools.parseInt(request.getParameter(AtendimentoFrequente.FIELD_ATENDIMENTO_FREQUENTE_ID.getFieldAlias()))
        );
    }
    // se o comando é desconhecido...
    else {
      throw new UnknownCommandException();
    } // if

    // nosso FrameBar
    FrameBar frameBar = new FrameBar(facade, "frameBarAtendimentoFrequente");
    // nosso Form de dados
    Form formData = new Form(request, "formDataAtendimentoFrequente", AtendimentoFrequente.ACTION_CADASTRO, AtendimentoFrequente.COMMAND_SAVE, "", true, true);
    // nosso PageControl
    PageControl pageControl = new PageControl(facade, "pageControl", true);
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
              <td><%=CommandControl.formHyperlink(facade, formData, ImageList.COMMAND_SAVE, "Deseja mesmo salvar o registro?", true, false)%></td>
            </tr>
            <tr>
              <td><%=CommandControl.entityFormHyperlink(facade, atendimentofrequente, AtendimentoFrequente.ACTION_CADASTRO, AtendimentoFrequente.COMMAND_INSERT, ImageList.COMMAND_INSERT, false)%></td>
            </tr>
            <tr>
              <td><%=CommandControl.entityBrowseHyperlink(facade, atendimentofrequente, AtendimentoFrequente.ACTION, ImageList.COMMAND_BACK, "Voltar", "Volta para a consulta.", false)%></td>
            </tr>
          </table>
        <%=frameBar.endFrame()%>
      <%=frameBar.endFrameArea()%>

      <!-- Form de dados -->
      <%=formData.begin()%>

        <!-- área de dados -->
        <%=frameBar.beginClientArea()%>
          <!-- dados ocultos -->
          <%=EntityFormEdit.script(facade, AtendimentoFrequente.FIELD_EMPRESA_ID, atendimentofrequenteInfo, request, -1, "", "")%>
          <%=EntityFormEdit.script(facade, AtendimentoFrequente.FIELD_ATENDIMENTO_FREQUENTE_ID, atendimentofrequenteInfo, request, -1, "", "")%>

          <!-- dados de AtendimentoFrequente -->
          <table style="width:100%; height:100%;">
            <tr style="height:50px;">
              <td>

                <%=GroupBox.begin("Identificação")%>
                  <table style="width:100%; table-layout: fixed;">
                    <tr>
                      <td style="width:auto;"><%=EntityFieldLabel.script(facade, AtendimentoFrequente.FIELD_NOME, request, "", "", false)%></td>
                      <td style="width:200px;"><%=EntityLookupLabel.script(facade, AtendimentoFrequente.LOOKUP_DEPARTAMENTO, request, "linkDepartamento", "", Departamento.ACTION, false)%></td>
                      <td style="width:200px;"><%=EntityLookupLabel.script(facade, AtendimentoFrequente.LOOKUP_ASSUNTO, request, "linkAssunto", "", Assunto.ACTION, false)%></td>
                    </tr>
                    <tr>
                      <td><%=EntityFormEdit.script(facade, AtendimentoFrequente.FIELD_NOME, atendimentofrequenteInfo, request, 0, "", "")%></td>
                      <td><%=DepartamentoUI.lookupListForLookup(facade, Atendimento.LOOKUP_DEPARTAMENTO, atendimentofrequenteInfo, selectedEmpresaId, 0, "", "")%></td>
                      <td><%=EntityFormLookupSelectEx.script(facade, Atendimento.CLASS_NAME, Atendimento.LOOKUP_ASSUNTO, Assunto.LEVEL_DELIMITER, atendimentofrequenteInfo, request, 0, "", "", true)%></td>
                    </tr>
                  </table>
                <%=GroupBox.end()%>

              </td>
            </tr>
            <tr style="height:auto;">
              <td>

                <%=pageControl.begin()%>
                  <%=pageControl.beginTabSheet("Descrição")%>
                    <%=EntityFormMemo.script(facade, AtendimentoFrequente.FIELD_DESCRICAO, atendimentofrequenteInfo, request, 0, 0, "", "", false, false)%>
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
      var wysiwygId = "<%=Atendimento.FIELD_DESCRICAO.getFieldAlias()%>";
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
