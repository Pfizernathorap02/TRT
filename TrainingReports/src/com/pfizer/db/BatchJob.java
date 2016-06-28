package com.pfizer.db; 

import java.util.Date;

public class BatchJob {
	public static final String STATUS_ACTIVE = "A";
	public static final String STATUS_FORCE = "F";
	public static final String STATUS_RUNNING = "R";
	public static final String STATUS_INACTIVE = "I";
	
	private long batchId;
	private String name;
	private String status;
	private Date lastRun;
	private String comment;
	
	public long getBatchId() {
		return batchId;
	}
	public void setBatchId(long batchId) {
		this.batchId = batchId;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public Date getLastRun() {
		return lastRun;
	}
	public void setLastRun(Date lastRun) {
		this.lastRun = lastRun;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
		sb.append( "       batchId: " + batchId + "\n" );
		sb.append( "       name: " + name + "\n" );
		sb.append( "       status: " + status + "\n" );
		sb.append( "       lastRun: " + lastRun + "\n" );
		sb.append( "       comment: " + comment + "\n" );
		return sb.toString();
	}
	
} 
