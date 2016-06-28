package com.pfizer.db;

import com.tgix.Utils.Util;
import com.tgix.html.LabelValueBean;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class LaunchMeeting {
    public static final String FIELD_TRACK_ID = "trackId";
    public static final String FIELD_TRACK_LABEL = "trackLabel";
    public static final String FIELD_TRACK_TYPE = "trackType";
    public static final String FIELD_DO_OVERALL = "doOverall";
    public static final String FIELD_DO_SUBMIT = "doSubmit";
    public static final String FIELD_DO_PHASE_SUBMIT = "doPhaseSubmit";



    private String doSubmit;
    private String doPhaseSubmit;
    private String doOverall;
    private String trackId;
    private String trackType;
    private String trackLabel;
    private List phases = new ArrayList();
    private List unFilteredList = new ArrayList();

    public static List yesNolist = new ArrayList();

    static {
        yesNolist = new ArrayList();
        LabelValueBean bean = new LabelValueBean("Yes","Y");
        yesNolist.add(bean);
        bean = new LabelValueBean("No","N");
        yesNolist.add(bean);
	}


    public LaunchMeeting() {
    }
    public void setTrackId( String id ) {
        this.trackId = id;
    }
    public String getTrackId() {
        return this.trackId;
    }
    public void setTrackLabel( String label ) {
        this.trackLabel = label;
    }
    public String getTrackLabel() {
        return this.trackLabel;
    }
    public String getTrackType() {
        return trackType;
    }
    public void setTrackType( String type ) {
        this.trackType = type;
    }
    public void setDoPhaseSubmit( String str ) {
        this.doPhaseSubmit = str;
    }
    public String getDoPhaseSubmit() {
        return this.doPhaseSubmit;
    }
    public void setDoSubmit( String str ) {
        this.doSubmit = str;
    }
    public String getDoSubmit() {
        return this.doSubmit;
    }
    public void setDoOverall( String str ) {
        this.doOverall = str;
    }
   
    public boolean getDoOverall() {
        return "Y".equals(this.doOverall);

    }
    public void setPhases( List list ) {
        phases.clear();
        unFilteredList.clear();
        unFilteredList.addAll(list);
        for ( Iterator it = list.iterator(); it.hasNext(); ) {
            LaunchMeetingDetails tmp = (LaunchMeetingDetails)it.next();
            if ( !"EMPTY".equals( tmp.getPhaseNumber() ) ) {
                phases.add(tmp);
            }
        }
    }
    public List getCompletePhaseList() {
        return unFilteredList;
    }
    public List getPhases() {
        return this.phases;
    }

    public String getAllNodesDelimit() {
        StringBuffer sb = new StringBuffer();
        for ( Iterator it = phases.iterator(); it.hasNext(); ) {
            LaunchMeetingDetails tmp = (LaunchMeetingDetails)it.next();
            if (Util.isEmpty( sb.toString() )) {
                sb.append(tmp.getRootActivityId());
                if ( !Util.isEmpty( tmp.getAlttActivityId() ) ) {
                    sb.append( "," + tmp.getAlttActivityId() );
                }
            } else {
                sb.append("," + tmp.getRootActivityId());
                if ( !Util.isEmpty( tmp.getAlttActivityId() ) ) {
                    sb.append( "," + tmp.getAlttActivityId() );
                }
            }
        }
        return sb.toString();
    }
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("\nP2lTrack");
        sb.append("\ntrackId:" + trackId);
        sb.append("\ntrackLabel:" + trackLabel);
        sb.append("\n----------");

        return sb.toString();
    }
}
