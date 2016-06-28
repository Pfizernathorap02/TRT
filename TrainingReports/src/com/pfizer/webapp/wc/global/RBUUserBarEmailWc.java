package com.pfizer.webapp.wc.global;



import com.pfizer.webapp.AppConst;

import com.pfizer.webapp.user.User;

import com.tgix.wc.WebComponent;



public class RBUUserBarEmailWc extends WebComponent {

	private User user;



	public RBUUserBarEmailWc(User user) {

		this.user = user;

	}



	public User getUser() {

		return this.user;

	}

    public String getJsp() {

		return AppConst.JSP_LOC + "/global/userBarRBUEmail.jsp";

	}

	public void setupChildren() {}

}

