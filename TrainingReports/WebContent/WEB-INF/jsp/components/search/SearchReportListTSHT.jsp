<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.pfizer.db.EmpSearchTSHT"%>
<%@ page import="com.pfizer.processor.OverallResult"%>
<%@ page import="com.pfizer.webapp.AppConst"%>
<%@ page import="com.pfizer.webapp.wc.components.search.SearchResultListTSHTWc"%>
<%@ page import="com.tgix.Utils.Util"%>
<%@ page import="java.awt.GraphicsEnvironment"%>
<%@ taglib uri="/WEB-INF/dynamic-include.tld" prefix="inc" %>
<%@ page import="java.util.List"%>
<%@ page import="java.util.Iterator"%>

<%
	SearchResultListTSHTWc wc = (SearchResultListTSHTWc)request.getAttribute(SearchResultListTSHTWc.ATTRIBUTE_NAME);
	List ret = wc.getResults();
%>
<br>
    <head>
        <title>
            Historical Training Server Transcripts Report 
        </title>        
    </head>
    
<script src="/TrainingReports/resources/js/sorttable.js"></script>
<LINK href="/TrainingReports/resources/css/trainning.css" type="text/css" rel="STYLESHEET">

<script type="text/javascript" language="JavaScript">
addEvent(window, "load", sortables_init);


function sortables_init() {
    // Find all tables with class sortable and make them sortable
    if (!document.getElementsByTagName) return;
    tbls = document.getElementsByTagName("table");
    for (ti=0;ti<tbls.length;ti++) {
        thisTbl = tbls[ti];
        if (((' '+thisTbl.id+' ').indexOf("employee_table") != -1) && (thisTbl.id)) {
            //initTable(thisTbl.id);
            ts_makeSortable(thisTbl);
        }
    }
}
</script>    
<table cellspacing="0" id="employee_table" width="90%" class="employee_table" style="margin-left:10px;margin-right:10px">
<%	    
    String requestFromSearch=request.getParameter("fromSearch")==null?"":request.getParameter("fromSearch");
    if(requestFromSearch.equalsIgnoreCase("true")){
        %>
        <tr>
                <th>Last Name</th>
                <th>First Name</th>                
                <th>Emplid</th>
        </tr>
        <%
    }
    String colorClass="even";
    String tempId = "";    
    for(Iterator iter=ret.iterator();iter.hasNext();){        
    	EmpSearchTSHT emp = (EmpSearchTSHT)iter.next();
        
      
        if(colorClass.equalsIgnoreCase("even"))colorClass="odd";
            else
        if(colorClass.equalsIgnoreCase("odd"))colorClass="even";
        %>
        <tr class="<%=colorClass%>">
    <%            
        if(tempId.equalsIgnoreCase(emp.getEmplId())){%>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
    <%}else{%>  
    
            <td>
            <a href="<%=AppConst.APP_ROOT%>/TSHTReports/showTSHTDetail?emplid=<%=emp.getEmplId()%>">                                  
            <%=Util.toEmpty(emp.getLastName())%>
            </a>
            </td>
            <td><%=Util.toEmpty(emp.getFirstName())%></td>
            <td><%=Util.toEmpty(emp.getEmplId())%></td>    
    <%}%>                            
<%      
        tempId = emp.getEmplId();      
    }//end of FOR LOOP			
%>


</table>