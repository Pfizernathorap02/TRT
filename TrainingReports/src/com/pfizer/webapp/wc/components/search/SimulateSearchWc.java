package com.pfizer.webapp.wc.components.search; 

import com.pfizer.webapp.AppConst;
import com.pfizer.webapp.search.EmplSearchForm;
import com.tgix.wc.WebComponent;
import java.util.List;

public class SimulateSearchWc extends WebComponent { 
	private SimulateSearchFormWc searchFormWc;
	private SimulateSearchResultListWc resultWc;
	private EmplSearchForm form;
	
	public SimulateSearchWc(EmplSearchForm form, List results) {
		this.form = form;
		searchFormWc = new SimulateSearchFormWc(form);
		resultWc = new SimulateSearchResultListWc( results );
	}
	
    public void setSearchForm(SimulateSearchFormWc wc) {
        this.searchFormWc = wc;
    }
	public EmplSearchForm getSearchForm() {
		return form;
	}	
	
	public WebComponent getSearchFormWc() {
		return searchFormWc;
	}	
	public WebComponent getResultWc() {
		return resultWc;
	}
	
    public String getJsp() {
		return AppConst.JSP_LOC + "/components/search/SimulateUserSearch.jsp";
	}

    public void setupChildren() {
    }
} 
