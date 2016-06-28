package com.pfizer.db;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.pfizer.hander.SceHandler;
import com.pfizer.utils.LatestActivityStatus;
import com.pfizer.utils.StatusDateComparator;
import com.tgix.Utils.Util;

public class P2lActivityStatus {

    private HashMap item;
    private List children = new ArrayList();
    private P2lActivityStatus parent;
    private int raiseLevel = 0;
    private Map sortedStatusMap;
    private LatestActivityStatus activityStatus;
    private List sortedStatusList;
    private java.sql.Date statusDateToDisplay;
  	// These are special codes that require special handling
	public static  Map specialCodes;
/*	static {
		Map tMap = new HashMap();
		tMap.put( "5980A-PHA6-NHR06TS1",  "5");
		tMap.put( "5980B-PHA4-NTS04SCORE",  "6");
		tMap.put( "5983B-PHA4-NAI04SCORE", "7");
		tMap.put( "5983C-PHA4-NEN04SCORE",  "7");
		tMap.put( "5983A-PHA4-NAG04SCORE",  "7");
		tMap.put( "5983D-PHA4-NON04SCORE", "7" );
		tMap.put( "5983E-PHA4-NOP04SCORE", "7" );
		//tMap.put( "5980A-PHA1-NHR01ADRUG", "5" );

		specialCodes = tMap;
	}
*/
	
    public  P2lActivityStatus( HashMap map ) {
        this.item = map;
        this.sortedStatusMap = new java.util.TreeMap(new StatusDateComparator());
        this.sortedStatusList = new ArrayList();
        if(item.get("COMDATE")!=null){  
            if(item.get("ESTATUS")!=null && item.get("ESTATUS").toString().equalsIgnoreCase("Waived")){
                activityStatus = new LatestActivityStatus();
                activityStatus.setStatus("Waived");
                activityStatus.setStatusDate(new java.sql.Date(((java.util.Date)item.get("COMDATE")).getTime()));
                sortedStatusList.add(activityStatus);
                //sortedStatusMap.put("Waived",new java.sql.Date(((java.util.Date)item.get("COMDATE")).getTime()));
             }else{
                activityStatus = new LatestActivityStatus();
                activityStatus.setStatus("Complete");
                activityStatus.setStatusDate(new java.sql.Date(((java.util.Date)item.get("COMDATE")).getTime()));
                sortedStatusList.add(activityStatus);
                //sortedStatusMap.put("Complete",new java.sql.Date(((java.util.Date)item.get("COMDATE")).getTime()));
                //System.out.println("in complete ");
             }                                  
            
        }
        if(item.get("REGDATE")!=null){
           activityStatus = new LatestActivityStatus();
           activityStatus.setStatus("Registered");
           activityStatus.setStatusDate(new java.sql.Date(((java.util.Date)item.get("REGDATE")).getTime()));
           sortedStatusList.add(activityStatus);    
           //sortedStatusMap.put("Registered",new java.sql.Date(((java.util.Date)item.get("REGDATE")).getTime()));
        }
        if(item.get("ASNDATE")!=null){
           activityStatus = new LatestActivityStatus();
           activityStatus.setStatus("Assigned");
           activityStatus.setStatusDate(new java.sql.Date(((java.util.Date)item.get("ASNDATE")).getTime()));
           sortedStatusList.add(activityStatus);   
           //sortedStatusMap.put("Assigned",new java.sql.Date(((java.util.Date)item.get("ASNDATE")).getTime()));
        }
        if(item.get("IPDATE")!=null){
           activityStatus = new LatestActivityStatus();
           activityStatus.setStatus("In Progress");
           activityStatus.setStatusDate(new java.sql.Date(((java.util.Date)item.get("IPDATE")).getTime()));
           sortedStatusList.add(activityStatus);
           //sortedStatusMap.put("In Progress",new java.sql.Date(((java.util.Date)item.get("IPDATE")).getTime()));
        }
        if(item.get("CANDATE")!=null){
           activityStatus = new LatestActivityStatus();
           activityStatus.setStatus("Cancelled");
           activityStatus.setStatusDate(new java.sql.Date(((java.util.Date)item.get("CANDATE")).getTime()));
           sortedStatusList.add(activityStatus);
           //sortedStatusMap.put("Cancelled",new java.sql.Date(((java.util.Date)item.get("CANDATE")).getTime()));
        }
        if(item.get("NOSHOWDATE")!=null){
           activityStatus = new LatestActivityStatus();
           activityStatus.setStatus("No Show");
           activityStatus.setStatusDate(new java.sql.Date(((java.util.Date)item.get("NOSHOWDATE")).getTime()));
           sortedStatusList.add(activityStatus);
           //sortedStatusMap.put("No Show",new java.sql.Date(((java.util.Date)item.get("NOSHOWDATE")).getTime()));
        }
        
        Collections.sort(sortedStatusList,new StatusDateComparator());
        
        //noshowdate
        
        //this.sortedStatusList = new ArrayList();
        
    }

