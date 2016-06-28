package com.pfizer.webapp.wc.components; 

import com.pfizer.db.Employee;
import com.pfizer.webapp.AppConst;
import com.pfizer.webapp.user.User;
import com.tgix.wc.WebComponent;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Vector;

public class EditMenuWc extends WebComponent { 
	private User user;
	private Vector menu;
    private String menuName;
    private String rootID;
    private String errorMsg = "";
    
	public EditMenuWc(User user) {
		this.user = user;
	}	
    
    public String getErrorMsg() {
        return errorMsg;
    }
    public void setErrorMsg( String str ) {
        this.errorMsg = str;
    }
    	
    public User getUser() {
		return user;
	}    
    public String getJsp(){
		return AppConst.JSP_LOC + "/components/admin/editMenu.jsp";
	}  
	public void setupChildren() {}
    
    public Vector getMenu() {
		return menu; 
	}

	public void setMenu(Vector menu) {
		this.menu = menu;
	}		
	public String getMenuName() {
		return menuName;
	}
	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}
	public String getRootID() {
		return rootID;
	}
	public void setRootID(String rootID) {
		this.rootID = rootID;
	}    
} 
