<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.pfizer.db.Employee"%>
<%@ page import="com.pfizer.db.Product"%>
<%@ page import="java.util.List"%>
<%@ page import="com.pfizer.webapp.AppConst"%>
<%@ page import="com.pfizer.webapp.wc.components.ProductSelectWc"%>
<%@ page import="com.pfizer.webapp.wc.components.ReportSelectPDFHSWc"%>
<%@ page import="com.pfizer.webapp.wc.components.ReportSelectRBUWc"%>
<%@ page import="com.pfizer.webapp.wc.components.ReportSelectWc"%>
<%
	ReportSelectRBUWc wc = (ReportSelectRBUWc)request.getAttribute(ReportSelectRBUWc.ATTRIBUTE_NAME);
	List products = wc.getUser().getProducts();
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
                        
                        <h5>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="/TrainingReports/RBU/RBUTrainingSchedule"> > Training Schedule Report</a></h5>                        
                        <h5>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="/TrainingReports/RBU/RBUTrainingScheduleByTrack"> > Training Schedule By Track Report</a></h5>
                        <h5>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="/TrainingReports/RBU/VarianceReport"> > Variance Report</a></h5>
                        <h5>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="/TrainingReports/RBU/RBUEnrollmentExceptionReport"> > Enrollment Exceptions Report</a></h5> 
                                                
                         <h5>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="/TrainingReports/RBU/RBUGCConflictsReport"> > Guest Trainer Conflict Report</a></h5>  
                         
                        <h5>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="/TrainingReports/RBU/RBUClassRoomReport"> > Classroom Grid </a></h5>  
                         <h5>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="/TrainingReports/RBU/RBUEnrollmentChangeReport"> > Enrollment Change Report</a></h5>
                          <h5>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="/TrainingReports/PrintHome/printPersonalizedAgenda"> > Personalized Agenda Report </a></h5>
                           <h5>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="/TrainingReports/RBU/displayClassRosterReport?firstTime=true"> > Class Roster Report </a></h5>
                        
                           <h5>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="/TrainingReports/RBU/rbuUnassignedEmployeesReport?firstTime=true"> > Unassigned Tables Report </a></h5>        
                        
                       
                       
                        <!--
                        <h5>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="/TrainingReports/RBU/RBUEnrollmentChangeReport.do"> > Enrollment Change Report</a></h5>
                        <h5>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="/TrainingReports/PWRA/PDFHSReportPersonalAgenda.do"> > Personalized Agenda Report</a></h5>
                        <h5>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="/TrainingReports/PWRA/PDFHSReportClassRoster.do"> > Class Roster Report</a></h5>
                        <h5>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="/TrainingReports/PWRA/PDFHSReportGeneralSessionAttendance.do"> > General Session Attendance Report</a></h5>
                        <h5>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="/TrainingReports/PWRA/PDFEnrollmentSummaryReport.do"> > Training Enrollment Report</a></h5>
                        <h5>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="/TrainingReports/PWRA/PDFEnrollmentChangeReport.do"> > Enrollment Change Report</a></h5>                        
                        -->
                    </td>
                </tr>
			
		</td>
	</tr>
</table>
