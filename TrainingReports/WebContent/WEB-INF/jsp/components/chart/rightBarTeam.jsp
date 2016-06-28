<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.pfizer.db.Product"%>
<%@ page import="com.pfizer.webapp.AppConst"%>
<%@ page import="com.pfizer.webapp.user.UserTerritory"%>
<%@ page import="com.pfizer.webapp.wc.components.chart.RightBarWc"%>
<%@ page import="com.pfizer.webapp.user.TerritoryFilterForm"%>
<%@ page import="com.tgix.html.HtmlBuilder"%>
<%@ taglib uri="/WEB-INF/dynamic-include.tld" prefix="inc" %>
<%@ page import="java.util.List"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.Hashtable"%>
<%@ page import="java.util.Enumeration"%>
<%@ page import="java.util.Vector"%>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.HashMap" %>


<%
	RightBarWc wc = (RightBarWc)request.getAttribute(RightBarWc.ATTRIBUTE_NAME);
	UserTerritory ut = wc.getUserTerritory();
	List products = wc.getUser().getProducts();
	TerritoryFilterForm form = wc.getUserFilterForm();
%>

<inc:include-wc component="<%=wc.getTerritorySelect()%>"/>


