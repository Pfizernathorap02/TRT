<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page import="com.pfizer.PWRA.EmployeeDetailFacade.EmployeeInfo"%>
<%@ page import="com.pfizer.PWRA.EmployeeDetailFacade.PLCExamStatus"%>
<%@ page import="com.pfizer.PWRA.EmployeeDetailFacade.PLCStatus"%>
<%@ page import="com.pfizer.PWRA.EmployeeDetailFacade.ProductAssignmentInfoRBU"%>
<%@ page import="com.pfizer.PWRA.EmployeeDetailFacade.TrainingMaterialHistory"%>
<%@ page import="com.pfizer.PWRA.EmployeeDetailFacade.TrainingSchedule"%>
<%@ page import="com.pfizer.actionForm.RBUGetEmployeeDetailForm"%>
<%@ page import="com.pfizer.db.Product"%>
<%@ page import="com.pfizer.db.RBUAllStatus"%>
<%@ page import="com.pfizer.db.RBUGuestTrainersClassData"%>
<%@ page import="com.pfizer.db.RBUPedagogueExam"%>
<%@ page import="com.pfizer.webapp.AppConst"%>
<%@ page import="com.pfizer.webapp.user.User"%>
<%@ page import="com.pfizer.webapp.user.UserSession"%>
<%@ page import="com.pfizer.webapp.wc.RBU.RBUEmployeeDetailsEmailWc"%>
<%@ page import="com.pfizer.webapp.wc.RBU.RBUEmployeeDetailsWc"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.HashMap"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.Map"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="java.util.Date"%>
<%@ page import="java.util.Collections"%>
<%@ page import="com.tgix.Utils.Util"%>
<%-- <%@ taglib uri="netui-tags-databinding.tld" prefix="netui-data"%>
<%@ taglib uri="netui-tags-html.tld" prefix="netui"%>
<%@ taglib uri="netui-tags-template.tld" prefix="netui-template"%> --%>
<%

     UserSession uSession = UserSession.getUserSession(request);	
     User user = uSession.getUser();
    //try to get form bean, beacause it is not visible.
    String mode0 = request.getParameter("m0")==null?"":request.getParameter("m0");
    String mode1 = request.getParameter("m1")==null?"":request.getParameter("m1");
    String mode2 = request.getParameter("m2")==null?"":request.getParameter("m2");
    RBUEmployeeDetailsEmailWc wc = (RBUEmployeeDetailsEmailWc)request.getAttribute(RBUEmployeeDetailsWc.ATTRIBUTE_NAME);
    request.setAttribute("getEmployeeDetailForm",wc.getFormBean());
    RBUGetEmployeeDetailForm getEmployeeDetailForm = wc.getFormBean();
    EmployeeInfo info = getEmployeeDetailForm.getEmployeeInfo();
    if(info == null){
        info = new EmployeeInfo();
    }
    ProductAssignmentInfoRBU ainfo = getEmployeeDetailForm.getProductAssignmentInfo();
    if(ainfo == null){
        ainfo = new ProductAssignmentInfoRBU();
    }
    // Prepare Render Title
    String titleGeneral = "";
    //String titleSpecific = ""; 
    String eventId = "";

    titleGeneral = "PSCPT";
    // Event id is 8 for RBU in SCE so pass 10
    eventId = "8";
    
    titleGeneral = titleGeneral+" Detail";
    //UserSession uSession = UserSession.getUserSession(request); 
    //todo - check on this - Shannon
    String sceUrl = "http://sce.pfizer.com/SCEWeb/evaluation/evaluateTR";
	
	String url = request.getRequestURL().toString();
	if ( url != null ) {
		url = url.toLowerCase();
	}
	if ( url != null 
		&& url.indexOf("trt-stg.pfizer.com") > 0 ) {
		sceUrl = "http://sce-stg.pfizer.com/SCEWeb/evaluation/evaluateTR";	
	}    
    if ( url != null 
		&& (url.indexOf("localhost") > 0 || url.indexOf("trt-tst.pfizer.com") > 0 )) {
		sceUrl = "http://sce-tst.pfizer.com/SCEWeb/evaluation/evaluateTR";	
	}
    if ( url != null 
		&& (url.indexOf("trt-dev.pfizer.com") > 0)) {
		sceUrl = "http://sce-dev.pfizer.com/SCEWeb/evaluation/evaluateTR";	
	}
    
