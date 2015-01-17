<%@page import="java.sql.*"%>
<%@page import="java.util.*"%>

<%@page import="iobjects.*"%>
<%@page import="iobjects.entity.*"%>
<%@page import="iobjects.process.*"%>
<%@page import="iobjects.report.*"%>
<%@page import="iobjects.servlet.*"%>
<%@page import="iobjects.sql.*"%>
<%@page import="iobjects.util.*"%>
<%@page import="iobjects.util.print.*"%>
<%@page import="iobjects.ui.*"%>
<%@page import="iobjects.ui.entity.*"%>
<%@page import="iobjects.ui.param.*"%>
<%@page import="iobjects.ui.treeview.*"%>
<%@page import="iobjects.ui.report.*"%>

<%// beans de escopo de sessão%>
<jsp:useBean id="facade" type="iobjects.Facade" scope="session" />
