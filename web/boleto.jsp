
<%@page import="java.sql.*"%>

<%@page import="iobjects.*"%>
<%@page import="iobjects.servlet.*"%>
<%@page import="iobjects.util.*"%>

<%@page import="imanager.entity.*"%>
<%@page import="imanager.misc.*"%>
<%@page import="imanager.process.*"%>

<%--
  Esta p�gina deve ser copiada no diret�rio raiz de iObjects. Ela utiliza as
  class de iManager Financeiro para exibir t�tulos em aberto e emiss�o de
  boletos banc�rios.
  A logomarca deve ser copiada na pasta /boleto com o nome da conex�o 
  informada e a extens�o ".png'.
--%>

<%!
  public void logon(Facade facade, String connectionName) throws Exception {
    // efetua logoff
    if (facade.getLoggedUser() != null)
      facade.logoff();
    // se n�o fizemos logoff ou estamos em outra conex�o...
    if ((facade.getLoggedUser() == null) || !facade.getDefaultConnectionName().equals(connectionName)) {
      // efetua logoff
      if (facade.getLoggedUser() != null)
        facade.logoff();
      // efetua logon
      facade.logon(connectionName, CobrancaAutomatica.BOLETO_USER_NAME, CobrancaAutomatica.BOLETO_PASSWORD);
      facade.masterRelation().setValues(new String[]{"1"});
      facade.masterRelation().setUserValues(new String[]{"Default"});
    } // if
  }
%>

<%// se devemos redirecionar para o boleto...
  if (Controller.getCommand(request).equals("redirect")) {
    response.sendRedirect("controller?action=boletoBancarioEmissao&userParamEmpresaId=" + request.getParameter(CobrancaAutomatica.BOLETO_PARAM_EMPRESA_ID) + "&userParamTituloId=" + request.getParameter(CobrancaAutomatica.BOLETO_PARAM_TITULO_ID));
    return;
  } // if
%>