%>
<script language="javascript">
    self.name = 'mainWin';
	function openPopup(){
    
		window.open("","_popup","toolbars=0,location=0,width=500,height=500");
		return true;
	}
    function pw() {return window.innerWidth || document.documentElement.clientWidth || document.body.clientWidth}; 
    function mouseX(evt) {return evt.clientX ? evt.clientX + (document.documentElement.scrollLeft || document.body.scrollLeft) : evt.pageX;} 
    function mouseY(evt) {return evt.clientY ? evt.clientY + (document.documentElement.scrollTop || document.body.scrollTop) : evt.pageY} 
    function popUp(evt,oi, content) 
    {
    if (document.getElementById) {
    var wp = pw(); dm = document.getElementById(oi); 
    dm.innerHTML = content;
    ds = dm.style; st = ds.visibility; 
    if (dm.offsetWidth) ew = dm.offsetWidth; 
    else if (dm.clip.width) ew = dm.clip.width; 
    if (st == "visible" || st == "show") { 
    ds.visibility = "hidden"; 
    } else {tv = mouseY(evt) + 20; lv = mouseX(evt) - (ew/4); 
    if (lv < 2) lv = 2; else if (lv + ew > wp) lv -= ew/2; 
    lv += 'px';tv += 'px';  
    ds.left = lv; ds.top = tv; ds.visibility = "visible";}}}
    
    function validateReorder(){
        var checkFound = false;
        var myForm = document.all.material;
        for (var counter=0; counter < myForm.length; counter++) {
            if ((myForm.elements[counter].name == "reorder") && (myForm.elements[counter].checked == true)) {
             checkFound = true;
            }
        }
        if (checkFound != true) {
            alert('Please select atleast one training material to re-order.');
            return false;
        }
    }
    
