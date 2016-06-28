package com.pfizer.webapp.wc.components; 

import com.pfizer.db.Employee;
import com.pfizer.webapp.AppConst;
import com.pfizer.webapp.user.User;
import com.tgix.wc.WebComponent;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Vector;

public class ListSCEWc extends WebComponent { 
	private User user;
    private String errorMsg = "";
    private Vector SCEList;    
    
	public ListSCEWc(User user) {
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
		return AppConst.JSP_LOC + "/components/adminSCE/listSCE.jsp";
	}  
	public void setupChildren() {}
    
    public Vector getSCEList() {
		return SCEList;
	}

	public void setSCEList(Vector list) {
		SCEList = list;
	}


} 
