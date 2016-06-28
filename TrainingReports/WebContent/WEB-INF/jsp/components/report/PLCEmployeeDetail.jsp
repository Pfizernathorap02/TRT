<%@ page language="java" contentType="text/html;charset=UTF-8"%>

<%@ page import="com.pfizer.PWRA.EmployeeDetailFacade.PDFHomeStudyStatus"%>
<%@ page import="com.pfizer.PWRA.EmployeeDetailFacade.PDFProduct"%>
<%@ page import="com.pfizer.PWRA.EmployeeDetailFacade.PLCExamStatus"%>
<%@ page import="com.pfizer.PWRA.EmployeeDetailFacade.PLCStatus"%>
<%@ page import="com.pfizer.PWRA.EmployeeDetailFacade.TrainingMaterialHistory"%>
<%@ page import="com.pfizer.PWRA.EmployeeDetailFacade.TrainingSchedule"%>
<%@ page import="com.pfizer.actionForm.PWRAGetEmployeeDetailForm"%>
<%@ page import="com.pfizer.db.Product"%>
<%@ page import="com.pfizer.webapp.AppConst"%>
<%@ page import="com.pfizer.webapp.user.UserSession"%>
<%@ page import="com.pfizer.webapp.wc.components.report.PLCEmployeeDetailWc"%>
<%@ page import="com.tgix.Utils.Util"%>
<%-- <%@ taglib uri="netui-tags-databinding.tld" prefix="netui-data"%>
<%@ taglib uri="netui-tags-html.tld" prefix="netui"%>
<%@ taglib uri="netui-tags-template.tld" prefix="netui-template"%> --%>
<%
    String mode0 = request.getParameter("m0")==null?"":request.getParameter("m0");
    String mode1 = request.getParameter("m1")==null?"":request.getParameter("m1");
    String mode2 = request.getParameter("m2")==null?"":request.getParameter("m2");
    boolean gnsmPage = (mode1.equalsIgnoreCase("GNSM"))?true:false;    
    boolean msepiPage = (mode1.equalsIgnoreCase("MSEPI"))?true:false;   
    /* Added for Vista Rx Spiriva enhancement
    */    
    boolean vrsPage = (mode1.equalsIgnoreCase("VRS"))?true:false; 
    /*End of addition */
    
    //try to get form bean, beacause it is not visible.
    PLCEmployeeDetailWc wc = (PLCEmployeeDetailWc)request.getAttribute(PLCEmployeeDetailWc.ATTRIBUTE_NAME);
    request.setAttribute("getEmployeeDetailForm",wc.getFormBean());
    PWRAGetEmployeeDetailForm getEmployeeDetailForm = wc.getFormBean();
    // Prepare Render Title
    String titleGeneral = "";
    //String titleSpecific = ""; 
    String eventId = "";
    if(gnsmPage){
        titleGeneral = "Geodon National Sales Meeting";
    }
    if(msepiPage){
        titleGeneral = "MS/Epi National Sales Meeting";
    }
    /* Added for Vista Rx Spiriva enhancement
    */  
    if (vrsPage){
        titleGeneral = "Vista RX Spiriva";
    }
    /* End of addition */
    if(mode1.equalsIgnoreCase("PDF")){
        titleGeneral = "POWERS Driving Force";
        eventId = AppConst.EVENTID_PDF;
    }else if(mode1.equalsIgnoreCase("SPF")){
        titleGeneral = "Steere Path Forward";
        eventId = AppConst.EVENTID_SPF;
    }
    
    titleGeneral = titleGeneral+" Detail";
    UserSession uSession = UserSession.getUserSession(request); 
    
    String sceUrl = "http://sce.pfizer.com/SCEWeb/evaluation/evaluateTR";
	
	String url = request.getRequestURL().toString();
	if ( url != null ) {
		url = url.toLowerCase();
	}
	if ( url != null 
		&& (url.indexOf("localhost") > 0 || url.indexOf("trt-stg.pfizer.com") > 0 )) {
		sceUrl = "http://sce-stg.pfizer.com/SCEWeb/evaluation/evaluateTR";	
	}    
    if ( url != null 
		&& (url.indexOf("trt-tst.pfizer.com") > 0)) {
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
</script>
<TABLE class="basic_table"><TR><TD><IMG src="/TrainingReports/resources/images/spacer.gif" width="15"></TD><TD>
    <BR><BR>
    <B>District:</B>&nbsp;
    <%=getEmployeeDetailForm.getEmployeeInfo().getDistrictDesc()%>
    <BR><BR>
    <%=titleGeneral%>
    <BR><BR>
    <P id="table_title" style="font-size:1.2em; font-weight:bold;">
    <%=getEmployeeDetailForm.getEmployeeInfo().getLastName()%>,
    <%=getEmployeeDetailForm.getEmployeeInfo().getPreferredName()%>
    </P>
                    
    <TABLE class="no_space_width" width="0" border=1>
    <TR><TD>                
    <%--RENDER EMPLOYEE INFO --%>      
    <TABLE class="blue_table" width="550px">
    <TR><TH colspan="3" align="left">Employee Information</TH></TR>
    <TR CLASS="even">
    <TD STYLE="font-weight: bold;">Employee Status</TD>
    <TD>
    <%
        String emplStatus= getEmployeeDetailForm.getEmployeeInfo().getStatus();
    	System.out.println("employee Status is "+emplStatus.length());
        if(emplStatus.equalsIgnoreCase("On-Leave")||emplStatus.equalsIgnoreCase("Terminated") ){ 
            out.println("<font color='red'><b>"+getEmployeeDetailForm.getEmployeeInfo().getStatus()+"</b></font>");
        }else{
            out.println(getEmployeeDetailForm.getEmployeeInfo().getStatus());
        }
    %>
    </TD>
    <TD ROWSPAN="14" VALIGN="top"><img src="<%=getEmployeeDetailForm.getEmployeeInfo().getImageURL()%>"/>    
    </TD>        
    </TR>
    <TR CLASS="odd">
    <TD STYLE="font-weight: bold;">Employee ID</TD>
    <TD>
    	<%=getEmployeeDetailForm.getEmployeeInfo().getEmplID()%>
    </TD>
    </TR>
    
    <TR CLASS="even">
    <TD STYLE="font-weight: bold;">Full Name</TD>
    <TD>
    <%=getEmployeeDetailForm.getEmployeeInfo().getLastName()%>
    <%=getEmployeeDetailForm.getEmployeeInfo().getPreferredName()%>
    </TD>
    </TR>

    <TR CLASS="odd">
    <TD STYLE="font-weight: bold;">Gender</TD>
    <TD>
    <%=Util.toEmpty(getEmployeeDetailForm.getEmployeeInfo().getGender())%>
    </TD>
    </TR>

    <TR CLASS="even">
    <TD STYLE="font-weight: bold;">Hire Date</TD>
    <TD>
    <%=Util.formatDateShort(getEmployeeDetailForm.getEmployeeInfo().getHireDate())%>
    </TD>
    </TR>

    <TR CLASS="odd">
    <TD STYLE="font-weight: bold;">Promotion Date</TD>
    <TD>
    <%=Util.formatDateShort(getEmployeeDetailForm.getEmployeeInfo().getPromotionDate())%>
    </TD>
    </TR>

    <TR CLASS="even">
    <TD STYLE="font-weight: bold;">E-Mail</TD>
    <TD>
         
      <a href="mailto:<%=getEmployeeDetailForm.getEmployeeInfo().getEmail()%>?subject=<%=titleGeneral%>">
      <%=Util.toEmpty(getEmployeeDetailForm.getEmployeeInfo().getEmail())%>      
      </a>        
        
    </TD> 
    </TR>

    <TR CLASS="odd">
    <TD STYLE="font-weight: bold;">Therapeutic Cluster</TD>
    <TD>
    <%=Util.toEmpty(getEmployeeDetailForm.getEmployeeInfo().getClusterCD())%>
    </TD>
    </TR>

    <TR CLASS="even">
    <TD STYLE="font-weight: bold;">Team</TD>
    <TD>
    <%=Util.toEmpty(getEmployeeDetailForm.getEmployeeInfo().getTeamCD())%>
    </TD>
    </TR>

    <TR CLASS="odd">
    <TD STYLE="font-weight: bold;">Role</TD>
    <TD>
    <%=Util.toEmpty(getEmployeeDetailForm.getEmployeeInfo().getTerritoryRole())%>
    </TD>
    </TR>

    <TR CLASS="even">
    <TD STYLE="font-weight: bold;">Territory Code</TD>
    <TD>
    <%=Util.toEmpty(getEmployeeDetailForm.getEmployeeInfo().getTerritoryID())%>
    </TD>
    </TR>

    <TR CLASS="odd">
    <TD STYLE="font-weight: bold;">Region</TD>
    <TD>
    <%=Util.toEmpty(getEmployeeDetailForm.getEmployeeInfo().getAreaDesc())%>
    </TD>
    </TR>

    <TR CLASS="even">
    <TD STYLE="font-weight: bold;">District</TD>
    <TD>
    <%=Util.toEmpty(getEmployeeDetailForm.getEmployeeInfo().getDistrictDesc())%>
    </TD>
    </TR>

    <TR CLASS="odd">
    <TD STYLE="font-weight: bold;">Reports to</TD>
    <TD>
    
    <%if(getEmployeeDetailForm.getEmployeeInfo().getReportToEmail()!=null){%>
        <a href="mailto:<%=getEmployeeDetailForm.getEmployeeInfo().getReportToEmail()%>?subject=<%=titleGeneral%>">
        <%=Util.toEmpty(getEmployeeDetailForm.getEmployeeInfo().getReportToLastName())%>,
        <%=Util.toEmpty(getEmployeeDetailForm.getEmployeeInfo().getReportToPreferredName())%>      
        </a>
    <%}%>
        
    </TD>    
    </TR>
    </TABLE>
    
    </TD>
    <td><img src="/TrainingReports/resources/images/spacer.gif" width="15"></td>
    <TD valign="top">
    
    
    <%--RENDER OVERALL STATUS --%>        
     <TABLE class="blue_table">
    <TR><TH colspan="2" align="left">Training Information</TH></TR>
    <TR bgcolor="#DDDDDD"><TD>&nbsp;</TD><TD>Overall Status</TD></TR>
    <%if (AppConst.EVENT_PDF.equalsIgnoreCase(mode1)) {%>
    <TR><TD>Home Study</TD><TD><%=Util.toEmpty(getEmployeeDetailForm.getOverallHomeStudyStatus())%></TD></TR>
    <TR><TD>PLC</TD><TD><%=Util.toEmpty(getEmployeeDetailForm.getOverallPLCStatus())%></TD></TR>
    <%} else if (AppConst.EVENT_SPF.equalsIgnoreCase(mode1)) {%>
    <TR><TD>SPF</TD><TD><%=Util.toEmpty(getEmployeeDetailForm.getOverallPLCStatus())%></TD></TR>
    <%} else if(gnsmPage){%>
        <TR><TD>Geodon National Sales Meeting</TD><TD><%=Util.toEmpty(getEmployeeDetailForm.getOverallStatus())%></TD></TR>
    <%} else if(msepiPage){%>
        <TR><TD>MS/Epi National Sales Meeting</TD><TD><%=Util.toEmpty(getEmployeeDetailForm.getOverallStatus())%></TD></TR>
    <%} else if(vrsPage){%>
        <TR><TD>Vista RX Spiriva</TD><TD><%=Util.toEmpty(getEmployeeDetailForm.getOverallStatus())%></TD></TR>
    <%}%>
    </TABLE>
    
    </TD></TR></TABLE>
    
    <BR><BR>    
    
    <%-- MS/Epi National Sales Meeting --%>    
    <%if(msepiPage){%>
    <%@ include file="/WEB-INF/jsp/PWRA/employeeDetail/msepi.jsp" %>
    <%  return;
    }   
    %>        
                  
    <%--Geodon National Sales Meeting STATUS --%>      
    <%if(gnsmPage){%>
        <%@ include file="/WEB-INF/jsp/PWRA/employeeDetail/gnsm.jsp" %>
    <%  return;
    }   
    %>
    <%if(vrsPage){%>
        <%@ include file="/WEB-INF/jsp/PWRA/employeeDetail/vrs.jsp" %>
    <%  return;
    }   
    %>        
        
    <%--RENDER PRODUCT ASSIGNMENT STATUS --%>    
    
    <TABLE class="blue_table" width="550px">
    <TR><TH colspan="2" align="left">Product Assignment</TH></TR>
    <TR><TD bgcolor="#DDDDDD">
    <B>Pre <%=mode1%> Team&frasl;Products</B>
    </TD><TD bgcolor="#DDDDDD">
    <B>Post <%=mode1%> Team&frasl;Products</B>    
    </TD></TR>    
    <TR><TD><B>Team:</B> 
    <%=(getEmployeeDetailForm.getProductAssignmentInfo().getPrePDFProductTeam()==null)?"":getEmployeeDetailForm.getProductAssignmentInfo().getPrePDFProductTeam()%>
    </TD>
    <TD><B>Team:</B> 
    <%=(getEmployeeDetailForm.getProductAssignmentInfo().getPostPDFProductTeam()==null)?"":getEmployeeDetailForm.getProductAssignmentInfo().getPostPDFProductTeam()%>
    </TD>
    </TR>
    <TR><TD><B>Product:</B> 
    <%    
        for(int i=0;i<getEmployeeDetailForm.getProductAssignmentInfo().getPrePDFProducts().size();i++){        
            String product = (String)getEmployeeDetailForm.getProductAssignmentInfo().getPrePDFProducts().elementAt(i);
            out.println(product);
            if(i!=getEmployeeDetailForm.getProductAssignmentInfo().getPrePDFProducts().size()-1){
                out.println(",");        
            }     
        }
    %>    
    </TD>
    <TD><B>Product:</B> 
    <%    
        for(int i=0;i<getEmployeeDetailForm.getProductAssignmentInfo().getPostPDFProducts().size();i++){        
            String product = (String)getEmployeeDetailForm.getProductAssignmentInfo().getPostPDFProducts().elementAt(i);
            out.println(product);
            if(i!=getEmployeeDetailForm.getProductAssignmentInfo().getPostPDFProducts().size()-1){
                out.println(",");        
            }     
        }
    %>    
    </TD>
    </TR>    
    </TABLE>  
    
    <BR><BR>
    
    <FORM action="#" method="post">
    
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
    <%  if(getEmployeeDetailForm.getTrainingMaterialHistoryInfo()!=null){
        for(int i=0;i<getEmployeeDetailForm.getTrainingMaterialHistoryInfo().size();i++){
        TrainingMaterialHistory data = (TrainingMaterialHistory)getEmployeeDetailForm.getTrainingMaterialHistoryInfo().elementAt(i);
    %>
    <TR CLASS="<%=(i%2==0)?"even":"odd"%>">
    <TD><INPUT type="checkbox" name="reorder<%=i%>" value="<%=data.getInvID()%>"></TD>    
    <TD><%=data.getInvID()%></TD>
    <TD><%=data.getTrmOrderID()%></TD>
    <TD><%=data.getStatus()%></TD>
    <TD><%=data.getMaterialDesc()%></TD>
    <TD><%=Util.formatDateShort(data.getOrderDate())%></TD>
    <TD><%=(data.getTrackingNumber()==null)?"":data.getTrackingNumber()%></TD>
    </TR>
    <%}}%>
    <TR><TD colspan="7" align="left">
    <INPUT type="hidden" name="emplid" value="<%=getEmployeeDetailForm.getEmployeeInfo().getEmplID()%>">
    <INPUT type="submit" name="commandreorder" value="Re-Order">
    </TD></TR>
    </TABLE>    
    </FORM>
    
    <BR><BR>

    <%if (AppConst.EVENT_PDF.equalsIgnoreCase(mode1)) {%>
    <TABLE class="blue_table" width="760px">    
    <TR><TH colspan="5" align="left">PDF Home Study Status</TH></TR>
    <TR bgcolor="#DDDDDD">
    <TD><B>Product</B></TD>
    <TD><B>Exam</B></TD>
    <TD><B>Score</B></TD>
    <TD><B>Status</B></TD>
    <TD><B>Completion Date</B></TD>
    </TR>    

    <%        
        for(int i=0;i<getEmployeeDetailForm.getPdfHomeStudyStatus().size();i++){
        PDFHomeStudyStatus data = (PDFHomeStudyStatus)getEmployeeDetailForm.getPdfHomeStudyStatus().elementAt(i);
    %>
    <TR CLASS="<%=(i%2==0)?"even":"odd"%>">
    <TD><%=data.getProductDesc()%></TD>
    <TD><%=(data.getPedagogueExam()==null)?"Pedagogue":data.getPedagogueExam()%></TD>
    <TD><%=(data.getScore()==null)?"":data.getScore()%></TD>
    <TD><%=data.getStatus()%></TD>
    <TD><%=Util.formatDateShort(data.getCompletionDate())%></TD>
    </TR>
    <%}%>
    
    </TABLE>
    <BR><BR>
    <%}%>
    
    <TABLE class="blue_table" width="760px">    
    <TR><TH colspan="6" align="left">
    <%if (AppConst.EVENT_PDF.equalsIgnoreCase(mode1)) {%>
    PDF PLC Status
    <%} else {%>
    SPF Status
    <%}%>
    </TH></TR>
    <TR bgcolor="#DDDDDD">
    <TD><B>Product</B></TD>
    <TD><B>Exam</B></TD>
    <TD><B>Score</B></TD>
    <TD><B>Status</B></TD>
    <TD><B>Completion Date</B></TD>
    <TD><B>Status</B></TD>
    </TR>    

    <%        
        for(int i=0;i<getEmployeeDetailForm.getPlcStatusInfo().size();i++){
        PLCStatus dataMain = (PLCStatus)getEmployeeDetailForm.getPlcStatusInfo().elementAt(i);
        int numRows = dataMain.getPlcExamStatusList().size();        
    %>
    
    
        <%
            for(int j=0;j<dataMain.getPlcExamStatusList().size();j++){
            PLCExamStatus data = (PLCExamStatus)dataMain.getPlcExamStatusList().elementAt(j);    
        %>  
    <TR CLASS="<%=(i%2==0)?"even":"odd"%>">        
    <%if(j==0){%>  
    <TD rowspan="<%=numRows%>"><%=dataMain.getProduct()%></TD>    
    <%}%>
    <TD><%=(data.getExamName()==null)?"":data.getExamName()%></TD>
    <TD>
    <%if ("SCE".equalsIgnoreCase(data.getExamType())) {%>
    <form name="evaluateFormView<%=i%><%=j%>" id="evaluateFormView<%=i%><%=j%>" method="post" action="<%=sceUrl%>" target="_popup">
        <input type="hidden" name="eventId" value="<%=eventId%>" id="eventId"/>
        <input type="hidden" name="emplId" value="<%=getEmployeeDetailForm.getEmployeeInfo().getEmplID()%>" id="emplId"/>
        <input type="hidden" name="productCode" value="<%=dataMain.getProductCode()%>" id="productCode"/>
        <input type="hidden" name="action" value="view" id="action"/>
    </form>
    <form name="evaluateFormNew<%=i%><%=j%>" id="evaluateFormNew<%=i%><%=j%>" method="post" action="<%=sceUrl%>" target="_popup">
        <input type="hidden" name="eventId" value="<%=eventId%>" id="eventId"/>
        <input type="hidden" name="emplId" value="<%=getEmployeeDetailForm.getEmployeeInfo().getEmplID()%>" id="emplId"/>
        <input type="hidden" name="product" value="<%=dataMain.getProduct()%>" id="product"/>
        <input type="hidden" name="productCode" value="<%=dataMain.getProductCode()%>" id="productCode"/>
        <input type="hidden" name="evaluatorEmplId" value="<%=uSession.getUser().getId()%>" id="evaluatorEmplId"/>
        <input type="hidden" name="action" value="create" id="action"/>
    </form>
    <%}%>
    <%
    if (data.getScore()==null){
        out.println("");        
    }else{
    %>
        &nbsp;
        <% if (data.getExamType().equalsIgnoreCase("SCE")){%>
            <a href="javascript:document.getElementById('evaluateFormView<%=i%><%=j%>').submit();"><%=data.getScore()%></a> 
        <%}else{%>
            <%=data.getScore()%></a> 
        <%}%>
    <%
    }    
    %>
    
    </TD>
    <TD>
    <%=data.getExamStatus()%>
    <%
        if(data.getExamStatus().equalsIgnoreCase("Not Complete")&&data.getExamType().equalsIgnoreCase("SCE")){
    %>
            <a href="javascript:document.getElementById('evaluateFormNew<%=i%><%=j%>').submit();">(Evaluate)</a>
    <%
        }        
    %>
    
    </TD>
    <TD><%=Util.formatDateShort(data.getCompletionDate())%></TD>

    <%if(j==0){%>
    <TD rowspan="<%=numRows%>">
    <%=dataMain.getStatus()%>
    </TD>
    <%}%>
        </TR>
    
        <%}%>
    
    <%}%>
 
    </TABLE> 
    
    
    <BR><BR>    
    <TABLE class="blue_table" width="550px">    
    <TR><TH colspan="3" align="left">Training Schedule</TH></TR>
    <TR bgcolor="#DDDDDD"> 
    <TD><B>Product</B></TD>
    <TD><B>Date</B></TD>
    <TD><B>Cancel</B></TD>
    </TR>
    <%        
        for(int i=0;i<getEmployeeDetailForm.getTrainingSchedule().size();i++){
        TrainingSchedule data = (TrainingSchedule)getEmployeeDetailForm.getTrainingSchedule().elementAt(i);
    %>
    <TR CLASS="<%=(i%2==0)?"even":"odd"%>">
    <TD><%=data.getCourseDescription()%></TD>
    <TD><%=Util.formatDateShort(data.getCourseSchedule())%>
    <input type=image src="<%=AppConst.IMAGE_DIR%>/calendar.jpg"  border="0" height="17"  alt="See Other Available Dates." value="Change Date" name="changeDate" onclick="window.open('updateTrainingDate?emplid=<%=getEmployeeDetailForm.getEmployeeInfo().getEmplID()%>&courseID=<%=data.getCourseID()%>&product=<%=data.getCourseDescription()%>&role=<%=getEmployeeDetailForm.getEmployeeInfo().getTerritoryRole()%>&m0=<%=mode0%>&m1=<%=mode1%>&m2=<%=mode2%>','_blank','width=400,height=250,scrollbars=yes');">            
    </TD>
    <TD>
    <input type=image src="<%=AppConst.IMAGE_DIR%>/cancel.gif"  border="0" height="17"  alt="Cancel Training." value="Cancel Training" name="cancelTraining" onclick="if(window.confirm('Do you want to Cancel \'<%=data.getCourseDescription()%>\' for <%=getEmployeeDetailForm.getEmployeeInfo().getPreferredName()%> <%=getEmployeeDetailForm.getEmployeeInfo().getLastName()%>?')) window.open('cancelTraining?emplid=<%=getEmployeeDetailForm.getEmployeeInfo().getEmplID()%>&courseID=<%=data.getCourseID()%>&product=<%=data.getCourseDescription()%>&role=<%=getEmployeeDetailForm.getEmployeeInfo().getTerritoryRole()%>&m0=<%=mode0%>&m1=<%=mode1%>&m2=<%=mode2%>','_blank','width=400,height=250,scrollbars=yes');">                    
    </TD>    
    </TR>
    <%}%>
    

    <%if(getEmployeeDetailForm.getCancelTraining().size() > 0){%>
    </TABLE>   
    <BR><BR>    
    <TABLE class="blue_table" width="550px">    
    <TR><TH colspan="4" align="left">Cancelled Training</TH></TR>
    <TR bgcolor="#DDDDDD"> 
    <TD><B>Product</B></TD>
    <TD><B>Date</B></TD>
    <TD><B>Reason</B></TD>
    <TD><B>Recover</B></TD>
    </TR>
    <%        
        for(int i=0;i<getEmployeeDetailForm.getCancelTraining().size();i++){
        TrainingSchedule data = (TrainingSchedule)getEmployeeDetailForm.getCancelTraining().elementAt(i);
    %>
    <TR CLASS="<%=(i%2==0)?"even":"odd"%>">
    <TD><%=data.getCourseDescription()%></TD>
    <TD><%=Util.formatDateShort(data.getCourseSchedule())%></TD>
    <TD><%=data.getCancelReason()%></TD>    
    
    <TD>    
     <input type=image src="<%=AppConst.IMAGE_DIR%>/trm/recover.gif"  border="0" height="17"  alt="Click to Recover." value="Recover" name="recover" onclick="javascript:if(window.confirm('Do you want to Recover \'<%=data.getCourseDescription()%>\' for <%=getEmployeeDetailForm.getEmployeeInfo().getPreferredName()%> <%=getEmployeeDetailForm.getEmployeeInfo().getLastName()%>?'))  window.open('getEmployeeDetail?commandchangetime=recoverTraining&emplid=<%=getEmployeeDetailForm.getEmployeeInfo().getEmplID()%>&courseid=<%=data.getCourseID()%>&m0=<%=mode0%>&m1=<%=mode1%>&m2=<%=mode2%>', '_self');">    
     </TD>    

    </TR>
    <%}%>    
    </TABLE> 
    <%}%>              
    </TD></TR></TABLE> 