    public static void setCodes( Map codes ) {
        specialCodes = codes;
    }
    public int getMaxLevel(int currentMax) {


        if ( getLevel() > currentMax) {
            if ( hasRecord(false) ) {
                currentMax = getLevel();
            }
        }
        for (Iterator it= getKids().iterator(); it.hasNext();) {
            P2lActivityStatus tmp = (P2lActivityStatus)it.next();
            currentMax = tmp.getMaxLevel(currentMax);
            //System.out.println(tmp.getActivityId() + ":" + currentMax);
        }
        return currentMax;
    }

    public void setRaiselevel(int num) {
        raiseLevel = raiseLevel + num;
        for (Iterator it= getKids().iterator(); it.hasNext();) {
            P2lActivityStatus tmp = (P2lActivityStatus)it.next();
            tmp.setRaiselevel(num);
        }
    }
    public String getBuffer() {
        String buffer = "";
        for ( int i = 3; i < getLevel(); i ++ ) {
            buffer = buffer + "&nbsp;&nbsp;&nbsp;&nbsp;";
        }
        return buffer;
    }

    public boolean checkCompleteAction() {
        if (children.size() == 0) {
            if ( isAssessment() || isComplete() ) {
                return false;
            }
            return true;
        }
        return false;
    }
    public void setParent(P2lActivityStatus parent) {
        this.parent = parent;
    }

    public boolean isComplete() {
         if (item.get("HAS_COMPLETED") != null ) {
            String tmp = Util.toEmpty((String)item.get("HAS_COMPLETED"));
            return "true".equals(tmp);
         }

        String stat = "";
        if ( item.get("COMPLETIONSTATUS") != null ) {
            stat = (String)item.get("COMPLETIONSTATUS");
        } else {
            if ( item.get("ACTION_CHECK") != null ) {
                stat = (String)item.get("ACTION_CHECK");
            } else {
                return false;
            }
        }
         return "Complete".equals(stat);
    }

    public P2lActivityStatus getParent() {
        return parent;
    }
    public int getLevel() {
        return ((BigDecimal)item.get("MLEVEL")).intValue() + raiseLevel;
    }
    public String getActivityName() {
        return (String)item.get("ACTIVITYNAME");
    }
    public boolean isPedagogue() {
        String name = getActivityName();
        return (name.indexOf("(Pedagogue)")>0);
    }

    public String getReportType() {
        if ( isPedagogue() )
            return "Pedagogue";
        if ( isSce() )
            return "SCE";

        return " ";
    }
    public String hasPass() {

        if ( isAssessment() && !Util.isEmpty(getScore()) && isComplete() ) {
            String tmp = Util.toEmpty((String)item.get("SUCCESS"));
            return "Pass".equals(tmp)?"Pass":"Fail";
        }

        return "";
    }

    public boolean hasRecordNoSub() {
        if ( isSubscription() ) {
            return false;
        }
        if (item.get("REG_CHECK") != null ) {
            if ("true".equals((String)item.get("REG_CHECK"))) {
                return true;
            }
        }
        if (item.get("ASSIGN_CHECK") != null ) {
            if ("true".equals((String)item.get("ASSIGN_CHECK"))) {
                return true;
            }
        }
        if (item.get("CANCEL_CHECK") != null ) {
            if ("true".equals((String)item.get("CANCEL_CHECK"))) {
                return true;
            }
        }
        if (item.get("HAS_ATTEMPT") != null ) {
            if ("true".equals((String)item.get("HAS_ATTEMPT"))) {
                return true;
            }
        }
        /* Added for 'In Progress' status change by Meenakshi */
        if (item.get("IP_CHECK") != null ) {
            if ("true".equals((String)item.get("IP_CHECK"))) {
                return true;
            }
        }
        if (item.get("HAS_NOSHOW") != null ) {
            if ("true".equals((String)item.get("HAS_NOSHOW"))) {
                return true;
            }
        }

        for (Iterator it= getKids().iterator(); it.hasNext();) {
            P2lActivityStatus tmp = (P2lActivityStatus)it.next();
            if (tmp.hasRecordNoSub()) {
                return true;
            }
        }
        return false;
    }

