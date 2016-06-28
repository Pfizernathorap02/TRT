package com.pfizer.webapp.wc.components; 

import com.pfizer.webapp.AppConst;
import com.tgix.wc.WebComponent;
import java.util.List;

//Added file for TRT 3.6 Enhancement F4.4 - (admin configuration of employee grid)

public class EmployeeGridConfigAdminWc extends WebComponent
{ 
    private String postUrl = "/TrainingReports/admin/employeeGridConfig" ; 
    	private String target = "";
    	private String onSubmit = "javascript:concatSelectedFields();";
    	private String selectedOptFields;
    	private List optFields;
    
    
    public String getPostUrl(){
        return this.postUrl;
    }
     public String getTarget(){
        return this.target;
    }
     public String getOnSubmit(){
        return this.onSubmit;
    }
    
    public void setSelectedOptFields(String r) {
    	this.selectedOptFields = r;
    }	
    public void setOptFields(List r) {
    	this.optFields = r;
    }	
    
    public String getSelectedOptFields() {
    	return this.selectedOptFields;
    }	
    public List getOptFields() {
    	return this.optFields;
    }	

    public String getJsp() {
		return AppConst.JSP_LOC + "/components/admin/employeeGridConfig.jsp";
	}

    public void setupChildren() {
    }

} 
