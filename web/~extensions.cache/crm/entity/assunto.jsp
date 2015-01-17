     
<%@include file="../include/beans.jsp"%>

<%@page import="imanager.entity.*"%>

<%!
  public class EntityGridAssuntoEventListener implements EntityGrid.EventListener {
    private Assunto assunto = null;

    public EntityGridAssuntoEventListener(Assunto assunto) {
      this.assunto = assunto;
    }

    public String onAddCell(EntityInfo   entityInfo,
                            EntityField  entityField,
                            EntityLookup entityLookup,
                            String       value) {
      // se não é a coluna do campo Nome...retorna o valor original
      if ((entityField == null) || (!entityField.getFieldName().equals(Assunto.FIELD_NOME.getFieldName())))
        return value;
      // nome do Assunto
      String nome = ((AssuntoInfo)entityInfo).getNome();
      // obtém o nível do Assunto pela quantidade de '/' existentes
      String[] parts = nome.split(Assunto.LEVEL_DELIMITER);
      int level = parts.length-1;
      // retira o nome do Assunto pai de 'value', que está em forma de link
      String a = value.substring(0, value.indexOf(">") + 1);
      value = a + parts[parts.length - 1] + "</a>";
      // retorna a imagem da pasta ao lado do nome do centro
      // e indenta o nome do Assunto em 22px para cada nível
      return "<span style=padding-left:" + 22*level + "px;>"
           +   value
           + "</span>";
    }
  }

  public AssuntoInfo[] search(Assunto assunto, StringBuffer searchErrorMessage) {
    try {
      // resultado da pesquisa...
      return assunto.selectByFilter("%", null);
    }
    catch (Exception e) {
      searchErrorMessage.append(ExtendedException.extractMessage(e));
      return new AssuntoInfo[0];
    } // try-catch
  }
%>

