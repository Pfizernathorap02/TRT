<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.pfizer.webapp.AppConst"%>
<%@ page import="com.pfizer.webapp.user.UserTerritory"%>
<%@ page import="com.pfizer.webapp.wc.components.report.phasereports.TrainingSummaryWc"%>
<%@ page import="java.awt.GraphicsEnvironment"%>
<%@ taglib uri="/WEB-INF/dynamic-include.tld" prefix="inc" %>

<%
	TrainingSummaryWc wc = (TrainingSummaryWc)request.getAttribute(TrainingSummaryWc.ATTRIBUTE_NAME);
    
%>

	
<table class="blue_table" >
    <tr>
        <th  align="left">Training Summary</th>
    </tr>
    <tr class="even">
        <td>cv</td>
    </tr>	
</table>
