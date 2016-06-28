<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.pfizer.db.ManagementSummaryReport"%>
<%@ page import="com.pfizer.hander.ManagementFilterHandler"%>
<%@ page import="com.pfizer.webapp.AppConst"%>
<%@ page import="com.pfizer.webapp.AppQueryStrings"%>
<%@ page import="com.pfizer.webapp.wc.components.EditManagementFilterCriteriaWc"%>
<%@ page import="com.pfizer.webapp.wc.components.admin.EditReportWc"%>
<%@ page import="com.tgix.html.HtmlBuilder"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.Map"%>
<%@ page import="java.util.HashMap"%>
<%@ page import="java.util.Iterator"%>

<style type="text/css">@import url(/TrainingReports/resources/js/jscalendar-1.0/calendar-blue.css);</style>
<script type="text/javascript" src="/TrainingReports/resources/js/jscalendar-1.0/calendar.js"></script>        
<script type="text/javascript" src="/TrainingReports/resources/js/jscalendar-1.0/lang/calendar-en.js"></script>
<script type="text/javascript" src="/TrainingReports/resources/js/jscalendar-1.0/calendar-setup.js"></script>
<script type="text/javascript" src="/TrainingReports/resources/js/jquery.js"></script>

<%
 EditManagementFilterCriteriaWc wc = (EditManagementFilterCriteriaWc)request.getAttribute(EditManagementFilterCriteriaWc.ATTRIBUTE_NAME);
    ManagementSummaryReport track = wc.getTrack();
   
%>
    
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
myW.location =  "/TrainingReports/adminHome/searchCourseManagement?track=<%=wc.getTrack().getTrackId()%>";
} 
    
// Added instead onblur function
function displayvals() {

	var a = [];
	var b = [];
	var i;

	var selectedValueSorg = null;
	var selectedIndexSorg = null;
    
    var selectedValuebu = null;
	var selectedIndexbu = null;
    
    var selectedValueRoleCd = null;
	var selectedIndexRoleCd = null;
    
    var selectedValueCourseCode = null;
	var selectedIndexCourseCode = null;
    
    var selectedValueGender = null;
	var selectedIndexGender = null;
    
	$('#SalesOrg :selected').each(function(i, selected) {
			
			b[i] = $(selected).text();
			a[i] = $(selected).val();
			//alert(a[i]);
			if (selectedValueSorg == null) {
				selectedValueSorg = b[i];
				selectedIndexSorg = a[i];
			} else {
				selectedValueSorg = selectedValueSorg + "," + b[i];
				selectedIndexSorg = selectedIndexSorg + "," + a[i];
			}

	} );
    	$('#BUnit :selected').each(function(i, selected) {
			
			b[i] = $(selected).text();
			a[i] = $(selected).val();
			//alert(a[i]);
			if (selectedValuebu == null) {
				selectedValuebu = b[i];
				selectedIndexbu = a[i];
			} else {
				selectedValuebu = selectedValuebu + "," + b[i];
				selectedIndexbu = selectedIndexbu + "," + a[i];
			}

	} );
    	$('#roleCodes :selected').each(function(i, selected) {
			
			b[i] = $(selected).text();
			a[i] = $(selected).val();
			//alert(a[i]);
			if (selectedValueRoleCd == null) {
				selectedValueRoleCd = b[i];
				selectedIndexRoleCd = a[i];
			} else {
				selectedValueRoleCd = selectedValueRoleCd + "," + b[i];
				selectedIndexRoleCd = selectedIndexRoleCd + "," + a[i];
			}

	} );
    	$('#courseCodes :selected').each(function(i, selected) {
			
			b[i] = $(selected).text();
			a[i] = $(selected).val();
			//alert(a[i]);
			if (selectedValueCourseCode == null) {
				selectedValueCourseCode = b[i];
				selectedIndexCourseCode = a[i];
			} else {
				selectedValueCourseCode = selectedValueCourseCode + "," + b[i];
				selectedIndexCourseCode = selectedIndexCourseCode + "," + a[i];
			}

	} );
    
    $('#Gender :selected').each(function(i, selected) {
			
			b[i] = $(selected).text();
			a[i] = $(selected).val();
			//alert(a[i]);
			if (selectedValueGender == null) {
				selectedValueGender = b[i];
				selectedIndexGender = a[i];
			} else {
				selectedValueGender = selectedValueGender + "," + b[i];
				selectedIndexGender = selectedIndexGender + "," + a[i];
			}

	} );
    
     document.getElementById('sOrg').value = selectedIndexSorg;
     document.getElementById('bu').value = selectedIndexbu;
     document.getElementById('roles').value = selectedIndexRoleCd;
     document.getElementById('courses').value = selectedIndexCourseCode;
     document.getElementById('gender_hidden').value = selectedIndexGender;
    // alert("Course Codes:=="+document.getElementById('courses').value);
}


