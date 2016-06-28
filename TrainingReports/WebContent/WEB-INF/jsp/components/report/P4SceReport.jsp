<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.pfizer.webapp.AppConst"%>
<%@ page import="com.pfizer.webapp.wc.components.report.P4SceReportWc"%>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Map" %>

<%
	P4SceReportWc wc = (P4SceReportWc)request.getAttribute(P4SceReportWc.ATTRIBUTE_NAME);
%>
<script src="/TrainingReports/resources/js/sorttable.js"></script>


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
<script type="text/javascript" language="JavaScript">
    var mySecondList = new Array();
    var myObj;
    
            //mySecondList[0] = {'name':'all','date':'all%>'};

    <%  
        List dateClassMap = wc.getAllDateClassMap();
        int counter = 0;
        for (Iterator it = dateClassMap.iterator();it.hasNext();) {
            Map map = (Map)it.next();
    %>
            mySecondList[<%=counter%>] = {'name':'<%=map.get("exam_name")%>','date':'<%=map.get("date")%>'};
    <%
            counter = counter + 1;
        }
    %>    


    function getSecondList(fvalue, examname) {
        var myselect = document.getElementById("examname");
        var myl = myselect.length;
        
        for (x=0;x<myl;x++) {
            myselect.remove(0);
        }
        
        myselect.add(new Option("all","all"));
        var sidx = 0;
        for (x=0;x<mySecondList.length;x++) {
            if (fvalue == mySecondList[x].date || fvalue == 'all') {
                if (examname == mySecondList[x].name) {
                    var tet = new Option(mySecondList[x].name,mySecondList[x].name,true);
                    tet.selected  = true;
                    myselect.add(tet);
                    
                } else {
                    myselect.add(new Option(mySecondList[x].name,mySecondList[x].name));
                }
            }
        }   
    }
    
    function dropInit() {
       // alert('init');
        var datevalue  = '<%=wc.getSelectedDate()%>';
        var testvalue  = '<%=wc.getSelectedExam()%>';
        getSecondList(datevalue,testvalue);
    }
</script>

<% if (!wc.getExcelDownload()) {%>

<div style="margin-left:10px;margin-right:10px">

        <h3 style="MARGIN-TOP:15PX">
            P4 Training - SCE Report
        </h3>  
        
<!-- <form action="p4scereport.do" method="post"> -->
<form action="p4scereport" method="post">
<table>
<tr>
    <td align="left">Date</td>
    <td align="left">Exam Name</td>
</tr>
<tr>
<td>

<select  name="examdate" onchange="getSecondList(examdate.options[examdate.selectedIndex].value,'all')">
    <option value = 'all'>all</option>
    <%  
        List dateClassdates = wc.getAllDateClassMap();
        for (Iterator it = dateClassdates.iterator();it.hasNext();) {
            Map map = (Map)it.next();
            String selected = "";
            if (wc.getSelectedDate().equals(map.get("date"))) {
                selected = "selected";    
            }
    %>
            <option <%=selected%> value = '<%=map.get("date")%>'><%=map.get("date")%></option>
    <%
        }
    %>    

</select>
</td>
<td>
<select id="examname" name="examname" class="form_basic">
    <option value = 'all'>all</option>
</select>
<input type="submit">

</td>
</tr>
</table>
</form>

        <div style="margin-left:10px;margin-right:10px">
            <div id="table_inst">
              <%--   <img src="<%=AppConst.APP_ROOT%>/resources/images/excel.gif" width="15px">&nbsp;<a href="<%=AppConst.APP_ROOT%>/P4/p4scereport.do?downloadExcel=true&examdate=<%=wc.getSelectedDate()%>&examname=<%=wc.getSelectedExam()%>">Download to Excel</a>
                &nbsp;&nbsp;| <a href="<%=AppConst.APP_ROOT%>/reportselect.do">TRT Home</a> &nbsp;&nbsp;
           --%>
            <img src="<%=AppConst.APP_ROOT%>/resources/images/excel.gif" width="15px">&nbsp;<a href="<%=AppConst.APP_ROOT%>/P4/p4scereport?downloadExcel=true&examdate=<%=wc.getSelectedDate()%>&examname=<%=wc.getSelectedExam()%>">Download to Excel</a>
                &nbsp;&nbsp;| <a href="<%=AppConst.APP_ROOT%>/TrainingReports/reportselect">TRT Home</a> &nbsp;&nbsp;
          
          
            </div>            
        </div>
<%}%>
<table cellspacing="0" id="employee_table" width="90%" class="employee_table" style="margin-left:10px;margin-right:10px">
    <tr>
        <th>First Name</th>
        <th>Last Name</th>
        <th>Exam Name</th>
        <th>Date Taken</th>
        <th>Test Score</th>

    </tr>
    <%  
        List report = wc.getReportList();
        for (Iterator it = report.iterator();it.hasNext();) {
            Map map = (Map)it.next();
    %>
            <tr>
                <td><%=(String)map.get("first_name")%></td>
                <td><%=(String)map.get("last_name")%></td>
                <td><%=(String)map.get("exam_name")%></td>
                <td><%=(String)map.get("fdate")%></td>
                <td><%=(String)map.get("test_score")%></td>
            </tr>
    <%
        }
    %>    
</table>
