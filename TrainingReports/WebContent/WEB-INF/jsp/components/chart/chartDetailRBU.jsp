<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.Hashtable"%>
<%@ page import="java.util.Enumeration"%>
<%@ page import="java.util.Vector"%>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.HashMap" %>

<%@ page import="com.pfizer.db.RBUProductDocsLinks"%>

<%@ page import="com.pfizer.webapp.AppConst"%>

<%@ page import="com.pfizer.webapp.wc.components.chart.ChartDetailWc"%>

<%@ taglib uri="/WEB-INF/dynamic-include.tld" prefix="inc" %>
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
         <td valign="top" width="50%" align="left">
         <%
            List linksList = wc.getLinks();
            Iterator iter = linksList.iterator();
            while(iter.hasNext()){
            RBUProductDocsLinks links = (RBUProductDocsLinks)iter.next();    
         %>
         <a href="<%=links.getUrl()%>" target="_blank"><%=links.getDisplayName()%></a><br><br>
         <% 
            }
         %>
          </td>    
        <%
            }
        %> 

		<td align="center">

			<img src="<%= wc.getChart().getGraphURL()%>"  border=0 usemap="#<%= wc.getChart().getFilename()%>">

		</td>
		

	</tr>

	<tr>
        <td valign="top" width="50%" align="left">&nbsp;<br></td>
		<td  align="center"><inc:include-wc component="<%=wc.getLegend()%>"/></td>
	</tr>

	<tr>

		

		<td align="center">

			<% if ( wc.isShowDescription() ) { %>

				<font class="small"><%=wc.getDescription()%></font>

			<% } %>

		</td>

		

	</tr>

</table>

