<%@ page import="com.pfizer.hander.P2lHandler"%>
<%@ page import="com.pfizer.utils.DBUtil"%>
<%@ page import="com.pfizer.webapp.AppConst"%>
<%@ page import="com.pfizer.webapp.AppQueryStrings"%>

<%@ page import="com.pfizer.webapp.user.TerritoryFilterForm"%>
<%@ page import="com.pfizer.webapp.user.User"%>
<%@ page import="com.pfizer.webapp.user.UserTerritory"%>
<%@ page import="com.pfizer.webapp.wc.components.shared.ReportFilterSelectWc"%>
<%@ page import="com.pfizer.webapp.wc.components.shared.TerritorySelectWc"%>
<%@ page import="com.tgix.html.HtmlBuilder"%>
<%@ page import="com.tgix.printing.LoggerHelper"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.Hashtable"%>
<%@ page import="java.util.Enumeration"%>
<%@ page import="java.util.Vector"%>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Map" %>

<%
	ReportFilterSelectWc wc = (ReportFilterSelectWc)request.getAttribute(ReportFilterSelectWc.ATTRIBUTE_NAME);
	List areas = wc.getUserTerritory().getAreas();
	UserTerritory ut = wc.getUserTerritory();
	TerritoryFilterForm tff = wc.getTerritoryFilterForm();
    List firstDropdown = wc.getUserTerritory().getFirstDropdown();
    /* Added for Phase 1 by Meenakshi*/
    wc.getTerritoryFilterForm().setStatusList();
    /*End of addition */

%>
<style type="text/css">@import url(/TrainingReports/resources/js/jscalendar-1.0/calendar-blue.css);</style>
<script type="text/javascript" src="/TrainingReports/resources/js/jscalendar-1.0/calendar.js"></script>

<script type="text/javascript" src="/TrainingReports/resources/js/jscalendar-1.0/lang/calendar-en.js"></script>

<script type="text/javascript" src="/TrainingReports/resources/js/jscalendar-1.0/calendar-setup.js"></script>



<script type="text/javascript" language="JavaScript">
	function getNextLevel(id,level) {
        var temp=id.selectedIndex;
        var selected_text = id.options[temp].text;
        var selected_value = id.options[temp].value;
        var tmpSel = document.getElementById( '<%=wc.getTerritoryFilterForm().FIELD_SALESORG%>' );
       // alert(tmpSel);
        if(tmpSel.options !=null)
        {
            var selected_sales_text = tmpSel.options[tmpSel.selectedIndex].value;
        }
        else{
            var selected_sales_text ='All';
        }
        ///added for TRT Phase 2 employee grid////
        window.location="/TrainingReports/phase/getNextP2lPhaseLevel?<%=AppQueryStrings.FIELD_SALES%>=" +selected_text+"&<%=AppQueryStrings.FIELD_SALESVALUE%>="+selected_value+"&<%=AppQueryStrings.FIELD_SALESORG%>="+selected_sales_text+"&<%=AppQueryStrings.FIELD_SALESLEVEL%>="+level;
        //var frm = document.selOptionalFieldsForm;
        //concatSelectedFields();
        //frm.action='/TrainingReports/phase/getNextP2lPhaseLevel?<%=AppQueryStrings.FIELD_SALES%>=" +selected_text+"&<%=AppQueryStrings.FIELD_SALESVALUE%>="+selected_value+"&<%=AppQueryStrings.FIELD_SALESORG%>="+selected_sales_text+"&<%=AppQueryStrings.FIELD_SALESLEVEL%>='+level;
        //frm.submit();
        //////end employee grid////
    }

    function clearDates(sid){
        document.getElementById(sid).value="";
    }
    function validate(frmDate,toDate){
    //alert('inside validate');
     var hstart=document.getElementById(frmDate);
     var hend=document.getElementById(toDate);
   //  alert(hstart.value);
   //  alert(hend.value);
     if(hstart.value!="" && hend.value!=""){
           // alert("step 1");
            var hsDate=new Date(hstart.value);
            var heDate=new Date(hend.value);
            if(hsDate > heDate){
            //    alert("step 2");
                document.getElementById("msg").innerHTML='From Date cannot be greater than To date';
                document.getElementById("msg").focus();
                //alert('returning false');
                return false;
            }
          }
          //alert('returning true');

     return true;
    }


