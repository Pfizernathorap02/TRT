<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.pfizer.db.Attendance"%>
<%@ page import="com.pfizer.db.Employee"%>
<%@ page import="com.pfizer.db.ExamModule"%>
<%@ page import="com.pfizer.db.PedagogueExam"%>
<%@ page import="com.pfizer.db.Product"%>
<%@ page import="com.pfizer.db.Sce"%>
<%@ page import="com.pfizer.db.SceFull"%>
<%@ page import="com.pfizer.db.TrainingOrder"%>
<%@ page import="com.pfizer.processor.AttendanceProcessor"%>
<%@ page import="com.pfizer.processor.OverallResult"%>
<%@ page import="com.pfizer.processor.SceProcessor"%>
<%@ page import="com.pfizer.webapp.AppConst"%>
<%@ page import="com.pfizer.webapp.user.User"%>
<%@ page import="com.pfizer.webapp.wc.components.report.MainDetailReportWc"%>
<%@ page import="com.pfizer.webapp.wc.components.report.MainExemptReportWc"%>
<%@ page import="com.tgix.Utils.Util"%>
<%@ page import="java.awt.GraphicsEnvironment"%>
<%@ taglib uri="/WEB-INF/dynamic-include.tld" prefix="inc" %>

<%
	MainExemptReportWc wc = (MainExemptReportWc)request.getAttribute(MainExemptReportWc.ATTRIBUTE_NAME);
	Employee employee = wc.getEmployee();
    Employee reportsTo = wc.getReportsTo();
    String exemptionReason=wc.getExemptionReason();
%>
<STYLE TYPE="text/css">
.mybutton   {
	border-style: inset;
	color: white;
    border-color: #1f61A9;
    background-color: #1f61A9;
    text-decoration: none;          
	width: 140px;
    position:center;
	cursor: hand;
    text-align: center;
}

</STYLE> 
<script type="text/javascript" language="JavaScript">
	var pressFlag = 'no';
	function revokeExemption() {
		if (pressFlag == 'no') {
			var my = document.getElementById('revokebutton');
			pressFlag = 'yes';
			my.innerHTML = '<font id="revokingtext">Revoking Exemption.  Please wait 30 seconds...</font>';
			//alert(my.innerHTML);
			blink('revokingtext');
			window.location='revokeExemption?emplid=<%=wc.getEmployee().getEmplId()%>&productCode=<%=wc.getProductCode()%>'
		} else {
			// do nothing
		}
	}
	
	function blink (elId) {
		var html = '';
		if (document.all) {
			html += 'var el = document.all.' + elId + ';';
		} else if (document.getElementById) {
			html += 'var el = document.getElementById("' + elId + '");';
		}
		html += 'el.style.visibility = ' + 'el.style.visibility == "hidden" ? "visible" : "hidden"';
		if (document.all || document.getElementById) {
			setInterval(html, 1000);
		}
	}
