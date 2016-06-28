package com.pfizer.webapp.wc.components.report.phasereports; 

import com.pfizer.webapp.AppConst;
import com.pfizer.webapp.user.UserSession;
import com.tgix.wc.WebComponent;
import java.util.List;

public class SceCodesWc  extends WebComponent {
    private UserSession uSession;
    private String emplid;
   
    
	public SceCodesWc(UserSession uSession,String emplid) {
		super();
        
        this.uSession = uSession;
        this.emplid = emplid;
	}
   
    public UserSession getUserSession() {
        return uSession;
    }
   
    public String getEmplid() {
        return this.emplid;
    }
    public String getJsp() {
		return AppConst.JSP_LOC + "/components/report/phasereports/SceCodes.jsp";
	}
	public void setupChildren() {} 
} 
