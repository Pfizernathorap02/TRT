<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.pfizer.webapp.AppConst"%>
<%@ page import="com.pfizer.webapp.user.User"%>
<%@ page import="com.pfizer.webapp.user.UserSession"%>
<%@ page import="com.pfizer.webapp.user.UserTerritory"%>
<%@ page import="com.pfizer.webapp.wc.global.RBUUserBarEmailWc"%>
<%@ page import="com.pfizer.webapp.wc.global.RBUUserBarWc"%>
<%@ page import="com.tgix.Utils.Util"%>

<%
	RBUUserBarEmailWc wc = (RBUUserBarEmailWc)request.getAttribute(RBUUserBarWc.ATTRIBUTE_NAME);
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
  
  if (appURL.indexOf("localhost") != -1 || appURL.indexOf("amrnwlw058") != -1 || appURL.indexOf("trt-dev.pfizer.com") != -1 || appURL.indexOf("trt-dev") != -1){
    appEnvironment = "Development";
  }else if (appURL.indexOf("trt-tst.pfizer.com") != -1 || appURL.indexOf("trt-tst") != -1){			//trint.pfizer.com
    appEnvironment = "Integration";
  }else if (appURL.indexOf("trt-stg.pfizer.com") != -1 || appURL.indexOf("trt-stg") != -1){			//trstg.pfizer.com
    appEnvironment = "Staging";
  }else if (appURL.indexOf("trt.pfizer.com") != -1 ){
    appEnvironment = "Production";
  }
    
%>
<% if (uSession.isAdmin()) { %>
<script type="text/javascript" language="JavaScript">
	function adminFunction(selected) {
		window.location=selected.options[selected.selectedIndex].value;
	}
    
    
</script>





<% } %>

<table  class="no_space_table"   background="<%=AppConst.IMAGE_DIR%>/h3_background.gif" style="background-repeat: repeat-y;" bgcolor="#eff7fc">
	<tr>
		<td><img src="<%=AppConst.IMAGE_DIR%>/spacer.gif" width="30px" height="30px"></td>
	<% if ( user != null) { %>		
			<td  nowrap><b>Name:</b> <%=user.getName()%></td>
			<td ><img src="<%=AppConst.IMAGE_DIR%>/spacer.gif" width="20px"></td>
			<td  nowrap><b>Role:</b> <%=user.getRole()%></td>
            
            <td  nowrap><b> &nbsp;&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp; Application Environment:</b> <font color="red"><%=appEnvironment%></font></td>
            
			<td ><img src="<%=AppConst.IMAGE_DIR%>/spacer.gif" width="20px"></td>
			<td nowrap>
				<% if ( !user.isAdmin() || !Util.isEmpty(user.getDisplayCluster())) { %>
					<b>Cluster:</b> <%=user.getDisplayCluster()%>
				<% } %>
			</td>
			<td ><img src="<%=AppConst.IMAGE_DIR%>/spacer.gif" width="20px"></td>
			<td nowrap>
				<% if ( !user.isAdmin() || !Util.isEmpty(user.getTeam())) { %>
					<b>Team:</b> <%=user.getTeam()%>
				<% } %>	
			</td>
			<td ><img src="<%=AppConst.IMAGE_DIR%>/spacer.gif" width="20px"></td>
	
			<td  width="100%">&nbsp; </td>
<%
String fromReport=session.getAttribute("ReportType")==null?"":session.getAttribute("ReportType").toString();
if (uSession.isAdmin() && !"RBU".equalsIgnoreCase(fromReport))



 
 
 { %>
			
<% }%>
			
	<% } %>
	</tr>
</table>
