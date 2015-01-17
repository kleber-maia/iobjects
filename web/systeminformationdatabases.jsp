<%@include file="include/beans.jsp"%>

<%@page import="java.io.*"%>
<%@page import="java.sql.*"%>
<%@page import="java.util.*"%>

<%@page import="iobjects.xml.sql.*"%>
<%@page import="iobjects.xml.*"%>

<%!
  String  CONNECTION_NAME = "cn";
  String  SQL             = "sql";
  Command COMMAND_EXECUTE = new Command("execute", "Executar", "");
%>

<%
  try {
    // arquivos de conexões existentes
    ConnectionFiles connectionFiles = facade.connectionManager().connectionFiles();
    // nosso form
    Form form = new Form(request, "formDatabase", Controller.ACTION_SYSTEM_INFORMATION_DATABASES, COMMAND_EXECUTE, "", false);
    // nosso page control
    PageControl pageControl = new PageControl(facade, "pageControl", true);

    // conexões selecionadas
    String[] connectionNames = {};
    // SQL para executar
    String sql = "";

    // mensagem de execução
    StringBuffer message = new StringBuffer();
    Exception    ex      = null;
    // se estamos executando...
    if (Controller.getCommand(request).equals(COMMAND_EXECUTE.getName())) {
      try {
        // conexões selecionadas
        connectionNames = request.getParameterValues(CONNECTION_NAME);
        // SQL para executar
        sql = request.getParameter(SQL);
        // loop nas conexões
        for (int i=0; i<connectionNames.length; i++) {
          // inicia transação
          facade.beginTransaction();
          try {
            // conexão da vez
            Connection connection = facade.getConnection(connectionNames[i]);
            // executa o SQL
            SqlTools.execute(connection, sql);
            // adiciona nas mensagens
            message.append("--> " + connectionNames[i] + ": OK\r\n"); 
            // salva tudo
            facade.commitTransaction();
          }
          catch (Exception e) {
            // adiciona nas mensagens
            message.append("--> " + connectionNames[i] + ": EXCEÇÃO\r\n");
            message.append(e.getMessage() + "\r\n\r\n");
            // desfaz tudo
            facade.rollbackTransaction();
          } // try-catch
        } // for
      }
      catch (Exception e) {
        ex = e;
      } // try-catch
    } // if
%>

<html>
  <head>
    <title><%=facade.applicationInformation().getName()%> - Informações do Sistema</title>
    <link href="<%=facade.getStyle().userInterface()%>" rel="stylesheet" type="text/css">
    <link href="<%=facade.iconURL()%>" rel="shortcut icon" />
    <script src="include/scripts.js" type="text/javascript"></script>
  </head>

  <body style="margin:0px;" onselectstart="return true;" oncontextmenu="return true;">

    <!-- form -->
    <%=form.begin()%>
      <!-- layout em duas colunas -->
      <table style="width:100%; height:100%;">
        <tr>
          <td style="width:345px;">

            <%
              Grid gridConnections = new Grid(facade, "gridConnections", new String[]{CONNECTION_NAME}, Grid.SELECT_MULTIPLE, 0, 0);
              gridConnections.columns().add(new Grid.Column("Conexão", 300));
            %>
            <%=gridConnections.begin()%>
              <%for (int i=0; i<connectionFiles.size(); i++) {
                  ConnectionFile connectionFile = connectionFiles.get(i);
                  File           file           = new File(connectionFile.getFileName());
                  String         name           = file.getName().substring(0, file.getName().indexOf("."));%>
                <%=gridConnections.addRow(new String[]{name}, new String[]{name})%>
              <%}%>
            <%=gridConnections.end()%>
            <script type="text/javascript">
              <%for (int i=0; i<connectionNames.length; i++) {%>
                <%=gridConnections.checkRow(connectionFiles.indexOf(connectionNames[i]))%>
              <%}%>
            </script>

          </td>
          <td style="width:auto;">

            <%=pageControl.begin()%>
              <%=pageControl.beginTabSheet("SQL")%>
                <table cellpadding="0" cellspacing="0" style="width:100%; height:100%;">
                  <tr>
                    <td style="height:25px;" valign="top">
                      <%=CommandControl.formButton(facade, form, ImageList.COMMAND_EXECUTE, "Deseja mesmo executar o SQL em todos os bancos de dados selecionados?", true, false)%>
                    </td>
                  </tr>
                  <tr>
                    <td>
                      <%=FormMemo.script(facade, SQL, sql, 0, 0, 0)%>
                    </td>
                  </tr>
                </table>
              <%=pageControl.endTabSheet()%>
              <%=pageControl.beginTabSheet("Resultado")%>
                <%=FormMemo.script(facade, "dummy", message.toString(), 0, 0, 0)%>
              <%=pageControl.endTabSheet()%>
            <%=pageControl.end()%>
          </td>
        </tr>
      </table>
    <%=form.end()%>

  </body>
  <script type="text/javascript">
    <%if (message.length() > 0) {%>
      PageControl_SelectTab(pageControlId, 1);
    <%} // if%>
  </script>
</html>

<%}
  catch (Exception e) {
    Controller.forwardException(e, pageContext);
  } // try-catch
%>
