<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%-- <%@ taglib uri="netui-tags-databinding.tld" prefix="netui-data"%>
<%@ taglib uri="netui-tags-html.tld" prefix="netui"%>
<%@ taglib uri="netui-tags-template.tld" prefix="netui-template"%> --%>

<%@ page import="com.pfizer.db.ManagementSummaryReport"%>
<%@ page import="com.pfizer.db.P2lActivityStatus"%>
<%@ page import="com.pfizer.db.SceFull"%>
<%@ page import="com.pfizer.hander.SceHandler"%>
<%@ page import="com.pfizer.utils.DateComparator"%>
<%@ page import="com.pfizer.webapp.AppConst"%>
<%@ page import="com.pfizer.webapp.user.UserSession"%>
<%@ page import="com.pfizer.webapp.wc.components.report.phasereports.TrainingDetailWc"%>

<%@ page import="com.tgix.Utils.Util"%>
<%@ page import="java.awt.GraphicsEnvironment"%>
<%@ taglib uri="/WEB-INF/dynamic-include.tld" prefix="inc" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page import="java.util.List"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.Map"%>
<%@ page import="java.util.HashMap"%>
<%@ page import="java.util.Collections"%>
<%@ page import="java.util.Iterator"%>


<%
	TrainingDetailWc wc = (TrainingDetailWc)request.getAttribute(TrainingDetailWc.ATTRIBUTE_NAME);
    
    request.setAttribute("SCEEVAL",wc);
	
	String sceUrl = "http://sce.pfizer.com/SCEWeb/evaluation/evaluateTR";
	
	String url = request.getRequestURL().toString();
	if ( url != null ) {
		url = url.toLowerCase();
	}
	if ( url != null 
		&& ( url.indexOf("trt-stg.pfizer.com") > 0  || url.indexOf("tgix-dev.pfizer.com") > 0 )) {
		sceUrl = "http://sce-stg.pfizer.com/SCEWeb/evaluation/evaluateTR";	
	}
    if(url !=null && (url.indexOf("localhost") > 0))
    {
        sceUrl = "http://localhost:7001/SCEWeb/evaluation/evaluateTR";	
    }  
    if ( url != null 
		&& (url.indexOf("trt-tst.pfizer.com") > 0 )) {
		
        sceUrl = "http://sce-tst.pfizer.com/SCEWeb/evaluation/evaluateTR";
        //shannon for testing 
        //sceUrl = "http://localhost:7001/SCEWeb/evaluation/evaluateTR";		
	}
    if ( url != null 
		&& (url.indexOf("trt-dev.pfizer.com") > 0)) {
		sceUrl = "http://sce-dev.pfizer.com/SCEWeb/evaluation/evaluateTR";	
	}
    
    String flag = wc.getAccessFlag(); 
     
