package com.pfizer.db; 

import java.util.Date;

public class ClassRosterBean 
{ 
    private String classroom;
    private String productCode;
    private String productDesc;
    private Integer courseId;
    private String courseDesc;
    private Date startDate;
    private Date endDate;
    private Date trainingDate;
    
    public String getClassroom() {
        return classroom;
    }
    
    public void setClassroom(String classroom) {
        this.classroom = classroom;
    }
    
    public String getProductCode() {
        return productCode;
    }
    
    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }
    
    public String getProductDesc() {
        return productDesc;
    }
    
    public void setProductDesc(String productDesc) {
        this.productDesc = productDesc;
    }
    
    public Integer getCourseId() {
        return courseId;
    }
    
    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }
    
    public String getCourseDesc() {
        return courseDesc;
    }
    
    public void setCourseDesc(String courseDesc) {
        this.courseDesc = courseDesc;
    }
    
    public Date getStartDate() {
        return startDate;
    }
    
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }
    
    public Date getEndDate() {
        return endDate;
    }
    
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
    
    public Date getTrainingDate() {
        return trainingDate;
    }
    
    public void setTrainingDate(Date trainingDate) {
        this.trainingDate = trainingDate;
    }
} 

