
<%@include file="include/beans.jsp"%>

<%
  try {
    imanager.process.CobrancaAutomatica cobrancaAutomatica = (imanager.process.CobrancaAutomatica)facade.getProcess(imanager.process.CobrancaAutomatica.CLASS_NAME);
    cobrancaAutomatica.execute();
%>

<%
  }
  catch (Exception e) {
    %>
      <table align="center" style="width:700px;">
        <tr>
          <td>
            <p><b>Ocorreu a seguinte exce��o na tentativa de executar a opera��o desejada:</b></p>
            <p><%e.printStackTrace(response.getWriter());%></p>
          </td>
        </tr>
      </table>
    <%
    e.printStackTrace();
  } // try-catch
%>
