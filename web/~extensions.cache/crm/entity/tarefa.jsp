     
<%@include file="../include/beans.jsp"%>

<%@page import="imanager.entity.*"%>
<%@page import="imanager.process.*"%>
<%@page import="imanager.misc.*"%>
<%@page import="imanager.ui.entity.*"%>

<%@page import="securityservice.ui.entity.*"%>

<%!
  public class EntityGridTarefaEventListener implements EntityGrid.EventListener {

    private Tarefa    tarefa = null;
    private Timestamp today = new Timestamp(DateTools.getActualDate().getTime());

    public EntityGridTarefaEventListener(Tarefa tarefa) {
      this.tarefa = tarefa;
    }

    public String onAddCell(EntityInfo   entityInfo,
                            EntityField  entityField,
                            EntityLookup entityLookup,
                            String       value) {
      // se é a coluna Prazo e está em atraso...
      if ((entityField == Tarefa.FIELD_PRAZO) && ((TarefaInfo)entityInfo).getPrazo().before(today) && (((TarefaInfo)entityInfo).getStatus() != StatusTarefa.CONCLUIDA))
        return "<font style=color:red>" + value + "</font>";
      // se é a coluna Status e está Concluída...
      else if ((entityField == Tarefa.FIELD_STATUS) && (((TarefaInfo)entityInfo).getStatus() == StatusTarefa.CONCLUIDA))
        return "<font style=text-decoration:line-through>" + value + "</font>";
      // se é a coluna Assunto...
      else if (entityLookup == Tarefa.LOOKUP_ASSUNTO) {
        int index = value.lastIndexOf("/");
        return index >= 0 ? value.substring(index+1) : value;
      }
      // se é a coluna de anotações...
      else if (entityField == Tarefa.FIELD_DESCRICAO) {
        int pos = value.indexOf("\n");
        if (pos > 0)
          return value.substring(0, pos).replaceAll("\r", " ").replaceAll("\n", " ");  
        else 
          return value;
      } 
      // não temos o que fazer
      else
        return value;
    }
  }

  public TarefaInfo[] search(Tarefa tarefa, int empresaId, StringBuffer searchErrorMessage) {
    try {
      // resultado da pesquisa...
      TarefaInfo[] result = tarefa.selectByFilter(
              empresaId,
              NumberTools.parseInt(tarefa.USER_PARAM_TAREFA_ID.getValue()),
              NumberTools.parseInt(tarefa.USER_PARAM_DEPARTAMENTO_ID.getValue()),
              NumberTools.parseInt(tarefa.USER_PARAM_ASSUNTO_ID.getValue()),
              NumberTools.parseInt(tarefa.USER_PARAM_STATUS.getValue()),
              NumberTools.parseInt(tarefa.USER_PARAM_CLIENTE_ID.getValue()),
              NumberTools.parseInt(tarefa.USER_PARAM_USUARIO_ID.getValue()),
              DateTools.parseDate(tarefa.USER_PARAM_DATA_INICIAL.getValue()),
              DateTools.parseDate(tarefa.USER_PARAM_DATA_FINAL.getValue()),
              NumberTools.parseInt(tarefa.USER_PARAM_ATRASADAS.getValue()),
              null);
      // reaprovieta os dados dos contatos
      ContatoProducer contatoProducer = ContatoProducer.getInstance(tarefa.getFacade());
      contatoProducer.addCollection(result, Tarefa.FIELD_CLIENTE_ID.getFieldAlias());
      // retorna
      return result;
    }
    catch (Exception e) {
      searchErrorMessage.append(ExtendedException.extractMessage(e));
      return new TarefaInfo[0];
    } // try-catch
  }
%>