<%
  try {
    // obt�m nossa fachada
    Facade facade = Controller.getFacade(request);
    // nossos par�metros
    String paramConnectionName = request.getParameter(CobrancaAutomatica.BOLETO_PARAM_CONNECTION_NAME);
    String paramCpfCnpj        = request.getParameter(CobrancaAutomatica.BOLETO_PARAM_CPF_CNPJ);
    String paramEmpresaId      = request.getParameter(CobrancaAutomatica.BOLETO_PARAM_EMPRESA_ID);
    String paramTituloId       = request.getParameter(CobrancaAutomatica.BOLETO_PARAM_TITULO_ID);
    
    // se n�o temos o nome da conex�o...exce��o
    if ((paramConnectionName == null) || (paramConnectionName.isEmpty()))
      throw new Exception("O nome da conex�o n�o foi informado.");

    // nosso contato
    ContatoInfo  contatoInfo    = new ContatoInfo();
    // t�tulos a exibir
    TituloInfo[] tituloInfoList = new TituloInfo[0];
    
    // se temos a identifica��o do cliente...pesquisa seus t�tulos
    if ((paramCpfCnpj != null) && !paramCpfCnpj.isEmpty()) {
      // efetua logon
      logon(facade, paramConnectionName);
      // nossos objetos
      Titulo titulo = (Titulo)facade.getEntity(Titulo.CLASS_NAME);
      Contato contato = (Contato)facade.getEntity(Contato.CLASS_NAME);
      // pesquisa pelo contato pelo CPF
      ContatoInfo[] contatoInfoList = contato.selectByFilter("", 0, TipoPessoa.TODOS, Personalidade.TODOS, "", "", 0, Mes.TODOS, Mes.TODOS, Sexo.TODOS, paramCpfCnpj, "", "", "", "", NaoSim.TODOS);
      // se n�o encontramos...pesquisa pelo CNPJ
      if (contatoInfoList.length == 0)
        contatoInfoList = contato.selectByFilter("", 0, TipoPessoa.TODOS, Personalidade.TODOS, "", "", 0, Mes.TODOS, Mes.TODOS, Sexo.TODOS, "", paramCpfCnpj, "", "", "", NaoSim.TODOS);
      // se n�o encontramos...exce��o
      if (contatoInfoList.length == 0)
        throw new Exception("Cadastro n�o encontrado para o CPF/CNPJ: " + paramCpfCnpj + ".");
      // guarda o contato
      contatoInfo = contatoInfoList[0];
      // pesquisa seus t�tulos
      tituloInfoList = titulo.selectByFilter(0, 0, contatoInfo.getContatoId(), 0, TipoTitulo.RECEBER, new Timestamp(DateTools.getActualDate().getTime()), new Timestamp(DateTools.getCalculatedDays(10).getTime()), NaoSim.SIM, NaoSim.TODOS, NaoSim.TODOS, Administradora.NENHUMA, NaoSim.TODOS, NaoSim.TODOS, null);
    } // if
%>       
  <html>
    <head>
      <title></title>
      <style type="text/css">
        body, a, b, p, span, table, div, h1, h2, h3, input {
          font-family: Tahoma, Arial;
          font-size:   12pt;
          color:       rgb(100, 100, 100);          
        }
        a {
          color:       rgb(0, 0, 255);
        }
        button {
          background-color: rgb(230, 230, 230);
          border:           1px solid rgb(150, 150, 150);
          height:           24px;
        }
        input {
          background-color: rgb(240, 240, 240);
          border:           1px solid rgb(150, 150, 150);
          height:           24px;
        }
        h1 {
          font-size:   14pt;
          color:       rgb(100, 100, 100);          
        }
        h2 {
          font-size:   12pt;
          color:       rgb(125, 125, 125);
        }
        h3 {
          font-size:   10pt;
          color:       rgb(150, 150, 150);
        }
        .info {
          font-size:   10pt;
          color:       rgb(150, 150, 150);
        }
        em {
          font-style:  normal;
          font-weight: bold;
          font-size:   12pt;
          color:       rgb(100, 100, 100);          
        }
      </style>
    </head>
    <body onselectstart="return true;" oncontextmenu="return false;" onload="<%if ((paramEmpresaId != null) && (paramTituloId != null)) {%>showBoletoBancario(<%=paramEmpresaId%>, <%=paramTituloId%>);<%} // if%>">

      <script type="text/javascript">
        // se � o FireFox...avisa
        if ((navigator.userAgent.toUpperCase().indexOf("FIREFOX") > 0))
          alert("ATEN��O - O navegador utilizado n�o � compat�vel com este servi�o. Recomendamos utilizar o Internet Explorer, Chrome ou Safari em suas vers�es mais atualizadas.");
        
        function showBoletoBancario(empresaId, tituloId) {
          document.getElementById("frameBoleto").src = "boleto.jsp?command=redirect&<%=CobrancaAutomatica.BOLETO_PARAM_EMPRESA_ID%>=" + empresaId + "&<%=CobrancaAutomatica.BOLETO_PARAM_TITULO_ID%>=" + tituloId;
        }
      </script>
      
      <!-- layout -->
      <table align="center" style="width:850px;">
        <tr>
          <td>
            
            <!-- logo e t�tulo -->
            <table style="width:100%;">
              <tr>
                <td style="width:50%;"><img src="boleto/<%=paramConnectionName%>.png" /></td>
                <td style="width:50%; text-align:right;"><h1 style="margin-bottom:4px;">Boleto de Cobran�a</h1><font class="info">Pague suas faturas com toda praticidade.</font></td>
              </tr>
            </table>

            <!-- separador -->
            <div style="height:100px;"></div>
                
            <%// se n�o temos a identifica��o do cliente...
              if ((paramCpfCnpj == null) || paramCpfCnpj.isEmpty()) {%>
              
                <!-- identifica��o do cliente -->
                <p style="text-align:center;">Por favor, informe seu CPF ou o CNPJ da sua empresa:</p>
                <form id="formCliente">
                  <!-- dados ocults -->
                  <input type="hidden" name="<%=CobrancaAutomatica.BOLETO_PARAM_CONNECTION_NAME%>" value="<%=paramConnectionName%>" />
                  <!-- dados do usu�rio -->
                  <table align="center">
                    <tr>
                      <td>
                        <input type="text" maxlength="14" id="<%=CobrancaAutomatica.BOLETO_PARAM_CPF_CNPJ%>" name="<%=CobrancaAutomatica.BOLETO_PARAM_CPF_CNPJ%>" value="" style="text-align:center;" />
                      </td>
                      <td>
                        <button type="submit">&nbsp;OK&nbsp;</button>
                      </td>
                    </tr>
                    <tr>
                      <td align="center"><font class="info">Digite apenas os n�meros.</font></td>
                      <td></td>
                    </tr>
                  </table>
                </form>
                <script>
                  document.getElementById("<%=CobrancaAutomatica.BOLETO_PARAM_CPF_CNPJ%>").focus();
                </script>

            <%}
              // se temos...
              else {%>
                
                <!-- cliente -->
                <p style="text-align:center;"><b>Cliente:</b> <%=contatoInfo.getRazaoSocial().isEmpty() ? contatoInfo.getNome() : contatoInfo.getRazaoSocial() %></p>

                <!-- separador -->
                <div style="height:50px;"></div>
                
                <!-- t�tulos -->
                <%if (tituloInfoList.length == 0) {%>
                  <p style="text-align:center;"><em>N�o existem t�tulos para pagamento nos pr�ximos dias.</em></p>
                <%}
                  else {%>
                  
                  <table align="center" cellspacing="4">
                    <tr>
                      <th style="width:100px; text-align:center;">Documento</th>
                      <th style="width:100px; text-align:center;">Vencimento</th>
                      <th style="width:250px; text-align:left;">Descri��o</th>
                      <th style="width:80px; text-align:right;">Valor</th>
                      <th style="width:80px; text-align:right;">Multa</th>
                      <th style="width:80px; text-align:right;">Juros</th>
                      <th style="width:120px; text-align:center">Boleto</th>
                    </tr>
                  <%// loop nos t�tulos
                    Timestamp today = new Timestamp(DateTools.getActualDate().getTime());
                    boolean   hasIndisponiveis = false;
                    for (int i=0; i<tituloInfoList.length; i++) {
                      // t�tulo da vez
                      TituloInfo tituloInfo = tituloInfoList[i];
                      // atrasado?
                      boolean atrasado = tituloInfo.getDataVencimento().before(today);
                      // quantos dias?
                      int diasAtraso = DateTools.getSubtractedDays(today, tituloInfo.getDataVencimento());
                      // pr�xima recorr�ncia
                      Timestamp proximaRecorrencia;
                      switch (tituloInfo.getRecorrencia()) {
                        case Recorrencia.ANUAL     : proximaRecorrencia = new Timestamp(DateTools.getCalculatedMonths(tituloInfo.getDataVencimento(), 12).getTime()); break;
                        case Recorrencia.TRIMESTRAL: proximaRecorrencia = new Timestamp(DateTools.getCalculatedMonths(tituloInfo.getDataVencimento(), 3).getTime()); break;
                        case Recorrencia.BIMESTRAL : proximaRecorrencia = new Timestamp(DateTools.getCalculatedMonths(tituloInfo.getDataVencimento(), 2).getTime()); break;
                        case Recorrencia.MENSAL    : proximaRecorrencia = new Timestamp(DateTools.getCalculatedMonths(tituloInfo.getDataVencimento(), 1).getTime()); break;
                        case Recorrencia.QUINZENAL : proximaRecorrencia = new Timestamp(DateTools.getCalculatedWeeks(tituloInfo.getDataVencimento(), 2).getTime()); break;
                        case Recorrencia.SEMANAL   : proximaRecorrencia = new Timestamp(DateTools.getCalculatedWeeks(tituloInfo.getDataVencimento(), 1).getTime()); break;
                        default: 
                          proximaRecorrencia = today;
                      } // switch
                      // o boleto estar� indispon�vel se o t�tulo j� foi arquivado
                      // ou se j� est� uma recorr�ncia atrasado
                      boolean indisponivel = (tituloInfo.getArquivo() == NaoSim.SIM) || (proximaRecorrencia.before(today));
                      if (indisponivel)
                        hasIndisponiveis = true;
                      // categoria
                      String categoria = tituloInfo.lookupValueList().get(Titulo.LOOKUP_CATEGORIA).getDisplayFieldValuesToString();
                      if (categoria.contains("/"))
                        categoria = categoria.substring(categoria.lastIndexOf("/")+1);%>
                    <tr>
                      <td style="text-align:center;"><%=NumberTools.completeZero(tituloInfo.getTituloId(), 8)%></td>
                      <td style="text-align:center; <%=atrasado ? "color:#ff0000;" : ""%>"><%=DateTools.formatDate(tituloInfo.getDataVencimento())%></td>
                      <td style="text-align:left;"><%=categoria%></td>
                      <td style="text-align:right;"><%=NumberTools.format(tituloInfo.getValor())%></td>
                      <td style="text-align:right;"><%=NumberTools.format(atrasado ? tituloInfo.getValor() * tituloInfo.getPercentualMulta() / 100 : 0)%></td>
                      <td style="text-align:right;"><%=NumberTools.format(atrasado ? tituloInfo.getValor() * tituloInfo.getPercentualJuros() * diasAtraso / 100 : 0)%></td>
                      <td style="text-align:center;"><%=indisponivel ? "INDISPON�VEL" : "<a href=\"javascript:void(0);\" onclick=\"showBoletoBancario(" + tituloInfo.getEmpresaId() + ", " + tituloInfo.getTituloId() + ")\">Emitir</a>"%></td>
                    </tr>
                  <%} // for%>
                  </table>
                  <%if (hasIndisponiveis) {%>
                  <p class="info">*Para emitir um BOLETO INDISPON�VEL � necess�rio entrar em contato com nosso setor financeiro.</p>
                  <%} // if%>
                <%} // if%>

                <!-- separador -->
                <div style="height:50px;"></div>
                
                <iframe id="frameBoleto" frameborder="0" style="border:none; width:100%; height:900px;"></iframe>

            <%} // if%>
            
          </td>
        </tr>
      </table>
      
<%
  }
  catch (Exception e) {
    %>
      <table align="center" style="width:700px;">
        <tr>
          <td>
            <p><b>Ocorreu a seguinte exce��o na tentativa de executar a opera��o desejada:</b></p>
            <p><%=e.getMessage()%></p>
          </td>
        </tr>
      </table>
    <%
    e.printStackTrace();
  } // try-catch
%>
    </body>
  </html>

