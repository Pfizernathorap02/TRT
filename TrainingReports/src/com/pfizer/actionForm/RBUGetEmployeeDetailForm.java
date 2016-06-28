package com.pfizer.actionForm;

import java.util.List;
import java.util.Vector;

import com.opensymphony.xwork2.ActionSupport;
import com.pfizer.PWRA.EmployeeDetailFacade.EmployeeInfo;
import com.pfizer.PWRA.EmployeeDetailFacade.ProductAssignmentInfoRBU;

public class RBUGetEmployeeDetailForm extends ActionSupport{
    private Vector pdfHomeStudyStatus;
    private Vector trainingMaterialHistoryInfo;
    private Vector plcStatusInfo;
    private EmployeeInfo employeeInfo;
    private ProductAssignmentInfoRBU productAssignmentInfo;
    private String overallPLCStatus;
    private String overallHomeStudyStatus;
    private Vector trainingSchedule;
    private Vector cancelTraining;
    private String overallStatus;
    private Vector toviazLaunchStatusInfo;
    private String mcAttendacne;
    private String attendacne;  
    private String plAttendacne;        
    /* Variable added for Vista Rx Spiriva enhancment */
    private Vector vrsStatusInfo;
    /*added for RBU*/
    private List rbuStatuses;
    // Added for RBU Guest trainers
    private List rbuGuestTrainers;
     public List getRbuGuestTrainers() {
        return rbuGuestTrainers;
    }

    public void setRbuGuestTrainers(List rbuGuestTrainers) {
        this.rbuGuestTrainers = rbuGuestTrainers;
    }

    public List getRbuStatuses() {
        return rbuStatuses;
    }

    public void setRbuStatus(List rbuStatuses) {
        this.rbuStatuses = rbuStatuses;
    }

    public String getPlAttendacne() {
        return plAttendacne;
    }

    public void setPlAttendacne(String plAttendacne) {
        this.plAttendacne = plAttendacne;
    }

    public String getMcAttendacne() {
        return mcAttendacne;
    }
    public void setMcAttendacne(String mcAttendacne) {
        this.mcAttendacne = mcAttendacne;
    }
    public String getAttendacne() {
        return attendacne;
    }
    public void setAttendacne(String attendacne) {
        this.attendacne = attendacne;
    }
    
    public Vector getToviazLaunchStatusInfo() {
        return toviazLaunchStatusInfo;
    }
    public void setToviazLaunchStatusInfo(Vector toviazLaunchStatusInfo) {
        this.toviazLaunchStatusInfo = toviazLaunchStatusInfo;
    }                    
    public String getOverallStatus() {
        return overallStatus;
    }
    public void setOverallStatus(String overallStatus) {
        this.overallStatus = overallStatus;
    }                    
    public Vector getCancelTraining(){
        return this.cancelTraining;    
    }
    public void setCancelTraining(Vector cancelTraining){
        this.cancelTraining = cancelTraining;
    }
    
    public EmployeeInfo getEmployeeInfo(){
        return this.employeeInfo;    
    }
    public void setEmployeeInfo(EmployeeInfo employeeInfo){
        this.employeeInfo = employeeInfo;
    }         
    public ProductAssignmentInfoRBU getProductAssignmentInfo(){
        return this.productAssignmentInfo;    
    }
    public void setProductAssignmentInfo(ProductAssignmentInfoRBU productAssignmentInfo){
        this.productAssignmentInfo = productAssignmentInfo;            
    }
    
    public Vector getTrainingMaterialHistoryInfo() {
        return trainingMaterialHistoryInfo;
    }
	public void setTrainingMaterialHistoryInfo(Vector trainingMaterialHistoryInfo) {
    	this.trainingMaterialHistoryInfo = trainingMaterialHistoryInfo;
    }
	public Vector getPdfHomeStudyStatus() {
    	return pdfHomeStudyStatus;
    }
    public void setPdfHomeStudyStatus(Vector pdfHomeStudyStatus) {
        this.pdfHomeStudyStatus = pdfHomeStudyStatus;
    }
    public String getOverallPLCStatus() {
        return overallPLCStatus;
    }
    public void setOverallPLCStatus(String overallPLCStatus) {
        this.overallPLCStatus = overallPLCStatus;
    }
    public String getOverallHomeStudyStatus() {
        return overallHomeStudyStatus;
    }
    public void setOverallHomeStudyStatus(String overallHomeStudyStatus) {
        this.overallHomeStudyStatus = overallHomeStudyStatus;
    }
    public Vector getPlcStatusInfo() {
        return plcStatusInfo;
    }
    public void setPlcStatusInfo(Vector plcStatusInfo) {
        this.plcStatusInfo = plcStatusInfo;
    }
    public Vector getTrainingSchedule() {
        return trainingSchedule;
    }
    public void setTrainingSchedule(Vector trainingSchedule) {
        this.trainingSchedule = trainingSchedule;
    }        
    /* Getter and setter methods added for Vista Rx Spiriva enhancement 
    */        
    public Vector getVrsStatusInfo() {
        return vrsStatusInfo;
    }
    public void setVrsStatusInfo(Vector vrsStatusInfo) {
        this.vrsStatusInfo = vrsStatusInfo;
    } 
    /* End of addition */
    
}
