<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.pfizer.actionForm.RBUGetEmployeeDetailForm"%>
<%@ page import="com.pfizer.actionForm.RBUTrainingClassesForm"%>
<%@ page import="com.pfizer.actionForm.RBUTrainingScheduleListForm"%>
<%@ page import="com.pfizer.db.RBUClassData"%>
<%@ page import="com.pfizer.webapp.AppConst"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.Hashtable"%>
<%@ page import="java.util.Enumeration"%>
<%@ page import="java.util.Vector"%>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.HashMap" %>


<%@ page import="com.tgix.Utils.Util"%>
<%-- <%@ taglib uri="netui-tags-databinding.tld" prefix="netui-data"%>
<%@ taglib uri="netui-tags-html.tld" prefix="netui"%>
<%@ taglib uri="netui-tags-template.tld" prefix="netui-template"%> --%>
<LINK href="/TrainingReports/resources/css/header.css"  type="text/css" rel="STYLESHEET">           
<LINK href="/TrainingReports/resources/css/trainning.css" type="text/css" rel="STYLESHEET">
<script type="text/javascript" language="JavaScript" src="<%=AppConst.JAVASCRIPT_LOC%>/prototype-1.6.0.3.js"></script>


<%
RBUTrainingClassesForm form = (RBUTrainingClassesForm)request.getAttribute("trainingClassesForm");
    List prods = form.getFutureProds();
    System.out.println("size of prods = " + prods.size());
    List classes = form.getClasses();
    System.out.println("size of classes = " + prods.size());
    String selectedProd = request.getParameter("selectedproduct")==null?"":request.getParameter("selectedproduct");
%>

