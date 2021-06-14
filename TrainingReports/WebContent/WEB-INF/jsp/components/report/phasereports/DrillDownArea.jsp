<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.pfizer.webapp.AppConst"%>
<%@ page import="com.pfizer.webapp.wc.components.report.phasereports.DrillDownAreaWc"%>
<%@ page import="com.tgix.Utils.Util"%>
<%@ page import="com.tgix.printing.LoggerHelper"%>
<%@ page import="java.math.BigDecimal"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.Hashtable"%>
<%@ page import="java.util.Enumeration"%>
<%@ page import="java.util.Vector"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="java.util.Map"%>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<title>Implementing css and javascript</title>

<link rel="stylesheet" href="<%=request.getContextPath()%>/resources/css/jquery.treeview.css" type="text/css">
<script language="JavaScript" type="text/JavaScript" 
src="<%=AppConst.APP_ROOT%>/resources/js/jquery.js"></script>
<script language="JavaScript" type="text/JavaScript" 
src="<%=AppConst.APP_ROOT%>/resources/js/jquery.treeview.js"></script>
</head>



<%
	DrillDownAreaWc wc = (DrillDownAreaWc)request.getAttribute(DrillDownAreaWc.ATTRIBUTE_NAME);
    
%>

<script>
$(document).ready(function(){

	// first example
	$("#rootnode").treeview({
		persist: "location",
		collapsed: true,
		unique: true
	});

});
</script>
<!-- shindo edge width auto  -->
<% if((wc.getActivities().size() != 1))  { %>
<div  id="target" style="overflow: auto; width: auto; height: 300px;">
<div>
<UL id=rootnode>
<%LoggerHelper.logSystemDebug("jsp777" );%>
<%
    int prevLvl = 0;
    for (Iterator it = wc.getActivities().iterator(); it.hasNext();) {
        Map currMap = (Map)it.next();
        int lvl  = ((BigDecimal)currMap.get("LEVEL")).toBigInteger().intValue();
        String code = (String)currMap.get("ACTIVITY_CODE");   
        
        String start_date = (String)currMap.get("START_DATE");
        String end_date = (String)currMap.get("END_DATE");
        String name = (String)currMap.get("ACTIVITYNAME");
        if(start_date != null && end_date != null){        
        name = (String)currMap.get("ACTIVITYNAME")+" ("+Util.toEmpty(start_date)+ " - "+Util.toEmpty(end_date)+")";
        }
                
        int id = ((BigDecimal)currMap.get("ACTIVITY_PK")).toBigInteger().intValue();
	    String url ="listReportAllStatus?activitypk=" +  id ;
        if (lvl == 1 && prevLvl == 0) {
%>
		<LI><A HREF="<%=Util.toEmpty(url)%>"><%=Util.toEmpty(name)%></A>
<%
        } else if(lvl == prevLvl) {
%>
		</LI><LI style="align:left"><A HREF="<%=Util.toEmpty(url)%>"><%=Util.toEmpty(name)%></A>
<%
        } else if(lvl > prevLvl) {
%>
		<UL><LI><A HREF="<%=Util.toEmpty(url)%>"><%=Util.toEmpty(name)%></A>
<%
        } else if(lvl < prevLvl) {		
%>
</LI>
<%           while(prevLvl > lvl) {
%>
			</UL></LI>
<%	
			prevLvl--;
		}  
%>
		<LI><A HREF="<%=Util.toEmpty(url)%>"><%=Util.toEmpty(name)%></A>
<%
       } //end if
   	
	prevLvl = lvl;
} //end for

if (prevLvl > 0) {
%>
  </LI>
<%  while(prevLvl > 1) {
%>
     </UL></LI>
<%
     prevLvl--;
   }  
}
%>

</UL>
</div>
</div>
<% } else { %>
<div id="target" style="overflow: auto; width: auto; height: 400px;"></div>
<% } %>

<!-- shindo edge width auto  -->
