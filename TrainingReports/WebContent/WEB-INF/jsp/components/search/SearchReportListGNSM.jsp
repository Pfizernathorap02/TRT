<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.pfizer.db.EmpSearch"%>
<%@ page import="com.pfizer.db.EmpSearchGNSM"%>
<%@ page import="com.pfizer.db.EmpSearchPDFHS"%>
<%@ page import="com.pfizer.db.Employee"%>
<%@ page import="com.pfizer.processor.OverallResult"%>
<%@ page import="com.pfizer.webapp.AppConst"%>
<%@ page import="com.pfizer.webapp.wc.components.search.SearchResultListGNSMWc"%>
<%@ page import="com.pfizer.webapp.wc.components.search.SearchResultListPDFHSWc"%>
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

<%
	SearchResultListGNSMWc wc = (SearchResultListGNSMWc)request.getAttribute(SearchResultListGNSMWc.ATTRIBUTE_NAME);
	List ret = wc.getResults();
%>
<br>
<br>
<style>
p{border-style: hidden; }
</style>
<table class="blue_table">
<%	    
    String requestFromSearch=request.getParameter("fromSearch")==null?"":request.getParameter("fromSearch");
    if(requestFromSearch.equalsIgnoreCase("true")){
        %>
        <tr>
                <th>Team</th>
                <th>District</th>
                <th>Last Name</th>
                <th>First Name</th>                
                <th>Role</th>
                <th>Emplid</th>
                <th>Activity</th>
                <th>Status</th>
                <th>Overall Status</th>
        </tr>
        <%
    }
    String colorClass="even";
    String tempId = "";    
    for(Iterator iter=ret.iterator();iter.hasNext();){        
    	EmpSearchGNSM emp = (EmpSearchGNSM)iter.next();
        
      
        if(colorClass.equalsIgnoreCase("even"))colorClass="odd";
            else
        if(colorClass.equalsIgnoreCase("odd"))colorClass="even";
        %>
        <tr class="<%=colorClass%>">
    <%            
        if(tempId.equalsIgnoreCase(emp.getEmplId())){%>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
    <%}else{%>        
            <td><%=Util.toEmpty(emp.getTeamCode())%></td>
            <td><%=Util.toEmpty(emp.getDistrictDesc())%></td>                
            <td><%=Util.toEmpty(emp.getLastName())%></td>
            <td><%=Util.toEmpty(emp.getFirstName())%></td>
            <td><%=Util.toEmpty(emp.getRole())%></td>
            <td><%=Util.toEmpty(emp.getEmplId())%></td>    
    <%}%>                            
            <td>
                <%-- Infosys code changes starts here
                <a href="<%=AppConst.APP_ROOT%>/PWRA/employeeDetail/getEmployeeDetail.do?emplid=<%=emp.getEmplId()%>&m1=GNSM&m0=search">                            
                --%>
                <a href="<%=AppConst.APP_ROOT%>/PWRA/employeeDetail/getEmployeeDetail?emplid=<%=emp.getEmplId()%>&m1=GNSM&m0=search">                            
               <%-- Infosys code changes ends here --%>
                <%=Util.toEmpty(emp.getActivityName())%>        
                </a>            
            </td>
            <td><%=Util.toEmpty(emp.getActivityStatus())%></td>
            <td><%=Util.toEmpty(emp.getOverallStatus())%></td>                
        </tr>
<%      
        tempId = emp.getEmplId();      
    }//end of FOR LOOP			
%>


</table>