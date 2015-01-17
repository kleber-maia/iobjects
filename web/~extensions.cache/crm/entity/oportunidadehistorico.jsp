     
<%@include file="../include/beans.jsp"%>

<%@page import="imanager.entity.*"%>
<%@page import="imanager.misc.*"%>

<%!
  public class EntityGridOportunidadeHistoricoEventListener implements EntityGrid.EventListener {

    private OportunidadeHistorico oportunidadeHistorico = null;

    public EntityGridOportunidadeHistoricoEventListener(OportunidadeHistorico oportunidadeHistorico) {
      this.oportunidadeHistorico = oportunidadeHistorico;
    }

    public String onAddCell(EntityInfo   entityInfo,
                            EntityField  entityField,
                            EntityLookup entityLookup,
                            String       value) {
      // se é a coluna Status...
      if (entityField == OportunidadeHistorico.FIELD_STATUS) {
        // se está ganha...
        if (((OportunidadeHistoricoInfo)entityInfo).getStatus() == StatusOportunidade.GANHA)
          return "<font style=color:green>" + value + "</span>";
        // se está perdida...
        else if (((OportunidadeHistoricoInfo)entityInfo).getStatus() == StatusOportunidade.PERDIDA)
          return "<font style=color:red>" + value + "</span>";
        // se está em andamento...
        else if (((OportunidadeHistoricoInfo)entityInfo).getStatus() == StatusOportunidade.EM_ANDAMENTO)
          return "<font style=color:blue>" + value + "</span>";
        else
          return value;
      }
      // não temos o que fazer
      else
        return value;
      }
    }

  public OportunidadeHistoricoInfo[] search(OportunidadeHistorico oportunidadeHistorico, StringBuffer searchErrorMessage) {
    try {
      // resultado da pesquisa...
      return oportunidadeHistorico.selectByFilter(
              NumberTools.parseInt(oportunidadeHistorico.USER_PARAM_EMPRESA_ID.getValue()),
              NumberTools.parseInt(oportunidadeHistorico.USER_PARAM_OPORTUNIDADE_ID.getValue()),
              null);
    }
    catch (Exception e) {
      searchErrorMessage.append(ExtendedException.extractMessage(e));
      return new OportunidadeHistoricoInfo[0];
    } // try-catch
  }
%>

<%
  // início do bloco protegido
  try {
    // nossa instância de OportunidadeHistorico
    OportunidadeHistorico oportunidadeHistorico = (OportunidadeHistorico)facade.getEntity(OportunidadeHistorico.CLASS_NAME);

    // lista que iremos exibir
    OportunidadeHistoricoInfo[] oportunidadeHistoricoInfoList = new OportunidadeHistoricoInfo[0];
    // mensagem de erro na pesquisa
    StringBuffer searchErrorMessage = new StringBuffer();

    // se estamos excluindo...
    if (Controller.isDeleting(request)) {
      // registros selecionados
      OportunidadeHistoricoInfo[] selectedInfoList = (OportunidadeHistoricoInfo[])EntityGrid.selectedInfoList(oportunidadeHistorico, request);
      // compõe a resposta
      Form.composeAjaxResponse(response, Form.COMPOSE_TYPE_JAVA_SCRIPT);
      // nosso Grid
      EntityGrid entityGrid = EntityGrid.getInstance(facade, oportunidadeHistorico);
      // exclui e apaga do Grid
      for (int i=0; i<selectedInfoList.length; i++) {
        oportunidadeHistorico.delete(selectedInfoList[i]);%>
        <%=entityGrid.deleteRow(selectedInfoList[i])%>
      <%} // for
      // dispara
      return;
    }
    // se estamos pesquisando...
    else if (Controller.isSearching(request)) {
      // define os valores dos parâmetros de usuário
      oportunidadeHistorico.userParamList().setParamsValues(request);
      // compõe a resposta
      Form.composeAjaxResponse(response, Form.COMPOSE_TYPE_JAVA_SCRIPT);
      // atualiza tudo
      oportunidadeHistoricoInfoList = search(oportunidadeHistorico, searchErrorMessage);
      %><%=EntityGrid.getInstance(facade, oportunidadeHistorico).update(oportunidadeHistoricoInfoList, "", searchErrorMessage.toString())%><%
      // dispara
      return;
    } // if

    // define os valores dos parâmetros de usuário
    oportunidadeHistorico.userParamList().setParamsValues(request);
    // realiza a pesquisa
    oportunidadeHistoricoInfoList = search(oportunidadeHistorico, searchErrorMessage);

    // nosso Form de pesquisa
    Form formSearch = new Form(request, "formSearchOportunidadeHistorico", OportunidadeHistorico.ACTION, OportunidadeHistorico.COMMAND_SEARCH, "", true, true);
    // nosso Form de exclusão
    Form formDelete = new Form(request, "formDeleteOportunidadeHistorico", OportunidadeHistorico.ACTION, OportunidadeHistorico.COMMAND_DELETE, "", false, true);
    // nosso FrameBar
    FrameBar frameBar = new FrameBar(facade, "frameBarOportunidadeHistorico");
    // nosso Grid
    EntityGrid entityGrid = EntityGrid.getInstance(facade, oportunidadeHistorico, 0, 0);
    entityGrid.addEventListener(new EntityGridOportunidadeHistoricoEventListener(oportunidadeHistorico));
    entityGrid.setSelectType(EntityGrid.SELECT_TYPE_NONE);
    entityGrid.addColumn(OportunidadeHistorico.LOOKUP_USUARIO, 200);
    entityGrid.addColumn(OportunidadeHistorico.FIELD_STATUS, 500);
    entityGrid.addColumn(OportunidadeHistorico.FIELD_DATA_HORA_INCLUSAO, 120);
  
%>

<html>
  <head>
    <title><%=OportunidadeHistorico.ACTION.getCaption()%></title>
    <link href="<%=facade.getStyle().userInterface()%>" rel="stylesheet" type="text/css">
    <script src="include/scripts.js" type="text/javascript"></script>
  </head>
  <body style="margin:0px;" onselectstart="return true;" oncontextmenu="return false;">

    <!-- inicia o FrameBar -->
    <%=frameBar.begin()%>

      <!-- área do Grid -->
      <%=frameBar.beginClientArea()%>

        <!-- Grid -->
        <%=entityGrid.script(oportunidadeHistoricoInfoList, "", searchErrorMessage.toString())%>

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
