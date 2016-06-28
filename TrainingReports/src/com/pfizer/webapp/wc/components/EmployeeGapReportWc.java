package com.pfizer.webapp.wc.components; 

import com.pfizer.webapp.AppConst;
import com.tgix.wc.WebComponent;
import java.util.List;

public class EmployeeGapReportWc extends WebComponent
{ 
    private List statusForGapList;
    private List statusForNotGapList;
    private String user;
    
    public void setStatusForGapList(List statusForGapList){
        this.statusForGapList=statusForGapList;   
    }
    public List getStatusForGapList(){
        return this.statusForGapList;
    }
    public void setStatusForNotGapList(List statusForNotGapList){
        this.statusForNotGapList=statusForNotGapList;   
    }
    public List getStatusForNotGapList(){
        return this.statusForNotGapList;
    }
        
    public String getJsp() {
        return AppConst.JSP_LOC + "/components/employeeGapReportPage.jsp";
    }

    public void setupChildren() {
        // children.add(employeeInfo);
    }
    public String getUser() {
		return this.user;
	}
    public void setUser(String user){
		//return this.user;
         this.user = user;
	}
} 
