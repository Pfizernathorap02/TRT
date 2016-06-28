<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.pfizer.webapp.AppConst"%>
<%@ page import="com.pfizer.webapp.wc.components.HomepageWc"%>
<%@ taglib uri="/WEB-INF/dynamic-include.tld" prefix="inc" %>

<%
	HomepageWc wc = (HomepageWc)request.getAttribute(HomepageWc.ATTRIBUTE_NAME);
%>

<table>
	<tr>
		<td> SVPs, DCOs, NSDs </
		<td> RMs, SDs, ASDs, ARMs,SDIRs,SDTLs,CADs </td>
		<td> DMs </td>
	</tr>
	<tr>
		<td valign="top"> <inc:include-wc component="<%=wc.getSvpComponent()%>"/>
		<br>
		<%-- Infosys code changes starts here
		 <a href="<%=AppConst.APP_ROOT%>/originaluser.do">Original User</a> --%>
		<a href="<%=AppConst.APP_ROOT%>/originaluser">Original User</a>
		<!-- Infosys migrated code weblogic to jboss changes end here -->
        <br>
        <br>
        RCs
        <inc:include-wc component="<%=wc.getRcComponent()%>"/>
		</td>
		<td valign="top"> <inc:include-wc component="<%=wc.getVpComponent()%>"/></td>
		<td valign="top"> <inc:include-wc component="<%=wc.getRmComponent()%>"/></td>
		<td valign="top"> <inc:include-wc component="<%=wc.getDmComponent()%>"/></td>
	</tr>
</table>