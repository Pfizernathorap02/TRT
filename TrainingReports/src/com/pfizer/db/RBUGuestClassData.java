package com.pfizer.db; 



import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;



public class RBUGuestClassData {
	private String emplid;
    private String firstname;
    private String lastname;
    private String classid;
    private String email;
    private String enrolledby;
    private String nt_domain;
    private String nt_id;
    

	public RBUGuestClassData() {}

	public String getEmplid() {
		return emplid;
	}

	public void setEmplid(String emplid) {
		this.emplid = emplid;
    }
    
    public String getNt_domain() {
		return nt_domain;
	}

	public void setNt_domain(String nt_domain) {
		this.nt_domain = nt_domain;
    }


	public String getNt_id() {
		return nt_id;
	}

	public void setNt_id(String nt_id) {
		this.nt_id = nt_id;
    }


	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}	
    
    public String getFirstname() {
		return firstname;
	}

	public void setProductdesc(String lastname) {
		this.lastname = lastname;
	}	
    
    public String getLastname() {
		return lastname;
	}
    public void setLastname(String lastname) {
		this.lastname = lastname;
	}	
    
        
    public String getClassid() {
		return classid;
	}
	public void setClassid(String classid) {
		this.classid = classid;
	}
    	
    public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}	
        	
    public String getEnrolledby() {
		return enrolledby;
	}
	public void setEnrolledby(String enrolledby) {
		this.enrolledby = enrolledby;
	}

} 

