package com.pfizer.webapp.wc.components.search; 

import com.pfizer.webapp.AppConst;
import com.pfizer.webapp.search.EmplSearchForm;
import com.tgix.wc.WebComponent;
import java.util.List;

public class EmployeeSearchPOAWc extends WebComponent { 
	private SearchFormPOAWc searchFormPOAWc;
	private SearchResultListPOAWc resultWc;
	private EmplSearchForm form;
	
	public EmployeeSearchPOAWc(EmplSearchForm form, List results) {
		this.form = form;
		searchFormPOAWc = new SearchFormPOAWc(form);
		resultWc = new SearchResultListPOAWc( results );
	}
	
	public EmplSearchForm getSearchForm() {
		return form;
	}	
	
	public WebComponent getSearchFormPOAWc() {
		return searchFormPOAWc;
	}	
	public WebComponent getResultWc() {
		return resultWc;
	}
	
    public String getJsp() {
		return AppConst.JSP_LOC + "/components/search/EmployeeSearchPOA.jsp";
	}

    public void setupChildren() {
    }
} 
