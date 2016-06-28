package com.pfizer.webapp.sce; 

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
public class SCE 
{ 
    private Integer id;
    private Integer eventId;
    private String emplId;
    private String productCode;
    private String product;
    private String course;
    private String classroom;
    private String tableName;
    //private Date trainingDate;
    private Integer templateVersionId;
    private String precallRating;
    private String precallComments;
    private String postcallRating;
    private String postcallComments;
    private String comments;
    private String hlcCompliant;
    private String reviewed;
    private String overallRating;
    private String overallComments;
    private Date submittedDate;
    private Date uploadedDate;
    private String submittedByEmplId;
    private String lmsFlag;
    
    /* P2L Fields */
    private String status;
    private String preparedByEmplId;
    private Double overallScore;
    /* End P2L Fields */
    
    private String formTitle;
    private String hlcCritical;
    List sceDetailList = new ArrayList();        
    
    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    
    public Integer getEventId() {
        return eventId;
    }
    
    public void setEventId(Integer eventId) {
        this.eventId = eventId;
    }
    
    public String getEmplId() {
        return emplId;
    }
    
    public void setEmplId(String emplId) {
        this.emplId = emplId;
    }
    
    public String getProduct() {
        return product;
    }
    
    public void setProduct(String product) {
        this.product = product;
    }
    
    public String getCourse() {
        return course;
    }
    
    public void setCourse(String course) {
        this.course = course;
    }
    
    public String getProductCode() {
        return productCode;
    }
    
    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }
    
    public String getClassroom() {
        return classroom;
    }
    
    public void setClassroom(String classroom) {
        this.classroom = classroom;
    }
    
    public String getTableName() {
        return tableName;
    }
    
    public void setTableName(String tableName) {
        this.tableName = tableName;
    }
    
    /*public Date getTrainingDate() {
        return trainingDate;
    }
    
    public void setTrainingDate(Date trainingDate) {
        this.trainingDate = trainingDate;
    }*/
    
    public Integer getTemplateVersionId() {
        return templateVersionId;
    }
    
    public void setTemplateVersionId(Integer templateVersionId) {
        this.templateVersionId = templateVersionId;
    }
    
    public String getPrecallRating() {
        return precallRating;
    }
    
    public void setPrecallRating(String precallRating) {
        this.precallRating = precallRating;
    }
    
    public String getPrecallComments() {
        return precallComments;
    }
    
    public void setPrecallComments(String precallComments) {
        this.precallComments = precallComments;
    }
    
    public String getPostcallRating() {
        return postcallRating;
    }
    
    public void setPostcallRating(String postcallRating) {
        this.postcallRating = postcallRating;
    }

    public String getPostcallComments() {
        return postcallComments;
    }
    
    public void setPostcallComments(String postcallComments) {
        this.postcallComments = postcallComments;
    }
    
    public String getComments() {
        return comments;
    }
    
    public void setComments(String comments) {
        this.comments = comments;
    }
    
    public String getHlcCompliant() {
        return hlcCompliant;
    }
    
    public void setHlcCompliant(String hlcCompliant) {
        this.hlcCompliant = hlcCompliant;
    }
    
    public boolean isHlcCompliantBy() {
        return "Y".equals(this.hlcCompliant);
    }        
    
    public String getReviewed() {
        return reviewed;
    }
    
    public void setReviewed(String reviewed) {
        this.reviewed = reviewed;
    }
    
    public boolean isReviewedBy() {
        return "Y".equals(this.reviewed);
    }        
    
    public String getOverallRating() {
        return overallRating;
    }
    
    public void setOverallRating(String overallRating) {
        this.overallRating = overallRating;
    }
    
    public String getOverallComments() {
        return overallComments;
    }
    
    public void setOverallComments(String overallComments) {
        this.overallComments = overallComments;
    }
    
    public String getLmsFlag() {
        return lmsFlag;
    }
    
    public void setLmsFlag(String lmsFlag) {
        this.lmsFlag = lmsFlag;
    }
    
    public Date getSubmittedDate() {
        return submittedDate;
    }
    
    public void setSubmittedDate(Date submittedDate) {
        this.submittedDate = submittedDate;
    }
    
    public Date getUploadedDate() {
        return uploadedDate;
    }
    
    public void setUploadedDate(Date uploadedDate) {
        this.uploadedDate = uploadedDate;
    }
    
    public String getSubmittedByEmplId() {
        return submittedByEmplId;
    }
    
    public void setSubmittedByEmplId(String submittedByEmplId) {
        this.submittedByEmplId = submittedByEmplId;
    }
    
    public String getFormTitle() {
        return formTitle;
    }
    
    public void setFormTitle(String formTitle) {
        this.formTitle = formTitle;
    }
    
    public String getHlcCritical() {
        return hlcCritical;
    }
    
    public void setHlcCritical(String hlcCritical) {
        this.hlcCritical = hlcCritical;
    }    
    
    public List getSceDetailList() {
        return this.sceDetailList;
    }
    
    
    /* P2L Fields */
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    } 
    
    public String getPreparedByEmplId() {
        return preparedByEmplId;        
    }
    
    public void setPreparedByEmplId(String preparedByEmplId) {
        this.preparedByEmplId = preparedByEmplId;        
    }   
    
    public Double getOverallScore() {
        return this.overallScore;
    }
    
    public void setOverallScore(Double overallScore) {
        this.overallScore = overallScore;
    }
    /* END P2L Fields */
}  
