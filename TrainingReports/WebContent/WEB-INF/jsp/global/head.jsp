<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.pfizer.webapp.AppConst"%>
<%@ taglib uri="/WEB-INF/dynamic-include.tld" prefix="inc" %>

<%@ page import = "com.tgix.wc.HeadWc" %>
<%@ page import="com.tgix.wc.WebComponent"%>
<%@ page import="com.tgix.wc.DynamicJavascriptWc"%>
<%@ page import = "java.util.Iterator" %>
<%
	HeadWc head = (HeadWc)request.getAttribute(HeadWc.ATTRIBUTE_NAME);
%>
<head>
	<title>
		Training Reports
	</title>
		<script type="text/javascript" language="JavaScript" src="<%=AppConst.APP_ROOT%>/resources/js/main.js"></script>
        <script type="text/javascript" language="JavaScript" src="<%=AppConst.APP_ROOT%>/resources/js/functions.js"></script>	
        <script type="text/javascript" language="JavaScript" src="<%=AppConst.APP_ROOT%>/resources/js/sorttable.js"></script>	
	
		<!-- WebComponent dependant static javascript includes goes here -->
	<% for(Iterator it = head.getJavascriptFiles().iterator(); it.hasNext();) { %>
		<script type="text/javascript" language="JavaScript" src="<%= (String)it.next() %>"></script>
	<% } %>


		<!-- WebComponent dependant dynamic javascript includes goes here -->
	<% for(Iterator it = head.getDynamicJavascripts().iterator(); it.hasNext();) { %>
		<% WebComponent temp = (WebComponent)it.next(); %>
		<%-- MUST be of type DynamicJavascriptWc --%>
		<% if (temp instanceof DynamicJavascriptWc) { %>			
			<inc:include-wc component="<%= temp %>"/>
		<% } else { %>
		<!-- 
			This component was not loaded because it was not of type DynamicJavascriptWc:
			Class:<%=temp.getClass().getName()%>
			  jsp:<%=temp.getJsp()%> 
		-->
		<% } %>
	<% } %>


        <!--[if IE 6]>
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/resources/css/ie-6.0.css" />
        <![endif]-->     
		
			
		<!-- Dynamic css files includes here -->
	<% for(Iterator it = head.getCssFiles().iterator(); it.hasNext();) { %>
		<link rel="stylesheet" type="text/css" href="<%= (String)it.next() %>"/>
	<% } %>	
</head>

