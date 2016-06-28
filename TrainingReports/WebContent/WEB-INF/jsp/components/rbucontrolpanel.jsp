<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.pfizer.db.Employee"%>
<%@ page import="com.pfizer.db.Product"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.HashMap"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.Map"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="java.util.Date"%>
<%@ page import="java.util.Collections"%>

<%@ page import="com.pfizer.webapp.AppConst"%>
<%@ page import="com.pfizer.webapp.wc.components.ProductSelectWc"%>
<%@ page import="com.pfizer.webapp.wc.components.RBUControlPanelWc"%>
<%@ page import="com.pfizer.webapp.wc.components.ReportSelectPDFHSWc"%>
<%@ page import="com.pfizer.webapp.wc.components.ReportSelectRBUWc"%>
<%@ page import="com.pfizer.webapp.wc.components.ReportSelectWc"%>
<%@ page import="com.tgix.printing.PrintingConstants"%>
<%
	RBUControlPanelWc wc = (RBUControlPanelWc)request.getAttribute(ReportSelectRBUWc.ATTRIBUTE_NAME);
	List products = wc.getUser().getProducts();
%>
<script language="JAVASCRIPT">

    function confirmProcess(url){
        var r=window.confirm("Are you sure you want to run this process?");
            if (r){
                openLink(url);
                return true;
                   }
                return false;
    }
    
    function openLink(url){
      window.open(url,'rr',"left=240, top=170,width=400,height=250,scrollbars=yes,location=no,status=yes,resizable = yes");
      }
      
   function confirmProcessEmail(url){
        var r=window.confirm("Are you sure you want to run this process?");
            if (r){
                openLink(url);
                return true;
                   }
                return false;
    }
    
    function openLinkEmail(url){
      window.open(url,'rr',"left=240, top=170,width=1000,height=700,scrollbars=yes,location=no,status=yes,resizable = yes");
      }   
</script>
<table class="no_space_width"> 
<%
   String appURL = request.getRequestURL().toString();
   String url = "/TrainingReports/RBU/";
   String requestURL = "";
                if (appURL.indexOf("localhost") != -1 || appURL.indexOf("amrnwlw058") != -1 || appURL.indexOf("trt-dev.pfizer.com") != -1 || appURL.indexOf("trt-dev") != -1){
                   requestURL = PrintingConstants.APPLICATION_PATH_JSP_DEV  + url;
              }else if (appURL.indexOf("trt-tst.pfizer.com") != -1 || appURL.indexOf("trt-tst") != -1){
                requestURL = PrintingConstants.APPLICATION_PATH_JSP_INT  + url;
              
              }else if (appURL.indexOf("trt-stg.pfizer.com") != -1 || appURL.indexOf("trt-stg") != -1){
                requestURL = PrintingConstants.APPLICATION_PATH_JSP_STG  + url;
              
              }else if (appURL.indexOf("trt.pfizer.com") != -1 ){
              requestURL = PrintingConstants.APPLICATION_PATH_JSP_PROD  + url;
              }
String requestURL_TravelFeed =   requestURL + "rbutravelfeed";
String requestURL_TravelFeedGEMS =   requestURL + "rbugemstravelfeed";

String requestURL_P2L =   requestURL + "mailP2LRegistrationFile";
String requestURL_TableAssignment =   requestURL + "getavailableweeks";

