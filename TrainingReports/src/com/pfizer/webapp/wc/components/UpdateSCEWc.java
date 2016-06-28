package com.pfizer.webapp.wc.components; 

import com.pfizer.db.Employee;
import com.pfizer.webapp.AppConst;
import com.pfizer.webapp.user.User;
import com.tgix.wc.WebComponent;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Vector;

public class UpdateSCEWc extends WebComponent { 
	private User user;
    private String errorMsg = "";
	private String courseCode;
	private String eventID;

	public String getCourseCode() {
		return courseCode;
	}
	public void setCourseCode(String courseCode) {
		this.courseCode = courseCode;
	}
	public String getEventID() {
		return eventID;
	}
	public void setEventID(String eventID) {
		this.eventID = eventID;
	}

    
	public UpdateSCEWc(User user) {
		this.user = user;
	}	
    
    public String getErrorMsg() {
        return errorMsg;
    }
    public void setErrorMsg( String str ) {
        this.errorMsg = str;
    }
    	
    public User getUser() {
		return user;
	}    
    public String getJsp(){
		return AppConst.JSP_LOC + "/components/adminSCE/updateSCE.jsp";
	}  
	public void setupChildren() {}    

} 
