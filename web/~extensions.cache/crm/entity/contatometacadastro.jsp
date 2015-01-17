
<%@include file="../include/beans.jsp"%>

<%@page import="imanager.entity.*"%>

<%
  // início do bloco protegido
  try {
    // nossa instância de Contato Meta
    ContatoMeta contatoMeta = (ContatoMeta)facade.getEntity(ContatoMeta.CLASS_NAME);

    // ContatoMetaInfo para editar
    ContatoMetaInfo contatoMetaInfo = null;

    // se estamos inserindo...
    if (Controller.isInserting(request)) {
      // info em branco
      contatoMetaInfo = new ContatoMetaInfo();
      // valores padrão
      contatoMetaInfo.setEmpresaId(selectedEmpresaId);
      contatoMetaInfo.setContatoId(contatoMeta.USER_PARAM_CONTATO_ID.getIntValue());
      try {
        // localiza a Meta mais recente para aproveitar seus dados
        ContatoMetaInfo lastContatoMetaInfo = contatoMeta.selectLast(selectedEmpresaId, contatoMetaInfo.getContatoId());
        // define o próximo mês e ano
        contatoMetaInfo.setAno(lastContatoMetaInfo.getAno());
        contatoMetaInfo.setMes(lastContatoMetaInfo.getMes()+1);
        contatoMetaInfo.setMetaVenda(lastContatoMetaInfo.getMetaVenda());
        contatoMetaInfo.setMetaServico(lastContatoMetaInfo.getMetaServico());
        // se passamos de ano...
        if (contatoMetaInfo.getMes() > 12) {
          contatoMetaInfo.setAno(contatoMetaInfo.getAno() + 1);
          contatoMetaInfo.setMes(1);
        } // if
      }
      catch (RecordNotFoundException e) {
        // define o mês e ano atuais
        contatoMetaInfo.setAno(DateTools.getActualYear());
        contatoMetaInfo.setMes(DateTools.getActualMonth());
      } // try-catch
    }
    // se estamos salvando...
    else if (Controller.isSaving(request)) {
      // info preenchido na página
      contatoMetaInfo = (ContatoMetaInfo)contatoMeta.entityInfoFromRequest(request);
      // se estamos salvando e é um novo...inclui
      if (Controller.isSavingNew(request)) {
        contatoMeta.insert(contatoMetaInfo);
      }
      // se estamos salvando e é um existente...atualiza
      else {
        contatoMeta.update(contatoMetaInfo);
      } // if
      // compõe a resposta
      Form.composeAjaxResponse(response, Form.COMPOSE_TYPE_JAVA_SCRIPT);
      // atualiza a consulta
      %><%=EntityGrid.forwardBrowse(facade, contatoMeta, ContatoMeta.ACTION, Controller.isSavingNew(request) ? EntityGrid.OPERATION_INSERT : EntityGrid.OPERATION_UPDATE, contatoMetaInfo)%><%
      // dispara
      return;
    }
    // se estamos editando...
    else if (Controller.isEditing(request)) {
      // info do registro para editar
      contatoMetaInfo =
        contatoMeta.selectByPrimaryKey(
          NumberTools.parseInt(request.getParameter(ContatoMeta.FIELD_EMPRESA_ID.getFieldAlias())),
          NumberTools.parseInt(request.getParameter(ContatoMeta.FIELD_CONTATO_ID.getFieldAlias())),
          NumberTools.parseInt(request.getParameter(ContatoMeta.FIELD_ANO.getFieldAlias())),
          NumberTools.parseInt(request.getParameter(ContatoMeta.FIELD_MES.getFieldAlias()))
        );
    }
    // se o comando é desconhecido...
    else {
      throw new UnknownCommandException();
    } // if

    // nosso Form de dados
    Form formData = new Form(request, "formDataContatoMeta", ContatoMeta.ACTION_CADASTRO, ContatoMeta.COMMAND_SAVE, "", true, true);
%>

<html>
  <head>
    <title><%=ContatoMeta.ACTION.getCaption()%></title>
    <link href="<%=facade.getStyle().userInterface()%>" rel="stylesheet" type="text/css">
    <script src="include/scripts.js" type="text/javascript"></script>
  </head>
  <body style="margin:0px;" onselectstart="return true;" oncontextmenu="return false;">

    <!-- Form de dados -->
    <%=formData.begin()%>
      <!-- dados ocultos -->
      <%=EntityFormEdit.script(facade, ContatoMeta.FIELD_EMPRESA_ID, contatoMetaInfo, request, -1, "", "")%>
      <%=EntityFormEdit.script(facade, ContatoMeta.FIELD_CONTATO_ID, contatoMetaInfo, request, -1, "", "")%>
      <!-- dados de Contato Meta -->
      <%=GroupBox.begin("Identificação")%>
        <table>
          <tr>
            <td style="width:80px;">Mês/Ano*</td>
            <td style="width:80px;"><%=EntityFieldLabel.script(facade, ContatoMeta.FIELD_META_VENDA, request)%></td>
            <td style="width:80px;"><%=EntityFieldLabel.script(facade, ContatoMeta.FIELD_META_SERVICO, request)%></td>
          </tr>
          <tr>
            <td>
              <%=EntityFormEdit.script(facade, ContatoMeta.FIELD_MES, contatoMetaInfo, request, 25, "", "")%>
              /
              <%=EntityFormEdit.script(facade, ContatoMeta.FIELD_ANO, contatoMetaInfo, request, 40, "", "")%>
            </td>
            <td><%=EntityFormEdit.script(facade, ContatoMeta.FIELD_META_VENDA, contatoMetaInfo, request, 100, "", "")%></td>
            <td><%=EntityFormEdit.script(facade, ContatoMeta.FIELD_META_SERVICO, contatoMetaInfo, request, 100, "", "")%></td>
          </tr>
        </table>
      <%=GroupBox.end()%>
    <%=formData.end()%>

    <%=CommandControl.formButton(facade, formData, ImageList.COMMAND_SAVE, "Deseja mesmo salvar o registro?", true, false)%>&nbsp;
    <%=CommandControl.entityFormButton(facade, contatoMeta, ContatoMeta.ACTION_CADASTRO, ContatoMeta.COMMAND_INSERT, ImageList.COMMAND_INSERT, false)%>&nbsp;
    <%=CommandControl.entityBrowseButton(facade, contatoMeta, ContatoMeta.ACTION, ImageList.COMMAND_BACK, "Voltar", "Volta para a consulta.", false)%>

  </body>
</html>

<%}
  // término do bloco protegido
  catch (Exception e) {
    Controller.forwardException(e, pageContext);
  } // try-catch
%>
