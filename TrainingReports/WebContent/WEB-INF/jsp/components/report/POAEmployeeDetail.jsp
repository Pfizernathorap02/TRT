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
<%@ page import="com.pfizer.webapp.wc.components.report.POAEmployeeDetailWc"%>
<%@ page import="com.tgix.Utils.Util"%>
<%@ page import="java.awt.GraphicsEnvironment"%>
<%@ page import="com.pfizer.db.EmpSearchPOA"%>
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
	POAEmployeeDetailWc wc = (POAEmployeeDetailWc)request.getAttribute(POAEmployeeDetailWc.ATTRIBUTE_NAME);
	Employee employee = wc.getEmployee();
    
	Employee reportsTo = wc.getReportsTo();
    EmpSearchPOA[] esPOA = wc.getEmpSearchPOA();

	//Attendance[] courses = wc.getCourseAttendance();
	//OverallResult or = wc.getOverall();
	User user = wc.getUser();
	List prods = user.getProducts();
	//String currProductCode = wc.getUserFilter().getProduct();
    /*
	Product currentProduct = null;
	for ( Iterator it = prods.iterator(); it.hasNext(); ) {
		Product curr = (Product)it.next();
		if ( currProductCode.equals(curr.getProductCode() ) ) {
			currentProduct = curr;
		}
	}
	boolean oddEvenFlag = false;
	String sceUrl = "http://sce.pfizer.com/SCEWeb/evaluation/evaluateTR";
	
	String url = request.getRequestURL().toString();
	if ( url != null ) {
		url = url.toLowerCase();
	}
	if ( url != null 
		&& (url.indexOf("localhost") > 0 || url.indexOf("trstg.pfizer.com") > 0 )) {
		sceUrl = "http://scestg.pfizer.com/SCEWeb/evaluation/evaluateTR";	
	} 
    */
%>

<script type="text/javascript" language="JavaScript">
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
	function trim(str) { 
		str.replace(/^\s*/, '').replace(/\s*$/, ''); 
		str.replace(/^\n*/, '').replace(/\n*$/, ''); 
		alert(str);
		return str;
	} 
</script>

<STYLE TYPE="text/css">
.mybuttoncls   {
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
  
  .buttontext {color: white; 
                text-decoration: none;   
                font: bold 7pt Verdana;
                cursor: hand;}
</STYLE>

<table class="basic_table">
	<tr>		
		<td rowspan="10"><img src="<%=AppConst.IMAGE_DIR%>/spacer.gif" width="15"></td>
		<td>
			<br>
			
			<br>
			<b>District: </b> <%= Util.toEmpty(employee.getDistrictDesc())%>
			<br>
			<br>
			POWERA Mid-POA1 Colleague Detail
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
									if(emplStatus.equalsIgnoreCase("On-Leave")||emplStatus.equalsIgnoreCase("Terminated") ){ %>
										<font color="red"><b><%=emplStatus%></b></font>
								<%	} else { %>
										<%=emplStatus%>
                                <%	} %>
								 </td>
                                <% if ( !Util.isEmpty( wc.getEmployeeImg() ) ) {%>
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
								<td><a href="mailto:<%=employee.getEmail()%>?subject=Powers Mid-POA1  Follow-up"><%=employee.getEmail()%></a></td>
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
									<a href="mailto:<%=reportsTo.getEmail()%>?subject=Powers Mid-POA1  Follow-up">	<%=reportsTo.getLastName()%>, <%=reportsTo.getPreferredName()%> </a>
									<% } %>
								</td>
							</tr>	
						</table>
					</td>
					<td><img src="<%=AppConst.IMAGE_DIR%>/spacer.gif" width="15"></td>
					<td align="left" valign="top">
						<table class="blue_table" >
							<tr>
								<th  align="left">Training Information</th>
							</tr>
							<tr>
								<td>Overall</td>
							</tr>
							<tr class="even">
                            <%
                            //String courseSatus=or.isPassed()?"Completed":or.isOnLeave()?"On Leave":"Not Complete";
                            %>
								<td><%=esPOA[0].getOverallExamStatus()%></td>
                                
							</tr>
                            
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
	 <th colspan="3" align="left">Powers Mid-POA1  </th>
	 </tr>
     <tr style="font-weight:bold;">
		<td width="150px">Presentation</td>
	    <td width="150px">Completion Status</td>
        <td width="100px">Completion Date</td>
     </tr>
     <% for(int i=0; i< esPOA.length;i++){
        EmpSearchPOA es = esPOA[i];
        %>
     <tr >
	<td><%="Mid-POA1 Breeze"%>&nbsp;</td>
	<td><%=es.getExamStatus()%>&nbsp;</td>
    <td><%=Util.toEmpty(es.getCompletedDate())%>&nbsp;</td>
	</tr> 
    <%}%>
   </table>
   </td>
	</tr>
</table>