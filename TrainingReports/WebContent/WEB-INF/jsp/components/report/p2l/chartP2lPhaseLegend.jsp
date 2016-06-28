<%@ page language="java" contentType="text/html;charset=UTF-8"%>

<%@ page import="com.pfizer.processor.OverallProcessor"%>

<%@ page import="com.pfizer.webapp.AppConst"%>

<%@ page import="com.pfizer.webapp.wc.components.chart.ChartLegendWc"%>
<%@ page import="com.pfizer.webapp.wc.components.chart.ChartP2lLegendWc"%>
<%@ page import="com.pfizer.webapp.wc.components.chart.ChartP2lPhaseLegendWc"%>
<%@ page import="com.tgix.printing.LoggerHelper"%>

<%

	ChartP2lPhaseLegendWc wc = (ChartP2lPhaseLegendWc)request.getAttribute(ChartP2lPhaseLegendWc.ATTRIBUTE_NAME);

%>

 

<table class="no_space_width" style="font-size: .8em; border-width: 1px;" >

	<tr>

		<td bgcolor="#2988C7" width="10px"></td>

		<td>&nbsp;&nbsp;Complete&nbsp;<%="("+wc.getChart().getCompleted()+")"%></td>

	</tr>
   <!--
    <% if (wc.showExempt()) { %>
        
	<tr>
       
		<td bgcolor="#ff9900" width="10px"></td>

		<td>&nbsp;&nbsp;Waived&nbsp;<%="("+wc.getChart().getExempt()+")"%></td>

	</tr>
    <% } %> 
    -->
    

       
	<tr>
      
		<td bgcolor="#99cc00" width="10px"></td>

		<td>&nbsp;&nbsp;Assigned&nbsp;<%="("+wc.getChart().getAssigned()+")"%></td>

	</tr>
<!--    

    <% if (wc.showPending()) { %>
    
	<tr>
		<td bgcolor="#FF0000" width="10px"></td>
		<td>&nbsp;&nbsp;Pending Approval&nbsp;&nbsp;&nbsp;</td>
	</tr> 
    <% } %>
    -->
    <%LoggerHelper.logSystemDebug("todaynew7");%>
   
	<tr>

		<td bgcolor="#993300" width="10px"></td>

		<td>&nbsp;&nbsp;Not Complete&nbsp;<%="("+wc.getChart().getNotComplete()+")"%></td>

	</tr>
	<tr>

		<td bgcolor="#fdf39c" width="10px"></td>

		<td>&nbsp;&nbsp;On-Leave&nbsp;<%="("+wc.getChart().getOnleave()+")"%></td>

	</tr>
    <!--
    <tr>

		<td bgcolor="#00FFFF" width="10px"></td>

		<td>&nbsp;&nbsp;Cancelled&nbsp<%="("+wc.getChart().getCancelled()+")"%></td>

	</tr>-->

</table>
