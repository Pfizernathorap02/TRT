<%@ page language="java" contentType="text/html;charset=UTF-8"%>

<%@ page import="com.pfizer.processor.OverallProcessor"%>

<%@ page import="com.pfizer.webapp.AppConst"%>

<%@ page import="com.pfizer.webapp.wc.components.chart.ChartLegendWc"%>
<%@ page import="com.pfizer.webapp.wc.components.chart.ChartP2lLegendWc"%>
<%@ page import="com.pfizer.webapp.wc.components.report.p2l.OverallStatusWc"%>
<%@ page import="com.pfizer.webapp.wc.components.report.p2l.P2lBreadCrumbWc"%>

<%

	P2lBreadCrumbWc wc = (P2lBreadCrumbWc)request.getAttribute(P2lBreadCrumbWc.ATTRIBUTE_NAME);

%>

<table class="no_space_width" width="90%" height="0%"> 
    <tr>
        <td>
            <img src="<%=AppConst.IMAGE_DIR%>/spacer.gif" width="5">
        </td>
        <td colspan="=2">
            <img src="<%=AppConst.IMAGE_DIR%>/spacer.gif" height="25">
        </td>
    </tr>
    <tr>
        <td>
            <img src="<%=AppConst.IMAGE_DIR%>/spacer.gif" width="5">
        </td>
        <td>
        	<div class="breadcrumb">
                <a href="/TrainingReports/reportselect" style="margin-left:0px;">Home</a> / 
                 <%if(wc.getTrack().getTrackId().equalsIgnoreCase("CUE")){%>
                <a href="/TrainingReports/CUE/begin?track=<%=wc.getTrack().getTrackId()%>" style="margin-left:0px;"><%=wc.getTrack().getTrackLabel()%></a> / 
                <%}else{%>
                <a href="/TrainingReports/p2l/begin?track=<%=wc.getTrack().getTrackId()%>" style="margin-left:0px;"><%=wc.getTrack().getTrackLabel()%></a> / 
                <%}%>
        	</div>
        </td>
	</tr>
</table>