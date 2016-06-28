<%@ page language="java" contentType="text/html;charset=UTF-8"%>

<%@ page import="com.pfizer.webapp.AppConst"%>

<%@ page import="com.pfizer.webapp.wc.components.chart.ChartHeaderWc"%>

<%

	ChartHeaderWc wc = (ChartHeaderWc)request.getAttribute(ChartHeaderWc.ATTRIBUTE_NAME);

%>



<table class="no_space_width"> 

	<tr>

		<td colspan="3" rowspan="2">

			<a class="pdflink" href="<%=AppConst.APP_ROOT%>/resources/pdf/<%=wc.getProductCode() + "_TrainingSummary_Final.pdf" %>" target="_new">FFT Training Summary</a>

			<br>

			<a class="pdflink" href="<%=AppConst.APP_ROOT%>/resources/pdf/<%=wc.getProductCode() + "_CoachingObjections.pdf"%>" target="_new">Coaching Objections</a>

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

		<%--

		<td align="right">

			&nbsp;&nbsp;&nbsp;<font class="normal_bold">Area:</font> <font class="normal"><%=wc.getArea()%></font>&nbsp;&nbsp;

			<font class="normal_bold">Region:</font> <font class="normal"><%=wc.getRegion()%></font>&nbsp;&nbsp;

			<font class="normal_bold">District:</font> <font class="normal"><%=wc.getDistrict()%></font>

		</td>

		--%>

	</tr>

	

</table>