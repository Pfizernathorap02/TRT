<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.pfizer.db.Employee"%>
<%@ page import="com.pfizer.db.Product"%>
<%@ page import="com.pfizer.webapp.AppConst"%>
<%@ page import="com.pfizer.webapp.AppQueryStrings"%>
<%@ page import="com.pfizer.webapp.user.User"%>
<%@ page import="com.pfizer.webapp.user.UserSession"%>
<%@ page import="com.pfizer.webapp.wc.components.ProductSelectWc"%>
<%@ page import="com.pfizer.webapp.wc.components.ReportSelectWc"%>
<%@ page import="com.pfizer.webapp.wc.components.admintoolsSelectPDFHSWc"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.Hashtable"%>
<%@ page import="java.util.Enumeration"%>
<%@ page import="java.util.Vector"%>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.HashMap" %>
<%
	admintoolsSelectPDFHSWc wc = (admintoolsSelectPDFHSWc)request.getAttribute(admintoolsSelectPDFHSWc.ATTRIBUTE_NAME);
	List products = wc.getUser().getProducts();
    User user;
 	UserSession uSession;    
    boolean bDisplayPrintingMenu = true;
    AppQueryStrings qStrings = new AppQueryStrings();
    uSession = UserSession.getUserSession(request);
    bDisplayPrintingMenu = uSession.isAdmin() || uSession.isSuperAdmin();      
    
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
                        <h5>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="javascript:openSessionAttendance();"> > General Session Attendance</a></h5>
                        <h5>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="javascript:openTRMEMail();"> > email Invitation</a></h5>                        
                        <h5>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="javascript:openGenerateP2L();"> > Generate P2L Registration File</a></h5>                        
                    </td>
                </tr>
			</table>
		</td>
	</tr>
</table>