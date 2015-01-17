<%@page import="iobjects.*"%>
<%@page import="iobjects.entity.*"%>
<%@page import="iobjects.help.*"%>
<%@page import="iobjects.misc.*"%>
<%@page import="iobjects.process.*"%>
<%@page import="iobjects.report.*"%>
<%@page import="iobjects.security.*"%>
<%@page import="iobjects.servlet.*"%>
<%@page import="iobjects.sql.*"%>
<%@page import="iobjects.util.*"%>
<%@page import="iobjects.util.mail.*"%>
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

<jsp:directive.page contentType="text/html" pageEncoding="ISO-8859-1"/>

<%--
  // evita o uso da tecla backspace
  response.setHeader("Pragma","no-cache");
  response.setHeader("Cache-Control","no-cache");
  response.setDateHeader("Expires", -1);
--%>
