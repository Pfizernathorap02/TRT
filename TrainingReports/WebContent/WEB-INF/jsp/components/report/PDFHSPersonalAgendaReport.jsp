<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.pfizer.db.EmpReport"%>
<%@ page import="com.pfizer.db.Employee"%>
<%@ page import="com.pfizer.webapp.AppConst"%>
<%@ page import="com.pfizer.webapp.AppQueryStrings"%>
<%@ page import="com.pfizer.webapp.user.User"%>
<%@ page import="com.pfizer.webapp.wc.components.report.PersonalAgendaWc"%>
<%@ page import="com.pfizer.webapp.wc.components.report.ReportListWc"%>
<%@ page import="com.pfizer.webapp.wc.page.ListReportWpc"%>
<%@ page import="com.tgix.Utils.Util"%>
<%@ page import="com.tgix.html.FormUtil"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%-- <%@ taglib uri="netui-tags-databinding.tld" prefix="netui-data"%>
<%@ taglib uri="netui-tags-html.tld" prefix="netui"%>
<%@ taglib uri="netui-tags-template.tld" prefix="netui-template"%> --%>

<%
    boolean downloadExcel = request.getParameter("downloadExcel") != null && request.getParameter("downloadExcel").equals("true");  
%>
<html>
    <head>
        <title>
            PDF - Personalized Agenda Report
        </title>
    </head>

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
   
    
    <body>
        <div style="margin-left:10px;margin-right:10px">
        <%
        PersonalAgendaWc wc = (PersonalAgendaWc)request.getAttribute(ListReportWpc.ATTRIBUTE_NAME);         
        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
        %>
        
        <%if (!downloadExcel) {%>
        <h3 style="MARGIN-TOP:15PX">
            PDF - Personalized Agenda Report
        </h3>
        <div style="margin-left:10px;margin-right:10px">
            <div id="table_inst">
                <img src="<%=AppConst.APP_ROOT%>/resources/images/excel.gif" width="15px">&nbsp;<a href="<%=AppConst.APP_ROOT%>/PWRA/PDFHSReportPersonalAgenda?downloadExcel=true">Download to Excel</a>
                &nbsp;&nbsp;|&nbsp;&nbsp;
                <a href="<%=AppConst.APP_ROOT%>/pdfhsreportselect">PDF Reports Home</a>
            </div>            
        </div>  
        <%}%>      
        
        <table cellspacing="0" id="employee_table" width="90%" class="employee_table" style="margin-left:10px;margin-right:10px">
        <%if (downloadExcel) {%>
        <tr>
            <td colspan="5"><b>PDF - Personalized Agenda Report</b></td>
        </tr>
        <tr></tr>
        <%}%>
        <tr>
            <th nowrap>EMPLID</th>
            <th nowrap>First Name</th>
            <th nowrap>Last Name</th>
            <th nowrap>Preferred Name</th>
            <th nowrap>Training Date</th>
            <th nowrap>Time</th>
            <th nowrap>Product Training</th>
            <th nowrap>Cluster</th>
            <th nowrap>Team</th>
            <th nowrap>Role</th>            
        </tr>
        <%
            String strTrainingDate = "";
            EmpReport[] arrEmpReport = wc.getEmpReport();
            EmpReport oEmpReport;
            if (arrEmpReport != null)
            for(int i=0; i<arrEmpReport.length; i++) {
                oEmpReport = arrEmpReport[i];
                try {
                    strTrainingDate = format.format(oEmpReport.getTrainingDate());
                }
                catch (Exception e) {
                    strTrainingDate = "";
                }                                  
        %>
        <tr>
            <td><%=downloadExcel? Util.toEmptyNBSP(Util.formatStrExcel(oEmpReport.getEmplId())) : Util.toEmptyNBSP(oEmpReport.getEmplId())%></td>
            <%if (!downloadExcel) {%>
            <td><a href="<%=AppConst.APP_ROOT%>/PWRA/employeeDetail/getEmployeeDetail?emplid=<%=oEmpReport.getEmplId()%>&m0=report&m1=PDF"><%=Util.toEmptyNBSP(oEmpReport.getFirstName())%></a></td>
            <td><a href="<%=AppConst.APP_ROOT%>/PWRA/employeeDetail/getEmployeeDetail?emplid=<%=oEmpReport.getEmplId()%>&m0=report&m1=PDF"><%=Util.toEmptyNBSP(oEmpReport.getLastName())%></a></td>
            <%} else {%>
            <td><%=Util.toEmptyNBSP(oEmpReport.getFirstName())%></td>
            <td><%=Util.toEmptyNBSP(oEmpReport.getLastName())%></td>
            <%}%>
            <td><%=Util.toEmptyNBSP(oEmpReport.getPreferredName())%></td>
            <td><%=Util.toEmptyNBSP(strTrainingDate)%></td>
            <td>7:30 - 5:30</td>
            <td><%=Util.toEmptyNBSP(oEmpReport.getProductDesc())%></td>
            <td><%=Util.toEmptyNBSP(oEmpReport.getClusterDesc())%></td>
            <td><%=Util.toEmptyNBSP(oEmpReport.getTeamDesc())%></td>
            <td><%=Util.toEmptyNBSP(oEmpReport.getRole())%></td>            
        <%
            }         
        %>
        </table>
        </div>
    </body>
</html>