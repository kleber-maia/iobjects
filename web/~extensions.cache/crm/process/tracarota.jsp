
<%@include file="../include/beans.jsp"%>

<%@page import="imanager.entity.*"%>
<%@page import="imanager.misc.*"%>
<%@page import="imanager.process.*"%>
<%@page import="imanager.ui.entity.*"%>

<%
  // início do bloco protegido
  try {
    // nossa instância de Traça Rota
    TracaRota tracaRota = (TracaRota)facade.getProcess(TracaRota.CLASS_NAME);

    // nosso Wizard
    Wizard wizard = new Wizard(facade, request, "wizardTracaRota", TracaRota.ACTION, TracaRota.COMMAND_EXECUTE, tracaRota.processStepList());
    // nosso Ajax
    Ajax ajaxDelete = new Ajax(facade, "ajaxDelete", TracaRota.ACTION.getName(), TracaRota.COMMAND_DELETE_CONTATO.getName());
    // nosso Grid
    Grid gridEndereco = new Grid(facade, "gridEndereco", new String[]{tracaRota.USER_PARAM_CONTATO_ID.getName()}, Grid.SELECT_MULTIPLE, 0, 0);
    gridEndereco.columns().add(new Grid.Column("Nome", 500, Align.ALIGN_LEFT));
    gridEndereco.columns().add(new Grid.Column("Endereço", 60, Align.ALIGN_LEFT));

    // se estamos excluindo contatos...
    if (Controller.getCommand(request).equals(tracaRota.COMMAND_DELETE_CONTATO.getName())) {
      // lista de Contato ID
      String[] contatoIdList = request.getParameterValues(tracaRota.USER_PARAM_CONTATO_ID.getName());
      // prepara a resposta
      Ajax.setResponseTypeText(response);
      // apaga os Contatos
      for (int i=0; i<contatoIdList.length; i++) {
        tracaRota.deleteEndereco(Integer.parseInt(contatoIdList[i]));
        %><%=gridEndereco.deleteRow(gridEndereco.rowIndex(new String[]{contatoIdList[i]}))%><%
      } // for
      // dispara
      return;
    } // if

    // se estamos iniciando...
    if (wizard.isStarting()) {
      // limpa os parâmetros de usuário
      tracaRota.userParamList().clearValues();
      // se não temos dados para reaproveitar...avisa como usar o processo
      if (!Controller.getCommand(request).equals(TracaRota.COMMAND_USE_PRODUCER.getName()))
        throw new Exception("Este processo deve ser chamado através do comando '" + TracaRota.COMMAND_USE_PRODUCER.getCaption() + "' existente em vários cadastros e relatórios do sistema.");
    } // if

    // define os valores dos parâmetros de usuário
    if (!Controller.isGoingPrevious(request) && !Controller.isRestarting(request))
      tracaRota.userParamList().setParamsValues(request);
    // Empresa selecionada
    tracaRota.USER_PARAM_EMPRESA_ID.setValue(selectedEmpresaId + "");

    // nosso resultado
    String result = "";
    // se estamos selecionando Contatos...
    if (wizard.actualStep() == TracaRota.PROCESS_STEP_CONTATOS) {
      // se devemos reutilizar os dados de um Producer...
      if (Controller.getCommand(request).equals(TracaRota.COMMAND_USE_PRODUCER.getName()))
        tracaRota.addEnderecoFromProducer();
      // se está adicionando um Contato...
      else if (Controller.getCommand(request).equals(TracaRota.COMMAND_ADD_CONTATO.getName()))
        tracaRota.addEndereco(tracaRota.USER_PARAM_CONTATO_ID.getIntValue());
      // se está removendo um Contato...
      else if (Controller.getCommand(request).equals(TracaRota.COMMAND_DELETE_CONTATO.getName())) {
        tracaRota.deleteEndereco(tracaRota.USER_PARAM_DELETE_INDEX.getIntValue());
        tracaRota.USER_PARAM_DELETE_INDEX.setValue("");
      }
    }
    // se estamos executando...
    else if (Controller.isExecuting(request)) {
      // executa
      result = tracaRota.execute(selectedEmpresaId);
    } // if
%>

<html>
  <head>
    <title><%=TracaRota.ACTION.getCaption()%></title>
    <link href="<%=facade.getStyle().userInterface()%>" rel="stylesheet" type="text/css">
    <script src="include/scripts.js" type="text/javascript"></script>
  </head>
  <body style="margin:0px;" onselectstart="return true;" oncontextmenu="return false;">

    <!-- inicia o assistente -->
    <%=wizard.begin()%>

      <!-- Contatos -->
      <%if (wizard.actualStep() == TracaRota.PROCESS_STEP_CONTATOS) {
          // lista de endereços
          Vector enderecos = null;
          if (tracaRota.USER_PARAM_ENDERECO_LIST.getObject() == null)
            enderecos = new Vector();
          else
            enderecos = (Vector)tracaRota.USER_PARAM_ENDERECO_LIST.getObject();
        %>
        <table style="width:100%; height:100%;">
          <!-- Grid -->
          <tr style="height:auto;">
            <td>
              <%=gridEndereco.begin()%>
                <%for (int i=0; i<enderecos.size(); i++) {
                    TracaRota.EnderecoInfo endereco = (TracaRota.EnderecoInfo)enderecos.elementAt(i);%>
                    <%=gridEndereco.addRow(new String[]{endereco.getNome(), (endereco.getCep().equals("") || endereco.getEndereco().equals("") || endereco.getNumero().equals("") || endereco.getCidade().equals("") || endereco.getUf().equals("") ? "Não" : "Sim")},
                                           new String[]{endereco.getId() + ""})%>
                <%} %>
              <%=gridEndereco.end("<img src='" + ImageList.COMMAND_DELETE + "' align='absmiddle' />&nbsp;<a href='javascript:deleteContatos();' title='Exclui os Contatos selecionados.'>Excluir</a>")%>
            </td>
          </tr>
        </table>

        <script type="text/javascript">

          function deleteContatos() {
            // nossas variáveis
            var contatoIdList = <%=gridEndereco.selectedKeyValues()%>;
            // se não temos nada selecionado...dispara
            if (<%=gridEndereco.selectedIndex()%>.length == 0) {
              alert("Nenhum Contato selecionado.");
              return;
            } // if
            // lista de Contatos selecionados
            var params = "";
            for (var i=0; i<contatoIdList.length; i++)
              params += (i > 0 ? "&" : "") + "<%=tracaRota.USER_PARAM_CONTATO_ID.getName()%>=" + contatoIdList[i];
            // faz a requisição
            <%=ajaxDelete.request(new String[]{"params"})%>
            // se ocorreu tudo OK...
            if (<%=ajaxDelete.isResponseStatusOK()%>) {
              // executa o script retornado
              eval(<%=ajaxDelete.responseText()%>);
            }
            // se ocorreu erro...
            else {
              alert(<%=ajaxDelete.responseErrorMessage()%>);
              return;
            } // if
          }

        </script>

      <%} // if%>

      <!-- Final -->
      <%if (wizard.actualStep() == TracaRota.PROCESS_STEP_FINAL) {%>
        <p>A rota foi traçada na tentativa de estimar o caminho mais curto entre
           os endereços. Você poderá incluir e excluir endereços e alterar
           livremente a ordem do itinerário diretamente no mapa.</p>
        <p>Se o mapa não foi exibido automaticamente, <a href="<%=result%>" target="_blank">clique aqui</a>.</p>
        <script type="text/javascript">
          window.open("<%=result%>");
        </script>
      <%} // if%>

    <%=wizard.end()%>

  </body>
</html>

<%}
  // término do bloco protegido
  catch (Exception e) {
    Controller.forwardException(e, pageContext);
  } // try-catch
%>
