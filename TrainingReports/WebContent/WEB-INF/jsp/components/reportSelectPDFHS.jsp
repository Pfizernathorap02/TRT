<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.pfizer.db.Employee"%>
<%@ page import="com.pfizer.db.Product"%>
<%@ page import="java.util.List"%>
<%@ page import="com.pfizer.webapp.AppConst"%>
<%@ page import="com.pfizer.webapp.wc.components.ProductSelectWc"%>
<%@ page import="com.pfizer.webapp.wc.components.ReportSelectPDFHSWc"%>
<%@ page import="com.pfizer.webapp.wc.components.ReportSelectWc"%>
<%
	ReportSelectPDFHSWc wc = (ReportSelectPDFHSWc)request.getAttribute(ReportSelectPDFHSWc.ATTRIBUTE_NAME);
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
                        
                        <h5>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="/TrainingReports/PWRA/PDFHSReportTrainingSchedule"> > Training Schedule Report</a></h5>                        
                        <h5>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="/TrainingReports/PWRA/PDFHSReportVariance"> > Variance Report</a></h5>
                        <h5>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="/TrainingReports/PWRA/PDFHSReportPersonalAgenda"> > Personalized Agenda Report</a></h5>
                        <h5>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="/TrainingReports/PWRA/PDFHSReportClassRoster"> > Class Roster Report</a></h5>
                        <h5>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="/TrainingReports/PWRA/PDFHSReportGeneralSessionAttendance"> > General Session Attendance Report</a></h5>
                        <h5>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="/TrainingReports/PWRA/PDFEnrollmentSummaryReport"> > Training Enrollment Report</a></h5>
                        <h5>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="/TrainingReports/PWRA/PDFEnrollmentChangeReport"> > Enrollment Change Report</a></h5>                        
                    </td>
                </tr>
			
		</td>
	</tr>
</table>