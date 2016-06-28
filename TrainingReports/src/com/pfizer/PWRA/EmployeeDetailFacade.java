package com.pfizer.PWRA;


//import com.bea.control.Control;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

public interface EmployeeDetailFacade {

	
	    static final long serialVersionUID = 1L;
	    
	   
	    public static class EmployeeInfo{
	    	
	        String emplID ;
	        Date promotionDate;
	        Date hireDate;
	        String gender;
	        String email;
	        String reportToEmplID;
	        String Status;
	        String areaCD;
	        String areaDesc;
	        String regionCD;
	        String regionDesc;
	        String districtID;
	        String districtDesc;
	        String territoryID;
	        String territoryRole;
	        String teamCD;
	        String clusterCD;
	        String lastName;
	        String middleName;
	        String preferredName;
	        String imageURL;
	        String reportToEmail;
	        String reportToLastName;
	        String reportToPreferredName;
	        String reportToFirstName;
	        String future_manager;
	    	
	        //added for RBU
	        String bu;
	        List currentProds = new ArrayList();
	        List futureProds = new ArrayList();;
	        List credits = new ArrayList();
	        // Added for Bug 4918
	        String salesPositionId;
	        String salesPositionDesc;
	        String futureRole;
	        String futureBU;
	        String futureRBU;
	        String futureReportsToEmplID;
	        String futureReportToEmail;  
	        String futureReportToLastName;  
	        String futureReportToPreferredName;
	        String futureReportToFirstName;
	        
	        public String getFutureReportToEmail() {
	            return futureReportToEmail;
	        }
	        public void setFutureReportToEmail(String futureReportToEmail) {
	            this.futureReportToEmail = futureReportToEmail;
	        }
	        public String getFutureReportToLastName() {
	            return futureReportToLastName;
	        }
	        public void setFutureReportToLastName(String futureReportToLastName) {
	            this.futureReportToLastName = futureReportToLastName;
	        }
	        public String getFutureReportToPreferredName() {
	            return futureReportToPreferredName;
	        }
	        public void setFutureReportToPreferredName(String futureReportToPreferredName) {
	            this.futureReportToPreferredName = futureReportToPreferredName;
	        }
	        public String getFutureReportToFirstName() {
	            return futureReportToFirstName;
	        }
	        public void setFutureReportToFirstName(String futureReportToFirstName) {
	            this.futureReportToFirstName = futureReportToFirstName;
	        }
	            
	            
	         public String getFutureReportsToEmplID() {
	            return futureReportsToEmplID;
	        }
	        public void setFutureReportsToEmplID(String futureReportsToEmplID) {
	            this.futureReportsToEmplID = futureReportsToEmplID;
	        }
	        
	        public String getSalesPositionId() {
	            return salesPositionId;
	        }
	        public void setSalesPositionId(String salesPositionId) {
	            this.salesPositionId = salesPositionId;
	        }
	         public String getSalesPositionDesc() {
	            return salesPositionDesc;
	        }
	        public void setSalesPositionDesc(String salesPositionDesc) {
	            this.salesPositionDesc = salesPositionDesc;
	        }
	        
	        public String getFutureRole() {
	            return futureRole;
	        }
	        public void setFutureRole(String futureRole) {
	            this.futureRole = futureRole;
	        }
	        public String getFutureBU() {
	            return futureBU;
	        }
	        public void setFutureBU(String futureBU) {
	            this.futureBU = futureBU;
	        }
	        public String getFutureRBU() {
	            return futureRBU;
	        }
	        public void setFutureRBU(String futureRBU) {
	            this.futureRBU = futureRBU;
	        }
	        
	        public List getCurrentProds() {
	            return currentProds;
	        }
	        public void setCurrentProds(List c) {
	            this.currentProds = c;
	        }
	        
	        public List getFurrentProds() {
	            return currentProds;
	        }
	        public void setFurrentProds(List c) {
	            this.futureProds = c;
	        }
	        