<%
  // início do bloco protegido
  try {
    // nossa instância de Tarefa
    Tarefa tarefa = (Tarefa)facade.getEntity(Tarefa.CLASS_NAME);
    // nossa instância de Empresa
    Empresa empresa = (Empresa)facade.getEntity(Empresa.CLASS_NAME);

    // se a Empresa é apenas um Estoque...exceção
    if (empresa.isEstoque(selectedEmpresaId))
      throw new Exception("Operação não permitida em uma Empresa que é apenas estoque.");

    // lista que iremos exibir
    TarefaInfo[] tarefaInfoList = new TarefaInfo[0];
    // mensagem de erro na pesquisa
    StringBuffer searchErrorMessage = new StringBuffer();

    // se estamos excluindo...
    if (Controller.isDeleting(request)) {
      // registros selecionados
      TarefaInfo[] selectedInfoList = (TarefaInfo[])EntityGrid.selectedInfoList(tarefa, request);
      // compõe a resposta
      Form.composeAjaxResponse(response, Form.COMPOSE_TYPE_JAVA_SCRIPT);
      // nosso Grid
      EntityGrid entityGrid = EntityGrid.getInstance(facade, tarefa);
      // exclui e apaga do Grid
      for (int i=0; i<selectedInfoList.length; i++) {
        tarefa.delete(selectedInfoList[i]);%>
        <%=entityGrid.deleteRow(selectedInfoList[i])%>
      <%} // for
      // dispara
      return;
    }
    // se estamos pesquisando...
    else if (Controller.isSearching(request)) {
      // define os valores dos parâmetros de usuário
      tarefa.userParamList().setParamsValues(request);
      // compõe a resposta
      Form.composeAjaxResponse(response, Form.COMPOSE_TYPE_JAVA_SCRIPT);
      // atualiza tudo
      tarefaInfoList = search(tarefa, selectedEmpresaId, searchErrorMessage);
      %><%=EntityGrid.getInstance(facade, tarefa).update(tarefaInfoList, "", searchErrorMessage.toString())%><%
      // dispara
      return;
    } // if
    
    // estamos na central de atendimento?
    boolean isCentralAtendimento = request.getParameter(tarefa.CONTROL_PARAM_CENTRAL_ATENDIMENTO.getName()) != null;
    // grava na sessão
    session.setAttribute(tarefa.CONTROL_PARAM_CENTRAL_ATENDIMENTO.getName(), isCentralAtendimento);

    if (isCentralAtendimento)
      tarefa.USER_PARAM_CLIENTE_ID.setValue("-1");
    else if (tarefa.USER_PARAM_CLIENTE_ID.getIntValue() < 0)
      tarefa.USER_PARAM_CLIENTE_ID.setValue("0");

    // define os valores dos parâmetros de usuário
    tarefa.userParamList().setParamsValues(request);
    // realiza a pesquisa
    tarefaInfoList = search(tarefa, selectedEmpresaId, searchErrorMessage);


    // nosso Form de pesquisa
    Form formSearch = new Form(request, "formSearchTarefa", Tarefa.ACTION, Tarefa.COMMAND_SEARCH, "", true, true);
    // nosso Form de exclusão
    Form formDelete = new Form(request, "formDeleteTarefa", Tarefa.ACTION, Tarefa.COMMAND_DELETE, "", false, true, true);
    // nosso FrameBar
    FrameBar frameBar = new FrameBar(facade, "frameBarTarefa");
    // nosso Grid
    EntityGrid entityGrid = EntityGrid.getInstance(facade, tarefa, 0, 0);
    entityGrid.setSelectType(EntityGrid.SELECT_TYPE_MULTIPLE);
    entityGrid.addEventListener(new EntityGridTarefaEventListener(tarefa));
    entityGrid.addColumn(Tarefa.FIELD_TAREFA_ID, 35, Tarefa.ACTION_CADASTRO, Tarefa.COMMAND_EDIT, "", true);
    entityGrid.addColumn(Tarefa.FIELD_PRAZO, 70);
    entityGrid.addColumn(Tarefa.LOOKUP_CLIENTE, 250);
    entityGrid.addColumn(Tarefa.LOOKUP_DEPARTAMENTO, 200);
    entityGrid.addColumn(Tarefa.LOOKUP_ASSUNTO, 200);
    entityGrid.addColumn(Tarefa.FIELD_DESCRICAO, 200);
    entityGrid.addColumn(Tarefa.LOOKUP_USUARIO, 150);
    entityGrid.addColumn(Tarefa.FIELD_STATUS, 200);
%>

<html>
  <head>
    <title><%=Tarefa.ACTION.getCaption()%></title>
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
            <%=frameBar.actionFrame(Tarefa.ACTION)%>

            <!-- Frame de comandos -->
            <%=frameBar.beginFrame("Comandos", false, false)%>
              <table style="width:100%;">
                <tr>
                  <td><%=CommandControl.entityFormHyperlink(facade, tarefa, Tarefa.ACTION_CADASTRO, Tarefa.COMMAND_INSERT, ImageList.COMMAND_INSERT, false)%></td>
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
                      <td><%=ParamLabel.script(facade, tarefa.USER_PARAM_TAREFA_ID)%></td>
                    </tr>
                    <tr>
                      <td><%=ParamFormEdit.script(facade, tarefa.USER_PARAM_TAREFA_ID, 0, "", "")%></td>
                    </tr>
                    <tr>
                      <td><%=ParamLabel.script(facade, tarefa.USER_PARAM_DEPARTAMENTO_ID)%></td>
                    </tr>
                    <tr>
                      <td><%=DepartamentoUI.lookupListForParam(facade, selectedEmpresaId, tarefa.USER_PARAM_DEPARTAMENTO_ID, 0, "", "")%></td>
                    </tr>
                    <tr>
                      <td><%=ParamLabel.script(facade, tarefa.USER_PARAM_ASSUNTO_ID)%></td>
                    </tr>
                    <tr>
                      <td><%=AssuntoUI.lookupSelectExForParam(facade, tarefa.USER_PARAM_ASSUNTO_ID, 0, "", "")%></td>
                    </tr>
                    <tr>
                      <td><%=ParamLabel.script(facade, tarefa.USER_PARAM_STATUS)%></td>
                    </tr>
                    <tr>
                      <td><%=ParamFormSelect.script(facade, tarefa.USER_PARAM_STATUS, 0, "", "")%></td>
                    </tr>
                    <tr>
                      <td><%=ParamLabel.script(facade, tarefa.USER_PARAM_CLIENTE_ID)%></td>
                    </tr>
                    <tr>
                      <td><%=ContatoUI.lookupSearchForParam(facade, tarefa.USER_PARAM_CLIENTE_ID, NaoSim.SIM, NaoSim.TODOS, NaoSim.TODOS, NaoSim.TODOS, NaoSim.TODOS, 0, "", "", false)%></td>
                    </tr>
                    <tr>
                      <td><%=ParamLabel.script(facade, tarefa.USER_PARAM_USUARIO_ID)%></td>
                    </tr>
                    <tr>
                      <td><%=UsuarioUI.lookupSearchForParam(facade, tarefa.USER_PARAM_USUARIO_ID, 0, "", "", false)%></td>
                    </tr>
                    <tr>
                      <td><%=ParamLabel.script(facade, tarefa.USER_PARAM_DATA_INICIAL)%></td>
                    </tr>
                    <tr>
                      <td><%=ParamFormEdit.script(facade, tarefa.USER_PARAM_DATA_INICIAL, 0, "", "")%></td>
                    </tr>
                    <tr>
                      <td><%=ParamLabel.script(facade, tarefa.USER_PARAM_DATA_FINAL)%></td>
                    </tr>
                    <tr>
                      <td><%=ParamFormEdit.script(facade, tarefa.USER_PARAM_DATA_FINAL, 0, "", "")%></td>
                    </tr>
                    <tr>
                      <td><%=ParamLabel.script(facade, tarefa.USER_PARAM_ATRASADAS)%></td>
                    </tr>
                    <tr>
                      <td><%=ParamFormSelect.script(facade, tarefa.USER_PARAM_ATRASADAS, 0, "", "")%></td>
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
                  <%=entityGrid.script(tarefaInfoList, "", searchErrorMessage.toString())%>
                </td>
              </tr>
              <tr>
                <td style="height:30px;">
                  <%=formSearch.begin()%>
                    <%=ParamFormEdit.script(facade, tarefa.USER_PARAM_CLIENTE_ID, -1, "", "")%>
                    <table>
                      <tr>
                        <td><%=CommandControl.entityFormButton(facade, tarefa, Tarefa.ACTION_CADASTRO, Tarefa.COMMAND_INSERT, ImageList.COMMAND_INSERT, false)%></td>
                        <td style="width:50px"></td>
                        <td><b>Pesquisa</b>&nbsp;&nbsp;&nbsp;</td>
                        <td><%=ParamLabel.script(facade, tarefa.USER_PARAM_TAREFA_ID)%></td>
                        <td style="width:50px;"><%=ParamFormEdit.script(facade, tarefa.USER_PARAM_TAREFA_ID, 0, "", "")%></td>
                        <td><%=ParamLabel.script(facade, tarefa.USER_PARAM_DATA_INICIAL)%></td>
                        <td style="width:90px;"><%=ParamFormEdit.script(facade, tarefa.USER_PARAM_DATA_INICIAL, 0, "", "")%></td>
                        <td><%=ParamLabel.script(facade, tarefa.USER_PARAM_DATA_FINAL)%></td>
                        <td style="width:90px;"><%=ParamFormEdit.script(facade, tarefa.USER_PARAM_DATA_FINAL, 0, "", "")%></td>
                        <td><%=CommandControl.formButton(facade, formSearch, ImageList.COMMAND_SEARCH, "", true, false)%></td>
                      </tr>
                    </table>
                  <%=formSearch.end()%>
                </td>
              </tr>
            </table>
        <%} // if
          else {%>
            <%=entityGrid.script(tarefaInfoList, "", searchErrorMessage.toString())%>
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