    public String getCurrentAttempt() {
        if (item.get("CURRENTATTEMPTIND") == null) {
            return "";
        }
        return ((BigDecimal)item.get("CURRENTATTEMPTIND")).intValue() + "";

    }

    public boolean childHasRecord() {
        for (Iterator it= getKids().iterator(); it.hasNext();) {
            P2lActivityStatus tmp = (P2lActivityStatus)it.next();
            if (tmp.hasRecord(false)) {
                return true;
            }
        }
        return false;
    }
    public boolean hasRecord( boolean override) {

        if (item.get("REG_CHECK") != null ) {
            if ("true".equals((String)item.get("REG_CHECK"))) {
                //System.out.println("REG_CHECK");
                return true;
            }
        }
        if (item.get("ASSIGN_CHECK") != null ) {
            if ("true".equals((String)item.get("ASSIGN_CHECK"))) {
                //System.out.println("ASSIGN_CHECK");
                return true;
            }
        }
        if (item.get("CANCEL_CHECK") != null ) {
            if ("true".equals((String)item.get("CANCEL_CHECK"))) {
                System.out.println("CANCEL_CHECK");
                return true;
            }
        }
        if (item.get("HAS_ATTEMPT") != null ) {
            if ("true".equals((String)item.get("HAS_ATTEMPT"))) {
                //System.out.println("HAS_ATTEMPT");
                return true;
            }
        }
        /* Added for 'In Progress' status by Meenakshi */
        if (item.get("IP_CHECK") != null ) {
            if ("true".equals((String)item.get("IP_CHECK"))) {
                return true;
            }
        }        
        if (item.get("HAS_NOSHOW") != null ) {
            if ("true".equals((String)item.get("HAS_NOSHOW"))) {
                return true;
            }
        }
        /*End of addition */
        for (Iterator it= getKids().iterator(); it.hasNext();) {
            P2lActivityStatus tmp = (P2lActivityStatus)it.next();
            if (tmp.hasRecordNoSub()) {
                return true;
            }
        }
        return override;
    }
    public boolean hasRecordAtAll() {

        if (item.get("REG_CHECK") != null ) {
            if ("true".equals((String)item.get("REG_CHECK"))) {
                //System.out.println("REG_CHECK");
                return true;
            }
        }
        if (item.get("ASSIGN_CHECK") != null ) {
            if ("true".equals((String)item.get("ASSIGN_CHECK"))) {
                //System.out.println("ASSIGN_CHECK");
                return true;
            }
        }
         if (item.get("CANCEL_CHECK") != null ) {
            if ("true".equals((String)item.get("CANCEL_CHECK"))) {
                //System.out.println("ASSIGN_CHECK");
                return true;
            }
        }
        if (item.get("HAS_ATTEMPT") != null ) {
            if ("true".equals((String)item.get("HAS_ATTEMPT"))) {
                //System.out.println("HAS_ATTEMPT");
                return true;
            }
        }
        /*Added for 'In Progress' status my Meenakshi */
        if (item.get("IP_CHECK") != null ) {
            if ("true".equals((String)item.get("IP_CHECK"))) {
                //System.out.println("HAS_ATTEMPT");
                return true;
            }
        }
        if (item.get("HAS_NOSHOW") != null ) {
            if ("true".equals((String)item.get("HAS_NOSHOW"))) {
                return true;
            }
        }
        /* End of addition */
        for (Iterator it= getKids().iterator(); it.hasNext();) {
            P2lActivityStatus tmp = (P2lActivityStatus)it.next();
            if (tmp.hasRecordAtAll()) {
                return true;
            }
        }
        return false;
    }
    public boolean currenttHasRecord( boolean override) {

        if (item.get("REG_CHECK") != null ) {
            if ("true".equals((String)item.get("REG_CHECK"))) {
                //System.out.println("REG_CHECK");
                return true;
            }
        }
        if (item.get("ASSIGN_CHECK") != null ) {
            if ("true".equals((String)item.get("ASSIGN_CHECK"))) {
                //System.out.println("ASSIGN_CHECK");
                return true;
            }
        }
        if (item.get("CANCEL_CHECK") != null ) {
            if ("true".equals((String)item.get("CANCEL_CHECK"))) {
                //System.out.println("ASSIGN_CHECK");
                return true;
            }
        }
        if (item.get("HAS_ATTEMPT") != null ) {
            if ("true".equals((String)item.get("HAS_ATTEMPT"))) {
                //System.out.println("HAS_ATTEMPT");
                return true;
            }
        }
        /* Added for 'In Progress' status by Meenakshi */
        if (item.get("IP_CHECK") != null ) {
            if ("true".equals((String)item.get("IP_CHECK"))) {
                return true;
            }
        }
        if (item.get("HAS_NOSHOW") != null ) {
            if ("true".equals((String)item.get("HAS_NOSHOW"))) {
                return true;
            }
        }
        /*End of addition */

        return override;
    }
    public boolean isExempt() {
        //if ("Exempt".equals((String)item.get("ESTATUS"))) {
          /* Changing Exempt to Waived by Meenakshi */
          if ("Waived".equals((String)item.get("ESTATUS"))) {
            return true;
        }
        return false;
    }

