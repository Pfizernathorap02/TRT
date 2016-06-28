package com.pfizer.webapp.wc.components.admin;

import com.pfizer.webapp.AppConst;
import com.tgix.printing.LoggerHelper;
import com.tgix.wc.WebComponent;
import java.util.List;

public class ActivityDrillDownAdminWc extends WebComponent {
    
    

private String postUrl = "/TrainingReports/activityDrilldownConfig.do " ; 
    	private String target = "";
    	private String onSubmit = "";
    	private String currGroup;
    	private List groups;
        
public ActivityDrillDownAdminWc(){LoggerHelper.logSystemDebug("rishi1"); }


    public void setCurrGroup(String r) {
    	this.currGroup = r;
    }	
    public void setGroups(List grps) {
    	this.groups = grps;
    }	
    public void setPostUrl(String postUrl) {
    	this.postUrl = postUrl;
    }	
    public void setOnSubmit(String onSubmit) {
    	this.onSubmit = onSubmit;
    }
    public void setTarget(String target) {
    	this.target = target;
    }
    public List getGroups() {
    	return this.groups;
    }	
    public String getCurrGroup() {
    	return this.currGroup;
    }	
   public String getPostUrl() {
    	return postUrl;
    }	
    public String getOnSubmit() {
    	return onSubmit;
    }
    public String getTarget() {
    	return target;
    }
    public String getJsp() {
		return AppConst.JSP_LOC + "/components/activityDrillDownAdmin.jsp";
	}

    public void setupChildren() {
    }
}

