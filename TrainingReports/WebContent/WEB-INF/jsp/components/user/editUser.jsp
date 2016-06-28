<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.pfizer.db.UserAccess"%>
<%@ page import="com.pfizer.webapp.AppConst"%>
<%@ page import="com.pfizer.webapp.AppQueryStrings"%>
<%@ page import="com.pfizer.webapp.user.User"%>
<%@ page import="com.pfizer.webapp.user.UserSession"%>
<%@ page import="com.pfizer.webapp.wc.components.user.EditUserWc"%>
<%@ page import="com.pfizer.webapp.wc.components.user.UserListWc"%>
<%@ page import="com.tgix.Utils.Util"%>
<%@ taglib uri="/WEB-INF/dynamic-include.tld" prefix="inc" %>

<%
	EditUserWc wc = (EditUserWc)request.getAttribute(EditUserWc.ATTRIBUTE_NAME);
	UserAccess ua = wc.getUserAccess();
%>
<script type="text/javascript" language="JavaScript">
	function goback() {
		window.location="/TrainingReports/admin/user";
	}
</script>

<table class="no_space_table">
	<tr>
		<td rowspan="3" width="20"></td>
		<td></td>
	</tr>
	<tr>
		<td><br><br><br></td>
	</tr>
	<tr>
		<td>
		
			<table class="no_space_table">
				<form class="form_basic" action="/TrainingReports/admin/saveuser" method="post">
				<input type="hidden" name="<%=UserAccess.FIELD_USER_ID%>" value="<%=Util.toEmpty(ua.getUserId())%>">
				<tr>
					<td><label>Last Name</label></td>
					<td width="30"></td>
					<td><input type="text" class="text" size="30" value="<%=Util.toEmpty(ua.getLname())%>" name="<%=UserAccess.FIELD_LNAME%>"></td>
				</tr>
				<tr>
					<td colspan="3"><img src="/TrainingReports/resources/images/spacer.gif" height="5"></td>
				</tr>
				<tr>
					<td><label>First Name</label></td>
					<td></td>
					<td><input type="text"  class="text"  size="30" value="<%=Util.toEmpty(ua.getFname())%>" name="<%=UserAccess.FIELD_FNAME%>"></td>
				</tr>
				<tr>
					<td colspan="3"><img src="/TrainingReports/resources/images/spacer.gif" height="5"></td>
				</tr>
				<tr>
					<td><label>Email</label></td>
					<td></td>
					<td><input type="text"  class="text"  size="30" value="<%=Util.toEmpty(ua.getEmail())%>" name="<%=UserAccess.FIELD_EMAIL%>"></td>
				</tr>
				<tr>
					<td colspan="3"><img src="/TrainingReports/resources/images/spacer.gif" height="5"></td>
				</tr>
				<tr>
					<td><label>Emplid</label></td>
					<td></td>
					<td><input type="text"  class="text"  size="30" value="<%=Util.toEmpty(ua.getEmplid())%>" name="<%=UserAccess.FIELD_EMPLID%>"></td>
				</tr>
				<tr>
					<td colspan="3"><img src="/TrainingReports/resources/images/spacer.gif" height="5"></td>
				</tr>
				<tr>
					<td><label>NT ID</label></td>
					<td></td>
					<td><input type="text"   class="text" size="30" value="<%=Util.toEmpty(ua.getNtId())%>" name="<%=UserAccess.FIELD_NTID%>"></td>
				</tr>
				<tr>
					<td colspan="3"><img src="/TrainingReports/resources/images/spacer.gif" height="5"></td>
				</tr>
				<tr>
					<td><label>NT Domain</label></td>
					<td></td>
					<td><input type="text"   class="text" size="30" value="<%=Util.toEmpty(ua.getNtDomain())%>" name="<%=UserAccess.FIELD_NTDOMAIN%>"></td>
				</tr>
				<tr>
					<td colspan="3"><img src="/TrainingReports/resources/images/spacer.gif" height="5"></td>
				</tr>
				<tr>
					<td><label>User Type</label></td>
					<td></td>
					<td>
						<select name="<%=UserAccess.FIELD_USER_TYPE%>">
							<option <%=User.USER_TYPE_ADMIN.equals(ua.getUserType())?"selected":""%> value="<%=User.USER_TYPE_ADMIN%>">Admin</option>
							<option <%=User.USER_TYPE_SUPER_ADMIN.equals(ua.getUserType())?"selected":""%> value="<%=User.USER_TYPE_SUPER_ADMIN%>">Super Admin</option>
							<option <%=User.USER_TYPE_TSR.equals(ua.getUserType())?"selected":""%> value="<%=User.USER_TYPE_TSR%>">TSR Admin</option>
                            <option <%=User.USER_TYPE_FBU.equals(ua.getUserType())?"selected":""%> value="<%=User.USER_TYPE_FBU%>">Feedback User</option>
                            <!-- Added for TRT Phase 2 - Requirement no. F6-->
                            <option <%=User.USER_TYPE_HQ.equals(ua.getUserType())?"selected":""%> value="<%=User.USER_TYPE_HQ%>">HQ User</option>
						</select>
					</td>
				</tr>
				<tr>
					<td colspan="3"><img src="/TrainingReports/resources/images/spacer.gif" height="5"></td>
				</tr>
				<tr>
					<td><label>Status</label></td>
					<td></td>
					<td>
						<select name="<%=UserAccess.FIELD_STATUS%>">
							<option <%="Active".equals(ua.getStatus())?"selected":""%> value="Active">Active</option>
							<option <%="Inactive".equals(ua.getStatus())?"selected":""%> value="Inactive">Inactive</option>
						</select>
					</td>
				</tr>
				<tr>
					<td colspan="3"><img src="/TrainingReports/resources/images/spacer.gif" height="5"></td>
				</tr>
				
				<tr>
					<td></td>
					<td></td>
					<td>
						<input type="submit" value=" Save ">
						<input type="submit" value=" Cancel " onclick="goback(); return false;">	
					</td>
				</tr>
				</form>
			</table>
			
		</td>
	</tr>
</table>