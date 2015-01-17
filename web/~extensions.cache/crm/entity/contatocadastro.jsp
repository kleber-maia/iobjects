
<%@page import="javax.imageio.ImageIO"%>
<%@page import="java.io.File"%>
<%@page import="java.awt.image.BufferedImage"%>
<%@page import="java.awt.Image"%>
<%@page import="java.net.URLEncoder"%>
<%@include file="../include/beans.jsp"%>

<%@page import="imanager.entity.*"%>
<%@page import="imanager.misc.*"%>
<%@page import="imanager.report.*"%>


<%!
  String COMMAND_CEP       = "commandCep";
  String COMMAND_FACEBOOK  = "commandFacebook";
  String PARAM_CEP         = "paramCep";
  Param  TEMP_NOME_USUARIO = new Param("tempNomeUsuario", new String());
  String urlPic            = "";
  String nomeUsuarioFace   = "";
  
  public String retornaDados(String userName, boolean isPage) throws Exception {
    if (isPage)
      return  HttpTools.browse("http://graph.facebook.com/fql?q=SELECT%20name,%20pic%20FROM%20page%20WHERE%20username='" + (userName.length() > 0 ? userName : "__") + "'%20OR%20page_id='" + (userName.length() > 0 ? userName : "__") + "'");
    else
      return  HttpTools.browse("http://graph.facebook.com/fql?q=SELECT%20name,%20pic%20FROM%20user%20WHERE%20username='" + (userName.length() > 0 ? userName : "__") + "'%20OR%20uid='" + (userName.length() > 0 ? userName : "__") + "'");
  }
%>

