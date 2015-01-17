
<%@include file="../include/beans.jsp"%>

<%@page import="java.sql.*"%>
<%@page import="securityservice.entity.*"%>

<%!
  String DUMMY    = "no password";
  String PASSWORD = "password";
%>

<%
  try {
    // nossa instância de Usuário
    Usuario usuario = (Usuario)facade.getEntity(Usuario.CLASS_NAME);
    // nossa instância de Papel Usuário
    PapelUsuario papelUsuario = (PapelUsuario)facade.getEntity(PapelUsuario.CLASS_NAME);

    // UsuarioInfo para editar
    UsuarioInfo usuarioInfo = null;
    // se estamos incluindo...
    if (Controller.isInserting(request)) {
      // info em branco
      usuarioInfo = new UsuarioInfo();
    }
    // se estamos salvando...
    else if (Controller.isSaving(request)) {
      // info preenchido na página
      usuarioInfo = (UsuarioInfo)usuario.entityInfoFromRequest(request);
      // se estamos salvando e é um novo...inclui
      if (Controller.isSavingNew(request)) {
        usuario.insert(usuarioInfo);
      }
      // se estamos salvando e é um existente...
      else {
        // obtém a senha...
        String password = request.getParameter(PASSWORD);
        // se alterou a senha...usa a nova
        if (!password.equals(DUMMY))
          usuarioInfo.setSenha(password);
        // se não alterou...usaremos a senha antiga
        else {
          UsuarioInfo oldUsuarioInfo = usuario.selectByUsuarioId(usuarioInfo.getUsuarioId());
          usuarioInfo.setSenha(oldUsuarioInfo.getSenha());
        } // if
        // atualiza
        usuario.update(usuarioInfo);
      } // if
      // compõe a resposta
      Form.composeAjaxResponse(response, Form.COMPOSE_TYPE_JAVA_SCRIPT);
      // atualiza a consulta
      %><%=EntityGrid.forwardBrowse(facade, usuario, Usuario.ACTION, Controller.isSavingNew(request) ? EntityGrid.OPERATION_INSERT : EntityGrid.OPERATION_UPDATE, usuarioInfo)%><%
      // dispara
      return;
    }
    // se estamos editando...
    else if (Controller.isEditing(request)) {
      // info do registro para editar
      usuarioInfo =
        usuario.selectByUsuarioId(
          NumberTools.parseInt(request.getParameter(Usuario.FIELD_USUARIO_ID.getFieldAlias()))
        );
    }
    // se o comando é desconhecido...
    else {
      // compõe a resposta
      throw new Exception("Comando desconhecido.");
    } // if

    // nosso FrameBar
    FrameBar frameBar = new FrameBar(facade, "frameBarUsuario");
    // nosso PageControl
    PageControl pageControl = new PageControl(facade, "pageControlUsuario", true);
    // nosso Form de dados
    Form formData = new Form(request, "formDataUsuario", Usuario.ACTION_CADASTRO, Usuario.COMMAND_SAVE, "", true, true);
%>

<html>
  <head>
    <title><%=Usuario.ACTION.getCaption()%></title>
    <link href="<%=facade.getStyle().userInterface()%>" rel="stylesheet" type="text/css">
    <script src="include/scripts.js" type="text/javascript"></script>
  </head>
  <body style="margin:0px;" onselectstart="return true;" oncontextmenu="return false;">

    <!-- inicia o FrameBar -->
    <%=frameBar.begin()%>

      <!-- área de frames -->
      <%=frameBar.beginFrameArea()%>

        <!-- Frame de identificação do objeto -->
        <%=frameBar.actionFrame(Usuario.ACTION)%>

        <!-- Frame de comandos -->
        <%=frameBar.beginFrame("Comandos", false, false)%>
          <table style="width:100%;">
            <tr>
              <td><%=CommandControl.formHyperlink(facade, formData, ImageList.COMMAND_SAVE, "Deseja mesmo salvar o Usuário?", true, false)%></td>
            </tr>
            <tr>
              <td><%=CommandControl.entityFormHyperlink(facade, usuario, Usuario.ACTION_CADASTRO, Usuario.COMMAND_INSERT, ImageList.COMMAND_INSERT, false)%></td>
            </tr>
            <tr>
              <td><%=CommandControl.entityBrowseHyperlink(facade, usuario, Usuario.ACTION, ImageList.COMMAND_BACK, "Voltar", "Voltar para consulta", false)%></td>
            </tr>
          </table>
        <%=frameBar.endFrame()%>
      <%=frameBar.endFrameArea()%>

      <!-- Form de dados -->
      <%=formData.begin()%>
        <!-- dados ocultos -->
        <%=EntityFormEdit.script(facade, Usuario.FIELD_USUARIO_ID, usuarioInfo, request, -1, "", "")%>
        <%=EntityFormEdit.script(facade, Usuario.FIELD_HASH, usuarioInfo, request, -1, "", "")%>
        <%=EntityFormEdit.script(facade, Usuario.FIELD_DATA_ULTIMO_LOGON, usuarioInfo, request, -1, "", "")%>
        <!-- área de dados -->
        <%=frameBar.beginClientArea()%>
          <!-- dados de Usuário -->
          <table style="width:100%; height:100%;">
            <tr style="height:40px;">
              <td>

                <!-- campos Nome e Email -->
                <table style="width:100%;">
                  <tr>
                    <td><%=EntityFieldLabel.script(facade, Usuario.FIELD_NOME, request)%></td>
                    <td><%=EntityFieldLabel.script(facade, Usuario.FIELD_EMAIL, request)%></td>
                  </tr>
                  <tr>
                    <td style="width:50%"><%=EntityFormEdit.script(facade, Usuario.FIELD_NOME, usuarioInfo, request, 0, "", "")%></td>
                    <td style="width:50%"><%=EntityFormEdit.script(facade, Usuario.FIELD_EMAIL, usuarioInfo, request, 0, "", "")%></td>
                  </tr>
                </table>

              </td>
            </tr>
            <tr style="height:20px;">
              <td>

                <!-- campo Descrição -->
                <table style="width:100%;">
                  <tr>
                    <td style="width: auto"><%=EntityFieldLabel.script(facade, Usuario.FIELD_DESCRICAO, request)%></td>
                    <td style="width: 90px"><%=EntityFieldLabel.script(facade, Usuario.FIELD_DATA_EXPIRACAO, request)%></td>
                    <td style="width: 150px"><%=EntityFieldLabel.script(facade, Usuario.FIELD_TIPO_EXPIRACAO, request)%></td>
                  </tr>
                  <tr>
                    <td><%=EntityFormEdit.script(facade, Usuario.FIELD_DESCRICAO, usuarioInfo, request, 0, "", "")%></td>
                    <td><%=EntityFormEdit.script(facade, Usuario.FIELD_DATA_EXPIRACAO, usuarioInfo, request, 0, "", "")%></td>
                    <td><%=EntityFormSelect.script(facade, Usuario.FIELD_TIPO_EXPIRACAO, usuarioInfo, request, 0, "", "", false)%></td>
                  </tr>
                </table>

              </td>
            </tr>
            <tr style="height:60px;">
              <td>

                <table style="width:100%;">
                  <tr>
                    <td valign="top" style="width:50%;">
                      <table>
                        <!-- campo Senha -->
                        <tr>
                          <td><%=EntityFieldLabel.script(facade, Usuario.FIELD_SENHA, request)%></td>
                          <%// se estamos inserindo...
                            if (Controller.isInserting(request)) {%>
                          <td><%=EntityFormEdit.script(facade, Usuario.FIELD_SENHA, usuarioInfo, request, 140, "", "", false, true)%></td>
                          <%}
                            // se estamos editando...
                            else {%>
                          <td><%=FormEdit.script(facade, PASSWORD, DUMMY, 140, Usuario.FIELD_SENHA.getFieldSize(), FormEdit.ALIGN_LEFT, "", "", "", "", "", "", "", "", false, true)%></td>
                          <%} // if%>
                        </tr>
                        <!-- campo Alteração Senha -->
                        <tr>
                          <td><%=EntityFieldLabel.script(facade, Usuario.FIELD_ALTERACAO_SENHA, request)%></td>
                          <td><%=EntityFormEdit.script(facade, Usuario.FIELD_ALTERACAO_SENHA, usuarioInfo, request, 140, "", "")%></td>
                        </tr>
                        <!-- campo Nível -->
                        <tr>
                          <td><%=EntityFieldLabel.script(facade, Usuario.FIELD_NIVEL, request)%></td>
                          <td><%=EntityFormEdit.script(facade, Usuario.FIELD_NIVEL, usuarioInfo, request, 140, "", "")%></td>
                        </tr>
                      </table>
                    </td>
                    <td style="width:50%;">
                      <table>
                        <!-- campo Não Pode Alterar Senha -->
                        <tr>
                          <td><%=EntityFieldLabel.script(facade, Usuario.FIELD_NAO_PODE_ALTERAR_SENHA, request)%></td>
                          <td><%=EntityFormSelect.script(facade, Usuario.FIELD_NAO_PODE_ALTERAR_SENHA, usuarioInfo, request, 70, "", "", false)%></td>
                        </tr>
                        <!-- campo Alterar Senha -->
                        <tr>
                          <td><%=EntityFieldLabel.script(facade, Usuario.FIELD_ALTERAR_SENHA, request)%></td>
                          <td><%=EntityFormSelect.script(facade, Usuario.FIELD_ALTERAR_SENHA, usuarioInfo, request, 70, "", "", false)%></td>
                        </tr>
                        <!-- campo Inativo -->
                        <tr>
                          <td><%=EntityFieldLabel.script(facade, Usuario.FIELD_INATIVO, request)%></td>
                          <td><%=EntityFormSelect.script(facade, Usuario.FIELD_INATIVO, usuarioInfo, request, 70, "", "", false)%></td>
                        </tr>
                      </table>
                    </td>
                  </tr>
                </table>

              </td>
            </tr>
            <tr style="height:auto;">
              <td>

                <%=pageControl.begin()%>

                  <!-- Papéis -->
                  <%if (Controller.isInserting(request)) {%>
                    <%=pageControl.beginTabSheet("Papéis")%>
                      <span>Não disponível em tempo de inclusão.</span>
                    <%=pageControl.endTabSheet()%>
                  <%}
                    else {%>
                    <%=pageControl.iFrameTabSheet("Papéis", PapelUsuario.ACTION.url(papelUsuario.USER_PARAM_MASTER.getName() + "=" + PapelUsuario.MASTER_IS_USUARIO + "&" + papelUsuario.USER_PARAM_USUARIO_ID.getName() + "=" + usuarioInfo.getUsuarioId()))%>
                  <%} // if%>

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
  catch (Exception e) {
    Controller.forwardException(e, pageContext);
  } // try-catch
%>
