<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.pfizer.db.GAProdCourse"%>
<%@ page import="com.pfizer.db.GAProduct"%>
<%@ page import="com.pfizer.webapp.AppConst"%>
<%@ page import="com.pfizer.webapp.wc.components.admin.GapAnalysisAdminWc"%>
<%@ page import="com.tgix.Utils.Util"%>
<%@ page import="com.tgix.html.HtmlBuilder"%>
<%@ taglib prefix="s" uri="/struts-tags"%> 

<%            
	GapAnalysisAdminWc wc = (GapAnalysisAdminWc)request.getAttribute(GapAnalysisAdminWc.ATTRIBUTE_NAME);
    GAProduct[] products = wc.getProducts();
    GAProdCourse[] pcMap = wc.getPcMap();
%>
<script type="text/javascript">
function displayCoursesPopup() {
/* 
 Infosys code changes starts here
 window.name = "searchAndSelectCourses.do"; */
window.name = "searchAndSelectCourses";
 // Infosys code changes ends here
var srcURL = "child.html";
// windows features
var winFeatures = "dialogHeight:300px; dialogLeft:200px;";
// The form id as param
var obj = courses;
window.showModalDialog(srcURL, obj, winFeatures);
}

function reloadPage(){
 /* Infosys migrated code weblogic to jboss changes start here */
 /* window.location = "editGapAnalysisReport.do";  */
 window.location = "editGapAnalysisReport";
/*  Infosys migrated code weblogic to jboss changes end here  */
}
var myW;
function openP2lWindow() { 
    if (myW != null)
        {
            if (!myW.closed) {
            myW.focus();
            } 
        }
    window.name = "main";
    var product = document.getElementById("product").value;
    var code = document.getElementById("code").value;
    myW = window.open("","myW","height=500,width=600,scrollbars=yes,resizable=yes,statusbar=no,menubar=no,toolbar=no,dependent=yes");
  /*   myW.location =  "/TrainingReports/adminHome/searchAndSelectCourses.do?product="+product+"&code="+code;
 */
 // Infosys Migrated code changes start
 
    myW.location =  "/TrainingReports/adminHome/searchAndSelectCourses?product="+product+"&code="+code;

 //Infosys Migrated code changes ends
 
 } 


function saveMapping() { 
var product = document.getElementById("product").value;
var code = document.getElementById("code").value;

var compRegChk = document.getElementById("addCourseCompRegChk").value;

//alert("compRegChk : "+compRegChk);
var compCheck="N";
var regCheck="N";
if(compRegChk=="Comp")
{
    compCheck="Y";
    regCheck="N";
}
if(compRegChk=="Reg"){
    compCheck="N";
    regCheck="Y";
}

//alert("compCheck: " +compCheck);
//alert("regCheck: " +regCheck);
if(compCheck=="N" && regCheck=="N")
{
    alert("Please select value for either Completion check or Registration check.");
    return false;
}
//return false;
/* window.location = "editGapAnalysisReport.do?type=Save&product="+product+"&code="+code+"&compCheck="+compCheck+"&regCheck="+regCheck;
 */
 //Infosys migrated code starts
window.location = "editGapAnalysisReport?type=Save&product="+product+"&code="+code+"&compCheck="+compCheck+"&regCheck="+regCheck;
//Infosys migrated code ends
 }

function setVal(str){
var value=str;
//alert("Selected check is : "+str);
document.getElementById("addCourseCompRegChk").value=str;
}


function saveProductCourseMapping(index)
{
var product = document.getElementsByName("pCode").item(index).value;
var code = document.getElementsByName("cCode").item(index).value;

var compCheck = document.getElementsByName("rCompletionCheck").item(index).value;
var regCheck = document.getElementsByName("rRegistrationCheck").item(index).value;

//alert("product: " +product);
//alert("code: " +code);

//alert("compCheck: " +compCheck);
//alert("regCheck: " +regCheck);

//return false;
// Infosys migrated code starts here
/* window.location = "editGapAnalysisReport.do?type=Save&product="+product+"&code="+code+"&compCheck="+compCheck+"&regCheck="+regCheck;
 */
 
window.location = "editGapAnalysisReport?type=Save&product="+product+"&code="+code+"&compCheck="+compCheck+"&regCheck="+regCheck;
// Infosys migrated code ends here
}


function removeGroup(index) { 
var product = document.getElementsByName("pCode").item(index).value;
var code = document.getElementsByName("cCode").item(index).value;

var compChk = document.getElementsByName("rCompletionCheck").item(index).value;
var RegChk = document.getElementsByName("rRegistrationCheck").item(index).value;

//alert("product: " +product);
//alert("code: " +code);

//alert("compChk: " +compChk);
//alert("RegChk: " +RegChk);


//return false;
// Infosys migrated code starts here
/* window.location = "editGapAnalysisReport.do?type=Remove&product="+product+"&code="+code+"&compChk="+compChk+"&RegChk="+RegChk;
 */

window.location = "editGapAnalysisReport?type=Remove&product="+product+"&code="+code+"&compChk="+compChk+"&RegChk="+RegChk;
//Infosys migrated code ends here
}


