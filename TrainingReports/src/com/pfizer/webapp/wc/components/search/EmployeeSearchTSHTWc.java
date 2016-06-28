package com.pfizer.webapp.wc.components.search; 

import com.pfizer.webapp.AppConst;
import com.pfizer.webapp.search.EmplSearchTSHTForm;
import com.tgix.wc.WebComponent;
import java.util.List;

public class EmployeeSearchTSHTWc extends WebComponent { 
	private SearchFormTSHTWc searchFormTSHTWc;
	private SearchResultListTSHTWc resultWc;
	private EmplSearchTSHTForm form;
	
	public EmployeeSearchTSHTWc(EmplSearchTSHTForm form, List results) {
		this.form = form;
		searchFormTSHTWc = new SearchFormTSHTWc(form);
		resultWc = new SearchResultListTSHTWc( results );
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
		return AppConst.JSP_LOC + "/components/search/EmployeeSearchTSHT.jsp";
	}

    public void setupChildren() {
    }
} 
