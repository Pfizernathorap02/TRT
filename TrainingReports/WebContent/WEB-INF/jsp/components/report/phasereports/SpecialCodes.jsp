<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.pfizer.webapp.AppConst"%>
<%@ page import="com.pfizer.webapp.user.User"%>
<%@ page import="com.pfizer.webapp.user.UserTerritory"%>
<%@ page import="com.pfizer.webapp.wc.components.report.phasereports.DetailPageWc"%>
<%@ page import="com.pfizer.webapp.wc.components.report.phasereports.SpecialCodesWc"%>
<%@ page import="com.tgix.Utils.Util"%>
<%@ page import="java.awt.GraphicsEnvironment"%>
<%@ page import="java.math.BigDecimal"%>
<%@ taglib uri="/WEB-INF/dynamic-include.tld" prefix="inc" %>
<%@ page import="java.util.List"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.Hashtable"%>
<%@ page import="java.util.Enumeration"%>
<%@ page import="java.util.Vector"%>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.HashMap" %>

<%
	SpecialCodesWc wc = (SpecialCodesWc)request.getAttribute(SpecialCodesWc.ATTRIBUTE_NAME);
    String sceUrl = "http://sce.pfizer.com/SCEWeb/evaluation/evaluateTR";
	
	String url = request.getRequestURL().toString();
	if ( url != null ) {
		url = url.toLowerCase();
	}
	if ( url != null 
		&& (url.indexOf("trt-stg.pfizer.com") > 0 || url.indexOf("tgix-dev.pfizer.com") > 0 )) {
		sceUrl = "http://sce-stg.pfizer.com/SCEWeb/evaluation/evaluateTR";	
	} 
    if ( url != null 
		&& (url.indexOf("trt-tst.pfizer.com") > 0 )) {
		sceUrl = "http://sce-tst.pfizer.com/SCEWeb/evaluation/evaluateTR";	
	}
    if ( url != null 
		&& (url.indexOf("localhost") > 0 )) {
		sceUrl = "http://localhost:7001/SCEWeb/evaluation/evaluateTR";	
	} 
    if ( url != null 
		&& (url.indexOf("trt-dev.pfizer.com") > 0)) {
		sceUrl = "http://sce-dev.pfizer.com/SCEWeb/evaluation/evaluateTR";	
	}
    //shannon for testing
    //sceUrl = "http://localhost:7001/SCEWeb/evaluation/evaluateTR";	
    //System.out.println("asdfadf" + wc.getUserSession().getUser());
    User user = wc.getUserSession().getUser();  
    String scoreFlag= user.getScoresFlag();
    System.out.println("scoreFlag in SPECIALCODES"+scoreFlag);
   /* if ( wc.getUserSession().isAdmin() ) {
        System.out.println("Inside if");
        user = wc.getUserSession().getOrignalUser();
    } */
%>
<html>
<head>
		<link rel="stylesheet" type="text/css" href="/TrainingReports/resources/css/header.css"/>
	
		<link rel="stylesheet" type="text/css" href="/TrainingReports/resources/css/trainning.css"/>
        <script type="text/javascript" language="JavaScript" src="/TrainingReports/resources/js/sorttable.js"></script>	

<script type="text/javascript" language="JavaScript">
addEvent(window, "load", sortables_init);


function sortables_init() {
    // Find all tables with class sortable and make them sortable
    if (!document.getElementsByTagName) return;
    tbls = document.getElementsByTagName("table");
    for (ti=0;ti<tbls.length;ti++) {
        thisTbl = tbls[ti];
        if (((' '+thisTbl.id+' ').indexOf("tsr_table") != -1) && (thisTbl.id)) {
            //initTable(thisTbl.id);
            ts_makeSortable(thisTbl);
        }
    }
}

function callSce()
{
    alert("Inside Call Sce");
    document.domain = 'pfizer.com';
    document.evaluateFormNew.submit();
}

</script>
</head>
<body>	
<table cellspacing="0" width="100%" >
<tr>
<td>&nbsp;&nbsp;&nbsp;&nbsp;
</td>
<td>
<table cellspacing="0" id="tsr_table" width="100%" class="blue_table">
    <tr>
        <th nowrap>Activity Name</th>
        <th nowrap>Date</th>
        <th nowrap>Score</th>
       </tr>  
<%  boolean oddEvenFlag=false;
    int total = 0;
    int totalCount = 0;
    for (Iterator it = wc.getResults().iterator(); it.hasNext(); ) { 
        oddEvenFlag = !oddEvenFlag; 
        Map tmp = (Map)it.next();
        BigDecimal num = (BigDecimal)tmp.get("SCORE");
        if ( num != null ) {
            total = total + num.intValue();
            totalCount++;
        }
        
%>
<tr class="<%=oddEvenFlag?"even":"odd"%>">
    <td>
        <%=Util.toEmpty((String)tmp.get("ACTIVITYNAME"))%>
    </td>
    <td>
        <%=Util.objectToString((java.util.Date)tmp.get("ENDDT"))%>
    </td>
     <%/* Condition added for P2L scores for CSO enhancement */
        if(scoreFlag.equals("Y")){%>
    <td>
        <%=Util.objectToString((BigDecimal)tmp.get("SCORE"))%> 
    </td>
    <%}else{%>
    <td> </td>
    <%}%>
    
</tr>
<% } %>

<%
    int average = 0;
    if (totalCount >0 ) {
        average = total/totalCount;
    }
%>
<tr>
    <td colspan="2" align="right">Average Score</td>
    <%/* Condition added for P2L scores for CSO enhancement */
    if(scoreFlag.equals("Y")){%>
    <td ><%=average%></td>
    <%}else{%>
     <td></td>
    <%}%>
</tr>
</table>


    <form name='evaluateFormNew' id='evaluateFormNew' method='post' action='<%=sceUrl%>' > 
        <input type='hidden' name='emplId' value='<%=wc.getEmplid()%>' id='emplId'/> 
        <input type='hidden' name='eventId' value='<%=wc.getType()%>' id='eventId'/> 
        <input type='hidden' name='product' value='<%=wc.getActivityName()%>' id='product'/> 
        <input type='hidden' name='productCode' value='<%=wc.getActivityid()%>' id='productCode'/> 
        <input type='hidden' name='evaluatorEmplId' value='<%=user.getId()%>' id='evaluatorEmplId'/>
        <input type='hidden' name='evaluatorRole' value='<%=user.getRole()%>' id='evaluatorRole'/>        
        <input type='hidden' name='examScore' value='<%=average%>' id='examScore'/>        
        <input type='hidden' name='action' value='create' id='action'/>
        <!--<input type= 'submit' name= 'submit'/> -->
        <a href="javascript:document.domain = 'pfizer.com'; document.evaluateFormNew.submit();"  type="submit" onclick="">Continue with evaluation</a>   
        &nbsp;&nbsp;&nbsp;&nbsp;
        <a href="javascript:window.close();" >Close Window</a>
        </form> 
        
       <!-- <a href="#" onclick="callSce();">Continue with evaluation</a> -->

   
</td>
<td>&nbsp;&nbsp;&nbsp;&nbsp;
</td>
</tr></table>