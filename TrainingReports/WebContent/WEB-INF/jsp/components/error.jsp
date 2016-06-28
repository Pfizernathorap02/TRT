<%@ page language="java" contentType="text/html;charset=UTF-8" isErrorPage="true"%>
<%@ page import="com.pfizer.webapp.wc.components.ErrorWc"%>

<%
	ErrorWc wc = (ErrorWc)request.getAttribute(ErrorWc.ATTRIBUTE_NAME);
%>

  <table bgcolor="#ebebe1" cellpadding="0" cellspacing="0" border="0" width="100%">
    <tr>
      <td  style="padding:4px 2px;">
        <table cellpadding="0" cellspacing="0" border="0" width="100%">
          <tr>
            <td class="label">An error has occurred</td>
          </tr>          
          <tr class="boxdata">
            <td>
              <li><a href="#" onclick="javascript:window.open('<%=request.getContextPath()%>/error?session_key=<%=wc.getSessionKey()%>','_new','scrollbars,resizable,height=600,width=616');">Click here</a><font color="#ff0000"> to view error detail and optionally send error notification to the system administrator.</font></li>
            </td>
          </tr>
        </table>
      </td>
    </tr>
    <tr>
      <td background="<%=request.getContextPath()%>/resources/images/tablefooter_tall_bgimage.gif" height="7" align="right" valign="middle">
      </td>      
    </tr>
  </table>

<% response.setStatus(200); %>

