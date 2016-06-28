package com.pfizer.webapp.wc.components.search; 

import com.pfizer.webapp.AppConst;
import com.pfizer.webapp.search.EmplSearchForm;
import com.tgix.wc.WebComponent;
import java.util.List;

public class EmployeeSearchVRSWc extends WebComponent { 
	private SearchFormVRSWc searchFormVRSWc;
	private SearchResultListVRSWc resultWc;
	private EmplSearchForm form;
	
	public EmployeeSearchVRSWc(EmplSearchForm form, List results) {
		this.form = form;
		searchFormVRSWc = new SearchFormVRSWc(form);
		resultWc = new SearchResultListVRSWc( results );
	}
	
	public EmplSearchForm getSearchForm() {
		return form;
	}	
	
	public WebComponent getSearchFormVRSWc() {
		return searchFormVRSWc;
	}	
	public WebComponent getResultWc() {
		return resultWc;
	}
	
    public String getJsp() {
		return AppConst.JSP_LOC + "/components/search/EmployeeSearchVRS.jsp";
	}

    public void setupChildren() {
    }
} 