function goback() {
		//window.location="/TrainingReports/admin/user.do";
        window.location="/TrainingReports/adminHome/editMenu?id=5000&name=Reports";
	}
 
function checkDuplicate()
{
var duplicate  = false;
var none=0;
document.getElementById("formerrors").value = "";
	for (i=1;i<7;i++)
	{
		duplicate = false;
		for(j=1;j<7;j++)
		{
			if ((i != j) && (document.getElementById("Groupby"+i).value!="" || document.getElementById("Groupby"+j).value!="" ) && (document.getElementById("Groupby"+i).value == document.getElementById("Groupby"+j).value ))
			{
				duplicate = true;
				document.getElementById("formerrors").value = "true";
			}
		}
        if(document.getElementById("Groupby"+i).value=="")
             none++;
		if (duplicate)	{
            document.getElementById("Groupby"+ i + "Msg").innerHTML = "<font color=red>Duplicate</font>";
          //  document.getElementById("msg").focus();
          return false;
            }
        else
             document.getElementById("Groupby"+ i + "Msg").innerHTML = "";
 	}
    if(none==6) 
    {
        document.getElementById("Groupby1Msg").innerHTML = "<font color=red>Select atleast one 'group by' option </font>";
                           document.getElementById("formerrors").value = "true";
        return false;
    }
    return true;
}

function submitDocument()
{
    if(validate()){
    checkDuplicate();
	if (document.getElementById("formerrors").value == "")
    {
            document.getElementById("msg").innerHTML = "";
    	return true;
	}
    else 
        {
            
            document.getElementById("msg").innerHTML = "Please check the errors in the form before submitting";
            document.getElementById("msgSelect").focus();
            return false;
        }
       
    }
    else {
       // alert("validate=false");
        return false;
    }
}

 function updateAvailableCourses(selectIndex,selectedValue)
    {
    	 $('#courseCodes').append(
		                 "<option selected value='" + selectIndex + "'>" + selectedValue
                        + "</option>");
    }   
    
function deleteCourse(){
      var selectedArray = new Array();
        var selObj = document.getElementById('courseCodes');
        var i;
        var j;
        var count = 0;
        
        for (i = 0; i < selObj.options.length; i++) {
            //alert('selObj.options.length='+selObj.options.length);
            $('#courseCodes :selected').each(function(j, selected) {
                var selectedValue = $(selected).val();
                
                if(selectedValue == selObj.options[i].value){
                            $(selObj.options[i]).remove();
                }
            } );
        }

}

