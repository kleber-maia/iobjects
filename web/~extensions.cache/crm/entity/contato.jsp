
<%@include file="../include/beans.jsp"%>

<%@page import="securityservice.ui.entity.*"%>
<%@page import="imanager.entity.*"%>
<%@page import="imanager.process.*"%>
<%@page import="imanager.ui.entity.*"%>

<%!
  public class EntityGridContatoEventListener implements EntityGrid.EventListener {

    private Contato    contato = null;
    private Timestamp today = new Timestamp(DateTools.getActualDate().getTime());

    public EntityGridContatoEventListener(Contato contato) {
      this.contato = contato; 
    }

    public String onAddCell(EntityInfo   entityInfo,
                            EntityField  entityField,
                            EntityLookup entityLookup,
                            String       value) {
      // se é a coluna Categoria...
      if (entityLookup == Contato.LOOKUP_GRUPO_CONTATO) {
        int index = value.lastIndexOf((GrupoContato.LEVEL_DELIMITER));
        return index >= 0 ? value.substring(index+1) : value;
      }
      // não temos o que fazer
      else
        return value;
    }
  }

  public ContatoInfo[] search(Contato contato, StringBuffer searchErrorMessage) {
    try {
      // resultado da pesquisa...
      ContatoInfo[] result = contato.selectByFilter(contato.USER_PARAM_NOME.getValue(),
                                                    NumberTools.parseInt(contato.USER_PARAM_GRUPO_CONTATO_ID.getValue()),
                                                    NumberTools.parseInt(contato.USER_PARAM_TIPO_PESSOA.getValue()),
                                                    NumberTools.parseInt(contato.USER_PARAM_PERSONALIDADE.getValue()),
                                                    contato.USER_PARAM_TELEFONE.getValue(),
                                                    contato.USER_PARAM_EMAIL.getValue(),
                                                    NumberTools.parseInt(contato.USER_PARAM_USUARIO_CARTEIRA.getValue()),
                                                    NumberTools.parseInt(contato.USER_PARAM_MES_NASCIMENTO.getValue()),
                                                    NumberTools.parseInt(contato.USER_PARAM_MES_CADASTRO.getValue()),
                                                    NumberTools.parseInt(contato.USER_PARAM_SEXO.getValue()),
                                                    contato.USER_PARAM_CPF.getValue(),
                                                    contato.USER_PARAM_CNPJ.getValue(),
                                                    contato.USER_PARAM_BAIRRO.getValue(),
                                                    contato.USER_PARAM_CIDADE.getValue(),
                                                    contato.USER_PARAM_UF.getValue(),
                                                    NumberTools.parseInt(contato.USER_PARAM_ARQUIVO.getValue()));
      // reaprovieta os dados dos contatos
      ContatoProducer contatoProducer = ContatoProducer.getInstance(contato.getFacade());
      contatoProducer.addCollection(result);
      // retorna
      return result;
    }
    catch (Exception e) {
      searchErrorMessage.append(ExtendedException.extractMessage(e));
      return new ContatoInfo[0];
    } // try-catch
  }
%>