///added for TRT Phase 2 employee grid////

function concatSelectedFields()
{
try{
//alert('inside concat');
var frm = document.selOptionalFieldsForm;
var strResult = " ";

    for(i=0; i<frm.chkBox.length; i++)
    {
      if(frm.chkBox[i].checked){
         if(strResult == " "){
            strResult=frm.chkBox[i].value;}
         else{

            strResult = strResult + "," + frm.chkBox[i].value ;

            }
       }
    }

    frm.newSet.value = strResult;
    //alert(strResult);
    return true;
    }
    catch(err){return true;}
}

function concatSelectedHQFields()
{  try{
    //alert('inside concatHQ');
    var frm = document.selOptionalFieldsForm;
    var strResult1 = " ";

    for(i=0; i<frm.chkBoxHQ.length; i++)
    {
      if(frm.chkBoxHQ[i].checked){
         if(strResult1 == " "){
            strResult1=frm.chkBoxHQ[i].value;}
         else{

            strResult1 = strResult1 + "," + frm.chkBoxHQ[i].value ;

            }
       }
    }

    frm.newHQSet.value = strResult1;
  //  alert(strResult1);
    return true;
    }
    catch(err){return true;}
}


//////end employee grid////

</script>
<table class="basic_table" border="0" width="0"  style="font-size=12">
		<form name="selOptionalFieldsForm" action="<%=wc.getPostUrl()%>" method="post" class="form_basic">
        <%--/* Added for Phase 1 by Meenakshi */ --%>
<tr><td valign="top" width="30%">
<table  style="font-size=12">
<tr>
    <td colspan="3"><h5 align="center">Search Criteria</h5>
    </td>

</tr>
<tr>
	            <td width="50px">Status:</td>
                <td width="100px"  style="font-size=12">
                    <select id="<%=wc.getTerritoryFilterForm().FIELD_CHKSTATUS%>" name="<%=wc.getTerritoryFilterForm().FIELD_CHKSTATUS%>" style="width:120px;font-size=12">
                            <%=HtmlBuilder.getOptionsFromLabelValue(tff.getStatusList(),tff.getChkStatus())%>
							<%System.out.println("Status drop down"+tff.getChkStatus());%>
                    </select>
                </td>
                <td>
                </td>
</tr>
<tr>           <td nowrap>From Date:</td>
                <td>
<table>
<tr>            <td>
                <input class="text" type="text" name="<%=wc.getTerritoryFilterForm().FIELD_FROMDATE%>" id="<%=wc.getTerritoryFilterForm().FIELD_FROMDATE%>"  value="<%=wc.getTerritoryFilterForm().getFromDate()%>" size="12" readonly></td>
                <td valign="top"><input type=image src="/TrainingReports/resources/images/calendar.jpg" id="fromDate_Id"  border="0" height="17" width="20" alt="Select From Date" value="Change Date" name="changeDate" >

                <script type="text/javascript">

                                Calendar.setup({
                                    inputField     :    "<%=wc.getTerritoryFilterForm().FIELD_FROMDATE%>",
                                    ifFormat       :    "%m/%d/%Y",
                                    button         :    "fromDate_Id"
                                });

                            </script>
                             
                </td>
                <td><img src="/TrainingReports/resources/images/cancel.gif" alt="Clear From Date" onmouseover="this.style.cursor='hand'" height="17" width="20" onclick="clearDates('<%=wc.getTerritoryFilterForm().FIELD_FROMDATE%>');">
                </td>
</tr>
<tr>
            <td><div class="text" id="msg" style="color:red;font-size:12px"></div></td><td>&nbsp;
                </td>
</tr>

</table>
                </td>
</tr>

<tr>            <td nowrap>To Date:</td>
                <td>
