<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%-- <%@ taglib uri="netui-tags-databinding.tld" prefix="netui-data"%> --%>
<%-- <%@ taglib uri="netui-tags-html.tld" prefix="netui"%> --%>
<%-- <%@ taglib uri="netui-tags-template.tld" prefix="netui-template"%> --%>

<%@ page import="com.pfizer.db.ManagementSummaryReport"%>
<%@ page import="com.pfizer.webapp.AppConst"%>
<%@ page import="com.pfizer.webapp.wc.components.ManagementSummaryReportWc"%>
<%@ page import="com.tgix.Utils.Util"%>
<%@ page import="java.awt.GraphicsEnvironment"%>
<%@ page import="java.util.LinkedHashMap"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.Iterator"%>
<%@ taglib uri="/WEB-INF/dynamic-include.tld" prefix="inc" %>

<!-- Newly Added for TRT major enhancement (Management Summary Report)-->
<%
    ManagementSummaryReportWc wc = (ManagementSummaryReportWc )request.getAttribute(ManagementSummaryReportWc.ATTRIBUTE_NAME);
    ManagementSummaryReport track = wc.getTrack();  
    
//    List courseList = new ArrayList();
 //   List groupByList =track.getGroupByList();
    
//    List reportList = wc.getReportList();
//    List reportDataList = wc.getReportDataList();   
   
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
    <td>
        <table class="basic_table">
	<tr>
		<td colspan="<%=columns%>">&nbsp</td>
	</tr>
	<tr>
        <td></td>
		<td colspan="<%=columns-3%>">&nbsp;</td>
	</tr>
	<tr>
		<td colspan="4">
		<table class="blue_table" width="100%">
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