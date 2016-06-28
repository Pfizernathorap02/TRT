package com.pfizer.db; 

import java.util.List;

public class P2lActivityTree {
    public static final String FIELD_TRACK_ID = "trackId";
    public static final String FIELD_TRACK_LABEL = "trackLabel";
    private String trackId;
    private String trackLabel;
    private List phases;
    
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
    public void setPhases( List list ) {
        this.phases = list;
    }
    public List getPhases() {
        return this.phases;
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
