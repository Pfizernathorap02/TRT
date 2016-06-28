<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.pfizer.webapp.AppConst"%>
<%@ page import="com.pfizer.webapp.user.UserTerritory"%>
<%@ page import="com.pfizer.webapp.wc.components.report.MainReportListWc"%>
<%@ page import="com.tgix.Utils.Util"%>
<%@ page import="java.awt.GraphicsEnvironment"%>
<%@ taglib uri="/WEB-INF/dynamic-include.tld" prefix="inc" %>

<%
	MainReportListWc wc = (MainReportListWc)request.getAttribute(MainReportListWc.ATTRIBUTE_NAME);
	UserTerritory ut = wc.getUserTerritory();
    String comingFrom =session.getAttribute("ReportType")==null?"":(String)session.getAttribute("ReportType");
    String mailSub="FFT Training Follow-up";
    if("PowersPOA".equalsIgnoreCase(comingFrom)) 
      mailSub ="Powers Mid-POA1  Follow-up";

    if("PowersDFHStudy".equalsIgnoreCase(comingFrom)) 
      mailSub="Powers Driving Force Home Study Follow-up";

    if("PowersPLC".equalsIgnoreCase(comingFrom))    
      mailSub="POWERS Driving Force PLC Report";          
    
%>


<table class="basic_table" >
	
	<tr>
		<td><img src="<%=AppConst.IMAGE_DIR%>/spacer.gif" width="15"></td>
		<td align="left">
			<table>
				<tr>
					<td colspan="3">
						<table class="no_space_width">
							<tr>
								<td><p id="table_title" style="font-size=.9em"><%=wc.getReportTitle()%></p></td>
                                <%
                                if("Overall".equalsIgnoreCase(wc.getUserFilter().getProduct())){
                                    %>
                                    <td><img src="<%=AppConst.IMAGE_DIR%>/spacer.gif" width="15"></td>
                                    <%
                                }else if  (!Util.isEmpty(wc.getUserFilter().getProduct()) ) {
                                    %>
                                    <td align="center"><img src="<%=AppConst.IMAGE_DIR%>/logos/<%=wc.getUserFilter().getProduct()%>_logo.gif" alt="<%=wc.getUserFilter().getProduct()%>" ></td>
                                    <%
                                } else {
                                    %>
                                    <td></td>
                                    <%
                                }
                                %>
								
							</tr>
						</table>
					</td>
				</tr>
				<tr>
					<td valign="top">
						<inc:include-wc component="<%=wc.getTerritorySelect()%>"/>
					</td>
					<td><img src="<%=AppConst.IMAGE_DIR%>/spacer.gif" width="15"></td>
					<td valign="top">
						<table>                        
                        <%
                         if("PowersPOA".equalsIgnoreCase(comingFrom) || "PowersDFHStudy".equalsIgnoreCase(comingFrom)
                         || "PowersPLC".equalsIgnoreCase(comingFrom) || "SPFPLC".equalsIgnoreCase(comingFrom)) {
                            %>
                            <tr>
                            <td>&nbsp;</td>
                            </tr>
                            <%
                         }
                        
                        %>
                        
							<% if ( ut.getType() == UserTerritory.TYPE_NATIONAL ) { %>
								<tr>
								<% if ( wc.getAreaManager() != null ) { %>
										<td height="23">
											<label>VP:</label><font><a style="text-decoration = underline" href="mailto:<%=wc.getAreaManager().getEmail()%>?subject=<%=mailSub%>"> <%= wc.getAreaManager().getPreferredName() + " " + wc.getAreaManager().getLastName()%></a></font>
										</td>
								<% } else { %>
										<td height="23">&nbsp;</td>
								<% } %>
								</tr>
							<% } %>
							<% if ( ut.getType() == UserTerritory.TYPE_AREA ||	
								ut.getType() == UserTerritory.TYPE_NATIONAL ) { %>							
								<tr>
									<% if ( wc.getRegionManager() != null ) { %>
										<td height="23">
											<label>RM:</label><a style="text-decoration = underline"  href="mailto:<%=wc.getRegionManager().getEmail()%>?subject=<%=mailSub%>"> <%= wc.getRegionManager().getPreferredName() + " " + wc.getRegionManager().getLastName()%></a>
										</td>
									<% } else { %>
											<td height="23">&nbsp;</td>
									<% } %>
								</tr>
							<% } %>
							<% if ( ut.getType() == UserTerritory.TYPE_REGION ||
								ut.getType() == UserTerritory.TYPE_AREA ||	
								ut.getType() == UserTerritory.TYPE_NATIONAL ) { %>							
								<tr>
									<% if ( wc.getDistrictManager() != null ) { %>
										<td height="23">
											<label>DM:</label><a style="text-decoration = underline" href="mailto:<%=wc.getDistrictManager().getEmail()%>?subject=<%=mailSub%>"> <%= wc.getDistrictManager().getPreferredName() + " " + wc.getDistrictManager().getLastName()%></a>
										</td>
									<% } else { %>
											<td height="23">&nbsp;</td>
									<% } %>
								</tr>
							<% } %>
						</table>
					</td>				
				</tr>
			</table>
		</td>
		<td align="right">
			<% if (wc.getChart() != null) { %>
				<inc:include-wc component="<%=wc.getChart()%>"/>
			<% } %>
		</td>
		<td><img src="<%=AppConst.IMAGE_DIR%>/spacer.gif" width="15"></td>
	</tr>
	<tr>
		<td><img src="<%=AppConst.IMAGE_DIR%>/spacer.gif" width="15"></td>
		<td valign="top" colspan="2">
			<% if (wc.getReportList() != null) { %>
				<inc:include-wc component="<%=wc.getReportList()%>"/>
			<% } %>
			
			<% if (wc.getTotal() == 0) {%>
				<p>There are no trainees that meet this criteria</p>
			<% } %>
		</td>
		<td><img src="<%=AppConst.IMAGE_DIR%>/spacer.gif" width="15"></td>
	</tr>
</table>