function clearDates(sid){
    var hStartDate=document.getElementById(sid);
    document.getElementById(sid).value="";
   
}
function validate()
{
     var hstart=document.ManagementReport.HStartDate.value;
     var hend=document.ManagementReport.HEndDate.value;
     var tstart=document.ManagementReport.TStartDate.value;
     var tend=document.ManagementReport.TEndDate.value;
     var rstart=document.ManagementReport.RStartDate.value;
     var rend=document.ManagementReport.REndDate.value;
     
     if(hstart=="" || hend=="" || tstart=="" || tend=="" || rstart=="" || rend==""){
        document.getElementById("msg").innerHTML='All Date fields are mandatory.';
        document.getElementById("msgSelect").focus();
         return false;
     }
     if((hstart!="" && hend!="") || (tstart!="" && tend!="") || (rstart!="" && rend!="")){
            var hsDate=new Date(hstart);
            var heDate=new Date(hend);
            var tsDate=new Date(tstart);
            var teDate=new Date(tend);
            var rsDate=new Date(rstart);
            var reDate=new Date(rend); 
            if(hsDate>heDate || tsDate>teDate || rsDate>reDate){
                document.getElementById("msg").innerHTML='Start Date cannot be greater than End date';
                document.getElementById("msgSelect").focus();
                return false;
            }
          }
    var courselength=document.getElementById('courses').value;   
    if(courselength=="null"){
         //   alert(Please select atleast one Course Code);
            document.getElementById("msg").innerHTML='Please select atleast one Course Code.';
            document.getElementById("msgSelect").focus();
            return false;
    }
    return true;
}
</script>
<%

   
    AppQueryStrings qString = new AppQueryStrings();
     
     
   // For getting all salesorg, rolecodes.etc from database 
    List allSalesOrg = track.getAllSalesOrg();
    List allBusinessUnits = track.getAllBusinessUnit();
    List allRoleCodes = track.getAllRoleCodes();
    List allCourseCodes = track.getAllCourseCodes();
    HashMap CourseNames = new HashMap();
    CourseNames = wc.getCourseCodeMap();
    

    ArrayList buList = new ArrayList();
    ArrayList salesOrgList = new ArrayList();
    ArrayList roleList = new ArrayList();
    ArrayList courseList = new ArrayList();
    ArrayList gender = new ArrayList();
    //Getting data from management_filter_criteria- comma separated values
    String salesOrg = track.getSalesOrg();
    String businessUnit = track.getBusinessUnit();
    String roleCode = track.getRoleCode();
    String courseCode = track.getCourseCode();
    String selGender = track.getGender();
    

    String[] arr=null;
    String[] arr1 = null;
    String[] arr2 = null;
    String[] arr3 =null;
    String[] arr4 = null;
    
  if( businessUnit != null ){
   //if(!wc.getFirstTime()) {
       arr = businessUnit.split(",");
        for(int j=0;j<arr.length;j++){
                    buList.add(arr[j]);
                }
               
  }
     if( salesOrg !=null  ){
        arr1 = salesOrg.split(",");
        for(int j=0;j<arr1.length;j++){
                    salesOrgList.add(arr1[j]);
                  
                }
     }
      if(roleCode != null  ){
     arr2 = roleCode.split(",");
     for(int j=0;j<arr2.length;j++){
                    roleList.add(arr2[j]);
                   
                }
      }
     if( courseCode!=null ){
         arr3 = courseCode.split(","); 
         for(int j=0;j<arr3.length;j++){
                    courseList.add(arr3[j]);
                  
                }
     }
     if( selGender!=null ){
        arr4 = selGender.split(",");
        for(int j=0;j<arr4.length;j++){
                    gender.add(arr4[j]);
                  
                }
     }
// end of addition
    List mFilter = track.getManagementFilter();
      
%>


<table class="basic_table">
<tr>
    <td rowspan="2">&nbsp;&nbsp;</td>
    <td>&nbsp;</td>
    <td rowspan="2">&nbsp;&nbsp;</td>
</tr>
<tr>
    <td>
   
    <table class="no_space_width"  height="0%"> 
    <tr>
        <td>
            <img src="<%=AppConst.IMAGE_DIR%>/spacer.gif" width="5">
        </td>
        <td colspan="=2">
            <img src="<%=AppConst.IMAGE_DIR%>/spacer.gif" height="25">
        </td>
    </tr>
    <tr>
        <td>
            <img src="<%=AppConst.IMAGE_DIR%>/spacer.gif" width="5">
        </td>
        <td>
        	<div class="breadcrumb">
                <a href="/TrainingReports/reportselect" style="margin-left:0px;">Home</a> / 
                <a href="/TrainingReports/adminHome/editMenu?name=Reports&id=<%=wc.getMenu().getId()%>"> <%=wc.getMenu().getLabel()%> </a> /
                <%=track.getTrackLabel()%>
        	</div>
        </td>
	</tr>
    </table>
    <table class="blue_table_without_border">
    <tr><td><b>Instructions</b></td></tr>
    <tr><td>1. Please select the filter criteria to configure Management Summary Reports.</td></tr> 
    <tr><td>2. Hire Start Date, Hire Completion Date, Training Completion Start Date, Training Completion End Date, Training Registration Start Date, Training Registration End Date fields are mandatory.</td></tr>
    <tr><td>3. * indicates mandatory fields.</td></tr>
    <tr><td>4. Please select atleast one Course code.</td></tr>
    </table>    
    
    <table ><tr><td><label class="basic_label">Report Name : </label><label><%=track.getTrackLabel()%></label></td></tr></table>
   <br>
   <table class="blue_table_without_border" align="center">
        <tr>
            <td><a id="msgSelect" href=""></a><span id="msg" style="color:red;font-size:15px;"></span></td>
        </tr>
