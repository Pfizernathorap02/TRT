<%@ page language="java" contentType="text/html;charset=UTF-8"%>

<%@ page import="com.pfizer.webapp.AppConst"%>

<%@ page import="com.pfizer.webapp.wc.components.chart.ChartHeaderPWRAWc"%>

<%

	ChartHeaderPWRAWc wc = (ChartHeaderPWRAWc)request.getAttribute(ChartHeaderPWRAWc.ATTRIBUTE_NAME);

%>



<table class="no_space_width"> 

	<tr>

		<td colspan="3" rowspan="2">



			<br>



		</td>

	</tr>

	<tr>

		<td>

			<img align="left" src="<%=AppConst.IMAGE_DIR%>/logos/<%=wc.getProductCode()%>_logo.gif" alt="<%=wc.getProductCode()%>" >

		</td>

		<td align="left">

			<font class="normal_bold">Charts based on</font> <%=wc.getNumTrainees()%> 

			<font class="normal_bold">Trainees.</font>

		</td>

		<td align="right">

			&nbsp;&nbsp;&nbsp;<font class="normal_bold">Area:</font> <font class="normal"><%=wc.getArea()%></font>&nbsp;&nbsp;

			<font class="normal_bold">Region:</font> <font class="normal"><%=wc.getRegion()%></font>&nbsp;&nbsp;

			<font class="normal_bold">District:</font> <font class="normal"><%=wc.getDistrict()%></font>

		</td>

	</tr>

</table>