package com.pfizer.db; 

import java.util.ArrayList;

public class PhaseEvaluation {
	public static final String FIELD_ROLECD 	= "roleCd";
	public static final String FIELD_ROLEDESC	= "roleDesc";
	public static final String FIELD_SAVE       = "save";
	public static final String FIELD_SUBMIT		= "submit";
    public static final String FIELD_REPORTTYPE = "reportType";	
    
    private String roleCd;
	private String roleDesc;
	private String save;
	private String submit;
    private String reportType;

	
	public PhaseEvaluation() {}
    
    public String getRoleCd() {
		return roleCd;
	}

	public void setRoleCd(String roleCd) {
		this.roleCd = roleCd;
	}

	public String getRoleDesc() {
		return roleDesc;
	}

	public void setRoleDesc(String roleDesc) {
		this.roleDesc = roleDesc;
	}

	public String getSave() {
		return save;
	}

	public void setSave(String save) {
		this.save = save;
	}
    
    public String getSubmit() {
		return submit;
	}

	public void setSubmit(String submit) {
		this.submit = submit;
	}
    
    public String getReportType() {
		return reportType;
	}

	public void setReportType(String reportType) {
		this.reportType = reportType;
	}
    
} 