	        public List getCredits() {
	            return credits;
	        }
	        public void setCredits(List c) {
	            this.credits = c;
	        }
	        public String getBu() {
	            return bu;
	        }
	        public void setBu(String bu) {
	            this.bu = bu;
	        }
	        public String getFuture_manager() {
	            return future_manager;
	        }
	        public void setFuture_manager(String future_manager) {
	            this.future_manager = future_manager;
	        }
	        public String getEmplID() {
	            return emplID;
	        }
	        public void setEmplID(String emplID) {
	            this.emplID = emplID;
	        }
	        public Date getPromotionDate() {
	            return promotionDate;
	        }
	        public void setPromotionDate(Date promotionDate) {
	            this.promotionDate = promotionDate;
	        }
	        public Date getHireDate() {
	            return hireDate;
	        }
	        public void setHireDate(Date hireDate) {
	            this.hireDate = hireDate;
	        }
	        public String getGender() {
	            return gender;
	        }
	        public void setGender(String gender) {
	            this.gender = gender;
	        }
	        public String getEmail() {
	            return email;
	        }
	        public void setEmail(String email) {
	            this.email = email;
	        }
	        public String getReportToEmplID() {
	            return reportToEmplID;
	        }
	        public void setReportToEmplID(String reportToEmplID) {
	            this.reportToEmplID = reportToEmplID;
	        }
	        public String getStatus() {
	            return Status;
	        }
	        public void setStatus(String status) {
	            Status = status;
	        }
	        public String getAreaCD() {
	            return areaCD;
	        }
	        public void setAreaCD(String areaCD) {
	            this.areaCD = areaCD;
	        }
	        public String getAreaDesc() {
	            return areaDesc;
	        }
	        public void setAreaDesc(String areaDesc) {
	            this.areaDesc = areaDesc;
	        }
	        public String getRegionCD() {
	            return regionCD;
	        }
	        public void setRegionCD(String regionCD) {
	            this.regionCD = regionCD;
	        }
	        public String getRegionDesc() {
	            return regionDesc;
	        }
	        public void setRegionDesc(String regionDesc) {
	            this.regionDesc = regionDesc;
	        }
	        public String getDistrictID() {
	            return districtID;
	        }
	        public void setDistrictID(String districtID) {
	            this.districtID = districtID;
	        }
	        public String getDistrictDesc() {
	            return districtDesc;
	        }
	        public void setDistrictDesc(String districtDesc) {
	            this.districtDesc = districtDesc;
	        }
	        public String getTerritoryID() {
	            return territoryID;
	        }
	        public void setTerritoryID(String territoryID) {
	            this.territoryID = territoryID;
	        }
	        public String getTerritoryRole() {
	            return territoryRole;
	        }
	        public void setTerritoryRole(String territoryRole) {
	            this.territoryRole = territoryRole;
	        }
	        public String getTeamCD() {
	            return teamCD;
	        }
	        public void setTeamCD(String teamCD) {
	            this.teamCD = teamCD;
	        }
	        public String getClusterCD() {
	            return clusterCD;
	        }
	        public void setClusterCD(String clusterCD) {
	            this.clusterCD = clusterCD;
	        }
	        public String getLastName() {
	            return lastName;
	        }
	        public void setLastName(String lastName) {
	            this.lastName = lastName;
	        }
	        public String getMiddleName() {
	            return middleName;
	        }
	        public void setMiddleName(String middleName) {
	            this.middleName = middleName;
	        }
	        public String getPreferredName() {
	            return preferredName;
	        }
	        public void setPreferredName(String preferredName) {
	            this.preferredName = preferredName;
	        }
	        public String getImageURL() {
	            return imageURL;
	        }
	        public void setImageURL(String imageURL) {
	            this.imageURL = imageURL;
	        }
	        public String getReportToEmail() {
	            return reportToEmail;
	        }
	        public void setReportToEmail(String reportToEmail) {
	            this.reportToEmail = reportToEmail;
	        }
	        public String getReportToLastName() {
	            return reportToLastName;
	        }
	        public void setReportToLastName(String reportToLastName) {
	            this.reportToLastName = reportToLastName;
	        }
	        public String getReportToPreferredName() {
	            return reportToPreferredName;
	        }
	        public void setReportToPreferredName(String reportToPreferredName) {
	            this.reportToPreferredName = reportToPreferredName;
	        }
	        public String getReportToFirstName() {
	            return reportToFirstName;
	        }
	        public void setReportToFirstName(String reportToFirstName) {
	            this.reportToFirstName = reportToFirstName;
	        }
	    }
	    public static class PDFProduct{
	        String team;
	        String product;
	        public String getTeam() {
	            return team;
	        }
	        public void setTeam(String team) {
	            this.team = team;
	        }
	        public String getProduct() {
	            return product;
	        }
	        public void setProduct(String product) {
	            this.product = product;
	        }        
	    }
	    
