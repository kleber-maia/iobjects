
<%@page import="imanager.ui.entity.ContatoUI"%>
<%@include file="../include/beans.jsp"%>

<%@page import="imanager.entity.*"%>
<%@page import="imanager.misc.*"%>
<%@page import="imanager.ui.*"%>

<%!
  String COMMAND_CEP = "commandCep";
  String PARAM_CEP   = "paramCep";
%>

<%
  // início do bloco protegido
  try {
    // nossa instância de Empresa
    Empresa empresa = (Empresa)facade.getEntity(Empresa.CLASS_NAME);
    // nossa instância de Contato
    Contato contato = (Contato)facade.getEntity(Contato.CLASS_NAME);
    // noso ajax de Cep
    Ajax ajaxConsultaCep = new Ajax(facade, "consultaCep", Contato.ACTION_CADASTRO.getName(), COMMAND_CEP);

    // EmpresaInfo para editar
    EmpresaInfo empresaInfo = null;

    // se estamos inserindo...
    if (Controller.isInserting(request)) {
      // info em branco
      empresaInfo = new EmpresaInfo();
    }
    // se estamos salvando...
    else if (Controller.isSaving(request)) {
      // info preenchido na página
      empresaInfo = (EmpresaInfo)empresa.entityInfoFromRequest(request);
      empresaInfo.setTelefone(empresaInfo.getTelefone().replaceAll('_' + "", ""));
      // se estamos salvando e é um novo...inclui
      if (Controller.isSavingNew(request)) {
        empresa.insert(empresaInfo);
      }
      // se estamos salvando e é um existente...atualiza
      else {
        empresa.update(empresaInfo);
      } // if
      // compõe a resposta
      Form.composeAjaxResponse(response, Form.COMPOSE_TYPE_JAVA_SCRIPT);
      // atualiza a consulta
      %><%=EntityGrid.forwardBrowse(facade, empresa, Empresa.ACTION, Controller.isSavingNew(request) ? EntityGrid.OPERATION_INSERT : EntityGrid.OPERATION_UPDATE, empresaInfo)%><%
      // dispara
      return;
    }
    // se estamos editando...
    else if (Controller.isEditing(request)) {
      // info do registro para editar
      empresaInfo =
        empresa.selectByPrimaryKey(
          NumberTools.parseInt(request.getParameter(Empresa.FIELD_EMPRESA_ID.getFieldAlias()))
        );
    }
    // se quer o Cep...
    else if (Controller.getCommand(request).equals(COMMAND_CEP)) {
      // configura nossa resposta
      Ajax.setResponseTypeText(response);
      // obtém o CEP
      Contato.CepInfo cepInfo = contato.consultaCep(request.getParameter(PARAM_CEP));
      // retorna o script para alterar os dados na tela
      if (!cepInfo.endereco.equals("")) {
      %>
        document.getElementById("<%=Empresa.FIELD_ENDERECO.getFieldAlias()%>").value = "<%=cepInfo.endereco%>";
        document.getElementById("<%=Empresa.FIELD_ENDERECO_NUMERO.getFieldAlias()%>").value = "";
        document.getElementById("<%=Empresa.FIELD_ENDERECO_COMPLEMENTO.getFieldAlias()%>").value = "";
        document.getElementById("<%=Empresa.FIELD_ENDERECO_BAIRRO.getFieldAlias()%>").value = "<%=cepInfo.bairro%>";
        document.getElementById("<%=Empresa.FIELD_CIDADE.getFieldAlias()%>").value = "<%=cepInfo.municipio%>";
        document.getElementById("<%=Empresa.FIELD_UF.getFieldAlias()%>").value = "<%=cepInfo.uf%>";
        document.getElementById("<%=Empresa.FIELD_CODIGO_MUNICIPIO.getFieldAlias()%>").value = "<%=cepInfo.codigoMunicipio%>";
      <%
      }
      else {
      %>
        document.getElementById("<%=Empresa.FIELD_ENDERECO.getFieldAlias()%>").value = "";
        document.getElementById("<%=Empresa.FIELD_ENDERECO_NUMERO.getFieldAlias()%>").value = "";
        document.getElementById("<%=Empresa.FIELD_ENDERECO_COMPLEMENTO.getFieldAlias()%>").value = "";
        document.getElementById("<%=Empresa.FIELD_ENDERECO_BAIRRO.getFieldAlias()%>").value = "";
        document.getElementById("<%=Empresa.FIELD_CIDADE.getFieldAlias()%>").value = "";
        document.getElementById("<%=Empresa.FIELD_UF.getFieldAlias()%>").value = "";
        document.getElementById("<%=Empresa.FIELD_CODIGO_MUNICIPIO.getFieldAlias()%>").value = "";
      <%
      }// if
      // dispara
      return;
    }
    // se o comando é desconhecido...
    else {
      throw new UnknownCommandException();
    } // if

    // nosso FrameBar
    FrameBar frameBar = new FrameBar(facade, "frameBarEmpresa");
    // nosso Form de dados
    Form formData = new Form(request, "formDataEmpresa", Empresa.ACTION_CADASTRO, Empresa.COMMAND_SAVE, "", true, true);
%>

<html>
  <head>
    <title><%=Empresa.ACTION.getCaption()%></title>
    <link href="<%=facade.getStyle().userInterface()%>" rel="stylesheet" type="text/css">
    <script src="include/scripts.js" type="text/javascript"></script>
  </head>
  <body style="margin:0px;" onselectstart="return true;" oncontextmenu="return false;">

    <script type="text/javascript">

      function ajaxConsultaCep() {
        // nosso CEP
        var cep = document.getElementById("<%=Empresa.FIELD_CEP.getFieldAlias()%>");
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
          eval(<%=ajaxConsultaCep.responseText()%>)
        }
        // se ocorreu um erro...
        else {
          // mostra
          alert(<%=ajaxConsultaCep.responseErrorMessage()%>);
        } // if
      }
      
      function linkIBGE() {
      // link do IBGE
      window.open('http://www.ibge.gov.br/home/geociencias/areaterritorial/area.shtm', 'CódigoIBGE', 'resizable=yes,scrollbars=yes,status=no');
      }

    </script>

    <!-- inicia o FrameBar -->
    <%=frameBar.begin()%>

      <!-- área de frames -->
      <%=frameBar.beginFrameArea()%>

        <!-- Frame de identificação do objeto -->
        <%=frameBar.actionFrame(Empresa.ACTION)%>

        <!-- Frame de comandos -->
        <%=frameBar.beginFrame("Comandos", false, false)%>
          <table style="width:100%;">
            <tr>
              <td><%=CommandControl.formHyperlink(facade, formData, ImageList.COMMAND_SAVE, "Deseja mesmo salvar o registro?", true, false)%></td>
            </tr>
            <%if (facade.getLoggedUser().getSuperUser()) {%>
            <tr>
              <td><%=CommandControl.entityFormHyperlink(facade, empresa, Empresa.ACTION_CADASTRO, Empresa.COMMAND_INSERT, ImageList.COMMAND_INSERT, false)%></td>
            </tr>
            <%} // if%>
            <tr>
              <td><%=CommandControl.entityBrowseHyperlink(facade, empresa, Empresa.ACTION, ImageList.COMMAND_BACK, "Voltar", "Volta para a consulta.", false)%></td>
            </tr>
          </table>
        <%=frameBar.endFrame()%>
      <%=frameBar.endFrameArea()%>

      <!-- Form de dados -->
      <%=formData.begin()%>

        <!-- área de dados -->
        <%=frameBar.beginClientArea()%>
          <!-- campos ocultos -->
          <%=EntityFormEdit.script(facade, Empresa.FIELD_EMPRESA_ID, empresaInfo, request, -1, "", "")%>
          <%=EntityFormEdit.script(facade, Empresa.FIELD_USUARIO_INCLUSAO_ID, empresaInfo, request, -1, "", "")%>
          <%=EntityFormEdit.script(facade, Empresa.FIELD_DATA_INCLUSAO, empresaInfo, request, -1, "", "")%>
          <%=EntityFormEdit.script(facade, Empresa.FIELD_USUARIO_ALTERACAO_ID, empresaInfo, request, -1, "", "")%>
          <%=EntityFormEdit.script(facade, Empresa.FIELD_DATA_HORA_ALTERACAO, empresaInfo, request, -1, "", "")%>
          <!-- dados de Empresa -->
          <table cellpadding="0" cellspacing="0" style="width:100%;">
            <tr>
              <td style="height:30px;">

                <!-- campos Nome, Reposição Mix -->
                <%=GroupBox.begin("Identificação")%>
                  <table style="width:100%;">
                    <tr>
                      <td><%=EntityFieldLabel.script(facade, Empresa.FIELD_NOME, request)%></td>
                    </tr>
                    <tr>
                      <td><%=EntityFormEdit.script(facade, Empresa.FIELD_NOME, empresaInfo, request, 0, "", "")%></td>
                    </tr>
                  </table>
                <%=GroupBox.end()%>

              </td>
            </tr>
            <tr>
              <td style="height:30px;">

                <!-- campos Razão Social, CNPJ, Inscrições, Regime Tributário, Utiliza PAF -->
                <%=GroupBox.begin("Dados Cadastrais")%>
                  <table style="width:100%;">
                    <tr style="width:100%;">
                      <td style="width:auto;"><%=EntityFieldLabel.script(facade, Empresa.FIELD_RAZAO_SOCIAL, request)%></td>
                      <td style="width:110px;"><%=EntityFieldLabel.script(facade, Empresa.FIELD_CNPJ, request)%></td>
                      <td style="width:110px;"><%=EntityFieldLabel.script(facade, Empresa.FIELD_INSCRICAO_ESTADUAL, request)%></td>
                      <td style="width:110px;"><%=EntityFieldLabel.script(facade, Empresa.FIELD_INSCRICAO_MUNICIPAL, request)%></td>
                      <td style="width:110px;"><%=EntityFieldLabel.script(facade, Empresa.FIELD_CNAE, request)%></td>
                      <td style="width:150px;"><%=EntityFieldLabel.script(facade, Empresa.FIELD_REGIME_TRIBUTARIO, request)%></td>
                      <td style="width:75px;"><%=EntityFieldLabel.script(facade, Empresa.FIELD_UTILIZA_PAF, request, "", "", !facade.getLoggedUser().getSuperUser())%></td>
                    </tr>
                    <tr style="width:100%;">
                      <td style="width:auto;"><%=EntityFormEdit.script(facade, Empresa.FIELD_RAZAO_SOCIAL, empresaInfo, request, 0, "", "")%></td>
                      <td style="width:110px;"><%=EntityFormEdit.script(facade, Empresa.FIELD_CNPJ, empresaInfo, request, 0, "", "")%></td>
                      <td style="width:110px;"><%=EntityFormEdit.script(facade, Empresa.FIELD_INSCRICAO_ESTADUAL, empresaInfo, request, 0, "", "")%></td>
                      <td style="width:110px;"><%=EntityFormEdit.script(facade, Empresa.FIELD_INSCRICAO_MUNICIPAL, empresaInfo, request, 0, "", "")%></td>
                      <td style="width:110px;"><%=EntityFormEdit.script(facade, Empresa.FIELD_CNAE, empresaInfo, request, 0, "", "")%></td>
                      <td style="width:150px;"><%=EntityFormSelect.script(facade, Empresa.FIELD_REGIME_TRIBUTARIO, empresaInfo, request, 0, "", "", false)%></td>
                      <%if (facade.getLoggedUser().getSuperUser()) {%>
                        <td style="width:75px;"><%=EntityFormSelect.script(facade, Empresa.FIELD_UTILIZA_PAF, empresaInfo, request, 0, "", "", false)%></td>
                      <%}
                        else {%>
                       <td style="width:75px;"><%=EntityFormEdit.script(facade, Empresa.FIELD_UTILIZA_PAF, empresaInfo, request, 0, "", "", true, false)%></td>
                      <%} // if%>
                    </tr>
                  </table>
                <%=GroupBox.end()%>

              </td>
            </tr>
            <tr>
              <td style="height:60px;">

                <!-- campos Endereço, Cidade, UF, CEP, Telefones, Emails -->
                <%=GroupBox.begin("Endereço")%>
                  <table style="width:100%;">
                    <tr>
                      <td style="width:60px;"><%=EntityFieldLabel.script(facade, Empresa.FIELD_CEP, request)%></td>
                      <td style="width:auto;"><%=EntityFieldLabel.script(facade, Empresa.FIELD_ENDERECO, request)%></td>
                      <td style="width:60px;"><%=EntityFieldLabel.script(facade, Empresa.FIELD_ENDERECO_NUMERO, request)%></td>
                      <td style="width:100px;"><%=EntityFieldLabel.script(facade, Empresa.FIELD_ENDERECO_COMPLEMENTO, request)%></td>
                      <td style="width:150px;"><%=EntityFieldLabel.script(facade, Empresa.FIELD_ENDERECO_BAIRRO, request)%></td>
                      <td style="width:auto;"><%=EntityFieldLabel.script(facade, Empresa.FIELD_CIDADE, request)%></td>
                      <td style="width:75px;"><a href="#" onclick="linkIBGE()" <%=EntityFieldLabel.script(facade, Empresa.FIELD_CODIGO_MUNICIPIO, request)%></a></td>
                      <td style="width:25px;"><%=EntityFieldLabel.script(facade, Empresa.FIELD_UF, request)%></td>
                    </tr>
                    <tr>
                      <td><%=EntityFormEdit.script(facade, Empresa.FIELD_CEP, empresaInfo, request, 0, "", "ajaxConsultaCep();")%></td>
                      <td><%=EntityFormEdit.script(facade, Empresa.FIELD_ENDERECO, empresaInfo, request, 0, "", "")%></td>
                      <td><%=EntityFormEdit.script(facade, Empresa.FIELD_ENDERECO_NUMERO, empresaInfo, request, 0, "", "")%></td>
                      <td><%=EntityFormEdit.script(facade, Empresa.FIELD_ENDERECO_COMPLEMENTO, empresaInfo, request, 0, "", "")%></td>
                      <td><%=EntityFormEdit.script(facade, Empresa.FIELD_ENDERECO_BAIRRO, empresaInfo, request, 0, "", "")%></td>
                      <td><%=EntityFormEdit.script(facade, Empresa.FIELD_CIDADE, empresaInfo, request, 0, "", "")%></td>
                      <td><%=EntityFormEdit.script(facade, Empresa.FIELD_CODIGO_MUNICIPIO, empresaInfo, request, 0, "", "")%></td>
                      <td><%=EntityFormEdit.script(facade, Empresa.FIELD_UF, empresaInfo, request, 0, "", "")%></td>
                    </tr>
                  </table>
                  <table style="width:100%;">
                    <tr>
                      <td style="width:50%;"><%=EntityFieldLabel.script(facade, Empresa.FIELD_TELEFONE, request)%></td>
                      <td style="width:50%;"><%=EntityFieldLabel.script(facade, Empresa.FIELD_EMAIL, request)%></td>
                    </tr>
                    <tr>
                      <td><%=EntityFormEdit.script(facade, Empresa.FIELD_TELEFONE, empresaInfo, request, 0, "", "")%></td>
                      <td><%=EntityFormEdit.script(facade, Empresa.FIELD_EMAIL, empresaInfo, request, 0, "", "")%></td>
                    </tr>
                  </table>
                <%=GroupBox.end()%>

              </td>
            </tr>
            <tr>
              <td>
                  
                <table style="width:100%;height:30px;">
                  <tr>
                    <td style="width:300px;">

                        <!-- campos Contador e CRC -->
                        <%=GroupBox.begin("Contador")%>
                          <table style="width:100%;">
                            <tr>
                              <td style="width:230px;"><%=EntityLookupLabel.script(facade, Empresa.LOOKUP_CONTADOR, request)%></td>
                              <td style="width:70px;"><%=EntityFieldLabel.script(facade, Empresa.FIELD_CONTADOR_CRC, request)%></td>
                            </tr>
                            <tr>
                              <td><%=ContatoUI.lookupSearchForLookup(facade, Empresa.LOOKUP_CONTADOR, empresaInfo, NaoSim.TODOS, NaoSim.SIM, NaoSim.TODOS, NaoSim.TODOS, NaoSim.TODOS, 0, "", "", false)%></td>
                              <td><%=EntityFormEdit.script(facade, Empresa.FIELD_CONTADOR_CRC, empresaInfo, request, 0, "", "")%></td>
                            </tr>
                          </table>
                        <%=GroupBox.end()%>
                        
                    </td>
                    <td style="width:235px;">
                      
                      <!-- campos NF-e -->
                      <%=GroupBox.begin("NF-e")%>
                        <table>
                          <tr>
                            <td style="width:30px;"><%=EntityFieldLabel.script(facade, Empresa.FIELD_SERIE_DOCUMENTO, request)%></td>
                            <td style="width:75px;"><%=EntityFieldLabel.script(facade, Empresa.FIELD_NUMERO_DOCUMENTO_FISCAL, request)%></td>
                            <td style="width:130px;"><%=EntityFieldLabel.script(facade, Empresa.FIELD_IDENTIFICACAO_AMBIENTE, request)%></td>
                          </tr>
                          <tr>
                            <td style="width:30px;"><%=EntityFormEdit.script(facade, Empresa.FIELD_SERIE_DOCUMENTO, empresaInfo, request, 0, "", "")%></td>
                            <td style="width:75px;"><%=EntityFormEdit.script(facade, Empresa.FIELD_NUMERO_DOCUMENTO_FISCAL, empresaInfo, request, 0, "", "")%></td>
                            <td style="width:130px;"><%=EntityFormSelect.script(facade, Empresa.FIELD_IDENTIFICACAO_AMBIENTE, empresaInfo, request, 0, "", "", false)%></td>
                          </tr>
                        </table>
                      <%=GroupBox.end()%>
                      
                    </td>
                    <td style="width:auto;">

                      <!-- campos SMTP -->
                      <%=GroupBox.begin("Configurações de Envio de E-mail (SMTP)")%>
                        <table style="width:100%;">
                          <tr>
                            <td style="width:auto;"><%=EntityFieldLabel.script(facade, Empresa.FIELD_SERVIDOR_SMTP, request)%></td>
                            <td style="width:35px;"><%=EntityFieldLabel.script(facade, Empresa.FIELD_PORTA_SMTP, request)%></td>
                            <td style="width:75px;"><%=EntityFieldLabel.script(facade, Empresa.FIELD_REQUER_SSL_SMTP, request)%></td>
                            <td style="width:100px;"><%=EntityFieldLabel.script(facade, Empresa.FIELD_USUARIO_EMAIL, request)%></td>
                            <td style="width:100px;"><%=EntityFieldLabel.script(facade, Empresa.FIELD_SENHA_EMAIL, request)%></td>
                            <td style="width:75px;"><%=EntityFieldLabel.script(facade, Empresa.FIELD_COPIA_EMAIL, request)%></td>
                          </tr>
                          <tr>
                            <td><%=EntityFormEdit.script(facade, Empresa.FIELD_SERVIDOR_SMTP, empresaInfo, request, 0, "", "")%></td>
                            <td><%=EntityFormEdit.script(facade, Empresa.FIELD_PORTA_SMTP, empresaInfo, request, 0, "", "")%></td>
                            <td><%=EntityFormSelect.script(facade, Empresa.FIELD_REQUER_SSL_SMTP, empresaInfo, request, 0, "", "", false)%></td>
                            <td><%=EntityFormEdit.script(facade, Empresa.FIELD_USUARIO_EMAIL, empresaInfo, request, 0, "", "")%></td>
                            <td><%=EntityFormEdit.script(facade, Empresa.FIELD_SENHA_EMAIL, empresaInfo, request, 0, "", "", false, true)%></td>
                            <td><%=EntityFormSelect.script(facade, Empresa.FIELD_COPIA_EMAIL, empresaInfo, request, 0, "", "", false)%></td>
                          </tr>
                        </table>
                      <%=GroupBox.end()%>
                      
                    </td>
                  </tr>
                </table>
                    
              </td>
            </tr>
            <tr>
              <td>
                
                <%=GroupBox.begin("Segurança")%>
                  <table style="width: 100%;">
                    <tr>
                      <td style="width: 90px"><%=EntityFieldLabel.script(facade, Empresa.FIELD_ESTOQUE, request, "", "", !facade.getLoggedUser().getSuperUser())%></td>
                      <td style="width: 90px"><%=EntityFieldLabel.script(facade, Empresa.FIELD_ARQUIVO, request, "", "", !facade.getLoggedUser().getSuperUser())%></td>
                      <td style="width: auto"><%=EntityFieldLabel.script(facade, Empresa.FIELD_USUARIO_INCLUSAO_ID, request)%></td>
                      <td style="width: auto"><%=EntityFieldLabel.script(facade, Empresa.FIELD_USUARIO_ALTERACAO_ID, request)%></td>
                    </tr>
                    <tr>
                    <%if (facade.getLoggedUser().getSuperUser()) {%>
                        <td><%=EntityFormSelect.script(facade, Empresa.FIELD_ESTOQUE, empresaInfo, request, 0, "", "", false)%></td>
                        <td><%=EntityFormSelect.script(facade, Empresa.FIELD_ARQUIVO, empresaInfo, request, 0, "", "", false)%></td> 
                    <%} 
                      else {%>
                      <td><%=EntityFormEdit.script(facade, Empresa.FIELD_ESTOQUE, empresaInfo, request, 0, "", "", true, false)%></td>
                      <td><%=EntityFormEdit.script(facade, Empresa.FIELD_ARQUIVO, empresaInfo, request, 0, "", "", true, false)%></td>
                    <%}%>
                      <td><%=FormEdit.script(facade, "inclusao", (empresaInfo.getUsuarioInclusaoId() > 0 ? facade.securityService().getUser(empresaInfo.getUsuarioInclusaoId()).getName() : "") + " - " + DateTools.formatDate(empresaInfo.getDataInclusao()), 0, 0, FormEdit.ALIGN_LEFT, "", "", "", "", "", true)%></td>
                      <td><%=FormEdit.script(facade, "alteracao", (empresaInfo.getUsuarioAlteracaoId() > 0 ? facade.securityService().getUser(empresaInfo.getUsuarioAlteracaoId()).getName() : "") + " - " + DateTools.formatDateTime(empresaInfo.getDataHoraAlteracao()), 0, 0, FormEdit.ALIGN_LEFT, "", "", "", "", "", true)%></td>
                    </tr>
                  </table>
                <%=GroupBox.end()%>
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
