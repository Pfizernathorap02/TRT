<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.pfizer.db.Employee"%>
<%@ page import="com.pfizer.webapp.AppConst"%>
<%@ page import="com.pfizer.webapp.wc.components.SelectEmployeeWc"%>
<%
	SelectEmployeeWc wc = (SelectEmployeeWc)request.getAttribute(SelectEmployeeWc.ATTRIBUTE_NAME);
	Employee[] emp = wc.getList();
%>


<table class="basic_table" style="border-left:solid 1px #ccc;	border-top: solid 1px #ccc;">
<% for ( int i=0; i < emp.length; i ++ ) { %>
	<tr>
		<td style="border-right:solid 1px #ccc;	border-bottom: solid 1px #ccc;"><a href="<%=AppConst.APP_ROOT%>/reportselect?emplid=<%=emp[i].getEmplId()%>"><%= emp[i].getFirstName() %> <%= emp[i].getLastName() %></a></td>
		<td style="border-right:solid 1px #ccc;	border-bottom: solid 1px #ccc;"><%= emp[i].getDisplayCluster()%></td>
	</tr>
<% } %>
</table>