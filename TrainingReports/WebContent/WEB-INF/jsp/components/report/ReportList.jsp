<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.pfizer.db.Attendance"%>
<%@ page import="com.pfizer.db.Employee"%>
<%@ page import="com.pfizer.db.PassFail"%>
<%@ page import="com.pfizer.db.Sce"%>
<%@ page import="com.pfizer.processor.AttendanceProcessor"%>
<%@ page import="com.pfizer.processor.OverallProcessor"%>
<%@ page import="com.pfizer.processor.OverallResult"%>
<%@ page import="com.pfizer.processor.PassFailProcessor"%>
<%@ page import="com.pfizer.processor.SceProcessor"%>
<%@ page import="com.pfizer.webapp.AppConst"%>
<%@ page import="com.pfizer.webapp.AppQueryStrings"%>
<%@ page import="com.pfizer.webapp.wc.components.report.ReportListWc"%>
<%@ page import="com.tgix.Utils.Util"%>
<%@ page import="java.awt.GraphicsEnvironment"%>
<%@ taglib uri="/WEB-INF/dynamic-include.tld" prefix="inc" %>
<%@ page import="java.util.List"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.Hashtable"%>
<%@ page import="java.util.Enumeration"%>
<%@ page import="java.util.Vector"%>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.HashMap" %>


<%
	ReportListWc wc = (ReportListWc)request.getAttribute(ReportListWc.ATTRIBUTE_NAME);
	List reportNames = wc.getReportNames();
	PassFailProcessor pfp = wc.getProcessor().getPassFailProcessor();
	AttendanceProcessor aProcessor = wc.getProcessor().getAttendanceProcessor();
	SceProcessor sProcessor = wc.getProcessor().getSceProcessor();
	AppQueryStrings qString = wc.getQueryStrings();
	String reverseString = "";
	if ( !"reverse".equals( qString.getSortBy().getDirection() ) ) {
		reverseString = "&sb_direction=reverse";
	}	
	List allEmployees = wc.getProcessor().getAllEmployeeList( qString.getSortBy() );
	//Set allEmployeeIds = allEmployees.keySet();
	
%>
<script type="text/javascript" language="JavaScript">
var emailWindow;

function submitEmail() {
	var myform = document.emailSelectForm;
	var emails = ''; 
	var counter = 0;
	var currentUserEmail = '<%=wc.getUser().getEmail()%>';
    var answer = true;
    var doneflag = false;

	for(var i=0;i<myform.length;++i) { 
		if(myform.elements[i].type=='checkbox') { 
			if (myform.elements[i].checked) {
				if ( counter == 0 ) {
					emails = myform.elements[i].value;
				} else if ( doneflag ) {
                    myform.elements[i].checked = false;
                } else {
                    var tmpstr = emails + '; ' + myform.elements[i].value; 
                    if ( tmpstr.length > 1950 ) {
                        var answer = confirm('Too many email recipients selected. The first ' + (counter-1) + ' recipients can be emailed now, the others will be de-selected.  Would you like to continue?');
                        if (answer) {
                            myform.elements[i].checked = false;
                            doneflag = true;
                        } else {
                            i = myform.length;
                        }
                    } else {
    					emails = emails + '; ' + myform.elements[i].value;
                    }
				}
				counter = counter + 1;
			} 
		} 
	}
	
	var emailToStr = 'mailto:';	
	var ccString = '';
	var subjectStr = '&subject=FFT Training Follow-up';	
	var sendToStr = '&subject=';
	if (counter == 1) {
		sendToStr = emailToStr + emails +  subjectStr;
	} else {
		sendToStr = emailToStr + '?bcc=' + emails + subjectStr;
	}

    if (answer) {
    	window.location = sendToStr;
    } 
}

