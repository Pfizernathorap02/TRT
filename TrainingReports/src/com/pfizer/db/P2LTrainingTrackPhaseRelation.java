package com.pfizer.db; 

import java.util.ArrayList;
import java.util.Vector;

public class P2LTrainingTrackPhaseRelation { 
    private String trackID;
    private String trackLabel;
    private Vector phaseNumber;
    private Vector rootActivityID;
        
	public String getTrackID() {
		return trackID;
	}
	public void setTrackID(String trackID) {
		this.trackID = trackID;
	}
	public String getTrackLabel() {
		return trackLabel;
	}
	public void setTrackLabel(String trackLabel) {
		this.trackLabel = trackLabel;
	}
	public Vector getPhaseNumber() {
        if(phaseNumber==null){
            phaseNumber = new Vector();    
        }
		return phaseNumber;
	}
	public void setPhaseNumber(Vector phaseNumber) {
		this.phaseNumber = phaseNumber;
	}
	public Vector getRootActivityID() {
        if(rootActivityID==null){
            rootActivityID = new Vector();
        }
		return rootActivityID;
	}
	public void setRootActivityID(Vector rootActivityID) {
		this.rootActivityID = rootActivityID;
	}
    
    
} 
