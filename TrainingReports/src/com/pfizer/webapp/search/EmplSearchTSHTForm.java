package com.pfizer.webapp.search; 

public class EmplSearchTSHTForm {
	public static String FIELD_FNAME = "SearchForm_fname";
	public static String FIELD_LNAME = "SearchForm_lname";
    public static String FIELD_EMPLID = "SearchForm_emplid";
    public static String FIELD_COURSE_NAME = "SearchForm_courseName";
    
	
	private String fname = new String();
	private String lname = new String();
    private String emplid=new String();
    private String courseName=new String();
	 
	public void setFname( String fname ) {
		this.fname = fname;
	} 
	public String getFname() {
		return fname;
	}
	
	public void setLname( String lname ) {
		this.lname = lname;
	} 
	public String getLname() {
		return lname;
	}
    
    public String getEmplid() {
		return this.emplid;
	}
	public void setEmplid(String emplid) {
		this.emplid = emplid;
	}
	public String getCourseName() {
		return this.courseName;
	}
	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}
	 
    
    
	
} 