	    public static class RBUProduct{
	        String preteam;
	        String product;
	         public String getPreTeam() {
	            return preteam;
	        }
	        public void setPreTeam(String preteam) {
	            this.preteam = preteam;
	        }
	        public String getProduct() {
	            return product;
	        }
	        public void setProduct(String product) {
	            this.product = product;
	        }        
	    }
	    
	    public static class RBUPostProduct{
	        String product;
	        String bu;
	        public String getProduct() {
	            return product;
	        }
	        public void setProduct(String product) {
	            this.product = product;
	        }        
	        public String getBu() {
	            return bu;
	        }
	        public void setBU(String bu) {
	            this.bu = bu;
	        }
	    }
	    
	    public static class ProductAssignmentInfo{
	        private Vector prePDFProducts;
	        private Vector postPDFProducts;        
	        private String prePDFProductTeam;
	        private String postPDFProductTeam;
	        
	        public Vector getPrePDFProducts() {
	            return prePDFProducts;
	        }
	        public void setPrePDFProducts(Vector prePDFProducts) {
	            this.prePDFProducts = prePDFProducts;
	        }
	        public Vector getPostPDFProducts() {
	            return postPDFProducts;
	        }
	        public void setPostPDFProducts(Vector postPDFProducts) {
	            this.postPDFProducts = postPDFProducts;
	        }
	        public String getPrePDFProductTeam() {
	            return prePDFProductTeam;
	        }
	        public void setPrePDFProductTeam(String prePDFProductTeam) {
	            this.prePDFProductTeam = prePDFProductTeam;
	        }
	        public String getPostPDFProductTeam() {
	            return postPDFProductTeam;
	        }
	        public void setPostPDFProductTeam(String postPDFProductTeam) {
	            this.postPDFProductTeam = postPDFProductTeam;
	        }        
	    }
	    public static class ProductAssignmentInfoRBU{
	        private Vector currentProducts;
	        private Vector futureProducts;        
	        private String preTeam;
	        private String futureBU;
	        
	        public void setCurrentProducts(Vector c){
	            this.currentProducts = c;
	        }
	        
	        public Vector getCurrentProducts(){
	            return currentProducts;
	        }
	        
	        public void setFutureProducts(Vector c){
	            this.futureProducts = c;
	        }
	        
	        public Vector getFutureProducts(){
	            return futureProducts;
	        }
	        
	        public void setPreTeams(String c){
	            this.preTeam = c;
	        }
	        
	        public String getPreTeam(){
	            return preTeam;
	        }
	        
	        public void setFutureBU(String c){
	            this.futureBU = c;
	        }
	        
