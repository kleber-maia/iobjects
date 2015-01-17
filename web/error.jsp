<%@include file="include/beans.jsp"%>

<%
  // pega a última exceção da sessão
  Exception lastException = (Exception)session.getAttribute(Controller.LAST_EXCEPTION);
  // se não temos uma exceção...cria uma desconhecida
  if (lastException == null)
    lastException = new Exception("Exceção desconhecida.");
  // obtém as informações da exceção
  String              message        = ExtendedException.extractMessage(lastException);
  String              className      = ExtendedException.extractClassName(lastException);
  String              methodName     = ExtendedException.extractMethodName(lastException);
  String              exceptionClass = ExtendedException.extractExceptionClassName(lastException);
  StackTraceElement[] stackTrace     = lastException.getStackTrace();
  // traduz com o dicionário de sinônimos
  message = message;
%>

<html>
  <head>
    <title>Exceção</title>
    <link href="<%=facade.getStyle().userInterface()%>" rel="stylesheet" type="text/css">
    <link href="<%=facade.iconURL()%>" rel="shortcut icon" />
    <script src="include/scripts.js" type="text/javascript"></script> 
  </head>

  <body class="FrameBar" style="margin:0px;" onselectstart="return false;" oncontextmenu="return false;">

    <!-- centraliza na tela -->
    <table style="width:100%; height:100%;">
      <tr>
        <td>

          <!-- janela de exceção -->
          <table align="center" class="MsgBox" style="width:75%; padding:8px;">
            <!-- título -->
            <tr class="ActiveCaption">
              <td style="padding:6px;"><b>Exceção</b></td>
            </tr>
            <!-- interior da janela -->
            <tr>
              <td class="BtnFace" style="font-weight:normal; padding:8px;">



                <table width="100%" class="BtnFace" style="table-layout:fixed;">
                  <tr>
                    <td valign="top" style="width:48px;"><img src="images/warning32x32.png" alt=""></td>
                    <td class="BtnFace" style="width:auto;">
                      <div id="message">
                        <p>
                          Ocorreu a seguinte exceção na tentativa de realizar
                          a operação desejada:
                        </p>
                        <p>
                          <%=message%>
                        </p>
                        <%if (!className.equals("")) {%>
                          <p>Origem: <%=className + (!methodName.equals("") ? "." + methodName + "()" : "()")%></p>
                        <%} // if%>
                        <%if (!exceptionClass.equals("")) {%>
                        <p>Tipo: <%=exceptionClass%></p>
                        <%} // if%>
                      </div>
                    </td>
                  </tr>
                </table>
                
                <p align="center">
                  <%=Button.script(facade, "buttonVoltar", "Voltar", "Volta para a tela anterior.", ImageList.COMMAND_BACK, "", Button.KIND_DEFAULT, "width:90px;", "history.back();", false)%>&nbsp;
                  <%=Button.script(facade, "buttonCopiar", "Copiar", "Copia a mensagem para ser colada em outra aplicação.", ImageList.COMMAND_COPY, "", Button.KIND_DEFAULT, "width:90px;", "copyToClipboard();", false)%>&nbsp;
                  <%=Button.script(facade, "buttonDetalhes", "Detalhes", "Mostra a pilha de rastreamento da exceção.", ImageList.IMAGE_WARNING, "", Button.KIND_DEFAULT, "width:90px;", "if (detail.style.display == 'none') detail.style.display = 'block'; else detail.style.display = 'none';", false)%>
                  <script type="text/javascript">
                    // se não temos para onde voltar...desabilita o botão voltar
                    if (history.length == 0)
                      buttonVoltar.disabled = true;

                    function copyToClipboard() {
                      var text = message.innerText + "\r\n" + "Detalhes: " + detailMessage.innerText;
                      window.clipboardData.setData('Text', text);
                      alert('A mensagem foi copiada para a área de transferência. Alterne para a aplicação desejada e tecle Ctrl+V para colar.');
                    }
                  </script>
                </p>
                
                <!-- detalhes -->
                <div id="detail" style="display:none;">
                  Detalhes<br>
                  <textarea id="detailMessage" style="width:100%; height:100px; padding-left:4px;" readonly="readonly"><%for (int i=0; i<stackTrace.length; i++) {%><%=stackTrace[i].getClassName() + "." + stackTrace[i].getMethodName() + "(" + stackTrace[i].getFileName() + ":" + stackTrace[i].getLineNumber() + ")\r\n"%><%} // for%></textarea>
                </div>

              </td>
            </tr>
          </table>

        </td>
      </tr>
    </table>



  </body>
</html>
