<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.pfizer.db.EmpSearchPOA"%>
<%@ page import="com.pfizer.db.Employee"%>
<%@ page import="com.pfizer.processor.OverallResult"%>
<%@ page import="com.pfizer.webapp.AppConst"%>
<%@ page import="com.pfizer.webapp.wc.components.search.SearchResultListPOAWc"%>
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
	SearchResultListPOAWc wc = (SearchResultListPOAWc)request.getAttribute(SearchResultListPOAWc.ATTRIBUTE_NAME);
	List ret = wc.getResults();
%>
<br>
<br>
<style>
p{border-style: hidden; }
</style>
<table class="blue_table">
<%
	int count = 1;
	String currId = " ";
    String status="";
    String requestFromSearch=request.getParameter("fromSearch")==null?"":request.getParameter("fromSearch");
    if(requestFromSearch.equalsIgnoreCase("true")){
        %>
        <tr>
                <th>Last Name</th>
                <th>First Name</th>
                <th>Emplid</th>
                <th>Role</th>
                <th>Employee Status</th>
                <th>Presentation</th>
                <th>Status</th>
                <th>Date Completed</th>
                 

                
        </tr>
        <%
    }
    String colorClass="even";
for(Iterator iter=ret.iterator();iter.hasNext();){
    	EmpSearchPOA emp = (EmpSearchPOA)iter.next();
     
        	if ( !currId.equals( emp.getEmplId() ) ) {
                currId = emp.getEmplId();
                count=1;
                  if(colorClass.equalsIgnoreCase("even"))colorClass="odd";
                    else
                    if(colorClass.equalsIgnoreCase("odd"))colorClass="even";
                %>
                </tr>
                <tr class="<%=colorClass%>">
                <%
            }
                if(count==1){
                %>
                
                <td><%=Util.toEmpty(emp.getLastName())%></td>
                <td><%=Util.toEmpty(emp.getFirstName())%></td>
                <td><%=Util.toEmpty(emp.getEmplId())%></td>
                <td><%=Util.toEmpty(emp.getRoleCd())%></td>
                <td>
                <%
                status=emp.getFieldActive();
                if(status.equalsIgnoreCase("On-Leave")){
                    %>
                    <font color="red"><b><%=Util.toEmpty(emp.getFieldActive())%></b></font>
                    <%
                }else{
                    %>
                    <%=Util.toEmpty(emp.getFieldActive())%>
                    <%
                }
                %>
                
                </td>
                <td>
                <a href="/TrainingReports/overview/detailreportPOA?emplid=<%=emp.getEmplId()%>&productCode=<%=emp.getProductCd()%>&search=true&needTraining=<%=emp.getTrainingNeed()%>">
                <%=Util.toEmpty("Mid-POA1")%>
                </a>
                </td>
                <td><%=Util.toEmpty(emp.getExamStatus())%>&nbsp;</td>
                <td><%=Util.toEmpty(emp.getCompletedDate())%>&nbsp;</td>

                </tr>
                <%
                }else{
                    %>
                    
                <tr class="<%=colorClass%>">
               <td >&nbsp;</td>
                
                <td>&nbsp;</td>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
                <td>
                <a href="/TrainingReports/overview/detailreportPOA?emplid=<%=emp.getEmplId()%>&productCode=<%=emp.getProductCd()%>&search=true&needTraining=<%=emp.getTrainingNeed()%>">
                <%=Util.toEmpty("Mid-POA1")%>
                </a>
                </td>
                <td><%=Util.toEmpty(emp.getExamStatus())%>&nbsp;</td>
                <td><%=Util.toEmpty(emp.getCompletedDate())%>&nbsp;</td>

                </tr>
                    <%
                }
                count++;
                
            
}//end of FOR LOOP			
%>


</table>