	        public String getFutureBU(){
	            return futureBU;
	        }
	      
	    }
	    public static class TrainingMaterialHistory{
	        private String invID;
	        private String status;
	        private String materialDesc;
	        private Date orderDate;
	        private String trackingNumber;        
	        private String trmOrderID; 
	        public String getInvID() {
	            return invID;
	        }
	        public void setInvID(String invID) {
	            this.invID = invID;
	        }
	        public String getStatus() {
	            return status;
	        }
	        public void setStatus(String status) {
	            this.status = status;
	        }
	        public String getMaterialDesc() {
	            return materialDesc;
	        }
	        public void setMaterialDesc(String materialDesc) {
	            this.materialDesc = materialDesc;
	        }
	        public Date getOrderDate() {
	            return orderDate;
	        }
	        public void setOrderDate(Date orderDate) {
	            this.orderDate = orderDate;
	        }
	        public String getTrackingNumber() {
	            return trackingNumber;
	        }
	        public void setTrackingNumber(String trackingNumber) {
	            this.trackingNumber = trackingNumber;
	        }
	        public String getTrmOrderID() {
	            return trmOrderID;
	        }
	        public void setTrmOrderID(String trmOrderID) {
	            this.trmOrderID = trmOrderID;
	        }                  
	    }
	    public static class RBUExam{
	        public static final String EXAM_STATUS_NA = "N/A";
	        public static final String EXAM_STATUS_COMPLETE = "C";
	        public static final String EXAM_STATUS__NOT_COMPLETE = "NC";
	        public static final String EXAM_TYPE_PED = "PED";
	        public static final String EXAM_TYPE_SCE = "SCE";
	        String examtype;
	        String score;
	        String status;
	        
	        public String getExamtype (){
	            return examtype;
	        }
	        
	        public void setExamtype(String examtype){
	            this.examtype = examtype;
	        }
	        public String getScore (){
	            return score;
	        }
	        public void setScore(String s){
	            this.score = s;
	        }
	        public String getStatus (){
	            return status;
	        }
	        public void setStatus(String status){
	            this.status = status;
	        }
	    }
	    public static class RBUTrainingStatus{
	        public static final String TRAINING_STATUS_CREDIT = "Credit";
	        public static final String TRAINING_STATUS_COMPLETE = "C";
	        public static final String TRAINING_STATUS_NOT_COMPLETE = "NC";
	        
	        String productDesc;
	        List exams;
	        Date startdate;
	        String note;
	        String status;
	        
	        public String getProductDesc() {
	            return productDesc;
	        }
	        public void setProductDesc(String productDesc) {
	            this.productDesc = productDesc;
	        }
	        public void setExam (List e){
	            exams = new ArrayList();
	            exams.addAll(e);            
	        }
	        public List getExam (){
	            return new ArrayList(exams);
	        }
	        public Date getStartDate(){
	            return startdate;
	        }
	        public void setStartDate(Date sdate){
	            startdate = sdate;
	        }
	        public String getNote() {
	            return note;
	        }
	        public void setNote(String note) {
	            this.note = note;
	        }
	        public String getStatus (){
	            return status;
	        }
	        public void setStatus(String status){
	            this.status = status;
	        }
	        
	    }
	    public static class PDFHomeStudyStatus{
	        String productDesc;
	        String pedagogueExam;
	        String score;
	        String status;
	        Date completionDate;
	        public String getProductDesc() {
	            return productDesc;
	        }
	        public void setProductDesc(String productDesc) {
	            this.productDesc = productDesc;
	        }
	        public String getPedagogueExam() {
	            return pedagogueExam;
	        }
	        public void setPedagogueExam(String pedagogueExam) {
	            this.pedagogueExam = pedagogueExam;
	        }
	        public String getScore() {
	            return score;
	        }
	        public void setScore(String score) {
	            this.score = score;
	        }
	        public String getStatus() {
	            return status;
	        }
	        public void setStatus(String status) {
	            this.status = status;
	        }
	        public Date getCompletionDate() {
	            return completionDate;
	        }
	        public void setCompletionDate(Date completionDate) {
	            this.completionDate = completionDate;
	        }
	    }
	    public static class PLCStatus{
	        String product;
	        String status;
	        String productCode;
	        Vector plcExamStatusList = new Vector();
	        Date classdate;
	        String class_id;
	        
	        public String getProduct() {
	            return product;
	        }
	        public void setProduct(String product) {
	            this.product = product;
	        }
	        public String getClass_id() {
	            return class_id;
	        }
	        public void setClass_id(String class_id) {
	            this.class_id = class_id;
	        }
	                
	        public Date getClassDate() {
	            return classdate;
	        }
	        public void setClassDate(Date classdate) {
	            this.classdate = classdate;
	        }
	        
