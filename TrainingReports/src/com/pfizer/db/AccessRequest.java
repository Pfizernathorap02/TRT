/**
 * 
 */
package com.pfizer.db;

import java.util.Date;

/**
 * @author MEHRAA04
 *
 */
public class AccessRequest 
{
	private Integer id;
	private String  firstName;
	private String  lastName;
	private String  eamilID;
	private String  ntid;
	private String  ntidDomain;
	private String  employeeId;
	private String  pfizerEmployee;
	private String requestStatus;
	private String comments;
	private String Approvers_comments;
	
	private Date  date_submitted;
	private Date  date_action;
	
	public static final String SUBMITTED ="Submitted";
	public static final String APPROVED ="Approved";
	public static final String REJECTED ="Rejected";
	
	
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getEamilID() {
		return eamilID;
	}
	public void setEamilID(String eamilID) {
		this.eamilID = eamilID;
	}
	public String getNtid() {
		return ntid;
	}
	public void setNtid(String ntid) {
		this.ntid = ntid;
	}
	public String getNtidDomain() {
		return ntidDomain;
	}
	public void setNtidDomain(String ntidDomain) {
		this.ntidDomain = ntidDomain;
	}
	public String getEmployeeId() {
		return employeeId;
	}
	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}
	public String getPfizerEmployee() {
		return pfizerEmployee;
	}
	public void setPfizerEmployee(String pfizerEmployee) {
		this.pfizerEmployee = pfizerEmployee;
	}
	public String getRequestStatus() {
		return requestStatus;
	}
	public void setRequestStatus(String requestStatus) {
		this.requestStatus = requestStatus;
	}
	public Date getDate_submitted() {
		return date_submitted;
	}
	public void setDate_submitted(Date date_submitted) {
		this.date_submitted = date_submitted;
	}
	public Date getDate_action() {
		return date_action;
	}
	public void setDate_action(Date date_action) {
		this.date_action = date_action;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	public String getApprovers_comments() {
		return Approvers_comments;
	}
	public void setApprovers_comments(String approvers_comments) {
		Approvers_comments = approvers_comments;
	}
	
	
}