%>
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
                        <h5>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<div><a href="#" onclick="confirmProcess('/TrainingReports/RBU/rbuEnrollmentProcess')"> > Run Enrollment Process  </a></div></h5>                                          
                    </td>
                </tr>
                  <tr>
            		<td><img src="<%=AppConst.IMAGE_DIR%>/spacer.gif" width="45"></td>
                    <td >
                    <br>
                        <h5>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<div><a href="#" onclick="confirmProcess('/TrainingReports/RBU/placeShipmentOrders')"> > Place TRM Orders  </a></div></h5>  
                    </td>
                </tr>
                 <tr>
            		<td><img src="<%=AppConst.IMAGE_DIR%>/spacer.gif" width="45"></td>
                    <td><br>
                        <h5>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<div><a href="#" onclick="confirmProcess('/TrainingReports/RBU/createP2LRegistrations')"> > Generate P2L Registration File  </a></div></h5>                                          
                    </td>
                </tr>
                 <tr>
            		<td><img src="<%=AppConst.IMAGE_DIR%>/spacer.gif" width="45"></td>
                    <td><br>
                        <h5>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<div ><a href="#" onclick="confirmProcess('<%=requestURL_TravelFeed%>')"> > Generate Travel Feed </a></div></h5>                                          
                    </td>
                    </tr>
                                    <tr>
            		<td><img src="<%=AppConst.IMAGE_DIR%>/spacer.gif" width="45"></td>
                    <td><br>
                        <h5>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<div ><a href="#" onclick="confirmProcess('<%=requestURL_TravelFeedGEMS%>')"> > Generate GEMS Travel Feed </a></div></h5>                                          
                    </td>
                    </tr> 
                    
                  <tr>
            		<td><img src="<%=AppConst.IMAGE_DIR%>/spacer.gif" width="45"></td>
                    <td><br>
                        <h5>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<div><a href="/TrainingReports/PrintHome/emailInvitation"> > Email Invitations  </a></div></h5>                                          
                    </td>
                </tr>
               
              
                 <tr>
            		<td><img src="<%=AppConst.IMAGE_DIR%>/spacer.gif" width="45"></td>
                    <td><br>
                        <h5>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<div><a href="#" onclick="confirmProcess('/TrainingReports/RBU/rbuSandboxRefreshProcess')"> > Refresh Sandbox Data Views </a></div></h5>                                          
                    </td>
                </tr>
                
                                <tr>
            		<td><img src="<%=AppConst.IMAGE_DIR%>/spacer.gif" width="45"></td>
                    <td><br>
                        <h5>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<div><a href="#" onclick="confirmProcess('/TrainingReports/RBU/getavailableweeks')"> > Run Table Assignment Process</a></div></h5>                                          
                    </td>
                </tr>
                
                                                <tr>
            		<td><img src="<%=AppConst.IMAGE_DIR%>/spacer.gif" width="45"></td>
                    <td><br>
                        <h5>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<div><a href="#" onclick="confirmProcess('/TrainingReports/RBU/rbuSCEGuestProcess')"> > Run SCE Access Process for GTs</a></div></h5>                                          
                    </td>
                </tr>
                <tr><td>&nbsp;</td></tr>
                <tr><td>&nbsp;</td></tr>
                <tr><td>&nbsp;</td></tr>
                <tr><td>&nbsp;</td></tr>
                <tr><td>&nbsp;</td></tr>
                <tr><td>&nbsp;</td></tr>
                <tr><td>&nbsp;</td></tr>
                <tr><td>&nbsp;</td></tr>
                <tr><td>&nbsp;</td></tr>
                <tr>
            		<td><img src="<%=AppConst.IMAGE_DIR%>/spacer.gif" width="45"></td>
                    <td><br>
                        <h5>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<div><a href="#" onclick="confirmProcess('<%=requestURL_P2L%>')"> <font color="white"> Email P2L Registration File  (For testing)</font></a></div></h5>                                          
                    </td>
                </tr>
                <tr>
            		<td><img src="<%=AppConst.IMAGE_DIR%>/spacer.gif" width="45"></td>
                    <td><br>
                        <h5>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<div><a href="#" onclick="confirmProcess('/TrainingReports/RBU/testEmail')"> <font color="white">Test Email</font></a></div></h5>                                          
                    </td>
                </tr>
                
                <tr>
            		<td><img src="<%=AppConst.IMAGE_DIR%>/spacer.gif" width="45"></td>
                    <td><br>
                        <h5>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<div><a href="#" onclick="confirmProcess('/TrainingReports/RBU/emailReminder')"> <font color="white">Email Reminder</font></a></div></h5>                                          
                    </td>
                </tr>
                
                
                    <tr>
            		<td><img src="<%=AppConst.IMAGE_DIR%>/spacer.gif" width="45"></td>
                    <td><br>
                        <h5>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<div><a href="#" onclick="confirmProcess('/TrainingReports/RBU/rbuSandboxRefreshProcesse')"> <font color="white"> Run Sandbox Refresh Process  (For testing)</font></a></div></h5>                                          
                    </td>
                </tr>
               
		</td>
	</tr>
</table>