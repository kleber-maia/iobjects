
<%--
The MIT License (MIT)

Copyright (c) 2008 Kleber Maia de Andrade

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
--%>
<%@include file="../include/beans.jsp"%>

<%!
  String DEFAULT_CONNECTION = "defaultConnection";
  String DEMO               = "demo";
  String EMAIL              = "email";
  String LOGOFF             = "logoff";
  String LOGIN              = "login";
  String MENSAGEM           = "mensagem";
  String MENSAGEM_DEMO      = "Informe seus dados pessoais<br />para receber um login e senha.";
  String MENSAGEM_LOGIN     = "Informe seus dados pessoais<br />para se autenticar no sistema.";
  String NOME               = "nome";
  String SENHA              = "password";
%>

<%
  try {
    // nosso Ajax
    Ajax ajaxDemo = new Ajax(facade, "ajaxDemo", Controller.ACTION_LOGIN.getName(), DEMO);
    
    // nossas variáveis
    String nomeUsuario       = null;
    String senha             = null;
    String defaultConnection = null;
    boolean savedPassword    = false;
    boolean logoff           = request.getParameter(LOGOFF) != null;

    // devemos efetuar logoff?
    if (logoff) {
      // efetua logoff
      facade.logoff();
      // apaga a senha
      HttpTools.setCookieValue(response, SENHA, "", false);
    } // if
    // se deseja acessar a demonstração...
    else if (Controller.getCommand(request).equals(DEMO)) {
      // obtém o serviço de demonstração
      String mensagem = facade.demoService().requestDemo(request.getParameter(NOME), request.getParameter(EMAIL));
      // retorna a mensagem
      Ajax.setResponseHeader(response, MENSAGEM, mensagem);
      // dispara
      return;
    } // if

    // pega os parâmetros esperados
    defaultConnection = request.getParameter(DEFAULT_CONNECTION);
    if (defaultConnection == null)
      defaultConnection = HttpTools.getCookieValue(request, DEFAULT_CONNECTION, "");
    nomeUsuario = request.getParameter(LOGIN);
    if (nomeUsuario == null)
      nomeUsuario = HttpTools.getCookieValue(request, LOGIN, "");
    senha = request.getParameter(SENHA);
    if (logoff)
      senha = "";
    else if (senha == null) {
      senha = HttpTools.getCookieValue(request, SENHA, "");
      savedPassword = !senha.equals("");
    } // if

    // se temos nomeUsuario e senha...
    if (!nomeUsuario.equals("") && !senha.equals("")) {
      try {
        // tenta efetuar o logon
        facade.logon(facade.applicationDefaultConnection().getOnLogonTime() ? defaultConnection : "", nomeUsuario, senha);
        // salva em cookie
        if (!savedPassword) {
          HttpTools.setCookieValue(response, DEFAULT_CONNECTION, defaultConnection, false);
          HttpTools.setCookieValue(response, LOGIN, nomeUsuario, false);
          HttpTools.setCookieValue(response, SENHA, nomeUsuario.hashCode() + "$" + senha.hashCode(), false, 1);
        }
        // deixa o Controller redirecionar para o lugar certo
        HttpTools.forward(Controller.ACTION_HOME_MOBILE.getJsp(), pageContext);
        // dispara
        return;
      }
      catch (Exception e) {
        // se a senha não veio do cookie...mostra a exceção
        if (!savedPassword)
          throw e;
      } // try-catch
    } // if
%>

<html>
  <head>
    <title><%=facade.applicationInformation().getName()%></title>
    <link href="<%=facade.getStyle().userInterface()%>" rel="stylesheet" type="text/css">
    <link href="<%=facade.iconURL()%>" rel="shortcut icon" />
    <script src="include/scripts.js" type="text/javascript"></script>
    <!-- iPod, iPhone e iPad -->
    <meta name="apple-mobile-web-app-capable" content="yes" />
    <meta name="apple-mobile-web-app-status-bar-style" content="black" />
    <meta name="viewport" content="width=device-width; initial-scale=1.0; maximum-scale=1.0; user-scalable=0;" />
  		<meta name="apple-touch-fullscreen" content="yes" />
    <link rel="apple-touch-icon" href="<%=facade.iconURL()%>" />
  </head>

  <script type="text/javascript">
    // se fomos carregados em um Frame...
    if (window.frameElement != null) {
      // avisa a janela pai que vamos susbtituir seu conteúdo
      // isto para o caso de estarmos sendo carregados sobre home.jsp
      window.parent.replacedByLogin = true;
      // recarrega-nos em seu lugar
      window.parent.location.href = window.location.href;
    } // if
  </script>

  <body class="Login" style="margin:8px;" onselectstart="return true;" oncontextmenu="return false;">

    <center>
      <!-- logo -->
      <%// se temos uma logo para a aplicação...
        if (!facade.applicationLogo().getFileName().equals("")) {
          // tamanho da logo
          int logoWidth = facade.applicationLogo().getWidth();
          int logoHeight = facade.applicationLogo().getHeight();
          // razão entre a altura de exibição (48 pixels) com a altura da imagem
          int logoHeightProportion = 48 * 100 / logoHeight;
          int newLogoWidth = logoWidth * logoHeightProportion / 100;
      %>
        <img src="<%=facade.logoURL()%>" height="48" width="<%=newLogoWidth%>" align="absmiddle" alt=""><%if (!facade.applicationInformation().getTitle().equals("")) {%><span class="BackgroundText" style="font-size:30pt;">&nbsp;<%=facade.applicationInformation().getTitle()%></span><%} // if%>
      <%}
        // se não temos uma logo...usa a padrão
        else {%>
        <img src="images/iobjects.png" width="48" height="48" align="absmiddle" alt=""><span class="BackgroundText" style="font-size:30pt;">&nbsp;<%=facade.applicationInformation().getTitle()%></span>
      <%} // if%>

      <p>&nbsp;</p>

      <!-- form -->
      <form name="formLogin" action="controller" method="POST" onsubmit="return validateLogin();">
        <input type="hidden" name="<%=Controller.ACTION%>" value="<%=Controller.ACTION_LOGIN.getName()%>">
        <!--controles de dados-->
        <table>
          <%if (facade.applicationDefaultConnection().getOnLogonTime()) {%>
          <tr id="trMasterRelation">
            <td><%=facade.applicationDefaultConnection().getCaption()%></td>
          </tr>
          <tr id="trMasterRelationInput">
            <td><input type="text" name="<%=DEFAULT_CONNECTION%>" value="<%=defaultConnection != null ? defaultConnection : ""%>" style="width:250px; height:26px;"></td>
          </tr>
          <%} // if%>
          <tr id="trLogin">
            <td>Nome ou e-mail</td>
          </tr>
          <tr id="trLoginInput">
            <td><input type="text" name="<%=LOGIN%>" value="<%=nomeUsuario != null ? nomeUsuario : ""%>" style="width:250px; height:26px;"></td>
          </tr>
          <tr id="trPassword">
            <td>Senha</td>
          </tr>
          <tr id="trPasswordInput">
            <td><input type="password" name="<%=SENHA%>" style="width:250px; height:26px;"></td>
          </tr>
          <tr id="trNome" style="display:none;">
            <td>Nome</td>
          </tr>
          <tr id="trNomeInput" style="display:none;">
            <td><input type="text" id="<%=NOME%>" style="width:250px; height:26px;"></td>
          </tr>
          <tr id="trEmail" style="display:none;">
            <td>E-mail</td>
          </tr>
          <tr id="trEmailInput" style="display:none;">
            <td><input type="text" id="<%=EMAIL%>" style="width:250px; height:26px;"></td>
          </tr>
          <tr>
            <td align="center" style="padding-top:8px;">
              <!-- botões -->
              <input type="submit" id="buttonEntrar" value="Entrar" class="Button" onmouseover="className='ButtonOver';" onmouseout="className='Button';" style="width:117px; height:26px;" />
              <input type="button" id="buttonEnviar" value="Enviar" class="Button" onmouseover="className='ButtonOver';" onmouseout="className='Button';" onclick="demo();" style="width:117px; height:26px; display:none;" />
              <input type="reset"  id="buttonLimpar" value="Limpar" class="Button" onmouseover="className='ButtonOver';" onmouseout="className='Button';" style="width:117px; height:26px; margin-left:13px;" />
            </td>
          </tr>
          <tr>
            <td style="padding-top:8px;">
            <%// demonstração
              if (facade.applicationDemo().showOnLogonTime()) {%>
              <input type="button" id="buttonDemo" value="<%=facade.applicationDemo().getCaption()%>" class="ButtonOver" onmouseover="className='ButtonOver';" onmouseout="className='Button';" onclick="showDemo();" style="width:250px;" />
            <%} // if%>
            </td>
          </tr>
          <tr>
            <td align="center" style="padding-top:8px;"><%=facade.applicationInformation().getURL()%></td>
          </tr>
          <tr>
            <td align="center" id="<%=MENSAGEM%>" style="padding-top:8px;"><%=MENSAGEM_LOGIN%></td>
          </tr>
        </table>
      </form>

    </center>

    <script type="text/javascript">
      var formSubmited = false;

      // põe o foco no controle
      //formLogin.<%=(facade.applicationDefaultConnection().getOnLogonTime() ? DEFAULT_CONNECTION : LOGIN)%>.focus();

    <%// demonstração
      if (facade.applicationDemo().showOnLogonTime()) {%>
      function demo() {
        // nossos valores
        var nome  = document.getElementById("<%=NOME%>").value;
        var email = document.getElementById("<%=EMAIL%>").value;
        // se não informou tudo...dispara
        if (trim(nome) == "" || trim(email) == "") {
          alert("Por favor, informe todos os dados.");
          return;
        } // if
        // se o e-mail parace inválido
        if (email.indexOf("@") < 0 || email.indexOf(".") < 0) {
          alert("O e-mail informado é inválido.");
          return;
        } // if
        // envia a requisição
        var params = "<%=NOME%>=" + nome + "&<%=EMAIL%>=" + email;
        <%=ajaxDemo.request(new String[]{"params"})%>
        // se ocorreu um erro...mostra
        if (<%=ajaxDemo.isResponseStatusError()%>) {
          alert(<%=ajaxDemo.responseErrorMessage()%>);
          return;
        }
        // se ocorreu tudo bem...
        else {
          // mostra a mensagem
          alert(<%=ajaxDemo.getResponseHeader(MENSAGEM)%>);
          // mostra o login
          showLogin();
        } // if
      }

      function showDemo() {
        // oculta os controles de login
        if (document.getElementById("trMasterRelation") != null) {
          document.getElementById("trMasterRelation").style.display      = "none";
          document.getElementById("trMasterRelationInput").style.display = "none";
        } // if
        document.getElementById("trLogin").style.display               = "none";
        document.getElementById("trLoginInput").style.display          = "none";
        document.getElementById("trPassword").style.display            = "none";
        document.getElementById("trPasswordInput").style.display       = "none";
        document.getElementById("buttonEntrar").style.display          = "none";
        document.getElementById("buttonDemo").style.display            = "none";
        // exibe os controles de demonstração
        document.getElementById("trNome").style.display                = "block";
        document.getElementById("trNomeInput").style.display           = "block";
        document.getElementById("trEmail").style.display               = "block";
        document.getElementById("trEmailInput").style.display          = "block";
        document.getElementById("buttonEnviar").style.display          = "inline";
        // muda a mensagem
        document.getElementById("<%=MENSAGEM%>").innerHTML             = "<%=MENSAGEM_DEMO%>";
        // foco
        //document.getElementById("<%=NOME%>").focus();
      }

      function showLogin() {
        // oculta os controles de login
        if (document.getElementById("trMasterRelation") != null) {
          document.getElementById("trMasterRelation").style.display      = "block";
          document.getElementById("trMasterRelationInput").style.display = "block";
        } // if
        document.getElementById("trLogin").style.display               = "block";
        document.getElementById("trLoginInput").style.display          = "block";
        document.getElementById("trPassword").style.display            = "block";
        document.getElementById("trPasswordInput").style.display       = "block";
        document.getElementById("buttonEntrar").style.display          = "inline";
        document.getElementById("buttonDemo").style.display            = "inline";
        // exibe os controles de demonstração
        document.getElementById("trNome").style.display                = "none";
        document.getElementById("trNomeInput").style.display           = "none";
        document.getElementById("trEmail").style.display               = "none";
        document.getElementById("trEmailInput").style.display          = "none";
        document.getElementById("buttonEnviar").style.display          = "none";
        // muda a mensagem
        document.getElementById("<%=MENSAGEM%>").innerHTML             = "<%=MENSAGEM_LOGIN%>";
        // foco
        //document.getElementById("<%=NOME%>").focus();
      }
    <%} // if%>

      function validateLogin() {
        // se o form já foi submetido...pede que aguarda
        if (formSubmited) {
          alert("Aguarde o término da operação. Clique em OK para continuar.");
          return false;
        } // if
        // se não informou o nome e a senha...dispara
        if ((formLogin.<%=LOGIN%>.value == "") || (formLogin.<%=SENHA%>.value == "")) {
          alert("Obrigatório informar todos os dados.");
          return false;
        } // if
        // marca o form como submetido
        //formSubmited = true; // retirado pelo iPhone/iPad
        // tudo ok
        return true;
      }
    </script>

  </body>
</html>
<%}
  catch (Exception e) {
    Controller.forwardException(e, pageContext);
  } // try-catch
%>
