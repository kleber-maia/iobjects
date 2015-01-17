
<%@include file="../include/beans.jsp"%>

<%@page import="imanager.entity.*"%>
<%@page import="imanager.misc.*"%>
<%@page import="imanager.process.*"%>
<%@page import="imanager.ui.entity.*"%>

<%@page import="securityservice.ui.entity.*"%>

<%!
  public class EntityGridAtendimentoEventListener implements EntityGrid.EventListener {

    private Atendimento    tarefa = null;
    private Timestamp today = new Timestamp(DateTools.getActualDate().getTime());

    public EntityGridAtendimentoEventListener(Atendimento tarefa) {
      this.tarefa = tarefa;
    }

    public String onAddCell(EntityInfo   entityInfo,
                            EntityField  entityField,
                            EntityLookup entityLookup,
                            String       value) {
      // se é a coluna Assunto...
      if (entityLookup == Atendimento.LOOKUP_ASSUNTO) {
        int index = value.lastIndexOf("/");
        return index >= 0 ? value.substring(index+1) : value;
      }
      // se é a coluna Assunto...
      if (entityField == Atendimento.FIELD_DESCRICAO) {
        return value.length() > 500 ? value.substring(0, 500).replaceAll("\r\n", " ") : value.replaceAll("\r\n", " ");
      }      
      // não temos o que fazer
      else
        return value;
    }
  }

  public AtendimentoInfo[] search(Atendimento atendimento, int empresaId, StringBuffer searchErrorMessage) {
    try {
      // resultado da pesquisa...
      AtendimentoInfo[] result = atendimento.selectByFilter(
              empresaId,
              NumberTools.parseInt(atendimento.USER_PARAM_ATENDIMENTO_ID.getValue()),
              NumberTools.parseInt(atendimento.USER_PARAM_DEPARTAMENTO_ID.getValue()),
              NumberTools.parseInt(atendimento.USER_PARAM_ASSUNTO_ID.getValue()),
              NumberTools.parseInt(atendimento.USER_PARAM_MEIO_ID.getValue()),
              NumberTools.parseInt(atendimento.USER_PARAM_CLIENTE_ID.getValue()),
              NumberTools.parseInt(atendimento.USER_PARAM_CAMPANHA_ID.getValue()),
              NumberTools.parseInt(atendimento.USER_PARAM_USUARIO_ID.getValue()),
              DateTools.parseDate(atendimento.USER_PARAM_DATA_INICIAL.getValue()),
              DateTools.parseDate(atendimento.USER_PARAM_DATA_FINAL.getValue()),
              null
            );
      // reaprovieta os dados dos contatos
      ContatoProducer contatoProducer = ContatoProducer.getInstance(atendimento.getFacade());
      contatoProducer.addCollection(result, Atendimento.FIELD_CLIENTE_ID.getFieldAlias());
      // retorna
      return result;
    }
    catch (Exception e) {
      searchErrorMessage.append(ExtendedException.extractMessage(e));
      return new AtendimentoInfo[0];
    } // try-catch
  }
%>

