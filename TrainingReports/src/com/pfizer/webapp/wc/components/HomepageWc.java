package com.pfizer.webapp.wc.components; 

import com.pfizer.db.Employee;
import com.pfizer.webapp.AppConst;
import com.pfizer.webapp.user.User;
import com.tgix.wc.WebComponent;

public class HomepageWc extends WebComponent { 
	private Employee[] vpList;
	private Employee[] svpList;
	private Employee[] rmList;
	private Employee[] dmList;
	private Employee[] rcList;
		
	public HomepageWc( Employee[] svpList, Employee[] vpList, Employee[] rmList, Employee[] dmList, Employee[] rcList ) {
		this.svpList = svpList;
		this.vpList = vpList;
		this.rmList = rmList;
		this.dmList = dmList;
        this.rcList = rcList;
	}
	
    
	public WebComponent getRcComponent() {
		return new SelectEmployeeWc( rcList );
	}
	public WebComponent getVpComponent() {
		return new SelectEmployeeWc( vpList );
	}
	public WebComponent getSvpComponent() {
		return new SelectEmployeeWc( svpList );
	}
	public WebComponent getRmComponent() {
		return new SelectEmployeeWc( rmList );
	}
	public WebComponent getDmComponent() {
		return new SelectEmployeeWc( dmList );
	}
	
    public String getJsp(){
		return AppConst.JSP_LOC + "/components/homepage.jsp";
	}
	public void setupChildren() {}
} 