<!-- 
<netui:html> -->
<html>
    <head>
        <title>
            Add Training             
        </title>
        <style type="text/css">
            body { font-family: Tahoma, Geneva, Arial, Helvetica, sans-serif; font-size: 12px; } td { font-family: Tahoma, Geneva, Arial, Helvetica, sans-serif; font-size: 12px; } th { font-family: Tahoma, Geneva, Arial, Helvetica, sans-serif; font-size: 12px; } .bodystyle { font-family: Tahoma, Geneva, Arial, Helvetica, sans-serif; font-size: 12px; } .small { font-family: Tahoma, Geneva, Arial, Helvetica, sans-serif; font-size: 9px; } .medium { font-family: Tahoma, Geneva, Arial, Helvetica, sans-serif; font-size: 12px; } .big { font-family: Tahoma, Geneva, Arial, Helvetica, sans-serif; font-size: 16px; } .xbig { font-family: Tahoma, Geneva, Arial, Helvetica, sans-serif; font-size: 24px; } .expanded { font-family: Tahoma, Geneva, Arial, Helvetica, sans-serif; font-size: 12px; line-height: 16px; letter-spacing: 2px; } .justified { font-family: Tahoma, Geneva, Arial, Helvetica, sans-serif; text-align: justify; } .footer { font-family: "Times New Roman", Times, serif; font-size: 9px; color: #999999; } .box1 { padding: 3px; border-width: thin; border-style: solid; border-color: #CCCCCC #666666 #666666 #CCCCCC; } .box2 { font-style: italic; word-spacing: 2pt; padding: 3px; border-width: thin; border-style: solid; } .botline { border-bottom-width: 1px; border-bottom-style: solid; border-bottom-color: #666666; } .pad { padding: 10px; } .mainNavOn { font-weight: bold; color: #FFFFFF; background-color: #FFFF00; } .navonlink { color: #FFFFFF; } .mainNavOff { font-weight: bold; color: #FFFFFF; background-color: #024c98; } .navOffLink { color: #FFFFFF; } .SecondaryNavOff { font-weight: bold; color: #000000; background-color: ffffcc; border-bottom-width: 1px; border-bottom-style: solid; border-bottom-color: #333333; } .SecondaryNavOn { font-weight: bold; color: #000000; background-color: #FFFF00; border-bottom-width: 1px; border-bottom-style: solid; border-bottom-color: #333333; } .popTitle { font-weight: bold; color: #000000; background-color: #9FBFDF; } .popSortLink { color: #000000; } .TblBrdr { border: 1px solid #333333; } .TDLeftBrdr { border-top-width: 1px; border-right-width: 1px; border-bottom-width: 1px; border-left-width: 1px; border-left-style: solid; border-top-color: #333333; border-right-color: #333333; border-bottom-color: #333333; border-left-color: #333333; } .textBox { FONT-WEIGHT: normal; FONT-SIZE: 9pt; COLOR: black; FONT-FAMILY: Arial; } .trainingTable { border-top: 1px solid #333333; border-right: 1px solid #333333; border-bottom: 1px solid #333333; border-left: 1px solid #333333; } .trainingTable1 { border-bottom: 1px solid #333333; } .ActionsTableHeader { border: 1px solid #9FBDDA; font-family: Arial, Helvetica, sans-serif; font-size: 14px; font-weight: bold; background-color: #5C97C7; color: #FFFFFF; } .HeadingStyle{ FONT-WEIGHT: bold; FONT-SIZE: 11pt; COLOR: black; FONT-FAMILY: Arial; } .dataSection{ font-family: Arial; FONT-WEIGHT: NORMAL; FONT-SIZE: 9pt; }        
        </style>        
        
    </head>
    <script>
function submitupdate()
{     
  if(document.ChangeDate.ddlSlave.selectedIndex==0){
    alert('Make A Selection First !');
    return;
  }
  
  if(document.ChangeDate.reason.value.length <= 0){
    alert('Please Enter the Reason');
    document.all.reason.focus();
    return;
  }
   
  document.ChangeDate.reason.value = document.ChangeDate.reason.value.replace(/^\s+|\s+$/g, '');
  document.ChangeDate.courseID.value = document.ChangeDate.ddlSlave.options[document.ChangeDate.ddlSlave.selectedIndex].value;
  
  document.all.ChangeDate.target = "mainWin";
  if(window.confirm('Do you want to continue ?')) 
      document.all.ChangeDate.submit();
  window.close();
}

function limitText(limitField, limitCount, limitNum) {
	if (limitField.value.length > limitNum) {
		limitField.value = limitField.value.substring(0, limitNum);
	} else {
		limitCount.value = limitNum - limitField.value.length;
	}
}


</script>
    <body onload="window.focus();">    
        <form name="ChangeDate" method="post"  target="" action="getEmployeeDetails">  
        <input type="hidden" name="commandchangetime" value="addTraining">
        <input type="hidden" name="courseID" value="1">
        <input type="hidden" name="emplid" value="<%=form.getEmplid()%>">	
        <TABLE class="TblBrdr" cellpadding="5" cellspacing="0" width="100%"  height="100%" align="center">

        <tr>
        <td align="left">
        <LABEL FOR=ddlMaster>Product:</LABEL>
             <select id='ddlMaster' style="width: 200px">
            </select>
            <tr>
            <td align="left"> 
            <LABEL FOR=ddlSlave>Date:</LABEL>       
              <select id='ddlSlave' style="width: 200px" disabled=true>
            </select>
            </td>
             </tr>
            </td>
             </tr>
<script type="text/javascript">

//var master = {'1':'product 1', '2':'product 2'};
//var slave = {'1': {'1': 'slave 1','2': 'slave 2'},'2': {'3': 'slave 3','4': 'slave 4'}};

var master = new Hash();
var slave = new Hash();

            <%   
                int index = 1;
                for(Iterator i = prods.iterator();i.hasNext();){
                    String product = (String)  i.next();
                    out.print(" master.set( "+ index + ", '"+product+"'); ");
                    out.print("var slavedata = new Hash(); ");
                    int index1 = 1;
                    for(Iterator iter = classes.iterator();iter.hasNext();){
                         RBUClassData data = (RBUClassData)  iter.next();     
                         if(data.getProductdesc().equals(product)){  
                             out.print(" slavedata.set( " + data.getCourseID() + ", '"+data.getStartDate() +" - " + data.getCourseName() + "');");
                             index1 ++;
                         }
                    }
                    out.print(" slave.set(" + index + " , slavedata); ");
                    index ++;
                }
            %>    

var ddlMaster = $('ddlMaster');
master.each(function(pair) {
  ddlMaster.insert('<option value="' + pair.key + '">' + pair.value + '</option>');
});
ddlMaster.insert({top: '<option value="0">Select Product</option>'});
ddlMaster.observe('change', fillSlave);
ddlMaster.selectedIndex = 0;

function fillSlave() {
  var ddlSlave = $('ddlSlave');
  ddlSlave.childElements().invoke('remove');
  ddlSlave.disabled = true;
  var slavedata = slave.get(ddlMaster.getValue());
  if (slavedata != null) {
	ddlSlave.disabled = false;
    slavedata.each(function(pair) {
        ddlSlave.insert('<option value="' + pair.key + '">' + pair.value + '</option>');
    });
}
ddlSlave.insert({top: '<option value="0">Select Start Date</option>'});
ddlSlave.selectedIndex = 0;
}
</script>
        <tr>
        <TH width="100%"  valign="middle" align="left" >Please Enter Reason for update</TH>   
        </tr>
        <TR> 
        <TD align="left" >                             
            <textarea id="reason" name="reason" rows="4" cols="25"
                      onKeyDown="limitText(this.form.reason,this.form.countdown,256);" 
                      onKeyUp="limitText(this.form.reason,this.form.countdown,256);"            
            >
            </textarea>
            <br>
            <font size="1">(Maximum characters: 256)<br>
             You have <input readonly type="text" name="countdown" size="3" value="256"> characters left.</font>            
        </TD>
        </TR> 
        <TR>
        <TD>
            <input type="button" name="Change" value="Change" onclick="return submitupdate();">
            <input type="button" name="cancel" value="Cancel" onclick="window.close();">               
        </TD>
        </TR>        
        </TABLE>
        </form>
    </body>
<!-- </netui:html> -->
</html>