
<%--
The MIT License (MIT)

Copyright (c) 2008 Kleber Maia de Andrade

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
--%>
<%@include file="../../include/beans.jsp"%>

<%@page import="java.util.*"%>

<%@page import="iobjects.ui.report.*"%>
<%@page import="iobjects.xml.ui.*"%>

<%!
  /**
   * Retorna true se 'businessObjects' possuir Param's com descri��o para que
   * possam ser exibidos na ajuda.
   * @param businessObject BusinessObject cujos Param's seram verificados.
   * @return Retorna true se 'businessObjects' possuir Param's com descri��o
   *         para que possam ser exibidos na ajuda.
   */
  public boolean hasParams(BusinessObject businessObject) {
    // loop nos Params
    for (int i=0; i<businessObject.userParamList().size(); i++)
      // se o Param tem descri��o...retorna OK
      if (!businessObject.userParamList().get(i).getDescription().equals(""))
        return true;
    // se chegou at� aqui...n�o temos ajuda para exibir
    return false;
  }
%>

<%
  try {
    // nossos par�metros
    String actionName = request.getParameter(HelpManager.ACTION_NAME);
    // se n�o recebemos um nome de Action...exce��o
    if ((actionName == null) || (actionName.equals("")))
      throw new Exception("Nenhum t�pico de ajuda para exibir.");

    // nosso Action
    Action action = facade.actionList().get(actionName, false);
    // nosso objeto de neg�cio
    BusinessObject object = facade.getBusinessObject(action.getDeclaringClassName());

    // obt�m as mudan�as nas notas de vers�o
    Hashtable changes = new Hashtable();
    for (int i=0; i<facade.releaseNotes().releaseList().length; i++) {
      // vers�o da vez
      ReleaseNotesFile.Release release = facade.releaseNotes().releaseList()[i];
      // procura por mudan�a
      ReleaseNotesFile.Change change = release.changeList().get(actionName);
      // adiciona na nossa lista
      if (change != null)
        changes.put(release, change);
    } // for
%>

<html>
  <head>
    <title><%=facade.applicationInformation().getName() + " - Ajuda"%></title>
    <link href="<%=facade.getStyle().reportInterface()%>" rel="stylesheet" type="text/css">
    <script src="include/scripts.js" type="text/javascript"></script>
  </head>

  <body style="margin:5px;" onselectstart="return true;" oncontextmenu="return false;">

    <!-- t�tulo -->
    <p class="Title"><%=action.getCaption()%></p>

    <!-- descri��o -->
    <p>
      <%=action.getDescription()%>
    </p>

    <%// se temos ajuda...
       if (!action.getHelp().equals("")) {%>
    <p>
      <%=action.getHelp()%>
    </p>
    <%} // if%>

    <%// se temos Actions aninhados...
       if (action.nestedActionList().size() > 0) {%>
    <p><b>Objetos detalhe</b></p>
    <p>
      <%// loop nos Actions aninhados
        for (int i=0; i<action.nestedActionList().size(); i++) {
          // Action da Vez
          Action nestedAction = action.nestedActionList().get(i);%>
          <%=i > 0 ? ", " : ""%><a href="<%=Controller.ACTION_HELP_BUSINESS_OBJECT.url(HelpManager.ACTION_NAME + "=" + nestedAction.getName())%>"><%=nestedAction.getCaption()%></a>
      <%} // for%>
    </p>
    <%} // if%>

    <!-- Entidade? -->
    <%if (action.getCategory() == Action.CATEGORY_ENTITY) {
        // nossos campos
        EntityFieldList fieldList = ((Entity)object).fieldList();
        // nossos lookups
        EntityLookupList lookupList = ((Entity)object).lookupList();%>

      <!-- campos -->
      <p class="Title1">Campos</p>
      <table style="width:100%;">
        <%// loop nos campos
          for (int i=0; i<fieldList.size(); i++) {
            // campo da vez
            EntityField field = fieldList.get(i);
            // se n�o tem descri��o...continua
            if (field.getDescription().equals(""))
              continue;%>
          <tr>
            <td nowrap="nowrap" valign="top"><%=field.getCaption()%></td>
            <td><%=field.getDescription()%></td>
          </tr>
        <%} // for%>

        <%// loop nos lookups
          for (int i=0; i<lookupList.size(); i++) {
            // loookup da vez
            EntityLookup lookup = lookupList.get(i);
            // se n�o tem descri��o...continua
            if (lookup.getDescription().equals(""))
              continue;%>
          <tr>
            <td nowrap="nowrap" valign="top"><%=lookup.getCaption()%></td>
            <td><%=lookup.getDescription()%></td>
          </tr>
        <%} // for%>
      </table>
    <%} // if%>

    <!-- Processo? -->
    <%if (action.getCategory() == Action.CATEGORY_PROCESS) {
        // nossas etapas
        ProcessStepList stepList = ((iobjects.process.Process)object).processStepList();
        // loop nas etapas
        for (int i=0; i<stepList.size(); i++) {
          // etapa da vez
          ProcessStep step = stepList.get(i);
          // se n�o tem descri��o...continua
          if (step.getDescription().equals(""))
            continue;%>
          <table style="width:100%;">
            <tr>
              <td class="Title3"><%=step.getCaption()%></td>
            </tr>
          </table>
          <p>
            <%=step.getDescription()%>
          </p>
          <%// se temos par�metros
            if (hasParams(object)) {%>
            <table style="width:100%;">
              <%// loop nos par�metros da etapa
                for (int w=0; w<step.paramList().size(); w++) {
                  // par�metros da vez
                  Param param = step.paramList().get(w);
                  // se n�o tem descri��o...continua
                  if (param.getDescription().equals(""))
                    continue;%>
                <tr>
                  <td nowrap="nowrap" valign="top"><%=param.getCaption()%></td>
                  <td><%=param.getDescription()%></td>
                </tr>
              <%} // for%>
              </table>
          <%} // if%>
        <%} // for%>
    <%}
      // se n�o � um Processo mas temos par�metros...
      else if (hasParams(object)) {%>
      <!-- par�metros -->
      <p class="Title1">Par�metros de pesquisa</p>
      <table style="width:100%;">
        <%// loop nos par�metros
          for (int i=0; i<object.userParamList().size(); i++) {
            // par�metros da vez
            Param param = object.userParamList().get(i);
            // se n�o tem descri��o...continua
            if (param.getDescription().equals(""))
              continue;%>
          <tr>
            <td nowrap="nowrap" valign="top"><%=param.getCaption()%></td>
            <td><%=param.getDescription()%></td>
          </tr>
        <%} // for%>
      </table>
    <%} // if%>

    <%if (action.commandList().size() > 0) {%>
    <!-- comandos -->
    <p class="Title1">Comandos</p>
    <table style="width:100%;">
      <%// loop nos comandos
        for (int i=0; i<action.commandList().size(); i++) {
          // comando da vez
          Command command = action.commandList().get(i);
          // se n�o tem descri��o...continua
          if (command.getDescription().equals(""))
            continue;%>
        <tr>
          <td nowrap="nowrap" valign="top"><%=command.getCaption()%></td>
          <td><%=command.getDescription()%></td>
        </tr>
      <%} // for%>
    </table>
    <%} // if%>

    <%// se temos FAQ's
      if (object.faqList().size() > 0) {%>
    <!-- FAQ's -->
    <p class="Title1">Veja tamb�m</p>
    <ul>
      <%// loop nas FAQ's
        for (int i=0; i<object.faqList().size(); i++) {
          // FAQ da vez
          FAQ faq = object.faqList().get(i);%>
        <li><a href="<%=Controller.ACTION_HELP_FAQ.url(HelpManager.FAQ_NAME + "=" + faq.getName())%>"><%=faq.getQuestion()%></a></li>
      <%} // for%>
    </ul>
    <%} // if%>

    <%// se temos notas de vers�o
      if (changes.size() > 0) {%>
    <!-- Mudan�as's -->
    <p class="Title1">Notas de vers�o</p>
      <table style="width:100%;">
      <%// loop nas mudan�as
        Enumeration releaseEnum = changes.keys();
        Enumeration changeEnum  = changes.elements();
        for (int i=0; i<changes.size(); i++) {
          // mudan�a da vez
          ReleaseNotesFile.Release release = (ReleaseNotesFile.Release)releaseEnum.nextElement();
          ReleaseNotesFile.Change  change = (ReleaseNotesFile.Change)changeEnum.nextElement();%>
        <tr>
          <td nowrap="nowrap" valign="top"><%=release.getLabel()%> em <%=DateTools.formatDate(release.getDate())%></b></td>
          <td><%=change.getDescription()%></td>
        </tr>
      <%} // for%>
      </table>
    <%} // if%>

  </body>
</html>

<%}
  catch (Exception e) {
    Controller.forwardException(e, pageContext);
  } // try-catch
%>
