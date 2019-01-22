<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.pfizer.webapp.AppConst"%>
<%@ page import="com.pfizer.webapp.user.User"%>
<%@ page import="com.pfizer.webapp.user.UserSession"%>
<%@ page import="com.pfizer.webapp.user.UserTerritory"%>
<%@ page import="com.pfizer.webapp.wc.global.UserBarWc"%>
<%@ page import="com.tgix.Utils.Util"%>
<%@ taglib prefix="s" uri="/struts-tags" %>

<%
	UserBarWc wc = (UserBarWc)request.getAttribute(UserBarWc.ATTRIBUTE_NAME);
	UserSession uSession = UserSession.getUserSession(request);	
	User user = wc.getUser();
	UserTerritory uTerritory = null;
	if ( user != null) {
		uTerritory = user.getUserTerritory();
	}
  
  String appURL = "";
  try{  
    appURL = request.getRequestURL().toString();
  }catch (Exception e){}
    

  
  String appEnvironment ="";
  
  if (appURL.indexOf("localhost:") != -1 || appURL.indexOf("amrnwlw058") != -1 || appURL.indexOf("trt-dev.pfizer.com") != -1 || appURL.indexOf("trt-dev") != -1 || appURL.indexOf("amrndhl275") != -1){
    appEnvironment = "Development";
  }else if (appURL.indexOf("trt-tst.pfizer.com") != -1 || appURL.indexOf("trt-tst") != -1){
    appEnvironment = "Integration";
  }else if (appURL.indexOf("trt-stg.pfizer.com") != -1 || appURL.indexOf("trt-stg") != -1){
    appEnvironment = "Staging";
  }else if (appURL.indexOf("trt.pfizer.com") != -1 ){
    appEnvironment = "Production";
  }
    
%>
<% if (uSession.isAdmin()) { %>
<script type="text/javascript" lang="JavaScript">
	function adminFunction(selected) {
		window.location=selected.options[selected.selectedIndex].value;
	}
    
    
</script>





<% } %>

