<%@include file="../../include/beans.jsp"%>

<%@page import="java.net.*"%>
<%@page import="java.sql.*"%>
<%@page import="java.util.*"%>

<%!
  public final String SELECTOR        = "selector";
  public final String SEARCH_FIELD    = "searchField";
  public final String SEARCH_VALUE    = "searchValue";

  public ResultSet getResultSet(Connection connection,
                                String     tableName,
                                String[]   displayFieldNames,
                                String[]   returnFieldNames,
                                String[]   returnUserFieldNames,
                                String[]   orderFieldNames,
                                String     filterExpression,
                                String     extraFilterExpression) throws Exception {
    // campos que iremos utilizar
    String[] fields = sumStringArrays(displayFieldNames, returnFieldNames);
    fields = sumStringArrays(fields, returnUserFieldNames);
    // cláusula where
    String where = filterExpression;
    if (!extraFilterExpression.equals(""))
      where = where.equals("") ? extraFilterExpression : "(" + where + ") AND (" + extraFilterExpression + ")";
    // entidade de consulta
    PreparedStatement preparedStatement = SqlTools.prepareSelect(connection,
                                                                 tableName,
                                                                 fields,
                                                                 orderFieldNames,
                                                                 where);
    preparedStatement.execute();
    return preparedStatement.getResultSet();
  }

  public String getRowClass(int rowIndex) {
    if (rowIndex % 2 > 0)
      return "GridRowEven";
    else
      return "GridRowOdd";
  }

  public String getSelector(byte      selectType,
                            ResultSet resultSet,
                            String[]  returnFieldNames,
                            String[]  returnFieldValues,
                            String[]  returnUserFieldNames) throws Exception {
    // tipo da seleção: radio ou checkbox?
    String type = selectType == ExternalLookup.SELECT_TYPE_SINGLE ? "radio" : "checkbox";
    // valores do selecionador
    String value     = "";
    String userValue = "";
    for (int i=0; i<returnFieldNames.length; i++) {
      if (!value.equals(""))
        value += ";";
      value = resultSet.getString(returnFieldNames[i]);
    } // for
    for (int i=0; i<returnUserFieldNames.length; i++) {
      if (!userValue.equals(""))
        userValue += " - ";
      userValue += resultSet.getString(returnUserFieldNames[i]);
    } // for
    // exclui os separadores que não possuírem valor entre si
    userValue = userValue.replaceAll(" -  - ", "");
    // devemos selecionar?
    String checked = "";
    if (StringTools.arrayContains(returnFieldValues, value))
      checked = " checked='checked'";
    // retorna o selecionador
    return "<input type='" + type + "' name='" + SELECTOR + "' value='" + value + "'" + checked + ">"
         + "<input type='hidden' id='" + SELECTOR + ExternalLookup.USER + "' value='" + userValue + "'>";
  }

  public int[] getIntArray(String value) {
    StringTokenizer tokenizer = new StringTokenizer(value, ";", false);
    int[] result = new int[tokenizer.countTokens()];
    for (int i=0; i<result.length; i++)
      result[i] = Integer.parseInt(tokenizer.nextToken());
    return result;
  }

  public String[] getStringArray(String value) {
    StringTokenizer tokenizer = new StringTokenizer(value, ";", false);
    String[] result = new String[tokenizer.countTokens()];
    for (int i=0; i<result.length; i++)
      result[i] = tokenizer.nextToken();
    return result;
  }

  public String[] sumStringArrays(String[] array1, String[] array2) {
    List list1 = Arrays.asList(array1);
    List list2 = Arrays.asList(array2);
    HashSet hashSet = new HashSet(list1.size() + list2.size());
    hashSet.addAll(list1);
    hashSet.addAll(list2);
    String[] result = {};
    result = (String[])hashSet.toArray(result);
    return result;
  }

  static private String getValuesFromArray(String[] array) {
    String result = "";
    for (int i=0; i<array.length; i++)
      if (result.equals(""))
        result = array[i];
      else
        result += ";" + array[i];
    return result;
  }

  static private String getValuesFromArray(int[] array) {
    String result = "";
    for (int i=0; i<array.length; i++)
      if (result.equals(""))
        result = array[i] + "";
      else
        result += ";" + array[i];
    return result;
  }

