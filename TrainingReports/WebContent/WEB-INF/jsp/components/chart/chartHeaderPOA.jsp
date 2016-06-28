<%@ page language="java" contentType="text/html;charset=UTF-8"%>

<%@ page import="com.pfizer.webapp.AppConst"%>

<%@ page import="com.pfizer.webapp.wc.components.chart.ChartHeaderWc"%>

<%

	ChartHeaderWc wc = (ChartHeaderWc)request.getAttribute(ChartHeaderWc.ATTRIBUTE_NAME);

%>



<table class="no_space_width"> 

	

	<tr>
		<td align="right">

			&nbsp;&nbsp;&nbsp;
            <font class="normal_bold">Area:</font> <font class="normal"><%=wc.getArea()%></font>&nbsp;&nbsp;

			<font class="normal_bold">Region:</font> <font class="normal"><%=wc.getRegion()%></font>&nbsp;&nbsp;

			<font class="normal_bold">District:</font> <font class="normal"><%=wc.getDistrict()%></font>

		</td>

	</tr>

</table>