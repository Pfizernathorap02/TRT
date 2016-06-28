package com.pfizer.db; 

import java.util.ArrayList;

public class HQuserlist 
{ 
    public static final String FIELD_FNAME	= "firstname";
	public static final String FIELD_LNAME       = "lastname";
    public static final String FIELD_HQUSERLIST  = "hquser";
    
    private String fName;
	private String lName;
    private String user;
    
    public HQuserlist() {}
    
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
    
    public String getHQUser() {
		return user;
	}

	public void setHQUser(String User) {
		this.user = User;
	}
} 
