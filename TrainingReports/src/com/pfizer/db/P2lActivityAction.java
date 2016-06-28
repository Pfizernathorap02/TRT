package com.pfizer.db; 

import java.util.Date;

public class P2lActivityAction {
     private int id;
     private String activity_id;
     private String emplid;
     private String byEmplId;
     private Date subDate;
     private String actiontype;
     private String guid;
     
	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}
	public String getGuid() {
		return guid;
	}


	public void setGuid(String id) {
		this.guid = id;
	}


	public String getActivity_id() {
		return activity_id;
	}


	public void setActivity_id(String activity_id) {
		this.activity_id = activity_id;
	}


	public String getEmplid() {
		return emplid;
	}


	public void setEmplid(String emplid) {
		this.emplid = emplid;
	}


	public String getByEmplId() {
		return byEmplId;
	}


	public void setByEmplId(String byEmplId) {
		this.byEmplId = byEmplId;
	}


	public Date getSubDate() {
		return subDate;
	}


	public void setSubDate(Date subDate) {
		this.subDate = subDate;
	}


	public String getActiontype() {
		return actiontype;
	}


	public void setActiontype(String actiontype) {
		this.actiontype = actiontype;
	}     
} 