function checkAll() {
	var myform = document.emailSelectForm;
	for(var i=0;i<myform.length;++i) { 
		if(myform.elements[i].type=='checkbox') { 
			myform.elements[i].checked = true; 
		} 
	}	
}
function unCheckAll() {
	var myform = document.emailSelectForm;
	for(var i=0;i<myform.length;++i) { 
		if(myform.elements[i].type=='checkbox') { 
			myform.elements[i].checked = false; 
		} 
	}	
}

</script>


<table class="no_space_width">
	<tr>
		<td>
			<p id="table_inst_title">Click on employee name to get detailed report</p>
		</td>
		<td>&nbsp;</td>
		<td>
			<% if ( "SVP".equals( wc.getUser().getRole() )
					|| "VP".equals( wc.getUser().getRole() ) 
					|| wc.getUser().isAdmin()  
					|| "RM".equals( wc.getUser().getRole() ) 
					|| "ARM".equals( wc.getUser().getRole() ) ){ %>
				<table class="no_space_width" style="font-size: .8em;" >
					<tr>
						<td>&nbsp;</td>
						<td bgcolor="#ffd699" width="10px"></td>
						<td>&nbsp;Regional Manager</td>
						<td>&nbsp;</td>
						<td bgcolor="#c2d6eb" width="10px"></td>
						<td>&nbsp;District Manager</td>
					</tr>
				</table>
			<% } else { %>
				<table class="no_space_width" style="font-size: .8em;" >
					<tr>
						<td>&nbsp;</td>
						<td bgcolor="#c2d6eb" width="10px"></td>
						<td>&nbsp;District Manager</td>
					</tr>
				</table>
			<% } %>
		</td>
	</tr>
</table>
<div>
	<div id="table_inst">
		<img src="<%=AppConst.APP_ROOT%>/resources/images/excel.gif" width="15px">&nbsp;<a href="<%=AppConst.APP_ROOT%>/overview/listreport?downloadExcel=true&<%=qString.getFullQueryString()%>">Download to Excel</a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a  href="<%=AppConst.APP_ROOT%>/overview/listreport?<%=qString.getQueryStringsNoSort()%>">Back to default sort</a>
	</div>
	<div class="top_table_buttons" style="float:right;">	
		<img src="<%=AppConst.IMAGE_DIR%>/training/b_checkall.gif"onclick="checkAll()" />
		<img src="<%=AppConst.IMAGE_DIR%>/training/b_uncheckall.gif" onclick="unCheckAll()" />
		<img src="<%=AppConst.IMAGE_DIR%>/training/b_sendemail.gif" onclick="submitEmail()" />
	</div>
	<div class="clear"></div>	
</div>

