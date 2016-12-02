package com.pfizer.db;

public class AccessApproversMembers 
{
	public static final String FIELD_ID	= "id";
	public static final String FIELD_EMAILID  = "emailId";
	public static final String FIELD_APPROVERTYPE		= "approverType";
	
	
	
	private Integer id;
	private String emailId;
	private String approverType;
	
	public static final String BUSINESS_OWNER_1 = "BUSINESS_OWNER_1";
	public  static final String BUSINESS_OWNER_2 = "BUSINESS_OWNER_2";
	public static final  String APP_OWNER= "APP_OWNER";
	
	
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getEmailId() {
		return emailId;
	}
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}
	public String getApproverType() {
		return approverType;
	}
	public void setApproverType(String approverType) {
		this.approverType = approverType;
	}
	
	
	
}
