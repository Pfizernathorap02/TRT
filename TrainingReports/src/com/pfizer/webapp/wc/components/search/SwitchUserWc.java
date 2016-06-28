package com.pfizer.webapp.wc.components.search; 

import com.pfizer.db.EmpSearch;
import com.pfizer.webapp.AppConst;
import com.pfizer.webapp.user.User;
import com.tgix.Utils.LoggerHelper;
import com.tgix.wc.WebComponent;
import java.util.List;

public class SwitchUserWc extends WebComponent
{ 
    
    EmpSearch[] arr;
    User user;
	
	public SwitchUserWc(EmpSearch[] arr) {
       
		this.arr = arr;
        
	}
	
	public EmpSearch[] getResults() {
		return arr;
	}	
    public String getJsp() {
		return AppConst.JSP_LOC + "/components/search/SwitchUser.jsp";
	}

    public void setupChildren() {
    }
    //Added for TRT Phase 2 - To provide link in switch user.
    public User getUser(){
        return this.user;
    }
    public void setUser(User user){
        this.user = user;
    }
 
} 
