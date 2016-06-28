package com.pfizer.db; 

public class ActivityHierarchy2 {
    private int activityPk;
    private String activityName;
    private String actlabelName;
    private int parent;
    private String parentName;
    private String parentLabel;
    private String relType;
    
    public ActivityHierarchy2() {}

	public int getActivityPk() {
		return activityPk;
	}

	public void setActivityPk(int activityPk) {
		this.activityPk = activityPk;
	}

	public String getActivityName() {
		return activityName;
	}

	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}

	public String getActlabelName() {
		return actlabelName;
	}

	public void setActlabelName(String actlabelName) {
		this.actlabelName = actlabelName;
	}

	public int getParent() {
		return parent;
	}

	public void setParent(int parent) {
		this.parent = parent;
	}

	public String getParentName() {
		return parentName;
	}

	public void setParentName(String parentName) {
		this.parentName = parentName;
	}

	public String getParentLabel() {
		return parentLabel;
	}

	public void setParentLabel(String parentLabel) {
		this.parentLabel = parentLabel;
	}

	public String getRelType() {
		return relType;
	}

	public void setRelType(String relType) {
		this.relType = relType;
	} 
    
    
} 
