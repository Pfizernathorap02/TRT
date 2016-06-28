package com.pfizer.webapp.wc.components.search; 

import com.pfizer.db.DelegateBean;
import com.pfizer.db.DelegatedEmp;
import com.pfizer.db.EmpSearch;
import com.pfizer.webapp.AppConst;
import com.tgix.Utils.LoggerHelper;
import com.tgix.wc.WebComponent;
import java.util.List;


public class DelegateAccessWc extends WebComponent { 
	//private EmplSearchForm form;
	private String postUrl = "/TrainingReports/search" ; // default value for backward compaitibility
    private String target = "";
    private String onSubmit = "";
    DelegatedEmp[] arr;
    
    private DelegateBean fromBean = null;
    private DelegateBean toBean = null;
    private String errorMsg = "";
    boolean ListEmpty = false;
    
	public DelegateAccessWc(DelegatedEmp[] arr) {
		this.arr = arr;
        
	}
    public DelegateAccessWc() {
		
	}
    
     public DelegatedEmp[] getList() {
        return arr;
    }
	
    public boolean getListEmpty() {
        return this.ListEmpty;
    }
        
    public void setListEmpty(boolean ListEmpty) {
        this.ListEmpty = ListEmpty;
    }
    
    public DelegateBean getFromBean() {
        return fromBean;
    }
        
    public void setFromBean(DelegateBean v) {
        this.fromBean = v;
    }
	
    public DelegateBean getToBean() {
        return toBean;
    }
        
    public void setToBean(DelegateBean v) {
        this.toBean = v;
    }
            
    public String getTarget() {
        return target;
    }
    public String getOnSubmit() {
        return onSubmit;
    }
    public void setOnSubmit( String tmp ) {
        this.onSubmit = tmp;
    }
    
    public void setTarget( String target) {
        this.target=target;
    }
    public void setPostUrl( String url ) {
        this.postUrl=url;
    }
    public String getPostUrl() {
        return postUrl;
    }
     public String getErrorMsg() {
        return errorMsg;
    }
    public void setErrorMsg( String str ) {
        this.errorMsg = str;
    }
	//public EmplSearchForm getSearchForm() {
		//return form;
	//}	
	
		
    public String getJsp() {
		return AppConst.JSP_LOC + "/components/search/DelegateAccess.jsp";
	}

    public void setupChildren() {
    }
} 
