<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.pfizer.db.EnrollChangeReport"%>
<%@ page import="com.pfizer.webapp.AppConst"%>
<%@ page import="com.pfizer.webapp.wc.components.report.EnrollmentChangeReportWc"%>
<%@ page import="com.pfizer.webapp.wc.page.ListReportWpc"%>
<%@ page import="com.tgix.Utils.Util"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="java.util.HashMap" %>
<%-- <%@ taglib uri="netui-tags-databinding.tld" prefix="netui-data"%>
<%@ taglib uri="netui-tags-html.tld" prefix="netui"%>
<%@ taglib uri="netui-tags-template.tld" prefix="netui-template"%> --%>

<%! private HashMap getEmplIDSize(EnrollChangeReport[] empReport){
        HashMap map = new HashMap();
        String emplid = null;
        for(int i=0;i<empReport.length;i++){
            emplid = empReport[i].getEmplId();
            if(map.get(emplid)==null){
                map.put(emplid,new Integer(1));        
            }else{
                map.put(emplid,new Integer(((Integer)map.get(emplid)).intValue()+1));
            }            
        }
        return map;
    }
    
%>
<%
    boolean downloadExcel = request.getParameter("downloadExcel") != null && request.getParameter("downloadExcel").equals("true");
%>
    <body>
        <div style="margin-left:10px;margin-right:10px">
        <%
        String title = null;
        String backMenu = null;
        String backLink = null;
        String excelURL = null;
        EnrollmentChangeReportWc wc = (EnrollmentChangeReportWc)request.getAttribute(ListReportWpc.ATTRIBUTE_NAME);                 
        EnrollChangeReport[] empReport = wc.getEmpReport();        
        
        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
        
        if(wc.getEvent().equalsIgnoreCase(AppConst.EVENT_PDF)){
            title = "PDF - Enrollment Change Report";
            backMenu ="PDF Reports Home";
            backLink ="pdfhsreportselect";  
            excelURL = "/PWRA/PDFEnrollmentChangeReport";          
        }else{
            title = "SPF - Enrollment Change Report";
            backMenu ="SPF Reports Home";
           /*  Infosys code changes starts here
           backLink ="spfreportselect.do";             
            excelURL = "/SPF/SPFEnrollmentChangeReport.do";  */   
            backLink ="spfreportselect";             
            excelURL = "/SPF/SPFEnrollmentChangeReport"; 
            /*  Infosys code changes ends here  */
        }                                
        %>
        
        <%if (!downloadExcel) {%>
        <h3 style="MARGIN-TOP:15PX">
        <%=title%>
        </h3>        
        <div style="margin-left:10px;margin-right:10px">
            <div id="table_inst">
                <img src="<%=AppConst.APP_ROOT%>/resources/images/excel.gif" width="15px">&nbsp;<a href="<%=AppConst.APP_ROOT%><%=excelURL%>?downloadExcel=true">Download to Excel</a>
                &nbsp;&nbsp;|&nbsp;&nbsp;
                <a href="<%=AppConst.APP_ROOT%>/<%=backLink%>"><%=backMenu%></a>
            </div>            
        </div>
                    
        <%}%>
        <table cellspacing="0" id="employee_table" width="90%" class="employee_table" style="margin-left:10px;margin-right:10px">
        <%if (downloadExcel) {%>
        <tr>
            <td colspan="6"><b><%=title%></b></td>
        </tr>
        <tr></tr>
        <%}%>
        <tr>
            <th nowrap>Employee ID</th>
        	<th nowrap>First Name</th>
			<th nowrap>Last Name</th>		
			<th nowrap>Email</th>		
			<th nowrap>Product</th>
            <th nowrap>Operation</th>
            <th nowrap>Comments</th>
            <th nowrap>Operation Date</th>
        </tr>
        
        <%        
            HashMap mapRowSpanSize = getEmplIDSize(empReport);
            String tempFirstEmplID = null;
            String emplid = null;
            String url = null;
            int rowTotalDisplay = -1;
            String sHighlight = "";
            
           
            
            //boolean totalDisplay = false;
            int rowSpanSize = 0; 
            for(int i=0;i<empReport.length;i++){                                
                //code = empReport[i].getProdctCd();
                emplid = empReport[i].getEmplId();
                if(!emplid.equals(tempFirstEmplID)){
                    rowSpanSize = ((Integer)mapRowSpanSize.get(emplid)).intValue();    
                }
                if(empReport[i] != null && empReport[i].getOperation().equalsIgnoreCase("DELETED"))
                {
                  sHighlight = "style='color:red'";
                }else{
                    sHighlight = "";
                }                      
        %> 
            <tr>            
                <%if(rowSpanSize!=0)
                  {
                    rowTotalDisplay = rowTotalDisplay+rowSpanSize;                                 
                %>                
                    <td rowspan="<%=rowSpanSize%>" valign="top"><%=Util.toEmptyNBSP(empReport[i].getEmplId())%></td>
                    <%if (!downloadExcel) {%>
                    <td rowspan="<%=rowSpanSize%>" valign="top">
                      <%-- Infosys code changes starts here
                      <a href="<%=AppConst.APP_ROOT%>/PWRA/employeeDetail/getEmployeeDetail.do?emplid=<%=empReport[i].getEmplId()%>&m0=report&m1=PDF">
                       --%> 
                       <a href="<%=AppConst.APP_ROOT%>/PWRA/employeeDetail/getEmployeeDetail?emplid=<%=empReport[i].getEmplId()%>&m0=report&m1=PDF">
                       <%-- Infosys code changes ends here --%>
                       
                        <%=Util.toEmptyNBSP(empReport[i].getFirstName())%>
                      </a>
                    </td>

                    <td rowspan="<%=rowSpanSize%>" valign="top">
                      <%-- Infosys code changes starts here
                      <a href="<%=AppConst.APP_ROOT%>/PWRA/employeeDetail/getEmployeeDetail.do?emplid=<%=empReport[i].getEmplId()%>&m0=report&m1=PDF">
                       --%> 
                       <a href="<%=AppConst.APP_ROOT%>/PWRA/employeeDetail/getEmployeeDetail?emplid=<%=empReport[i].getEmplId()%>&m0=report&m1=PDF">
                      <%-- Infosys code changes ends here --%>
                       
                        <%=Util.toEmptyNBSP(empReport[i].getLastName())%>
                      </a>
                    </td>
                    <%}else{%>
                      <td  rowspan="<%=rowSpanSize%>" valign="top"><%=Util.toEmptyNBSP(empReport[i].getFirstName())%></td>
                      <td  rowspan="<%=rowSpanSize%>" valign="top"><%=Util.toEmptyNBSP(empReport[i].getLastName())%></td>
                    <%}%>
                    
                    
                    <td rowspan="<%=rowSpanSize%>" valign="top"><%=Util.toEmptyNBSP(empReport[i].getEmailAddress())%></td> 
                <%}%>
                <td <%=sHighlight%>><%=Util.toEmptyNBSP(empReport[i].getProdctCd())%></td>                                    
                <td <%=sHighlight%>><%=Util.toEmptyNBSP(empReport[i].getOperation())%></td>
                <td <%=sHighlight%>><%=Util.toEmptyNBSP(empReport[i].getReason())%></td>
                <td <%=sHighlight%>><%=Util.toEmptyNBSP(format.format(empReport[i].getOperationDate()))%></td>
                
            </tr>
        <%
            rowSpanSize = 0; 
            tempFirstEmplID = emplid;
            }
        %>

        </table>
        </div>

 