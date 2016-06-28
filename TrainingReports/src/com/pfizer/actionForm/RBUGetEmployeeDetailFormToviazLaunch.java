package com.pfizer.actionForm;

import java.util.List;
import java.util.Vector;

import com.opensymphony.xwork2.ActionSupport;
import com.pfizer.PWRA.EmployeeDetailFacade.EmployeeInfo;

public class RBUGetEmployeeDetailFormToviazLaunch extends ActionSupport{
    private EmployeeInfo employeeInfo;
    private Vector toviazLaunchStatusInfo;
    private String disableAll;
    private String repAttendedToviazLaunch;
    private String postLaunchConference;
    private String registered;
    private String attendance;
    private String managerCertification;
    private List examStatus;
    private String complianceStatus;
    private String overallStatus;
  
  
    public List getExamStatus() {
        return examStatus;
    }
    public void setExamStatus(List examStatus) {
        this.examStatus = examStatus;
    }
  
    public String getDisableAll() {
        return disableAll;
    }
    public void setDisableAll(String disableAll) {
        this.disableAll = disableAll;
    }
    
    public String getOverallStatus() {
        return overallStatus;
    }
    public void setOverallStatus(String overallStatus) {
        this.overallStatus = overallStatus;
    }
    
    public String getComplianceStatus() {
        return complianceStatus;
    }
    public void setComplianceStatus(String complianceStatus) {
        this.complianceStatus = complianceStatus;
    }
    
    public String getAttendance() {
        return attendance;
    }
    public void setAttendance(String attendance) {
        this.attendance = attendance;
    }
    public String getManagerCertification() {
        return managerCertification;
    }
    public void setManagerCertification(String managerCertification) {
        this.managerCertification = managerCertification;
    }
    
    
    public String getRepAttendedToviazLaunch() {
        return repAttendedToviazLaunch;
    }
    public void setRepAttendedToviazLaunch(String repAttendedToviazLaunch) {
        this.repAttendedToviazLaunch = repAttendedToviazLaunch;
    }
    public String getPostLaunchConference() {
        return postLaunchConference;
    }
    public void setPostLaunchConference(String postLaunchConference) {
        this.postLaunchConference = postLaunchConference;
    }
    public String getRegistered() {
        return registered;
    }
    public void setRegistered(String registered) {
        this.registered = registered;
    }
    
    public Vector getToviazLaunchStatusInfo() {
        return toviazLaunchStatusInfo;
    }
    public void setToviazLaunchStatusInfo(Vector toviazLaunchStatusInfo) {
        this.toviazLaunchStatusInfo = toviazLaunchStatusInfo;
    }                    
    
    public EmployeeInfo getEmployeeInfo(){
        return this.employeeInfo;    
    }
    public void setEmployeeInfo(EmployeeInfo employeeInfo){
        this.employeeInfo = employeeInfo;
    }         
   
    /* End of addition */
    
}