<%
  // início do bloco protegido
  try {
    // nossa instância de Assunto
    Assunto assunto = (Assunto)facade.getEntity(Assunto.CLASS_NAME);

    // lista que iremos exibir
    AssuntoInfo[] assuntoInfoList = new AssuntoInfo[0];
    // mensagem de erro na pesquisa
    StringBuffer searchErrorMessage = new StringBuffer();

    // se estamos excluindo...
    if (Controller.isDeleting(request)) {
      // registros selecionados
      AssuntoInfo[] selectedInfoList = (AssuntoInfo[])EntityGrid.selectedInfoList(assunto, request);
      // compõe a resposta
      Form.composeAjaxResponse(response, Form.COMPOSE_TYPE_JAVA_SCRIPT);
      // nosso Grid
      EntityGrid entityGrid = EntityGrid.getInstance(facade, assunto);
      // exclui e apaga do Grid
      for (int i=0; i<selectedInfoList.length; i++)
        assunto.delete(selectedInfoList[i]);
      // atualiza tudo
      assuntoInfoList = search(assunto, searchErrorMessage);
      %><%=EntityGrid.getInstance(facade, assunto).update(assuntoInfoList, "", searchErrorMessage.toString())%><%
      // dispara
      return;
    }
    // se estamos pesquisando...
    else if (Controller.isSearching(request)) {
      // define os valores dos parâmetros de usuário
      assunto.userParamList().setParamsValues(request);
      // compõe a resposta
      Form.composeAjaxResponse(response, Form.COMPOSE_TYPE_JAVA_SCRIPT);
      // atualiza tudo
      assuntoInfoList = search(assunto, searchErrorMessage);
      %><%=EntityGrid.getInstance(facade, assunto).update(assuntoInfoList, "", searchErrorMessage.toString())%><%
      // dispara
      return;
    } // if

    // define os valores dos parâmetros de usuário
    assunto.userParamList().setParamsValues(request);
    // realiza a pesquisa
    assuntoInfoList = search(assunto, searchErrorMessage);

    // nosso Form de pesquisa
    Form formSearch = new Form(request, "formSearchAssunto", Assunto.ACTION, Assunto.COMMAND_SEARCH, "", true, true);
    // nosso Form de exclusão
    Form formDelete = new Form(request, "formDeleteAssunto", Assunto.ACTION, Assunto.COMMAND_DELETE, "", false, true, true);
    // nosso FrameBar
    FrameBar frameBar = new FrameBar(facade, "frameBarAssunto");
    // nosso Grid
    EntityGrid entityGrid = EntityGrid.getInstance(facade, assunto, 0, 0);
    entityGrid.addEventListener(new EntityGridAssuntoEventListener(assunto));
    entityGrid.addColumn(Assunto.FIELD_NOME, 500, Assunto.ACTION_CADASTRO, Assunto.COMMAND_EDIT, "", true);
%>

<html>
  <head>
    <title><%=Assunto.ACTION.getCaption()%></title>
    <link href="<%=facade.getStyle().userInterface()%>" rel="stylesheet" type="text/css">
    <script src="include/scripts.js" type="text/javascript"></script>
  </head>
  <body style="margin:0px;" onselectstart="return true;" oncontextmenu="return false;">

    <script type="text/javascript">

      /**
       * Insere um novo Assunto.
       */
      function insert() {
        // chama o JSP de cadastro passando o assuntoID=0
        EntityGrid_forwardForm('gridcrm_assunto', '<%=Assunto.ACTION_CADASTRO.getName()%>', '<%=Assunto.COMMAND_INSERT.getName()%>', ['<%=assunto.USER_PARAM_ASSUNTO_PAI_ID.getName()%>'], ['0']);
      }

      /**
       * Insere um Assunto filho do Assunto selecionado no Grid.
       */
      function insertChild() {
        // obtém o Conteúdo selecionado
        var selectedKeys = <%=entityGrid.selectedKeyValues()%>;
        // se não temos um Conteudo selecionado...dispara
        if (selectedKeys.length == 0) {
          alert("Nenhum registro selecionado.");
          return;
        }
        // se temos mais de um Conteudo selecionado...dispara
        else if (selectedKeys.length > 1) {
          alert("Mais de um registro selecionado.");
          return;
        } // if
        // aguarde
        document.body.style.cursor = 'wait';
        // chama o JSP de cadastro passando o assuntoID selecionado
        EntityGrid_forwardForm('gridcrm_assunto', '<%=Assunto.ACTION_CADASTRO.getName()%>', '<%=Assunto.COMMAND_INSERT.getName()%>', ['<%=assunto.USER_PARAM_ASSUNTO_PAI_ID.getName()%>'], [selectedKeys[0]]);
      }

      function search() {
        <%=formSearch.submitScript()%>
      }

    </script>

    <!-- inicia o FrameBar -->
    <%=frameBar.begin()%>

      <!-- área de frames -->
      <%=frameBar.beginFrameArea()%>

        <!-- Frame de identificação do objeto -->
        <%=frameBar.actionFrame(Assunto.ACTION)%>

        <!-- Frame de comandos -->
        <%=frameBar.beginFrame("Comandos", false, false)%>
          <table style="width:100%;">
            <tr>
              <td><%=CommandControl.actionCustomScriptHyperlink(facade, Assunto.ACTION_CADASTRO, Assunto.COMMAND_INSERT, ImageList.COMMAND_INSERT, "javascript:insert()", "", "", false)%></td>
            </tr>
            <tr>
              <td><%=CommandControl.actionCustomScriptHyperlink(facade, Assunto.ACTION_CADASTRO, Assunto.COMMAND_INSERT_CHILD, ImageList.COMMAND_INSERT, "javascript:insertChild()", "", "", false)%></td>
            </tr>
            <tr>
              <td><%=CommandControl.formHyperlink(facade, formDelete, ImageList.COMMAND_DELETE, "Deseja mesmo excluir o(s) registro(s) selecionado(s)?", true, false)%></td>
            </tr>
          </table>
        <%=frameBar.endFrame()%>

        <!-- Form de pesquisa -->
        <%=formSearch.begin()%>
        <%=formSearch.end()%>

      <%=frameBar.endFrameArea()%>

      <!-- área do Grid -->
      <%=frameBar.beginClientArea()%>
        <!-- Form de exclusão -->
        <%=formDelete.begin()%>

          <!-- Grid -->
          <%=entityGrid.script(assuntoInfoList, "", searchErrorMessage.toString())%>

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
