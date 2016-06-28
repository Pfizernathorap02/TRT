package com.pfizer.webapp.wc.components; 

import com.pfizer.db.Employee;
import com.pfizer.webapp.AppConst;
import com.pfizer.webapp.user.User;
import com.tgix.wc.WebComponent;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Vector;

public class ReportSelectWc extends WebComponent { 
	private User user;
	private Hashtable menu;
    private Vector menuHeader;	
    private String busUnit;
    private String salesOrg;
    private String role;
    	
	public ReportSelectWc(User user) {
		this.user = user;
	}
	
	public User getUser() {
		return user;
	}
	
    public String getJsp(){
		return AppConst.JSP_LOC + "/components/reportSelect.jsp";
	}  
	public void setupChildren() {}
    
    public Hashtable getMenu() {
		return menu; 
	}

	public void setMenu(Hashtable menu) {
		this.menu = menu;
	}		
    public Vector getMenuHeader() {
		return menuHeader;
	} 
	public void setMenuHeader(Vector keys) {
		this.menuHeader = keys; 
	}		
    
    /*Added for RBU */
      public String getBusUnit() {
		return busUnit;
	} 
	public void setBusUnit(String busUnit ) {
		this.busUnit = busUnit; 
	}		
      public String getSalesOrg() {
		return salesOrg;
	} 
	public void setSalesOrg(String salesOrg) {
		this.salesOrg = salesOrg; 
	}		
      public String getRole() {
		return role;
	} 
	public void setRole(String role) {
		this.role = role; 
	}		
    /* End of RBU changes */			
    		
    
} 
