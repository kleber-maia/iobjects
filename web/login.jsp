
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
<%@page import="java.net.URLDecoder"%>
<%@page import="java.net.URLEncoder"%>
<%@include file="include/beans.jsp"%>

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
  String SAVED_PASSWORD     = "savedPassword";
%>

<%
  try {
    // nosso Ajax
    Ajax ajaxDemo = new Ajax(facade, "ajaxDemo", Controller.ACTION_LOGIN.getName(), DEMO);
    
    // nossas variaveis
    boolean savedPassword    = request.getParameter(SAVED_PASSWORD) != null && request.getParameter(SAVED_PASSWORD).equals("true");
    boolean logoff           = request.getParameter(LOGOFF) != null;
    
    // se devemos efetuar logoff...
    if (logoff)
      facade.logoff();
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
    String defaultConnection = request.getParameter(DEFAULT_CONNECTION);
    if (facade.applicationDefaultConnection().getOnLogonTime()) {    
      if (defaultConnection == null)
        defaultConnection = HttpTools.getCookieValue(request, DEFAULT_CONNECTION, "");
    } // if 
    String nomeUsuario = request.getParameter(LOGIN);
    if (nomeUsuario == null)
      nomeUsuario = URLDecoder.decode(HttpTools.getCookieValue(request, LOGIN, ""));
    String senha = request.getParameter(SENHA);
    // se temos nomeUsuario e senha...
    if (!nomeUsuario.equals("") && senha!=null && !senha.equals("")) {
      // tenta efetuar o logon
      facade.logon(facade.applicationDefaultConnection().getOnLogonTime() ? defaultConnection : "", nomeUsuario, senha);      
      // salva em cookie
      if (facade.applicationDefaultConnection().getOnLogonTime())
        HttpTools.setCookieValue(response, DEFAULT_CONNECTION, StringTools.removeAccents(defaultConnection), false);
      HttpTools.setCookieValue(response, LOGIN, URLEncoder.encode(nomeUsuario), false);
      if (savedPassword) {
        HttpTools.setCookieValue(response, StringTools.removeAccents((defaultConnection + '_' + nomeUsuario).replaceAll(" ", "")), senha.contains(nomeUsuario.hashCode() + "$") ? senha : nomeUsuario.hashCode() + "$" + senha.hashCode(), false, 5);
      }      
      else {
        HttpTools.setCookieValue(response, StringTools.removeAccents((defaultConnection + '_' + nomeUsuario).replaceAll(" ", "")), "", false, 0);
      }
      // deixa o Controller redirecionar para o lugar certo
      HttpTools.forward(Controller.ACTION_HOME.url(), pageContext);
      return;
    } // if
%>

<html>
  <head>
    <title><%=facade.applicationInformation().getName()%> - Efetuar Logon</title>
    <link href="<%=facade.getStyle().userInterface()%>" rel="stylesheet" type="text/css">
    <link href="<%=facade.iconURL()%>" rel="shortcut icon" />
    <script src="include/scripts.js" type="text/javascript"></script>
    <!-- iPod, iPhone e iPad -->
    <meta name="apple-mobile-web-app-capable" content="yes" />
    <meta name="apple-mobile-web-app-status-bar-style" content="black" />
    <meta name="viewport" content="initial-scale=1 maximum-scale=1 user-scalable=0" />
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
    }
    // se não fomos carregados em um Popup...
    else if (window.opener == null) {
      // altura e largura
      var inWidth  = screen.availWidth - 10;
      var inHeight = screen.availHeight - 32;
      // posição
      var inLeft = 0;
      var inTop  = 0;
      // carrega-nos em um Popup principal
      var wnd = window.open(window.location.href, '<%=facade.applicationInformation().getName().replace(' ', '_')%>', 'top=' + inTop + ',left=' + inLeft + ',width=' + inWidth + ',height=' + inHeight + ',menubar=no,location=no,scrollbars=no,status=no,resizable=no');
      wnd.focus();
      // mostra o aviso de popup em nosso lugar
      window.location.href = "popup.jsp";
      // dispara
      document.close();
    }
    // se fomos carregados em uma janela Popup, mas não é o Popup principal...
    else if ((window.opener != null) && (window.opener.name != "")) {
      // recarrega-nos na janela que abriu esse Popup
      window.opener.location.href = window.location.href;
      window.close();
    } // if
  </script>

  <body class="Login" onload="javascript: searchPassword()" onselectstart="return true;" oncontextmenu="return false;">

    <!-- alinha tudo no centro da tela -->
    <table style="width:100%; height:100%;">
      <tr>
        <td align="center" valign="middle">

          <!-- marca e form de login -->
          <table style="width:100%; height:220px;">
            <tr>
              <td style="width:50%;">&nbsp;</td>
              <!-- linha vertical -->
              <td rowspan="3" style="width:10px;" align="center">
                <div class="VerticalLine" style="width:1px; height:100%;"></div>
              </td>
              <td style="width:auto;">&nbsp;</td>
            </tr>
            <tr>
              <!-- logo -->
              <td align="right" valign="top" style="height:48px;">
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
                  <img src="images/iobjects.png" width="48" height="48" align="absmiddle" alt=""><span style="font-size:30pt;">&nbsp;<%=facade.applicationInformation().getTitle()%></span>
                <%} // if%>
                <p style="margin-top:42px;"><%=facade.applicationInformation().getURL()%></p>
                <p style="margin-top:42px;" id="<%=MENSAGEM%>"><%=MENSAGEM_LOGIN%></p>
              </td>
              <!-- form -->
              <td rowspan="2" align="left" valign="middle">
                <form name="formLogin" action="controller" method="POST" onsubmit="return validateLogin();">
                  <input type="hidden" name="<%=Controller.ACTION%>" value="<%=Controller.ACTION_LOGIN.getName()%>">
                  <!--controles de dados-->
                  <table style="width:100%;">
                    <%if (facade.applicationDefaultConnection().getOnLogonTime()) {%>
                    <tr id="trMasterRelation">
                      <td><%=facade.applicationDefaultConnection().getCaption()%></td>
                    </tr>
                    <tr id="trMasterRelationInput">
                      <td><input type="text" id="<%=DEFAULT_CONNECTION%>" name="<%=DEFAULT_CONNECTION%>" value="<%=defaultConnection%>" onchange="javascript: searchPassword()" style="width:250px;"></td>
                    </tr>
                    <%} // if%>
                    <tr id="trLogin">
                      <td>Nome ou e-mail</td>
                    </tr>
                    <tr id="trLoginInput">
                      <td><input type="text" id="<%=LOGIN%>" name="<%=LOGIN%>" value="<%=nomeUsuario%>" onchange="javascript: searchPassword()" style="width:250px;"></td>
                    </tr>
                    <tr id="trPassword">
                      <td>Senha</td>
                    </tr>
                    <tr id="trPasswordInput">
                      <td><input type="password" id="<%=SENHA%>" name="<%=SENHA%>" style="width:250px;"></td>
                    </tr>
                    <tr id="trSavePasswordCheck">
                      <td>
                        <table>
                          <tr>
                            <td><input type="checkbox" id="<%=SAVED_PASSWORD%>" name="<%=SAVED_PASSWORD%>" value="true"/></td>
                            <td onclick="javascript:var check = document.getElementById('<%=SAVED_PASSWORD%>'); if (check.checked) check.checked = false; else check.checked = true;">Salvar Senha</td>
                          </tr>
                        </table>
                      </td>
                    </tr>
                    <tr id="trNome" style="display:none;">
                      <td>Nome</td>
                    </tr>
                    <tr id="trNomeInput" style="display:none;">
                      <td><input type="text" id="<%=NOME%>" style="width:250px;"></td>
                    </tr>
                    <tr id="trEmail" style="display:none;">
                      <td>E-mail</td>
                    </tr>
                    <tr id="trEmailInput" style="display:none;">
                      <td><input type="text" id="<%=EMAIL%>" style="width:250px;"></td>
                    </tr>
                    <tr>
                      <td style="padding-top:8px;">
                        <!-- botões -->
                        <input type="submit" id="buttonEntrar" value="Entrar" class="Button" onmouseover="className='ButtonOver';" onmouseout="className='Button';" style="width:117px;" />
                        <input type="button" id="buttonEnviar" value="Enviar" class="Button" onmouseover="className='ButtonOver';" onmouseout="className='Button';" onclick="demo();" style="width:117px; display:none;" />
                        <input type="reset"  id="buttonLimpar" value="Limpar" class="Button" onmouseover="className='ButtonOver';" onmouseout="className='Button';" style="width:117px; margin-left:13px;" />
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
                  </table>
                </form>
              </td>
            </tr>
            <tr>
              <td>&nbsp;</td>
            </tr>
          </table>

        </td>
      </tr>
    </table>

    <script type="text/javascript">
      
      // controle de submissão do form
      var formSubmited = false;
      $(window).on("pageshow", function(event){
        formSubmited = false;
      });
      
      function checkUncheck() {
        var check = document.getElementById("<%=SAVED_PASSWORD%>");
        if (check.checked) 
          check.checked = false;
        else 
          check.checked = true;
      }
      
      function searchPassword() {
        var connection = document.getElementById("<%=DEFAULT_CONNECTION%>");
        var login      = document.getElementById("<%=LOGIN%>");
        var senha      = readCookieValue(removeAccents((<%=facade.applicationDefaultConnection().getOnLogonTime() ? "connection.value + '_'" : "''"%> + login.value).replace(" ", ""), false));
        if (senha != null || senha == "") {
          document.getElementById("<%=SENHA%>").value = senha;
          document.getElementById("<%=SAVED_PASSWORD%>").checked = true;
        }
        else {
          document.getElementById("<%=SENHA%>").value = "";
          document.getElementById("<%=SAVED_PASSWORD%>").checked = false;
        }
      }

      // põe o foco no controle
      formLogin.<%=(facade.applicationDefaultConnection().getOnLogonTime() ? DEFAULT_CONNECTION : LOGIN)%>.focus();

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
        document.getElementById("trSavePasswordCheck").style.display   = "none";
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
        document.getElementById("<%=NOME%>").focus();
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
        document.getElementById("trSavePasswordCheck").style.display   = "block";
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
        document.getElementById("<%=NOME%>").focus();
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
        formSubmited = true;
        // tudo ok
        return true;
      }
    </script>

  </body>
</html>
<%}
  catch (Exception e) {
    e.printStackTrace();
    Controller.forwardException(e, pageContext);
  } // try-catch
%>
