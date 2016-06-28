package com.pfizer.db; 

public class SubActivityBean {
	private String rootActivityID;
	private String activityName;
	private String activityPK;

	  
    public String getRootActivityID() {
        return rootActivityID;
    }
    
    public void setRootActivityID(String rootActivityID) {
        this.rootActivityID =rootActivityID;
    }
    public String getActivityName() {
        return activityName;
    }
    
    public void setActivityName(String activityName) {
        this.activityName =activityName;
    }
    public String getActivityPK() { 
        return activityPK;
    }
    
    public void setActivityPK(String activityPK) {
        this.activityPK =activityPK;
    }
}
