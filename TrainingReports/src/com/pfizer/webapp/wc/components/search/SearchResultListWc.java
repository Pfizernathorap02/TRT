package com.pfizer.webapp.wc.components.search; 

import java.util.List;

import com.pfizer.webapp.AppConst;
import com.tgix.wc.WebComponent;

public class SearchResultListWc extends WebComponent { 
	private List results;
	
	public SearchResultListWc( List results ) {
		this.results = results;
	}
	
	public List getResults() {
		return results;
	}	
    public String getJsp() {
		return AppConst.JSP_LOC + "/components/search/SearchReportList.jsp";
	}

    public void setupChildren() {
    }
} 
