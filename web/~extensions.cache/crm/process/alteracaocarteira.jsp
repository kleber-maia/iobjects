     
<%@include file="../include/beans.jsp"%>

<%@page import="imanager.entity.*"%>
<%@page import="imanager.process.*"%>
<%@page import="securityservice.ui.entity.*"%>

<%
  // início do bloco protegido
  try {
    // nossa instância de Alteração Carteira
    AlteracaoCarteira alteracaoCarteira = (AlteracaoCarteira)facade.getProcess(AlteracaoCarteira.CLASS_NAME);
    // nossa instância de Contato
    Contato contato = (Contato)facade.getEntity(Contato.CLASS_NAME);

    // nosso Wizard
    Wizard wizard = new Wizard(facade, request, "wizardAlteracaoCarteira", AlteracaoCarteira.ACTION, AlteracaoCarteira.COMMAND_EXECUTE, alteracaoCarteira.processStepList());

    // se estamos iniciando...
    if (wizard.isStarting()) {
      // limpa os parâmetros de usuário
      alteracaoCarteira.userParamList().clearValues();
      // se não temos produtos para ajustar...avisa como usar o processo
      if (!Controller.getCommand(request).equals(AlteracaoCarteira.COMMAND_ALTERAR.getName()))
        throw new Exception("Este processo deve ser chamado através do comando '" + AlteracaoCarteira.COMMAND_ALTERAR.getCaption() + "' existente no cadastro de Contato.");
    } // if

    // define os valores dos parâmetros de usuário
    if (!Controller.isGoingPrevious(request) && !Controller.isRestarting(request))
      alteracaoCarteira.userParamList().setParamsValues(request);

    // se estamos executando...executa
    if (Controller.isExecuting(request))
      alteracaoCarteira.execute();
%>

<html>
  <head>
    <title><%=AlteracaoCarteira.ACTION.getCaption()%></title>
    <link href="<%=facade.getStyle().userInterface()%>" rel="stylesheet" type="text/css">
    <script src="include/scripts.js" type="text/javascript"></script>
  </head>
  <body style="margin:0px;" onselectstart="return true;" oncontextmenu="return false;">

    <!-- inicia o assistente -->
    <%=wizard.begin()%>

      <!-- primeira etapa -->
      <%if (wizard.actualStep() == AlteracaoCarteira.PROCESS_STEP_USUARIO) {%>
        <input name="dummy" style="display:none;" />
        <table>
          <tr>
            <td><%=ParamLabel.script(facade, alteracaoCarteira.USER_PARAM_USUARIO_CARTEIRA)%></td>
          </tr>
          <tr>
            <td><%=UsuarioUI.lookupSearchForParam(facade, alteracaoCarteira.USER_PARAM_USUARIO_CARTEIRA, 250, "", "", false)%></td>
          </tr>
        </table>
      <%} // if%>

      <!-- segunda etapa -->
      <%if (wizard.actualStep() == AlteracaoCarteira.PROCESS_STEP_RESULTADO) {%>
        <p>
          O Usuário foi associado à carteira dos Contatos selecionados com sucesso.
        </p>
      <%
          // obtém o grid de Contato
          EntityGrid entityGrid = EntityGrid.getInstance(facade, contato);
          // script para atualização dos lançamentos
          StringBuffer script = new StringBuffer();
          // lista de contatos selecionados
          ContatoInfo[] contatoInfoList = alteracaoCarteira.getContatoInfoList();
          // loop nos contatos selecionados
          for (int i=0; i<contatoInfoList.length; i++) {
            // contato da vez
            ContatoInfo contatoInfo = contatoInfoList[i];
            // atualiza o contato
            script.append(entityGrid.updateRow(contatoInfo));
          } // for
          // libera recursos
          alteracaoCarteira.clearContatoInfoList();
      %>
        <textarea id="textScript" style="display:none;"><%=script.toString()%></textarea>
        <script type="text/javascript">
          window.opener.eval(document.getElementById("textScript").value);
        </script>
      <%
        } // if
      %>

    <%=wizard.end()%>

  </body>
</html>

<%}
  // término do bloco protegido
  catch (Exception e) {
    Controller.forwardException(e, pageContext);
  } // try-catch
%>
