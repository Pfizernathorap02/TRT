<%@ page language="java" contentType="text/html;charset=UTF-8" isErrorPage="true"%>

<%
	java.util.Date dateTime = new java.util.Date();
	String errorSessionKey = request.getParameter("session_key");
    String errorStacktrace = (String)session.getAttribute(errorSessionKey + ".error.stacktrace");
%>

      <table cellpadding="2" cellspacing="0">
		<form class="form_basic" action="/TrainingReports/logerror">
        <tr>
          <td>          
            <table width="100%">
				<tr>
					<td>
						<span class="label">Date/Time</span>
					</td>
					<td align="right">
						&nbsp;
					</td>
				</tr>
			</table>          
          </td>
        </tr>
        <tr class="boxdata">
          <td><%=dateTime%></td>
        </tr>
        <tr>
          <td class="label">Please describe the circumstances of this error</td>
        </tr>
        <tr class="boxdata">
          <td>
            <textarea name="description" cols="60" rows="10"></textarea>
          </td>
        </tr>
        <tr class="boxdata">
          <td>
            &nbsp;
          </td>
        </tr>           
          <tr>
            <td  height="21" align="left">
				<input type="hidden" name="session_key" value="<%=errorSessionKey%>">
				<input type="submit" value="Submit"/>
				<input type="submit"  value="Cancel" onClick="window.close(); return false;"/>    
			</td>
          </tr>             
          <tr>
            <td align="left">
            <strong> Error Details</strong>:
          </tr>                       
        <tr class="boxdata">
          <td>
            <%=errorStacktrace!=null ? errorStacktrace : ""%>
          </td>
        </tr>   
		</form>     
      </table>

<% response.setStatus(200); %>

