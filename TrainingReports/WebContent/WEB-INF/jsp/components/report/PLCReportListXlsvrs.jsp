
<%@ page import="com.pfizer.db.Employee"%>
<%@ page import="com.pfizer.webapp.AppConst"%>
<%@ page import="com.pfizer.webapp.AppQueryStrings"%>
<%@ page import="com.pfizer.webapp.user.User"%>
<%@ page import="com.pfizer.webapp.wc.components.report.POAReportList"%>
<%@ page import="com.pfizer.webapp.wc.components.report.ReportListWc"%>
<%@ page import="com.pfizer.webapp.wc.page.ListReportWpc"%>
<%@ page import="com.tgix.Utils.Util"%>
<%@ page import="com.tgix.html.FormUtil"%>
<%-- <%@ taglib uri="netui-tags-databinding.tld" prefix="netui-data"%>
<%@ taglib uri="netui-tags-html.tld" prefix="netui"%>
<%@ taglib uri="netui-tags-template.tld" prefix="netui-template"%> --%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.Hashtable"%>
<%@ page import="java.util.Enumeration"%>
<%@ page import="java.util.Vector"%>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.HashMap" %>


<!-- 
<netui:html> -->
<html>
           <%
           response.setContentType("application/vnd.ms-excel;charset=ISO-8859-2"); 
           pageContext.setAttribute("exceldownload","YES"); 
           response.setHeader ("Content-Disposition","attachment; filename=\"Completion Summary.xls\""); 
           String xlsType=(String)session.getAttribute("xlsType");
           String section=(String)session.getAttribute("section");  
        
           %>
           
           
   

<table cellspacing="0" id="employee_table" width="100%">
  <tr>
	
			
		
        <th align="left">Team</th>
        <th align="left">District</th>
		<th align="left">Last Name</th>
		<th align="left">First Name</th>
		<th align="left">Role</th>
		<th align="left">Emplid</th>
       <%if("Overall".equalsIgnoreCase(xlsType)){%>
            <th nowrap><b>Spiriva Exam 1</b></th>
                <th nowrap><b>Spiriva Exam 2</b></th>
                <th nowrap><b>Attendance</b></th>
          <!--      <th nowrap><b>Overall</b></th> !-->
            <%}%> 
        <th align="left"><%=xlsType%></th>
        </tr>
        

 
        <%
         Employee[] employeeBean=(Employee[])session.getAttribute("xlsBean");
          HashMap empHashMap=new HashMap();
         Employee emp;
         System.out.println("--------------Printing values-----------------");
         System.out.println("EMP 1"+employeeBean[1].getProductStatusMap());
         for(int i=0;i<employeeBean.length;i++){
         emp=employeeBean[i];
          String trClass = "";
         boolean doFlag = false;  // flag to determine of a row should be shown
          if (!doFlag) { 	
			
				if ( "VP".equals( emp.getRole() ) ) {
					trClass = "class='active_row avp_row'";
				}
				if ( "RM".equals( emp.getRole() ) || "ARM".equals( emp.getRole() ) ) {
					trClass = "class='active_row rm_row'";
				}
				if ( "DM".equals( emp.getRole() ) ) {
					trClass = "class='active_row dm_row'";
				}
         }
         
         %>
         <tr  >
                <td><%=emp.getTeamCode()%></td>
				<td><%=(emp.getDistrictDesc()==null)?"":emp.getDistrictDesc()%></td>
                <td><%=emp.getLastName()%></td>
				<td><%=emp.getFirstName()%></td>
				<td><%=emp.getRole()%></td>
				<td>&nbsp;<%=emp.getEmplId()%></td>
                
                 <%if("Overall".equalsIgnoreCase(xlsType)){
                    empHashMap=emp.getProductStatusMap();
                    
                    %>
                    <td ><%=Util.toEmptyNA((String)empHashMap.get("VISTASPRV1"))%></td>
                    <td ><%=Util.toEmptyNA((String)empHashMap.get("VISTASPRV2"))%></td>                   
                    <td ><%=Util.toEmptyNA((String)empHashMap.get("VISSPRVATT"))%></td>                
                    <td ><%=section%></td>
                
                <%}else{
                    %>
                    <td ><%=emp.getCourseStatus()%></td>
                    <%
                }%>
                
                
              
			
              
                
                </tr>
                <%
         }
                %>
        
</table>
    </body>
<!-- </netui:html> -->
</html>