
<%@include file="../include/beans.jsp"%>

<%@page import="java.sql.*"%>
<%@page import="securityservice.entity.*"%>

<%
  try {
    // nossa instância de Papel Ação
    PapelAcao papelAcao = (PapelAcao)facade.getEntity(PapelAcao.CLASS_NAME);

    // se estamos salvando...
    if (Controller.isSaving(request)) {
      // pega o Papel Id
      int papelId = NumberTools.parseInt(request.getParameter(papelAcao.USER_PARAM_PAPEL_ID.getName()));
      // pega a lista de ações marcadas
      String[] actionList = request.getParameterValues(PapelAcao.FIELD_ACAO.getFieldAlias());
      if (actionList == null)
        actionList = new String[0];
      // salva
      papelAcao.insert(papelId, actionList);
    } // if

    // define valores dos parâmetros de usuário
    papelAcao.userParamList().setParamsValues(request);

    // lista que iremos exibir
    PapelAcaoInfo[] papelAcaoInfoList = papelAcao.selectByPapelId(
            NumberTools.parseInt(papelAcao.USER_PARAM_PAPEL_ID.getValue())
          );

    // nosso Form de dados
    Form formData = new Form(request, "formDataPapelAcao", PapelAcao.ACTION_CADASTRO, PapelAcao.COMMAND_SAVE, "", false);

    // árvore de objetos
    BusinessObjectTreeView treeView = null;
    // configura a árvore de objetos
    treeView = new BusinessObjectTreeView(facade, "papelAcaoTreeView", true);
    treeView.setActionList(facade.actionList());
    treeView.setCheckBoxId(PapelAcao.FIELD_ACAO.getFieldAlias());
    treeView.setShowCommands(true);
    treeView.setShowNestedActions(true);
    treeView.setShowMobileActions(true);

    // define a lista de ações e comandos selecionados
    Vector actions = new Vector();
    Vector commands = new Vector();
    for (int i=0; i<papelAcaoInfoList.length; i++) {
      // ação da vez
      PapelAcaoInfo papelAcaoInfo = papelAcaoInfoList[i];
      // adiciona a ação
      actions.add(papelAcaoInfo.getAcao());
      // seus comandos
      String[] acaoComandos = papelAcaoInfo.getComandos().split(";");
      for (int w=0; w<acaoComandos.length; w++)
        commands.add(papelAcaoInfo.getAcao() + ":" + acaoComandos[w]);
    } // for
    String[] actionList = new String[actions.size()];
    String[] commandList = new String[commands.size()];
    // *
    actions.copyInto(actionList);
    commands.copyInto(commandList);
    // *
    treeView.setCheckedActionNames(actionList);
    treeView.setCheckedCommandNames(commandList);
%>

<html>
  <head>
    <title><%=PapelAcao.ACTION_CADASTRO.getCaption()%></title>
    <link href="<%=facade.getStyle().userInterface()%>" rel="stylesheet" type="text/css">
    <script src="include/scripts.js" type="text/javascript"></script>
  </head>
  <body style="margin:0px;" onselectstart="return true;" oncontextmenu="return false;">

    <!-- Form de dados -->
    <%=formData.begin()%>
      <!-- dados ocultos -->
      <input type="hidden" name="<%=papelAcao.USER_PARAM_PAPEL_ID.getName()%>" value="<%=papelAcao.USER_PARAM_PAPEL_ID.getValue()%>" />
      <!-- treeview e barra de ferramentas -->
      <table style="width:100%; height:100%;">
        <tr style="height:auto;">
          <td>
             <%=treeView.script()%>
          </td>
        </tr>
        <tr style="height:20px;">
          <td>

            <table cellpadding="0" cellspacing="0">
              <tr>
                <td>
                  <%=CommandControl.formButton(facade, formData, ImageList.COMMAND_SAVE, "Deseja mesmo salvar os Objetos e Comandos selecionados para o Papel?", true, false)%>&nbsp;
                  <%=Button.script(facade, "buttonCancelar", "Cancelar", "Cancela a seleção de Objetos e Comandos para o Papel.", ImageList.COMMAND_DELETE, "", Button.KIND_DEFAULT, "width:auto;", "window.location.reload();", false)%>&nbsp;
                </td>
              </tr>
            </table>

          </td>
        </tr>
      </table>

    <%=formData.end()%>

  </body>
</html>

<%}
  catch (Exception e) {
    Controller.forwardException(e, pageContext);
  } // try-catch
%>
