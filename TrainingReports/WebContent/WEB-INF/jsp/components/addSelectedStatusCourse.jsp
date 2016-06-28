<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.pfizer.db.ForecastReport"%>
<%-- <%@ taglib uri="netui-tags-databinding.tld" prefix="netui-data"%> --%>
<%-- <%@ taglib uri="netui-tags-html.tld" prefix="netui"%> --%>
<%-- <%@ taglib uri="netui-tags-template.tld" prefix="netui-template"%> --%>

<%@ page import="com.pfizer.webapp.wc.components.AddSelectedStatusCourseWc"%>
<%@ page import="com.pfizer.webapp.wc.components.EditReportForecastFilterWc"%>
<%@ page import="com.pfizer.webapp.wc.components.admin.EditReportWc"%>
<%-- <%@ taglib uri="netui-tags-html.tld" prefix="netui"%> --%>


<%            
	AddSelectedStatusCourseWc wc = (AddSelectedStatusCourseWc)request.getAttribute(AddSelectedStatusCourseWc.ATTRIBUTE_NAME);
    String status=(String)request.getParameter("status");
%>

<html>
    <head>
    <link rel="stylesheet" type="text/css" href="/TrainingReports/resources/css/header.css"/>
    <link rel="stylesheet" type="text/css" href="/TrainingReports/resources/css/trainning.css"/>
    <script type="text/javascript" src="/TrainingReports/resources/js/jquery.js"></script>
    <script type="text/javascript">
    var myW;
    function openP2lWindow() { 
        if (myW != null)
        {
    
            if (!myW.closed) {
            myW.focus();
            } 
    
        }
        window.name = "main";
        myW = window.open("","myW","height=500,width=600,scrollbars=yes,resizable=yes,statusbar=no,menubar=no,toolbar=no,dependent=yes");
        myW.location =  "/TrainingReports/adminHome/searchCourseForecast?status=<%=status%>";
    }
    
    var arr0=new Array();
    var arr1=new Array();
    
    function moveto() {

        var destObj = document.getElementById('selectedCourses');
        var length = 0;
        if (destObj != null) {
            length = destObj.options.length;
          //  alert("length = " + length);
        }
        if (length > 6) destObj.size=length+2;
        var a = $('#availableCourses').val() || [];
        var b = [];
        var i;
        var selectedValue;
        var selectedIndex;
        var OR = "OR";
        if($('#availableCourses :selected').val() != undefined && $('#availableCourses :selected').val() != ""){
    
        $('#availableCourses :selected').each(function(i, selected) {
            b[i] = $(selected).text();
            a[i] = $(selected).val();
            //alert(a[i]);
            if (selectedValue == null) {
                selectedValue = b[i];
                selectedIndex = a[i];
            } else {
                selectedValue = selectedValue + " AND " + b[i];
                selectedIndex = selectedIndex + " AND " + a[i];
            }
            //alert("selectedValue=" + selectedValue);
            //alert("selectedIndex=" + selectedIndex);
        });
    
        var selectedList = $('#availableCourses :selected').size();
        //alert("Size=" + selectedList);
        //alert("$('#selectedCourses').size()==" + $('#selectedCourses').size());
        //alert("$('#selectedCourses').length==" + $('#selectedCourses').length);
    
        if (length > 0) {
            $('#selectedCourses').append("<option value='" + OR + "'>" + OR + "</option>");
        }
        $('#selectedCourses').append(
                "<option value='" + selectedIndex + "'>" + selectedValue
                        + "</option>");
                        
        document.form1.idList.value = arr0;
        document.form1.valueList.value = arr1;
        }
    }
    
    
    function deleteCourses() {

        var selectedArray = new Array();
        var selObj = document.getElementById('selectedCourses');
        var i;
        var j;
        var count = 0;
        
        for (i = 0; i < selObj.options.length; i++) {
            //alert('selObj.options.length='+selObj.options.length);
            $('#selectedCourses :selected').each(function(j, selected) {
                var selectedValue = $(selected).val();
                //alert('selObj.options[i].value='+selObj.options[i].value);
                //alert('$(selected).val()='+$(selected).val());

                if(selectedValue == selObj.options[i].value){
                    //alert('inside');
                    if (selectedValue != 'OR') { // user cannot remove 'OR'
                    if (i == 0)
                        $(selObj.options[i + 1]).remove();//remove any trailing 'OR'
                    $(selObj.options[i]).remove();
                    if (i > 1)
                        $(selObj.options[i - 1]).remove();// remove any leading 'OR' 
                    }
                }
            } );
        }
    }

    
    function getCourses(){
        var selObj = document.getElementById('selectedCourses');
        var i;
        var count = 0;
        var selectedId = "";
        var selectedDesc = "";
        var andSelected;
        for (i = 0; i < selObj.options.length; i++) {
                //alert(selObj.options[i].value);
                var selectedValue = selObj.options[i].value;
    
                if (selectedValue != 'OR') {
                   // andSelected = "("+ selObj.options[i].value +")";
                    andSelected =  selObj.options[i].value ;
                    selectedId = selectedId + andSelected;
                    
                   // selectedDesc = selectedDesc + "("+ selObj.options[i].text +")";
                   selectedDesc = selectedDesc +  selObj.options[i].text ;
                    //alert("andSelected"+selectedId);
                } else {
                    selectedId = selectedId + ", OR ,";
                    selectedDesc = selectedDesc + ", OR ,";
                }
            }
             document.form1.idList.value = selectedId;
             document.form1.valueList.value = selectedDesc;
    }
    
    function updateAvailableCourses(selectIndex,selectedValue)
    {
    	 $('#availableCourses').append(
		                 "<option value='" + selectIndex +"'>" + selectedValue
                        + "</option>");
    }

  
    </script>
    </head>
    <body>
    
        <br>
        
        <br>
        <table class="blue_table_without_border">
            <tr><td><b>&nbsp;&nbsp;Instructions&nbsp; :</b></td></tr>
            <tr><td>&nbsp;&nbsp;1.&nbsp; Click on "Search" button to search for the courses.</td></tr>
            <tr><td>&nbsp;&nbsp;2.&nbsp; Repeat step-1 for searching multiple type of courses.</td></tr>
            <tr><td>&nbsp;&nbsp;3.&nbsp; Select a course in the "Available Courses" and click on "-->" button to populate the selection in the "Selected courses".</td></tr>
            <tr><td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;OR</td></tr>
            <tr><td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Select multiple courses holding the "ctrl" key and click on "-->" button to populate the selections in the "Selected Courses" with an <b>"AND"</b> between them.</td></tr>
            <tr><td>&nbsp;&nbsp;4.&nbsp; Repeating step-3 will put an <b>"OR"</b> between current selection and previous selection.</td></tr>
            <tr><td>&nbsp;&nbsp;5.&nbsp; Select row(s) in the "Selected courses" and click on "Delete" button to delete.</td></tr>
            <tr><td>&nbsp;&nbsp;6.&nbsp; Click on <b>"Back"</b> button for discarding the "Selected courses" criteria and  returning to the Filter Criteria Page.</td></tr>
            <tr><td>&nbsp;&nbsp;7.&nbsp; Click on <b>"Save"</b> button to populate the "Selected courses" criteria to the Filter Criteria Page.</td></tr>
        </table>
        <form  name="form1" id="form1" action="/TrainingReports/adminHome/editForecastFilterCriteria?status=<%=status%>&trackID=<%=session.getAttribute("trackID")%>&trackName=<%=session.getAttribute("trackName")%>&cancel=true" method="post">
        <table class="blue_table" width="80%" align="center">
        <tr >
        <TH colspan="3" align="left">Add <%=status%> Courses</TH></tr>
        <tr><td>Available Courses</td><td>&nbsp;</td><td>Selected Courses</td></tr>
        <tr>
        <td >
        <div >
        <select size="8" name="availableCourses" id="availableCourses" multiple style="width:400px" >
        <%
        ForecastReport editWc = new ForecastReport();
        
        String[] selectIdList=wc.getSelectIdList();
        String[] selectValueList=wc.getSelectValueList();
            if(selectIdList!=null && selectValueList!=null){
                for(int i=0;i<selectIdList.length;i++){
        %>
        <option value="<%=selectIdList[i]%>"><%=selectValueList[i]%>
        <%}}%>
        </select>
        </div>
        </td>
        <td>
        <input type="button" value="----->" onclick="moveto();">
        </td>
        <td>
        <div style="width:400px;height:150px;overflow:auto;">
        <%int count=editWc.getNumOptionalValFromMap(session,status);
             if (count==0){
        %>
        <select size="8" name="selectedCourses" id="selectedCourses" style="width:500px" multiple>
        <%} else {
            if (count < 8) count=8;%>
        <select size="<%=count%>" name="selectedCourses"  id="selectedCourses" multiple>
        <%}%>
        <%=editWc.getOptionalValFromMap(session,status)%>
        </select>
        </div>
        
            

        </td>
        </tr>
        <tr>
        <td>
        <input type="button" value="Search" onclick="openP2lWindow(); return false;" title="Click to search courses" onmouseover="this.style.cursor='hand';">
        
        </td>
        <td>
        </td>
        <td>
        <input type="submit" value="Save" onclick="getCourses();" title="Click to save selected courses" onmouseover="this.style.cursor='hand';">
        <input type="button" value="Delete" onclick="deleteCourses();" title="Click to delete selected course" onmouseover="this.style.cursor='hand';">
        <input type="button" value="Back" onclick="window.location='/TrainingReports/adminHome/editForecastFilterCriteria?trackID=<%=session.getAttribute("trackID")%>&trackName=<%=session.getAttribute("trackName")%>&cancel=true'" title="Click to go back" onmouseover="this.style.cursor='hand';">
        <input type="hidden" name="idList" id="idList" value="">
        <input type="hidden" name="valueList" id="valueList" value="">
        
        
        
        
        
        </td>
        </tr>
        </table>
        </form>
        
    
    
    </body>
</html>