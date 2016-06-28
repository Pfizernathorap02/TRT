package com.pfizer.db; 

import java.util.Date;
import java.util.HashMap;

public class GeneralSessionEmployee { 
    
    public static final String ATTRIBUTE_NAME = "GeneralSessionResult";
    
	private String districtDesc = "";	
	private String teamCode = "";
	private String lastName = "";
    private String firstName = "";
    private String role = "";
    private String emplId = "";
    private String attended = ""; //P for Attended Status, Blank for Non Attendance.
    private Integer courseId;
    private Date startDate;
    
    private HashMap productStatusMap=new HashMap();
	
	public GeneralSessionEmployee() {}
        
	public String getEmplId() {
		return this.emplId;
	}	
	public void setEmplId( String id ) {
		this.emplId = id;
	}
        
	public void setAttended(String attended) {
		this.attended = attended;
	}
	public String getAttended() {
		return this.attended;
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
	
	public String getTeamCode() {
		return teamCode;
	}
	public void setTeamCode(String code) {
		this.teamCode = code;
	}
		
	public String getDistrictDesc() {
		return this.districtDesc;
	}
	public void setDistrictDesc( String desc ) {
		this.districtDesc = desc;
	}
	
	public String getRole() {
		return this.role;
	}
	public void setRole(String role) {
		this.role = role;
	}

    public Integer getCourseId() {
        return courseId;
    }
    
    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }        
	
    public Date getStartDate() {
        return startDate;
    }
    
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }
    
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append( "\n" + this.getClass().getName() + "\n" );
		sb.append( "       emplId: " + emplId + "\n" );
        sb.append( "       courseId: " + courseId + "\n" );
		sb.append( "    firstName: " + firstName + "\n" );
		sb.append( "     lastName: " + lastName + "\n" );
		sb.append( "         role: " + role + "\n" );
		sb.append( "     teamCode: " + teamCode + "\n" );		
		return sb.toString();
	}
} 
