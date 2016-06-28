<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.pfizer.db.EmpSearchTSHT"%>
<%@ page import="com.pfizer.processor.OverallResult"%>
<%@ page import="com.pfizer.webapp.AppConst"%>
<%@ page import="com.pfizer.webapp.wc.components.search.SearchResultListTSHTDetailWc"%>
<%@ page import="com.tgix.Utils.Util"%>
<%@ page import="java.awt.GraphicsEnvironment"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.Iterator"%>

<%@ taglib uri="/WEB-INF/dynamic-include.tld" prefix="inc" %>

<%
	SearchResultListTSHTDetailWc wc = (SearchResultListTSHTDetailWc)request.getAttribute(SearchResultListTSHTDetailWc.ATTRIBUTE_NAME);
	List ret = wc.getResults();
    String emplid = "";
    for(Iterator iter=ret.iterator();iter.hasNext();){        
                EmpSearchTSHT emp = (EmpSearchTSHT)iter.next();  
                emplid= emp.getEmplId();
    }
%>
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

<%boolean bDownloadExcel = false;
  String sExcelDownload = (String)request.getParameter("downloadExcel");
  String sEmplid = (String)request.getParameter("emplid");
  if(sExcelDownload != null && sExcelDownload.equalsIgnoreCase("true"))
  {
    bDownloadExcel = true;
  }
%>

    <body>
    <div style="margin-left:10px;margin-right:10px">

<%if(bDownloadExcel == false){%>	  
<div style="margin-left:10px;margin-right:10px">
      <div id="table_inst">
		<img src="<%=AppConst.APP_ROOT%>/resources/images/excel.gif" width="15px">&nbsp;<a href="<%=AppConst.APP_ROOT%>/TSHTReports/showTSHTDetail?downloadExcel=true&emplid=<%=emplid%>">Download to Excel</a>        
        &nbsp;&nbsp;|&nbsp;&nbsp;
        <a href="javascript:history.go(-1)">Back to Search Page</a>
	  </div>	  
</div>     
<%}%>


<table cellspacing="0" id="employee_table" width="90%" class="employee_table" style="margin-left:10px;margin-right:10px">
    <%if(bDownloadExcel == true){%>
      <tr bgcolor="#1f61a9" >
                    <th nowrap>First Name</th>                
                    <th nowrap>Last Name</th>
                    <th nowrap>Emplid</th>
                    <th nowrap>Hire Date</th>
                    <th nowrap>Course Code</th>                                                
                    <th nowrap>Course Name</th>                
                    <th nowrap>Course Status</th>                       
                    <th nowrap>Completion Date</th>                
                    <th nowrap>Score</th>                
                    <th nowrap>Credits</th>                
                    <th nowrap>Notes</th>
                    <th nowrap>HR Status</th>
                    <th nowrap>Field Status</th>                
                    <th nowrap>Current Role</th>                                
            </tr>  
    <%}else{%>
      <tr>  
                    <th nowrap>First Name</th>                
                    <th nowrap>Last Name</th>
                    <th nowrap>Emplid</th>
                    <th nowrap>Hire Date</th>
                    <th nowrap>Course Code</th>                                                
                    <th nowrap>Course Name</th>                
                    <th nowrap>Course Status</th>                
                    <th nowrap>Completion Date</th>                
            </tr>  
    <%}%>  
    <%

    String colorClass="even";
    String tempId = "";    
    for(Iterator iter=ret.iterator();iter.hasNext();){        
    	EmpSearchTSHT emp = (EmpSearchTSHT)iter.next();
        
      
        if(colorClass.equalsIgnoreCase("even"))colorClass="odd";
            else
        if(colorClass.equalsIgnoreCase("odd"))colorClass="even";
        %>
        <tr class="<%=colorClass%>">
    
            <td><%=Util.toEmpty(emp.getFirstName())%></td>
            <td><%=Util.toEmpty(emp.getLastName())%></td>
            <td><%=Util.toEmpty(emp.getEmplId())%></td>
            <td><%=Util.formatDateLong(emp.getHireDate())%></td>
            <td><%=Util.toEmpty(emp.getCourceCode())%></td>                                                    
            <td><%=Util.toEmpty(emp.getCourceName())%></td>
            <td><%=Util.toEmpty(emp.getCourseStatus())%></td>                                                                                        
            <td><%=Util.formatDateLong(emp.getCompletionDate())%></td>            
            <%if(bDownloadExcel == true){%>            
                <td><%=Util.toEmpty(emp.getScores())%></td>
                <td><%=Util.toEmpty(emp.getCredits())%></td>                                        
                <td><%=Util.toEmpty(emp.getNotes())%></td>                        
                <td><%=Util.toEmpty(emp.getHrStatus())%></td>                                    
                <td><%=Util.toEmpty(emp.getFieldActive())%></td>                           
                <td><%=Util.toEmpty(emp.getRole())%></td>                                                                
            <%}%>
<%      
        tempId = emp.getEmplId();      
    }//end of FOR LOOP			
%>
</table>
</div>
</body>