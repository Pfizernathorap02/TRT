package com.pfizer.webapp.wc.components; 



import com.pfizer.db.Employee;

import com.pfizer.webapp.AppConst;

import com.pfizer.webapp.user.User;

import com.tgix.wc.WebComponent;



public class SelectEmployeeWc extends WebComponent { 

	private Employee[] employees;

		

	public SelectEmployeeWc(Employee[] employees) {

		this.employees = employees;

	}

	

	public Employee[] getList() {

		return employees;

	}

	

    public String getJsp(){

		return AppConst.JSP_LOC + "/components/selectEmployee.jsp";

	}

	public void setupChildren() {}

} 

