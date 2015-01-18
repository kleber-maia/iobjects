
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
<%@include file="include/beans.jsp"%>

<%@page import="iobjects.xml.ui.*"%>

<%!
  String FLOW_CHART_NAME = "flowChartName";
  int    FRAME_WIDTH     = 200;
%>

<%
  try {
    // nome do primeiro FlowChart
    String firstName = "";
    if (facade.flowChartManager().flowChartFiles().size() > 0)
      firstName = facade.flowChartManager().flowChartFiles().getName(0);

    // se estams mudando de FlowChart
    String flowChartName = request.getParameter(FLOW_CHART_NAME);
    // se não temos um nome...obtém dos parâmetros
    if (flowChartName == null) {
      // nome do último FlowChart exibido
      Param flowChartParam = facade.userParamList().get(FLOW_CHART_NAME);
      // se temos...usa seu nome
      if ((flowChartParam != null) && facade.flowChartManager().flowChartFiles().contains(flowChartParam.getValue()))
        flowChartName = flowChartParam.getValue();
      // se não temos...usa o primeiro da lista
      else
        flowChartName = firstName;
    }
    // se temos um nome...salva nos parâmetros
    else {
      // obtém o parâmetro da lista
      Param flowChartParam = facade.userParamList().get(FLOW_CHART_NAME);
      // se não temos...cria
      if (flowChartParam == null) {
        flowChartParam = new Param(FLOW_CHART_NAME, flowChartName);
        facade.userParamList().add(flowChartParam);
      }
      // se temos...altera seu valor
      else {
        flowChartParam.setValue(flowChartName);
      } // if
    } // if

    // FlowChart
    FlowChart flowChart = facade.getFlowChart(flowChartName);
    // nosso FrameBar
    FrameBar frameBar = new FrameBar(facade, "frameBar", FRAME_WIDTH);
%>

<html>
  <head>
    <title><%=flowChart.getCaption()%></title>
    <link href="<%=facade.getStyle().userInterface()%>" rel="stylesheet" type="text/css">
    <link href="<%=facade.iconURL()%>" rel="shortcut icon" />
    <script src="include/scripts.js" type="text/javascript"></script>
  </head>

  <body class="FrameBar" style="margin:<%=FrameBar.FRAME_BAR_OFFSET*2%>px; margin-left:0px;" onselectstart="return false;" oncontextmenu="return false;" onresize="adjustOptionsPos();">

    <table cellpadding="0" cellspacing="0" style="width:100%; height:100%;">
      <tr style="height:20px">
        <td>
          <!-- ícone e caption -->
          <p style="margin-bottom:<%=FrameBar.FRAME_BAR_OFFSET*2%>px;">
            <img id="iconModule" src="images/flowchart32x32.png" align="absmiddle" alt="" />
            <span class="BackgroundText" style="font-size:14pt;"><%=flowChart.getCaption()%></span>
          </p>
        </td>
      </tr>
      <tr style="height:auto;">
        <td>
          <!-- FlowChart -->
          <%=flowChart.script(facade, "parent")%>
        </td>
      </tr>
    </table>

    <!-- descrição das etapas -->
    <div id="options" style="position:absolute; width:<%=FRAME_WIDTH%>px;">
      <table style="width:100%; height:100%;" cellpadding="0" cellspacing="0" align="center">
        <tr>
          <td class="ApplicationListViewHeader" style="padding:4px; height:26px;">Etapas</td>
        </tr>
        <tr id="listViewContainer" class="ApplicationListViewBody" style="height:auto;">
          <td>
            <div id="internalOptions" style="width:100%; padding:4px; overflow:auto;">
              ?
            </div>
          </td>
        </tr>
      </table>
    </div>
    <script type="text/javascript">
      function adjustOptionsPos() {
        options.style.top  = <%=FrameBar.FRAME_BAR_OFFSET*2%>;
        options.style.left = document.body.offsetWidth - options.offsetWidth - <%=(FrameBar.FRAME_BAR_OFFSET*2)%>;
        // *
        var maxHeight = document.body.offsetHeight - 32;
        if (internalOptions.offsetHeight > maxHeight)
          internalOptions.style.height = maxHeight;
      }
      // ajusta
      adjustOptionsPos();
    </script>

  </body>

</html>

<%}
  catch (Exception e) {
    Controller.forwardException(e, pageContext);
  }%>
