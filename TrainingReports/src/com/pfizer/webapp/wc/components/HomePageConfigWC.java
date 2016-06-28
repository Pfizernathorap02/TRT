package com.pfizer.webapp.wc.components; 

import com.pfizer.webapp.AppConst;
import com.tgix.wc.WebComponent;
import java.util.List;

public class HomePageConfigWC extends WebComponent
{ 
     private String postUrl = "/TrainingReports/admin/editHomePage" ; 
     private String target = "";
     private List sectionList;
     private List idList;
     private List checkedValuesList;
     private String errorMsg="";
   //  private String onSubmit = "javascript:concatSelectedFields();";
     
    
    public String getPostUrl(){
        return this.postUrl;
    }
     public String getTarget(){
        return this.target;
    }
    // public String getOnSubmit(){
     //   return this.onSubmit;
    //}
    public void setSectionList(List sectionList){
        this.sectionList = sectionList;
    }
    public List getSectionList(){
        return sectionList;
    }
    
    public void setIdList(List idList){
        this.idList = idList;
    }
    public List getIdList(){
        return idList;
    }
     public void setCheckedValuesList(List checkedValuesList){
        this.checkedValuesList = checkedValuesList;
    }
    public List getcheckedValuesList(){
        return checkedValuesList;
    }
    
    public void setErrorMsg( String msg ) {
        this.errorMsg = msg;
    }
    public String getErrorMsg() {
        return this.errorMsg;
    }
    
    public String getJsp() {
		return AppConst.JSP_LOC + "/components/admin/editHomePage.jsp";
	}

    public void setupChildren() {
    }
    
 

} 
