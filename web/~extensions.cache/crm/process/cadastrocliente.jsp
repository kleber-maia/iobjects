
<%@include file="../include/beans.jsp"%>

<%@page import="imanager.entity.*"%>
<%@page import="imanager.misc.*"%>
<%@page import="imanager.process.*"%>
<%@page import="imanager.ui.entity.*"%>

<%!
  String COMMAND_CEP = "commandCep";
  String PARAM_CEP   = "paramCep";
%>

<%
  // início do bloco protegido
  try {
    // nossa instância de Cadastro Cliente
    CadastroCliente cadastroCliente = (CadastroCliente)facade.getProcess(CadastroCliente.CLASS_NAME);
    // nossa instância de Contato
    Contato contato = (Contato)facade.getEntity(Contato.CLASS_NAME);
    // noso ajax de Cep
    Ajax ajaxConsultaCep = new Ajax(facade, "consultaCep", cadastroCliente.ACTION.getName(), COMMAND_CEP);

    // se quer o Cep...
    if (Controller.getCommand(request).equals(COMMAND_CEP)) {
      // configura nossa resposta
      Ajax.setResponseTypeText(response);
      // obtém o CEP
      Contato.CepInfo cepInfo = contato.consultaCep(StringTools.removeNotNumbers(request.getParameter(PARAM_CEP)));
      // retorna o script para alterar os dados na tela
      if (!cepInfo.endereco.equals("")) {
      %>
        document.getElementById("<%=cadastroCliente.USER_PARAM_ENDERECO.getName()%>").value = "<%=cepInfo.endereco%>";
        document.getElementById("<%=cadastroCliente.USER_PARAM_ENDERECO_NUMERO.getName()%>").value = "";
        document.getElementById("<%=cadastroCliente.USER_PARAM_ENDERECO_COMPLEMENTO.getName()%>").value = "";
        document.getElementById("<%=cadastroCliente.USER_PARAM_ENDERECO_BAIRRO.getName()%>").value = "<%=cepInfo.bairro%>";
        document.getElementById("<%=cadastroCliente.USER_PARAM_MUNICIPIO.getName()%>").value = "<%=cepInfo.municipio%>";
        document.getElementById("<%=cadastroCliente.USER_PARAM_CODIGO_MUNICIPIO.getName()%>").value = "<%=cepInfo.codigoMunicipio%>";
        document.getElementById("<%=cadastroCliente.USER_PARAM_UF.getName()%>").value = "<%=cepInfo.uf%>";
      <%
      }
      else {
      %>
        document.getElementById("<%=cadastroCliente.USER_PARAM_ENDERECO.getName()%>").value = "";
        document.getElementById("<%=cadastroCliente.USER_PARAM_ENDERECO_NUMERO.getName()%>").value = "";
        document.getElementById("<%=cadastroCliente.USER_PARAM_ENDERECO_COMPLEMENTO.getName()%>").value = "";
        document.getElementById("<%=cadastroCliente.USER_PARAM_ENDERECO_BAIRRO.getName()%>").value = "";
        document.getElementById("<%=cadastroCliente.USER_PARAM_MUNICIPIO.getName()%>").value = "";
        document.getElementById("<%=cadastroCliente.USER_PARAM_CODIGO_MUNICIPIO.getName()%>").value = "";
        document.getElementById("<%=cadastroCliente.USER_PARAM_UF.getName()%>").value = "";
      <%
      }// if
      // dispara
      return;
    }

    // nosso Wizard
    Wizard wizard = new Wizard(facade, request, "wizardCadastroCliente", CadastroCliente.ACTION, CadastroCliente.COMMAND_EXECUTE, cadastroCliente.processStepList());

    // se estamos iniciando...
    if (wizard.isStarting()) {
      // limpa os parâmetros de usuário
      cadastroCliente.userParamList().clearValues();
      // se não temos dados para reaproveitar...avisa como usar o processo
      if (!Controller.getCommand(request).equals(CadastroCliente.COMMAND_CADASTRAR.getName()))
        throw new Exception("Este processo deve ser chamado através do comando 'Cadastrar Cliente' existente em vários cadastros do sistema.");
    } // if

    // define os valores dos parâmetros de usuário
    if (!Controller.isGoingPrevious(request) && !Controller.isRestarting(request))
      cadastroCliente.userParamList().setParamsValues(request);

    // se estamos executando...executa
    ContatoInfo contatoInfo = null;
    if (Controller.isExecuting(request))
      contatoInfo = cadastroCliente.execute();
%>

<html>
  <head>
    <title><%=CadastroCliente.ACTION.getCaption()%></title>
    <link href="<%=facade.getStyle().userInterface()%>" rel="stylesheet" type="text/css">
    <script src="include/scripts.js" type="text/javascript"></script>
  </head>
  <body style="margin:0px;" onselectstart="return true;" oncontextmenu="return false;">

    <!-- inicia o assistente -->
    <%=wizard.begin()%>

      <!-- primeira etapa -->
      <%if (wizard.actualStep() == CadastroCliente.PROCESS_STEP_DADOS) {
          // nosso PageControl
          PageControl pageControl = new PageControl(facade, "pageControlContato", true);%>

          <script type="text/javascript">

            function ajaxConsultaCep() {
              // nosso CEP
              var cep = document.getElementById("<%=cadastroCliente.USER_PARAM_CEP.getName()%>");
              // se não temos um CEP...dispara
              if (cep.value == "")
                return;
              // nosso parâmetro
              var params = "<%=PARAM_CEP%>=" + cep.value;
              // faz a requisição
              <%=ajaxConsultaCep.request(new String[]{"params"})%>
              // se ocorreu tudo bem...
              if (<%=ajaxConsultaCep.isResponseStatusOK()%>) {
                // executa o script retornado
                eval(<%=ajaxConsultaCep.responseText()%>);
              }
              // se ocorreu um erro...
              else {
                // mostra
                alert(<%=ajaxConsultaCep.responseErrorMessage()%>);
              } // if
            }
            
            function linkIBGE() {
              <%--var cidade = document.getElementById("<%=contato.FIELD_CIDADE.getFieldAlias()%>");
              http://www.ibge.gov.br/home/geociencias/areaterritorial/area.php?nome=jaboatao%20dos%20guararapes&codigo=&submit.x=0&submit.y=0--%>
              // link do IBGE
              window.open('http://www.ibge.gov.br/home/geociencias/areaterritorial/area.shtm', 'CodigoIBGE', 'resizable=yes,scrollbars=yes,status=no');
            }
            function showHideControls(){
              // nossos elementos
              var tipoPessoa                      = document.getElementById("<%=cadastroCliente.USER_PARAM_TIPO_PESSOA.getName()%>");
              var tablePessoaFisica               = document.getElementById("tablePessoaFisica");
              var tablePessoaJuridica             = document.getElementById("tablePessoaJuridica");
              // nossas variáveis
              var pessoaFisica = tipoPessoa.options[tipoPessoa.selectedIndex].value == <%=TipoPessoa.FISICA%>;
              // mostra ou oculta os controles
              tablePessoaFisica.style.display   = (pessoaFisica ? "block" : "none");
              tablePessoaJuridica.style.display = (!pessoaFisica ? "block" : "none");
            }

          </script>

          <!-- dados do cliente -->
          <table cellpadding="0" cellspacing="0" style="width:100%; height:100%;">
            <tr>
              <td style="height:40px;">

                <!-- Nome, Grupo Contato -->
                <%=GroupBox.begin("Identificação")%>
                  <table style="width:100%;">
                    <tr>
                      <td style="width:auto;"><%=ParamLabel.script(facade, cadastroCliente.USER_PARAM_NOME)%>*</td>
                      <td style="width:176px;"><%=ParamLabel.script(facade, cadastroCliente.USER_PARAM_GRUPO_CONTATO_ID)%>*</td>
                      <td style="width:80px;"><%=ParamLabel.script(facade, cadastroCliente.USER_PARAM_TIPO_PESSOA)%></td>
                    </tr>
                    <tr>
                      <td style="width:auto;"><%=ParamFormEdit.script(facade, cadastroCliente.USER_PARAM_NOME, 0, "", "")%></td>
                      <td style="width:176px;"><%=GrupoContatoUI.lookupListForParam(facade, cadastroCliente.USER_PARAM_GRUPO_CONTATO_ID, 0, "", "")%></td>
                      <td style="width:80px;"><%=ParamFormSelect.script(facade, cadastroCliente.USER_PARAM_TIPO_PESSOA, 0, "", "showHideControls();")%></td>
                    </tr>
                  </table>
                <%=GroupBox.end()%>

              </td>
            </tr>
            <tr>
              <td style="height:40px;">

                <!-- CPF, RG, Data Nascimento, Sexo -->
                <%=GroupBox.begin("Dados Cadastrais")%>
                  <table id="tablePessoaFisica" style="width:100%;">
                    <tr>
                      <td style="width:150px;"><%=ParamLabel.script(facade, cadastroCliente.USER_PARAM_CPF)%></td>
                      <td style="width:150px;"><%=ParamLabel.script(facade, cadastroCliente.USER_PARAM_RG)%></td>
                      <td style="width:118px;"><%=ParamLabel.script(facade, cadastroCliente.USER_PARAM_DATA_NASCIMENTO)%></td>
                      <td style="width:auto;"><%=ParamLabel.script(facade, cadastroCliente.USER_PARAM_SEXO)%></td>
                    </tr>
                    <tr>
                      <td style="width:150px;"><%=ParamFormEdit.script(facade, cadastroCliente.USER_PARAM_CPF, 0, "", "")%></td>
                      <td style="width:150px;"><%=ParamFormEdit.script(facade, cadastroCliente.USER_PARAM_RG, 0, "", "")%></td>
                      <td style="width:118px;"><%=ParamFormEdit.script(facade, cadastroCliente.USER_PARAM_DATA_NASCIMENTO, 0, "", "")%></td>
                      <td style="width:auto;"><%=ParamFormSelect.script(facade, cadastroCliente.USER_PARAM_SEXO, 0, "", "")%></td>
                    </tr>
                  </table>
                  <table id="tablePessoaJuridica" style="display:none; width:100%;">
                    <tr>
                      <td style="width:auto;"><%=ParamLabel.script(facade, cadastroCliente.USER_PARAM_RAZAO_SOCIAL)%></td>
                      <td style="width:110px;"><%=ParamLabel.script(facade, cadastroCliente.USER_PARAM_CNPJ)%></td>
                      <td style="width:95px;"><%=ParamLabel.script(facade, cadastroCliente.USER_PARAM_INSCRICAO_ESTADUAL)%></td>
                      <td style="width:95px;"><%=ParamLabel.script(facade, cadastroCliente.USER_PARAM_INSCRICAO_MUNICIPAL)%></td>
                    </tr>
                    <tr>
                      <td style="width:auto;"><%=ParamFormEdit.script(facade, cadastroCliente.USER_PARAM_RAZAO_SOCIAL, 0, "", "")%></td>
                      <td style="width:110px;"><%=ParamFormEdit.script(facade, cadastroCliente.USER_PARAM_CNPJ, 0, "", "")%></td>
                      <td style="width:95px;"><%=ParamFormEdit.script(facade, cadastroCliente.USER_PARAM_INSCRICAO_ESTADUAL, 0, "", "")%></td>
                      <td style="width:95px;"><%=ParamFormEdit.script(facade, cadastroCliente.USER_PARAM_INSCRICAO_MUNICIPAL, 0, "", "")%></td>
                    </tr>
                  </table>                  
                <%=GroupBox.end()%>

              </td>
            </tr>
            <tr>
              <td style="height:40px;">

                <!-- CEP, Endereço, Número, Complemento, Bairro, Cidade, CEP, Telefones -->
                <%=GroupBox.begin("Endereço")%>
                  <table style="width:100%;">
                    <tr>
                      <td style="width:60px;"><%=ParamLabel.script(facade, cadastroCliente.USER_PARAM_CEP)%></td>
                      <td style="width:auto;"><%=ParamLabel.script(facade, cadastroCliente.USER_PARAM_ENDERECO)%></td>
                      <td style="width:60px;"><%=ParamLabel.script(facade, cadastroCliente.USER_PARAM_ENDERECO_NUMERO)%></td>
                      <td style="width:100px;"><%=ParamLabel.script(facade, cadastroCliente.USER_PARAM_ENDERECO_COMPLEMENTO)%></td>
                      <td style="width:150px;"><%=ParamLabel.script(facade, cadastroCliente.USER_PARAM_ENDERECO_BAIRRO)%></td>
                    </tr>
                    <tr>
                      <td style="width:60px;"><%=ParamFormEdit.script(facade, cadastroCliente.USER_PARAM_CEP, 0, "", "ajaxConsultaCep();")%></td>
                      <td style="width:auto;"><%=ParamFormEdit.script(facade, cadastroCliente.USER_PARAM_ENDERECO, 0, "", "")%></td>
                      <td style="width:60px;"><%=ParamFormEdit.script(facade, cadastroCliente.USER_PARAM_ENDERECO_NUMERO, 0, "", "")%></td>
                      <td style="width:100px;"><%=ParamFormEdit.script(facade, cadastroCliente.USER_PARAM_ENDERECO_COMPLEMENTO, 0, "", "")%></td>
                      <td style="width:150px;"><%=ParamFormEdit.script(facade, cadastroCliente.USER_PARAM_ENDERECO_BAIRRO, 0, "", "")%></td>
                    </tr>
                  </table>
                  <table style="width:100%;">
                    <tr>
                      <td style="width:auto;"><%=ParamLabel.script(facade, cadastroCliente.USER_PARAM_MUNICIPIO)%></td>
                      <td style="width:75px;"><a href="#" onclick="linkIBGE()" <%=ParamLabel.script(facade, cadastroCliente.USER_PARAM_CODIGO_MUNICIPIO)%></a></td>
                      <td style="width:25px;"><%=ParamLabel.script(facade, cadastroCliente.USER_PARAM_UF)%></td>
                      <td style="width:90px;"><%=ParamLabel.script(facade, cadastroCliente.USER_PARAM_TELEFONE_RESIDENCIAL)%></td>
                      <td style="width:90px;"><%=ParamLabel.script(facade, cadastroCliente.USER_PARAM_TELEFONE_CELULAR)%></td>
                      <td style="width:90px;"><%=ParamLabel.script(facade, cadastroCliente.USER_PARAM_TELEFONE_TRABALHO)%></td>
                    </tr>
                    <tr>
                      <td style="width:auto;"><%=ParamFormEdit.script(facade, cadastroCliente.USER_PARAM_MUNICIPIO, 0, "", "")%></td>
                      <td style="width:75px;"><%=ParamFormEdit.script(facade, cadastroCliente.USER_PARAM_CODIGO_MUNICIPIO, 0, "", "")%></td>
                      <td style="width:25px;"><%=ParamFormEdit.script(facade, cadastroCliente.USER_PARAM_UF, 0, "", "")%></td>
                      <td style="width:90px;" style="width:90px;"><%=ParamFormEdit.script(facade, cadastroCliente.USER_PARAM_TELEFONE_RESIDENCIAL, 0, "", "")%></td>
                      <td style="width:90px;"><%=ParamFormEdit.script(facade, cadastroCliente.USER_PARAM_TELEFONE_CELULAR, 0, "", "")%></td>
                      <td style="width:90px;"><%=ParamFormEdit.script(facade, cadastroCliente.USER_PARAM_TELEFONE_TRABALHO, 0, "", "")%></td>
                    </tr>
                  </table>
                  <table style="width:100%;">
                    <tr>
                      <td style="width:auto;"><%=ParamLabel.script(facade, cadastroCliente.USER_PARAM_EMAIL)%></td>
                    </tr>
                    <tr>
                      <td><%=ParamFormEdit.script(facade, cadastroCliente.USER_PARAM_EMAIL, 0, "", "")%></td>
                    </tr>
                  </table>
                <%=GroupBox.end()%>

              </td>
            </tr>
            <tr>
              <td style="height:auto;">

                <%--
                <%=pageControl.begin()%>
                  <%=pageControl.beginTabSheet("Anotações")%>
                    <%=ParamFormMemo.script(facade, cadastroCliente.USER_PARAM_ANOTACOES, 0, 0, "", "", false, false)%>
                  <%=pageControl.endTabSheet()%>
                <%=pageControl.end()%>
                --%>

              </td>
            </tr>
          </table>
      <%} // if
      %>

      <!-- segunda etapa -->
      <%if (wizard.actualStep() == CadastroCliente.PROCESS_STEP_RESULTADO) {%>
        <p>O Cliente foi inserido com sucesso e já está selecionado no formulário.</p>
        <script type="text/javascript">
          // chama a função de callback na janela pai...
          window.opener.eval("cadastroClienteChangeValue(\"<%=contatoInfo.getContatoId()%>\",\"<%=contatoInfo.getNome()%>\");");
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
