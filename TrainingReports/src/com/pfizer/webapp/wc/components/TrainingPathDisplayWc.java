package com.pfizer.webapp.wc.components; 

import com.pfizer.webapp.AppConst;
import com.pfizer.webapp.user.User;
import com.tgix.wc.WebComponent;
import java.util.List;

public class TrainingPathDisplayWc extends WebComponent
{ 
    private String message;
    private List path;
    private String user;
    
    public String getJsp()
    {
       return AppConst.JSP_LOC + "/components/trainingPathDisplay.jsp";
    }

    public void setupChildren()
    {
    }
    public String getMessage() {
        return message;
    }
    public void setMessage( String message ) {
        this.message = message;
    }
    public List getTrainingPath() {
        return path;
    }
    public void setTrainingPath( List path ) {
        this.path = path;
    }
    public String getUser() {
		return this.user;
	}
    public void setUser(String user){
		//return this.user;
         this.user = user;
	}
} 
