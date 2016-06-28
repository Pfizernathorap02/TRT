package com.pfizer.db; 

import java.util.Date;
import java.util.HashMap;

public class RBUTrainee { 
	private String emplId = "";
	private String role = "";
	private String lastName = "";
	private String firstName = "";
	//private String table_id = "";
	//private String class_id = "";
	
	public RBUTrainee() {}
    
    public String getEmplId() {
		return this.emplId;
	}	
	public void setEmplId( String id ) {
		this.emplId = id;
	}
	

	
	public void setFirstName( String first ) {
		this.firstName = first;
	}
	public String getFirstName() {
		return this.firstName;
	}
	
	public void setLastName( String lastName ) {
		this.lastName = lastName;
	}
	public String getLastName() {
		return this.lastName;
	}
	

	public String getRole() {
		return this.role;
	}
	public void setRole(String role) {
		this.role = role;
	}

} 
