package com.pfizer.webapp.wc.components.report.phasereports; 

import com.pfizer.webapp.AppConst;
import com.pfizer.webapp.user.UserSession;
import com.tgix.wc.WebComponent;
import java.util.List;

public class SpecialCodesWc extends WebComponent {
    private List results;
    private UserSession uSession;
    private String emplid;
    private String eventType;
    private String activityId;
    private String activityName;
    
	public SpecialCodesWc(List results, UserSession uSession, 
            String emplid, String type, String activityId, String aname) {
		super();
        this.results = results;
        this.uSession = uSession;
        this.emplid = emplid;
        this.eventType = type;
        this.activityId = activityId;
        this.activityName = aname;
	}
    public String getActivityid() {
        return activityId;
    }
    public String getActivityName() {
        return activityName;
    }
    public UserSession getUserSession() {
        return uSession;
    }
    public String getType() {
        return eventType;
    }
    public List getResults() {
        return results;
    }
    public String getEmplid() {
        return this.emplid;
    }
    public String getJsp() {
		return AppConst.JSP_LOC + "/components/report/phasereports/SpecialCodes.jsp";
	}
	public void setupChildren() {} 
} 
