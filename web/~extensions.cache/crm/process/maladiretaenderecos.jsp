<jsp:useBean id="facade" type="iobjects.Facade" scope="session" /><%@page import="iobjects.*"%><%@page import="imanager.process.*"%><%MalaDireta malaDireta = (MalaDireta)facade.getProcess(MalaDireta.CLASS_NAME);%><%=malaDireta.getMalaDiretaInfo().getEnderecos()%>
