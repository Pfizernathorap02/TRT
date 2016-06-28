<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.pfizer.webapp.AppConst"%>
<%@ page import="com.pfizer.webapp.user.User"%>
<%@ page import="com.pfizer.webapp.user.UserTerritory"%>
<%@ page import="com.pfizer.webapp.wc.components.report.phasereports.MainReportAllStatusWc"%>
<%@ page import="java.awt.GraphicsEnvironment"%>
<%@ page import="com.pfizer.webapp.user.UserSession"%>
<%@ page import="com.tgix.Utils.Util"%>
<%@ taglib uri="/WEB-INF/dynamic-include.tld" prefix="inc" %>

<%	
	MainReportAllStatusWc wc = (MainReportAllStatusWc)request.getAttribute(MainReportAllStatusWc.ATTRIBUTE_NAME);
    
    //Added for Major Enhancement 3.6 - F1
    if(session.getAttribute("parentActivityPk") != null){
            String fromRequest = (String)session.getAttribute("parentActivityPk");
            session.setAttribute("parentActivityPk",fromRequest);
    }
    
    if(session.getAttribute("listlevel") != null){
            String fromReq = (String)session.getAttribute("listlevel");
            session.setAttribute("listlevel",fromReq);
    }
     //End:Added for Major Enhancement 3.6 - F1
   
%>

<script type="text/javascript" language="JavaScript">
</script>

<table class="blue_table_without_border">
    <tr>
        <td  colspan="2">
            <img src="<%=AppConst.IMAGE_DIR%>/spacer.gif">
        </td>  
    </tr>             
    <tr>
        <td colspan="2">	
            <div class="breadcrumb">
                <a href="/TrainingReports/reportselect" style="margin-left:0px;">Home</a> / 
                <a href="begin?track=<%=wc.getTrack().getTrackId()%>"><%=wc.getTrack().getTrackLabel()%></a> / 
                <a href="listReportAllStatus?activitypk=<%=wc.getActivityId()%>"><%=wc.getPageName()%></a>
            </div>
        </td>
    </tr>
    <tr>
    <%if (wc.getErrorMsg().length() > 0) {%> 
        <td colspan="2">
                <%=wc.getErrorMsg()%>
        </td>
    <%} else {%>
        <td>
            <inc:include-wc component="<%=wc.getArea1()%>"/>
        </td>
        <td colspan="3" width="60%">
            <% if(wc.getArea3() != null) { %>
                <inc:include-wc component="<%=wc.getArea3()%>"/>	
            <% } %>
        </td>
    <%}%>
    </tr>
    <tr>
        <td align="left" valign="top" colspan="2" nowrap>
            <div class="chartscontrol_area">
            <table class="blue_table_without_border" align="left">
                <tr>
                    <td align="left" valign="top" colspan="2" >
                    <div class="chartscontrol_area" align="left"><inc:include-wc component="<%=wc.getArea2()%>"/></div>	
                    </td>
                    <td>
<!-- Added for TRT Phase 2 - HQ Users -->
                    <% User user = wc.getUser();
                    if(!user.isHQUser()){%>
                        <div class="chartscontrol_area" align="left"><inc:include-wc component="<%=wc.getESearch()%>"/></div>
                <%}%>
                    </td>
                </tr>
            </table>
            </div>
        </td>
    </tr>
</table>



