<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.pfizer.db.P2lTrack"%>
<%@ page import="com.pfizer.db.P2lTrackPhase"%>
<%@ page import="com.pfizer.webapp.wc.components.EditMenuWc"%>
<%@ page import="com.pfizer.webapp.wc.components.admin.ActivitySearchLaunchMeetingWc"%>
<%@ page import="com.pfizer.webapp.wc.components.admin.ActivitySearchWc"%>
<%@ page import="com.pfizer.webapp.wc.components.admin.EditReportWc"%>
<%@ page import="com.tgix.Utils.Util"%>
<%@ page import="java.math.BigDecimal"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.Hashtable"%>
<%@ page import="java.util.Enumeration"%>
<%@ page import="java.util.Vector"%>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Map" %>
<%            
	ActivitySearchLaunchMeetingWc wc = (ActivitySearchLaunchMeetingWc)request.getAttribute(ActivitySearchWc.ATTRIBUTE_NAME);
    System.out.println("Size:" + wc.getSearchResults().size());
%>
<html>
<head>
		<link rel="stylesheet" type="text/css" href="/TrainingReports/resources/css/header.css"/>
	
		<link rel="stylesheet" type="text/css" href="/TrainingReports/resources/css/trainning.css"/>
</head>

<body >

<script type="text/javascript" language="javascript"> 
<!-- 
var lastShowId = null;

function showRow(id) {
    //alert('show' + id); 
    if ( lastShowId != null ) {
        if (lastShowId != id ) {
            hideRow(lastShowId);
        }
    }
    var item = document.getElementById(id);
    var element = item.style;
    element.display == 'none' ? element.display = 'block' :
    element.display='none';
    lastShowId = id;
} 
function hideRow(hid) { 
    //alert('hide:' +hid);
    var item = document.getElementById(hid);
    var element = item.style;
    //alert(element.display);
    element.display = 'none';
} 

--> 
</script>
<%!
    public String getBuffer(int level) {
        String buffer = "";
        for ( int i = 1; i < level; i ++ ) {
            buffer = buffer + "&nbsp;&nbsp;&nbsp;&nbsp;";
        }
        return buffer;    
    }
%>


<div id="wrap2">
<%
    for (Iterator it = wc.getCurrent().iterator(); it.hasNext();) {
        Map currMap = (Map)it.next();
        int lvl  = ((BigDecimal)currMap.get("LEVEL")).toBigInteger().intValue();
        String color="";
        String code = (String)currMap.get("ACTIVITY_CODE");
        if (Util.isEmpty(code)) {
            System.out.println("hello empty");
            continue;
        }        
        
        String name = getBuffer(lvl) + currMap.get("ACTIVITYNAME");
        if (lvl == 1) {
            int id = ((BigDecimal)currMap.get("ACTIVITY_PK")).toBigInteger().intValue();
%>

<table class="no_space_width" width="100%">
<tr>
    <td rowspan="2">&nbsp;&nbsp;</td><td>&nbsp;&nbsp;</td><td rowspan="2">&nbsp;&nbsp;</td>
</tr>
<tr>
<td>
Current
            <table CLASS="blue_table" width="90%">
            <tr onclick="showRow('id_<%=id%>')">
                <td bgcolor="#eff7fc" width="150"><%=Util.toEmpty(code)%></td>
                <td bgcolor="#eff7fc"><%=Util.toEmpty(name)%></td>
            </tr>
            </table>
            <table   CLASS="blue_table" style="display:none;" width="90%" id="id_<%=id%>">
<%            
        } else {
%>
            <tr>
                <td bgcolor="#ffffff" width="150"><%=Util.toEmpty(code)%></td>
                <td bgcolor="#ffffff"><%=Util.toEmpty(name)%></td>
            </tr>

<%        }
%>
<%  }   %>

</table>

<form class="form_basic" method="post" action="/TrainingReports/adminHome/editNodeLaunchMeeting?type=<%=wc.getType()%>&id=<%=wc.getPhase().getTrackPhaseId()%>">
<table />
<tr>
    <td colspan="2">
        Name
    </td>

    <td colspan="2">
        Code
    </td>
</tr>
<tr>
<td>
    <input class="text" type="text" name="<%=ActivitySearchWc.FIELD_ACTIVITY_NAME%>" value="<%=wc.getActivityname()%>">
</td>
<td>
Or
</td>
<td> 
    <input class="text" type="text" name="<%=ActivitySearchWc.FIELD_CODE%>" value="<%=wc.getCode()%>">
</td>
<td>
    <input type="submit" value="Search">
</td>
</table>
</form>
Click on blue row to show child activities.
<table class="basic_table" width="100%">
<%
    int lastLvl = 0;
    for (Iterator it = wc.getSearchResults().iterator(); it.hasNext();) {
        Map currMap = (Map)it.next();
        int lvl  = ((BigDecimal)currMap.get("LEVEL")).toBigInteger().intValue();
        String color="";
        String code = (String)currMap.get("ACTIVITY_CODE");
        if (Util.isEmpty(code)) {
            System.out.println("hello empty");
            continue;
        }
        String name = getBuffer(lvl) + currMap.get("ACTIVITYNAME");
        if (lvl == 1) {
            int id = ((BigDecimal)currMap.get("ACTIVITY_PK")).toBigInteger().intValue();
            if (lastLvl == 1) { 
            %>
            <tr>
                <td>No activities found</td>
            </tr>
            <%}
%>
            </table>
            <form action="/TrainingReports/adminHome/editReportLaunchMeeting?type=<%=wc.getType()%>&track=<%=wc.getPhase().getTrackId()%>" method="post" onsubmit="this.submit();window.close();" target="main" >
            <table CLASS="blue_table" width="90%">
            <tr >
                <td width="50">
                    <input type="hidden" name="activitypk" value="<%=id%>">
                    <input type="hidden" name="id" value="<%=wc.getPhase().getTrackPhaseId()%>">
                    <input type="submit" value="Select" >
                </td>
                <td onclick="showRow('id_<%=id%>')" bgcolor="#eff7fc" width="150"><%=Util.toEmpty(code)%></td>
                <td onclick="showRow('id_<%=id%>')" bgcolor="#eff7fc"><%=Util.toEmpty(name)%></td>
            </tr>
            </table>
            </form>
            <table   CLASS="blue_table" style="display:none;" width="90%" id="id_<%=id%>">
<%            
        } else {
%>
            <tr>
                <td bgcolor="#ffffff" width="150"><%=Util.toEmpty(code)%></td>
                <td bgcolor="#ffffff"><%=Util.toEmpty(name)%></td>
            </tr>

<%      }
        lastLvl = lvl;
%>
<%  }   %>
    </table>

</td>
</tr>
</table>

</div>
</body>
</html>
