package com.pfizer.webapp.wc.components.search; 

import com.pfizer.webapp.AppConst;
import com.pfizer.webapp.search.EmplSearchTSHTForm;
import com.tgix.wc.WebComponent;
import java.util.List;

public class EmployeeSearchTSHTDetailWc extends WebComponent { 
	private SearchFormTSHTWc searchFormTSHTWc;
	private SearchResultListTSHTDetailWc resultWc;
	private EmplSearchTSHTForm form;
	
	public EmployeeSearchTSHTDetailWc(EmplSearchTSHTForm form, List results) {
		this.form = form;
		searchFormTSHTWc = new SearchFormTSHTWc(form);
		resultWc = new SearchResultListTSHTDetailWc( results );
	}
	
	public EmplSearchTSHTForm getSearchForm() {
		return form;
	}	
	
	public WebComponent getSearchFormTSHTWc() {
		return searchFormTSHTWc;
	}	
	public WebComponent getResultWc() {
		return resultWc;
	}
	
    public String getJsp() {
		return AppConst.JSP_LOC + "/components/search/EmployeeSearchTSHTDetail.jsp";
	}

    public void setupChildren() {
    }
} 