<table  class="no_space_table" background="<%= AppConst.IMAGE_DIR %>/h3_background.gif" style="background-repeat: repeat-y;" bgcolor="#eff7fc">
	<tr>
		<td><img src="<%= AppConst.IMAGE_DIR %>/spacer.gif" width="5px" height="30px"></td>
	<% if ( user != null) { %>		
			<td  nowrap><b>Name:</b> <%=user.getName()%></td>
			<td ><img src="<%=AppConst.IMAGE_DIR%>/spacer.gif" width="5px"></td>
			<td  nowrap><b>&nbsp;Role:</b> <%=user.getRole()%></td>
            
            <td><img src="<%=AppConst.IMAGE_DIR%>/spacer.gif" width="5px"></td>
				<td nowrap>
                <!-- Added for TRT Phase 2 - Added HQ user -->
				<% if ( (!user.isAdmin() &&  !user.isHQUser()) || !Util.isEmpty(user.getBusinessUnit())) { %>
				<%// if ( !user.isAdmin() || !Util.isEmpty(user.getBusinessUnit())) { %>
					<b>&nbsp;BU:</b> <%=user.getBusinessUnit()%>
				<% } %>
			</td>
            <td><img src="<%=AppConst.IMAGE_DIR%>/spacer.gif" width="5px"></td>
            <td nowrap><b>&nbsp;&nbsp;Environment:</b> <font color="red"><%=appEnvironment%></font></td>
            
				
            <td ><img src="<%=AppConst.IMAGE_DIR%>/spacer.gif" width="5px"></td>
				<td nowrap>
                <!-- Added for TRT Phase 2 - Added HQ user -->
				<% if ( (!user.isAdmin() &&  !user.isHQUser()) || !Util.isEmpty(user.getSalesOrganization())) { %>

				<%// if ( !user.isAdmin() || !Util.isEmpty(user.getSalesOrganization())) { %>
					<b>&nbsp;Sales Org:</b> <%=user.getSalesOrganization()%>
				<% } %>
			</td>	
			<td ><img src="<%=AppConst.IMAGE_DIR%>/spacer.gif" width="5px"></td>
	
			<!--<td  width="100%">&nbsp; </td>-->
<%
String fromReport=session.getAttribute("ReportType")==null?"":session.getAttribute("ReportType").toString();
//System.out.println("FeedBack USer in Header?"+uSession.getOrignalUser().isFeedbackUser());
if (uSession.isAdmin() && !"PowersPOA".equalsIgnoreCase(fromReport))
 { %>
			<td>
				<table class="basic_table">
					<form>
					<tr>
                    <%
                       //  if ((uSession.getOrignalUser().isAdmin() || uSession.getOrignalUser().isSuperAdmin()|| uSession.getOrignalUser().isTsrAdmin()) && (!uSession.getOrignalUser().isFeedbackUser())) 
                         if (( uSession.getOrignalUser().isSuperAdmin()|| uSession.getOrignalUser().isTsrAdmin()) && (!uSession.getOrignalUser().isFeedbackUser())) 
                         { 
                      %>
						<td>
							<label><b>&nbsp;Admin&nbsp;Functions&nbsp;&nbsp;</b></label>
						</td>
						<td>
								<select name="adminfunction" onchange="adminFunction(this)" >
									<option >Select function</option>
                                    <!--added for TRT major enhancement 3.6- F6-->
                                    <!-- <option value="/TrainingReports/activityDrilldownConfig.do">Activity Drill Down</option> -->
                                    <!-- <option value="/TrainingReports/phase/coursecomplete.do">Course Completion</option> -->
                                   	 <!--added for TRT major enhancement 3.6- F6-->
                                    <option value="/TrainingReports/delegateAccess">Delegate Access</option>
                                    <!-- Start: Modified for TRT 3.6 enhancement - F 4 -(admin configuration of employee grid)     -->    
                                    <!-- <option value="/TrainingReports/admin/employeeGridConfig.do">Employee Grid Configuration </option> -->
                                    <!-- End: Modified for TRT 3.6 enhancement - F 4 -(admin configuration of employee grid)       -->  
                                    <!--ends here-->
                                    <option value="/TrainingReports/admin/group">Group Administration</option>
                                    <option value="/TrainingReports/TSHTReports/searchTSHT">Historical Training Server Transcripts</option>                                  
                                    <option value="/TrainingReports/admin/editHomePage">Home Page Configuration</option>
                                    <option value="/TrainingReports/admin/phaseEvaluation">Phase Evaluation Administration</option>
									<!-- <option value="/TrainingReports/FailureReports/showFailureReport.do">PHR Ph. 6 â€“ TSR Ph. 4 Failure Rep.</option> -->                                   								
                                    <% // Start: Modified for TRT 3.6 enhancement - F 7.6 -(Gap analysis - product - course mapping)  %>
                                    <option value="/TrainingReports/adminHome/editGapAnalysisReport">Product Mappings</option>
                            		<% // End: Modified for TRT 7.6 enhancement - F 7.6 -(Gap analysis - product - course mapping)  %>
                                    <option value="/TrainingReports/simulateuser">Simulate Users</option>
									<!--<option value="/TrainingReports/sitereport/runreports.do">Site Usage</option>-->
                                    <!-- <option value="/TrainingReports/adminHome/trainingPath.do">Training Path Configuration</option> -->
                                    <option value="/TrainingReports/admin/user">User Admin</option>
                                    
                                    <option value="/TrainingReports/admin/accessApprovers">Access Approvers</option>
                                    <option value="/TrainingReports/admin/approverList">Pending Approvals</option>
                                    
                                </select>
						</td>
                        <%
                         }
                        %>
					</tr>
					</form>
				</table>
			</td>
<% }%>
			
	<% } %>
	</tr>
</table>
