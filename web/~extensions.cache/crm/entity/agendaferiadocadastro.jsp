       
<%@page import="imanager.ui.entity.AgendaUI"%>
<%@page import="imanager.misc.NaoSim"%>
<%@page import="imanager.misc.TipoBloqueio"%>
<%@include file="../include/beans.jsp"%>

<%@page import="imanager.entity.*"%>

<%! 
    String EMPRESA_ID = "empresaId";
%>

<%
  // início do bloco protegido
  try {
    // nossa instância de Empresa
    Empresa empresa = (Empresa)facade.getEntity(Empresa.CLASS_NAME);
    // nossa instância de Agenda
    Agenda agenda = (Agenda)facade.getEntity(Agenda.CLASS_NAME);
    // nossa instância de AgendaFeriado
    AgendaFeriado agendaferiado = (AgendaFeriado)facade.getEntity(AgendaFeriado.CLASS_NAME);
    
    // AgendaFeriadoInfo para editar
    AgendaFeriadoInfo agendaFeriadoInfo = null;

    // se estamos inserindo...
    if (Controller.isInserting(request)) {
      // info em branco
      agendaFeriadoInfo = new AgendaFeriadoInfo();
    }
    // se estamos salvando...
    else if (Controller.isSaving(request)) {
      // info preenchido na página
      agendaFeriadoInfo = (AgendaFeriadoInfo)agendaferiado.entityInfoFromRequest(request);
      // se o tipo de bloqueio é por empresa... seta a empresa selecionada
      if (agendaFeriadoInfo.getBloqueio() == TipoBloqueio.EMPRESA) 
        agendaFeriadoInfo.setEmpresaId(selectedEmpresaId);
      // se o tipo de bloqueio é por agenda... seta a empresa e agenda selecionadas
      else if (agendaFeriadoInfo.getBloqueio() == TipoBloqueio.AGENDA) {
        agendaFeriadoInfo.setEmpresaId(selectedEmpresaId);
        agendaFeriadoInfo.setAgendaId(NumberTools.parseInt(request.getParameter("formSelectAgenda")));
      } // else - if
      // se estamos salvando e é um novo...inclui
      if (Controller.isSavingNew(request)) {
        agendaferiado.insert(agendaFeriadoInfo);
      }
      // se estamos salvando e é um existente...atualiza
      else {
        agendaferiado.update(agendaFeriadoInfo);
      } // if
      // compõe a resposta
      Form.composeAjaxResponse(response, Form.COMPOSE_TYPE_JAVA_SCRIPT);
      // atualiza a consulta
      %><%=EntityGrid.forwardBrowse(facade, agendaferiado, AgendaFeriado.ACTION, Controller.isSavingNew(request) ? EntityGrid.OPERATION_INSERT : EntityGrid.OPERATION_UPDATE, agendaFeriadoInfo)%><%
      // dispara
      return;
    }
    // se estamos editando...
    else if (Controller.isEditing(request)) {
      // info do registro para editar
      agendaFeriadoInfo =
        agendaferiado.selectByPrimaryKey(
          NumberTools.parseInt(request.getParameter(AgendaFeriado.FIELD_AGENDA_FERIADO_ID.getFieldAlias()))
        );
    }
    // se o comando é desconhecido...
    else {
      throw new UnknownCommandException();
    } // if

    // nosso FrameBar
    FrameBar frameBar = new FrameBar(facade, "frameBarAgendaFeriado");
    // nosso Form de dados
    Form formData = new Form(request, "formDataAgendaFeriado", AgendaFeriado.ACTION_CADASTRO, AgendaFeriado.COMMAND_SAVE, "", true, true);
%>

<html>
  <head>
    <title><%=AgendaFeriado.ACTION.getCaption()%></title>
    <link href="<%=facade.getStyle().userInterface()%>" rel="stylesheet" type="text/css">
    <script src="include/scripts.js" type="text/javascript"></script>
  </head>
  <body style="margin:0px;" onselectstart="return true;" oncontextmenu="return false;" onload="showHideEmpresaAgenda()">

    <!-- inicia o FrameBar -->
    <%=frameBar.begin()%>

      <!-- área de frames -->
      <%=frameBar.beginFrameArea()%>

        <!-- Frame de identificação do objeto -->
        <%=frameBar.actionFrame(AgendaFeriado.ACTION)%>

        <!-- Frame de comandos -->
        <%=frameBar.beginFrame("Comandos", false, false)%>
          <table style="width:100%;">
            <tr>
              <td><%=CommandControl.formHyperlink(facade, formData, ImageList.COMMAND_SAVE, "Deseja mesmo salvar o registro?", true, false)%></td>
            </tr>
            <tr>
              <td><%=CommandControl.entityFormHyperlink(facade, agendaferiado, AgendaFeriado.ACTION_CADASTRO, AgendaFeriado.COMMAND_INSERT, ImageList.COMMAND_INSERT, false)%></td>
            </tr>
            <tr>
              <td><%=CommandControl.entityBrowseHyperlink(facade, agendaferiado, AgendaFeriado.ACTION, ImageList.COMMAND_BACK, "Voltar", "Volta para a consulta.", false)%></td>
            </tr>
          </table>
        <%=frameBar.endFrame()%>
      <%=frameBar.endFrameArea()%>

      <!-- Form de dados -->
      <%=formData.begin()%>
      
      <script type="text/javascript">
        
        function showHideEmpresaAgenda() {
          var selectBloqueio   = document.getElementById("<%=AgendaFeriado.FIELD_BLOQUEIO.getFieldAlias()%>");
          var selectedBloqueio = selectBloqueio.options[selectBloqueio.selectedIndex].value;
          var agendaLabel      = document.getElementById("labelAgendaId");
          var agendaSelect     = document.getElementById("selectAgendaId");
          var agenda           = document.getElementById("formSelectAgenda");
          var agendaOpcoes     = agenda.options;
          // some e aparece com os tds dependendo do tipo do bloqueio
          agendaLabel.style.display = (selectedBloqueio == <%=TipoBloqueio.AGENDA%> ? "block" : "none");
          agendaSelect.style.display = (selectedBloqueio == <%=TipoBloqueio.AGENDA%> ? "block" : "none");
          // carrega a agenda salva
          for (var i = 0; i < agendaOpcoes.length; i++) {
            if (agendaOpcoes[i].value == <%=agendaFeriadoInfo.getAgendaId()%>) {
              agendaOpcoes[i].selected = true;
              return;
            }
          }
        }
        
      </script>
        
      <%// lista as agendas
        AgendaInfo[] agendaInfoList = agenda.selectByFilter(selectedEmpresaId, "", null);
        String[] agendaOptions = new String[agendaInfoList.length];
        String[] agendaValues  = new String[agendaInfoList.length];
        // loop nas agendas
        for (int i = 0; i < agendaInfoList.length; i++) {
          // armazena as agendas e seus valores
          agendaOptions[i] = agendaInfoList[i].getNome();
          agendaValues[i]  = agendaInfoList[i].getAgendaId() + "";
        } // for
      %>

        <!-- área de dados -->
        <%=frameBar.beginClientArea()%>
        <!-- dados ocultos -->
        <%=EntityFormEdit.script(facade, AgendaFeriado.FIELD_AGENDA_FERIADO_ID, agendaFeriadoInfo, request, -1, "", "")%>
        
          <!-- dados de AgendaFeriado -->
          <table style="width:100%">
            <tr>
              <td style="width:auto;">
                <%=GroupBox.begin("Identificação")%>
                <table style="width:100%">
                  <tr>
                    <td style="width:auto"><%=EntityFieldLabel.script(facade, AgendaFeriado.FIELD_NOME, request)%></td>
                    <td style="width:90px"><%=EntityFieldLabel.script(facade, AgendaFeriado.FIELD_FERIADO, request)%></td>
                  </tr>
                  <tr>
                    <td><%=EntityFormEdit.script(facade, AgendaFeriado.FIELD_NOME, agendaFeriadoInfo, request, 0, "", "")%></td>
                    <td><%=EntityFormEdit.script(facade, AgendaFeriado.FIELD_FERIADO, agendaFeriadoInfo, request, 0, "", "")%></td>
                  </tr>
                  </table>
                <%=GroupBox.end()%>
              </td>
              <td style="width:300px;">
                <%=GroupBox.begin("Bloqueio")%>
                  <table>
                    <tr>
                      <td style="width:100px"><%=EntityFieldLabel.script(facade, AgendaFeriado.FIELD_BLOQUEIO, request)%></td>
                      <td style="width:200px" id="labelAgendaId" style="display:none"><%=EntityFieldLabel.script(facade, AgendaFeriado.FIELD_AGENDA_ID, request)%></td>
                    </tr>
                    <tr>
                      <td style="width:auto"><%=EntityFormSelect.script(facade, AgendaFeriado.FIELD_BLOQUEIO, agendaFeriadoInfo, request, 0, "", "showHideEmpresaAgenda()", false)%></td>
                      <td id="selectAgendaId" style="display:none"><%=FormSelect.script(facade, "formSelectAgenda",  agendaOptions, agendaValues, "", FormSelect.SELECT_TYPE_SINGLE, "")%></td>
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
