package com.pfizer.db; 

public class PassFail {
	// possible statuses
	public static final String CONST_TEST_NOT_TAKEN = "Not Complete";
	public static final String CONST_TEST_PASS		= ">=80%";
	public static final String CONST_TEST_FAIL		= "<80%";
	
	
	private String status;
	private String examName;
	private String emplid;
	private int score = 0;
	private String setId;	
	
	public PassFail(){}
	
	public void setExamName(String name) {
		this.examName = name;
	}
	public String getExamName() {
		return this.examName;
	}
	public void setStatus(String status) {
		this.status = status;
	} 
	public String getStatus() {
		return status;
	}
	
	public void setEmplid(String id) {
		this.emplid = id;
	}
	public String getEmplId() {
		return this.emplid;
	}
	
	public String getSetId() {
		return setId;
	}
	public void setSetId(String setId) {
		this.setId = setId;
	}
	public int getScore() {
		return score;
	}
	public void setScore( int score ) {
		this.score = score;
	}
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append( "\n" + this.getClass().getName() + "\n" );
		sb.append( "examName: " + examName + "\n" );
		sb.append( "  emplid: " + emplid + "\n" );
		sb.append( "  status: " + status + "\n" );
		
		return sb.toString();
	}
	
	public boolean equals( Object obj ) {
		return (obj instanceof PassFail && 
					this.getExamName().equals( ((PassFail)obj).getExamName() ) &&
					this.getEmplId().equals( ((PassFail)obj).getEmplId() ) );
	} 
	
	/**
	 * wrote this because i needed really needed to override equals().
	 */
	public int hashCode() { 
		int hash = 1;
		hash = hash * 31 + getEmplId().hashCode();
		hash = hash * 31 
        + (getEmplId() == null ? 0 : getEmplId().hashCode());
		return hash;
	}
} 
