<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.pfizer.db.ManagementSummaryReport"%>
<%@ page import="com.pfizer.db.TrainingPathConfigBean"%>
<%@ page import="com.pfizer.hander.ManagementFilterHandler"%>
<%@ page import="com.pfizer.hander.TrainingPathHandler"%>
<%@ page import="com.pfizer.webapp.AppConst"%>
<%@ page import="com.pfizer.webapp.AppQueryStrings"%>
<%@ page import="com.pfizer.webapp.search.EmplSearchForm"%>
<%@ page import="com.pfizer.webapp.wc.components.EditManagementFilterCriteriaWc"%>
<%@ page import="com.pfizer.webapp.wc.components.admin.EditReportWc"%>
<%@ page import="com.pfizer.webapp.wc.components.TrainingPathAdminWc"%>
<%@ page import="com.pfizer.webapp.wc.components.search.SearchFormWc"%>
<%@ page import="com.pfizer.webapp.wc.templates.MainTemplateWpc"%>
<%@ page import="com.tgix.Utils.Util"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="java.util.Collection"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.HashMap"%>
<%@ page import="com.tgix.html.HtmlBuilder"%>

<style type="text/css">@import url(/TrainingReports/resources/js/jscalendar-1.0/calendar-blue.css);</style>
<script type="text/javascript" src="/TrainingReports/resources/js/jscalendar-1.0/calendar.js"></script>        
<script type="text/javascript" src="/TrainingReports/resources/js/jscalendar-1.0/lang/calendar-en.js"></script>
<script type="text/javascript" src="/TrainingReports/resources/js/jscalendar-1.0/calendar-setup.js"></script>
<script type="text/javascript" src="/TrainingReports/resources/js/jquery.js"></script>

<%
    TrainingPathAdminWc wc = (TrainingPathAdminWc)request.getAttribute(TrainingPathAdminWc.ATTRIBUTE_NAME);
    List resultList = wc.getReportList();
  //  int configid =  wc.getConfigId();
    TrainingPathConfigBean track = wc.getTrack();
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
myW.location =  "/TrainingReports/adminHome/searchCourseTrainingPath";
} 
    
