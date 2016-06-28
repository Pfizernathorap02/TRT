package com.pfizer.webapp.wc.global; 



import com.pfizer.webapp.AppConst;

import com.tgix.wc.WebComponent;



public class FooterWc extends WebComponent {

	public FooterWc() {

		super();

	}

    public String getJsp() {

		return AppConst.JSP_LOC + "/global/footer.jsp";

	}

	public void setupChildren() {} 

} 

