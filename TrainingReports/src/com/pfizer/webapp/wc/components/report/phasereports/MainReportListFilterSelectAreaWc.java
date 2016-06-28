package com.pfizer.webapp.wc.components.report.phasereports; 

import com.pfizer.db.P2lTrackPhase;
import com.pfizer.webapp.AppConst;
import com.pfizer.webapp.user.TerritoryFilterForm;
import com.pfizer.webapp.user.User;
import com.pfizer.webapp.user.UserFilter;
import com.pfizer.webapp.wc.components.shared.ReportFilterSelectWc;
import com.pfizer.webapp.wc.components.shared.TerritorySelectWc;
import com.tgix.printing.LoggerHelper;
import com.tgix.wc.WebComponent;

public class MainReportListFilterSelectAreaWc extends WebComponent {
    private TerritoryFilterForm userFilterForm;
    private ReportFilterSelectWc reportFilterSelect;
    
    private User user;
    private P2lTrackPhase phase;
    
	public MainReportListFilterSelectAreaWc(User user, UserFilter filter, P2lTrackPhase phase) {
        
         
	super();
    this.userFilterForm = filter.getFilterForm() ;
    this.user=user;
    this.phase = phase;
    LoggerHelper.logSystemDebug("inside MainReportListFilterSelectAreaWc");
    /*Infosys - Weblogic to Jboss Migrations changes start here*/
	 //reportFilterSelect = new ReportFilterSelectWc(userFilterForm, user.getUserTerritory(), "listreport.do?activitypk=" + filter.getQuseryStrings().getActivitypk(),phase );
    reportFilterSelect = new ReportFilterSelectWc(userFilterForm, user.getUserTerritory(), "listreport?activitypk=" + filter.getQuseryStrings().getActivitypk(),phase );
    /*Infosys - Weblogic to Jboss Migrations changes end here*/
	 reportFilterSelect.setShowTeam(true);
     /////added for TRT phase 2 Hq user/////
     reportFilterSelect.setUser(this.user);
     /////end phase 2/////////////
        if(filter.getLayoutNew().equals("4"))
        { 
            LoggerHelper.logSystemDebug("inside layout_p2lnew");
        	reportFilterSelect.setLayout( TerritorySelectWc.LAYOUT_p2lnew );
        }
        else{
            LoggerHelper.logSystemDebug("inside layout_p2l");
        	reportFilterSelect.setLayout( TerritorySelectWc.LAYOUT_p2l );
        }
	 javascriptFiles.add(AppConst.JAVASCRIPT_LOC + "tgixDynamicSelect.js");        
	}
    
    public WebComponent getSelect() {
        return reportFilterSelect;
    }
    public TerritoryFilterForm getForm() {
        return userFilterForm;
    }
    
    public User getUser() {
        return user;
    }
    
    public String getJsp() {
		return AppConst.JSP_LOC + "/components/report/phasereports/MainReportListFilterSelectArea.jsp";
	}
	public void setupChildren() {
        children.add(reportFilterSelect);
    } 


} 