// Added instead onblur function
function displayvals() {


	var a = [];
	var b = [];
	var i;
    
    var selObj = document.getElementById('courseCodes');
    var selObjAlias = document.getElementById('courseAlias');
    
    for (i = 0; i < selObj.options.length; i++) {
     //   alert(selObj.options[i].value);
        selObj.options[i].selected = true;
        selObjAlias.options[i].selected = true;
    }

	var selectedValueSorg = null;
	var selectedIndexSorg = null;
    
    var selectedValuebu = null;
	var selectedIndexbu = null;
    
    var selectedValueRoleCd = null;
	var selectedIndexRoleCd = null;
    
    var selectedValueCourseCode = null;
	var selectedIndexCourseCode = null;
    
    var selectedValueCourseAlias = null;
	var selectedIndexCourseAlias = null; 
    var selectedIndexCourseAliasCode = null;  
    
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
    
    $('#courseAlias :selected').each(function(i, selected) {
			b[i] = $(selected).text();
			a[i] = $(selected).val();
			//alert(a[i]);
			if (selectedValueCourseAlias == null) {
				selectedValueCourseAlias = b[i];
				selectedIndexCourseAlias = a[i];
			} else {
				selectedValueCourseAlias = selectedValueCourseAlias + "," + b[i];
				selectedIndexCourseAlias = selectedIndexCourseAlias + "," + a[i];
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
    
     document.getElementById('trsOrg').value = selectedIndexSorg;
     document.getElementById('trBu').value = selectedIndexbu;
     document.getElementById('trRoles').value = selectedIndexRoleCd;
     document.getElementById('trCourses').value = selectedIndexCourseCode;
     document.getElementById('trAlias').value = selectedValueCourseAlias;
     document.getElementById('trAliasCode').value = selectedIndexCourseAlias;
     document.getElementById('trCourseNames').value = selectedValueCourseCode;
     
     
  //   alert(document.getElementById('trsOrg').value);
  //   document.getElementById('gender_hidden').value = selectedIndexGender;
    // alert("Course Codes:=="+document.getElementById('courses').value);
  //  alert("Course Codes:=="+document.getElementById('trCourseNames').value);
}


function goback() {
		//window.location="/TrainingReports/admin/user.do";
        document.getElementById('trackId').value = "";
        window.location="/TrainingReports/adminHome/trainingPath";
	}
 
function delConfig(id){
    var msg = "Do you want to continue ?";
    document.getElementById('delt').value = id;
 
    if ( confirm(msg) ) {        
       document.form1.submit();
      }
   // document.form1.submit();
}

function editConfig(id){
  //  alert("id in edit"+id);
    document.getElementById('edit').value = id;
 //   alert(document.getElementById('edit').value);
    document.forms[1].trackId.value = id;
    document.forms[1].submit();
}
 function updateAvailableCourses(selectIndex,selectedValue)
    {
         var selObj = document.getElementById('courseCodes');
         var duplicate = false;
         
         for (i = 0; i < selObj.options.length; i++) {
              if(selObj.options[i].value==selectIndex){
             alert(selectedValue+" has already been selected" );
             duplicate = true;
            }
          }  
         if(!duplicate){   
    	 $('#courseCodes').append(
		                 "<option selected value='" + selectIndex + "'>" + selectedValue
                        + "</option>");
         $('#courseAlias').append(
		                 "<option value='" + selectIndex + "'>" + selectedValue
                        + "</option>");               
        }
    }   
    
function deleteCourse(){
      var selectedArray = new Array();
        var selObj = document.getElementById('courseCodes');
        var selObjAlias = document.getElementById('courseAlias');
        
        var i;
        var j;
        var count = 0;
        
        for (i = 0; i < selObj.options.length; i++) {
            //alert('selObj.options.length='+selObj.options.length);
            $('#courseCodes :selected').each(function(j, selected) {
                var selectedValue = $(selected).val();
                
                if(selectedValue == selObj.options[i].value){
                            $(selObj.options[i]).remove();
                             $(selObjAlias.options[i]).remove();
                }
            } );
        }

}

function validate(){
    //alert('inside validate');
     var courselength=document.getElementById('trCourses').value; 
     var salesOrgLength=document.getElementById('trsOrg').value; 
     var buLength=document.getElementById('trBu').value; 
     var roleCodeLength=document.getElementById('trRoles').value; 

    
    if(buLength=="null"){
         //   alert(Please select atleast one Course Code);
            document.getElementById("msg").innerHTML='Please select atleast one Business Unit.';
            document.getElementById("msgSelect").focus();
            return false;
    }
    if(salesOrgLength=="null"){
         //   alert(Please select atleast one Course Code);
            document.getElementById("msg").innerHTML='Please select atleast one Sales Organization.';
            document.getElementById("msgSelect").focus();
            return false;
    }  
    if(roleCodeLength=="null"){
         //   alert(Please select atleast one Course Code);
            document.getElementById("msg").innerHTML='Please select atleast one Role Code.';
            document.getElementById("msgSelect").focus();
            return false;
    }  
    if(courselength=="null"){
         //   alert(Please select atleast one Course Code);
            document.getElementById("msg").innerHTML='Please select atleast one Course Code.';
            document.getElementById("msgSelect").focus();
            return false;
    }
      
    return true;
}

function movecourseUp(){
    var ls = document.getElementById('courseCodes');
    var alias  = document.getElementById('courseAlias');
                var el; 
          if(ls.selectedIndex == -1)
          //alert('Please select an Item to move up.');
          return false;
        else{
          if(ls.selectedIndex == 0)
           {
               // alert('First element cannot be moved up');
                return false;
           }
          else{
                var tempValue = ls.options[ls.selectedIndex].value;
                var tempIndex = ls.selectedIndex-1;
                ls.options[ls.selectedIndex].value = ls.options[ls.selectedIndex-1].value;
                ls.options[ls.selectedIndex-1].value = tempValue; 
                
                var tempText = ls.options[ls.selectedIndex].text;
                ls.options[ls.selectedIndex].text = ls.options[ls.selectedIndex-1].text;
                ls.options[ls.selectedIndex-1].text = tempText;
                               
                var tempValue2 = alias.options[ls.selectedIndex].value;
                var tempIndex2 = ls.selectedIndex-1;
                alias .options[ls.selectedIndex].value = alias.options[ls.selectedIndex-1].value;
                alias .options[ls.selectedIndex-1].value = tempValue2; 
                
                var tempText2 = alias.options[ls.selectedIndex].text;
                alias.options[ls.selectedIndex].text = alias.options[ls.selectedIndex-1].text;
                alias.options[ls.selectedIndex-1].text = tempText2;
                
                ls.selectedIndex = tempIndex; 
                alias.selectedIndex = tempIndex2; 
         }
       }        
}

function movedown(){
        var ls = document.getElementById('courseCodes');
        var alias = document.getElementById('courseAlias');
        
        if(ls.selectedIndex == -1)
         // alert('Please select an Item to move up.');
         return false;
        else{ 
            if(ls.selectedIndex == ls.options.length-1) 
                // alert('Last element cannot be moved down'); 
                 return false;
            else
            {  
                var tempValue = ls.options[ls.selectedIndex].value;
                var tempIndex = ls.selectedIndex+1;
                ls.options[ls.selectedIndex].value = ls.options[ls.selectedIndex+1].value;
                ls.options[ls.selectedIndex+1].value = tempValue; 
                
                var tempText = ls.options[ls.selectedIndex].text;
                ls.options[ls.selectedIndex].text = ls.options[ls.selectedIndex+1].text;
                ls.options[ls.selectedIndex+1].text = tempText;
                                
                var tempValue2 = alias.options[ls.selectedIndex].value;
                var tempIndex2 = ls.selectedIndex+1;
                alias.options[ls.selectedIndex].value = alias.options[ls.selectedIndex+1].value;
                alias.options[ls.selectedIndex+1].value = tempValue2; 
                
                var tempText2 = alias.options[ls.selectedIndex].text;
                alias.options[ls.selectedIndex].text = alias.options[ls.selectedIndex+1].text;
                alias.options[ls.selectedIndex+1].text = tempText2;
                
                ls.selectedIndex = tempIndex;
                alias.selectedIndex = tempIndex2;
                
            }
       } 
} 

function changeAlias(selectedElement)
{
    var selectedOption=selectedElement.options[selectedElement.selectedIndex];
    var alias = prompt("Please edit the Alias selected :",selectedOption.text);
    if (alias!=null && alias!=""){
        selectedOption.text = alias;
    }
}
</script>

<%

   
    AppQueryStrings qString = new AppQueryStrings();
 
    List allSalesOrg = track.getAllSalesOrg();
    List allBusinessUnits = track.getAllBusinessUnit();
    List allRoleCodes = track.getAllRoleCodes();
  //  List allCourseCodes = track.getAllCourseCodes();
    
    List buList = new ArrayList();
    List salesOrgList = new ArrayList();
    List roleList = new ArrayList();
    List courseList = new ArrayList();
    List aliasList = new ArrayList();
        
    salesOrgList = wc.getSalesList();
    buList = wc.getBuList();
    roleList = wc.getRoleList();
    HashMap CourseNames = new HashMap();
    CourseNames = wc.getCourseCodeMap();
    HashMap CourseAlias = new HashMap();
    CourseAlias = wc.getCourseAliasMap();
    courseList = wc.getCourseList();
    System.out.println("courseList in jsp page=="+courseList);
    
    aliasList = wc.getCourseAliasList();
       System.out.println("aliasList in jsp page=="+aliasList);
    
    List configIdList = wc.getConfigIdList(); 
    
    TrainingPathConfigBean tPathConfig = wc.getTrainingPath();
    
    List trainingPathConfigurations = new ArrayList();
    trainingPathConfigurations = wc.getTrainingPathConfigurationList();
   
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
            <img src="<%=AppConst.IMAGE_DIR%>/spacer.gif" height="5">
        </td>
    </tr>
  
    <tr>
    <td>
        <div class="breadcrumb">
           <a href="/TrainingReports/reportselect" style="margin-left:0px;">Home</a> / 
           Training Path Configuration
        </div>
    </td>
    </tr>
    </table>
  
        <table width="100%">
            <tr>
            <td>
                <div class="breadcrumb">
                  <center><b> Training Path Configuration</b></center>
                </div>
            </td>
            </tr>
        </table>
 <table class="blue_table_without_border" align="center">
        <tr>
            <td><a id="msgSelect" href=""></a><span id="msg" style="color:red;font-size:13px;"></span></td>
        </tr>
</table> 
    
   
   <br>
   
    <table width="80%" align="center" class="blue_table">
    
    <form name="TrainingPathConfig" id="TrainingPathConfig" action="/TrainingReports/adminHome/trainingPath" method="post" onsubmit="">
        
      <tr>
      <th>Business Unit<font color="red">&nbsp;*</font></th>
      <th>Sales Organization<font color="red">&nbsp;*</font></th>
      <th>Role Codes<font color="red">&nbsp;*</font></th>
      <th>Course Code Alias<font color="red">&nbsp;*</font></th>
      <th>Course Codes<font color="red">&nbsp;*</font></th>
      <th></th>
      
      </tr>
      
        
      <tr >
        
            
        
        <td><select style="width:225px;height:120px" id="BUnit" name="BUnit" size="5" multiple ><!-- onblur="javascript:selectMultipleOptions(this.id,'bu');"> -->
            <%=HtmlBuilder.getMultipleOptionalFromLabelValue(track.getAllBusinessUnitList(),buList)%>
        </select></td>
        
              
        
        <td><select style="width:260px;height:120px" id="SalesOrg" name="SalesOrg" size="5" multiple> <!-- onblur="javascript:selectMultipleOptions(this.id,'sOrg');"> -->
            <%=HtmlBuilder.getMultipleOptionalFromLabelValue(track.getAllSalesOrgList(),salesOrgList)%>
        </select>
        </td>
           
        
        <td><select style="width:210px;height:120px" id="roleCodes" name="roleCodes" size="5" multiple><!-- onblur="javascript:selectMultipleOptions(this.id,'roles');"> -->
            <%=HtmlBuilder.getMultipleOptionalFromLabelValue(track.getAllRoleList(),roleList)%>
        </select></td>
        <td>
       <div style="width:300px;height:140px;overflow:auto;"> 
        <!--<div style="width:500px;height:100px;overflow:auto;"-->
        <select style="width:290px;height:130px" id="courseAlias" name="courseAlias" size="5" multiple onchange="changeAlias(this);"> 
            <%= wc.getOptionalFieldsFromMap(courseList,CourseAlias)%>
        </select>
       </div><input type="hidden" name="test" id="test"> 
        </td>
        <td>
       <div style="width:300px;height:140px;overflow:auto;"> 
        <!--<div style="width:500px;height:100px;overflow:auto;"-->
        <select style="width:290px;height:130px" id="courseCodes" name="courseCodes" size="5" multiple> <!--onblur="javascript:selectMultipleOptions(this.id,'courses');"> -->
            <%//System.out.println("Course List"+courseList+"Size=="+CourseNames.size());//HtmlBuilder.getMultipleOptionalFromLabelValue(track.getAllCourseList(),courseList)%>
            <%= wc.getOptionalFieldsFromMap(courseList,CourseNames)%>
        </select>
       </div><input type="hidden" name="test" id="test"> 
        </td>
        
        <td>
        <input type="button" style="width:100px;height:25px;font-size:90%" name="addCourses" value="Search Course" onclick='openP2lWindow(); return false;'/>
        <input type="button" style="width:70px;height:25px;font-size:90%" name="moveUp" value="MoveUp" onclick="javascript:movecourseUp();"/>
        <input type="button" style="width:70px;height:25px;font-size:90%" name="moveDown" value="MoveDown" onclick="javascript:movedown();"/>
        <input type="button" style="width:70px;height:25px;font-size:90%" name="delCourses" value="Remove" onclick='deleteCourse();'/>
        </td>
        </tr> 
        
     <table width="30%" class="blue_table_without_border" align="center"><tr><td>
        <input type="submit" style="" name="savenewpath" id="savenewpath" value="Save Training Path" onclick="javascript:displayvals();return validate();"/>
     </td>
     <td>
        <input type="button" style="width:100px;height:25px;font-size:100%" name="cancel" value="Cancel" onclick='javascript:goback();'/>
     </td></tr></table>      
        
       
        
     
            <input type="hidden" name="trsOrg" id="trsOrg" value="">
            <input type="hidden" name="trBu" id="trBu" value="">
            <input type="hidden" name="trRoles" id="trRoles" value="">
            <input type="hidden" name="trCourses" id="trCourses" value="">
            
            <input type="hidden" name="trCourseNames" id="trCourseNames" value="">
            
            <input type="hidden" name="trAlias" id="trAlias" value="">
            <input type="hidden" name="trAliasCode" id="trAliasCode" value="">
            
            
            <input type="hidden" name="trackId" id="trackId" value="<%=wc.getTrackId()%>">
            
            <input type="hidden" id="formerrors" value="">
            <%System.out.println("Track Id in Training paths page in form 0=="+wc.getTrackId());%>
      
        
        </form>

    </table>
   
        
    </td>
</tr>

</table>

 <form name="form1" id="form1" action ="/TrainingReports/adminHome/trainingPath" method = "post"  >

<table class="no_space_width"  height="0%">
   <tr>
    <td>
        <div class="breadcrumb">
          &nbsp;&nbsp;&nbsp; Training Paths:
        </div>
    </td>
    </tr>
</table>

 
<table class="blue_table_without_border" align="center">
        <tr>
            <td><span id="msg" style="color:red;"><%=wc.getErrorMsg()%></span></td>
        </tr>
       
</table>
<table cellspacing="0" id="tsr_table" width="90%" class="blue_table">
  
    
    <tr>
        <!-- <th nowrap>Config Id</th> -->
        <th nowrap width="10%">Buisness Unit</th>
        <th nowrap>Sales Organization</th>
        <th nowrap width="20%">Role Description</th>
        <th nowrap>Course Codes</th>
        <th nowrap>Course Code Alias</th>
        <th nowrap width="13%">Action</th>
        
    </tr>
        <%  boolean oddEvenFlag=false;
              %>
              <%for(int count=0;count<trainingPathConfigurations.size();count++){
             tPathConfig = (TrainingPathConfigBean)trainingPathConfigurations.get(count); 
        //      System.out.println("-----------------------------------------------Start For"); %>
                <tr class="<%=oddEvenFlag?"even":"odd"%>">
                 <%--   <td valign="top">
                         <div> <%=//configIdList.get(count)%> </div>
                    </td> --%>
                    <%%>
                    <td valign="top">
                      <% 
                        Collection c = tPathConfig.getBuDescList().values();
                        Iterator iter = c.iterator();
             //           System.out.println(" ---------------getBuDescList"+c.size());
                        while (iter.hasNext())
                        {
                            String o = tPathConfig.getBuDescList().get(iter.next().toString()).toString();
                         //   System.out.println(o);
                            %>
                           <div> <%=o.toString()%></div>
                       <%}%>
                    </td>
                    
                    <td valign="top">
                       <% 
                        c = tPathConfig.getSalesDescList().values();
                        iter = c.iterator();
                    //     System.out.println(" ---------------getSalesDescList");
                        while (iter.hasNext())
                        {
                            Object o = iter.next();
                     //       System.out.println(o.toString()); %>
                            <div><%=o.toString()%></div>
                      <%}%>
                    </td>
                    
                    <td valign="top">
                       <% 
                        c = tPathConfig.getRoleDescList().values();
                        iter = c.iterator();
                     //    System.out.println(" ---------------getRoleDescList");
                        while (iter.hasNext())
                        {
                            Object o = iter.next();
                    //        System.out.println(o.toString()); %>
                            <div><%=o.toString()%></div>
                      <%}%>
                   </td>
                    
                    <td valign="top">
                       <% 
                        c = tPathConfig.getCourseDescList().values();
                       iter = c.iterator();
                    //     System.out.println(" ---------------getCourseDescList");
                        while (iter.hasNext())
                        {
                            Object o = iter.next();
                    //        System.out.println(o.toString()); %>
                            <div><%=o.toString()%></div>
                      <%}%>
                    </td>
                    
                    <td valign="top">
                       <% 
                        c = tPathConfig.getCourseAliasList().values();
                       iter = c.iterator();
                    //     System.out.println(" ---------------getCourseDescList");
                        while (iter.hasNext())
                        {
                            Object o = iter.next();
                    //        System.out.println(o.toString()); %>
                            <div><%=o.toString()%></div>
                      <%}%>
                    </td>
                    
                    <td><input type="button" value="Edit" name="EditConfig" onclick="editConfig(<%=configIdList.get(count)%>);">
                    <input type="button" name="delete" value="Delete" onclick="delConfig(<%=configIdList.get(count)%>);" /> 
                    
                     
                </tr>
           <%-- //System.out.println("-----------------------------------------------End for"); --%>
           <%   }%>
        
</table> 
   
         <input type="hidden" name="delt" id="delt" value="">
        <input type="hidden" name="edit" id="edit" value="">
       <input type="hidden" name="trackId" id="trackId" value="<%=wc.getTrackId()%>">
       <%System.out.println("Track Id in Training paths page in form 1=="+wc.getTrackId());%>
</form>
        

