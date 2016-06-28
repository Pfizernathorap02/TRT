<%@ page language="java" contentType="text/html;charset=UTF-8"%>

<%@ page import="com.pfizer.processor.OverallProcessor"%>

<%@ page import="com.pfizer.webapp.AppConst"%>

<%@ page import="com.pfizer.webapp.wc.components.chart.ChartP2lLegendWc"%>
<%@ page import="com.pfizer.webapp.wc.components.chart.ChartP2lSubsetLegendWc"%>

<%

	ChartP2lSubsetLegendWc wc = (ChartP2lSubsetLegendWc)request.getAttribute(ChartP2lSubsetLegendWc.ATTRIBUTE_NAME);

%>



<table class="no_space_width" style="font-size: .8em; border-width: 1px;" >

	
    <%if(wc.getChkStatus().equals("All")) {%>
    
    <tr>

		<td bgcolor="#2988C7" width="10px"></td>

		<td>&nbsp;&nbsp;Complete</td>

	</tr>
   
    <% if (wc.showExempt()) { %>
        
	<tr>
       
		<td bgcolor="#ff9900" width="10px"></td>

		<td>&nbsp;&nbsp;Waived</td>

	</tr>
    <% } %>
    

    <% if (wc.showAssigned()) { %>
       
	<tr>
      
		<td bgcolor="#99cc00" width="10px"></td>

		<td>&nbsp;&nbsp;Assigned</td>

	</tr>
    <% } %>

    <% if (wc.showPending()) { %>
    
	<tr>
		<td bgcolor="#FF3366" width="10px"></td>
		<td>&nbsp;&nbsp;Pending&nbsp;&nbsp;&nbsp;</td>
	</tr> 
    <% } %>
  
   
	<tr>

		<td bgcolor="#993300" width="10px"></td>

		<td>&nbsp;&nbsp;Registered&nbsp;&nbsp;&nbsp;</td>

	</tr>
	<tr>

		<td bgcolor="#fdf39c" width="10px"></td>

		<td>&nbsp;&nbsp;On-Leave</td>

	</tr>
    <tr>

		<td bgcolor="#00FFFF" width="10px"></td>

		<td>&nbsp;&nbsp;Cancelled</td>

	</tr>
    
    
    <% } else { %>
    
    <%if(wc.getChkStatus().equals("Complete")) {%>
    
    <tr>

		<td bgcolor="#2988C7" width="10px"></td>

		<td>&nbsp;&nbsp;Complete</td>

	</tr>
    <%}%>
    
    <%if(wc.getChkStatus().equals("Exempt")) {%>
    
   <tr>
       
		<td bgcolor="#ff9900" width="10px"></td>

		<td>&nbsp;&nbsp;Exempt</td>

	</tr>
    <%}%>
    
    <%if(wc.getChkStatus().equals("Assigned")) {%>
    
   <tr>
      
		<td bgcolor="#99cc00" width="10px"></td>

		<td>&nbsp;&nbsp;Assigned</td>

	</tr>
    
    <%}%>
    
    <%if(wc.getChkStatus().equals("Pending")) {%>
    
   <tr>
		<td bgcolor="#FF3366" width="10px"></td>
		<td>&nbsp;&nbsp;Pending &nbsp;&nbsp;&nbsp;</td>
	</tr> 
    
    <%}%>
    
    <%if(wc.getChkStatus().equals("Registered")) {%>
    
   <tr>

		<td bgcolor="#993300" width="10px"></td>

		<td>&nbsp;&nbsp;Registered&nbsp;&nbsp;&nbsp;</td>

	</tr>
    
    <%}%>
    
    <%if(wc.getChkStatus().equals("On-Leave")) {%>
    
   <tr>

		<td bgcolor="#fdf39c" width="10px"></td>

		<td>&nbsp;&nbsp;On-Leave</td>

	</tr>
    
    <%}%>
    
    <%if(wc.getChkStatus().equals("Cancelled")) {%>
    
   <tr>

		<td bgcolor="#00FFFF" width="10px"></td>

		<td>&nbsp;&nbsp;Cancelled</td>

	</tr>
    
    <%}%>
    
	<tr>

		<td bgcolor="#CC99FF" width="10px"></td>

		<td>&nbsp;&nbsp;Rest</td>

	</tr>
    <%}%>

</table>

