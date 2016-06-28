package com.pfizer.webapp.wc.components.report.phasereports; 

import com.pfizer.webapp.AppConst;
import com.pfizer.webapp.user.User;
// Added for TRT Major Enhancement 3.6(Employee grid user view)
import com.pfizer.webapp.user.UserSession;
// End for TRT Major Enhancement
import com.tgix.wc.WebComponent;
import java.util.Collection;
//Added for TRT major enhancement
import java.util.List;
//End for TRT Major enhancement

public class MainReportListReportAreaHQWc extends WebComponent {
    private WebComponent massEmail;
    private Collection result;
    private User currentUser;
    private String activityPk;
    private int layout = 0;
    private String section = "";
    private String emailSubject = "Phased Training Follow-up";
// Added for TRT enhancement 3.6 (Employee grid user view)
    private List selOptHQFields;
    private UserSession session;
// end of modification
    public static final int LAYOUT_XLS = 1;
    
	public MainReportListReportAreaHQWc(Collection result, User usr, String activityPk, String section) {
		super();
        this.result = result;
        this.currentUser = usr;
        this.activityPk = activityPk;
        this.section=section;
	}
    // Added for TRT enhancement 3.6 (Employee grid user view)
	public MainReportListReportAreaHQWc(Collection result, User usr, String activityPk, String section,List optFields ) {
		super();
        this.result = result;
        this.currentUser = usr;
        this.activityPk = activityPk;
        this.section=section;
        this.selOptHQFields=optFields;
	}
    
    public List getSelOptHQFields()
    {
        return this.selOptHQFields;
    }
    // end of modification
    public void setMassEmailWc( WebComponent email ) {
        this.massEmail = email;
    }
    public WebComponent getMassEmail() {
        return this.massEmail;
    }
    public String getEmailSubject() {
        return emailSubject;
    }
    public void setEmailSubject( String str ){
        this.emailSubject = str;
    }
    public String getSection() {
        return this.section;
    }
    public User getCurrentUser() {
        return currentUser;
    }
    public String getActivityPk() {
        return activityPk;
    }
    public void setLayout(int layout) {
        this.layout=layout;
    }
    public Collection getResult() {
        return result;
    }
    public String getJsp() {
        if ( layout == LAYOUT_XLS ) {
            return AppConst.JSP_LOC + "/components/report/phasereports/MainReportListReportAreaHQXls.jsp";
        }
        return AppConst.JSP_LOC + "/components/report/phasereports/MainReportListReportAreaHQ.jsp";
	}
	public void setupChildren() {
       
    } 
// Added for TRT major enhancement CSO impact
     public void setSession(UserSession session) {
        this.session=session;
    }
    public UserSession getSession() {
        return session;
    }
//End of addtion
} 
