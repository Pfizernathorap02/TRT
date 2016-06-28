package com.pfizer.webapp.wc.global; 

import com.pfizer.webapp.AppConst;
import com.tgix.Utils.Util;
import com.tgix.wc.WebComponent;

public class StaticJspWc extends WebComponent {
    private String jsp = "";
    
	public StaticJspWc(String page) {
		super();
        this.jsp = page;
	}
    public String getJsp() {
        if ( !Util.isEmpty(jsp)) {
            return jsp;
        } else {
           return AppConst.JSP_LOC + "/global/emptypage.jsp";
        }
	}
	public void setupChildren() {} 
} 