%>

<%
  // inicia transação
  facade.beginTransaction();
  try {
    // nossos parâmetros
    String     className            = request.getParameter(ExternalLookup.CLASS_NAME);
    String     tableName            = request.getParameter(ExternalLookup.TABLE_NAME);
    String[]   searchFieldNames     = getStringArray(request.getParameter(ExternalLookup.SEARCH_FIELD_NAMES));
    String[]   searchFieldTitles    = getStringArray(request.getParameter(ExternalLookup.SEARCH_FIELD_TITLES));
    int[]      searchFieldFormats   = getIntArray(request.getParameter(ExternalLookup.SEARCH_FIELD_FORMATS));
    byte       searchLike           = Byte.parseByte(request.getParameter(ExternalLookup.SEARCH_LIKE));
    String[]   displayFieldNames    = getStringArray(request.getParameter(ExternalLookup.DISPLAY_FIELD_NAMES));
    String[]   displayFieldTitles   = getStringArray(request.getParameter(ExternalLookup.DISPLAY_FIELD_TITLES));
    int[]      displayFieldWidths   = getIntArray(request.getParameter(ExternalLookup.DISPLAY_FIELD_WIDTHS));
    String[]   returnFieldNames     = getStringArray(request.getParameter(ExternalLookup.RETURN_FIELD_NAMES));
    String[]   returnFieldValues    = getStringArray(request.getParameter(ExternalLookup.RETURN_FIELD_VALUES));
    String[]   returnUserFieldNames = getStringArray(request.getParameter(ExternalLookup.RETURN_USER_FIELD_NAMES));
    String[]   orderFieldNames      = getStringArray(request.getParameter(ExternalLookup.ORDER_FIELD_NAMES));
    String     filterExpression     = StringTools.decodeURL(request.getParameter(ExternalLookup.FILTER_EXPRESSION));
    byte       selectType           = Byte.parseByte(request.getParameter(ExternalLookup.SELECT_TYPE));
    String     windowTitle          = request.getParameter(ExternalLookup.WINDOW_TITLE);
    String     externalLookupId     = request.getParameter(ExternalLookup.EXTERNAL_LOOKUP_ID);
    // conexão que iremos utilizar
    Connection connection = facade.getConnectionForClassName(className);
    // nosso conjunto de dados
    ResultSet resultSet = null;
    boolean   hasRecords = false;
    // nossos parâmetros
    Param paramSearchField = new Param(SEARCH_FIELD, "");
    Param paramSearchValue = new Param(SEARCH_VALUE, "");
    // obtém os valores da requisição
    paramSearchField.setValue(request);
    paramSearchValue.setValue(request);
    // retira os caracteres inválidos ' e " do valor de pesquisa
    paramSearchValue.setValue(paramSearchValue.getValue().replaceAll("'", "").replaceAll("\"", ""));
    // se temos campos de pesquisa
    if (searchFieldNames.length > 0) {
      // constraints especiais
      paramSearchValue.setSpecialConstraint(true, true);
      // se ainda não temos um campo de pesquisa...seleciona o primeiro como padrão
      if (paramSearchField.getValue().equals(""))
        paramSearchField.setValue(searchFieldNames[0]);
      // formato do parâmetro de pesquisa
      paramSearchValue.setFormat(searchFieldFormats[StringTools.arrayIndexOf(searchFieldNames, paramSearchField.getValue())]);
    } // if
    // se não temos campos de pesquisa...
    if (searchFieldNames.length == 0) {
      resultSet = getResultSet(connection,
                               tableName,
                               displayFieldNames,
                               returnFieldNames,
                               returnUserFieldNames,
                               orderFieldNames,
                               filterExpression,
                               "");
    }
    // se temos campos de pesquisa e um valor para pesquisar...
    else if (!paramSearchValue.getValue().equals("")) {
      // filtro extra baseado no valor informado
      String extraFilterExpression = paramSearchField.getValue()
                                   + " LIKE '"
                                   + (searchLike == ExternalLookup.SEARCH_LIKE_SUFFIX ? "%" : "")
                                   + (searchLike == ExternalLookup.SEARCH_LIKE_BOTH ? "%" : "")
                                   + paramSearchValue.getFormatedValue()
                                   + (searchLike == ExternalLookup.SEARCH_LIKE_PREFIX ? "%" : "")
                                   + (searchLike == ExternalLookup.SEARCH_LIKE_BOTH ? "%" : "")
                                   + "'";
      // vamos ordenar também pelo campo de pesquisa
      String[] newOrderFieldNames = orderFieldNames;
      if (!StringTools.arrayContains(newOrderFieldNames, paramSearchField.getValue()))
        newOrderFieldNames = StringTools.arrayConcat(newOrderFieldNames, paramSearchField.getValue());
      // pega o ResultSet
      resultSet = getResultSet(connection,
                               tableName,
                               displayFieldNames,
                               returnFieldNames,
                               returnUserFieldNames,
                               newOrderFieldNames,
                               filterExpression,
                               extraFilterExpression);
    } // if

    // nosso Form
    Form formPesquisa = new Form(request, "formPesquisa", Controller.ACTION_EXTERNAL_LOOKUP, Controller.COMMAND_EXTERNAL_LOOKUP_SEARCH, "", false);
%>



<html>
  <head>
    <title><%=facade.applicationInformation().getName() + " - " + windowTitle%></title>
    <link href="<%=facade.getStyle().userInterface()%>" rel="stylesheet" type="text/css">
    <script src="include/scripts.js" type="text/javascript"></script>
  </head>

  <body style="margin: 0px 0px 0px 0px;" onselectstart="return true;" oncontextmenu="return false;">

    <script language="javascript" type="">

      <%// se o tipo de seleção é múltipla...
        if (selectType == ExternalLookup.SELECT_TYPE_MULTIPLE) {%>
      function optionExists(optionValue) {
        // loop nos options locais
        for (w=0; w<formPesquisa.localSelect.options.length; w++) {
          // se tem o valor procurado...retorna ok
          if (formPesquisa.localSelect.options[w].value == optionValue)
            return true;
        } // for
        // se chegou até aqui...não achamos nada
        return false;
      }

      function populateLocalOptions() {
        // select da página que nos abriu
        externalSelect = window.opener.document.all.<%=externalLookupId%>;
        // loop nos options do select recebido
        for (i=0; i<externalSelect.options.length; i++) {
          // option externo da vez
          externalOption = externalSelect.options[i];
          // novo option local
          localOption = window.document.createElement("OPTION");
          localOption.value = externalOption.value;
          localOption.text  = externalOption.text;
          // adiciona na lista local
          formPesquisa.localSelect.add(localOption);
        } // for
      }
      <%}// if%>

      function insertSelection() {
        // externalLookup na janela que nos abriu
        externalLookup = window.opener.document.all.<%=externalLookupId%>;
      <%// se o tipo de seleção é simples...
        if (selectType == ExternalLookup.SELECT_TYPE_SINGLE) {%>
        // input para o usuário
        externalLookupUser = window.opener.document.all.<%=externalLookupId + ExternalLookup.USER%>;
        // botão de pesquisa
        externalLookupSearch = window.opener.document.all.<%=externalLookupId + ExternalLookup.SEARCH%>;
        // mostra ao usuário que o valor foi alterado
        externalLookupUser.style.color = "#0000FF";
        // torna Read-Only
        externalLookupUser.readOnly = true;
        try {
          externalLookupSearch.focus();
        }
        catch (e) {
        } // try-catch
        // se só temos 1 selector
        if (formPesquisa.<%=SELECTOR%>.length == undefined) {
          // único selector
          selectorItem     = formPesquisa.<%=SELECTOR%>;
          userSelectorItem = formPesquisa.<%=SELECTOR + ExternalLookup.USER%>;
          // se está selecionado...usa seu valor
          if (selectorItem.checked) {
            externalLookup.value = selectorItem.value;
            externalLookupUser.value = userSelectorItem.value;
          } // if
        }
        // se tem vários selectors
        else {
          // loop nos selectors
          for (i=0; i<formPesquisa.<%=SELECTOR%>.length; i++) {
            // selector da vez
            selectorItem     = formPesquisa.<%=SELECTOR%>[i];
            userSelectorItem = formPesquisa.<%=SELECTOR + ExternalLookup.USER%>[i];
            // se está selecionado...usa seu valor
            if (selectorItem.checked) {
              externalLookup.value = selectorItem.value;
              externalLookupUser.value = userSelectorItem.value;
              break;
            } // if
          } // for
        } // if
      <%}
        // se o tipo de seleção é múltipla...
        else {%>
        // mostra ao usuário que o valor foi alterado
        externalLookup.style.color = "#0000FF";
        // pega as opções atuais para pesquisa
        populateLocalOptions();
        // se só temos 1 selector
        if (formPesquisa.<%=SELECTOR%>.length == undefined) {
          // único selector
          selectorItem     = formPesquisa.<%=SELECTOR%>;
          userSelectorItem = formPesquisa.<%=SELECTOR + ExternalLookup.USER%>;
          // se está selecionado...usa seu valor
          if (selectorItem.checked) {
            // se o item ainda não existe...
            if (!optionExists(selectorItem.value)) {
              newOption = window.opener.document.createElement("OPTION");
              newOption.text = userSelectorItem.value;
              newOption.value = selectorItem.value;
              externalLookup.options.add(newOption);
            } // if
          } // if
        }
        // se tem vários selectors
        else {
          // loop nos selectors
          for (i=0; i<formPesquisa.<%=SELECTOR%>.length; i++) {
            // selector da vez
            selectorItem     = formPesquisa.<%=SELECTOR%>[i];
            userSelectorItem = formPesquisa.<%=SELECTOR + ExternalLookup.USER%>[i];
            // se está selecionado...usa seu valor
            if (selectorItem.checked) {
              // se o item ainda não existe...
              if (!optionExists(selectorItem.value)) {
                newOption = window.opener.document.createElement("OPTION");
                newOption.text = userSelectorItem.value;
                newOption.value = selectorItem.value;
                externalLookup.options.add(newOption);
              } // if
            } // if
          } // for
        } // if
        // seleciona todos os options do ExternalLookp
        for (i=0; i<externalLookup.options.length; i++) {
          externalLookup.options[i].selected = true;
        } // for
      <%} // if%>
        // chama a função onChange
        window.opener.<%=externalLookupId%>OnChange();
        // fecha nossa janela
        close();
      }

    </script>

    <%=formPesquisa.begin()%>
      <input type="hidden" name="<%=ExternalLookup.CLASS_NAME%>" value="<%=className%>"/>
      <input type="hidden" name="<%=ExternalLookup.TABLE_NAME%>" value="<%=tableName%>"/>
      <input type="hidden" name="<%=ExternalLookup.SEARCH_FIELD_NAMES%>" value="<%=getValuesFromArray(searchFieldNames)%>"/>
      <input type="hidden" name="<%=ExternalLookup.SEARCH_FIELD_TITLES%>" value="<%=getValuesFromArray(searchFieldTitles)%>"/>
      <input type="hidden" name="<%=ExternalLookup.SEARCH_FIELD_FORMATS%>" value="<%=getValuesFromArray(searchFieldFormats)%>"/>
      <input type="hidden" name="<%=ExternalLookup.SEARCH_LIKE%>" value="<%=searchLike%>"/>
      <input type="hidden" name="<%=ExternalLookup.DISPLAY_FIELD_NAMES%>" value="<%=getValuesFromArray(displayFieldNames)%>"/>
      <input type="hidden" name="<%=ExternalLookup.DISPLAY_FIELD_TITLES%>" value="<%=getValuesFromArray(displayFieldTitles)%>"/>
      <input type="hidden" name="<%=ExternalLookup.DISPLAY_FIELD_WIDTHS%>" value="<%=getValuesFromArray(displayFieldWidths)%>"/>
      <input type="hidden" name="<%=ExternalLookup.RETURN_FIELD_NAMES%>" value="<%=getValuesFromArray(returnFieldNames)%>"/>
      <input type="hidden" name="<%=ExternalLookup.RETURN_FIELD_VALUES%>" value="<%=getValuesFromArray(returnFieldValues)%>"/>
      <input type="hidden" name="<%=ExternalLookup.RETURN_USER_FIELD_NAMES%>" value="<%=getValuesFromArray(returnUserFieldNames)%>"/>
      <input type="hidden" name="<%=ExternalLookup.ORDER_FIELD_NAMES%>" value="<%=getValuesFromArray(orderFieldNames)%>"/>
      <input type="hidden" name="<%=ExternalLookup.FILTER_EXPRESSION%>" value="<%=StringTools.encodeURL(filterExpression)%>"/>
      <input type="hidden" name="<%=ExternalLookup.SELECT_TYPE%>" value="<%=selectType%>"/>
      <input type="hidden" name="<%=ExternalLookup.WINDOW_TITLE%>" value="<%=windowTitle%>"/>
      <input type="hidden" name="<%=ExternalLookup.EXTERNAL_LOOKUP_ID%>" value="<%=externalLookupId%>"/>

      <script type="text/javascript">
        function checkConstraints() {
          // se não informou nada...dispara
          if (formPesquisa.<%=SEARCH_VALUE%>.value.length == 0)
            return false;
          <%// temos constraint especial de tamanho mínimo?
            if ((Param.getParamFile() != null) && (Param.getParamFile().specialConstraints().getMinimumLength() > 0)) {%>
            // se não tem o tamanho mínimo...avisa
            if (formPesquisa.<%=SEARCH_VALUE%>.value.length < <%=Param.getParamFile().specialConstraints().getMinimumLength()%>) {
              alert("O valor de pesquisa deve conter pelo menos <%=Param.getParamFile().specialConstraints().getMinimumLength()%> caracteres.");
              return false;
            } // if
          <%} // if%>
          // se chegou até aqui...retorna OK
          return true;
        }
      </script>

      <!-- elementos da janela -->
      <table style="width:100%; height:100%;" cellpadding="0" cellspacing="0">
        <!-- barra de título -->
        <tr style="height:62px;">
          <td class="Window">

            <!-- identificação e ícone -->
            <table style="width:100%; height:100%;" cellpadding="4">
              <tr>
                <td style="width:auto;">

                  <!-- identificação -->
                  <table style="width:100%; height:100%;">
                    <tr>
                      <td colspan="2"><b><%=windowTitle%></b></td>
                    </tr>
                    <tr>
                      <td width="10">
                      </td>
                      <td>
                      <%if (searchFieldNames.length > 0) {%>
                      Escolha o campo de pesquisa, informe o valor para pesquisar e em
                      seguinda clique em 'Pesquisar'.
                      <%} // if%>
                      Selecione o item de sua preferência e clique em 'OK' para efetivar
                      sua seleção.
                      </td>
                    </tr>
                  </table>

                </td>
                <td style="width:50px;">

                  <!-- ícone -->
                  <table class="ActiveCaption" cellspacing="4" style="width:100%; height:100%;">
                    <tr>
                      <td class="Window" align="center" valign="middle">
                        <img src="images/externallookup32x32.png" alt="" />
                      </td>
                    </tr>
                  </table>

                </td>
              </tr>
            </table>


          </td>
        </tr>

        <!-- divisão da barra de título -->
        <tr style="height:4px;">
          <td valign="top">

            <table style="width:100%; border-top:1px solid;">
              <tr><td></td></tr>
            </table>

          </td>
        </tr>


        <!-- área de pesquisa -->
        <%if (searchFieldNames.length > 0) {%>
        <tr style="height:40px;">
          <td>

            <table class="BtnFace" cellpadding="4" style="width:100%;">
              <tr>
                <td>
                  <table style="width:100%;">
                    <tr>
                      <td style="width:150px;">Campo de pesquisa</td>
                      <td style="width:auto;">Valor para pesquisar</td>
                      <td style="width:100px;"></td>
                    </tr>
                    <tr>
                      <td>
                        <select name="<%=SEARCH_FIELD%>" style="width:100%;" onchange="<%=paramSearchValue.getName()%>.value='';<%=formPesquisa.submitLastCommandScript()%>">
                        <%// mostra as opções em campos de pesquisa
                          for (int i=0; i<searchFieldNames.length; i++) {
                            String searchFieldName = searchFieldNames[i];%>
                          <option value="<%=searchFieldName%>" <%=searchFieldName.equals(paramSearchField.getValue()) ? "selected" : ""%>><%=searchFieldTitles[i]%></option>
                        <%} // for%>
                        </select>
                      </td>
                      <td>
                        <%=ParamFormEdit.script(facade, paramSearchValue, 0, "", "", "if ((event.keyCode == 13) && (value != '')) " + formPesquisa.submitScript(true), "", "", false, false)%>
                        <script language="javascript" type="">
                          formPesquisa.<%=SEARCH_VALUE%>.focus();
                        </script>
                      </td>
                      <td>
                        <input type="text" name="dummy" value="dummy" style="width:0px; height:0px; visibility:hidden;" disabled="disabled" />
                        <%=CommandControl.formButton(facade, formPesquisa, ImageList.COMMAND_SEARCH, "", true, false)%>
                      </td>
                    </tr>
                  </table>
                </td>
              </tr>
            </table>

          </td>
        </tr>
        <%} // if%>

        <!-- Grid -->
        <tr style="height:auto;">
          <td>

            <table style="width:100%; height:100%;" cellpadding="4">
              <tr>
                <td>
                  <%Grid grid = new Grid(facade, "gridExternalLookup", 0, 0);
                    grid.columns().add(new Grid.Column("!", 30, Grid.ALIGN_CENTER));
                    for (int i=0; i<displayFieldNames.length; i++)
                      grid.columns().add(new Grid.Column(displayFieldTitles[i], displayFieldWidths[i]));%>
                  <%=grid.begin()%>
                  <%// loop no ResultSet
                    if (resultSet != null) {
                      while (resultSet.next()) {
                        hasRecords = true;
                        Vector vector = new Vector();
                        vector.add(getSelector(selectType, resultSet, returnFieldNames, returnFieldValues, returnUserFieldNames));
                        for (int i=0; i<displayFieldNames.length; i++)
                          vector.add(resultSet.getString(displayFieldNames[i]));
                        String[] values = new String[vector.size()];
                        vector.copyInto(values);%>
                      <%=grid.addRow(values)%>
                    <%} // while
                    // fecha o ResultSet e o Statement
                    resultSet.getStatement().close();
                    resultSet.close();
                  } // if%>
                  <%=grid.end()%>
                </td>
              </tr>
            </table>

          </td>
        </tr>

        <!--botões de comando-->
        <tr style="height:40px;">
          <td>

            <table cellpadding="4" style="width:100%;">
              <tr>
<%--
                <%if (selectType == ExternalLookup.SELECT_TYPE_MULTIPLE) {%>
                <td>
                  <input type="checkbox" name="marcarTodos" checked="checked" readonly="readonly" /> Marcar todos
                </td>
                <%} // if%>
--%>
                <td align="right">
                  <%=Button.script(facade, "buttonOK", "OK", "", "", "", Button.KIND_DEFAULT, "", "insertSelection();", false)%>&nbsp;
                  <%=Button.script(facade, "buttonCancelar", "Cancelar", "", "", "", Button.KIND_DEFAULT, "", "window.close();", false)%>
                </td>
              </tr>
            </table>

          </td>
        </tr>
      </table>

      <%// se o tipo de seleção é múltipla...
        if (selectType == ExternalLookup.SELECT_TYPE_MULTIPLE) {%>
        <!-- select local temporário para as opções da página que contém o Lookup -->
        <select id="localSelect" style="width:0px; height:0px; visibility:hidden;">
        </select>
      <%}
        // se o tipo de seleção é simples...
        else {%>
        <script type="text/javascript">
          // se a pesquisa retornou exatamente 1 resultado...seleciona
          var mySelector = document.getElementsByName("<%=SELECTOR%>");
          if ((mySelector != null) && (mySelector.length == 1)) {
            mySelector[0].checked = true;
            document.all.buttonOK.focus();
          } // if
        </script>
      <%} // if%>
    <%=formPesquisa.end()%>

  </body>
</html>

<%
    // salva tudo
    facade.commitTransaction();
  }
  catch (Exception e) {
    // desfaz tudo
    facade.rollbackTransaction();
    // encaminha exceção
    Controller.forwardException(e, pageContext);
  } // try-catch
%>
