<%@include file="include/beans.jsp"%>

<%
  // pega a �ltima exce��o da sess�o
  Exception lastException = (Exception)session.getAttribute(Controller.LAST_EXCEPTION);
  // se n�o temos uma exce��o...cria uma desconhecida
  if (lastException == null)
    lastException = new Exception("Exce��o desconhecida.");
  // obt�m as informa��es da exce��o
  String              message        = ExtendedException.extractMessage(lastException);
  String              className      = ExtendedException.extractClassName(lastException);
  String              methodName     = ExtendedException.extractMethodName(lastException);
  String              exceptionClass = ExtendedException.extractExceptionClassName(lastException);
  StackTraceElement[] stackTrace     = lastException.getStackTrace();
  // traduz com o dicion�rio de sin�nimos
  message = message;
%>

<html>
  <head>
    <title>Exce��o</title>
    <link href="<%=facade.getStyle().userInterface()%>" rel="stylesheet" type="text/css">
    <link href="<%=facade.iconURL()%>" rel="shortcut icon" />
    <script src="include/scripts.js" type="text/javascript"></script> 
  </head>

  <body class="FrameBar" style="margin:0px;" onselectstart="return false;" oncontextmenu="return false;">

    <!-- centraliza na tela -->
    <table style="width:100%; height:100%;">
      <tr>
        <td>

          <!-- janela de exce��o -->
          <table align="center" class="MsgBox" style="width:75%; padding:8px;">
            <!-- t�tulo -->
            <tr class="ActiveCaption">
              <td style="padding:6px;"><b>Exce��o</b></td>
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
                          Ocorreu a seguinte exce��o na tentativa de realizar
                          a opera��o desejada:
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
                  <%=Button.script(facade, "buttonCopiar", "Copiar", "Copia a mensagem para ser colada em outra aplica��o.", ImageList.COMMAND_COPY, "", Button.KIND_DEFAULT, "width:90px;", "copyToClipboard();", false)%>&nbsp;
                  <%=Button.script(facade, "buttonDetalhes", "Detalhes", "Mostra a pilha de rastreamento da exce��o.", ImageList.IMAGE_WARNING, "", Button.KIND_DEFAULT, "width:90px;", "if (detail.style.display == 'none') detail.style.display = 'block'; else detail.style.display = 'none';", false)%>
                  <script type="text/javascript">
                    // se n�o temos para onde voltar...desabilita o bot�o voltar
                    if (history.length == 0)
                      buttonVoltar.disabled = true;

                    function copyToClipboard() {
                      var text = message.innerText + "\r\n" + "Detalhes: " + detailMessage.innerText;
                      window.clipboardData.setData('Text', text);
                      alert('A mensagem foi copiada para a �rea de transfer�ncia. Alterne para a aplica��o desejada e tecle Ctrl+V para colar.');
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
