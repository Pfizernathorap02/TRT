<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.pfizer.db.P2lActivityStatus"%>
<%@ page import="com.pfizer.db.P2lTrackPhase"%>
<%@ page import="com.pfizer.db.SceFull"%>
<%@ page import="com.pfizer.hander.SceHandler"%>
<%@ page import="com.pfizer.webapp.AppConst"%>
<%@ page import="com.pfizer.webapp.AppQueryStrings"%>
<%@ page import="com.pfizer.webapp.user.User"%>
<%@ page import="com.pfizer.webapp.user.UserSession"%>
<%@ page import="com.pfizer.webapp.user.UserTerritory"%>
<%@ page import="com.pfizer.db.Employee"%>
<%@ page
	import="com.pfizer.webapp.wc.components.report.phasereports.PhaseTrainingDetailWc"%>
<%@ page
	import="com.pfizer.webapp.wc.components.report.phasereports.TrainingSummaryWc"%>
<%@ page import="com.tgix.Utils.Util"%>
<%@ page import="java.math.BigDecimal"%>
<%@ page import="com.pfizer.utils.DateComparator"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.Collections"%>
<%@ page import="java.util.Map"%>
<%@ page import="java.util.HashMap"%>
<%@ page import="java.util.Iterator"%>
<%@ taglib uri="/WEB-INF/dynamic-include.tld" prefix="inc"%>

