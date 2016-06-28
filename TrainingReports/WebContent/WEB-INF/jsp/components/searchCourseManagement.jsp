<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.pfizer.db.P2lTrack"%>
<%@ page import="com.pfizer.db.P2lTrackPhase"%>
<%@ page import="com.pfizer.webapp.wc.components.EditMenuWc"%>
<%@ page import="com.pfizer.webapp.wc.components.search.CourseSearchWc"%>
<%@ page import="com.pfizer.webapp.wc.components.admin.GapAnalysisAdminWc"%>
<%@ page import="com.pfizer.webapp.wc.components.searchCourseManagementWc"%>
<%@ page import="com.tgix.Utils.Util"%>
<%@ page import="java.math.BigDecimal"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.Hashtable"%>
<%@ page import="java.util.Enumeration"%>
<%@ page import="java.util.Vector"%>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.HashMap" %>

<%            
	searchCourseManagementWc wc = (searchCourseManagementWc)request.getAttribute(searchCourseManagementWc.ATTRIBUTE_NAME);
 //   System.out.println("Size:" + wc.getSearchResults().size());
//    System.out.println(wc.getTrackId());
%>
<html>
<head>
		<link rel="stylesheet" type="text/css" href="/TrainingReports/resources/css/header.css"/>
	
		<link rel="stylesheet" type="text/css" href="/TrainingReports/resources/css/trainning.css"/>
</head>

<body >
<script type="text/javascript" language="javascript"> 
 
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

 var arr0 = new Array();
 var arr1 = new Array();
    
function selectedActivityManagement(){
        var idSelected= new Array();
        var valueSelected= new Array();
        var j = 0;
        
            for(var i=0;i<document.form1.length;++i) { 
               if(document.form1.elements[i].type=='checkbox') {
                    if (document.form1.elements[i].checked) {
                        idSelected[j] = document.form1.elements[i].id;
                        valueSelected[j] = document.form1.elements[i].value;
                        if (("SelectAll1" != idSelected[j]) && ("SelectAll2" != idSelected[j]))
                        window.opener.updateAvailableCourses(idSelected[j],valueSelected[j]);
                        j++;
                    }
                }
             }
          document.form1.activitySelectedIdList.value = idSelected;
          document.form1.activitySelectedValueList.value = valueSelected;
          window.close();
    }
    
function selectAll(selectAllCheckBox){
   // alert("checkbox" + document.getElementById(selectAllCheckBox).checked);
    if (document.getElementById(selectAllCheckBox).checked == true)
    {
        for(var i=0;i<document.form1.length;i++) {
            if(document.form1.elements[i].type=='checkbox') {
                document.form1.elements[i].checked =true;
            }
        }
    }else
    {
     for(var i=0;i<document.form1.length;i++) {
            if(document.form1.elements[i].type=='checkbox') {
                document.form1.elements[i].checked =false;
            }
        }
    }
       
    }    
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
          //  System.out.println("hello empty");
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
            <table   class="blue_table" style="display:none;" width="90%" id="id_<%=id%>">
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

<form class="form_basic" method="post" action="/TrainingReports/adminHome/searchCourseManagement?track=<%=wc.getTrackId()%>">
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
    <input class="text" type="text" name="<%=searchCourseManagementWc.FIELD_ACTIVITY_NAME%>" value="<%=wc.getActivityname()%>">
</td>
<td>
Or
</td>
<td> 
    <input class="text" type="text" name="<%=searchCourseManagementWc.FIELD_CODE%>" value="<%=wc.getCode()%>">
</td>
<td> <% String selectedProduct = request.getParameter("product");
System.out.println("-------   product name = " + selectedProduct);
                    if (null != selectedProduct) {%> 
                    <input type="hidden" name="product" value="<%=selectedProduct%>">
                    <%}%>
    <input type="submit" value="Search">
</td>
</tr>
</table>
</form>
Click on blue row to show child activities.

<form name="form1" action="/TrainingReports/adminHome/editManagementFilterCriteria?trackID=<%=wc.getTrackId()%>" method="post" onsubmit="this.submit();window.close();" target="main" >
    <%if(wc.getSearchResults()!=null&&wc.getSearchResults().size()>0){%>
    <table class="blue_table_without_border">
       <tr><td><b>&nbsp;&nbsp;Instructions :</b></td></tr>
       <tr><td>&nbsp;&nbsp;1.&nbsp;Select the checkboxes to select courses.</td></tr>
       <tr><td>&nbsp;&nbsp;2.&nbsp;Click on "Insert Selected" button to add the selected courses.</td></tr>
       </table>
       <br>
    
    <table class="blue_table" width="90%">
        <tr><th width="100"><input type ="checkbox" id ="SelectAll1" name="SelectAll" onclick="javascript:selectAll('SelectAll1');" >Select All</th>
        <th width="150"><input type ="button" id ="Select" name="Select" value="Insert Selected" onclick="javascript:selectedActivityManagement();"> <!-- onclick="selectedActivity();"> --></th>
        <th >&nbsp;</th>
    </tr>

    </table>

<%}%>


<table class="basic_table" width="100%">
<%
    int lastLvl = 0;
    for (Iterator it = wc.getSearchResults().iterator(); it.hasNext();) {
        Map currMap = (Map)it.next();
        int lvl  = ((BigDecimal)currMap.get("LEVEL")).toBigInteger().intValue();
        String color="";
        String code = (String)currMap.get("ACTIVITY_CODE");
        //String activityName = ((BigDecimal)currMap.get("activity_pk")).toBigInteger().intValue();
       // System.out.println("Activity Name + Pavan"+activityName);
        if (Util.isEmpty(code)) {
           // System.out.println("hello empty");
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
            
            <table CLASS="blue_table" width="90%">
            
            <tr >
                <td width="50">
                    <input type="hidden" name="activitypk" value="<%=id%>">
                    <input type="hidden" name="code" value="<%=Util.toEmpty(code)%>">
                    <% 
                    if (null != selectedProduct) {%> 
                    <input type="hidden" name="product" value="<%=selectedProduct%>">
                    <%}%>
                 <!--    <input type="submit" value="Select" >  -->
                 <input type="checkbox" id="<%=((BigDecimal)currMap.get("ACTIVITY_PK")).toBigInteger().intValue()%>" value="<%=currMap.get("ACTIVITYNAME")%>">
                </td>
                <td onclick="showRow('id_<%=id%>')" bgcolor="#eff7fc" width="150"><%=Util.toEmpty(code)%></td>
                <td onclick="showRow('id_<%=id%>')" bgcolor="#eff7fc"><%=Util.toEmpty(name)%></td>
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

<%      }
        lastLvl = lvl;
%>
<%  }   %>
    </table>
    <%if(wc.getSearchResults()!=null&&wc.getSearchResults().size()>0){%>
        <table class="blue_table" width="90%">
        <tr><th width="100"><input type ="checkbox" id ="SelectAll2" name="SelectAll2" onclick="javascript:selectAll('SelectAll2');" >Select All</th>
        <th width="150"><input type ="button" id ="Select" name="Select" value="Insert Selected" onclick="javascript:selectedActivityManagement();"></th>
        <th>&nbsp;</th>
        </tr>
        </table>
    <%}%>
    
    <input type="hidden" id="activitySelectedIdList" name="activitySelectedIdList" value="">
       <input type="hidden" id="activitySelectedValueList" name="activitySelectedValueList" value="">
</form>
</td>
</tr>
</table>

</div>
</body>
</html>
