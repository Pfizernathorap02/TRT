package com.pfizer.db; 

import java.util.Date;

public class EmpSearchTSHT extends Employee{ 
	private String activityName;
    private String fieldActive;    
    private String courceCode;   
    private String courceName;  
    private Date completionDate;   
    private String hireDate;
    private String scores;
    private String notes;
    private String hrStatus;    
    private String courseStatus;
    private String credits;
    
    
	public String getActivityName() {
		return activityName;
	}
	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}
    
	public String getFieldActive() {
		return fieldActive;
	}
	public void setFieldActive(String fieldActive) {
		this.fieldActive = fieldActive;
	}

	public String getCourceCode() {
		return courceCode;
	}
	public void setCourceCode(String courceCode) {
		this.courceCode = courceCode;
	}

	public String getCourceName() {
		return courceName;
	}
	public void setCourceName(String courceName) {
		this.courceName = courceName;
	}

	public Date getCompletionDate() {
		return completionDate;
	}
	public void setCompletionDate(Date completionDate) {
		this.completionDate = completionDate;
	}

	public String getHire_Date() {
		return hireDate;
	}
	public void setHire_Date(String hireDate) {
		this.hireDate = hireDate;
	}

	public String getScores() {
		return scores;
	}
	public void setScores(String scores) {
		this.scores = scores;
	}

	public String getNotes() {
		return notes;
	}
	public void setNotes(String notes) {
		this.notes = notes;
	}

	public String getHrStatus() {
		return hrStatus;
	}
	public void setHrStatus(String hrStatus) {
		this.hrStatus = hrStatus;
	}


    public String getCredits() {
		return credits;
	}
	public void setCredits(String credits) {
		this.credits = credits;
	}

} 
