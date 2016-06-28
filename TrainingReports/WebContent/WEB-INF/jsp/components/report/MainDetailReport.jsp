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
	MainDetailReportWc wc = (MainDetailReportWc)request.getAttribute(MainDetailReportWc.ATTRIBUTE_NAME);
	Employee employee = wc.getEmployee();
    
	Employee reportsTo = wc.getReportsTo();
    TrainingOrder thisTrainingOrder=wc.getTrainingOrder();
	Attendance[] courses = wc.getCourseAttendance();
	SceFull[] sce = wc.getFullSce();
	List exams = wc.getExams();
	OverallResult or = wc.getOverall();
	User user = wc.getUser();
	List prods = user.getProducts();
	String currProductCode = wc.getUserFilter().getProduct();
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
		&& (url.indexOf("localhost") > 0 || url.indexOf("trt-stg.pfizer.com") > 0 )) {
		sceUrl = "http://sce-stg.pfizer.com/SCEWeb/evaluation/evaluateTR";	
	} 
    if ( url != null 
		&& (url.indexOf("trt-tst.pfizer.com") > 0 )) {
		sceUrl = "http://sce-tst.pfizer.com/SCEWeb/evaluation/evaluateTR";	
	}
    if ( url != null 
		&& (url.indexOf("trt-dev.pfizer.com") > 0)) {
		sceUrl = "http://sce-dev.pfizer.com/SCEWeb/evaluation/evaluateTR";	
	} 
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
	function grantExemption() {
		var my = document.getElementById('mybutton');
		var tmpStr = my.innerHTML + '';
		if (tmpStr.length == 16) {
			window.open('grantExemption.do?emplId=<%=employee.getEmplId()%>&prodCd=<%=currentProduct.getProductCode()%>&lastName=<%=employee.getLastName()%>&firstName=<%=employee.getFirstName()%>','mywindow','width=425,height=175,left=50,top=200,screenX=0,screenY=100')
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
			<img src="<%=AppConst.IMAGE_DIR%>/logos/<%=wc.getUserFilter().getProduct()%>_logo.gif" alt="<%=wc.getUserFilter().getProduct()%>" >
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
									if(emplStatus.equalsIgnoreCase("On-Leave")||emplStatus.equalsIgnoreCase("Terminated") ){ %>
										<font color="red"><b><%=emplStatus%></b></font>
								<%	} else { %>
										<%=emplStatus%>
                                <%	} %>
								 </td>
                                <% if ( !Util.isEmpty( wc.getEmployeeImage() ) ) {%>
									<td rowspan="14" valign="top"><img src="<%=wc.getEmployeeImage()%>" /></td>
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
								<th  align="left">Training Information</th>
							</tr>
							<tr>
								<td>Overall</td>
							</tr>
							<tr class="even">
                            <%
                            String courseSatus=or.isPassed()?"Completed":or.isOnLeave()?"On Leave":"Not Complete";
                            %>
								<td><%=courseSatus%></td>
                                
							</tr>
                            <% if(courseSatus.equalsIgnoreCase("Not Complete") && wc.getUser().isExemptionRole()){ %>
								<tr>
									<td>
<!--									 <A  HREF="#" CLASS="buttontext"  onclick="window.open('grantExemption.do?emplId=<%=employee.getEmplId()%>&prodCd=<%=currentProduct.getProductCode()%>&lastName=<%=employee.getLastName()%>&firstName=<%=employee.getFirstName()%>','mywindow','width=425,height=175,left=50,top=200,screenX=0,screenY=100')" > -->
									 <DIV   class="mybuttoncls" align="center" ID="mybutton" onclick="grantExemption()">
										Grant Exemption
									 </DIV>
<!--									 </A> -->
									 <form name="exemptAction" action="/TrainingReports/overview/grantExemption" >
										<input type="hidden"  name="fromReason" value="true">
										<input type="hidden"  name="emplId" value="<%=employee.getEmplId()%>">
										<input type="hidden"  name="prodCd" value="<%=wc.getUserFilter().getProduct()%>">
										<input type="hidden"  name="exemptReason" value="">
									 </form>
									 
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
	<td><%=thisTrainingOrder.getTrmId()%></td>
	<td><%=thisTrainingOrder.getOrderDate()%> </td>
	</tr> 
   </table>
   
  
  </td>
  </tr>
  <!--End of  Training Materials-->
  
    
	<tr>
		<td><img src="<%=AppConst.IMAGE_DIR%>/spacer.gif" height="15"></td>
	</tr>
    
	<tr>
		<td>
			<table class="blue_table" width="925px">
				<tr>
					<th colspan="6" align="left">Pedagogue Exams</th>
				</tr>
				<tr style="font-weight:bold;">
					<td width="150px">Exam Name</td>
                    <td width="75px">Issue Date</td>
					<td width="75px">Exam Date</td>
					<td width="75px">Exam Score</td>
					<td width="450px">Learning System Modules Covered</td>
					<td width="100px">Coaching</td>
				</tr>
				<%  oddEvenFlag = false;
					for(Iterator it = exams.iterator(); it.hasNext();){ 
					PedagogueExam currExam = (PedagogueExam)it.next();
					oddEvenFlag = !oddEvenFlag; 
				%>
					<tr class="<%=oddEvenFlag?"even":"odd"%>">
						<td><%=currExam.getExamName()%></td>
                        <td><%=Util.toEmpty(currExam.getExamIssueDate())%>
						<td><%=currExam.getExamDate()==null?"":Util.formatDateShort(currExam.getExamDate())%></td>
						<td><%=currExam.getExamScore()==null?"":currExam.getExamScore()%></td>
                        <td><a href="<%=((ExamModule)wc.getExamModules().get( currExam.getExamName() )).getLink()%>" target="_new"><%=((ExamModule)wc.getExamModules().get( currExam.getExamName() )).getModules()%></a></td>
						<% if ("Recommended".equals( currExam.getCoaching() )) {%>
							<td><%=currExam.getCoaching()%>  <img src="<%=AppConst.APP_ROOT%>/resources/images/i_10x10.gif"  onMouseOver="stm(Text['examRecommended'],Style[0])" onMouseOut="htm()" style="font-style: italic;" ></td>
						<% } else if ("Not Required".equals( currExam.getCoaching() )) { %>
							<td><%=currExam.getCoaching()%>  <img src="<%=AppConst.APP_ROOT%>/resources/images/i_10x10.gif"  onMouseOver="stm(Text['Not Required'],Style[0])" onMouseOut="htm()" style="font-style: italic;" ></td>
						<% } else { %>
							<td><%=currExam.getCoaching()%>  <img src="<%=AppConst.APP_ROOT%>/resources/images/i_10x10.gif"  onMouseOver="stm(Text['Test Required'],Style[0])" onMouseOut="htm()" style="font-style: italic;" ></td>
						<% } %>
					</tr>
				<% } %>
			</table>
		</td>
	</tr>
	<tr>
		<td><img src="<%=AppConst.IMAGE_DIR%>/spacer.gif" height="15"></td>
	</tr>
	<tr>
		<td>
			<% if ( wc.getOverallProcessor().getAttendanceProcessor() != null) { %>
	
				<table class="blue_table" >
					<tr>
						<th colspan="3" align="left">Attendance</th>
					</tr>
					<tr style="font-weight:bold;">
						<td width="150px">Training</td>
						<td width="100px">Date</td>
						<td width="100px">Attendance</td>
					</tr>
					<%  oddEvenFlag = false;
						if ( courses != null) { %>
						<% for ( int i=0; i < courses.length; i++) { 
							oddEvenFlag = !oddEvenFlag; 
						%>
							<tr class="<%=oddEvenFlag?"even":"odd"%>">
								<td><%=courses[i].getCourseDesc() %></td>
								<!-- check of Transitional Training and Regional Training -->								
								<%	SceProcessor scep = wc.getOverallProcessor().getSceProcessor();
									if (AttendanceProcessor.STATUS_REGIONAL.equals(courses[i].getStatus())) {
										courses[i].setStartDate(null);
									}
									if (scep != null && 
										(AttendanceProcessor.STATUS_SCHEDULED.equals(courses[i].getStatus()) 
										|| AttendanceProcessor.STATUS_REGIONAL.equals(courses[i].getStatus())) ) {
										if (scep.getComppetenceMap().containsKey(employee.getEmplId())
											|| scep.getNeedsImprovementMap().containsKey(employee.getEmplId()) ) {
											courses[i].setStatus(AttendanceProcessor.STATUS_ATTENED);
											if ( sce != null) { 
												courses[i].setStartDate(sce[0].getEvalDate());
											}
										}
									}
								%>
								<td><%=Util.formatDateShort(courses[i].getStartDate())%> </td>
								<td><%=courses[i].getStatus() %></td>
								
							</tr> 
						<% } %>
					<% } %>
					
				</table>
			<% } %>
		</td>
	</tr>
    
	<tr>
		<td><img src="<%=AppConst.IMAGE_DIR%>/spacer.gif" height="15"></td>
	</tr>
	<tr>
		<td>
		
			<% if ( wc.getOverallProcessor().getSceProcessor() != null) { %>
			<form name="evaluateFormView" id="evaluateForm" method="post" action="<%=sceUrl%>" target="_popup">
                <input type="hidden" name="eventId" value="<%=AppConst.EVENTID_FFT%>" id="eventId"/>
				<input type="hidden" name="emplId" value="<%=employee.getEmplId()%>" id="emplId"/>
				<input type="hidden" name="productCode" value="<%=wc.getUserFilter().getProduct()%>" id="productCode"/>
				<input type="hidden" name="action" value="view" id="action"/>
			</form>
			<form name="evaluateFormNew" id="evaluateForm" method="post" action="<%=sceUrl%>" target="_popup">
                <input type="hidden" name="eventId" value="<%=AppConst.EVENTID_FFT%>" id="eventId"/>
				<input type="hidden" name="emplId" value="<%=employee.getEmplId()%>" id="emplId"/>
				<input type="hidden" name="product" value="<%=currentProduct.getProductDesc()%>" id="product"/>
				<input type="hidden" name="productCode" value="<%=wc.getUserFilter().getProduct()%>" id="productCode"/>
				<input type="hidden" name="evaluatorEmplId" value="<%=wc.getUser().getEmplid()%>" id="evaluatorEmplId"/>
				<input type="hidden" name="action" value="create" id="action"/>
			</form>

				<table class="blue_table" width="900px">
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
					<%	oddEvenFlag = false;
						if ( sce != null) { %>
						<% for ( int i=0; i < sce.length; i++) { 
							oddEvenFlag = !oddEvenFlag; 
						%>
							<tr class="<%=oddEvenFlag?"even":"odd"%>">
								<td><%=Util.formatDateShort(sce[i].getEvalDate())%> </td>
								<td><%=(sce[i].getEvalFName()==null?"":sce[i].getEvalFName()) + " " + (sce[i].getEvalLName()==null?"":sce[i].getEvalLName())%></td>
								
								<% if ( !Sce.STATUS_NOT_COMPLETE.equals( sce[i].getRating() ) ) { %>
									<td><a href="javascript:document.evaluateFormView.submit();"><%=sce[i].getRating()%></a></td>
								<% }  else { %>
									<td><%=sce[i].getRating()%></td>
								<% } %>
								<td><%=(sce[i].getComment())==null?"":sce[i].getComment() %></td>
								<% if ( Sce.STATUS_NI.equals( sce[i].getRating() ) ) { %>
									<td><a href="#">new evaluation</a></td>
									<td>Recommended <img src="<%=AppConst.APP_ROOT%>/resources/images/i_10x10.gif"  onMouseOver="stm(Text['sceRecommended'],Style[0])" onMouseOut="htm()" style="font-style: italic;" ></td>
								<% } else if ( Sce.STATUS_UN.equals( sce[i].getRating() ) ) { %>
									<td><a href="#">new evaluation</a></td>
									<td>Required <img src="<%=AppConst.APP_ROOT%>/resources/images/i_10x10.gif"  onMouseOver="stm(Text['Required'],Style[0])" onMouseOut="htm()" style="font-style: italic;" ></td>
								<% } else if ( Sce.STATUS_NOT_COMPLETE.equals( sce[i].getRating() ) ) { %>
									<td><a href="javascript:document.evaluateFormNew.submit();">new evaluation</a></td>
									<td></td>
								<% } else { %>
									<td></td>
									<td>None</td>
								<% } %>
							</tr> 
						<% } %>
					<% } %>			
				</table>
			<% } %>
		</td>
	</tr>
</table>