<table>
<tr>            <td>
                <input class="text" type="text" name="<%=wc.getTerritoryFilterForm().FIELD_TODATE%>" id="<%=wc.getTerritoryFilterForm().FIELD_TODATE%>" value="<%=wc.getTerritoryFilterForm().getToDate()%>" size="12" readonly> </td>
                <td valign="top"><input type=image src="/TrainingReports/resources/images/calendar.jpg" id="toDate_Id"  border="0" height="17" width="20" alt="Select To Date" value="Change Date" name="changeDate" >

                <script type="text/javascript">

                                Calendar.setup({
                                    inputField     :    "<%=wc.getTerritoryFilterForm().FIELD_TODATE%>",
                                    ifFormat       :    "%m/%d/%Y",
                                    button         :    "ToDate_Id"
                                });


                            </script>
                            </td>
                <td>
                <img src="/TrainingReports/resources/images/cancel.gif" alt="Clear To Date" onmouseover="this.style.cursor='hand'" height="17" width="20" onclick="clearDates('<%=wc.getTerritoryFilterForm().FIELD_TODATE%>');">
                </td>
</tr>
</table>
                </td>
</tr>

<tr>

					<td align="center" colspan="3">
					<!--edited for HQ user-->
						<input name="" type="image" src="/TrainingReports/resources/images/btn_getreport.gif" style="margin-top:20px;" onclick="javascript:concatSelectedFields();concatSelectedHQFields();return validate('<%=wc.getTerritoryFilterForm().FIELD_FROMDATE%>','<%=wc.getTerritoryFilterForm().FIELD_TODATE%>');concatSelectedFields();">
					<!--end editing for HQ user-->
					</td>
</tr>

</table>
                </td>
                <!-- /////////Added for TRT Phase 2 amployee grid////////-->
                <%User user=wc.getUser();
               
                // Added for TRT Phase 2 - HQ user
                System.out.println("user.isHQUser()=="+user.isHQUser());
                //System.out.println("admin=="+admin);
                //if(user.isHQUser() || user.isAdmin()){
            %>
                <td width="5%">&nbsp;</td>
                <td colspan="2" valign="top" nowrap width="30%">
                <h5 align="center">Optional 'Field Force' fields</h5>
                <%
                   List selList=new ArrayList();

                   selList=(ArrayList)session.getAttribute("selectedOptEmpFields");
                   System.out.println("selectedOptEmpFields="+selList);
                %>
                <input type="hidden" name="newSet" value=""/>
                <div style="width:190px;height:135px;overflow-y:scroll;">
<table class="blue_table" width="100%"  style="font-size=12">
                <tr class="even"><td><input type="checkbox" name="chkBox" id="chkBox" value="EMPLID"
                <%if(selList!=null){for(int i=0;i<selList.size();i++){if("EMPLID".equals(selList.get(i).toString())){%> checked<%}}}%>></td>
                <td>Employee ID</td></tr>
                <tr class="odd"><td><input type="checkbox" name="chkBox" id="chkBox" value="Sex"
                <%if(selList!=null){for(int i=0;i<selList.size();i++){if("Sex".equals(selList.get(i).toString())){%> checked<%}}}%>></td>
                <td>Gender</td></tr>
                <tr class="even"><td><input type="checkbox" name="chkBox" id="chkBox" value="GEO_DESC"
                <%if(selList!=null){for(int i=0;i<selList.size();i++){if("GEO_DESC".equals(selList.get(i).toString())){%> checked<%}}}%>></td>
                <td>Geography Description</td></tr>
                 <tr class="odd"><td><input type="checkbox" name="chkBox" id="chkBox" value="GUID"
                <%if(selList!=null){for(int i=0;i<selList.size();i++){if("GUID".equals(selList.get(i).toString())){%> checked<%}}}%>></td>
                <td>GUID</td></tr>
                <tr class="even"><td><input type="checkbox" name="chkBox" id="chkBox" value="HIRE_DATE"
                <%if(selList!=null){for(int i=0;i<selList.size();i++){if("HIRE_DATE".equals(selList.get(i).toString())){%> checked<%}}}%>></td>
                <td>Hire Date</td></tr>
                <tr class="odd"><td><input type="checkbox" name="chkBox" id="chkBox" value="MEMAILADDRESS"
                <%if(selList!=null){for(int i=0;i<selList.size();i++){if("MEMAILADDRESS".equals(selList.get(i).toString())){%> checked<%}}}%>></td>
                <td>Manager Email Id</td></tr>
                <tr class="even"><td><input type="checkbox" name="chkBox" id="chkBox" value="PROMOTION_DATE"
                <%if(selList!=null){for(int i=0;i<selList.size();i++){if("PROMOTION_DATE".equals(selList.get(i).toString())){%> checked<%}}}%>></td>
                <td>Promotion Date</td></tr>
                <tr class="odd"><td><input type="checkbox" name="chkBox" id="chkBox" value="STATE"
                <%if(selList!=null){for(int i=0;i<selList.size();i++){if("STATE".equals(selList.get(i).toString())){%> checked<%}}}%>></td>
                <td>Regional Office State</td></tr>
                <tr class="even"><td><input type="checkbox" name="chkBox" id="chkBox" value="SALES_POSITION_ID"
                <%if(selList!=null){for(int i=0;i<selList.size();i++){if("SALES_POSITION_ID".equals(selList.get(i).toString())){%> checked<%}}}%>></td>
                <td>Sales Position ID</td></tr>
                <tr class="odd"><td><input type="checkbox" name="chkBox" id="chkBox" value="SALES_POSITION_TYPE_CD"
                <%if(selList!=null){for(int i=0;i<selList.size();i++){if("SALES_POSITION_TYPE_CD".equals(selList.get(i).toString())){%> checked<%}}}%>></td>
                <td>Source</td></tr>
                <!-- Added by Swati-->
                <tr class="even"><td><input type="checkbox" name="chkBox" id="chkBox" value="HOME_STATE"
                <%if(selList!=null){for(int i=0;i<selList.size();i++){if("HOME_STATE".equals(selList.get(i).toString())){%> checked<%}}}%>></td>
                <td>Home State</td></tr>

