<%@include file="include/beans.jsp"%>

<%!
  String SENHA                  = "senha";
  String NOVA_SENHA             = "novaSenha";
  String CONFIRMACAO_NOVA_SENHA = "confirmacaoNovaSenha";
%>

<%
  try {
    // pega os parâmetros esperados
    String senha = request.getParameter(SENHA);
    if (senha == null)
      senha = "";
    String novaSenha = request.getParameter(NOVA_SENHA);
    if (novaSenha == null)
      novaSenha = "";

    // nossa senha foi alterada?
    boolean passwordChanged = false;
    // se recebemos os parâmetros...tenta trocar a senha
    if (!senha.equals("") && !novaSenha.equals("")) {
      // altera a senha
      facade.securityService().changePassword(facade.getLoggedUser().getId(),
                                              senha,
                                              novaSenha);
      // a senha foi alterada
      passwordChanged = true;
      // não precisa mais alterar a senha
      facade.getLoggedUser().setChangePassword(false);
    }; // if
%>

<html>
  <head>
    <title><%=facade.applicationInformation().getName()%> - Alterar Senha</title>
    <link href="<%=facade.getStyle().userInterface()%>" rel="stylesheet" type="text/css">
    <link href="<%=facade.iconURL()%>" rel="shortcut icon" />
    <script src="include/scripts.js" type="text/javascript"></script>
  </head>

  <body style="margin:0px;" onselectstart="return false;" oncontextmenu="return false;">

  <script type="text/javascript">

    function formSubmit() {
      // controles
      senha     = formChangePassword.<%=SENHA%>;
      novaSenha = formChangePassword.<%=NOVA_SENHA%>;
      confirmacaoNovaSenha = formChangePassword.<%=CONFIRMACAO_NOVA_SENHA%>;
      // se não informou a senha atual...avisa
      if (senha.value == "") {
        alert("Informe a senha atual.");
        senha.focus();
        return;
      }
      // se não informou a nova senha...avisa
      if (novaSenha.value == "") {
        alert("Informe a nova senha.");
        novaSenha.focus();
        return;
      }
      // se não confirmou a nova senha...avisa
      if (confirmacaoNovaSenha.value != novaSenha.value) {
        alert("Confirme a nova senha.");
        confirmacaoNovaSenha.value = "";
        confirmacaoNovaSenha.focus();
        return;
      }
      // submete o formulário
      formChangePassword.submit();
    }

  </script>

    <%// se alteramos a senha
      if (passwordChanged) {
    %>
      <script type="text/javascript">
        alert("Senha alterada com sucesso.");
        window.close();
      </script>
    <%}
      // se não alteramos a senha
      else {%>
      <!-- formulário de alteração de senha -->
      <form id="formChangePassword" action="controller" method="POST">
        <input type="hidden" name="<%=Controller.ACTION%>" value="<%=Controller.ACTION_CHANGE_PASSWORD.getName()%>">
        <!-- logomarca -->
        <table class="FrameBar" cellpadding="8" cellspacing="0" style="width:100%; height:70px;">
          <tr style="height:70px;">
            <td valign="middle">
              <%// se temos uma logo para a aplicação...
                if (!facade.applicationLogo().getFileName().equals("")) {
                  // tamanho da logo
                  int logoWidth = facade.applicationLogo().getWidth();
                  int logoHeight = facade.applicationLogo().getHeight();
                  // razão entre a altura de exibição, 48, com a altura da imagem
                  int logoHeightProportion = 48 * 100 / logoHeight;
                  int newLogoWidth = logoWidth * logoHeightProportion / 100;
              %>
                <img src="<%=facade.logoURL()%>" height="48" width="<%=newLogoWidth%>" align="absmiddle" alt=""><span class="BackgroundText" style="font-size:30pt;">&nbsp;<%=facade.applicationInformation().getTitle()%></span>
              <%}
                // se não temos uma logo...usa a padrão
                else {%>
                <img src="images/iobjects.png" width="48" height="48" align="absmiddle" alt=""><span class="BackgroundText" style="font-size:30pt;">&nbsp;<%=facade.applicationInformation().getTitle()%></span>
              <%} // if%>
            </td>
          </tr>
        </table>
        <!-- divisão da barra de título -->
        <table cellpadding="0" cellspacing="0" style="width:100%; height:16px;">
          <tr>
            <td valign="top">

              <table style="width:100%; border-top:1px solid;">
                <tr><td></td></tr>
              </table>

            </td>
          </tr>
        </table>
        <!-- dados -->
        <table style="width:100%;">
          <tr>
            <td style="padding:7px; padding-top:0px;">
              <table style="width:100%;">
                <tr>
                  <td style="width:35%;">Usuário</td>
                  <td style="width:65%;"><input type="text" value="<%=facade.getLoggedUser().getName()%> (<%=facade.getLoggedUser().getEmail()%>)" style="width:100%;" disabled="disabled"></td>
                </tr>
                <tr>
                  <td>Senha atual</td>
                  <td><input type="password" name="<%=SENHA%>" maxlength="20" style="width:100%;"></td>
                </tr>
                <tr>
                  <td>Nova senha</td>
                  <td><input type="password" name="<%=NOVA_SENHA%>" maxlength="20" style="width:100%;"></td>
                </tr>
                <tr>
                  <td>Confirme a nova senha </td>
                  <td><input type="password" name="<%=CONFIRMACAO_NOVA_SENHA%>" maxlength="20" style="width:100%;"></td>
                </tr>
                <tr>
                  <td>&nbsp;</td>
                  <td>&nbsp;</td>
                </tr>
                <tr>
                  <td></td>
                  <td>
                    <%=Button.script(facade, "buttonOK", "OK", "", "", "", Button.KIND_DEFAULT, "", "formSubmit();", false)%>&nbsp;
                    <%=Button.script(facade, "buttonCancel", "Cancelar", "", "", "", Button.KIND_DEFAULT, "", "window.close();", false)%>
                  </td>
                </tr>
              </table>
              <script language="javascript" type="">formChangePassword.<%=SENHA%>.focus()</script>
            </td>
          </tr>
        </table>
      </form>
    <%}%>

  </body>
</html>

<%}
  catch (Exception e) {
    Controller.forwardException(e, pageContext);
  } // try-catch
%>