%>
<%!
    public String loginUser=null;
    public String UserName=null;
  
    public String drawRow(List status,String emplid, UserSession uSession, String actid, Map pedScores, boolean isDebug, String sceUrl, TrainingDetailWc wc) {
        //System.out.println("Inside draw row ");
        StringBuffer sb = new StringBuffer();
        loginUser = uSession.getUser().getId();
        UserName = uSession.getUser().getName();
        boolean headflag = false;
        String scoreVisible= uSession.getUser().getScoresFlag();
        System.out.println("scoreVisible"+scoreVisible);
        for (Iterator it = status.iterator();it.hasNext();) {
            P2lActivityStatus tmpstatus = (P2lActivityStatus)it.next(); 
            // Check to see if user has any record at all
            if ( !tmpstatus.hasRecordAtAll() ) {
                continue;
            }
            if ( tmpstatus.isComplete()) {
                // handle LEVEL=1 data
                // Level one records normally doesn't get rendered unless it's a single row activity
                // if so, it will be rendered both at the blue and white rows.
                if ( tmpstatus.getLevel() == 1 ) { 
                    String completestring = "";
                    if ( tmpstatus.isComplete() ) {
                        completestring = " - " + tmpstatus.getStatus();
                    }
                    sb.append("<tr><th colspan='6' style='font-size:14px;'><strong>" + tmpstatus.getActivityName() + completestring +"</strong></th></tr>");
                    sb.append("<tr id='greyscell'><td nowrap>Activity&nbsp;Type</td><td>Exam&nbsp;Type</td><td>Status</td><td >Date&nbsp;Taken</td><td >Score</td><td align='center'>Action</td></tr>");
                    sb.append("<tr><td colspan='1' style='font-size:14px;' nowrap>" + tmpstatus.getBuffer()+ tmpstatus.getActivityName() + "</td>");
                    sb.append("<td  style='font-size:14px;'>" + tmpstatus.getReportType() + "</td>");
                    /*Changed for bug fix - 05-Feb-2010 */
                    if ( tmpstatus.isSce() ) {
                        sb.append("<td  style='font-size:14px;'>" +  tmpstatus.getStatus()  +  "</td>");                        
                    } else {
                        sb.append("<td  style='font-size:14px;'>" +  tmpstatus.getStatus()  +  "</td>");
                    }                    
                    sb.append("<td  style='font-size:14px;'> " + tmpstatus.getEndDate()+ "  </td>");
                    /* Condition added for P2L scores hiding for CSO enhancement */
                    if ( tmpstatus.isPedagogue() && scoreVisible.equalsIgnoreCase("Y") ) {
                       sb.append("<td  style='font-size:14px;'>" + tmpstatus.getScore() + "</td>");
                    } /*End of addition */
                    else {   
                        if ( tmpstatus.isSce() ) {
                            /*Changed for Bug fix: 05-Feb-2010 */
                            sb.append("<td  style='font-size:14px;'>" +  getSceScore(emplid,tmpstatus,"4")  +  "</td>");                        
                        }
                        /* Condition added for P2L scores hiding for CSO enhancement */ 
                        else if (scoreVisible.equalsIgnoreCase("Y")) {
                            sb.append("<td  style='font-size:14px;'>" +  tmpstatus.getScore()  +  "</td>");
                        } /*End of addition */
                        else {
                            sb.append("<td  style='font-size:14px;'>" + "" +  "</td>");
                        }                        
                    }
                    /* Added for Bug fix */
                    if ( tmpstatus.isSce() ) {                        
                        sb.append("<td  style='font-size:14px;'>" + ("SCE".equals(tmpstatus.getScore())?createSceString(emplid,tmpstatus,loginUser,"4",0,sceUrl):"") + "</td>");
                    }
                    else if (P2lActivityStatus.specialCodes!=null && P2lActivityStatus.specialCodes.containsKey(tmpstatus.getCourseCode())) 
                    {
                        String envt =(String)P2lActivityStatus.specialCodes.get(tmpstatus.getCourseCode());
                        if ( tmpstatus.isRegistered() || tmpstatus.isAssigned() || tmpstatus.isComplete()||tmpstatus.isCancel() ) {
                            if ("Draft".equals(getSceStatus(emplid,tmpstatus,envt)) ) {
                                sb.append("<td  style='font-size:14px;'><a href='/TrainingReports/phase/handleSpecialCase?productCode=" + tmpstatus.getCourseCode() + "&type=" + envt + "&activitypk=" + actid + "&emplid=" + emplid + "&sactivitid=" +tmpstatus.getActivityId() + "' onClick='OpenSpecialCase();' target='myWin' >Edit</a></td>");
                                //sb.append("<td  style='font-size:14px;'>" + createSceString(emplid,tmpstatus,loginUser,envt, 0,sceUrl) + "</td>");
                            } else if ("Submitted".equals(getSceStatus(emplid,tmpstatus,envt))) {
                                sb.append("<td  style='font-size:14px;'>" + createSceViewLink(emplid,tmpstatus,loginUser, envt,sceUrl) + "</td>");
                            } else {
                                sb.append("<td  style='font-size:14px;'><a href='/TrainingReports/phase/handleSpecialCase?productCode=" + tmpstatus.getCourseCode() + "&type=" + envt + "&activitypk=" + actid + "&emplid=" + emplid + "&sactivitid=" +tmpstatus.getActivityId() + "' onClick='OpenSpecialCase();' target='myWin' >Evaluate</a></td>");
                            }
                        } else {
                            sb.append("<td  style='font-size:14px;'>&nbsp;</td>");
                        }
                    } 
                    else
                    {
                    sb.append("<td  style='font-size:14px;'>&nbsp;</td>");
                    }
                    continue;
                }
                
                // handle LEVEL=2 data
                if ( tmpstatus.getLevel() == 2 ) {
                    String completestring = "";
                    // This draws the status on the blue row
                    if ( tmpstatus.isComplete() || tmpstatus.isAssigned()) {
                        completestring = " - " + tmpstatus.getStatus();
                    }
                    sb.append("<tr><th colspan='6' style='font-size:14px;'><strong><a href='listReportAllStatus?activitypk="+tmpstatus.getActivityId()+"&section=Complete&pieIndex=0'>" + tmpstatus.getActivityName() + completestring +"</a></strong></th></tr>");
                    sb.append("<tr id='greyscell'><td nowrap>Activity&nbsp;Type</td><td >Exam&nbsp;Type</td><td >Status</td><td >Date&nbsp;Taken</td><td >Score</td><td  align='center'>Action</td></tr>");
                    System.out.println("asdfasdfasdfd:" + tmpstatus.getMaxLevel(0));
                    if ( tmpstatus.getMaxLevel(0) == 2) {
                        System.out.println("Getting inside maxlevel condition");
                        List tmpkids = new ArrayList();
                        tmpstatus.setRaiselevel(1);
                        tmpkids.add(tmpstatus);
                        System.out.println("Final level"+tmpstatus.getLevel());         
                        sb.append(drawRow(tmpkids,emplid,uSession,actid,pedScores,isDebug,sceUrl, wc));
                        continue;
                    }
                    sb.append(drawRow(tmpstatus.getKids(),emplid,uSession,actid,pedScores,isDebug,sceUrl, wc));
                    continue;
                }   
    
                // handle LEVEL>2 data
                if ( tmpstatus.getLevel() > 2 && tmpstatus.hasRecord(false)) {
                     System.out.println("Inside level greater than 2");     
                    // If child link is subscription, draw that row instead
                    if ( tmpstatus.isChildSubscription()) {
                        sb.append(drawRow(tmpstatus.getKids(),emplid,uSession,actid,pedScores,isDebug,sceUrl, wc));
                        continue;
                    }                
                    if ( isDebug ) {
                        String tmpStr = tmpstatus.getBuffer()+ tmpstatus.getActivityName() +  " - " + tmpstatus.getActivityId() + " - " + tmpstatus.getActLabelName() + " - " + tmpstatus.getCourseCode();
                        sb.append("<tr><td colspan='1' style='font-size:14px;' nowrap><a href='listReportAllStatus?activitypk="+tmpstatus.getActivityId()+"&section=Complete&pieIndex=0'>" + tmpStr + "</a></td>");
                    } else {
                        sb.append("<tr><td colspan='1' style='font-size:14px;' nowrap><a href='listReportAllStatus?activitypk="+tmpstatus.getActivityId()+"&section=Complete&pieIndex=0'>" + tmpstatus.getBuffer()+ tmpstatus.getActivityName() + "</a></td>");
                    }
                    sb.append("<td  style='font-size:14px;'>"  + tmpstatus.getReportType() +  "</td>");
                    
                    if ( P2lActivityStatus.specialCodes!=null && P2lActivityStatus.specialCodes.containsKey(tmpstatus.getCourseCode())) {
                        String envt =(String)P2lActivityStatus.specialCodes.get(tmpstatus.getCourseCode());
                        sb.append("<td  style='font-size:14px;'>" +  getSceStatus(emplid,tmpstatus,envt) +  "</td>");  
                    } else {
                        sb.append("<td  style='font-size:14px;'>" +  tmpstatus.getStatus()  +  "</td>");
                    }                 
                    sb.append("<td  style='font-size:14px;'> " + tmpstatus.getEndDate()+ "  </td>");

                    if ( tmpstatus.isSce() ) {
                        //System.out.println("score call");
                        sb.append("<td  style='font-size:14px;'>" +  getSceScore(emplid,tmpstatus,"4")  +  "</td>");                        
                    } 
                    /* Condition added for P2L scores for CSO enhancement */
                    else if (scoreVisible.equalsIgnoreCase("Y")) {
                        sb.append("<td  style='font-size:14px;'>" + tmpstatus.getScore() + "</td>");
                    } /* End of addition */
                    else {
                        sb.append("<td  style='font-size:14px;'>" + " " + "</td>");
                    }                            

                    if ( tmpstatus.isSce() ) {                        
                        sb.append("<td  style='font-size:14px;'>" + ("SCE".equals(tmpstatus.getScore())?createSceString(emplid,tmpstatus,loginUser,"4",0,sceUrl):"") + "</td>");
                    } else if (P2lActivityStatus.specialCodes!=null && P2lActivityStatus.specialCodes.containsKey(tmpstatus.getCourseCode())) 
                    {
                        String envt =(String)P2lActivityStatus.specialCodes.get(tmpstatus.getCourseCode());
                        if ( tmpstatus.isRegistered() || tmpstatus.isAssigned() || tmpstatus.isComplete()||tmpstatus.isCancel() ) {
                            if ("Draft".equals(getSceStatus(emplid,tmpstatus,envt)) ) {
                                sb.append("<td  style='font-size:14px;'><a href='/TrainingReports/phase/handleSpecialCase?productCode=" + tmpstatus.getCourseCode() + "&type=" + envt + "&activitypk=" + actid + "&emplid=" + emplid + "&sactivitid=" +tmpstatus.getActivityId() + "' onClick='OpenSpecialCase();' target='myWin' >Edit</a></td>");
                                //sb.append("<td  style='font-size:14px;'>" + createSceString(emplid,tmpstatus,loginUser,envt, 0,sceUrl) + "</td>");
                            } else if ("Submitted".equals(getSceStatus(emplid,tmpstatus,envt))) {
                                sb.append("<td  style='font-size:14px;'>" + createSceViewLink(emplid,tmpstatus,loginUser, envt,sceUrl) + "</td>");
                            } else {
                                sb.append("<td  style='font-size:14px;'><a href='/TrainingReports/phase/handleSpecialCase?productCode=" + tmpstatus.getCourseCode() + "&type=" + envt + "&activitypk=" + actid + "&emplid=" + emplid + "&sactivitid=" +tmpstatus.getActivityId() + "' onClick='OpenSpecialCase();' target='myWin' >Evaluate</a></td>");
                            }
                        } else {
                            sb.append("<td  style='font-size:14px;'>&nbsp;</td>");
                        }
                    } else {                        
                        sb.append("<td  style='font-size:14px;'>&nbsp;</td>");
                    }
                    List kids = tmpstatus.getKids();
                    sb.append(drawRow(kids,emplid,uSession,actid,pedScores,isDebug,sceUrl, wc));
                } 
            } 
        }
        return sb.toString();
    }
    
    public String getSceStatus(String emplid,P2lActivityStatus status, String eventId) {
        SceHandler sHandler = new SceHandler();
        SceFull[] sce = sHandler.getSalesCallEvaluationFull2( emplid ,status.getActivityId()+"",eventId );
        
        if ( sce != null && sce.length > 0 ) {
            if ("4".equals(eventId)){
                return sce[0].getRating();
            } else {
                return sce[0].getStatus();
            }
        } else {
            return "Registered";
        }
    }
    public String getSceScore(String emplid,P2lActivityStatus status, String eventId) {
        SceHandler sHandler = new SceHandler();
        SceFull[] sce = sHandler.getSalesCallEvaluationFull2( emplid ,status.getActivityId()+"",eventId );
        
        if ( sce != null && sce.length > 0 ) {
            eventId = sce[0].getEventId();
            if ("4".equals(eventId) || sce[0].getRating()!=null || sce[0].getRating() !=""){
                return sce[0].getRating();
            } else {
                return sce[0].getStatus();
            }
        } else {
            return "";
        }
    }
    
    public SceFull getSce(String emplid,P2lActivityStatus status, String eventId) {
        SceHandler sHandler = new SceHandler();
        SceFull[] sce = sHandler.getSalesCallEvaluationFull2( emplid ,status.getActivityId()+"",eventId );
        if ( sce != null && sce.length > 0 ) {
                return sce[0];
        }
        return null;
    }
    public String createSceViewLink(String emplid,P2lActivityStatus status, String loginUserId, String eventId, String sceUrl) {
        StringBuffer sb = new StringBuffer();
                    
        String formString = "";
        
          
        formString =    "<form name='evaluateFormView" + status.getActivityId() + "' id='evaluateFormView' method='post' action='" + sceUrl + "' target='_popup'> " +
                            "<input type='hidden' name='emplId' value='" + emplid + "' id='emplId'/> " +
                            "<input type='hidden' name='eventId' value='" + eventId + "' id='eventId'/> " +
                            "<input type='hidden' name='productCode' value='" + status.getActivityId() + "' id='productCode'/> " +
                            "<input type='hidden' name='action' value='view' id='action'/> " +
                        "</form><a href=\"javascript:document.domain = 'pfizer.com';  document.evaluateFormView" + status.getActivityId() + ".submit();\">View&nbsp;Evaluation</a>";
        return formString;
    }
    public String createSceString(String emplid,P2lActivityStatus status, String loginUserId, String eventId, int average, String sceUrl) {
        StringBuffer sb = new StringBuffer();
        SceFull rating = getSce(emplid, status, eventId);
        System.out.println("rating:" + rating);
        String formString = "";
        String specialStr = "<input type='hidden' name='examScore' value='" + average + "' id='examScore'/>";
        
        if ( rating != null && !Util.isEmpty(rating.getRating()) && !"Draft".equals(rating.getStatus())) {
            
            formString =    "<form name='evaluateFormView" + status.getActivityId() + "_" + status.getParentid() + "' id='evaluateFormView' method='post' action='" + sceUrl + "' target='_popup'> " +
                                "<input type='hidden' name='emplId' value='" + emplid + "' id='emplId'/> " +
                                "<input type='hidden' name='eventId' value='" + rating.getEventId() + "' id='eventId'/> " +
                                "<input type='hidden' name='productCode' value='" + rating.getProductCd() + "' id='productCode'/> " +
                                "<input type='hidden' name='action' value='view' id='action'/> " +
                            "</form><a href=\"javascript:document.domain = 'pfizer.com';  document.evaluateFormView" + status.getActivityId() + "_" + status.getParentid() + ".submit();\">View&nbsp;Evaluation</a>";
        } else {
            String actingString = "Evaluate";
            if ( rating!= null && "Draft".equals(rating.getStatus()) ) {
                actingString = "Edit";
            }
            System.out.println("status.getActivityId()"+status.getActivityId());
            System.out.println(" status.getParentid()"+ status.getParentid());
            System.out.println("emplid"+emplid);
            System.out.println("eventId"+eventId);
            System.out.println("status.getActivityName() "+status.getActivityName());
            System.out.println("status.getActivityName() "+status.getActivityName());
            System.out.println("status.getActivityName() "+status.getActivityName());
            
            formString =    "<form name='evaluateFormNew" + status.getActivityId() + "_" + status.getParentid() + "' id='evaluateFormNew' method='post' action='" + sceUrl + "' target='_popup'> " +
                                "<input type='hidden' name='emplId' value='" + emplid + "' id='emplId'/> " +
                                "<input type='hidden' name='eventId' value='" + eventId + "' id='eventId'/> " +
                                "<input type='hidden' name='product' value='" + status.getActivityName() + "' id='product'/> " +
                                "<input type='hidden' name='productCode' value='" + status.getActivityId() + "' id='productCode'/> " +
                                "<input type='hidden' name='evaluatorEmplId' value='" + loginUserId + "' id='evaluatorEmplId'/> ";
            if ( average > 0 ) {
                formString = formString + specialStr;
            }                  
                formString = formString + "<input type='hidden' name='action' value='create' id='action'/> " +
                    "</form><a href=\"javascript:document.domain = 'pfizer.com'; document.evaluateFormNew" + status.getActivityId() + "_" + status.getParentid() + ".submit();\">" + actingString + "</a>" ;            
        }
        return formString;
    }
    
    /* This function is added to decide whether Feedback form is to be displayed or not */
    public boolean isDisplayFeedbackForm(String trackid)
    {
        SceHandler sHandler = new SceHandler();
        int count= sHandler.getFeedbackTrackMapping(trackid);
        System.out.println(" Training Details"+count);
        if(count>0)
        {
            return true;
        }
        else
        {
            return false;
        }
        
    }
