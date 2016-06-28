<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.pfizer.db.EmpReport"%>
<%@ page import="com.pfizer.db.Employee"%>
<%@ page import="com.pfizer.webapp.AppConst"%>
<%@ page import="com.pfizer.webapp.AppQueryStrings"%>
<%@ page import="com.pfizer.webapp.report.ClassFilterForm"%>
<%@ page import="com.pfizer.webapp.user.User"%>
<%@ page import="com.pfizer.webapp.wc.components.report.TrainingScheduleEmplListWc"%>
<%@ page import="com.pfizer.webapp.wc.components.report.ReportListWc"%>
<%@ page import="com.pfizer.webapp.wc.page.ListReportWpc"%>
<%@ page import="com.tgix.Utils.Util"%>
<%@ page import="com.tgix.html.FormUtil"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%-- <%@ taglib uri="netui-tags-databinding.tld" prefix="netui-data"%>
<%@ taglib uri="netui-tags-html.tld" prefix="netui"%>
<%@ taglib uri="netui-tags-template.tld" prefix="netui-template"%> --%>

<%
    TrainingScheduleEmplListWc wc = (TrainingScheduleEmplListWc)request.getAttribute(ListReportWpc.ATTRIBUTE_NAME);         
    ClassFilterForm form = wc.getClassFilterForm();
    SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
    boolean downloadExcel = request.getParameter("downloadExcel") != null && request.getParameter("downloadExcel").equals("true");  
    boolean enrollmentPage = ((form.getEnrollmentDate()==null||form.getEnrollmentDate().equals("")))?false:true;
