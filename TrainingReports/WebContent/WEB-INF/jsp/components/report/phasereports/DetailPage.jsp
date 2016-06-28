<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.pfizer.webapp.AppConst"%>
<%@ page import="com.pfizer.webapp.user.UserTerritory"%>
<%@ page import="com.pfizer.webapp.wc.components.report.phasereports.DetailPageWc"%>
<%@ page import="java.awt.GraphicsEnvironment"%>
<%@ taglib uri="/WEB-INF/dynamic-include.tld" prefix="inc" %>

<%
	DetailPageWc wc = (DetailPageWc)request.getAttribute(DetailPageWc.ATTRIBUTE_NAME);
    
%>

<table class="basic_table" border="1">
<tr>
    <td colspan="5">
        <img src="<%=AppConst.IMAGE_DIR%>/spacer.gif" height="5">
    </td>

</tr>
<tr>
    <td>
    </td>
    <td colspan="4">
        <div class="breadcrumb">
            <a href="/TrainingReports/reportselect" style="margin-left:0px;">Home</a> / 
            <a href="begin?track=<%=wc.getTrack().getTrackId()%>"><%=wc.getTrack().getTrackLabel()%></a> / 
            <a href="listreport?section=<%=wc.getSlice()%>&activitypk=<%=wc.getActivityId()%>"><%=wc.getPageName()%></a> /
            Employee Detail 
        </div>
    </td>

</tr>
<tr>
    <td>
        <img src="<%=AppConst.IMAGE_DIR%>/spacer.gif" height="5">
    </td>
    <td>
        <inc:include-wc component="<%=wc.getEmployeeInfo()%>"/>
<!-- Added for TRT Phase 2 -->
        <br>
        <a  onmouseover="this.style.cursor='hand';" onclick="DoThis12();">Click Here</a>&nbsp;to go your Training Transcripts from P2L 
        <br>
        <a onmouseover="this.style.cursor='hand';" onclick="DoThis13();">Click Here</a>&nbsp;to go to your Direct Reports Training Transcripts from P2L 
<!-- end -->
    </td>
    <td>
        <img src="<%=AppConst.IMAGE_DIR%>/spacer.gif" height="5">
<!-- Added for TRT Phase 2 -->
    </td>
    <td valign="top" width="45%">
        <%-- <inc:include-wc component="<%=wc.getTrainingPath()%>"/> --%>
        <br>
        <inc:include-wc component="<%=wc.getGapReport()%>"/>
<!-- End -->
    </td>
    <td valign="top">   
        <img src="<%=AppConst.IMAGE_DIR%>/spacer.gif" height="40">
        <% if (wc.getOverallStatus() != null) { %>
            <inc:include-wc component="<%=wc.getOverallStatus()%>"/>
        <% } %>
      <%--   <inc:include-wc component="<%=wc.getTrainingSummary()%>"/> --%>
    </td>
    <td>
        <img src="<%=AppConst.IMAGE_DIR%>/spacer.gif" height="5">
    </td>
</tr>
<tr>
    <td colspan="5">
        <img src="<%=AppConst.IMAGE_DIR%>/spacer.gif" height="5">
    </td>

</tr>
<tr>
    <td>
        <img src="<%=AppConst.IMAGE_DIR%>/spacer.gif" height="5">
    </td>
    <td colspan="3">
        <inc:include-wc component="<%=wc.getPhaseDetail() %>"/>
    </td>
    <td>
        <img src="<%=AppConst.IMAGE_DIR%>/spacer.gif" height="5">
    </td>

</tr>
</table>