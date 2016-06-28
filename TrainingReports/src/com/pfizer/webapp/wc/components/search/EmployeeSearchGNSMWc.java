package com.pfizer.webapp.wc.components.search; 

import com.pfizer.webapp.AppConst;
import com.pfizer.webapp.search.EmplSearchForm;
import com.tgix.wc.WebComponent;
import java.util.List;

public class EmployeeSearchGNSMWc extends WebComponent { 
	private SearchFormGNSMWc searchFormGNSMWc;
	private SearchResultListGNSMWc resultWc;
	private EmplSearchForm form;
	
	public EmployeeSearchGNSMWc(EmplSearchForm form, List results) {
		this.form = form;
		searchFormGNSMWc = new SearchFormGNSMWc(form);
		resultWc = new SearchResultListGNSMWc( results );
	}
	
	public EmplSearchForm getSearchForm() {
		return form;
	}	
	
	public WebComponent getSearchFormGNSMWc() {
		return searchFormGNSMWc;
	}	
	public WebComponent getResultWc() {
		return resultWc;
	}
	
    public String getJsp() {
		return AppConst.JSP_LOC + "/components/search/EmployeeSearchGNSM.jsp";
	}

    public void setupChildren() {
    }
} 
