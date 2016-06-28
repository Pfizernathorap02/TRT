<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.pfizer.db.RBUEnrollmentException"%>
<%@ page import="com.pfizer.webapp.AppConst"%>
<%@ page import="com.pfizer.webapp.AppQueryStrings"%>
<%@ page import="com.pfizer.webapp.report.ClassFilterForm"%>
<%@ page import="com.pfizer.webapp.user.User"%>
<%@ page import="com.pfizer.webapp.wc.components.report.RBUEnrollmentExceptionWc"%>
<%@ page import="com.pfizer.webapp.wc.page.ListReportWpc"%>
<%@ page import="com.tgix.Utils.Util"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.HashMap"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.Map"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="com.tgix.html.FormUtil"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%-- <%@ taglib uri="netui-tags-databinding.tld" prefix="netui-data"%>
<%@ taglib uri="netui-tags-html.tld" prefix="netui"%>
<%@ taglib uri="netui-tags-template.tld" prefix="netui-template"%> --%>



<%
    boolean downloadExcel = request.getParameter("downloadExcel") != null && request.getParameter("downloadExcel").equals("true");  
%>
<!-- <netui:html> -->
<html>
    <head>
        <title>
            Product Training (PSCPT) - Enrollment Exceptions Report 
        </title>
    </head>
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

    <body>
        <div style="margin-left:10px;margin-right:10px">
        <%
        RBUEnrollmentExceptionWc wc = (RBUEnrollmentExceptionWc)request.getAttribute(RBUEnrollmentExceptionWc.ATTRIBUTE_NAME);         
        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
        List report = wc.getReport();    
        %>
        
        <%if (!downloadExcel) {%>
        <h3 style="MARGIN-TOP:15PX">
            Product Training (PSCPT) - Enrollment Exceptions Report
        </h3>        
        <div style="margin-left:10px;margin-right:10px">
            <div id="table_inst">
                <img src="<%=AppConst.APP_ROOT%>/resources/images/excel.gif" width="15px">&nbsp;<a href="<%=AppConst.APP_ROOT%>/RBU/RBUEnrollmentExceptionReport?downloadExcel=true">Download to Excel</a>
                &nbsp;&nbsp;|&nbsp;&nbsp;
                <%-- Infosys code changes starts here
                <a href="<%=AppConst.APP_ROOT%>/rbusreportselect.do">PSCPT Admin Reports</a> --%>
                <a href="<%=AppConst.APP_ROOT%>/rbusreportselect">PSCPT Admin Reports</a>
            </div>            
        </div>
                    
        <%}%>
        <table cellspacing="0" id="employee_table" width="90%" class="employee_table" style="margin-left:10px;margin-right:10px">
        <%if (downloadExcel) {%>
        <tr>
            <td colspan="5"><b>Product Training (PSCPT) - Enrollment Exceptions Report</b></td>
        </tr>
        <tr></tr>
        <%}%>
        <%if(downloadExcel){%>
        <tr bgcolor="#1f61a9">
        <%}else{%>
        <tr>
        <%}%>
            <th nowrap>EMPLID</th>
            <th nowrap>First Name</th>
            <th nowrap>Last Name</th>
            <th nowrap>Exception Date</th>
        	<th nowrap>Exception Reason</th>
			<th nowrap>Required Products</th>

        </tr>

        <%
            RBUEnrollmentException exc;
            for(Iterator i= report.iterator(); i.hasNext();){
                exc = (RBUEnrollmentException) i.next();
         %>
         <tr>
            <td><%=downloadExcel?Util.toEmptyNBSP(Util.formatStrExcel(exc.getEmplId())) : Util.toEmptyNBSP(exc.getEmplId())%></td>
            <td><%=exc.getFirstname()%></td>
            <td><%=exc.getLastname()%></td>
            <td><%=format.format(exc.getExceptionDate())%></td>
            <td><%=exc.getReason()%></td>
            <td><%=exc.getProducts()%></td>
        </tr>
         <%
         }
         %>

        </table>
        </div>
    </body>
<!-- </netui:html> -->
</html>