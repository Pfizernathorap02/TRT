<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.pfizer.db.EmpReport"%>
<%@ page import="com.pfizer.db.Employee"%>
<%@ page import="com.pfizer.webapp.AppConst"%>
<%@ page import="com.pfizer.webapp.AppQueryStrings"%>
<%@ page import="com.pfizer.webapp.report.ClassFilterForm"%>
<%@ page import="com.pfizer.webapp.user.User"%>
<%@ page import="com.pfizer.webapp.wc.components.report.EnrollmentSummaryReportWc"%>
<%@ page import="com.pfizer.webapp.wc.components.report.TrainingScheduleWc"%>
<%@ page import="com.pfizer.webapp.wc.page.ListReportWpc"%>
<%@ page import="com.tgix.Utils.Util"%>
<%@ page import="java.util.HashMap"%>
<%@ page import="com.tgix.html.FormUtil"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.Hashtable"%>
<%@ page import="java.util.Enumeration"%>
<%@ page import="java.util.Vector"%>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.HashMap" %>

<%--  <%@ taglib uri="netui-tags-databinding.tld" prefix="netui-data"%>
<%@ taglib uri="netui-tags-html.tld" prefix="netui"%>
<%@ taglib uri="netui-tags-template.tld" prefix="netui-template"%> --%> 

<%! private HashMap getProductSize(EmpReport[] empReport){
        HashMap map = new HashMap();
        String code = null;
        for(int i=0;i<empReport.length;i++){
            code = empReport[i].getProductCode();
            if(map.get(code)==null){
                map.put(code,new Integer(1));        
            }else{
                map.put(code,new Integer(((Integer)map.get(code)).intValue()+1));
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
        String appURL = null;        
        String backMenu = null;
        String backLink = null;
        String excelURL = null;
        EnrollmentSummaryReportWc wc = (EnrollmentSummaryReportWc)request.getAttribute(ListReportWpc.ATTRIBUTE_NAME);                 
        EmpReport[] empReport = wc.getEmpReport();        
        EmpReport empReportTotal = wc.getEmpReportTotal();        
        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
        if(wc.getEvent().equalsIgnoreCase(AppConst.EVENT_PDF)){
            title = "PDF - Enrollment Summary Report";
            appURL = "/PWRA/PDFHSReportTrainingScheduleEmplList";
            backMenu ="PDF Reports Home";
            backLink ="pdfhsreportselect";  
            excelURL = "/PWRA/PDFEnrollmentSummaryReport";          
        }else{
            title = "SPF - Enrollment Summary Report";
            appURL = "/SPF/SPFHSReportTrainingScheduleEmplList";
            backMenu ="SPF Reports Home";
            backLink ="spfreportselect";             
            excelURL = "/SPF/SPFEnrollmentSummaryReport";          
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
            <th nowrap>PRODUCT</th>
        	<th nowrap>Enrollment Date</th>
			<th nowrap>Enrollment Count</th>		
			<th nowrap>TRM Shipment Count</th>		
			<th nowrap>Email Invitation Count</th>
            <th nowrap>P2L registration Count</th>
        </tr>
        
        <%
        
            HashMap mapRowSpanSize = getProductSize(empReport);
            String tempFirstProduct = null;
            String code = null;
            String url = null;
            int rowTotalDisplay = -1;
            int count = 0;
            int trmShipmentCount = 0;
            int emailInvitationCount = 0;
            int p2LregistrationCount = 0;
            int countTotal = 0;
            int trmShipmentCountTotal = 0;
            int emailInvitationCountTotal = 0;
            int p2LregistrationCountTotal = 0;            
            String productDesc=null;
            //boolean totalDisplay = false;
            int rowSpanSize = 0; 
            for(int i=0;i<empReport.length;i++){                                
                code = empReport[i].getProductCode();                                
                url ="<a href=\""+AppConst.APP_ROOT+appURL+"?ClassFilterForm_product="+code+"&ClassFilterForm_EnrollmentDate="+format.format(empReport[i].getStartDate())+"\">";                                                           
                count =empReport[i].getCount().intValue();
                trmShipmentCount = empReport[i].getTRMShipmentCount().intValue();                
                emailInvitationCount = empReport[i].getEmailInvitationCount().intValue();
                p2LregistrationCount = empReport[i].getP2LregistrationCount().intValue();
                countTotal = countTotal+count;
                trmShipmentCountTotal = trmShipmentCountTotal+trmShipmentCount;
                emailInvitationCountTotal = emailInvitationCountTotal+emailInvitationCount;
                p2LregistrationCountTotal = p2LregistrationCountTotal+p2LregistrationCount;                            
                productDesc=empReport[i].getProductDesc();
                if(!code.equals(tempFirstProduct)){
                    rowSpanSize = ((Integer)mapRowSpanSize.get(code)).intValue();    
                }                 
        %> 
            <tr>
            
            <%if(rowSpanSize!=0){
                rowTotalDisplay = rowTotalDisplay+rowSpanSize;                                 
            %>                
            <td valign="top" rowspan="<%=rowSpanSize%>"><%=productDesc%></td>
            <%}%>
            <td><%=format.format(empReport[i].getStartDate())%></td>
            <td align= "right">
            <%=(!downloadExcel)?url:""%>            
                <%=count%>
            <%=(!downloadExcel)?"</a>":""%>                                                                        
            </td>            
            <td align= "right">
            <%=(!downloadExcel)?url:""%>                        
                <%=trmShipmentCount%>
            <%=(!downloadExcel)?"</a>":""%>                                                                        
            </td>
            <td align= "right">
            <%=(!downloadExcel)?url:""%>
                <%=emailInvitationCount%>
            <%=(!downloadExcel)?"</a>":""%>                                                        
            </td>            
            <td align= "right">
            <%=(!downloadExcel)?url:""%>            
                <%=p2LregistrationCount%>
            <%=(!downloadExcel)?"</a>":""%>                                                                        
            </td>
            </tr>
            <%if(rowTotalDisplay==i){%>
                <tr bgcolor="#ffd699">
                <td colspan="1">&nbsp;</td>
                <td><b><%=productDesc.toUpperCase()%> TOTAL</b></td>
                <td align="right"><b>                                                    
                <%=countTotal%></b></td>
                <td align="right"><b>                            
                <%=trmShipmentCountTotal%></b></td>
                <td align="right"><b>            
                <%=emailInvitationCountTotal%></b></td>            
                <td align="right"><b>            
                <%=p2LregistrationCountTotal%></b></td>            
            </tr>
            <%
                countTotal = 0;
                trmShipmentCountTotal = 0;
                emailInvitationCountTotal = 0;
                p2LregistrationCountTotal = 0;                                    
            }%>
        <%
            rowSpanSize = 0; 
            tempFirstProduct = code;
            }
        %>

        <tr>
            <td colspan="6">&nbsp;</td>
        </tr>
        <tr bgcolor="#d6ebad">
            <td>&nbsp;</td>
            <td><b>Total PDF Trainees</b></td>                        
            <td align="right"><b>                        
            <%=empReportTotal.getCount()%>
            </b></td>
            <td align="right"><b>            
            <%=empReportTotal.getTRMShipmentCount()%>
            </b></td>
            <td align="right"><b>            
            <%=empReportTotal.getEmailInvitationCount()%>
            </b></td>
            <td align="right"><b>            
            <%=empReportTotal.getP2LregistrationCount()%>
            </b></td>                        
        </tr>
        
        </table>
        </div>

 