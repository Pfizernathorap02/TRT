package com.pfizer.webapp.wc.components.search; 

import com.pfizer.db.Attendance;
import com.pfizer.db.Employee;
import com.pfizer.db.PassFail;
import com.pfizer.db.PedagogueExam;
import com.pfizer.db.Product;
import com.pfizer.db.SceFull;
import com.pfizer.processor.OverallProcessor;
import com.pfizer.processor.OverallResult;
import com.pfizer.processor.PassFailProcessor;
import com.pfizer.webapp.AppConst;
import com.pfizer.webapp.AppQueryStrings;
import com.pfizer.webapp.user.TerritoryFilterForm;
import com.pfizer.webapp.user.User;
import com.pfizer.webapp.user.UserFilter;
import com.pfizer.webapp.user.UserTerritory;
import com.pfizer.webapp.wc.components.chart.ChartDetailWc;
import com.pfizer.webapp.wc.components.shared.AreaRegionDistJsWc;
import com.pfizer.webapp.wc.components.shared.TerritorySelectWc;
import com.tgix.Utils.Util;
import com.tgix.wc.WebComponent;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import com.pfizer.webapp.search.NonAtlasEmployeeSearchForm;

public class NASearchFormWc extends WebComponent
{ 
    private NonAtlasEmployeeSearchForm form;
	private String postUrl = "/TrainingReports/admin/NAsearch" ; // default value for backward compaitibility
    private String target = "";
    private String onSubmit = "";
    
	public NASearchFormWc(NonAtlasEmployeeSearchForm form) {
		this.form = form;
	}
	
    public String getTarget() {
        return target;
    }
    public String getOnSubmit() {
        return onSubmit;
    }
    public void setOnSubmit( String tmp ) {
        this.onSubmit = tmp;
    }
    
    public void setTarget( String target) {
        this.target=target;
    }
    public void setPostUrl( String url ) {
        this.postUrl=url;
    }
    public String getPostUrl() {
        return postUrl;
    }
	public NonAtlasEmployeeSearchForm getSearchForm() {
		return form;
	}	
	
		
    public String getJsp() {
		return AppConst.JSP_LOC + "/components/NASearchForm.jsp";
	}

    public void setupChildren() {
    }
} 
