package com.pfizer.webapp.wc.components.search; 

import com.pfizer.webapp.AppConst;
import com.pfizer.webapp.search.EmplSearchForm;
import com.tgix.wc.WebComponent;
import java.util.List;

public class EmployeeSearchRBUWc extends WebComponent { 
	private SearchFormRBUWc searchFormRBUWc;
	private SearchResultListRBUWc resultWc;
	private EmplSearchForm form;
	
	public EmployeeSearchRBUWc(EmplSearchForm form, List results) {
		this.form = form;
		searchFormRBUWc = new SearchFormRBUWc(form);
		resultWc = new SearchResultListRBUWc( results );
	}
	
	public EmplSearchForm getSearchForm() {
		return form;
	}	
	
	public WebComponent getSearchFormRBUWc() {
		return searchFormRBUWc;
	}	
	public WebComponent getResultWc() {
		return resultWc;
	}
	
    public String getJsp() {
		return AppConst.JSP_LOC + "/components/search/EmployeeSearchRBU.jsp";
	}

    public void setupChildren() {
    }
} 
