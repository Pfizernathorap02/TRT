<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.pfizer.webapp.AppConst"%>
<%@ page import="com.pfizer.webapp.user.UserTerritory"%>
<%@ page import="com.pfizer.webapp.wc.components.report.phasereports.CourseCompletionResultWc"%>
<%@ page import="com.pfizer.webapp.wc.components.report.phasereports.CourseCompletionWc"%>
<%@ page import="com.pfizer.webapp.wc.components.report.phasereports.CourseSearchForm"%>
<%@ page import="com.pfizer.webapp.wc.components.report.phasereports.MainReportListWc"%>
<%@ page import="com.tgix.Utils.Util"%>
<%@ page import="java.awt.GraphicsEnvironment"%>
<%@ page import="java.math.BigDecimal"%>
<%@ page import="java.util.Map"%>
<%@ page import="java.util.Iterator"%>
<%@ taglib uri="/WEB-INF/dynamic-include.tld" prefix="inc" %>

<%
	CourseCompletionResultWc wc = (CourseCompletionResultWc)request.getAttribute(CourseCompletionResultWc.ATTRIBUTE_NAME);
    
%>
<% if ( !wc.isExcel() ) { %>

<script type="text/javascript" language="JavaScript">
function checkAll() {
	var myform = document.completeSelect;
	for(var i=0;i<myform.length;++i) { 
		if(myform.elements[i].type=='checkbox') { 
			myform.elements[i].checked = true; 
		} 
	}	
}
function unCheckAll() {
	var myform = document.completeSelect;
	for(var i=0;i<myform.length;++i) { 
		if(myform.elements[i].type=='checkbox') { 
			myform.elements[i].checked = false; 
		} 
	}	
}
function submitForm() {
    document.completeSelect.submit();
}
</script>


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

<% } %>

<% if (wc.getResult().size() > 0 ) { %>
<table class="no_space_width">
<tr>
<td>
<% if ( !wc.isExcel() ) { %>
<div>
	<div  style="float:left;">
        <%= wc.getResult().size() %> Trainees
	</div>
	<div class="top_table_buttons" style="float:right; align:bottom;">	
		<img src="<%=AppConst.APP_ROOT%>/resources/images/excel.gif" width="15px">&nbsp;<a href="/TrainingReports/phase/coursecomplete?downloadExcel=true&activitypk=<%=wc.getActivityId()%>">Download to Excel</a>&nbsp;&nbsp;&nbsp;&nbsp;
		<img src="<%=AppConst.IMAGE_DIR%>/training/b_checkall.gif"onclick="checkAll()" />
		<img src="<%=AppConst.IMAGE_DIR%>/training/b_uncheckall.gif" onclick="unCheckAll()" />
		<img src="/TrainingReports/resources/images/btn_complete.gif" onclick="submitForm()" />
	</div>
	<div class="clear"></div>	
</div>
<% } %>
</td>
</tr>
<tr>
<td>


<table cellspacing="0" id="tsr_table" width="100%" class="blue_table">

<form name="completeSelect" action="/TrainingReports/phase/coursecomplete?type=complete&activitypk=<%=wc.getActivityId()%>" method="post">
    
		<tr>
			<th width="112" align="left">Last Name</th> 
			<th width="113" align="left">First Name</th> 
			<th width="109" align="left">Role</th> 
			<th width="109" align="left">Sales Org</th> 
			<th width="209" align="left">Course ID</th>
			<th width="240" align="left">Course Name</th> 
			<th  align="center">Status</th> 
		  </tr>
          
        <%
            for (Iterator it = wc.getResult().iterator(); it.hasNext();) {
                Map item = (Map)it.next();
                
        %>          
                <tr>
                    <td align="left"><%=Util.toEmpty((String)item.get("LAST_NAME"))%></td> 
                    <td align="left"><%=Util.toEmpty((String)item.get("FIRST_NAME"))%></td> 
                    <td align="left"><%=Util.toEmpty((String)item.get("ROLE_CD"))%></td> 
                    <td align="left"><%=Util.toEmpty((String)item.get("SALES_GROUP"))%></td> 
                    <td align="left" id="yellowcell"><%=Util.toEmpty((String)item.get("CODE"))%></td>
                    <td align="left"><%=Util.toEmpty((String)item.get("ACTIVITYNAME"))%></td> 
                    
                    <% if ( !wc.isExcel() ) { %>
                        <% if ("N".equals((String)item.get("COMPLETE_STATUS"))) { %>
                        
                        <td><a href="/TrainingReports/phase/coursecomplete?emplid=<%=Util.toEmpty((String)item.get("EMPLID"))%>&type=uncomplete&activitypk=<%=Util.toEmpty(((BigDecimal)item.get("ACTIVITY_PK")).toString())%>">undo</a></td>                    
                        <% }  else if ("C".equals((String)item.get("COMPLETE_STATUS"))) { %>
                        <td>Locked</td>
                        <% }  else { %>
                        <td><input name="markedpeople" value="<%=Util.toEmpty(((BigDecimal)item.get("ACTIVITY_PK")).toString()) + ":" + Util.toEmpty((String)item.get("EMPLID"))%>"  type="checkbox">
                        <% } %>
                    <% } %>
                </tr>
        <% } %>
    </form>
    </table>
</td>
</tr>
</table>
<% } %>