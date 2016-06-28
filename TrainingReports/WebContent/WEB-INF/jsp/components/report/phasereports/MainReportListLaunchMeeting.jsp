<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.pfizer.webapp.AppConst"%>
<%@ page import="com.pfizer.webapp.user.UserTerritory"%>
<%@ page import="com.pfizer.webapp.wc.components.report.phasereports.MainReportListLaunchMeetingWc"%>
<%@ page import="com.pfizer.webapp.wc.components.report.phasereports.MainReportListWc"%>
<%@ page import="java.awt.GraphicsEnvironment"%>
<%@ taglib uri="/WEB-INF/dynamic-include.tld" prefix="inc" %>

<%
	MainReportListLaunchMeetingWc wc = (MainReportListLaunchMeetingWc)request.getAttribute(MainReportListWc.ATTRIBUTE_NAME);
    
%>

<script type="text/javascript" language="JavaScript">
addEvent(window, "load", sortables_init);


function sortables_init() {
    // Find all tables with class sortable and make them sortable
    if (!document.getElementsByTagName) return;
    tbls = document.getElementsByTagName("table");
    for (ti=0;ti<tbls.length;ti++) {
        thisTbl = tbls[ti];
        if (((' '+thisTbl.id+' ').indexOf("tsr_table") != -1) && (thisTbl.id)) {
            //initTable(thisTbl.id);
            ts_makeSortable(thisTbl);
        }
    }
}
</script>

<table class="basic_table" border=1>
	
	<tr>
		<td>
            <img src="<%=AppConst.IMAGE_DIR%>/spacer.gif" width="0"></td>
		<td align="left">
            <table>
                <tr>
                    <td colspan="=2">
                        <img src="<%=AppConst.IMAGE_DIR%>/spacer.gif" height="5">
                    </td>
                </tr>            
                <tr>
                    <td>	
                        <div class="breadcrumb">
                        	<!-- Weblogic to Jboss migration changes start here -->
                            <!-- <a href="/TrainingReports/reportselect" style="margin-left:0px;">Home</a> / -->
                            <a href="/TrainingReports/reportselect" style="margin-left:0px;">Home</a> /
                            <%-- <a href="begin.do?track=<%=wc.getTrack().getTrackId()%>"><%=wc.getTrack().getTrackLabel()%></a> / --%> 
                            <a href="begin?track=<%=wc.getTrack().getTrackId()%>"><%=wc.getTrack().getTrackLabel()%></a> /
                            <%-- <a href="listreport.do?section=<%=wc.getSlice()%>&activitypk=<%=wc.getActivityId()%>"><%=wc.getPageName()%></a> / --%>
                            <a href="listreport?section=<%=wc.getSlice()%>&activitypk=<%=wc.getActivityId()%>"><%=wc.getPageName()%></a> /
                            <!-- Weblogic to Jboss migration changes end here --> 
                            <%=wc.getSlice()%>
                        </div>
                    </td>
                </tr>
                <tr>
                    <td>
                        <inc:include-wc component="<%=wc.getArea1()%>"/>
                    </td>
                </tr>
            </table>
		</td>
		<td align="right">
            <inc:include-wc component="<%=wc.getArea2()%>"/>	
        </td>
		<td><img src="<%=AppConst.IMAGE_DIR%>/spacer.gif" width="15"></td>
	</tr>
	<tr>
		<td><img src="<%=AppConst.IMAGE_DIR%>/spacer.gif" width="15"></td>
		<td valign="top" colspan="2">
          <inc:include-wc component="<%=wc.getArea3()%>"/>
		</td>
		<td><img src="<%=AppConst.IMAGE_DIR%>/spacer.gif" width="15"></td>
	</tr>
</table>