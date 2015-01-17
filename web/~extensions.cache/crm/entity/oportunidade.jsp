     
<%@include file="../include/beans.jsp"%>

<%@page import="imanager.entity.*"%>
<%@page import="imanager.misc.*"%>
<%@page import="imanager.process.*"%>
<%@page import="imanager.ui.entity.*"%>
<%@page import="securityservice.ui.entity.*"%>

<%!
  public class EntityGridOportunidadeEventListener implements EntityGrid.EventListener {

    private Oportunidade oportunidade = null;

    public EntityGridOportunidadeEventListener(Oportunidade oportunidade) {
      this.oportunidade = oportunidade;
    }

    public String onAddCell(EntityInfo   entityInfo,
                            EntityField  entityField,
                            EntityLookup entityLookup,
                            String       value) {
      // se é a coluna Status...retorna a cor
      if (entityField == Oportunidade.FIELD_STATUS)
        return "<font style=color:" + StatusOportunidade.COLOR_LIST_FOR_FIELD[((OportunidadeInfo)entityInfo).getStatus()] + ">" + value + "</font>";
      // não temos o que fazer
      else
        return value;
    }
    
  }

  public OportunidadeInfo[] search(Oportunidade oportunidade, int empresaId, StringBuffer searchErrorMessage) {
    try {
      // resultado da pesquisa...
      OportunidadeInfo[] result = oportunidade.selectByFilter(
              empresaId,
              NumberTools.parseInt(oportunidade.USER_PARAM_OPORTUNIDADE_ID.getValue()),
              NumberTools.parseInt(oportunidade.USER_PARAM_DEPARTAMENTO_ID.getValue()),
              NumberTools.parseInt(oportunidade.USER_PARAM_USUARIO_ID.getValue()),
              NumberTools.parseInt(oportunidade.USER_PARAM_CAMPANHA_ID.getValue()),
              NumberTools.parseInt(oportunidade.USER_PARAM_CLIENTE_ID.getValue()),
              NumberTools.parseInt(oportunidade.USER_PARAM_FASE_ID.getValue()),
              NumberTools.parseInt(oportunidade.USER_PARAM_STATUS.getValue()),
              null);
      // reaprovieta os dados dos contatos
      ContatoProducer contatoProducer = ContatoProducer.getInstance(oportunidade.getFacade());
      contatoProducer.addCollection(result, Oportunidade.FIELD_CLIENTE_ID.getFieldAlias());  
      return result;
    }
    catch (Exception e) {
      searchErrorMessage.append(ExtendedException.extractMessage(e));
      return new OportunidadeInfo[0];
    } // try-catch
  }
%>

