package com.pfizer.actionForm;

import com.opensymphony.xwork2.ActionSupport;

public class UploadGuestForm extends ActionSupport {
	private String classid;

	public void setClassid(String classid) {
		this.classid = classid;
	}

	public String getClassid() {
		return this.classid;
	}
}