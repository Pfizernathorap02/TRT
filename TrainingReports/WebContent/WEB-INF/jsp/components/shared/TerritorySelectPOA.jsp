<%@ page import="com.pfizer.webapp.AppConst"%>

<%@ page import="com.pfizer.webapp.user.TerritoryFilterForm"%>

<%@ page import="com.pfizer.webapp.user.UserTerritory"%>

<%@ page import="com.pfizer.webapp.wc.components.shared.TerritorySelectWc"%>
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

%>

<% if (ut.getType() == UserTerritory.TYPE_REGION || 

		ut.getType() == UserTerritory.TYPE_AREA || 

		ut.getType() == UserTerritory.TYPE_NATIONAL) { %>



	<table class="basic_table">

		<form action="<%=wc.getPostUrl()%>" method="post" class="form_basic">

		

			

				<% if (ut.getType() == UserTerritory.TYPE_NATIONAL) { %>

					<tr>

						<td><label>Area:</label></td>

					</tr>

					<tr>

						<td>

							<select id="<%=wc.getUserFilterForm().FIELD_AREA%>" name="<%=wc.getUserFilterForm().FIELD_AREA%>" onchange="updateTgixSelect(this,dynamicSelect)">

							</select>

						</td>

					</tr>

				<% } %>

				<% if (ut.getType() == UserTerritory.TYPE_AREA ||

						ut.getType() == UserTerritory.TYPE_NATIONAL) { %>

					<tr>

						<td><label>Region:</label></td>

					</tr>

					<tr>

						<td>

							<select id="<%=wc.getUserFilterForm().FIELD_REGION%>" name="<%=wc.getUserFilterForm().FIELD_REGION%>" onchange="updateTgixSelect(this,dynamicSelect)">

							</select>

						</td>

					</tr>

				<% } %>

				<tr>

					<td><label>District:</label></td>

				</tr>

				<tr>

					<td>

						<select id="<%=wc.getUserFilterForm().FIELD_DISTRICT%>" name="<%=wc.getUserFilterForm().FIELD_DISTRICT%>"  >

						</select>

					</td>

				</tr>

				<tr>

					<td>

						<input type="submit" value="Get Reports">	

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

