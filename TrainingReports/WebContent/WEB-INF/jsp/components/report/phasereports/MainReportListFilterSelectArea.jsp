<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.pfizer.webapp.AppConst"%>
<%@ page import="com.pfizer.webapp.user.UserTerritory"%>
<%@ page import="com.pfizer.webapp.wc.components.report.phasereports.MainReportListFilterSelectAreaWc "%>
<%@ page import="java.awt.GraphicsEnvironment"%>
<%@ page import="com.tgix.Utils.Util"%>
<%@ taglib uri="/WEB-INF/dynamic-include.tld" prefix="inc" %>

<%
	MainReportListFilterSelectAreaWc wc = (MainReportListFilterSelectAreaWc)request.getAttribute(MainReportListFilterSelectAreaWc.ATTRIBUTE_NAME);  
%>

<table>
    <tr>
        <td>
            <img src="<%=AppConst.IMAGE_DIR%>/spacer.gif" height="5">
        </td>
    </tr> 
    <tr>
        <td>
            <inc:include-wc component="<%=wc.getSelect()%>"/>
        </td>
    </tr>
</table>

