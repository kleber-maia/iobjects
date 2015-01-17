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
