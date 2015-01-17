<%@include file="include/beans.jsp"%>

<%@page import="iobjects.xml.ui.*"%>

<%
  // nosso Advertising
  Advertising advertising = facade.advertising();
  // próxima media para exibir
  AdvertisingFile.Media media = advertising.getNextMedia(request, response);
%>

<html>
  <head>
    <title><%=facade.applicationInformation().getName()%></title>
    <link href="<%=facade.getStyle().userInterface()%>" rel="stylesheet" type="text/css">
    <script src="include/scripts.js" type="text/javascript"></script>
  </head>

  <body class="MainFrameBody" style="margin:0px;" onselectstart="return false;" oncontextmenu="return false;" onload="setTimeout('window.location.reload();', <%=media.getInterval() * 1000%>);">

    <!-- alinha a media no centro da área disponível -->
    <table cellpadding="0" cellspacing="0" style="width:100%; height:100%;">
      <tr>
        <td id="container" align="center" valign="middle">
        <%// se a media é uma Imagem...
          if (media.getType().equals(AdvertisingFile.TYPE_IMAGE)) {%>
          <a href="<%=media.getHref()%>" target="_blank"><img alt="" src="<%=media.getUrl()%>" border="0" /></a>
        <%}
          // se a media é uma animação Flash
          else if (media.getType().equals(AdvertisingFile.TYPE_FLASH)) {%>
          <script type="text/javascript">
            var so = new SWFObject("<%=media.getUrl()%>", "sotester", "100%", "100%", "9", "#ffffff");
            so.addParam("scale", "noscale");
            so.write("container");
          </script>
        <%} // if%>
        </td>
      </tr>
    </table>

  </body>

</html>
