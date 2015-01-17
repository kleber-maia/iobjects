
<%@include file="../include/beans.jsp"%>

<%@page import="imanager.entity.*"%>

<%
  // início do bloco protegido
  try {
    // nossa instância de Agenda
    Agenda agenda = (Agenda)facade.getEntity(Agenda.CLASS_NAME);

    // AgendaInfo para editar
    AgendaInfo agendaInfo = null;

    // se estamos inserindo...
    if (Controller.isInserting(request)) {
      // info em branco
      agendaInfo = new AgendaInfo();
      // insere a empresa
      agendaInfo.setEmpresaId(selectedEmpresaId);
      agendaInfo.setTabelaHorario(Agenda.TABELA_HORARIO_COMERCIAL);
    }
    // se estamos salvando...
    else if (Controller.isSaving(request)) {
      // info preenchido na página
      agendaInfo = (AgendaInfo)agenda.entityInfoFromRequest(request);

      // obtém os horários permitidos por dia
      boolean[][] arrayHorario = new boolean[7][24];
      for (int i=0; i<7; i++) {
        // horários marcados na página para o dia
        String[] stringDay = request.getParameterValues("d" + i);
        // se temos horários para o dia...
        if (stringDay != null) {
          // marca os horários no nosso array
          for (int w=0; w<stringDay.length; w++)
            arrayHorario[i][NumberTools.parseInt(stringDay[w])] = true;
        } // if
      } // for

      // define a nova Tabela Horário
      agendaInfo.setTabelaHorario(agenda.encodeTabelaHorario(arrayHorario));

      // se estamos salvando e é um novo...inclui
      if (Controller.isSavingNew(request)) {
        agenda.insert(agendaInfo);
      }
      // se estamos salvando e é um existente...atualiza
      else {
        agenda.update(agendaInfo);
      } // if
      // compõe a resposta
      Form.composeAjaxResponse(response, Form.COMPOSE_TYPE_JAVA_SCRIPT);
      // atualiza a consulta
      %><%=EntityGrid.forwardBrowse(facade, agenda, Agenda.ACTION, Controller.isSavingNew(request) ? EntityGrid.OPERATION_INSERT : EntityGrid.OPERATION_UPDATE, agendaInfo)%><%
      // dispara
      return;
    }
    // se estamos editando...
    else if (Controller.isEditing(request)) {
      // info do registro para editar
      agendaInfo =
        agenda.selectByPrimaryKey(
          NumberTools.parseInt(request.getParameter(Agenda.FIELD_EMPRESA_ID.getFieldAlias())),
          NumberTools.parseInt(request.getParameter(Agenda.FIELD_AGENDA_ID.getFieldAlias()))
        );
    }
    // se o comando é desconhecido...
    else {
      throw new UnknownCommandException();
    } // if

    // nosso FrameBar
    FrameBar frameBar = new FrameBar(facade, "frameBarAgenda");
    // nosso Form de dados
    Form formData = new Form(request, "formDataAgendaTabelaHorario", Agenda.ACTION_CADASTRO, Agenda.COMMAND_SAVE, "", true, true, true);
    PageControl pageControl = new PageControl(facade, "pageControlAgenda", true);
    pageControl.setTabWidth(120);

    // obtém os horários permitidos por dia
    boolean[][] arrayHorario = agenda.decodeTabelaHorario(agendaInfo.getTabelaHorario());

    // nosso Grid de horário
    Grid gridHorario = new Grid(facade, "gridHorario", 0, 0);
    gridHorario.columns().add(new Grid.Column("!", 50, Grid.ALIGN_CENTER));
    gridHorario.columns().add(new Grid.Column("<a href='javascript:checkUncheckAll(formDataAgendaTabelaHorario.d0);' title='Marca/desmarca todos.'>Domingo</a>", 100, Grid.ALIGN_CENTER));
    gridHorario.columns().add(new Grid.Column("<a href='javascript:checkUncheckAll(formDataAgendaTabelaHorario.d1);' title='Marca/desmarca todos.'>Segunda</a>", 100, Grid.ALIGN_CENTER));
    gridHorario.columns().add(new Grid.Column("<a href='javascript:checkUncheckAll(formDataAgendaTabelaHorario.d2);' title='Marca/desmarca todos.'>Terça</a>", 100, Grid.ALIGN_CENTER));
    gridHorario.columns().add(new Grid.Column("<a href='javascript:checkUncheckAll(formDataAgendaTabelaHorario.d3);' title='Marca/desmarca todos.'>Quarta</a>", 100, Grid.ALIGN_CENTER));
    gridHorario.columns().add(new Grid.Column("<a href='javascript:checkUncheckAll(formDataAgendaTabelaHorario.d4);' title='Marca/desmarca todos.'>Quinta</a>", 100, Grid.ALIGN_CENTER));
    gridHorario.columns().add(new Grid.Column("<a href='javascript:checkUncheckAll(formDataAgendaTabelaHorario.d5);' title='Marca/desmarca todos.'>Sexta</a>", 100, Grid.ALIGN_CENTER));
    gridHorario.columns().add(new Grid.Column("<a href='javascript:checkUncheckAll(formDataAgendaTabelaHorario.d6);' title='Marca/desmarca todos.'>Sábado</a>", 100, Grid.ALIGN_CENTER));
%>

<html>
  <head>
    <title><%=Agenda.ACTION.getCaption()%></title>
    <link href="<%=facade.getStyle().userInterface()%>" rel="stylesheet" type="text/css">
    <script src="include/scripts.js" type="text/javascript"></script>
  </head>
  <body style="margin:0px;" onselectstart="return true;" oncontextmenu="return true;">

    <script type="text/javascript">
      
      function checkUncheckAll(day) {
        // iremos marcar ou desmarcar todos?
        var checked = !day[0].checked;
        // loop nos horários do dia
        for (i=0; i<day.length; i++) {
          day[i].checked = checked;
        } // for
      }

    </script>

    <!-- inicia o FrameBar -->
    <%=frameBar.begin()%>

      <!-- área de frames -->
      <%=frameBar.beginFrameArea()%>

        <!-- Frame de identificação do objeto -->
        <%=frameBar.actionFrame(Agenda.ACTION)%>

        <!-- Frame de comandos -->
        <%=frameBar.beginFrame("Comandos", false, false)%>
          <table style="width:100%;">
            <tr>
              <td><%=CommandControl.formHyperlink(facade, formData, ImageList.COMMAND_SAVE, "Deseja mesmo salvar o registro?", true, false)%></td>
            </tr>
            <tr>
              <td><%=CommandControl.entityFormHyperlink(facade, agenda, Agenda.ACTION_CADASTRO, Agenda.COMMAND_INSERT, ImageList.COMMAND_INSERT, false)%></td>
            </tr>
            <tr>
              <td><%=CommandControl.entityBrowseHyperlink(facade, agenda, Agenda.ACTION, ImageList.COMMAND_BACK, "Voltar", "Volta para a consulta.", false)%></td>
            </tr>
          </table>
        <%=frameBar.endFrame()%>
      <%=frameBar.endFrameArea()%>

      <!-- área de dados -->
      <%=frameBar.beginClientArea()%>

        <!-- Form de dados -->
        <%=formData.begin()%>
          <! dados ocultos-->
            <%=EntityFormEdit.script(facade, Agenda.FIELD_EMPRESA_ID, agendaInfo, request, -1, "", "")%>
            <%=EntityFormEdit.script(facade, Agenda.FIELD_AGENDA_ID, agendaInfo, request, -1, "", "")%>
          <!-- dados de Agenda -->
          <table style="width:70%">
            <!-- campo Nome -->
            <tr style="width:100%">
              <td style="width:auto;"><%=EntityFieldLabel.script(facade, Agenda.FIELD_NOME, request)%></td>
              <td style="width:80px;"><%=EntityFieldLabel.script(facade, Agenda.FIELD_INTERVALO, request)%></td>
            </tr>
            <tr style="width:100%">
              <td style="width:auto;"><%=EntityFormEdit.script(facade, Agenda.FIELD_NOME, agendaInfo, request, 0, "", "")%></td>
              <td style="width:80px;"><%=EntityFormEdit.script(facade, Agenda.FIELD_INTERVALO, agendaInfo, request, 0, "", "")%></td>
            </tr>
            <tr>
            <%=pageControl.begin()%>

              <%=pageControl.beginTabSheet("Horários")%>
                <!-- tabela de horário -->
                <table style="width:100%; height:100%;">
                  <tr style="height:auto;">
                    <td>
                      <%=gridHorario.begin()%>
                        <%for (int i=0; i<24; i++) {%>
                        <%=gridHorario.addRow(new String[]{i + "h",
                                              "<input name='d0' id='d0' type='checkbox' value='" + i + "' " + (arrayHorario[0][i] ? "checked=checked" : "") + " style='height:15px;' />",
                                              "<input name='d1' id='d1' type='checkbox' value='" + i + "' " + (arrayHorario[1][i] ? "checked=checked" : "") + " style='height:15px;' />",
                                              "<input name='d2' id='d2' type='checkbox' value='" + i + "' " + (arrayHorario[2][i] ? "checked=checked" : "") + " style='height:15px;' />",
                                              "<input name='d3' id='d3' type='checkbox' value='" + i + "' " + (arrayHorario[3][i] ? "checked=checked" : "") + " style='height:15px;' />",
                                              "<input name='d4' id='d4' type='checkbox' value='" + i + "' " + (arrayHorario[4][i] ? "checked=checked" : "") + " style='height:15px;' />",
                                              "<input name='d5' id='d5' type='checkbox' value='" + i + "' " + (arrayHorario[5][i] ? "checked=checked" : "") + " style='height:15px;' />",
                                              "<input name='d6' id='d6' type='checkbox' value='" + i + "' " + (arrayHorario[6][i] ? "checked=checked" : "") + " style='height:15px;' />"
                                              })%>
                        <%} // for%>
                      <%=gridHorario.end()%>
                    </td>
                  </tr>
                </table>
              <%=pageControl.endTabSheet()%>

            <%=pageControl.end()%>
            </tr>
          </table>
        <%=formData.end()%>

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