    public String getStatus() {                      
            String empActivityStatus="";
            if(this.sortedStatusList!=null && this.sortedStatusList.size()>0){
                System.out.println("inside getstatus");
                LatestActivityStatus latestStatus = (LatestActivityStatus) this.sortedStatusList.get(0);
                empActivityStatus = latestStatus.getStatus();  
                this.statusDateToDisplay = latestStatus.getStatusDate();              
            }
            return empActivityStatus;
            
            /*Set statuses = sortedStatusMap.keySet();
            Iterator iter = statuses.iterator();
            if(iter.hasNext()){
                String stat = iter.next().toString();
                System.out.println("latest status is "+stat);
                return stat;
            }else return "";
                
         /*   
        if ( isComplete() ) {            
            if ( isExempt() ) {             
                return "Waived";
            }            
            return "Complete";
        }
        if ( isCancel() ) {             
            //pStatus.setStatusDate(new java.sql.Date(((java.util.Date)item.get("canceldate")).getTime()));
            
            //if(item.get("canceldate"))
            return "Cancelled";
        }
        if ( isRegistered() ) {
            //
            return "Registered";
        }
        // Added for 'In Progress' status by Meenakshi 
        if ( isInProgress() ) {
            return "In Progress";
        }
        if ( isAssigned() ) {
            if ( pendingCheck() ) {
                return "Pending";
            }
            if ( getAssignedRegistered() ) {
                return "Registered";
            }
            return "Assigned";
        }
        


        return ""; */
    }

    /**
     * If Assigned see if any child is registered, if so, then this record is registered also.
     */
    public boolean getAssignedRegistered() {
        if ( isRegistered() ) {
            return true;
        }
        if ( isComplete() ) {
            return true;
        }
        for (Iterator it= getKids().iterator(); it.hasNext();) {
            P2lActivityStatus tmp = (P2lActivityStatus)it.next();
            if (tmp.getAssignedRegistered()) {
                return true;
            }
        }
        return false;
    }

    public boolean pendingCheck() {
        String tmp = Util.toEmpty((String)item.get("PENDING_CHECK"));
        //System.out.println("ASSIGN_CHECK:" + tmp);
        return "yes".equals(tmp);
    }
    public boolean isAssigned() {
        String tmp = Util.toEmpty((String)item.get("ASSIGN_CHECK"));
        //System.out.println("ASSIGN_CHECK:" + tmp);
        return "true".equals(tmp);
    }
    public boolean isCancel() {
        String tmp = Util.toEmpty((String)item.get("CANCEL_CHECK"));
        //System.out.println("ASSIGN_CHECK:" + tmp);
        return "true".equals(tmp);
    }    
    /* Added for 'In Progress' status by Meenakshi */
    public boolean isInProgress() {
        String tmp = Util.toEmpty((String)item.get("IP_CHECK"));
        return "true".equals(tmp);
    }
    
  /*  public boolean isSce() {
         if (item.get("ACTIVITYNAME") != null ) {
            String tmp = Util.toEmpty((String)item.get("ACTIVITYNAME"));           
            System.out.println("Activity Name-----"+tmp);
            System.out.println("Index-----"+tmp.indexOf("Evaluated Sales Call"));
            return (tmp.indexOf("Evaluated Sales Call")>0 || tmp.startsWith("Evaluated Sales Call"));
            //return (tmp.lastIndexOf("Evaluated Sales Call")>0);
         }
        return false;
    }*/
   public boolean isSce() {
    if(item.get("ACTIVITYNAME") != null ){        
       boolean isMapped=SceHandler.isSCE((String)item.get("ACTIVITYNAME"));
      if(isMapped){
        return true;
      }
     else{
        return false;   
    }
    
   }
   return false;
   }

