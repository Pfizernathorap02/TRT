package com.pfizer.db; 

public class UserAccess {
	public static final String FIELD_USER_ID	= "userId";
	public static final String FIELD_USER_TYPE	= "userType";
	public static final String FIELD_EMPLID		= "emplid";
	public static final String FIELD_FNAME		= "fname";
	public static final String FIELD_LNAME		= "lname";
	public static final String FIELD_EMAIL		= "email";
	public static final String FIELD_NTID		= "ntId";
	public static final String FIELD_NTDOMAIN	= "ntDomain";
	public static final String FIELD_STATUS		= "status";
	public static final String FIELD_REQUESTEDACCESS= "RequestedAccess";
	public static final String FIELD_ACCESSREQUESTID= "accessRequestId";
	
	public static final String STATUS_ACTIVE = "Active";
	public static final String STATUS_INACTIVE = "Inactive";
	
	private String emplid;
	private String userType;
	private String userId;
	private String ntId;
	private String ntDomain;
	private String email;
	private String fname;
	private String lname;
	private String status;
	
	
	private Boolean RequestedAccess;
	
	private String accessRequestId;
	
	private Employee employee;
	 
	public UserAccess() {}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getEmplid() {
		return emplid;
	}

	public void setEmplid(String emplid) {
		this.emplid = emplid;
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	public String getFname() {
		return fname;
	}

	public void setFname(String fname) {
		this.fname = fname;
	}

	public String getLname() {
		return lname;
	}

	public void setLname(String lname) {
		this.lname = lname;
	}

	public String getNtDomain() {
		return ntDomain;
	}

	public void setNtDomain(String ntDomain) {
		this.ntDomain = ntDomain;
	}

	public String getNtId() {
		return ntId;
	}

	public void setNtId(String ntId) {
		this.ntId = ntId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}
	
	public void setStatus( String status ) {
		this.status = status;
	}
	public String getStatus() {
		return this.status;
	}

	public Boolean getRequestedAccess() {
		return RequestedAccess;
	}

	public void setRequestedAccess(Boolean isRequestedAccess) {
		this.RequestedAccess = isRequestedAccess;
	}

	public String getAccessRequestId() {
		return accessRequestId;
	}

	public void setAccessRequestId(String accessRequestId) {
		this.accessRequestId = accessRequestId;
	}



} 
