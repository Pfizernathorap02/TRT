<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.tgix.Utils.Util"%>
<%@ page import="com.pfizer.webapp.wc.components.report.TsrReportWc"%>
<%@ taglib uri="/WEB-INF/dynamic-include.tld" prefix="inc" %>
<%@ page import="java.util.List"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.Hashtable"%>
<%@ page import="java.util.Enumeration"%>
<%@ page import="java.util.Vector"%>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.HashMap" %>

<%
	TsrReportWc wc = (TsrReportWc)request.getAttribute(TsrReportWc.ATTRIBUTE_NAME);
%>
<script type="text/javascript" language="JavaScript">
addEvent(window, "load", sortables_init);


function sortables_init() {
    // Find all tables with class sortable and make them sortable
    if (!document.getElementsByTagName) return;
    tbls = document.getElementsByTagName("table");
    for (ti=0;ti<tbls.length;ti++) {
        thisTbl = tbls[ti];
        if (((' '+thisTbl.id+' ').indexOf("tsr_table") != -1) && (thisTbl.id)) {
            //initTable(thisTbl.id);
            ts_makeSortable(thisTbl);
        }
    }
}
</script>

<br>

<table class = "basic_table">
	<tr>
		<td rowspan="3" width="15"></td>
	</tr>
	<tr>
		<td><h2><%=wc.getHeader()%></h2></td>
	</tr>
	<tr>
		<td>

			<table id="tsr_table" class="blue_table" >
					<tr>
						<th>Employee Id</th>	
						<th>First Name</th>	
						<th>Last Name</th>	
						<th>Cluster</th>	
						<th>Team</th>	
						<th>Product</th>	
						<th>Date Exempted</th>	
					</tr>
			
			<%	boolean oddEvenFlag = false;
				for (Iterator it = wc.getResults().iterator(); it.hasNext(); ) {
					HashMap map = (HashMap)it.next();
					oddEvenFlag = !oddEvenFlag; 
					String emplid = Util.toEmpty((String)map.get("emplid".toUpperCase()));
					String productCd = Util.toEmpty((String)map.get("PRODUCT_CD".toUpperCase()));
			%>
					<tr class="<%=oddEvenFlag?"even":"odd"%>">
						<td><a href="/TrainingReports/overview/detailreport?emplid=<%=emplid%>&needTraining=Exempted&productCode=<%=productCd%>"><%=emplid%></a></td>	
						<td><%=Util.toEmpty((String)map.get("FIRST_NAME".toUpperCase()))%></td>	
						<td><%=Util.toEmpty((String)map.get("LAST_NAME".toUpperCase()))%></td>	
						<td><%=Util.toEmpty((String)map.get("cluster_cd".toUpperCase()))%></td>	
						<td><%=Util.toEmpty((String)map.get("TEAM_CD".toUpperCase()))%></td>	
						<td><%=Util.toEmpty((String)map.get("product_desc".toUpperCase()))%></td>	
						<td><%=Util.toEmpty((String)map.get("date_exempted".toUpperCase()))%></td>	
					</tr>
			<%	} %>
			</table>

		</td>
	</tr>
</table>