<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.pfizer.db.RBUProductDocsLinks"%>
<%@ page import="com.pfizer.webapp.AppConst"%>
<%@ page import="com.pfizer.webapp.wc.components.chart.ChartDetailWc"%>
<%@ page import="com.pfizer.db.ActivityDocsLinks"%>
<%@ taglib uri="/WEB-INF/dynamic-include.tld" prefix="inc" %>
<%@ page import="java.util.List"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.Hashtable"%>
<%@ page import="java.util.Enumeration"%>
<%@ page import="java.util.Vector"%>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Map" %>
<%

	ChartDetailWc wc = (ChartDetailWc)request.getAttribute(ChartDetailWc.ATTRIBUTE_NAME);

%>

<%=wc.getChart().getImageMap()%>

<%
    int width = AppConst.CHART_WIDTH;
    if(wc.getLinkIdentifier() != null && !wc.getLinkIdentifier().equals("") && wc.getLinkIdentifier().equals("showLink")){
            width =  AppConst.CHART_WIDTH_WITH_LINK;       
    }
%>
<table class="no_space_width" width="<%=width%>">
	<tr>
         <%
            if(wc.getLinkIdentifier() != null && !wc.getLinkIdentifier().equals("") && wc.getLinkIdentifier().equals("showLink")){
         %>
         <td valign="top" width="50%" align="left">&nbsp;<br>
         <%
            List linksList = wc.getLinks();
            Iterator iter = linksList.iterator();
            while(iter.hasNext()){
            RBUProductDocsLinks links = (RBUProductDocsLinks)iter.next();    
         %>
         <a href="<%=links.getUrl()%>" target="_blank"><%=links.getDisplayName()%></a><br>
         <% 
            }
         %>
          </td>    
        <%
            }
        %> 

		<td align="center">
                <!--Added for major enhancement 3.6 - F1-->
                <% if(wc.getP2lPhaseChartURL() == null) { %> 
			<img src="<%= wc.getChart().getGraphURL()%>"  border=0 usemap="#<%= wc.getChart().getFilename()%>">
                <% } else { %>
		        <a href="<%=wc.getP2lPhaseChartURL()%>"><img src="<%= wc.getChart().getGraphURL()%>"  border=0 usemap="#<%= wc.getChart().getFilename()%>"></a> 
                 
	            <% } %>
                
               <!--ends here-->

		</td>
		 <%
            if(wc.getLinkIdentifier() != null && !wc.getLinkIdentifier().equals("") && wc.getLinkIdentifier().equals("showReportLink")){
         %>
         <td valign="middle" width="50%" align="right" nowrap>&nbsp;<br>
         <%
            List linksList = wc.getLinks();
            Iterator iter = linksList.iterator();
            while(iter.hasNext()){
            ActivityDocsLinks links = (ActivityDocsLinks)iter.next();    
         %>
         <a href="<%=links.getUrl()%>" target="_blank"><%=links.getDisplayName()%></a>
         <% 
            }
         %>
          </td>    
        <%
            }
        %> 
         		
	</tr>
    <!--If condition added for major enhancement 3.6 - F1-->
    <%if(wc.getLegend() != null) {%>
	<tr>
		<td colspan="3" align="center"><inc:include-wc component="<%=wc.getLegend()%>"/></td>
	</tr>
    <%}%>
	<tr>

		<td><img src="<%=AppConst.IMAGE_DIR%>/spacer.gif" width="0"></td>

		<td align="center">

			<% if ( wc.isShowDescription() ) { %>

				<font class="small"><%=wc.getDescription()%></font>

			<% } %>

		</td>

		<td><img src="<%=AppConst.IMAGE_DIR%>/spacer.gif" width="0"></td>

	</tr>

</table>

