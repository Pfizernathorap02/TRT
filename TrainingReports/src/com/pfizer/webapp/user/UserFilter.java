package com.pfizer.webapp.user; 

import com.pfizer.webapp.AppQueryStrings;
import com.tgix.Utils.Util;

public class UserFilter {
	private String product;
	private String clusterCode;
	
	private TerritoryFilterForm filterForm;
	private AppQueryStrings qString=new AppQueryStrings();
	private String employeeId = new String();
    private String employeeIdForSplRole = "";
	private boolean isAdmin = false;
	private boolean isTsr = false;
    private boolean isSpecialRoleUser = false;
	
    private boolean isRefresh = true;
    private String layout=new String();
    
    //Added for TRT Phase 2 - HQ Users Requirement
    private boolean isHQ = false;
	public UserFilter() {}
	
	public void setProdcut(String product) {
		if ( !Util.isEmpty(product) ) {
			this.product = product;
		}
	}
	public String getProduct() {
		return this.product;
	}
	
	public void setEmployeeId( String id ) {
		this.employeeId = id;
	}
	public String getEmployeeId() {
		return employeeId;
	}//employeeIdForSplRole
    public void setEmployeeIdForSplRole( String id ) {
		this.employeeIdForSplRole = id;
	}
	public String getEmployeeIdForSplRole() {
		return employeeIdForSplRole;
	}
	
	public void setClusterCode( String code ) {
		this.clusterCode = code; 
	}
	public String getClusterCode() {
		return this.clusterCode;
	}
	public void setFilterForm(TerritoryFilterForm form) {
		this.filterForm = form;
	}
	
	public TerritoryFilterForm getFilterForm() {
		return this.filterForm;
	}
	 
	public void setQueryStrings(AppQueryStrings qStrings) {
		this.qString = qStrings;
	}
	public AppQueryStrings getQuseryStrings() {
		return this.qString;
	}
	public boolean isAdmin() {
		return isAdmin;
	}
	public void setAdmin(boolean flag) {
		this.isAdmin = flag;
	}
	public boolean isTsrAdmin() {
		return isTsr;
	}
    public void setTsrAdmin(boolean flag) {
        isTsr = flag;
    }
    
    public boolean isTsrOrAdmin() {
        if (isTsrAdmin()) {
            return true;
        }
        return isAdmin();
    }
    
       /* Added for RBU */
    public boolean isRefresh() {
		return isRefresh;
	}
    public void setRefresh(boolean flag) {
        isRefresh = flag;
    }
    
    public void setLayoutNew( String layout ) {
		this.layout = layout; 
	}
	public String getLayoutNew() {
		return this.layout;
	}
    
    /* End of addition */
    
    // Start: Added for TRT Phase 2 enhancement - HQ Users - Requirement no. F6
    public boolean isHqUser() {
		return isHQ;
	}
    public void setHqUser(boolean flag) {
        isHQ = flag;
    }
    
    public void setIsSpecialRoleUser( boolean flag ) {
		isSpecialRoleUser = flag;
	}
	public boolean isSpecialRoleUser() {
		return isSpecialRoleUser;
	}
    //End
} 
