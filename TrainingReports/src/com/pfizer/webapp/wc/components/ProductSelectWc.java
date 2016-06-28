package com.pfizer.webapp.wc.components; 



import com.pfizer.db.Employee;

import com.pfizer.webapp.AppConst;

import com.pfizer.webapp.user.User;

import com.tgix.wc.WebComponent;



public class ProductSelectWc extends WebComponent { 

	private User user;

		

	public ProductSelectWc( User user) {

		this.user = user;

	}

	

	public User getUser() {

		return user;

	}

	

    public String getJsp(){

		return AppConst.JSP_LOC + "/components/productSelect.jsp";

	}

	public void setupChildren() {}

} 

