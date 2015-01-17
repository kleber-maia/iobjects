
<%@page import="imanager.report.RelatorioMalaDiretaEtiqueta"%>
<%@include file="../include/beans.jsp"%>

<%@page import="imanager.entity.*"%>
<%@page import="imanager.misc.*"%>
<%@page import="imanager.process.*"%>
<%@page import="imanager.ui.entity.*"%>

<%
  // início do bloco protegido
  try {
    // nossa instância de Mala Direta
    MalaDireta malaDireta = (MalaDireta)facade.getProcess(MalaDireta.CLASS_NAME);
    // nossa instância de malaDiretaEtiqueta
    RelatorioMalaDiretaEtiqueta malaDiretaEtiqueta = (RelatorioMalaDiretaEtiqueta)facade.getReport(RelatorioMalaDiretaEtiqueta.CLASS_NAME);

    // nosso Wizard
    Wizard wizard = new Wizard(facade, request, "wizardMalaDireta", MalaDireta.ACTION, MalaDireta.COMMAND_EXECUTE, malaDireta.processStepList());
    // nosso Ajax
    Ajax ajaxDelete = new Ajax(facade, "ajaxDelete", MalaDireta.ACTION.getName(), MalaDireta.COMMAND_DELETE_CONTATO.getName());
    // nosso form
    Form form = new Form(request, "formRelatorioMalaDireta", RelatorioMalaDiretaEtiqueta.ACTION, RelatorioMalaDiretaEtiqueta.COMMAND_ETIQUETA, "frameContent", false);
    // nosso pageControl
    PageControl pageControl = new PageControl(facade, "pageControl", true);
    // altera tamanho da aba
    pageControl.setTabWidth(110);
    // nosso Grid
    Grid gridEndereco = new Grid(facade, "gridEndereco", new String[]{malaDireta.USER_PARAM_CONTATO_ID.getName()}, Grid.SELECT_MULTIPLE, 0, 0);
    gridEndereco.columns().add(new Grid.Column("Nome", 250, Align.ALIGN_LEFT));
    gridEndereco.columns().add(new Grid.Column("Endereço", 60, Align.ALIGN_LEFT));
    gridEndereco.columns().add(new Grid.Column("Residencial", 90, Align.ALIGN_LEFT));
    gridEndereco.columns().add(new Grid.Column("Celular", 90, Align.ALIGN_LEFT));
    gridEndereco.columns().add(new Grid.Column("Trabalho", 90, Align.ALIGN_LEFT));
    gridEndereco.columns().add(new Grid.Column("E-mail", 250, Align.ALIGN_LEFT));
    
    // nosso nomeArquivo
    String nomeArquivo = facade.getDefaultConnectionName() + "_" + malaDireta.USER_PARAM_EMPRESA_ID.getValue() + "_" + facade.getLoggedUser().getId() + ".txt";
    
    // nossa maladiretaInfo
    MalaDireta.MalaDiretaInfo malaDiretaInfo = null;

    // se estamos excluindo contatos...
    if (Controller.getCommand(request).equals(malaDireta.COMMAND_DELETE_CONTATO.getName())) {
      // lista de Contato ID
      String[] contatoIdList = request.getParameterValues(malaDireta.USER_PARAM_CONTATO_ID.getName());
      // prepara a resposta
      Ajax.setResponseTypeText(response);
      // apaga os Contatos
      for (int i=0; i<contatoIdList.length; i++) {
        malaDireta.deleteEndereco(Integer.parseInt(contatoIdList[i]));
        %><%=gridEndereco.deleteRow(gridEndereco.rowIndex(new String[]{contatoIdList[i]}))%><%
      } // for
      // dispara
      return;
    } // if

    // se estamos iniciando...
    if (wizard.isStarting()) {
      // limpa os parâmetros de usuário
      malaDireta.userParamList().clearValues();
      // se não temos dados para reaproveitar...avisa como usar o processo
      if (!Controller.getCommand(request).equals(MalaDireta.COMMAND_USE_PRODUCER.getName()))
        throw new Exception("Este processo deve ser chamado através do comando '" + MalaDireta.COMMAND_USE_PRODUCER.getCaption() + "' existente em vários cadastros e relatórios do sistema.");
    } // if


    // define os valores dos parâmetros de usuário
    if (!Controller.isGoingPrevious(request) && !Controller.isRestarting(request))
      malaDireta.userParamList().setParamsValues(request);
    // Empresa selecionada
    malaDireta.USER_PARAM_EMPRESA_ID.setValue(selectedEmpresaId + "");

    // se estamos selecionado Contatos...
    if (wizard.actualStep() == MalaDireta.PROCESS_STEP_CONTATOS) {
      // se devemos reutilizar os dados de um Producer...
      if (Controller.getCommand(request).equals(MalaDireta.COMMAND_USE_PRODUCER.getName()))
        malaDireta.addEnderecoFromProducer();
      // se está adicionando um Contato...
      else if (Controller.getCommand(request).equals(MalaDireta.COMMAND_ADD_CONTATO.getName()))
        malaDireta.addEndereco(malaDireta.USER_PARAM_CONTATO_ID.getIntValue());
      // se está removendo um Contato...
      else if (Controller.getCommand(request).equals(MalaDireta.COMMAND_DELETE_CONTATO.getName())) {
        malaDireta.deleteEndereco(malaDireta.USER_PARAM_DELETE_INDEX.getIntValue());
        malaDireta.USER_PARAM_DELETE_INDEX.setValue("");
      }
    }
    // se estamos executando...
    else if (Controller.isExecuting(request)) {
      // executa
      malaDiretaInfo = malaDireta.execute();
    } // if
%>

<html>
  <head>
    <title><%=MalaDireta.ACTION.getCaption()%></title>
    <link href="<%=facade.getStyle().userInterface()%>" rel="stylesheet" type="text/css">
    <script src="include/scripts.js" type="text/javascript"></script>
  </head>
  <body style="margin:0px;" onselectstart="return true;" oncontextmenu="return false;">
    
    <div style="display: none; margin: 0 0 0 0;  padding: 0 0 0 0;"><%=form.begin()%> <input name="<%=malaDiretaEtiqueta.CONTROL_PARAM_ENDERECO_LIST.getName()%>" id="<%=malaDiretaEtiqueta.CONTROL_PARAM_ENDERECO_LIST.getName()%>" value="<%=nomeArquivo%>" /><%=form.end()%></div>

    <!-- inicia o assistente -->
    <%=wizard.begin()%>

      <!-- Contatos -->
      <%if (wizard.actualStep() == MalaDireta.PROCESS_STEP_CONTATOS) {
          // lista de endereços
          Vector enderecos = null;
          if (malaDireta.USER_PARAM_ENDERECO_LIST.getObject() == null)
            enderecos = new Vector();
          else
            enderecos = (Vector)malaDireta.USER_PARAM_ENDERECO_LIST.getObject();
        %>
        <table style="width:100%; height:100%;">
          <%--
          <!-- ExternalLookup -->
          <tr style="height:10px;">
            <td>Incluir Contato</td>
          </tr>
          <tr style="height:20px;">
            <td><%=ContatoUI.externalLookup(facade,
                                            malaDireta.USER_PARAM_CONTATO_ID.getName(),
                                            0,
                                            0,
                                            0,
                                            "",
                                            NaoSim.NAO,
                                            new int[]{Personalidade.PERSONALIDADE_CLIENTE, Personalidade.PERSONALIDADE_CONSUMIDOR, Personalidade.PERSONALIDADE_FORNECEDOR, Personalidade.PERSONALIDADE_FUNCIONARIO, Personalidade.PERSONALIDADE_OUTRA},
                                            ContatoUI.SELECT_TYPE_SINGLE,
                                            wizard.getForm().submitCustomCommandScript(MalaDireta.COMMAND_ADD_CONTATO, true),
                                            "",
                                            "")%>
                <input type="text" name="dummy" style="visibility:hidden; width:0px; height:0px;" /></td>
          </tr>
          --%>
          <!-- Grid -->
          <tr style="height:auto;">
            <td>
              <%=gridEndereco.begin()%>
                <%for (int i=0; i<enderecos.size(); i++) {
                    MalaDireta.EnderecoInfo endereco = (MalaDireta.EnderecoInfo)enderecos.elementAt(i);%>
                    <%=gridEndereco.addRow(new String[]{endereco.getNome(), (endereco.getCep().equals("") || endereco.getEndereco().equals("") || endereco.getNumero().equals("") || endereco.getCidade().equals("") || endereco.getUf().equals("") ? "Não" : "Sim"), StringTools.formatCustomMask(endereco.getTelefoneResidencial(), "(00) 0000-0000"), StringTools.formatCustomMask(endereco.getTelefoneCelular(), "(00) 0000-0000"), StringTools.formatCustomMask(endereco.getTelefoneTrabalho(), "(00) 0000-0000"), endereco.getEmail()},
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
              params += (i > 0 ? "&" : "") + "<%=malaDireta.USER_PARAM_CONTATO_ID.getName()%>=" + contatoIdList[i];
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
      <%if (wizard.actualStep() == MalaDireta.PROCESS_STEP_FINAL) {
          Grid grid = new Grid(facade, "gridEmails", 0, 0);
          grid.columns().add(new Grid.Column("!", 30, Grid.ALIGN_CENTER));
          grid.columns().add(new Grid.Column("Grupo", 570, Grid.ALIGN_LEFT));%>
        
        <%=pageControl.begin()%>
          <%=pageControl.beginTabSheet("Grupos de e-mail")%>
            <table style="width:100%; height:100%;">
              <!-- grupos de e-mail -->
              <tr style="height:20px;">
                <td>
                  Para evitar sistemas de controle de spam, os e-mails dos contatos
                  foram organizados em grupos de 100 destinatários. É necessário enviar
                  uma mensagem para cada grupo separadamente.
                </td>
              </tr>
              <tr style="height:auto;">
                <td>
                  <%=grid.begin()%>
                    <%String[] gruposEmail = malaDireta.getMalaDiretaInfo().getGruposEmail();
                    for (int i=0; i<gruposEmail.length; i++) {%>
                      <%=grid.addRow(new String[]{"<input type=checkbox id=checkbox" + i + " disabled=disabled />", "Grupo " + (i+1) + " - <a href=javascript:copiarGrupo(" + i + "); />Copiar</a>"})%>
                    <%} // for%>
                  <%=grid.end(gruposEmail.length + " grupos")%>
                  <%for (int i=0; i<gruposEmail.length; i++) {%>
                    <div id="grupoEmail<%=i%>" style="display:none;"><%=gruposEmail[i]%></div>
                  <%} // for%>
                </td>
              </tr>
              <tr style="height:10px;">
                <td>&nbsp;</td>
              </tr>
            </table>
          <%=pageControl.endTabSheet()%>
          
          <!-- endereços -->
          <%=pageControl.beginTabSheet("Etiquetas")%>
            <table style="height:100%; width: 100%;margin: 0 0 0 0; padding: 0 0 0 0;" cellspacing="0" cellpadding="0">
              <tr>
                <%// nossa instância de Mala Direta Etiqueta
                FileTools.saveTextFile(facade.tempLocalPath() + nomeArquivo , malaDiretaInfo.getEnderecos(), "");
                FileTools.saveTextFile(facade.tempLocalPath() + "etiqueta_" + nomeArquivo, malaDiretaInfo.getEtiquetas(), "");
                %>
                <td><iframe id="etiquetas" frameborder="0" src="<%=RelatorioMalaDiretaEtiqueta.ACTION.url(RelatorioMalaDiretaEtiqueta.COMMAND_ETIQUETA.getAction() + "&" +malaDiretaEtiqueta.CONTROL_PARAM_ENDERECO_LIST.getName() + "=etiqueta_" + nomeArquivo)%>" style="width:100%; height:100%; margin: 0 0 0 0; padding: 0 0 0 0;"></iframe></td>
              </tr>                                        
            </table>            
          <%=pageControl.endTabSheet()%>
          <%=pageControl.beginTabSheet("Lista de endereços")%>
           <table style="height:70px; width: 100%;margin: 0 0 0 0; padding: 0 0 0 0;" cellspacing="0" cellpadding="0">
            <tr style="height:20px;">
              <td>
                A lista de endereços deverá ser salva em um arquivo para criação de
                etiquetas através de várias aplicações, como o Microsoft Word.
              </td>
            </tr>
            <tr style="height:35px;">
              <td valign="bottom">
                <iframe id="enderecos" src="<%=MalaDireta.ACTION_ENDERECOS.url()%>" style="position:absolute; visibility:hidden;"></iframe>
                <%=Button.script(facade, "buttonEnderecos", "Salvar lista de endereços", "Salva a lista de endereços no seu micro.", ImageList.COMMAND_SAVE, "", Button.KIND_DEFAULT, "width:170px;", "salvarListaEnderecos();", false)%>
              </td>
            </tr>                
          </table>      
          <%=pageControl.endTabSheet()%>          
        <%=pageControl.end()%>

        <script type="text/javascript">

          function copiarGrupo(index) {
            // marca o CheckBox
            document.getElementById("checkbox" + index).checked = true;
            // copia os e-mails para a área de transferência
            window.clipboardData.setData("Text", document.getElementById("grupoEmail" + index).innerText);
            // avisa como proceder
            alert("Os e-mails do Grupo " + (index+1) + " foram copiados. Crie uma "
                + "nova mensagem de e-mail e cole os endereços (tecle CTRL + V) "
                + "no campo 'CCO' ou 'Cópia Oculta'.");
          }

          function salvarListaEnderecos() {
            enderecos.window.document.execCommand("SaveAs", 0, "Mala Direta - Lista de Endereços.html");
          }

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
