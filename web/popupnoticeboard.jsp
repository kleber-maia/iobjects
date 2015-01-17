<%@include file="include/beans.jsp"%>

<%@page import="iobjects.xml.ui.*"%>

<%!
  String NOTICE_INDEX = "noticeIndex";
%>

<%
  // índice do aviso
  int noticeIndex = NumberTools.parseInt(request.getParameter(NOTICE_INDEX));
  // aviso
  NoticeBoardFile.Notice notice = facade.noticeBoard().get(noticeIndex);
  String[] dateParts = DateTools.splitDate(notice.getDate());
%>

<html>
  <head>
    <title><%=facade.applicationInformation().getName()%></title>
    <link href="<%=facade.getStyle().userInterface()%>" rel="stylesheet" type="text/css">
    <link href="<%=facade.iconURL()%>" rel="shortcut icon" />
    <script src="include/scripts.js" type="text/javascript"></script>
  </head>

  <body class="FrameBody" style="border:none; margin:8px;" onselectstart="return false;" oncontextmenu="return false;">

    <table cellpadding="4" style="width:100%; height:100%;">
      <tr style="height:32px;">
        <td>
          <img src="<%=notice.getWarning() ? "images/warning32x32.png" : "images/information32x32.png"%>" align="absmiddle" alt="">
        </td>
        <td>
          <b><%=dateParts[0] + "/" + dateParts[1]%> - <%=notice.getTitle()%></b>
        </td>
      </tr>
      <tr style="height:auto;">
        <td></td>
        <td valign="top">
          <%=notice.getDescription()%>
        </td>
      </tr>
      <tr style="height:32px;">
        <td></td>
        <td align="right">
          <%=Button.script(facade, "buttonFechar", "Fechar", "", "", "", Button.KIND_DEFAULT, "", "window.close()", false)%>
        </td>
      </tr>
    </table>

  </body>

</html>
