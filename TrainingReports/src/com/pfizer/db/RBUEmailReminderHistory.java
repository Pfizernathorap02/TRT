package com.pfizer.db;

import java.util.Date;

public class RBUEmailReminderHistory {
	private long jobId;
	private String managerEmplId;
	private Date  runDate;
	private String reportsEmplId ;
    private long historyId;

	public long getJobId() {
		return jobId;
	}
	public void setJobId(long jobId) {
		this.jobId = jobId;
	}
    
    public long getHistoryId() {
		return historyId;
	}
	public void setHistoryId(long historyId) {
		this.historyId = historyId;
	}
	public String getReportsEmplId() {
		return reportsEmplId;
	}
	public void setReportsEmplId(String reportsEmplId) {
		this.reportsEmplId = reportsEmplId;
	}
	public String getManagerEmplId() {
		return managerEmplId;
	}
	public void setManagerEmplId(String managerEmplId) {
		this.managerEmplId = managerEmplId;
	}
	public Date getRunDate() {
		return runDate;
	}
	public void setRunDate(Date runDate) {
		this.runDate = runDate;
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		
		return sb.toString();
	}

}
