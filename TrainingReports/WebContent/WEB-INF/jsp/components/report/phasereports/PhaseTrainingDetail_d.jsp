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
<%@ page import="com.pfizer.webapp.wc.components.report.phasereports.PhaseTrainingDetailWc"%>
<%@ page import="com.pfizer.webapp.wc.components.report.phasereports.TrainingSummaryWc"%>
<%@ page import="com.tgix.Utils.Util"%>
<%@ page import="java.math.BigDecimal"%>
<%@ page import="com.pfizer.utils.DateComparator"%>
<%@ taglib uri="/WEB-INF/dynamic-include.tld" prefix="inc" %>
<%@ page import="java.util.List"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.Hashtable"%>
<%@ page import="java.util.Enumeration"%>
<%@ page import="java.util.Vector"%>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.Collections"%>

<%
	PhaseTrainingDetailWc wc = (PhaseTrainingDetailWc)request.getAttribute(PhaseTrainingDetailWc.ATTRIBUTE_NAME);
    
    request.setAttribute("SCEEVAL",wc);
    System.out.println("&&&&&&&&&&&&&getEmployee&&&&&&&&&&&&&&&&"+wc.getEmployee());
	System.out.println("&&&&&&&&&&&&&&&getNtId&&&&&&&&&&&&&&"+wc.getEmployee().getNtId());
	//String sceUrl = "http://sce.pfizer.com/SCEWeb/evaluation/evaluateTR.do";
    String sceUrl = "http://sceint.pfizer.com/SCEWeb/evaluation/evaluateTR.do";
	
	String url = request.getRequestURL().toString();
	if ( url != null ) {
		url = url.toLowerCase();
	}
	if ( url != null 
		&& ( url.indexOf("trstg.pfizer.com") > 0  || url.indexOf("tgix-dev.pfizer.com") > 0 )) {
		sceUrl = "http://scestg.pfizer.com/SCEWeb/evaluation/evaluateTR.do";	
	}
    if(url !=null && (url.indexOf("localhost") > 0))
    {
        sceUrl = "http://localhost:7001/SCEWeb/evaluation/evaluateTR.do";	
    }  
    if ( url != null 
		&& (url.indexOf("trint.pfizer.com") > 0 )) {
		
        sceUrl = "http://sceint.pfizer.com/SCEWeb/evaluation/evaluateTR.do";
        //shannon for testing 
        //sceUrl = "http://localhost:7001/SCEWeb/evaluation/evaluateTR.do";		
	}
    if ( url != null 
		&& (url.indexOf("trtdev.pfizer.com") > 0)) {
		sceUrl = "http://scedev.pfizer.com/SCEWeb/evaluation/evaluateTR.do";	
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
        UserName = uSession.getUser().getName();
        
        boolean headflag = false;
        String scoreVisible= uSession.getUser().getScoresFlag();
        System.out.println("scoreVisible"+scoreVisible);
        for (Iterator it = status.iterator();it.hasNext();) {
            P2lActivityStatus tmpstatus = (P2lActivityStatus)it.next(); 
            // Check to see if user has any record at all
           if ( !tmpstatus.hasRecordAtAll() ) {
                 System.out.println("Inside hasRecordAtAll"+tmpstatus.getActivityName()); 
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
                    sb.append("<tr><th colspan='6' style='font-size:14px;'><strong><a href='listReportAllStatus.do?activitypk="+tmpstatus.getActivityId()+"&section=Complete&pieIndex=0'>" + tmpstatus.getActivityName() + completestring +"</a></strong></th></tr>");
                    sb.append("<tr id='greyscell'><td nowrap>Activity&nbsp;Type</td><td>Status</td><td >Status&nbsp;Date</td><td >Score</td><td align='center'>Action</td></tr>");
                    sb.append("<tr><td colspan='1' style='font-size:14px;' nowrap><a href='listReportAllStatus.do?activitypk="+tmpstatus.getActivityId()+"&section=Complete&pieIndex=0'>" + tmpstatus.getBuffer()+ tmpstatus.getActivityName() + "</a></td>");
                    //sb.append("<td  style='font-size:14px;'>" + tmpstatus.getReportType() + "</td>");
                    /*Changed for bug fix - 05-Feb-2010 */
                    if ( tmpstatus.isSce() ) {
                        sb.append("<td  style='font-size:14px;'>" +  tmpstatus.getStatus()  +  "abc</td>");                        
                    } else {
                        sb.append("<td  style='font-size:14px;'>" +  tmpstatus.getStatus()  +  "def</td>");
                    }                    
                    sb.append("<td  style='font-size:14px;'> " + tmpstatus.getStatusDate()+ "  ghi</td>");
                    /* Condition added for P2L scores hiding for CSO enhancement */
                    if ( tmpstatus.isPedagogue() && scoreVisible.equalsIgnoreCase("Y") ) {
                       sb.append("<td  style='font-size:14px;'>" + tmpstatus.getScore() + "jkl</td>");
                    } /*End of addition */
                    else {   
                        if ( tmpstatus.isSce() ) {
                            /*Changed for Bug fix: 05-Feb-2010 */
                            sb.append("<td  style='font-size:14px;'>" +  getSceScore(emplid,tmpstatus,"4")  +  "mno</td>");                        
                        }
                        /* Condition added for P2L scores hiding for CSO enhancement */ 
                        else if (scoreVisible.equalsIgnoreCase("Y")) {
                            sb.append("<td  style='font-size:14px;'> heree33333333" +  tmpstatus.getScore()  +  "heree33333333</td>");
                        } /*End of addition */
                        else {
                            sb.append("<td  style='font-size:14px;'>" + "" +  "pqr</td>");
                        }                        
                    }
                    /* Added for Bug fix */
                    if ( tmpstatus.isSce() ) {                        
                       // sb.append("<td  style='font-size:14px;'>" + (("SCE".equals(tmpstatus.getScore()))?createSceString(emplid,tmpstatus,loginUser,"4",0,sceUrl):"") + "stuuu</td>");
                       String scoreValue = getSceScore(emplid,tmpstatus,"4");
                                System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$emplid: "+emplid+" tmpstatus.getCourseCode():"+tmpstatus.getCourseCode()+" envt"+"4"+" scoreValue"+scoreValue+" $$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
                                System.out.println("$$$$$$$$$$$$$$ product_cd = 2700845 hardcoded");
                                System.out.println("$$$$$$$$$$$$$$ scoreValue  = NI hardcoded");
                                String productCd = String.valueOf(tmpstatus.getActivityId()); //ex: "2700845"
                                System.out.println("$productCd:::"+productCd);
                                String scoreLegend = SceHandler.getScoreLegend(scoreValue);
                                System.out.println("scoreLegend:::"+scoreLegend);
                                String employeeNtId = wc.getEmployee().getNtId();
                                String linkParamsStr = "employeeNtId="+employeeNtId+"&emplid="+ emplid +"&productCode=" + tmpstatus.getCourseCode()+"&eventId=4";
                                System.out.println("**********************Here1****************/"+getSceScore(emplid,tmpstatus,"4")+"/**********");
                                if("".equals(getSceScore(emplid,tmpstatus,"4"))){
                                    
                                    sb.append("<td  style='font-size:14px;'><a href='/TrainingReports/phase/validateTRTActions.do?"+linkParamsStr+"&linkname=Evaluate' onClick='OpenSpecialCase();' target='myWin' >Evaluate</a></td>");
                                }else if(!SceHandler.isLMSMapped(emplid, productCd,"4",scoreLegend)){
                                    //re-evaluate
                                    sb.append("<td  style='font-size:14px;'><a href='/TrainingReports/phase/validateTRTActions.do?"+linkParamsStr+"&linkname=ReEvaluate' onClick='OpenSpecialCase();' target='myWin' >Re-Evaluate</a>");
                                    sb.append("<br><a href='/TrainingReports/phase/validateTRTActions.do?"+linkParamsStr+"&linkname=viewEval' onClick='OpenSpecialCase();' target='myWin' >View&nbsp;Evaluation(s)</a></td>");
                                   
                                    
                                    //sb.append("<td  style='font-size:14px;'><a href='/TrainingReports/phase/handleSpecialCase.do?productCode=" + tmpstatus.getCourseCode() + "&type=" + "4" + "&activitypk=" + actid + "&emplid=" + emplid + "&sactivitid=" +tmpstatus.getActivityId() + "' onClick='OpenSpecialCase();' target='myWin' >Re-Evaluate</a>HEREE1111");
                                    //sb.append("" + (("SCE".equals(tmpstatus.getScore()))?createSceString(emplid,tmpstatus,loginUser,"4",0,sceUrl):"") + "G100000000</td>");
                                    //System.out.println("************sceUrl:"+sceUrl);
                                    System.out.println("***********Evaluate and Re-evaluate Link added");
                                }else{
                                //shinoy added ends
                                sb.append("<td  style='font-size:14px;'><a href='/TrainingReports/phase/validateTRTActions.do?"+linkParamsStr+"&linkname=viewEval' onClick='OpenSpecialCase();' target='myWin' >View&nbsp;Evaluation(s)</a></td>");
                                //System.out.println("************sceUrl:"+sceUrl);
                                   // sb.append("<td  style='font-size:14px;'>" + (("SCE".equals(tmpstatus.getScore()))?createSceString(emplid,tmpstatus,loginUser,"4",0,sceUrl):"") + "G100000000</td>");
                                }
                    } 
                   
                    else if (P2lActivityStatus.specialCodes!=null && P2lActivityStatus.specialCodes.containsKey(tmpstatus.getCourseCode())) 
                    {
                        String envt =(String)P2lActivityStatus.specialCodes.get(tmpstatus.getCourseCode());
                          /* Modified for 'In Progress' status by Meenakshi*/
                        if ( tmpstatus.isRegistered() || tmpstatus.isAssigned() || tmpstatus.isComplete()||tmpstatus.isCancel() ||tmpstatus.isInProgress()) {
                            if ("Draft".equals(getSceStatus(emplid,tmpstatus,envt)) && tmpstatus.isRegistered() ) {
                                sb.append("<td  style='font-size:14px;'><a href='/TrainingReports/phase/handleSpecialCase.do?productCode=" + tmpstatus.getCourseCode() + "&type=" + envt + "&activitypk=" + actid + "&emplid=" + emplid + "&sactivitid=" +tmpstatus.getActivityId() + "' onClick='OpenSpecialCase();' target='myWin' >Edit</a>vwx</td>");
                                //sb.append("<td  style='font-size:14px;'>" + createSceString(emplid,tmpstatus,loginUser,envt, 0,sceUrl) + "</td>");
                            } else if ("Submitted".equals(getSceStatus(emplid,tmpstatus,envt))) {
                                //shinoy added starts
                                //emplid,productCd,eventId,scoreValue
                               /* String scoreValue = getSceScore(emplid,tmpstatus,"4");
                                System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$emplid: "+emplid+" tmpstatus.getCourseCode():"+tmpstatus.getCourseCode()+" envt"+envt+" scoreValue"+scoreValue+" $$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
                                if(SceHandler.isLMSMapped(emplid,tmpstatus.getCourseCode(),envt,scoreValue)){
                                    //re-evaluate
                                    sb.append("<td  style='font-size:14px;'><a href='/TrainingReports/phase/handleSpecialCase.do?productCode=" + tmpstatus.getCourseCode() + "&type=" + envt + "&activitypk=" + actid + "&emplid=" + emplid + "&sactivitid=" +tmpstatus.getActivityId() + "' onClick='OpenSpecialCase();' target='myWin' >Re-Evaluate</a></td>");
                                    System.out.println("************Re-evaluate Link added");
                                }
                                System.out.println("************sceUrl:"+sceUrl);
                                //shinoy added ends
                                */ 
                                sb.append("<td  style='font-size:14px;'>" + createSceViewLink(emplid,tmpstatus,loginUser, envt,sceUrl) + "</td>");
                                
                                
                                System.out.println("************View evaluate Link added");
                            }
                            else if(tmpstatus.isRegistered()){
                                sb.append("<td  style='font-size:14px;'><a href='/TrainingReports/phase/handleSpecialCase.do?productCode=" + tmpstatus.getCourseCode() + "&type=" + envt + "&activitypk=" + actid + "&emplid=" + emplid + "&sactivitid=" +tmpstatus.getActivityId() + "' onClick='OpenSpecialCase();' target='myWin' >Evaluate</a>yzzz</td>");
                                //System.out.println("**** Status ***-"+tmpstatus.getStatus());
                                //System.out.println("**** Status ***-"+tmpstatus.getActivityId());
                            }else if(tmpstatus.isCancel())
                            sb.append("<td  style='font-size:14px;'>&nbsp;hhhhhhhhhhhh</td>");
                            
                        }
                         else {
                            sb.append("<td  style='font-size:14px;'>&nbsp;kkkkkkkk</td>");
                        }
                    } 
                    else
                    {
                    sb.append("<td  style='font-size:14px;'>&nbsp;pppppppppppppp</td>");
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
                    sb.append("<tr><th colspan='6' style='font-size:14px;'><strong><a href='listReportAllStatus.do?activitypk="+tmpstatus.getActivityId()+"&section=Complete&pieIndex=0'>" + tmpstatus.getActivityName() + completestring +"</a></strong></th></tr>");
                    sb.append("<tr id='greyscell'><td nowrap>Activity&nbsp;Type</td><td >Status</td><td >Status&nbsp;Date</td><td >Score</td><td  align='center'>Action</td></tr>");
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
                if ( tmpstatus.getLevel() > 2  && tmpstatus.hasRecord(false)) {
                     System.out.println("Inside level greater than 2");     
                    // If child link is subscription, draw that row instead
                   if ( tmpstatus.isChildSubscription()) {
                    System.out.println("Inside child subscription1"+tmpstatus.getActivityName());
                       sb.append(drawRow(tmpstatus.getKids(),emplid,uSession,actid,pedScores,isDebug,sceUrl, wc));
                    System.out.println("Inside child subscription2"+tmpstatus.getActivityName());   
                        continue;
                    }      
                    if ( isDebug ) {
                        String tmpStr = tmpstatus.getBuffer()+ tmpstatus.getActivityName() +  " - " + tmpstatus.getActivityId() + " - " + tmpstatus.getActLabelName() + " - " + tmpstatus.getCourseCode();
                        sb.append("<tr><td colspan='1' style='font-size:14px;' nowrap><a href='listReportAllStatus.do?activitypk="+tmpstatus.getActivityId()+"&section=Complete&pieIndex=0'>" + tmpStr + "</a>G2222</td>");
                    } else {
                        sb.append("<tr><td colspan='1' style='font-size:14px;' nowrap><a href='listReportAllStatus.do?activitypk="+tmpstatus.getActivityId()+"&section=Complete&pieIndex=0'>" + tmpstatus.getBuffer()+ tmpstatus.getActivityName() + "</a>G3333</td>");
                    }
                    //sb.append("<td  style='font-size:14px;'>"  + tmpstatus.getReportType() +  "</td>");
                    
                    if ( P2lActivityStatus.specialCodes!=null && P2lActivityStatus.specialCodes.containsKey(tmpstatus.getCourseCode())) {
                        String envt =(String)P2lActivityStatus.specialCodes.get(tmpstatus.getCourseCode());
                        sb.append("<td  style='font-size:14px;'>" +  tmpstatus.getStatus() +  "G4444444</td>");  
                        //sb.append("<td  style='font-size:14px;'>" +  getSceStatus(emplid,tmpstatus,envt) +  "</td>");  
                    } else {
                        sb.append("<td  style='font-size:14px;'>" +  tmpstatus.getStatus()  +  "G55555555</td>");
                    }                 
                    sb.append("<td  style='font-size:14px;'> " + tmpstatus.getStatusDate()+ "  G6666666</td>");

                    if ( tmpstatus.isSce() ) {
                        //System.out.println("score call");
                        sb.append("<td  style='font-size:14px;'>" +  getSceScore(emplid,tmpstatus,"4")  +  "G777777777</td>");                        
                    } 
                    /* Condition added for P2L scores for CSO enhancement */
                    else if (scoreVisible.equalsIgnoreCase("Y")) {
                        sb.append("<td  style='font-size:14px;'>" + tmpstatus.getScore() + "G8888888888</td>");
                    } /* End of addition */
                    else {
                        sb.append("<td  style='font-size:14px;'>" + " " + "G99999999999</td>");
                    }                            

                    if ( tmpstatus.isSce() ) {                        
                        
                        //shinoy added starts
                                //emplid,productCd,eventId,scoreValue
                                //productCd is  status.getActivityId()
                                String scoreValue = getSceScore(emplid,tmpstatus,"4");
                                System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$emplid: "+emplid+" tmpstatus.getCourseCode():"+tmpstatus.getCourseCode()+" envt"+"4"+" scoreValue"+scoreValue+" $$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
                                System.out.println("$$$$$$$$$$$$$$ product_cd = 2700845 hardcoded");
                                System.out.println("$$$$$$$$$$$$$$ scoreValue  = NI hardcoded");
                                String productCd = String.valueOf(tmpstatus.getActivityId()); //ex: "2700845"
                                System.out.println("$productCd:::"+productCd);
                                String scoreLegend = SceHandler.getScoreLegend(scoreValue);
                                System.out.println("scoreLegend:::"+scoreLegend);
                                String employeeNtId = wc.getEmployee().getNtId();
                                String linkParamsStr = "employeeNtId="+employeeNtId+"&emplid="+ emplid +"&productCode=" + tmpstatus.getCourseCode()+"&eventId=4";
                                System.out.println("******************Here2********************/"+getSceScore(emplid,tmpstatus,"4")+"/**********");
                                if("".equals(getSceScore(emplid,tmpstatus,"4"))){
                                    sb.append("<td  style='font-size:14px;'><a href='/TrainingReports/phase/validateTRTActions.do?"+linkParamsStr+"&linkname=Evaluate' onClick='OpenSpecialCase();' target='myWin' >Evaluate</a></td>");
                                }else if(!SceHandler.isLMSMapped(emplid, productCd,"4",scoreLegend)){
                                    //re-evaluate
                                    sb.append("<td  style='font-size:14px;'><a href='/TrainingReports/phase/validateTRTActions.do?"+linkParamsStr+"&linkname=ReEvaluate' onClick='OpenSpecialCase();' target='myWin' >Re-Evaluate</a>");
                                    sb.append("<br><a href='/TrainingReports/phase/validateTRTActions.do?"+linkParamsStr+"&linkname=viewEval' onClick='OpenSpecialCase();' target='myWin' >View&nbsp;Evaluation(s)</a></td>");
                                   
                                    
                                    //sb.append("<td  style='font-size:14px;'><a href='/TrainingReports/phase/handleSpecialCase.do?productCode=" + tmpstatus.getCourseCode() + "&type=" + "4" + "&activitypk=" + actid + "&emplid=" + emplid + "&sactivitid=" +tmpstatus.getActivityId() + "' onClick='OpenSpecialCase();' target='myWin' >Re-Evaluate</a>HEREE1111");
                                    //sb.append("" + (("SCE".equals(tmpstatus.getScore()))?createSceString(emplid,tmpstatus,loginUser,"4",0,sceUrl):"") + "G100000000</td>");
                                    //System.out.println("************sceUrl:"+sceUrl);
                                    System.out.println("***********Evaluate and Re-evaluate Link added");
                                }else{
                                //shinoy added ends
                                sb.append("<td  style='font-size:14px;'><a href='/TrainingReports/phase/validateTRTActions.do?"+linkParamsStr+"&linkname=viewEval' onClick='OpenSpecialCase();' target='myWin' >View&nbsp;Evaluation(s)</a></td>");
                                //System.out.println("************sceUrl:"+sceUrl);
                                   // sb.append("<td  style='font-size:14px;'>" + (("SCE".equals(tmpstatus.getScore()))?createSceString(emplid,tmpstatus,loginUser,"4",0,sceUrl):"") + "G100000000</td>");
                                }
                    } 
                     
                    else if (P2lActivityStatus.specialCodes!=null && P2lActivityStatus.specialCodes.containsKey(tmpstatus.getCourseCode())) 
                    {
                        String envt =(String)P2lActivityStatus.specialCodes.get(tmpstatus.getCourseCode());
                           /* Modified for 'In Progress' status by Meenakshi*/
                        if ( tmpstatus.isRegistered() || tmpstatus.isAssigned() || tmpstatus.isComplete()||tmpstatus.isCancel() ||tmpstatus.isInProgress() ) {
                            if ("Draft".equals(getSceStatus(emplid,tmpstatus,envt)) && tmpstatus.isRegistered() ) {
                                sb.append("<td  style='font-size:14px;'><a href='/TrainingReports/phase/handleSpecialCase.do?productCode=" + tmpstatus.getCourseCode() + "&type=" + envt + "&activitypk=" + actid + "&emplid=" + emplid + "&sactivitid=" +tmpstatus.getActivityId() + "' onClick='OpenSpecialCase();' target='myWin' >Edit</a>G11-11111</td>");
                                //sb.append("<td  style='font-size:14px;'>" + createSceString(emplid,tmpstatus,loginUser,envt, 0,sceUrl) + "</td>");
                            } else if ("Submitted".equals(getSceStatus(emplid,tmpstatus,envt))) {
                                sb.append("<td  style='font-size:14px;'>" + createSceViewLink(emplid,tmpstatus,loginUser, envt,sceUrl) + "</td>G12-222222");
                            }
                            
                             else if(tmpstatus.isRegistered()){
                                
                                sb.append("<td  style='font-size:14px;'><a href='/TrainingReports/phase/handleSpecialCase.do?productCode=" + tmpstatus.getCourseCode() + "&type=" + envt + "&activitypk=" + actid + "&emplid=" + emplid + "&sactivitid=" +tmpstatus.getActivityId() + "' onClick='OpenSpecialCase();' target='myWin' >Evaluate</a>G12-22222</td>");
                                
                                //System.out.println("**** Status ***-"+tmpstatus.getStatus());
                                //System.out.println("**** Status ***-"+tmpstatus.getActivityId());
                            }
                            else if(tmpstatus.isCancel())
                            sb.append("<td  style='font-size:14px;'>&nbsp;</td>");
                        }else {
                            sb.append("<td  style='font-size:14px;'>&nbsp;G14---1111111</td>");
                        }
                    } else {                        
                        sb.append("<td  style='font-size:14px;'>&nbsp;G155555</td>");
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
            //return " SCE null or 0 size -shinoy added to debug";
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
                        "</form><a href=\"javascript:document.domain = 'pfizer.com';  document.evaluateFormView" + status.getActivityId() + ".submit();\">View&nbsp;Evaluation(s1)</a>";
        return formString;
    }
    public String createSceString(String emplid,P2lActivityStatus status, String loginUserId, String eventId, int average, String sceUrl) {
        StringBuffer sb = new StringBuffer();
        SceFull rating = getSce(emplid, status, eventId);
        System.out.println("rating**********:" + rating);
        String formString = "";
        String specialStr = "<input type='hidden' name='examScore' value='" + average + "' id='examScore'/>";
        
        if ( rating != null && !Util.isEmpty(rating.getRating()) && !"Draft".equals(rating.getStatus())) {
            // SceHandler.isLMSMapped()
            formString =    "<form name='evaluateFormView" + status.getActivityId() + "_" + status.getParentid() + "' id='evaluateFormView' method='post' action='" + sceUrl + "' target='_popup'> " +
                                "<input type='hidden' name='emplId' value='" + emplid + "' id='emplId'/> " +
                                "<input type='hidden' name='eventId' value='" + rating.getEventId() + "' id='eventId'/> " +
                                "<input type='hidden' name='productCode' value='" + rating.getProductCd() + "' id='productCode'/> " +
                                "<input type='hidden' name='action' value='view' id='action'/> " +
                            "</form><a href=\"javascript:document.domain = 'pfizer.com';  document.evaluateFormView" + status.getActivityId() + "_" + status.getParentid() + ".submit();\">View&nbsp;Evaluation(s2)</a>";
        } else if(status.isRegistered()) {
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
        System.out.println("Phase Training Details"+count);
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
        var myWin = window.open("","myWin","height=800,width=1024,scrollbars=yes,resizable=yes,statusbar=no,menubar=no,toolbar=no,dependent=yes") 
    } 
    function OpenSceCase() { 
        window.name = "SceCase";
        var myWin = window.open("","myWin","height=800,width=1024,scrollbars=yes,resizable=yes,statusbar=no,menubar=no,toolbar=no,dependent=yes") 
    } 
    
    function OpenSceEval() { 
    window.name = "SceEval";
   var myWin = window.open("","myWin","height=800,width=1024,scrollbars=yes,resizable=yes,statusbar=no,menubar=no,toolbar=no,dependent=yes") 
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
        <td align="left" style="background-color:#E8EEF7;">        
        	<div class="phasedtraining_wrapper">
            	<div class="phasedtraining_hdr_wrapper">
                    <div class="phasedtraining_tabLinks" >
                    <%
                        for (Iterator it = wc.getTrack().getPhases().iterator(); it.hasNext();) {
                            P2lTrackPhase phase = (P2lTrackPhase)it.next();
                    %>
                        
                        <% if (phase.getRootActivityId().equals(wc.getActivityPk()) || (!Util.isEmpty(phase.getAlttActivityId()) && phase.getAlttActivityId().equals(wc.getActivityPk())) ) {%>
                        	<a href="detailpage.do?activitypk=<%=phase.getRootActivityId()%>&emplid=<%=wc.getEmployee().getEmplId()%>" id="phasedtraining_tabLinks_on" ><%=phase.getPhaseNumber()%></a>
                        <%} else { %>
                        	<a href="detailpage.do?activitypk=<%=phase.getRootActivityId()%>&emplid=<%=wc.getEmployee().getEmplId()%>" ><%=phase.getPhaseNumber()%></a>
                        <% } %>
                    
                    <%
                        }
                    %>
                    
                    <% if(isDisplayFeedbackForm(wc.getTrack().getTrackId())){%>
                    <a href="javascript:void(0);" onClick="SceTab(scetb,p2ltabs)" id="sce">Feedback Form </a>
                    <%}%>
                	</div>
                </div>
            
				<div class="phasedtraining_whitebox">
                	<div class="clear" ><img src="/TrainingReports/resources/images/spacer.gif"></div>
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
                    
                    <table class="phasedtraining_table" style="display:none;" id="scetb" width="75%">
                    <tr><th colspan='6' style='font-size:14px;'><strong>Representative Feedback Form</strong></th></tr>
                    <tr id='greyscell'><td nowrap>Form</td><td>Action</td></tr>
                    <tr>
                    <td colspan='1' style='font-size:14px;' nowrap>Representative Feedback Form</td>
                    <td colspan='1' style='font-size:14px;' nowrap>
                    <%
                    String Fname = wc.getEmployee().getLastName()+","+wc.getEmployee().getFirstName();
                    Fname=Fname.replaceAll("\'", "%3C");  
                    boolean isSAdmin = wc.getUser().isSuperAdmin();
                    System.out.println("Is Super Admin in PhaseTraining Detail?"+isSAdmin);
                    %>
                                         
                    <a href='/TrainingReports/phase/SCEeval.do?&<%=AppQueryStrings.FIELD_EMPLID%>=<%=wc.getEmployee().getEmplId()%>&<%=AppQueryStrings.FIELD_ACTIVITYID%>=<%=wc.getActivityPk()%>&<%=AppQueryStrings.FIELD_EVALID%>=<%=wc.getUser().getEmplid()%>&<%=AppQueryStrings.FIELD_EVALNM%>=<%=wc.getUser().getName()%>&<%=AppQueryStrings.FIELD_EMPNM%>=<%=Fname%>&<%=AppQueryStrings.FIELD_FLAG%>=<%=flag%>&<%=AppQueryStrings.FIELD_SUPERADMIN%>=<%=isSAdmin%>&<%=AppQueryStrings.FIELD_TRACKID%>=<%=wc.getTrack().getTrackId()%>' onClick='OpenSceEval();' target='myWin'>
                    View/Evaluate
                    </a>
                    
                    </td>
                     
                    </tr>
                    </table>
                    
                </div>
            </div>
        </td>
    </tr>
</table>
                      
                     





 