	        public String getStatus() {
	            return status;
	        }
	        public void setStatus(String status) {
	            this.status = status;
	        }
	    	public String getProductCode() {
	        	return productCode;
	        }
	        public void setProductCode(String productCode) {
	            this.productCode = productCode;
	        }
	        public Vector getPlcExamStatusList() {
	            return plcExamStatusList;
	        }    
	        public void setPlcExamStatusList(Vector plcExamStatusList) {
	            this.plcExamStatusList = plcExamStatusList;
	        }                
	    }
	    public static class PLCExamStatus{
	        String examType;
	        String score;
	        String examTypeName;
	        String examName;
	        String examStatus;
	        Date completionDate;
	        String productCode;
	         public String getExamTypeName() {
	            if(getExamType().equalsIgnoreCase("PED")){
	                return "Pedagogue";    
	            }else if(getExamType().equalsIgnoreCase("SCE")){
	                return "Sales Call Evaluation";
	            }            
	            return examTypeName;
	        }
	        public void setExamTypeName(String examTypeName) {
	            this.examTypeName = examTypeName;
	        }
	        public String getExamType() {
	            return examType;
	        }
	        public void setExamType(String examType) {
	            this.examType = examType;
	        }
	        public String getExamName() {
	            return examName;
	        }
	        public void setExamName(String examName) {
	            this.examName = examName;
	        }
	        public String getScore() {
	            return score;
	        }
	        public void setScore(String score) {
	            this.score = score;
	        }
	        public String getExamStatus() {
	            return examStatus;
	        }
	        public void setExamStatus(String examStatus) {
	            this.examStatus = examStatus;
	        }
	        public Date getCompletionDate() {
	            return completionDate;
	        }
	        public void setCompletionDate(Date completionDate) {
	            this.completionDate = completionDate;
	        }
	        public String getProductCode() {
	            return productCode;
	        }
	        public void setProductCode(String productCode) {
	            this.productCode = productCode;
	        }
	    }
	    public static class TrainingSchedule{
	        String courseID;
	        String courseDescription;
	        Date courseSchedule;
	        String reason;
	        public String getCourseID() {
	            return courseID;
	        }
	        public void setCourseID(String courseID) {
	            this.courseID = courseID;
	        }
	        public String getCourseDescription() {
	            return courseDescription;
	        }
	        public void setCourseDescription(String courseDescription) {
	            this.courseDescription = courseDescription;
	        }
	        public Date getCourseSchedule() {
	            return courseSchedule;
	        }
	        public void setCourseSchedule(Date courseSchedule) {
	            this.courseSchedule = courseSchedule;
	        }

	        public String getCancelReason() {
	            return reason;
	        }
	        public void setCancelReason(String param) {
	            this.reason = param;
	            
	        }        
	    }
	    public static class TrainingScheduleList{
	        String courseID;
	        Date startDate;
	        String courseName;
	        
	        public String getCourseID() {
	            return courseID;
	        }
	        public void setCourseID(String courseID) {
	            this.courseID = courseID;
	        }
	        public Date getStartDate() {
	            return startDate;
	        }
	        public void setStartDate(Date startDate) {
	            this.startDate = startDate;
	        }        
	         public String getCourseName() {
	            return courseName;
	        }
	        public void setCourseName(String courseName) {
	            this.courseName = courseName;
	        }
	    }    
	    

	    EmployeeInfo getEmployeeInfo(String emplid);
	    Vector getTrainingMaterialHistory(java.lang.String emplid);    
	    void reOrderTrainingMaterialHistory(java.util.Vector invIDs, java.lang.String emplid);
	    java.util.Vector getTrainingScheduleInfo(java.lang.String emplid);

	    java.lang.String getOverallHomeStudyStatus(java.lang.String emplid);

	    java.lang.String getOverallPLCStatus(java.lang.String emplid);

	    java.util.Vector getPLCStatusInfo(java.lang.String emplid);

	    java.lang.String getOverallSPFStatus(java.lang.String emplid);

	    java.util.Vector getSPFStatusInfo(java.lang.String emplid);

	    com.pfizer.PWRA.EmployeeDetailFacade.ProductAssignmentInfo getProductAssignmentSPF(java.lang.String emplid);

