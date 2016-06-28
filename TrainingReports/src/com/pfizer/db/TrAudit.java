package com.pfizer.db; 

import com.pfizer.webapp.user.User;
import java.util.Date;

public class TrAudit {
	public static final String ACTION_LOGIN		= "login";
	public static final String ACTION_REPORT	= "report";
	
	private long id;
	private String userId;
	private String action;
	private String pageName = null;
	private String pie = null;
	private String slice = null;
	private String product = null;
	private String area = null;
	private String region = null;
	private String district = null;
	private Date actionDate;
	
	public TrAudit () {
	}
	
	public TrAudit ( User user, String action ) {
		if ( user.isAdmin() ) {
			this.userId = user.getId();
		} else {
			this.userId = user.getEmplid();
		}
		this.action = action;
		this.actionDate = new Date();
	}
	
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public Date getActionDate() {
		return actionDate;
	}
	public void setActionDate(Date actionDate) {
		this.actionDate = actionDate;
	}
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public String getDistrict() {
		return district;
	}
	public void setDistrict(String district) {
		this.district = district;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getPageName() {
		return pageName;
	}
	public void setPageName(String pageName) {
		this.pageName = pageName;
	}
	public String getPie() {
		return pie;
	}
	public void setPie(String pie) {
		this.pie = pie;
	}
	public String getProduct() {
		return product;
	}
	public void setProduct(String product) {
		this.product = product;
	}
	public String getRegion() {
		return region;
	}
	public void setRegion(String region) {
		this.region = region;
	}
	public String getSlice() {
		return slice;
	}
	public void setSlice(String slice) {
		this.slice = slice;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
} 