%>
<script type="text/javascript" language="JavaScript">
    function update() {
        window.location.reload(); /*please use this only when there are no 'form submits' on the page, to avoid resubmit*/
    }
    function OpenSpecialCase() { 
        window.name = "specialcase";
        var myWin = window.open("","myWin","height=800,width=1024,scrollbars=yes,resizable=yes,statusbar=no,menubar=no,toolbar=no,dependent=yes"); 
    } 
    function OpenSceCase() { 
        window.name = "SceCase";
        var myWin = window.open("","myWin","height=800,width=1024,scrollbars=yes,resizable=yes,statusbar=no,menubar=no,toolbar=no,dependent=yes");
    } 
    
    function OpenSceEval() { 
    window.name = "SceEval";
   var myWin = window.open("","myWin","height=800,width=1024,scrollbars=yes,resizable=yes,statusbar=no,menubar=no,toolbar=no,dependent=yes") ;
    }
    
    function SceTab(view,hideA){
    var scetab = document.getElementById('sce');
    var phasetab = document.getElementById('phasedtraining_tabLinks_on');
    
            if (view.style.display="none") {
                view.style.display = "block";
                hideA.style.display="none";
                
                }
        scetab.id="phasedtraining_tabLinks_on";
        phasetab.id="sce";
    }
