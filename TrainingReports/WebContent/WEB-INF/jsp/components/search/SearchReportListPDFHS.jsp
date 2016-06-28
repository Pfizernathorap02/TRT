<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.pfizer.db.EmpSearch"%>
<%@ page import="com.pfizer.db.EmpSearchPDFHS"%>
<%@ page import="com.pfizer.processor.OverallResult"%>
<%@ page import="com.pfizer.webapp.AppConst"%>
<%@ page import="com.pfizer.webapp.wc.components.search.SearchResultListPDFHSWc"%>
<%@ page import="com.tgix.Utils.Util"%>
<%@ page import="java.awt.GraphicsEnvironment"%>
<%@ taglib uri="/WEB-INF/dynamic-include.tld" prefix="inc" %>
<%@ page import="java.util.HashMap"  %>
<%@ page import="java.util.List"  %>
<%@ page import="java.util.Iterator"  %>

<%
	SearchResultListPDFHSWc wc = (SearchResultListPDFHSWc)request.getAttribute(SearchResultListPDFHSWc.ATTRIBUTE_NAME);
    String mode1 = request.getParameter("m1");  
    String mode2 = request.getParameter("m2");  
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
                <th>Product</th>
                <%if (AppConst.EVENT_PDF.equalsIgnoreCase(mode1)) {%>
                <th>HS Status</th>
                <th>HS Date Completed</th>
                <th>Overall HS Status</th>
                <%}%>
                <th>PLC Status</th>                                                
                <th>PLC Date Completed</th>
                <th>Overall PLC Status</th>                
        </tr>
        <%
    }
    String colorClass="even";
for(Iterator iter=ret.iterator();iter.hasNext();){
    	EmpSearchPDFHS emp = (EmpSearchPDFHS)iter.next();
     
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
                <%-- Infosys code changes starts here
                <a href="<%=AppConst.APP_ROOT%>/PWRA/employeeDetail/getEmployeeDetail.do?emplid=<%=emp.getEmplId()%>&m0=search&m1=<%=mode1%>&m2=<%=mode2%>">                                            
                --%>
                <a href="<%=AppConst.APP_ROOT%>/PWRA/employeeDetail/getEmployeeDetail?emplid=<%=emp.getEmplId()%>&m0=search&m1=<%=mode1%>&m2=<%=mode2%>">                
                                          
               <%-- Infosys code changes ends here --%>
               
               
                
                
                <%=Util.toEmpty(emp.getProductCd())%>
                </a>
                </td>
                <%if (AppConst.EVENT_PDF.equalsIgnoreCase(mode1)) {%>
                <td><%=Util.toEmpty(emp.getExamStatus())%>&nbsp;</td>
                <td><%=Util.toEmpty(emp.getCompletedDate())%>&nbsp;</td>
                <td><%=Util.toEmpty(emp.getOverallExamStatus())%>&nbsp;</td>
                <%}%>
                <td>
                <%--<%=Util.toEmpty(emp.getPLCStatus())%>&nbsp;--%>
                <%
                    String plcStatus = Util.toEmpty(emp.getPLCStatus());
                    if (status.equalsIgnoreCase("On-Leave") && "Not Completed".equalsIgnoreCase(emp.getPLCStatus())) {
                        plcStatus = "On Leave";
                    }                        
                %>                
                <%=plcStatus%>&nbsp;
                </td>                                
                <td><%=Util.toEmpty(emp.getcompletedDatePLC())%>&nbsp;</td>
                <td><%=Util.toEmpty(emp.getoverallExamStatusPLC())%>&nbsp;</td>                                
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
               <%--  <a href="<%=AppConst.APP_ROOT%>/PWRA/employeeDetail/getEmployeeDetail.do?emplid=<%=emp.getEmplId()%>&m0=search&m1=<%=mode1%>&m2=<%=mode2%>">                
                --%>
                 <a href="<%=AppConst.APP_ROOT%>/PWRA/employeeDetail/getEmployeeDetail?emplid=<%=emp.getEmplId()%>&m0=search&m1=<%=mode1%>&m2=<%=mode2%>">                
               
                <%=Util.toEmpty(emp.getProductCd())%>
                </a>
                </td>
                 <%if (AppConst.EVENT_PDF.equalsIgnoreCase(mode1)) {%>
                <td><%=Util.toEmpty(emp.getExamStatus())%>&nbsp;</td>
                <td><%=Util.toEmpty(emp.getCompletedDate())%>&nbsp;</td>
                <td>&nbsp;</td>
                <%}%>
                <td>
                <%--<%=Util.toEmpty(emp.getPLCStatus())%>&nbsp;--%>
                <%
                    String plcStatus = Util.toEmpty(emp.getPLCStatus());
                    if (status.equalsIgnoreCase("On-Leave") && "Not Completed".equalsIgnoreCase(emp.getPLCStatus())) {
                        plcStatus = "On Leave";
                    }                        
                %>
                <%=plcStatus%>&nbsp; 
                </td>     
                <td><%=Util.toEmpty(emp.getcompletedDatePLC())%>&nbsp;</td>
                <td>&nbsp;</td>
                </tr>
                    <%
                }
                count++;
                
            
}//end of FOR LOOP			
%>


</table>