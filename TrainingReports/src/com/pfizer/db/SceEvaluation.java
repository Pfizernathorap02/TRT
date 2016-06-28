package com.pfizer.db; 

import java.util.ArrayList;

public class SceEvaluation 
{ 
    public static final String FIELD_USERGROUP	= "userGroup";
	public static final String FIELD_SAVE       = "save";
	public static final String FIELD_SUBMIT		= "submit";
    
    private String userGroup;
	private String save;
	private String submit;
    
    public SceEvaluation() {}
    
     public String getUserGroup() {
		return userGroup;
	}

	public void setUserGroup(String userGroup) {
		this.userGroup = userGroup;
	}
    
    public String getSave() {
		return save;
	}

	public void setSave(String save) {
		this.save = save;
	}
    
    public String getSubmit() {
		return submit;
	}

	public void setSubmit(String submit) {
		this.submit = submit;
	}
    
} 
