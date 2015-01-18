
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

<html>
  <head>
    <title><%=facade.applicationInformation().getName()%> - Efetuar Logon</title>
    <link href="<%=facade.getStyle().reportInterface()%>" rel="stylesheet" type="text/css">
    <link href="<%=facade.iconURL()%>" rel="shortcut icon" />
    <script src="include/scripts.js" type="text/javascript"></script>
  </head>

  <body class="Background" onselectstart="return false;" oncontextmenu="return false;">

    <table style="width:70%">
      <tr>
        <td style="width:48px;" valign="top"><img alt="" src="images/information32x32.png" /></td>
        <td style="width:auto;" valign="top">
          <p class="Title">
            <%=facade.applicationInformation().getName()%>
          </p>
          <p class="Title1"></p>
          <p>
            O <%=facade.applicationInformation().getName()%> é sempre exibido em
            uma outra janela em tela cheia.
            Se o <%=facade.applicationInformation().getName()%> não for exibido
            automaticamente, desative o bloqueador de popup do seu browser e
            clique na opção abaixo.
          </p>
          <p>
            <img alt="" src="images/presentation16x16.png" align="absmiddle" />
            <a href="controller?<%=Controller.PARAM_WORK_CONFIGURATION + "=" + facade.getWorkConfiguration()%>">
              <b>Exibir o <%=facade.applicationInformation().getName()%> em tela cheia...</b>
            </a>
          </p>
        </td>
      </tr>
    </table>

  </body>
</html>
