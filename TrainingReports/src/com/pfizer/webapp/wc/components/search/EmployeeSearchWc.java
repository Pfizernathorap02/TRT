package com.pfizer.webapp.wc.components.search; 

import com.pfizer.webapp.AppConst;
import com.pfizer.webapp.search.EmplSearchForm;
import com.tgix.wc.WebComponent;
import java.util.List;

public class EmployeeSearchWc extends WebComponent { 
	private SearchFormWc searchFormWc;
	private SearchResultListWc resultWc;
	private EmplSearchForm form;
	
	public EmployeeSearchWc(EmplSearchForm form, List results) {
		this.form = form;
		searchFormWc = new SearchFormWc(form);
		resultWc = new SearchResultListWc( results );
	}
	
    public void setSearchForm(SearchFormWc wc) {
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
		return AppConst.JSP_LOC + "/components/search/EmployeeSearch.jsp";
	}

    public void setupChildren() {
    }
} 
