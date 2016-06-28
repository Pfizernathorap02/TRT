package com.pfizer.webapp.wc.components.report; 

import com.pfizer.db.Employee;
import com.pfizer.db.RBUSearchBean;
import com.pfizer.db.VarianceReportBean;
import com.pfizer.webapp.AppConst;
import com.pfizer.webapp.AppQueryStrings;
import com.pfizer.webapp.user.User;
import com.pfizer.webapp.user.UserFilter;
import com.tgix.wc.WebComponent;

public class RBUResultsListWc extends WebComponent {
    private RBUSearchBean[] searchBean;
    private AppQueryStrings qStrings;
    private User user;
    public static final int LAYOUT_EXCEL	= 2;
    private int layout = 1;
    private String event;
  
    
    public RBUResultsListWc(UserFilter filter, RBUSearchBean[] reportBean, User user, String event) {
        qStrings = filter.getQuseryStrings();
        this.user=user;
        setRBUSearchBean(reportBean);
        this.setEvent(event);
    }  

    public void setRBUSearchBean(RBUSearchBean[] reportBean){
        this.searchBean =new RBUSearchBean[reportBean.length];
        for(int i=0;i<reportBean.length;i++){
            this.searchBean[i]=reportBean[i];
        }
    }
    
    public RBUSearchBean[] getEmployeeBean(){
        return this.searchBean;
    }
    
    public AppQueryStrings getQueryStrings() {
		return qStrings;
	}
    
    public User getUser(){
        return this.user;
    }
	
    public void setLayout( int layout) {
		this.layout = layout;
	}
    
    public void setEvent( String event) {
		this.event = event;
	}
    
    public String getEvent() {
		return this.event;
	}
    
    public String getJsp() {         

        if (AppConst.EVENT_RBU.equalsIgnoreCase(event)) {            
            return AppConst.JSP_LOC + "/components/report/RBUSearchResultsList.jsp";		
        }
        return AppConst.JSP_LOC + "/components/report/RBUSearchResultsList.jsp";	
	}
    
    public void setupChildren() {                
    }
    
} 
