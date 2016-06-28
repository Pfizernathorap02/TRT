package com.pfizer.webapp.wc.components; 

import com.pfizer.webapp.AppConst;
import com.pfizer.webapp.user.User;
import com.tgix.wc.WebComponent;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

public class PreviewHomePageWc extends WebComponent
{ 
    private User user;
	private Hashtable menu;
    private Vector menuHeader;	
    private String busUnit;
    private String salesOrg;
    private String role;
    private List sectionNameList;
    private List trackIdList;
    private List minimizeList;
    
	public PreviewHomePageWc(User user) {
		this.user = user;
	}
	
	public User getUser() {
		return user;
	}
	
    public String getJsp(){
		return AppConst.JSP_LOC + "/components/previewHomePage.jsp";
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
    
    public void setSectionNameList(List sectionNameList){
        this.sectionNameList = sectionNameList;
    }
    public List getSectionNameList(){
        return this.sectionNameList;
    }
    
    public void setTrackIdList(List trackIdList){
        this.trackIdList = trackIdList;
    }
    public List getTrackIdList(){
        return this.trackIdList;
    }
    
    public void setMinimizeList(List minimizeList){
        this.minimizeList = minimizeList;
    }
    public List getMinimizeList(){
        return this.minimizeList;
    }
} 