<%
  // início do bloco protegido
  try {
    // nossa instância de Contato
    Contato contato = (Contato)facade.getEntity(Contato.CLASS_NAME);
    // nossa instância de Contato Meta
    ContatoMeta contatoMeta = (ContatoMeta)facade.getEntity(ContatoMeta.CLASS_NAME);
    // nossa instância de Historico Contato
    RelatorioHistoricoContato relatorioHistoricoContato = (RelatorioHistoricoContato)facade.getReport(RelatorioHistoricoContato.CLASS_NAME);

    // noso ajax de Cep
    Ajax ajaxConsultaCep = new Ajax(facade, "consultaCep", Contato.ACTION_CADASTRO.getName(), COMMAND_CEP);
    // noso ajax de Facebook
    Ajax ajaxFacebook    = new Ajax(facade, "ajaxFacebook", Contato.ACTION_CADASTRO.getName(), COMMAND_FACEBOOK);  

    // ContatoInfo para editar
    ContatoInfo contatoInfo = null;

    // se estamos inserindo...
    if (Controller.isInserting(request)) {
      // info em branco
      contatoInfo = new ContatoInfo();
      // valores padrão
      contatoInfo.setCliente(NaoSim.SIM);
    }
    // se estamos salvando...
    else if (Controller.isSaving(request)) {
      // info preenchido na página
      contatoInfo = (ContatoInfo)contato.entityInfoFromRequest(request);
      contatoInfo.setTelefoneCelular(contatoInfo.getTelefoneCelular().replaceAll('_' + "", ""));
      contatoInfo.setTelefoneResidencial(contatoInfo.getTelefoneResidencial().replaceAll('_' + "", ""));
      contatoInfo.setTelefoneTrabalho(contatoInfo.getTelefoneTrabalho().replaceAll('_' + "", "")); 
      // se estamos salvando e é um novo...inclui
      if (Controller.isSavingNew(request)) {
        contato.insert(contatoInfo);
      }
      // se estamos salvando e é um existente...atualiza
      else {
        contato.update(contatoInfo);
      } // if
      // compõe a resposta
      Form.composeAjaxResponse(response, Form.COMPOSE_TYPE_JAVA_SCRIPT);
      // atualiza a consulta
      %><%=EntityGrid.forwardBrowse(facade, contato, Contato.ACTION, Controller.isSavingNew(request) ? EntityGrid.OPERATION_INSERT : EntityGrid.OPERATION_UPDATE, contatoInfo)%><%
      // dispara
      return;
    }
    // se estamos editando...
    else if (Controller.isEditing(request)) {
      // info do registro para editar
      contatoInfo =
        contato.selectByPrimaryKey(
          NumberTools.parseInt(request.getParameter(Contato.FIELD_CONTATO_ID.getFieldAlias()))
        );
    }
    // se quer o Facebook...
    else if (Controller.getCommand(request).equals(COMMAND_FACEBOOK)) {
      // configura nossa resposta
      Ajax.setResponseTypeText(response);
      TEMP_NOME_USUARIO.setValue(request.getParameter(TEMP_NOME_USUARIO.getName()));
      String dados = retornaDados(TEMP_NOME_USUARIO.getValue(), false); 
      if (!dados.contains("name")){
        dados = retornaDados(TEMP_NOME_USUARIO.getValue(), true);   
      }// if
      if (dados.contains("name")) { 
        String nome = dados.substring(dados.indexOf("\"name\":\"") + 8, dados.indexOf(",") - 1);
        String picture = dados.substring(dados.indexOf("\"pic\":\"") + 7, dados.indexOf("}", 2) - 1);%>
        document.getElementById("picture").src = "<%=picture%>";
        document.getElementById("semUsuario").innerHTML = "";
        document.getElementById("linkFacebook").innerHTML = "Acessar Perfil";
        document.getElementById("linkFacebook").title = "Ir para o Facebook de <%=nome%>";
        document.getElementById("linkFacebook").href = "http://www.facebook.com/<%=TEMP_NOME_USUARIO.getValue()%>";
      <%}
      else if (TEMP_NOME_USUARIO.getValue().trim().isEmpty()) {%>
        document.getElementById("picture").src = "<%=contato.extension().getUrl()%>/images/logo_facebook.jpg?nocache";         
        document.getElementById("semUsuario").innerHTML = "";
        document.getElementById("linkFacebook").innerHTML = "Pesquisar";
        document.getElementById("linkFacebook").title = "Pesquisar Usuário";
        document.getElementById("linkFacebook").href = "javascript:pesquisarUsuario();";
        document.getElementById("linkFacebook").target = "";
      <%}
      else {%>
        document.getElementById("picture").src = "<%=contato.extension().getUrl()%>/images/logo_facebook.jpg?nocache";      
        document.getElementById("semUsuario").innerHTML = "Usuário não encontrado";
        document.getElementById("linkFacebook").innerHTML = "";
        document.getElementById("linkFacebook").title = "";
        document.getElementById("linkFacebook").href = "";        
      <% }
      // dispara
      return;
    }      
    // se quer o Cep...
    else if (Controller.getCommand(request).equals(COMMAND_CEP)) {
      // configura nossa resposta
      Ajax.setResponseTypeText(response);
      System.out.println(request.getParameter(PARAM_CEP));
      // obtém o CEP
      Contato.CepInfo cepInfo = contato.consultaCep(StringTools.removeNotNumbers(request.getParameter(PARAM_CEP)));
      // retorna o script para alterar os dados na tela
      if (!cepInfo.endereco.equals("")) {
      %>
        document.getElementById("<%=Contato.FIELD_ENDERECO.getFieldAlias()%>").value = "<%=cepInfo.endereco%>";
        document.getElementById("<%=Contato.FIELD_ENDERECO_NUMERO.getFieldAlias()%>").value = "";
        document.getElementById("<%=Contato.FIELD_ENDERECO_COMPLEMENTO.getFieldAlias()%>").value = "";
        document.getElementById("<%=Contato.FIELD_ENDERECO_BAIRRO.getFieldAlias()%>").value = "<%=cepInfo.bairro%>";
        document.getElementById("<%=Contato.FIELD_CIDADE.getFieldAlias()%>").value = "<%=cepInfo.municipio%>";
        document.getElementById("<%=Contato.FIELD_CODIGO_MUNICIPIO.getFieldAlias()%>").value = "<%=cepInfo.codigoMunicipio%>";
        document.getElementById("<%=Contato.FIELD_UF.getFieldAlias()%>").value = "<%=cepInfo.uf%>";
      <%
      }
      else {
      %>
        document.getElementById("<%=Contato.FIELD_ENDERECO.getFieldAlias()%>").value = "";
        document.getElementById("<%=Contato.FIELD_ENDERECO_NUMERO.getFieldAlias()%>").value = "";
        document.getElementById("<%=Contato.FIELD_ENDERECO_COMPLEMENTO.getFieldAlias()%>").value = "";
        document.getElementById("<%=Contato.FIELD_ENDERECO_BAIRRO.getFieldAlias()%>").value = "";
        document.getElementById("<%=Contato.FIELD_CIDADE.getFieldAlias()%>").value = "";
        document.getElementById("<%=Contato.FIELD_CODIGO_MUNICIPIO.getFieldAlias()%>").value = "";
        document.getElementById("<%=Contato.FIELD_UF.getFieldAlias()%>").value = "";
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
    FrameBar frameBar = new FrameBar(facade, "frameBarContato");
    // nosso Form de dados
    Form formData = new Form(request, "formDataContato", Contato.ACTION_CADASTRO, Contato.COMMAND_SAVE, "", true, true);
    // nosso PageControl
    PageControl pageControl = new PageControl(facade, "pageControlContato", true);

    // pode alterar o nome?
    boolean canChangeNome = Controller.isInserting(request) || facade.getLoggedUser().hasAccessRight(Contato.ACTION, Contato.COMMAND_CHANGE_NOME);
%>

<html>
  <head>
    <title><%=Contato.ACTION.getCaption()%></title>
    <link href="<%=facade.getStyle().userInterface()%>" rel="stylesheet" type="text/css">
    <script src="include/scripts.js" type="text/javascript"></script>
  </head>
  <body style="margin:0px;" onselectstart="return true;" oncontextmenu="return false;" onload="showHideControls();">

    <script type="text/javascript">
      
      function pegaImagemFacebook() {
        var userName = document.getElementById('<%=Contato.FIELD_USUARIO_FACEBOOK.getFieldAlias()%>').value;
        // nossos parâmetros
        var params = "<%=TEMP_NOME_USUARIO.getName()%>=" + userName + "&nomeContato=" + document.getElementById('<%=Contato.FIELD_NOME.getFieldAlias()%>').value;
        // faz a requisição
        <%=ajaxFacebook.request(new String[]{"params"})%>
        // se ocorreu tudo OK...
        if (<%=ajaxFacebook.isResponseStatusOK()%>) {
          // executa o script retornado
          eval(<%=ajaxFacebook.responseText()%>);
        }
        // se ocorreu erro...
        else {
          alert(<%=ajaxFacebook.responseErrorMessage()%>);
          return;
        } // if
      }      
      
      function pesquisarUsuario() {
        var url = "https://www.facebook.com/search/results.php?q=" + removeAccents(document.getElementById('<%=Contato.FIELD_NOME.getFieldAlias()%>', true).value);
        var name = "paginaFacebook";
        var windowHeight= screen.availHeight;
        var windowWidht =  screen.availWidth;
        var windowTop=(screen.availHeight-windowHeight)/2; 
        var windowLeft=(screen.availWidth-windowWidht)/2; 
        window.open(url, name, 'top=' + windowTop + ', left=' + windowLeft + ', width=' + windowWidht + ', height=' + windowHeight + 'location=yes, toolbar=yes, status=yes, menubar=yes, resizable=yes, scrollbars=yes');        
      }

      function ajaxConsultaCep() {
        // nosso CEP
        var cep = document.getElementById("<%=Contato.FIELD_CEP.getFieldAlias()%>");
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

      function relatorioHistoricoContato() {
        // contatoId
        var contatoId = document.getElementById("<%=Contato.FIELD_CONTATO_ID.getFieldAlias()%>").value;
        // chama o relatório
        <%=RelatorioHistoricoContato.ACTION.url(RelatorioHistoricoContato.COMMAND_MAKE_REPORT, relatorioHistoricoContato.USER_PARAM_CONTATO_ID.getName() + "=' + contatoId + '")%>
      }

      function showHideControls() {
        // nossos elementos
        var tipoPessoa                      = document.getElementById("<%=contato.FIELD_TIPO_PESSOA.getFieldAlias()%>");
        var vendedor                        = document.getElementById("<%=contato.FIELD_VENDEDOR.getFieldAlias()%>");
        var cliente                         = document.getElementById("<%=contato.FIELD_CLIENTE.getFieldAlias()%>");
        var labelPercentualComissao         = document.getElementById("label<%=contato.FIELD_PERCENTUAL_COMISSAO.getFieldAlias()%>");
        var labelPercentualIndicacao        = document.getElementById("label<%=contato.FIELD_PERCENTUAL_INDICACAO.getFieldAlias()%>");
        var percentualComissao              = document.getElementById("<%=contato.FIELD_PERCENTUAL_COMISSAO.getFieldAlias()%>");
        var percentualIndicacao             = document.getElementById("<%=contato.FIELD_PERCENTUAL_INDICACAO.getFieldAlias()%>");
        var labelPercentualComissaoServico  = document.getElementById("label<%=contato.FIELD_PERCENTUAL_COMISSAO_SERVICO.getFieldAlias()%>");
        var labelPercentualIndicacaoServico = document.getElementById("label<%=contato.FIELD_PERCENTUAL_INDICACAO_SERVICO.getFieldAlias()%>");
        var percentualComissaoServico       = document.getElementById("<%=contato.FIELD_PERCENTUAL_COMISSAO_SERVICO.getFieldAlias()%>");
        var percentualIndicacaoServico      = document.getElementById("<%=contato.FIELD_PERCENTUAL_INDICACAO_SERVICO.getFieldAlias()%>");        
        var tablePessoaFisica               = document.getElementById("tablePessoaFisica");
        var tablePessoaJuridica             = document.getElementById("tablePessoaJuridica");
        // nossas variáveis
        var pessoaFisica = tipoPessoa.options[tipoPessoa.selectedIndex].value == <%=TipoPessoa.FISICA%>;
        var vendedorSim = vendedor.options[vendedor.selectedIndex].value == <%=NaoSim.SIM%>;
        var clienteSim = cliente.options[cliente.selectedIndex].value == <%=NaoSim.SIM%>;
        // mostra ou oculta os controles
        tablePessoaFisica.style.display   = (pessoaFisica ? "block" : "none");
        tablePessoaJuridica.style.display = (!pessoaFisica ? "block" : "none");
        // *
        labelPercentualComissao.disabled  = !vendedorSim;
        percentualComissao.readOnly       = !vendedorSim;
        // *
        labelPercentualIndicacao.disabled  = !clienteSim;
        percentualIndicacao.readOnly       = !clienteSim;
        // *
        labelPercentualComissaoServico.disabled  = !vendedorSim;
        percentualComissaoServico.readOnly       = !vendedorSim;
        // *
        labelPercentualIndicacaoServico.disabled  = !clienteSim;
        percentualIndicacaoServico.readOnly       = !clienteSim;
        
        if (vendedorSim)
          <%=pageControl.showTabSheet(1)%>
        else
          <%=pageControl.hideTabSheet(1)%>
      }

      function linkIBGE() {
        <%--var cidade = document.getElementById("<%=contato.FIELD_CIDADE.getFieldAlias()%>");
        http://www.ibge.gov.br/home/geociencias/areaterritorial/area.php?nome=jaboatao%20dos%20guararapes&codigo=&submit.x=0&submit.y=0--%>
        // link do IBGE
        window.open('http://www.ibge.gov.br/home/geociencias/areaterritorial/area.shtm', 'CodigoIBGE', 'resizable=yes,scrollbars=yes,status=no');
      }

    </script>

    <!-- inicia o FrameBar -->
    <%=frameBar.begin()%>

      <!-- área de frames -->
      <%=frameBar.beginFrameArea()%>

        <!-- Frame de identificação do objeto -->
        <%=frameBar.actionFrame(Contato.ACTION)%>

        <!-- Frame de comandos -->
        <%=frameBar.beginFrame("Comandos", false, false)%>
          <table style="width:100%;">
            <tr>
              <td><%=CommandControl.formHyperlink(facade, formData, ImageList.COMMAND_SAVE, "Deseja mesmo salvar o registro?", true, false)%></td>
            </tr>
            <tr>
              <td><%=CommandControl.entityFormHyperlink(facade, contato, Contato.ACTION_CADASTRO, Contato.COMMAND_INSERT, ImageList.COMMAND_INSERT, false)%></td>
            </tr>
            <tr>
              <td><%=CommandControl.actionCustomScriptHyperlink(facade, RelatorioHistoricoContato.ACTION, RelatorioHistoricoContato.COMMAND_MAKE_REPORT, ImageList.IMAGE_REPORT, "javascript:void(0);", "", "relatorioHistoricoContato()", Controller.isInserting(request))%></td>
            </tr>
            <tr>
              <td><%=CommandControl.entityBrowseHyperlink(facade, contato, Contato.ACTION, ImageList.COMMAND_BACK, "Voltar", "Volta para a consulta.", false)%></td>
            </tr>
          </table>
        <%=frameBar.endFrame()%>
      <%=frameBar.endFrameArea()%>

      <!-- Form de dados -->
      <%=formData.begin()%>

        <!-- área de dados -->
        <%=frameBar.beginClientArea()%>
          <!-- dados ocultos -->
          <%=EntityFormEdit.script(facade, Contato.FIELD_CONTATO_ID, contatoInfo, request, -1, "", "")%>
          <%=EntityFormEdit.script(facade, Contato.FIELD_USUARIO_INCLUSAO_ID, contatoInfo, request, -1, "", "")%>
          <%=EntityFormEdit.script(facade, Contato.FIELD_DATA_INCLUSAO, contatoInfo, request, -1, "", "")%>
          <%=EntityFormEdit.script(facade, Contato.FIELD_USUARIO_ALTERACAO_ID, contatoInfo, request, -1, "", "")%>
          <%=EntityFormEdit.script(facade, Contato.FIELD_DATA_HORA_ALTERACAO, contatoInfo, request, -1, "", "")%>
          <%=EntityFormEdit.script(facade, Contato.FIELD_USUARIO_CARTEIRA_ID, contatoInfo, request, -1, "", "")%>
          <%=EntityFormEdit.script(facade, Contato.FIELD_DATA_HORA_CARTEIRA, contatoInfo, request, -1, "", "")%>
          <!-- dados de Contato -->
          <table cellpadding="0" cellspacing="0" style="width:100%; height:100%;">
            <tr>
              <td style="height:40px;" colspan="2">

                <!-- campos Nome, Tipo Pessoa e Personalidades -->
                <%=GroupBox.begin("Identificação")%>
                  <table style="width:100%;">
                    <tr>
                      <td style="width:auto;"><%=EntityFieldLabel.script(facade, Contato.FIELD_NOME, request, "", "", !canChangeNome)%></td>
                      <td style="width:150px;"><%=EntityLookupLabel.script(facade, Contato.LOOKUP_GRUPO_CONTATO, request, "linkGrupoContato", "", GrupoContato.ACTION, false)%></td>
                      <td style="width:80px;"><%=EntityFieldLabel.script(facade, Contato.FIELD_TIPO_PESSOA, request)%></td>
                      <td style="width:70px;"><%=EntityFieldLabel.script(facade, Contato.FIELD_CLIENTE, request)%></td>
                      <td style="width:70px;"><%=EntityFieldLabel.script(facade, Contato.FIELD_FORNECEDOR, request)%></td>
                      <td style="width:70px;"><%=EntityFieldLabel.script(facade, Contato.FIELD_FUNCIONARIO, request)%></td>
                      <td style="width:70px;"><%=EntityFieldLabel.script(facade, Contato.FIELD_VENDEDOR, request)%></td>
                      <td style="width:70px;"><%=EntityFieldLabel.script(facade, Contato.FIELD_TRANSPORTADOR, request)%></td>
                    </tr>
                    <tr>
                      <td><%=EntityFormEdit.script(facade, Contato.FIELD_NOME, contatoInfo, request, 0, "", "", !canChangeNome, false)%></td>
                      <td><%=EntityFormLookupSelectEx.script(facade, Contato.CLASS_NAME, Contato.LOOKUP_GRUPO_CONTATO, GrupoContato.LEVEL_DELIMITER, contatoInfo, request, 0, "", "", true)%></td>
                      <td><%=EntityFormSelect.script(facade, Contato.FIELD_TIPO_PESSOA, contatoInfo, request, 0, "", "showHideControls();", false)%></td>
                      <td><%=EntityFormSelect.script(facade, Contato.FIELD_CLIENTE, contatoInfo, request, 0, "", "showHideControls();", false)%></td>
                      <td><%=EntityFormSelect.script(facade, Contato.FIELD_FORNECEDOR, contatoInfo, request, 0, "", "", false)%></td>
                      <td><%=EntityFormSelect.script(facade, Contato.FIELD_FUNCIONARIO, contatoInfo, request, 0, "", "", false)%></td>
                      <td><%=EntityFormSelect.script(facade, Contato.FIELD_VENDEDOR, contatoInfo, request, 0, "", "showHideControls();", false)%></td>
                      <td><%=EntityFormSelect.script(facade, Contato.FIELD_TRANSPORTADOR, contatoInfo, request, 0, "", "", false)%></td>
                    </tr>
                  </table>
                <%=GroupBox.end()%>

              </td>
            </tr>
            <tr>
              <td style="height:40px; width: auto">

                  <table style="width:100%;">
                    <tr>
                      <td style="width:480px;">
                        <%=GroupBox.begin("Comissões")%>
                          <!-- campos Grupo Contato e Percentual Comissão -->
                          <table style="width:100%;">
                            <tr>
                              <td style="width:120px;"><%=EntityFieldLabel.script(facade, Contato.FIELD_PERCENTUAL_COMISSAO, request, "label" + Contato.FIELD_PERCENTUAL_COMISSAO.getFieldAlias(), "")%></td>
                              <td style="width:120px;"><%=EntityFieldLabel.script(facade, Contato.FIELD_PERCENTUAL_COMISSAO_SERVICO, request, "label" + Contato.FIELD_PERCENTUAL_COMISSAO_SERVICO.getFieldAlias(), "")%></td>
                              <td style="width:120px;"><%=EntityFieldLabel.script(facade, Contato.FIELD_PERCENTUAL_INDICACAO, request, "label" + Contato.FIELD_PERCENTUAL_INDICACAO.getFieldAlias(), "")%></td>
                              <td style="width:120px;"><%=EntityFieldLabel.script(facade, Contato.FIELD_PERCENTUAL_INDICACAO_SERVICO, request, "label" + Contato.FIELD_PERCENTUAL_INDICACAO_SERVICO.getFieldAlias(), "")%></td>
                            </tr>
                            <tr>
                              <td><%=EntityFormEdit.script(facade, Contato.FIELD_PERCENTUAL_COMISSAO, contatoInfo, request, 0, "", "")%></td>
                              <td><%=EntityFormEdit.script(facade, Contato.FIELD_PERCENTUAL_COMISSAO_SERVICO, contatoInfo, request, 0, "", "")%></td>
                              <td><%=EntityFormEdit.script(facade, Contato.FIELD_PERCENTUAL_INDICACAO, contatoInfo, request, 0, "", "")%></td>
                              <td><%=EntityFormEdit.script(facade, Contato.FIELD_PERCENTUAL_INDICACAO_SERVICO, contatoInfo, request, 0, "", "")%></td>
                            </tr>
                          </table>
                        <%=GroupBox.end()%>
                      </td>
                      <td style="width:auto;">
                        
                        <%=GroupBox.begin("Dados Cadastrais")%>
                          <!-- campos CPF, RG, Data Nascimento e Sexo -->
                          <table id="tablePessoaFisica" style="display:none;">
                            <tr>
                              <td style="width:90px;"><%=EntityFieldLabel.script(facade, Contato.FIELD_CPF, request)%></td>
                              <td style="width:80px;"><%=EntityFieldLabel.script(facade, Contato.FIELD_RG, request)%></td>
                              <%// verifica se extensão está carregada
                                  if(facade.extensionLoaded("imanager_faceimagem")) {%>
                                <td style="width:100px;"><%=EntityFieldLabel.script(facade, Contato.FIELD_CRO, request)%></td>
                              <%}%>                              
                              <td style="width:100px;"><%=EntityFieldLabel.script(facade, Contato.FIELD_DATA_NASCIMENTO, request)%></td>
                              <td style="width:100px;"><%=EntityFieldLabel.script(facade, Contato.FIELD_SEXO, request)%></td>
                            </tr>
                            <tr>
                              <td><%=EntityFormEdit.script(facade, Contato.FIELD_CPF, contatoInfo, request, 0, "", "")%></td>
                              <td><%=EntityFormEdit.script(facade, Contato.FIELD_RG, contatoInfo, request, 0, "", "")%></td>
                              <%// verifica se extensão está carregada
                                  if(facade.extensionLoaded("imanager_faceimagem")) {%>
                                <td><%=EntityFormEdit.script(facade, Contato.FIELD_CRO, contatoInfo, request, 0, "", "")%></td>
                              <%}%>                                                            
                              <td><%=EntityFormEdit.script(facade, Contato.FIELD_DATA_NASCIMENTO, contatoInfo, request, 0, "", "")%></td>
                              <td><%=EntityFormSelect.script(facade, Contato.FIELD_SEXO, contatoInfo, request, 0, "", "", false)%></td>
                            </tr>
                          </table>
                          <!-- campos Razão Social, CNPJ, Inscrições -->
                          <table id="tablePessoaJuridica" style="display:none; width:100%;">
                            <tr>
                              <td style="width:auto;"><%=EntityFieldLabel.script(facade, Contato.FIELD_RAZAO_SOCIAL, request)%></td>
                              <td style="width:110px;"><%=EntityFieldLabel.script(facade, Contato.FIELD_CNPJ, request)%></td>
                              <td style="width:95px;"><%=EntityFieldLabel.script(facade, Contato.FIELD_INSCRICAO_ESTADUAL, request)%></td>
                              <td style="width:95px;"><%=EntityFieldLabel.script(facade, Contato.FIELD_INSCRICAO_MUNICIPAL, request)%></td>
                            </tr>
                            <tr>
                              <td><%=EntityFormEdit.script(facade, Contato.FIELD_RAZAO_SOCIAL, contatoInfo, request, 0, "", "")%></td>
                              <td><%=EntityFormEdit.script(facade, Contato.FIELD_CNPJ, contatoInfo, request, 0, "", "")%></td>
                              <td><%=EntityFormEdit.script(facade, Contato.FIELD_INSCRICAO_ESTADUAL, contatoInfo, request, 0, "", "")%></td>
                              <td><%=EntityFormEdit.script(facade, Contato.FIELD_INSCRICAO_MUNICIPAL, contatoInfo, request, 0, "", "")%></td>
                            </tr>
                          </table>
                        <%=GroupBox.end()%>
                      </td>
                    </tr>
                  </table>


              </td>
              <td style="width: 150px; height: 200px; padding-top: 3px" rowspan="3">
                <!-- campo Facebook -->
                <%=GroupBox.begin("Facebook")%>
                  <table style="width:100%; height: 200px">
                    <tr>
                      <td style="height: 10px" align="center"><span id="semUsuario"><%=contatoInfo.getUsuarioFacebook().trim().isEmpty() ? "Pesquisar" : "Usuário não encontrado"%></span><a id="linkFacebook" target="__blank" href="javascript: void(0);"></a></td>
                    </tr>
                    <tr>
                      <td style="height: 170px;" align="center"><img id="picture" src="" /></td>
                    </tr>
                    <tr>
                      <td colspan="2" style="height: 10px"><%=EntityFieldLabel.script(facade, Contato.FIELD_USUARIO_FACEBOOK, request)%></td>
                    </tr>                    
                    <tr>
                      <td colspan="2" style="height: 10px"><%=EntityFormEdit.script(facade, Contato.FIELD_USUARIO_FACEBOOK, contatoInfo, request, 0, "", "pegaImagemFacebook();")%></td>
                    </tr>                                        
                  </table>
                <%=GroupBox.end()%>
              </td>              
            </tr>
            <tr>
              <td style="height:80px; width: auto">

                <!-- campos CEP, Endereço, Número, Complemento, Bairro, Cidade, CEP, Telefones, Emails -->
                <%=GroupBox.begin("Endereço")%>
                  <table style="width:100%;">
                    <tr style="width:100%;">
                      <td style="width: 60px;"><%=EntityFieldLabel.script(facade, Contato.FIELD_CEP, request)%></td>
                      <td style="width: auto;"><%=EntityFieldLabel.script(facade, Contato.FIELD_ENDERECO, request)%></td>
                      <td style="width: 60px;"><%=EntityFieldLabel.script(facade, Contato.FIELD_ENDERECO_NUMERO, request)%></td>
                      <td style="width: 100px;"><%=EntityFieldLabel.script(facade, Contato.FIELD_ENDERECO_COMPLEMENTO, request)%></td>
                      <td style="width: 150px;"><%=EntityFieldLabel.script(facade, Contato.FIELD_ENDERECO_BAIRRO, request)%></td>
                      <td style="width: 150px;"><%=EntityFieldLabel.script(facade, Contato.FIELD_CIDADE, request)%></td>
                    </tr>
                    <tr style="width:100%;">
                      <td style="width: 60px;"><%=EntityFormEdit.script(facade, Contato.FIELD_CEP, contatoInfo, request, 0, "", "ajaxConsultaCep();")%></td>
                      <td style="width: auto;"><%=EntityFormEdit.script(facade, Contato.FIELD_ENDERECO, contatoInfo, request, 0, "", "")%></td>
                      <td style="width: 60px;"><%=EntityFormEdit.script(facade, Contato.FIELD_ENDERECO_NUMERO, contatoInfo, request, 0, "", "")%></td>
                      <td style="width: 100px;"><%=EntityFormEdit.script(facade, Contato.FIELD_ENDERECO_COMPLEMENTO, contatoInfo, request, 0, "", "")%></td>
                      <td style="width: 150px;"><%=EntityFormEdit.script(facade, Contato.FIELD_ENDERECO_BAIRRO, contatoInfo, request, 0, "", "")%></td>
                      <td style="width: 150px;"><%=EntityFormEdit.script(facade, Contato.FIELD_CIDADE, contatoInfo, request, 0, "", "")%></td>
                    </tr>
                  </table>
                  <table style="width:100%;">
                    <tr style="width:100%;">
                      <td style="width:75px;"><a href="#" onclick="linkIBGE()" <%=EntityFieldLabel.script(facade, Contato.FIELD_CODIGO_MUNICIPIO, request)%></a></td>
                      <td style="width:25px;"><%=EntityFieldLabel.script(facade, Contato.FIELD_UF, request)%></td>                      
                      <td style="width:90px;"><%=EntityFieldLabel.script(facade, Contato.FIELD_TELEFONE_RESIDENCIAL, request)%></td>
                      <td style="width:90px;"><%=EntityFieldLabel.script(facade, Contato.FIELD_TELEFONE_CELULAR, request)%></td>
                      <td style="width:90px;"><%=EntityFieldLabel.script(facade, Contato.FIELD_TELEFONE_TRABALHO, request)%></td>
                      <td style="width:auto;"><%=EntityFieldLabel.script(facade, Contato.FIELD_EMAIL, request)%></td>
                    </tr>
                    <tr style="width:100%;">
                      <td style="width:75px;"><%=EntityFormEdit.script(facade, Contato.FIELD_CODIGO_MUNICIPIO, contatoInfo, request, 0, "", "")%></td>
                      <td style="width:25px;"><%=EntityFormEdit.script(facade, Contato.FIELD_UF, contatoInfo, request, 0, "", "")%></td>                      
                      <td style="width:90px;"><%=EntityFormEdit.script(facade, Contato.FIELD_TELEFONE_RESIDENCIAL, contatoInfo, request, 0, "", "")%></td>
                      <td style="width:90px;"><%=EntityFormEdit.script(facade, Contato.FIELD_TELEFONE_CELULAR, contatoInfo, request, 0, "", "")%></td>
                      <td style="width:90px;"><%=EntityFormEdit.script(facade, Contato.FIELD_TELEFONE_TRABALHO, contatoInfo, request, 0, "", "")%></td>
                      <td style="width:auto;"><%=EntityFormEdit.script(facade, Contato.FIELD_EMAIL, contatoInfo, request, 0, "", "")%></td>
                    </tr>
                  </table>
                <%=GroupBox.end()%>

              </td>
            </tr>            
            <tr>
              <td style="height:40px;">

                <!-- campos Arquivo e Usuários e Datas -->
                <%=GroupBox.begin("Segurança")%>
                  <table style="width:100%;">
                    <tr style="width:100%;">
                      <td style="width:110px;"><%=EntityFieldLabel.script(facade, Contato.FIELD_ARQUIVO, request)%></td>
                      <td style="width:auto;"><%=EntityFieldLabel.script(facade, Contato.FIELD_USUARIO_INCLUSAO_ID, request)%></td>
                      <td style="width:auto;"><%=EntityFieldLabel.script(facade, Contato.FIELD_USUARIO_ALTERACAO_ID, request)%></td>
                      <td style="width:auto;"><%=EntityFieldLabel.script(facade, Contato.FIELD_USUARIO_CARTEIRA_ID, request)%></td>
                    </tr>
                    <tr style="width:100%;">
                      <td style="width:110px;"><%=EntityFormSelect.script(facade, Contato.FIELD_ARQUIVO, contatoInfo, request, 0, "", "", false)%></td>
                      <td style="width:auto;"><%=FormEdit.script(facade, "inclusao", (contatoInfo.getUsuarioInclusaoId() > 0 ? facade.securityService().getUser(contatoInfo.getUsuarioInclusaoId()).getName() : "") + " - " + DateTools.formatDate(contatoInfo.getDataInclusao()), 0, 0, FormEdit.ALIGN_LEFT, "", "", "", "", "", true)%></td>
                      <td style="width:auto;"><%=FormEdit.script(facade, "alteracao", (contatoInfo.getUsuarioAlteracaoId() > 0 ? facade.securityService().getUser(contatoInfo.getUsuarioAlteracaoId()).getName() : "") + " - " + DateTools.formatDateTime(contatoInfo.getDataHoraAlteracao()), 0, 0, FormEdit.ALIGN_LEFT, "", "", "", "", "", true)%></td>
                      <td style="width:auto;"><%=FormEdit.script(facade, "carteira", (contatoInfo.getUsuarioCarteiraId() > 0 ? facade.securityService().getUser(contatoInfo.getUsuarioCarteiraId()).getName() : "") + " - " + DateTools.formatDateTime(contatoInfo.getDataHoraCarteira()), 0, 0, FormEdit.ALIGN_LEFT, "", "", "", "", "", true)%></td>
                    </tr>
                  </table>
                <%=GroupBox.end()%>

              </td>
            </tr>
            <tr>
              <td style="height:6px;" colspan="2"></td>
            </tr>
            <tr>
              <td style="height:auto;" colspan="2">

                <%=pageControl.begin()%>
                  <%=pageControl.beginTabSheet("Anotações")%>
                    <%=EntityFormMemo.script(facade, Contato.FIELD_ANOTACOES, contatoInfo, request, 0, 0, "", "", false, false)%>
                  <%=pageControl.endTabSheet()%>

                  <%if (Controller.isInserting(request)) {%>
                    <%=pageControl.beginTabSheet("Metas")%>
                      Não disponível durante a inclusão.
                    <%=pageControl.endTabSheet()%>
                  <%}
                    else {%>
                    <%=pageControl.iFrameTabSheet("Metas", ContatoMeta.ACTION.url(contatoMeta.USER_PARAM_CONTATO_ID.getName() + "=" + contatoInfo.getContatoId()))%>
                  <%} // if%>

                <%=pageControl.end()%>

              </td>
            </tr>
          </table>
          
                <script type="text/javascript">
                    pegaImagemFacebook();
                </script>

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
