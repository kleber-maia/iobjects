     
<%@include file="../include/beans.jsp"%>

<%@page import="imanager.entity.*"%>

<%!
  public class EntityGridGrupoContatoEventListener implements EntityGrid.EventListener {
    private GrupoContato grupoContato = null;

    public EntityGridGrupoContatoEventListener(GrupoContato grupoContato) {
      this.grupoContato = grupoContato;
    }

    public String onAddCell(EntityInfo   entityInfo,
                            EntityField  entityField,
                            EntityLookup entityLookup,
                            String       value) {
      // se não é a coluna do campo Nome...retorna o valor original
      if ((entityField == null) || (!entityField.getFieldName().equals(GrupoContato.FIELD_NOME.getFieldName()))) 
        return value;
      // nome da Centro
      String nome = ((GrupoContatoInfo)entityInfo).getNome();
      // obtém o nível da Centro pela quantidade de '/' existentes
      String[] parts = nome.split(GrupoContato.LEVEL_DELIMITER);
      int level = parts.length-1;
      // retira o nome da Centro pai de 'value', que está em forma de link
      String a = value.substring(0, value.indexOf(">") + 1);
      value = a + parts[parts.length - 1] + "</a>";
      // retorna a imagem da pasta ao lado do nome do centro
      // e indenta o nome do centro em 22px para cada nível
      return "<span style=padding-left:" + 22*level + "px;>"
           +   value
           + "</span>";
    }
  }
  
  public GrupoContatoInfo[] search(GrupoContato grupoContato, StringBuffer searchErrorMessage) {
    try {
      // resultado da pesquisa...
      return grupoContato.selectByFilter("%");
    }
    catch (Exception e) {
      searchErrorMessage.append(ExtendedException.extractMessage(e));
      return new GrupoContatoInfo[0];
    } // try-catch
  }
%>

