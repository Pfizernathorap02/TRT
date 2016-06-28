package com.pfizer.db; 



import java.util.Date;



public class Attendance {

	private String status;

	private String emplid;

	private String courseDesc;

	private Date startDate;

	

	public Attendance(){}

	

	public void setStatus(String status) {

		this.status = status;

	} 

	public String getStatus() {

		return status;

	}

	public void setCourseDesc( String desc ) {

		this.courseDesc = desc;

	}

	public String getCourseDesc() {

		return this.courseDesc;

	}

	public Date getStartDate() {

		return startDate;

	}

	public void setStartDate( Date start ) {

		this.startDate = start;

	}

	

	public String getEmplid() {

		return this.emplid;

	}

	public void setEmplid(String id) {

		this.emplid = id;

	}

	

	public String toString() {

		StringBuffer sb = new StringBuffer();

		sb.append( "\n" + this.getClass().getName() + "\n" );

		sb.append( "status: " + status + "\n" );

		sb.append( "emplid: " + emplid + "\n" );

		sb.append( "courseDesc: " + courseDesc + "\n" );

		sb.append( "startDate: " + startDate + "\n" );

		return sb.toString();

	}

} 

