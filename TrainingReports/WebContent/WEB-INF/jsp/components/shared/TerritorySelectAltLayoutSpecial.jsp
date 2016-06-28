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
    System.out.println("teamlist:" + tff.getTeamList().size());
%>

<% if (ut.getType() == UserTerritory.TYPE_REGION || 

		ut.getType() == UserTerritory.TYPE_AREA || 

		ut.getType() == UserTerritory.TYPE_NATIONAL) { %>


		
<div class="chartscontrol_area">
	<table class="basic_table"  border="2">

		<form action="<%=wc.getPostUrl()%>" method="post" class="form_basic">

                
                <% if ( wc.isShowTeam() ) {%>
                    <tr>
                        <td><img src="<%=AppConst.IMAGE_DIR%>/spacer.gif" width="25"></td><td><div class="chartscontrol_areaHdr">Team:</div></td>

						<td>

							<select id="<%=wc.getUserFilterForm().FIELD_TEAM%>" name="<%=wc.getUserFilterForm().FIELD_TEAM%>" onchange="updateTgixSelect(this,dynamicSelect)">
                                    <%=HtmlBuilder.getOptionsFromLabelValue(tff.getTeamList(),tff.getTeam())%>
                                    <%System.out.println("TFF Filter Form value"+tff.getTeam());%>
							</select>

						</td>
                        

					</tr>
                <% } %>

				<% if (ut.getType() == UserTerritory.TYPE_NATIONAL) { %>

					<tr>
                        <td><img src="<%=AppConst.IMAGE_DIR%>/spacer.gif"   height="0" width="25"></td>
						<td><div class="chartscontrol_areaHdr">Area:</div></td>


						<td>

							<select id="<%=wc.getUserFilterForm().FIELD_AREA%>" name="<%=wc.getUserFilterForm().FIELD_AREA%>" onchange="updateTgixSelect(this,dynamicSelect)">

							</select>

						</td>

					</tr>

				<% } %>

				<% if (ut.getType() == UserTerritory.TYPE_AREA ||

						ut.getType() == UserTerritory.TYPE_NATIONAL) { %>

					<tr>
<td><img src="<%=AppConst.IMAGE_DIR%>/spacer.gif"  height="0" width="25"></td>
						<td><div class="chartscontrol_areaHdr">Region:</div></td>


						<td>

							<select id="<%=wc.getUserFilterForm().FIELD_REGION%>" name="<%=wc.getUserFilterForm().FIELD_REGION%>" onchange="updateTgixSelect(this,dynamicSelect)">

							</select>

						</td>

					</tr>

				<% } %>

				<tr>
<td><img src="<%=AppConst.IMAGE_DIR%>/spacer.gif"   height="0" width="25"></td>
					<td><div class="chartscontrol_areaHdr">District:</div></td>


					<td>

						<select id="<%=wc.getUserFilterForm().FIELD_DISTRICT%>" name="<%=wc.getUserFilterForm().FIELD_DISTRICT%>"  >

						</select>

					</td>

				</tr>

				<tr>
<td colspan="2"><img src="<%=AppConst.IMAGE_DIR%>/spacer.gif"   height="0" width="25"></td>
					<td>

						<input name="" type="image" src="/TrainingReports/resources/images/btn_getreport.gif" style="margin-top:20px;">

					</td>

				</tr>	
				<tr>
<td colspan="2"><img src="<%=AppConst.IMAGE_DIR%>/spacer.gif"   height="0" width="25"></td>
					<td>


					</td>

				</tr>	

		</form>

	</table>
    
</div>

<script type="text/javascript" language="JavaScript">

	// initializes the onload function

	document.onload = dropDownPopulate();

</script>





<% } %>

