package com.pfizer.webapp.wc.components.report.phasereports; 

import com.pfizer.webapp.AppConst;
import com.pfizer.webapp.user.TerritoryFilterForm;
import com.pfizer.webapp.user.User;
import com.pfizer.webapp.user.UserFilter;
import com.pfizer.webapp.wc.components.shared.TerritorySelectWc;
import com.tgix.wc.WebComponent;

public class MainReportListSelectAreaWc extends WebComponent {
    private TerritoryFilterForm userFilterForm;
    private TerritorySelectWc territorySelect;
    
    private User user;
    
	public MainReportListSelectAreaWc(User user, UserFilter filter) {
		super();
        this.userFilterForm = filter.getFilterForm() ;
        this.user=user;
        /*Infosys - Weblogic to Jboss Migrations changes start here*/
		//territorySelect = new TerritorySelectWc( userFilterForm, user.getUserTerritory(), "listreport.do?section=" + filter.getQuseryStrings().getSection() + "&activitypk=" + filter.getQuseryStrings().getActivitypk() );
        territorySelect = new TerritorySelectWc( userFilterForm, user.getUserTerritory(), "listreport?section=" + filter.getQuseryStrings().getSection() + "&activitypk=" + filter.getQuseryStrings().getActivitypk() );
        /*Infosys - Weblogic to Jboss Migrations changes end here*/
		territorySelect.setShowTeam(true);
        if(filter.getLayoutNew().equals("4"))
        {
        territorySelect.setLayout( TerritorySelectWc.LAYOUT_p2lnew );
        }
        else{
        territorySelect.setLayout( TerritorySelectWc.LAYOUT_p2l );
        }
		javascriptFiles.add(AppConst.JAVASCRIPT_LOC + "tgixDynamicSelect.js");        
	}
    
    public WebComponent getSelect() {
        return territorySelect;
    }
    public TerritoryFilterForm getForm() {
        return userFilterForm;
    }
    
    public User getUser() {
        return user;
    }
    
    public String getJsp() {
		return AppConst.JSP_LOC + "/components/report/phasereports/MainReportListSelectArea.jsp";
	}
	public void setupChildren() {
        children.add(territorySelect);
    } 
} 