<%
	PhaseTrainingDetailWc wc = (PhaseTrainingDetailWc)request.getAttribute(PhaseTrainingDetailWc.ATTRIBUTE_NAME);
    
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
  
    public String drawRow(List status,String emplid, UserSession uSession, String actid, Map pedScores, boolean isDebug, String sceUrl, PhaseTrainingDetailWc wc) {
        //System.out.println("Inside draw row ");
        StringBuffer sb = new StringBuffer();
        loginUser = uSession.getUser().getId();
        String loginUserEmailId = uSession.getUser().getEmail();
        //System.out.println("loginUser emplId**********"+loginUser);
        UserName = uSession.getUser().getName();
        boolean headflag = false;
        String scoreVisible= uSession.getUser().getScoresFlag();
        //System.out.println("scoreVisible"+scoreVisible);
        String employeeNtId = null;
        String loginUserNtId = null;
       // if(wc != null && wc.getEmployee() != null){
            
         //   employeeNtId =  SceHandler.getNtId(wc.getEmployee().getEmplId());
         //    System.out.println(" inside check  employeeNtId"+employeeNtId);
       // }
     //System.out.println("loginUser loginUserEmailId**********"+loginUserEmailId); 
     //System.out.println("Employee emplid:"+emplid); 
     //System.out.println("loginUser emplId**********"+loginUser);   
     employeeNtId =  SceHandler.getNtId("employee",emplid,null);  
     System.out.println("loginUser in phase training Details is "+loginUser);
     System.out.println("loginUserEmailId.toLowerCase() in phase training Details is "+loginUserEmailId.toLowerCase());
      loginUserNtId=  SceHandler.getNtId("user",loginUser,loginUserEmailId.toLowerCase());
     System.out.println("loginUserNtId in phase training Details is "+loginUserNtId);
     System.out.println("employeeNtId in phase training Details is "+employeeNtId);   
     // System.out.println("product"+employeeNtId);      
        
        for (Iterator it = status.iterator();it.hasNext();) {
            P2lActivityStatus tmpstatus = (P2lActivityStatus)it.next(); 
            // Check to see if user has any record at all
           //System.out.println("tmpstatus.getActivityId()"+tmpstatus.getActivityId());
           //System.out.println("tmpstatus.getActivityname"+tmpstatus.getActivityName());
            //System.out.println("tmpstatus.getCourseCode()"+tmpstatus.getCurrentAttempt()); 
           if ( !tmpstatus.hasRecordAtAll() ) {
                 //System.out.println("Inside hasRecordAtAll"+tmpstatus.getActivityName()); 
                continue;
            }
            if ( ( tmpstatus.hasRecord("CPT".equals(wc.getTrack().getTrackType())) || tmpstatus.isComplete())  ) {
                // handle LEVEL=1 data
                // Level one records normally doesn't get rendered unless it's a single row activity
                // if so, it will be rendered both at the blue and white rows.
                if ( tmpstatus.getLevel() == 1 ) { 
                    String completestring = "";
                    if ( tmpstatus.isComplete() ) {
                        completestring = " - " + tmpstatus.getStatus();
                    }
                    sb.append("<tr><th colspan='5' style='font-size:14px;'><strong><a href='listReportAllStatus?activitypk="+tmpstatus.getActivityId()+"&section=Complete&pieIndex=0'>" + tmpstatus.getActivityName() + completestring +"</a></strong></th></tr>");
                    sb.append("<tr id='greyscell'><td nowrap>Activity&nbsp;Type</td><td>Status</td><td >Status&nbsp;Date</td><td >Score</td><td align='center'>Action</td></tr>");
                    sb.append("<tr><td colspan='1' style='font-size:14px;' nowrap><a href='listReportAllStatus?activitypk="+tmpstatus.getActivityId()+"&section=Complete&pieIndex=0'>" + tmpstatus.getBuffer()+ tmpstatus.getActivityName() + "</a></td>");
                    //sb.append("<td  style='font-size:14px;'>" + tmpstatus.getReportType() + "</td>");
                    /*Changed for bug fix - 05-Feb-2010 */
                    if ( tmpstatus.isSce() ) {
                        sb.append("<td  style='font-size:14px;'>" +  tmpstatus.getStatus()  +  "</td>");                        
                    } else {
                        sb.append("<td  style='font-size:14px;'>" +  tmpstatus.getStatus()  +  "</td>");
                    }                    
                    sb.append("<td  style='font-size:14px;'> " + tmpstatus.getStatusDate()+ "  </td>");
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
                        //sb.append("<td  style='font-size:14px;'>" + (("SCE".equals(tmpstatus.getScore()))?createSceString(emplid,tmpstatus,loginUser,"4",0,sceUrl):"") + "</td>");
                        String scoreValue = getSceScore(emplid,tmpstatus,"4");
                        //String productCd = String.valueOf(tmpstatus.getActivityId()); //ex: "2700845"
                        //System.out.println("$productCd:::"+productCd);
                        //SceFull rating = getSce(emplid, tmpstatus, "4");
                        String productCd = String.valueOf(tmpstatus.getActivityId());//rating.getProductCd();
                        String product= tmpstatus.getActivityName();
                       
                        String scoreLegend = SceHandler.getScoreLegend(scoreValue);
                        
                        String linkParamsStr = "product="+product+"&employeeNtId="+employeeNtId+"&loginUserNtId="+loginUserNtId+"&emplid="+ emplid +"&productCode=" + productCd+"&eventId=4";
                        
                      	//2020 Q3: start of muzees for multiple evaluation
                        String sceScore=getSceScore(emplid,tmpstatus,"4");
                        boolean multipleEvaluation=false;
                        if(!"".equals(sceScore)){
                        multipleEvaluation=SceHandler.checkMultipleEvaluation(emplid,tmpstatus.getActivityId()+"","4");
                        }
                        boolean isMapped=SceHandler.isCourseMapped(product);
                        if(tmpstatus.isRegistered() && "".equals(getSceScore(emplid,tmpstatus,"4"))){
                            sb.append("<td  style='font-size:14px;'><a href='/TrainingReports/redirectToSCE.jsp?"+linkParamsStr+"&linkName=evaluate' onClick='return OpenSpecialCase("+isMapped+");' target='myWin' >Evaluate</a></td>");
                        }
                        else if(tmpstatus.isRegistered()&&!SceHandler.isLMSMapped1(emplid,  tmpstatus.getActivityId()+"","4",scoreLegend)){
                            sb.append("<td  style='font-size:14px;'><a href='/TrainingReports/redirectToSCE.jsp?"+linkParamsStr+"&linkName=reEvaluate' onClick=' return OpenSpecialCase("+isMapped+");' target='myWin' >Re-Evaluate</a>");
                            sb.append("<br><a href='/TrainingReports/redirectToSCE.jsp?"+linkParamsStr+"&linkName=viewEvaluations' onClick='OpenSpecialCase();' target='myWin' >View&nbsp;Evaluation(s)</a></td>");
                        }else if(tmpstatus.isRegistered() && multipleEvaluation){
                        	sb.append("<td  style='font-size:14px;'><a href='/TrainingReports/redirectToSCE.jsp?"+linkParamsStr+"&linkName=evaluate' onClick='return OpenSpecialCase("+isMapped+");' target='myWin' >Re-Certify</a>");
                        	sb.append("<br><a href='/TrainingReports/redirectToSCE.jsp?"+linkParamsStr+"&linkName=viewEvaluations' onClick='OpenSpecialCase();' target='myWin' >View&nbsp;Evaluation(s)</a></td>");
                        }else if(scoreLegend!=null ){
                            sb.append("<td  style='font-size:14px;'><a href='/TrainingReports/redirectToSCE.jsp?"+linkParamsStr+"&linkName=viewEvaluations' onClick='OpenSpecialCase();' target='myWin' >View&nbsp;Evaluation(s)</a></td>");
                        }
                        else{
                            sb.append("<td style='font-size:14px;'></td>"); 
                        }
                    } 
                   
                    else if (P2lActivityStatus.specialCodes!=null && P2lActivityStatus.specialCodes.containsKey(tmpstatus.getCourseCode())) 
                    {
                        String envt =(String)P2lActivityStatus.specialCodes.get(tmpstatus.getCourseCode());
                          /* Modified for 'In Progress' status by Meenakshi*/
                        if ( tmpstatus.isRegistered() || tmpstatus.isAssigned() || tmpstatus.isComplete()||tmpstatus.isCancel() ||tmpstatus.isInProgress()) {
                            if ("Draft".equals(getSceStatus(emplid,tmpstatus,envt)) && tmpstatus.isRegistered() ) {
                                sb.append("<td  style='font-size:14px;'><a href='/TrainingReports/phase/handleSpecialCase?productCode=" + tmpstatus.getCourseCode() + "&type=" + envt + "&activitypk=" + actid + "&emplid=" + emplid + "&sactivitid=" +tmpstatus.getActivityId() + "' onClick='OpenSpecialCase();' target='myWin' >Edit</a></td>");
                                //sb.append("<td  style='font-size:14px;'>" + createSceString(emplid,tmpstatus,loginUser,envt, 0,sceUrl) + "</td>");
                            } else if ("Submitted".equals(getSceStatus(emplid,tmpstatus,envt))) {
                                sb.append("<td  style='font-size:14px;'>" + createSceViewLink(emplid,tmpstatus,loginUser, envt,sceUrl) + "</td>");
                            }
                            else if(tmpstatus.isRegistered()){
                                sb.append("<td  style='font-size:14px;'><a href='/TrainingReports/phase/handleSpecialCase?productCode=" + tmpstatus.getCourseCode() + "&type=" + envt + "&activitypk=" + actid + "&emplid=" + emplid + "&sactivitid=" +tmpstatus.getActivityId() + "' onClick='OpenSpecialCase();' target='myWin' >Evaluate</a></td>");
                                //System.out.println("**** Status ***-"+tmpstatus.getStatus());
                                //System.out.println("**** Status ***-"+tmpstatus.getActivityId());
                            }else if(tmpstatus.isCancel())
                            sb.append("<td  style='font-size:14px;'>&nbsp;</td>");
                            
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
                    sb.append("<tr><th colspan='5' style='font-size:14px;'><strong><a href='listReportAllStatus?activitypk="+tmpstatus.getActivityId()+"&section=Complete&pieIndex=0'>" + tmpstatus.getActivityName() + completestring +"</a></strong></th></tr>");
                    sb.append("<tr id='greyscell'><td nowrap>Activity&nbsp;Type</td><td >Status</td><td >Status&nbsp;Date</td><td >Score</td><td  align='center'>Action</td></tr>");
                    //System.out.println("asdfasdfasdfd:" + tmpstatus.getMaxLevel(0));
                    if ( tmpstatus.getMaxLevel(0) == 2) {
                        //System.out.println("Getting inside maxlevel condition");
                        List tmpkids = new ArrayList();
                        tmpstatus.setRaiselevel(1);
                        tmpkids.add(tmpstatus);
                        //System.out.println("Final level"+tmpstatus.getLevel());         
                        sb.append(drawRow(tmpkids,emplid,uSession,actid,pedScores,isDebug,sceUrl, wc));
                        continue;
                    }
                    sb.append(drawRow(tmpstatus.getKids(),emplid,uSession,actid,pedScores,isDebug,sceUrl, wc));
                    continue;
                }   
    
                // handle LEVEL>2 data
                if ( tmpstatus.getLevel() > 2  && tmpstatus.hasRecord(false)) {
                     //System.out.println("Inside level greater than 2");     
                    // If child link is subscription, draw that row instead
                   if ( tmpstatus.isChildSubscription()) {
                    //System.out.println("Inside child subscription1"+tmpstatus.getActivityName());
                       sb.append(drawRow(tmpstatus.getKids(),emplid,uSession,actid,pedScores,isDebug,sceUrl, wc));
                    //System.out.println("Inside child subscription2"+tmpstatus.getActivityName());   
                        continue;
                    }      
                    if ( isDebug ) {
                        String tmpStr = tmpstatus.getBuffer()+ tmpstatus.getActivityName() +  " - " + tmpstatus.getActivityId() + " - " + tmpstatus.getActLabelName() + " - " + tmpstatus.getCourseCode();
                        sb.append("<tr><td colspan='1' style='font-size:14px;' nowrap><a href='listReportAllStatus?activitypk="+tmpstatus.getActivityId()+"&section=Complete&pieIndex=0'>" + tmpStr + "</a></td>");
                    } else {
                        sb.append("<tr><td colspan='1' style='font-size:14px;' nowrap><a href='listReportAllStatus?activitypk="+tmpstatus.getActivityId()+"&section=Complete&pieIndex=0'>" + tmpstatus.getBuffer()+ tmpstatus.getActivityName() + "</a></td>");
                    }
                   // sb.append("<td  style='font-size:14px;'>"  + tmpstatus.getReportType() +  "</td>");
                    
                    if ( P2lActivityStatus.specialCodes!=null && P2lActivityStatus.specialCodes.containsKey(tmpstatus.getCourseCode())) {
                        String envt =(String)P2lActivityStatus.specialCodes.get(tmpstatus.getCourseCode());
                        sb.append("<td  style='font-size:14px;'>" +  tmpstatus.getStatus() +  "</td>");  
                        //sb.append("<td  style='font-size:14px;'>" +  getSceStatus(emplid,tmpstatus,envt) +  "</td>");  
                    } else {
                        sb.append("<td  style='font-size:14px;'>" +  tmpstatus.getStatus()  +  "</td>");
                    }                 
                    sb.append("<td  style='font-size:14px;'> " + tmpstatus.getStatusDate()+ "  </td>");

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
                        //sb.append("<td  style='font-size:14px;'>" + (("SCE".equals(tmpstatus.getScore()))?createSceString(emplid,tmpstatus,loginUser,"4",0,sceUrl):"") + "</td>");
                        String scoreValue = getSceScore(emplid,tmpstatus,"4");
                        String productCd = String.valueOf(tmpstatus.getActivityId()); //ex: "2700845"
                        //System.out.println("emplid,tmpstatus,4"+emplid+" "+tmpstatus);
                        String product= tmpstatus.getActivityName();
                        //SceFull rating = getSce(emplid, tmpstatus, "4");
                        //System.out.println("rating::::::::::::::::::"+rating);
                        //String productCd = null;
                        //if(rating != null){
                        //productCd = rating.getProductCd();
                       // }
                        //System.out.println("$productCd::::::::::::::::::"+productCd);
                        String scoreLegend = SceHandler.getScoreLegend(scoreValue);
                        //System.out.println("scoreLegend:::"+scoreLegend);
                        // SceFull rating = getSce(emplid, status, eventId);
                         //rating.getProductCd();
                        //String employeeNtId = loginUserNtId; // wc.getEmployee().getNtId();
                        String linkParamsStr = "product="+product+"&employeeNtId="+employeeNtId+"&loginUserNtId="+loginUserNtId+"&emplid="+ emplid +"&productCode=" + productCd+"&eventId=4";
                        //System.out.println("linkParamsStr:"+linkParamsStr);
                        String sceScore=getSceScore(emplid,tmpstatus,"4");
                        boolean multipleEvaluation=false;
                        if(!"".equals(sceScore)){
                        multipleEvaluation=SceHandler.checkMultipleEvaluation(emplid,tmpstatus.getActivityId()+"","4");
                        }
                        if(tmpstatus.isRegistered()&& "".equals(sceScore)){
                            sb.append("<td  style='font-size:14px;'><a href='/TrainingReports/redirectToSCE.jsp?"+linkParamsStr+"&linkName=evaluate' onClick='OpenSpecialCase();' target='myWin' >Evaluate</a></td>");
                        }
                        else if(tmpstatus.isRegistered() && multipleEvaluation){
                        	sb.append("<td  style='font-size:14px;'><a href='/TrainingReports/redirectToSCE.jsp?"+linkParamsStr+"&linkName=evaluate' onClick='OpenSpecialCase();' target='myWin' >Re-certify</a></td>");
                        	sb.append("<br><a href='/TrainingReports/redirectToSCE.jsp?"+linkParamsStr+"&linkName=viewEvaluations' onClick='OpenSpecialCase();' target='myWin' >View&nbsp;Evaluation(s)</a></td>");
                        }else if(tmpstatus.isRegistered()&& !SceHandler.isLMSMapped1(emplid, tmpstatus.getActivityId()+"","4",scoreLegend)){
                            boolean isMapped=SceHandler.isCourseMapped(product);
                            sb.append("<td  style='font-size:14px;'><a href='/TrainingReports/redirectToSCE.jsp?"+linkParamsStr+"&linkName=reEvaluate' onClick='return OpenSpecialCase("+isMapped+");' target='myWin' >Re-Evaluate</a>");
                            sb.append("<br><a href='/TrainingReports/redirectToSCE.jsp?"+linkParamsStr+"&linkName=viewEvaluations' onClick='OpenSpecialCase();' target='myWin' >View&nbsp;Evaluation(s)</a></td>");
                        }else if(scoreLegend!=null){
                            sb.append("<td  style='font-size:14px;'><a href='/TrainingReports/redirectToSCE.jsp?"+linkParamsStr+"&linkName=viewEvaluations' onClick='OpenSpecialCase();' target='myWin' >View&nbsp;Evaluation(s)</a></td>");
                        }
                        else{
                            sb.append("<td style='font-size:14px;'></td>"); 
                        }
                        
                    } 
                     
                    else if (P2lActivityStatus.specialCodes!=null && P2lActivityStatus.specialCodes.containsKey(tmpstatus.getCourseCode())) 
                    {
                        String envt =(String)P2lActivityStatus.specialCodes.get(tmpstatus.getCourseCode());
                           /* Modified for 'In Progress' status by Meenakshi*/
                        if ( tmpstatus.isRegistered() || tmpstatus.isAssigned() || tmpstatus.isComplete()||tmpstatus.isCancel() ||tmpstatus.isInProgress() ) {
                            if ("Draft".equals(getSceStatus(emplid,tmpstatus,envt)) && tmpstatus.isRegistered() ) {
                                sb.append("<td  style='font-size:14px;'><a href='/TrainingReports/phase/handleSpecialCase?productCode=" + tmpstatus.getCourseCode() + "&type=" + envt + "&activitypk=" + actid + "&emplid=" + emplid + "&sactivitid=" +tmpstatus.getActivityId() + "' onClick='OpenSpecialCase();' target='myWin' >Edit</a></td>");
                                //sb.append("<td  style='font-size:14px;'>" + createSceString(emplid,tmpstatus,loginUser,envt, 0,sceUrl) + "</td>");
                            } else if ("Submitted".equals(getSceStatus(emplid,tmpstatus,envt))) {
                                sb.append("<td  style='font-size:14px;'>" + createSceViewLink(emplid,tmpstatus,loginUser, envt,sceUrl) + "</td>");
                            }
                            
                             else if(tmpstatus.isRegistered()){
                                sb.append("<td  style='font-size:14px;'><a href='/TrainingReports/phase/handleSpecialCase?productCode=" + tmpstatus.getCourseCode() + "&type=" + envt + "&activitypk=" + actid + "&emplid=" + emplid + "&sactivitid=" +tmpstatus.getActivityId() + "' onClick='OpenSpecialCase();' target='myWin' >Evaluate</a></td>");
                                //System.out.println("**** Status ***-"+tmpstatus.getStatus());
                                //System.out.println("**** Status ***-"+tmpstatus.getActivityId());
                            }
                            else if(tmpstatus.isCancel())
                            sb.append("<td  style='font-size:14px;'>&nbsp;</td>");
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
                        "</form><a href=\"javascript:document.domain = 'pfizer.com';  document.evaluateFormView" + status.getActivityId() + ".submit();\">View&nbsp;Evaluation(s)</a>";
        return formString;
    }
    public String createSceString(String emplid,P2lActivityStatus status, String loginUserId, String eventId, int average, String sceUrl) {
        StringBuffer sb = new StringBuffer();
        SceFull rating = getSce(emplid, status, eventId);
        //System.out.println("rating:" + rating);
        String formString = "";
        String specialStr = "<input type='hidden' name='examScore' value='" + average + "' id='examScore'/>";
        
        if ( rating != null && !Util.isEmpty(rating.getRating()) && !"Draft".equals(rating.getStatus())) {
            
            formString =    "<form name='evaluateFormView" + status.getActivityId() + "_" + status.getParentid() + "' id='evaluateFormView' method='post' action='" + sceUrl + "' target='_popup'> " +
                                "<input type='hidden' name='emplId' value='" + emplid + "' id='emplId'/> " +
                                "<input type='hidden' name='eventId' value='" + rating.getEventId() + "' id='eventId'/> " +
                                "<input type='hidden' name='productCode' value='" + rating.getProductCd() + "' id='productCode'/> " +
                                "<input type='hidden' name='product' value='" + rating.getProductName() + "' id='product'/> " +
                                "<input type='hidden' name='action' value='view' id='action'/> " +
                            "</form><a href=\"javascript:document.domain = 'pfizer.com';  document.evaluateFormView" + status.getActivityId() + "_" + status.getParentid() + ".submit();\">View&nbsp;Evaluation(s)</a>";
        } else if(status.isRegistered()) {
            String actingString = "Evaluate";
            if ( rating!= null && "Draft".equals(rating.getStatus()) ) {
                actingString = "Edit";
            }
            //System.out.println("status.getActivityId()"+status.getActivityId());
            //System.out.println(" status.getParentid()"+ status.getParentid());
            //System.out.println("emplid"+emplid);
            //System.out.println("eventId"+eventId);
            //System.out.println("status.getActivityName() "+status.getActivityName());
            //System.out.println("status.getActivityName() "+status.getActivityName());
            //System.out.println("status.getActivityName() "+status.getActivityName());
            
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
        //System.out.println("Phase Training Details"+count);
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
    function OpenSpecialCase(isMapped) {    
    if(isMapped== false){
    alert("This evaluation is no longer active and we are currently making the adjustments.");
    return false;
    }   
        window.name = "specialcase";
        var myWin = window.open("","myWin","height=600,width=900,scrollbars=yes,resizable=yes,statusbar=no,menubar=no,toolbar=no,dependent=yes"); 
   
    return true; 
    }
    function OpenSceCase() {         
        window.name = "SceCase";
        var myWin = window.open("","myWin","height=600,width=900,scrollbars=yes,resizable=yes,statusbar=no,menubar=no,toolbar=no,dependent=yes"); 
    } 
    
    function OpenSceEval() { 
    window.name = "SceEval";
   var myWin = window.open("","myWin","height=600,width=900,scrollbars=yes,resizable=yes,statusbar=no,menubar=no,toolbar=no,dependent=yes"); 
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
		<th align="left">Training Detail</th>
	</tr>
	<tr>
		<td align="left" style="background-color: #E8EEF7;">
			<div class="phasedtraining_wrapper">
				<div class="phasedtraining_hdr_wrapper">
					<div class="phasedtraining_tabLinks">
						<%
                        for (Iterator it = wc.getTrack().getPhases().iterator(); it.hasNext();) {
                            P2lTrackPhase phase = (P2lTrackPhase)it.next();
                            if(phase.getRootActivityId()!=null) {
                    %>

						<% if (phase.getRootActivityId().equals(wc.getActivityPk()) || (!Util.isEmpty(phase.getAlttActivityId()) && phase.getAlttActivityId().equals(wc.getActivityPk())) ) {%>


						<%-- <a href="detailpage?activitypk=<%=phase.getRootActivityId()%>&emplid=<%=wc.getEmployee().getEmplId()%>" id="phasedtraining_tabLinks_on" ><%=phase.getPhaseNumber()%></a> --%>
						<a
							href="detailpage?activitypk=<%=phase.getRootActivityId()%>&emplid=<%=wc.getEmployee().getEmplId()%>"
							id="phasedtraining_tabLinks_on"><%=phase.getPhaseNumber()%></a>
						<%} else { %>
						<%-- <a href="detailpage?activitypk=<%=phase.getRootActivityId()%>&emplid=<%=wc.getEmployee().getEmplId()%>" ><%=phase.getPhaseNumber()%></a> --%>
						<a
							href="detailpage?activitypk=<%=phase.getRootActivityId()%>&emplid=<%=wc.getEmployee().getEmplId()%>"><%=phase.getPhaseNumber()%></a>
						<% } %>

						<%
                        } }
                    %>

						<% if(isDisplayFeedbackForm(wc.getTrack().getTrackId())){%>
						<a href="javascript:void(0);" onClick="SceTab(scetb,p2ltabs)"
							id="sce">Feedback Form </a>
						<%}%>
					</div>
				</div>

				<div class="phasedtraining_whitebox">
					<div class="clear">
						<img src="/TrainingReports/resources/images/spacer.gif">
					</div>
					<table class="phasedtraining_table" id="p2ltabs">

						<%
                        P2lActivityStatus status = wc.getStatus();
                        Map scores = new HashMap();
                        status.getAveragePedScores(scores);
                        List kids = status.getKids();
                        //System.out.println("KIDS SIZE"+kids.size());  
                                                
                        if ( (kids.size() == 0 || !status.childHasRecord() || status.getMaxLevel(0) == 1 ) && !"CPT".equals(wc.getTrack().getTrackType())) {
                            kids = new ArrayList();
                            kids.add(status);
                        }
                        // This is special CPT logic.  check to see if any record in the structure.  What is different between
                        // CPT and other reports is that the child records does not have records that normally
                        // would attach itself to a structure.  There are many CPT records that are not attached to
                        // it structure but we have to attach them ourselfs.
                        if ( status.getMaxLevel(0) == 1  && "CPT".equals(wc.getTrack().getTrackType())) {
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

					<table class="phasedtraining_table" style="display: none;"
						id="scetb" width="75%">
						<tr>
							<th colspan='6' style='font-size: 14px;'><strong>Representative
									Feedback Form</strong></th>
						</tr>
						<tr id='greyscell'>
							<td nowrap>Form</td>
							<td>Action</td>
						</tr>
						<tr>
							<td colspan='1' style='font-size: 14px;' nowrap>Representative
								Feedback Form</td>
							<td colspan='1' style='font-size: 14px;' nowrap>
								<%
                    String Fname = wc.getEmployee().getLastName()+","+wc.getEmployee().getFirstName();
                    Fname=Fname.replaceAll("\'", "%3C");  
                    boolean isSAdmin = wc.getUser().isSuperAdmin();
                    //System.out.println("Is Super Admin in PhaseTraining Detail?"+isSAdmin);
                    System.out.println("phasereports.jsp | AppQueryStrings.FIELD_EMPLID --> "+wc.getEmployee().getEmplId());
                    
                    System.out.println("phasereports.jsp | AppQueryStrings.FIELD_ACTIVITYID --> "+wc.getActivityPk());
                    
                    System.out.println("phasereports.jsp | AppQueryStrings.FIELD_EVALID --> ?"+wc.getUser().getEmplid());
                    
                    System.out.println("phasereports.jsp | AppQueryStrings.FIELD_EVALNM --> ?"+wc.getUser().getName());
                    
                    System.out.println("phasereports.jsp | AppQueryStrings.FIELD_EMPNM --> ?"+Fname);
                    
                    System.out.println("phasereports.jsp | AppQueryStrings.FIELD_FLAG --> ?"+flag);
                    
                    System.out.println("phasereports.jsp | AppQueryStrings.FIELD_SUPERADMIN --> ?"+isSAdmin);
                    
                    System.out.println("phasereports.jsp | AppQueryStrings.FIELD_TRACKID --> ?"+wc.getTrack().getTrackId());
                    %> <a
								href='/TrainingReports/phase/SCEeval?&<%=AppQueryStrings.FIELD_EMPLID%>=<%=wc.getEmployee().getEmplId()%>&<%=AppQueryStrings.FIELD_ACTIVITYID%>=<%=wc.getActivityPk()%>&<%=AppQueryStrings.FIELD_EVALID%>=<%=wc.getUser().getEmplid()%>&<%=AppQueryStrings.FIELD_EVALNM%>=<%=wc.getUser().getName()%>&<%=AppQueryStrings.FIELD_EMPNM%>=<%=Fname%>&<%=AppQueryStrings.FIELD_FLAG%>=<%=flag%>&<%=AppQueryStrings.FIELD_SUPERADMIN%>=<%=isSAdmin%>&<%=AppQueryStrings.FIELD_TRACKID%>=<%=wc.getTrack().getTrackId()%>'
								onClick='OpenSceEval();' target='myWin'> View/Evaluate </a>

							</td>

						</tr>
					</table>

				</div>
			</div>
		</td>
	</tr>
</table>








