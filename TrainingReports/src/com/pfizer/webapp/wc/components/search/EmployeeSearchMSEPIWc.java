package com.pfizer.webapp.wc.components.search; 

import com.pfizer.webapp.AppConst;
import com.pfizer.webapp.search.EmplSearchForm;
import com.tgix.wc.WebComponent;
import java.util.List;

public class EmployeeSearchMSEPIWc extends WebComponent { 
	private SearchFormMSEPIWc searchFormMSEPIWc;
	private SearchResultListMSEPIWc resultWc;
	private EmplSearchForm form;
	
	public EmployeeSearchMSEPIWc(EmplSearchForm form, List results) {
		this.form = form;
		searchFormMSEPIWc = new SearchFormMSEPIWc(form);
		resultWc = new SearchResultListMSEPIWc( results );
	}
	
	public EmplSearchForm getSearchForm() {
		return form;
	}	
	
	public WebComponent getSearchFormMSEPIWc() {
		return searchFormMSEPIWc;
	}	
	public WebComponent getResultWc() {
		return resultWc;
	}
	
    public String getJsp() { 
		return AppConst.JSP_LOC + "/components/search/EmployeeSearchMSEPI.jsp";
	}

    public void setupChildren() {
    }
} 