</table>
                </div>
                </td>
                <%//}%>

                <%if(user.isHQUser() || user.isAdmin()){%>
                <td width="5%">&nbsp;</td>

                <td colspan="2" valign="top" nowrap width="30%">
                 <h5 align="center">Optional 'HQ' fields</h5>
                <%
                   List selHQList=new ArrayList();

                   selHQList=(ArrayList)session.getAttribute("selectedOptHQEmpFields");
                   System.out.println("selectedOptHQEmpFields="+selHQList);
                %>
                <input type="hidden" name="newHQSet" value=""/>
                <div style="width:190px;height:150px;">
                <table class="blue_table" width="100%">
                <tr class="even"><td><input type="checkbox" name="chkBoxHQ" id="chkBoxHQ" value="DEPTID"
                <%if(selHQList!=null){for(int i=0;i<selHQList.size();i++){if("DEPTID".equals(selHQList.get(i).toString())){%> checked<%}}}%>></td>
                <td>Department ID</td></tr>
                <tr class="odd"><td><input type="checkbox" name="chkBoxHQ" id="chkBoxHQ" value="EMPLID"
                <%if(selHQList!=null){for(int i=0;i<selHQList.size();i++){if("EMPLID".equals(selHQList.get(i).toString())){%> checked<%}}}%>></td>
                <td>Employee ID</td></tr>
                <tr class="even"><td><input type="checkbox" name="chkBoxHQ" id="chkBoxHQ" value="GUID"
                <%if(selHQList!=null){for(int i=0;i<selHQList.size();i++){if("GUID".equals(selHQList.get(i).toString())){%> checked<%}}}%>></td>
                <td>GUID</td></tr>
                <tr class="odd"><td><input type="checkbox" name="chkBoxHQ" id="chkBoxHQ" value="LOCATION"
                <%if(selHQList!=null){for(int i=0;i<selHQList.size();i++){if("LOCATION".equals(selHQList.get(i).toString())){%> checked<%}}}%>></td>
                <td>Location Description</td></tr>
                <tr class="even"><td><input type="checkbox" name="chkBoxHQ" id="chkBoxHQ" value="SUPERVISORNAME"
                <%if(selHQList!=null){for(int i=0;i<selHQList.size();i++){if("SUPERVISORNAME".equals(selHQList.get(i).toString())){%> checked<%}}}%>></td>
                <td>Supervisor Name</td></tr>
                
                
                </table>
                </div>
                </td>
                <%}%>
</tr>

        <%-- End of status--%>

        <%-- End of From and To Date--%>
        <!-- ///////////end emploeyee grid////////// -->
		</form>

</table>



