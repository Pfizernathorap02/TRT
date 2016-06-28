package com.pfizer.webapp.wc.components; 

import com.pfizer.webapp.AppConst;
import com.tgix.wc.WebComponent;

public class ProductMappingsWC extends WebComponent { 
			

    public String getJsp() {
		return AppConst.JSP_LOC + "/components/user/editUser.jsp";
	}

    public void setupChildren() {
    }
} 