	    java.lang.String getTeam(java.lang.String emplId);

	    void updateCourseList(java.lang.String userId, java.lang.String emplid, java.lang.String oldCourseID, java.lang.String newCourseID);

	    java.util.Vector getTrainingScheduleListLYRC(java.lang.String courseID, java.lang.String team);

	    void cancelTraining(java.lang.String emplid, java.lang.String courseid, java.lang.String sRole, java.lang.String gender, java.lang.String role, java.lang.String status, java.lang.String reason);

	    java.util.Vector getCancelTraining(java.lang.String emplid);

	    java.util.Vector getPDFTrainingScheduleList(java.lang.String courseID);

	    java.util.Vector getSPFTrainingScheduleList(java.lang.String courseID);

	    java.util.Vector getPDFTrainingScheduleListPHR(java.lang.String courseID);

	    java.util.Vector getSPFTrainingScheduleListPHR(java.lang.String courseID);

	    java.lang.String getOverallGNSMStatus(java.lang.String emplid);

	    java.util.Vector getGNSMStatusInfo(java.lang.String emplid);

	    java.lang.String getAttendance(java.lang.String emplid, java.lang.String mode);

	    java.lang.String getPLAttendanceStatus(java.lang.String emplid);

	    java.lang.String getOverallMSEPIStatus(java.lang.String emplid); 

	    java.lang.String getMSEPIPLAttendanceStatus(java.lang.String emplid);

	    void updateMSEPIAttendance(java.lang.String emplid, java.lang.String result, java.lang.String mode, java.lang.String userID);
	    
	    /* Added the following methods for Vista Rx Spiriva enhancement
	     * Author: Meenakshi
	     * Date: 14-Sep-2008
	    */
	    java.lang.String getOverallVRSStatus(java.lang.String emplid);

	    java.util.Vector getVRSStatusInfo(java.lang.String emplid); 
	    /* End of addition */

	    void updateGNSMAttendance(java.lang.String emplid, java.lang.String result, java.lang.String mode, java.lang.String userID);

	    

	    void updateVRSAttendance(java.lang.String emplid, java.lang.String result, java.lang.String mode, java.lang.String userID);

	    java.lang.String getMSEPIAttendance(java.lang.String emplid, java.lang.String mode);

	    java.lang.String getVRSAttendance(java.lang.String emplid, java.lang.String mode);

	    com.pfizer.PWRA.EmployeeDetailFacade.ProductAssignmentInfo getProductAssignment(java.lang.String emplid);

	    com.pfizer.PWRA.EmployeeDetailFacade.EmployeeInfo getEmployeeInfoGNSM(java.lang.String emplid);

	    com.pfizer.PWRA.EmployeeDetailFacade.EmployeeInfo getEmployeeInfoRBU(java.lang.String emplid);

	    com.pfizer.PWRA.EmployeeDetailFacade.ProductAssignmentInfoRBU getProductAssignmentRBU(java.lang.String emplid);

	    /**
	     * added by shannon for RBU UPDATE
	     */
	    void updateRBUClass(java.lang.String userId, java.lang.String emplid, java.lang.String oldCourseID, java.lang.String newCourseID);

	    void cancelRBUTraining(java.lang.String userId, java.lang.String emplid, java.lang.String courseid, java.lang.String reason);

	    java.util.List getRBUTrainingStatus(java.lang.String emplid, com.pfizer.PWRA.EmployeeDetailFacade.EmployeeInfo employeeinfo);

	    java.util.Vector getPLCStatusInfoRBU(java.lang.String emplid);

	    java.util.Vector getRBUTrainingScheduleInfo(java.lang.String emplid);

	    java.util.Vector getRBUCancelTraining(java.lang.String emplid);

	    java.util.Vector getRBUTrainingScheduleList(java.lang.String courseID);

	    java.util.Vector getRBUTrainingScheduleListByProduct(java.lang.String productcd);

	    java.util.Vector getPdfHomeStudyStatusInfo(java.lang.String emplid);
	} 

	
	
	
	
	
	
	
	
	
	
	
	
	

