<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.pfizer.db.Product"%>
<%@ page import="com.pfizer.webapp.AppConst"%>
<%@ page import="com.pfizer.webapp.user.UserTerritory"%>
<%@ page import="com.pfizer.webapp.wc.components.chart.GenericRightBarWc"%>
<%@ page import="com.pfizer.webapp.wc.components.chart.RightBarPl2Wc"%>
<%@ page import="com.pfizer.webapp.wc.components.chart.RightBarWc"%>
<%@ page import="com.pfizer.webapp.user.TerritoryFilterForm"%>
<%@ page import="com.tgix.html.HtmlBuilder"%>
<%@ taglib uri="/WEB-INF/dynamic-include.tld" prefix="inc" %>

<%
	GenericRightBarWc wc = (GenericRightBarWc)request.getAttribute(GenericRightBarWc.ATTRIBUTE_NAME);
%>

<script type="text/javascript" language="javascript"> 
<!-- 
function DoThis12() { 
window.name = "main";
var myW = window.open("","myW","height=400,width=500,scrollbars=yes,resizable=yes,statusbar=no,menubar=no,toolbar=no,dependent=yes") 
} 
--> 
</script>

<div class="chartscontrol_area">
    <inc:include-wc component="<%=wc.getTerritorySelect()%>"/>
    
    <% if ( wc.getBottomWc() != null) { %>
        <inc:include-wc component="<%=wc.getBottomWc()%>"/>
    <% } %>
</div>


