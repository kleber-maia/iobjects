
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
<%@include file="../../../include/beans.jsp"%>

<%
  try {
    // se o serviço de envio de e-mail não foi ativado...exceção
    if (!facade.mailService().getSmtpActive()) {
      throw new ExtendedException("Serviço de envio de mensagens inativo.");
    } // if
    // nossos parâmetros
    String   smtpHost     = request.getParameter(Controller.PARAM_SEND_MAIL_SMTP_HOST);
    int      smtpPort     = NumberTools.parseInt(request.getParameter(Controller.PARAM_SEND_MAIL_SMTP_PORT));
    String   smtpUsername = request.getParameter(Controller.PARAM_SEND_MAIL_SMTP_USERNAME);
    String   smtpPassword = request.getParameter(Controller.PARAM_SEND_MAIL_SMTP_PASSWORD);
    boolean  smtpSSL      = Boolean.parseBoolean(request.getParameter(Controller.PARAM_SEND_MAIL_SMTP_SSL));
    String   subject      = request.getParameter(Controller.PARAM_SEND_MAIL_SUBJECT);
    String   url          = StringTools.decodeURL(request.getParameter(Controller.PARAM_SEND_MAIL_URL));
    // obtém os endereços
    String   strFrom      = request.getParameter(Controller.PARAM_SEND_MAIL_FROM);
    String[] from         = (strFrom.equals("") ? new String[0] : strFrom.split(";"));
    // *
    String   strReplyTo   = request.getParameter(Controller.PARAM_SEND_MAIL_REPLY_TO);
    String[] replyTo      = (strReplyTo.equals("") ? new String[0] : strReplyTo.split(";"));
    // *
    String   strTo        = request.getParameter(Controller.PARAM_SEND_MAIL_TO);
    String[] to           = (strTo.equals("") ? new String[0] : strTo.split(";"));
    // *
    String   strCc        = request.getParameter(Controller.PARAM_SEND_MAIL_CC);
    String[] cc           = (strCc.equals("") ? new String[0] : strCc.split(";"));
    // *
    String   strBcc       = request.getParameter(Controller.PARAM_SEND_MAIL_BCC);
    String[] bcc          = (strBcc.equals("") ? new String[0] : strBcc.split(";"));

    // se não recebemos dados de envio...pega do serviço Smtp
    if (smtpHost.equals("")) {
      smtpHost = facade.mailService().getSmtpProperties().getHostName();
      smtpPort = facade.mailService().getSmtpProperties().getPort();
      smtpUsername = facade.mailService().getSmtpProperties().getUserName();
      smtpPassword = facade.mailService().getSmtpProperties().getPassword();
      // *
      from = (facade.mailService().getSmtpProperties().getFromAddress()).split(";");
    } // if

    // verifica se temos um Action na URL
    Action action    = null;
    int    actionPos = url.indexOf(Controller.ACTION + "=");
    if (actionPos >= 0) {
      // obtém o nome do Action
      String actionName = url.substring(url.indexOf("=", actionPos)+1);
      if (actionName.indexOf("&") > 0)
        actionName = actionName.substring(0, actionName.indexOf("&"));
      // obtém o Action
      action = facade.actionList().get(actionName, false);
    } // if

    // nosso Form
    Form form = new Form(request, "form", Controller.ACTION_SEND_MAIL, Controller.COMMAND_SEND_MAIL_EXECUTE, "", false);

    // se devemos enviar o e-mail
    if (Controller.isExecuting(request)) {
      // cria nosso Smtp
      Smtp smtp = new Smtp(smtpHost, smtpPort, smtpUsername, smtpPassword, smtpSSL);
      // cria a mensagem
      HTMLMessage message = new HTMLMessage(from,
                                            replyTo,
                                            to,
                                            cc,
                                            bcc,
                                            subject,
                                            new java.net.URL(Controller.getRemoteURL() + "/" + url),
                                            session.getId());
      // envia
      smtp.send(message);
    } // if
%>

<html>
  <head>
    <title><%=action != null ? action.getCaption() : "Enviar e-mail"%></title>
    <link href="<%=facade.getStyle().userInterface()%>" rel="stylesheet" type="text/css">
    <script src="include/scripts.js" type="text/javascript"></script>
  </head>

  <body style="margin:10px;" onselectstart="return true;" oncontextmenu="return false;">

    <%// se enviamos o e-mail...avisa
      if (Controller.isExecuting(request)) {%>
      <script type="text/javascript">
        alert("Mensagem enviada com sucesso!");
        window.close();
      </script>
    <%}
      // se vamos enviar o e-mail...confirma os dados
      else {%>
      <!-- nosso Form -->
      <%=form.begin()%>
        <!-- dados ocultos -->
        <input type="hidden" name="<%=Controller.PARAM_SEND_MAIL_SMTP_HOST%>" value="<%=smtpHost%>" />
        <input type="hidden" name="<%=Controller.PARAM_SEND_MAIL_SMTP_PORT%>" value="<%=smtpPort%>" />
        <input type="hidden" name="<%=Controller.PARAM_SEND_MAIL_SMTP_USERNAME%>" value="<%=smtpUsername%>" />
        <input type="hidden" name="<%=Controller.PARAM_SEND_MAIL_SMTP_PASSWORD%>" value="<%=smtpPassword%>" />
        <input type="hidden" name="<%=Controller.PARAM_SEND_MAIL_SMTP_SSL%>" value="<%=smtpSSL%>" />
        <input type="hidden" name="<%=Controller.PARAM_SEND_MAIL_FROM%>" value="<%=StringTools.arrayStringToString(from, ";")%>" />
        <input type="hidden" name="<%=Controller.PARAM_SEND_MAIL_REPLY_TO%>" value="<%=StringTools.arrayStringToString(replyTo, ";")%>" />
        <input type="hidden" name="<%=Controller.PARAM_SEND_MAIL_URL%>" value="<%=StringTools.encodeURL(url)%>" />
        <!-- dados -->
        <table style="width:100%; height:100%;">
          <tr style="height:auto;">
            <td valign="top" style="width:40px;"><img src="images/mail32x32.png" alt=""/></td>
            <td valign="top" style="width:auto;">
              <p>
              Informe os dados de envio e recebimento da mensagem.
              </p>
              <table>
                <tr>
                  <td style="width:100px;">Remetente</td>
                  <td><input type="text" name="<%=Controller.PARAM_SEND_MAIL_FROM%>" value="<%=StringTools.arrayStringToString(from, ";")%>" style="width:270px;" disabled="disabled" /></td>
                </tr>
                <tr>
                  <td>Resposta</td>
                  <td><input type="text" name="<%=Controller.PARAM_SEND_MAIL_REPLY_TO%>" value="<%=StringTools.arrayStringToString(replyTo, ";")%>" style="width:270px;" disabled="disabled" /></td>
                </tr>
                <tr>
                  <td>Destinatário</td>
                  <td><input type="text" name="<%=Controller.PARAM_SEND_MAIL_TO%>" value="<%=StringTools.arrayStringToString(to, ";")%>" style="width:270px;" /></td>
                </tr>
                <tr>
                  <td>CC</td>
                  <td><input type="text" name="<%=Controller.PARAM_SEND_MAIL_CC%>" value="<%=StringTools.arrayStringToString(cc, ";")%>" style="width:270px;" /></td>
                </tr>
                <tr>
                  <td>BCC</td>
                  <td><input type="text" name="<%=Controller.PARAM_SEND_MAIL_BCC%>" value="<%=StringTools.arrayStringToString(bcc, ";")%>" style="width:270px;" /></td>
                </tr>
                <tr>
                  <td>Assunto</td>
                  <td><input type="text" name="<%=Controller.PARAM_SEND_MAIL_SUBJECT%>" value="<%=subject%>" style="width:270px;" /></td>
                </tr>
              </table>
            </td>
          </tr>
          <tr style="height:20px;">
            <td align="right" colspan="2">
              <%=Button.script(facade, "buttonSend", "Enviar", "", "", "", Button.KIND_DEFAULT, "", form.submitScript(true), false)%>&nbsp;
              <%=Button.script(facade, "buttonCancel", "Cancelar", "", "", "", Button.KIND_DEFAULT, "", "window.close();", false)%>
            </td>
          </tr>
        </table>
        <script type="text/javascript">
          form.buttonSend.focus();
        </script>
      <%=form.end()%>
    <%} // if%>

  </body>
</html>

<%}
  // término do bloco protegido
  catch (Exception e) {
    Controller.forwardException(e, pageContext);
  } // try-catch
%>
