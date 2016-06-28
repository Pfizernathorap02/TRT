<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.pfizer.db.SceFull"%>
<%@ page import="com.pfizer.webapp.AppConst"%>
<%@ page import="com.pfizer.webapp.user.User"%>
<%@ page import="com.pfizer.webapp.wc.components.report.FailureReportWc"%>
<%@ page import="com.pfizer.webapp.wc.page.ListReportWpc"%>
<%@ page import="java.text.DecimalFormat"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="com.tgix.Utils.Util"%>
<%-- <%@ taglib uri="netui-tags-databinding.tld" prefix="netui-data"%>
<%@ taglib uri="netui-tags-html.tld" prefix="netui"%>
<%@ taglib uri="netui-tags-template.tld" prefix="netui-template"%> --%>
<%@ taglib uri="/WEB-INF/dynamic-include.tld" prefix="inc" %>

<%
    FailureReportWc wc = (FailureReportWc)request.getAttribute(ListReportWpc.ATTRIBUTE_NAME);         
    boolean downloadExcel = request.getParameter("downloadExcel") != null && request.getParameter("downloadExcel").equals("true");      
    SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");    
    DecimalFormat scoreFormatter = new DecimalFormat("###0.##");
%>
<!-- <netui:html> -->
<html>
    <head>
        <title>
            PHR Phase 6 - TSR Phase 4 Failure Report
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
        
             
        <div style="margin-top:10px">
        <%if (!downloadExcel) {%>   
        <inc:include-wc component="<%=wc.getMassEmail()%>"/>
        <form name="emailSelectForm" id="emailSelectForm">
            <div style="width:95%">
            <div class="top_table_buttons" style="float:right; align:bottom;">	
                <img src="/TrainingReports/resources/images/excel.gif" width="15px">&nbsp;<a href="<%=AppConst.APP_ROOT%>/FailureReports/showFailureReport?downloadExcel=true">Download to Excel</a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                <img src="/TrainingReports/resources/images/training/b_checkall.gif"onclick="checkAll()" />
                <img src="/TrainingReports/resources/images/training/b_uncheckall.gif" onclick="unCheckAll()" />
                <img id="pos_bottom" src="/TrainingReports/resources/images/training/b_sendemail.gif" onclick="submitEmail()" />
            </div>
            <div class="clear"></div>	
        </div>
        <%}%>
        <table cellspacing="0" id="employee_table" width="90%" class="employee_table" style="margin-top:10px" align="center">
        <%if (downloadExcel) {%>
        <tr>
            <td colspan="9"><b>PHR Phase 6 - TSR Phase 4 Failure Report</b></td>
        </tr>
        <tr></tr>
        <%}%>
        <tr>
            <%--<th nowrap>SlNo.</th>--%>
            <th nowrap>Last Name</th>
        	<th nowrap>First Name</th>			
            <th nowrap>EMPLID</th>
            <th nowrap>Role</th>
            <th nowrap>BU</th>
            <th nowrap>Sales Group</th>
            <th nowrap>Activity Name</th>
            <th nowrap>Evaluation Date</th>
            <th nowrap>Score</th>
            <%if (!downloadExcel) {%>   
            <th>Email</th>         
            <%}%>
        </tr>
        <%
            String strEvalDate = "";
            SceFull[] arrFailureList = wc.getFailureList();
            SceFull oFailure;
            if (arrFailureList != null)
            for(int i=0; i<arrFailureList.length; i++) {
                oFailure = arrFailureList[i];
                try {
                    strEvalDate = format.format(oFailure.getEvalDate());
                }
                catch (Exception e) {
                    strEvalDate = "";
                }
        %>
        <tr>
            <%--<td><%=i+1%></td>--%>
            <td><%=Util.toEmptyNBSP(oFailure.getEvalLName())%></td>
            <td><%=Util.toEmptyNBSP(oFailure.getEvalFName())%></td>
            <td><%=downloadExcel? Util.toEmptyNBSP(Util.formatStrExcel(oFailure.getEmplid())) : Util.toEmptyNBSP(oFailure.getEmplid())%></td>
            <td><%=Util.toEmptyNBSP(oFailure.getRole())%></td>
            <td><%=Util.toEmptyNBSP(oFailure.getClusterCode())%></td>
            <td><%=Util.toEmptyNBSP(oFailure.getTeamCode())%></td>
            <td><%=Util.toEmptyNBSP(oFailure.getProductName())%></td>
            <td><%=Util.toEmptyNBSP(strEvalDate)%></td>
            <td><%=Util.ifNull(oFailure.getScore(), scoreFormatter, "")%></td>
            <%if (!downloadExcel) {%>   
            <td><input name="MailSelectForm_email_<%=oFailure.getEmplid()%>" value="<%=oFailure.getEmail()%>"  type="checkbox">
            <%}%>
        <%
            }         
        %>
        </table>
        </form>
        </div>
    </body>
<!-- </netui:html> -->
</html>