package com.pfizer.webapp.wc.components.search; 

import com.pfizer.webapp.AppConst;
import com.pfizer.webapp.search.EmplSearchForm;
import com.tgix.wc.WebComponent;
import java.util.List;

public class DelegateSearchWc extends WebComponent { 
	private DelegateSearchFormWc searchFormWc;
	private DelegateSearchResultListWc resultWc;
	private EmplSearchForm form;
	
	public DelegateSearchWc(DelegateSearchFormWc searchFormWc, DelegateSearchResultListWc resultWc) {
		
		this.searchFormWc = searchFormWc;	
        this.resultWc = resultWc;
	}
	
    public void setSearchForm(DelegateSearchFormWc wc) {
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
		return AppConst.JSP_LOC + "/components/search/DelegateUserSearch.jsp";
	}

    public void setupChildren() {
    }
} 