<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.pfizer.db.Employee"%>
<%@ page import="com.pfizer.db.Product"%>
<%@ page import="com.pfizer.webapp.AppConst"%>
<%@ page import="com.pfizer.webapp.wc.components.ProductSelectWc"%>
<%@ page import="com.pfizer.webapp.wc.components.ReportSelectSPFHSWc"%>
<%@ page import="com.pfizer.webapp.wc.components.ReportSelectWc"%>
<%
	ReportSelectSPFHSWc wc = (ReportSelectSPFHSWc)request.getAttribute(ReportSelectSPFHSWc.ATTRIBUTE_NAME);	
%>

<table class="no_space_width"> 
	<tr>
		<td><img src="<%=AppConst.IMAGE_DIR%>/spacer.gif" width="15"></td>
		<td>
			<br>
			<b>Welcome <%=wc.getUser().getName()%>, </b>
			
		</td>
	</tr>
	<tr>
		<td><img src="<%=AppConst.IMAGE_DIR%>/spacer.gif" width="15"></td>
		<td>
            <table class="no_space_width">
                <tr>
            		<td><img src="<%=AppConst.IMAGE_DIR%>/spacer.gif" width="45"></td>
                    <td><br>
                        <b>Please select Report Type: </b>
                        
                        <%-- <h5>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="/TrainingReports/SPF/SPFHSReportTrainingSchedule.do"> > Training Schedule Report</a></h5>                        
                        <h5>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="/TrainingReports/SPF/SPFHSReportVariance.do"> > Variance Report</a></h5>
                        <h5>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="/TrainingReports/SPF/SPFHSReportPersonalAgenda.do"> > Personalized Agenda Report</a></h5>
                        <h5>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="/TrainingReports/SPF/SPFHSReportClassRoster.do"> > Class Roster Report</a></h5>
                        <h5>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="/TrainingReports/SPF/SPFHSReportGeneralSessionAttendance.do"> > General Session Attendance Report</a></h5>
                        <h5>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="/TrainingReports/SPF/SPFEnrollmentSummaryReport.do"> > Training Enrollment Report</a></h5>
                        <h5>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="/TrainingReports/SPF/SPFEnrollmentChangeReport.do"> > Enrollment Change Report</a></h5>
                     --%>
                    <h5>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="/TrainingReports/SPF/SPFHSReportTrainingSchedule"> > Training Schedule Report</a></h5>                        
                        <h5>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="/TrainingReports/SPF/SPFHSReportVariance"> > Variance Report</a></h5>
                        <h5>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="/TrainingReports/SPF/SPFHSReportPersonalAgenda"> > Personalized Agenda Report</a></h5>
                        <h5>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="/TrainingReports/SPF/SPFHSReportClassRoster"> > Class Roster Report</a></h5>
                        <h5>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="/TrainingReports/SPF/SPFHSReportGeneralSessionAttendance"> > General Session Attendance Report</a></h5>
                        <h5>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="/TrainingReports/SPF/SPFEnrollmentSummaryReport"> > Training Enrollment Report</a></h5>
                        <h5>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="/TrainingReports/SPF/SPFEnrollmentChangeReport"> > Enrollment Change Report</a></h5>
                    
                    
                    
                    
                    
                    </td>
                </tr>
			
		</td>
	</tr>
</table>