<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.pfizer.db.NAUserSearch"%>
<%@ page import="com.pfizer.processor.OverallResult"%>
<%@ page import="com.pfizer.webapp.AppConst"%>
<%@ page import="com.pfizer.webapp.wc.components.search.NASearchResultListWc"%>
<%@ page import="com.tgix.Utils.Util"%>
<%@ page import="java.util.List"%>
<%@ page import="java.awt.GraphicsEnvironment"%>
<%@ page import="com.pfizer.webapp.AppQueryStrings"%>
<%@ taglib uri="/WEB-INF/dynamic-include.tld" prefix="inc" %>

<%
AppQueryStrings app=new AppQueryStrings();
	NASearchResultListWc wc = (NASearchResultListWc)request.getAttribute(NASearchResultListWc.ATTRIBUTE_NAME);
	List ret = wc.getResults();
    System.out.println("RESULT SET IN NASEARCHUSERLIST"+ret.size());
    String SelectedName;
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

 
</script>

</head>
<body>	
<form name="emailSelectForm" >
<!-- <input name="" type="image" src="/TrainingReports/resources/images/btn_complete.gif" style="margin-top:20px;" onclick="TrainingReports/admin/editgroup.do">-->
<table cellspacing="0" id="tsr_table" width="100%" class="blue_table">

    <tr>
        <th nowrap>First Name</th>
        <th nowrap>Last Name</th>
        <th nowrap>NT ID</th>    
        <th nowrap>Email ID</th>
        <th nowrap>Select</th>
     </tr>
    <% if ( ret != null ) { 
        
              
         %> 
        <%  boolean oddEvenFlag=false;
            for (int i=0; i < wc.getResults().size(); i++) {
              oddEvenFlag = !oddEvenFlag; 
              NAUserSearch curr = (NAUserSearch)wc.getResults().get(i); 
              SelectedName = curr.toString();
              System.out.println("!!!!!!!!@@@@@@@@@@@###########$$$$$$$$$$$$$"+SelectedName);
              
        %>
                <tr class="<%=oddEvenFlag?"even":"odd"%>">
                    <td><%=Util.toEmpty(curr.getFirstName())%></td>
                    <td><%=Util.toEmpty(curr.getLastName())%> </td>
                    <td><%=Util.toEmpty(curr.getNtacct())%> </td>
                    <td><%=Util.toEmpty(curr.getEmailid())%></td>
                    <td><input name="SelectedField" value ="<%//=SelectedName%>" type="checkbox" id="selectedvalue"></td>
                    
                </tr>
        
        <%
        
         } 
        
         } %>
     

</table>
</form>
</body>
</html>

</table>