<table cellspacing="0" id="employee_table" width="100%">
	<form name="emailSelectForm" >
	<tr>
		<% if ( "district".equals(qString.getSortBy().getField() ) ) { %>
			<th nowrap><a style="color:ffffff;" href="<%=AppConst.APP_ROOT%>/overview/listreport?<%=qString.getQueryStringsNoSort()%>&sb_field=district<%=reverseString%>">District</a>&nbsp;<img src="/TrainingReports/resources/images/<%=Util.isEmpty(reverseString)?"button_arrowdown.gif":"button_arrowup.gif"%>"></th>
		<% } else { %>
			<th nowrap><a style="color:ffffff;" href="<%=AppConst.APP_ROOT%>/overview/listreport?<%=qString.getQueryStringsNoSort()%>&sb_field=district">District</a></th>
		<% } %>
		<% if ( "lname".equals(qString.getSortBy().getField() ) ) { %>
			<th nowrap><a style="color:ffffff;" href="<%=AppConst.APP_ROOT%>/overview/listreport?<%=qString.getQueryStringsNoSort()%>&sb_field=lname<%=reverseString%>">Last Name</a>&nbsp;<img src="/TrainingReports/resources/images/<%=Util.isEmpty(reverseString)?"button_arrowdown.gif":"button_arrowup.gif"%>"></th>
		<% } else { %>
			<th nowrap><a style="color:ffffff;" href="<%=AppConst.APP_ROOT%>/overview/listreport?<%=qString.getQueryStringsNoSort()%>&sb_field=lname">Last Name</a></th>
		<% } %>
		<% if ( "fname".equals(qString.getSortBy().getField() ) ) { %>
			<th nowrap><a style="color:ffffff;" href="<%=AppConst.APP_ROOT%>/overview/listreport?<%=qString.getQueryStringsNoSort()%>&sb_field=fname<%=reverseString%>">First Name</a>&nbsp;<img src="/TrainingReports/resources/images/<%=Util.isEmpty(reverseString)?"button_arrowdown.gif":"button_arrowup.gif"%>"></th>
		<% } else { %>
			<th nowrap><a style="color:ffffff;" href="<%=AppConst.APP_ROOT%>/overview/listreport?<%=qString.getQueryStringsNoSort()%>&sb_field=fname">First Name</a></th>
		<% } %>
		<% if ( "role".equals(qString.getSortBy().getField() ) ) { %>
			<th nowrap><a style="color:ffffff;" href="<%=AppConst.APP_ROOT%>/overview/listreport?<%=qString.getQueryStringsNoSort()%>&sb_field=role<%=reverseString%>">Role</a>&nbsp;<img src="/TrainingReports/resources/images/<%=Util.isEmpty(reverseString)?"button_arrowdown.gif":"button_arrowup.gif"%>"></th>
		<% } else { %>
			<th nowrap><a style="color:ffffff;" href="<%=AppConst.APP_ROOT%>/overview/listreport?<%=qString.getQueryStringsNoSort()%>&sb_field=role">Role</a></th>
		<% } %>
		<% if ( "empid".equals(qString.getSortBy().getField() ) ) { %>
			<th nowrap><a style="color:ffffff;" href="<%=AppConst.APP_ROOT%>/overview/listreport?<%=qString.getQueryStringsNoSort()%>&sb_field=empid<%=reverseString%>">Emplid</a>&nbsp;<img src="/TrainingReports/resources/images/<%=Util.isEmpty(reverseString)?"button_arrowdown.gif":"button_arrowup.gif"%>"></th>
		<% } else { %>
			<th nowrap><a style="color:ffffff;" href="<%=AppConst.APP_ROOT%>/overview/listreport?<%=qString.getQueryStringsNoSort()%>&sb_field=empid">Emplid</a></th>
		<% } %>
		<% if ( "team".equals(qString.getSortBy().getField() ) ) { %>
			<th nowrap><a style="color:ffffff;" href="<%=AppConst.APP_ROOT%>/overview/listreport?<%=qString.getQueryStringsNoSort()%>&sb_field=team<%=reverseString%>">Team</a>&nbsp;<img src="/TrainingReports/resources/images/<%=Util.isEmpty(reverseString)?"button_arrowdown.gif":"button_arrowup.gif"%>"></th>
		<% } else { %>
			<th nowrap><a style="color:ffffff;" href="<%=AppConst.APP_ROOT%>/overview/listreport?<%=qString.getQueryStringsNoSort()%>&sb_field=team">Team</a></th>
		<% } %>
		<%-- 
			This is where all the test names get generated, most products have 2 exams
			but a couple has 4.
		--%>
		<%  int testCounter = 1;
			String tmpTest; 
			for ( Iterator it = reportNames.iterator(); it.hasNext(); ) { 
				tmpTest = (String)it.next();
		%>
			<% if ( ((String)OverallProcessor.testColumnMap.get(tmpTest)).equals(qString.getSortBy().getField() ) ) { %>
				<th nowrap><a style="color:ffffff;" href="<%=AppConst.APP_ROOT%>/overview/listreport?<%=qString.getQueryStringsNoSort()%>&sb_field=<%=((String)OverallProcessor.testColumnMap.get(tmpTest))%><%=reverseString%>"><%=tmpTest%></a>&nbsp;<img src="/TrainingReports/resources/images/<%=Util.isEmpty(reverseString)?"button_arrowdown.gif":"button_arrowup.gif"%>"></th>
			<% } else { %>
				<th nowrap><a style="color:ffffff;" href="<%=AppConst.APP_ROOT%>/overview/listreport?<%=qString.getQueryStringsNoSort()%>&sb_field=<%=((String)OverallProcessor.testColumnMap.get(tmpTest))%>"><%=tmpTest%></a></th>
			<% } %>
			<% testCounter = testCounter + 1;%>
		<% } %>

		<% if (aProcessor != null) { %>
		
			<% if ( "attendance".equals(qString.getSortBy().getField() ) ) { %>
				<th nowrap><a style="color:ffffff;" href="<%=AppConst.APP_ROOT%>/overview/listreport?<%=qString.getQueryStringsNoSort()%>&sb_field=attendance<%=reverseString%>">Attendance</a>&nbsp;<img src="/TrainingReports/resources/images/<%=Util.isEmpty(reverseString)?"button_arrowdown.gif":"button_arrowup.gif"%>"></th>
			<% } else { %>
				<th nowrap><a style="color:ffffff;" href="<%=AppConst.APP_ROOT%>/overview/listreport?<%=qString.getQueryStringsNoSort()%>&sb_field=attendance">Attendance</a></th>
			<% } %>
		<% } %>
	
		<% if (sProcessor != null) { %>
			
			<% if ( "sce".equals(qString.getSortBy().getField() ) ) { %>
				<th nowrap><a style="color:ffffff;" href="<%=AppConst.APP_ROOT%>/overview/listreport?<%=qString.getQueryStringsNoSort()%>&sb_field=sce<%=reverseString%>">SCE</a>&nbsp;<img src="/TrainingReports/resources/images/<%=Util.isEmpty(reverseString)?"button_arrowdown.gif":"button_arrowup.gif"%>"></th>
			<% } else { %>
				<th nowrap><a style="color:ffffff;" href="<%=AppConst.APP_ROOT%>/overview/listreport?<%=qString.getQueryStringsNoSort()%>&sb_field=sce">SCE</a></th>
			<% } %>
		<% } %>

		<% if ( "overall".equals(qString.getSortBy().getField() ) ) { %>
			<th nowrap><a style="color:ffffff;" href="<%=AppConst.APP_ROOT%>/overview/listreport?<%=qString.getQueryStringsNoSort()%>&sb_field=overall<%=reverseString%>">Overall</a>&nbsp;<img src="/TrainingReports/resources/images/<%=Util.isEmpty(reverseString)?"button_arrowdown.gif":"button_arrowup.gif"%>"></th>
		<% } else { %>
			<th nowrap><a style="color:ffffff;" href="<%=AppConst.APP_ROOT%>/overview/listreport?<%=qString.getQueryStringsNoSort()%>&sb_field=overall">Overall</a></th>
		<% } %>
		
		<th>Email</th>
	</tr>
	
	<%-- 
		This is where it loops through every employee to generate the list.
	--%>
	<% for ( Iterator it = allEmployees.iterator(); it.hasNext(); ) { 
		OverallResult oResult = (OverallResult)it.next();
		Employee emp = oResult.getEmployee();
		
		boolean doFlag = false;  // flag to determine of a row should be shown.
		
		// these set of if conditions determine if the row should be shown.
		// The list is not filtered before hand so every report loops through
		// every employee and here is where they get filtered.
		if ( "attend".equals( qString.getType() ) ) {
			doFlag = aProcessor.checkStatusByEmployeeId( qString.getSection(), emp.getEmplId() );
		}	
		if ( "sce".equals( qString.getType() ) ) {
			doFlag = sProcessor.checkStatusByEmployeeId( qString.getSection(), emp.getEmplId() );
		}	
		if ( "test".equals( qString.getType() ) ) {
			doFlag = pfp.checkStatusByEmployeeId( qString.getExam(), qString.getSection(), emp.getEmplId() );
		}	
		if ( "overall".equals( qString.getType() ) ) {
			if (OverallProcessor.STATUS_COMPLETE.equals( qString.getSection() ) ) {
				if ( oResult.isPassed() ) {
					doFlag = true;
				}
			}
			if (OverallProcessor.STATUS_ON_LEAVE.equals( qString.getSection() ) ) {
				if ( oResult.isOnLeave() ) {
					doFlag = true;
				}
			}
			if (OverallProcessor.STATUS_INCOMPLETE.equals( qString.getSection() ) ) {
				if ( !oResult.isPassed() && !oResult.isOnLeave()) {
					doFlag = true;
				}
			}
		}	
    %>	
		<% if (doFlag) { %>		
			<%-- 
				This is where the row color logic is happening.
			--%>		
			<% String trClass = "";
				if ( "VP".equals( emp.getRole() ) ) {
					trClass = "class='active_row avp_row'";
				}
				if ( "RM".equals( emp.getRole() ) || "ARM".equals( emp.getRole() ) ) {
					trClass = "class='active_row rm_row'";
				}
				if ( "DM".equals( emp.getRole() ) ) {
					trClass = "class='active_row dm_row'";
				}
			%>
			<tr <%=trClass%> >
				<td><%=(emp.getDistrictDesc()==null)?"":emp.getDistrictDesc()%></td>
				<td><a href="<%=AppConst.APP_ROOT%>/overview/detailreport?emplid=<%=emp.getEmplId()%>"><%=emp.getLastName()%> </a></td>
				<td><a href="<%=AppConst.APP_ROOT%>/overview/detailreport?emplid=<%=emp.getEmplId()%>"><%=emp.getPreferredName()%></a></td>
				<td><%=emp.getRole()%></td>
				<td><%=emp.getEmplId()%></td>
				<td><%=emp.getTeamCode()%></td>
				
				<% for ( Iterator itb = reportNames.iterator(); itb.hasNext(); ) { 
					String testName = (String)itb.next();
					String cssName = "";
					if ( "test".equals( qString.getType() ) ) {
						if ( testName.equals( qString.getExam() ) ) {
							cssName = "class='highlight_col'";
						}
					}
				%>				
					<td <%= PassFail.CONST_TEST_PASS.equals( pfp.getPassedByTestEmployeeId( testName, emp.getEmplId() ) ) ? "" : " style='color : ff0000;' " %> <%=cssName%> ><%=pfp.getTestScoreEmployeeId( testName, emp.getEmplId() )%></td>	
				<% } %>
				
				<% if (aProcessor != null) { %>				
					<td <%= AttendanceProcessor.STATUS_ATTENED.equals( aProcessor.getAttendanceStatusByEmployeeId( emp.getEmplId() ) ) ? "" : " style='color : ff0000;' " %> <%="attend".equals( qString.getType() )?"class='highlight_col'":""%> ><%=aProcessor.getAttendanceStatusByEmployeeId( emp.getEmplId() )%></td>
				<% } %>
				
				<% if (sProcessor != null) { %>
					<td <%= "DC".equals( sProcessor.getSceStatusByEmployeeId( emp.getEmplId() ) ) ? "" : " style='color : ff0000;' " %> <%="sce".equals( qString.getType() )?"class='highlight_col'":""%> ><%=sProcessor.getSceStatusByEmployeeId( emp.getEmplId() )%></td>
				<% } %>

				<td <%= oResult.isPassed() ? "" : " style='color : ff0000;' " %> <%="overall".equals( qString.getType() )?"class='highlight_col'":""%> ><%=oResult.isPassed()?"Completed":oResult.isOnLeave()?"On Leave":"NotComplete"%></td>
				<td><input name="MailSelectForm_email_<%=emp.getEmplId()%>" value="<%=emp.getEmail()%>"  type="checkbox"></td>
	
			</tr>
		<% } %>
	<% } %>
	</form>
</table>