    public boolean isRegistered() {
         if (item.get("HAS_REGISTERED") != null ) {
            String tmp = Util.toEmpty((String)item.get("HAS_REGISTERED"));
            return "true".equals(tmp);
         }
         return false;
    }
    public boolean isAssessment() {
        if (item.get("ACTLABEL_NAME") != null ) {
            return "Assessment".equals(getActLabelName());
        }
        return false;
    }
    public boolean isFullfilment() {
        if (item.get("REL_TYPE") != null ) {
            return "Fulfillment".equals(getRelType());
        }
        return false;
    }
    public boolean isSubscription() {
        if (item.get("REL_TYPE") != null ) {
            return "Subscription".equals(getRelType().trim());
        }
        return false;
    }

    public void getAveragePedScores(Map scoreMap) {
        for (Iterator it= getKids().iterator(); it.hasNext();) {
            P2lActivityStatus tmp = (P2lActivityStatus)it.next();
            if (tmp.isPedagogue() && !Util.isEmpty(tmp.getScore()) && tmp.isSubscription()) {
                scoreMap.put(tmp.getActivityId()+ "",tmp.getScore());
            }
            tmp.getAveragePedScores(scoreMap);
        }

    }
    public void getRegisteredPedExams(Map scoreMap) {
        for (Iterator it= getKids().iterator(); it.hasNext();) {
            P2lActivityStatus tmp = (P2lActivityStatus)it.next();
           // if (tmp.isPedagogue() && ( tmp.isRegistered() || tmp.isComplete()  ) && tmp.isSubscription()) {
             /* Changing the condition for Maintenance Release of Pedagogue scores. This is to take 
              into account all the Pedagogue activities under a particular curriculum */
              System.out.println("Course:Code:"+tmp.getCourseCode());             
              if (tmp.isPedagogue() && ( tmp.isRegistered() || tmp.isComplete()) && (tmp.getCourseCode() !=null) && (!tmp.getCourseCode().equals(""))) {
                System.out.println("Activity ID for Ped exams inside if---"+tmp.getActivityId());
                System.out.println("Activity Name for Ped exams inside if---"+tmp.getActivityName());
                scoreMap.put(tmp.getActivityId()+ "",tmp.getScore());
            }

            tmp.getRegisteredPedExams(scoreMap);
        }

    }

       public boolean isChildSubscription() {       
        for (Iterator it= getKids().iterator(); it.hasNext();)  {
            P2lActivityStatus tmp = (P2lActivityStatus)it.next();       
           if (tmp.isSubscription()) {                
               
                return true;
            }
         
        }
        return false;
    }
    
    public String getRelType() {
        return (String)item.get("REL_TYPE");
    }

    public String getCourseCode() {
        if (item.get("CODE") != null ) {
            return (String)item.get("CODE");
        } else {
            return "";
        }
    }
    public int getActivityId() {
        return ((BigDecimal)item.get("ACTIVITY_PK")).intValue();
    }
    public int getParentid() {
        if (item.get("PRNTACTFK") == null) {
            return 0;
        }
        return ((BigDecimal)item.get("PRNTACTFK")).intValue();
    }
    public String getScore() {
        if ( isSce() ) {
            return "SCE";
        }
        if (item.get("SCORE") == null) {
            return "";
        }
        if ( ((BigDecimal)item.get("SCORE")).intValue() == 0 ) {
            return "";
        }
        return  ((BigDecimal)item.get("SCORE")).intValue() + "";
    }
    public String getActLabelName() {
        return (String)item.get("ACTLABEL_NAME");
    }
    public List getKids() {
        return children;
    }

    public String getEndDate() {
        //return " ";
        if (item.get("ENDDT") != null) {
            System.out.println("Inside........................................... Timestamp");
            //            Timestamp tmp = (Timestamp)item.get("ENDDT");
            Date tmp = (Date)item.get("ENDDT");
            return Util.formatDateShort(tmp);
       } else {
            return " ";
        }
    }
    
    public String getStatusDate() {
        //return " ";
        if (this.activityStatus != null) {            //
            //Timestamp tmp = (Timestamp)item.get("ENDDT");
            Date tmp = this.statusDateToDisplay;
           //Timestamp tmp = (TimeStamp) this.statusDateToDisplay;
           // System.out.println("status date is "+Util.formatDateShort(tmp));
            return Util.formatDateShort(tmp);
       } else {
            return " ";
        }
    }
    
    public Date getRealEndDate() {
        //return " ";
        if (item.get("ENDDT") != null) {
            Date tmp = (Date)item.get("ENDDT");
            return tmp;
       } else {
            return null;
        }
    }
    public void addChild(P2lActivityStatus tmp ) {
        children.add(tmp);
    }

}
