<%@ page import="com.pfizer.webapp.AppConst"%>
<%@ page import="com.pfizer.webapp.user.TerritoryFilterForm"%>
<%@ page import="com.pfizer.webapp.user.UserTerritory"%>
<%@ page import="com.pfizer.webapp.wc.components.shared.TerritorySelectWc"%>
<%@ page import="com.tgix.html.HtmlBuilder"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.Hashtable"%>
<%@ page import="java.util.Enumeration"%>
<%@ page import="java.util.Vector"%>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.HashMap" %>

<%
	TerritorySelectWc wc = (TerritorySelectWc)request.getAttribute(TerritorySelectWc.ATTRIBUTE_NAME);
	List areas = wc.getUserTerritory().getAreas();
	UserTerritory ut = wc.getUserTerritory();
	TerritoryFilterForm tff = wc.getUserFilterForm();
    String msg="";
    
    
%>
<% if (ut.getType() == UserTerritory.TYPE_REGION || 
		ut.getType() == UserTerritory.TYPE_AREA || 
		ut.getType() == UserTerritory.TYPE_NATIONAL) { %>

	<table class="basic_table">
		<form action="<%=wc.getPostUrl()%>" method="post" class="form_basic">

                <% if ( wc.isShowTeam() ) {%>
                    <tr>
                        <td><label>Team:</label></td>
                        <td><img src="<%=AppConst.IMAGE_DIR%>/spacer.gif" width="10"></td>

						<td>

							<select id="<%=wc.getUserFilterForm().FIELD_TEAM%>" name="<%=wc.getUserFilterForm().FIELD_TEAM%>" onchange="updateTgixSelect(this,dynamicSelect)">
                                    <%=HtmlBuilder.getOptionsFromLabelValue(tff.getTeamList(),tff.getTeam())%>
							</select>

						</td>

					</tr>
                <% } %>		
			
				<% if (ut.getType() == UserTerritory.TYPE_NATIONAL) { %>
					<tr>
						<td height="23"><label>Area:</label></td>
						<td><img src="<%=AppConst.IMAGE_DIR%>/spacer.gif" width="10"></td>
						<td>
							<select id="<%=wc.getUserFilterForm().FIELD_AREA%>" name="<%=wc.getUserFilterForm().FIELD_AREA%>" onchange="updateTgixSelect(this,dynamicSelect)">
							</select>
						</td>
					</tr>
				<% } %>
				<% if (ut.getType() == UserTerritory.TYPE_AREA ||
						ut.getType() == UserTerritory.TYPE_NATIONAL) { %>
					<tr>
						<td height="23"><label>Region:</label></td>
						<td><img src="<%=AppConst.IMAGE_DIR%>/spacer.gif" width="10"></td>
						<td>
							<select id="<%=wc.getUserFilterForm().FIELD_REGION%>" name="<%=wc.getUserFilterForm().FIELD_REGION%>" onchange="updateTgixSelect(this,dynamicSelect)">
							</select>
						</td>
					</tr>
				<% } %>
				<tr>
					<td height="23"><label>District:</label></td>
					<td><img src="<%=AppConst.IMAGE_DIR%>/spacer.gif" width="10"></td>
					<td>
						<select id="<%=wc.getUserFilterForm().FIELD_DISTRICT%>" name="<%=wc.getUserFilterForm().FIELD_DISTRICT%>"  >
						</select>
					</td>
				</tr>
				<tr>
					<td colspan="2"></td>
					<td>
						<input type="submit" value="Re-run Report">	
					</td>
				</tr>	
		</form>
	</table>
	<br>
	<br>
	<br>
<script type="text/javascript" language="JavaScript">
	// initializes the onload function
	document.onload = dropDownPopulate();
</script>


<% } %>
