<%@ page language="java" contentType="text/html;charset=UTF-8"%>

<%@ page import="com.pfizer.webapp.AppConst"%>

<%@ page import="com.pfizer.webapp.wc.components.chart.ChartHeaderWc"%>
<%@ page import="com.tgix.Utils.Util"%>

<%

	ChartHeaderWc wc = (ChartHeaderWc)request.getAttribute(ChartHeaderWc.ATTRIBUTE_NAME);

%>



<table class="no_space_width" width="90%" height="0%"> 
    <tr>
        <td colspan="=2">
            <img src="<%=AppConst.IMAGE_DIR%>/spacer.gif" height="25">
        </td>
    </tr>
    <tr>
        <td>
        	<div class="breadcrumb">
                <a href="/TrainingReports/reportselect" style="margin-left:0px;">Home</a> / 
                <a href="/TrainingReports/phase/begin?track=<%=wc.getTrack().getTrackId()%>"><%=wc.getTrack().getTrackLabel()%></a> 	
        	</div>
        </td>
		<td align="right" valign="top">
			<div class="chartinfo">
            </div>
		</td>
	</tr>
</table>