<%
  // início do bloco protegido
  try {
    // nossa instância de Oportunidade
    Oportunidade oportunidade = (Oportunidade)facade.getEntity(Oportunidade.CLASS_NAME);
    // nossa instância de Empresa
    Empresa empresa = (Empresa)facade.getEntity(Empresa.CLASS_NAME);

    // se a Empresa é apenas um Estoque...exceção
    if (empresa.isEstoque(selectedEmpresaId))
      throw new Exception("Operação não permitida em uma Empresa que é apenas estoque.");

    // lista que iremos exibir
    OportunidadeInfo[] oportunidadeInfoList = new OportunidadeInfo[0];
    // mensagem de erro na pesquisa
    StringBuffer searchErrorMessage = new StringBuffer();

    // se estamos excluindo...
    if (Controller.isDeleting(request)) {
      // registros selecionados
      OportunidadeInfo[] selectedInfoList = (OportunidadeInfo[])EntityGrid.selectedInfoList(oportunidade, request);
      // compõe a resposta
      Form.composeAjaxResponse(response, Form.COMPOSE_TYPE_JAVA_SCRIPT);
      // nosso Grid
      EntityGrid entityGrid = EntityGrid.getInstance(facade, oportunidade);
      // exclui e apaga do Grid
      for (int i=0; i<selectedInfoList.length; i++) {
        oportunidade.delete(selectedInfoList[i]);%>
        <%=entityGrid.deleteRow(selectedInfoList[i])%>
      <%} // for
      // dispara
      return;
    }
    // se estamos pesquisando...
    else if (Controller.isSearching(request)) {
      // define os valores dos parâmetros de usuário
      oportunidade.userParamList().setParamsValues(request);
      // compõe a resposta
      Form.composeAjaxResponse(response, Form.COMPOSE_TYPE_JAVA_SCRIPT);
      // atualiza tudo
      oportunidadeInfoList = search(oportunidade, selectedEmpresaId, searchErrorMessage);
      %><%=EntityGrid.getInstance(facade, oportunidade).update(oportunidadeInfoList, "", searchErrorMessage.toString())%><%
      // dispara
      return;
    } // if

    // estamos na central de atendimento?
    boolean isCentralAtendimento = request.getParameter(oportunidade.CONTROL_PARAM_CENTRAL_ATENDIMENTO.getName()) != null;
    // grava na sessão
    session.setAttribute(oportunidade.CONTROL_PARAM_CENTRAL_ATENDIMENTO.getName(), isCentralAtendimento);

    if (isCentralAtendimento)
      oportunidade.USER_PARAM_CLIENTE_ID.setValue("-1");
    else if (oportunidade.USER_PARAM_CLIENTE_ID.getIntValue() < 0)
      oportunidade.USER_PARAM_CLIENTE_ID.setValue("0");

    // define os valores dos parâmetros de usuário
    oportunidade.userParamList().setParamsValues(request);
    // realiza a pesquisa
    oportunidadeInfoList = search(oportunidade, selectedEmpresaId, searchErrorMessage);


    // nosso Form de pesquisa
    Form formSearch = new Form(request, "formSearchOportunidade", Oportunidade.ACTION, Oportunidade.COMMAND_SEARCH, "", true, true);
    // nosso Form de exclusão
    Form formDelete = new Form(request, "formDeleteOportunidade", Oportunidade.ACTION, Oportunidade.COMMAND_DELETE, "", false, true, true);
    // nosso FrameBar
    FrameBar frameBar = new FrameBar(facade, "frameBarOportunidade");
    // nosso Grid
    EntityGrid entityGrid = EntityGrid.getInstance(facade, oportunidade, 0, 0);
    entityGrid.setSelectType(EntityGrid.SELECT_TYPE_MULTIPLE);
    entityGrid.addEventListener(new EntityGridOportunidadeEventListener(oportunidade));
    entityGrid.addColumn(Oportunidade.FIELD_OPORTUNIDADE_ID, 35, Oportunidade.ACTION_CADASTRO, Oportunidade.COMMAND_EDIT, "", true);
    entityGrid.addColumn(Oportunidade.LOOKUP_CLIENTE, 250);
    entityGrid.addColumn(Oportunidade.LOOKUP_DEPARTAMENTO , 200);
    entityGrid.addColumn(Oportunidade.LOOKUP_FASE, 200);
    entityGrid.addColumn(Oportunidade.FIELD_DATA_ACOMPANHAMENTO, 90);
    entityGrid.addColumn(Oportunidade.FIELD_PERCENTUAL_SUCESSO, 80);
    entityGrid.addColumn(Oportunidade.FIELD_STATUS, 90);
    entityGrid.addColumn(Oportunidade.FIELD_VALOR, 100);

  
%>

<html>
  <head>
    <title><%=Oportunidade.ACTION.getCaption()%></title>
    <link href="<%=facade.getStyle().userInterface()%>" rel="stylesheet" type="text/css">
    <script src="include/scripts.js" type="text/javascript"></script>
  </head>
  <body style="margin:0px;" onselectstart="return true;" oncontextmenu="return false;">

    <!-- inicia o FrameBar -->
    <%=frameBar.begin()%>

      <%// se não estamos na central de atendimento...
        if (!isCentralAtendimento) {%>

          <!-- área de frames -->
          <%=frameBar.beginFrameArea()%>

            <!-- Frame de identificação do objeto -->
            <%=frameBar.actionFrame(Oportunidade.ACTION)%>

            <!-- Frame de comandos -->
            <%=frameBar.beginFrame("Comandos", false, false)%>
              <table style="width:100%;">
                <tr>
                  <td><%=CommandControl.entityFormHyperlink(facade, oportunidade, Oportunidade.ACTION_CADASTRO, Oportunidade.COMMAND_INSERT, ImageList.COMMAND_INSERT, false)%></td>
                </tr>
                <tr>
                  <td><%=CommandControl.formHyperlink(facade, formDelete, ImageList.COMMAND_DELETE, "Deseja mesmo excluir o(s) registro(s) selecionado(s)?", true, false)%></td>
                </tr>
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
                      <td><%=ParamLabel.script(facade, oportunidade.USER_PARAM_OPORTUNIDADE_ID)%></td>
                    </tr>
                    <tr>
                      <td><%=ParamFormEdit.script(facade, oportunidade.USER_PARAM_OPORTUNIDADE_ID, 0, "", "")%></td>
                    </tr>
                    <tr>
                      <td><%=ParamLabel.script(facade, oportunidade.USER_PARAM_DEPARTAMENTO_ID)%></td>
                    </tr>
                    <tr>
                      <td><%=DepartamentoUI.lookupListForParam(facade, selectedEmpresaId, oportunidade.USER_PARAM_DEPARTAMENTO_ID, 0, "", "")%></td>
                    </tr>
                    <tr>
                      <td><%=ParamLabel.script(facade, oportunidade.USER_PARAM_USUARIO_ID)%></td>
                    </tr>
                    <tr>
                      <td><%=UsuarioUI.lookupSearchForParam(facade, oportunidade.USER_PARAM_USUARIO_ID, 0, "", "", false)%></td>
                    </tr>
                    <tr>
                      <td><%=ParamLabel.script(facade, oportunidade.USER_PARAM_CAMPANHA_ID)%></td>
                    </tr>
                    <tr>
                      <td><%=CampanhaUI.lookupListForParam(facade, oportunidade.USER_PARAM_OPORTUNIDADE_ID, NaoSim.NAO, 0, "", "")%></td>
                    </tr>
                    <tr>
                      <td><%=ParamLabel.script(facade, oportunidade.USER_PARAM_CLIENTE_ID)%></td>
                    </tr>
                    <tr>
                      <td><%=ContatoUI.lookupSearchForParam(facade, oportunidade.USER_PARAM_CLIENTE_ID, NaoSim.SIM, NaoSim.TODOS, NaoSim.TODOS, NaoSim.TODOS, NaoSim.TODOS, 0, "", "", false)%></td>
                    </tr>
                    <tr>
                      <td><%=ParamLabel.script(facade, oportunidade.USER_PARAM_FASE_ID)%></td>
                    </tr>
                    <tr>
                      <td><%=FaseUI.lookupListForParam(facade, oportunidade.USER_PARAM_FASE_ID, 0, "", "")%></td>
                    </tr>
                    <tr>
                      <td><%=ParamLabel.script(facade, oportunidade.USER_PARAM_STATUS)%></td>
                    </tr>
                    <tr>
                      <td><%=ParamFormSelect.script(facade, oportunidade.USER_PARAM_STATUS, 0, "", "")%></td>
                    </tr>
                    <tr>
                      <td><%=CommandControl.formButton(facade, formSearch, ImageList.COMMAND_SEARCH, "", true, false)%></td>
                    </tr>
                  </table>
              <%=formSearch.end()%>
            <%=frameBar.endFrame()%>

          <%=frameBar.endFrameArea()%>
      <%} // if%>


      <!-- área do Grid -->
      <%=frameBar.beginClientArea()%>
        <!-- Form de exclusão -->
        <%if (!isCentralAtendimento) {%>
          <%=formDelete.begin()%>
        <%} // if%>

         <!-- Grid -->
         <%if (isCentralAtendimento) {%>
            <table style="width:100%;height:100%;"  cellpadding="0" cellspacing="0">
              <tr>
                <td style="width:100%;height:auto;">
                  <%entityGrid.setSelectType(EntityGrid.SELECT_TYPE_NONE);%>
                  <%=entityGrid.script(oportunidadeInfoList, "", searchErrorMessage.toString())%>
                </td>
              </tr>
              <tr>
                <td style="height:30px;">
                  <%=formSearch.begin()%>
                    <%=ParamFormEdit.script(facade, oportunidade.USER_PARAM_CLIENTE_ID, -1, "", "")%>
                    <table>
                      <tr>
                        <td><%=CommandControl.entityFormButton(facade, oportunidade, Oportunidade.ACTION_CADASTRO, Oportunidade.COMMAND_INSERT, ImageList.COMMAND_INSERT, false)%></td>
                        <td style="width:50px;"></td>
                        <td><b>Pesquisa</b>&nbsp;&nbsp;&nbsp;</td>
                        <td><%=ParamLabel.script(facade, oportunidade.USER_PARAM_OPORTUNIDADE_ID)%></td>
                        <td style="width:50px;"><%=ParamFormEdit.script(facade, oportunidade.USER_PARAM_OPORTUNIDADE_ID, 0, "", "")%></td>
                        <td><%=CommandControl.formButton(facade, formSearch, ImageList.COMMAND_SEARCH, "", true, false)%></td>
                      </tr>
                    </table>
                  <%=formSearch.end()%>
                </td>
              </tr>
            </table>
        <%} // if
          else {%>
            <%=entityGrid.script(oportunidadeInfoList, "", searchErrorMessage.toString())%>
        <%} // else%>

        <%if (!isCentralAtendimento) {%>
          <%=formDelete.end()%>
        <%} // if%>
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
