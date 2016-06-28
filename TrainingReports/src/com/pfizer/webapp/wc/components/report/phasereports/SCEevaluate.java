package com.pfizer.webapp.wc.components.report.phasereports; 

import com.pfizer.webapp.AppConst;
import com.pfizer.webapp.user.UserSession;
import com.tgix.wc.WebComponent;
import java.util.List;

public class SCEevaluate extends WebComponent {
  
    
	public SCEevaluate() {
	
	}
   
    public String getJsp() {
        System.out.println("JSP FORWARD");
		return AppConst.JSP_LOC + "/components/report/phasereports/SCEEvaluationForm.jsp";
	}
	public void setupChildren() {} 
} 
