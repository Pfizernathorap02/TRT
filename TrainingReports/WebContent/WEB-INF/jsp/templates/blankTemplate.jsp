<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.pfizer.webapp.AppConst"%>
<%@ page import="com.pfizer.webapp.wc.templates.BlankTemplateWpc"%>
<%@ taglib uri="/WEB-INF/dynamic-include.tld" prefix="inc" %>

<%@ page import = "com.pfizer.webapp.wc.templates.MainTemplateWpc" %>
<%@ page import = "com.tgix.wc.WebComponent" %>

<%
	BlankTemplateWpc wc = (BlankTemplateWpc)request.getAttribute(BlankTemplateWpc.ATTRIBUTE_NAME);
%>
<%--
Template:<%=wc.getJsp()%>
Template Class:<%=wc.getClass().getName()%>
Main Jsp:<%=wc.getMain().getJsp()%>
Main Class:<%=wc.getMain().getClass().getName()%>
--%>

<inc:include-wc component="<%=wc.getMain()%>"/>