%>
<!-- <netui:html> -->
<html>
    <head>
        <title>
            SPF - Training Schedule Detail Report
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
        <%if (!downloadExcel) {%>
        <h3 style="MARGIN-TOP:15PX">
                SPF - Training Schedule Detail Report 
        </h3>
        <div style="margin-left:10px;margin-right:10px">
            <div id="table_inst">
            <%
                String excelURL = "&"+wc.getClassFilterForm().FIELD_PRODUCT+"="+form.getProduct();     
                excelURL = excelURL+"&"+wc.getClassFilterForm().FIELD_STARTDATE+"="+form.getStartDate();
                excelURL = excelURL+"&"+wc.getClassFilterForm().FIELD_ENDDATE+"="+form.getEndDate();
                excelURL = excelURL+"&"+wc.getClassFilterForm().FIELD_TEAMCD+"="+form.getTeamCd();
                if(enrollmentPage){                 
                    excelURL = excelURL+"&"+wc.getClassFilterForm().FIELD_ENROLLMENTDATE+"="+form.getEnrollmentDate();                
                }            
            %>            
                <img src="<%=AppConst.APP_ROOT%>/resources/images/excel.gif" width="15px">&nbsp;<a href="<%=AppConst.APP_ROOT%>/SPF/SPFHSReportTrainingScheduleEmplList?downloadExcel=true<%=excelURL%>">Download to Excel</a>
                &nbsp;&nbsp;|&nbsp;&nbsp;                
                <%if(!enrollmentPage){%>
                    <a href="<%=AppConst.APP_ROOT%>/SPF/SPFHSReportTrainingSchedule">Back To Training Schedule Summary</a>
                <%}else{%>
                    <a href="<%=AppConst.APP_ROOT%>/SPF/SPFEnrollmentSummaryReport">Back To Enrollment Summary</a>
                <%}%>        

            </div>            
        </div>  
        <%}%>      
        
        <table cellspacing="0" id="employee_table" width="90%" class="employee_table" style="margin-left:10px;margin-right:10px">
        <%if (downloadExcel) {%>
        <tr>
            <td colspan="5"><b>SPF - Training Schedule Detail Report</b></td>
        </tr>
        <tr></tr>
        <%}%>
        <tr>
            <th nowrap>SlNo.</th>
            <th nowrap>Product</th>
        	<th nowrap>Start Date</th>
			<th nowrap>End Date</th>		
            <th nowrap>EMPLID</th>
            <th nowrap>First Name</th>
            <th nowrap>Last Name</th>
            <th nowrap>Post - SPF Cluster</th>
            <th nowrap>Post - SPF Team</th>
            <th nowrap>Post - SPF Role</th>
            <th nowrap>Address</th>
            <th nowrap>Area</th>
            <th nowrap>Region</th>
            <th nowrap>District</th>
            <th nowrap>Territory</th>
            <th nowrap>Reports To<br>(Empl Id)</th>
			<th nowrap>HR Status</th>		
			<th nowrap>Field Status</th>
            <%if(enrollmentPage){%>
                <th nowrap>TRM Shipment Date</th>
                <th nowrap>Email Invitation Sent Date</th>
                <th nowrap>P2L Emrollment Date</th>
            <%}%>            
        </tr>
        <%
            String strStartDate = "";
            String strEndDate = "";
            String TRMShipmentDate = "";
            String EmailInvitationSentDate = "";
            String P2LEmrollmentDate = "";            
            EmpReport[] arrEmpReport = wc.getEmpReport();
            EmpReport oEmpReport;
            if (arrEmpReport != null)
            for(int i=0; i<arrEmpReport.length; i++) {
                oEmpReport = arrEmpReport[i];
                try {
                    strStartDate = format.format(oEmpReport.getStartDate());
                }
                catch (Exception e) {
                    strStartDate = "";
                }                  
                try {
                    strEndDate = format.format(oEmpReport.getEndDate());
                }
                catch (Exception e) {
                    strEndDate = "";
                }                  
                try {
                    TRMShipmentDate = format.format(oEmpReport.getTRMShipmentDate());
                }
                catch (Exception e) {
                    TRMShipmentDate = "";
                }                  
                try {
                    EmailInvitationSentDate = format.format(oEmpReport.getEmailInvitationDate());
                }
                catch (Exception e) {
                    EmailInvitationSentDate = "";
                }                  
                try {
                    P2LEmrollmentDate = format.format(oEmpReport.getP2LregistrationDate());
                }
                catch (Exception e) {
                    P2LEmrollmentDate = "";
                }                                                  
                
        %>
        <tr>
            <td><%=i+1%></td>
            <td><%=Util.toEmptyNBSP(oEmpReport.getProductDesc())%></td>
            <td><%=Util.toEmptyNBSP(strStartDate)%></td>
            <td><%=Util.toEmptyNBSP(strEndDate)%></td>
            <td><%=downloadExcel? Util.toEmptyNBSP(Util.formatStrExcel(oEmpReport.getEmplId())) : Util.toEmptyNBSP(oEmpReport.getEmplId())%></td>
            <%if (!downloadExcel) {%>
            <%-- Infosys code changes starts here
            <td><a href="<%=AppConst.APP_ROOT%>/PWRA/employeeDetail/getEmployeeDetail.do?emplid=<%=oEmpReport.getEmplId()%>&m0=report&m1=SPF"><%=Util.toEmptyNBSP(oEmpReport.getFirstName())%></a></td>
            <td><a href="<%=AppConst.APP_ROOT%>/PWRA/employeeDetail/getEmployeeDetail.do?emplid=<%=oEmpReport.getEmplId()%>&m0=report&m1=SPF"><%=Util.toEmptyNBSP(oEmpReport.getLastName())%></a></td>
            --%>
            <td><a href="<%=AppConst.APP_ROOT%>/PWRA/employeeDetail/getEmployeeDetail?emplid=<%=oEmpReport.getEmplId()%>&m0=report&m1=SPF"><%=Util.toEmptyNBSP(oEmpReport.getFirstName())%></a></td>
            <td><a href="<%=AppConst.APP_ROOT%>/PWRA/employeeDetail/getEmployeeDetail?emplid=<%=oEmpReport.getEmplId()%>&m0=report&m1=SPF"><%=Util.toEmptyNBSP(oEmpReport.getLastName())%></a></td>
            <%-- Infosys code changes ends here --%>   
            <%} else {%>
            <td><%=Util.toEmptyNBSP(oEmpReport.getFirstName())%></td>
            <td><%=Util.toEmptyNBSP(oEmpReport.getLastName())%></td>
            <%}%>
            <td><%=Util.toEmptyNBSP(oEmpReport.getClusterDesc())%></td>
            <td><%=Util.toEmptyNBSP(oEmpReport.getTeamDesc())%></td>
            <td><%=Util.toEmptyNBSP(oEmpReport.getRole())%></td>
            <td><%=Util.toEmptyNBSP(oEmpReport.getFullAddress())%></td>
            <td><%=Util.toEmptyNBSP(oEmpReport.getAreaDesc())%></td>
            <td><%=Util.toEmptyNBSP(oEmpReport.getRegionDesc())%></td>
            <td><%=Util.toEmptyNBSP(oEmpReport.getDistrictDesc())%></td>
            <td><%=Util.toEmptyNBSP(oEmpReport.getTerritoryId())%></td>
            <td><%=Util.toEmptyNBSP(oEmpReport.getReportsToEmplid())%></td>
            <td><%=Util.toEmptyNBSP(oEmpReport.getEmplStatus())%></td>
            <td><%=Util.toEmptyNBSP(oEmpReport.getFieldActive())%></td> 
            <%if(enrollmentPage){%>
                <td><%=Util.toEmptyNBSP(TRMShipmentDate)%></td>            
                <td><%=Util.toEmptyNBSP(EmailInvitationSentDate)%></td>
                <td><%=Util.toEmptyNBSP(P2LEmrollmentDate)%></td>
            <%}%>                       
        <%
            }         
        %>
        </table>
        </div>
    </body>
<!-- </netui:html> -->
</html>