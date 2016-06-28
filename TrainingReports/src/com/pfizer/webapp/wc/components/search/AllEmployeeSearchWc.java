package com.pfizer.webapp.wc.components.search; 

import com.pfizer.webapp.AppConst;
import com.pfizer.webapp.search.EmplSearchForm;
import com.tgix.wc.WebComponent;
import java.util.List;

public class AllEmployeeSearchWc extends WebComponent { 
	private AllEmployeeSearchFormWc allEmployeeSearchFormWc;
	private AllEmployeeSearchResultWc resultWc;
	private EmplSearchForm form;
	
	public AllEmployeeSearchWc(EmplSearchForm form, List results) {
		this.form = form;
		allEmployeeSearchFormWc = new AllEmployeeSearchFormWc(form);
		resultWc = new AllEmployeeSearchResultWc( results );
	}
	
    public void setSearchForm(AllEmployeeSearchFormWc wc) {
        this.allEmployeeSearchFormWc = wc;
    }
	public EmplSearchForm getSearchForm() {
		return form;
	}	
	
	public WebComponent getSearchFormWc() {
		return allEmployeeSearchFormWc;
	}	
	public WebComponent getResultWc() {
		return resultWc;
	}
	
    public String getJsp() {
		return AppConst.JSP_LOC + "/components/search/AllEmployeeSearch.jsp";
	}

    public void setupChildren() {
    }
} 

