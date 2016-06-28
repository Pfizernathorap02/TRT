package com.pfizer.db; 

import java.util.Date;

public class SceFull {
	
	private String rating;
	private String status;
	private String emplid;
	private String evalFName;
	private String evalLName;
	private String comment;
	private Date evalDate;
	private String eventId;
    private String productCd;
	
    /*For Failure Report*/
    private String role;
	private String teamCode;
	private String clusterCode;
    private String productName;
    private Double score;
    private String email;
    
	public SceFull(){}
	
    public void setEventId( String id ) {
        this.eventId = id;
    }
    public String getEventId() {
        return this.eventId;
    }
    public void setProductCd( String id ) {
        this.productCd = id;
    }
    public String getProductCd() {
        return this.productCd;
    }
	public void setRating(String rating) {
		this.rating = rating;
	} 
	public String getRating() {
		return rating;
	}
	public void setStatus(String status) {
		this.status = status;
	} 
	public String getStatus() {
		return status;
	}
	public String getEmplid() {
		return this.emplid;
	}
	public void setEmplid(String id) {
		this.emplid = id;
	}
	
	public void setEvalFName( String fname ) {
		this.evalFName = fname;
	}
	public String getEvalFName() {
		return this.evalFName;
	}
	
	public void setEvalLName( String lname ) {
		this.evalLName = lname;
	}
	public String getEvalLName() {
		return this.evalLName;
	}

	public void setEvalDate( Date date ) {
		this.evalDate = date;
	}
	public Date getEvalDate() {
		return this.evalDate;
	}
	public void setComment( String comment ) {
		this.comment = comment;
	}
	public String getComment() {
		return this.comment;
	}
    
    public String getTeamCode() {
		return teamCode;
	}
	public void setTeamCode(String code) {
		this.teamCode = code;
	}
	
	public String getClusterCode() {
		return clusterCode;
	}
	
	public void setClusterCode(String code) {
		this.clusterCode = code;
	}
    
    public String getRole() {
		return this.role;
	}
	public void setRole(String role) {
		this.role = role;
	}
    
    public void setProductName( String productName ) {
        this.productName = productName;
    }
    public String getProductName() {
        return this.productName;
    }
    
    public void setScore( Double score ) {
        this.score = score;
    }
    public Double getScore() {
        return this.score;
    }
    
    public void setEmail( String email ) {
        this.email = email;
    }
    public String getEmail() {
        return this.email;
	}
	
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append( "\n" + this.getClass().getName() + "\n" );
		sb.append( "rating: " + rating + "\n" );
		sb.append( "emplid: " + emplid + "\n" );
		sb.append( "comment: " + comment + "\n" );
		sb.append( "evalDate: " + evalDate + "\n" );
		sb.append( "evalFName: " + evalFName + "\n" );
		sb.append( "evalLName: " + evalLName + "\n" );
		return sb.toString();
	}
} 
