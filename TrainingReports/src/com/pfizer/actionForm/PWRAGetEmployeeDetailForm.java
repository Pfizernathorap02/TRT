package com.pfizer.actionForm;

import java.util.Vector;

import com.opensymphony.xwork2.ActionSupport;
import com.pfizer.PWRA.EmployeeDetailFacade.EmployeeInfo;
import com.pfizer.PWRA.EmployeeDetailFacade.ProductAssignmentInfo;

public class PWRAGetEmployeeDetailForm extends ActionSupport{
    private Vector pdfHomeStudyStatus;
    private Vector trainingMaterialHistoryInfo;
    private Vector plcStatusInfo;
    private EmployeeInfo employeeInfo;
    private ProductAssignmentInfo productAssignmentInfo;
    private String overallPLCStatus;
    private String overallHomeStudyStatus;
    private Vector trainingSchedule;
    private Vector cancelTraining;
    private String overallStatus;
    private Vector gnsmStatusInfo;
    private String mcAttendacne;
    private String attendacne;  
    private String plAttendacne;        
    /* Variable added for Vista Rx Spiriva enhancment */
    private Vector vrsStatusInfo;


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
    
    public Vector getGnsmStatusInfo() {
        return gnsmStatusInfo;
    }
    public void setGnsmStatusInfo(Vector gnsmStatusInfo) {
        this.gnsmStatusInfo = gnsmStatusInfo;
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
    public ProductAssignmentInfo getProductAssignmentInfo(){
        return this.productAssignmentInfo;    
    }
    public void setProductAssignmentInfo(ProductAssignmentInfo productAssignmentInfo){
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
