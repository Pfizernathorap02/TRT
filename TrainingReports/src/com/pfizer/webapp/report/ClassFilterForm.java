package com.pfizer.webapp.report; 

import com.pfizer.db.Product;
import com.tgix.Utils.Util;
import com.tgix.html.LabelValueBean;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ClassFilterForm {
	// form field names
	public static final String FIELD_CLASSROOM		= "ClassFilterForm_classroom";									
	public static final String FIELD_TRAININGDATE	= "ClassFilterForm_trainingDate";									
	public static final String FIELD_PRODUCT		= "ClassFilterForm_product";		
    public static final String FIELD_STARTDATE		= "ClassFilterForm_startDate";		
    public static final String FIELD_ENDDATE		= "ClassFilterForm_endDate";		
    public static final String FIELD_TEAMCD         = "ClassFilterForm_teamCd";		
    public static final String FIELD_ENROLLMENTDATE = "ClassFilterForm_EnrollmentDate";		
    public static final String FIELD_ROLEGRROUP         = "ClassFilterForm_roleGroup";	
    public static final String FIELD_ISMANAGER         = "ClassFilterForm_ifManager";
    //added for rbu training schedule by track report 
    public static final String FIELD_CLASS = "ClassFilterForm_class";
    public static final String FIELD_TRACK = "ClassFilterForm_track";				

    							
	// form values selected								
	private String classroom;
	private String trainingDate;
	private String product;	
    private String startDate;	
    private String endDate;	
    private String teamCd;	
    private String enrollmentDate ="";	
     //added for rbu training schedule by track report 
    private String classes;	
    private String rolegroup;	
    private String track;	
    private String ifmanager;
    
	
    public ClassFilterForm() {
		classroom = new String();
		trainingDate = new String();
		product = new String(); 
        teamCd = new String(); 
        startDate = new String(); 
        endDate = new String();        
	}
	
    	
	/////////////////////////////////////////////////////////////////////////////
	// Form values
	/////////////////////////////////////////////////////////////////////////////
	public String getClassRoom() {
		return this.classroom;
	}
	public void setClassroom( String classroom ) {
		if ( Util.isEmpty( classroom ) ) {
			return;
		}
		this.classroom = classroom;
	} 
	
	public String getTrainingDate() {
		return this.trainingDate;
	}
	public void setTrainingDate( String trainingDate ) {
		if ( Util.isEmpty( trainingDate ) ) {
			return;
		}

		this.trainingDate = trainingDate;
	}
	
	public String getProduct() {
		return this.product;
	}
	public void setProduct( String product ) {
		if ( Util.isEmpty( product ) ) {
			return;
		}
		this.product = product;
	}
    
    public String getTeamCd() {
		return this.teamCd;
	}
	public void setTeamCd( String teamCd ) {
		if ( Util.isEmpty( teamCd ) ) {
			return;
		}
		this.teamCd = teamCd;
	}
    
    public String getStartDate() {
		return this.startDate;
	}
	public void setStartDate( String startDate ) {
		if ( Util.isEmpty( startDate ) ) {
			return;
		}

		this.startDate = startDate;
	}
    
    public String getEndDate() {
		return this.endDate;
	}
	public void setEndDate( String endDate ) {
		if ( Util.isEmpty( endDate ) ) {
			return;
		}

		this.endDate = endDate;
	}
    
    public String getEnrollmentDate() {
		return this.enrollmentDate;
	}
	public void setEnrollmentDate( String enrollmentDate ) {
		if ( Util.isEmpty( enrollmentDate ) ) {
			return;
		}

		this.enrollmentDate = enrollmentDate;
	}
    
    public String getClasses() {
		return this.classes;
	}
	public void setClasses( String classes ) {
		this.classes = classes;
	}
    
    public String getRolegroup() {
		return this.rolegroup;
	}
	public void setRolegroup( String rolegroup ) {
		this.rolegroup = rolegroup;
	}
        
    public String getTrack() {
		return this.track;
	}
	public void setTrack( String track ) {
		this.track = track;
	}
    
    public String getIfmanager() {
		return this.ifmanager;
	}
	public void setIfmanager( String ifmanager ) {
		this.ifmanager = ifmanager;
	}
    
} 