</script>
<table class="basic_table">
	<tr>		
		<td rowspan="10"><img src="<%=AppConst.IMAGE_DIR%>/spacer.gif" width="15"></td>
		<td>
			<br>
			<img src="<%=AppConst.IMAGE_DIR%>/logos/<%=wc.getProductCode()%>_logo.gif" alt="<%=wc.getProductCode()%>" >
			<br>		
			<b>District: </b> <%= Util.toEmpty(employee.getDistrictDesc())%>
			<br>
			<br>
			FFT Colleague Detail
			<p id="table_title" style="font-size:1.2em; font-weight:bold;"><%=employee.getLastName()%>, <%=employee.getPreferredName()%></p>
		</td>
	</tr>
	<tr>
		<td>
			<table class="no_space_width" width="0">
				<tr>
					<td width="0">
						<table class="blue_table" >
							<tr>
								<th colspan="3" align="left">Employee Information</th>
							</tr>
                            <tr class="even">
								<td style="font-weight: bold;">Employee Status</td>
                                <td width="150px">
                                <%
								String emplStatus=employee.getEmployeeStatus();
								if(emplStatus.equalsIgnoreCase("On-Leave")||emplStatus.equalsIgnoreCase("Terminated") ){
								%>
									<font color="red"><b><%=emplStatus%></b></font>
								<% }  else { %>
									<%=emplStatus%>
                                <% } %>
								 </td>
                                <% if ( !Util.isEmpty( wc.getEmployeeImg()) ) {%>
									<td rowspan="14" valign="top"><img src="<%=wc.getEmployeeImg()%>" /></td>
								<% } else { %>
									<td rowspan="14" >No Photo</td>
								<% } %>
							</tr>	
							<tr class="odd">
								<td style="font-weight: bold;">Employee ID</td>
								<td ><%=employee.getEmplId()%></td>
								
							</tr>
                            	
							<tr class="even">
								<td style="font-weight: bold;">Full Name</td>
								<td><%=employee.getLastName()%>, <%=employee.getPreferredName()%></td>
							</tr>	
							<tr class="odd">
								<td  style="font-weight: bold;">Gender</td>
								<td><%=employee.getGender()%></td>
							</tr>	
							<tr class="even">
								<td  style="font-weight: bold;">Hire Date</td>
								<td><%=Util.formatDateShort(employee.getHireDate())%></td>
							</tr>	
							<tr class="odd">
								<td style="font-weight: bold;">Promotion Date</td>
								<td><%=Util.formatDateShort(employee.getPromoDate())%></td>
							</tr>	
							<tr class="even">
								<td style="font-weight: bold;">E-Mail</td>
								<td><a href="mailto:<%=employee.getEmail()%>?subject=FFT Training Follow-up"><%=employee.getEmail()%></a></td>
							</tr>	
							<tr class="odd">
								<td style="font-weight: bold;">Therapeutic Cluster</td>
								<td><%=employee.getDisplayCluster()%></td>
							</tr>	
							<tr class="even">
								<td style="font-weight: bold;">Team</td>
								<td><%=employee.getTeamCode()%></td>
							</tr>	
							<tr class="odd">
								<td style="font-weight: bold;">Role</td>
								<td><%=employee.getRole()%></td>
							</tr>	
							<tr class="even">
								<td style="font-weight: bold;">Territory Code</td>
								<td><%=employee.getTerritoryId()%></td>
							</tr>	
							<tr class="odd">
								<td style="font-weight: bold;">Region</td>
								<td><%=employee.getAreaDesc()%></td>
							</tr>	
							<tr class="even">
								<td style="font-weight: bold;">District</td>
								<td><%=Util.toEmpty(employee.getDistrictDesc())%></td>
							</tr>	
							<tr class="odd">
								<td style="font-weight: bold;">Reports to</td>
								<td><% if (reportsTo != null) { %>
									<a href="mailto:<%=reportsTo.getEmail()%>?subject=FFT Training Follow-up">	<%=reportsTo.getLastName()%>, <%=reportsTo.getPreferredName()%> </a>
									<% } %>
								</td>
							</tr>	
						</table>
					</td>
					<td><img src="<%=AppConst.IMAGE_DIR%>/spacer.gif" width="15"></td>
					<td align="left" valign="top">
						<table class="blue_table" >
							<tr>
								<th  align="left">Training Information&nbsp;&nbsp;&nbsp;&nbsp;</th>
							</tr>
							<tr>
								<td>Overall</td>
							</tr>
							<tr class="even">
								<td nowrap>
                                Exempted
                                <%if(exemptionReason!=null){%>
                                &nbsp;-&nbsp;<%=exemptionReason%>
                                <%}%>
                                </td>
							</tr>
							
							<% if (wc.getUser().isExemptionRole()) {%>
								<tr class="odd">
									<td>
										<DIV   align="center" class="mybutton" ID="revokebutton" onclick="javascript:revokeExemption();" >
											Revoke Exemption
										</DIV> 
									</td>
								</tr>
							<% } %>
						</table>
					</td>
				</tr>
			</table>
		</td>
	</tr>
  
   <!-- For the Purpose of Training Materials-->
   <tr>
		<td><img src="<%=AppConst.IMAGE_DIR%>/spacer.gif" height="15"></td>
   </tr>
   
   <tr>
   <td>
   <table class="blue_table" >
	 <tr>
	 <th colspan="2" align="left">Training Material Shipment </th>
	 </tr>
     <tr style="font-weight:bold;">
		<td width="150px">TRM Order No.</td>
	    <td width="100px">Shipment Date</td>
     </tr>
     <tr >
	<td>&nbsp;</td>
	<td>&nbsp;</td>
	</tr> 
   </table>
   </td>
   </tr>
   
   
   	<tr>
		<td><img src="<%=AppConst.IMAGE_DIR%>/spacer.gif" height="15"></td>
	</tr>
    
	<tr>
		<td>
			<table class="blue_table" width="700px">
				<tr>
					<th colspan="6" align="left">Pedagogue Exams</th>
				</tr>
				<tr style="font-weight:bold;">
					<td width="150px">Exam Name</td>
                    <td width="75px">Issue Date</td>
					<td width="75px">Exam Date</td>
					<td width="75px">Exam Score</td>
					<td width="250px">Learning System Modules Covered</td>
					<td width="100px">Coaching</td>
				</tr>
				<%  
				%>
					<tr class="odd">
						<td>&nbsp;</td>
                        <td>&nbsp;</td>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
                        <td>&nbsp;</td>
                        <td>&nbsp;</td>
			</table>
		</td>
	</tr>
   
   <tr>
		<td><img src="<%=AppConst.IMAGE_DIR%>/spacer.gif" height="15"></td>
	</tr>
	<tr>
		<td>
			
				<table class="blue_table" >
					<tr>
						<th colspan="3" align="left">Attendance</th>
					</tr>
					<tr style="font-weight:bold;">
						<td width="150px">Training</td>
						<td width="100px">Date</td>
						<td width="100px">Attendance</td>
					</tr>
					<tr class="odd">
								<td>&nbsp;</td>
								<!-- check of Transitional Training and Regional Training -->								
								<td>&nbsp;</td>
								<td>&nbsp;</td>
								
							</tr> 
					
				</table>
			
		</td>
	</tr>
    
	<tr>
		<td><img src="<%=AppConst.IMAGE_DIR%>/spacer.gif" height="15"></td>
	</tr>
	<tr>
		<td>
		
			
			
				<table class="blue_table" width="700px">
					<tr>
						<th colspan="6" align="left">Sales Call Evaluation Results</th>
					</tr>
					<tr style="font-weight:bold;">
						<td width="100px">SCE Date</td>
						<td width="100px">Evaluator</td>
						<td width="100px">Score</td>
						<td width="100px">Comment</td>
						<td width="100px">Enter Evaluation</td>
						<td width="100px">Coaching</td>
					</tr>
					<tr class="odd">
								<td></td>
								<td></td>
								
							<td></td>
								
								<td></td>
                                
									<td></td>
								
							</tr> 
					</table>	
				
		</td>
	</tr>
     
   </table>