<%
  // início do bloco protegido
  try {
    // nossa instância de Atendimento
    Atendimento atendimento = (Atendimento)facade.getEntity(Atendimento.CLASS_NAME);
    // nossa instância de Empresa
    Empresa empresa = (Empresa)facade.getEntity(Empresa.CLASS_NAME);

    // se a Empresa é apenas um Estoque...exceção
    if (empresa.isEstoque(selectedEmpresaId))
      throw new Exception("Operação não permitida em uma Empresa que é apenas estoque.");

    // lista que iremos exibir
    AtendimentoInfo[] atendimentoInfoList = new AtendimentoInfo[0];
    // mensagem de erro na pesquisa
    StringBuffer searchErrorMessage = new StringBuffer();

    // se estamos excluindo...
    if (Controller.isDeleting(request)) {
      // registros selecionados
      AtendimentoInfo[] selectedInfoList = (AtendimentoInfo[])EntityGrid.selectedInfoList(atendimento, request);
      // compõe a resposta
      Form.composeAjaxResponse(response, Form.COMPOSE_TYPE_JAVA_SCRIPT);
      // nosso Grid
      EntityGrid entityGrid = EntityGrid.getInstance(facade, atendimento);
      // exclui e apaga do Grid
      for (int i=0; i<selectedInfoList.length; i++) {
        atendimento.delete(selectedInfoList[i]);%>
        <%=entityGrid.deleteRow(selectedInfoList[i])%>
      <%} // for
      // dispara
      return;
    }
    // se estamos pesquisando...
    else if (Controller.isSearching(request)) {
      // define os valores dos parâmetros de usuário
      atendimento.userParamList().setParamsValues(request);
      // compõe a resposta
      Form.composeAjaxResponse(response, Form.COMPOSE_TYPE_JAVA_SCRIPT);
      // atualiza tudo
      atendimentoInfoList = search(atendimento, selectedEmpresaId, searchErrorMessage);
      %><%=EntityGrid.getInstance(facade, atendimento).update(atendimentoInfoList, "", searchErrorMessage.toString())%><%
      // dispara
      return;
    } // if

    // estamos na central de atendimento?
    boolean isCentralAtendimento = request.getParameter(atendimento.CONTROL_PARAM_CENTRAL_ATENDIMENTO.getName()) != null;
    // grava na sessão
    session.setAttribute(atendimento.CONTROL_PARAM_CENTRAL_ATENDIMENTO.getName(), isCentralAtendimento);

    if (isCentralAtendimento)
      atendimento.USER_PARAM_CLIENTE_ID.setValue("-1");
    else if (atendimento.USER_PARAM_CLIENTE_ID.getIntValue() < 0)
      atendimento.USER_PARAM_CLIENTE_ID.setValue("0");

    // define os valores dos parâmetros de usuário
    atendimento.userParamList().setParamsValues(request);
    // realiza a pesquisa
    atendimentoInfoList = search(atendimento, selectedEmpresaId, searchErrorMessage);


    // nosso Form de pesquisa
    Form formSearch = new Form(request, "formSearchAtendimento", Atendimento.ACTION, Atendimento.COMMAND_SEARCH, "", true, true);
    // nosso Form de exclusão
    Form formDelete = new Form(request, "formDeleteAtendimento", Atendimento.ACTION, Atendimento.COMMAND_DELETE, "", false, true, true);
    // nosso FrameBar
    FrameBar frameBar = new FrameBar(facade, "frameBarAtendimento");
    // nosso Grid
    EntityGrid entityGrid = EntityGrid.getInstance(facade, atendimento, 0, 0);
    entityGrid.setSelectType(EntityGrid.SELECT_TYPE_MULTIPLE);
    entityGrid.addEventListener(new EntityGridAtendimentoEventListener(atendimento));
    entityGrid.addColumn(Atendimento.FIELD_ATENDIMENTO_ID, 35, Atendimento.ACTION_CADASTRO, Atendimento.COMMAND_EDIT, "", true);
    entityGrid.addColumn(Atendimento.FIELD_DATA_HORA_INICIO, 100);
    entityGrid.addColumn(Atendimento.LOOKUP_CLIENTE, 250);
    entityGrid.addColumn(Atendimento.LOOKUP_DEPARTAMENTO, 200);
    entityGrid.addColumn(Atendimento.LOOKUP_ASSUNTO, 200);
    entityGrid.addColumn(Atendimento.LOOKUP_MEIO, 150);
    entityGrid.addColumn(Atendimento.FIELD_DESCRICAO, 500);
%>

<html>
  <head>
    <title><%=Atendimento.ACTION.getCaption()%></title>
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
            <%=frameBar.actionFrame(Atendimento.ACTION)%>

            <!-- Frame de comandos -->
            <%=frameBar.beginFrame("Comandos", false, false)%>
              <table style="width:100%;">
                <tr>
                  <td><%=CommandControl.entityFormHyperlink(facade, atendimento, Atendimento.ACTION_CADASTRO, Atendimento.COMMAND_INSERT, ImageList.COMMAND_INSERT, false)%></td>
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
                      <td><%=ParamLabel.script(facade, atendimento.USER_PARAM_ATENDIMENTO_ID)%></td>
                    </tr>
                    <tr>
                      <td><%=ParamFormEdit.script(facade, atendimento.USER_PARAM_ATENDIMENTO_ID, 0, "", "")%></td>
                    </tr>
                    <tr>
                      <td><%=ParamLabel.script(facade, atendimento.USER_PARAM_DEPARTAMENTO_ID)%></td>
                    </tr>
                    <tr>
                      <td><%=DepartamentoUI.lookupListForParam(facade, selectedEmpresaId, atendimento.USER_PARAM_DEPARTAMENTO_ID, 0, "", "")%></td>
                    </tr>
                    <tr>
                      <td><%=ParamLabel.script(facade, atendimento.USER_PARAM_ASSUNTO_ID)%></td>
                    </tr>
                    <tr>
                      <td><%=AssuntoUI.lookupSelectExForParam(facade, atendimento.USER_PARAM_ASSUNTO_ID, 0, "", "")%></td>
                    </tr>
                    <tr>
                      <td><%=ParamLabel.script(facade, atendimento.USER_PARAM_MEIO_ID)%></td>
                    </tr>
                    <tr>
                      <td><%=MeioUI.lookupListForParam(facade, atendimento.USER_PARAM_MEIO_ID, 0, "", "")%></td>
                    </tr>
                    <tr>
                      <td><%=ParamLabel.script(facade, atendimento.USER_PARAM_CLIENTE_ID)%></td>
                    </tr>
                    <tr>
                      <td><%=ContatoUI.lookupSearchForParam(facade, atendimento.USER_PARAM_CLIENTE_ID, NaoSim.SIM, NaoSim.TODOS, NaoSim.TODOS, NaoSim.TODOS, NaoSim.TODOS, 0, "", "", false)%></td>
                    </tr>
                    <tr>
                      <td><%=ParamLabel.script(facade, atendimento.USER_PARAM_CAMPANHA_ID)%></td>
                    </tr>
                    <tr>
                      <td><%=CampanhaUI.lookupListForParam(facade, atendimento.USER_PARAM_CAMPANHA_ID, NaoSim.NAO, 0, "", "")%></td>
                    </tr>
                    <tr>
                      <td><%=ParamLabel.script(facade, atendimento.USER_PARAM_USUARIO_ID)%></td>
                    </tr>
                    <tr>
                      <td><%=UsuarioUI.lookupSearchForParam(facade, atendimento.USER_PARAM_USUARIO_ID, 0, "", "", false)%></td>
                    </tr>
                    <tr>
                      <td><%=ParamLabel.script(facade, atendimento.USER_PARAM_DATA_INICIAL)%></td>
                    </tr>
                    <tr>
                      <td><%=ParamFormEdit.script(facade, atendimento.USER_PARAM_DATA_INICIAL, 0, "", "")%></td>
                    </tr>
                    <tr>
                      <td><%=ParamLabel.script(facade, atendimento.USER_PARAM_DATA_FINAL)%></td>
                    </tr>
                    <tr>
                      <td><%=ParamFormEdit.script(facade, atendimento.USER_PARAM_DATA_FINAL, 0, "", "")%></td>
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
            <table style="width:100%;height:100%;" cellpadding="0" cellspacing="0">
              <tr>
                <td style="width:100%;height:auto;">
                  <%entityGrid.setSelectType(EntityGrid.SELECT_TYPE_NONE);%>
                  <%=entityGrid.script(atendimentoInfoList, "", searchErrorMessage.toString())%>
                </td>
              </tr>
              <tr>
                <td style="height:30px;">
                  <%=formSearch.begin()%>
                    <%=ParamFormEdit.script(facade, atendimento.USER_PARAM_CLIENTE_ID, -1, "", "")%>
                    <table>
                      <tr>
                        <td><b>Pesquisa</b>&nbsp;&nbsp;&nbsp;</td>
                        <td><%=ParamLabel.script(facade, atendimento.USER_PARAM_ATENDIMENTO_ID)%></td>
                        <td style="width:50px;"><%=ParamFormEdit.script(facade, atendimento.USER_PARAM_ATENDIMENTO_ID, 0, "", "")%></td>
                        <td><%=ParamLabel.script(facade, atendimento.USER_PARAM_DATA_INICIAL)%></td>
                        <td style="width:90px;"><%=ParamFormEdit.script(facade, atendimento.USER_PARAM_DATA_INICIAL, 0, "", "")%></td>
                        <td><%=ParamLabel.script(facade, atendimento.USER_PARAM_DATA_FINAL)%></td>
                        <td style="width:90px;"><%=ParamFormEdit.script(facade, atendimento.USER_PARAM_DATA_FINAL, 0, "", "")%></td>
                        <td><%=CommandControl.formButton(facade, formSearch, ImageList.COMMAND_SEARCH, "", true, false)%></td>
                      </tr>
                    </table>
                  <%=formSearch.end()%>
                </td>
              </tr>
            </table>
        <%} // if
          else {%>
            <%=entityGrid.script(atendimentoInfoList, "", searchErrorMessage.toString())%>
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