</script>
<style type="text/css">
                    .tip {font:10px/12px
                    Arial,Helvetica,sans-serif; border:solid 1px
                    #666666; width:150px;height:50px; padding:1px;
                    position:absolute; z-index:100;
                    visibility:hidden; color:#333333; top:20px;
                    left:90px; background-color:#ffffcc;
                    layer-background-color:#ffffcc;}
</style>

<div id="t1" class="tip">This is a Javascript Tooltip</div>


<TABLE class="basic_table"><TR><TD><IMG src="/TrainingReports/resources/images/spacer.gif" width="15"></TD><TD>
    <P id="table_title" style="font-size:1.2em; font-weight:bold;">
    <%if(info != null && info.getLastName() !=null){%>
    <%=getEmployeeDetailForm.getEmployeeInfo().getLastName()%>,
    <%=getEmployeeDetailForm.getEmployeeInfo().getPreferredName()%>
    
    <%
    }
        String emplStatus = null;
        
        if(info !=null) emplStatus = info.getStatus();
        if(emplStatus != null && emplStatus.equalsIgnoreCase("On-Leave") ){ 
            out.println("<font color='red'><b>"+getEmployeeDetailForm.getEmployeeInfo().getStatus()+"</b></font>");
        }
    
    %>

    </P>
    
    <TABLE class="blue_table" width="800px">
    <TR>
    <TH colspan="6" align="left">Employee Information</TH>
    </TR>
    <TR>
    <TD ROWSPAN="8" VALIGN="left">    
    <%if(info.getImageURL() !=null){%>
        <!-- <img src="{request.getEmployeeDetailForm.employeeInfo.imageURL}"/> -->    
        <img src="<%=getEmployeeDetailForm.getEmployeeInfo().getImageURL()%>"/>
    <%}%>
    </TD>
    <TD>Employee Status</TD>
    <TD>
        <%
        if(emplStatus != null){
            if(emplStatus.equalsIgnoreCase("On-Leave")||emplStatus.equalsIgnoreCase("Terminated") ){ 
                out.println("<font color='red'><b>"+getEmployeeDetailForm.getEmployeeInfo().getStatus()+"</b></font>");
            }else{
                out.println(emplStatus);
            }
        }
    %>
    </TD>
    <TD ROWSPAN="8" VALIGN="left" bgcolor="#DDDDDD"></TD>
    <TD>Team</TD>
    <TD>
    <%if(info != null && info.getTeamCD() !=null){%>
    <%=getEmployeeDetailForm.getEmployeeInfo().getTeamCD()%>
    <%}%>
    </TD>
    </TR>
    <TR>
    <TD>Employee ID</TD>
    <TD>
    <%if(info != null && info.getEmplID() !=null){%>
    <%=getEmployeeDetailForm.getEmployeeInfo().getEmplID()%>
    <%}%>
    </TD>
    <TD>Role</TD>
    <TD>
    <%if(info != null && info.getTerritoryRole() !=null){%>
    <%=getEmployeeDetailForm.getEmployeeInfo().getTerritoryRole()%>
    <%}%>
    </TD>
    </TR>
    <TR>
    <TD>Full Name</TD>
    <TD>    
    <%if(info != null && info.getLastName() !=null ){%>
    <%=getEmployeeDetailForm.getEmployeeInfo().getLastName()%>,
    <%if( info.getPreferredName() !=null ){%>
    <%=getEmployeeDetailForm.getEmployeeInfo().getPreferredName()%>
    <%}%>
    <%}%>
    </TD>
    <TD>Territory Code</TD>
    <TD>
    <%if(info != null && info.getTerritoryID() !=null ){%>
    <%=getEmployeeDetailForm.getEmployeeInfo().getTerritoryID()%>
    <%}%>
    </TD>
    </TR>
    <tr>
    <TD>Gender</TD>
    <TD>
    <%if(info != null && info.getGender() !=null ){%>
     <%=getEmployeeDetailForm.getEmployeeInfo().getGender()%>
     <%}%>
    </TD>
    <TD>Region</TD>
    <TD>
    <%if(info != null && info.getAreaDesc() !=null ){%>
    <%=getEmployeeDetailForm.getEmployeeInfo().getAreaDesc()%>
    <%}%>
    </TD>
    </TR>  
    <TR>  
    <TD>Hire Date</TD>
    <TD>
    <%if(info != null && info.getHireDate() !=null ){%>
    <%=Util.formatDateShort(info.getHireDate())%>
    <%}%>
    </TD>
    <TD>District</TD>
    <TD>
    <%if(info != null && info.getDistrictDesc() !=null ){%>
    <%=getEmployeeDetailForm.getEmployeeInfo().getDistrictDesc()%>
    <%}%>
    </TD>
    </TR>
    <TR>  
    <TD>Promotion Date</TD>
    <TD>
    <%if(info != null && info.getPromotionDate() !=null ){%>
    <%=Util.formatDateShort(info.getPromotionDate())%>
    <%}%>
    </TD>
    <TD>Reports to</TD>
    <TD>
    <%if(info != null&&info.getReportToLastName()!=null && info.getReportToFirstName()!=null){%>
     <a href="mailto:<%=info.getReportToEmail()%>?subject=<%=titleGeneral%>">
     <%=Util.toEmptyNBSP(info.getReportToLastName())%>, <%=Util.toEmptyNBSP(info.getReportToFirstName())%>
     </a>
    <%}%>
    </TD>
    </TR>
        <TR>  
    <TD>Email</TD>
    <TD>
    <%if(info != null && info.getEmail() !=null ){%>
     <a href="mailto:<%=info.getEmail()%>?subject=<%=titleGeneral%>">
      <%=getEmployeeDetailForm.getEmployeeInfo().getEmail()%>      
      </a>  
     <%}%>
    </TD>
    <TD>Cluster</TD>
     <TD>
    <%if(info != null && info.getClusterCD() !=null ){%>
    <%=getEmployeeDetailForm.getEmployeeInfo().getClusterCD()%>
    <%}%>
    </TD>
    </TR>
    </TABLE>
    <BR><BR>
    
    <%--Future Information --%>
     <table class="blue_table_without_border" >
    <tr valign="top">
    <td>
     <TABLE class="blue_table" width="500px">
    <TR>
    <TH colspan="2" align="left">Employee Post PSCPT Information</TH>
    </TR>
    <TR>
    <TD>Post PSCPT Manager</TD>
   <TD><%=Util.toEmptyNBSP(info.getFuture_manager())%></TD>
    </TR>
    <TR>
    <TD>Sales Position Id</TD>
   <TD><%=Util.toEmptyNBSP(info.getSalesPositionId())%></TD>
   </TR>
   <TR>
    <TD>Sales Position Description</TD>
   <TD><%=Util.toEmptyNBSP(info.getSalesPositionDesc())%></TD>
   </TR>
       <TR>
    <TD>Post PSCPT Role</TD>
   <TD><%=Util.toEmptyNBSP(info.getFutureRole())%></TD>
   </TR>
    <TR>
    <TD>Post PSCPT BU</TD>
   <TD><%=Util.toEmptyNBSP(info.getFutureBU())%></TD>
   </TR>
    <TR>
    <TD>Post PSCPT RBU</TD>
   <TD><%=Util.toEmptyNBSP(info.getFutureRBU())%></TD>
   </TR>
    </TABLE>
     </td>
    <td>&nbsp;</td>
    </tr>
    </table>
    
    <%--Future Information end--%>
    
    
                  
                  <br>
                  <br>

    
        
    <%--RENDER PRODUCT ASSIGNMENT STATUS --%>    
    <table class="blue_table_without_border" >
    <tr valign="top">
    <td>
    <TABLE class="blue_table">
        <tr>
        <TH colspan="2" align="left">Product Assignment</TH>
        </TR>
        <TR><TD bgcolor="#DDDDDD" >
        <B>Pre PSCPT Team&frasl;Products</B>
        </TD>
        <TD bgcolor="#DDDDDD" >
        <B>Post PSCPT BU&frasl;Products</B>    
        </TD>
        </TR>    
        <TR><TD><B>Team:</B> 
        <%=(ainfo.getPreTeam()==null)?"":ainfo.getPreTeam()%>
        </TD>
        <TD><B>BU:</B> 
        <%=(ainfo.getFutureBU()==null)?"":ainfo.getFutureBU()%>
        </TD>
        </TR>
        <TR><TD><B>Product:</B> 
        <%    
            for(int i=0;i<ainfo.getCurrentProducts().size();i++){        
                String product = (String)ainfo.getCurrentProducts().elementAt(i);
                out.println(product);
                if(i!=ainfo.getCurrentProducts().size()-1){
                    out.println(",");        
                }     
            }
        %>    
        </TD>
        <TD><B>Product:</B> 
        <%    
            for(int i=0;i<ainfo.getFutureProducts().size();i++){        
                String product = (String)ainfo.getFutureProducts().elementAt(i);
                out.println(product);
                if(i!=ainfo.getFutureProducts().size()-1){
                    out.println(",");        
                }     
            }
        %>    
        </TD>
      </TR>  
    </TABLE> 
    </td>
    <td>&nbsp;</td>
 
    </tr>
    </table>
    
    <BR><BR>
    <!--New PSCPT Training Status-->
    <TABLE class="blue_table">
    <TR><TH colspan="11" align="left">PSCPT Training Status</TH></TR>
    <TR bgcolor="#DDDDDD">
    <TD><B>Product</B></TD>
    <TD><B>Overall</B></TD>
    <TD><B>PED1</B></TD>
    <TD><B>PED2</B></TD>
    <TD><B>PED3</B></TD>
    <TD><B>PED4</B></TD>
    <TD><B>SCE</B></TD> 
    <TD><B>Manager Action Needed?</B></TD> 
    <TD><B>PLC Training</B></TD>
    <TD><B>Cancel</B></TD>
    <TD><B>Notes</B></TD>
    </TR>
    <%
    List statuses = getEmployeeDetailForm.getRbuStatuses();
    System.out.println("status size  + " + statuses.size());
    int l = 0;
     for (Iterator iter = statuses.iterator(); iter.hasNext();){
        RBUAllStatus status = (RBUAllStatus) iter.next();      
        l++;
    %>
    <tr>
    <td><%=status.getProductdesc()%></td>
    <td><%=status.getOverallStatus()%></td> 
     <%
     int m = 0;
     for (Iterator p = status.getPeds().iterator(); p.hasNext();){
            m++;
            RBUPedagogueExam ped = (RBUPedagogueExam) p.next();
            if(ped.getExamScore()!= null){
                if(Integer.parseInt(ped.getExamScore())<80){
                    out.print("<td> <font color='red'> "+ ped.getExamScore() + "</font>");
                }else{
                out.print("<td>"+ ped.getExamScore());
                }
                out.print(" <!--ped name " +ped.getExamDisplayName() + " with score "+ped.getExamScore()+" -->");
                if(!ped.getExamDisplayName().equalsIgnoreCase("ped1")&&!ped.getExamDisplayName().equalsIgnoreCase("ped2")&&!ped.getExamDisplayName().equalsIgnoreCase("ped3")&&!ped.getExamDisplayName().equalsIgnoreCase("ped4")){
                 out.print(" (" + ped.getExamDisplayName()+ ")</td>");
                }else{
                    out.print("</td>");
                }
            }else{
                out.print(" <!--ped name " +ped.getExamDisplayName() + " null score -->");
                out.print("<td>"+ ped.getExamStatus() );
                if(!ped.getExamDisplayName().equalsIgnoreCase("ped1")&&!ped.getExamDisplayName().equalsIgnoreCase("ped2")&&!ped.getExamDisplayName().equalsIgnoreCase("ped3")&&!ped.getExamDisplayName().equalsIgnoreCase("ped4")){
                    out.print(" (" + ped.getExamDisplayName()+ ")</td>");
                }else{
                    out.print("</td>");
                }
            } 
        }
        System.out.println("ped count " + status.getPed_count());
        for(int i= 0;i < 4 - status.getPed_count(); i ++){
            out.print("<td>N/A</td>");
        } 
        
        if(status.getSCE()!= null){
            if(status.getSCE().getExamScore()!=null){
                %>    
                    
                     <td><%=status.getSCE().getExamScore()%></td>
                 <%    
            }else{
                    
                     %>       
                             <td><%=status.getSCE().getExamStatus()%>
                          <%
                            //  Display the Evaluation link if the exam is SCE and the status is NC.
                            // Also verify that this link is available only to Admins/Super admins/TSR admins and the direct manager
                             if(status.getSCE().getExamStatus().equals("NC")) 
                             {
                                System.out.println("User emplid >>>>>>>> " + user.getEmplid() + "Future " + getEmployeeDetailForm.getEmployeeInfo().getFutureReportsToEmplID());
                                if(user.isAdmin() || user.isSuperAdmin() || user.isTsrAdmin() 
                                || (user.getEmplid().equals(getEmployeeDetailForm.getEmployeeInfo().getFutureReportsToEmplID())))
                                {
                          %>  
                                     <br>
                                     <a href="javascript:document.getElementById('evaluateFormNew<%=l%><%=m%>').submit();">(Evaluate)</a>    
                                      <form name="evaluateFormView<%=l%><%=m%>" id="evaluateFormView<%=l%><%=m%>" method="post" action="<%=sceUrl%>" target="_popup" >
                                        <input type="hidden" name="eventId" value="<%=eventId%>" id="eventId"/>
                                        <input type="hidden" name="emplId" value="<%=getEmployeeDetailForm.getEmployeeInfo().getEmplID()%>" id="emplId"/>
                                        <input type="hidden" name="productCode" value="<%=status.getProductcd()%>" id="productCode"/>
                                        <input type="hidden" name="action" value="view" id="action"/>
                                    </form>
                                    <form name="evaluateFormNew<%=l%><%=m%>" id="evaluateFormNew<%=l%><%=m%>" method="post" action="<%=sceUrl%>" target="_popup">
                                        <input type="hidden" name="eventId" value="<%=eventId%>" id="eventId"/>
                                        <input type="hidden" name="emplId" value="<%=getEmployeeDetailForm.getEmployeeInfo().getEmplID()%>" id="emplId"/>
                                        <input type="hidden" name="product" value="<%=status.getProductdesc()%>" id="product"/>
                                        <input type="hidden" name="productCode" value="<%=status.getProductcd()%>" id="productCode"/>
                                        <input type="hidden" name="evaluatorEmplId" value="<%=uSession.getUser().getId()%>" id="evaluatorEmplId"/>
                                        <input type="hidden" name="action" value="create" id="action"/>
                                    </form>
                             <%
                            }
                         
                        }
                         %>    
                             
                             </td>
                       <%      
                } 
                           
        } else{
            out.print("<td>N/A</td>");
        }
        
        //for manager function required?
        if(status.isManagerRequired()){
            out.print("<td><a href='"+ status.getLso() + "'>Link to LSO</a>" +  "</td>");
        }else{
            out.print("<td>N</td>");
        }
        
        //check null 
        Date today=new Date(); 
        
        
        
        if(status.getClassid()!=null && Integer.parseInt(status.getClassid())>=400 && Integer.parseInt(status.getClassid())<500 ){
            %>          
            
           <%
	    	    if (user.isAdmin() || user.isSuperAdmin()|| user.isTsrAdmin()) { 
            %>
             <td>No PLC Class
            <input type=image src="<%=AppConst.IMAGE_DIR%>/calendar.jpg"  border="0" height="17"  alt="See Other Available Dates" value="Change Date" name="changeDate" onclick="window.open('updateTrainingDate?emplid=<%=getEmployeeDetailForm.getEmployeeInfo().getEmplID()%>&product=<%=status.getProductdesc()%>&courseID=<%=status.getClassid()%>&m0=<%=mode0%>&m1=<%=mode1%>&m2=<%=mode2%>','_blank','width=400,height=250,scrollbars=yes');">            
            </td>
             
            <td><input type=image src="<%=AppConst.IMAGE_DIR%>/cancel.gif"  border="0" height="17"  alt="Cancel Training" value="Cancel Training" name="cancelTraining" onclick="if(window.confirm('Do you want to Cancel \'<%=status.getProductdesc()%>\' for <%=getEmployeeDetailForm.getEmployeeInfo().getPreferredName().replaceAll("'", "`")%> <%=getEmployeeDetailForm.getEmployeeInfo().getLastName().replaceAll("'", "`")%>?')) window.open('cancelTraining?emplid=<%=getEmployeeDetailForm.getEmployeeInfo().getEmplID()%>&oldclassid=<%=status.getClassid()%>&m0=<%=mode0%>&m1=<%=mode1%>&m2=<%=mode2%>','_blank','width=400,height=250,scrollbars=yes');"></td>                    
            <%
                }
                else{
                    
            %>
              <td>No PLC Class
            <image src="<%=AppConst.IMAGE_DIR%>/calendar.jpg"  border="0" height="17"   value="Change Date" name="changeDate" />     
            </td>
            
            <td>&nbsp;</td>                    
             <%
                }
            %>
                 
        <%}else     
        if(status.getClasschedule()!= null ){            
          //  if(today.compareTo(status.getClasschedule())<=0){  
           %>
            <%
	    	     if (user.isAdmin() || user.isSuperAdmin()|| user.isTsrAdmin()) { 
            %>
            <td><%=Util.formatDateShort(status.getClasschedule())%>
            <input type=image src="<%=AppConst.IMAGE_DIR%>/calendar.jpg"  border="0" height="17"  alt="See Other Available Dates" value="Change Date" name="changeDate" onclick="window.open('updateTrainingDate?emplid=<%=getEmployeeDetailForm.getEmployeeInfo().getEmplID()%>&product=<%=status.getProductdesc()%>&courseID=<%=status.getClassid()%>&m0=<%=mode0%>&m1=<%=mode1%>&m2=<%=mode2%>','_blank','width=400,height=250,scrollbars=yes');">            
            </td>

           
            <td><input type=image src="<%=AppConst.IMAGE_DIR%>/cancel.gif"  border="0" height="17"  alt="Cancel Training" value="Cancel Training" name="cancelTraining" onclick="if(window.confirm('Do you want to Cancel \'<%=status.getProductdesc()%>\' for <%=getEmployeeDetailForm.getEmployeeInfo().getPreferredName().replaceAll("'", "`")%> <%=getEmployeeDetailForm.getEmployeeInfo().getLastName().replaceAll("'", "`")%>?')) window.open('cancelTraining?emplid=<%=getEmployeeDetailForm.getEmployeeInfo().getEmplID()%>&oldclassid=<%=status.getClassid()%>&m0=<%=mode0%>&m1=<%=mode1%>&m2=<%=mode2%>','_blank','width=400,height=250,scrollbars=yes');"></td>                    
            <%
                }
                else{
            %>
            <td><%=Util.formatDateShort(status.getClasschedule())%>
            <image src="<%=AppConst.IMAGE_DIR%>/calendar.jpg"  border="0" height="17"  value="Change Date" name="changeDate" />            
            </td>
            <td>&nbsp;</td>                    
            <%
                }
            %>
            
          <%--  <%
            }else{
            %>
            <td><%=Util.formatDateShort(status.getClasschedule())%>
            </td>            
            <td>&nbsp;</td>
            <%
            } 
            --%>           
        <%    
        }else if("D".equalsIgnoreCase(status.getMostRecentUpdateFlag())){
        %>
            <%
	    	     if (user.isAdmin() || user.isSuperAdmin()|| user.isTsrAdmin()) { 
            %> 
            <td><DIV onclick ="window.open('addTraining?emplid=<%=getEmployeeDetailForm.getEmployeeInfo().getEmplID()%>&product=<%=status.getProductdesc()%>&productcd=<%=status.getProductcd()%>&m0=<%=mode0%>&m1=<%=mode1%>&m2=<%=mode2%>','_blank','width=400,height=250,scrollbars=yes');"><a href="#">Re-Enroll</a>
            <%--<input type=image src="<%=AppConst.IMAGE_DIR%>/calendar.jpg"  border="0" height="17"  alt="See Other Available Dates." value="Change Date" name="changeDate" onclick="window.open('addTraining?emplid=<%=getEmployeeDetailForm.getEmployeeInfo().getEmplID()%>&product=<%=status.getProductdesc()%>&productcd=<%=status.getProductcd()%>&m0=<%=mode0%>&m1=<%=mode1%>&m2=<%=mode2%>','_blank','width=400,height=250,scrollbars=yes');"> --%>           
            </td>
            <%
                 }
                 else{
            %>
            <td>Re-Enroll
            <%--<input type=image src="<%=AppConst.IMAGE_DIR%>/calendar.jpg"  border="0" height="17"  alt="See Other Available Dates." value="Change Date" name="changeDate" onclick="window.open('addTraining?emplid=<%=getEmployeeDetailForm.getEmployeeInfo().getEmplID()%>&product=<%=status.getProductdesc()%>&productcd=<%=status.getProductcd()%>&m0=<%=mode0%>&m1=<%=mode1%>&m2=<%=mode2%>','_blank','width=400,height=250,scrollbars=yes');"> --%>           
            </td>
            <%
                 }
            %>
            <td>&nbsp;</td>
        <%   
        }else if(emplStatus != null && emplStatus.equalsIgnoreCase("On-Leave") ){%>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
        <%
        }else if(status.getProductcd().equalsIgnoreCase("RVTONOPLC") || status.getProductcd().equalsIgnoreCase("HSLTOVZ")){
        %>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
        
        <%}else {%>
        
            <%
	    	     if (user.isAdmin() || user.isSuperAdmin()|| user.isTsrAdmin()) { 
            %>
            <td><div onclick="window.open('addTraining?emplid=<%=getEmployeeDetailForm.getEmployeeInfo().getEmplID()%>&product=<%=status.getProductdesc()%>&productcd=<%=status.getProductcd()%>&m0=<%=mode0%>&m1=<%=mode1%>&m2=<%=mode2%>','_blank','width=400,height=250,scrollbars=yes');"><a href="#">Enroll</a> </td>
            <%  
                 }
                 else{
                    
            %>
              <td>Enroll</td>  
            <%  
                 }
            %>
            <%--<input type=image src="<%=AppConst.IMAGE_DIR%>/calendar.jpg"  border="0" height="17"  alt="See Other Available Dates." value="Change Date" name="changeDate" onclick="window.open('addTraining?emplid=<%=getEmployeeDetailForm.getEmployeeInfo().getEmplID()%>&product=<%=status.getProductdesc()%>&productcd=<%=status.getProductcd()%>&m0=<%=mode0%>&m1=<%=mode1%>&m2=<%=mode2%>','_blank','width=400,height=250,scrollbars=yes');"> --%>           
            <td>&nbsp;</td>
        <%
        
        }
        if(status.getNotes() != null){
        %>
        <!--<td onmouseover="popUp(event,'t1', \'<%=status.getNotes()%>\')" onmouseout="popUp(event,'t1', '')">View notes</td>-->
        <td onmouseover='popUp(event,"t1", "<%=status.getNotes().replaceAll("'", "&#39;")%>")' onmouseout='popUp(event,"t1", "")'>View notes</td>
        <%
     
        }else{
            out.print("<td>&nbsp;</td>");
        }
    %>
        </tr>
    <%
     }
      %>
      
      <%--<tr><td colspan="11">      
            <button type="button" onclick="window.open('addProductClass?emplid=<%=getEmployeeDetailForm.getEmployeeInfo().getEmplID()%>','_blank','width=400,height=400,scrollbars=yes');">Add Product Training</button>
    </td></tr>--%>


    </TABLE>
    
    <%--Render Guest trainer assignments Changes made for RBU by jeevan--%>
     <%
        List guestTrainerList = getEmployeeDetailForm.getRbuGuestTrainers();
        if(guestTrainerList != null && guestTrainerList.size() > 0){
      %>  


    <TABLE  width="350px" class="blue_table">
        <tr>
        <TH colspan="3" align="left">Guest Trainer Assignment</TH>
        </TR>
        <TR>
        <TD bgcolor="#DDDDDD"><B>Product</B> </TD>
        <TD bgcolor="#DDDDDD"><B>Start Date</B> </TD>
        <TD bgcolor="#DDDDDD"><B>End Date</B> </TD>
        </TR>       
        <TR>
            <%
        System.out.println("status size ***** + " + guestTrainerList.size());
         for (Iterator iter = guestTrainerList.iterator(); iter.hasNext();){
            RBUGuestTrainersClassData guestTrainer = (RBUGuestTrainersClassData) iter.next();      
        %>
          <tr>  
         <td><%=guestTrainer.getProductDesc()%></td>
         <td><%=Util.formatDateShort(guestTrainer.getStartDate())%></td>
         <td><%=Util.formatDateShort(guestTrainer.getEndDate())%></td> 
         </tr>
         <%
            }
         %>  
        </TR>
    </TABLE>

    <%
        }
    %>

     <!--Training Material History-->
    <FORM action="#" method="post" name="material">
    <TABLE class="blue_table">
    <TR><TH colspan="7" align="left">Training Material History</TH></TR>
    <TR bgcolor="#DDDDDD">
    <TD>&nbsp;</TD>
    <TD><B>TN NO.</B></TD>
    <TD><B>TRM ORDER ID</B></TD>
    <TD><B>Status</B></TD>
    <TD><B>Material Description</B></TD>
    <TD><B>Order Date</B></TD> 
    <TD><B>Tracking Number</B></TD>
    </TR>
   
    <%
        for(int i=0;i<getEmployeeDetailForm.getTrainingMaterialHistoryInfo().size();i++){
        TrainingMaterialHistory data = (TrainingMaterialHistory)getEmployeeDetailForm.getTrainingMaterialHistoryInfo().elementAt(i);
    %>
    <TR CLASS="<%=(i%2==0)?"even":"odd"%>">
     <%
	     if (user.isAdmin() || user.isSuperAdmin()|| user.isTsrAdmin()) { 
    %>
    <TD><INPUT type="checkbox" name="reorder"  value="<%=data.getInvID()%>"></TD>    
    <%
         }
         else{
    %>
     <TD><INPUT type="checkbox" disabled name="reorder<%=i%>" value="<%=data.getInvID()%>"></TD>  
    <%
         }
    %>
    <TD><%=data.getInvID()%></TD>
    <TD><%=data.getTrmOrderID()%></TD>
    <TD><%=data.getStatus()%></TD>
    <TD><%=data.getMaterialDesc()%></TD>
    <TD><%=Util.formatDateShort(data.getOrderDate())%></TD>
    <TD><%=(data.getTrackingNumber()==null)?"":data.getTrackingNumber()%></TD>
    </TR>
    <%}%>
   
    <TR><TD colspan="7" align="left">
    <INPUT type="hidden" name="emplid" value="<%=info.getEmplID()%>">
    <%
	     if (user.isAdmin() || user.isSuperAdmin()|| user.isTsrAdmin()) { 
    %>
    <INPUT type="submit" name="commandreorder" value="Re-Order" onclick="return validateReorder()">
    <%
         }
         else{
    %>
    <INPUT type="submit" name="commandreorder"  disabled  value="Re-Order">
    <%
         }
    %>
    </TD></TR>

    </TABLE>    
    </FORM>
   
    <BR><BR>



 
    </TD></TR></TABLE> 