</script>


<table class="datagrid">
    	
    <tr>
        <td align="left" style="background-color:#E8EEF7;">        
        	<div class="phasedtraining_wrapper">
            
				<div class="phasedtraining_whitebox">
                	
                    <table class="phasedtraining_table" id="p2ltabs">
                    
                    <%
                        P2lActivityStatus status = wc.getStatus();
                        Map scores = new HashMap();
                        status.getAveragePedScores(scores);
                        List kids = status.getKids();
                        //System.out.println("KIDS SIZE"+kids.size());  
                                                
                        if ( (kids.size() == 0 || !status.childHasRecord() || status.getMaxLevel(0) == 1 )) {
                            kids = new ArrayList();
                            kids.add(status);
                        }
                        // This is special CPT logic.  check to see if any record in the structure.  What is different between
                        // CPT and other reports is that the child records does not have records that normally
                        // would attach itself to a structure.  There are many CPT records that are not attached to
                        // it structure but we have to attach them ourselfs.
                        if ( status.getMaxLevel(0) == 1) {
                            kids = new ArrayList();
                            kids.add(status);
                        }
                        
                        /* 08-Oct-2009
                        This condition is added to ensure sorting of completed records 
                        in the descending order of completion date */
// Start: Uncommented for Major Enhancement 3.6
                        //System.out.println("KIDS SIZE"+kids.size());                       
                        if(kids.size()>1)
                        {   
                           Collections.sort(kids, new DateComparator());
                           Collections.reverse(kids);                           
                        } 
// Ends here
                    %>

                    <%=drawRow(kids,wc.getEmployee().getEmplId(),wc.getUserSesion(),wc.getActivityPk(),scores, wc.isDebug(),sceUrl, wc)%>

                    
                    </table>
                    
                    
                    
                </div>
            </div>
        </td>
    </tr>
</table>
                      
                     





 
