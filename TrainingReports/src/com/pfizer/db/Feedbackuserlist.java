package com.pfizer.db; 

import java.util.ArrayList;

public class Feedbackuserlist 
{ 
    public static final String FIELD_FNAME	= "firstname";
	public static final String FIELD_LNAME       = "lastname";
    public static final String FIELD_FBUSERLIST  = "fbuser";
    
    private String fName;
	private String lName;
    private String user;
    
    public Feedbackuserlist() {}
    
     public String getFName() {
		return fName;
	}

	public void setFName(String FName) {
		this.fName = FName;
	}
    
    public String getLName() {
		return lName;
	}

	public void setLName(String LName) {
		this.lName = LName;
	}
    
    public String getFbUser() {
		return user;
	}

	public void setFbUser(String User) {
		this.user = User;
	}
} 
