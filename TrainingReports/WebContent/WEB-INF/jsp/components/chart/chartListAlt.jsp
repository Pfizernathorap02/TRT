<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.pfizer.webapp.wc.components.chart.ChartListWc"%>
<%@ page import="com.pfizer.webapp.wc.components.chart.ChartDetailWc"%>
<%@ taglib uri="/WEB-INF/dynamic-include.tld" prefix="inc" %>
<%@ page import="java.util.*"%>

<%
	ChartListWc wc = (ChartListWc)request.getAttribute(ChartListWc.ATTRIBUTE_NAME);
	Iterator it = wc.getChartList().iterator();	
	ChartDetailWc chart1;
	ChartDetailWc chart2;
	ChartDetailWc chart3;
	int colspan = 1;
    
   
%>


<%
if(wc.getChartList().size()==0){
    %>
    
    	<table class="basic_table">
				<tr>
					<td valign="top" align="left" >
							<p>There are no trainees that meet this criteria</p>
					
					</td>
				</tr>
			</table>
    
    <%
}

%>


<table class="no_space_table">

<%--	
	This will render 3 charts per row.
--%>
<% while (it.hasNext()) {%>
	<%	
		chart1=(ChartDetailWc)it.next();
		colspan=6;
		
		if (it.hasNext()) {
			chart2=(ChartDetailWc)it.next();
			colspan=3;
		} else {
			chart2=null;
		} 
		

	%> 
		<tr>
			<td align="center" valign="top" colspan="<%=colspan%>">
				<inc:include-wc component="<%=chart1%>"/>
			</td>
			
			<% if (chart2 != null) {%>
				<td align="center" valign="top" colspan="<%=colspan%>">
					<inc:include-wc component="<%=chart2%>"/>
				</td>	
			<% } %>
		
		
		</tr>
		<tr>
			<td colspan="6">&nbsp;</td>
		</tr>
<% } %> <%-- End While loop--%>

</table>