function setCompCheck(index){
var v_compChk = document.getElementsByName("rCompletionCheck").item(index).value;
var v_RegChk =  document.getElementsByName("rRegistrationCheck").item(index).value;

//alert("v_compChk: " +v_compChk);
//alert("v_RegChk: " +v_RegChk);

//alert("After changing values in setCompCheck...");

document.getElementsByName("rCompletionCheck").item(index).value="Y";
document.getElementsByName("rRegistrationCheck").item(index).value="N";

//alert("v_compChk: " +document.getElementsByName("rCompletionCheck").item(index).value);
//alert("v_RegChk: " +document.getElementsByName("rRegistrationCheck").item(index).value);


}

function setRegCheck(index){
var v_compChk = document.getElementsByName("rCompletionCheck").item(index).value;
var v_RegChk = document.getElementsByName("rRegistrationCheck").item(index).value;

//alert("v_compChk: " +v_compChk);
//alert("v_RegChk: " +v_RegChk);

//alert("After changing values in setRegCheck...");

document.getElementsByName("rCompletionCheck").item(index).value="N";
document.getElementsByName("rRegistrationCheck").item(index).value="Y";

//alert("v_compChk: " +document.getElementsByName("rCompletionCheck").item(index).value);
//alert("v_RegChk: " +document.getElementsByName("rRegistrationCheck").item(index).value);



}

</script>


<table class="no_space_width" width = "875">
<tr></tr>
<tr align="center"><td><%=wc.getMessage()%></td></tr>
<tr></tr>
<tr><td>
<table align="center" width="870">
<tr>
 <td>   
     <table align="center" width="870">
         <tr>
            <td>Product : <select id="product">
                <%=HtmlBuilder.getOptionsFromLabelValue(wc.getProductDisplayList(),wc.getSelectedProduct())%>		
                </select>
            </td>
            <td> <!â€”course listing with search button->
            Course Code : <input type="text" id="code" value="<%=wc.getCourseCode()%>" readonly/>
            <input type="button" name="addCourses" value="Search Course" onclick='openP2lWindow(); return false;'/>
            </td>
         </tr>
         <tr><td></td></tr>
         <tr>
            <td> <!-- added id shindo -->
            Completion Check : <input id="addCourseCompRegChk" type="radio" name="addCourseCompRegChk" value="N" onclick='setVal("Comp");'/> 
            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
            Registration Check : <input id="addCourseCompRegChk" type="radio" name="addCourseCompRegChk" value="N" onclick='setVal("Reg");'/>
            </td> 
         </tr>
         <tr><td></td></tr>
          <tr><td></td></tr>
           <tr><td></td></tr>
         <tr>
                <td align="right"><input type=button value="Save" onclick='saveMapping();'/></td>
                <td align="left"><input type=button value="Cancel" onclick='reloadPage();'></td>
         </tr>
     </table>
 </td>	
</tr>
<tr>
</tr>
<tr>	<!â€”product course mapping->
	
	<td align="center">
		<table id="mappingTable" class="blue_table">
			<tr>
				<th>Product</th>
				<th>Course</th>
                <th>Completion Check</th>
				<th>Registration Check</th>
				<th>Action</th>
			</tr>
			<%	boolean oddEvenFlag = false;
			for(int i = 0;i<pcMap.length;i++) {
                oddEvenFlag = !oddEvenFlag; 
			%>
			<tr class="<%=oddEvenFlag?"even":"odd"%>">
				<td><%=pcMap[i].getProductCode()%></td>							
				<td><%=pcMap[i].getCourseCode()%></td>
                
                <td align="center">
                    <%if(pcMap[i].getCompletion()!=null && pcMap[i].getCompletion().equalsIgnoreCase("Y")){%>
                    <input type="radio" name="rCompReg<%=i%>" CHECKED onclick='setCompCheck(<%=i%>);'>  
                    <%}else{%>
                    <input type="radio" name="rCompReg<%=i%>" onclick='setCompCheck(<%=i%>);'>
                    <%}%>
                </td>
                
                <td align="center">
                    <%if(pcMap[i].getRegistration()!=null && pcMap[i].getRegistration().equalsIgnoreCase("Y")){%>
                    <input type="radio" name="rCompReg<%=i%>" onclick='setRegCheck(<%=i%>);' <%if("Y".equalsIgnoreCase(pcMap[i].getRegistration())){%> CHECKED <%}%> >  
                    <%}else{%>
                    <input type="radio" name="rCompReg<%=i%>" onclick='setRegCheck(<%=i%>);'>
                    <%}%>
                </td>
                
                
    <td>
    <input type=hidden name=pCode value="<%=pcMap[i].getProductCode()%>">
    <input type=hidden name=cCode value="<%=pcMap[i].getCourseCode()%>">
  	
    <input type=hidden name=rCompletionCheck value="<%=pcMap[i].getCompletion()%>">
    <input type=hidden name=rRegistrationCheck value="<%=pcMap[i].getRegistration()%>">
    
    
    <div style="display:none" class="mprodcode">
        <%=pcMap[i].getProductCode()%>,<%=pcMap[i].getCourseCode()%></div>
        <!-- shindo padding edge-->
    <img style="cursor: hand;margin-bottom: 4;" src="/TrainingReports/resources/images/admin/button_save.gif" alt="Save" width="40" height="18" onclick='saveProductCourseMapping(<%=i%>);'/>
    <img style="cursor: hand;" src="/TrainingReports/resources/images/admin/button_remove.gif" alt="Remove" width="56" height="18" onclick='removeGroup(<%=i%>);'/>
    
    </td>
			</tr>
			<%
			}
			%> 
		</table>
	</td>
</tr>
</table></td>
</tr>
</table>
