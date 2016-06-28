<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.pfizer.db.ManagementSummaryReport"%>
<%@ page import="com.pfizer.webapp.AppConst"%>
<%@ page import="com.pfizer.webapp.wc.components.ManagementSummaryReportWc"%>
<%@ page import="com.pfizer.webapp.wc.components.admin.EditReportWc"%>
<%@ page import="java.util.LinkedHashMap"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.Iterator"%>

<!-- Newly Added for TRT major enhancement (Management Summary Report)-->
<%
    ManagementSummaryReportWc wc = (ManagementSummaryReportWc )request.getAttribute(EditReportWc.ATTRIBUTE_NAME);
    ManagementSummaryReport track = wc.getTrack();  
    
    List courseList = new ArrayList();
    List groupByList =track.getGroupByList();
    
    List reportList = wc.getReportList();
    List reportDataList = wc.getReportDataList();   
   
   // Added for group by changes
    List resultList = wc.getReportList();
  //  System.out.println("resultList=="+resultList.toString());
	ArrayList columnNames = new ArrayList();
	LinkedHashMap record = new LinkedHashMap();
	int columns = 0;
	if (resultList != null && resultList.size() > 0)
		record = (LinkedHashMap) resultList.get(0);
	if (record != null) {
		columns = record.size();
		for (Iterator i = record.keySet().iterator(); i.hasNext();) {
			String key = (String) i.next();
			columnNames.add(key);
         //   System.out.println("key="+key);
		}
	}
    
%>

<%! boolean salesOrg, roleCd, gender, courseCode = false;%>
<table class="basic_table">
<tr>
    <td rowspan="2">&nbsp;&nbsp;</td>
    <td>&nbsp;</td>
    <td rowspan="2">&nbsp;&nbsp;</td>
</tr>
<tr>
    <td>
        <table class="no_space_width"  height="0%"> 
            <tr>
                <td>
                    <img src="<%=AppConst.IMAGE_DIR%>/spacer.gif" width="5">
                </td>
                <td colspan="=2">
                    <img src="<%=AppConst.IMAGE_DIR%>/spacer.gif" height="25">
                </td>
            </tr>
            <tr>
                <td>
                    <img src="<%=AppConst.IMAGE_DIR%>/spacer.gif" width="5">
                </td>
                <td>
        	<div class="breadcrumb">
        	<%
                request.setAttribute("track", wc.getTrack().getTrackLabel());
        	 System.out.println("Track id *****"+request.getAttribute("track"));%>
        	 <!-- Infosys - Weblogic to Jboss Migrations changes start here -->
                <!-- <a href="/TrainingReports/reportselect" style="margin-left:0px;">Home</a> / -->
                <a href="/TrainingReports/reportselect" style="margin-left:0px;">Home</a> /
             <!-- Infosys - Weblogic to Jboss Migrations changes end here --> 
                <%=wc.getTrack().getTrackLabel()%>
            </div>
        </td>
            </tr>
            
        </table>
        
        
        <table><tr><td><label class="basic_label">Report Label&nbsp;:&nbsp;&nbsp;</label></td><td><%=track.getTrackLabel()%></td></table>
        <table class="basic_table">
	<tr>
		<td colspan="<%=columns%>">&nbsp</td>
	</tr>
	<tr>
        <%if (resultList.size() > 0) { %>
		<td>
            
            <img src="<%=AppConst.APP_ROOT%>/resources/images/excel.gif" width="15px"/>&nbsp;
            <!-- Infosys - Weblogic to Jboss Migrations changes start here -->
            <%-- <a href="getCourses.do?downloadExcel=true&track=<%=wc.getTrack().getTrackId()%>">Download to Excel</a> --%>
            <a href="getCourses?downloadExcel=true&track=<%=wc.getTrack().getTrackId()%>">Download to Excel</a>
            <!-- Infosys - Weblogic to Jboss Migrations changes end here -->
            <br>
            <%=resultList.size()%> records found.
            </td>
        <%} else {%>
        <td>No Data Found.</td>
        <%}%>
		<td></td>
		<td colspan="<%=columns-3%>">&nbsp;</td>
	</tr>
	<tr>
		<td colspan="4">
		<table class="blue_table" align="center">
			<tr>
				<%
					for (int j = 0; j < columnNames.size(); j++) {
                        //System.out.println("columnNamesr="+columnNames.get(j));
				%>
				<th><%=columnNames.get(j)%></th>
				<%
					}
				%>
			</tr>
			<%
				boolean oddEvenFlag = false;
				for (int i = 0; i < resultList.size(); i++) {
					oddEvenFlag = !oddEvenFlag;
					record = (LinkedHashMap) resultList.get(i);
			%>
			<tr class="<%=oddEvenFlag?"even":"odd"%>">
				<%
					for (int j = 0; j < columns; j++) {
				%>
				<td><%=record.get(columnNames.get(j))%></td>
				<%
					}
				%>
			</tr>
			<%
				}
			%>
		</table>
		</td>
	</tr>
</table>


</tr>
</table>