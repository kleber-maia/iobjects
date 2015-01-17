<%@page import="java.sql.*"%>
<%@page import="java.util.*"%>

<%@page import="iobjects.*"%>
<%@page import="iobjects.entity.*"%>
<%@page import="iobjects.help.*"%>
<%@page import="iobjects.misc.*"%>
<%@page import="iobjects.process.*"%>
<%@page import="iobjects.report.*"%>
<%@page import="iobjects.servlet.*"%>
<%@page import="iobjects.sql.*"%>
<%@page import="iobjects.util.*"%>
<%@page import="iobjects.util.mail.*"%>
<%@page import="iobjects.util.print.*"%>
<%@page import="iobjects.ui.*"%>
<%@page import="iobjects.ui.ajax.*"%>
<%@page import="iobjects.ui.entity.*"%>
<%@page import="iobjects.ui.help.*"%>
<%@page import="iobjects.ui.param.*"%>
<%@page import="iobjects.ui.process.*"%>
<%@page import="iobjects.ui.report.*"%>
<%@page import="iobjects.ui.treeview.*"%>

<%-- beans de escopo de sessão --%>
<jsp:useBean id="facade" type="iobjects.Facade" scope="session" />

<%
  // ID da Empresa selecionada no Master Relation
  int selectedEmpresaId = facade.masterRelation().isEmpty() ? 0 : facade.masterRelation().getInt(imanager.entity.Empresa.FIELD_EMPRESA_ID.getFieldName());
%>