<%
  // início do bloco protegido
  try {
    // nossa instância de Grupo Contato
    GrupoContato grupoContato = (GrupoContato)facade.getEntity(GrupoContato.CLASS_NAME);

    // lista que iremos exibir
    GrupoContatoInfo[] grupoContatoInfoList = new GrupoContatoInfo[0];
    // mensagem de erro na pesquisa
    StringBuffer searchErrorMessage = new StringBuffer();

    // se estamos excluindo...
    if (Controller.isDeleting(request)) {
      // registros selecionados
      GrupoContatoInfo[] selectedInfoList = (GrupoContatoInfo[])EntityGrid.selectedInfoList(grupoContato, request);
      // compõe a resposta
      Form.composeAjaxResponse(response, Form.COMPOSE_TYPE_JAVA_SCRIPT);
      // nosso Grid
      EntityGrid entityGrid = EntityGrid.getInstance(facade, grupoContato);
      // exclui e apaga do Grid
      for (int i=0; i<selectedInfoList.length; i++) {
        grupoContato.delete(selectedInfoList[i]);%>
        <%=entityGrid.deleteRow(selectedInfoList[i])%>
      <%} // for
      // dispara
      return;
    }
    // se estamos pesquisando...
    else if (Controller.isSearching(request)) {
      // define os valores dos parâmetros de usuário
      grupoContato.userParamList().setParamsValues(request);
      // compõe a resposta
      Form.composeAjaxResponse(response, Form.COMPOSE_TYPE_JAVA_SCRIPT);
      // atualiza tudo
      grupoContatoInfoList = search(grupoContato, searchErrorMessage);
      %><%=EntityGrid.getInstance(facade, grupoContato).update(grupoContatoInfoList, "", searchErrorMessage.toString())%><%
      // dispara
      return;
    } // if

    // define os valores dos parâmetros de usuário
    grupoContato.userParamList().setParamsValues(request);
    // realiza a pesquisa
    grupoContatoInfoList = search(grupoContato, searchErrorMessage);

    // nosso Form de pesquisa
    Form formSearch = new Form(request, "formSearchGrupoContato", GrupoContato.ACTION, GrupoContato.COMMAND_SEARCH, "", false, true);    
    // nosso Form de exclusão
    Form formDelete = new Form(request, "formDeleteGrupoContato", GrupoContato.ACTION, GrupoContato.COMMAND_DELETE, "", false, true, true);
    // nosso FrameBar
    FrameBar frameBar = new FrameBar(facade, "frameBarGrupoContato");
    // nosso Grid
    EntityGrid entityGrid = EntityGrid.getInstance(facade, grupoContato, 0, 0);
    entityGrid.addEventListener(new EntityGridGrupoContatoEventListener(grupoContato));    
    entityGrid.addColumn(GrupoContato.FIELD_NOME, 500, GrupoContato.ACTION_CADASTRO, GrupoContato.COMMAND_EDIT, "", true);
    
%>

<html>
  <head>
    <title><%=GrupoContato.ACTION.getCaption()%></title>
    <link href="<%=facade.getStyle().userInterface()%>" rel="stylesheet" type="text/css">
    <script src="include/scripts.js" type="text/javascript"></script>
  </head>
  <body style="margin:0px;" onselectstart="return true;" oncontextmenu="return false;">
    
    <script type="text/javascript">

      /**
       * Insere um novo Categoria.
       */
      function insert() {
        // chama o JSP de cadastro passando o grupoContatoID=0
        EntityGrid_forwardForm('gridrelacionamento_grupo_contato', '<%=GrupoContato.ACTION_CADASTRO.getName()%>', '<%=GrupoContato.COMMAND_INSERT.getName()%>', ['<%=grupoContato.USER_PARAM_GRUPO_CONTATO_PAI_ID.getName()%>'], ['0']); 
      }

      /**
       * Insere um GrupoContato filho do GrupoContato selecionado no Grid.
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
        // chama o JSP de cadastro passando o grupoContatoID selecionado
        EntityGrid_forwardForm('gridrelacionamento_grupo_contato', '<%=GrupoContato.ACTION_CADASTRO.getName()%>', '<%=GrupoContato.COMMAND_INSERT.getName()%>', ['<%=grupoContato.USER_PARAM_GRUPO_CONTATO_PAI_ID.getName()%>'], [selectedKeys[0]]);
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
        <%=frameBar.actionFrame(GrupoContato.ACTION)%>

        <!-- Frame de comandos -->
        <%=frameBar.beginFrame("Comandos", false, false)%>
          <table style="width:100%;">
            <tr>
              <td><%=CommandControl.actionCustomScriptHyperlink(facade, GrupoContato.ACTION_CADASTRO, GrupoContato.COMMAND_INSERT, ImageList.COMMAND_INSERT, "javascript:insert()", "", "", false)%></td>  
            </tr>
            <tr>
              <td><%=CommandControl.actionCustomScriptHyperlink(facade, GrupoContato.ACTION_CADASTRO, GrupoContato.COMMAND_INSERT_CHILD, ImageList.COMMAND_INSERT, "javascript:insertChild()", "", "", false)%></td>
            </tr>            
            <tr>
              <td><%=CommandControl.formHyperlink(facade, formDelete, ImageList.COMMAND_DELETE, "Deseja mesmo excluir o(s) registro(s) selecionado(s)?", true, false)%></td>
            </tr>
          </table>
        <%=frameBar.endFrame()%>

        <!-- form de pesquisa -->
        <%=formSearch.begin()%>
        <%=formSearch.end()%>        

      <%=frameBar.endFrameArea()%>

      <!-- área do Grid -->
      <%=frameBar.beginClientArea()%>
        <!-- Form de exclusão -->
        <%=formDelete.begin()%>

          <!-- Grid -->
          <%=entityGrid.script(grupoContatoInfoList, "", searchErrorMessage.toString())%>

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
