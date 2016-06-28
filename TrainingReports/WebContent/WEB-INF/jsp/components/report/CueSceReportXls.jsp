<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.pfizer.webapp.AppConst"%>
<%@ page import="com.pfizer.webapp.wc.components.report.CueSceReportWc"%>
<%@ page import="com.pfizer.db.P2lEmployeeStatus"%>
<%@ page import="com.tgix.Utils.Util"%>
<%@ page import="java.sql.Timestamp"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.Hashtable"%>
<%@ page import="java.util.Enumeration"%>
<%@ page import="java.util.Vector"%>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.HashMap" %>

<%
	CueSceReportWc wc = (CueSceReportWc)request.getAttribute(CueSceReportWc.ATTRIBUTE_NAME);
%>
<script src="/TrainingReports/resources/js/sorttable.js"></script>


<script type="text/javascript" language="JavaScript">
addEvent(window, "load", sortables_init);


function sortables_init() {
    // Find all tables with class sortable and make them sortable
    if (!document.getElementsByTagName) return;
    tbls = document.getElementsByTagName("table");
    for (ti=0;ti<tbls.length;ti++) {
        thisTbl = tbls[ti];
        if (((' '+thisTbl.id+' ').indexOf("employee_table") != -1) && (thisTbl.id)) {
            //initTable(thisTbl.id);
            ts_makeSortable(thisTbl);
        }
    }
}
</script>
<script type="text/javascript" language="JavaScript">
 
</script>

<% if (!wc.getExcelDownload()) {%>

<div style="margin-left:10px;margin-right:10px">

        <h3 style="MARGIN-TOP:15PX">
            CUE Training - SCE Report
        </h3>  
        
<form action="p4scereport" method="post">
<table>
<tr>
<td>
</td>
<td>
</td>
</tr>
</table>
</form>

        
<%}%>
<table cellspacing="0" id="tsr_table" width="100%" class="blue_table">
    <tr>
        <th>Manager Last Name</th>
        <th>Manager First Name</th>
        <th>Student Last Name</th>
        <th>Student First Name</th>
        <th>Employee Id</th>
        <th>Role</th>
        <th>Business Unit</th>
        <th>Sales Org</th>
        <th>Training Status</th>
        <th>Date Taken</th>
        <th>Test Score</th>
    </tr>
     <%  
       boolean oddEvenFlag=false;
       for (Iterator it = wc.getReportList().iterator(); it.hasNext();) { 
            oddEvenFlag = !oddEvenFlag; 
            P2lEmployeeStatus curr = (P2lEmployeeStatus)it.next();  
          
     %>
         
    
             <tr class="<%=oddEvenFlag?"even":"odd"%>">     
                <td><%=Util.toEmpty(curr.getEmployee().getManagerLname())%></td>
                <td><%=Util.toEmpty(curr.getEmployee().getManagerFname())%> </td> 
                <td><%=Util.toEmpty(curr.getEmployee().getLastName())%></td>
                <td><%=Util.toEmpty(curr.getEmployee().getFirstName())%> </td>
                <td><%=Util.toEmpty(curr.getEmployee().getEmplId())%> </td>
                <td><%=Util.toEmpty(curr.getEmployee().getRole())%></td>
                <td><%=Util.toEmpty(curr.getEmployee().getBusinessUnit())%></td>
                <td><%=Util.toEmpty(curr.getEmployee().getSalesOrgDesc() )%></td>
                <td bgcolor="#FFE87C"><%=Util.toEmpty(curr.getStatus() )%></td>
                <%if(curr.getCompleteDate()==null){%>
                <td><%=""%></td>
                <%}else{   
                    %>
                <td><%=Util.formatDateSqlObj(curr.getCompleteDate())%></td>
                <%}%>
                <td align="center"><%=Util.toEmpty(curr.getScore())%></td>
             
            </tr>
    <%
        }
    %>    
</table>

</div>