package com.pfizer.webapp.wc.components.search; 

import com.pfizer.webapp.AppConst;
import com.pfizer.webapp.search.EmplSearchForm;
import com.tgix.wc.WebComponent;
import java.util.List;

public class EmployeeSearchPDFHSWc extends WebComponent { 
	private SearchFormPDFHSWc searchFormPDFHSWc;
	private SearchResultListPDFHSWc resultWc;
	private EmplSearchForm form;
	
	public EmployeeSearchPDFHSWc(EmplSearchForm form, List results) {
		this.form = form;
		searchFormPDFHSWc = new SearchFormPDFHSWc(form);
		resultWc = new SearchResultListPDFHSWc( results );
	}
	
	public EmplSearchForm getSearchForm() {
		return form;
	}	
	
	public WebComponent getSearchFormPDFHSWc() {
		return searchFormPDFHSWc;
	}	
	public WebComponent getResultWc() {
		return resultWc;
	}
	
    public String getJsp() {
		return AppConst.JSP_LOC + "/components/search/EmployeeSearchPDFHS.jsp";
	}

    public void setupChildren() {
    }
} 
