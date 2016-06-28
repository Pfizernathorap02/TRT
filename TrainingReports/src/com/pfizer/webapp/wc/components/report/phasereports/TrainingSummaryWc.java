package com.pfizer.webapp.wc.components.report.phasereports; 

import com.pfizer.db.Employee;
import com.pfizer.db.PassFail;
import com.pfizer.db.Product;
import com.pfizer.processor.OverallProcessor;
import com.pfizer.webapp.AppConst;
import com.pfizer.webapp.AppQueryStrings;
import com.pfizer.webapp.user.TerritoryFilterForm;
import com.pfizer.webapp.user.User;
import com.pfizer.webapp.user.UserFilter;
import com.pfizer.webapp.user.UserTerritory;
import com.pfizer.webapp.wc.components.ErrorWc;
import com.pfizer.webapp.wc.components.UnauthorizedWc;
import com.pfizer.webapp.wc.components.chart.ChartDetailWc;
import com.pfizer.webapp.wc.components.shared.AreaRegionDistJsWc;
import com.pfizer.webapp.wc.components.shared.TerritorySelectWc;
import com.tgix.wc.WebComponent;
import java.util.ArrayList;

public class TrainingSummaryWc extends WebComponent { 
    	
	public TrainingSummaryWc() {
        super();
        
    }
    
    public String getJsp() {
        return AppConst.JSP_LOC + "/components/report/phasereports/TrainingSummary.jsp";
    }

    public void setupChildren() {
    }
} 
