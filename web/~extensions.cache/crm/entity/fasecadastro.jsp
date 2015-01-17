
<%@include file="../include/beans.jsp"%>

<%@page import="imanager.entity.*"%>

<%
  // início do bloco protegido
  try {
    // nossa instância de Fase
    Fase fase = (Fase)facade.getEntity(Fase.CLASS_NAME);

    // FaseInfo para editar
    FaseInfo faseInfo = null;

    // se estamos inserindo...
    if (Controller.isInserting(request)) {
      // info em branco
      faseInfo = new FaseInfo();
    }
    // se estamos salvando...
    else if (Controller.isSaving(request)) {
      // info preenchido na página
      faseInfo = (FaseInfo)fase.entityInfoFromRequest(request);
      // se estamos salvando e é um novo...inclui
      if (Controller.isSavingNew(request)) {
        fase.insert(faseInfo);
      }
      // se estamos salvando e é um existente...atualiza
      else {
        fase.update(faseInfo);
      } // if
      // compõe a resposta
      Form.composeAjaxResponse(response, Form.COMPOSE_TYPE_JAVA_SCRIPT);
      // atualiza a consulta
      %><%=EntityGrid.forwardBrowse(facade, fase, Fase.ACTION, Controller.isSavingNew(request) ? EntityGrid.OPERATION_INSERT : EntityGrid.OPERATION_UPDATE, faseInfo)%><%
      // dispara
      return;
    }
    // se estamos editando...
    else if (Controller.isEditing(request)) {
      // info do registro para editar
      faseInfo =
        fase.selectByPrimaryKey(
          NumberTools.parseInt(request.getParameter(Fase.FIELD_FASE_ID.getFieldAlias()))
        );
    }
    // se o comando é desconhecido...
    else {
      throw new UnknownCommandException();
    } // if

    // nosso FrameBar
    FrameBar frameBar = new FrameBar(facade, "frameBarFase");
    // nosso Form de dados
    Form formData = new Form(request, "formDataFase", Fase.ACTION_CADASTRO, Fase.COMMAND_SAVE, "", true, true);
    // nosso PageControl
    PageControl pageControl = new PageControl(facade, "pageControl", true);
%>

<html>
  <head>
    <title><%=Fase.ACTION.getCaption()%></title>
    <link href="<%=facade.getStyle().userInterface()%>" rel="stylesheet" type="text/css">
    <script src="include/scripts.js" type="text/javascript"></script>
  </head>
  <body style="margin:0px;" onselectstart="return true;" oncontextmenu="return false;">

    <!-- inicia o FrameBar -->
    <%=frameBar.begin()%>

      <!-- área de frames -->
      <%=frameBar.beginFrameArea()%>

        <!-- Frame de identificação do objeto -->
        <%=frameBar.actionFrame(Fase.ACTION)%>

        <!-- Frame de comandos -->
        <%=frameBar.beginFrame("Comandos", false, false)%>
          <table style="width:100%;">
            <tr>
              <td><%=CommandControl.formHyperlink(facade, formData, ImageList.COMMAND_SAVE, "Deseja mesmo salvar o registro?", true, false)%></td>
            </tr>
            <tr>
              <td><%=CommandControl.entityFormHyperlink(facade, fase, Fase.ACTION_CADASTRO, Fase.COMMAND_INSERT, ImageList.COMMAND_INSERT, false)%></td>
            </tr>
            <tr>
              <td><%=CommandControl.entityBrowseHyperlink(facade, fase, Fase.ACTION, ImageList.COMMAND_BACK, "Voltar", "Volta para a consulta.", false)%></td>
            </tr>
          </table>
        <%=frameBar.endFrame()%>
      <%=frameBar.endFrameArea()%>

      <!-- Form de dados -->
      <%=formData.begin()%>

        <!-- área de dados -->
        <%=frameBar.beginClientArea()%>
          <!-- dados ocultos -->
          <%=EntityFormEdit.script(facade, Fase.FIELD_FASE_ID, faseInfo, request, -1, "", "")%>
          <!-- dados de Fase -->
          <table style="width:100%; height:100%;">
            <tr style="height:50px;">
              <td>
                <!-- campos Nome, Dias Acompanhamento e Percentual Sucesso -->
                <%=GroupBox.begin("Identificação")%>
                  <table style="width:100%; table-layout: fixed;">
                    <tr>
                      <td style="width:auto;"><%=EntityFieldLabel.script(facade, Fase.FIELD_NOME, request)%></td>
                      <td style="width:90px;"><%=EntityFieldLabel.script(facade, Fase.FIELD_DIAS_ACOMPANHAMENTO, request)%></td>
                      <td style="width:90px;"><%=EntityFieldLabel.script(facade, Fase.FIELD_PERCENTUAL_SUCESSO, request)%></td>
                    </tr>
                    <tr>
                      <td><%=EntityFormEdit.script(facade, Fase.FIELD_NOME, faseInfo, request, 0, "", "")%></td>
                      <td><%=EntityFormEdit.script(facade, Fase.FIELD_DIAS_ACOMPANHAMENTO, faseInfo, request, 0, "", "")%></td>
                      <td><%=EntityFormEdit.script(facade, Fase.FIELD_PERCENTUAL_SUCESSO, faseInfo, request, 0, "", "")%></td>
                    </tr>
                  </table>
              </td>
            </tr>
            <tr style="height:auto;">
              <td>
                <%=pageControl.begin()%>
                  <%=pageControl.beginTabSheet("Descrição")%>
                    <%=EntityFormMemo.script(facade, Fase.FIELD_DESCRICAO, faseInfo, request, 0, 0, "", "", false, false)%>
                  <%=pageControl.endTabSheet()%>
                <%=pageControl.end()%>
              </td>
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
