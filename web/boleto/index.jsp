<%@page import="imanager.process.*"%>

<%--
  Esta página deve ser copiada no diretório /boleto do site da empresa.
  O nome da conexão será obtida automaticamente a partir do nome do domínio, ex:
  softgroup.com.br = softgroup.
--%>

<%
  // parâmetros recebidos
  String paramConnectionName = request.getParameter("p1");
  String paramCpfCnpj        = request.getParameter("p2");
  String paramEmpresaId      = request.getParameter("p3");
  String paramTituloId       = request.getParameter("p4");
  
  // se não recebemos o nome da conexão...
  if (paramConnectionName == null) {
    // obtém o domínio da requisição
    paramConnectionName = request.getLocalName().replaceAll("www.", "");
    // remove de .com em diante
    if (paramConnectionName.contains("."))
      paramConnectionName = paramConnectionName.substring(0, paramConnectionName.indexOf("."));
  } // if
  
  // redireciona para o servidor
  response.sendRedirect("http://server.imanager.com.br/boleto.jsp?p1=" + paramConnectionName + (paramCpfCnpj != null ? "&p2=" + paramCpfCnpj : "") + (paramEmpresaId != null ? "&p3=" + paramEmpresaId : "") + (paramTituloId != null ? "&p4=" + paramTituloId : ""));
%>
