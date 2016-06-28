package com.pfizer.webapp.wc.global; 



import com.pfizer.webapp.AppConst;

import com.pfizer.webapp.user.User;

import com.tgix.wc.WebComponent;



public class RBUUserBarWc extends WebComponent {

	private User user;

	

	public RBUUserBarWc(User user) {

		this.user = user;

	}

	

	public User getUser() {

		return this.user;

	}

    public String getJsp() {

		return AppConst.JSP_LOC + "/global/userBarRBU.jsp";

	}

	public void setupChildren() {} 

} 

