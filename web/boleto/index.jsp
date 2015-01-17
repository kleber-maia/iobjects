<%@page import="imanager.process.*"%>

<%--
  Esta p�gina deve ser copiada no diret�rio /boleto do site da empresa.
  O nome da conex�o ser� obtida automaticamente a partir do nome do dom�nio, ex:
  softgroup.com.br = softgroup.
--%>

<%
  // par�metros recebidos
  String paramConnectionName = request.getParameter("p1");
  String paramCpfCnpj        = request.getParameter("p2");
  String paramEmpresaId      = request.getParameter("p3");
  String paramTituloId       = request.getParameter("p4");
  
  // se n�o recebemos o nome da conex�o...
  if (paramConnectionName == null) {
    // obt�m o dom�nio da requisi��o
    paramConnectionName = request.getLocalName().replaceAll("www.", "");
    // remove de .com em diante
    if (paramConnectionName.contains("."))
      paramConnectionName = paramConnectionName.substring(0, paramConnectionName.indexOf("."));
  } // if
  
  // redireciona para o servidor
  response.sendRedirect("http://server.imanager.com.br/boleto.jsp?p1=" + paramConnectionName + (paramCpfCnpj != null ? "&p2=" + paramCpfCnpj : "") + (paramEmpresaId != null ? "&p3=" + paramEmpresaId : "") + (paramTituloId != null ? "&p4=" + paramTituloId : ""));
%>
