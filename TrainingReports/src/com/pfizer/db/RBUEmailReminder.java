package com.pfizer.db;

import java.util.Date;

public class RBUEmailReminder {
	public static final String STATUS_ACTIVE = "On";
	public static final String STATUS_INACTIVE = "Off";
    public static final String STATUS_RUN = "Run";

	private long jobId;
	private String name;
	private String status;
	private double days;
    private Date startDate;
    private String subject;

	public long getJobId() {
		return jobId;
	}
	public void setJobId(long jobId) {
		this.jobId = jobId;
	}
    public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
    
	public double getDays() {
		return days;
	}
	public void setDays(double days) {
		this.days = days;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
    
    public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
    
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append( "\n" + this.getClass().getName() + "\n" );
		sb.append( "       batchId: " + jobId + "\n" );
		sb.append( "       name: " + name + "\n" );
		sb.append( "       status: " + status + "\n" );
		return sb.toString();
	}

}
