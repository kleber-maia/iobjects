<%@include file="include/beans.jsp"%>

<%!
  String STYLE_NAME = "styleName";
%>

<%
  // pega os parâmetros esperados
  String styleName = request.getParameter(STYLE_NAME);
  if (styleName == null)
    styleName = "";

  // o estilo foi alterado?
  boolean styleChanged = !styleName.equals("") && !styleName.equals(facade.getStyle().css().getName());
  // se alterou o estilo atual...
  if (styleChanged) {
    // pega o novo estilo a ser utilizado
    iobjects.xml.ui.StyleFile style = facade.styleManager().getStyle(styleName);
    // se o estilo existe...
    if (style != null) {
      // define o novo estilo
      Controller.setLastUserStyle(response, facade, style);
    } // if
  } // if
%>

<html>
  <head>
    <title>Alterar Estilo</title>
    <link href="<%=facade.getStyle().userInterface()%>" rel="stylesheet" type="text/css">
    <link href="<%=facade.iconURL()%>" rel="shortcut icon" />
    <script src="include/scripts.js" type="text/javascript"></script>
  </head>
  <body style="margin:0px 0px 0px 0px;" onselectstart="return false;" oncontextmenu="return false;">
    <!-- formulário de alteração de estilo -->
    <form id="formChangeStyle" action="controller" method="POST">
      <input type="hidden" name="<%=Controller.ACTION%>" value="<%=Controller.ACTION_CHANGE_STYLE.getName()%>">
      <!-- dados -->
      <table>
        <tr>
          <td style="padding:10px 10px 10px 10px;">
            <table>
              <tr>
                <td style="width:34px;"><img src="images/style32x32.png" alt="" /></td>
                <td>A lista abaixo contém os esquemas de estilos e cores
                    que podem ser utilizados para personalizar a aplicação
                    de acordo com suas próprias preferências.</td>
              </tr>
              <tr>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
              </tr>
              <tr>
                <td></td>
                <td>Selecione o estilo desejado:</td>
              </tr>
              <tr>
                <td></td>
                <td>
                  <select name="<%=STYLE_NAME%>" size="7" style="width:330px; height:100px;">
                    <%for (int i=0; i<facade.styleManager().styles().length; i++) {
                        String styleName_ = facade.styleManager().styles()[i].css().getName();%>
                      <option value="<%=styleName_%>" <%=styleName_.equals(facade.getStyle().css().getName()) ? "selected" : ""%>><%=styleName_%></option>
                    <%} // for%>
                  </select>
                </td>
              </tr>
              <tr>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
              </tr>
              <tr>
                <td></td>
                <td align="right">
                  <%=Button.script(facade, "buttonOK", "OK", "", "", "", Button.KIND_DEFAULT, "", "formChangeStyle.submit();", false)%>&nbsp;
                    <%=Button.script(facade, "buttonCancel", "Cancelar", "", "", "", Button.KIND_DEFAULT, "", "window.close();", false)%>
                </td>
              </tr>
            </table>
            <script language="javascript" type="">formChangeStyle.<%=STYLE_NAME%>.focus()</script>
          </td>
        </tr>
      </table>
    </form>
    <script type="text/javascript">
      <%if (styleChanged) {%>
        // atualiza tudo
        window.opener.location.reload();
        // fecha nossa janela
        window.close();
      <%} // if%>
    </script>
  </body>
</html>
