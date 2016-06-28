<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.pfizer.webapp.AppConst"%>
<%@ page import="com.pfizer.webapp.user.UserTerritory"%>
<%@ page import="com.pfizer.webapp.wc.components.report.phasereports.MainReportListSelectAreaWc"%>
<%@ page import="java.awt.GraphicsEnvironment"%>
<%@ page import="com.tgix.Utils.Util"%>
<%@ taglib uri="/WEB-INF/dynamic-include.tld" prefix="inc" %>

<%
	MainReportListSelectAreaWc wc = (MainReportListSelectAreaWc)request.getAttribute(MainReportListSelectAreaWc.ATTRIBUTE_NAME);  
%>

<table width="180">
    <tr>
        <td>
            <img src="<%=AppConst.IMAGE_DIR%>/spacer.gif" height="5">
        </td>
    </tr>            
    <tr>
        <td >
            <img src="<%=AppConst.IMAGE_DIR%>/spacer.gif" height="5">
        </td>
    </tr>            
    <tr>
        <td>
            <inc:include-wc component="<%=wc.getSelect()%>"/>
        </td>
    </tr>
</table>



