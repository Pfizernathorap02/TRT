package com.pfizer.db;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class RBUUnassignedEmployees {
	private String emplId;
	private String firstName;
	private String lastName;
	private String futureRole;
	private String productDesc;
	private String classId;
	private List tablesForClassId;
	private String isTrainer;
    private String tableId = "";
    
	public String getEmplId() {
		return emplId;
	}
	public void setEmplId(String emplId) {
		this.emplId = emplId;
	}
    public String getTableId() {
		return tableId;
	}
	public void setTableId(String tableId) {
		this.tableId = tableId;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getFutureRole() {
		return futureRole;
	}
	public void setFutureRole(String futureRole) {
		this.futureRole = futureRole;
	}
	public String getProductDesc() {
		return productDesc;
	}
	public void setProductDesc(String productDesc) {
		this.productDesc = productDesc;
	}
	public String getClassId() {
		return classId;
	}
	public void setClassId(String classId) {
		this.classId = classId;
	}
	public List getTablesForClassId() {
		return tablesForClassId;
	}
	public void setTablesForClassId(List tablesForClassId) {
		this.tablesForClassId = tablesForClassId;
	}
	public String getIsTrainer() {
		return isTrainer;
	}
	public void setIsTrainer(String isTrainer) {
		this.isTrainer = isTrainer;
	}

}
