package com.pfizer.db; 

import com.tgix.Utils.Util;
import com.tgix.html.LabelValueBean;
import java.util.ArrayList;
import java.util.List;

public class P2lTrackPhase {
    public static final String EMPTY_FLAG = "EMPTY";
    public static final String FIELD_TRACK_ID = "trackId";
    public static final String FIELD_SORT_ORDER = "sortorder";
    public static final String FIELD_COURSE_CODE_ALT = "coursecodealt";
    public static final String FIELD_ACTIVITYNAME_ALT = "activitynamealt";
    public static final String FIELD_COURSE_CODE = "coursecode";
    public static final String FIELD_ACTIVITYNAME = "activityname";
    public static final String FIELD_TRACK_PHASE_ID = "trackPhaseId";
    public static final String FIELD_PHASE_NUMBER = "phaseNumber";
    public static final String FIELD_ROOT_ACTIVITY_ID = "rootActivityId";
    public static final String FIELD_ALT_ACTIVITY_ID = "altActivityId";
    public static final String FIELD_TRACK_LABEL = "trackLabel";
    public static final String FIELD_PREREQUISITE = "prerequisite";
    public static final String FIELD_APPROVAL_STATUS = "approvalStatus";
    public static final String FIELD_ASSIGNED_STATUS = "assigned";
    public static final String FIELD_EXEMPT_STATUS = "exempt";
    public static final String CONST_PREREQ_NAME = "PREQACT";
    
    private String coursecode;
    private String activityname;

////////START:MERGING///////////////

    private boolean gapExists;

////////START:MERGING///////////////

    private String coursecodealt;
    private String activitynamealt;

    private String sortorder;
    private String trackId;
    private String trackPhaseId;
    private String phaseNumber;
    private String rootActivityId;
    private String altActivityId;
    private String trackLabel;
    private int prerequisite;
    private String approvalStatus;
    private String assigned;
    private String exempt;
    private List activities;
    private P2lTrack track;
    public static List yesNoList;
    public static List yesNoAltList;

    static {
        yesNoList = new ArrayList();
        LabelValueBean bean = new LabelValueBean("Yes","Yes");
        yesNoList.add(bean);
        bean = new LabelValueBean("No","No"); 
        yesNoList.add(bean);    
        yesNoAltList = new ArrayList();
        
        bean = new LabelValueBean("Yes","1");
        yesNoAltList.add(bean);
        bean = new LabelValueBean("No","0"); 
        yesNoAltList.add(bean);    
           
	}    
    public String getCoursecodealt() {
        return this.coursecodealt;
    }
    public void setCoursecodealt( String id ) {
        this.coursecodealt = id;
    }
    public String getSortorder() {
        if (Util.isEmpty(this.sortorder)) {
            return "";
        }
        return this.sortorder;
    }
    public void setSortorder( String sortorder ) {
        this.sortorder = sortorder;
    }
    public String getCoursecode() {
        return this.coursecode;
    }
    public void setCoursecode( String id ) {
        this.coursecode = id;
    }
    public String getActivitynamealt() {
        return this.activitynamealt;
    }
    public void setActivitynamealt( String id ) {
        this.activitynamealt = id;
    }
    public String getActivityname() {
        return this.activityname;
    }
    public void setActivityname( String id ) {
        this.activityname = id;
    }
    public String getTrackPhaseId() {
        return this.trackPhaseId;
    }
    public void setTrackPhaseId( String id ) {
        this.trackPhaseId = id;
    }
    public void setTrack( P2lTrack track ) {
        this.track = track;
    }
    public P2lTrack getTrack() {
        return this.track;
    }
    public void setTrackId( String id ) {
        this.trackId = id;
    }
    public String getTrackId() {
        return this.trackId;
    }
    
    public void setApprovalStatus( String status) {
        this.approvalStatus = status;
    }
    public boolean getApprovalStatus() {
        return "Yes".equals(approvalStatus);
    }
    public void setAssigned( String status) {
        this.assigned = status;
    }
    public boolean getAssigned() {
        return "Yes".equals(assigned);
    }
    public void setExempt( String status) {
        this.exempt = status;
    }
    public boolean getExempt() {
        return "Yes".equals(exempt);
    }
    
    public void setPhaseNumber( String phase ) {
        this.phaseNumber = phase;
    }
    public String getPhaseNumber() {
        return phaseNumber;
    }
    public void setPrerequisite( int prerequisite ) {
        this.prerequisite = prerequisite;
    }
    public boolean getPrerequisite() {
        if ( this.prerequisite == 1) {
            return true;
        }
        return false;
    }
    public void setRootActivityId( String rootActivityId ) {
        this.rootActivityId = rootActivityId;
    }
    public String getRootActivityId() {
        return this.rootActivityId;
    }
    public void setAltActivityId( String altActivityId ) {
        this.altActivityId = altActivityId;
    }
    public String getAlttActivityId() {
        return altActivityId;
    }
    
    public void setTrackLabel( String label ) {
        this.trackLabel = label;
    }
    public String getTrackLabel() {
        return this.trackLabel;
    }
    public void setActivities( List acts ) {
        this.activities = acts;
    }
    public List getActivities() {
        return this.activities;
    }
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("\nP2lTrackPhase");
        sb.append("\ntrackId:" + trackId);
        sb.append("\nphaseNumber:" + phaseNumber);
        sb.append("\nrootActivityId:" + rootActivityId);
        sb.append("\naltActivityId:" + altActivityId);
        sb.append("\n----------");
        
        return sb.toString();
    }
    
    ////////START:MERGING///////////////

 	 public boolean getGapExists() {
	        return this.gapExists;
} 
    public void setGapExists( boolean id ) {
	        this.gapExists = id;
    }

} //////////END:MERGING///////////////////