<%
  // início do bloco protegido
  try {
    // nossa instância de Contato
    Contato contato = (Contato)facade.getEntity(Contato.CLASS_NAME);
    // nossa instância de Alteração Carteira
    AlteracaoCarteira alteracaoCarteira = (AlteracaoCarteira)facade.getProcess(AlteracaoCarteira.CLASS_NAME);

    // lista que iremos exibir
    ContatoInfo[] contatoInfoList = new ContatoInfo[0];
    // mensagem de erro na pesquisa
    StringBuffer searchErrorMessage = new StringBuffer();

    // se estamos excluindo...
    if (Controller.isDeleting(request)) {
      // registros selecionados
      ContatoInfo[] selectedInfoList = (ContatoInfo[])EntityGrid.selectedInfoList(contato, request);
      // compõe a resposta
      Form.composeAjaxResponse(response, Form.COMPOSE_TYPE_JAVA_SCRIPT);
      // nosso Grid
      EntityGrid entityGrid = EntityGrid.getInstance(facade, contato);
      // exclui e apaga do Grid
      for (int i=0; i<selectedInfoList.length; i++) {
        contato.delete(selectedInfoList[i]);%>
        <%=entityGrid.deleteRow(selectedInfoList[i])%>
      <%} // for
      // dispara
      return;
    }
    // se estamos pesquisando...
    else if (Controller.isSearching(request)) {
      // define os valores dos parâmetros de usuário
      contato.userParamList().setParamsValues(request);
      // compõe a resposta
      Form.composeAjaxResponse(response, Form.COMPOSE_TYPE_JAVA_SCRIPT);
      // atualiza tudo
      contatoInfoList = search(contato, searchErrorMessage);
      %><%=EntityGrid.getInstance(facade, contato).update(contatoInfoList, "", searchErrorMessage.toString())%><%
      // dispara
      return;
    } // if

    // define os valores dos parâmetros de usuário
    contato.userParamList().setParamsValues(request);
    // realiza a pesquisa
    contatoInfoList = search(contato, searchErrorMessage);

    // nosso Form de pesquisa
    Form formSearch = new Form(request, "formSearchContato", Contato.ACTION, Contato.COMMAND_SEARCH, "", true, true);
    // nosso Form de exclusão
    Form formDelete = new Form(request, "formDeleteContato", Contato.ACTION, Contato.COMMAND_DELETE, "", false, true, true);
    // nosso FrameBar
    FrameBar frameBar = new FrameBar(facade, "frameBarContato");
    // nosso Grid
    EntityGrid entityGrid = EntityGrid.getInstance(facade, contato, 0, 0);
    entityGrid.addEventListener(new EntityGridContatoEventListener(contato));
    entityGrid.addColumn(Contato.FIELD_NOME, 250, Contato.ACTION_CADASTRO, Contato.COMMAND_EDIT, "", true);
    entityGrid.addColumn(Contato.LOOKUP_GRUPO_CONTATO, 150);
    entityGrid.addColumn(Contato.FIELD_TELEFONE_RESIDENCIAL, 90);
    entityGrid.addColumn(Contato.FIELD_TELEFONE_CELULAR, 90);
    entityGrid.addColumn(Contato.FIELD_TELEFONE_TRABALHO, 90);
    entityGrid.addColumn(Contato.FIELD_EMAIL, 250);
    entityGrid.addColumn(Contato.LOOKUP_USUARIO_CARTEIRA, 150);
%>

<html>
  <head>
    <title><%=Contato.ACTION.getCaption()%></title>
    <link href="<%=facade.getStyle().userInterface()%>" rel="stylesheet" type="text/css">
    <script src="include/scripts.js" type="text/javascript"></script>
  </head>
  <body style="margin:0px;" onselectstart="return true;" oncontextmenu="return false;">

    <script type="text/javascript">
      function alterarCarteira() {
        // lita de produtos selecionados
        var keyValues = <%=entityGrid.selectedKeyValues()%>;
        // se não temos nada selecionado...dispara
        if (keyValues.length == 0) {
          alert("Nenhum Contato selecionado.");
          return;
        } // if
        // lista de parâmetros
        var params = "";
        for (var i=0; i<keyValues.length; i++)
          params += (params != "" ? "&" : "") + "<%=alteracaoCarteira.CONTROL_PARAM_CONTATO_ID.getName()%>=" + keyValues[i][0];
        // chama o processo
        <%=alteracaoCarteira.ACTION.url(AlteracaoCarteira.COMMAND_ALTERAR, "' + params + '")%>;
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
              <td><%=CommandControl.entityFormHyperlink(facade, contato, Contato.ACTION_CADASTRO, Contato.COMMAND_INSERT, ImageList.COMMAND_INSERT, false)%></td>
            </tr>
            <tr>
              <td><%=CommandControl.formHyperlink(facade, formDelete, ImageList.COMMAND_DELETE, "Deseja mesmo excluir o(s) registro(s) selecionado(s)?", true, false)%></td>
            </tr>
            <tr>
              <td><%=CommandControl.actionCustomScriptHyperlink(facade, AlteracaoCarteira.ACTION, AlteracaoCarteira.COMMAND_ALTERAR, ImageList.IMAGE_PROCESS, "javascript:void(0);", "", "alterarCarteira()", false)%></td>
            </tr>
            <%=frameBar.showMoreRow(false)%>
            <tr>
              <td><%=CommandControl.actionHyperlink(facade, MalaDireta.ACTION, MalaDireta.COMMAND_USE_PRODUCER, ImageList.IMAGE_MAIL, false)%></td>
            </tr>
            <tr>
              <td><%=CommandControl.actionHyperlink(facade, TracaRota.ACTION, TracaRota.COMMAND_USE_PRODUCER, ImageList.IMAGE_MAP, false)%></td>
            </tr>
          </table>
        <%=frameBar.endFrame()%>

        <!-- Frame de pesquisa -->
        <%=frameBar.beginFrame("Pesquisa", false, true)%>
          <!-- Form de pesquisa -->
          <%=formSearch.begin()%>
              <table style="width:100%;">
                <tr>
                  <td><%=ParamLabel.script(facade, contato.USER_PARAM_NOME)%></td>
                </tr>
                <tr>
                  <td><%=ParamFormEdit.script(facade, contato.USER_PARAM_NOME, 0, "", "")%></td>
                </tr>
                <tr>
                  <td><%=ParamLabel.script(facade, contato.USER_PARAM_GRUPO_CONTATO_ID)%></td>
                </tr>
                <tr>
                  <td><%=GrupoContatoUI.lookupSelectExForParam(facade, contato.USER_PARAM_GRUPO_CONTATO_ID, 0, "", "")%></td>
                </tr>
                <tr>
                  <td><%=ParamLabel.script(facade, contato.USER_PARAM_PERSONALIDADE)%></td>
                </tr>
                <tr>
                  <td><%=ParamFormSelect.script(facade, contato.USER_PARAM_PERSONALIDADE, 0, "", "")%></td>
                </tr>
                <tr>
                  <td><%=ParamLabel.script(facade, contato.USER_PARAM_USUARIO_CARTEIRA)%></td>
                </tr>
                <tr>
                  <td><%=UsuarioUI.lookupSearchForParam(facade, contato.USER_PARAM_USUARIO_CARTEIRA, 0, "", "", false)%></td>
                </tr>
                <%=frameBar.showMoreRow(true)%>
                <tr>
                  <td><%=ParamLabel.script(facade, contato.USER_PARAM_TIPO_PESSOA)%></td>
                </tr>
                <tr>
                  <td><%=ParamFormSelect.script(facade, contato.USER_PARAM_TIPO_PESSOA, 0, "", "")%></td>
                </tr>
                <tr>
                  <td><%=ParamLabel.script(facade, contato.USER_PARAM_MES_NASCIMENTO)%></td>
                </tr>
                <tr>
                  <td><%=ParamFormSelect.script(facade, contato.USER_PARAM_MES_NASCIMENTO, 0, "", "")%></td>
                </tr>
                <tr>
                  <td><%=ParamLabel.script(facade, contato.USER_PARAM_MES_CADASTRO)%></td>
                </tr>
                <tr>
                  <td><%=ParamFormSelect.script(facade, contato.USER_PARAM_MES_CADASTRO, 0, "", "")%></td>
                </tr>
                <tr>
                  <td><%=ParamLabel.script(facade, contato.USER_PARAM_SEXO)%></td>
                </tr>
                <tr>
                  <td><%=ParamFormSelect.script(facade, contato.USER_PARAM_SEXO, 0, "", "")%></td>
                </tr>
                <tr>
                  <td><%=ParamLabel.script(facade, contato.USER_PARAM_CPF)%></td>
                </tr>
                <tr>
                  <td><%=ParamFormEdit.script(facade, contato.USER_PARAM_CPF, 0, "", "")%></td>
                </tr>
                <tr>
                  <td><%=ParamLabel.script(facade, contato.USER_PARAM_CNPJ)%></td>
                </tr>
                <tr>
                  <td><%=ParamFormEdit.script(facade, contato.USER_PARAM_CNPJ, 0, "", "")%></td>
                </tr>
                <tr>
                  <td><%=ParamLabel.script(facade, contato.USER_PARAM_TELEFONE)%></td>
                </tr>
                <tr>
                  <td><%=ParamFormEdit.script(facade, contato.USER_PARAM_TELEFONE, 0, "", "")%></td>
                </tr>
                <tr>
                  <td><%=ParamLabel.script(facade, contato.USER_PARAM_EMAIL)%></td>
                </tr>
                <tr>
                  <td><%=ParamFormEdit.script(facade, contato.USER_PARAM_EMAIL, 0, "", "")%></td>
                </tr>
                <tr>
                  <td><%=ParamLabel.script(facade, contato.USER_PARAM_BAIRRO)%></td>
                </tr>
                <tr>
                  <td><%=ParamFormEdit.script(facade, contato.USER_PARAM_BAIRRO, 0, "", "")%></td>
                </tr>
                <tr>
                  <td><%=ParamLabel.script(facade, contato.USER_PARAM_CIDADE)%></td>
                </tr>
                <tr>
                  <td><%=ParamFormEdit.script(facade, contato.USER_PARAM_CIDADE, 0, "", "")%></td>
                </tr>
                <tr>
                  <td><%=ParamLabel.script(facade, contato.USER_PARAM_UF)%></td>
                </tr>
                <tr>
                  <td><%=ParamFormEdit.script(facade, contato.USER_PARAM_UF, 0, "", "")%></td>
                </tr>
                <tr>
                  <td><%=ParamLabel.script(facade, contato.USER_PARAM_ARQUIVO)%></td>
                </tr>
                <tr>
                  <td><%=ParamFormSelect.script(facade, contato.USER_PARAM_ARQUIVO, 0, "", "")%></td>
                </tr>
                <tr>
                  <td><%=CommandControl.formButton(facade, formSearch, ImageList.COMMAND_SEARCH, "", true, false)%></td>
                </tr>
              </table>
          <%=formSearch.end()%>
        <%=frameBar.endFrame()%>

      <%=frameBar.endFrameArea()%>

      <!-- área do Grid -->
      <%=frameBar.beginClientArea()%>
        <!-- Form de exclusão -->
        <%=formDelete.begin()%>

          <!-- Grid -->
          <%=entityGrid.script(contatoInfoList, "", searchErrorMessage.toString())%>

        <%=formDelete.end()%>
      <%=frameBar.endClientArea()%>

    <%=frameBar.end()%>


  </body>
</html>

<%}
  // término do bloco protegido
  catch (Exception e) {
    Controller.forwardException(e, pageContext);
  } // try-catch
%>
