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

public class searchResultWc extends WebComponent 
{ 
  
    private NASearchFormWc searchFormWc;
	private NASearchResultListWc resultWc;
	private NonAtlasEmployeeSearchForm form;
	
	public searchResultWc(NonAtlasEmployeeSearchForm form, List results) {
		this.form = form;
		searchFormWc = new NASearchFormWc(form);
		resultWc = new NASearchResultListWc( results );
	}
	
    public void setSearchForm(NASearchFormWc wc) {
        this.searchFormWc = wc;
    }
	public NonAtlasEmployeeSearchForm getSearchForm() {
		return form;
	}	
	
	public WebComponent getSearchFormWc() {
		return searchFormWc;
	}	
	public WebComponent getResultWc() {
		return resultWc;
	}
	
    public String getJsp() {
		return AppConst.JSP_LOC + "/components/search/NAUserSearch.jsp";
	}

    public void setupChildren() {
    }

    
} 