</table>
    <table width="80%" align="center" class="blue_table">
    
    <form name="ManagementReport" id="ManagementReport" action="/TrainingReports/adminHome/editManagementFilterCriteria?track=<%=track.getTrackId()%>" method="post" onsubmit="">
        
        <tr><th colspan="2" align="left">Management Filter Criteria</th>
        
        </tr>
        
       <tr><td width="40%" >Sales Organization</td>
        <td width="60%"><select style="width:300px;height:130px" id="SalesOrg" name="SalesOrg" size="5" multiple> <!-- onblur="javascript:selectMultipleOptions(this.id,'sOrg');"> -->
            <%=HtmlBuilder.getMultipleOptionalFromLabelValue(track.getAllSalesOrgList(),salesOrgList)%>
        </select>
        </td></tr>
        
        <tr><td style="width:50%">Gender</td>
            <td><select style="width:300px;height:50px" id= "Gender" name="Gender" size="2" multiple> <!-- onblur="javascript:selectMultipleOptions(this.id,'gender');"> -->
            <%=HtmlBuilder.getMultipleOptionalFromLabelValue(track.getAllGender(),gender)%>
            </select></td>
        </tr>  
        
        <tr><td width="40%">Hire Start Date <font color="red">*</font></td>
        <td width="40%"><input class="text" type="text" name="HStartDate" id="HStartDate" value="<%=track.getHireStartDate()==null?"":track.getHireStartDate()%>" readonly > <!-- onchange="checkHireStartDate();"> -->
         <input type=image src="/TrainingReports/resources/images/calendar.jpg" id="hfromDate_Id"  border="0" height="17" width="20" alt="Select From Date" value="Change Date" name="clearHireDate" >
                <script type="text/javascript">
                                Calendar.setup({
                                    inputField     :    "HStartDate",
                                    ifFormat       :    "%m/%d/%Y",
                                    button         :    "hfromDate_Id"                         
                                });
                               
                            </script><img src="/TrainingReports/resources/images/cancel.gif" alt="Clear Hire Start Date" onmouseover="this.style.cursor='hand'" onclick="clearDates('HStartDate');">
        </td></tr>
        <tr><td width="40%">Hire End Date<font color="red">*</font></td>
        <td><input class="text" type="text" name="HEndDate" id="HEndDate" value="<%=track.getHireEndDate()==null?"":track.getHireEndDate()%>" readonly > <!-- onchange="checkHireEndDate();">  -->
        <input type=image src="/TrainingReports/resources/images/calendar.jpg" id="htoDate_Id"  border="0" height="17" width="20" alt="Select To Date" value="Change Date" name="changeDate" >
                <script type="text/javascript">
                                Calendar.setup({
                                    inputField     :    "HEndDate",
                                    ifFormat       :    "%m/%d/%Y",
                                    button         :    "htoDate_Id"                         
                                });
                               
                            </script><img src="/TrainingReports/resources/images/cancel.gif" alt="Clear Hire End Date" onmouseover="this.style.cursor='hand'" onclick="clearDates('HEndDate');">
                            <!-- <input type="button" style="width:90px;height:25px;font-size:80%" value="Reset Hire Dates"> <!--onclick="clearDates('HEndDate','HStartDate');" -->
        </td></tr>
        
        
        <tr><td width="60%">Training Completion Start Date<font color="red">*</font></td>
        <td width="40%"><input class="text" type="text" name="TStartDate" value="<%=track.getTrainingCompletionStartdate()==null?"":track.getTrainingCompletionStartdate()%>" readonly>
        <input type=image src="/TrainingReports/resources/images/calendar.jpg" id="tcfromDate_Id"  border="0" height="17" width="20" alt="Select From Date" value="Change Date" name="changeDate" >
                <script type="text/javascript">
                                Calendar.setup({
                                    inputField     :    "TStartDate",
                                    ifFormat       :    "%m/%d/%Y",
                                    button         :    "tcfromDate_Id"                         
                                });
                               
                            </script><img src="/TrainingReports/resources/images/cancel.gif" alt="Clear Completion Start Date" onmouseover="this.style.cursor='hand'" onclick="clearDates('TStartDate');">
        </td></tr>
        <tr><td>Training Completion End Date<font color="red"> * </font></td>
        <td><input class="text" type="text" name="TEndDate" value="<%=track.getTrainingCompletionEndDate()==null?"":track.getTrainingCompletionEndDate()%>" readonly>
        <input type=image src="/TrainingReports/resources/images/calendar.jpg" id="tctoDate_Id"  border="0" height="17" width="20" alt="Select To Date" value="Change Date" name="changeDate" >
                <script type="text/javascript">
                                Calendar.setup({
                                    inputField     :    "TEndDate",
                                    ifFormat       :    "%m/%d/%Y",
                                    button         :    "tctoDate_Id"                         
                                });
                               
                            </script><img src="/TrainingReports/resources/images/cancel.gif" alt="Clear Completion End Date" onmouseover="this.style.cursor='hand'" onclick="clearDates('TEndDate');">
                            <!--<input type="button" style="width:120px;height:25px;font-size:80%" value="Reset Completion Dates"><!-- onclick="clearDates('TStartDate','TEndDate');"> -->
        </td></tr>
        
        
        
        <tr><td>Training Registration Start Date<font color="red">*</font></td>
        <td><input class="text" type="text" name="RStartDate" value="<%=track.getTrainingRegistrationStartDate()==null?"":track.getTrainingRegistrationStartDate()%>" readonly>
        <input type=image src="/TrainingReports/resources/images/calendar.jpg" id="trfromDate_Id"  border="0" height="17" width="20" alt="Select From Date" value="Change Date" name="changeDate" >
                <script type="text/javascript">
                                Calendar.setup({
                                    inputField     :    "RStartDate",
                                    ifFormat       :    "%m/%d/%Y",
                                    button         :    "trfromDate_Id"                         
                                });
                               
                            </script><img src="/TrainingReports/resources/images/cancel.gif" alt="Clear Registration Start Date" onmouseover="this.style.cursor='hand'" onclick="clearDates('RStartDate');">
        </td></tr>
        <tr><td>Training Registration End Date<font color="red"> * </font></td>
        <td><input class="text" type="text" name="REndDate" value="<%=track.getTrainingRegistrationEndDate()==null?"":track.getTrainingRegistrationEndDate()%>" readonly>
        <input type=image src="/TrainingReports/resources/images/calendar.jpg" id="trtoDate_Id"  border="0" height="17" width="20" alt="Select To Date" value="Change Date" name="changeDate" >
                <script type="text/javascript">
                                Calendar.setup({
                                    inputField     :    "REndDate",
                                    ifFormat       :    "%m/%d/%Y",
                                    button         :    "trtoDate_Id"                         
                                });
                               
                            </script><img src="/TrainingReports/resources/images/cancel.gif" alt="Clear Registration End Date" onmouseover="this.style.cursor='hand'" onclick="clearDates('REndDate');">
                            <!-- <input type="button" style="width:120px;height:25px;font-size:80%" value="Reset Registration Dates" onclick="clearDates('RStartDate','REndDate');"> -->
        </td></tr>
        
        <tr><td>Business Unit</td>
        <td><select style="width:300px;height:100px" id="BUnit" name="BUnit" size="5" multiple ><!-- onblur="javascript:selectMultipleOptions(this.id,'bu');"> -->
            <%=HtmlBuilder.getMultipleOptionalFromLabelValue(track.getAllBusinessUnitList(),buList)%>
        </select></td>
        </tr>    
        
       <tr><td>Role Codes</td>
        <td><select style="width:300px;height:150px" id="roleCodes" name="roleCodes" size="5" multiple><!-- onblur="javascript:selectMultipleOptions(this.id,'roles');"> -->
            <%=HtmlBuilder.getMultipleOptionalFromLabelValue(track.getAllRoleList(),roleList)%>
        </select></td>
        
        </tr>   
      <tr><td>Course Codes<font color="red"> * </font></td>
        <td><select style="width:350px;height:150px" id="courseCodes" name="courseCodes" size="5" multiple> <!--onblur="javascript:selectMultipleOptions(this.id,'courses');"> -->
            <%System.out.println("Course List"+courseList+"Size=="+CourseNames.size());//HtmlBuilder.getMultipleOptionalFromLabelValue(track.getAllCourseList(),courseList)%>
            <%= wc.getOptionalFieldsFromMap(courseList,CourseNames)%>
        </select>
        <input type="button" style="width:90px;height:25px;font-size:90%" name="addCourses" value="Search Course" onclick='openP2lWindow(); return false;'/>
        <input type="button" style="width:70px;height:25px;font-size:90%" name="delCourses" value="Delete" onclick='deleteCourse();'/></td>
        </tr> 
        
        <tr><td>Select First Group by</td>
        <td><select style="width=70%" name="Groupby1" id="Groupby1" onChange='checkDuplicate();'>
        <%=HtmlBuilder.getOptionsFromLabelValue(track.getGroupByLabelValueList(),track.getGroupBy1())%>
        </select><div id='Groupby1Msg'></div></td></tr>
        
        
        <tr><td>Select Second Group by</td>
        
         <td><select style="width=70%" name="Groupby2" id="Groupby2" onChange='checkDuplicate();'>
        <%=HtmlBuilder.getOptionsFromLabelValue(track.getGroupByLabelValueList(),track.getGroupBy2())%>
        </select><div id='Groupby2Msg'></div></td></tr>
        
        
        <tr><td>Select Third Group by</td>
     
        <td><select style="width=70%" name="Groupby3" id="Groupby3" onChange='checkDuplicate();'>
        <%=HtmlBuilder.getOptionsFromLabelValue(track.getGroupByLabelValueList(),track.getGroupBy3())%>
        </select><div id='Groupby3Msg'></div></td></tr>
        
        
        <tr><td>Select Fourth Group by</td>
      
        <td><select style="width=70%" name="Groupby4" id="Groupby4" onChange='checkDuplicate();'>
        <%=HtmlBuilder.getOptionsFromLabelValue(track.getGroupByLabelValueList(),track.getGroupBy4())%>
        </select><div id='Groupby4Msg'></div></td></tr>
        
        
        <tr><td>Select Fifth Group by</td>
      
        <td><select style="width=70%" name="Groupby5" id="Groupby5" onChange='checkDuplicate();'>
        <%=HtmlBuilder.getOptionsFromLabelValue(track.getGroupByLabelValueList(),track.getGroupBy5())%>
        </select><div id='Groupby5Msg'></div></td></tr>
        
        <tr><td>Select Sixth Group by</td>
      
        <td><select style="width=70%" name="Groupby6" id="Groupby6" onChange='checkDuplicate();'>
        <%=HtmlBuilder.getOptionsFromLabelValue(track.getGroupByLabelValueList(),track.getGroupBy6())%>
        </select><div id='Groupby6Msg'></div></td></tr>
        
            
            <input type="hidden" name="sOrg" id="sOrg" value="">
            <input type="hidden" name="bu" id="bu" value="">
            <input type="hidden" name="roles" id="roles" value="">
            <input type="hidden" name="courses" id="courses" value="">

            <input type="hidden" name="gender_hidden" id="gender_hidden" value=""> 
            <input type="hidden" name="trackID" value="<%=track.getTrackId()%>">
            <input type="hidden" id="formerrors" value="">
      
      <table align="center">
        <tr>
        <td colspan="2" align="center">
        <input type="submit" id="save" name="save" value="Save" onclick="javascript:displayvals();return submitDocument();" >
<!--        <input type="submit" id="process" name="process" value="Delete">  -->
        <input type="submit" value=" Cancel " onclick="goback(); return false;">
        </td> 
        </tr>
        </table>
        
        
        </form>

    </table>
    </td>
</tr>

</table>
   <%=wc.getErrorMsg()%> 
<table class="blue_table_without_border" align="center">
        <tr>
            <td><span id="msg" style="color:red;"></span></td>
        </tr>
       
</table>
