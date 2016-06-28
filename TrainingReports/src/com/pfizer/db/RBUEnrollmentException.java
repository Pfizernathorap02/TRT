package com.pfizer.db; 

import java.util.Date;

public class RBUEnrollmentException 

{ 
    private String emplId;
    private String firstname;
    private String lastname;
    private Date exceptionDate;
    private String reason;
    private String products;
    
    public String getFirstname() {
		return this.firstname;
	}	
	public void setFirstname( String firstname ) {
		this.firstname = firstname;
	}
    public String getEmplId() {
		return this.emplId;
	}	
	public void setEmplId( String id ) {
		this.emplId = id;
	}
    
    public String getLastname() {
		return this.lastname;
	}	
	public void setLastname( String lastname ) {
		this.lastname = lastname;
	}
    public Date getExceptionDate() {
        return exceptionDate;
    }
    
    public void setExceptionDate(Date exceptionDate) {
        this.exceptionDate = exceptionDate;
    }
    
    public String getReason() {
		return this.reason;
	}	
	public void setReason( String reason ) {
		this.reason = reason;
	}
    
        public String getProducts() {
		return this.products;
	}	
	public void setproducts( String products ) {
		this.products = products;
	}
    
    
} 
