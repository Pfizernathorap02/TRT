package com.pfizer.actionForm;

import java.util.Vector;

import com.opensymphony.xwork2.ActionSupport;

public class RBUTrainingScheduleListForm extends ActionSupport{
    String emplid;
    String productName;
    String oldCourseID;
    String productCD;
    Vector trainingScheduleList;
    public Vector getTrainingScheduleList() {
        return trainingScheduleList;
    }

    public void setTrainingScheduleList(Vector trainingScheduleList) {
        this.trainingScheduleList = trainingScheduleList;
    }
    public String getProductName() {
        return productName;
    }
    public void setProductName(String productName) {
        this.productName = productName;
    }        
    public String getProductCD() {
        return productCD;
    }
    public void setProductCD(String productCD) {
        this.productCD = productCD;
    }   
    public String getEmplid() {
        return emplid;
    }
    public void setEmplid(String emplid) {
        this.emplid = emplid;
    }   
    public String getOldCourseID() {
        return oldCourseID;
    }
    public void setOldCourseID(String oldCourseID) {
        this.oldCourseID = oldCourseID;
    }     
}
