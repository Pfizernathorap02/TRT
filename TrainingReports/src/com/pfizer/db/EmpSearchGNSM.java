package com.pfizer.db; 

import java.util.Date;

public class EmpSearchGNSM extends Employee{ 
	private String activityName;
	private String activityStatus;
	private String overallStatus;    
	public String getActivityName() {
		return activityName;
	}
	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}
	public String getActivityStatus() {
		return activityStatus;
	}
	public void setActivityStatus(String activityStatus) {
		this.activityStatus = activityStatus;
	}
	public String getOverallStatus() {
		return overallStatus;
	}
	public void setOverallStatus(String overallStatus) {
		this.overallStatus = overallStatus;
	}
    
    
} 
