package com.pfizer.webapp.report; 

import java.util.Date;

public class AuditForm {
	public static String FIELD_REPORT_TYPE	= "AuditForm_reportType";
	public static String FIELD_START_DATE	= "AuditForm_startDate";
	public static String FIELD_END_DATE		= "AuditForm_endDate";
			
	public String reportType;
	public Date startDate;
	public Date endDate; 
	
	public AuditForm() {}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getReportType() {
		return reportType;
	}

	public void setReportType(String reportType) {
		this.reportType = reportType;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	
	
	
} 
