package com.pfizer.webapp.wc.components.report; 

import com.pfizer.db.Employee;
import com.pfizer.db.VarianceReportBean;
import com.pfizer.webapp.AppConst;
import com.pfizer.webapp.AppQueryStrings;
import com.pfizer.webapp.user.User;
import com.pfizer.webapp.user.UserFilter;
import com.tgix.wc.WebComponent;

public class VarianceReportListWc extends WebComponent {
    private VarianceReportBean[] varianceReportBean;
    private AppQueryStrings qStrings;
    private User user;
    public static final int LAYOUT_EXCEL	= 2;
    private int layout = 1;
    private String event;
  
    
    public VarianceReportListWc(UserFilter filter, VarianceReportBean[] varianceReportBean, User user, String event) {
        qStrings = filter.getQuseryStrings();
        this.user=user;
        setVarianceReportBean(varianceReportBean);
        this.setEvent(event);
    }  

    public void setVarianceReportBean(VarianceReportBean[] varianceReportBean){
        this.varianceReportBean =new VarianceReportBean[varianceReportBean.length];
        for(int i=0;i<varianceReportBean.length;i++){
            this.varianceReportBean[i]=varianceReportBean[i];
        }
    }
    
    public VarianceReportBean[] getEmployeeBean(){
        return this.varianceReportBean;
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
        if (AppConst.EVENT_PDF.equalsIgnoreCase(event)) {
            return AppConst.JSP_LOC + "/components/report/PDFHSVarianceReportList.jsp";		
        }
        if (AppConst.EVENT_SPF.equalsIgnoreCase(event)) {            
            return AppConst.JSP_LOC + "/components/report/SPFHSVarianceReportList.jsp";		
        }
        if (AppConst.EVENT_RBU.equalsIgnoreCase(event)) {            
            return AppConst.JSP_LOC + "/components/report/RBUVarianceReportList.jsp";		
        }
        return AppConst.JSP_LOC + "/components/report/PDFHSVarianceReportList.jsp";	
	}
    
    public void setupChildren() {                
    }
    
} 
